package eu.nohkumado.utilsapp.commands;
/*
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
import com.nohkumado.utilsapp.R;

import eu.nohkumado.nohutils.Command;
import eu.nohkumado.nohutils.CommandI;
import eu.nohkumado.nohutils.ShellI;
import eu.nohkumado.utilsapp.test.NohTest;

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
      name("test");
      //if (s != null) name = s.msg(R.string.test_cmd);
    }// public Command()

    public TestCmd(ShellI s, String n)
    {
      super(s, n);
    }// public SetCommand()
    /**

     execute

     activate this command

     * @return the result
     */
    @Override
    public String execute()
    {
      NohTest tester = new NohTest(shell);
      return(tester.runTest());
    }//end execute

    /** 
     * parse a setting line 
     * with no parameter its prints the whole list
     * with one parameter it prints the value of that parameter
     * with 2 parameters it replaces the parameter
     * 
     * @param line arguments
     * @return the unparsable rest
     */
    @Override
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
      return (TestCmd)super.clone();
    }//public Object clone()
  }//public class Command
