<?php

namespace App\Http\Middleware;

use Closure;

class SetLocale
{
	/**
	 * Run the request filter.
	 *
	 * @param  \Illuminate\Http\Request  $request
	 * @param  \Closure  $next
	 * @return mixed
	 */
	public function handle($request, Closure $next)
	{
		if ($request->has('locale'))
			\App::setLocale($request->get('locale'));
		if ($request->hasCookie('locale'))
			\App::setLocale($request->cookie('locale'));

		return $next($request);
	}

}