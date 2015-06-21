package phibao37.support.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Respesent a HTTP response with JSONObject type
 */
public class Response {
	
	private static int DEFAULT_CODE = 0;
	
	/***
	 * Set the default code to use with {@link Response#make(String)}
	 */
	public static void setDefaultCode(int code){
		DEFAULT_CODE = code;
	}
	
	/** The response code */
	public final int code;
	
	/** The response message */
	public final String message;
	
	private JSONObject mObjectData;
	private JSONArray mArrayData;
	
	private Response(JSONObject json) throws JSONException{
		code = json.getInt(JS_CODE);
		message = json.getString(JS_MESSAGE);
		if (json.has(JS_DATA)){
			try{
				mObjectData = json.getJSONObject(JS_DATA);
			} catch (JSONException e){
				mArrayData = json.getJSONArray(JS_DATA);
			}
		}
	}
	
	private Response(int code, String message){
		this.code = code;
		this.message = message;
	}
	
	/***
	 * Parse an JSONOjbect data as a response
	 * @param json the object received by HTTP response, must contains these mapping name:
	 * <ul>
	 * 	<li><b>success (int)</b>: Represent a 'success' code</li>
	 * 	<li><b>message (String)</b>: Represent a response message</li>
	 * 	<li><i>success (Optional)</i>: Represent an optional response data<br/>
	 * 	If set, the mapped value must be a {@link JSONObject} or {@link JSONArray}</li>
	 * </ul>
	 * @return the {@link Response} object contains a HTTP response data
	 */
	public static Response parse(JSONObject json){
		try {
			return new Response(json);
		} catch (JSONException e) {
			return null;
		}
	}
	
	/** Make a response with the message, the code is set to the default code*/
	public static Response make(String message){
		return new Response(DEFAULT_CODE, message);
	}
	
	/** Make a response with the message and the code*/
	public static Response make(String message, int code){
		return new Response(code, message);
	}
	
	/**
	 * Return the data attached to the response
	 */
	@SuppressWarnings("unchecked")
	public <T> T getData(Class<T> type){
		if (type == JSONObject.class)
			return (T) mObjectData;
		else
			return (T) mArrayData;
	}
	
	private static final String JS_CODE = "success";
	private static final String JS_MESSAGE = "message";
	private static final String JS_DATA = "data";
}
