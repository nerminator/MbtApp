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

    /**
    * Get the latest Yemek_xx.xx.xxxx.xlsx file
    */
   private function getLatestFoodMenuFile($directory)
   {
       $files = glob($directory . "Yemek_*.xlsx");

       if (empty($files)) {
           return null;
       }

       // Sort files by modification time (newest first)
       usort($files, function ($a, $b) {
           return filemtime($b) - filemtime($a);
       });

       // Return the latest file name
       return basename($files[0]);
    }


    private function _setFoodMenu($lang)
    {
        if ($lang == 'en') {
            app('translator')->setLocale('en');
            setlocale(LC_TIME, null);
            \Carbon\Carbon::setLocale('en');
        }

	$fileName = $this->getLatestFoodMenuFile("/var/www/html/bizizFiles/food/");
	$filePath = "/var/www/html/bizizFiles/food/$fileName";
	if (!$fileName) {
            Log::info("set:food_menu $lang file not found.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için yemek menüsü $lang dili için güncellenemedi!");
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
		    //print_r($foodInfo[$itemIndex]['foodList']);
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
            Log::info("Updated the food menu cache");
            //Log::info("First 3 items of foodInfo array: " . json_encode(array_slice($foodInfo, 0, 3), JSON_PRETTY_PRINT | JSON_UNESCAPED_UNICODE));
                $minutes = 6 * 60;
                Cache::put("foodInfo_$fileName" . "_$lang", $foodInfo, $minutes);
                //TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak yemek menüsü $lang dili için başarıyla güncellendi.");
        } else {
                Log::info("Food array is empty, couldnt update the cache");
        }
    }
}
