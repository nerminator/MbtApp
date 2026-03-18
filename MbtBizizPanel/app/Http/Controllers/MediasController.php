<?php
/**
 * Created by
 * User: Abdullah.Soylemez
 * Date: 22.6.2017
 * Time: 11:52
 */

namespace App\Http\Controllers;

use Carbon\Carbon;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Input;
use Illuminate\Support\Facades\Log;

class MediasController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('auth');
    }

    /**
     * Show the application dashboard.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {

    }

    public static function getMedias()
    {
        $results = DB::table('social_media')->get();
        $medias = [];
        foreach ($results as $item) {
            $medias[] = [
                'id' => $item->id,
                'name' => $item->name,
                'details' => self::getMediaDetails($item->id)
            ];
        } 
        return $medias;
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

    public function addMedia()
    {
        $newId = DB::table('social_media')->insertGetId([] );
        return json_encode($newId);

    }
    
    public function deleteMedia(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
        $id = $request->input('id'); 
        $affectedRowCount = DB::table('social_media')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);
    }

    public function addMediaDetail(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'media_id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
        $media_id = $request->input('media_id'); 
        $newId = DB::table('social_media_detail')->insertGetId(['media_id' => $media_id] );
        return json_encode($newId );

    }
    public function deleteMediaDetail(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
        $id = $request->input('id'); 
        $affectedRowCount = DB::table('social_media_detail')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);
    }

    public function updateMedia(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'name' => 'nullable|string|max:55'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $name = $request->input('name'); 

        $affectedRowCount = DB::table('social_media')->where('id', $id)->update(['name' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updateMediaAccount(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'account' => 'nullable|string|max:100'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $account = $request->input('account'); 

        $affectedRowCount = DB::table('social_media_detail')->where('id', $id)->update(['account' => $account]);

        return json_encode($affectedRowCount > 0);
    }

    public function updateMediaUrl(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'url' => 'nullable|string|max:200'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $url = $request->input('url'); 

        $affectedRowCount = DB::table('social_media_detail')->where('id', $id)->update(['url' => $url]);

        return json_encode($affectedRowCount > 0);
    }

}