/** Id: ShellI.java,v 1+4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
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

//import com.gnu.utils.*;
import android.widget.*;
import java.util.*;
import java.io.*;

public interface ShellI extends PipableI 
{

	//public File getBaseDir();


	/**

    init

    after instantiation initialisation

   */
  public boolean init();
  /**

    process

    this Method is the one that catches the commands and interprets them
    should copy some parts of nSim:lineShellI concerning batches!
   */
  public String process(String line);
  /**

    process

    this Method is the one that catches the commands and interprets them
    should copy some parts of nSim:lineShellI concerning batches!

    this one is for internal use, when invoking commands through the shell programmately, so no command parsing is needed, parameters are in a hastable
   */
  public String process(String line,HashMap<String,Object> parm);
  /**

    prompt

    build up the short help string
   */
  public String prompt();
  public void prompt(String p);
	public void printOnCmdline(String prompt)
  /**

    exit

    quit ans close the shell


   */
  public void exit();
  public void exit(String endMsg);
  /** 
   * ressources 
   *
   * equivalent to the environment variables of a shell....
   * 
   * @param envname 
   * @param envname 
   */
  public String preference(String locname);
  public String preference(String locname,Object res);
  
  /** 
   * local settings 
   * 
   * @param envname 
   * @return 
   */
  public Object get(String envname);
	public Map<String, Object> getAll();

  
  /** 
   * ressources 
   *
   * equivalent to the environment variables of a shell....
   * 
   * @param envname 
   * @param envname 
   */
  public Object set(String envname, Object obj);
  /** 
   * prototype for a help function 
   * 
   * @return 
   */
  public String help();
  /**
    issue a statement....
   */
  public void print(String n);
  /**
    issue a question and expect a return
   */
  public void ask(String n,CommandI asker);
  public void ask(String question, String defaultValue,CommandI asker);
  public void ask(String question, HashMap<String,Object> options,CommandI asker);
  public void askNum(String n,CommandI asker);
  public void askNum(String question, String defaultValue,CommandI asker);
  public void askNum(String question, HashMap<String,Object> options,CommandI asker);
  /**
    retrieve a localized message
   */
  public String msg(String errorMsg);
	public String msg(int errorId);
	
	/**
	 issue an debug message
   */
  public void debug(String errorMsg);
	/**
	 issue an error
   */
  public void error(String errorMsg);
  /**
     isRunning
     return if the shell is running or not
   */
   public boolean isRunning();
    /**
     *
     *  feedCmds
     *
     * @param cmds 
     * @return 
     */
    public HashMap<String,CommandI> feedCmds(HashMap<String,CommandI> cmds);

	public int getDisplayWidth();
	/** 
	set the input editext and the output screen
	*/
	public void setInOut(EditText in, TextView out);
	public InputStream open(String name)  throws IOException;
	
}//public class ShellI
