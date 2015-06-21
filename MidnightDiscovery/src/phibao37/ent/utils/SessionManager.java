package phibao37.ent.utils;

import phibao37.ent.app.App;
import phibao37.ent.app.ID;
import phibao37.ent.models.User;
import android.content.SharedPreferences;

public class SessionManager {
	private static final String PREF_NAME = "session";
	private static final SessionManager instance = new SessionManager();
	
	/** Get the logged in user */
	public static User getUser(){
		return instance.mUser;
	}
	
	private SharedPreferences prefs;
	private User mUser;
	
	private SessionManager(){
		prefs = App.getInstance().getSharedPreferences(PREF_NAME, 0);
		if (prefs.contains(ID.PARAM_ID))
			mUser = new User(
					prefs.getInt(ID.PARAM_ID, 0),
					prefs.getString(ID.PARAM_USERNAME, null),
					prefs.getString(ID.PARAM_EMAIL, null),
					prefs.getString(ID.PARAM_FULLNAME, null),
					prefs.getString(ID.PARAM_BIRTHDAY, null),
					prefs.getBoolean(ID.PARAM_SEX, false)
				);
	}
	
	/***
	 * Set this user as an logged in account
	 */
	public static void setUser(User user){
		instance.mUser = user;
		
		instance.prefs.edit()
			.putInt(ID.PARAM_ID, user.getId())
			.putString(ID.PARAM_USERNAME, user.getUsername())
			.putString(ID.PARAM_EMAIL, user.getEmail())
			.putString(ID.PARAM_FULLNAME, user.getFullname())
			.putString(ID.PARAM_BIRTHDAY, user.getBirthday())
			.putBoolean(ID.PARAM_SEX, user.isMale())
			.commit();
	}
	
	/** Check if there is an user logged into application */
	public static boolean isLoggedin(){
		return instance.mUser != null;
	}
	
	/** Clear the application session */
	public static void logout(){
		instance.mUser = null;
		instance.prefs.edit().clear().commit();
	}
}
