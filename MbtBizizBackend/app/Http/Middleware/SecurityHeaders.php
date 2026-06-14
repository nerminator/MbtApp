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

        $frameBaseUrl = rtrim((string) config('app.url', url('/')), '/');
        $response->headers->set('X-Frame-Options', 'allow-from ' . $frameBaseUrl . '/');
        $response->headers->set('Strict-Transport-Security', 'max-age=31536000; includeSubDomains; preload');

        $response->headers->set('Cache-Control', 'no-store, no-cache');
        $response->headers->set('Pragma', 'no-cache');

        $response->headers->set('Content-Security-Policy', "script-src 'self'");

        return $response;
    }
}
