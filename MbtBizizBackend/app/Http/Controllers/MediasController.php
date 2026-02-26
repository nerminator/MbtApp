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
use App\Services\MenuViewService;

class MediasController extends Controller
{
    public function getMedias()
    {
    
      $result = DB::table('social_media')->orderBy('order')->get();
      $medias = [];
      foreach ($result as $item) {
          $medias[] = [
              'id' => $item->id,
              'name' => $item->name,
              'details' => self::getMediaDetails($item->id)
          ];
      }
      MenuViewService::increment('SocialMedia');
      return response()->json([
          'statusCode' => 200,
          'responseData' => [
              'medias' => $medias
          ],
          'errorMessage' => null
      ]);
    }

  public static function getMediaDetails($media_id)
  {
      $result = DB::table('social_media_detail')->where('media_id',$media_id)->orderBy('order')->get();
      $details = [];
      foreach ($result as $item) {
          $details[] = [
              'id' => $item->id,
              'account' => $item->account,
              'url' => $item->url
          ];
      }
      return $details;
  }
}
