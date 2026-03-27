<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateWorkingHoursInfoTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('working_hours_info', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('register_number')->unsigned();
            $table->foreign('register_number')->references('register_number')->on('users');
            $table->timestampTz('info_date');
            $table->string('hours_info')->nullable();
            $table->float('plus_hours', 4, 2);
            $table->float('minus_hours', 4, 2);
            $table->float('total_hours', 4, 2);
            $table->float('cumulative_hours', 4, 2)->nullable();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('working_hours_info');
    }
}
