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
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Log;

class SocialClubsController extends Controller
{
  public function getSocialClubLocs()
  {
    $result = DB::table('social_clubs_locations')->orderBy('id')->get();
    $locs = [];
    foreach ($result as $item) {
        $locs[] = [
            'id' => $item->id,
            'location' => $item->location
        ];
    }
    return response()->json([
        'statusCode' => 200,
        'responseData' => [
            'locs' => $locs
        ],
        'errorMessage' => null
    ]);
  }

  public function getSocialClubs($loc_id)
  {
    $sqlQuery = "select n.*,
							( select ni.image
                                from news_images ni
                                where ni.news_id = n.id
                                LIMIT 1
                            ) as image                            
                    from news n
                    where n.status <> 0 and n.loc_id = ?
                    order by n.order";

    $result = DB::select($sqlQuery, [$loc_id]);
    return response()->json([
        'statusCode' => 200,
        'responseData' => [
            'clubs' => $result
        ],
        'errorMessage' => null
    ]);
  }

  public static function getClubResponsibles($club_id)
  {
      $result = DB::table('social_clubs_detail')->where('club_id',$club_id)->orderBy('id')->get();
      $respList = [];
      foreach ($result as $item) {
          $respList[] = [
              'id' => $item->id,
              'responsible' => $item->responsible,
              'contact' => $item->contact
          ];
      }
      return $respList;
  }

}