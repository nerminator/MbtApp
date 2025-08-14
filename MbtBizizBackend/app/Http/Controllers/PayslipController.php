<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Cache;
use SoapClient;

class PayslipController extends Controller
{
    /**
     * Kullanıcıya OTP gönderir
     */
    public function requestOtp(Request $request)
    {
        $user = auth()->user();

        // örnek 6 haneli OTP
        $otp = rand(100000,999999);

        // otp yi session/cache veritabanına yazabilirsiniz
        
        Cache::put("payslip_otp_" . $user->id, $otp, 300); // 5 dk geçerli
 
        // SMS gönderme
        // burada örnek: SmsService::send($user->phone, "Bordro OTP kodunuz: $otp");
        // SMS provider'a göre implement edeceksiniz
        $this->sendOtpSms($user->phone, "Bordro OTP kodunuz: $otp");
        Log::info("Payslip OTP sent to user: " . $user->id);

        return response()->json([
            'status' => 'success',
            'message' => 'OTP gönderildi'
        ]);
    }

    /**
     * Kullanıcının OTP doğrulamasını kontrol eder
     */
    public function verifyOtp(Request $request)
    {
        $user = auth()->user();
        $otp = $request->input('otp');

        $cachedOtp = Cache::get("payslip_otp_" . $user->id);

        if ($otp != $cachedOtp) {
            // OTP expired / invalid
            return response()->json([
                'statusCode' => 401,             // <— internal code = WSStatusCode.authorizationError
                'errorMessage' => 'Kod yanlış veya kullanım süresi doldu'
            ], 200); 
        }

        // flag set edelim
        Cache::put("payslip_verified_" . $user->id, true, 300);

        return response()->json([
            'status' => 'success',
            'message' => 'OTP doğrulandı'
        ]);
    }

    /**
     * SAP bordro servisini çağırır ve Base64 döner
     */
    public function fetchPayslip(Request $request)
    {
        $user = Auth()->user();

        $verified = Cache::get("payslip_verified_" . $user->id);

        if (!$verified) {
            return response()->json(['status'=>'fail', 'message'=>'OTP doğrulanmadı'], 403);
        }

        $year = $request->input('year');
        $month = $request->input('month');

        $period = sprintf("%04d%02d", $year, $month);

        Log::debug("Fetching payslip for employee_id: {$user->register_number}, period: {$period}");

        try {
            $soapClient = new SoapClient(
                base_path('wsdl/payslip.wsdl'), 
                [
                    'trace' => true,
                    'login' => env('BORDRO_USER'),
                    'password' => env('BORDRO_PASSWORD'),
                    'cache_wsdl' => WSDL_CACHE_NONE,
                    'exceptions' => true,
                    'connection_timeout' => 10,
                ]
            );

            $params = [
                'IV_PERIOD' => $period,
                'IV_PERNR' => $user->register_number // sicil numarası
            ];

            Log::debug("SOAP params: ", $params);

            $response = $soapClient->__soapCall("osPayslipBase64Get", [$params]);

            Log::debug("SOAP response: ", (array) $response);

            $pdfBase64 = $response->EV_BASE64 ?? null;

            Log::debug("Base64 length: " . strlen($pdfBase64));
            file_put_contents(storage_path("payslip_raw.b64"), $pdfBase64);

            if (!$pdfBase64) {
                return response()->json(['status'=>'fail', 'message'=>'Bordro bulunamadı']);
            }

            // LOG işlemi
            Log::info('PayslipView', ['user'=>$user->id, 'period'=>$period, 'ts'=>now()->toIso8601String()]);

            return response()->json([
                'status' => 'success',
                'data' => [
                    'base64' => $pdfBase64
                ]
            ]);

        } catch (\Exception $ex) {
            Log::error("Payslip error: ".$ex->getMessage());
            return response()->json(['status'=>'fail', 'message'=>'SAP servisi hatası']);
        }
    }

    private function sendOtpSms($phone, $otp)
    {
        $url = "https://otpsms.postaguvercini.com/api_http/sendsms.asp";

        $username = env('PG_USER');
        $password = env('PG_PASSWORD');

        $text = "Bordro OTP kodunuz: $otp";

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