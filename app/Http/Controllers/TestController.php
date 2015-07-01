<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Http\Response;
use Input;

class TestController extends Controller {
	
	/**
	 * @param  Request  $r
	 * @return Response
	 */
	public function test(Request $r){
		print_r(Input::all());
	}
}
