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
use Illuminate\Support\Facades\Log;

class NewsController extends Controller
{
    private const TYPE_MAP = [
        'image' => '',
        'document' => 'documents/',
        'pdf' => 'pdf/',
    ];

    private const ALLOWED_EXTENSIONS = ['png', 'jpg', 'jpeg', 'gif', 'webp', 'pdf'];

    private const MIME_MAP = [
        'png' => 'image/png',
        'jpg' => 'image/jpeg',
        'jpeg' => 'image/jpeg',
        'gif' => 'image/gif',
        'webp' => 'image/webp',
        'pdf' => 'application/pdf',
    ];

    private function getProjectUrl(): string
    {
        if (app()->environment('production')) {
            return rtrim(config('app.panel_production_base_url'), '/');
        }

        if (app()->environment('local')) {
            return rtrim(config('app.panel_local_base_url'), '/');
        }

        return rtrim(config('app.panel_staging_base_url'), '/');
    }

    private function isAbsoluteUrl(string $value): bool
    {
        return filter_var($value, FILTER_VALIDATE_URL) !== false;
    }

    private function isStoredRelativePath(string $value): bool
    {
        return strncmp($value, 'contents/news/', 14) === 0;
    }

    private function toStoredRelativePath(string $value): string
    {
        if ($this->isStoredRelativePath($value)) {
            return ltrim($value, '/');
        }

        if (!$this->isAbsoluteUrl($value)) {
            return $value;
        }

        $urlPath = (string) parse_url($value, PHP_URL_PATH);
        $storageMarker = '/storage/';
        $storagePosition = strpos($urlPath, $storageMarker);
        if ($storagePosition !== false) {
            return ltrim(substr($urlPath, $storagePosition + strlen($storageMarker)), '/');
        }

        if (preg_match('#/(?:api/v1/)?news/media/(\d+)/(image|document|pdf)/([a-zA-Z0-9_\-.]+)$#', $urlPath, $matches) === 1) {
            $subDirectory = $matches[2] === 'image' ? '' : ($matches[2] === 'document' ? 'documents/' : 'pdf/');
            return 'contents/news/' . $matches[1] . '/' . $subDirectory . $matches[3];
        }

        return $value;
    }

    private static function parseStoredNewsMediaPath(string $stored): ?array
    {
        if (filter_var($stored, FILTER_VALIDATE_URL) !== false) {
            $urlPath = parse_url($stored, PHP_URL_PATH);
            $marker = '/storage/';
            $position = strpos((string) $urlPath, $marker);
            if ($position === false) {
                return null;
            }

            $stored = substr($urlPath, $position + strlen($marker));
        }

        $parts = explode('/', ltrim($stored, '/'));
        if (count($parts) < 4 || $parts[0] !== 'contents' || $parts[1] !== 'news') {
            return null;
        }

        $newsId = $parts[2];
        if (!ctype_digit((string) $newsId)) {
            return null;
        }

        if (count($parts) === 4) {
            $type = 'image';
            $filename = $parts[3];
        } elseif (count($parts) === 5 && in_array($parts[3], ['documents', 'pdf'], true)) {
            $type = $parts[3] === 'documents' ? 'document' : 'pdf';
            $filename = $parts[4];
        } else {
            return null;
        }

        if (!preg_match('/^[a-zA-Z0-9_\-.]+$/', $filename)) {
            return null;
        }

        return [
            'newsId' => $newsId,
            'type' => $type,
            'filename' => $filename,
        ];
    }

