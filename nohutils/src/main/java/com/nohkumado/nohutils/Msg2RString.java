package com.nohkumado.nohutils;

import android.annotation.*;
import android.app.*;
import android.content.*;
import android.content.pm.*;
import android.content.res.*;
import android.database.sqlite.*;
import android.media.*;
import android.os.*;
import android.util.*;
import java.util.*;

@SuppressWarnings("WeakerAccess")
@SuppressLint("Registered")
public class Msg2RString extends Activity implements MsgR2StringI
//,SharedPreferences.OnSharedPreferenceChangeListener see sample below
{
	public static final String TAG = "msg2Str";
	private static final int RESULT_SETTINGS = 1;
	private static final int PERM_REQUEST_READ = 2;
	
	protected ShellI shell;

	@Override
	public SQLiteOpenHelper getDbHelper()
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public String callback(String name, HashMap<String, Object> args)
	{
		// TODO: Implement this method
		return null;
	}

	@Override
	public String msg(int stringid)
	{
		String result = "MSGNOTFOUND";
		if (stringid == 0) return result;
		try
		{
			//int resourceId = context.getResources().getIdentifier(m, "strings", context.getPackageName());
			result = getResources().getString(stringid);
		}
		catch (Resources.NotFoundException e)
		{ Log.e(TAG, "not found message : " + stringid);}
		//Log.d(TAG,"msg returns "+result);
		return(result);
	}//  public String msg(int stringid)

	@Override
	public String msg(String stringid)
	{
		int resourceId = getResources().getIdentifier(stringid, "strings", getPackageName());
		return msg(resourceId);
	}

	@Override
	public String[] msgArr(int resourceId)
	{
		try
		{
			//int resourceId = context.getResources().getIdentifier(m, "strings", context.getPackageName());
			return(getResources().getStringArray(resourceId));
		}
		catch (Resources.NotFoundException e)
		{ Log.e(TAG, "not found message : " + resourceId);}
		// TODO: Implement this method
		return null;
	}

	/**
	 */
	public void playSound()
	{
		final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
		tg.startTone(ToneGenerator.TONE_PROP_BEEP);
		// Double beeps:     tg.startTone(ToneGenerator.TONE_PROP_ACK);
		// Double beeps:     tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
		// Sounds all wrong: tg.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE); 
		// Double beeps:     tg.startTone(ToneGenerator.TONE_DTMF_0);
		// Double beeps:     tg.startTone(ToneGenerator.TONE_DTMF_S);
	}//public void playSound()

	@Override
	public void display(String path, String type)
	{
		Log.e(TAG, "don't know how to display " + type + " for " + path);
	}

	/**
	 * sample onCreate
	 @Override
	 protected void onCreate(Bundle savedInstanceState)
	 {
	 super.onCreate(savedInstanceState);
	 Log.d(TAG, "################## start #######################");
	 setContentView(R.layout.main);
	 SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
	 sp.registerOnSharedPreferenceChangeListener(this);

	 LoggerFrag screen = (LoggerFrag) findViewById(R.id.textOut);
	 screen.setTypeface(Typeface.MONOSPACE); 
	 EditText cmdLine = (EditText) findViewById(R.id.TextIn);
	 shell.setInOut(cmdLine, screen);
	 }// protected void onCreate(Bundle savedInstanceState)
	 */
	/*
	 * sample optionsmenu

	 @Override
	 public boolean onCreateOptionsMenu(Menu menu)
	 {
	 getMenuInflater().inflate(R.menu.settings, menu);
	 return true;
	 }

	 @Override
	 public boolean onOptionsItemSelected(MenuItem item)
	 {
	 switch (item.getItemId())
	 {

	 case R.id.menu_settings:
	 Log.d(TAG, "case is settings, creating intent");
	 //Intent intent = new Intent();
	 //intent.setClassName(this, "com.nohkumado.nohfibu.SettingsActivity");
	 Intent intent = new Intent(this, SettingsActivity.class);
	 startActivity(intent);
	 //startActivityForResult(i, RESULT_SETTINGS);
	 Log.d(TAG, "fired intent");
	 break;
	 case R.id.mabout:
	 FragmentManager fm = getFragmentManager();
	 AboutDialogFragment about = new AboutDialogFragment();
	 about.show(fm, getResources().getString(R.string.mabout));
	 break;
	 }

	 return true;
	 }
	 @Override
	 public void onSharedPreferenceChanged(SharedPreferences preference, String key)
	 {
	 Log.d(TAG, "notified that a pref changed : " + " k = " + key + " new val = " + preference.getString(key, key));
	 if (key.equals("dataName"))
	 {
	 String actSet = shell.preference("dataName");
	 .. do something with the new data ....
	 }
	 }

	 */

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode)
		{
			case RESULT_SETTINGS:
				break;

		}

	}
	@Override
	public void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.
		if (shell != null) shell.saveState(savedInstanceState);
	}//public void onSaveInstanceState(Bundle savedInstanceState)

	@Override
	public void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		if (shell != null) shell.restoreState(savedInstanceState);
	}// public void onRestoreInstanceState(Bundle savedInstanceState)
	@Override
	public String askPermission(String whichPerm)
	{
		if (checkSelfPermission(whichPerm) != PackageManager.PERMISSION_GRANTED)
		{
			// Should we show an explanation?
			if (shouldShowRequestPermissionRationale(whichPerm)) 
			{
				String[] splitted = whichPerm.split(".");
				if(splitted.length >1)
				{
					Log.d(TAG,"split gave us a hit : "+splitted[splitted.length-1]);
					shell.print(shell.msg(splitted[splitted.length-1]));
				}
				else shell.print(shell.msg(whichPerm));
				// Show an explanation to the user *asynchronously* -- don't block
				// this thread waiting for the user's response! After the user
				// sees the explanation, try again to request the permission.
			} 
			else 
			{
				// No explanation needed, we can request the permission.
				requestPermissions(new String[]{whichPerm},PERM_REQUEST_READ);

				// MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
				// app-defined int constant. The callback method gets the
				// result of the request.
			}//else 
		}//if(checkSelfPermission(whichPerm) != PackageManager.PERMISSION_GRANTED)

		return "";
	}//public String askPermission(String whichPerm)
	
}//public class Msg2RString extends Activity implements MsgR2StringI,SharedPreferences.OnSharedPreferenceChangeListener

