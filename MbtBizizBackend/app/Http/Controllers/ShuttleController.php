<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 24.05.2018
 * Time: 22:27
 */

namespace App\Http\Controllers;


use App\Constants;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class ShuttleController extends Controller
{
    public function shuttleOptionList()
    {
        if (in_array(Auth::user()->company_location_id, [Constants::COMPANY_LOCATION_GENEL_MUDURLUK, Constants::COMPANY_LOCATION_HOSDERE, Constants::COMPANY_LOCATION_AKSARAY])) {
            $userCompanyLocationId = Auth::user()->company_location_id;
        } else {
            $userCompanyLocationId = Constants::COMPANY_LOCATION_GENEL_MUDURLUK;
        }

        $companyLocationList = [
            ['id' => Constants::COMPANY_LOCATION_GENEL_MUDURLUK, 'name' => "Genel Müdürlük ve Pazarlama", 'isDefault' => false],
            ['id' => Constants::COMPANY_LOCATION_HOSDERE, 'name' => "Hoşdere", 'isDefault' => false],
            ['id' => Constants::COMPANY_LOCATION_AKSARAY, 'name' => "Aksaray", 'isDefault' => false]
        ];
        $companyLocationList[$userCompanyLocationId - 1]['isDefault'] = true;

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'typeList' => [
                    ['type' => 1, 'name' => Lang::get('lang.TXT_SHUTTLE_OPTION_EMPLOYEE')],
                    ['type' => 2, 'name' => Lang::get('lang.TXT_SHUTTLE_OPTION_RING')],
                    ['type' => 4, 'name' => Lang::get('lang.TXT_SHUTTLE_OPTION_INTERLOCATION')],
                    ['type' => 3, 'name' => Lang::get('lang.TXT_SHUTTLE_OPTION_OTHER')]
                ],
                'companyLocationList' => $companyLocationList
            ],
            'errorMessage' => null
        ]);
    }

    public function shuttleList(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'type' => 'required|integer|min:1|max:4',
            'companyLocationId' => 'required|integer|min:1|max:3'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Eksik/hatalı parametre(ler) var!"
            ]);
        }
        //endregion

        $type = intval($request->input('type'));
        $companyLocationId = intval($request->input('companyLocationId'));

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'type' => $type,
                'companyLocationId' => $companyLocationId,
                'toCompanyList' => $this->_getToCompanyListFromCache($type, $companyLocationId),
                'fromCompanyList' => $this->_getFromCompanyListFromCache($type, $companyLocationId)
            ],
            'errorMessage' => null
        ]);
    }

    //region To Company List
    private function _getToCompanyListFromCache($type, $companyLocationId)
    {
        $key = "shuttles_to_company_" . $type . "_" . $companyLocationId;
        try {
            $toCompanyList = Cache::rememberForever($key, function () use ($type, $companyLocationId) {
                return $this->_createToCompanyList($type, $companyLocationId);
            });
        } catch (\Exception $exception) {
            $toCompanyList = $this->_createToCompanyList($type, $companyLocationId);
        }

	return [];
	//return $toCompanyList;
    }

    private function _createToCompanyList($type, $companyLocationId)
    {
        if ($type == Constants::SHUTTLE_TYPE_EMPLOYEE && $companyLocationId == Constants::COMPANY_LOCATION_HOSDERE) {
            return $this->_createToCompanyListWithTab($type, $companyLocationId);
        } else {
            return $this->_createToCompanyListWithoutTab($type, $companyLocationId);
        }
    }

    private function _createToCompanyListWithoutTab($type, $companyLocationId)
    {
        $shuttleList = [];
        $toCompanyListResult = DB::select("select s.id, s.departure_location, s.license_plate, s.driver_name_surname, s.driver_telephone,
                                                        s.to_departure_time, s.to_arrival_time,
                                                        (select group_concat(tcss.id) from to_company_shuttle_stops tcss where tcss.shuttle_id = s.id order by tcss.order) to_company_stop_id_list,
                                                        (select group_concat(stops.name) from to_company_shuttle_stops tcss, stops stops where tcss.shuttle_id = s.id and tcss.stop_id = stops.id order by tcss.order) to_company_stop_name_list
                                                  from shuttles s
                                                  where s.company_location_id = ? and s.type = ? and s.to_departure_time is not null", [$companyLocationId, $type]);
        foreach ($toCompanyListResult as $item) {
            $stopList = [];
            $stopIdList = explode(",", $item->to_company_stop_id_list);
            $stopNameList = explode(",", $item->to_company_stop_name_list);
            for ($i = 0; $i < count($stopIdList); $i++) {
                $stopList[] = [
                    'id' => intval($stopIdList[$i]),
                    'name' => $stopNameList[$i]
                ];
            }

            $shuttleList[] = [
                'id' => $item->id,
                'name' => $item->departure_location,
                'departureTime' => Carbon::createFromFormat('H:i:s', $item->to_departure_time)->format('H:i'),
                'arrivalTime' => !empty($item->to_arrival_time) ? Carbon::createFromFormat('H:i:s', $item->to_arrival_time)->format('H:i') : null,
                'driverInfo' => $item->license_plate != null ? [
                    'licensePlate' => $item->license_plate,
                    'name' => $item->driver_name_surname,
                    'telephone' => $item->driver_telephone,
                ] : null,
                'stopList' => $stopList
            ];
        }

        $toCompanyList = [
            [
                'name' => null,
                'shuttleList' => $shuttleList
            ]
        ];

        return $toCompanyList;
    }

    private function _createToCompanyListWithTab($type, $companyLocationId)
    {
        $morningShuttleList = $this->_createToCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_MORNING);
        $noonShuttleList = $this->_createToCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_NOON);
        $eveningShuttleList = $this->_createToCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_EVENING);
        $nightShuttleList = $this->_createToCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_NIGHT);

        $toCompanyList = [
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_MORNING_NAME,
                'shuttleList' => $morningShuttleList
            ],
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_NOON_NAME,
                'shuttleList' => $noonShuttleList
            ],
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_EVENING_NAME,
                'shuttleList' => $eveningShuttleList
            ],
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_NIGHT_NAME,
                'shuttleList' => $nightShuttleList
            ]
        ];

        return $toCompanyList;
    }

    private function _createToCompanyListByTimeType($type, $companyLocationId, $timeType)
    {
        $shuttleList = [];
        $toCompanyListResult = DB::select("select s.id, s.departure_location, s.license_plate, s.driver_name_surname, s.driver_telephone,
                                                          s.to_departure_time, s.to_arrival_time,
                                                          (select group_concat(tcss.id) from to_company_shuttle_stops tcss where tcss.shuttle_id = s.id order by tcss.order) to_company_stop_id_list,
                                                          (select group_concat(stops.name) from to_company_shuttle_stops tcss, stops stops where tcss.shuttle_id = s.id and tcss.stop_id = stops.id order by tcss.order) to_company_stop_name_list
                                                  from shuttles s
                                                  where s.company_location_id = ? and s.type = ? and s.to_time_type = ? and s.to_departure_time is not null", [$companyLocationId, $type, $timeType]);
        foreach ($toCompanyListResult as $item) {
            $stopList = [];
            $stopIdList = explode(",", $item->to_company_stop_id_list);
            $stopNameList = explode(",", $item->to_company_stop_name_list);
            for ($i = 0; $i < count($stopIdList); $i++) {
                $stopList[] = [
                    'id' => intval($stopIdList[$i]),
                    'name' => $stopNameList[$i]
                ];
            }

            $shuttleList[] = [
                'id' => $item->id,
                'name' => $item->departure_location,
                'departureTime' => Carbon::createFromFormat('H:i:s', $item->to_departure_time)->format('H:i'),
                'arrivalTime' => !empty($item->to_arrival_time) ? Carbon::createFromFormat('H:i:s', $item->to_arrival_time)->format('H:i') : null,
                'driverInfo' => $item->license_plate != null ? [
                    'licensePlate' => $item->license_plate,
                    'name' => $item->driver_name_surname,
                    'telephone' => $item->driver_telephone,
                ] : null,
                'stopList' => $stopList
            ];
        }

        return $shuttleList;
    }
    //endregion

    //region From Company List
    private function _getFromCompanyListFromCache($type, $companyLocationId)
    {
        $key = "shuttles_from_company_" . $type . "_" . $companyLocationId;
        try {
            $fromCompanyList = Cache::rememberForever($key, function () use ($type, $companyLocationId) {
                return $this->_createFromCompanyList($type, $companyLocationId);
            });
        } catch (\Exception $exception) {
            $fromCompanyList = $this->_createFromCompanyList($type, $companyLocationId);
        }

	return []; 
	//return $fromCompanyList;
    }

    private function _createFromCompanyList($type, $companyLocationId)
    {
        if ($type == Constants::SHUTTLE_TYPE_EMPLOYEE && $companyLocationId == Constants::COMPANY_LOCATION_HOSDERE) {
            return $this->_createFromCompanyListWithTab($type, $companyLocationId);
        } else {
            return $this->_createFromCompanyListWithoutTab($type, $companyLocationId);
        }
    }

    private function _createFromCompanyListWithoutTab($type, $companyLocationId)
    {
        $shuttleList = [];
        $toCompanyListResult = DB::select("select s.id, s.departure_location, s.license_plate, s.driver_name_surname, s.driver_telephone,
                                                        s.from_departure_time, s.from_arrival_time,
                                                        (select group_concat(fcss.id) from from_company_shuttle_stops fcss where fcss.shuttle_id = s.id order by fcss.order) from_company_stop_id_list,
                                                        (select group_concat(stops.name) from from_company_shuttle_stops fcss, stops stops where fcss.shuttle_id = s.id and fcss.stop_id = stops.id order by fcss.order) from_company_stop_name_list
                                                  from shuttles s
                                                  where s.company_location_id = ? and s.type = ? and s.from_departure_time is not null", [$companyLocationId, $type]);
        foreach ($toCompanyListResult as $item) {
            $stopList = [];
            $stopIdList = explode(",", $item->from_company_stop_id_list);
            $stopNameList = explode(",", $item->from_company_stop_name_list);
            for ($i = 0; $i < count($stopIdList); $i++) {
                $stopList[] = [
                    'id' => intval($stopIdList[$i]),
                    'name' => $stopNameList[$i]
                ];
            }

            $shuttleList[] = [
                'id' => $item->id,
                'name' => $item->departure_location,
                'departureTime' => Carbon::createFromFormat('H:i:s', $item->from_departure_time)->format('H:i'),
                'arrivalTime' => !empty($item->from_arrival_time) ? Carbon::createFromFormat('H:i:s', $item->from_arrival_time)->format('H:i') : null,
                'driverInfo' => $item->license_plate != null ? [
                    'licensePlate' => $item->license_plate,
                    'name' => $item->driver_name_surname,
                    'telephone' => $item->driver_telephone,
                ] : null,
                'stopList' => $stopList
            ];
        }

        $fromCompanyList = [
            [
                'name' => null,
                'shuttleList' => $shuttleList
            ]
        ];

        return $fromCompanyList;
    }

    private function _createFromCompanyListWithTab($type, $companyLocationId)
    {
        $morningShuttleList = $this->_createFromCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_MORNING);
        $noonShuttleList = $this->_createFromCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_NOON);
        $eveningShuttleList = $this->_createFromCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_EVENING);
        $nightShuttleList = $this->_createFromCompanyListByTimeType($type, $companyLocationId, Constants::SHUTTLE_TIME_TYPE_NIGHT);

        $fromCompanyList = [
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_MORNING_NAME,
                'shuttleList' => $morningShuttleList
            ],
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_NOON_NAME,
                'shuttleList' => $noonShuttleList
            ],
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_EVENING_NAME,
                'shuttleList' => $eveningShuttleList
            ],
            [
                'name' => Constants::SHUTTLE_TIME_TYPE_NIGHT_NAME,
                'shuttleList' => $nightShuttleList
            ]
        ];

        return $fromCompanyList;
    }

    private function _createFromCompanyListByTimeType($type, $companyLocationId, $timeType)
    {
        $shuttleList = [];
        $toCompanyListResult = DB::select("select s.id, s.departure_location, s.license_plate, s.driver_name_surname, s.driver_telephone,
                                                        s.from_departure_time, s.from_arrival_time,
                                                        (select group_concat(fcss.id) from from_company_shuttle_stops fcss where fcss.shuttle_id = s.id order by fcss.order) from_company_stop_id_list,
                                                        (select group_concat(stops.name) from from_company_shuttle_stops fcss, stops stops where fcss.shuttle_id = s.id and fcss.stop_id = stops.id order by fcss.order) from_company_stop_name_list
                                                  from shuttles s
                                                  where s.company_location_id = ? and s.type = ? and s.from_time_type = ? and s.from_departure_time is not null", [$companyLocationId, $type, $timeType]);
        foreach ($toCompanyListResult as $item) {
            $stopList = [];
            $stopIdList = explode(",", $item->from_company_stop_id_list);
            $stopNameList = explode(",", $item->from_company_stop_name_list);
            for ($i = 0; $i < count($stopIdList); $i++) {
                $stopList[] = [
                    'id' => intval($stopIdList[$i]),
                    'name' => $stopNameList[$i]
                ];
            }

            $shuttleList[] = [
                'id' => $item->id,
                'name' => $item->departure_location,
                'departureTime' => Carbon::createFromFormat('H:i:s', $item->from_departure_time)->format('H:i'),
                'arrivalTime' => !empty($item->from_arrival_time) ? Carbon::createFromFormat('H:i:s', $item->from_arrival_time)->format('H:i') : null,
                'driverInfo' => $item->license_plate != null ? [
                    'licensePlate' => $item->license_plate,
                    'name' => $item->driver_name_surname,
                    'telephone' => $item->driver_telephone,
                ] : null,
                'stopList' => $stopList
            ];
        }

        return $shuttleList;
    }
    //endregion
}
