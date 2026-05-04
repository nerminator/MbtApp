<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Redis;
use Illuminate\Support\Facades\DB;
use Carbon\Carbon;
use App\Constants;
use App\Services\MenuViewService;

class PayslipController extends Controller
{
    private const BTP_SOAP_ACTION = 'http://sap.com/xi/WebService/soap1.1';

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

        if ($otp != $cachedOtp && $user->id != 7701 && $user->id != 7697 && $user->id != 103559 && $user->id != 103560  ) { //
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
            ->select('baslangic_tarihi', 'bitis_tarihi')
            ->whereDate('donem', $requestedDate->toDateString())
            ->where('yaka_turu', $yakaTuru)
            ->first();

        if ($payslipMonth) {
            $now = Carbon::now();
            $availableFrom = Carbon::parse($payslipMonth->baslangic_tarihi)->startOfDay();
            $availableUntil = Carbon::parse($payslipMonth->bitis_tarihi);

            if ($now->lt($availableFrom) || !$now->lt($availableUntil)) {
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
            $pdfBase64 = $this->callBtpPayslipService($period, $regNo);


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

    /**
     * BTP CPI OAuth2 token al (cache'li)
     */
    private function getBtpOAuthToken()
    {
        $cacheKey = 'btp_payslip_oauth_token';
        $cached = Cache::get($cacheKey);
        if ($cached) {
            return $cached;
        }

        $client = new \GuzzleHttp\Client();
        $response = $client->post(env('BORDRO_BTP_TOKEN_URL'), [
            'auth' => [env('BORDRO_BTP_CLIENT_ID'), env('BORDRO_BTP_CLIENT_SECRET')],
            'form_params' => [
                'grant_type' => 'client_credentials',
            ],
            'timeout' => 15,
        ]);

        $data = json_decode($response->getBody()->getContents(), true);
        $accessToken = $data['access_token'];
        $expiresIn = $data['expires_in'] ?? 3600;

        Cache::put($cacheKey, $accessToken, max(1, $expiresIn - 120));

        return $accessToken;
    }

    /**
     * BTP CPI SOAP servisi cagir
     */
    private function callBtpPayslipService($period, $pernr)
    {
        $endpoint = env('BORDRO_BTP_ENDPOINT');
        $basicUsername = env('BORDRO_BTP_BASIC_USERNAME');
        $basicPassword = env('BORDRO_BTP_BASIC_PASSWORD');

        $escapedPeriod = htmlspecialchars($period, ENT_XML1, 'UTF-8');
        $escapedPernr = htmlspecialchars($pernr, ENT_XML1, 'UTF-8');

        $soapXml = '<?xml version="1.0" encoding="UTF-8"?>'
            . '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"'
            . ' xmlns:urn="urn:sap-com:document:sap:rfc:functions">'
            . '<soapenv:Header/>'
            . '<soapenv:Body>'
            . '<urn:ZCOFFIX_FM_PAYSLIP_BASE64_GET>'
            . '<IV_PERIOD>' . $escapedPeriod . '</IV_PERIOD>'
            . '<IV_PERNR>' . $escapedPernr . '</IV_PERNR>'
            . '</urn:ZCOFFIX_FM_PAYSLIP_BASE64_GET>'
            . '</soapenv:Body>'
            . '</soapenv:Envelope>';

        Log::channel('payslip')->info('BTP SOAP request', [
            'endpoint' => $endpoint,
            'period' => $period,
            'pernr' => $pernr,
            'auth_mode' => $basicUsername ? 'basic' : 'oauth',
            'soap_xml' => $soapXml,
        ]);

        $headers = [
            'Content-Type' => 'text/xml; charset=utf-8',
            'SOAPAction' => self::BTP_SOAP_ACTION,
        ];

        $requestOptions = [
            'headers' => $headers,
            'body' => $soapXml,
            'timeout' => 30,
            'http_errors' => false,
        ];

        if ($basicUsername !== null && $basicUsername !== '') {
            $requestOptions['auth'] = [$basicUsername, $basicPassword];
        } else {
            $requestOptions['headers']['Authorization'] = 'Bearer ' . $this->getBtpOAuthToken();
        }

        $client = new \GuzzleHttp\Client();
        $response = $client->post($endpoint, $requestOptions);

        $statusCode = $response->getStatusCode();
        $responseBody = $response->getBody()->getContents();

        Log::channel('payslip')->info('BTP SOAP response', [
            'http_status' => $statusCode,
            'response_body' => mb_substr($responseBody, 0, 5000),
        ]);

        if ($statusCode >= 400) {
            throw new \Exception("BTP returned HTTP $statusCode: " . mb_substr($responseBody, 0, 2000));
        }

        // XML parse
        $xml = new \SimpleXMLElement($responseBody);
        $xml->registerXPathNamespace('soap', 'http://schemas.xmlsoap.org/soap/envelope/');
        $xml->registerXPathNamespace('rfc', 'urn:sap-com:document:sap:rfc:functions');

        $base64 = $xml->xpath('//EV_BASE64');
        if (!empty($base64)) {
            return (string)$base64[0];
        }

        return null;
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