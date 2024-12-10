<?php

return [
    /*
    |--------------------------------------------------------------------------
    | Tenant ID
    |--------------------------------------------------------------------------
    |
    | This value is equal to the 'Directory (tenant) ID' as found in the Azure
    | portal
    |
    */

    'tenant_id' => env('AZURE_TENANT_ID', '505cca53-5750-4134-9501-8d52d5df3cd1'),

    /*
    |--------------------------------------------------------------------------
    | Client Info
    |--------------------------------------------------------------------------
    |
    | These values are equal to 'Application (client) ID' and the secret you
    | made in 'Client secrets' as found in the Azure portal
    |
    */
    'client' => [
        'id' => env('AZURE_CLIENT_ID', '85447726-1110-48d2-8523-5db3d7e22970'),
        'secret' => env('AZURE_CLIENT_SECRET', '***REDACTED***'),
    ],

    /*
    |--------------------------------------------------------------------------
    | Resource ID
    |--------------------------------------------------------------------------
    |
    | This value is equal to the 'Object ID' as found in the Azure portal
    |
    */
    'resource' => env('AZURE_RESOURCE', 'https://graph.microsoft.com/'),

    /*
    |--------------------------------------------------------------------------
    | Domain Hint
    |--------------------------------------------------------------------------
    |
    | This value can be used to help users know which email address they
    | should login with.
    | https://azure.microsoft.com/en-us/updates/app-service-auth-and-azure-ad-domain-hints/
    |
    */
    'domain_hint' => env('AZURE_DOMAIN_HINT', ''),

    /*
    |--------------------------------------------------------------------------
    | Permission Scope
    |--------------------------------------------------------------------------
    |
    | This value indicates the permissions granted to the OAUTH session.
    | https://docs.microsoft.com/en-us/graph/api/resources/users?view=graph-rest-1.0
    |
    */
    'scope' => env('AZURE_SCOPE', 'openid'),
];