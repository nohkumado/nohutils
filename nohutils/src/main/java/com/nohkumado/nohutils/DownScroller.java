package com.nohkumado.nohutils;
import android.widget.*;

@SuppressWarnings("WeakerAccess")
public class DownScroller implements Runnable
{
	ScrollView scroll_view;
	
	public DownScroller(ScrollView sc)
	{
		scroll_view = sc;
	}
	
	@Override
	public void run() 
	{
		// This method works but animates the scrolling 
		// which looks weird on first load
		// scroll_view.fullScroll(View.FOCUS_DOWN);

		// This method works even better because there are no animations.
		scroll_view.scrollTo(0, scroll_view.getBottom());
	}
}
