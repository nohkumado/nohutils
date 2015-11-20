/** Id: CmdLineParser.java,v 1+4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME CmdLineParser 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp+org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * takes a line of text and tries to split it by hash value
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  CmdLineParser.java is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; version 2
 *  of the License+
 *
 *  This program is distributed in the hope that it will be importful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE+  See the
 *  GNU General Public License for more details+

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc+, 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA+
 */

package com.nohkumado.nohutils;

//import com.gnu.utils.*;
import java.util.*;
import java.util.regex.*;
import android.util.*;

public class CmdLineParser  implements Cloneable,CommandParserI
{
	protected TreeMap<String,CommandI> commands = new TreeMap<String,CommandI>();
	protected ShellI shell = null;
	public static final String TAG="CmdP";


	public CmdLineParser()
	{
		super(); 
	}// public CmdLineParser(ConfigHandlerInterface config, MessageHandlerInterface table)«»

	/**
	 *
	 *  feedCmds
	 *
	 * @param cmds 
	 * @return 
	 */
	public HashMap<String,CommandI> feedCmds(HashMap<String,CommandI> cmds)
	{
		for (String key: cmds.keySet())
		{
			if (cmds.get(key) instanceof CommandI)
			{
	      CommandI aCmd = cmds.get(key);
	      //aCmd.name(key);
				//Log.d(TAG,"about to add comd "+key+" v: "+aCmd+" to "+commands);
				commands.put(key, aCmd);
			}// if(cmds.get(key) instanceof Command)

		}// for(String key: commands.keySet())
		return(null);
	}//public void init
	/**

	 parse

	 this Method is the one that catches the commands and interprets them
	 TODO still not functional


	 */
	public ArrayList<CommandI> parse(String line)
	{
		ArrayList<CommandI> resultStack = new ArrayList<CommandI>();
		String lastprompt = (String)shell.get("prompt");
		if(lastprompt != null) line = line.replace(lastprompt,"").trim();
			//Log.d(TAG,"parsing line : "+line);
			//maybe this isnt needed as long as no command is found the help is called anyway TODO
			if (line.matches("^help|^h$|^\\?"))
			{ 
			  //System.out.println(" = help " + line);
				help(); 
				return(resultStack);
			}
			//Log.d(TAG,"continuing parse");
			/*TODO let see, we need to break up the line to extract a key or a subpart of the key of a command, containsKey(Object key) does the job for a complete key, but not for part of it, then we need to know what the separator is, easiet cas is space, but for the jrl editor we will have single char commands, vi style with eventual modifiers... usual modifiers are numerical, can there be textual modifiers? but then we can uppose/impose apostrophing them!
			 */
			//char to char test if in Strng ' or >"
			//then test if ;
			//then split in cmd args
			//search cmd and clone cmds and feed args
			//put into vector
			String mode;
			if (shell.get("parsing") != null) mode = shell.get("parsing").toString();
			else mode = "tokenized";
			if (mode.equals("parsing"))
			{
				shell.set("parsing", "tokenized");
				mode = "tokenized";
			}
			//Log.d(TAG,"mode "+mode);

			if (mode == "tokenized")
			{
				TokenParser parser = new TokenParser(this);
				if(!parser.parse(line, resultStack))
				{
					if(parser.errorCode() == parser.UNPARSED_ARGS)
					{
						StringBuilder sb = new StringBuilder();
						sb.append(shell.msg(R.string.syntax_error));
						sb.append(" ");
						sb.append(shell.msg(R.string.cmd_command));
						sb.append(" ");
						sb.append(parser.errorCmd());
						sb.append(" ");
						sb.append(shell.msg(R.string.cmd_unparsed_args));
						sb.append(" ");
						sb.append(parser.errorMsg());
						
						shell.print(sb.toString());
					}
				}
			}//if(mode == null || mode == "tokenized")
			else if (mode == "vilike")
			{
				Log.d(TAG,"please provide an implementation for this parsing mode");
			}//else if(mode == "vilike")
			else
			{
				Log.e(TAG,"unsupported parsing mode");
				help(); return(resultStack);
			}//else
	
		if (resultStack.size() <= 0)  shell.print(shell.msg(R.string.syntax_error));

		return(resultStack);
	}//public Vector<CommandI> parse(String line)
	/** 
	 return the acutal shell
	 */
	public ShellI shell()
	{
		return(shell);
	}// public SchellI getShell()«»
  public void shell(ShellI  s)
	{ shell = s; }
  /** -------------------------- formatProperties --------------------------
   *
   TODO only proto here
   */
  public String  formatProperties(Properties arg0)
  {
    return(arg0.toString());
  }// public String  formatProperties (Properties arg0)«»
  /** -------------------------- setRessource --------------------------
   *
   TODO only proto here
   */
  public void  setRessource(Properties arg0)
  {
    ;
  }// public void  setRessource (Properties arg0)
  /** -------------------------- getHelp --------------------------
   * compile the Help from the commands
   */
  public String help()
  {
    String help = "";
    for (Iterator<String> i = commands.keySet().iterator(); i.hasNext();)
    {
      String cmdName = i.next();
      CommandI aCmd = commands.get(cmdName);
      help += cmdName + " : " + aCmd.help();
    }//for(Iterator<String> i = commands.keySet().iterator(); i.hasNext();)
    shell.print(help);
    return(help);
  }//public String help()
  /*
	 find a command, means try key completion if not found directly clone the command and return it
	 */
	public CommandI findCmd(String token)
	{
		//System.out.println("find command "+token);

		if (commands.containsKey(token)) 
		{
			//System.out.println("got it:"+commands.get(token));
			return((CommandI)commands.get(token).clone());
		}//if(commands.containsKey(token)) 
		String key = "";
		ArrayList<String> matchingKeys = new ArrayList();
		for (Iterator<String> e = commands.keySet().iterator(); e.hasNext();)
		{
			String actKey = e.next();
			if (actKey.startsWith(token))
			{
				key = actKey;
				matchingKeys.add(actKey);
			}//if(actKey.matches("^"+token))
		}//for(Iterator<String> e = commands.keySet().iterator(); e.hasNext();)
		if (matchingKeys.size() == 1) return((CommandI)commands.get(key).clone());
		if (matchingKeys.size() > 1)
		{
			CommandI aCmd = new Command(shell); // dummy command to avoid the help output
			shell.print(shell.msg("need specifying") + " " + matchingKeys.toString());
			return(aCmd);
		}//if(matchingKeys.size() > 1)
		return(null);
	}//protected CommandI findCmd(String token)
}//public class CmdLineParser
