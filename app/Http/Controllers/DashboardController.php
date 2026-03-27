<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Log\Logger;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Redis;
use Carbon\Carbon;

class DashboardController extends Controller
{
    public function index()
    {
        // ----- KPI VALUES -----

        // KPI DATE
        $sinceDate = '2025-09-17';

        // Total logged-in users since date
        $totalLoggedIn = DB::table('users')
            ->whereNotNull('last_login_at')
            ->where('last_login_at', '>=', $sinceDate)
            ->count();

        $totalUsers = DB::table('users')->count();


        // White collar logged in
        $whiteLoggedIn = DB::table('users')
            ->where('type', '=', 1)
            ->whereNotNull('last_login_at')
            ->where('last_login_at', '>=', $sinceDate)
            ->count();

        $totalWhiteUsers = DB::table('users')
            ->where('type', '=', 1)
            ->count();


        // Blue collar logged in
        $blueLoggedIn = DB::table('users')
            ->where('type', '=', 2)
            ->whereNotNull('last_login_at')
            ->where('last_login_at', '>=', $sinceDate)
            ->count();

        $totalBlueUsers = DB::table('users')
            ->where('type', '=', 2)
            ->count();


        // Type NULL (others)
        $otherLoggedIn = DB::table('users')
            ->whereNull('type')
            ->whereNotNull('last_login_at')
            ->where('last_login_at', '>=', $sinceDate)
            ->count();

        $totalOtherUsers = DB::table('users')
            ->whereNull('type')
            ->count();

                // ---------------------------
        // 1. ACTIVE USERS (last login)
        // ---------------------------
        $activeUsers = DB::table('user_logins')
            ->select(DB::raw('DATE(login_at) AS date'), DB::raw('COUNT(DISTINCT user_id) AS total'))
            ->where('login_at', '>=', Carbon::now()->subDays(30))
            ->groupBy('date')
            ->orderBy('date')
            ->get();

        $activeUsersChart = [
            'labels' => $activeUsers->pluck('date')->values()->all(),
            'data'   => $activeUsers->pluck('total')->values()->all(),
        ];

        //Log::info('Active Users Data: ', $activeUsersChart);

        // -------------------------------
        // 2. MOST VIEWED NEWS (from Redis)
        // -------------------------------
        $newsViews = [];

        $redisKeys = Redis::keys('viewCountForNews*');

        foreach ($redisKeys as $key) {

            $newsId = str_replace('viewCountForNews', '', $key);
            $views = Redis::get($key);

            // Fetch title from DB
            $news = DB::table('news')
                ->select('id', 'title')
                ->where('id', $newsId)
                ->first();

            // ✅ If title is null/empty → delete Redis key + skip
            if (!$news || empty(trim($news->title))) {

                Log::info("Deleting Redis key '$key' because title for News ID $newsId is null");

                Redis::del($key); // <-- delete the key here
                continue;
            }

            // ✅ Keep only valid news
            $newsViews[] = [
                'news_id' => $news->id,
                'title'   => $news->title,
                'views'   => (int)$views,
            ];
        }

        $totalData = Redis::zrevrange(
            'menu:view:categories',
            0,
            19,
            ['withscores' => true]
        );

        $menuViews = [];
        foreach ($totalData as $menuKey => $totalViews) {

            // Per-type scores (may return null if not exists)
            $white = Redis::zscore('menu:view:categories:white', $menuKey);
            $blue  = Redis::zscore('menu:view:categories:blue', $menuKey);
            $other = Redis::zscore('menu:view:categories:other', $menuKey);

            $menuViews[] = [
                'menu_key'   => $menuKey,
                'total'      => (int) $totalViews,
                'white'      => (int) ($white ?? 0),
                'blue'       => (int) ($blue ?? 0),
                'other'      => (int) ($other ?? 0),
            ];
        }

        // ✅ Sort DESC by views
        usort($newsViews, fn($a, $b) => $b['views'] <=> $a['views']);

        // ✅ Only keep top 20 most viewed news
        $newsViews = array_slice($newsViews, 0, 20);

        // ✅ Only keep top 20 most viewed menus
        $menuViews = array_slice($menuViews, 0, 20);

        //Log::info('Most Viewed News Data: ', $newsViews->toArray());

        return view('dashboard', [
            'activeUsersChart' => $activeUsersChart,
            'newsViews'        => $newsViews,
            'menuViews'        => $menuViews,

            'totalLoggedIn'    => $totalLoggedIn,
            'totalUsers'       => $totalUsers,

            'whiteLoggedIn'    => $whiteLoggedIn,
            'totalWhiteUsers'  => $totalWhiteUsers,

            'blueLoggedIn'     => $blueLoggedIn,
            'totalBlueUsers'   => $totalBlueUsers,

            'otherLoggedIn'    => $otherLoggedIn,
            'totalOtherUsers'  => $totalOtherUsers,
        ]);
    }
}