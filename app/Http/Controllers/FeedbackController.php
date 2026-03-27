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
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Log;

class FeedbackController extends Controller
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

    public function index()
    {
        $list = $this->getFeedbacksForOffset(0);
        $emails = Redis::get('feedbackEmail');
        return view('feedback')->with('feedbacks', $list)->with('emails', $emails);
    }

    public function updateEmails(Request $request)
    {
        $success = true;
        $errorMessage = '';

        //region Controls
        $validator = Validator::make($request->all(), [
          'emails' => 'required|string|min:3|max:1000',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return response()->json([
                'success' => false,
                'errorMessage' => "Parameter error"
            ]); 
        }
        //endregion

        $newValue = $request->input('emails'); 
        $myArray = explode(',', $newValue);
        
        foreach ($myArray as $email){   
            //check email format
            if (!filter_var(trim($email), FILTER_VALIDATE_EMAIL)) {
                // invalid emailaddress
                $success = false;
                $errorMessage = $errorMessage .'Email format problem for '.$email;
            }
        }
        if ($success) {
            $emails = Redis::set('feedbackEmail', $newValue);
        }

        return response()->json([
            'success' => $success,
            'errorMessage' => $errorMessage
        ]);
    }
    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function getFeedbacks(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
          'offset' => 'required|integer|min:0',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
    
        $offset = $request->input('offset'); 
        $list = $this->getFeedbacksForOffset($offset);
        return json_encode($list);
    }

    public static function getFeedbacksForOffset($offset)
    {
      $results = DB::select("select f.*,  (select name_surname from users where id=f.user_id) as userName
              from app_feedback f
              order by f.id desc LIMIT ?, 20", [$offset] );

      $list = [];
      foreach ($results as $item) {
          $list[] = [
              'id' => $item->id,
              'user' => $item->userName,
              'text' => $item->text,
              'created_at' => $item->created_at
          ];
      } 
      return $list;
    }

}