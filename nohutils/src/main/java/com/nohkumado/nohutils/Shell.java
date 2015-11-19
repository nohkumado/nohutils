/** Id: Shell.java,v 1+4 2005/09/30 16:24:48 bboett Exp  -*- java -*-
 *
 * NAME Shell 
 *
 * AUTHOR Bruno Boettcher <nohkumado at gmail dot com> 
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
 *  Shell.java is free software; you can redistribute it and/or
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
import android.content.*;
import android.content.res.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.TextView.*;
import java.io.*;
import java.util.*;

/*
 TODO
 -  maybe add a pseudo graphical mode, 
 meaning if a cmd can be done graphically it should be done so....
 - delegate the command queue to a worker thread.... 

 */
public class Shell implements Cloneable,ShellI,OnEditorActionListener
{
	protected HashMap<String,Object> localVars = new HashMap<String,Object>();
	protected ArrayList<String> screenContent = null;
  protected CommandParserI cmdParser = null;
  protected  TextView out = null;
  protected  EditText in = null;

  protected boolean batchMode = false;
  protected boolean running = true;
  protected String scanType = null;

	public static final String TAG="Shell";
	Context context = null;

	private static final int MAXLINES = 100;
	int maxLines;

  /** CTOR

   */
  //public Shell(CmdLineParserI p)
  public Shell(Context c, CommandParserI p)
  {
    super();
		context = c;
    if (p == null) cmdParser = new CmdLineParser();
    else cmdParser = p;
    cmdParser.shell(this);
    set("shell", this);
  }// public Shell()
  public Shell(Context c)
  {
		this(c, null);
  }// public Shell()

