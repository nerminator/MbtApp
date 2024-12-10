<?php

namespace App\Providers;

use App\User;
use Illuminate\Support\Facades\Http;
use Illuminate\Support\ServiceProvider;
use Firebase\JWT\JWT;
use Firebase\JWT\JWK;
use Firebase\JWT\Key;
use Laravel\Socialite\Facades\Socialite;
use \stdClass;

class AuthServiceProvider extends ServiceProvider
{
    /**
     * Register any application services.
     *
     * @return void
     */
    public function register()
    {
        //
    }

    /**
     * Boot the authentication services for the application.
     *
     * @return void
     */
    public function boot()
    {
        // Here you may define how you wish users to be authenticated for your Lumen
        // application. The callback which receives the incoming request instance
        // should return either a User instance or null. You're free to obtain
        // the User instance via an API token or any other method necessary.

        $this->app['auth']->viaRequest('api', function ($request) {
            if ($request->header('token')) {
                if ($request->header('lang') == 'en') {
                    app('translator')->setLocale('en');
                    setlocale(LC_TIME, null);
                    \Carbon\Carbon::setLocale('en');
                }

                return User::where('token', $request->header('token'))
                            ->where('status', 1)->first();
            } elseif ($request->header('Authorization')) {
                $token = str_replace('Bearer ', '', $request->header('Authorization'));

                // Fetch the JWKS
                $jwks = Http::get('https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1/discovery/v2.0/keys')->json();
                
                // Decode the JWT header without verifying to get the 'kid'
                $decodedHeader = json_decode(base64_decode(str_replace('_', '/', str_replace('-', '+', explode('.', $token)[0]))));
                
                // Find the corresponding key in JWKS
                $jwk = null;
                foreach ($jwks['keys'] as $key) {
                    if ($key['kid'] == $decodedHeader->kid) {
                        $jwk = $key;
                        break;
                    }
                }
                if (!$jwk) {
                    return null;
                }
                // Convert the JWK to PEM format
                $jwk['alg'] = 'RS256';
                $publicKey = JWK::parseKey($jwk);

                // Verify and decode the token using RS256
                $decoded = JWT::decode($token, $publicKey);

                $oidcUser = strtok($decoded->upn, '@');  // upn    nsarifa@tbdir.net

                return User::where('user_name', $oidcUser)
                            ->where('status', 1)->first();
            }

            return null;
        });
    }
}
