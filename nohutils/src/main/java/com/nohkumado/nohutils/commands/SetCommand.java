/** Id: SetCommand.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME SetCommand 
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
 *  SetCommand.java is free software; you can redistribute it and/or modify it under
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
import com.nohkumado.nohutils.*;
import java.util.*;
import java.util.prefs.*;
import java.util.regex.*;
//import com.gnu.utils.*;

public class SetCommand extends Command implements Cloneable, CommandI
{
  protected String var_name = "", var_value = "";
  /**
	 CTOR

	 Build up a reference

   */
  public SetCommand(ShellI s)
  {
    super(s);
		if(s != null) name = s.msg(R.string.set);
  }// public Command()

  public SetCommand(ShellI s, String n)
  {
    super(s, n);
  }// public SetCommand()
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
    if (var_name != "")
    {
      result += var_name + " : ";
      if (var_value != "") shell.set(var_name, var_value);
      if (shell.preference(var_name) != null)
				result += shell.preference(var_name) + "\n";
      else result += shell.get(var_name) + "\n";
    }// if(value != "")
    else
    {
      result = shell.msg(R.string.variable_list) + ":\nEnvironment:\n";
			//TODO suspect here.... check it out...
      HashMap<String,Object> environment = (HashMap<String, Object>) shell.getAll();
      for (Iterator<String> e = environment.keySet().iterator(); e.hasNext();)
      {
				String argName = e.next();
				result += argName + " : " + environment.get(argName) + "\n";
      }// for(Iterator<String> e = environment.keySet().iterator(); e.hasNext();)
      result += shell.msg(R.string.variables) + "\n";
      Map<String,Object> locenvironment = (Map<String,Object>)shell.getAll();
			for (Map.Entry<String, Object> entry : locenvironment.entrySet())
      {
				result += entry.getKey() + " : " + entry.getValue() + "\n";
      }// for(Iterator<String> e = environment.keySet().iterator(); e.hasNext();)
      result += shell.msg(R.string.endlist) + "\n";
    }// else
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
		//TODO need to be able to set differently env vars and local vars and need to be able to destroy env vars or local vars
		Pattern pattern = Pattern.compile("^(\\S+)\\s+(.*)$");
		Matcher matcher = pattern.matcher(line);
		if (matcher.find())
		{
			var_name = matcher.group(1);
			var_value = matcher.group(2);
		}//if(matcher.find())
		//System.out.print("parsed: ("+name+","+value+")\n");
		return("");
	}// public String parse(String line)«»
	/** 
	 * instead of parsing the options, give them directly, eg when invoking a command from the program code directly
	 * 
	 * @param parms the hashtable with the options
	 * @param parms 
	 */
	public void setParameters(HashMap<String,Object> parms)
	{
		if (parms.containsKey("name")) var_name = (String)parms.get("name");
		if (parms.containsKey("value")) var_value = (String)parms.get("value");
	}// public void setParameters(HashMap<String,Object> parms)«»
  /**

	 help

	 issue the help message associated with this command

   */
  public String help()
  {
		//return(shell.msg("set") + " " + var_name + " " + shell.msg("value") + " " + shell.msg("to_set_a_value") + "\n");
    return(shell.msg(R.string.sethelp));
  }//end help
	//make a copy of this object
	public SetCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
		SetCommand cloned = (SetCommand)super.clone();
		//  SetCommand cloned = new SetCommand(shell);
		//cloned.type = type;
		//cloned.name = name;
		//cloned.group = group;
		//cloned.messageHandler = messageHandler;
		//cloned.shell = shell;
    return cloned;
	}//public Object clone()
}//public class Command
