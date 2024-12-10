<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It is a breeze. Simply tell Lumen the URIs it should respond to
| and give it the Closure to call when that URI is requested.
|
*/

$router->get('/', function () use ($router) {
});



$router->group(['prefix' => 'api/v1', 'middleware' => 'securityHeaders'], function () use ($router) {
    $router->post('init', 'InitController@init');
  //  $router->get('captcha', ['middleware' => 'throttle:60,1', 'uses' => 'LoginController@captcha']);
    $router->post('checkPhone', ['middleware' => 'throttle:5,5', 'uses' => 'LoginController@checkPhone']);
  //  $router->post('checkPhoneWithCaptcha', ['middleware' => 'throttle:5,5', 'uses' => 'LoginController@checkPhoneWithCaptcha']);
    $router->post('login', 'LoginController@login');

    // Add the OIDC callback route
    //$router->post('oidc_callback.html', 'LoginController@oidcLogin');
    $router->post('appStartup', 'LoginController@appStartup');

    $router->group(['middleware' => 'auth'], function () use ($router) {
        $router->post('newsList', 'NewsController@newsList');
        $router->get('newsDetail/{id}', 'NewsController@newsDetail');
        $router->get('birthdayList', 'NewsController@birthdayList');

        $router->post('saveDeviceInfo', 'DeviceInfoController@saveDeviceInfo');
        $router->post('deleteDeviceInfo', 'DeviceInfoController@deleteDeviceInfo');

        $router->post('notificationList', 'NotificationController@notificationList');
        $router->post('deleteNotification', 'NotificationController@deleteNotification');
        $router->get('notificationBadgeCount', 'NotificationController@notificationBadgeCount');

        $router->get('foodMenu', 'FoodMenuController@foodMenu');

        $router->get('profile', 'ProfileController@profile');
        $router->get('workCalendar/{date}', 'ProfileController@workCalendar');
        $router->get('yearlyWorkHours/{year}', 'ProfileController@yearlyWorkHours');
        $router->get('monthlyWorkHours/{year}/{month}', 'ProfileController@monthlyWorkHours');

        $router->get('notificationSettings', 'SettingController@notificationSettings');
        $router->post('changeNotificationSetting', 'SettingController@changeNotificationSetting');

        $router->get('maps', 'MapController@maps');

        $router->get('shuttleOptionList', 'ShuttleController@shuttleOptionList');
        $router->post('shuttleList', 'ShuttleController@shuttleList');

        $router->get('signOut', 'LoginController@signOut');

        $router->get('userConfig', 'InitController@userConfig');

        $router->get('getDiscountCode/{newsId}', 'NewsController@getDiscountCode');

        $router->get('socialClubLocs', 'SocialClubsController@getSocialClubLocs');
        $router->get('phoneLocs', 'PhonesController@getPhoneLocs');

        $router->get('socialClubs/{loc_id}', 'SocialClubsController@getSocialClubs');
        $router->get('phones/{loc_id}', 'PhonesController@getPhones');

        $router->get('medias', 'MediasController@getMedias');
        $router->post('submitFeedback', 'AppFeedbackController@submitFeedback');
    });
});

//$router->group(['prefix' => 'api/v2'], function () use ($router) {
//    $router->post('init', 'InitController@init');
//    $router->post('checkPhone', ['middleware' => 'throttle:5,5', 'uses' => 'LoginController@checkPhoneWithCaptcha']);
//
//    $router->group(['middleware' => 'auth'], function () use ($router) {
//
//    });
//});
