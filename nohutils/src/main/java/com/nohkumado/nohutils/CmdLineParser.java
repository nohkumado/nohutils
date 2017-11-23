/*
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

//import com.nohkumado.utils.*;
import java.util.*;

import android.util.*;

@SuppressWarnings({"SameParameterValue", "WeakerAccess", "CanBeFinal"})
public class CmdLineParser  implements Cloneable,CommandParserI
{
	protected TreeMap<String,CommandI> commands = new TreeMap<>();
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
	 * @param cmds list of commands
	 * @return map of commands
	 */
	public HashMap<String,CommandI> feedCmds(HashMap<String,CommandI> cmds)
	{
		for (String key: cmds.keySet())
		{
			//Log.d(TAG,"about to add command "+key);
			CommandI aCmd = cmds.get(key);
			//Log.d(TAG,"command is "+aCmd);
			//aCmd.name(key);
			//Log.d(TAG, "about to add comd '" + key + "' v: " + aCmd + " to " + commands);
			if (key == null)
			{
				Log.e(TAG, "key was null for cmd " + aCmd + " to " + commands);
			}
			else commands.put(key, aCmd);
		}// for(String key: commands.keySet())
		return(null);
	}//public void init
	/**

	 parse

	 this Method is the one that catches the commands and interprets them
	 TODO still not functional


	 */
	public ArrayList<CommandI> parse(String line)
	{return parse(line, true);}

	public ArrayList<CommandI> parse(String line, boolean strictParse)
	{
		ArrayList<CommandI> resultStack = new ArrayList<>();
		String lastprompt = shell.prompt();
		if (lastprompt != null) line = line.replace(lastprompt, "").trim();
		//Log.d(TAG,"parsing line : "+line);
		//maybe this isnt needed as long as no command is found the help is called anyway TODO
		if (line.matches("^help|^h$|^\\?"))
		{ 
			//Log.d(TAG, " = help " + line);
			help(); 
			if (strictParse) return(resultStack);
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

		if (Objects.equals(mode, "tokenized"))
		{
			TokenParser parser = new TokenParser(this);
			if (strictParse && !parser.parse(line, resultStack))
			{
				if (parser.errorCode() == TokenParser.UNPARSED_ARGS)
				{
					StringBuilder sb = new StringBuilder();
					sb.append(shell.msg(com.nohkumado.nohutils.R.string.syntax_error));
					sb.append(" ");
					sb.append(shell.msg(com.nohkumado.nohutils.R.string.cmd_command));
					sb.append(" ");
					sb.append(parser.errorCmd());
					sb.append(" ");
					sb.append(shell.msg(com.nohkumado.nohutils.R.string.cmd_unparsed_args));
					sb.append(" ");
					sb.append(parser.errorMsg());

					shell.print(sb.toString());
				}
			}
		}//if(mode == null || mode == "tokenized")
		else if (Objects.equals(mode, "char"))
		{
			CharParser parser = new CharParser(this);
			if (strictParse && !parser.parse(line, resultStack))
			{
				if (parser.errorCode() == CharParser.UNPARSED_ARGS)
				{
					StringBuilder sb = new StringBuilder();
					sb.append(shell.msg(com.nohkumado.nohutils.R.string.syntax_error));
					sb.append(" ");
					sb.append(shell.msg(com.nohkumado.nohutils.R.string.cmd_command));
					sb.append(" ");
					sb.append(parser.errorCmd());
					sb.append(" ");
					sb.append(shell.msg(com.nohkumado.nohutils.R.string.cmd_unparsed_args));
					sb.append(" ");
					sb.append(parser.errorMsg());

					shell.print(sb.toString());
				}
			}
		}//if(mode == null || mode == "tokenized")

		else if (Objects.equals(mode, "vilike"))
		{
			Log.d(TAG, "please provide an implementation for this parsing mode");
		}//else if(mode == "vilike")
		else
		{
			Log.e(TAG, "unsupported parsing mode");
			help(); return(resultStack);
		}//else

		if (strictParse && resultStack.size() <= 0)  shell.print(shell.msg(com.nohkumado.nohutils.R.string.syntax_error));

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
	}// public void  setRessource (Properties arg0)
	/** -------------------------- getHelp --------------------------
	 * compile the Help from the commands
	 */
	public String help()
	{
		StringBuilder helpBld = new StringBuilder();
		helpBld.append("help\n");
		for (String cmdName : commands.keySet())
		{
			helpBld.append(cmdName).append(" : ");
			CommandI aCmd = commands.get(cmdName);
			String cmdHelp = aCmd.help();
			helpBld.append(cmdHelp);
			if (!(cmdHelp != null && cmdHelp.endsWith("\n"))) helpBld.append("\n");
		}//for(Iterator<String> i = commands.keySet().iterator(); i.hasNext();)
		shell.print(helpBld.toString());
		return(helpBld.toString());
	}//public String help()
	/*
	 find a command, means try key completion if not found directly clone the command and return it
	 */
	public CommandI findCmd(String token)
	{
		//shell.debug("find command " + token);

		if (commands.containsKey(token)) 
		{
			//System.out.println("got it:"+commands.get(token));
			return commands.get(token).clone();
		}//if(commands.containsKey(token)) 
		String key = "";
		ArrayList<String> matchingKeys = new ArrayList<>();
		for (String actKey : commands.keySet())
		{
			if (actKey.startsWith(token))
			{
				key = actKey;
				matchingKeys.add(actKey);
				//Log.d(TAG, "key matches " + key);
			}//if(actKey.matches("^"+token))
			//else Log.d(TAG, "no key matches " + actKey);
		}//for(Iterator<String> e = commands.keySet().iterator(); e.hasNext();)
		if (matchingKeys.size() == 1) return commands.get(key).clone();
		if (matchingKeys.size() > 1)
		{
			CommandI aCmd = new Command(shell); // dummy command to avoid the help output
			shell.print(shell.msg(com.nohkumado.nohutils.R.string.need_specifying) + " " + matchingKeys.toString());
			return(aCmd);
		}//if(matchingKeys.size() > 1)
		return(null);
	}//protected CommandI findCmd(String token)
	@Override
	public void clearCmds()
	{
		commands.clear();
	}
	@Override
	public void parseMode(String mode)
	{
		shell.set("parsing", mode);
	}
}//public class CmdLineParser
