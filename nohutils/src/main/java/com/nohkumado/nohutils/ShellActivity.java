package com.nohkumado.nohutils;

import android.annotation.*;
import android.app.*;
import android.content.pm.*;
import android.content.res.*;
import android.database.sqlite.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.nohkumado.nohutils.commands.*;
import com.nohkumado.nohutils.view.*;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "EmptyMethod"})
@SuppressLint("Registered")
public class ShellActivity extends Activity implements MsgR2StringI
{
  @Override
  public SQLiteOpenHelper getDbHelper()
  {
    // TODO: Implement this method
    return null;
  }

	transient protected ShellI shell = null;
	protected int textOut, textIn;
	public static final String TAG="SA";
	protected static final int PERM_REQUEST_READ = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//Log.d(TAG, "################## start #######################");
		//setContentView(R.layout.main);
		//SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
		//sp.registerOnSharedPreferenceChangeListener(this);

		if (shell == null) 	createMinimalShell();

		if (shell != null) shell.set("msger", this);
		//String pwd = (String)shell.get("pwd");
		//if (pwd == null || pwd.equals(""))
		//	shell.set("pwd", getExternalFilesDir(null).getAbsolutePath());

		if (textOut != 0)
		{
			//TextView screen = (TextView) findViewById(textOut);
			LoggerFrag screen = (LoggerFrag) getFragmentManager().findFragmentById(textOut);
      if(screen == null)
      {
        screen = new LoggerFrag();
        getFragmentManager().beginTransaction().replace(textOut,screen).commit();
      }

			screen.setTypeface(Typeface.MONOSPACE);
			EditText cmdLine = (EditText)findViewById(textIn);
			if (shell != null) shell.setInOut(cmdLine, screen);
			//Log.d(TAG,"s:"+screen+" i:"+cmdLine);	
		}

	}

	protected void createMinimalShell()
	{
		shell = new Shell(this);
		CommandI[] cmds = new CommandI[]
		{
			new SetCommand(shell),
			new QuitCommand(shell),
			new PwdCommand(shell),
			new LsCommand(shell),
			new CdCommand(shell)
		};
		HashMap<String,CommandI> availableCmds = new HashMap<>();
		for (CommandI cmd : cmds) availableCmds.put(cmd.name(), cmd);

		shell.feedCmds(availableCmds);
	}

	@Override
	public String msg(int stringid)
	{
		try
		{
			//int resourceId = context.getResources().getIdentifier(m, "strings", context.getPackageName());
			return(getResources().getString(stringid));
		}
		catch (Resources.NotFoundException e)
		{ Log.e(TAG, "not found message : " + stringid);}
		return("MSGNOTFOUND");
	}

	@Override
	public String msg(String stringid)
	{
		int resourceId = getResources().getIdentifier(stringid, "strings", getPackageName());
		return msg(resourceId);
	}
	@Override
	public String[] msgArr(int resourceId)
	{
		return getResources().getStringArray(resourceId);
	}
	
	/**
	 */
	public void playSound()
	{
		final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
		tg.startTone(ToneGenerator.TONE_PROP_BEEP);
	}

	@Override
	protected void onSaveInstanceState(Bundle savedInstanceState)
	{
		super.onSaveInstanceState(savedInstanceState);
		// Save UI state changes to the savedInstanceState.
		// This bundle will be passed to onCreate if the process is
		// killed and restarted.

		if (shell != null)  shell.saveState(savedInstanceState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState)
	{
		super.onRestoreInstanceState(savedInstanceState);
		// Restore UI state from the savedInstanceState.
		// This bundle has also been passed to onCreate.
		if (shell != null) shell.restoreState(savedInstanceState);
	}

	@Override
	protected void onPause()
	{
		// TODO: Implement this method
		super.onPause();
	}

	@Override
	protected void onResume()
	{
		// TODO: Implement this method
		super.onResume();
	}

	@Override
	public void display(String path, String type)
	{
		// TODO: Implement this method
	}

	public void textOut(int textOut)
	{
		this.textOut = textOut;
	}

	public int textOut()
	{
		return textOut;
	}

	public void textIn(int textIn)
	{
		this.textIn = textIn;
	}

	public int textIn()
	{
		return textIn;
	}
	public void shell(ShellI shell)
	{
		this.shell = shell;
	}

	public ShellI shell()
	{
		return shell;
	}

	@Override
	public String callback(String name, HashMap<String, Object> args)
	{
		// TODO: Implement this method
		return null;
	}
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
	
}
