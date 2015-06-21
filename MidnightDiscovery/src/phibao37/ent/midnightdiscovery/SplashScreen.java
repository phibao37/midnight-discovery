package phibao37.ent.midnightdiscovery;

import java.util.Locale;

import phibao37.ent.adapter.IntroduceScreenAdapter;
import phibao37.ent.app.App;
import phibao37.ent.app.ID;
import phibao37.ent.utils.SessionManager;
import phibao37.support.utils.GraphUtils;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SplashScreen extends AppCompatActivity {
	
	private ViewPager mPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setupApplication();
		if (SessionManager.isLoggedin()){
			Intent intent = new Intent(this, MainActivity.class);
			
			startActivity(intent);
			finish();
		}
		
		setContentView(R.layout.activity_splash_screen);
		mPager = (ViewPager) findViewById(R.id.pager_introduce);
		IntroduceScreenAdapter adapter = new IntroduceScreenAdapter(
				getSupportFragmentManager());
		mPager.setAdapter(adapter);
		
		findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SplashScreen.this, LoginActivity.class));
			}
		});
		
		findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
			}
		});
		
		findViewById(R.id.btn_go_home).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(SplashScreen.this, MainActivity.class));
				finish();
			}
		});
	}
	
	private void setupApplication(){
		
		App.getInstance()
			.getWebBuilder()
			.setURL(ID.HOST)
			.addCookie(ID.CK_LOCALE, Locale.getDefault().getLanguage());
	}

	public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        
        if (hasFocus){
        	mPager.animate()
        		.setDuration(1000)
        		.translationY(GraphUtils.dpToPixel(50, this));
        }
    }
}
