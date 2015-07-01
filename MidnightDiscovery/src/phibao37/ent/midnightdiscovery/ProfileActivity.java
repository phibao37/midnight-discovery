package phibao37.ent.midnightdiscovery;

import phibao37.ent.app.ID;
import phibao37.ent.app.List;
import phibao37.ent.models.User;
import phibao37.ent.utils.SessionManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.content.Intent;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {
	
	private User mUser;
	
	private TextView txtName;
	private TextView txtBirthday;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
		
		Intent intent = getIntent();
		int userId = intent.getIntExtra(ID.EXTRA_USER, 0);
		if (userId == 0)
			mUser = SessionManager.getUser();
		else
			mUser = List.getUserManager().get(userId);
		
		// Set up the action bar.
		Toolbar actionBar = (Toolbar) findViewById(R.id.bar_actionbar);
		setSupportActionBar(actionBar);

		ViewPager pager = (ViewPager) findViewById(R.id.pager_profile);
		pager.setAdapter(new ProfileSectionAdapter(
				getSupportFragmentManager(), new int[]{
					R.string.t_profile
				}));
		
		txtName = (TextView) findViewById(R.id.txt_user_name);
		txtBirthday = (TextView) findViewById(R.id.txt_user_birthday);
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		txtName.setText(mUser.getUsername());
		txtBirthday.setText(mUser.getBirthday());
		
		return super.onPrepareOptionsMenu(menu);
	}

	class ProfileFragment extends Fragment {
		static final String EXTRA_PAGE = "page";
		
		private TextView txtUsername;
		private TextView txtEmail;
		private TextView txtSex;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View root = inflater.inflate(R.layout.profile_details_fragment, 
					container, false);
			
			txtUsername = (TextView) root.findViewById(R.id.txt_username);
			txtEmail = (TextView) root.findViewById(R.id.txt_email);
			txtSex = (TextView) root.findViewById(R.id.txt_sex);
			
			return root;
		}

		@Override
		public void onPrepareOptionsMenu(Menu menu) {
			super.onPrepareOptionsMenu(menu);
			
			txtUsername.setText(mUser.getUsername());
			txtEmail.setText(mUser.getEmail());
			txtSex.setText(mUser.isMale() ? R.string.sex_male : R.string.sex_female);
		}
	}
	
	class ProfileSectionAdapter extends FragmentPagerAdapter{
		private int[] mTitles;
		
		public ProfileSectionAdapter(FragmentManager fm, int[] titles) {
			super(fm);
			mTitles = titles;
		}

		@Override
		public Fragment getItem(int position) {
			ProfileFragment fragment = new ProfileFragment();
			Bundle b = new Bundle();
			
			b.putInt(ProfileFragment.EXTRA_PAGE, position);
			fragment.setArguments(b);
			fragment.setHasOptionsMenu(true);
			return fragment;
		}

		@Override
		public int getCount() {
			return mTitles.length;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return getString(mTitles[position]);
		}
		
	}
}
