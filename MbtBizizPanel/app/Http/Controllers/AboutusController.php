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

class AboutusController extends Controller
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
        $projectUrl = $this->getProjectUrl();
        $aboutUsTRHtml = file_get_contents("$projectUrl/storage/aboutusTR.html");
        $aboutUsENHtml = file_get_contents("$projectUrl/storage/aboutusEN.html");
        return view('aboutus')->with('contentTR', $aboutUsTRHtml)->with('contentEN', $aboutUsENHtml);
    }
    public function updateAboutus(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'aboutusTRHtml' => 'required|string|max:10000',
            'aboutusENHtml' => 'required|string|max:10000'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return 'Beklenmedik bir hata oluştu. Hata kodu: 16';
        }

        Storage::put("aboutusTR.html", $request->Input('aboutusTRHtml'), 'public');
        Storage::put("aboutusEN.html", $request->Input('aboutusENHtml'), 'public');
       
        return view('home');
    }

}