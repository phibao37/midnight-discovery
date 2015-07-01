package phibao37.ent.app;

import phibao37.ent.models.Manager;
import phibao37.ent.models.User;

/**
 * Hold the reference to the global manager list
 */
public class List {
	
	private static Manager<User> listUser;
	
	/** Get the global user manager */
	public static synchronized Manager<User> getUserManager(){
		if (listUser == null)
			listUser = new Manager<User>();
		return listUser;
	}
	
}
