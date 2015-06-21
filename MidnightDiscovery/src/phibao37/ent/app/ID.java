package phibao37.ent.app;

/**
 * Define all application static id name here
 */
public class ID {
	
	/* ------------------- SERVER URL ------------------- */
	
	/** Base server for the application */
	public static final String HOST = "http://10.0.3.2/midnightdiscovery/";
	
	public static final String URL_REGISTER = HOST + "register";
	public static final String URL_LOGIN = HOST + "login";
	public static final String URL_REACTIVE = HOST + "reactive";
	public static final String URL_GET_PASSWORD = HOST + "getPassword";
	
	public static final String URL_TEST = HOST + "test";
	
	/* ------------------- REQUEST PARAM NAME ------------------- */
	
	public static final String PARAM_USERNAME = "username";
	public static final String PARAM_PASSWORD = "password";
	public static final String PARAM_PASSWORD_2 = "password2";
	public static final String PARAM_EMAIL = "email";
	public static final String PARAM_FULLNAME = "fullname";
	
	/** Use in session manager only */
	public static final String PARAM_ID = "id";
	
	public static final String PARAM_BIRTHDAY = "birthday";
	public static final String PARAM_SEX = "sex";
	
	/* ------------------- RESPONSE CODE ------------------- */
	
	/** This task was aborted by the user*/
	public static final int CODE_ABORT = -1;
	
	/** There is an error in this task */
	public static final int CODE_ERROR = 0;
	
	/** This task is run successfully */
	public static final int CODE_SUCCESS = 1;
	
	/** The user must sign out from the application to do this task */
	public static final int CODE_GUEST = 2;
	
	/** The user must be signed in to the application to do this task */
	public static final int CODE_AUTH = 3;
	
	/** This user account was not actived */
	public static final int CODE_ACTIVE = 4;
	
	/* ------------------- COOKIE PARAM ------------------- */
	
	public static final String CK_LOCALE = "locale";
}
