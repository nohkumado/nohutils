/** Id: LsCommand.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME LsCommand 
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
 *  LsCommand.java is free software; you can redistribute it and/or modify it under
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
import java.util.*;
import java.io.*;
import java.util.regex.*;
import com.nohkumado.nohutils.*;

public class LsCommand extends Command implements Cloneable, CommandI
{
  protected String path = "";
  protected FilenameFilter filter;
  /**
	 CTOR

	 Build up a reference

   */
  public LsCommand(ShellI s)
  {
    super(s);
		if (s != null) name = s.msg(R.string.ls);
  }// public Command()

  public LsCommand(ShellI s, String n)
  {
    super(s, n);
  }// public LsCommand()
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
    //TODO fetch PwdCommand from shell here would be more elegant
    String pwd = (String) shell.ressource("pwd");
    if (pwd == null) pwd = System.getProperty("user.dir");
    else if (pwd.length() <= 0) pwd = System.getProperty("user.dir");

    if (!path.equals(""))
    {
      if (!path.startsWith("/")) path = pwd + System.getProperty("file.separator") + path;
    }// if(value != "")
    else path = pwd;

    File theDir = new File(path);
    if (theDir.exists() && theDir.isDirectory())
    {
      String[] choices = theDir.list(filter);
      for (String name : choices) 
      {
				result +=  name + "\n";
      }//for(String name : choices) 
      return(result);
    }//if(theDir.exists() && theDir.isDirectory())

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
    path = "";
    if (line != null && line.length() > 0)
    {
      String[] result = line.split(System.getProperty("file.separator"));
      if (result.length > 1)
      {
				int x=0; 
				if (result.length > 1) path = ""; //reset the path if there is a path component!
				for (; x < result.length - 1; x++) path += result[x] + System.getProperty("file.separator");
				line = result[x];
      }// if(result.lenght > 1)«»

      Pattern regexp = Pattern.compile("^(.*?)\\*(.*)");
      Matcher matcher = regexp.matcher(line);
      if (matcher.find())
      {
				line = matcher.group(1) + ".*" + matcher.group(2);
				try
				{
					filter = new PatternFileFilter(line);
				}// try
				catch (PatternSyntaxException e)
				{
					shell.print(shell.msg(R.string.lscmd_invalid_pattern) + "\n");

				}// catch(PatternSyntaxException e)«»
      }//if(matcher.find())
      else path += line;
    }// if(line != null && line.length() > 0)

    return("");
  }// public String parse(String line)
  /**

	 help

	 issue the help message associated with this command

   */
  public String help()
  {
    return(shell.msg(R.string.ls_help)	+ "\n");
  }//end help
  //make a copy of this object
  public Object clone()
  {
    LsCommand cloned = (LsCommand)super.clone();
    return cloned;
  }//public Object clone()
}//public class Command
