package com.nohkumado.nohutils;

import java.util.*;
import java.util.regex.*;

@SuppressWarnings({"WeakerAccess", "CanBeFinal", "ConstantConditions"})
public class CharParser
{
	public static final String TAG="CharP";
	public static final int UNPARSED_ARGS = 1;

	protected CmdLineParser parentParser;
	protected ArrayList<Pattern> patterns = new ArrayList<>();

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
      //Log.d(TAG,"added "+aCmd+" pattern "+aCmd.pattern());
		}
	}

	public boolean parse(String line, ArrayList<CommandI> resultStack)
	{
		boolean  result = true;
		errorMsg = errorCmd = "";
		errorCode = 0;
    line = line.trim();
    
    //Log.d(TAG,"char parsing  '"+line+"'");
    
		Matcher matcher;
		int lineLength;
		do
		{
			lineLength = line.length();
			int cmd_count = resultStack.size();
			for (Pattern cmdPat : patterns)
			{
				//we need to give the line over to cmd.parse, so making the matcher her...
				//seems irrelevant... especially since we can't be sure to get the right pattern for all commands...
				if ((matcher = cmdPat.matcher(line)).find())
				{
          /*StringBuilder sb = new StringBuilder();
          sb.append(cmdPat).append(" match '").append(line).append("'").append("\n");
          sb.append("groups:");
          for(int i = 0; i <= matcher.groupCount();i++)
          {
            sb.append(" group").append(i).append(" ").append(matcher.group(i));
          }
          Log.d(TAG,sb.toString());
          */
          String cmdPart,argPart;
          if(matcher.groupCount()>0)
          {
            //Log.d(TAG,"num of groups  '"+matcher.groupCount()+"' for "+cmdPat);
            
            cmdPart = line.substring(0,matcher.start(1)); 
            argPart = matcher.group(1);
            line = line.substring(matcher.end(1),line.length());
          }
          else
          {
            //Log.d(TAG,"no arg cmd "+matcher.group()+" in line "+line);
            cmdPart = line;
            argPart = "";
          }
          
          //Log.d(TAG," split into  '"+cmdPart+"' and '"+argPart+"' rest '"+line+"'");
					
					//dont forget to call the parse method of the command  
					//need to split it up 
					//TODO BTW here we add the parsing ehm... 
					//since the Commands hold the shell, they dont need to get the heap 
					//explicitely, no?
					CommandI aCmd = parentParser.findCmd(cmdPart);
					if (aCmd != null) 
					{
						aCmd.parse(argPart);
						resultStack.add(aCmd);
            //Log.d(TAG," cmd '"+aCmd+"' added");
					}
					else
          {
            //Log.d(TAG," cmd '"+line+"' not found");
            result &= false;
          }
					//noinspection UnnecessaryContinue
					continue;
				}//if(line.matches("^(\\S+)\\s*$"))
        //else Log.d(TAG,cmdPat+" fail '"+line+"'");
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
