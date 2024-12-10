<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 10.05.2018
 * Time: 22:29
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Rap2hpoutre\FastExcel\FastExcel;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Log;
class FoodMenuController extends Controller
{
    public function foodMenu()
    {
        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'foodInfo' => $this->_getFoodInfoFromCache(),
                'shuttleInfo' => $this->_getShuttleInfoFromCache(),
                'densityList' => null
//                'densityList' => [
//                    [
//                        'locationNumber' => 1, // Hoşdere ve Genel Müdürlük
//                        'locationText' => "Hoşdere",
//                        'text' => "Anlık yemekhane yoğunluğu",
//                        'percent' => 30
//                    ],
//                    [
//                        'locationNumber' => 2, // Hoşdere ve Genel Müdürlük
//                        'locationText' => "Genel Müdürlük",
//                        'text' => "Anlık yemekhane yoğunluğu",
//                        'percent' => 30
//                    ]
//                ]
            ],
            'errorMessage' => null
        ]);
    }

    private function _getFoodInfoFromCache()
    {
        $carbonDate = Carbon::now();
//        $fileName = $carbonDate->startOfWeek()->format('d.m.Y') . "-" . $carbonDate->endOfWeek()->format('d.m.Y');
        $fileName = "Yemek_" . $carbonDate->startOfWeek()->format('d.m.Y');
        $foodInfo = [];

        try {
            // Cache::rememberForever("foodInfo_$fileName" . "_" . app('translator')->getLocale(), function () use ($fileName) {
//            $minutes = 10 * (24 * 60);
            $minutes = 6 * 60;
            $foodInfo = Cache::remember("foodInfo_$fileName" . "_" . app('translator')->getLocale(), $minutes, function () use ($fileName) {
                $foodInfo = [];
                //Log::info("/var/www/html/bizizFiles/food/$fileName.xlsx");
                $collection = (new FastExcel)->import("/var/www/html/bizizFiles/food/$fileName.xlsx");
                foreach ($collection as $item) {
                    $itemCarbonDate = Carbon::createFromFormat('d.m.Y', $item['TARİH']->format('d.m.Y'));
                    $itemIndex = from($foodInfo)->findIndex(function ($foodInfoItem) use ($itemCarbonDate) {
                        return $foodInfoItem["dateText"] == $itemCarbonDate->format('d.m.Y');
                    });

                    if (!is_null($itemIndex)) {
                        $foodInfo[$itemIndex]['foodList'][] = [
                            'name' => $item['YEMEK'],
                            'calorie' => intval($item['KALORİ']),
                            'detailList' => [
                                ['name' => Lang::get('lang.TXT_FOOD_DETAIL_PROTEIN'), 'value' => intval($item['PROTEİN'])],
                                ['name' => Lang::get('lang.TXT_FOOD_DETAIL_FAT'), 'value' => intval($item['YAĞ'])],
                                ['name' => Lang::get('lang.TXT_FOOD_DETAIL_CARBOHYDRATE'), 'value' => intval($item['K.HİDRAT'])]
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
                                        ['name' => Lang::get('lang.TXT_FOOD_DETAIL_PROTEIN'), 'value' => intval($item['PROTEİN'])],
                                        ['name' => Lang::get('lang.TXT_FOOD_DETAIL_FAT'), 'value' => intval($item['YAĞ'])],
                                        ['name' => Lang::get('lang.TXT_FOOD_DETAIL_CARBOHYDRATE'), 'value' => intval($item['K.HİDRAT'])]
                                    ],
                                    'info' => $item['AÇIKLAMA']
                                ]
                            ]
                        ];
                    }
                }
                return $foodInfo;
            });
        } catch (\Exception $exception) {

        }

        return $foodInfo;
    }

    private function _getShuttleInfoFromCache()
    {
        $shuttleInfo = new \stdClass();

        try {
            $shuttleInfo = Cache::rememberForever("diningHallShuttleInfo_" . app('translator')->getLocale(), function () {
                $shuttleInfo = [
                    'header' => Lang::get('lang.TXT_DINING_HALL_SHUTTLE_INFO_HEADER'),
                    'shuttleList' => []
                ];
                $collection = (new FastExcel)->import("/var/www/html/bizizFiles/food/Yemek_Servis.xlsx");
                foreach ($collection as $item) {
                    $shuttleInfo['shuttleList'][] = [
                        'location' => $item['LOKASYON'],
                        'timeList' => [
                            ['name' => Lang::get('lang.TXT_DINING_HALL_SHUTTLE_TYPE_LUNCH'), 'timeText' => $item['ÖĞLE']],
                            ['name' => Lang::get('lang.TXT_DINING_HALL_SHUTTLE_TYPE_DINNER'), 'timeText' => $item['AKŞAM']]
                        ]
                    ];
                }
                return $shuttleInfo;
            });
        } catch (\Exception $exception) {

        }

        return $shuttleInfo;
    }
}