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

public class CdCommand extends Command implements Cloneable, CommandI
{
  protected String name = "", path = "";
  /**
	 CTOR

	 Build up a reference

   */
  public CdCommand(ShellI s)
  {
    super(s);
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
    String result = "";
    if (path != null && path.length() > 0)
    {
      if (!path.startsWith("/"))
      {
				String pwd = (String)shell.ressource("pwd");
				if (pwd == null) pwd = System.getProperty("user.dir");
				else if (pwd.length() <= 0) pwd = System.getProperty("user.dir");
				shell.set("pwd", pwd);
				path = pwd + System.getProperty("file.separator") + path;
      }// else

      File newDir = new File(path);
      if (newDir.exists())
      {
				if (newDir.isDirectory()) shell.set("pwd", newDir.getAbsolutePath());
				else result += shell.msg("not_a_dir");
      }// if(newDir.exists())
      else result += shell.msg("does_not_exist");
    }// if(line != null && line.length() > 0)
    else result += shell.msg("provide_a_dir_to_enter");

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
		if (line.matches(".."))
		{
			String pwd = (String)shell.ressource("pwd");
			String[] result = pwd.split(System.getProperty("file.separator"));
			if (result.length > 1)
			{
				pwd = "";
				for (int x=0; x < result.length - 1; x++) pwd += result[x] + System.getProperty("file.separator");
			}// if(result.lenght > 1)
			path = pwd;
		}// if(line.matches(".."))
		else path = line;
		return("");
	}// public String parse(String line)
  /**

	 help

	 issue the help message associated with this command

   */
  public String help()
  {
    return(shell.msg("set") + " " + name + " " + shell.msg("value") + " " + shell.msg("to_set_a_value") + "\n");
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
