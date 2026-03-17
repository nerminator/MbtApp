<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 21.05.2018
 * Time: 23:42
 */

namespace App\Http\Controllers;


use App\Constants;
use Carbon\Carbon;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use App\Services\MenuViewService;

class ProfileController extends Controller
{
    public function profile()
    {
        $profileResult = $this->getFirstItemFromDb("select u.register_number, u.name_surname, u.title,
                                                                    u.expense_center_number, u.expense_center, u.company_code,
                                                                    u.location, cl.name as company_location_name, u.type
                                                              from users u, company_locations cl
                                                              where u.company_location_id = cl.id and u.id = ? and u.status = 1", [Auth::id()]);

        if ($profileResult == null) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => null
            ]);
        }

        $workHoursAvailable = in_array($profileResult->type, [Constants::EMPLOYEE_TYPE_WHITE_COLLAR, Constants::EMPLOYEE_TYPE_BLUE_COLLAR]);
        if ($profileResult->type == Constants::EMPLOYEE_TYPE_BLUE_COLLAR){
            $workHoursText = Lang::get('lang.TXT_BLUE_COLLAR_WORK_HOURS');
            $yearlyWorkHoursText = Lang::get('lang.TXT_BLUE_COLLAR_YEARLY_WORK_HOURS');
            $monthlyWorkHoursText = Lang::get('lang.TXT_BLUE_COLLAR_MONTHLY_WORK_HOURS');
        }else{
            $workHoursText = Lang::get('lang.TXT_WHITE_COLLAR_WORK_HOURS');
            $yearlyWorkHoursText = Lang::get('lang.TXT_WHITE_COLLAR_YEARLY_WORK_HOURS');
            $monthlyWorkHoursText = Lang::get('lang.TXT_WHITE_COLLAR_MONTHLY_WORK_HOURS');            
            if ($profileResult->company_code != null && $profileResult->company_code == 1402){
                $workHoursText='';
                $workHoursAvailable=false;
                $yearlyWorkHoursText='';
                $monthlyWorkHoursText='';
            }    
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'registerNumber' => $profileResult->register_number . "",
                'nameSurname' => $profileResult->name_surname,
                'title' => $profileResult->title,
                'officeLocation' => $profileResult->location . " / " . $profileResult->company_location_name,
                'organizationUnit' => $profileResult->expense_center_number . " / " . $profileResult->expense_center . " / " . $profileResult->company_code,
                'employeeType' => $profileResult->type,                
		        'workHoursText' => $workHoursText,
                'workHoursAvailable' => $workHoursAvailable,
                'yearlyWorkHoursText' => $yearlyWorkHoursText,
                'monthlyWorkHoursText' => $monthlyWorkHoursText
            ],
            'errorMessage' => null
        ]);
    }

    public function yearlyWorkHours($year)
    {
        //region Controls
        $validator = Validator::make(['year' => $year], [
            'year' => 'date_format:Y'
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

        $yearList = null;
        $selectedYear = intval($year);

        $regNo = Auth::user()->register_number; 
        if ($regNo == 1234563) {
            $regNo = 100038;
        }
        //Log::info($regNo);
        if ($year == 0) { // default (means selected last year)

            $yearListResult = DB::select("select year(info_date) as year_info
                                                  from working_hours_info
                                                  where register_number = ?
                                                  group by year(info_date)", [$regNo]);
            if ($yearListResult == null || count($yearListResult) == 0) {
                return response()->json([
                    'statusCode' => 401,
                    'responseData' => null,
                    'errorMessage' => Auth::user()->type == Constants::EMPLOYEE_TYPE_BLUE_COLLAR ? Lang::get('lang.TXT_SERVER_ERROR_BLUE_COLLAR_WORK_HOURS_FOUND') : Lang::get('lang.TXT_SERVER_ERROR_WHITE_COLLAR_WORK_HOURS_FOUND')
                ]);
            }

            $yearList = from($yearListResult)->select(function ($v) {return $v->year_info;})->toArray();
            //select('$v->year_info')->toArray();
            if ($yearList != null && count($yearList) != 0){
                $selectedYear = from($yearList)->max();
            }
        }

        if (Auth::user()->type == Constants::EMPLOYEE_TYPE_WHITE_COLLAR) { // beyaz yaka
            $monthList = DB::select("select month(whi.info_date) as month, cast(sum(whi.plus_hours) as char(6)) as plusHours,
                                              cast(sum(whi.minus_hours) as char(6)) as minusHours,
                                              cast((
                                                select whi2.cumulative_hours
                                                from working_hours_info whi2
                                                where whi2.register_number = whi.register_number and year(whi2.info_date) = year(whi.info_date) and
                                                      month(whi2.info_date) = month(whi.info_date)
                                                order by whi2.info_date desc
                                                limit 1
                                                ) as char(6)) as totalHours
                                            from working_hours_info whi
                                            where whi.register_number = ? and year(whi.info_date) = ?
                                            group by month(whi.info_date)", [Auth::user()->register_number, $selectedYear]);
        } else { // mavi yaka
            $monthList = DB::select("select month(info_date) as month, cast(sum(plus_hours) as char(6)) as plusHours,
                                              cast(sum(minus_hours) as char(6)) as minusHours, cast(sum(total_hours) as char(6)) as totalHours
                                            from working_hours_info
                                            where register_number = ? and year(info_date) = ?
                                            group by month(info_date)", [$regNo, $selectedYear]);
        }

        MenuViewService::increment('Profile_WorkHours');

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'plusHoursText' => Lang::get('lang.TXT_PLUS_HOURS'),
                'minusHoursText' => Lang::get('lang.TXT_MINUS_HOURS'),
                'yearList' => $yearList,
                'selectedYear' => $selectedYear,
                'monthList' => $monthList
            ],
            'errorMessage' => null
        ]);
    }

    public function monthlyWorkHours($year, $month)
    {
        //region Controls
        $validator = Validator::make(['year' => $year, 'month' => $month], [
            'month' => 'required|date_format:m',
            'year' => 'required|date_format:Y'
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

        if ($this->isLangEn()) {
            DB::statement("SET lc_time_names = 'en_US'");
        } else {
            DB::statement("SET lc_time_names = 'tr_TR'");
        }


        $regNo = Auth::user()->register_number; 
        if ($regNo == 1234563) {
            $regNo = 100038;
        }

        $dayList = DB::select("select date_format(info_date,'%d.%m.%Y %a') as dayText, hours_info as hoursText, cast(plus_hours as char(6)) as plusHours,
                                              cast(minus_hours as char(6)) as minusHours, cast(total_hours as char(6)) as totalHours
                                        from working_hours_info
                                        where register_number = ? and year(info_date) = ? and month(info_date) = ?
                                        order by info_date", [$regNo, $year, $month]);

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'plusHoursText' => Lang::get('lang.TXT_PLUS_HOURS'),
                'minusHoursText' => Lang::get('lang.TXT_MINUS_HOURS'),
                'dayList' => $dayList
            ],
            'errorMessage' => null
        ]);
    }

    public function workCalendar($date)
    {
        //region Controls
        $validator = Validator::make(['date' => $date], [
            'date' => 'required|date_format:d.m.Y'
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

        $startDate = Carbon::createFromFormat('d.m.Y', $date);
        $year = $startDate->year;
        $month = $startDate->month;

        $startDate->startOfMonth();
        if ($startDate->dayOfWeek == 1) {
            $startDate->subWeek();
        } else {
            $startDate->startOfWeek();
        }
        $endDate = $startDate->copy()->addDays(41);

        /*$dayList = DB::select("select date_format(wc.calendar_date,'%d.%m.%Y') as day, wct.name as typeName, wct.color as typeColor, date_format(wc.calendar_date,'%d %M %Y, %W') as dayText
                                      from working_calendar wc, working_calendar_types wct
                                      where wc.type_id = wct.id and wc.register_number = ? and wc.calendar_date >= ? and wc.calendar_date <= ?
                                      order by wc.calendar_date", [Auth::user()->register_number, $startDate->toDateTimeString(), $endDate->toDateTimeString()]);*/

        $initialDayListTemp = [];
        $dayList = [];

        if ($this->isLangEn()) {
            $typeNameStr = "wct.name_en";
            DB::statement("SET lc_time_names = 'en_US'");
        } else {
            $typeNameStr = "wct.name";
            DB::statement("SET lc_time_names = 'tr_TR'");
        }

        $dayListResult = DB::select("select date_format(wc.calendar_date,'%d.%m.%Y') as day, date_format(wc.calendar_date,'%d %M %Y, %W') as dayText,
                                            date_format(wc.calendar_date,'%d') as dayForInitial, date_format(wc.calendar_date,'%m.%Y') as monthYear,
                                            date_format(wc.calendar_date,'%M %Y') as monthYearText,
                                            wc.type_id, $typeNameStr as typeName, wct.color as typeColor
                                      from working_calendar wc, working_calendar_types wct
                                      where wc.type_id = wct.id and wc.register_number = ? and wc.calendar_date >= ? and wc.calendar_date <= ?
                                      order by wc.calendar_date", [Auth::user()->register_number, $startDate->toDateTimeString(), $endDate->toDateTimeString()]);
        foreach ($dayListResult as $item) {
            // initial list
            $typeIndex = from($initialDayListTemp)->findIndex(function ($initialDayItem) use ($item) {
                return $initialDayItem['typeId'] == $item->type_id && $initialDayItem['monthYear'] == $item->monthYear;
            });
            if (!is_null($typeIndex)) {
                $initialDayListTemp[$typeIndex]['dayList'][] = $item->dayForInitial;
            } else {

                $initialDayListTemp[] = [
                    'typeId' => $item->type_id,
                    'monthYear' => $item->monthYear,
                    'monthYearText' => $item->monthYearText,
                    'typeName' => $item->typeName,
                    'typeColor' => $item->typeColor,
                    'dayList' => [
                        $item->dayForInitial
                    ]
                ];
            }

            // day list
            $dayIndex = from($dayList)->findIndex(function ($dayItem) use ($item) {
                return $dayItem['day'] == $item->day;
            });
            if (!is_null($dayIndex)) {
                $dayList[$dayIndex]['typeList'][] = [
                    'typeName' => $item->typeName,
                    'typeColor' => $item->typeColor
                ];
            } else {
                $dayList[] = [
                    'day' => $item->day,
                    'dayText' => $item->dayText,
                    'typeList' => [
                        [
                            'typeName' => $item->typeName,
                            'typeColor' => $item->typeColor
                        ]
                    ]
                ];
            }
        }

        $initialDayList = [];
        if (count($initialDayListTemp) > 0) {
            foreach ($initialDayListTemp as $item) {
                $initialDayList[] = [
                    'dayText' => $this->_getRanges($item['dayList']) . " " . $item['monthYearText'],
                    'typeName' => $item['typeName'],
                    'typeColor' => $item['typeColor']
                ];
            }
        }

        MenuViewService::increment('Profile_WorkCalendar');

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'year' => $year,
                'month' => $month,
                'initialDayList' => $initialDayList,
                'dayList' => $dayList
            ],
            'errorMessage' => null
        ]);
    }

    private function _getRanges($aNumbers)
    {
        $aNumbers = array_unique($aNumbers);
        sort($aNumbers);
        $aGroups = array();
        for ($i = 0; $i < count($aNumbers); $i++) {
            if ($i > 0 && ($aNumbers[$i - 1] == $aNumbers[$i] - 1))
                array_push($aGroups[count($aGroups) - 1], $aNumbers[$i]);
            else
                array_push($aGroups, array($aNumbers[$i]));
        }
        $aRanges = array();
        foreach ($aGroups as $aGroup) {
            if (count($aGroup) == 1)
                $aRanges[] = $aGroup[0];
            else
                $aRanges[] = $aGroup[0] . '-' . $aGroup[count($aGroup) - 1];
        }
        return implode(', ', $aRanges);
    }
}
