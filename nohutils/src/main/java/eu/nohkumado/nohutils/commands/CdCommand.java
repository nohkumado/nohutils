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
package eu.nohkumado.nohutils.commands;
import android.util.*;

import com.nohkumado.nohutils.R;

import java.io.*;

import eu.nohkumado.nohutils.CommandI;
import eu.nohkumado.nohutils.ShellI;

@SuppressWarnings("WeakerAccess")
public class CdCommand extends FileExpandCommand implements Cloneable, CommandI
{
	public static final String TAG="CdCmd";
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

	 * @return message or result usually displayed by shell
	 */
	@Override
	public String execute()
	{
		//Log.d(TAG, "cd exe ");
		String result = "";
		if (path != null && path.length() > 0)
		{
			//Log.d(TAG, "path valid: " + path+" actpath = "+shell.get("pwd"));
			if (!path.startsWith("/"))
			{
				//Log.d(TAG, "no absolute path ");
				String pwd = (String)shell.get("pwd");
				//Log.d(TAG, "shell tells us pwd is:  " + pwd);
				if (pwd == null) pwd = (String)shell.get("user.dir");
				else if (pwd.length() <= 0) pwd = (String)shell.get("user.dir");
				//Log.d(TAG, "consistency?  " + pwd);

				if (!(pwd.equals("/") && pwd.endsWith("/"))) pwd += System.getProperty("file.separator");
				//Log.d(TAG, "add missing /?:  " + pwd);
				path = pwd + path;
				//Log.d(TAG, "no absolute path " + pwd);
			}// else
			//Log.d(TAG, "about to cd into " + path);
			File newDir = new File(path);
			shell.print(name+" : "+newDir.getAbsolutePath());
			if (newDir.exists())
			{
				if(!newDir.canRead() && path.startsWith((String)shell.get("user.dir")))
					{
					//Log.d(TAG,"need to ask permission for this dir!!");
						shell.getContext().askPermission("android.permission.READ_EXTERNAL_STORAGE");
				}
				
				//if (newDir.canRead()) result += shell.msg(R.string.cd_not_enough_rights);
				//else 
				if (newDir.isDirectory()) shell.set("pwd", newDir.getAbsolutePath());
				else result += shell.msg(R.string.cd_not_a_dir);
			}// if(newDir.exists())
			else result += shell.msg(R.string.cd_does_not_exist);
		}// if(line != null && line.length() > 0)
		else result += shell.msg(R.string.cd_provide_a_dir_to_enter);
		//Log.d(TAG, "act pwd = "+shell.get("pwd"));
		return(result);
	}//end execute

	/** 
	 * parse a setting line 
	 * with no parameter its prints the whole list
	 * with one parameter it prints the value of that parameter
	 * with 2 parameters it replaces the parameter
	 * 
	 * @param line arguments
	 * @return parse what is parsable and return the rest
	 */
	@Override
	public String parse(String line)
	{
		if (shell.get("app.home") == null)
		{
			PwdCommand pwdC = new PwdCommand(shell);
			pwdC.execute();
		}
		//Log.d(TAG, "parsing '" + line+"'");
		if (line.length() <= 0) //resetting path
		{
			//available "app.home", "user.dir" or "app.dir"
			if (shell.get("home") != null) path = shell.get("home").toString();
			else
			{
				
				path = shell.get("app.home").toString();
			}
				
			return("");
		}
		//	if (line.length() <= 0)
		File newpos;
		if (line.startsWith("~/"))
		{
			//Log.d(TAG,"relative to home");
			line = line.replace("~",shell.get("user.dir").toString());
			newpos = new File(line);
		}
		else if (line.startsWith("/"))
		{
			//Log.d(TAG,"absolute");
			newpos = new File(line);
		}
		else
		{
			//Log.d(TAG,"relative to pwd");
			newpos = new File((String)shell.get("pwd"), line);
		}

		try
		{
			path = newpos.getCanonicalPath();
		}
		catch (IOException e)
		{
			Log.e(TAG, "no such path :" + newpos.getAbsolutePath());
			shell.print("no such path :" + newpos.getAbsolutePath());
		}

		/*if (line.matches(".."))
		 {
		 String pwd = (String)shell.get("pwd");
		 Log.d(TAG, "got pwd from shell: " + pwd);
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
		 */
		//Log.d(TAG, "about to move into " + path);
		//Log.d(TAG, "locally " + this.path + " from parent '" + super.path + "'");
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
	public CdCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
		//  CdCommand cloned = new CdCommand(shell);
		//cloned.type = type;
		//cloned.name = name;
		//cloned.group = group;
		//cloned.messageHandler = messageHandler;
		//cloned.shell = shell;
		return (CdCommand)super.clone();
	}//public Object clone()

	/*
	 public String expand(String arg)
	 {
	 String actPath = path;
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
	 Log.d(TAG,"casse "+actPath+" at "+lastSlash+" for total "+actPath.length());
	 String rest = actPath.substring(lastSlash);
	 actPath = actPath.substring(0,lastSlash);

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
	 Log.d(TAG, "is dir! contents "+Arrays.toString(content));
	 ArrayList<String> choices = new ArrayList<String>();

	 for (String oneName : content)
	 if (oneName.startsWith(rest)) choices.add(oneName);
	 Log.d(TAG, "extracted choices "+choices);

	 if (choices.size() <= 0) shell.beep();
	 else if (choices.size() == 1)
	 {
	 String toReturn = choices.get(0).substring(rest.length());
	 actTry = new File(actPath+"/"+choices.get(0));
	 if (actTry.exists() && actTry.isDirectory()) toReturn +="/";
	 return toReturn;

	 }
	 else
	 {
	 for(String fN: choices) shell.print(fN+"\n");
	 shell.beep();
	 Log.d(TAG, "several choices searching least common denominator ");
	 String start = choices.remove(0);
	 ArrayList<String> subMenge = new ArrayList<String>();

	 for(String oneName: choices)
	 subMenge.add(greatestCommonPrefix(start,oneName));
	 int minLength = start.length();
	 for(String oneName: subMenge) if(oneName.length() < minLength) minLength = oneName.length();
	 return start.substring(0,minLength).substring(rest.length());
	 }
	 }
	 }
	 return("");
	 }
	 public String greatestCommonPrefix(String a, String b) {
	 int minLength = Math.min(a.length(), b.length());
	 for (int i = 0; i < minLength; i++) {
	 if (a.charAt(i) != b.charAt(i)) {
	 return a.substring(0, i);
	 }
	 }
	 return a.substring(0, minLength);
	 }
	 */
}//public class Command
