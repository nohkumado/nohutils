/*
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
import java.util.regex.*;
//import com.nohkumado.utils.*;

@SuppressWarnings("WeakerAccess")
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
		if (s != null) name = s.msg(com.nohkumado.nohutils.R.string.set);
  }// public Command()

  public SetCommand(ShellI s, String n)
  {
    super(s, n);
  }// public SetCommand()
  /**

	 execute

	 activate this command


   * @return execute
   */
  public String execute()
  {
    StringBuilder result = new StringBuilder();
    if (!Objects.equals(var_name, ""))
    {
      result.append(var_name).append(" : ");
      if (!Objects.equals(var_value, "")) shell.set(var_name, var_value);
      if (shell.preference(var_name) != null)
        result.append(shell.preference(var_name)).append("\n");
      else result.append(shell.get(var_name)).append("\n");
    }// if(value != "")
    else
    {
      result.append(shell.msg(com.nohkumado.nohutils.R.string.variable_list)).append(":\nEnvironment:\n");
			//TODO suspect here.... check it out...
      HashMap<String,Object> environment = (HashMap<String, Object>) shell.getAll();

			result.append(result);
      for (String argName : environment.keySet())
			{

				//TODO put this into a hiddenVars array....
				if (!(argName.equals("shell") || argName.equals("msger") || argName.equals("Fibu")))
					result.append(argName).append(" : ").append(environment.get(argName)).append("\n");
					//result += argName + " : " + environment.get(argName) + "\n";
			}// for(Iterator<String> e = environment.keySet().iterator(); e.hasNext();)
      result.append(shell.msg(com.nohkumado.nohutils.R.string.endlist)).append("\n");
    }// else
    return(result.toString());
  }//end execute

	/** 
	 * parse a setting line 
	 * with no parameter its prints the whole list
	 * with one parameter it prints the value of that parameter
	 * with 2 parameters it replaces the parameter
	 * 
	 * @param line line
	 * @return unparse
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
  @Override
	public String help()
  {
		//return(shell.msg("set") + " " + var_name + " " + shell.msg("value") + " " + shell.msg("to_set_a_value") + "\n");
    return(shell.msg(com.nohkumado.nohutils.R.string.sethelp));
  }//end help
	//make a copy of this object
	public SetCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
    //  SetCommand cloned = new SetCommand(shell);
		//cloned.type = type;
		//cloned.name = name;
		//cloned.group = group;
		//cloned.messageHandler = messageHandler;
		//cloned.shell = shell;
    return (SetCommand)super.clone();
	}//public Object clone()
}//public class Command
