<?php

namespace App\Services;

use Illuminate\Support\Facades\Redis;

class MenuViewService
{
    public static function increment(string $menuKey, int $count = 1): void
    {
        // total
        Redis::zincrby('menu:view:categories', $count, $menuKey);

        $userType = auth()->user()->type ?? 0;
        // type-based
        switch ($userType) {
            case 1:
                Redis::zincrby('menu:view:categories:white', 1, $menuKey);
                break;
            case 2:
                Redis::zincrby('menu:view:categories:blue', 1, $menuKey);
                break;
            default:
                Redis::zincrby('menu:view:categories:other', 1, $menuKey);
        }

    }

    public static function top(int $limit = 10): array
    {
        $data = Redis::zrevrange(
            'menu:view:categories',
            0,
            $limit - 1,
            ['withscores' => true]
        );

        $result = [];
        foreach ($data as $menu => $views) {
            $result[] = [
                'menu_key' => $menu,
                'views' => (int) $views
            ];
        }

        return $result;
    }
}