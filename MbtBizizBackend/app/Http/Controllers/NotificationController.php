<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 9.05.2018
 * Time: 23:48
 */

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class NotificationController extends Controller
{
    public function notificationList(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'firstDate' => 'date',
            'lastDate' => 'date'
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

        $notificationListResult = DB::select('call getNotificationList(?, ?, ?, ?)', [Auth::id(), Input::get('firstDate'), Input::get('lastDate'), app('translator')->getLocale()]);

        return response()->json([
            'statusCode' => 200,
            'responseData' => $this->_getNotificationList($notificationListResult),
            'errorMessage' => null
        ]);
    }

    private function _getNotificationList($notificationListResult)
    {
        $notificationList = [];

        foreach ($notificationListResult as $item) {
            $notificationList[] = [
                'id' => $item->id,
                'newsId' => $item->news_id,
                'seen' => $item->seen,
                'type' => $item->type,
                'listText' => $item->list_text,
                'discountType' => $item->discount_type,
                'image' => $item->image,
                'notificationTime' => $item->created_at
            ];
        }

        return $notificationList;
    }

    public function deleteNotification(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'notificationId' => 'required|integer'
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

        $notificationId = Input::get('notificationId');
        try {
            $affectedRowCount = DB::update("update user_notifications set deleted = 1 where id = ?", [$notificationId]);
            if ($affectedRowCount == null || $affectedRowCount == 0) {
                return response()->json([
                    'statusCode' => 400,
                    'responseData' => null,
                    'errorMessage' => null
                ]);
            }

            return response()->json([
                'statusCode' => 200,
                'responseData' => null,
                'errorMessage' => null
            ]);
        } catch (\Exception $exception) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => null
            ]);
        }
    }

    public function notificationBadgeCount()
    {
        $badgeCount = 0;
        $badgeCountResult = $this->getFirstItemFromDb("select count(id) as count
                                                                from user_notifications
                                                                where user_id = ? and seen = 0 and deleted = 0", [Auth::id()]);
        if ($badgeCountResult != null && $badgeCountResult->count != null) {
            $badgeCount = $badgeCountResult->count;
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => $badgeCount,
            'errorMessage' => null
        ]);
    }
}