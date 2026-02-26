<?php


namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Redis;

class DigitalCardSettings extends Controller
{
    public function __construct()
    {
        $this->middleware('auth');
    }

    public function index()
    {
        $videoUrl = Redis::get('digital_card_company_video_url');
        $linkedinUrl = Redis::get('digital_card_company_linkedin');

        return view('digitalcard', [
            'videoUrl' => $videoUrl,
            'linkedinUrl' => $linkedinUrl
        ]);
    }

    public function save(Request $request)
    {
        Redis::set('digital_card_company_video_url', $request->video_url);
        Redis::set('digital_card_company_linkedin', $request->linkedin_url);

        return back()->with('success', 'Digital card settings updated.');
    }
}