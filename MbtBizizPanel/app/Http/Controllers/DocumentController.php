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
                    'pdf_file' => $item->url
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
                $pdfFilePath = Storage::putFile("contents/news/$newsId/documents", $pdfFile, 'public');
                $projectUrl = app()->environment() == "production" ? "https://bizizapp.com/bizizPanel/public" : "http://localhost:8002";
                $pdfFileUrl = $projectUrl . "/storage/$pdfFilePath";
                $documentId = DB::table('mbtbiziz.news_pdf_files')->insertGetId([
                    'news_id' => $newsId,
                    'pdf_name' => $pdfFileName,
                    'pdf_file' => $pdfFileUrl,
                    'created_at' => $currentTime
                ]);
                $result = [
                    'id' => $documentId,
                    'url' => $pdfFileUrl
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