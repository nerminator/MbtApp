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

class DashboardController extends Controller
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
        $db = [
            'mostViewed3News' => getMostViewed3News()
        ];

        return view('dashboard')->with('db', $db);
    }

    public static function getMostViewed3News()
    {
        $mostViewed3News = [];
        $keys = Redis::keys('viewCountForNews*');
        foreach ($keys as $key) {
            $val = Redis::get($key);

            foreach ($mostViewed3News as $index=>$view) {
                if ($val > $view.viewCount) {
                    $temp = $view
                    $mostViewed3News[$index] = [
                        'id' => substr($key, 16),
                        'viewCount' => $val 
                    ]
                    //Shift the others
                    for ( $i = $index+1; $i < $mostViewed3News.count $i++) {
                        $temp2 = $mostViewed3News[$i];
                        $mostViewed3News[$i] =  $temp;
                        $temp = $temp2;
                    }
                }
            }
            
        } 
        foreach ($mostViewed3News as $index=>$view) {
           $mostViewed3News[$index]['title'] =  DB::select("select title from news where id = ?", [$mostViewed3News[$index]['id']]);
        }


        return $mostViewed3News;
    }

}