    public static function toDisplayUrl(?string $stored): ?string
    {
        if (empty($stored)) {
            return null;
        }

        $mediaParts = self::parseStoredNewsMediaPath($stored);
        if ($mediaParts === null) {
            return $stored;
        }

        if (app()->environment('production')) {
            $baseUrl = rtrim(config('app.panel_production_base_url'), '/');
        } elseif (app()->environment('local')) {
            $baseUrl = rtrim(config('app.panel_local_base_url'), '/');
        } else {
            $baseUrl = rtrim(config('app.panel_staging_base_url'), '/');
        }

        return sprintf(
            '%s/news/media/%s/%s/%s',
            $baseUrl,
            $mediaParts['newsId'],
            $mediaParts['type'],
            $mediaParts['filename']
        );
    }

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
   /* public function index()
    {
        $emp = $this->getEmployeeTypeList();
        $comp = $this->getCompanyList();
        $loc = $this->getLocationList();

        return view('addnews')
            ->with('emp', json_decode($emp, true))
            ->with('compL', json_decode($comp, true))
            ->with('compC', json_decode($loc, true));
    }*/

    public function addnewsEmpty()
    {
        $id = DB::table('news')->insertGetId(array());
        return redirect('editnews-'.$id);
    }
    public function addClubEmpty($loc_id)
    {
        $id = DB::table('news')->insertGetId(array('type' => 10, 'status' => 1,'loc_id'=> $loc_id));
        return redirect('editnews-'.$id);
    }

    public function serveMedia(Request $request, $newsId, $type, $filename)
    {
        $validator = Validator::make(
            ['newsId' => $newsId, 'type' => $type, 'filename' => $filename],
            [
                'newsId' => 'required|integer|min:1',
                'type' => 'required|in:image,document,pdf',
                'filename' => ['required', 'string', 'regex:/^[a-zA-Z0-9_\-.]+$/'],
            ]
        );

        if ($validator->fails()) {
            abort(400);
        }

        if (strpos($filename, '..') !== false || strpos($filename, '/') !== false || strpos($filename, "\0") !== false) {
            abort(400);
        }

        $extension = strtolower(pathinfo($filename, PATHINFO_EXTENSION));
        if (!in_array($extension, self::ALLOWED_EXTENSIONS, true)) {
            abort(400);
        }

        $relativePath = 'contents/news/' . $newsId . '/' . self::TYPE_MAP[$type] . $filename;

        $disk = null;
        foreach (['local', 'public'] as $diskName) {
            if (Storage::disk($diskName)->exists($relativePath)) {
                $disk = Storage::disk($diskName);
                break;
            }
        }

        if ($disk === null) {
            abort(404);
        }

        $stream = $disk->readStream($relativePath);
        if ($stream === false) {
            abort(404);
        }

        $mimeType = self::MIME_MAP[$extension] ?? 'application/octet-stream';
        $contentLength = $disk->size($relativePath);

        return response()->stream(function () use ($stream) {
            fpassthru($stream);
            fclose($stream);
        }, 200, [
            'Content-Type' => $mimeType,
            'Content-Length' => $contentLength,
            'Cache-Control' => 'private, max-age=3600',
            'Content-Disposition' => 'inline; filename="' . basename($filename) . '"',
        ]);
    }
    

