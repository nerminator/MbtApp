<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Storage;
use Illuminate\Support\Facades\Validator;

class NewsMediaController extends Controller
{
    // Allowed sub-directory types and their storage sub-paths
    private const TYPE_MAP = [
        'image'    => '',           // contents/news/{newsId}/{filename}
        'document' => 'documents/', // contents/news/{newsId}/documents/{filename}
        'pdf'      => 'pdf/',       // contents/news/{newsId}/pdf/{filename}
    ];

    private const ALLOWED_EXTENSIONS = ['png', 'jpg', 'jpeg', 'gif', 'webp', 'pdf'];

    public function serve(Request $request, $newsId, $type, $filename)
    {
        // Validate route parameters
        $validator = Validator::make(
            ['newsId' => $newsId, 'type' => $type, 'filename' => $filename],
            [
                'newsId'   => 'required|integer|min:1',
                'type'     => 'required|in:image,document,pdf',
                'filename' => ['required', 'string', 'regex:/^[a-zA-Z0-9_\-\.]+$/'],
            ]
        );

        if ($validator->fails()) {
            abort(400);
        }

        // Hard block on path traversal — belt-and-suspenders after regex above
        if (strpos($filename, '..') !== false || strpos($filename, '/') !== false || strpos($filename, "\0") !== false) {
            abort(400);
        }

        // Validate file extension is in the allowed set
        $ext = strtolower(pathinfo($filename, PATHINFO_EXTENSION));
        if (!in_array($ext, self::ALLOWED_EXTENSIONS, true)) {
            abort(400);
        }

        // Verify the authenticated user can access this news item.
        // Reuse the same access rules as newsDetail.
        $user = Auth::user();
        $newsExists = DB::table('news as n')
            ->where('n.id', (int) $newsId)
            ->where('n.status', 1)
            ->where(function ($q) {
                $q->whereNull('n.end_time')->orWhere('n.end_time', '>', now());
            })
            ->where(function ($q) use ($user) {
                $q->whereNull('n.employee_type')->orWhere('n.employee_type', $user->type);
            })
            ->where(function ($q) use ($user) {
                $q->whereRaw(
                    '(select count(ncl.id) from news_company_location ncl where ncl.news_id = n.id) = 0'
                )->orWhereRaw(
                    '? in (select ncl.company_location_id from news_company_location ncl where ncl.news_id = n.id)',
                    [$user->company_location_id]
                );
            })
            ->where(function ($q) use ($user) {
                $q->whereRaw(
                    '(select count(nel.id) from news_employee_location nel where nel.news_id = n.id) = 0'
                )->orWhereRaw(
                    '? in (select nel.employee_location_id from news_employee_location nel where nel.news_id = n.id)',
                    [$user->employee_location_id]
                );
            })
            ->exists();

        if (!$newsExists) {
            abort(403);
        }

        // Build the relative path inside Panel's storage
        $subdir       = self::TYPE_MAP[$type];
        $relativePath = "contents/news/{$newsId}/{$subdir}{$filename}";

        // Try private storage first (new files), then public storage (legacy files
        // not yet migrated by the server admin).
        if (Storage::disk('panel_news')->exists($relativePath)) {
            $disk = 'panel_news';
        } elseif (Storage::disk('panel_news_public')->exists($relativePath)) {
            $disk = 'panel_news_public';
        } else {
            abort(404);
        }

        $mimeType    = Storage::disk($disk)->mimeType($relativePath) ?: 'application/octet-stream';
        $fileContent = Storage::disk($disk)->get($relativePath);

        return response($fileContent, 200)
            ->header('Content-Type', $mimeType)
            ->header('Cache-Control', 'private, max-age=3600')
            ->header('Content-Disposition', 'inline; filename="' . $filename . '"');
    }
}
