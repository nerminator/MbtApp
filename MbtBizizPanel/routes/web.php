<?php

use App\Http\Controllers\OidcController;
use App\Livewire\About;
use App\Livewire\AppDescription;
use App\Livewire\Medias;
use App\Livewire\Phones;
use Illuminate\Support\Facades\Route;
use App\Livewire\Clubs;
use App\Http\Controllers\AboutusController;
use App\Http\Controllers\DBController;
use App\Http\Controllers\DiscountCodesController;
use App\Http\Controllers\DocumentController;
use App\Http\Controllers\EditNewsController;
use App\Http\Controllers\FeedbackController;
use App\Http\Controllers\HomeController;
use App\Http\Controllers\MediasController;
use App\Http\Controllers\NewsController;
use App\Http\Controllers\PhoneNumbersController;
use App\Http\Controllers\SocialClubsController;
use Livewire\Livewire;
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
    return Redirect::to('/home');
});
//Auth::routes();

/*Route::middleware(['auth'])->group(function () {
    Route::middleware(['password_expired'])->group(function () {
        Route::get('/home', [HomeController::class, 'index'])->name('home');
    });

    Route::get('password/expired', [Auth\ExpiredPasswordController::class, 'expired'])
        ->name('password.expired');
    Route::post('password/post_expired', [Auth\ExpiredPasswordController::class, 'postExpired'])
        ->name('password.post_expired');
});*/

Route::get('/connection', [DBController::class, 'index'])->name('connection');

Route::get('/login', '\App\Http\Middleware\AppAzure@azure')->name('login');
//Route::get('/login', '\RootInc\LaravelAzureMiddleware\Azure@azure')  ->name('login');
Route::get('/loginOIDC', '\App\Http\Middleware\AppAzure@azurecallback') ->name('azure.callback');

