<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

class EditNewsController extends Controller
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
    public function index($id)
    {
        $newsData = NewsController::getNews($id); 
        $emp = NewsController::getEmployeeTypeList();
        $compL = NewsController::getLocationList();
        $compC = NewsController::getCompanyList();

        return view('editnews', ['id' => $id])
                    ->with('newsData', $newsData)
                    ->with('emp', json_decode($emp, true))
                    ->with('compL', json_decode($compL, true))
                    ->with('compC', json_decode($compC, true));
    }

}
