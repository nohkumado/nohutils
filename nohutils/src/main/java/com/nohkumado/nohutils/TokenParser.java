package com.nohkumado.nohutils;
import android.util.*;
import java.util.*;
import java.util.regex.*;

public class TokenParser
{
	public static final String TAG="TokP";
	public static final int UNPARSED_ARGS = 1;

	protected CmdLineParser parentParser;
	Pattern pat_cmd_arg = Pattern.compile("^(\\S+)\\s+(.*)$");
	Pattern pat_semic1 = Pattern.compile("^(\\S+)\\s*;\\s*(.*)$");
	Pattern pat_semic = Pattern.compile("^(\\S+)\\s*(.*);\\s*(.*)$");

	protected String errorMsg = "";

	protected int errorCode = 0;
	protected String errorCmd = "";
	

	public TokenParser(CmdLineParser p)
	{parentParser = p;}

	public boolean parse(String line, ArrayList<CommandI> resultStack)
	{
		boolean  result = true;
		errorMsg = "";
		errorCode = 0;

		Matcher matcher;
		int lineLength = 1;
		do
		{
			Log.d(TAG, "parsing " + line);
			lineLength = line.length();
			if (line.matches("^(\\S+)\\s*$"))
			{
				Log.d(TAG, "found simple cmd ");

				line = line.trim();
				//dont forget to call the parse method of the command  
				//need to split it up 
				//TODO BTW here we add the parsing ehm... 
				//since the Commands hold the shell, they dont need to get the heap 
				//explicitely, no?
				CommandI aCmd = parentParser.findCmd(line);
				if (aCmd != null) 
				{
					aCmd.parse("");
					resultStack.add(aCmd);
				}
				else result = false;
			}//if(line.matches("^(\\S+)\\s*$"))
			else if ((matcher = pat_semic1.matcher(line)).find() || (matcher = pat_semic.matcher(line)).find())
			{
				Log.d(TAG, "found semicolon separated ");

				String cmd = matcher.group(1);
				String args;
				if (matcher.groupCount() == 2)
				{
					args = "";
					line = matcher.group(2);
				}
				else
				{
					args = matcher.group(2);
					line = matcher.group(3);
				}
				Log.d(TAG, "splitted into  " + cmd + ", " + args + ", " + line);


				CommandI aCmd = parentParser.findCmd(cmd);
				if (aCmd != null) 
				{
					String rest =  aCmd.parse(args);
					resultStack.add(aCmd);
					if (rest != null && rest.length() > 0)
					{
						result = false;
						errorCode =  UNPARSED_ARGS;
						errorMsg = rest;
						errorCmd = cmd;
						return result;
					}
				}//if(aCmd != null)
				else result = false;
			}//else if((matcher = pat_semic.matcher(line)).find())
			else if ((matcher = pat_cmd_arg.matcher(line)).find())
			{
				Log.d(TAG, "found cmd with args");

				String cmd = matcher.group(1);
				String args = matcher.group(2);
				CommandI aCmd = parentParser.findCmd(cmd);
				if (aCmd != null) 
				{
					line =  aCmd.parse(args);
					resultStack.add(aCmd);
				}//if(aCmd != null) 
				else result = false;
			}//else
			else Log.e(TAG, "failure of tokenized parsing of " + line);

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
