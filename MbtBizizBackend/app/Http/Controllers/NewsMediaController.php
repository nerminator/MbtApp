<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Validator;

class NewsMediaController extends Controller
{
    private const TYPE_MAP = [
        'image' => '',
        'document' => 'documents/',
        'pdf' => 'pdf/',
    ];

    private const ALLOWED_EXTENSIONS = ['png', 'jpg', 'jpeg', 'gif', 'webp', 'pdf'];

    private const MIME_MAP = [
        'png' => 'image/png',
        'jpg' => 'image/jpeg',
        'jpeg' => 'image/jpeg',
        'gif' => 'image/gif',
        'webp' => 'image/webp',
        'pdf' => 'application/pdf',
    ];

    public function serve(Request $request, $newsId, $type, $filename)
    {
        $validator = Validator::make(
            ['newsId' => $newsId, 'type' => $type, 'filename' => $filename],
            [
                'newsId' => 'required|integer|min:1',
                'type' => 'required|in:image,document,pdf',
                'filename' => ['required', 'string', 'regex:/^[a-zA-Z0-9_\-\.]+$/'],
            ]
        );

        if ($validator->fails()) {
            abort(400);
        }

        if (strpos($filename, '..') !== false || strpos($filename, '/') !== false || strpos($filename, "\0") !== false) {
            abort(400);
        }

        $ext = strtolower(pathinfo($filename, PATHINFO_EXTENSION));
        if (!in_array($ext, self::ALLOWED_EXTENSIONS, true)) {
            abort(400);
        }

        $sig = (string) $request->query('sig', '');
        $exp = (string) $request->query('exp', '');

        if ($sig === '' || $exp === '' || !ctype_digit($exp)) {
            abort(403);
        }

        if (time() > (int) $exp) {
            abort(403);
        }

        $signingKey = config('media.signing_key', '');
        $expectedSig = hash_hmac('sha256', "{$newsId}/{$type}/{$filename}/{$exp}", $signingKey);
        if (!hash_equals($expectedSig, $sig)) {
            abort(403);
        }

        $subdir = self::TYPE_MAP[$type];
        $relativePath = "contents/news/{$newsId}/{$subdir}{$filename}";

        $privateRoot = rtrim(
            env(
                'PANEL_NEWS_STORAGE_PATH',
                realpath(base_path() . '/../bizizPanel/storage/app') ?: dirname(base_path()) . '/bizizPanel/storage/app'
            ),
            '/'
        );
        $publicRoot = rtrim(
            env(
                'PANEL_NEWS_PUBLIC_STORAGE_PATH',
                realpath(base_path() . '/../bizizPanel/storage/app/public') ?: dirname(base_path()) . '/bizizPanel/storage/app/public'
            ),
            '/'
        );

        $absolutePath = null;
        foreach ([$privateRoot, $publicRoot] as $root) {
            $candidate = $root . '/' . $relativePath;
            $resolved = realpath($candidate);
            if ($resolved !== false && strncmp($resolved, $root . '/', strlen($root) + 1) === 0 && is_file($resolved)) {
                $absolutePath = $resolved;
                break;
            }
        }

        if ($absolutePath === null) {
            Log::warning('NewsMediaController: file not found on either disk', [
                'relativePath' => $relativePath,
                'privateRoot' => $privateRoot,
                'publicRoot' => $publicRoot,
            ]);
            abort(404);
        }

        $mimeType = self::MIME_MAP[$ext] ?? 'application/octet-stream';

        return response()->stream(function () use ($absolutePath) {
            $handle = fopen($absolutePath, 'rb');
            while (!feof($handle)) {
                echo fread($handle, 8192);
            }
            fclose($handle);
        }, 200, [
            'Content-Type' => $mimeType,
            'Content-Length' => filesize($absolutePath),
            'Cache-Control' => 'private, max-age=3600',
            'Content-Disposition' => 'inline; filename="' . basename($absolutePath) . '"',
        ]);
    }
}
