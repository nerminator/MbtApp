<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 8.05.2018
 * Time: 23:39
 */

namespace App\Jobs;


use Illuminate\Support\Facades\DB;

class SaveDeviceInfoJob extends Job
{
    private $userId, $deviceToken, $osType, $languageCode, $currentTime;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    public function __construct($userId, $deviceToken, $osType, $languageCode, $currentTime)
    {
        $this->userId = $userId;
        $this->deviceToken = $deviceToken;
        $this->osType = $osType; // android: 1, ios: 2
        $this->languageCode = $languageCode;
        $this->currentTime = $currentTime;
    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        if ($this->deviceToken != null && $this->osType != null) {
            $this->_saveUserDeviceInfo();
        }
    }

    private function _getFirstItemFromDb($sqlQuery, $params = [])
    {
        $dataResult = DB::select($sqlQuery, $params);
        if (count($dataResult) <= 0) // data not exist
        {
            return null;
        }

        return $dataResult[0];
    }

    private function _saveUserDeviceInfo()
    {
        // check if user exists
        $userExists = DB::table('users')->where('id', $this->userId)->exists();
        if (!$userExists) return;

        // check if user device token exists
        $userDeviceInfoId = DB::table('user_devices')
                ->where('user_id', $this->userId)
                ->where('os_type', $this->osType)
                ->orderByDesc('updated_at')
                ->value('id'); 

        if (!$userDeviceInfoId) {
        // insert
            DB::table('user_devices')->insert([
                'user_id' => $this->userId, 'device_token' => $this->deviceToken,
                'os_type' => $this->osType, 'language_code' => $this->languageCode,
                'created_at' => $this->currentTime, 'updated_at' => $this->currentTime
            ]);
        } else {
            DB::table('user_devices')->where('id', $userDeviceInfoId)->update([
                'device_token' => $this->deviceToken,
                'language_code' => $this->languageCode,
                'updated_at' => $this->currentTime
            ]);
        }

    }
}