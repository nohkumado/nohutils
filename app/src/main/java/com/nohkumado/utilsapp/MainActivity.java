package com.nohkumado.utilsapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;

import com.nohkumado.nohutils.CommandI;
import com.nohkumado.nohutils.Msg2RString;
import com.nohkumado.nohutils.ShellFragment;
import com.nohkumado.nohutils.commands.CdCommand;
import com.nohkumado.nohutils.commands.LsCommand;
import com.nohkumado.nohutils.commands.PwdCommand;
import com.nohkumado.nohutils.commands.QuitCommand;
import com.nohkumado.nohutils.commands.SetCommand;
import com.nohkumado.nohutils.commands.VersionCommand;
import com.nohkumado.utilsapp.commands.TestCmd;

import java.util.HashMap;

public class MainActivity extends Msg2RString
{
  private final static String TAG = "MA";

  @SuppressWarnings("UnusedAssignment")
  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);


    if (savedInstanceState == null)
      Log.d(TAG, "********************* start ************************");
    else Log.d(TAG, "********************* restart ************************");

    FragmentManager fm = getFragmentManager();
    ShellFragment shellfrag = (ShellFragment) fm.findFragmentByTag("shellFrag");
    if (shellfrag == null)
    {
      //Log.d(TAG, "no shell frag");
      shellfrag = new ShellFragment();
      shellfrag.callback(this);
      //Log.d(TAG, "shellfrag created parent linsting childs");
      //LinearLayout minSc = (LinearLayout) findViewById(R.id.mainscreen);

      //if(minSc != null)
      //	for(int index=0; index<minSc.getChildCount(); ++index) {
      //	View nextChild = minSc.getChildAt(index);
      //	Log.d(TAG,"main child["+index+"] "+nextChild);
      //}
      //Log.d(TAG,"done");

      fm.beginTransaction().add(R.id.mainscreen, shellfrag, "shellFrag").commit();
      //Log.d(TAG, "shellfrag added");
    }//if (shellfrag == null)
    shell = shellfrag.shell();
    //Log.d(TAG, "got shell back : " + shell);

    //if (shell == null) shell = new Shell(this);
    giveShellCmds();

    //LoggerFrag screen = (LoggerFrag) getFragmentManager().findFragmentById(R.id.textOut);
    //EditText cmdLine = findViewById(R.id.TextIn);
    //LoggerFrag logger = (LoggerFrag)getFragmentManager().findFragmentById(com.nohkumado.utilsapp.R.id.textOut);
    //if (logger == null)
    //{
    //	logger = (LoggerFrag)getFragmentManager().findFragmentByTag("screen");
    //	if (logger == null) logger = new LoggerFrag(); //well everything failed....
    //	getFragmentManager().beginTransaction().replace(R.id.textOut, logger, "screen").commit();
    //}
    //shell.setInOut(cmdLine, logger);
    //shell.init();
    shell.print("Welcome ");
  }//protected void onCreate(Bundle savedInstanceState)

  private void giveShellCmds()
  {
    if (shell.getContext() == null) shell.setContext(this);
    CommandI[] cmds = new CommandI[]
        {
            new SetCommand(shell).name(shell.msg(R.string.set)).help(shell.msg(R.string.sethelp)),
            new QuitCommand(shell).name(shell.msg(R.string.quit)).help(shell.msg(R.string.quit_help)),
            new PwdCommand(shell).name(shell.msg(R.string.pwd)).help(shell.msg(R.string.pwd_help)),
            new LsCommand(shell).name(shell.msg(R.string.ls)).help(shell.msg(R.string.ls_help)),
            new CdCommand(shell).name(shell.msg(R.string.cd)).help(shell.msg(R.string.cd_help)),
            new TestCmd(shell).name(shell.msg(R.string.test)).help(shell.msg(R.string.testhelp)),
            new VersionCommand(shell).name(shell.msg(R.string.version)).help(shell.msg(R.string.version_help))
        };
    HashMap<String, CommandI> availableCmds = new HashMap<>();
    for (CommandI cmd : cmds) availableCmds.put(cmd.name(), cmd);

    shell.feedCmds(availableCmds);
  }//protected void onCreate(Bundle savedInstanceState)

}//public class MainActivity extends Msg2RString
