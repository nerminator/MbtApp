<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
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
        // Raw SQL avoids Lumen query-builder closure issues with table aliases
        // while enforcing the same rules as newsDetail.
        $user = Auth::user();
        $newsExists = DB::selectOne(
            'SELECT 1 FROM news
             WHERE id = ?
               AND status = 1
               AND (end_time IS NULL OR end_time > NOW())
               AND (employee_type IS NULL OR employee_type = ?)
               AND (
                     (SELECT COUNT(id) FROM news_company_location WHERE news_id = news.id) = 0
                     OR ? IN (SELECT company_location_id FROM news_company_location WHERE news_id = news.id)
                   )
               AND (
                     (SELECT COUNT(id) FROM news_employee_location WHERE news_id = news.id) = 0
                     OR ? IN (SELECT employee_location_id FROM news_employee_location WHERE news_id = news.id)
                   )
             LIMIT 1',
            [(int) $newsId, $user->type, $user->company_location_id, $user->employee_location_id]
        );

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
            Log::warning('NewsMediaController: file not found on either disk', [
                'newsId'              => $newsId,
                'relativePath'        => $relativePath,
                'panel_news_root'     => config('filesystems.disks.panel_news.root'),
                'panel_news_pub_root' => config('filesystems.disks.panel_news_public.root'),
            ]);
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
