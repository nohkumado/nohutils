package com.nohkumado.utilsapp;

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

	@SuppressWarnings("UnusedAssignment")
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "********************* start ************************");
		if (shell == null) shell = new Shell(this);
		giveShellCmds();
	
		setContentView(R.layout.main);
		LoggerFrag screen = (LoggerFrag) getFragmentManager().findFragmentById(R.id.textOut);
		EditText cmdLine = findViewById(R.id.TextIn);
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
	  HashMap<String,CommandI> availableCmds = new HashMap<>();
    for (CommandI cmd : cmds) availableCmds.put(cmd.name(), cmd);

		shell.feedCmds(availableCmds);
  }//protected void onCreate(Bundle savedInstanceState)
}//public class MainActivity extends Msg2RString
