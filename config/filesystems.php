<?php
/**
 * Created by PhpStorm.
 * User: Abdullah
 * Date: 22.01.2020
 * Time: 21:24
 */
return [

    /*
    |--------------------------------------------------------------------------
    | Default Filesystem Disk
    |--------------------------------------------------------------------------
    |
    | Here you may specify the default filesystem disk that should be used
    | by the framework. A "local" driver, as well as a variety of cloud
    | based drivers are available for your choosing. Just store away!
    |
    | Supported: "local", "ftp", "s3", "rackspace"
    |
    */

    'default' => 'public',

    /*
    |--------------------------------------------------------------------------
    | Default Cloud Filesystem Disk
    |--------------------------------------------------------------------------
    |
    | Many applications store files both locally and in the cloud. For this
    | reason, you may specify a default "cloud" driver here. This driver
    | will be bound as the Cloud disk implementation in the container.
    |
    */

    'cloud' => 's3',

    /*
    |--------------------------------------------------------------------------
    | Filesystem Disks
    |--------------------------------------------------------------------------
    |
    | Here you may configure as many filesystem "disks" as you wish, and you
    | may even configure multiple disks of the same driver. Defaults have
    | been setup for each driver as an example of the required options.
    |
    */

    'disks' => [

        'local' => [
            'driver' => 'local',
            'root' => storage_path('app'),
        ],

        'public' => [
            'driver' => 'local',
            'root' => storage_path('app/public'),
            'visibility' => 'public',
        ],

        // Panel private storage — files are NOT web-accessible.
        // Default assumes bizizPanel/ and bizizBackend/ are sibling directories.
        // Override with PANEL_NEWS_STORAGE_PATH in .env if the layout differs.
        'panel_news' => [
            'driver' => 'local',
            'root' => env('PANEL_NEWS_STORAGE_PATH', dirname(base_path()) . '/bizizPanel/storage/app'),
        ],

        // Legacy: Panel public storage (files uploaded before the fix).
        // Used as a fallback during migration until old files are moved.
        'panel_news_public' => [
            'driver' => 'local',
            'root' => env('PANEL_NEWS_PUBLIC_STORAGE_PATH', dirname(base_path()) . '/bizizPanel/storage/app/public'),
        ],

        's3' => [
            'driver' => 's3',
            'key' => 'your-key',
            'secret' => 'your-secret',
            'region' => 'your-region',
            'bucket' => 'your-bucket',
        ],

    ],

];