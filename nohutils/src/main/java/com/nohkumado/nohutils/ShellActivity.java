package com.nohkumado.nohutils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.FragmentManager;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.util.Log;

import com.nohkumado.nohutils.commands.CdCommand;
import com.nohkumado.nohutils.commands.LsCommand;
import com.nohkumado.nohutils.commands.PwdCommand;
import com.nohkumado.nohutils.commands.QuitCommand;
import com.nohkumado.nohutils.commands.SetCommand;
import com.nohkumado.nohutils.commands.VersionCommand;

import java.util.HashMap;

@SuppressWarnings({"WeakerAccess", "EmptyMethod"})
@SuppressLint("Registered")
public class ShellActivity extends Activity implements MsgR2StringI
{
  transient protected ShellI shell = null;
  protected int mainWindow, shellWindow;
  public static final String TAG = "SA";
  protected static final int PERM_REQUEST_READ = 2;


  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    if (savedInstanceState == null)
      Log.d(TAG, "********************* start ************************");
    else Log.d(TAG, "********************* restart ************************");

    setContentView(mainWindow);
    //SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
    //sp.registerOnSharedPreferenceChangeListener(this);

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
      if (findViewById(shellWindow)!= null)
        fm.beginTransaction().add(shellWindow, shellfrag, "shellFrag").commit();
      else
        fm.beginTransaction().add(shellfrag, "shellFrag").commit();
      //Log.d(TAG, "shellfrag added");
    }//if (shellfrag == null)
    shell = shellfrag.shell();

    giveShellCommands();

    if (shell != null) shell.set("msger", this);
  }//protected void onCreate(Bundle savedInstanceState)

  protected void giveShellCommands()
  {
    CommandI[] cmds = new CommandI[]
        {
            new SetCommand(shell).name(shell.msg(R.string.set)).help(shell.msg(R.string.sethelp)),
            new QuitCommand(shell).name(shell.msg(R.string.quit)).help(shell.msg(R.string.quit_help)),
            new PwdCommand(shell).name(shell.msg(R.string.pwd)).help(shell.msg(R.string.pwd_help)),
            new LsCommand(shell).name(shell.msg(R.string.ls)).help(shell.msg(R.string.ls_help)),
            new CdCommand(shell).name(shell.msg(R.string.cd)).help(shell.msg(R.string.cd_help)),
            new VersionCommand(shell).name(shell.msg(R.string.version)).help(shell.msg(com.nohkumado.nohutils.R.string.version_help))
        };
    HashMap<String, CommandI> availableCmds = new HashMap<>();
    for (CommandI cmd : cmds) availableCmds.put(cmd.name(), cmd);

    shell.feedCmds(availableCmds);
  }//protected void createMinimalShell()


  @Override
  public String msg(int stringid)
  {
    try
    {
      return (getResources().getString(stringid));
    }
    catch (Resources.NotFoundException e)
    {
      Log.e(TAG, "not found message : " + stringid);
    }
    return ("MSGNOTFOUND");
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

    if (shell != null) shell.saveState(savedInstanceState);
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
    super.onPause();
  }

  @Override
  protected void onResume()
  {
    super.onResume();
  }

  @Override
  public void display(String path, String type)
  {
    Log.e(TAG, "don't know how to display " + type + " for " + path);
  }

  /*public void textOut(int textOut)
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
   */
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
  public SQLiteOpenHelper getDbHelper()
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
        if (splitted.length > 1)
        {
          //Log.d(TAG, "split gave us a hit : " + splitted[splitted.length - 1]);
          shell.print(shell.msg(splitted[splitted.length - 1]));
        } else shell.print(shell.msg(whichPerm));
        // Show an explanation to the user *asynchronously* -- don't block
        // this thread waiting for the user's response! After the user
        // sees the explanation, try again to request the permission.
      } else
      {
        // No explanation needed, we can request the permission.
        requestPermissions(new String[]{whichPerm}, PERM_REQUEST_READ);

        // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
        // app-defined int constant. The callback method gets the
        // result of the request.
      }//else
    }//if(checkSelfPermission(whichPerm) != PackageManager.PERMISSION_GRANTED)

    return "";
  }//public String askPermission(String whichPerm)

  public int getVersionCode()
  {
    int version = 0;
    try
    {
      version = getPackageManager().getPackageInfo(getPackageName(), 0).versionCode;
    }
    catch (PackageManager.NameNotFoundException ignored)
    {
    }

    return version;
  }//public int getVersionCode()

  public String getVersionName()
  {
    String version = "0";
    try
    {
      version = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
    }
    catch (PackageManager.NameNotFoundException ignored)
    {
    }

    return version;
  }//public String getVersionName()


}//public class ShellActivity extends Activity implements MsgR2StringI
