package eu.nohkumado.nohutils.foreign;

/*
base taken from from pskink on
 http://stackoverflow.com/questions/22318903/how-to-enable-overwrite-mode-in-edittext
 usage   editText.addTextChangedListener(watcher);
 */

import android.text.*;

@SuppressWarnings("WeakerAccess")
public class ShellTextWatcher implements TextWatcher
{
	boolean formatting;
	int mStart;
	int mEnd;
	boolean overwrite = false;

	private final static String TAG="STW";

	public ShellTextWatcher()
	{}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) 
	{
		this.mStart = -1;
		if (overwrite && before == 0)
		{
			mStart = start + count;
			mEnd = Math.min(mStart + count, s.length());
			formatting = false;
		}
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after)
	{
	}

	@Override
	public void afterTextChanged(Editable s)
	{
		if (!formatting)
		{
			if (mStart >= 0)
			{
				formatting = true;
				//Log.d(TAG, "afterTextChanged s " + mStart);
				//Log.d(TAG, "afterTextChanged e " + mEnd);
				s.replace(mStart, mEnd, "");
				formatting = false;
			}
		}
		//else Log.d(TAG,"no formatting needed");
	}
	public void setOverwrite(boolean overwrite)
	{
		this.overwrite = overwrite;
	}

	public boolean isOverwrite()
	{
		return overwrite;
	}
}

