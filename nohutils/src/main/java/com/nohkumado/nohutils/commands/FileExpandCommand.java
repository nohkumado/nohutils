package com.nohkumado.nohutils.commands;
/*
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
import java.io.*;
import java.util.*;

import com.nohkumado.nohutils.*;
import android.util.*;

@SuppressWarnings("WeakerAccess")
public class FileExpandCommand extends Command implements Cloneable, CommandI
{
	public static final String TAG="FexCmd";

	protected String path = "";
	/**
	 CTOR

	 Build up a reference

	 */
	public FileExpandCommand(ShellI s)
	{
		super(s);
	}// public Command()

	public FileExpandCommand(ShellI s, String n)
	{
		super(s, n);
	}// public CdCommand()
	//make a copy of this object
	public FileExpandCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
    //  CdCommand cloned = new CdCommand(shell);
		//cloned.type = type;
		//cloned.name = name;
		//cloned.group = group;
		//cloned.messageHandler = messageHandler;
		//cloned.shell = shell;
		return (FileExpandCommand)super.clone();
	}//public Object clone()

	public String expand()
	{
		String actPath = path;
		Log.d(TAG,"expanding '"+path+"'");
		File actTry = new File(path);
		if (actTry.exists())
		{

		}
		if (!path.startsWith("/")) actPath = shell.get("pwd") + "/" + actPath;

		if (actPath.startsWith("/"))
		{
			//its an absolute path.....
			int lastSlash = actPath.lastIndexOf("/");
			//to keep the slash on the path side
			lastSlash++;
			Log.d(TAG, "casse " + actPath + " at " + lastSlash + " for total " + actPath.length());
			String rest = actPath.substring(lastSlash);
			actPath = actPath.substring(0, lastSlash);

			//String[] splitted = actPath.split("/");
			//Log.d(TAG, "splitted the path: " + Arrays.toString(splitted));
			//String rest = splitted[splitted.length - 1];
			//StringBuilder temppath = new StringBuilder();
			//temppath.append("/");
			//for (int i = 0; i < splitted.length - 2; i++)	temppath.append(splitted[i] + "/");
			//actPath = temppath.toString();

			Log.d(TAG, "separated into " + actPath + " and " + rest);

			actTry = new File(actPath);
			if (actTry.exists() && actTry.isDirectory())
			{

				String[] content = actTry.list();
				Log.d(TAG, "is dir! contents " + Arrays.toString(content));
				ArrayList<String> choices = new ArrayList<>();

				for (String oneName : content)
					if (oneName.startsWith(rest)) choices.add(oneName);
				Log.d(TAG, "extracted choices " + choices);

				if (choices.size() <= 0) shell.beep();
				else if (choices.size() == 1)
				{
					String toReturn = choices.get(0).substring(rest.length());
					actTry = new File(actPath + "/" + choices.get(0));
					if (actTry.exists() && actTry.isDirectory()) toReturn += "/";
					return toReturn;

				}
				else
				{
					for (String fN: choices) shell.print(fN + "\n");
					shell.beep();
					Log.d(TAG, "several choices searching least common denominator ");
					String start = choices.remove(0);
					ArrayList<String> subMenge = new ArrayList<>();

					for (String oneName: choices)
						subMenge.add(greatestCommonPrefix(start, oneName));
					int minLength = start.length();
					for (String oneName: subMenge) if (oneName.length() < minLength) minLength = oneName.length();
					return start.substring(0, minLength).substring(rest.length());
				}
			}
		}
		return("");
	}
	protected String greatestCommonPrefix(String a, String b) 
	{
		int minLength = Math.min(a.length(), b.length());
		for (int i = 0; i < minLength; i++) 
		{
			if (a.charAt(i) != b.charAt(i))
			{
				return a.substring(0, i);
			}
		}
		return a.substring(0, minLength);
	}
}//public class Command
