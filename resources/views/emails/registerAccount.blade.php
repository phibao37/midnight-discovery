<?php 
function host($str){
	return str_replace("http://10.0.3.2", "http://localhost", $str);
}
?>
<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>{{trans('reg.active')}}</title>
</head>
<body>
	<h1>{{trans('core.welcome', array('name' => $name))}}</h1>
	{!! trans($first ? 'reg.message' : 'reg.message.resend') !!}
	<br /><br />
	<a href="{{ host(URL::route('activeAccount', array(
		'username' => $username, 
		'code' => $code,
		'locale' => App::getLocale()
	))) }}" 
	title="{{trans('reg.active')}}">{{trans('reg.click')}}</a>
</body>
</html>