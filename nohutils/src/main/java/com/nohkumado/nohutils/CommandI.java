/** Id: CommandI.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME CommandI 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 *
 * a class of object that takes a plan and produces something
 * commands::CommandI object destined to be executed, and that will perform on
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
 *  CommandI.java is free software; you can redistribute it and/or modify it under
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
package com.nohkumado.nohutils;
import java.util.*;
import java.util.regex.*;
//import com.gnu.utils.*;

public interface CommandI extends Cloneable,PipableI
{

	public CommandI clone();


	public Pattern pattern();

	/** 
	 * name
	 *
	 * return the name of this command
	 * 
	 * @return the name of this item as a string
	 */
	public String name();
	/** 
	 * name
	 *
	 * set the name of this command
	 * 
	 * @return the name of this item as a string
	 */
	public void name(String n);
	/**

	 execute

	 activate this command

	 * @param line 
	 * @param heap 
	 * @return 
	 */
	public String execute();
	/**

	 parse

	 parse for eventual arguments, return what is not needed

	 * @param line 
	 * @return 
	 */
	public String parse(String line);
	/** 
	 * instead of parsing the options, give them directly, eg when invoking a command from the program code directly
	 * 
	 * @param parms the hashtable with the options
	 * @param parms 
	 */
	public void setParameters(HashMap<String,Object> parms);
	/**

	 help

	 issue the help message associated with this command

	 */
	public String help();
	public String expand(String actArg);
}//public class CommandI
