<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Cache;
use Illuminate\Support\Facades\Redis;
use SoapClient;
use Carbon\Carbon;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Str;
use App\Services\MenuViewService;

class DijitalKartvizit extends Controller
{

    public function getUserBusinessCardState()
    {
        $user = auth()->user();

        if (!$user) {
            return response()->json([
                'statusCode' => 401,
                'responseData' => [
                    "isActivated" => false,
                    "digitalCardUrl" => null
                ],
                'errorMessage' => null
            ]);
        }

        MenuViewService::increment('Profile_DigitalBusinessCard');
        return response()->json([
                'statusCode' => 200,
                'responseData' => [
                    "isActivated" => (bool) $user->dk_enabled,
                    "digitalCardUrl" => $user->dk_enabled && $user->dk_uuid
                                        ? config('services.dk.url') . "/d/" . $user->dk_uuid
                                        : null
                ],
                'errorMessage' => null
        ]);

    }

    public function activateDigitalCard(Request $request)
    {
        $user = auth()->user();

        if (!$user->dk_uuid) {
            Log::info("Generating DK UUID for user: " . $user->id);
            $user->dk_enabled = true;
            $user->dk_uuid = Str::uuid();
            DB::update("update users set dk_enabled = 1, dk_uuid = ? where id = ?", [$user->dk_uuid, $user->id]);

        } else {
            Log::info("Enabling DK for existing UUID for user: " . $user->id);
            $user->dk_enabled = true; 
            DB::update("update users set dk_enabled = 1 where id = ?", [$user->id]);  
        }

        Log::info("Digital card activated for user: " . $user->id);
        return response()->json([
            'statusCode' => 200,
            'responseData' => [
                "digitalCardUrl" => config('services.dk.url') . "/d/" . $user->dk_uuid
            ],
            'errorMessage' => null
        ]);
    }

    public function deactivateDigitalCard(Request $request)
    {
        $user = auth()->user();

        $user->dk_enabled = false;
        DB::update("update users set dk_enabled = 0 where id = ?", [$user->id]);  

        return response()->json([
            'statusCode' => 200,
            'responseData' => [
            ],
            'errorMessage' => null
        ]);
    }

    public function show($uuid)
    {
        $user = User::where('dk_uuid', $uuid)->where('dk_enabled', 1)->first();

        if (!$user) {
            return response()->json(null, 404);
        }

        // URL to encode in QR
        $qrUrl = url("digitalCard/$uuid");

        // Get video & LinkedIn URL from Redis
        $linkedinUrl = Redis::get("digital_card_company_linkedin");

        $videoUrl = Redis::get("digital_card_company_video_url");
        $embedUrl = $videoUrl;

        if ($videoUrl) {

            // YOUTUBE: Convert to embed format
            if (str_contains($videoUrl, "youtube.com") || str_contains($videoUrl, "youtu.be")) {

                // handles both: watch?v= and youtu.be links
                if (str_contains($videoUrl, "watch?v=")) {
                    $embedUrl = str_replace("watch?v=", "embed/", $videoUrl);
                } elseif (str_contains($videoUrl, "youtu.be")) {
                    // convert youtu.be/VIDEO → youtube.com/embed/VIDEO
                    $videoId = substr(parse_url($videoUrl, PHP_URL_PATH), 1);
                    $embedUrl = "https://www.youtube.com/embed/" . $videoId;
                }

                // ensure no duplicate ?
                $embedUrl .= (str_contains($videoUrl, '?') ? '&' : '?') .
                            "autoplay=1&mute=1&playsinline=1&rel=0&modestbranding=1";
            }
            // VIMEO support (optional)
            else if (str_contains($videoUrl, "vimeo.com")) {
                $embedUrl = str_replace("vimeo.com", "player.vimeo.com/video", $videoUrl);
                $embedUrl .= "?autoplay=1&muted=1&playsinline=1";
            }
        }
            
        // Telefon formatlama backend'de (0 → +90)
        if ($user->mobile_phone) {

            $phone = preg_replace('/\s+/', '', $user->mobile_phone); // boşlukları sil
            $phone = preg_replace('/[^0-9]/', '', $phone);           // rakam dışı karakterleri sil

            if (str_starts_with($phone, '0')) {
                $phone = '+90' . substr($phone, 1);
            } elseif (!str_starts_with($phone, '+')) {
                $phone = '+90' . $phone;
            }

            $user->mobile_phone = $phone; // ✅ User nesnesi içinde override
        }

        // Email lowercase backend'de
        if ($user->email) {
            $user->email = strtolower($user->email);  // ✅ User nesnesi içinde override
        }

        return view('digital_card', [
            'user'       => $user,
            'linkedinUrl'=> $linkedinUrl,
            'embedUrl'   => $embedUrl,
        ]);
    }

    public function downloadVcf($uuid)
    {
        $user = DB::table('users')->where('dk_uuid', $uuid)->first();

        if (!$user) {
            return abort(404, "User not found");
        }

                // Telefon formatlama backend'de (0 → +90)
        if ($user->mobile_phone) {

            $phone = preg_replace('/\s+/', '', $user->mobile_phone); // boşlukları sil
            $phone = preg_replace('/[^0-9]/', '', $phone);           // rakam dışı karakterleri sil

            if (str_starts_with($phone, '0')) {
                $phone = '+90' . substr($phone, 1);
            } elseif (!str_starts_with($phone, '+')) {
                $phone = '+90' . $phone;
            }

            $user->mobile_phone = $phone; // ✅ User nesnesi içinde override
        }

        // Email lowercase backend'de
        if ($user->email) {
            $user->email = strtolower($user->email);  // ✅ User nesnesi içinde override
        }

        // ✅ Name parsing — last word is surname
        $fullName = trim($user->name_surname);
        $nameParts = explode(' ', $fullName);

        $lastName = array_pop($nameParts);         // son kelime
        $firstName = implode(' ', $nameParts);     // geriye kalan kelimeler

        $vcf = "BEGIN:VCARD\n";
        $vcf .= "VERSION:3.0\n";
        $vcf .= "N:{$lastName};{$firstName};;;\n";   // Correct format
        $vcf .= "FN:{$fullName}\n";
        if ($user->mobile_phone) {
            $vcf .= "TEL;type=CELL:{$user->mobile_phone}\n";
        }
        if ($user->email) {
            $vcf .= "EMAIL:{$user->email}\n";
        }
        $vcf .= "ORG:Mercedes-Benz Türk A.Ş.\n";
        if ($user->location) {
            $vcf .= "ADR:;;{$user->location};;;;\n";
        }
        $vcf .= "END:VCARD";

        return response($vcf, 200, [
            'Content-Type' => 'text/vcard',
            'Content-Disposition' => "attachment; filename=\"{$user->name_surname}.vcf\""
        ]);
      }

}