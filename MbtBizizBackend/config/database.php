<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 7.05.2018
 * Time: 22:46
 */

return [
    'migrations' => 'migrations',
    'fetch' => PDO::FETCH_CLASS,
    'default' => 'mbtbiziz',
    'connections' => [
        'mbtbiziz' => [
            'driver' => 'mysql',
            'host' => env('DB_HOST'),
            'database' => env('DB_DATABASE'),
            'username' => env('DB_USERNAME'),
            'password' => env('DB_PASSWORD'),
            'charset' => 'utf8mb4',
            'collation' => 'utf8mb4_unicode_ci',
            'prefix' => '',
            'strict' => false,
            'engine' => null,
            'unix_socket' => env('DB_SOCKET', '')
        ]
    ],
    'redis' => [
        'client' => env('REDIS_CLIENT', 'predis'),
        'cluster' => false,
        'default' => [
            'host' => env('REDIS_HOST', '127.0.0.1'),
            'password' => env('REDIS_PASSWORD', ''),
            'port' => env('REDIS_PORT', 6379),
            'database' => 0,
        ]
    ],
];