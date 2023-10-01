<?php
/**
 * Created by PhpStorm.
 * User: efserdem
 * Date: 2019-10-24
 * Time: 19:04
 */

namespace App\Http\Controllers\Auth;

use App\Http\Requests\PasswordExpiredRequest;
use Carbon\Carbon;
use Illuminate\Support\Facades\Hash;

class ExpiredPasswordController
{
    public function expired()
    {
        return view('auth.passwords.expired');
    }
    public function postExpired(PasswordExpiredRequest $request)
    {
        // Checking current password
        if (!Hash::check($request->current_password, $request->user()->password)) {
            return redirect()->back()->withErrors(['current_password' => 'Mevcut şifre hatalı.']);
        }

        $request->user()->update([
            'password' => bcrypt($request->password),
            'password_changed_at' => Carbon::now()->toDateTimeString()
        ]);
        return redirect()->back()->with(['status' => 'Şifreniz başarıyla değiştirilmiştir.']);
    }
}