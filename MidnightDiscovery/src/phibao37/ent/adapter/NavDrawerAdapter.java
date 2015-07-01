package phibao37.ent.adapter;

import java.util.ArrayList;

import phibao37.ent.midnightdiscovery.R;
import phibao37.ent.models.NavDrawerItem;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class NavDrawerAdapter extends BaseAdapter {
	
	private Activity activity;
	private ArrayList<NavDrawerItem> listNavItem;
	
	public NavDrawerAdapter(Activity activity, ArrayList<NavDrawerItem> navItemList) {
		this.activity = activity;
		this.listNavItem = navItemList;
	}
	
	@Override
	public int getCount() {
		return listNavItem.size();
	}

	@Override
	public Object getItem(int position) {
		return listNavItem.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null){
			convertView = activity.getLayoutInflater()
					.inflate(R.layout.item_nav_drawer, parent, false);
		}
		
		ImageView iconView = (ImageView) convertView.findViewById(R.id.img_nav_icon);
		TextView titleView = (TextView) convertView.findViewById(R.id.txt_nav_title);
		NavDrawerItem item = listNavItem.get(position);
		
		iconView.setImageResource(item.getIcon());
		titleView.setText(item.getTitle());
		return convertView;
	}

}
