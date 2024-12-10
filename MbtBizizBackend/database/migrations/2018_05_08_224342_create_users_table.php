<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUsersTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('users', function (Blueprint $table) {
            $table->increments('id');
            $table->integer('register_number')->unsigned()->unique();
            $table->string('user_name');
            $table->string('name_surname');
            $table->integer('expense_center_number')->unsigned();
            $table->string('expense_center');
            $table->string('birthday', 5)->nullable();
            $table->string('location');
            $table->integer('company_location_id')->unsigned();
            $table->foreign('company_location_id')->references('id')->on('company_locations');
            $table->string('title');
            $table->string('mobile_phone');
            $table->string('email');
            $table->integer('company_code')->unsigned();
            $table->uuid('token');
            $table->string('raw_type');
            $table->tinyInteger('type')->unsigned();
            $table->tinyInteger('status')->unsigned();
            $table->timestampsTz();
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('users');
    }
}
