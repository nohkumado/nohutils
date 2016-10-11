package com.nohkumado.nohutils;

import android.app.*;
import android.content.res.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.nohkumado.nohutils.commands.*;
import java.util.*;

public class ShellActivity extends Activity implements MsgR2StringI
{
	transient protected ShellI shell = null;
	protected int textOut, textIn;
	public static final String TAG="SA";


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
			TextView screen = (TextView) findViewById(textOut);
			screen.setTypeface(Typeface.MONOSPACE); 
			EditText cmdLine = (EditText) findViewById(textIn);
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
		HashMap<String,CommandI> availableCmds = new HashMap<String,CommandI>();
		for (int i = 0; i < cmds.length; i++) availableCmds.put(cmds[i].name(), cmds[i]);

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
	
}
