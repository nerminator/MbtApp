<?php

namespace App;

use Illuminate\Notifications\Notifiable;
use Illuminate\Foundation\Auth\User as Authenticatable;
use Illuminate\Auth\Notifications\ResetPassword as ResetPassword;
use Illuminate\Support\Facades\DB;

class User extends Authenticatable
{
    use Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $table = "oidc_users";
    protected $fillable = [
        'name', 'email', 'remember_token'
    ];



    public function sendPasswordResetNotification($token)
    {
        $this->notify(new ResetPassword($token));
    }
   
}