	public void setInOut(EditText in, TextView out)
	{
		this.out = out;
		this.in = in;
		if (screenContent == null) screenContent = new ArrayList<String>();
		if (in != null) in.setOnEditorActionListener(this);
		else Log.e(TAG, "couldn't set action listener");
	}
  /**

	 init

	 after instantiation initialisation

   */
  public boolean init()
  {
	  if (get("maxlines") == null) set("maxlines", MAXLINES);
		try
		{
			maxLines = Integer.parseInt(get("maxlines").toString());	
		}
		catch (NumberFormatException e)
		{
			maxLines = MAXLINES;
		}

		//Log.d(TAG, "init printing start message");
		print(msg(R.string.start));
    prompt();
    return(true);
  }//public void init
  /**

	 process

	 this Method is the one that catches the commands and interprets them
	 should copy some parts of nSim:lineShell concerning batches!

   */
  public String process(String line)
  {
    //TODO check if shell is allready running otherwise push into a TODO stack
    String retVal = "";
		ArrayList<CommandI> toWorkOf = cmdParser.parse(line);
		if (toWorkOf.size() > 0) 
			for (Iterator<CommandI> i = toWorkOf.iterator(); i.hasNext();)
			{
				CommandI aCmd = i.next();
				if (aCmd != null) 
				{
					//System.out.println("abpout to exe: "+aCmd);
					retVal = aCmd.execute();
					if (retVal != "") print(retVal);
					//System.out.println("retVal = "+retVal);
				}//if(aCmd != null) 
				else
					retVal = "cmd was null??";
			}//for(Iterator<CommandI> i = toWorkOf.iterator(); i.hasNext();)
    return(retVal);
  }//end process
  /**

	 process

	 this Method is the one that catches the commands and interprets them
	 should copy some parts of nSim:lineShellI concerning batches!

	 this one is for internal use, when invoking commands through the shell programmately, so no command parsing is needed, parameters are in a hastable
   */
  public String process(String line, HashMap<String,Object> parm)
  {
    //TODO check if shell is allready running otherwise push into a TODO stack
    String retVal = "";
    CommandI aCmd = cmdParser.findCmd(line);
    if (aCmd != null) 
    {
      aCmd.setParameters(parm);
      retVal = aCmd.execute();
    }//if(aCmd != null) 
    return(retVal);
  }//end process
  /**

	 prompt

	 build up the prompt
	 try to emulate in some future time the bash prompt:

	 \a     an ASCII bell character (07)
	 \d     the date in "Weekday Month Date" format (e.g., "Tue May 26")
	 \D{format}
	 the format is passed to strftime(3) and the result is inserted into the prompt string; an empty for‐
	 mat results in a locale-specific time representation.  The braces are required
	 \e     an ASCII escape character (033)
	 \h     the hostname up to the first `.'
	 \H     the hostname
	 \j     the number of jobs currently managed by the shell
	 \l     the basename of the shell's terminal device name
	 \n     newline
	 \r     carriage return
	 \s     the name of the shell, the basename of $0 (the portion following the final slash)
	 \t     the current time in 24-hour HH:MM:SS format
	 \T     the current time in 12-hour HH:MM:SS format
	 \@     the current time in 12-hour am/pm format
	 \A     the current time in 24-hour HH:MM format
	 \\u     the username of the current user
	 \v     the version of bash (e.g., 2.00)
	 \V     the release of bash, version + patch level (e.g., 2.00.0)
	 \w     the  current  working  directory,  with  $HOME  abbreviated  with  a  tilde  (uses  the value of the
	 PROMPT_DIRTRIM variable)
	 \W     the basename of the current working directory, with $HOME abbreviated with a tilde
	 \!     the history number of this command
	 \#     the command number of this command
	 \$     if the effective UID is 0, a #, otherwise a $
	 \nnn   the character corresponding to the octal number nnn
	 \\     a backslash
	 \[     begin a sequence of non-printing characters, which  could  be  used  to  embed  a  terminal  control
	 sequence into the prompt
	 \]     end a sequence of non-printing characters



   */
  public String prompt()
  {
    String prompt = "";
    if (get("prompt") == null) 
    {
      prompt = "> ";
			set("prompt", prompt);
    }// if(prompt == null)
		else prompt = get("prompt").toString();
    if (prompt.matches(".*(\\w).*"))
    {
      String pwd = (String)get("pwd");
      if (pwd == null) pwd = System.getProperty("user.dir");
      prompt = prompt.replaceAll("\\\\w", pwd);
    }// if(prompt.matches(".*\\\\w.*")pp)
		if (in != null)
		{
			in.setText(prompt);
			in.setSelection(prompt.length());			
		}
		set("prompt", prompt);
		return(prompt);
  }//end prompt
  /** 
   * setter for promtp 
   * 
   * @param p 
   */
  public void prompt(String p)
	{ set("prompt", p); }
  /**

	 exit

	 quit ans close the shell


   */
  public void exit()
  {
    rmRessource("pwd");
    running = false;
    //System.out.println("set running to false....");
  }//end exit
  /** 
   * ressources 
   *
   * equivalent to the environment variables of a shell....
   TODO check with the other projects here a confusion 
	 between settings from config handler which are stored betweend 
	 sessions and local vars that should be dropped::::
   * 
   * @param envname 
   */
  public String ressource(String envname)
  {
		if (context == null) return envname;
		SharedPreferences prefs = context.getSharedPreferences(
      "com.nohkumado.autils", Context.MODE_PRIVATE);
		return(prefs.getString(envname, envname)); 
  }// public Object ressource(String envname)
  /** 
   * rmRessources 
   *
   * equivalent to the rm environment variables of a shell....
   * 
   * @param envname 
   */
  public java.lang.Object rmRessource(String envname)
  {
    Object result = null;
    if (localVars.containsKey(envname)) result = localVars.remove(envname);
    return(result);
		/* TODO when we switch over to android library
		 SharedPreferences mySPrefs =PreferenceManager.getDefaultSharedPreferences(this);
		 SharedPreferences.Editor editor = mySPrefs.edit();
		 editor.remove(String key);
		 editor.apply();
		 */
  }// public Object rmRessource(String envname)
  /** 
   * get 
   *
   * equivalent to the environment variables of a shell....
   * 
   * @param envname 
   * @param envname 
   * @return the objet of the setting to get
   */
	public Object get(String varname)
	{
    return(localVars.get(varname));
	}
	public Map<String,Object> getAll()
	{
    return(localVars);
	}

