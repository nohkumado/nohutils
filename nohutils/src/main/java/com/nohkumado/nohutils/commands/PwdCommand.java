/*
 * NAME PwdCommand 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * shows the actual working directory
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  PwdCommand.java is free software; you can redistribute it and/or modify it under
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
package com.nohkumado.nohutils.commands;
import com.nohkumado.nohutils.*;

public class PwdCommand extends Command implements Cloneable, CommandI
{
  /**
    CTOR

    Build up a reference

   */
  public PwdCommand(ShellI s)
  {
    super(s);
		if(s != null) name = s.msg(R.string.pwd);
  }// public Command()

  public PwdCommand(ShellI s,String n)
  {
    super(s,n);
  }// public PwdCommand()
  /**

    execute

    activate this command

   * @return result
   */
  public String execute()
  {
    String result = "";
    String pwd = (String)shell.get("pwd");
    if(pwd == null ) pwd = System.getProperty("user.dir")+"sdcard";
    else if(pwd.length() <= 0 ) pwd = System.getProperty("user.dir"+"sdcard");
    shell.set("pwd",pwd);
		//shell.print(pwd);
    return(pwd);
  }//end execute
  /**

    help

    issue the help message associated with this command

   */
  public String help()
  {
    return(shell.msg(R.string.pwd_help)+"\n");
  }//end help
  /** 
   * copy this object 
   * 
   * @return clone
   */
  public PwdCommand clone()
  {
    //beware! shallow copy! if you command has some arrays or other deep structures, only the ref will be copied!
    return (PwdCommand)super.clone();
  }//public Object clone()
}//public class Command
