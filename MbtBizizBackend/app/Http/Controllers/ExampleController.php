<?php

namespace App\Http\Controllers;
use Illuminate\Support\Facades\Redis;

class ExampleController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
    }
    public function get(){
        if(Redis::exists('example5'))
        return "exists";
        else
        return "not exists";

        return (Redis::get('example5'));
    }
    public function set(){
        $count = Redis::get('example');
        Redis::set('example', $count+1);
        return 1;
    }
    //
}