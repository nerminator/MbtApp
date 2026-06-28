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
use App\Services\NewsViewService;

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

        $visibleNews = $this->buildVisibleNewsConstraint('n');
        $visibilitySql = $visibleNews['sql'];
        $bindings = $visibleNews['bindings'];

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
                          where {$visibilitySql}
                                ";
        } else {
            $sqlQuery = "select n.id, n.title, n.type, n.discount_type, n.start_time, n.url, n.phone,
                                (
                                    select ni.image
                                    from news_images ni
                                    where ni.news_id = n.id
                                    LIMIT 1
                                ) as image  
                          from news n
                          where {$visibilitySql}
                                ";
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

        $visibleNews = $this->buildVisibleNewsConstraint('n');
        $visibilitySql = $visibleNews['sql'];
        $params = array_merge([$id], $visibleNews['bindings']);
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
                                                                                                                                        where n.id = ?
                                                                                                                                                    and {$visibilitySql}", $params);
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
                                                                                                                                        where n.id = ?
                                                                                                                                                    and {$visibilitySql}", $params);
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

        // Transform stored values (legacy full Panel URLs or new relative paths)
        // into authenticated Backend proxy URLs.
        $imageUrl   = $this->toProxyUrl($imageUrl);
        $imageList  = array_map([$this, 'toProxyUrl'], $imageList);

        $pdfs = null;
        if ($newsObjectResult->pdfs) {
            $pdfs = json_decode($newsObjectResult->pdfs);
            foreach ($pdfs as $pdf) {
                $pdf->pdf = $this->toProxyUrl($pdf->pdf);
            }
        }

        $newsObject = [
            'type' => $newsObjectResult->type,
            'discountType' => $newsObjectResult->discount_type,
            'image' =>  $imageUrl,
            'images' => $imageList, 
            'pdfs' => $pdfs,            
            'dateInfo' => $dateInfo,
            'title' => $newsObjectResult->title,
            'text' => $newsObjectResult->text,
            'url' => $newsObjectResult->url,
            "discountCodeType" => $newsObjectResult->discount_code_type,
            "discountCodeAll" => $newsObjectResult->discount_code_all,
        ];

        NewsViewService::increment((int) $id);


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
                'image' => $this->toProxyUrl($item->image),
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

    private function buildVisibleNewsConstraint($alias = 'n')
    {
        $user = Auth::user();
        $bindings = [];
        $clauses = [
            "{$alias}.status = 1",
            "({$alias}.end_time is null or {$alias}.end_time > now())",
        ];

        if ($user->type != null) {
            $clauses[] = "({$alias}.employee_type is null or {$alias}.employee_type = ?)";
            $bindings[] = $user->type;
        }

        $clauses[] = "(
                            (select count(ncl.id) from news_company_location ncl where ncl.news_id = {$alias}.id) = 0
                            or
                            (? in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = {$alias}.id))
                        )";
        $bindings[] = $user->company_location_id;

        $clauses[] = "(
                            (select count(nel.id) from news_employee_location nel where nel.news_id = {$alias}.id) = 0
                            or
                            (? in (select nel.employee_location_id from news_employee_location nel where nel.news_id = {$alias}.id))
                        )";
        $bindings[] = $user->employee_location_id;

        return [
            'sql' => implode("\n                                and ", $clauses),
            'bindings' => $bindings,
        ];
    }

    /**
     * Convert a stored image/document value into a time-limited HMAC-signed
     * Backend proxy URL. No Authorization header is required to fetch it —
     * the embedded signature is the proof of authorization. This keeps
     * backward compatibility with older app versions.
     *
     * Handles two stored formats:
     *   Legacy  — full Panel public URL:   https://bizi....com/.../storage/contents/news/5/foo.png
     *   New     — relative path:           contents/news/5/foo.png
     *
     * Produces: {backendUrl}/api/v1/news/media/{newsId}/{type}/{filename}?sig={hmac}&exp={epoch}
     */
    private function toProxyUrl(?string $stored): ?string
    {
        if (empty($stored)) return null;

        // If it's a full URL, extract the relative path after /storage/
        if (filter_var($stored, FILTER_VALIDATE_URL) !== false) {
            $urlPath = parse_url($stored, PHP_URL_PATH);
            $marker  = '/storage/';
            $pos     = strpos($urlPath, $marker);
            if ($pos === false) {
                // Not a Panel storage URL (e.g. an external news image) — return unchanged
                return $stored;
            }
            $relativePath = substr($urlPath, $pos + strlen($marker));
        } else {
            $relativePath = $stored;
        }

        // Expected patterns:
        //   contents/news/{newsId}/{filename}              → type = image
        //   contents/news/{newsId}/documents/{filename}   → type = document
        //   contents/news/{newsId}/pdf/{filename}         → type = pdf
        $parts = explode('/', ltrim($relativePath, '/'));
        if (count($parts) < 4 || $parts[0] !== 'contents' || $parts[1] !== 'news') {
            return $stored; // unrecognised pattern — leave unchanged
        }

        $newsId = $parts[2];
        if (!ctype_digit((string) $newsId)) return $stored;

        if (count($parts) === 4) {
            $type     = 'image';
            $filename = $parts[3];
        } elseif (count($parts) === 5 && in_array($parts[3], ['documents', 'pdf'], true)) {
            $type     = $parts[3] === 'documents' ? 'document' : 'pdf';
            $filename = $parts[4];
        } else {
            return $stored;
        }

        // Validate filename is safe before embedding in URL
        if (!preg_match('/^[a-zA-Z0-9_\-\.]+$/', $filename)) return $stored;

        // Generate a time-limited HMAC signature
        $expiry     = time() + config('media.url_expiry_seconds', 86400);
        $signingKey = config('media.signing_key', '');
        $payload    = "{$newsId}/{$type}/{$filename}/{$expiry}";
        $sig        = hash_hmac('sha256', $payload, $signingKey);

        $configuredBaseUrl = rtrim((string) config('app.url', ''), '/');
        $baseUrl = $configuredBaseUrl !== '' ? $configuredBaseUrl : rtrim(url('/'), '/');
        return "{$baseUrl}/api/v1/news/media/{$newsId}/{$type}/{$filename}?sig={$sig}&exp={$expiry}";
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

        $visibleNews = $this->buildVisibleNewsConstraint('n');
        $visibilitySql = $visibleNews['sql'];
        $accessibleDiscount = $this->getFirstItemFromDb(
            "select n.id
             from news n
             where n.id = ?
                   and n.type = 3
                   and {$visibilitySql}",
            array_merge([$newsId], $visibleNews['bindings'])
        );

        if ($accessibleDiscount == null) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => Lang::get('lang.TXT_SERVER_ERROR_NEWS_NOT_FOUND'),
                'errorMessage' => null
            ]);
        }

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
