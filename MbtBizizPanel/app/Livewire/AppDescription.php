<?php

namespace App\Livewire;

use Livewire\Component;
use Illuminate\Support\Facades\Redis;

class AppDescription extends Component
{
    public $appDescriptionTR= '';
    public $appDescriptionEN= '';

    public function render()
    {
        $this->appDescriptionTR = Redis::get('appDescriptionTR') ;
        $this->appDescriptionEN = Redis::get('appDescriptionEN') ;     
        return view('livewire.app-description', [ 'appDescriptionTR' => $this->appDescriptionTR, 'appDescriptionEN' => $this->appDescriptionEN]);
    }
    public function save()
    {
        Redis::set('appDescriptionTR', $this->appDescriptionTR ) ;
        Redis::set('appDescriptionEN', $this->appDescriptionEN ) ;
    }
}
