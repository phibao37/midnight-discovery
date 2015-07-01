package phibao37.ent.midnightdiscovery;

import java.util.ArrayList;

import phibao37.ent.adapter.NavDrawerAdapter;
import phibao37.ent.models.NavDrawerItem;
import phibao37.ent.utils.SessionManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
	
	private static final int REQUEST_CODE_LOGIN = 1001;
	
	private DrawerLayout mDrawerLayout;
	private ListView lvDrawer;
	
	private ImageView imgAvatar;
	private Button btnLogin;
	private Button btnLogout;
	private TextView txtUserName;

	private ActionBarDrawerToggle mDrawerToggle;
	private ArrayList<NavDrawerItem> listNavItem; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		Toolbar actionBar = (Toolbar) findViewById(R.id.bar_actionbar);
		setSupportActionBar(actionBar);
		
		listNavItem = new ArrayList<NavDrawerItem>();
		listNavItem.add(new NavDrawerItem(R.string.nav_home, R.drawable.ic_home));
		listNavItem.add(new NavDrawerItem(R.string.nav_library, R.drawable.ic_library));
		lvDrawer = (ListView) findViewById(R.id.drawer_left);
		lvDrawer.setAdapter(new NavDrawerAdapter(this, listNavItem));
		lvDrawer.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				selectItem(position);
			}
		});
		
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, actionBar, 
				android.R.string.yes, android.R.string.no){
			
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		
		imgAvatar = (ImageView) findViewById(R.id.img_avatar);
		txtUserName = (TextView) findViewById(R.id.txt_user_name);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnLogout = (Button) findViewById(R.id.btn_logout);
		
		btnLogin.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(MainActivity.this, LoginActivity.class);
				startActivityForResult(i, REQUEST_CODE_LOGIN);
			}
		});
		btnLogout.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				SessionManager.logout();
				invalidateOptionsMenu();
			}
		});
		imgAvatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (SessionManager.isLoggedin()){
					Intent i = new Intent(MainActivity.this, ProfileActivity.class);
					startActivity(i);
					mDrawerLayout.closeDrawers();
				}
			}
		});
		
		if (savedInstanceState == null){
			selectItem(0);
		}
	}
	
	private void selectItem(int position){
		syncActionBar(position);
		mDrawerLayout.closeDrawers();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK)
			return;
		
		switch (requestCode) {
		case REQUEST_CODE_LOGIN:
			invalidateOptionsMenu();
			break;
		}
	}

	private void syncActionBar(int position){
		lvDrawer.setItemChecked(position, true);
		setTitle(listNavItem.get(position).getTitle());
		lvDrawer.setItemChecked(position, true);
	}
	

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (SessionManager.isLoggedin()) {
			btnLogin.setVisibility(View.GONE);
			btnLogout.setVisibility(View.VISIBLE);
			txtUserName.setText(SessionManager.getUser().getFullname());
		} else {
			btnLogin.setVisibility(View.VISIBLE);
			btnLogout.setVisibility(View.GONE);
			txtUserName.setText(R.string.app_name);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		switch (item.getItemId()){
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }
	
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		mDrawerToggle.syncState();
	}
	
}
