<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 4.01.2019
 * Time: 20:16
 */

namespace App\Http\Controllers;


use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Lang;
use Illuminate\Support\Facades\Validator;

class InitController
{
    private const ANDROID = 1;
    private const IOS = 2;

    private const BUTTON_TYPE_CONTINUE = 1;
    private const BUTTON_TYPE_DOWNLOAD = 2;

    public function init(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'version' => 'required|regex:/^[0-9.]+$/',
            'osType' => 'required|integer|min:1|max:2' // android: 1, ios: 2
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

        try {
            $userVersion = explode('.', $request->input('version'));
            if (count($userVersion) < 2) {
                return response()->json([
                    'statusCode' => 200,
                    'responseData' => null,
                    'errorMessage' => null
                ]);
            }

            if (count($userVersion) == 2) {
                $userVersion[] = 0;
            }

            $versionPopup = [];

	            // Only check for version differences if newApp flag is true
        if ($request->boolean('newApp')) {
            $currentVersion = $request->input('osType') == self::ANDROID ? explode('.', env('ANDROID_VERSION')) : explode('.', env('IOS_VERSION'));
            if ($currentVersion[0] > $userVersion[0]) {
                $versionPopup = [
                    'title' => Lang::get("lang.TXT_VERSION_POPUP_MAJOR_UPDATE_TITLE"),
                    'message' => Lang::get("lang.TXT_VERSION_POPUP_MAJOR_UPDATE_MESSAGE"),
                    'buttonList' => [
                        [
                            'type' => self::BUTTON_TYPE_DOWNLOAD,
                            'text' => Lang::get("lang.TXT_VERSION_POPUP_DOWNLOAD_BUTTON"),
                            'url' => env('DOWNLOAD_URL')
                        ]
                    ]
                ];
            } elseif ($currentVersion[0] == $userVersion[0] && $currentVersion[1] > $userVersion[1]) {
                $versionPopup = [
                    'title' => Lang::get("lang.TXT_VERSION_POPUP_MAJOR_UPDATE_TITLE"),
                    'message' => Lang::get("lang.TXT_VERSION_POPUP_MAJOR_UPDATE_MESSAGE"),
                    'buttonList' => [
                        [
                            'type' => self::BUTTON_TYPE_DOWNLOAD,
                            'text' => Lang::get("lang.TXT_VERSION_POPUP_DOWNLOAD_BUTTON"),
                            'url' => env('DOWNLOAD_URL')
                        ]
                    ]
                ];
            } elseif ($currentVersion[0] == $userVersion[0] && $currentVersion[1] == $userVersion[1] && $currentVersion[2] > $userVersion[2]) {
                $versionPopup = [
                    'title' => Lang::get("lang.TXT_VERSION_POPUP_MINOR_UPDATE_TITLE"),
                    'message' => Lang::get("lang.TXT_VERSION_POPUP_MINOR_UPDATE_MESSAGE"),
                    'buttonList' => [
                        [
                            'type' => self::BUTTON_TYPE_DOWNLOAD,
                            'text' => Lang::get("lang.TXT_VERSION_POPUP_DOWNLOAD_BUTTON"),
                            'url' => env('DOWNLOAD_URL')
                        ],
                        [
                            'type' => self::BUTTON_TYPE_CONTINUE,
                            'text' => Lang::get("lang.TXT_VERSION_POPUP_CONTINUE_BUTTON"),
                            'url' => null
                        ]
                    ]
                ];
            }
        }

        // Add fallback support contact info if versionPopup is still empty
        if (empty($versionPopup)) {
            $versionPopup = [
                'callPhone' => "02128673399",
                'phoneList' => [
                    [
                        'label' => Lang::get("lang.TXT_SUPPORT_PHONE_LABEL_1"),
                        'phone' => "0212 8673399"
                    ],
                    [
                        'label' => Lang::get("lang.TXT_SUPPORT_PHONE_LABEL_2"),
                        'phone' => ""
                    ],
                    [
                        'label' => Lang::get("lang.TXT_SUPPORT_PHONE_LABEL_3"),
                        'phone' => "2222"
                    ]
                ]
            ];
        }

            return response()->json([
                'statusCode' => 200,
                'responseData' => $versionPopup,
                'errorMessage' => null
            ]);
        } catch (\Exception $e) {
            return response()->json([
                'statusCode' => 200,
                'responseData' => null,
                'errorMessage' => $e->getMessage()
            ]);
        }
    }

    public function userConfig() {
        //$shouldShowQrCode = in_array(Auth::user()->email, ["SINEM.SASMAZEL@DAIMLER.COM", "NIL.ICKIN@DAIMLER.COM", "SECIL.ACAR@DAIMLER.COM",
        //                                                    "BILGEADAM.BOZKURT@DAIMLER.COM", "SEMIH_ALPEREN.KAYAALTI@DAIMLER.COM", "MBT.SARISALTIK@DAIMLER.COM",
        //                                                    "TEST@TEST.COM"]);

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'shouldShowQrCode' => false //$shouldShowQrCode
            ],
            'errorMessage' => null
        ]);
    }
}
