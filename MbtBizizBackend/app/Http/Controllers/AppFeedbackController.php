<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 21.01.2020
 * Time: 21:43
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Mail;
use App\Mail\NewFeedback;
use Illuminate\Support\Facades\Redis;

class AppFeedbackController extends Controller
{
    public function submitFeedback(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'text' => 'required|string|min:3|max:500'
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

        $user_id = Auth::user()->id;
        $text = $request->input('text');
        
        $newId = DB::table('app_feedback')->insertGetId(['user_id' => $user_id,
                                                        'text'=> $text]);

        if ($newId > 0){
            $profileResult = $this->getFirstItemFromDb("select u.name_surname, u.title,
                                                                    u.location, cl.name as company_location_name
                                                              from users u, company_locations cl
                                                              where u.company_location_id = cl.id and u.id = ? and u.status = 1", [Auth::id()]);


            $this->sendEmail([ 'userName' => $profileResult->name_surname, 'text' => $text ]);
            return response()->json([
                'statusCode' => 200,
                'responseData' => [
                ],
                'errorMessage' => null
            ]);
        } else {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Geri bildiriminiz gönderilemedi, lütfen daha sonra tekrar deneyiniz."
            ]);
        }

    }
    protected function sendEmail($someData){

        $to = [];
        
        if(Redis::exists('feedbackEmail')){
            $emails = Redis::get('feedbackEmail');
            $myArray = explode(',', $emails);
            foreach ($myArray as $email){   
                $to[] = ['email'=> trim($email) ];
            }
        };

        Mail::to($to)->send(new NewFeedback($someData));

    }
}
