<?php
/**
 * Created by PhpStorm.
 * User: efserdem
 * Date: 2019-10-24
 * Time: 19:05
 */

namespace App\Http\Requests;

use Illuminate\Foundation\Http\FormRequest;


class PasswordExpiredRequest extends FormRequest
{

    public function authorize()
    {
        return true;
    }

    public function rules()
    {
        return [
            'current_password' => 'required',
            'password' => [
                'required',
                'confirmed',
                'string',
                'min:8',                    // must be at least 8 characters in length
                'regex:/[a-z]/',            // must contain at least one lowercase letter
                'regex:/[A-Z]/',            // must contain at least one uppercase letter
           //   'regex:/[0-9]/',            // must contain at least one digit
                'regex:/[@$!%*#?&]/',       // must contain a special character
                'regex:/^(.(?!\d\d\d))+$/', // consecutive 3 digits
            ],
        ];
    }
    public function messages()
    {
        return [
            'password.min' => 'Şifreniz en az 8 karakter uzunluğunda olmalı.',
            'password.regex' => 'Şifreniz 1 büyük, 1 küçük, 1 özel karakter içermelidir.'
        ];
    }
}
