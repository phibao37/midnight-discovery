package phibao37.ent.midnightdiscovery;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class IntroduceScreenFragment extends Fragment {
	
	public static IntroduceScreenFragment newInstance(int imageId){
		IntroduceScreenFragment fragment = new IntroduceScreenFragment();
		Bundle b = new Bundle();
		
		b.putInt(ARG_IMAGE, imageId);
		fragment.setArguments(b);
		return fragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		int imageId = getArguments().getInt(ARG_IMAGE);
		
		if (imageId == 0){
			return inflater.inflate(R.layout.layout_introduce_first, container, false);
		} else {
			View root = inflater.inflate(R.layout.layout_introduce, container, false);
			ImageView imgIntro = (ImageView) root.findViewById(R.id.img_placeholder);
			
			imgIntro.setImageResource(imageId);
			return root;
		}
	}

	private static String ARG_IMAGE = "image";
}
