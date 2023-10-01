<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 5.06.2018
 * Time: 22:33
 */

namespace App\Console\Commands;


use App\Constants;
use App\Jobs\DeleteUsersJob;
use App\User;
use Carbon\Carbon;
use Faker\Factory;
use Illuminate\Console\Command;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Queue;

class SetUsers extends Command
{
    protected $name = 'set:users';

    public function handle()
    {
        Log::info("set:users started.");
        $this->_setUsers();
        Log::info("set:users finished.");

        return 'ok';
    }

    private function _setUsers()
    {
        $faker = Factory::create('tr_TR');
        $now = Carbon::now();
        $registerNumberList = [];
        $fileName = "mbt_persdata_" . Carbon::now()->format('dmY'); // mbt_persdata_ddmmYYYY
        $filePath = "/var/www/html/bizizFiles/users/$fileName.txt";
        if (!file_exists($filePath)) {
            Log::info("set:users file not found.");
            TelegramChannelService::sendMessage("UYARI: " . $filePath . " isimli dosya bulunamadığı için kullanıcılar güncellenemedi!");
            return;
        }

        $contents = $this->_file_get_contents_utf8($filePath);
        $lines = explode(PHP_EOL, $contents);
        $isHeaderLine = true;
        foreach ($lines as $line) {
            if ($isHeaderLine) {
                $isHeaderLine = false;
                continue;
            }

            $columns = explode("\t", $line);
            if (count($columns) != 15) {
                continue;
            }

            // PERSONEL SİCİL NUMARASI - KULLANICI ADI (x) - ADI SOYADI - MASRAF YERİ - MASRAF YERİ KOD - ORGANİZASYON ANAHTARI (x) - ORGANİZASYON BİRİM METNİ (x) - DOĞUM TARİHİ - ÇALIŞAN ALT GRUBU METNİ - LOKASYON - İŞ YERİ - TELEFON - EMAIL - POZİSYON - ŞİRKET KODU

            $registerNumber = trim($columns[0]); // PERSONEL SİCİL NUMARASI (-)
            $nameSurname = trim($columns[2]); // ADI SOYADI (-)
            $expenseCenterNumber = trim($columns[3]); // MASRAF YERİ (0000006310)
            $expenseCenter = trim($columns[4]); // MASRAF YERİ KOD (TE/STC)
            $birthday = trim($columns[7]); // DOĞUM TARİHİ (13.12)
            $rawType = trim($columns[8]); // ÇALIŞAN ALT GRUBU METNİ (009 Memur)
            $type = $this->_getUserType($rawType); // MAVİ / BEYAZ YAKA
            $location = trim($columns[9]); // LOKASYON (MBT İstanbul)
            $employeeLocationId = $this->_getEmployeeLocationId($location); // LOKASYON (MBT İstanbul)
            $companyLocationId = $this->_getCompanyLocationId(trim($columns[10])); // İŞ YERİ (GnMd&Pzrlm)
            $mobilePhone = trim($columns[11]); // TELEFON (-)
            if (substr($mobilePhone, 0, 1) === '0') {
                $mobilePhone = substr($mobilePhone, 1);
            }

            $email = trim($columns[12]); // EMAIL (-)
            $title = trim($columns[13]); // POZİSYON (YP Nakliye Sorumlusu)
            $companyCode = trim($columns[14]); // ŞİRKET KODU (1402)

            if (!empty($registerNumber)) {
                $registerNumberList[] = $registerNumber;
                $user = User::where('register_number', $registerNumber)->first();
                if ($user == null) {
                    DB::table('users')->insert([
                        'register_number' => $registerNumber,
                        'name_surname' => $nameSurname,
                        'expense_center_number' => $expenseCenterNumber,
                        'expense_center' => $expenseCenter,
                        'birthday' => !empty($birthday) ? $birthday : null,
                        'raw_type' => $rawType,
                        'type' => $type,
                        'location' => $location,
                        'employee_location_id' => $employeeLocationId,
                        'company_location_id' => $companyLocationId,
                        'mobile_phone' => !empty($mobilePhone) ? $mobilePhone : null,
                        'email' => !empty($email) ? $email : null,
                        'title' => $title,
                        'company_code' => $companyCode,
                        'token' => $faker->uuid,
                        'status' => 1,
                        'created_at' => $now
                    ]);
                } else {
                    $user->name_surname = $nameSurname;
                    $user->expense_center_number = $expenseCenterNumber;
                    $user->expense_center = $expenseCenter;
                    $user->birthday = !empty($birthday) ? $birthday : null;
                    $user->raw_type = $rawType;
                    $user->type = $type;
                    $user->location = $location;
                    $user->employee_location_id = $employeeLocationId;
                    $user->company_location_id = $companyLocationId;
                    $user->mobile_phone = !empty($mobilePhone) ? $mobilePhone : null;
                    $user->email = !empty($email) ? $email : null;
                    $user->title = $title;
                    $user->company_code = $companyCode;
                    $user->status = 1;
                    $user->updated_at = $now;
                    $user->save();
                }
            }
        }

        if (is_array($registerNumberList) && count($registerNumberList) > 0) {
            $testMobilePhoneList = [5551234561, 5551234562, 5551234563, 5551234564, 5551234565, 5551234566];
            DB::table('users')->whereNotIn('register_number', $registerNumberList)->whereNotIn('mobile_phone', $testMobilePhoneList)->where('status', 1)->update(['status' => 0, 'updated_at' => $now]);
//            DB::table('users')->whereNotIn('register_number', $registerNumberList)->where('status', 1)->update(['status' => 0, 'updated_at' => $now]);
            Queue::push(new DeleteUsersJob());

            TelegramChannelService::sendMessage("BİLGİLENDİRME: " . $filePath . " isimli dosya kullanılarak kullanıcılar başarıyla güncellendi.");
        }
    }

    private function _file_get_contents_utf8($fn)
    {
        $content = file_get_contents($fn);
        return mb_convert_encoding($content, 'UTF-8',
            mb_detect_encoding($content, 'UTF-8, ISO-8859-1', true));
    }

    private function _getUserType($typeText)
    {
        $whiteCollarList = ["001 Direktör", "004 Bölüm Müdürü", "006 Kısım Müdürü", "007 Grup Şefi", "009 Memur", "016 Engelii BY"];
//        $whiteCollarList = ["006 Kısım Müdürü", "007 Grup Şefi", "009 Memur"];
        $blueCollarList = ["010 Postabaşı", "013 Direkt İşçi", "014 Endirekt İşçi", "015 Özürlü/Hükümlü/T", "011 Postabaşı S", "015 Engelli MY"];

        if (in_array($typeText, $whiteCollarList)) {
            return Constants::EMPLOYEE_TYPE_WHITE_COLLAR;
        } elseif (in_array($typeText, $blueCollarList)) {
            return Constants::EMPLOYEE_TYPE_BLUE_COLLAR;
        } else {
            return null;
        }
    }

    private function _getEmployeeLocationId($employeeLocation)
    {
        $result = DB::table('employee_locations')->where('name', $employeeLocation)->first();
        if ($result == null || $result->id == null) {
            return DB::table('employee_locations')->insertGetId(['name' => $employeeLocation]);
        }

        return $result->id;
    }

    private function _getCompanyLocationId($companyLocation)
    {
        $result = DB::table('company_locations')->where('name', $companyLocation)->first();
        if ($result == null || $result->id == null) {
            return DB::table('company_locations')->insertGetId(['name' => $companyLocation]);
        }

        return $result->id;
    }
}
