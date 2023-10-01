<?php

namespace App\Http\Middleware;

use Closure;

class FrameHeadersMiddleware
{
    /**
     * Handle an incoming request.
     *
     * @param  \Illuminate\Http\Request $request
     * @param  \Closure $next
     * @return mixed
     */
    public function handle($request, Closure $next)
    {
        $response = $next($request);

        $response->header('X-Frame-Options', 'allow-from https://bizizapp.com/');
        $response->header('Strict-Transport-Security', 'max-age=31536000; includeSubDomains; preload');

        $response->header('Cache-Control', 'no-store, no-cache');
        $response->header('Pragma', 'no-cache');

        return $response;
    }
}