<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 6.06.2018
 * Time: 23:52
 */

namespace App\Console\Commands;


use Carbon\Carbon;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;

class SetWorkingHoursInfo extends Command
{
    protected $name = 'set:working_hours_info';

    private $idListToDelete;

    public function handle()
    {
        Log::info("set:working_hours_info started.");
        $this->_setIdListToDelete();
        $this->_setBlueCollarWorkingHoursInfo();
        //$this->_setWhiteCollarWorkingHoursInfo();
        $this->_deleteOldWorkingHoursInfo();
        Log::info("set:working_hours_info finished.");

        return 'ok';
    }

    private function _setIdListToDelete()
    {
        $idListToDeleteResult = DB::table('working_hours_info')
            ->whereBetween('info_date', [Carbon::now()->startOfMonth()->subMonth(), Carbon::now()->endOfMonth()])
            /*->whereYear('info_date', Carbon::now()->year)
            ->where(function ($query) {
                $query->whereMonth('info_date', Carbon::now()->month)
                    ->orWhereMonth('info_date', Carbon::now()->subMonth()->month);
            })*/
            ->get(['id'])->toArray();
        $this->idListToDelete = array_map(function ($ar) {
            return $ar->id;
        }, $idListToDeleteResult);
    }

    private function _setBlueCollarWorkingHoursInfo()
    {
        $workingHoursInfoList = [];
        $fileName = "mbt_my_timedata_" . Carbon::now()->format('dmY'); // mbt_my_timedata_ddmmYYYY
        $filePath = "/var/www/html/bizizFiles/working_hours_info/$fileName.txt";
        if (!file_exists($filePath)) {
            Log::info("set:working_hours_info file not found.");
            TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için personel çalışma saati verileri güncellenemedi!");
            return;
        }

        $contents = $this->_file_get_contents_utf8($filePath);
        $lines = explode(PHP_EOL, $contents);
        $isHeaderLine = true;
	$startOfDayNow = Carbon::now()->startOfDay();

	// Collect all register numbers
	$validRegisterNumbers = DB::table('users')->pluck('register_number')->toArray();

        foreach ($lines as $line) {
            if ($isHeaderLine) {
                $isHeaderLine = false;
                continue;
            }

            $columns = explode("\t", $line);
            if (count($columns) != 6 || $this->_contains("Personel", $columns[0])) {
                continue;
            }

            // Personel No - Tarih - Gerçek Saatler - NÇ - DVS - FM+

            if (Carbon::createFromFormat('d.m.Y', $columns[1])->startOfDay() >= $startOfDayNow) {
                continue;
            }

	    // Check if register number exists in users table
	    if (!in_array($columns[0], $validRegisterNumbers)) {
       		 Log::warning("Skipping insert: register_number '{$columns[0]}' does not exist in users table.");
           	 continue;
	    }

            $workingHoursInfoList[] = [
                'register_number' => $columns[0], // Personel No
                'info_date' => Carbon::createFromFormat('d.m.Y', $columns[1]), // Tarih
                'hours_info' => $this->_getHoursInfo($columns[2]), // Gerçek Saatler
                'plus_hours' => $columns[3] == "" ? 0 : $this->_handleMinusSign($columns[3]), // NÇ
                'minus_hours' => $columns[4] == "" ? 0 : $this->_handleMinusSign($columns[4]), // DVS
                'total_hours' => $columns[5] == "" ? 0 : $this->_handleMinusSign($columns[5]), // FM+
                'cumulative_hours' => null
            ];
        }

        if (is_array($workingHoursInfoList) && count($workingHoursInfoList) > 0) {
	       foreach (collect($workingHoursInfoList)->chunk(100)->toArray() as $list) {
		  try {
                	DB::table('working_hours_info')->insert($list);
            	  } catch (\Exception $e) {
                	Log::error("Error inserting working hours: " . $e->getMessage());
	      	  }
                }
            TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak personel çalışma saati verileri başarıyla güncellendi.");
        }
    }

    private function _setWhiteCollarWorkingHoursInfo()
    {
        $workingHoursInfoList = [];
        $fileName = "mbt_by_timedata_" . Carbon::now()->format('dmY'); // mbt_by_timedata_ddmmYYYY
        $contents = $this->_file_get_contents_utf8("/var/www/html/bizizFiles/working_hours_info/$fileName.txt");
        $lines = explode(PHP_EOL, $contents);
        $isHeaderLine = true;
        $startOfDayNow = Carbon::now()->startOfDay();
        foreach ($lines as $line) {
            if ($isHeaderLine) {
                $isHeaderLine = false;
                continue;
            }

            $columns = explode("\t", $line);
            if (count($columns) != 7 || $this->_contains("Personel", $columns[0])) {
                continue;
            }

            // Personel No - Tarih - Gerçek Saatler - ArtıSaat - EksiSaat - GünlükEÇH - K-EÇH

            if (Carbon::createFromFormat('d.m.Y', $columns[1])->startOfDay() >= $startOfDayNow) {
                continue;
            }

            $userForeign = DB::table('users')->where('register_number', $columns[0])->first();
            if ($userForeign) {
                $workingHoursInfoList[] = [
                    'register_number' => $columns[0], // Personel No
                    'info_date' => Carbon::createFromFormat('d.m.Y', $columns[1]), // Tarih
                    'hours_info' => $this->_getHoursInfo($columns[2]), // Gerçek Saatler
                    'plus_hours' => $columns[3] == "" ? 0 : $this->_handleMinusSign($columns[3]), // ArtıSaat
                    'minus_hours' => $columns[4] == "" ? 0 : $this->_handleMinusSign($columns[4]), // EksiSaat
                    'total_hours' => $columns[5] == "" ? 0 : $this->_handleMinusSign($columns[5]), // GünlükEÇH
                    'cumulative_hours' => $columns[6] == "" ? 0 : $this->_handleMinusSign($columns[6]) // K-EÇH
                ];
            }
        }

        if (is_array($workingHoursInfoList) && count($workingHoursInfoList) > 0) {
            foreach (collect($workingHoursInfoList)->chunk(100)->toArray() as $list) {
                DB::table('working_hours_info')->insert($list);
            }
        }
    }

    private function _deleteOldWorkingHoursInfo()
    {
        if (count($this->idListToDelete) > 0) {
            foreach (collect($this->idListToDelete)->chunk(100)->toArray() as $list) {
                DB::table('working_hours_info')->whereIn('id', $list)->delete();
            }
        }
    }

    private function _file_get_contents_utf8($fn)
    {
        $content = file_get_contents($fn);
        return mb_convert_encoding($content, 'UTF-8',
            mb_detect_encoding($content, 'UTF-8, ISO-8859-1', true));
    }

    private function _getHoursInfo($text)
    {
        if ($text == null || $text == "") {
            return null;
        }

        return str_replace("][", "] [", $text);
    }

    private function _handleMinusSign($text)
    {
        if (!empty($text) && substr($text, strlen($text) - 1) == '-') {
            $text = str_replace('-', '', $text);
            $text = '-' . $text;
        }

        return str_replace(',', '.', $text);
    }

    private function _contains($needle, $haystack)
    {
        return strpos($haystack, $needle) !== false;
    }
}
