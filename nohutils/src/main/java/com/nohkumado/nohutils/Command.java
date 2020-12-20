/*
 * NAME Command 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 *
 * a class of object that takes a plan and produces something
 * commands::Command object destined to be executed, and that will perform on
 * execution one action
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  Command.java is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; version 2 of the License.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE.  See the GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.nohkumado.nohutils;
import java.util.*;
import java.util.regex.*;
//import com.nohkumado.utils.*;

@SuppressWarnings("WeakerAccess")
public class Command
implements Cloneable, CommandI
{
	protected String type = null;
	protected String name = null;
	protected String group = "user";
	protected ShellI shell = null;
	protected HashMap<String,Object> parameter ;
	protected PipableI stream = null;

	private String helpMsg;

	/**
	 CTOR

	 Build up a reference

	 */
	public Command(ShellI s)
	{
		this(s, "");
	}// public Command()

	public Command(ShellI s, String n)
	{
		shell = s;
		stream = shell;
		name = n;
	}// public Command()
	/** 
	 * name
	 *
	 * return the name of this command
	 * 
	 * @return the name of this item as a string
	 */
	public String name()
	{ 
		if (name == null) name = "";
		return(name); 
	}// public String name()
	/** 
	 * name
	 *
	 * set the name of this command
	 * 
	 * @return the name of this item as a string
	 */
	public Command name(String n)
	{
		name = n;
		return this;
	}//public Command name(String n)

	/**

	 execute

	 activate this command

	 * @return result
	 */
	public String execute()
	{
		return("Command::exe : abstract class no code\n");
	}//end execute
	/**

	 help

	 issue the help message associated with this command

	 * @return the help message
	 */
	public String help()
	{
		if (helpMsg != null && !"".equals(helpMsg)) return helpMsg + "\n";
		return(shell.msg(com.nohkumado.nohutils.R.string.no_help) + "\n");
	}//end help
	/**

	 help

	 issue the help message associated with this command

	 * @return the help message
	 */
	public Command help(String msg)
	{
		helpMsg = msg;
		return(this);
	}//end help
	/** 
	 * clone this command 
	 * make a copy of this object
	 * 
	 * @return a copy of this object
	 */
	public Command clone()
	{
		try
		{
			//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
			Command cloned = (Command)super.clone();
			cloned.parameter = null;
			return(cloned);
		}
		catch (CloneNotSupportedException e)
		{System.err.println("can't clone this"); e.printStackTrace();}
		return(null);
	}//public Object clone()
	/** 
	 * parse the command line for evenutal other switches options etc 
	 * 
	 * @param line line woth ooptions but without the command name
	 * @return the unparsed rest
	 */
	public String parse(String line)
	{ return(line);}
	/** 
	 * instead of parsing the options, give them directly, eg when invoking a command from the program code directly
	 * 
	 * @param parms the hashtable with the options
	 */
	public void setParameters(HashMap<String,Object> parms)
	{ parameter = parms;}

    /**
	 * preparation for pipe command, redirect in and output 
	 * @param msg the stuff to send on to be printed out normally
	 */
	public void print(String msg)
	{ stream.print(msg);}

    /**
	 * preparation for pipe command, redirect in and output 
	 * @param out the thing to send the output
	 */
	@Override
	public boolean setOut(PipableI out)
	{
		stream = out;
		return stream != null;
	}
	@Override
	public String expand()
	{
		return "";
	}

	@Override
	public Pattern pattern()
	{
		return Pattern.compile("^" + name + "\\s*$");
	}
}//public class Command
