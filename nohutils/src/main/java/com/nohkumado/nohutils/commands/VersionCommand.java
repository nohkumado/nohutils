package com.nohkumado.nohutils.commands;

import com.nohkumado.nohutils.BuildConfig;
import com.nohkumado.nohutils.Command;
import com.nohkumado.nohutils.R;
import com.nohkumado.nohutils.ShellI;

/**
 * Created by bboett on 12.01.18.
 * gives back the version
 */

public class VersionCommand extends Command
{
  public VersionCommand(ShellI s)
  {
    super(s);
    if (s != null) name = s.msg(com.nohkumado.nohutils.R.string.version);
  }

  public VersionCommand(ShellI s, String n)
  {
    super(s, n);
  }

  @Override
  public String execute()
  {
    int versionCode = BuildConfig.VERSION_CODE;
    String versionName = BuildConfig.VERSION_NAME;
    return shell.msg(R.string.version)+" "+versionCode+"("+ versionName+")";
  }
}//public class VersionCommand extends Command
