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
use Illuminate\Support\Facades\Log;

class DiscountCodesController extends Controller
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

    public function getDiscountCodesInfo(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'newsId' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }

        $newsId = $request->Input('newsId');

        $resultCode = DB::select("select COUNT(code) as countCode from news_discount_codes where news_id = ?", [$newsId]);
        $resultUsedCode = DB::select("select COUNT(code) as countUsed from news_discount_codes where user_id!=0 and news_id = ?", [$newsId]);

        return json_encode([
            'codeCount' => $resultCode->countCode,
            'codeUsed' => $resultUsedCode->countUsed,
        ]);
    }

    public function addDiscountCodes(Request $request)
    {
        $validator = Validator::make($request->all(), [
            'newsId' => 'required|integer|min:1',
            'discountCodes' => 'required|array|max:10000',
            'discountCodes.*'  => 'required|string|distinct|min:3|max:250'
        ]);


        if ($validator->fails()) // missing parameters
        {
            return -1;
        }

      	$newsId =  $request->Input('newsId');
        $discountCodes = $request->input('discountCodes');


        $newsDiscountCodeList = array();
        foreach ($discountCodes as $code)
        {
            $newsDiscountCodeList[] = [
                'news_id' => $newsId,
                'code' => $code
            ];
        }

        DB::delete("delete from news_discount_codes where news_id = ?", [$newsId]);

        if (count($newsDiscountCodeList) > 0)
        {
            DB::table('news_discount_codes')->insert($newsDiscountCodeList);
            DB::table('news')->where('id', $newsId)->update(['discount_code_type' => 2, 'updated_at' => Carbon::now()]);
        }

        return 1;  
    }

    public function deleteDiscountCodes(Request $request)
    {
        $validator = Validator::make($request->all(), [
          'newsId' => 'required|integer|min:1'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }

        $newsId =  $request->Input('newsId');
        DB::delete("delete from news_discount_codes where news_id = ?", [$newsId]);
        return 1;
    }
}