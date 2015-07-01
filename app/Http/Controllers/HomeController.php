<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;

class HomeController extends Controller {
	
	/**
	 * @param  Request  $r
	 * @return Response
	 */
	public function csrf(Request $r){
		return msg('', CODE_SUCCESS, csrf_token());
	}
	
	/**
	 * @param  Request  $r
	 * @return Response
	 */
	public function index(Request $r){
		return view('welcome');
	}
}
