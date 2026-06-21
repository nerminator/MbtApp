<?php

return [

    /*
    |--------------------------------------------------------------------------
    | Media Signing Key
    |--------------------------------------------------------------------------
    |
    | A strong random secret used to sign time-limited media URLs so that
    | images and documents can be served without requiring an Authorization
    | header (backward compatible with older app versions).
    |
    | Generate with: php -r "echo bin2hex(random_bytes(32));"
    | Set MEDIA_SIGNING_KEY in .env — never commit the real value.
    |
    */

    'signing_key' => env('MEDIA_SIGNING_KEY', ''),

    /*
    |--------------------------------------------------------------------------
    | Signed URL Expiry (seconds)
    |--------------------------------------------------------------------------
    |
    | How long a signed media URL remains valid after it is generated.
    | Default: 86400 seconds (24 hours).
    |
    */

    'url_expiry_seconds' => (int) env('MEDIA_URL_EXPIRY_SECONDS', 86400),

];
