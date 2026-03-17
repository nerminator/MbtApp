<?php

namespace App\Http\Controllers;

use Laravel\Lumen\Routing\Controller as BaseController;
use Illuminate\Support\Facades\DB;

class Controller extends BaseController
{
    public function getFirstItemFromDb($sqlQuery, $params = [])
    {
        $dataResult = DB::select($sqlQuery, $params);
        if ($dataResult == null || count($dataResult) <= 0) {
            return null;
        }

        return $dataResult[0];
    }

    public function isLangEn()
    {
        return app('translator')->getLocale() == 'en';
    }
}
