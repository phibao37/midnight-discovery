package phibao37.ent.app;

import phibao37.support.utils.WebBuilder;
import android.app.Application;

public class App extends Application {
	
	private static App instance;
	private WebBuilder mWebBuilder;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}
	
	/** Get the instance of the global Application context*/
	public static App getInstance(){
		return instance;
	}
	
	/** Get the global WebBuilder*/
	public synchronized WebBuilder getWebBuilder(){
		if (mWebBuilder == null){
			mWebBuilder = new WebBuilder();
		}
		return mWebBuilder;
	}
}
