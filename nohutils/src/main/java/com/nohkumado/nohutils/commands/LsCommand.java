/** Id: LsCommand.java,v 1.4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME LsCommand 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION 
 * changes a settings of the shell
 *
 * SYNOPSIS 
 *
 * after instantiation, execute it!
 *
 * COPYRIGHT and LICENCE
 *
 *  Copyright (c) 2004 Bruno Boettcher
 *
 *  LsCommand.java is free software; you can redistribute it and/or modify it under
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
import java.util.*;
import java.io.*;
import java.util.regex.*;
import com.nohkumado.nohutils.*;
import android.util.*;

public class LsCommand extends FileExpandCommand implements Cloneable, CommandI
{
	public static final String TAG="LsCmd";

  protected FilenameFilter filter;
  /**
	 CTOR

	 Build up a reference

   */
  public LsCommand(ShellI s)
  {
    super(s);
		if (s != null) name = s.msg(R.string.ls);
  }// public Command()

  public LsCommand(ShellI s, String n)
  {
    super(s, n);
  }// public LsCommand()
  /**

	 execute

	 activate this command

   * @param line 
   * @param heap 
   * @return 
   */
  public String execute()
  {
		Log.d(TAG,"ls starting pwd: "+shell.get("pwd"));
		
		File testit = new File(""+shell.get("pwd"));
		Log.d(TAG,"pwd: "+testit.exists()+" dir?"+testit.canRead());
		
    String result = "";
		CommandI pwdCmd = new PwdCommand(shell);
    String pwd = pwdCmd.execute();
		Log.d(TAG,"from cmd pwd: "+pwd);
		
    //if (pwd == null) pwd = System.getProperty("user.dir");
    //else if (pwd.length() <= 0) pwd = System.getProperty("user.dir");
		//Log.d("LsCmd", "pwd = " + pwd);
		String sep = System.getProperty("file.separator");
    if (!path.equals(""))
    {
      if (!path.startsWith("/")) path = pwd + sep + path;
    }// if(value != "")
    else path = pwd;
		//Log.d("LsCmd", "path = " + path);

    File theDir = new File(path);
		if (!theDir.exists()) return(shell.msg(R.string.cd_does_not_exist));
		if (!theDir.canRead()) return(shell.msg(R.string.ls_not_enough_rights));
		
    if (theDir.exists() && theDir.isDirectory())
    {
			result += theDir.getAbsolutePath();
			if (theDir.isDirectory())
			{

				result += " :";
				//Log.d("LsCmd", "reading dir");
				String[] choices = theDir.list(filter);
				int maxlength = 1;
				ArrayList<String> content = new ArrayList<String>();
				for (String name : choices) 
				{
					//Log.d(TAG, "name= " + name);
					if (name == null) continue;
					//Log.d(TAG, "2name= " + name);
					File aFile = new File(path + sep + name);
					try
					{
						if (aFile != null && aFile.exists())
						{
							Log.d(TAG, "file exists, adding to content: " + content + " n:" + name + " max:" + maxlength);

							if (name != null && name.length() > maxlength) maxlength = name.length();
							//Log.d(TAG,"2file exists, adding to content: "+content+" n:"+name+" max:"+maxlength);
							if (name != null)
							{
								if (aFile.isDirectory()) content.add(name + "/");
								else if (aFile.isFile()) content.add(name + "*");
								else content.add(name + "");
							}
						}//if (aFile.exists())	
					}
					catch (NullPointerException e)
					{  }

				}//for(String name : choices)

				int numOfchars = shell.getDisplayWidth();

				int numOfCols = numOfchars / maxlength;
				if (numOfCols < 1) numOfCols = 1;
				//Log.d("lscm", "available chars: " + numOfchars + " malength: " + maxlength + " = " + numOfCols);

				ColumPrinter mp = new  ColumPrinter(numOfCols, 2, "-");
				String    oneRow[] = new String [ 3 ];
				int counter = 0;
				for (String name : content)
				{
					oneRow[counter] = name;
					counter++;
					if (counter % numOfCols == 0)
					{
						mp.add(oneRow);
						counter = 0;
					}
				}
				mp.print();
				result += mp.toString();
			}
			else
			{
				if (theDir.isHidden()) result += "!";
				result += "\n";
			}
			//Log.d("LsCmd", "read dir: "+result);
      return(result);
    }//if(theDir.exists() && theDir.isDirectory())
		
		return(result);
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
    path = "";
    if (line != null && line.length() > 0)
    {
      String[] result = line.split(System.getProperty("file.separator"));
      if (result.length > 1)
      {
				int x=0; 
				if (result.length > 1) path = ""; //reset the path if there is a path component!
				for (; x < result.length - 1; x++) path += result[x] + System.getProperty("file.separator");
				line = result[x];
      }// if(result.lenght > 1)«»

      Pattern regexp = Pattern.compile("^(.*?)\\*(.*)");
      Matcher matcher = regexp.matcher(line);
      if (matcher.find())
      {
				line = matcher.group(1) + ".*" + matcher.group(2);
				try
				{
					filter = new PatternFileFilter(line);
				}// try
				catch (PatternSyntaxException e)
				{
					shell.print(shell.msg(R.string.lscmd_invalid_pattern) + "\n");

				}// catch(PatternSyntaxException e)«»
      }//if(matcher.find())
      else path += line;
    }// if(line != null && line.length() > 0)

    return("");
  }// public String parse(String line)
  /**

	 help

	 issue the help message associated with this command

   */
  public String help()
  {
    return(shell.msg(R.string.ls_help)	+ "\n");
  }//end help
  //make a copy of this object
  public LsCommand clone()
  {
    LsCommand cloned = (LsCommand)super.clone();
		cloned.filter = new PatternFileFilter(filter.toString());
		
    return cloned;
  }//public Object clone()
}//public class Command
