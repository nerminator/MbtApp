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

        if (app()->environment('production')) {
            $frameOrigin = config('app.panel_frame_origin_production');
        } elseif (app()->environment('local')) {
            $frameOrigin = config('app.panel_frame_origin_local');
        } else {
            $frameOrigin = config('app.panel_frame_origin_staging');
        }

        $response->headers->set('X-Frame-Options', 'allow-from ' . rtrim($frameOrigin, '/') . '/');
        $response->headers->set('Strict-Transport-Security', 'max-age=31536000; includeSubDomains; preload');

        $response->headers->set('Cache-Control', 'no-store, no-cache');
        $response->headers->set('Pragma', 'no-cache');


        return $response;
    }
}