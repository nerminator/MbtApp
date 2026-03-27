<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 14.06.2018
 * Time: 19:55
 */

namespace App\Console\Commands;


use Carbon\Carbon;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SetWorkingCalendar extends Command
{
    protected $name = 'set:working_calendar';

    public function handle()
    {
        Log::info("set:working_calendar started.");
        $this->_setWorkingCalendar();
        Log::info("set:working_calendar finished.");

        return 'ok';
    }

    private function _setWorkingCalendar()
    {
        $workingCalendarList = [];
        $fileName = "mbt_dvm_dvms_" . Carbon::now()->format('dmY'); // mbt_dvm_dvms_ddmmYYYY
        $filePath = "/var/www/html/bizizFiles/working_calendar/$fileName.txt";
        if (!file_exists($filePath)) {
            Log::info("set:working_calendar file not found.");
            //TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için personel takvim verileri güncellenemedi!");
            return;
        }

        $contents = $this->_file_get_contents_utf8($filePath);
        $lines = explode(PHP_EOL, $contents);
        $isHeaderLine = true;

        $dayFrom = Carbon::now();
        $dayFrom->day = 1;
        $dayFrom->hour = 7;
        $dayFrom->minute = 0;
        $dayFrom->second = 0;
        $dayFrom = $dayFrom->subMonth();

        $idListToDeleteResult = DB::table('working_calendar')
            ->where('calendar_date', '>=', $dayFrom)
            ->get(['id'])->toArray();
        $idListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $idListToDeleteResult);

        foreach ($lines as $line) {
            if ($isHeaderLine) {
                $isHeaderLine = false;
                continue;
            }

            $columns = explode("\t", $line);
            if (count($columns) != 4) {
                continue;
            }

            // PERSONEL SİCİL NUMARASI - TARİH - DVM/DVMSZLK TÜRÜ - DVM/DVSZLK TÜRÜ TEXT

            $workingCalendarList[] = [
                'register_number' => $columns[0], // PERSONEL SİCİL NUMARASI
                'calendar_date' => Carbon::createFromFormat('d.m.Y', $columns[1]), // TARİH
                'type_id' => $this->_getType(trim($columns[2]), trim($columns[3])) // DVM/DVMSZLK TÜRÜ, DVM/DVSZLK TÜRÜ TEXT
            ];
        }

        if (is_array($workingCalendarList) && count($workingCalendarList) > 0) {
            foreach (collect($workingCalendarList)->chunk(100)->toArray() as $list) {
                DB::table('working_calendar')->insert($list);
            }

            if (is_array($idListToDelete) && count($idListToDelete) > 0) {
                foreach (collect($idListToDelete)->chunk(100)->toArray() as $list) {
                    DB::table('working_calendar')->whereIn('id', $list)->delete();
                }
            }

            //TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak personel takvim verileri başarıyla güncellendi.");
        }
    }

    private function _file_get_contents_utf8($fn)
    {
            // file UTF-8 zaten, hiçbir encoding değiştirme
            return file_get_contents($fn);
    }

    private function _getType($type, $name)
    {
        $result = DB::table('working_calendar_types')->where('type', $type)->where('name', $name)->first();
        if ($result == null) {
            return DB::table('working_calendar_types')->insertGetId([
                'type' => $type,
                'name' => $name,
                'name_en' => $name,
                'color' => "ff0000"
            ]);
        }

        return $result->id;
    }
}
