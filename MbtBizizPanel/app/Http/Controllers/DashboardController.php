<?php

namespace App\Http\Controllers;

use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
use Illuminate\Support\Facades\Log;
use Illuminate\Support\Facades\Redis;
use Carbon\Carbon;

class DashboardController extends Controller
{
    private function getNewsViewCounts(int $newsId): array
    {
        return [
            'total' => (int) Redis::get('viewCountForNews' . $newsId),
            'white' => (int) Redis::get('viewCountForNews:white:' . $newsId),
            'blue' => (int) Redis::get('viewCountForNews:blue:' . $newsId),
            'other' => (int) Redis::get('viewCountForNews:other:' . $newsId),
        ];
    }

    private function deleteNewsViewKeys(int $newsId): void
    {
        Redis::del('viewCountForNews' . $newsId);
        Redis::del('viewCountForNews:white:' . $newsId);
        Redis::del('viewCountForNews:blue:' . $newsId);
        Redis::del('viewCountForNews:other:' . $newsId);
    }

    private function resolveActiveUsersTypeFilter(Request $request): array
    {
        $type = $request->query('user_type', 'all');

        if ($type === 'white') {
            return [
                'value' => 'white',
                'title_suffix' => ' - White Collar',
            ];
        }

        if ($type === 'blue') {
            return [
                'value' => 'blue',
                'title_suffix' => ' - Blue Collar',
            ];
        }

        if ($type === 'other') {
            return [
                'value' => 'other',
                'title_suffix' => ' - Others',
            ];
        }

        return [
            'value' => 'all',
            'title_suffix' => '',
        ];
    }

    private function resolveActiveUsersRange(Request $request): array
    {
        $period = $request->query('period', '30d');
        $today = Carbon::today();

        if ($period === 'custom') {
            $startInput = $request->query('start_date');
            $endInput = $request->query('end_date');

            if ($startInput && $endInput) {
                try {
                    $startDate = Carbon::parse($startInput)->startOfDay();
                    $endDate = Carbon::parse($endInput)->endOfDay();

                    if ($startDate->gt($endDate)) {
                        [$startDate, $endDate] = [$endDate->copy()->startOfDay(), $startDate->copy()->endOfDay()];
                    }

                    return [
                        'period' => 'custom',
                        'start' => $startDate,
                        'end' => $endDate,
                        'title' => 'Active Users (Custom Interval)',
                        'start_date' => $startDate->toDateString(),
                        'end_date' => $endDate->toDateString(),
                    ];
                } catch (\Throwable $exception) {
                    Log::warning('Invalid custom dashboard date range.', [
                        'start_date' => $startInput,
                        'end_date' => $endInput,
                    ]);
                }
            }

            $period = '30d';
        }

        if ($period === '1y') {
            return [
                'period' => '1y',
                'start' => $today->copy()->subYear()->startOfDay(),
                'end' => $today->copy()->endOfDay(),
                'title' => 'Active Users (Last 1 Year)',
                'start_date' => null,
                'end_date' => null,
            ];
        }

        if ($period === '3m') {
            return [
                'period' => '3m',
                'start' => $today->copy()->subMonths(3)->startOfDay(),
                'end' => $today->copy()->endOfDay(),
                'title' => 'Active Users (Last 3 Months)',
                'start_date' => null,
                'end_date' => null,
            ];
        }

        return [
            'period' => '30d',
            'start' => $today->copy()->subDays(30)->startOfDay(),
            'end' => $today->copy()->endOfDay(),
            'title' => 'Active Users (Last 30 Days)',
            'start_date' => null,
            'end_date' => null,
        ];
    }

    private function buildActiveUsersChart(Carbon $startDate, Carbon $endDate, string $userType): array
    {
        $activeUsers = DB::table('user_logins')
            ->join('users', 'users.id', '=', 'user_logins.user_id')
            ->select(DB::raw('DATE(login_at) AS date'), DB::raw('COUNT(DISTINCT user_id) AS total'))
            ->whereBetween('login_at', [$startDate, $endDate]);

        if ($userType === 'white') {
            $activeUsers->where('users.type', 1);
        } elseif ($userType === 'blue') {
            $activeUsers->where('users.type', 2);
        } elseif ($userType === 'other') {
            $activeUsers->whereNull('users.type');
        }

        $activeUsers = $activeUsers
            ->groupBy('date')
            ->orderBy('date')
            ->get()
            ->pluck('total', 'date');

        $labels = [];
        $data = [];
        $cursor = $startDate->copy()->startOfDay();
        $lastDate = $endDate->copy()->startOfDay();

        while ($cursor->lte($lastDate)) {
            $dateKey = $cursor->toDateString();
            $labels[] = $dateKey;
            $data[] = (int) ($activeUsers[$dateKey] ?? 0);
            $cursor->addDay();
        }

        return [
            'labels' => $labels,
            'data' => $data,
        ];
    }

    public function index(Request $request)
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
        // 1. ACTIVE USERS (by interval)
        // ---------------------------
        $activeUsersFilter = $this->resolveActiveUsersRange($request);
        $activeUsersTypeFilter = $this->resolveActiveUsersTypeFilter($request);
        $activeUsersFilter['title'] .= $activeUsersTypeFilter['title_suffix'];
        $activeUsersChart = $this->buildActiveUsersChart(
            $activeUsersFilter['start'],
            $activeUsersFilter['end'],
            $activeUsersTypeFilter['value']
        );

        //Log::info('Active Users Data: ', $activeUsersChart);

        // -------------------------------
        // 2. MOST VIEWED NEWS (from Redis)
        // -------------------------------
        $newsViews = [];

        $redisKeys = Redis::keys('viewCountForNews[0-9]*');

        foreach ($redisKeys as $key) {
            if (!preg_match('/^viewCountForNews(\d+)$/', $key, $matches)) {
                continue;
            }

            $newsId = (int) $matches[1];
            $viewCounts = $this->getNewsViewCounts($newsId);

            // Fetch title from DB
            $news = DB::table('news')
                ->select('id', 'title')
                ->where('id', $newsId)
                ->first();

            // ✅ If title is null/empty → delete Redis key + skip
            if (!$news || empty(trim($news->title))) {

                Log::info("Deleting Redis key '$key' because title for News ID $newsId is null");

                $this->deleteNewsViewKeys($newsId);
                continue;
            }

            // ✅ Keep only valid news
            $newsViews[] = [
                'news_id' => $news->id,
                'title'   => $news->title,
                'views'   => $viewCounts['total'],
                'white'   => $viewCounts['white'],
                'blue'    => $viewCounts['blue'],
                'other'   => $viewCounts['other'],
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
            'activeUsersFilter' => $activeUsersFilter,
            'activeUsersTypeFilter' => $activeUsersTypeFilter,
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