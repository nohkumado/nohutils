package eu.nohkumado.nohutils;

import android.view.*;

@SuppressWarnings("WeakerAccess")
public interface KeyPressListener
{
	/**------------------------------------------------------------------
	 * onKey
	 * @param v, the view the event happened
	 * @param keyCode, code
	 * @param event event
	 */

    boolean onKey(View v, int keyCode, KeyEvent event);
	
}//public interface KeyPressListener
