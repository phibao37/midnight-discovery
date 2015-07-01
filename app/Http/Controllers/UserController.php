<?php
namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Database\Eloquent\Model;
use Validator, Auth, App\User, Illuminate\Support\Str, Mail;

class UserController extends Controller {
	
	/**
	 * Instantiate a new UserController instance.
	 */
	public function __construct()
	{
		$this->middleware('guest', ['only' => ['login', 'register'] ]);
		$this->middleware('auth', ['only' => ['logout'] ]);
	}
	
	/**
	 * @param  Request  $r
	 * @return Response
	 */
	public function login(Request $r){
		
		if (Validator::make($r->all(), [
				'username' => 'required',
				'password' => 'required'
		])->fails())
			return msg('core.blank');
		
		$username = $r->input('username');
		
		if (Auth::attempt(array(
				'username' => $username, 
				'password' => $r->input('password'),
				'activation' => NULL
		), true)){
			return msg('', CODE_SUCCESS, Auth::user());
		} else {
			$collects = User::where('username', $username);
			
			if ($collects->count() == 1 && $collects->first()->activation != NULL)
				return msg('reg.notactive', CODE_ACTIVE);
			
			return msg('reg.incorrect');
		}
	}
	
	public function register(Request $r){
		try{
			
		//Validate data
		$rules = array(
				'username' => 'required|min:4|regex:/^[a-zA-Z]+[a-zA-Z0-9_]*$/|unique:users',
				'password' => 'required|min:6|different:username|not_in:12345678,password',
				'password2' => 'required|same:password',
				'email' => 'required|email|unique:users',
				'fullname' => 'required|min:5'
		);
		$validator = Validator::make($r->all(), $rules);
		if ($validator->fails())
			return msg(implode("\n", $validator->messages()->all()));
			
		//Save register information
		$info = $r->except(['_token', 'password2']);
		$info['password'] = bcrypt($info['password']);
		$user = User::create($info);
		
		$this->sendActiveMail($user);
		return msg('reg.created', CODE_SUCCESS);
		
		} catch (Exception $e){
			return msg($e->getMessage());
		}
	}
	
	public function reactive(Request $r){
		if (Validator::make($r->all(), [
				'username' => 'required'
		])->fails())
			return msg('core.blank');
		
		$collects = User::where('username', $r->input('username'));
		if ($collects->count() == 0)
			return msg('reg.noAccount.username');
		
		$user = $collects->first();
		if ($user->activation == NULL)
			return msg('reg.activated');
		
		try{
			$this->sendActiveMail($user, FALSE);
			return msg('reg.resended', CODE_SUCCESS);
		} catch (Exception $e){
			return msg($e->getMessage());
		}
	}
	
	private function sendActiveMail(Model $user, $first = TRUE){
		//Generate activate token
		$email = $user->email;
		$code = md5(Str::random(6));
		$data = array(
				"name"	=>	$user->fullname,
				"username"	=> $user->username,
				"email"	=> $email,
				"code"	=>	$code,
				"first" => $first
		);
		$user->activation = $code;
		$user->save();
		
		//Send activate email
		Mail::send('emails.registerAccount', $data, function($message) use($data)
		{
			$message->to($data['email'], $data['name']);
			$message->subject(trans('reg.active'));
		});
	}
	
	public function activeAccount($username, $code){
		$collects = User::where('username', $username);
		
		if ($collects->count() == 0)
			return trans('reg.noAccount');
		
		$user = $collects->first();
		if ($user->activation == $code){
			$user->activation = NULL;
			$user->save();
			return trans('reg.success');
		} else {
			return trans('reg.mismatch');
		}
	}
	
	public function getPassword(Request $r){
		if (Validator::make($r->all(), [
				'email' => 'required'
		])->fails())
			return msg('core.blank');
		
		$email = $r->input('email');
		$collects = User::where('email', $email);
		
		if ($collects->count() == 0)
			return msg('reg.noAccount.email');
		$user = $collects->first();
		
		try{
			$newPassword = Str::random(10);
			$user->password = bcrypt($newPassword);
			$user->save();
			
			$data = array(
					'email' => $email,
					'name' => $user->fullname,
					'newPassword' => $newPassword
			);
			Mail::send('emails.getPassword', $data, function($message) use($data){
				$message->to($data['email'], $data['name']);
				$message->subject(trans('reg.getPassword'));
			});
			
			return msg('reg.passResetted', CODE_SUCCESS);
		} catch (Exception $e){
			return msg($e->getMessage());
		}
	}
	
}
