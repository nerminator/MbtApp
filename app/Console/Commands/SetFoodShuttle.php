<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 20.05.2018
 * Time: 16:15
 */

namespace App\Console\Commands;


use Rap2hpoutre\FastExcel\FastExcel;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Log;

class SetFoodShuttle extends Command
{
    protected $name = 'set:food_shuttle';

    public function handle()
    {
        Log::info("set:food_shuttle tr started.");
        $this->_setFoodShuttle('tr');
        Log::info("set:food_shuttle tr finished.");
        Log::info("set:food_shuttle en started.");
        $this->_setFoodShuttle('en');
        Log::info("set:food_shuttle en finished.");

        return 'ok';
    }

    private function _setFoodShuttle($lang)
    {
        $shuttleInfo = [
            'header' => Lang::get('lang.TXT_DINING_HALL_SHUTTLE_INFO_HEADER', [], $lang),
            'shuttleList' => []
        ];
        $filePath = "/var/www/html/bizizFiles/food/Yemek_Servis.xlsx";
        if (!file_exists($filePath)) {
            Log::info("set:food_shuttle $lang file not found.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için yemek servisleri $lang dili için güncellenemedi!");
            return;
        }
        $collection = (new FastExcel)->import($filePath);
        foreach ($collection as $item) {
            $shuttleInfo['shuttleList'][] = [
                'location' => $item['LOKASYON'],
                'timeList' => [
                    ['name' => Lang::get('lang.TXT_DINING_HALL_SHUTTLE_TYPE_LUNCH', [], $lang), 'timeText' => $item['ÖĞLE']],
                    ['name' => Lang::get('lang.TXT_DINING_HALL_SHUTTLE_TYPE_DINNER', [], $lang), 'timeText' => $item['AKŞAM']]
                ]
            ];
        }

        Cache::forever("diningHallShuttleInfo_$lang", $shuttleInfo);
        //TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak yemek servisleri $lang dili için başarıyla güncellendi.");
    }
}