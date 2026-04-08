<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 3.05.2018
 * Time: 21:54
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Faker\Factory;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\Http;

class LoginController extends Controller
{

    public function captcha()
    {
        $width = 600;
        $height = 150;

        try {
            $image = imagecreatetruecolor($width, $height);

            $background_color = imagecolorallocate($image, 255, 255, 255);
            $text_color = imagecolorallocate($image, 0, 255, 255);
            $line_color = imagecolorallocate($image, 64, 64, 64);
            $pixel_color = imagecolorallocate($image, 0, 0, 255);

            imagefilledrectangle($image, 0, 0, $width, $height, $background_color);

            $letters = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789';
            $len = strlen($letters);
            $letter = $letters[rand(0, $len - 1)];

            $text_color = imagecolorallocate($image, 0, 0, 0);
            $text = "";
            for ($i = 0; $i < 6; $i++) {
                $letter = $letters[rand(0, $len - 1)];
                $font = '/var/www/html/bizizFiles/Roboto-Regular.ttf';
//                imagestring($image, 7, 5 + ($i * 30), 20, $letter, $text_color);
                imagettftext($image, 50, 0, 90 + ($i * 75), 100, $text_color, $font, $letter);
                $text .= $letter;
            }

            for ($i = 0; $i < 10; $i++) {
                imageline($image, 0, rand() % $height, $width, rand() % $height, $line_color);
            }

            for ($i = 0; $i < 3000; $i++) {
                imagesetpixel($image, rand() % $width, rand() % $height, $pixel_color);
            }

            $uuid = Factory::create('tr_TR')->uuid;
            $imageName = $uuid . ".png";
            $imagePath = dirname(dirname(dirname(dirname(__FILE__)))) . "/storage/app/public/contents/captcha/$imageName";
            imagepng($image, $imagePath);
            $storageImagePath = "contents/captcha/" . $imageName;
            $projectUrl = app()->environment() == "production" ? "https://biziapp-test.app.daimlertruck.com/bizizBackend/public" : "http://95.214.97.107/bizizBackend/public";
            $imageUrl = $projectUrl . "/storage/$storageImagePath";

            DB::table('captcha')->insert([
                'text' => $text,
                'image' => $imageUrl,
                'token' => $uuid,
                'created_at' => Carbon::now()
            ]);

            imagedestroy($image);

            return response()->json([
                'statusCode' => 200,
                'responseData' => [
                    'token' => $uuid,
                    'image' => $imageUrl,
                ],
                'errorMessage' => null
            ]);
        } catch (\Exception $exception) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_CANNOT_CREATE_CAPTCHA') . " " . $exception->getMessage()
            ]);
        }
            
    }
