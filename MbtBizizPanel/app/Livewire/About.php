<?php

namespace App\Livewire;

use Livewire\Component;
use Illuminate\Support\Facades\Redis;
class About extends Component
{
    public $aboutTR = '';
    public $aboutEN = '';

    public function render()
    {
        $this->aboutTR = Redis::get('aboutTR') ;
        $this->aboutEN = Redis::get('aboutEN') ;        
        return view('livewire.about', [ 'aboutTR' => $this->aboutTR, 'aboutEN' => $this->aboutEN]);
    }
    public function save()
    {
        Redis::set('aboutTR', $this->aboutTR ) ;
        Redis::set('aboutEN', $this->aboutEN ) ;
    }
}
