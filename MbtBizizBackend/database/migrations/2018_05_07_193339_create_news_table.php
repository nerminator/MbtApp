<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateNewsTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('news', function (Blueprint $table) {
            $table->increments('id');
            $table->string('list_text');
            $table->string('list_text_en');
            $table->string('title')->nullable();
            $table->string('title_en')->nullable();
            $table->text('text');
            $table->text('text_en');
            $table->string('sub_title')->nullable();
            $table->string('sub_title_en')->nullable();
            $table->text('sub_text')->nullable();
            $table->text('sub_text_en')->nullable();
            $table->string('image')->nullable();
            $table->string('url')->nullable();
            $table->tinyInteger('type')->unsigned();
            $table->tinyInteger('discount_type')->unsigned()->nullable();
            $table->timestampTz('start_time');
            $table->timestampTz('end_time')->nullable();
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
        Schema::dropIfExists('news');
    }
}
