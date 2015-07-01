<?php

/*
|--------------------------------------------------------------------------
| Application Routes
|--------------------------------------------------------------------------
|
| Here is where you can register all of the routes for an application.
| It's a breeze. Simply tell Laravel the URIs it should respond to
| and give it the controller to call when that URI is requested.
|
*/

Route::get('csrf', function(){
	return msg('', CODE_SUCCESS, csrf_token());
});

Route::post('register', 'UserController@register');
Route::post('login', 'UserController@login');
Route::get('reactive', 'UserController@reactive');
Route::get('activeAccount/{username}/{code}', 
		['uses' => 'UserController@activeAccount', 'as' => 'activeAccount']);
Route::get('getPassword', 'UserController@getPassword');


Route::get('test', 'TestController@test');
Route::get('/', function () {
	
});