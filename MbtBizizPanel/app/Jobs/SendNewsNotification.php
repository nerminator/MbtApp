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
use Google\Auth\OAuth2;

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
    $text = $languageCode == 'tr' ? $this->news->title : $this->news->title_en;
    $bundleId = 'com.daimlertruck.dtag.internal.ios.mbt.test'; // ← your app's bundle ID
    $certPath = base_path() . Constants::APN_CERTIFICATE_PATH;

    foreach (collect($deviceTokenList)->chunk(100)->toArray() as $list) {
        foreach ($list as $deviceToken) {
            $payload = json_encode([
                'aps' => [
                    'alert' => [
                        'title' => $text,
                        'body'  => $text
                    ],
                    'sound' => 'default'
                ],
                'newsId'       => intval($this->newsId),
                'type'         => intval($this->news->type),
                'discountType' => intval($this->news->discount_type)
            ]);

            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, "https://api.push.apple.com/3/device/{$deviceToken}");
            curl_setopt($ch, CURLOPT_PORT, 443);
            curl_setopt($ch, CURLOPT_HTTP_VERSION, CURL_HTTP_VERSION_2_0);
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, $payload);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_SSLCERT, $certPath);
            curl_setopt($ch, CURLOPT_SSLCERTTYPE, 'PEM');
            curl_setopt($ch, CURLOPT_HTTPHEADER, [
                "apns-topic: {$bundleId}",
                "apns-push-type: alert",
                "Content-Type: application/json"
            ]);

            $response = curl_exec($ch);
            $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            $error    = curl_error($ch);

            curl_close($ch);

            Log::info("APNs CURL to device {$deviceToken}: HTTP {$httpCode}");
            Log::info("APNs Response: {$response}");
            if ($error) {
                Log::error("APNs CURL error: {$error}");
            }
        }
    }
}

    private function old_sendAPNPushNotification($deviceTokenList, $languageCode)
    {
//        echo "\n--- IOS ---\n";
        $text = $languageCode == 'tr' ? $this->news->title : $this->news->title_en;
        foreach (collect($deviceTokenList)->chunk(100)->toArray() as $list) {

	    $push = new PushNotification('apn');

            $push->setConfig(['certificate' => base_path().Constants::APN_CERTIFICATE_PATH,  'dry_run'=> false, 'topic'       => 'com.daimlertruck.dtag.internal.ios.mbt.test' ]);

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
	  //  Log::info("device tokens:" . json_encode(array_values($list)));
          //  Log::info("APN $languageCode feedback:\n" . print_r($feedback, true));
        }
    }

private function _sendFCMPushNotification($deviceTokenList, $languageCode)
{
    $text = $languageCode == 'tr' ? $this->news->title : $this->news->title_en;

    $serviceAccountPath = base_path('app/firebase-service-account.json');
    $projectId = 'mbt-app-eb79d'; // change this

    $scopes = ['https://www.googleapis.com/auth/firebase.messaging'];
    $auth = new OAuth2([
        'audience' => 'https://oauth2.googleapis.com/token',
        'issuer' => json_decode(file_get_contents($serviceAccountPath))->client_email,
        'signingAlgorithm' => 'RS256',
        'signingKey' => json_decode(file_get_contents($serviceAccountPath))->private_key,
        'tokenCredentialUri' => 'https://oauth2.googleapis.com/token',
        'scope' => $scopes,
    ]);

    $auth->fetchAuthToken();
    $accessToken = $auth->getLastReceivedToken()['access_token'];

    foreach (collect($deviceTokenList)->chunk(100)->toArray() as $list) {
        foreach ($list as $deviceToken) {
            $payload = [
                'message' => [
                    'token' => $deviceToken,
                    'notification' => [
                        'title' => $text,
                        'body'  => $text,
                    ],
                    'data' => [
                        'newsId'       => strval($this->newsId),
                        'type'         => strval($this->news->type),
                        'discountType' => strval($this->news->discount_type),
                    ]
                ]
            ];

            $ch = curl_init();
            curl_setopt($ch, CURLOPT_URL, "https://fcm.googleapis.com/v1/projects/{$projectId}/messages:send");
            curl_setopt($ch, CURLOPT_POST, true);
            curl_setopt($ch, CURLOPT_HTTPHEADER, [
                "Authorization: Bearer {$accessToken}",
                'Content-Type: application/json'
            ]);
            curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
            curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($payload));

            $response = curl_exec($ch);
            $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
            $error = curl_error($ch);

            curl_close($ch);

            Log::info("✅ FCM v1 HTTP Code: {$httpCode}");
            Log::info("✅ FCM v1 Response: {$response}");
            if ($error) {
                Log::error("❌ FCM v1 CURL error: {$error}");
            }
        }
    }
}


   private function _old2sendFCMPushNotification($deviceTokenList, $languageCode)
{
    $fcmApiKey = env('FCM_API_KEY');
    $text = $languageCode == 'tr' ? $this->news->title : $this->news->title_en;

    foreach (collect($deviceTokenList)->chunk(100)->toArray() as $list) {
        $tokens = array_values($list);

        $payload = [
            'registration_ids' => $tokens,
            'notification' => [
                'title' => $text,
                'body'  => $text,
                'sound' => 'default'
            ],
            'data' => [
                'newsId'       => intval($this->newsId),
                'type'         => intval($this->news->type),
                'discountType' => intval($this->news->discount_type)
            ]
        ];

        $ch = curl_init();
        curl_setopt($ch, CURLOPT_URL, 'https://fcm.googleapis.com/fcm/send');
        curl_setopt($ch, CURLOPT_POST, true);
        curl_setopt($ch, CURLOPT_HTTPHEADER, [
            'Authorization: key=' . $fcmApiKey,
            'Content-Type: application/json'
        ]);
        curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
        curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($payload));

        $response = curl_exec($ch);
        $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
        $error    = curl_error($ch);

        curl_close($ch);

        Log::info("📡 FCM tokens: " . json_encode($tokens));
        Log::info("📡 FCM HTTP Code: $httpCode");
        Log::info("📡 FCM Response: $response");
        if ($error) {
            Log::error("⚠️ FCM CURL Error: $error");
        }
    }
   }

    private function _sendiOldFCMPushNotification($deviceTokenList, $languageCode)
    {
//        echo "\n--- ANDROID ---\n";
        $text = $languageCode == 'tr' ? $this->news->title : $this->news->title_en;
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
