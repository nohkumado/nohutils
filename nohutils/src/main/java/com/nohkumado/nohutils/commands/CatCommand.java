package com.nohkumado.nohutils.commands;
/** Id: CatCommand.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME CatCommand 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * echo a file onto the shell
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2015 Bruno Boettcher
 *
 *  CatCommand.java is free software; you can redistribute it and/or modify it under
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
import android.util.*;
import com.nohkumado.nohutils.*;
import java.io.*;

public class CatCommand extends Command implements Cloneable, CommandI
{
	public static final String TAG="CatCmd";

	protected String path = "";
	/**
	 CTOR

	 Build up a reference

	 */
	public CatCommand(ShellI s)
	{
		super(s);
		name = shell.msg(com.nohkumado.nohutils.R.string.cd);
		//Log.d("CD","cd cmd: "+shell.msg(R.string.cd));
	}// public Command()

	public CatCommand(ShellI s, String n)
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
		Log.d(TAG, "cat exe ");
		String result = "";
		if (path != null && path.length() > 0)
		{
			Log.d(TAG, "about to cat " + path);
			File fileToEcho = new File(path);
			if (fileToEcho.exists() && fileToEcho.isFile())
			{
					shell.set("pwd", fileToEcho.getAbsolutePath());
			}// if(newDir.exists())
			else result += shell.msg(com.nohkumado.nohutils.R.string.cat_not_file);
		}// if(line != null && line.length() > 0)
		else result += shell.msg(com.nohkumado.nohutils.R.string.cd_provide_a_dir_to_enter);

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
		if (line.length() <= 0) path = (String)shell.get("pwd");
		else path = line;
		Log.d(TAG, "about to cat " + path);
		return("");
	}// public String parse(String line)
	/**

	 help

	 issue the help message associated with this command

	 */
	public String help()
	{
		return(shell.msg(R.string.cat_help) + "\n");
	}//end help
	//make a copy of this object
	public CatCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
		CatCommand cloned = (CatCommand)super.clone();
		//  CdCommand cloned = new CdCommand(shell);
		//cloned.type = type;
		//cloned.name = name;
		//cloned.group = group;
		//cloned.messageHandler = messageHandler;
		//cloned.shell = shell;
		return cloned;
	}//public Object clone()
}//public class Command
