<?php

namespace App\Livewire;
use App\Http\Controllers\PhoneNumbersController;
use Livewire\Component;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Redis;

class Phones extends Component
{
    public $phone_locations;
    public $santral;
    public function render()
    {
        $this->phone_locations = PhoneNumbersController::getLocations();
        $this->santral = Redis::get('phonesSantral');
        return view('livewire.phones', ['phone_locations' => $this->phone_locations, 'santral' => $this->santral]);
    }
    public function updatePhoneOrder($items)
    {
        //print("success");
        foreach ($items as $item) {
            DB::table('phone_numbers')->where('id', $item['value'])->update([ 'order' => $item['order']]);
        }

    }
}
