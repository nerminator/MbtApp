<?php

namespace App\Http\Controllers;

use App\Constants;
use App\Jobs\DeleteNewsNotification;
use App\Jobs\SendNewsNotification;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class NewsController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        $emp = $this->getEmployeeTypeList();
        $comp = $this->getCompanyList();
        $loc = $this->getLocationList();

        return view('addnews')
            ->with('emp', json_decode($emp, true))
            ->with('compL', json_decode($comp, true))
            ->with('compC', json_decode($loc, true));
    }

    public function getList(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'sortType' => 'required|integer|min:1|max:2',
            'searchText' => 'nullable|string|regex:/^[(a-zA-ZşŞıİçÇöÖüÜĞğ.\-\s)(0-9\s)]+$/u',
            'pageNumber' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

        $searchText = Input::get('searchText');
        $rowOffset = 20 * (Input::get('pageNumber') - 1);

        $sqlQuery = "select n.*,
                            (case
                                when n.employee_type = 1 then 'White Collar'
                                when n.employee_type = 2 then 'Blue Collar'
                                else null
                            end) as employee_type_name,
                            (
                              select group_concat(cl.name)
                              from news_company_location ncl
                              join company_locations cl on ncl.company_location_id = cl.id
                              where ncl.news_id = n.id
							) as company_names,
							(
                              select group_concat(el.name)
                              from news_employee_location nel
                              join employee_locations el on nel.employee_location_id = el.id
                              where nel.news_id = n.id
							) as location_names
                    from news n
                    where n.status <> 0";

        if (!empty($searchText)) {
            $sqlQuery .= " and
                          (n.title like ? or n.title_en like ? or
                            n.text like ? or n.text_en like ? or
                            n.sub_title like ? or n.sub_title_en like ? or
                            n.sub_text like ? or n.sub_text_en like ?)";
        }

        if (Input::get('sortType') == Constants::SORT_TYPE_CREATED_AT) {
            $sqlQuery .= " order by n.id desc";
        } else {
            $sqlQuery .= " order by n.start_time desc";
        }

        $sqlQuery .= " limit 20 offset ?";

        if (!empty($searchText)) {
            $searchValue = '%' . $searchText . '%';
            $params = array_fill(0, 8, $searchValue);
            $params[] = $rowOffset;
            $newsList = DB::select($sqlQuery, $params);
        } else {
            $newsList = DB::select($sqlQuery, [$rowOffset]);
        }

        /*if (Input::get('sortType') == Constants::SORT_TYPE_CREATED_AT) {
            if (!empty($searchText)) {
                $searchValue = '%' . $searchText . '%';
                $newsList = DB::table('news')->where('status', '<>', 0)->where(function ($query) use ($searchValue) {
                    $query->where('title', 'like', $searchValue)->orWhere('title_en', 'like', $searchValue)
                        ->orWhere('text', 'like', $searchValue)->orWhere('text_en', 'like', $searchValue)
                        ->orWhere('sub_title', 'like', $searchValue)->orWhere('sub_title_en', 'like', $searchValue)
                        ->orWhere('sub_text', 'like', $searchValue)->orWhere('sub_text_en', 'like', $searchValue);
                })->orderByDesc('id')->offset($rowOffset)->limit(20)->get();
            } else {
                $newsList = DB::table('news')->where('status', '<>', 0)->orderByDesc('id')->offset($rowOffset)->limit(20)->get();
            }
        } else {
            if (!empty($searchText)) {
                $searchValue = '%' . $searchText . '%';
                $newsList = DB::table('news')->where('status', '<>', 0)->where(function ($query) use ($searchValue) {
                    $query->where('title', 'like', $searchValue)->orWhere('text', 'like', $searchValue)
                        ->orWhere('text', 'like', $searchValue)->orWhere('text_en', 'like', $searchValue)
                        ->orWhere('sub_title', 'like', $searchValue)->orWhere('sub_title_en', 'like', $searchValue)
                        ->orWhere('sub_text', 'like', $searchValue)->orWhere('sub_text_en', 'like', $searchValue);
                })->orderByDesc('start_time')->offset($rowOffset)->limit(20)->get();
            } else {
                $newsList = DB::table('news')->where('status', '<>', 0)->orderByDesc('start_time')->offset($rowOffset)->limit(20)->get();
            }
        }*/

        return json_encode($newsList);
    }

    public function get(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

//        $newsResult = DB::table('news')->where('id', Input::get('id'))->first();
        $newsResult = $this->getFirstItemFromDb("select n.*,
                                                              (
                                                                select group_concat(ncl.id)
                                                                from news_company_location ncl
                                                                where ncl.news_id = n.id
                                                              ) as company_ids,
                                                              (
                                                                select group_concat(nel.id)
                                                                from news_employee_location nel
                                                                where nel.news_id = n.id
                                                              ) as location_ids
                                                          from news n
                                                          where id = ?", [Input::get('id')]);

        return json_encode($newsResult);
    }

    public static function getEmployeeTypeList()
    {
        return json_encode([
            [
                'text' => "White Collar",
                'value' => Constants::EMPLOYEE_TYPE_WHITE_COLLAR
            ],
            [
                'text' => "Blue Collar",
                'value' => Constants::EMPLOYEE_TYPE_BLUE_COLLAR
            ]
        ]);
    }

    public static function getCompanyList()
    {
        $companyLocationsResult = DB::table('company_locations')->get();
        $companyLocationList = [];
        foreach ($companyLocationsResult as $item) {
            $companyLocationList[] = [
                'text' => $item->name,
                'value' => $item->id
            ];
        }

        return json_encode($companyLocationList);
    }

    public static function getLocationList()
    {
        $companyCodesResult = DB::table('employee_locations')->get();
        $companyCodeList = [];
        foreach ($companyCodesResult as $item) {
            $companyCodeList[] = [
                'text' => $item->name,
                'value' => $item->id
            ];
        }

        return json_encode($companyCodeList);
    }

    public function add(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'listText' => 'required|string|max:100',
            'listTextEn' => 'required|string|max:100',
            'title' => 'nullable|string|max:100',
            'titleEn' => 'nullable|string|max:100',
            'text' => 'nullable|string|max:10000',
            'textEn' => 'nullable|string|max:10000',
            'subTitle' => 'nullable|string|max:100',
            'subTitleEn' => 'nullable|string|max:100',
            'subText' => 'nullable|string|max:10000',
            'subTextEn' => 'nullable|string|max:10000',
            'imageString' => 'nullable|string',
            'url' => 'nullable|url',
            'phone' => 'nullable|numeric',
            'type' => 'required|integer|min:1|max:9',
            'discountType' => 'required_if:type,==,3|integer|min:1|max:14',
            'startTime' => 'required|date',
            'endTime' => 'nullable|date',
            'employeeType' => 'nullable|integer|min:1|max:2',
            'companyIdList' => 'nullable|array',
            'companyIdList.*' => 'string',
            'locationIdList' => 'nullable|array',
            'locationIdList.*' => 'string'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
            //return response()->json($validator->errors());
        }
        //endregion

        $now = Carbon::now();
        $newsId = DB::table('news')->insertGetId([
            'list_text' => Input::get('listText'),
            'list_text_en' => Input::get('listTextEn'),
            'title' => Input::get('title'),
            'title_en' => Input::get('titleEn'),
            'text' => Input::get('text'),
            'text_en' => Input::get('textEn'),
            'sub_title' => Input::get('subTitle'),
            'sub_title_en' => Input::get('subTitleEn'),
            'sub_text' => Input::get('subText'),
            'sub_text_en' => Input::get('subTextEn'),
            'url' => Input::get('url'),
            'phone' => Input::get('phone'),
            'type' => Input::get('type'),
            'discount_type' => Input::get('discountType'),
            'start_time' => Input::get('startTime'),
            'end_time' => Input::get('endTime'),
            'employee_type' => Input::get('employeeType'),
            'status' => Constants::STATUS_PASSIVE,
            'created_at' => $now,
            'updated_at' => $now
        ]);

        $companyIdList = Input::get('companyIdList');
        if (is_array($companyIdList) && count($companyIdList) > 0) {
            $newsCompanyLocationList = [];
            foreach ($companyIdList as $companyId) {
                $newsCompanyLocationList[] = [
                    'news_id' => $newsId,
                    'company_location_id' => $companyId
                ];
            }
            DB::table('news_company_location')->insert($newsCompanyLocationList);
        }

        $locationIdList = Input::get('locationIdList');
        if (is_array($locationIdList) && count($locationIdList) > 0) {
            $newsEmployeeLocationList = [];
            foreach ($locationIdList as $locationId) {
                $newsEmployeeLocationList[] = [
                    'news_id' => $newsId,
                    'employee_location_id' => $locationId
                ];
            }
            DB::table('news_employee_location')->insert($newsEmployeeLocationList);
        }

        $imageString = Input::get('imageString');
        if (!empty($imageString)) {
            try {
                $base64Str = substr($imageString, strpos($imageString, ",") + 1);
                if ($this->getBase64ImageSize($base64Str) > 1.5) {
                    return redirect('addnews')->with('status', ' File must be less than 1.5 megabytes!'); // file size error
                    //return -2; // file size error
                }
                $image = base64_decode($base64Str);

                $imagePath = "contents/news/$newsId/" . uniqid() . uniqid() . uniqid() . ".png";
                Storage::disk('public')->put($imagePath, $image);
                $projectUrl = app()->environment() == "production" ? "https://bizizapp.com/bizizPanel/public" : "http://mbtbiziz:8888";
                $imageUrl = $projectUrl . "/storage/$imagePath";
                DB::table('news')->where('id', $newsId)->update(['image' => $imageUrl]);
            } catch (\Exception $e) {

            }
        }

        return view('home');
    }

    public function edit(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'listText' => 'required|string|max:100',
            'listTextEn' => 'required|string|max:100',
            'title' => 'nullable|string|max:100',
            'titleEn' => 'nullable|string|max:100',
            'text' => 'nullable|string|max:10000',
            'textEn' => 'nullable|string|max:10000',
            'subTitle' => 'nullable|string|max:100',
            'subTitleEn' => 'nullable|string|max:100',
            'subText' => 'nullable|string|max:10000',
            'subTextEn' => 'nullable|string|max:10000',
            'imageString' => 'nullable|string',
            'url' => 'nullable|url',
            'phone' => 'nullable|numeric',
            'type' => 'required|integer|min:1|max:9',
            'discountType' => 'required_if:type,==,3|integer|min:1|max:14',
            'startTime' => 'required|date',
            'endTime' => 'nullable|date',
            'employeeType' => 'nullable|integer|min:1|max:2',
            'companyIdList' => 'nullable|array',
            'companyIdList.*' => 'string',
            'locationIdList' => 'nullable|array',
            'locationIdList.*' => 'string'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return Input::get('companyIdList');

        }
        //endregion

        $newsId = Input::get('id');
        DB::table('news')->where('id', $newsId)->update([
            'list_text' => Input::get('listText'),
            'list_text_en' => Input::get('listTextEn'),
            'title' => Input::get('title'),
            'title_en' => Input::get('titleEn'),
            'text' => Input::get('text'),
            'text_en' => Input::get('textEn'),
            'sub_title' => Input::get('subTitle'),
            'sub_title_en' => Input::get('subTitleEn'),
            'sub_text' => Input::get('subText'),
            'sub_text_en' => Input::get('subTextEn'),
            'url' => Input::get('url'),
            'phone' => Input::get('phone'),
            'type' => Input::get('type'),
            'discount_type' => Input::get('discountType'),
            'start_time' => Input::get('startTime'),
            'end_time' => Input::get('endTime'),
            'employee_type' => Input::get('employeeType'),
            'updated_at' => Carbon::now()
        ]);

        DB::table('news_company_location')->where('news_id', $newsId)->delete();
        $companyIdList = Input::get('companyIdList');
        if (is_array($companyIdList) && count($companyIdList) > 0) {
            $newsCompanyLocationList = [];
            foreach ($companyIdList as $companyId) {
                $newsCompanyLocationList[] = [
                    'news_id' => $newsId,
                    'company_location_id' => $companyId
                ];
            }
            DB::table('news_company_location')->insert($newsCompanyLocationList);
        }

        DB::table('news_employee_location')->where('news_id', $newsId)->delete();
        $locationIdList = Input::get('locationIdList');
        if (is_array($locationIdList) && count($locationIdList) > 0) {
            $newsEmployeeLocationList = [];
            foreach ($locationIdList as $locationId) {
                $newsEmployeeLocationList[] = [
                    'news_id' => $newsId,
                    'employee_location_id' => $locationId
                ];
            }
            DB::table('news_employee_location')->insert($newsEmployeeLocationList);
        }

        $imageString = Input::get('imageString');
        if (!empty($imageString)) {
            try {
                $base64Str = substr($imageString, strpos($imageString, ",") + 1);
                if ($this->getBase64ImageSize($base64Str) > 1.5) {
                    return redirect('addnews')->with('status', ' File must be less than 1.5 megabytes!'); // file size error
                    //return -2; // file size error
                }
                $image = base64_decode($base64Str);

                $imagePath = "contents/news/$newsId/" . uniqid() . uniqid() . uniqid() . ".png";
                Storage::disk('public')->put($imagePath, $image);
                $projectUrl = app()->environment() == "production" ? "https://bizizapp.com/bizizPanel/public" : "http://mbtbiziz:8888";
                $imageUrl = $projectUrl . "/storage/$imagePath";
                DB::table('news')->where('id', $newsId)->update(['image' => $imageUrl]);
            } catch (\Exception $e) {

            }
        }

        return view('home');
    }

    private function getBase64ImageSize($base64Image)
    {
        try {
            $size_in_bytes = (int)(strlen(rtrim($base64Image, '=')) * 3 / 4);
            $size_in_kb = $size_in_bytes / 1024;
            $size_in_mb = $size_in_kb / 1024;

            return $size_in_mb;
        } catch (\Exception $e) {
            return $e;
        }
    }

    public function activate(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

        $newsId = Input::get('id');
        $affectedRowCount = DB::table('news')->where('id', $newsId)->update(['status' => Constants::STATUS_ACTIVE, 'updated_at' => Carbon::now()]);
        DeleteNewsNotification::dispatch($newsId);

        return json_encode($affectedRowCount > 0);
    }

    public function passivate(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

        $newsId = Input::get('id');
        $affectedRowCount = DB::table('news')->where('id', $newsId)->update(['status' => Constants::STATUS_PASSIVE, 'updated_at' => Carbon::now()]);
        DeleteNewsNotification::dispatch($newsId);

        return json_encode($affectedRowCount > 0);
    }

    public function delete(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

        $newsId = Input::get('id');
        $affectedRowCount = DB::table('news')->where('id', $newsId)->update(['status' => Constants::STATUS_DELETED, 'updated_at' => Carbon::now()]);
        DeleteNewsNotification::dispatch($newsId);

        return json_encode($affectedRowCount > 0);
    }

    public function deleteImage(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

        $affectedRowCount = DB::table('news')->where('id', Input::get('id'))->update(['image' => null, 'updated_at' => Carbon::now()]);

        return json_encode($affectedRowCount > 0);
    }

    public function sendPushNotification(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion

        $newsId = Input::get('id');
        $newsResult = DB::table('news')->where('id', $newsId)->where('status', Constants::STATUS_ACTIVE)->first();
        if (empty($newsResult)) {
            return -2;
        }

        SendNewsNotification::dispatch($newsId)->onQueue('panel');

        return json_encode(true);
    }
}
