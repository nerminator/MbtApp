<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Redis;
use App\Services\MenuViewService;

class MenuViewController extends Controller
{
    /**
     * Mobile app çağırır
     * Menüye basıldığında view sayısını artırır
     */
    public function increment(Request $request)
    {
        $this->validate($request, [
        'menu_key' => 'required|string|max:100'
        ]);

        MenuViewService::increment($request->menu_key);

        return response()->json(['status' => 'ok']);
    }

    /**
     * Admin panel için
     * En çok görüntülenen menüler
     */
    public function top(Request $request)
    {
        $limit = (int) $request->get('limit', 10);

        return response()->json(
          MenuViewService::top((int) $request->get('limit', 10))
      );
    }
}