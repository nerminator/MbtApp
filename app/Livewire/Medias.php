<?php

namespace App\Livewire;
use App\Http\Controllers\MediasController;
use App\Http\Controllers\PhoneNumbersController;
use Livewire\Component;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;

class Medias extends Component
{
    public $medias;

    public function render()
    {
        $this->medias = MediasController::getMedias();
        return view('livewire.medias', ['medias' => $this->medias]);
    }
    public function updateMediaDetailOrder($items)
    {
        //print("success");
        foreach ($items as $item) {
            DB::table('social_media_detail')->where('id', $item['value'])->update([ 'order' => $item['order']]);
        }

    }
}
