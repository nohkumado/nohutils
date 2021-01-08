/*
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
 *  This program is distributed in the hope that it will be useful, but WITHOUT ANY
 *  WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 *  PARTICULAR PURPOSE.  See the GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package eu.nohkumado.nohutils;

import java.util.HashMap;
import java.util.regex.Pattern;
//import com.nohkumado.utils.*;

@SuppressWarnings("SameParameterValue")
public interface CommandI extends Cloneable, PipableI
{

	CommandI clone();


	Pattern pattern();

	/**
	 * name
	 * <p>
	 * return the name of this command
	 *
	 * @return the name of this item as a string
	 */
	String name();

	/**
	 * name
	 * <p>
	 * set the name of this command
	 *
	 * @return the name of this item as a string
	 */
	CommandI name(String n);

	/**
	 * execute
	 * <p>
	 * activate this command
	 *
	 * @return reult
	 */
	String execute();

	/**
	 * parse
	 * <p>
	 * parse for eventual arguments, return what is not needed
	 *
	 * @param line arguments
	 * @return rest
	 */
	String parse(String line);

	/**
	 * instead of parsing the options, give them directly, eg when invoking a command from the program code directly
	 *
	 * @param parms the hashtable with the options
	 */
	void setParameters(HashMap<String, Object> parms);

	/**
	 * help
	 * <p>
	 * issue the help message associated with this command
	 */
	String help();
	CommandI help(String msg);

	String expand();
}//public class CommandI
