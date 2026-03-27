<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateWorkingCalendarTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('working_calendar', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('register_number')->unsigned();
            $table->foreign('register_number')->references('register_number')->on('users');
            $table->timestampTz('calendar_date');
            $table->integer('type_id')->unsigned();
            $table->foreign('type_id')->references('id')->on('working_calendar_types');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('working_calendar');
    }
}
