<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 23.05.2018
 * Time: 22:40
 */

namespace App\Http\Controllers;


use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class SettingController extends Controller
{
    const
        ACTIVITY = 1,
        DISCOUNT = 2/*,
        LEAVE = 3,
        PASSING = 4*/;

    public function notificationSettings()
    {
//        $notificationSettingResult = DB::select('select uns.activity, uns.discount, uns.leave, uns.passing from user_notification_settings uns where user_id = ?', [Auth::id()]);
        $notificationSettingResult = DB::select('select uns.activity, uns.discount from user_notification_settings uns where user_id = ?', [Auth::id()]);
        if ($notificationSettingResult == null || count($notificationSettingResult) == 0) {
            DB::table('user_notification_settings')->insert(['user_id' => Auth::id()]);
            $responseData = ['notificationSettingList' => $this->_createNotificationSettingList(null)];
        } else {
            $responseData = ['notificationSettingList' => $this->_createNotificationSettingList($notificationSettingResult[0])];
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => $responseData,
            'errorMessage' => null
        ]);
    }

    private function _createNotificationSettingList($notificationSettings)
    {
        return [
            [
                'type' => self::ACTIVITY,
                'title' => Lang::get('lang.TXT_NOTIFICATION_SETTINGS_ACTIVITY_TITLE'),
                'description' => Lang::get('lang.TXT_NOTIFICATION_SETTINGS_ACTIVITY_DESCRIPTION'),
                'value' => is_null($notificationSettings) ? 1 : $notificationSettings->activity
            ],
            [
                'type' => self::DISCOUNT,
                'title' => Lang::get('lang.TXT_NOTIFICATION_SETTINGS_DISCOUNT_TITLE'),
                'description' => Lang::get('lang.TXT_NOTIFICATION_SETTINGS_DISCOUNT_DESCRIPTION'),
                'value' => is_null($notificationSettings) ? 1 : $notificationSettings->discount
            ],
            /*[
                'type' => self::LEAVE,
                'title' => "Veda Bildirimleri",
                'description' => "Veda bildirimleri açıklaması",
                'value' => is_null($notificationSettings) ? 1 : $notificationSettings->leave
            ],
            [
                'type' => self::PASSING,
                'title' => "Vefat Bildirimleri",
                'description' => "Vefat bildirimleri açıklaması",
                'value' => is_null($notificationSettings) ? 1 : $notificationSettings->passing
            ]*/
        ];
    }

    public function changeNotificationSetting(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'type' => 'required|integer|min:1|max:2',
            'value' => 'required|boolean'
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

        $type = $request->input('type');
        $value = $request->input('value');

        switch ($type)
        {
            case self::ACTIVITY:
                $affectedRowCount = DB::update('update user_notification_settings uns set uns.activity = ? where user_id = ?', [$value, Auth::id()]);
                break;
            case self::DISCOUNT:
                $affectedRowCount = DB::update('update user_notification_settings uns set uns.discount = ? where user_id = ?', [$value, Auth::id()]);
                break;
            /*case self::LEAVE:
                $affectedRowCount = DB::update('update user_notification_settings uns set uns.leave = ? where user_id = ?', [$value, Auth::id()]);
                break;
            case self::PASSING:
                $affectedRowCount = DB::update('update user_notification_settings uns set uns.passing = ? where user_id = ?', [$value, Auth::id()]);
                break;*/
            default:
                $affectedRowCount = 0;
                break;
        }

        return response()->json([
            'statusCode' => $affectedRowCount > 0 ? 200 : 400,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }
}