	/** 
   * ressources 
   *
   * equivalent to the environment variables of a shell....
   * 
   * @param envname 
   * @param envname 
   */
	public Object set(String varname, Object value)
	{
		if (varname == null) return(value);
    return(localVars.put(varname, value));
	}
	/** 
   * prototype for a help function 
   * 
   * @return 
   */
  public String help()
  {

    return("");
  }//end help
  /** 
   * returns the message associated with a token
   * 
   * @param  resourceId
   * @return 
   */
  public String msg(int resourceId)
  {
		if (context == null) return "no context";
		return(context.getResources().getString(resourceId));
  }// public String msg(String m)
  /** 
   * returns the message associated with a token
   * 
   * @param m 
   * @return 
   */
  public String msg(String m)
  {
		if (context == null) return m;
		
		try
		{
			int resourceId = context.getResources().getIdentifier(m, "strings", context.getPackageName());
			return(context.getResources().getString(resourceId));
		}
	catch (Resources.NotFoundException e) { Log.e(TAG,"not found message : "+m);}
	return(m);
	}// public String msg(String m)
	/**
	 executeCommands
	 
	 @param toWorkOf
	 
	 cycle over list and execute the stored commands, i think not thread safe...
 */
	private void executeCommands(ArrayList<CommandI> toWorkOf)
	{
		if (toWorkOf.size() > 0) 
			for (Iterator<CommandI> i = toWorkOf.iterator(); i.hasNext();)
			{
				CommandI aCmd = i.next();
				if (aCmd != null) 
				{
					//System.out.println("abpout to exe: "+aCmd);
					String retVal = aCmd.execute();
					if (retVal != "") print(retVal);
					//System.out.println("retVal = "+retVal);
				}//if(aCmd != null) 
				else
					System.out.println("cmd was null??");
			}
	}//public void run()
  /**
	 read: 
	 read a line of commands
   */
  public String read()
  {
    //TODO at the moment we drop the batchfile func and drop the buggy Input Stream stuff
    /** now initialise the loop that will read from the inputStream until
     *exhaustion */
    String inputLine = "";
    if (scanType != null && scanType.equals("numeric"))
    {
      print(msg(R.string.numeric_asked));
      boolean isNumeric = false;
      do
      {
				try
				{
					inputLine = Double.parseDouble(in.getText().toString()) + "";
					isNumeric = true;
				}// try
				catch (InputMismatchException e)
				{
					print(msg(R.string.not_a_numeric) + "\n>");
					isNumeric = false;
				}// catch(InputMismatchException e)
      }
      while(isNumeric == false);
      scanType = null;
    }// if(scanType != null && scanType.equals("numeric"))
    else if (scanType != null && scanType.equals("int"))
    {
      print(msg(R.string.question_is_int));
      boolean isNumeric = false;
      do
      {
				try
				{
					inputLine = Integer.parseInt(in.getText().toString()) + "";
					isNumeric = true;
				}// try
				catch (InputMismatchException e)
				{
					print(msg("not_a_int") + "\n>");
					isNumeric = false;
				}// catch(InputMismatchException e)
      }
      while(isNumeric == false);
      scanType = null;
    }// if(scanType != null && scanType.equals("numeric"))
    else
      inputLine = in.getText().toString();
    //print("read: scanner returned '"+inputLine+"'\n");
    return(inputLine);
  }//end private String ReadFile(FileInputStream a)
	@Override
	public boolean onEditorAction(TextView tw, int actionId, KeyEvent event)
	{
		//Log.d(TAG, "editoraction id : "+actionId+" ev "+event);

		//if (tw != out) Log.d(TAG, "wrong source");
		//else Log.d(TAG, "good source");
		
		//if (actionId == EditorInfo.IME_NULL		&&   
		if (event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			//Log.d(TAG, "hit the enter key... calling read");
			String incoming = tw.getText().toString().trim();
			//Log.d(TAG, "read extracted " + incoming);
			String logEntry = prompt()+" "+incoming;
			//Log.d(TAG,"should print out '"+incoming+"'");
			print(incoming);
			//Log.d(TAG,"proceeding to parse");
			ArrayList<CommandI> toWorkOf = cmdParser.parse(incoming);
			executeCommands(toWorkOf);	
    }


		return true;
	}
  //------------------------------------------------------------------
  public void fedThroughString(String aBatch)
  {
		//TODO implement later...

		ArrayList<CommandI> toWorkOf = cmdParser.parse(aBatch);

		executeCommands(toWorkOf);
		//for(It
    //in = new StringBufferInputStream(aBatch);
    //  catch (IOException e)
    //  {
    //     print("#OpenFile : " + e+"\n");
    //     exit(0);
    //  }//end catch (IOException e)
    batchMode = true;
  }//public void fedThroughFile(String aFile)
  //------------------------------------------------------------------
  public void fedThroughFile(String aFile)
  {
    batchMode = true;
		try
		{
			String content = new Scanner(new File(aFile)).useDelimiter("\\Z").next();
			ArrayList<CommandI> toWorkOf = cmdParser.parse(content);

			executeCommands(toWorkOf);
		}
		catch (FileNotFoundException e)
		{
			print("#The file couln't be found\n");
			
		}
  }//public void fedThroughFile(String aFile)
  //------------------------------------------------------------------

