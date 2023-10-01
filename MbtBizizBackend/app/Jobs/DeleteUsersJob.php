<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 23.07.2020
 * Time: 23:35
 */

namespace App\Jobs;


use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class DeleteUsersJob extends Job
{
    private $userIdList;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    public function __construct()
    {

    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        DB::beginTransaction();
        try {
            $userList = DB::table('users')->where('status', 0)->select(['id', 'register_number'])->get()->toArray();
            foreach ($userList as $user) {
                DB::table('user_devices')->where('user_id', $user->id)->delete();
                DB::table('user_notifications')->where('user_id', $user->id)->delete();
                DB::table('user_notification_seen_times')->where('user_id', $user->id)->delete();
                DB::table('user_notification_settings')->where('user_id', $user->id)->delete();
                DB::table('user_pin_codes')->where('user_id', $user->id)->delete();
                DB::table('working_calendar')->where('register_number', $user->register_number)->delete();
                DB::table('working_hours_info')->where('register_number', $user->register_number)->delete();
                DB::table('users')->where('id', $user->id)->delete();
            }

            if (count($userList) > 0) {
                $registerNumberList = array_map(
                    function($user) { return $user->register_number; },
                    $userList
                );
                Log::info("Deleted user register number list: " . implode(",", $registerNumberList));
            }

            DB::commit();
        } catch (\Exception $e) {
            DB::rollBack();
            Log::error("DeleteUsersJob transaction error. " . $e->getMessage());
        }
    }
}