<?php

namespace App\Livewire;
use App\Http\Controllers\SocialClubsController;
use GrahamCampbell\ResultType\Success;
use Livewire\Component;
use Illuminate\Support\Facades\DB;

class Clubs extends Component
{
    public $locs;
 
    public function render()
    {
        $this->club_locations = SocialClubsController::getClubLocs();
        return view('livewire.clubs', ['club_locations' => $this->club_locations]);

    }
    public function updateClubOrder($items)
    {
        //print("success");
        foreach ($items as $item) {
            DB::table('news')->where('id', $item['value'])->update([ 'order' => $item['order']]);
        }
        $this->dispatch('Clubs reordered!');
        //$this->dispatchBrowserEvent('updated', ['message' => 'Clubs reordered']);
        
        session()->flash('message', 'Clubs reordered');
        //print "success";
    }
}
