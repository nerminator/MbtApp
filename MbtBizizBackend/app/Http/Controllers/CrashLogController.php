<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Validator;
use Carbon\Carbon;  // Import Carbon class

class CrashLogController extends Controller
{
    public function sendCrashLog(Request $request)
    {
        // Validate required and optional fields
        $validator = Validator::make($request->all(), [
            'platform' => 'required|string|max:50',
            'message' => 'required|string|max:1000',
            'stack_trace' => 'nullable|string',
            'device_model' => 'nullable|string|max:100',
            'os_version' => 'nullable|string|max:50',
            'app_version' => 'nullable|string|max:50',
        ]);

        if ($validator->fails()) {
            return response()->json([
                'statusCode' => 400,
                'responseData' => null,
                'errorMessage' => 'Eksik veya hatalı parametre(ler) var!'
            ]);
	}

	// Check if user is authenticated and get user ID
        $userId = null;
        if (auth()->check()) {
            $userId = auth()->id(); // Get the authenticated user's ID
        }

        // Insert into DB
        $inserted = DB::table('crash_logs')->insert([
            'platform' => $request->input('platform'),
            'message' => $request->input('message'),
            'stack_trace' => $request->input('stack_trace'),
            'device_model' => $request->input('device_model'),
            'os_version' => $request->input('os_version'),
	    'app_version' => $request->input('app_version'),
	    'user_id' => $userId, // Add the user ID if logged in
            'created_at' => Carbon::now(),
        ]);

        if ($inserted) {
            return response()->json([
                'statusCode' => 200,
                'responseData' => [],
                'errorMessage' => null
            ]);
        } else {
            return response()->json([
                'statusCode' => 500,
                'responseData' => null,
                'errorMessage' => 'Kayıt sırasında bir hata oluştu.'
            ]);
        }
    }
}
