<?php

namespace App\Http\Middleware;

use Closure;
use Illuminate\Contracts\Auth\Factory as Factory;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Auth as Auth;

class Authenticate
{
    /**
     * The authentication guard factory instance.
     *
     * @var \Illuminate\Contracts\Auth\Factory
     */
    protected $auth;

    /**
     * Create a new middleware instance.
     *
     * @param  \Illuminate\Contracts\Auth\Factory  $auth
     * @return void
     */
    public function __construct(Factory $auth)
    {
        $this->auth = $auth;
    }

    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \Closure  $next
     * @param  string|null  $guard
     * @return mixed
     */
  
     public function handle($request, Closure $next, $guard = null)
     { 

        // ✅ Detect old app usage via 'token' header
        if ($request->hasHeader('token')) {
            // Optional: log for tracking
            //Log::warning('Old app attempted access with token: ' . $request->header('token'));

            // ❌ Reject with clear message and Upgrade Required HTTP status

            return response()->json([
                'statusCode' => 200,
                'responseData' => null,
                'errorMessage' => 'Lütfen yeni uygulamayı indiriniz.'
            ]);

            //return response()->json([
            //    'message' => 'Your app version is no longer supported. Please download the new version.'
            //], 426); // 426 Upgrade Required
        }

        if ($this->auth->guard($guard)->guest()) {
            return response('Unauthorized.', 401);
        }
        return $next($request);
     }

}
