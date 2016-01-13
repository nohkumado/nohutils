package com.nohkumado.nohutils;

import android.util.*;
import java.util.*;
import java.util.regex.*;

public class CharParser
{
	public static final String TAG="CharP";
	public static final int UNPARSED_ARGS = 1;

	protected CmdLineParser parentParser;
	protected ArrayList<Pattern> patterns = new ArrayList<Pattern>();

	//Pattern pat_cmd_arg = Pattern.compile("^(\\S+)\\s+(.*)$");

	protected String errorMsg = "";

	protected int errorCode = 0;
	protected String errorCmd = "";

	private static final int UNPARSABLE = 2;

	/** CTOR
	 builds a list of patterns corresponding to the strings that match this command
	 */
	public CharParser(CmdLineParser p)
	{
		parentParser = p;
		for (CommandI aCmd : parentParser.commands.values())
		{
			patterns.add(aCmd.pattern());
		}
	}

	public boolean parse(String line, ArrayList<CommandI> resultStack)
	{
		boolean  result = true;
		errorMsg = errorCmd = "";
		errorCode = 0;

		Matcher matcher;
		int lineLength = 1;
		do
		{
			lineLength = line.length();
			int cmd_count = resultStack.size();
			for (Pattern cmdPat : patterns)
			{
				//we need to give the line over to cmd.parse, so making the mathcer her...
				//seems irrelevant... especially since we can't be sure to get the right pattern for all commands...
				if ((matcher = cmdPat.matcher(line)).find())
				{
					line = line.trim();
					//dont forget to call the parse method of the command  
					//need to split it up 
					//TODO BTW here we add the parsing ehm... 
					//since the Commands hold the shell, they dont need to get the heap 
					//explicitely, no?
					CommandI aCmd = parentParser.findCmd(line);
					if (aCmd != null) 
					{
						line = aCmd.parse(line);
						resultStack.add(aCmd);
					}
					else result &= false;
					continue;
				}//if(line.matches("^(\\S+)\\s*$"))

			}//for(Pattern cmPat : patterns)
			if (!result || cmd_count == resultStack.size())
			{
				errorCode =  UNPARSABLE;
				errorMsg = line;
				return result;
			}
		}while(line != null && line.length() > 0 && (line.length() - lineLength) != 0);

		return result && !(line != null && line.length() > 0);
	}

	public String errorCmd()
	{
		return errorCmd;
	}
	public String errorMsg()
	{
		return errorMsg;
	}
	public int errorCode()
	{
		return errorCode;
	}
}