    private function updateImages(Request $request, int $newsId, string $redirect  ){
        DB::table('news_images')->where('news_id', $newsId)->delete();
            
        $i = 0;
        $imageList = [];    
        $tmp=null;    
        $imageUrl=null;
        for ( ; $i < 10; $i++) {
            $strImg = ($i==0?'':$i);
            $imageString = $request->Input('imageString'. $strImg);
            if (!empty($imageString)) {
           
                    if ($this->isAbsoluteUrl($imageString) || $this->isStoredRelativePath($imageString)) { // already stored
                        $imageUrl = $this->toStoredRelativePath($imageString);
                    } else {
                        $base64Str = substr($imageString, strpos($imageString, ",") + 1);
                        if ($this->getBase64ImageSize($base64Str) > 1.5) {
                            return redirect($redirect)->with('status', ' File must be less than 1.5 megabytes!'); // file size error
                        }
                        $image = base64_decode($base64Str);
    
                        $imagePath = "contents/news/$newsId/" . uniqid() . uniqid() . uniqid() . ".png";
                        Storage::disk('local')->put($imagePath, $image);
                        $imageUrl = $imagePath; // relative path stored in DB
                    }

                    if ($i==0) {
                        $tmp=$imageUrl ;
                    }
                    $imageList[] =['news_id' => $newsId , 'image' => $imageUrl];
       
            } else{
                $tmp=null;
                break;
            }
        }    


        if ($imageList!=[]){
            DB::table('news')->where('id', $newsId)->update(['image' => $tmp]);
            DB::table('news_images')->where('news_id', $newsId)->delete();
            DB::table('news_images')->insert($imageList);
        }
    }
    private function updatePdfFiles(Request $request, int $newsId, string $redirect  ){
        DB::table('news_pdf_files')->where('news_id', $newsId)->delete();
            
        $i = 0;
        $list = [];    
        $tmp=null;    
        $url=null;
        for ( ; $i < 10; $i++) {
            $str = ($i==0?'':$i);
            $pdfFile = $request->Input('pdfFile'. $str);
            if (!empty($pdfFile)) {
           
                    if ($this->isAbsoluteUrl($pdfFile) || $this->isStoredRelativePath($pdfFile)) { // already stored
                        $url = $this->toStoredRelativePath($pdfFile);
                    } else {
                        if ($this->getFileSize($pdfFile) > 1.5) {
                            return redirect($redirect)->with('status', ' File must be less than 1.5 megabytes!'); // file size error
                        }

                        $path = "contents/news/$newsId/pdf/" . uniqid() . uniqid() . uniqid() . ".png";
                        Storage::disk('local')->put($path, $pdfFile);
                        $url = $path; // relative path stored in DB
                    }

                    if ($i==0) {
                        $tmp=$url ;
                    }
                    $list[] =['news_id' => $newsId , 'pdf_file' => $url];
       
            } else{
                break;
            }
        }    

        if ($list!=[]){
            DB::table('news_pdf_files')->where('news_id', $newsId)->delete();
            DB::table('news_pdf_files')->insert($list);
        }
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

        $searchText = $request->Input('searchText');
        $rowOffset = 20 * ($request->Input('pageNumber') - 1);

        $sqlQuery = "select n.id, n.title, n.type, n.discount_type, n.start_time, n.status, n.created_at,
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
							) as location_names,
							(
                                select ni.image
                                from news_images ni
                                where ni.news_id = n.id
                                LIMIT 1
                              ) as image                            
                    from news n
                    where n.status <> 0 and n.type!=10";

        if (!empty($searchText)) {
            $sqlQuery .= " and (n.title like ? ) ";
        }

        if ($request->Input('sortType') == Constants::SORT_TYPE_CREATED_AT) {
            $sqlQuery .= " order by n.id desc";
        } else {
            $sqlQuery .= " order by n.start_time desc";
        }

        $sqlQuery .= " limit 20 offset ?";

        if (!empty($searchText)) {
            $searchValue = '%' . $searchText . '%';
            $params = array_fill(0, 1, $searchValue);
            $params[] = $rowOffset;
            $newsList = DB::select($sqlQuery, $params);
        } else {
            $newsList = DB::select($sqlQuery, [$rowOffset]);
        }

        foreach ($newsList as $item) {
            if (isset($item->image)) {
                $item->image = self::toDisplayUrl($item->image);
            }
        }

        /*if ($request->Input('sortType') == Constants::SORT_TYPE_CREATED_AT) {
            if (!empty($searchText)) {
                $searchValue = '%' . $searchText . '%';
                $newsList = DB::table('news')->where('status', '<>', 0)->where(function ($query) use ($searchValue) {
                    $query->where('title', 'like', $searchValue)->orWhere('title_en', 'like', $searchValue)
                        ->orWhere('text', 'like', $searchValue)->orWhere('text_en', 'like', $searchValue) ;
                })->orderByDesc('id')->offset($rowOffset)->limit(20)->get();
            } else {
                $newsList = DB::table('news')->where('status', '<>', 0)->orderByDesc('id')->offset($rowOffset)->limit(20)->get();
            }
        } else {
            if (!empty($searchText)) {
                $searchValue = '%' . $searchText . '%';
                $newsList = DB::table('news')->where('status', '<>', 0)->where(function ($query) use ($searchValue) {
                    $query->where('title', 'like', $searchValue)->orWhere('text', 'like', $searchValue)
                        ->orWhere('text', 'like', $searchValue)->orWhere('text_en', 'like', $searchValue) );
                })->orderByDesc('start_time')->offset($rowOffset)->limit(20)->get();
            } else {
                $newsList = DB::table('news')->where('status', '<>', 0)->orderByDesc('start_time')->offset($rowOffset)->limit(20)->get();
            }
        }*/
	//Log::info($newsList);
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

