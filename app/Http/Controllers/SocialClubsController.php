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

class SocialClubsController extends Controller
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
    public static function getClubLocs()
    {
        return self::getSocialClubsLocations();
    }

    public static function getSocialClubsLocations()
    {
        $resultLoc = DB::table('social_clubs_locations')->get();
        $locs = [];
        foreach ($resultLoc as $item) {
            $locs[] = [
                'id' => $item->id,
                'location' => $item->location,
                'clubs' => self::getClubsAtLocation($item->id)
            ];
        } 
        //Log::info($locs);
        return $locs;
    }

    public static function getClubsAtLocation($loc_id)
    {
        $result = DB::table('news')->where('loc_id',$loc_id) ->orderBy('order')->get();
        $sqlQuery = "select n.*,
							(
                                select ni.image
                                from news_images ni
                                where ni.news_id = n.id
                                LIMIT 1
                              ) as image                            
                    from news n
                    where n.status <> 0 and n.loc_id = ?
                    order by n.order";

        $result = DB::select($sqlQuery, [$loc_id]);
        
        /*$clubs = [];
        foreach ($result as $item) {
            $clubs[] = [
                'id' => $item->id,
                'name' => $item->name,
                'respList' => self::getClubResponsibles($item->id)
            ];
        }*/
        return $result;
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

    public function addClubLoc()
    {
        $newId = DB::table('social_clubs_locations')->insertGetId([] );
        return json_encode($newId);

    }
    public function deleteClubLoc(Request $request)
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
        $affectedRowCount = DB::table('social_clubs_locations')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);
    }

    public function addClub(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'loc_id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
        $loc_id = $request->input('loc_id'); 
        $newId = DB::table('social_clubs')->insertGetId(['loc_id' => $loc_id] );
        return json_encode($newId );

    }
    public function deleteClub(Request $request)
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
        $affectedRowCount = DB::table('social_clubs')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);
    }

    public function addClubDetail(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'club_id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
        $club_id = $request->input('club_id'); 
        $newId = DB::table('social_clubs_detail')->insertGetId(['club_id' => $club_id] );
        return json_encode($newId);
    }
    public function deleteClubDetail(Request $request)
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
        $affectedRowCount = DB::table('social_clubs_detail')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);

    }

    public function updateClubLoc(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'name' => 'nullable|string|max:100'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $name = $request->input('name'); 

        $affectedRowCount = DB::table('social_clubs_locations')->where('id', $id)->update(['location' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updateClub(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'name' => 'nullable|string|max:100'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $name = $request->input('name'); 

        $affectedRowCount = DB::table('social_clubs')->where('id', $id)->update(['name' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updateClubPersonName(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'name' => 'nullable|string|max:100'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $name = $request->input('name'); 

        $affectedRowCount = DB::table('social_clubs_detail')->where('id', $id)->update(['responsible' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updateClubPersonContact(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'name' => 'nullable|string|max:100'
        ]); 
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $name = $request->input('name'); 

        $affectedRowCount = DB::table('social_clubs_detail')->where('id', $id)->update(['contact' => $name]);

        return json_encode($affectedRowCount > 0);
    }

}