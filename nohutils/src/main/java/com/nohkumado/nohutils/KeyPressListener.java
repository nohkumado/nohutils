package com.nohkumado.nohutils;

import android.view.*;

public interface KeyPressListener
{
	/**------------------------------------------------------------------
	 * onKey
	 * @param tw, the view the event happened
	 * @param actionId, 
	 * @param event
	 */

	public boolean onKey(View v, int keyCode, KeyEvent event); 
	
}
