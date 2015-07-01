<?php

/** There is an error in this task */
define('CODE_ERROR', 0);

/** This task is run successfully */
define('CODE_SUCCESS', 1);

/** The user must sign out from the application to do this task */
define('CODE_GUEST', 2);

/** The user must be signed in to the application to do this task */
define('CODE_AUTH', 3);

/** This user account was not actived */
define('CODE_ACTIVE', 4);

/** The csrf token missing or incorrect */
define('CODE_TOKEN', 5);

/**
 * Return a response with a message and code
 * 
 * @param string $msg_id        	
 * @param int $success        	
 * @param int|array|mixed $data     	
 * @return \Illuminate\Http\JsonResponse
 *
 */
function msg($msg_id, $success = CODE_ERROR, $data = null){
	$response = array('success' => $success, 'message' => trans($msg_id));
	if ($data != null)
		$response['data'] = $data;
	return response()->json($response);
}