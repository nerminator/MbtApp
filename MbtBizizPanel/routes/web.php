<?php

/*
|--------------------------------------------------------------------------
| Web Routes
|--------------------------------------------------------------------------
|
| Here is where you can register web routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| contains the "web" middleware group. Now create something great!
|
*/

Route::get('/', function ()
{
    return Redirect::to('/login');
});

Auth::routes();

Route::middleware(['auth'])->group(function () {
    Route::middleware(['password_expired'])->group(function () {
        Route::get('/home', 'HomeController@index')->name('home');
    });

    Route::get('password/expired', 'Auth\ExpiredPasswordController@expired')
        ->name('password.expired');
    Route::post('password/post_expired', 'Auth\ExpiredPasswordController@postExpired')
        ->name('password.post_expired');
});

Route::get('/connection', 'DBController@index')->name('connection');

Route::get('/addnews', 'NewsController@index')->name('addnewspage');
Route::get('/editnews-{id}', 'EditNewsController@index')->name('editnews');

Route::post('/savenews', 'NewsController@add')->name('savenews');
Route::post('/editnews', 'NewsController@edit')->name('editnews');
Route::post('deleteimage', 'NewsController@deleteImage')->name('deleteimage');
Route::post('getnewslist', array('as' => 'news.getList', 'uses' => 'NewsController@getList'));
Route::post('getnews', array('as' => 'news.get', 'uses' => 'NewsController@get'));
Route::post('deletenews', array('as' => 'news.delete', 'uses' => 'NewsController@delete'));
Route::post('enablenews', array('as' => 'news.activate', 'uses' => 'NewsController@activate'));
Route::post('disablenews', array('as' => 'news.passivate', 'uses' => 'NewsController@passivate'));
Route::post('sendnotification', array('as' => 'news.notif', 'uses' => 'NewsController@sendPushNotification'));

Route::get('/getEmployeeTypeList', 'NewsController@getEmployeeTypeList')->name('getEmployeeTypeList');
Route::get('/getCompanyList', 'NewsController@getCompanyList')->name('getCompanyList');
Route::get('/getLocationList', 'NewsController@getLocationList')->name('getLocationList');