/*
 * NAME PwdCommand 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * shows the actual working directory
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  PwdCommand.java is free software; you can redistribute it and/or modify it under
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
package eu.nohkumado.nohutils.commands;
import android.app.*;
import android.os.*;

import eu.nohkumado.nohutils.Command;
import eu.nohkumado.nohutils.CommandI;
import eu.nohkumado.nohutils.ShellI;

/**
* pwd command
* returns the actual working path
* on creation it sets up 3 variables
* "app.home" the pubic directory where the app stores its files
* "user.dir" the public diretory where all the user data resides
* "app.dir" the private diretory of the app  
*/
public class PwdCommand extends Command implements Cloneable, CommandI
{
	/**
	 CTOR

	 Build up a reference

	 */
	public PwdCommand(ShellI s)
	{
		super(s);
		if (s != null) name = s.msg(com.nohkumado.nohutils.R.string.pwd);
		setEnvPaths();
	}// public Command()

	public PwdCommand(ShellI s, String n)
	{
		super(s, n);
		setEnvPaths();
	}// public PwdCommand()
	/**

	 execute

	 activate this command

	 * @return result
	 */
	public String execute()
	{
		//String result = "";
		String pwd = (String)shell.get("pwd");
		if (pwd == null) 
		{
			pwd = setEnvPaths();
			shell.set("pwd", pwd);
		}//if (pwd == null)
		//pwd = System.getProperty("user.dir")+"sdcard";
		//else if(pwd.length() <= 0 ) pwd = System.getProperty("user.dir"+"sdcard");
		
		//shell.print(pwd);
		return(pwd);
	}//end execute
	public String setEnvPaths()
	{
		String pwd ;
		//starting up....
		shell.set("app.home", ((Activity)(shell.getContext())).getExternalFilesDir("").getAbsolutePath());
		shell.set("user.dir", Environment.getExternalStoragePublicDirectory("").getAbsolutePath());
		shell.set("app.dir", ((Activity)(shell.getContext())).getFilesDir().getAbsolutePath());
		if(isExternalStorageReadable()) pwd = shell.get("app.home").toString();
		else pwd = shell.get("app.dir").toString();
		return pwd;
	}
	/**

	 help

	 issue the help message associated with this command

	 */
	public String help()
	{
		return(shell.msg(com.nohkumado.nohutils.R.string.pwd_help) + "\n");
	}//end help
	/** 
	 * copy this object 
	 * 
	 * @return clone
	 */
	public PwdCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
		return (PwdCommand)super.clone();
	}//public Object clone()
	/* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state);
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
		String state = Environment.getExternalStorageState();
		return Environment.MEDIA_MOUNTED.equals(state) ||
				Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
	}
}//public class Command
