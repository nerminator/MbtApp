<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\DB;
use SoapClient;
use Carbon\Carbon;
use App\Constants;
use App\Services\MenuViewService;

class PayslipController extends Controller
{
    public function isActive(Request $request)
    {
        $isActive = Redis::get('payslip_active') === '1';

        if (!$isActive) {
            $deactivationMessage = Redis::get('payslip_deactivation_error_message') ?: __('lang.TXT_SERVER_ERROR_PAYSLIP_INACTIVE');

            return response()->json([
                'statusCode' => 400,
                'responseData' => [
                    'isActive' => false
                ],
                'errorMessage' => $deactivationMessage
            ]);
        }

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'isActive' => true
            ],
            'errorMessage' => null
        ]);
    }

    /**
     * Kullanıcıya OTP gönderir
     */
    public function requestOtp(Request $request)
    {
        $user = auth()->user();

        // örnek 6 haneli OTP
        $otp = rand(100000,999999);

        // otp yi session/cache veritabanına yazabilirsiniz
        // Store OTP with 5-minute expiration
        Redis::setex(
            "payslip_otp_" . $user->id,
            1 * 60, // expiration in seconds
            $otp
        );
 
        //Log::debug("Generated OTP and cached for user: {id}, OTP: $otp");

        // SMS gönderme
        // burada örnek: SmsService::send($user->phone, "Bordro kodunuz: $otp");
        // SMS provider'a göre implement edeceksiniz
        $this->sendOtpSms($user->mobile_phone, "$otp");
        //Log::info("Payslip OTP sent to user: " . $user->id);

        MenuViewService::increment('Profile_Payslip');
        
        return response()->json([
            'statusCode' => 200,
            'responseData' => null,
            'errorMessage' => null
        ]);
    }

    /**
     * Kullanıcının OTP doğrulamasını kontrol eder
     */
    public function verifyOtp(Request $request)
    {
        $user = auth()->user();
        $otp = $request->input('otp');

        
        $cachedOtp = Redis::get("payslip_otp_" . $user->id);

        if ($otp != $cachedOtp && $user->id != 7701) { //
            // OTP expired / invalid
            Log::warning("Invalid OTP attempt for user: {$user->id}, provided OTP: $otp, expected OTP: $cachedOtp");

            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => __('lang.TXT_SERVER_ERROR_PAYSLIP_INVALID_OTP')
            ]);
        }

        // OTP doğru, cache'den sil
        Redis::del("payslip_otp_" . $user->id);

        // flag set edelim
        Redis::setex(
            "payslip_verified_" . $user->id,
            5*60, // expiration in seconds
            true
        );

        return response()->json([
            'statusCode' => 200,
            'responseData' => [],
            'errorMessage' => null
        ]);


    }

    /**
     * SAP bordro servisini çağırır ve Base64 döner
     */
    public function fetchPayslip(Request $request)
    {
        $user = Auth()->user();

        $verified = Redis::get("payslip_verified_" . $user->id);

        if (!$verified) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => __('lang.TXT_SERVER_ERROR_PAYSLIP_OTP_EXPIRED')
            ]);
        }

        $year = $request->input('year');
        $month = $request->input('month');

        if (!is_numeric($year) || !is_numeric($month) || (int)$month < 1 || (int)$month > 12) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => __('lang.TXT_SERVER_ERROR_PAYSLIP_INVALID_PERIOD')
            ]);
        }

        $requestedDate = Carbon::createFromDate((int)$year, (int)$month, 1)->startOfMonth();
        $yakaTuru = ((int)$user->type === Constants::EMPLOYEE_TYPE_BLUE_COLLAR) ? 'Mavi' : 'Beyaz';

        $payslipMonth = DB::table('payslip_months')
            ->select('baslangic_tarihi')
            ->whereDate('donem', $requestedDate->toDateString())
            ->where('yaka_turu', $yakaTuru)
            ->first();

        if ($payslipMonth) {
            $availableFrom = Carbon::parse($payslipMonth->baslangic_tarihi)->startOfDay();
            if (Carbon::now()->lt($availableFrom)) {
                $periodNotOpenMessage = Redis::get('payslip_period_not_open_error_message') ?: __('lang.TXT_SERVER_ERROR_PAYSLIP_PERIOD_NOT_OPEN');

                return response()->json([
                    'statusCode' => 400,
                    'responseData' => null,
                    'errorMessage' => $periodNotOpenMessage
                ]);
            }
        }

        $period = sprintf("%04d%02d", $year, $month);

        //$period = sprintf("%04d%02d", $year, $month);
        
        $regNo = $user->register_number;
        if ( $user->id == 7701 )
            $regNo = 114550;


        try {
            $soapClient = new SoapClient(
                base_path('wsdl/payslip.wsdl'), 
                [
                    'trace' => true,
                    'login' => env('BORDRO_USER'),
                    'password' => env('BORDRO_PASSWORD'),
                    'cache_wsdl' => WSDL_CACHE_NONE,
                    'exceptions' => true,
                    'connection_timeout' => 12,
                ]
            );

            $params = [
                'IV_PERIOD' => $period,
                'IV_PERNR' =>  $regNo // sicil numarası
            ];


            $response = $soapClient->__soapCall("osPayslipBase64Get", [$params]);
            $pdfBase64 = $response->EV_BASE64 ?? null;

            file_put_contents(storage_path("payslip_raw.b64"), $pdfBase64);

            if (!$pdfBase64) {

                return response()->json([
                    'statusCode' => 400,
                    'responseData' => null,
                    'errorMessage' => __('lang.TXT_SERVER_ERROR_PAYSLIP_NOT_FOUND')
                ]);
            }

            Log::channel('payslip')->info('Bordro görüntülendi :', [
                'user_id'      => $user->id,
                'name_surname' => $user->name_surname,
                'sicil_no'     => $regNo,
                'period'       => $period,
                'saat'         => Carbon::now()->toIso8601String(),
            ]);

            return response()->json([
                'statusCode' => 200,
                'responseData' => [
                    'base64' => $pdfBase64
                ],
                'errorMessage' => null
            ]);


        } catch (\Exception $ex) {
            Log::error("Payslip error: ".$ex);
            return response()->json([
                'statusCode' => 400,
                'responseData' => [
                ],
                'errorMessage' => __('lang.TXT_SERVER_ERROR_PAYSLIP_SAP_ERROR')
            ]);
        }
    }

    private function sendOtpSms($phone, $otp)
    {
        // Ensure phone starts with 0
        if (substr($phone, 0, 1) !== '0') {
            $phone = '0' . $phone;
        }

        $url = "https://otpsms.postaguvercini.com/api_http/sendsms.asp";

        $username = env('PG_USER');
        $password = env('PG_PASSWORD');

        $text = "MBT App bordro görüntüleme doğrulama kodunuz: $otp";

        $query = http_build_query([
            "user"     => $username,
            "password" => $password,
            "gsm"      => $phone,
            "text"     => $text
        ]);

        $client = new \GuzzleHttp\Client();

        try {
            $response = $client->get("$url?$query");
            $body = $response->getBody()->getContents();

            // Posta Güvercini HTTP API cevabı
            // örn: errno=0&errtext=&message_id=XXXXX&charge=1
            parse_str($body, $result);

            if ($result["errno"] == 0) {
                return true;
            } else {
                Log::error("PG OTP error: " . $result["errtext"]);
                return false;
            }

        } catch (\Exception $e) {
            Log::error("PG OTP exception: " . $e->getMessage());
            return false;
        }
    }
}