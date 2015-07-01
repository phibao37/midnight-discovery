<!DOCTYPE html>
<html lang="en">
<head>
	<meta charset="UTF-8">
	<title>{{trans('reg.getPassword')}}</title>
</head>
<body>
	<h1>{{trans('core.welcome', array('name' => $name))}}</h1>
	{{trans('reg.message.getPassword')}}
	<br /><br />
	{{trans('reg.newPassword')}}: <b>{{$newPassword}}</b>
</body>
</html>