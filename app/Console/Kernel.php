<?php

namespace App\Console;

use Laravel\Lumen\Console\Kernel as ConsoleKernel;
use Illuminate\Console\Scheduling\Schedule;

class Kernel extends ConsoleKernel
{
    /**
     * The Artisan commands provided by your application.
     *
     * @var array
     */
    protected $commands = [
        'App\Console\Commands\SetFoodMenu',
        'App\Console\Commands\SetFoodShuttle',

        'App\Console\Commands\SetBirthdayList',
        'App\Console\Commands\SetUsers',

        'App\Console\Commands\SetWorkingHoursInfo',
        'App\Console\Commands\SetWorkingCalendar',

        'App\Console\Commands\SetEmployeeShuttles',
        'App\Console\Commands\SetRingShuttles',
        'App\Console\Commands\SetOtherShuttles',
        'App\Console\Commands\SetInterlocationShuttles',
    ];

    /**
     * Define the application's command schedule.
     *
     * @param  \Illuminate\Console\Scheduling\Schedule $schedule
     * @return void
     */
    protected function schedule(Schedule $schedule)
    {
        $schedule->command('set:employee_shuttles')->hourly()->timezone('Europe/Istanbul');
        $schedule->command('set:ring_shuttles')->hourly()->timezone('Europe/Istanbul');
        $schedule->command('set:other_shuttles')->hourly()->timezone('Europe/Istanbul');
        $schedule->command('set:interlocation_shuttles')->hourly()->timezone('Europe/Istanbul');

        $schedule->command('set:users')->dailyAt('07:05')->timezone('Europe/Istanbul');
        $schedule->command('set:birthday_list')->dailyAt('07:07')->timezone('Europe/Istanbul');

        $schedule->command('set:working_hours_info')->dailyAt('07:08')->timezone('Europe/Istanbul');
        $schedule->command('set:working_calendar')->dailyAt('07:08')->timezone('Europe/Istanbul');

        $schedule->command('set:food_menu')->dailyAt('07:09')->timezone('Europe/Istanbul');
        $schedule->command('set:food_shuttle')->dailyAt('07:09')->timezone('Europe/Istanbul');

        $schedule->command('set:food_menu')->hourly()->timezone('Europe/Istanbul');
        $schedule->command('set:food_shuttle')->hourly()->timezone('Europe/Istanbul');
    }
}
