<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 10.06.2018
 * Time: 13:51
 */

namespace App\Console\Commands;


use App\Constants;
use Carbon\Carbon;
use Rap2hpoutre\FastExcel\FastExcel;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SetEmployeeShuttles extends Command
{
    protected $name = 'set:employee_shuttles';

    private $shuttleType = Constants::SHUTTLE_TYPE_EMPLOYEE;

    public function handle()
    {
        Log::info("set:employee_shuttles started.");
        $this->_setPersonalShuttlesWithoutTab(Constants::COMPANY_LOCATION_GENEL_MUDURLUK, "Pazarlama");
        $this->_setPersonalShuttlesWithTab(Constants::COMPANY_LOCATION_HOSDERE, "Hosdere");
        $this->_setPersonalShuttlesWithoutTab(Constants::COMPANY_LOCATION_AKSARAY, "Aksaray");
        Log::info("set:employee_shuttles finished.");

        return 'ok';
    }

    private function _setPersonalShuttlesWithoutTab($companyLocationId, $name)
    {
        //region Check File
        $filePath = "/var/www/html/bizizFiles/shuttle/$name - Personel Servisleri.xlsx";
        if (!file_exists($filePath)) {
            Log::info("set:employee_shuttles file not found.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için personel servisleri güncellenemedi!");
            return;
        }

        $modificationTime = Carbon::createFromTimestamp(filemtime($filePath));
        if (Carbon::now()->diffInDays($modificationTime) > 1) {
            Log::info("No file update.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya 1 günden eski bir dosya olduğu için personel servisleri tekrar güncellenmedi!");
            return;
        }
        //endregion

        //region Set Lists To Delete
        $fromIdListToDeleteResult = DB::select("select id
                                                from from_company_shuttle_stops
                                                where shuttle_id in (select s.id
                                                                      from shuttles s
                                                                      where s.type = ? and s.company_location_id = ?)", [$this->shuttleType, $companyLocationId]);
        $fromIdListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $fromIdListToDeleteResult);

        $toIdListToDeleteResult = DB::select("select id
                                              from to_company_shuttle_stops
                                              where shuttle_id in (select s.id
                                                                    from shuttles s
                                                                    where s.type = ? and s.company_location_id = ?)", [$this->shuttleType, $companyLocationId]);
        $toIdListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $toIdListToDeleteResult);

        $shuttleIdListToDeleteResult = DB::select("select id
                                                    from shuttles
                                                    where type = ? and company_location_id = ?", [$this->shuttleType, $companyLocationId]);
        $shuttleIdListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $shuttleIdListToDeleteResult);
        //endregion

        DB::beginTransaction();
        try {
            //region Add New Data
            $collection = (new FastExcel)->import($filePath);
            foreach ($collection as $item) {
                if (!empty($item['GİDİŞ KALKIŞ SAATİ']) && !empty($item['GİDİŞ GÜZERGAH'])) { // to company exists
                    if (gettype($item['GİDİŞ KALKIŞ SAATİ']) == "string" || gettype($item['GİDİŞ VARIŞ SAATİ']) == "string") {
                        continue;
                    }

                    $toCompanyStopIds = [];
                    $fromCompanyStopIds = null;

                    $stopNameList = explode(' + ', $item['GİDİŞ GÜZERGAH']);
                    foreach ($stopNameList as $stopName) {
                        $stopResult = DB::table('stops')->where('name', mb_strtoupper($stopName, "UTF-8"))->first();
                        if ($stopResult == null || $stopResult->id == null) {
                            $toCompanyStopIds[] = DB::table('stops')->insertGetId(['name' => mb_strtoupper($stopName, "UTF-8")]);
                        } else {
                            $toCompanyStopIds[] = $stopResult->id;
                        }
                    }

                    if (!empty($item['DÖNÜŞ KALKIŞ SAATİ'])) { // from company exists
                        if (gettype($item['DÖNÜŞ KALKIŞ SAATİ']) == "string" || gettype($item['DÖNÜŞ VARIŞ SAATİ']) == "string") {
                            continue;
                        }

                        if (!empty($item['DÖNÜŞ GÜZERGAH'])) {
                            $fromCompanyStopIds = [];
                            $stopNameList = explode(' + ', $item['DÖNÜŞ GÜZERGAH']);
                            foreach ($stopNameList as $stopName) {
                                $stopResult = DB::table('stops')->where('name', mb_strtoupper($stopName, "UTF-8"))->first();
                                if ($stopResult == null || $stopResult->id == null) {
                                    $fromCompanyStopIds[] = DB::table('stops')->insertGetId(['name' => mb_strtoupper($stopName, "UTF-8")]);
                                } else {
                                    $fromCompanyStopIds[] = $stopResult->id;
                                }
                            }
                        } else {
                            $fromCompanyStopIds = array_reverse($toCompanyStopIds);
                        }
                    }

                    $shuttleId = DB::table('shuttles')->insertGetId([
                        'departure_location' => trim($item['KALKTIĞI YER']),
                        'company_location_id' => $companyLocationId,
                        'license_plate' => trim($item['PLAKA']),
                        'driver_name_surname' => trim($item['SÜRÜCÜ ADI']),
                        'driver_telephone' => "0" . trim($item['GSM']),
                        'to_departure_time' => $item['GİDİŞ KALKIŞ SAATİ']->format('H:i'),
                        'to_arrival_time' => $item['GİDİŞ VARIŞ SAATİ']->format('H:i'),
                        'to_time_type' => null,
                        'from_departure_time' => !empty($item['DÖNÜŞ KALKIŞ SAATİ']) ? $item['DÖNÜŞ KALKIŞ SAATİ']->format('H:i') : null,
                        'from_arrival_time' => !empty($item['DÖNÜŞ KALKIŞ SAATİ']) && !empty($item['DÖNÜŞ VARIŞ SAATİ']) ? $item['DÖNÜŞ VARIŞ SAATİ']->format('H:i') : null,
                        'from_time_type' => null,
                        'type' => $this->shuttleType
                    ]);

                    if (is_array($toCompanyStopIds) && count($toCompanyStopIds) > 0) {
                        $toCompanyStopList = [];
                        for ($i = 0; $i < count($toCompanyStopIds); $i++) {
                            $toCompanyStopList[] = [
                                'shuttle_id' => $shuttleId,
                                'stop_id' => $toCompanyStopIds[$i],
                                'order' => $i + 1
                            ];
                        }
                        DB::table('to_company_shuttle_stops')->insert($toCompanyStopList);
                    }

                    if (is_array($fromCompanyStopIds) && count($fromCompanyStopIds) > 0) {
                        $fromCompanyStopList = [];
                        for ($i = 0; $i < count($fromCompanyStopIds); $i++) {
                            $fromCompanyStopList[] = [
                                'shuttle_id' => $shuttleId,
                                'stop_id' => $fromCompanyStopIds[$i],
                                'order' => $i + 1
                            ];
                        }
                        DB::table('from_company_shuttle_stops')->insert($fromCompanyStopList);
                    }
                } elseif (!empty($item['DÖNÜŞ KALKIŞ SAATİ']) && !empty($item['DÖNÜŞ GÜZERGAH'])) { // only from company exists
                    if (gettype($item['DÖNÜŞ KALKIŞ SAATİ']) == "string" || gettype($item['DÖNÜŞ VARIŞ SAATİ']) == "string") {
                        continue;
                    }

                    $fromCompanyStopIds = [];
                    $stopNameList = explode(' + ', $item['DÖNÜŞ GÜZERGAH']);
                    foreach ($stopNameList as $stopName) {
                        $stopResult = DB::table('stops')->where('name', mb_strtoupper($stopName, "UTF-8"))->first();
                        if ($stopResult == null || $stopResult->id == null) {
                            $fromCompanyStopIds[] = DB::table('stops')->insertGetId(['name' => mb_strtoupper($stopName, "UTF-8")]);
                        } else {
                            $fromCompanyStopIds[] = $stopResult->id;
                        }
                    }

                    $shuttleId = DB::table('shuttles')->insertGetId([
                        'departure_location' => trim($item['KALKTIĞI YER']),
                        'company_location_id' => $companyLocationId,
                        'license_plate' => trim($item['PLAKA']),
                        'driver_name_surname' => trim($item['SÜRÜCÜ ADI']),
                        'driver_telephone' => "0" . trim($item['GSM']),
                        'to_departure_time' => null,
                        'to_arrival_time' => null,
                        'to_time_type' => null,
                        'from_departure_time' => $item['DÖNÜŞ KALKIŞ SAATİ']->format('H:i'),
                        'from_arrival_time' => !empty($item['DÖNÜŞ VARIŞ SAATİ']) ? $item['DÖNÜŞ VARIŞ SAATİ']->format('H:i') : null,
                        'from_time_type' => null,
                        'type' => $this->shuttleType
                    ]);

                    if (!is_array($fromCompanyStopIds)) {
                        continue;
                    }

                    $fromCompanyStopList = [];
                    for ($i = 0; $i < count($fromCompanyStopIds); $i++) {
                        $fromCompanyStopList[] = [
                            'shuttle_id' => $shuttleId,
                            'stop_id' => $fromCompanyStopIds[$i],
                            'order' => $i + 1
                        ];
                    }
                    DB::table('from_company_shuttle_stops')->insert($fromCompanyStopList);
                } else {
                    continue;
                }
            }
            //endregion

            //region Delete Old Data
            if (is_array($fromIdListToDelete) && count($fromIdListToDelete) > 0) {
                foreach (collect($fromIdListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('from_company_shuttle_stops')->whereIn('id', $list)->delete();
                }
            }
            if (is_array($toIdListToDelete) && count($toIdListToDelete) > 0) {
                foreach (collect($toIdListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('to_company_shuttle_stops')->whereIn('id', $list)->delete();
                }
            }
            if (is_array($shuttleIdListToDelete) && count($shuttleIdListToDelete) > 0) {
                foreach (collect($shuttleIdListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('shuttles')->whereIn('id', $list)->delete();
                }
            }
            //endregion

            DB::commit();

            //region Delete Cache
            $fromKey = "shuttles_from_company_" . $this->shuttleType . "_" . $companyLocationId;
            Cache::forget($fromKey);

            $toKey = "shuttles_to_company_" . $this->shuttleType . "_" . $companyLocationId;
            Cache::forget($toKey);

            //TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak personel servisleri başarıyla güncellendi.");
            //endregion
        } catch (\Exception $e) {
            DB::rollBack();
            Log::error("set:employee_shuttles transaction error. " . $e->getMessage());
        }
    }

    private function _setPersonalShuttlesWithTab($companyLocationId, $name)
    {
        //region Check File
        $filePath = "/var/www/html/bizizFiles/shuttle/$name - Personel Servisleri.xlsx";
        if (!file_exists($filePath)) {
            Log::info("set:employee_shuttles file not found.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için personel servisleri güncellenemedi!");
            return;
        }

        $modificationTime = Carbon::createFromTimestamp(filemtime($filePath));
        if (Carbon::now()->diffInDays($modificationTime) > 1) {
            Log::info("No file update.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya 1 günden eski bir dosya olduğu için personel servisleri tekrar güncellenmedi!");
            return;
        }
        //endregion

        //region Set Lists To Delete
        $fromIdListToDeleteResult = DB::select("select id
                                                from from_company_shuttle_stops
                                                where shuttle_id in (select s.id
                                                                      from shuttles s
                                                                      where s.type = ? and s.company_location_id = ?)", [$this->shuttleType, $companyLocationId]);
        $fromIdListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $fromIdListToDeleteResult);

        $toIdListToDeleteResult = DB::select("select id
                                              from to_company_shuttle_stops
                                              where shuttle_id in (select s.id
                                                                    from shuttles s
                                                                    where s.type = ? and s.company_location_id = ?)", [$this->shuttleType, $companyLocationId]);
        $toIdListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $toIdListToDeleteResult);

        $shuttleIdListToDeleteResult = DB::select("select id
                                                    from shuttles
                                                    where type = ? and company_location_id = ?", [$this->shuttleType, $companyLocationId]);
        $shuttleIdListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $shuttleIdListToDeleteResult);
        //endregion

        DB::beginTransaction();
        try {
            //region Add New Data
            $collection = (new FastExcel)->import($filePath);
            foreach ($collection as $item) {
                $toTabType = $this->_getTabType($item['GİDİŞ SAAT TİPİ']);
                $fromTabType = $this->_getTabType($item['DÖNÜŞ SAAT TİPİ']);
                if (!empty($item['GİDİŞ KALKIŞ SAATİ']) && !empty($item['GİDİŞ GÜZERGAH']) && $toTabType != null) { // to company exists
                    if (gettype($item['GİDİŞ KALKIŞ SAATİ']) == "string" || gettype($item['GİDİŞ VARIŞ SAATİ']) == "string") {
                        continue;
                    }

                    $toCompanyStopIds = [];
                    $fromCompanyStopIds = null;

                    $stopNameList = explode(' + ', $item['GİDİŞ GÜZERGAH']);
                    foreach ($stopNameList as $stopName) {
                        $stopResult = DB::table('stops')->where('name', mb_strtoupper($stopName, "UTF-8"))->first();
                        if ($stopResult == null || $stopResult->id == null) {
                            $toCompanyStopIds[] = DB::table('stops')->insertGetId(['name' => mb_strtoupper($stopName, "UTF-8")]);
                        } else {
                            $toCompanyStopIds[] = $stopResult->id;
                        }
                    }

                    $shuttleId = DB::table('shuttles')->insertGetId([
                        'departure_location' => trim($item['KALKTIĞI YER']),
                        'company_location_id' => $companyLocationId,
                        'license_plate' => trim($item['PLAKA']),
                        'driver_name_surname' => trim($item['SÜRÜCÜ ADI']),
                        'driver_telephone' => "0" . trim($item['GSM']),
                        'to_departure_time' => $item['GİDİŞ KALKIŞ SAATİ']->format('H:i'),
                        'to_arrival_time' => !empty($item['GİDİŞ VARIŞ SAATİ']) ? $item['GİDİŞ VARIŞ SAATİ']->format('H:i') : null,
                        'to_time_type' => $toTabType,
                        'from_departure_time' => null,
                        'from_arrival_time' => null,
                        'from_time_type' => null,
                        'type' => $this->shuttleType
                    ]);

                    if (is_array($toCompanyStopIds) && count($toCompanyStopIds) > 0) {
                        $toCompanyStopList = [];
                        for ($i = 0; $i < count($toCompanyStopIds); $i++) {
                            $toCompanyStopList[] = [
                                'shuttle_id' => $shuttleId,
                                'stop_id' => $toCompanyStopIds[$i],
                                'order' => $i + 1
                            ];
                        }
                        DB::table('to_company_shuttle_stops')->insert($toCompanyStopList);
                    }
                } elseif (!empty($item['DÖNÜŞ KALKIŞ SAATİ']) && !empty($item['DÖNÜŞ GÜZERGAH']) && $fromTabType != null) { // from company exists
                    if (gettype($item['DÖNÜŞ KALKIŞ SAATİ']) == "string" || gettype($item['DÖNÜŞ VARIŞ SAATİ']) == "string") {
                        continue;
                    }

                    $fromCompanyStopIds = [];
                    $stopNameList = explode(' + ', $item['DÖNÜŞ GÜZERGAH']);
                    foreach ($stopNameList as $stopName) {
                        $stopResult = DB::table('stops')->where('name', mb_strtoupper($stopName, "UTF-8"))->first();
                        if ($stopResult == null || $stopResult->id == null) {
                            $fromCompanyStopIds[] = DB::table('stops')->insertGetId(['name' => mb_strtoupper($stopName, "UTF-8")]);
                        } else {
                            $fromCompanyStopIds[] = $stopResult->id;
                        }
                    }

                    $shuttleId = DB::table('shuttles')->insertGetId([
                        'departure_location' => trim($item['KALKTIĞI YER']),
                        'company_location_id' => $companyLocationId,
                        'license_plate' => trim($item['PLAKA']),
                        'driver_name_surname' => trim($item['SÜRÜCÜ ADI']),
                        'driver_telephone' => "0" . trim($item['GSM']),
                        'to_departure_time' => null,
                        'to_arrival_time' => null,
                        'to_time_type' => null,
                        'from_departure_time' => $item['DÖNÜŞ KALKIŞ SAATİ']->format('H:i'),
                        'from_arrival_time' => !empty($item['DÖNÜŞ VARIŞ SAATİ']) ? $item['DÖNÜŞ VARIŞ SAATİ']->format('H:i') : null,
                        'from_time_type' => $fromTabType,
                        'type' => $this->shuttleType
                    ]);

                    if (!is_array($fromCompanyStopIds)) {
                        continue;
                    }

                    $fromCompanyStopList = [];
                    for ($i = 0; $i < count($fromCompanyStopIds); $i++) {
                        $fromCompanyStopList[] = [
                            'shuttle_id' => $shuttleId,
                            'stop_id' => $fromCompanyStopIds[$i],
                            'order' => $i + 1
                        ];
                    }
                    DB::table('from_company_shuttle_stops')->insert($fromCompanyStopList);
                } else {
                    continue;
                }
            }
            //endregion

            //region Delete Old Data
            if (is_array($fromIdListToDelete) && count($fromIdListToDelete) > 0) {
                foreach (collect($fromIdListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('from_company_shuttle_stops')->whereIn('id', $list)->delete();
                }
            }
            if (is_array($toIdListToDelete) && count($toIdListToDelete) > 0) {
                foreach (collect($toIdListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('to_company_shuttle_stops')->whereIn('id', $list)->delete();
                }
            }
            if (is_array($shuttleIdListToDelete) && count($shuttleIdListToDelete) > 0) {
                foreach (collect($shuttleIdListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('shuttles')->whereIn('id', $list)->delete();
                }
            }
            //endregion

            DB::commit();

            //region Delete Cache
            $fromKey = "shuttles_from_company_" . $this->shuttleType . "_" . $companyLocationId;
            Cache::forget($fromKey);

            $toKey = "shuttles_to_company_" . $this->shuttleType . "_" . $companyLocationId;
            Cache::forget($toKey);

            //TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak personel servisleri başarıyla güncellendi.");
            //endregion
        } catch (\Exception $e) {
            DB::rollBack();
            Log::error("set:employee_shuttles transaction error. " . $e->getMessage());
        }
    }

    private function _getTabType($tabTypeText)
    {
        if ($tabTypeText == "SABAH") {
            return Constants::SHUTTLE_TIME_TYPE_MORNING;
        } elseif ($tabTypeText == "ÖĞLE") {
            return Constants::SHUTTLE_TIME_TYPE_NOON;
        } elseif ($tabTypeText == "AKŞAM") {
            return Constants::SHUTTLE_TIME_TYPE_EVENING;
        } elseif ($tabTypeText == "GECE") {
            return Constants::SHUTTLE_TIME_TYPE_NIGHT;
        } else {
            return null;
        }
    }
}
