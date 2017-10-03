package com.nohkumado.nohutils.commands;

/*
 * NAME HelpCommand 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * stops the exectuion of a shell oject
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  HelpCommand.java is free software; you can redistribute it and/or modify it under
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
import com.nohkumado.nohutils.*;

public class HelpCommand extends Command implements Cloneable, CommandI
{
	/**
	 CTOR

	 Build up a reference

	 */
	public HelpCommand(ShellI s)
	{
		super(s);
		if (s != null) name = s.msg(com.nohkumado.nohutils.R.string.quit);
	}// public Command()

	public HelpCommand(ShellI s, String n)
	{
		super(s, n);
	}// public HelpCommand()
	/**

	 execute

	 activate this command

	 * @return reult
	 */
	public String execute()
	{
		return(shell.help());
	}//end execute
	/**

	 help

	 issue the help message associated with this command

	 */
	public String help()
	{
		return(shell.msg(com.nohkumado.nohutils.R.string.shell_help) + "\n");
	}//end help
	/**
	 make a copy of this object
	 */
	public HelpCommand clone()
	{
		//beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
    return (HelpCommand)super.clone();
	}//public Object clone()
}//public class Command
