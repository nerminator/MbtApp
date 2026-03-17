<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 23.05.2018
 * Time: 23:14
 */

namespace App\Http\Controllers;


use App\Constants;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\DB;
use App\Services\MenuViewService;

class MapController extends Controller
{
    private const
        BUILDING_TYPE = 1,
        EMERGENCY_POINT_TYPE = 2;

    public function maps()
    {
        MenuViewService::increment('Location');
        return response()->json([
            'statusCode' => 200,
            'responseData' => $this->_getMapListFromCache(),
            'errorMessage' => null
        ]);
    }

    private function _getMapListFromCache()
    {
        try {
            $mapList = Cache::rememberForever("maps", function () {
                return $this->_createMapList();
            });
        } catch (\Exception $exception) {
            $mapList = $this->_createMapList();
        }

        if (count($mapList) > 0) {
            if (in_array(Auth::user()->company_location_id, [Constants::COMPANY_LOCATION_GENEL_MUDURLUK, Constants::COMPANY_LOCATION_HOSDERE, Constants::COMPANY_LOCATION_AKSARAY])) {
                $companyLocationId = Auth::user()->company_location_id;
            } else {
                $companyLocationId = Constants::COMPANY_LOCATION_GENEL_MUDURLUK;
            }

            $mapIndex = from($mapList)->findIndex(function ($mapItem) use ($companyLocationId) {
                return $mapItem['id'] == $companyLocationId;
            });

            if (!is_null($mapIndex)) {
                $mapList[$mapIndex]['isDefault'] = true;
            }
        }

        return $mapList;
    }

    private function _createMapList()
    {
        $mapList = [];
        $mapListResult = DB::select("select cl.id as map_id, cl.name as map_name,
                                            b.id as building_id, b.name as building_name, b.short_name as building_short_name,
                                            b.latitude as building_latitude, b.longitude as building_longitude, b.order as building_order, b.type as building_type,
                                            mr.id as meeting_room_id, mr.name as meeting_room_name, mr.order as meeting_room_order
                                      from buildings b
                                      left join meeting_rooms mr on mr.building_id = b.id
                                      inner join company_locations cl on b.company_location_id = cl.id
                                      order by cl.id, b.order, mr.order");
        foreach ($mapListResult as $item) {
            $mapIndex = from($mapList)->findIndex(function ($mapItem) use ($item) {
                return $mapItem['id'] == $item->map_id;
            });

            if (!is_null($mapIndex)) {
                if ($item->building_type == self::BUILDING_TYPE) { // building
                    $buildingIndex = from($mapList[$mapIndex]['buildingList'])->findIndex(function ($buildingItem) use ($mapIndex, $item) {
                        return $buildingItem['id'] == $item->building_id;
                    });

                    if (!is_null($buildingIndex)) {
                        $mapList[$mapIndex]['buildingList'][$buildingIndex]['meetingRoomList'][] = $this->_createMeetingRoomItem($item);
                    } else {
                        $mapList[$mapIndex]['buildingList'][] = $this->_createBuildingItem($item);
                    }
                } elseif ($item->building_type == self::EMERGENCY_POINT_TYPE) { // emergency point
                    $emergencyPointIndex = from($mapList[$mapIndex]['emergencyPoints'])->findIndex(function ($emergencyPointItem) use ($mapIndex, $item) {
                        return $emergencyPointItem['id'] == $item->building_id;
                    });

                    if (is_null($emergencyPointIndex)) {
                        $mapList[$mapIndex]['emergencyPoints'][] = $this->_createEmergencyPoint($item);
                    }
                }
            } else {
                $mapList[] = $this->_createMapItem($item);
            }
        }
        return $mapList;
    }

    private function _createMapItem($item)
    {
        if ($item->building_type == self::EMERGENCY_POINT_TYPE) {
            return [
                'id' => $item->map_id,
                'name' => $item->map_name,
                'buildingList' => [],
                'emergencyPoints' => [
                    $this->_createEmergencyPoint($item)
                ]
            ];
        } else {
            return [
                'id' => $item->map_id,
                'name' => $item->map_name,
                'buildingList' => [
                    $this->_createBuildingItem($item)
                ],
                'emergencyPoints' => []
            ];
        }
    }

    private function _createBuildingItem($item)
    {
        return [
            'id' => $item->building_id,
            'name' => $item->building_name,
            'shortName' => $item->building_short_name,
            'latitude' => $item->building_latitude,
            'longitude' => $item->building_longitude,
            'order' => $item->building_order,
            'meetingRoomList' => !empty($item->meeting_room_id) ? [
                $this->_createMeetingRoomItem($item)
            ] : []
        ];
    }

    private function _createMeetingRoomItem($item)
    {
        return [
            'id' => $item->meeting_room_id,
            'name' => $item->meeting_room_name,
            'order' => $item->meeting_room_order
        ];
    }

    private function _createEmergencyPoint($item)
    {
        return [
            'id' => $item->building_id,
            'name' => $item->building_name,
            'shortName' => $item->building_short_name,
            'latitude' => $item->building_latitude,
            'longitude' => $item->building_longitude,
            'order' => $item->building_order,
            'meetingRoomList' => []
        ];
    }
}