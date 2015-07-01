package phibao37.ent.models;

import org.json.JSONException;
import org.json.JSONObject;

import phibao37.ent.app.ID;
import phibao37.ent.models.Manager.Manageable;

/** Represent a application user*/
public class User extends Manageable {
	
	private int id;
	private String username;
	private String email;
	private String fullname;
	private String birthday;
	private boolean sex;
	
	public User(int id, String username, String email, String fullname, 
			String birthday, boolean sex){
		this.id = id;
		this.username = username;
		this.email = email;
		this.fullname = fullname;
		this.birthday = birthday;
		this.sex = sex;
	}
	
	/** Create an user from HTTP JSON response */
	public static User parseUser(JSONObject user){
		try{
			int id = user.getInt(ID.PARAM_ID);
			String username = user.getString(ID.PARAM_USERNAME);
			String email = user.getString(ID.PARAM_EMAIL);
			String fullname = user.getString(ID.PARAM_FULLNAME);
			String birthday = null;
			boolean sex = false;
			
			if (!user.isNull(ID.PARAM_BIRTHDAY))
				birthday = user.getString(ID.PARAM_BIRTHDAY);
			if (!user.isNull(ID.PARAM_SEX))
				sex = user.getBoolean(ID.PARAM_SEX);
			
			return new User(id, username, email, fullname, birthday, sex);
		} catch (JSONException e){
			return null;
		}
	}
	
	public int getId() {
		return id;
	}

	public String getUsername() {
		return username;
	}

	public String getEmail() {
		return email;
	}

	public String getFullname() {
		return fullname;
	}

	public String getBirthday() {
		return birthday;
	}

	public boolean isMale() {
		return sex == false;
	}
	
}
