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
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Redis;
use App\Services\MenuViewService;

class NewsController extends Controller
{
    public function newsList(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'type' => 'required|integer|min:1|max:10',
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
        
        $type = $request->input('type');
        $discountType = $request->input('discountType');
        $rowOffset = 10 * intval($request->input('pageNumber'));

        $userType = Auth::user()->type;
        $userCompanyLocationId = Auth::user()->company_location_id;
        $userEmployeeLocationId = Auth::user()->employee_location_id;
        $bindings = [];
        $userTypeStr = "";
        if ($userType!= null){
            $userTypeStr = "and (n.employee_type is null or n.employee_type = ?)";
            $bindings[] = $userType;
        }

        //region Setting SQL Query
        if ($this->isLangEn()) {
            $sqlQuery = "select n.id, n.title_en as title, n.type, n.discount_type, n.start_time, n.url, n.phone,
                                (
                                    select ni.image
                                    from news_images ni
                                    where ni.news_id = n.id
                                    LIMIT 1
                                ) as image  
                          from news n
                          where n.status = 1 and (n.end_time is null or n.end_time > now())
                                $userTypeStr
                                and (
                                      (select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0
                                      or
                                      (? in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id))
                                    )
                                and (
                                      (select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0
                                      or
                                      (? in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id))
                                    )
                                ";
            $bindings[] = $userCompanyLocationId;
            $bindings[] = $userEmployeeLocationId;
        } else {
            $sqlQuery = "select n.id, n.title, n.type, n.discount_type, n.start_time, n.url, n.phone,
                                (
                                    select ni.image
                                    from news_images ni
                                    where ni.news_id = n.id
                                    LIMIT 1
                                ) as image  
                          from news n
                          where n.status = 1 and (n.end_time is null or n.end_time > now())
                                $userTypeStr
                                and (
                                      (select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0
                                      or
                                      (? in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id))
                                    )
                                and (
                                      (select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0
                                      or
                                      (? in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id))
                                    )
                                ";
            $bindings[] = $userCompanyLocationId;
            $bindings[] = $userEmployeeLocationId;
        }

        if ($type != 1) {
            $sqlQuery .= " and n.type = ? ";
            $bindings[] = $type;
        } else {
            $sqlQuery .= " and n.type != 8 and n.type != 9 and n.type != 10 ";
        }

        if ($discountType != null && $discountType != 1) {
            $sqlQuery .= " and n.discount_type = ? ";
            $bindings[] = $discountType;
        }

        if ($type == 10) {
            $loc = $request->input('locId');
            $sqlQuery .= " and loc_id = ? order by n.order limit 10 offset ? ";
            $bindings[] = $loc;
            $bindings[] = $rowOffset;
        } else {
            $sqlQuery .= " order by n.start_time desc limit 10 offset ?";
            $bindings[] = $rowOffset;
        }

        //endregion

        //Log::info("Executing SQL Query: $sqlQuery", ['bindings' => $bindings]);
        $newsListResult = DB::select($sqlQuery, $bindings);

        $birthdayListCount = null;
        if ($rowOffset == 0) {
            $birthdayListCount = $this->_getBirthdayListCountFromCache();
        }

        // Category View Count Increase for the Panel Dashboard
        switch ($type) {
            case 2:
                MenuViewService::increment('Events');
                break;
            case 3:
                MenuViewService::increment('Discounts');
                break;
            case 7:
                MenuViewService::increment('News');
                break;
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
            $newsObjectResult = $this->getFirstItemFromDb("select n.title_en as title, n.text_en as text, n.sub_title_en as sub_title, n.discount_code_type, n.discount_code_all,
                                                                            n.image, n.url, n.phone, n.type, n.discount_type, n.start_time, n.end_time,
                                                                            (
                                                                                select group_concat(ni.image)
                                                                                from news_images ni
                                                                                where ni.news_id = n.id
                                                                              ) as images,
                                                                              (
                                                                                select concat( '[', group_concat('{\"id\":',np.id,',\"pdf\":\"',np.pdf_file,'\",\"name\":\"',np.pdf_name,'\"}'), ']')
                                                                                from news_pdf_files np
                                                                                where np.news_id = n.id
                                                                              ) as pdfs
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
            $newsObjectResult = $this->getFirstItemFromDb("select n.title, n.text, n.image, n.url, n.phone, n.type, n.discount_type, n.start_time, n.end_time, n.discount_code_type, n.discount_code_all,
                                                                    (
                                                                        select group_concat(ni.image)
                                                                        from news_images ni
                                                                        where ni.news_id = n.id
                                                                    ) as images,
                                                                    (
                                                                        select concat( '[', group_concat('{\"id\":',np.id,',\"pdf\":\"',np.pdf_file,'\",\"name\":\"',np.pdf_name,'\"}'), ']')
                                                                        from news_pdf_files np
                                                                        where np.news_id = n.id
                                                                      ) as pdfs
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
            $dateInfo = Carbon::parse($newsObjectResult->start_time)->format('d.m.Y');
        }

        $imageList = [];
        if($newsObjectResult->images){
            $imageList = explode(",", $newsObjectResult->images);
        }

        $userId = Auth::id();
        if ($newsObjectResult->discount_code_type == 2 ){
            // check is there is already one assigned for this user
            $discountCodeAssigned = $this->getFirstItemFromDb("select code from news_discount_codes where news_id =? and user_id = ?", [$id, $userId]);
            if ($discountCodeAssigned != null) {
                $newsObjectResult->discount_code_type = 1;
                $newsObjectResult->discount_code_all = $discountCodeAssigned->code;
            } else {
                $freeDiscountCode = $this->getFirstItemFromDb("select code from news_discount_codes where news_id =? and user_id = 0 limit 1", [$id]);
                if ($freeDiscountCode == null) { // no code left  
                    $newsObjectResult->discount_code_type =  1;
                    $newsObjectResult->discount_code_all = null;
                } else { //code left, user will click on getDiscountCode
                    $newsObjectResult->discount_code_all = null;
                }   
            }
        }

        $imageUrl = $newsObjectResult->image;
        if (count($imageList) > 0){
            $imageUrl = $imageList[0];
        }

        $newsObject = [
            'type' => $newsObjectResult->type,
            'discountType' => $newsObjectResult->discount_type,
            'image' =>  $imageUrl,
            'images' => $imageList, 
            'pdfs' => $newsObjectResult->pdfs ? json_decode($newsObjectResult->pdfs) : null,            
            'dateInfo' => $dateInfo,
            'title' => $newsObjectResult->title,
            'text' => $newsObjectResult->text,
            'url' => $newsObjectResult->url,
            "discountCodeType" => $newsObjectResult->discount_code_type,
            "discountCodeAll" => $newsObjectResult->discount_code_all,
        ];

        if(Redis::exists('viewCountForNews').$id){
            $viewCount = Redis::get('viewCountForNews'.$id);
            Redis::set('viewCountForNews'.$id, $viewCount +1);
        } else {
            Redis::set('viewCountForNews'.$id, 1);
        }


        return response()->json([
            'statusCode' => 200,
            'responseData' => $newsObject,
            'errorMessage' => null
        ]);
    }

    public function birthdayList()
    {
        MenuViewService::increment('Birthdays');
        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'dateInfo' => Carbon::now()->format('d.m.Y'),
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
                return DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1 order by name_surname', [$now->format('d.m')]);
            });
        } catch (\Exception $exception) {
            $birthdayList = DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1 order by name_surname', [$now->format('d.m')]);
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
                'listText' => $item->title,
                'monthName' => $this->_getMonthName($item->start_time, $item->type),
                'discountType' => $item->discount_type,
                'image' => $item->image,
                'url' => $item->url,
                'phone' => $item->phone
            ];
        }

        return $newsList;
    }

    private function _getMonthName($startTime, $type)
    {
	 if ($type == 10) {  //Social clubs
            return '';
        }

        $carbonStartDate = Carbon::parse($startTime);
        if ($carbonStartDate->isSameYear()) {
            if ($carbonStartDate->isCurrentMonth()) {
                return $this->isLangEn() ? "THIS MONTH" : "BU AY";
            } elseif ($carbonStartDate->isLastMonth()) {
                return $this->isLangEn() ? "LAST MONTH" : "GEÇEN AY";
            }

            return strtoupper($carbonStartDate->format('m.Y'));
        }

        return strtoupper($carbonStartDate->format('m.Y'));
    }


    public function getDiscountCode($newsId)
    {
        //region Controls
        $validator = Validator::make(['newsId' => $newsId], [
            'newsId' => 'integer|min:1',
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

        $userId = Auth::id();
        
        // check is there is already one assigned for this user
        $discountCodeAssigned = $this->getFirstItemFromDb("select code from news_discount_codes where news_id =? and user_id = ?", [$newsId, $userId]);
        if ($discountCodeAssigned != null) {
            return response()->json([
                'statusCode' => 200,
                'responseData' => $discountCodeAssigned 
            ]);
        }

        DB::update("update news_discount_codes set user_id = ? where news_id = ? and user_id = 0 LIMIT 1", [$userId, $newsId]);
            
        $discountCodeNew = $this->getFirstItemFromDb("select code from news_discount_codes where news_id =? and user_id = ?", [$newsId, $userId]);

        if ($discountCodeNew != null) {
            return response()->json([
                'statusCode' => 200,
                'responseData' => $discountCodeNew 
            ]);
        } else {
            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => "Maalesef tüm indirim kodları diğer kullanıcılar tarafından kullanıldı"
            ]);
        }
    }
}
