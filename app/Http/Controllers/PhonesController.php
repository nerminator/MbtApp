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
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Redis;
use App\Services\MenuViewService;

class PhonesController extends Controller
{
    public function getPhoneLocs()
    {
        $result = DB::table('phone_number_locations')->orderBy('id')->get();
        $locs = [];
        foreach ($result as $item) {
            $locs[] = [
                'id' => $item->id,
                'location' => $item->location
            ];
        }
    
        MenuViewService::increment('EmergencyNumbers');

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                'locs' => $locs
            ],
            'errorMessage' => null
        ]);
    }

  public function getPhones($loc_id = 0)
  {
    $result_santral = DB::table('phone_number_locations')->where('id',$loc_id)->first();
    $santral = $result_santral->santral;

    $result = DB::table('phone_numbers')->where('loc_id',$loc_id) ->orderBy('order')->get();
    $phones = [];
    foreach ($result as $item) {
        $phones[] = [
            'id' => $item->id,
            'name' => $item->name,
            'details' => self::getPhoneDetails($item->id)
        ];
    }
    return response()->json([
        'statusCode' => 200,
        'responseData' => [
            'santral' => $santral, 
            'phones' => $phones
        ],
        'errorMessage' => null
    ]);
  }

  public static function getPhoneDetails($phone_id)
  {
      $result = DB::table('phone_numbers_detail')->where('phone_id',$phone_id)->orderBy('id')->get();
      $details = [];
      foreach ($result as $item) {
          $details[] = [
              'id' => $item->id,
              'unit' => $item->unit,
              'note' => $item->note,
              'internal' => $item->internal
          ];
      }
      return $details;
  }

}