/*
 * NAME ShellI 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp+org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * a class of object that takes a plan and produces something
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  ShellI.java is free software; you can redistribute it and/or
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

import android.os.*;
import android.widget.*;
import com.nohkumado.nohutils.view.*;
import java.io.*;
import java.util.*;

@SuppressWarnings({"SameParameterValue", "UnusedReturnValue"})
public interface ShellI extends PipableI
{

	public void closelog();


	public void startlog(String logFileName);

	int getInt(String p0);

	ShellI cpyCtor();


	void setParent(ShellI shell);

	void setChild(ShellI actShell);
	ShellI rmChild();
	void parseMode(String mode);
	void clearCmds();
	void setContext(MsgR2StringI context);
	MsgR2StringI getContext();
	void restoreState(Bundle savedInstanceState);
	void saveState(Bundle savedInstanceState);
	void setInputMode(boolean p0);
	//public File getBaseDir();
	/**
	 init

	 after instantiation initialisation
	 */
    boolean init();
	/**
	 process

	 this Method is the one that catches the commands and interprets them
	 should copy some parts of nSim:lineShellI concerning batches!
	 */
	String process(String line);
	/**

	 process

	 this Method is the one that catches the commands and interprets them
	 should copy some parts of nSim:lineShellI concerning batches!

	 this one is for internal use, when invoking commands through the shell programmately, so no command parsing is needed, parameters are in a hastable
	 */
	String process(String line, HashMap<String, Object> parm);
	/**

	 prompt

	 build up the short help string
	 */
	String prompt();
	void prompt(String p);
	void printOnCmdline(String prompt);
	String getCmdline();
	/**
	 exit

	 quit and close the shell

	 */
    void exit();
	void exit(String endMsg);
	/** 
	 * ressources 
	 *
	 * equivalent to the environment variables of a shell....
	 * 
	 * @param locname locname
	 */
	String preference(String locname);
	String preference(String locname, Object res);
	int intPref(String locaname);

	/** 
	 * local settings 
	 *
	 * @param envname envname
	 * @return envname
	 */
	Object get(String envname);
	Map<String, Object> getAll();
	/** 
	 * ressources 
	 *
	 * equivalent to the environment variables of a shell....
	 * 
	 * @param envname  envname
	 * @param obj obj
	 */
	Object set(String envname, Object obj);
	/** 
	 * prototype for a help function 
	 * 
	 * @return help
	 */
	String help();
	/**
	 issue a statement....
	 */
	void print(String n);
	/**
	 issue a question and expect a return
	 */
	void ask(String n, CommandI asker);
	void ask(String question, String defaultValue, CommandI asker);
	void ask(String question, HashMap<String, Object> options, CommandI asker);
	void askNum(String n, CommandI asker);
	void askNum(String question, String defaultValue, CommandI asker);
	void askNum(String question, HashMap<String, Object> options, CommandI asker);
	/**
	 retrieve a localized message
	 */
	String msg(String errorMsg);
	String msg(int resourceId);

	/**
	 issue an debug message
	 */
    void debug(String errorMsg);
	/**
	 issue an error
	 */
    void error(String errorMsg);
	/*
	 isRunning
	 return if the shell is running or not
	 */
	//public boolean isRunning();
	/**
	 *
	 *  feedCmds
	 *
	 * @param cmds cmds
	 * @return HashMap
	 */
    HashMap<String,CommandI> feedCmds(HashMap<String, CommandI> cmds);

	int getDisplayWidth();
	/** 
	 set the input editext and the output screen
	 */
	//public void setInOut(EditText in, TextView out);
    void setInOut(EditText in, LoggerFrag out);
	InputStream open(String name)  throws IOException;
	void beep();
	void endQuestion(); //stop forwarding keyevents to a question
}//public class ShellI
