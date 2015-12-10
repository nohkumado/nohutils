/** Id: CdCommand.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME CdCommand 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * changes a settings of the shell
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  CdCommand.java is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; version 2 of the License.
 *
 *  This program is distributed in the hope that it will be importful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE.  See the GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.nohkumado.nohutils.commands;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import com.nohkumado.nohutils.*;
import android.util.*;

public class CdCommand extends Command implements Cloneable, CommandI
{
	public static final String TAG="CdCmd";

  protected String path = "";
  /**
	 CTOR

	 Build up a reference

   */
  public CdCommand(ShellI s)
  {
    super(s);
		name = shell.msg(R.string.cd);
		//Log.d("CD","cd cmd: "+shell.msg(R.string.cd));
	}// public Command()

  public CdCommand(ShellI s, String n)
  {
    super(s, n);
  }// public CdCommand()
  /**

	 execute

	 activate this command

   * @param line 
   * @param heap 
   * @return 
   */
  public String execute()
  {
		Log.d(TAG,"cd exe ");
    String result = "";
    if (path != null && path.length() > 0)
    {
			Log.d(TAG,"path valid: "+path);
      if (!path.startsWith("/"))
      {
				Log.d(TAG,"no absolute path ");
				String pwd = (String)shell.get("pwd");
				Log.d(TAG,"shell tells us pwd is:  "+pwd);
				if (pwd == null) pwd = System.getProperty("user.dir");
				else if (pwd.length() <= 0) pwd = System.getProperty("user.dir");
				Log.d(TAG,"consistency?  "+pwd);
				
				if (!(pwd.equals("/") && pwd.endsWith("/"))) pwd += System.getProperty("file.separator");
				Log.d(TAG,"add missing /?:  "+pwd);
				path = pwd +path;
				Log.d(TAG,"no absolute path "+pwd);
      }// else
			Log.d(TAG, "about to cd into " + path);
      File newDir = new File(path);
      if (newDir.exists())
      {
				if (newDir.canRead()) result += shell.msg(R.string.cd_not_enough_rights);
				else if (newDir.isDirectory()) shell.set("pwd", newDir.getAbsolutePath());
				else result += shell.msg(R.string.cd_not_a_dir);
      }// if(newDir.exists())
      else result += shell.msg(R.string.cd_does_not_exist);
    }// if(line != null && line.length() > 0)
    else result += shell.msg(R.string.cd_provide_a_dir_to_enter);

    return(result);
  }//end execute

	/** 
	 * parse a setting line 
	 * with no parameter its prints the whole list
	 * with one parameter it prints the value of that parameter
	 * with 2 parameters it replaces the parameter
	 * 
	 * @param line 
	 * @return 
	 */
	public String parse(String line)
	{
		Log.d(TAG, "parsing " + line);
		if(line.length()<= 0) path = System.getProperty("user.dir");
		else if (line.matches(".."))
		{
			String pwd = (String)shell.get("pwd");
			Log.d(TAG,"got pwd from shell: "+pwd);
			String[] result = pwd.split(System.getProperty("file.separator"));
			if (result.length > 1)
			{
				pwd = "";
				for (int x=0; x < result.length - 1; x++) 
					pwd += result[x] + System.getProperty("file.separator");
			}// if(result.lenght > 1)
			path = pwd;
		}// if(line.matches(".."))
		else path = line;
		Log.d(TAG, "about to move into " + path);
		return("");
	}// public String parse(String line)
  /**

	 help

	 issue the help message associated with this command

   */
  public String help()
  {
    return(shell.msg(R.string.cd_help) + "\n");
  }//end help
	//make a copy of this object
	public Object clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
		CdCommand cloned = (CdCommand)super.clone();
		//  CdCommand cloned = new CdCommand(shell);
		//cloned.type = type;
		//cloned.name = name;
		//cloned.group = group;
		//cloned.messageHandler = messageHandler;
		//cloned.shell = shell;
    return cloned;
	}//public Object clone()
}//public class Command