Route::middleware('azure')->group(function () {
    Route::get('/logout', '\App\Http\Middleware\AppAzure@azurelogout') ->name('logout');

    Route::get('/home', [HomeController::class, 'index'])->name('home');
    Route::get('/addnewsEmpty', [NewsController::class, 'addnewsEmpty'])->name('addnewsEmpty');

    Route::get('/editnews-{id}', [EditNewsController::class, 'index'])->name('editnews');

    //SOCIAL CLUBS
    Route::get('/socialclubs', Clubs::class)->name('clubs');


    Route::get('/addClubEmpty/{loc_id}', [NewsController::class, 'addClubEmpty'])->name('addClubEmpty');

    Route::post('/addClubLoc', [SocialClubsController::class, 'addClubLoc'])->name('addClubLoc');
    Route::post('/deleteClubLoc', [SocialClubsController::class, 'deleteClubLoc'])->name('deleteclubloc');

    Route::post('/addClub', [SocialClubsController::class, 'addClub'])->name('addclub');
    Route::post('/deleteClub', [SocialClubsController::class, 'deleteClub'])->name('deleteclub');

    Route::post('/addClubDetail', [SocialClubsController::class, 'addClubDetail'])->name('addClubDetail');
    Route::post('/deleteClubDetail', [SocialClubsController::class, 'deleteClubDetail'])->name('deleteClubDetail');


    Route::post('/updateClubLoc', [SocialClubsController::class, 'updateClubLoc'])->name('updateClubLoc');
    Route::post('/updateClub', [SocialClubsController::class, 'updateClub'])->name('updateClub');
    Route::post('/updateClubPersonName', [SocialClubsController::class, 'updateClubPersonName'])->name('updateClubPersonName');
    Route::post('/updateClubPersonContact', [SocialClubsController::class, 'updateClubPersonContact'])->name('updateClubPersonContact');
    //SOCIAL CLUBS END

    //PHONES
    Route::get('/phones', Phones::class)->name('phones');


    Route::post('/addPhoneLoc', [PhoneNumbersController::class, 'addPhoneLoc'])->name('addPhoneLoc');
    Route::post('/deletePhoneLoc', [PhoneNumbersController::class, 'deletePhoneLoc'])->name('deletephoneloc');

    Route::post('/addPhone', [PhoneNumbersController::class, 'addPhone'])->name('addphone');
    Route::post('/deletePhone', [PhoneNumbersController::class, 'deletePhone'])->name('deletephone');

    Route::post('/addPhoneDetail', [PhoneNumbersController::class, 'addPhoneDetail'])->name('addphonedetail');
    Route::post('/deletePhoneDetail', [PhoneNumbersController::class, 'deletePhoneDetail'])->name('deletephonedetail');


    Route::post('/updatePhoneLoc', [PhoneNumbersController::class, 'updatePhoneLoc'])->name('updatePhoneLoc');
    Route::post('/updatePhone', [PhoneNumbersController::class, 'updatePhone'])->name('updatePhone');
    Route::post('/updatePhoneUnit', [PhoneNumbersController::class, 'updatePhoneUnit'])->name('updatePhoneUnit');
    Route::post('/updatePhoneNote', [PhoneNumbersController::class, 'updatePhoneNote'])->name('updatePhoneNote');
    Route::post('/updatePhoneInternal', [PhoneNumbersController::class, 'updatePhoneInternal'])->name('updatePhoneInternal');

    Route::post('/updateSantral', [PhoneNumbersController::class, 'updateSantral'])->name('updateSantral');

    //PHONES END

    //MEDIA
    Route::get('/medias', Medias::class)->name('medias');

    Route::post('/addMedia', [MediasController::class, 'addMedia'])->name('addMedia');
    Route::post('/deleteMedia', [MediasController::class, 'deleteMedia'])->name('deleteMedia');

    Route::post('/addMediaDetail', [MediasController::class, 'addMediaDetail'])->name('addMediaDetail');
    Route::post('/deleteMediaDetail', [MediasController::class, 'deleteMediaDetail'])->name('deleteMediaDetail');

    Route::post('/updateMedia', [MediasController::class, 'updateMedia'])->name('updateMedia');
    Route::post('/updateMediaAccount', [MediasController::class, 'updateMediaAccount'])->name('updateMediaAccount');
    Route::post('/updateMediaUrl', [MediasController::class, 'updateMediaUrl'])->name('updateMediaUrl');
    //MEDIA END

    //APP FEEDBACK
    Route::get('/feedback', [FeedbackController::class, 'index'])->name('feedback');
    Route::post('/getFeedbacks', [FeedbackController::class, 'getFeedbacks'])->name('getFeedbacks');
    Route::post('/updateEmails', [FeedbackController::class, 'updateEmails'])->name('updateEmails');

    //APP FEEDBACK END

    Route::get('/aboutus', [AboutusController::class, 'index'])->name('aboutus');
    Route::post('/updateAboutus', [AboutusController::class, 'updateAboutus'])->name('updateAboutus');

    Route::post('/savenews', [NewsController::class, 'add'])->name('savenews');
    Route::post('/editnews', [NewsController::class, 'edit'])->name('editnews');
    Route::post('deleteimage', [NewsController::class, 'deleteImage'])->name('deleteimage');
    Route::post('getnewslist', [NewsController::class,'getList']);
    Route::post('getnews', [NewsController::class, 'get']);
    Route::post('deletenews', [NewsController::class, 'delete']);
    Route::post('enablenews', [NewsController::class, 'activate']);
    Route::post('disablenews', [NewsController::class, 'passivate']);
    Route::post('sendnotification', [NewsController::class, 'sendPushNotification']);

    Route::get('/getEmployeeTypeList', [NewsController::class, 'getEmployeeTypeList'])->name('getEmployeeTypeList');
    Route::get('/getCompanyList', [NewsController::class, 'getCompanyList'])->name('getCompanyList');
    Route::get('/getLocationList', [NewsController::class, 'getLocationList'])->name('getLocationList');

    Route::post('addpdf', [DocumentController::class, 'addDocument']);
    Route::post('deletepdf', [DocumentController::class, 'deleteDocument']);

    Route::post('addDiscountCodes', [DiscountCodesController::class, 'addDiscountCodes']);
    Route::post('deleteDiscountCodes', [DiscountCodesController::class, 'deleteDiscountCodes']);

    //Dashboard
    //Route::get('/dashboard', [DashboardController::class, 'index'])->name('dashboard');

    //ABOUT
    Route::get('/about', About::class)->name('about');
    Route::get('/appDescription', AppDescription::class)->name('appDescription');

    Livewire::setUpdateRoute(function ($handle) {
        return Route::post( '/' . env('APP_SUBFOLDER') . '/livewire/update', $handle);
    });
    
});

Route::get('routes', function () {
    $routeCollection = Route::getRoutes();

    echo "<table style='width:100%'>";
    echo "<tr>";
    echo "<td width='10%'><h4>HTTP Method</h4></td>";
    echo "<td width='10%'><h4>Route</h4></td>";
    echo "<td width='10%'><h4>Name</h4></td>";
    echo "<td width='70%'><h4>Corresponding Action</h4></td>";
    echo "</tr>";
    foreach ($routeCollection as $value) {
        echo "<tr>";
        echo "<td>" . $value->methods()[0] . "</td>";
        echo "<td>" . $value->uri() . "</td>";
        echo "<td>" . $value->getName() . "</td>";
        echo "<td>" . $value->getActionName() . "</td>";
        echo "</tr>";
    }
    echo "</table>";
});



