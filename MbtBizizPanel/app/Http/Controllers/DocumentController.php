<?php
/**
 * Created by
 * User: Abdullah.Soylemez
 * Date: 22.6.2017
 * Time: 11:52
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class DocumentController extends Controller
{
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

    /**
     * Convert a stored value (relative path or legacy full URL) to a full Panel URL
     * for display in the admin UI only. Files are now stored privately.
     */
    private function toDisplayUrl(?string $stored): ?string
    {
        if (empty($stored)) return null;
        // Legacy records already have a full URL
        if (filter_var($stored, FILTER_VALIDATE_URL) !== false) {
            return $stored;
        }
        // New records store a relative path — build a temporary admin-facing URL
        // Note: this URL is only accessible to authenticated admin sessions via the
        // web server. The file is in private storage; direct web access will 404.
        // Use the Backend proxy URL pattern instead if direct download is needed.
        return $this->getProjectUrl() . '/storage/' . $stored;
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
    public function index()
    {
        return view('addnews');
    }

    public function getDocumentList(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'newsId' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }

        $newsId = $request->Input('newsId');

        $documentsResult = DB::select("select id, name, url, type from news_pdf_files where news_id = ?", [$newsId]);

        $fileList = array();

        foreach ($documentsResult as $item)
        {
                $fileList[] = [
                    'id' => $item->id,
                    'pdf_name' => $item->name,
                    'pdf_file' => $this->toDisplayUrl($item->url)
                ];
        }

        if (count($fileList) == 0 )
        {
            return 0;
        }

        return json_encode([
            'fileList' => $fileList
        ]);
    }

    public function addDocument(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'newsId' => 'required|integer|min:1',
            'pdfFile' => 'file|mimes:pdf|max:10000',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }


	    $newsId =  $request->Input('newsId');

        $currentTime = Carbon::now()->toDateTimeString();

        $pdfFile = $request->file('pdfFile');
        if ($pdfFile != null)
        {
            try
            {
                $pdfFileName = $pdfFile->getClientOriginalName();
                // Store in private (local) disk — not web-accessible
                $pdfFilePath = Storage::disk('local')->putFile("contents/news/$newsId/documents", $pdfFile);
                // Save only the relative path; the Backend API proxy will serve it with auth
                $pdfFileUrl = $pdfFilePath;
                $documentId = DB::table('mbtbiziz.news_pdf_files')->insertGetId([
                    'news_id' => $newsId,
                    'pdf_name' => $pdfFileName,
                    'pdf_file' => $pdfFileUrl,
                    'created_at' => $currentTime
                ]);
                $result = [
                    'id' => $documentId,
                    'url' => $this->toDisplayUrl($pdfFileUrl)
                ];
                return json_encode($result);
            }
            catch (\Exception $e)
            {

            }
        }

        return null;
    }

    public function deleteDocument(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'documentId' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }

        $affectedRowCount = DB::delete("delete from mbtbiziz.news_pdf_files where id = ?", [$request->Input('documentId')]);

        return json_encode($affectedRowCount > 0);
    }
}