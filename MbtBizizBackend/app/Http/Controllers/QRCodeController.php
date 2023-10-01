<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 21.01.2020
 * Time: 21:43
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;

class QRCodeController extends Controller
{
    public function sendQRCode(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'code' => 'required|string'
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

        $email = Auth::user()->email;
        if (is_null($email) || empty($email) || $email == "TEST@TEST.COM") {
            return response()->json([
                'statusCode' => 401,
                'responseData' => null,
                'errorMessage' => "Sistemde kayıtlı bir email adresiniz mevcut değildir."
            ]);
        }

        try {
            $code = Input::get('code');
            $text = $code . PHP_EOL . $email;
            $timeFormat = "d-m-Y_H-i-su";
            if (app()->environment() == "production") {
                $fileName = "/var/www/html/bizizFiles/qr/unprocessed/" . Carbon::now()->format($timeFormat) . "_" . $this->_generateRandomString();
            } else {
                $fileName = Carbon::now()->format($timeFormat) . "_" . $this->_generateRandomString();
            }
            file_put_contents($fileName, $text);
            return response()->json([
                'statusCode' => 200,
                'responseData' => [
                    'code' => $code
                ],
                'errorMessage' => null
            ]);
        } catch (\Exception $exception) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => [
                    'code' => $code
                ],
                'errorMessage' => "Bir hata oluştu. İşlem tamamlanamadı."
            ]);
        }

        /*$code = Input::get('code');
        if ($code == '4f)XT=vaE$R9SRMe') {
            return response()->json([
                'statusCode' => 200,
                'responseData' => [
                    'code' => $code
                ],
                'errorMessage' => null
            ]);
        } else {
            return response()->json([
                'statusCode' => 401,
                'responseData' => [
                    'code' => $code
                ],
                'errorMessage' => "Hatalı QR kod gönderildi."
            ]);
        }*/
    }

    private function _generateRandomString($length = 7)
    {
        $characters = '0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ';
        $charactersLength = strlen($characters);
        $randomString = '';
        for ($i = 0; $i < $length; $i++) {
            $randomString .= $characters[rand(0, $charactersLength - 1)];
        }
        return $randomString;
    }
}