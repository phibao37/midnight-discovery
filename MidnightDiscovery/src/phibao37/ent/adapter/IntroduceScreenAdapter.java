package phibao37.ent.adapter;

import phibao37.ent.midnightdiscovery.IntroduceScreenFragment;
import phibao37.ent.midnightdiscovery.R;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class IntroduceScreenAdapter extends FragmentPagerAdapter {

	protected static final int[] IMAGES = new int[] {
		0,
		R.drawable.slide1,
		R.drawable.slide2,
		R.drawable.slide3
	};
	
	public IntroduceScreenAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int position) {
		return IntroduceScreenFragment.newInstance(IMAGES[position]);
	}

	@Override
	public int getCount() {
		return IMAGES.length;
	}

}
