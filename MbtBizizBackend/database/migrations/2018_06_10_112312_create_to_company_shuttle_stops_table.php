<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateToCompanyShuttleStopsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('to_company_shuttle_stops', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('shuttle_id')->unsigned();
            $table->foreign('shuttle_id')->references('id')->on('shuttles');
            $table->integer('stop_id')->unsigned();
            $table->foreign('stop_id')->references('id')->on('stops');
            $table->integer('order')->unsigned();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('to_company_shuttle_stops');
    }
}
