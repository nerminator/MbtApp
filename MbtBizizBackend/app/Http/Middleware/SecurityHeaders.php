<?php

/** Added to resolve EPA finding */

namespace App\Http\Middleware;
 
use Closure;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\App;
 
class SecurityHeaders
{
    public function handle(Request $request, Closure $next)
    {
        $response = $next($request);
 
        if (app()->environment() == "production") {
            $response->headers->set('X-Frame-Options', 'allow-from https://bizizapp.com/');
        }else{
            $response->headers->set('X-Frame-Options', 'allow-from http://localhost:8002/');
        }
        $response->headers->set('Strict-Transport-Security', 'max-age=31536000; includeSubDomains; preload');

        $response->headers->set('Cache-Control', 'no-store, no-cache');
        $response->headers->set('Pragma', 'no-cache');

        $response->headers->set('Content-Security-Policy', "script-src 'self'");

        return $response;
    }
}
