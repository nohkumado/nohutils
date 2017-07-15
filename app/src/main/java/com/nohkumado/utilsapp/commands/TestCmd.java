package com.nohkumado.utilsapp.commands;
/** 
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  SetCommand.java is free software; you can redistribute it and/or modify it under
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
import com.nohkumado.nohutils.commands.*;
import com.nohkumado.utilsapp.*;
import java.util.*;
import java.util.regex.*;

import com.nohkumado.utilsapp.test.*;

public class TestCmd extends Command implements Cloneable, CommandI
  {
    protected String var_name = "", var_value = "";
    /**
     CTOR

     Build up a reference

     */
    public TestCmd(ShellI s)
    {
      super(s);
      if (s != null) name = s.msg(R.string.test_cmd);
    }// public Command()

    public TestCmd(ShellI s, String n)
    {
      super(s, n);
    }// public SetCommand()
    /**

     execute

     activate this command

     * @param line 
     * @param heap 
     * @return 
     */
    public String execute()
    {
      NohTest tester = new NohTest();
      return(tester.runTest());
    }//end execute

    /** 
     * parse a setting line 
     * with no parameter its prints the whole list
     * with one parameter it prints the value of that parameter
     * with 2 parameters it replaces the parameter
     * 
     * @param line 
     * @return 
     */
    public String parse(String line)
    {
      return("");
    }// public String parse(String line)«»
    /**

     help

     issue the help message associated with this command

     */
    public String help()
    {
      //return(shell.msg("set") + " " + var_name + " " + shell.msg("value") + " " + shell.msg("to_set_a_value") + "\n");
      return(shell.msg(R.string.testhelp));
    }//end help
    //make a copy of this object
    public TestCmd clone()
    {
      TestCmd cloned = (TestCmd)super.clone();
      return cloned;
    }//public Object clone()
  }//public class Command
