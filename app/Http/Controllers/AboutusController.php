<?php
/**
 * Created by
 * User: Abdullah.Soylemez
 * Date: 22.6.2017
 * Time: 11:52
 */

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;

class AboutusController extends Controller
{
    private function getAboutUsContent(string $fileName): string
    {
        $publicDisk = Storage::disk('public');

        if ($publicDisk->exists($fileName)) {
            return $publicDisk->get($fileName);
        }

        $fallbackPath = storage_path('app/deleted/' . $fileName);
        if (is_file($fallbackPath)) {
            return file_get_contents($fallbackPath);
        }

        return '';
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
        * @return \Illuminate\Contracts\View\View|\Illuminate\View\View
     */
    public function index()
    {
        $aboutUsTRHtml = $this->getAboutUsContent('aboutusTR.html');
        $aboutUsENHtml = $this->getAboutUsContent('aboutusEN.html');

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

        Storage::disk('public')->put('aboutusTR.html', $request->input('aboutusTRHtml'));
        Storage::disk('public')->put('aboutusEN.html', $request->input('aboutusENHtml'));
       
        return view('home');
    }

}