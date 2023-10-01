<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 8.05.2018
 * Time: 23:22
 */

namespace App\Http\Controllers;

use App\Jobs\DeleteDeviceInfoJob;
use App\Jobs\SaveDeviceInfoJob;
use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Queue;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class DeviceInfoController extends Controller
{
    public function saveDeviceInfo(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'deviceToken' => 'required|string',
            'osType' => 'required|integer|min:1|max:2' // android: 1, ios: 2
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

        $currentTime = Carbon::now()->toDateTimeString();

        Queue::push(new SaveDeviceInfoJob(Auth::id(), Input::get('deviceToken'), Input::get('osType'), app('translator')->getLocale(), $currentTime));

        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }

    public function deleteDeviceInfo(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'deviceToken' => 'required|string',
            'osType' => 'required|integer|min:1|max:2' // android: 1, ios: 2
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

        Queue::push(new DeleteDeviceInfoJob(Auth::id(), Input::get('deviceToken'), Input::get('osType')));

        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }
}