        return json_encode( $this->getNews($request->Input('id')) );
    }
    public static function getNews(int $newsId)
    {
        $newsResult = DB::select("select n.*,
                                                              (
                                                                select group_concat(ncl.id)
                                                                from news_company_location ncl
                                                                where ncl.news_id = n.id
                                                              ) as company_ids,
                                                              (
                                                                select group_concat(nel.id)
                                                                from news_employee_location nel
                                                                where nel.news_id = n.id
                                                              ) as location_ids,
                                                              (
                                                                select group_concat(ni.image)
                                                                from news_images ni
                                                                where ni.news_id = n.id
                                                              ) as images,
                                                              (
                                                                select concat( '[', group_concat('{\"id\":',np.id,',\"pdf\":\"',np.pdf_file,'\",\"name\":\"',np.pdf_name,'\"}'), ']')
                                                                from news_pdf_files np
                                                                where np.news_id = n.id
                                                              ) as pdfs,
                                                              (
                                                                select count(code) 
                                                                from news_discount_codes ndc
                                                                where ndc.news_id=n.id 
                                                               ) as codeCount, 
                                                              (
                                                                select count(code) 
                                                                from news_discount_codes ndc
                                                                where ndc.user_id!=0 and ndc.news_id=n.id 
                                                                ) as usedCodeCount                                                                

                                                          from news n
                                                          where id = ?", [$newsId]);

        $news = $newsResult[0];

        if (!empty($news->image)) {
            $news->image = self::toDisplayUrl($news->image);
        }

        if (!empty($news->images)) {
            $images = array_filter(explode(',', $news->images));
            $images = array_map([self::class, 'toDisplayUrl'], $images);
            $news->images = implode(',', $images);
        }

        if (!empty($news->pdfs)) {
            $pdfs = json_decode($news->pdfs);
            if (is_array($pdfs)) {
                foreach ($pdfs as $pdf) {
                    if (isset($pdf->pdf)) {
                        $pdf->pdf = self::toDisplayUrl($pdf->pdf);
                    }
                }
                $news->pdfs = json_encode($pdfs);
            }
        }

        return $news;
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

    public function edit(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'title' => 'nullable|string|max:150',
            'titleEn' => 'nullable|string|max:150',
            'text' => 'nullable|string|max:10000',
            'textEn' => 'nullable|string|max:10000',
            'imageString' => 'nullable|string',
            'imageString1' => 'nullable|string',            
            'imageString2' => 'nullable|string',  
            'imageString3' => 'nullable|string',  
            'imageString4' => 'nullable|string',  
            'imageString5' => 'nullable|string',  
            'imageString6' => 'nullable|string',  
            'imageString7' => 'nullable|string',                                                                          
            'imageString8' => 'nullable|string',                                                                          
            'imageString9' => 'nullable|string', 
            'url' => 'nullable|url',
            'phone' => 'nullable|numeric',
            'type' => 'integer|min:1|max:10',
            'discountType' => 'required_if:type,==,3|integer|min:1|max:14',
            'startTime' => 'required|date',
            'endTime' => 'nullable|date',
            'employeeType' => 'nullable|integer|min:1|max:2',
            'companyIdList' => 'nullable|array',
            'companyIdList.*' => 'string',
            'locationIdList' => 'nullable|array',
            'locationIdList.*' => 'string',
            'discountCodeType' => 'required_if:type,==,3|integer|min:0|max:2'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return json_encode( [
                'statusCode' => 200,
                'responseData' => -1,
                'errorMessage' => 'Beklenmedik bir hata oluştu. Hata kodu: 12'
            ] );
        } else if ( $request->Input('type') == 3 && $request->Input('discountCodeType') == 2 ){ // discount and one code for each
            //then we need at least one discount code loaded
            $resultCode = DB::select("select COUNT(code) as countCode from news_discount_codes where news_id = ?", [$request->Input('id')]);
            if ($resultCode[0]->countCode == 0){
                return json_encode( [
                    'statusCode' => 200,
                    'responseData' => -1,
                    'errorMessage' => 'En az bir adet indirim kodu girmelisiniz'
                ] );
            }
        }else if ( $request->Input('type') == 3 && $request->Input('discountCodeType') == 1 ){ // discount and one code for all
            //then we need the one code
             if ($request->Input('oneForAllCodeInput') == NULL){
                return json_encode( [
                    'statusCode' => 200,
                    'responseData' => -1,
                    'errorMessage' => 'Indirim Kodunu girmelisiniz'
                ] );
            }
        }
        //endregion

        $newsId = $request->Input('id');
        $discountCodeForAll = $request->Input('oneForAllCodeInput');
        if ( $request->Input('type') == 3 && $request->Input('discountCodeType') != 1 ){
            $discountCodeForAll = null;
        }

        DB::table('news')->where('id', $newsId)->update([
            'title' => $request->Input('title'),
            'title_en' => $request->Input('titleEn'),
            'text' => $request->Input('text'),
            'text_en' => $request->Input('textEn'),
            'url' => $request->Input('url'),
            'phone' => $request->Input('phone'),
            'type' => $request->Input('type'),
            'discount_type' => $request->Input('discountType'),
            'start_time' => $request->Input('startTime'),
            'end_time' => $request->Input('endTime'),
            'employee_type' => $request->Input('employeeType'),
            'updated_at' => Carbon::now(),
            'discount_code_type' => $request->Input('discountCodeType'),
            'discount_code_all' => $discountCodeForAll
        ]);

        DB::table('news_company_location')->where('news_id', $newsId)->delete();
        $companyIdList = $request->Input('companyIdList');
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
        $locationIdList = $request->Input('locationIdList');
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

        $this->updateImages($request, $newsId, 'editnews');
        //$this->updatePdfFiles($newsId, 'editnews');

        if ( $request->Input('type') == 3 && $request->Input('discountCodeType') != 2 ){ // no need for a list of codes
            //delete all codes
            DB::delete("delete from news_discount_codes where news_id = ?", [$newsId]);
        }

        return json_encode( [
            'statusCode' => 200,
            'responseData' => 0,
            'errorMessage' => null
        ] );
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

    private function getFileSize($content)
    {
        try {
            $size_in_bytes = (int)(strlen($content) * 3 / 4);
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

        $newsId = $request->Input('id');
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

        $newsId = $request->Input('id');
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

        $newsId = $request->Input('id');
        $affectedRowCount = DB::table('news')->where('id', $newsId)->delete();
        DeleteNewsNotification::dispatch($newsId);

        return json_encode($affectedRowCount > 0);
    }

   /* public function deleteImage(Request $request)
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

        $affectedRowCount = DB::table('news')->where('id', $request->Input('id'))->update(['image' => null, 'updated_at' => Carbon::now()]);

        return json_encode($affectedRowCount > 0);
    }*/

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

        $newsId = $request->Input('id');
        $newsResult = DB::table('news')->where('id', $newsId)->where('status', Constants::STATUS_ACTIVE)->first();
        if (empty($newsResult)) {
            return -2;
        }

        SendNewsNotification::dispatch($newsId)->onQueue('panel');

        return json_encode(true);
    }
}

