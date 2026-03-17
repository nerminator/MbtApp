<?php
/**
 * Created by PhpStorm.
 * User: efserdem
 * Date: 2020-02-02
 * Time: 18:27
 */

namespace App\Http\Controllers;

use Illuminate\Support\Facades\DB;


class DBController extends Controller
{
    public function index()
    {
        $result = $this->connection();

        return view('connection')->with('result', $result);

    }

    public function connection()
    {
        try
        {
            DB::connection()->getPdo();
            return ("Connection Success");
        }
        catch (\Exception $e)
        {
            return ("Connection Error:" . $e);
        }

    }
}