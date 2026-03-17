<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateShuttlesTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('shuttles', function (Blueprint $table) {
            $table->increments('id');
            $table->string('departure_location');
            $table->integer('company_location_id')->unsigned();
            $table->foreign('company_location_id')->references('id')->on('company_locations');
            $table->string('license_plate');
            $table->string('driver_name_surname');
            $table->string('driver_telephone');
            $table->time('to_departure_time')->nullable();
            $table->time('to_arrival_time')->nullable();
            $table->tinyInteger('to_time_type')->unsigned()->nullable();
            $table->time('from_departure_time')->nullable();
            $table->time('from_arrival_time')->nullable();
            $table->tinyInteger('from_time_type')->unsigned()->nullable();
            $table->tinyInteger('type')->unsigned()->nullable();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('shuttles');
    }
}
