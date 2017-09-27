package com.nohkumado.utilsapp;

import android.app.*;
import android.os.*;
import android.util.*;
import android.widget.*;
import com.nohkumado.nohutils.*;
import java.util.*;
import com.nohkumado.nohutils.commands.*;
import com.nohkumado.utilsapp.commands.*;
import com.nohkumado.nohutils.view.*;

public class MainActivity extends Msg2RString
{
	private final static String TAG ="MA";

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "********************* start ************************");
		if (shell == null) shell = new Shell(this);
		giveShellCmds();
	
		setContentView(R.layout.main);
		LoggerFrag screen = (LoggerFrag) getFragmentManager().findFragmentById(R.id.textOut);
		EditText cmdLine = (EditText) findViewById(R.id.TextIn);
    LoggerFrag logger = (LoggerFrag)getFragmentManager().findFragmentById(com.nohkumado.utilsapp.R.id.textOut);
    if(logger == null)
    {
      logger = (LoggerFrag)getFragmentManager().findFragmentByTag("screen");
      if(logger == null) logger = new LoggerFrag(); //well everything failed....
      getFragmentManager().beginTransaction().replace(R.id.textOut,logger,"screen").commit();
    }
		shell.setInOut(cmdLine, logger);
		shell.init();
    shell.print("Welcome ");
	}//protected void onCreate(Bundle savedInstanceState)

  private void giveShellCmds()
  {
    CommandI[] cmds = new CommandI[]
		{
			new SetCommand(shell),
			new QuitCommand(shell),
			new PwdCommand(shell),
			new LsCommand(shell),
			new CdCommand(shell),
      new TestCmd(shell)
		};
	  HashMap<String,CommandI> availableCmds = new HashMap<String,CommandI>();
		for (int i = 0; i < cmds.length; i++) availableCmds.put(cmds[i].name(), cmds[i]);

		shell.feedCmds(availableCmds);
  }//protected void onCreate(Bundle savedInstanceState)
}//public class MainActivity extends Msg2RString
