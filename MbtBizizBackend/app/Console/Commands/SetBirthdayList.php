<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 3.06.2018
 * Time: 23:31
 */

namespace App\Console\Commands;


use Carbon\Carbon;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SetBirthdayList extends Command
{
    protected $name = 'set:birthday_list';

    public function handle()
    {
        Log::info("set:birthday_list started.");
        $this->_setBirthdayList();
        Log::info("set:birthday_list finished.");
        TelegramChannelService::sendMessage("BİLGİLENDİRME: Doğum günü listesi başarılı bir şekilde ayarlandı!");

        return 'ok';
    }

    private function _setBirthdayList()
    {
        $now = Carbon::now();
        $birthdayList = DB::select('select name_surname as name, expense_center as title from users where birthday = ? and status = 1', [$now->format('d.m')]);

        Cache::put($now->toDateString() . "_birthday_list_count", count($birthdayList), 10080);
        Cache::put($now->toDateString() . "_birthday_list", $birthdayList, 10080);
    }
}