/*
    public function checkPhoneWithCaptcha(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'phoneNumber' => 'required|string|min:10|max:10',
            'captchaToken' => 'string|min:36|max:36',
            'captchaText' => 'string|min:6',
            'appVersion'  => 'string|min:3'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Eksik/hatalı parametre(ler) var!"
            ]);
        }
        //endregion

        $phoneNumber = $request->input('phoneNumber');
        if (!is_numeric($phoneNumber)) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Eksik/hatalı parametre(ler) var!"
            ]);
        }

        $appVersion = $request->input('appVersion');

        $now = Carbon::now();
        $previousTime = $now->copy()->subMinute();

        if ($appVersion != "1.2.0"){
            $captchaResult = $this->getFirstItemFromDb("select id
                                                                from captcha
                                                                where text = ? and token = ? and
                                                                        is_used = 0 and created_at >= ?
                                                                order by id desc
                                                                limit 1", [$request->input('captchaText'), $request->input('captchaToken'), $previousTime]);
            if ($captchaResult == null) {
                return response()->json([
                    'statusCode' => 401,
                    'responseData' => null,
                    'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_CAPTCHA_NOT_VALID')
                ]);
            }
            DB::update("update captcha set is_used = 1, updated_at = ? where id = ?", [$now, $captchaResult->id]);
        }
            $userResult = $this->getFirstItemFromDb("select id from users where mobile_phone = ? and status = 1", [$phoneNumber]);
            if ($userResult == null) {
    //            return response()->json([
    //                'statusCode' => 401,
    //                'responseData' => null,
    //                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_PHONE_NUMBER_NOT_FOUND')
    //            ]);
                return response()->json([
                    'statusCode' => 200,
                    'responseData' => null,
                    'errorMessage' => null
                ]);
            }

        //$checkPinCodeResult = $this->getFirstItemFromDb("select count(id) as result
        //                                                          from user_pin_codes
        //                                                          where user_id = ? and is_used = 0 and created_at >= ?", [$userResult->id, $previousTime->toDateTimeString()]);
        $checkPinCodeResult = $this->getFirstItemFromDb("select count(id) as result
                                                                  from user_pin_codes
                                                                  where user_id = ? and created_at >= ?", [$userResult->id, $previousTime->toDateTimeString()]);
        if ($checkPinCodeResult != null && $checkPinCodeResult->result >= 10) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'hello'=> 1,
                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_CHECK_PHONE_WAIT')
            ]);
        }

        $isTestPhone = in_array($phoneNumber, ["5551234567", "5367967265", "5551234561", "5551234562", "5551234563", "5551234564", "5551234565", "5551234566"]);
        $pinCode = random_int(1000, 9999);
        DB::table('user_pin_codes')->insert([
            'user_id' => $userResult->id,
            'pin_code' => !$isTestPhone ? $pinCode : 1234,
            'created_at' => $now
        ]);

        if (!$isTestPhone) {
            try {
                Log::warning("SMS sending to $phoneNumber with pin code $pinCode");
        
                // Temel doğrulama
                if (!preg_match('/^[0-9]{10}$/', $phoneNumber)) {
                    return response()->json([
                        'statusCode' => 400,
                        'responseData' => null,
                        'errorMessage' => 'Geçersiz telefon numarası'
                    ]);
                }
        
                // Güvenli HTTP çağrısı
                $response = Http::timeout(10)->get(
                    'https://www.postaguvercini.com/api_http/sendsms.asp',
                    [
                        'user' => 'Mercedesbulk',
                        'password' => '123456',
                        'gsm' => $phoneNumber,
                        'text' => "MBT BizIZ pin kodunuz: $pinCode",
                    ]
                );
        
                $sendSMSResponse = $response->body();
                Log::warning("SMS response : $sendSMSResponse");
        
                if (!str_contains($sendSMSResponse, 'errno=0')) {
                    Log::error("Couldn't send SMS to $phoneNumber\nResponse: $sendSMSResponse");
                }
            } catch (\Throwable $exception) {
                Log::warning("Exception occurred while sending SMS to $phoneNumber");
                Log::error("Couldn't send SMS to $phoneNumber\nException: " . $exception->getMessage());
                return response()->json([
                    'statusCode' => 401,
                    'responseData' => null,
                    'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_SEND_SMS'),
                ]);
            }
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }*/

    // will be removed
 /*   public function checkPhone(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'phoneNumber' => 'required|string|min:10|max:10'
        ]);

        return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => Lang::get('Lütfen yeni uygulamayı indiriniz.')
            ]);

        Log::warning("checkPhone called with phoneNumber: " . $request->input('phoneNumber'));

        if ($validator->fails()) // missing parameters
        {   
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Eksik/hatalı parametre(ler) var!"
            ]);
        }
        //endregion


        $phoneNumber = $request->input('phoneNumber');
        if (!is_numeric($phoneNumber)) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Eksik/hatalı parametre(ler) var!"
            ]);
        }

        Log::warning("checkPhone step2");

        $userResult = $this->getFirstItemFromDb("select id from users where mobile_phone = ? and status = 1", [$phoneNumber]);
        if ($userResult == null) {
//            return response()->json([
//                'statusCode' => 401,
//                'responseData' => null,
//                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_PHONE_NUMBER_NOT_FOUND')
//            ]);
            return response()->json([
                'statusCode' => 200,
                'responseData' => null,
                'errorMessage' => "test11"
            ]);
        }

        $now = Carbon::now();
        $previousTime = $now->copy()->subMinute();

        Log::warning("checkPhone step3");

        //$checkPinCodeResult = $this->getFirstItemFromDb("select count(id) as result from user_pin_codes where user_id = ? and is_used = 0 and created_at >= ?", [$userResult->id, $previousTime->toDateTimeString()]);
        $checkPinCodeResult = $this->getFirstItemFromDb("select count(id) as result from user_pin_codes where user_id = ? and created_at >= ?", [$userResult->id, $previousTime->toDateTimeString()]);
        if ($checkPinCodeResult != null && $checkPinCodeResult->result >= 10) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_CHECK_PHONE_WAIT')
            ]);
        }

        $isTestPhone = in_array($request->input('phoneNumber'), ["5551234567", "5367967265", "5551234561", "5551234562", "5551234563", "5551234564", "5551234565", "5551234566", "5357283398"]);

        $pinCode = random_int(1000, 9999);
        DB::table('user_pin_codes')->insert([
            'user_id' => $userResult->id,
            'pin_code' => !$isTestPhone ? $pinCode : 1234,
            'created_at' => $now
        ]);

        if (!$isTestPhone) {
            try {
                Log::warning("SMS sending to $phoneNumber with pin code $pinCode");
                
                //$sendSMSResponse = file_get_contents("https://www.postaguvercini.com/api_http/sendsms.asp?user=Mercedesbulk&password=123456&gsm=$phoneNumber&text=MBT%20BizIZ%20pin%20kodunuz%3A%20$pinCode");
                
                // Telefon numarası doğrulaması (regex ile)
                if (!preg_match('/^[0-9]{10}$/', $phoneNumber)) {
                    return response()->json([
                        'statusCode' => 400,
                        'errorMessage' => 'Geçersiz telefon numarası'
                    ]);
                }

                // Güvenli HTTP isteği
                $response = Http::get('https://www.postaguvercini.com/api_http/sendsms.asp', [
                    'user' => 'Mercedesbulk',
                    'password' => '123456',
                    'gsm' => $phoneNumber,
                    'text' => "MBT BizIZ pin kodunuz: $pinCode"
                ]);

                $sendSMSResponse = $response->body();

                // Yanıt kontrolü
                if (!str_contains($sendSMSResponse, "errno=0")) {
                    Log::error("Couldn't send SMS to $phoneNumber\nResponse: $sendSMSResponse");
                }
            } catch (\Exception $exception) {
                Log::warning("Exception occurred while sending SMS to $phoneNumber");
                Log::error("Couldn't send SMS to $phoneNumber\nException: " . $exception->getMessage());
                return response()->json([
                    'statusCode' => 401,
                    'responseData' => null,
                    'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_SEND_SMS')
                ]);
            }
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }*/
    
    public function oidcLogin(Request $request)
    {
        // Extract the token from the request
        $token = $request->bearerToken();

        // Check if a token is present
        if (!$token) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => 'No token provided' 
             ]);
        }

        // Optional: Log the token for debugging purposes
        //Log::info('OIDC Callback received token: ' . $token);

        // Since the authentication logic is handled in the middleware,
        // you don't need to do much here unless you want to do additional processing

        // For demonstration, we'll just return a success message
        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
         ]);

    }


    public function login(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'phoneNumber' => 'required|string|min:10|max:10',
            'pin' => 'required|integer|min:1000|max:9999'
        ]);

        if ($validator->fails()) // missing parameters
        {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => "Eksik/hatalı parametre(ler) var!"
            ]);
        }
        //endregion

        $checkPinCodeResult = $this->getFirstItemFromDb("select upc.id, upc.pin_code, upc.is_used, upc.try_count, u.token
                                                                  from user_pin_codes upc, users u
                                                                  where upc.user_id = u.id and u.mobile_phone = ? and u.status = 1 and upc.created_at > ?
                                                                  order by upc.id desc
                                                                  limit 1", [$request->input('phoneNumber'), Carbon::now()->subMinutes(2)]); 
        if ($checkPinCodeResult == null || $checkPinCodeResult->is_used) {
            return response()->json([
                'statusCode' => 402,
                'responseData' => null,
                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_REQUEST_PIN_CODE_AGAIN')
            ]);
        } elseif ($checkPinCodeResult->try_count >= 5) {
            return response()->json([
                'statusCode' => 402,
                'responseData' => null,
                'errorMessage' => Lang::get('lang.TXT_SERVER_ERROR_TOO_MUCH_PIN_CODE_TRY')
            ]);
        } else {
            if ($checkPinCodeResult->pin_code == $request->input('pin')) {
                DB::update("update user_pin_codes set is_used = 1, updated_at = ? where id = ?", [Carbon::now(), $checkPinCodeResult->id]);

                return response()->json([
                    'statusCode' => 200,
                    'responseData' => ['token' => $checkPinCodeResult->token],
                    'errorMessage' => null
                ]);
            } else {
                DB::update("update user_pin_codes set try_count = ?, updated_at = ? where id = ?", [$checkPinCodeResult->try_count + 1, Carbon::now(), $checkPinCodeResult->id]);

                return response()->json([
                    'statusCode' => ($checkPinCodeResult->try_count + 1) == 5 ? 402 : 401,
                    'responseData' => null,
                    'errorMessage' => ($checkPinCodeResult->try_count + 1) == 5 ? Lang::get('lang.TXT_SERVER_ERROR_TOO_MUCH_PIN_CODE_TRY') : Lang::get('lang.TXT_SERVER_ERROR_WRONG_PIN_CODE')
                ]);
            }
        }
    }

    public function signOut()
    {
        DB::update("update users set token = ?, updated_at = ? where id = ?", [Factory::create('tr_TR')->uuid, Carbon::now(), Auth::id()]);

        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }

    public function appStartup(Request $request)
    {
        if ($request->header('lang') == 'en') {
            app('translator')->setLocale('en');
            setlocale(LC_TIME, null);
            Carbon::setLocale('en');
        }

        if ($this->isLangEn()) {
            $aboutText = Redis::get('aboutEN');
            $appDescriptionText = Redis::get('appDescriptionEN');
        } else {
            $aboutText = Redis::get('aboutTR');
            $appDescriptionText = Redis::get('appDescriptionTR');
        }
        
        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'aboutText' => $aboutText,
                'appDescriptionText' => $appDescriptionText 
            ],
            'errorMessage' => null
        ]);

    }

    private function _contains($needle, $haystack)
    {
        return strpos($haystack, $needle) !== false;
    }
}
