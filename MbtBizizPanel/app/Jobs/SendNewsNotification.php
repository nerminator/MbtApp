<?php

namespace App\Jobs;

use App\Constants;
use Carbon\Carbon;
use Edujugon\PushNotification\PushNotification;
use Illuminate\Bus\Queueable;
use Illuminate\Queue\SerializesModels;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SendNewsNotification implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    private $newsId, $news, $userIdList, $userIdListForPN;

    /**
     * The number of times the job may be attempted.
     *
     * @var int
     */
    public $tries = 1;

    /**
     * The number of seconds the job can run before timing out.
     *
     * @var int
     */
    public $timeout = 18000;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    public function __construct($newsId)
    {
        $this->newsId = $newsId;
    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        $this->_setNews();
        if (!empty($this->news)) {
            $this->_setUserIdList();
            if (count($this->userIdList) > 0) {
                $this->_saveNotification();
                $this->_sendPushNotification();
            }
        }
    }

    private function _getFirstItemFromDb($sqlQuery, $params = [])
    {
        $dataResult = DB::select($sqlQuery, $params);
        if ($dataResult == null || count($dataResult) <= 0) {
            return null;
        }

        return $dataResult[0];
    }

    private function _setNews()
    {
        $this->news = DB::table('news')->where('id', $this->newsId)->where('status', Constants::STATUS_ACTIVE)->first();
    }

    private function _setUserIdList()
    {
        $companyLocationIdListResult = $this->_getFirstItemFromDb("select group_concat(company_location_id) as result from news_company_location where news_id = ?", [$this->newsId]);
        $employeeLocationIdListResult = $this->_getFirstItemFromDb("select group_concat(employee_location_id) as result from news_employee_location where news_id = ?", [$this->newsId]);

        $sqlQuery = "select id from users where status = 1";
        if (!is_null($this->news->employee_type)) {
            $sqlQuery .= " and type = " . $this->news->employee_type;
        }
        if (!is_null($companyLocationIdListResult) && !is_null($companyLocationIdListResult->result)) {
            $sqlQuery .= " and company_location_id in (" . $companyLocationIdListResult->result . ")";
        }
        if (!is_null($employeeLocationIdListResult) && !is_null($employeeLocationIdListResult->result)) {
            $sqlQuery .= " and employee_location_id in (" . $employeeLocationIdListResult->result . ")";
        }

        $userListResult = DB::select($sqlQuery);

        $this->userIdList = array_map(function ($ar) {
            return $ar->id;
        }, $userListResult);
    }

    private function _saveNotification()
    {
        $now = Carbon::now();
        $userNotificationList = [];
        foreach ($this->userIdList as $userId) {
            $userNotificationList[] = [
                'user_id' => $userId,
                'news_id' => $this->newsId,
                'seen' => 0,
                'deleted' => 0,
                'created_at' => $now
            ];
        }

        foreach (collect($userNotificationList)->chunk(100)->toArray() as $list) {
            DB::table('user_notifications')->insert($list);
        }
    }

    private function _sendPushNotification()
    {
        $this->userIdListForPN = $this->userIdList;
        if (in_array($this->news->type, [Constants::NEWS_TYPE_ACTIVITY, Constants::NEWS_TYPE_DISCOUNT, Constants::NEWS_TYPE_LEAVE, Constants::NEWS_TYPE_PASSING])) {
            $this->_setUserListForPushNotification();
        }

        if (count($this->userIdListForPN) > 0) {
            $userDeviceList = DB::table('user_devices')->whereIn('user_id', $this->userIdListForPN)->get(['device_token', 'os_type', 'language_code']);
            if ($userDeviceList != null && count($userDeviceList) > 0) {
                $androidDeviceTokenList = [];
                $iosDeviceTokenList = [];

                foreach ($userDeviceList as $userDevice) {
                    if ($userDevice->os_type == 1) {
                        $androidDeviceTokenList[$userDevice->language_code][] = $userDevice->device_token;
                    } else {
                        $iosDeviceTokenList[$userDevice->language_code][] = $userDevice->device_token;
                    }
                }

                if (count($iosDeviceTokenList) > 0) {
                    foreach ($iosDeviceTokenList as $languageCode => $deviceTokenList) {
                        $this->_sendAPNPushNotification($deviceTokenList, $languageCode);
                    }
                }
                if (count($androidDeviceTokenList) > 0) {
                    foreach ($androidDeviceTokenList as $languageCode => $deviceTokenList) {
                        $this->_sendFCMPushNotification($deviceTokenList, $languageCode);
                    }
                }
            }
        }
    }

    private function _setUserListForPushNotification()
    {
        $userIdStrForQuery = "";
        foreach ($this->userIdList as $userId) {

            $userIdStrForQuery .= "$userId,";
        }
        $userIdStrForQuery = rtrim($userIdStrForQuery, ",");

        $sqlQuery = "select u.id
                      from users u
                      where u.id in ($userIdStrForQuery) and not exists (select uns.id from user_notification_settings uns where uns.user_id = u.id ";
        if ($this->news->type == Constants::NEWS_TYPE_ACTIVITY) {
            $sqlQuery .= "and uns.activity = 0)";
        } elseif ($this->news->type == Constants::NEWS_TYPE_DISCOUNT) {
            $sqlQuery .= "and uns.discount = 0)";
        } elseif ($this->news->type == Constants::NEWS_TYPE_LEAVE) {
            $sqlQuery .= "and uns.leave = 0)";
        } else {
            $sqlQuery .= "and uns.passing = 0)";
        }

        $userListResult = DB::select($sqlQuery);
        if ($userListResult != null && count($userListResult) > 0) {
            $notificationUserIdList = array_map(function ($ar) {
                return $ar->id;
            }, $userListResult);
            $notificationUserIdList = array_values(array_unique($notificationUserIdList, SORT_NUMERIC));
        } else {
            $notificationUserIdList = [];
        }

        $this->userIdListForPN = $notificationUserIdList;
    }

    private function _sendAPNPushNotification($deviceTokenList, $languageCode)
    {
//        echo "\n--- IOS ---\n";
        $text = $languageCode == 'tr' ? $this->news->list_text : $this->news->list_text_en;
        foreach (collect($deviceTokenList)->chunk(100)->toArray() as $list) {

	    $push = new PushNotification('apn');

            $push->setConfig(['certificate' => base_path().Constants::APN_CERTIFICATE_PATH,  'dry_run'=> false]);

            $feedback = $push->setMessage([
                    'aps' => [
                        'alert' => [
                            'title' => $text,
                            'body' => $text
                        ],
                        'sound' => 'default'
                    ],
                    'extraPayLoad' => [
                        'newsId' => intval($this->newsId),
                        'type' => intval($this->news->type),
                        'discountType' => intval($this->news->discount_type)
                    ]
                ])
                ->setDevicesToken(array_values($list))
                ->send()
                ->getFeedback();
//            print_r($feedback);
//            echo "\n";
	    Log::info("device tokens:" . json_encode(array_values($list)));
            Log::info("APN $languageCode feedback:\n" . print_r($feedback, true));
        }
    }

    private function _sendFCMPushNotification($deviceTokenList, $languageCode)
    {
//        echo "\n--- ANDROID ---\n";
        $text = $languageCode == 'tr' ? $this->news->list_text : $this->news->list_text_en;
        foreach (collect($deviceTokenList)->chunk(100)->toArray() as $list) {
            $push = new PushNotification();
            $feedback = $push->setMessage([
                    'notification' => [
                        'title' => $text,
                        'body' => $text,
                        'sound' => 'default'
                    ],
                    'data' => [
                        'newsId' => intval($this->newsId),
                        'type' => intval($this->news->type),
                        'discountType' => intval($this->news->discount_type)
                    ]
                ])
                ->setApiKey(env('FCM_API_KEY'))
                ->setDevicesToken(array_values($list))
                ->send()
                ->getFeedback();
//            print_r($feedback);
//            echo "\n";
            Log::info("device tokens:" . json_encode(array_values($list)));
            Log::info("FCM $languageCode feedback:\n" . print_r($feedback, true));
        }
    }
}
