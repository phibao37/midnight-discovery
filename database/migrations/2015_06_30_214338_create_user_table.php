<?php

use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class CreateUserTable extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create ( 'users', function (Blueprint $t) {
			$t->increments ( 'id' );
			$t->string ( 'username', 16 );
			$t->string ( 'password', 64 );
			$t->string ( 'email', 30 );
			$t->string ( 'fullname', 30 );
			$t->date ( 'birthday' )->nullable();
			$t->boolean ( 'sex' )->nullable();
			$t->string ( 'activation', 40 )->nullable();
			$t->rememberToken ();
			$t->timestamps ();
		} );
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::drop ( 'users' );
    }
}
