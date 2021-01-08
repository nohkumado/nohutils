package eu.nohkumado.utilsapp;

import android.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import eu.nohkumado.nohutils.CommandI;
import eu.nohkumado.nohutils.Msg2RString;
import eu.nohkumado.nohutils.ShellFragment;
import eu.nohkumado.nohutils.commands.CdCommand;
import eu.nohkumado.nohutils.commands.LsCommand;
import eu.nohkumado.nohutils.commands.PwdCommand;
import eu.nohkumado.nohutils.commands.QuitCommand;
import eu.nohkumado.nohutils.commands.SetCommand;
import eu.nohkumado.nohutils.commands.VersionCommand;
import eu.nohkumado.nohutils.view.AboutDialogFragment;
import eu.nohkumado.nohutils.view.HelpDialogFragment;

import com.nohkumado.utilsapp.R;

import eu.nohkumado.utilsapp.commands.TestCmd;

import java.util.HashMap;

public class MainActivity extends Msg2RString
{
  private final static String TAG = "MA";

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
      //Log.d(TAG, "shellfrag created parent listing children");
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
  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
      if(item.getItemId() == R.id.menu_help) {
        HelpDialogFragment hFrag = new HelpDialogFragment();
        hFrag.show(getFragmentManager(), "MANUAL");
        //HelpDialog dia = new HelpDialog(this, getResources().getString(R.string.help_content));
        //HelpDialog dia = new HelpDialog((Context) this);
        //dia.show();
        return true;
      }
      else  if(item.getItemId() == R.id.menu_about) {

        //AboutDialogFragment dia = new AboutDialogFragment(this, getResources().getString(R.string.help_content));
        AboutDialogFragment dia = new AboutDialogFragment();
        dia.setInfo(dia.readRawTextFile(R.raw.info));
        dia.setLegal(dia.readRawTextFile(R.raw.legal));
        dia.show(getFragmentManager(),"ABOUT");
        return true;
      }
      else {
        return super.onOptionsItemSelected(item);
    }
  }
}//public class MainActivity extends Msg2RString
