<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 20.05.2018
 * Time: 13:51
 */

namespace App\Console\Commands;


use Carbon\Carbon;
use Rap2hpoutre\FastExcel\FastExcel;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Log;

class SetFoodMenu extends Command
{
    protected $name = 'set:food_menu';

    public function handle()
    {
        Log::info("set:food_menu tr started.");
        $this->_setFoodMenu('tr');
        Log::info("set:food_menu tr finished.");
        Log::info("set:food_menu en started.");
        $this->_setFoodMenu('en');
        Log::info("set:food_menu en finished.");

        return 'ok';
    }

    private function _setFoodMenu($lang)
    {
        if ($lang == 'en') {
            app('translator')->setLocale('en');
            setlocale(LC_TIME, null);
            \Carbon\Carbon::setLocale('en');
        }

        $carbonDate = Carbon::now();
//        $fileName = $carbonDate->startOfWeek()->format('d.m.Y') . "-" . $carbonDate->endOfWeek()->format('d.m.Y');
        $fileName = "Yemek_" . $carbonDate->startOfWeek()->format('d.m.Y');
        $filePath = "/var/www/html/bizizFiles/food/$fileName.xlsx";
        if (!file_exists($filePath)) {
            Log::info("set:food_menu $lang file not found.");
            TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için yemek menüsü $lang dili için güncellenemedi!");
            return;
        }

        $foodInfo = [];
        $collection = (new FastExcel)->import($filePath);
        foreach ($collection as $item) {
            if (gettype($item['TARİH']) != "string") {
                $itemCarbonDate = Carbon::createFromFormat('d.m.Y', $item['TARİH']->format('d.m.Y'));
                $itemIndex = from($foodInfo)->findIndex(function ($foodInfoItem) use ($itemCarbonDate) {
                    return $foodInfoItem["dateText"] == $itemCarbonDate->format('d.m.Y');
                });

                if (!is_null($itemIndex)) {
                    $foodInfo[$itemIndex]['foodList'][] = [
                        'name' => $item['YEMEK'],
                        'calorie' => intval($item['KALORİ']),
                        'detailList' => [
                            ['name' => Lang::get('lang.TXT_FOOD_DETAIL_PROTEIN', [], $lang), 'value' => intval($item['PROTEİN'])],
                            ['name' => Lang::get('lang.TXT_FOOD_DETAIL_FAT', [], $lang), 'value' => intval($item['YAĞ'])],
                            ['name' => Lang::get('lang.TXT_FOOD_DETAIL_CARBOHYDRATE', [], $lang), 'value' => intval($item['K.HİDRAT'])]
                        ],
                        'info' => $item['AÇIKLAMA']
                    ];
                } else {
                    $foodInfo[] = [
                        'dateTitle' => $itemCarbonDate->format('D'),
                        'dateText' => $itemCarbonDate->format('d.m.Y'),
                        'isToday' => $itemCarbonDate->isToday(),
                        'foodList' => [
                            [
                                'name' => $item['YEMEK'],
                                'calorie' => intval($item['KALORİ']),
                                'detailList' => [
                                    ['name' => Lang::get('lang.TXT_FOOD_DETAIL_PROTEIN', [], $lang), 'value' => intval($item['PROTEİN'])],
                                    ['name' => Lang::get('lang.TXT_FOOD_DETAIL_FAT', [], $lang), 'value' => intval($item['YAĞ'])],
                                    ['name' => Lang::get('lang.TXT_FOOD_DETAIL_CARBOHYDRATE', [], $lang), 'value' => intval($item['K.HİDRAT'])]
                                ],
                                'info' => $item['AÇIKLAMA']
                            ]
                        ]
                    ];
                }
            }
        }

        if (is_array($foodInfo) && count($foodInfo) > 0) {
//            $minutes = 10 * (24 * 60);
            $minutes = 6 * 60;
            Cache::put("foodInfo_$fileName" . "_$lang", $foodInfo, $minutes);
            TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak yemek menüsü $lang dili için başarıyla güncellendi.");
//            Cache::forever("foodInfo_$fileName" . "_$lang", $foodInfo);
        }
    }
}
