<?php

namespace App\Jobs;

use Illuminate\Bus\Queueable;
use Illuminate\Queue\SerializesModels;
use Illuminate\Queue\InteractsWithQueue;
use Illuminate\Contracts\Queue\ShouldQueue;
use Illuminate\Foundation\Bus\Dispatchable;
use Illuminate\Support\Carbon;
use Illuminate\Support\Facades\DB;

class DeleteNewsNotification implements ShouldQueue
{
    use Dispatchable, InteractsWithQueue, Queueable, SerializesModels;

    private $newsId;

    /**
     * Create a new job instance.
     *
     * @return void
     */
    public function __construct($newsId)
    {
        $this->newsId = $newsId;
    }

    /**
     * Execute the job.
     *
     * @return void
     */
    public function handle()
    {
        $this->_deleteNewsNotification();
    }

    private function _deleteNewsNotification()
    {
        DB::table('user_notifications')->where('news_id', $this->newsId)->where('deleted', 0)->update(['deleted' => 1, 'updated_at' => Carbon::now()]);
    }
}
