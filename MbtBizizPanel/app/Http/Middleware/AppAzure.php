<?php

namespace App\Http\Middleware;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;

use RootInc\LaravelAzureMiddleware\Azure;
use Microsoft\Graph\Graph;
use Microsoft\Graph\Model;

use Auth;

use App\User;

class AppAzure extends Azure
{

    protected function success(Request $request, $access_token, $refresh_token, $profile)
    {
      Log::info('Azure OIDC profile', ['profile' => $profile]);
      $user = User::firstOrNew (['email' => $profile->preferred_username]);

      $user = User::updateOrCreate(['email' => $profile->preferred_username], [
       'name' => $profile->name, 'remember_token' => false, 'access_token' =>  $access_token
      ]);

      $user ->save();
      Auth::login($user, true);

      return redirect('home');
      //return parent::success($request, $access_token, $refresh_token, $profile);
    }
}