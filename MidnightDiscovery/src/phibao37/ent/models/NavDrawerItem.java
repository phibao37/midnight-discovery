package phibao37.ent.models;

public class NavDrawerItem {
	private int icon;
	private int title;
	
	public NavDrawerItem(int titleResId, int iconResId){
		title = titleResId;
		icon = iconResId;
	}
	
	public int getIcon(){
		return icon;
	}
	
	public int getTitle(){
		return title;
	}
}
