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
use Illuminate\Support\Facades\Redis;

class PhoneNumbersController extends Controller
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

    public function updateSantral(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'id' => 'required|integer|min:1',
            'name' => 'nullable|string|max:7'
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
      
        $id = $request->input('id'); 
        $name = $request->input('name'); 

        $affectedRowCount = DB::table('phone_number_locations')->where('id', $id)->update(['santral' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public static function getLocations()
    {
        $resultLoc = DB::table('phone_number_locations')->get();
        $locs = [];
        foreach ($resultLoc as $item) {
            $locs[] = [
                'id' => $item->id,
                'location' => $item->location,
                'santral' => $item->santral,
                'phones' => self::getPhonesAtLocation($item->id)
            ];
        } 
        return $locs;
    }

    public static function getPhonesAtLocation($loc_id)
    {
        $result = DB::table('phone_numbers')->where('loc_id',$loc_id) ->orderBy('order')->get();
        $list = [];
        foreach ($result as $item) {
            $list[] = [
                'id' => $item->id,
                'name' => $item->name,
                'details' => self::getPhoneDetails($item->id)
            ];
        }
        return $list;
    }

    public static function getPhoneDetails($phone_id)
    {
        $result = DB::table('phone_numbers_detail')->where('phone_id',$phone_id)->orderBy('id')->get();
        $list = [];
        foreach ($result as $item) {
            $list[] = [
                'id' => $item->id,
                'unit' => $item->unit,
                'note' => $item->note,
                'internal' => $item->internal
            ];
        }
        return $list;
    }

    public function addPhoneLoc()
    {
        $newId = DB::table('phone_number_locations')->insertGetId([] );
        return json_encode($newId);
    }
    public function deletePhoneLoc(Request $request)
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
        $affectedRowCount = DB::table('phone_number_locations')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);
    }

    public function addPhone(Request $request)
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
        $newId = DB::table('phone_numbers')->insertGetId(['loc_id' => $loc_id] );
        return json_encode($newId );

    }
    public function deletePhone(Request $request)
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
        $affectedRowCount = DB::table('phone_numbers')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);
    }

    public function addPhoneDetail(Request $request)
    {
        //region Controls
        $validator = Validator::make($request->all(), [
            'phone_id' => 'required|integer|min:1',
        ]);
        if ($validator->fails()) // missing parameters
        {
            return -1;
        }
        //endregion
        $phone_id = $request->input('phone_id'); 
        $newId = DB::table('phone_numbers_detail')->insertGetId(['phone_id' => $phone_id] );
        return json_encode($newId);
    }
    public function deletePhoneDetail(Request $request)
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
        $affectedRowCount = DB::table('phone_numbers_detail')->where('id', $id)->delete();
        return json_encode($affectedRowCount > 0);

    }

    public function updatePhoneLoc(Request $request)
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

        $affectedRowCount = DB::table('phone_number_locations')->where('id', $id)->update(['location' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updatePhone(Request $request)
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

        $affectedRowCount = DB::table('phone_numbers')->where('id', $id)->update(['name' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updatePhoneUnit(Request $request)
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

        $affectedRowCount = DB::table('phone_numbers_detail')->where('id', $id)->update(['unit' => $name]);

        return json_encode($affectedRowCount > 0);
    }

    public function updatePhoneNote(Request $request)
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

        $affectedRowCount = DB::table('phone_numbers_detail')->where('id', $id)->update(['note' => $name]);

        return json_encode($affectedRowCount > 0);
    }
    public function updatePhoneInternal(Request $request)
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

        $affectedRowCount = DB::table('phone_numbers_detail')->where('id', $id)->update(['internal' => $name]);

        return json_encode($affectedRowCount > 0);
    }

}