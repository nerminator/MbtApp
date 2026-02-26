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

$router->get('dc/{uuid}', 'DijitalKartvizit@show');
$router->get('dc/{uuid}/downloadVcf', 'DijitalKartvizit@downloadVcf');
$router->get('digitalCard/{uuid}', 'DijitalKartvizit@show');
$router->get('digitalCard/{uuid}/downloadVcf', 'DijitalKartvizit@downloadVcf');

$router->group(['prefix' => 'api/v1', 'middleware' => 'securityHeaders'], function () use ($router) {
    $router->post('init', 'InitController@init');
    $router->get('test', 'InitController@test');
  //  $router->get('captcha', ['middleware' => 'throttle:60,1', 'uses' => 'LoginController@captcha']);
    $router->post('checkPhone', ['middleware' => 'throttle:5,5', 'uses' => 'LoginController@checkPhone']);
  //  $router->post('checkPhoneWithCaptcha', ['middleware' => 'throttle:5,5', 'uses' => 'LoginController@checkPhoneWithCaptcha']);
    $router->post('login', 'LoginController@login');

    // Add the OIDC callback route
    //$router->post('oidc_callback.html', 'LoginController@oidcLogin');
    $router->post('appStartup', 'LoginController@appStartup');

    $router->post('sendCrashLog', 'CrashLogController@sendCrashLog');
    
    

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

        // Bordro OTP başlat
        $router->post('payslip/request-otp', 'PayslipController@requestOtp');

        // Bordro OTP doğrulama
        $router->post('payslip/verify-otp', 'PayslipController@verifyOtp');

        // Bordro listeleme
        $router->post('payslip/fetch', 'PayslipController@fetchPayslip');

        // Dijital Kartvizit
        $router->get('getUserBusinessCardState', 'DijitalKartvizit@getUserBusinessCardState');
        $router->get('activateDigitalCard', 'DijitalKartvizit@activateDigitalCard');
        $router->get('deactivateDigitalCard', 'DijitalKartvizit@deactivateDigitalCard');
        

        //Mobile → menu click counter
        $router->post('menuIncrement', 'MenuViewController@increment');
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