  //------------------------------------------------------------------
  protected void print(Object something)
  {
		print(something.toString());
  }//protected void print(String something)
  //------------------------------------------------------------------
  public void print(String something)
  {
		something = something.trim();
		//Log.d(TAG,"print '"+something+"'");
		screenContent.add(something);
		if (screenContent.size() > maxLines) 
			while (screenContent.size() > maxLines) screenContent.remove(0);
		StringBuilder sb = new StringBuilder();
		for (String s : screenContent)
		{
			//Log.d(TAG,"adding line '"+s+"'");
			sb.append(s);
			sb.append("\n");
		}
		//Log.d(TAG,"complete text '"+sb.toString()+"'");
		out.setText(sb.toString());
		out.invalidate();
  }//protected void print(String something)
	//------------------------------------------------------------------
  public void error(String aMessage) 
  {
    Log.e(TAG,"Error:" + aMessage);
  }//public void error(String aMessage) 
	//------------------------------------------------------------------
	public void exit(String aMessage)
	{
		print(aMessage);
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}//public void die(String aMessag)
	//------------------------------------------------------------------
	/** in fact read a line...*/
	public String ask(String question)
	{
		if (question.length() > 0) print(question + " ");
		return(read());
	}//public String ask()
	/**
	 * in fact read a line...
	 * 
	 * @param question a string to issue to prompts for an answer
	 * @param options more data, e.g. default value, a range selection, captions for range selection,  
	 * @return 
	 */
	public String ask(String question, HashMap<String,Object> options) 
	{
		//for(Iterator<String> i = ((List<String>) values.get("select")).iterator() ; i.hasNext();) console.print("debug: "+i.next()+"\n");
		if (options.get("select") != null)
		{
			String defVal = (String)options.get("default");
			if (question.length() > 0) print(question + " ");
			question = "";
			List select = (List) options.get("select");
			List<String> captions = (List<String>) options.get("captions");

			List selectCopy = new ArrayList<String>();
			List captionsCopy = new ArrayList<String>();
			if (defVal != null && defVal.length() > 0)
			//if(defVal != null && defVal.length() > 0 && !defVal.equals("null"))
			{
				System.out.println("adding default: '" + defVal + " = " + defVal.getClass());
				selectCopy.add(defVal);
				captionsCopy.add("");
			}//if(defVal != null && defVal.size() > 0)
			for (int i = 0 ; i < select.size(); i++)
			{
				if (!select.get(i).equals(defVal))
				{
					selectCopy.add(select.get(i));
					if (captions != null && captions.size() > i)captionsCopy.add(captions.get(i));
					else captionsCopy.add("");
				}//if(!select.get(i).equals(defVal))
			}// for(int i = 0 ; i < select.length; i++)

			for (int i = 0 ; i < selectCopy.size(); i++)
			{
				question += "[" + i + "] : " + selectCopy.get(i);
				String cap = "" + captionsCopy.get(i);
				if (cap != null && cap.length() > 0) question += " (" + cap + ")";
				question += "\n>";
			}// for(int i = 0 ; i < select.length; i++)

			int index = 0;
			String answer = "";
			do
			{
				answer = ask(question);
				if (answer.equals(""))
				{index = 0;}
				else
				{
					try
					{
						index = (new Integer(answer)).intValue();
					}// try
					catch (NumberFormatException e)
					{
						System.out.println("no valid number: " + answer); 
						index = -1;
					}// catch(NumberFormatException e)
				}//else
			}// do
			while(index < 0  || index >= select.size()); 
			answer = selectCopy.get(index) + "";
			return(answer);
		}// if(options.get("select") != null)
		else if (options.get("type") != null) scanType = options.get("type") + "";
		else
			System.out.println("need to do something with the options:" + options);
		return(ask(question));
	}//public String ask(String question,HashMap<String,.Object> options) 
	/** in fact read a line...*/
	public String ask(String question, String defaultValue) 
	{
		String result = ask(question + "[" + defaultValue + "]");
		if (result == null || result.equals("")) result = defaultValue;
		return(result);
	}//public String ask(String question,HashMap<String,.Object> options) 
	//the sames and forcing numerical reading
	public String askNum(String question)
	{
    scanType = "numeric";
    return(ask(question));
	}// public String askNum(String question)
	public String askNum(String question, String defaultValue) 
	{
    scanType = "numeric";
    return(ask(question, defaultValue));
	}// public String askNum(String question)
	public String askNum(String question, HashMap<String,Object> options) 
	{
    scanType = "numeric";
    return(ask(question, options));
	}// public String askNum(String question,HashMap<String,Object> options) 
  /** Load a filename, parse it and instantiate the correkt elements, using
   * the Member datastring MemberClassName for the instantiation, generic
   * code that is able to load JournalEintrag, as well as KontoEintrag or
   * Currencies...
   * 
   * @param baseName 
   */
  public void load(String baseName)
  {
  }//public boolean load(String baseName)
  public void load()
  {
  }//public boolean load(String baseName)
  public void save()
  {
  }// public void save()
	public boolean isRunning()
	{ return(running);}
	/**
	 *
	 *  feedCmds
	 *
	 * @param cmds 
	 * @return 
	 */
	public HashMap<String,CommandI> feedCmds(HashMap<String,CommandI> cmds)
	{
		return(cmdParser.feedCmds(cmds));
	}// public HashMap<String,CommandI> feedCmds(HashMap<String,CommandI> cmds)
	@Override
	public void debug(String errorMsg)
	{
		Log.d(TAG, errorMsg);
	}


}//public class Shell
