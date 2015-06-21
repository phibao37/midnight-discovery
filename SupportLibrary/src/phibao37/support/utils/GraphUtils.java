package phibao37.support.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Utility for graph calculation and parsing
 */
public class GraphUtils {
	/** Convert value from DP to Pixel */
	public static float dpToPixel(float dp, Context context){
	    DisplayMetrics metrics = context.getResources().getDisplayMetrics();
	    return dp * (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
	}
}
