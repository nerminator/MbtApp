<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 8.05.2018
 * Time: 23:44
 */

namespace App\Jobs;


use Illuminate\Support\Facades\DB;

class DeleteDeviceInfoJob extends Job
{
    private $userId, $deviceToken, $osType;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    public function __construct($userId, $deviceToken, $osType)
    {
        $this->userId = $userId;
        $this->deviceToken = $deviceToken;
        $this->osType = $osType; // android: 1, ios: 2
    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        if ($this->deviceToken != null && $this->osType != null)
        {
            $this->_deleteUserDeviceInfo();
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

    private function _deleteUserDeviceInfo()
    {
        // check if user notification exists
        $userDeviceInfo = $this->_getFirstItemFromDb("select id
                                                                from user_devices
                                                                where user_id = ? and device_token = ? and os_type = ?
                                                                order by updated_at desc
                                                                limit 1", [$this->userId, $this->deviceToken, $this->osType]);
        if ($userDeviceInfo != null && isset($userDeviceInfo->id))
        {
            DB::delete("delete from user_devices where id = ?", [$userDeviceInfo->id]);
        }
    }
}