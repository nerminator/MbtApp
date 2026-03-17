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
use Illuminate\Support\Facades\Log;

class AuthServiceProvider extends ServiceProvider
{
    private function boolEnv(string $key, bool $default = false): bool
    {
        $value = env($key, $default);

        if (is_bool($value)) {
            return $value;
        }

        if (is_numeric($value)) {
            return (int)$value === 1;
        }

        return in_array(strtolower((string)$value), ['1', 'true', 'yes', 'on'], true);
    }

    private function resolveBypassedUser($request)
    {
        $isBypassEnabled = $this->boolEnv('OIDC_DEV_BYPASS_ENABLED', false);
        $appEnv = strtolower((string) env('APP_ENV', 'production'));
        $isNonProdEnv = !in_array($appEnv, ['production', 'prod', 'staging'], true);

        if (!$isBypassEnabled || !$isNonProdEnv) {
            return null;
        }

        $authorizationHeader = (string) $request->header('Authorization', '');
        if ($authorizationHeader === '' || stripos($authorizationHeader, 'Bearer ') !== 0) {
            return null;
        }

        $incomingToken = trim(substr($authorizationHeader, 7));
        $expectedToken = (string) env('OIDC_DEV_BYPASS_TOKEN', '');

        if ($incomingToken === '' || $expectedToken === '' || !hash_equals($expectedToken, $incomingToken)) {
            return null;
        }

        $username = (string) env('OIDC_DEV_BYPASS_USERNAME', '');

        if ($username !== '') {
            $user = User::where('user_name', $username)->where('status', 1)->first();
            if ($user) {
                return $user;
            }

            Log::warning('OIDC development bypass enabled but configured user not found/inactive: ' . $username);
        }

        return User::where('status', 1)->first();
    }

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
        
                return null;
                //return User::where('token', $request->header('token'))
                //            ->where('status', 1)->first();

            } elseif ($request->header('Authorization')) {
                $bypassedUser = $this->resolveBypassedUser($request);
                if ($bypassedUser) {
                    if ($request->header('lang') == 'en') {
                        app('translator')->setLocale('en');
                        setlocale(LC_TIME, null);
                        \Carbon\Carbon::setLocale('en');
                    }

                    return $bypassedUser;
                }

                $token = str_replace('Bearer ', '', $request->header('Authorization'));

                //Log::info('Token: ' . $token);
                
                // Fetch the JWKS
                $jwks = Http::get('https://login.microsoftonline.com/505cca53-5750-4134-9501-8d52d5df3cd1/discovery/v2.0/keys')->json();
                
                //Log::info('JWKS: ' . json_encode($jwks));

                // Decode the JWT header without verifying to get the 'kid'
                $decodedHeader = json_decode(base64_decode(str_replace('_', '/', str_replace('-', '+', explode('.', $token)[0]))));
                
                //Log::info('Decoded Header: ' . json_encode($decodedHeader));

                // Find the corresponding key in JWKS
                $jwk = null;
                foreach ($jwks['keys'] as $key) {
                    if ($key['kid'] == $decodedHeader->kid) {
                        $jwk = $key;
                        break;
                    }
                }
                if (!$jwk) {
                    Log::error('No matching JWK found for kid: ' . $decodedHeader->kid);
                    return null;
                }
                // Convert the JWK to PEM format
                $jwk['alg'] = 'RS256';
                $publicKey = JWK::parseKey($jwk);

                try {
                    // Verify and decode the token using RS256
                    $decoded = JWT::decode($token, $publicKey);
                } catch (\Firebase\JWT\ExpiredException $e) {
                    // Token expired → return null without ERROR log noise
                    return null;
                } catch (\Exception $e) {
                    //Log::error("Invalid token: " . $e->getMessage());
                    return null;
                }

                Log::info('Decoded Token: ' . json_encode($decoded));
                $oidcUser = strtok($decoded->upn, '@');  // upn    nsarifa@tbdir.net
                
                $user = User::where('user_name', $oidcUser)->where('status', 1)->first();
                if (!$user) {
                    Log::error('User not found or inactive: ' . $oidcUser);
                } 

                if ($request->header('lang') == 'en') {
                    app('translator')->setLocale('en');
                    setlocale(LC_TIME, null);
                    \Carbon\Carbon::setLocale('en');
                }

                return $user;
                
            }

            return null;
        });
    }
}
