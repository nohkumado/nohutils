package com.nohkumado.utilsapp;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.nohkumado.nohutils.*;
import java.util.*;
import com.nohkumado.nohutils.commands.*;

public class MainActivity extends Msg2RString
{
	protected Shell shell = null;
	private final static String TAG ="MA";


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "********************* start ************************");
		if (shell == null) shell = new Shell(this);
		CommandI[] cmds = new CommandI[]
		{
			new SetCommand(shell),
			new QuitCommand(shell),
			new PwdCommand(shell),
			new LsCommand(shell),
			new CdCommand(shell)
		};
	  HashMap<String,CommandI> availableCmds = new HashMap<String,CommandI>();
		for(int i = 0; i < cmds.length; i++) availableCmds.put(cmds[i].name(),cmds[i]);
		
		shell.feedCmds(availableCmds);
	
		
		setContentView(R.layout.main);
		TextView screen = (TextView) findViewById(R.id.textOut);
		EditText cmdLine = (EditText) findViewById(R.id.TextIn);
		shell.setInOut(cmdLine, screen);
		shell.init();
	}
}
