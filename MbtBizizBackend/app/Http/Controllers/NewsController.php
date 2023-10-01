<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 3.05.2018
 * Time: 22:22
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class NewsController extends Controller
{
    public function newsList(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'type' => 'required|integer|min:1|max:9',
            'discountType' => 'required_if:type,==,3|integer|min:1|max:14',
            'pageNumber' => 'required|integer|min:0'
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

        $type = Input::get('type');
        $discountType = Input::get('discountType');
        $rowOffset = 10 * Input::get('pageNumber');

        $userType = Auth::user()->type;
        $userCompanyLocationId = Auth::user()->company_location_id;
        $userEmployeeLocationId = Auth::user()->employee_location_id;

        //region Setting SQL Query
        if ($this->isLangEn()) {
            $sqlQuery = "select n.id, n.list_text_en as list_text, n.image, n.type, n.discount_type, n.start_time, n.url, n.phone
                          from news n
                          where n.status = 1 and (n.end_time is null or n.end_time > now())
                                and (n.employee_type is null or n.employee_type = $userType)
                                and (
                                      (select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0
                                      or
                                      ($userCompanyLocationId in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id))
                                    )
                                and (
                                      (select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0
                                      or
                                      ($userEmployeeLocationId in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id))
                                    )
                                ";
        } else {
            $sqlQuery = "select n.id, n.list_text, n.image, n.type, n.discount_type, n.start_time, n.url, n.phone
                          from news n
                          where n.status = 1 and (n.end_time is null or n.end_time > now())
                                and (n.employee_type is null or n.employee_type = $userType)
                                and (
                                      (select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0
                                      or
                                      ($userCompanyLocationId in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id))
                                    )
                                and (
                                      (select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0
                                      or
                                      ($userEmployeeLocationId in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id))
                                    )
                                ";
        }

        if ($type != 1) {
            $sqlQuery .= "and n.type = $type ";
        } else {
            $sqlQuery .= "and n.type != 8 and n.type != 9 ";
        }

        if ($discountType != null && $discountType != 1) {
            $sqlQuery .= "and n.discount_type = $discountType ";
        }

        $sqlQuery .= "order by n.start_time desc
                    limit 10 offset $rowOffset";
        //endregion

        $newsListResult = DB::select($sqlQuery);

        $birthdayListCount = null;
        if ($rowOffset == 0) {
            $birthdayListCount = $this->_getBirthdayListCountFromCache();
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'birthdayCount' => $birthdayListCount,
                'newsList' => $this->_getNewsList($newsListResult)
            ],
            'errorMessage' => null
        ]);
    }

    public function newsDetail($id)
    {
        //region Controls
        $validator = Validator::make(['id' => $id], [
            'id' => 'integer|min:1'
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

        $params = [$id, Auth::user()->type, Auth::user()->company_location_id, Auth::user()->employee_location_id];
        if ($this->isLangEn()) {
            $newsObjectResult = $this->getFirstItemFromDb("select n.title_en as title, n.text_en as text, n.sub_title_en as sub_title, n.sub_text_en as sub_text,
                                                                            n.image, n.url, n.phone, n.type, n.discount_type, n.start_time, n.end_time
                                                                    from news n
                                                                    where n.id = ? and (n.end_time is null or n.end_time > now())
                                                                          and (n.employee_type is null or n.employee_type = ?)
                                                                          and (
                                                                                (select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0
                                                                                or
                                                                                (? in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id))
                                                                              )
                                                                          and (
                                                                                  (select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0
                                                                                  or
                                                                                  (? in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id))
                                                                              )", $params);
        } else {
            $newsObjectResult = $this->getFirstItemFromDb("select n.title, n.text, n.sub_title, n.sub_text, n.image, n.url, n.phone, n.type, n.discount_type, n.start_time, n.end_time
                                                                    from news n
                                                                    where n.id = ? and (n.end_time is null or n.end_time > now())
                                                                          and (n.employee_type is null or n.employee_type = ?)
                                                                          and (
                                                                                (select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0
                                                                                or
                                                                                (? in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id))
                                                                              )
                                                                          and (
                                                                                  (select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0
                                                                                  or
                                                                                  (? in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id))
                                                                              )", $params);
        }
        if ($newsObjectResult == null) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => Lang::get('lang.TXT_SERVER_ERROR_NEWS_NOT_FOUND'),
                'errorMessage' => null
            ]);
        }

        if ($newsObjectResult->end_time != null) {
            $dateInfo = Carbon::parse($newsObjectResult->start_time)->format('d.m.Y') . " - " . Carbon::parse($newsObjectResult->end_time)->format('d.m.Y');
        } else {
            $dateInfo = Carbon::parse($newsObjectResult->start_time)->formatLocalized('%d %B %Y, %A');
        }

        $newsObject = [
            'type' => $newsObjectResult->type,
            'discountType' => $newsObjectResult->discount_type,
            'image' => $newsObjectResult->image,
            'dateInfo' => $dateInfo,
            'title' => $newsObjectResult->title,
            'text' => $newsObjectResult->text,
            'subTitle' => $newsObjectResult->sub_title,
            'subText' => $newsObjectResult->sub_text,
            'url' => $newsObjectResult->url
        ];

        return response()->json([
            'statusCode' => 200,
            'responseData' => $newsObject,
            'errorMessage' => null
        ]);
    }

    public function birthdayList()
    {
        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'dateInfo' => Carbon::now()->formatLocalized('%d %B %Y, %A'),
                'birthdayList' => $this->_getBirthdayListFromCache()
            ],
            'errorMessage' => null
        ]);
    }

    private function _getBirthdayListCountFromCache()
    {
        $now = Carbon::now();
        try {
            $birthdayListCount = Cache::remember($now->toDateString() . "_birthday_list_count", 10080, function () use ($now) {
                return count(DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1', [$now->format('d.m')]));
            });
        } catch (\Exception $exception) {
            $birthdayListCount = count(DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1', [$now->format('d.m')]));
        }

        return intval($birthdayListCount);
    }

    private function _getBirthdayListFromCache()
    {
        $now = Carbon::now();
        try {
            $birthdayList = Cache::remember($now->toDateString() . "_birthday_list", 10080, function () use ($now) {
                return DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1', [$now->format('d.m')]);
            });
        } catch (\Exception $exception) {
            $birthdayList = DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1', [$now->format('d.m')]);
        }

        return $birthdayList;
    }

    private function _getNewsList($newsListResult)
    {
        $newsList = [];

        foreach ($newsListResult as $item) {
            $newsList[] = [
                'id' => $item->id,
                'type' => $item->type,
                'listText' => $item->list_text,
                'monthName' => $this->_getMonthName($item->start_time),
                'discountType' => $item->discount_type,
                'image' => $item->image,
                'url' => $item->url,
                'phone' => $item->phone
            ];
        }

        return $newsList;
    }

    private function _getMonthName($startTime)
    {
        $carbonStartDate = Carbon::parse($startTime);
        if ($carbonStartDate->isSameYear()) {
            if ($carbonStartDate->isCurrentMonth()) {
                return $this->isLangEn() ? "THIS MONTH" : "BU AY";
            } elseif ($carbonStartDate->isLastMonth()) {
                return $this->isLangEn() ? "LAST MONTH" : "GEÇEN AY";
            }

            return strtoupper($carbonStartDate->formatLocalized('%B')) . ($this->isLangEn() ? "" : " AYI");
        }

        return strtoupper($carbonStartDate->formatLocalized('%Y %B')) . ($this->isLangEn() ? "" : " AYI");
    }
}
