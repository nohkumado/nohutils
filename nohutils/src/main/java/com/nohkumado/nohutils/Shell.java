
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

import android.content.*;
import android.content.res.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.TextView.*;
import java.io.*;
import java.util.*;

import android.view.View.OnKeyListener;

/*
 TODO
 -  maybe add a pseudo graphical mode, 
 meaning if a cmd can be done graphically it should be done so....
 - delegate the command queue to a worker thread.... 

 */
public class Shell implements Cloneable,ShellI,OnEditorActionListener,OnKeyListener
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
	MsgR2StringI context = null;

	private static final int MAXLINES = 100;
	int maxLines = 1024;

	protected CommandI actQuestion = null;

	protected ArrayList<String> history = new ArrayList<String>();

	protected  int maxHistory = 1024;
	protected int histNavigation = 0;
  /** CTOR

   */
  //public Shell(CmdLineParserI p)
  public Shell(MsgR2StringI c, CommandParserI p)
  {
    super();
		context = c;
    if (p == null) cmdParser = new CmdLineParser();
    else cmdParser = p;
    cmdParser.shell(this);
    set("shell", this);
  }// public Shell()
  public Shell(MsgR2StringI c)
  {
		this(c, null);
  }// public Shell()

	public void setInOut(EditText in, TextView out)
	{
		this.out = out;
		this.in = in;
		if (screenContent == null) screenContent = new ArrayList<String>();
		if (in != null)
		{
			in.setOnEditorActionListener(this);
			in.setOnKeyListener(this);
		}
		else Log.e(TAG, "couldn't set action listener");
		if (out == null) Log.e(TAG, "no output screen....");
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
					//TODO pipe ahould interced e here 
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
		//TODO argh....
		printOnCmdline(prompt);
		set("prompt", prompt);
		return(prompt);
  }

	public void printOnCmdline(String prompt)
	{
		if (in != null)
		{
			in.setText(prompt);
			in.setSelection(prompt.length());			
		}
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
		rmRessource("prompt");

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
	 changed now, be careful with other projects!! preferences is now called (instead of ressource) and it string only (will have to revert if necessary) and accesses shared prefs
   * 
   * @param envname 
   */
  public String preference(String envname)
  {
		if (context == null) return envname;
		SharedPreferences prefs = context.getSharedPreferences(
			context.getPackageName(), Context.MODE_PRIVATE);
		String result = prefs.getString(envname, envname);	
		return(result);
  }// public Object ressource(String envname)
	@Override
	public String preference(String locname, Object res)
	{
		if (context == null) return locname;
		SharedPreferences prefs = context.getSharedPreferences(
      context.getPackageName(), Context.MODE_PRIVATE);
		if (res instanceof String) prefs.edit().putString(locname, (String)res).apply();
		else if (res instanceof Integer) prefs.edit().putInt(locname, (Integer)res).apply();
		return null;
	}
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
		catch (Resources.NotFoundException e)
		{ Log.e(TAG, "not found message : " + m);}
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
		if (in == null)
		{
			Log.e(TAG, "oyoyoy??? no input field given....");
			return "";
		}
    String inputLine = "";
		//TODO we can't wait here.... we should put the question in the prompt after saving a backup, 
		//and notify the eventhandler callback that we are waiting for an answer....
		//means asynchronous working... we have to store the next workflow until we get an answer...

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
	/**
	 * onEditorAction
	 * @param tw, the view the event happened
	 * @param actionId, 
	 * @param event
	 */
	@Override
	public boolean onEditorAction(TextView tw, int actionId, KeyEvent event)
	{
		//Log.d(TAG, "editoraction id : "+actionId+" ev "+event);

		//if (tw != out) Log.d(TAG, "wrong source");
		//else Log.d(TAG, "good source");

		//if (actionId == EditorInfo.IME_NULL		&&   
		if (event.getAction() == KeyEvent.ACTION_DOWN) 
		{
			histNavigation = 0;
			//Log.d(TAG, "hit the enter key... calling read");
			String incoming = tw.getText().toString().trim();
			//removing prompt from incoming line

			String lastprompt = prompt();
			if (lastprompt != null) incoming = incoming.replace(lastprompt.trim(), "").trim();

			if (incoming.length() > 0)  history.add(incoming);
			if (history.size() > maxHistory) 
				while (history.size() > maxHistory) history.remove(0);
			
			//Log.d(TAG, "read extracted " + incoming);
			if (actQuestion != null)
			{
				CommandI toExe = actQuestion;
				actQuestion = null;
				//setting to 0 beforehand, since the parsing could arise new questions...
				toExe.parse(incoming);
				printOnCmdline(prompt());
			}
			else
			{
				String logEntry = prompt() + " " + incoming;
				//Log.d(TAG,"should print out '"+incoming+"'");

				print(incoming);
				//Log.d(TAG,"proceeding to parse");

				ArrayList<CommandI> toWorkOf = cmdParser.parse(incoming);
				executeCommands(toWorkOf);	
			}
    }


		return true;
	}
	/**------------------------------------------------------------------
	 * onKey
	 * @param tw, the view the event happened
	 * @param actionId, 
	 * @param event
	 */

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
		//Log.d(TAG, "hit key :" + keyCode + " in?(" + KeyEvent.KEYCODE_DPAD_UP + "," + KeyEvent.KEYCODE_TAB + ")");
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP)
		{
			histNavigation++;
			int actPost = history.size() - histNavigation;
			if(actPost < 0)
			{
				context.playSound();
				actPost = 0;
				histNavigation = history.size();
			}
			//TODO beware, if there are expansion commands in the prompt what then??
			printOnCmdline(prompt()+" "+history.get(actPost));
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN)
		{
			histNavigation--;
			int actPost = history.size() - histNavigation;
			String content = "";
			if(actPost >= history.size())
			{
				if(actPost == history.size()) {}
				else 
				{
					context.playSound();
					histNavigation = 0;	
				}
				content = "";
			}
			else content = history.get(actPost);
			//TODO beware, if there are expansion commands in the prompt what then??
			printOnCmdline(prompt()+" "+content);
		}
		
		else if (keyCode == KeyEvent.KEYCODE_TAB)
		{
			print("hit tab key");
		}
		return false;
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
		if (something == null) something = "";
		something = something.trim();
		//Log.d(TAG,"print '"+something+"'");
		if (screenContent == null)
		{Log.e(TAG, "oyoyoy??? no screen to print " + something + "!!"); return; }
		if (something.length() > 0)  screenContent.add(something);
		//Log.d(TAG, "maxlines = " + maxLines);
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
		if (out == null)
		{
			Log.e(TAG, sb.toString());
		}
		else 
		{
			out.setText(sb.toString());

			Object obj = out.getParent();
			if (obj instanceof ScrollView)
			{
				ScrollView scroll_view = (ScrollView) obj;
				DownScroller scroll = new DownScroller(scroll_view);
				scroll_view.post(scroll);
			}
			out.invalidate();
		}
  }//protected void print(String something)
	//------------------------------------------------------------------
  public void error(String aMessage) 
  {
    Log.e(TAG, "Error:" + aMessage);
  }//public void error(String aMessage) 
	//------------------------------------------------------------------
	public void exit(String aMessage)
	{
		print(aMessage);
		exit();
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}//public void die(String aMessag)
	//------------------------------------------------------------------
	/** in fact read a line...*/
	public void ask(String question, CommandI caller)
	{
		actQuestion = caller;
		if (question.length() > 0) print(question + " ");
		//return(read());
	}//public String ask()
	/**
	 * in fact read a line...
	 * 
	 * @param question a string to issue to prompts for an answer
	 * @param options more data, e.g. default value, a range selection, captions for range selection,  
	 * @return 
	 */
	public void ask(String question, HashMap<String,Object> options, CommandI caller) 
	{
		actQuestion = caller;

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
				//TODO shall change this to asynchronous mode
				//	answer = ask(question,actQuestion);
				ask(question, actQuestion);
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
			//return(answer);
		}// if(options.get("select") != null)
		else if (options.get("type") != null) scanType = options.get("type") + "";
		else
			System.out.println("need to do something with the options:" + options);
		//return(ask(question));
	}//public String ask(String question,HashMap<String,.Object> options) 
	/** in fact read a line...*/
	public void ask(String question, String defaultValue, CommandI caller) 
	{
		actQuestion = caller;
		ask(question + "[" + defaultValue + "]", caller);
		//if (result == null || result.equals("")) result = defaultValue;
		//String result = ask(question + "[" + defaultValue + "]");
		//if (result == null || result.equals("")) result = defaultValue;
		//return(result);
	}//public String ask(String question,HashMap<String,.Object> options) 
	//the sames and forcing numerical reading
	public void askNum(String question, CommandI caller)
	{
		actQuestion = caller;
    scanType = "numeric";
    ask(question, caller);
		//return(ask(question,caller));
	}// public String askNum(String question)
	public void askNum(String question, String defaultValue, CommandI caller) 
	{
		actQuestion = caller;
    scanType = "numeric";
    ask(question, defaultValue, caller);
		//return(ask(question, defaultValue),caller);
	}// public String askNum(String question)
	public void askNum(String question, HashMap<String,Object> options, CommandI caller) 
	{
		actQuestion = caller;
    scanType = "numeric";
    ask(question, options, caller);
		//return(ask(question, options));
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

	@Override
	public int getDisplayWidth()
	{
		int result = 20;
		if (out != null)
		{
			result = (int) (out.getWidth() / out.getTextSize());
		}
		return result;
	}
	@Override
	public boolean setOut(PipableI out)
	{
		// TODO: Implement this method
		return false;
	}
	/*@Override
	 public File getBaseDir()
	 {
	 return context.getExternalFilesDir(null);
	 }*/

	@Override
	public InputStream open(String name) throws IOException
	{
		/*
		 open private file
		 String FILENAME = "hello_file";
		 String string = "hello world!";

		 FileOutputStream fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
		 fos.write(string.getBytes());
		 fos.close();

		 get public file:
		 Manifest:
		 <manifest ...>
		 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
		 ...
		 </manifest>
		 // Checks if external storage is available for read and write 
		 public boolean isExternalStorageWritable() {
		 String state = Environment.getExternalStorageState();
		 if (Environment.MEDIA_MOUNTED.equals(state)) {
		 return true;
		 }
		 return false;
		 }

		 // Checks if external storage is available to at least read 
		 public boolean isExternalStorageReadable() {
		 String state = Environment.getExternalStorageState();
		 if (Environment.MEDIA_MOUNTED.equals(state) ||
		 Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		 return true;
		 }
		 return false;
		 }
		 public File getAlbumStorageDir(String albumName) {
		 // Get the directory for the user's public pictures directory.
		 File file = new File(Environment.getExternalStoragePublicDirectory(
		 Environment.DIRECTORY_PICTURES), albumName);
		 if (!file.mkdirs()) {
		 Log.e(LOG_TAG, "Directory not created");
		 }
		 return file;
		 }

		 <manifest ...>
		 <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
		 android:maxSdkVersion="18" />
		 ...
		 </manifest>

		 storageDir = context.getExternalFilesDir(null); 
		 -
		 */
		//Log.d(TAG,"listing asset files");
		//listAssetFiles(""); 
		//Log.d(TAG,"trying to open asset "+name);
		InputStream is = context.getResources().getAssets().open(name);
		return is;
	}
	private boolean listAssetFiles(String path) 
	{
    String [] list;
    try
		{
			list = context.getResources().getAssets().list(path);
			if (list.length > 0) 
			{
				// This is a folder
				for (String file : list) 
				{
					Log.d(TAG, "asset: " + path + "/" + file);
					if (!listAssetFiles(path + "/" + file))
						return false;
				}
			}
			else
			{
				// This is a file
				// TODO: add file name to an array list
				//Log.d(TAG, "asset single file : " + path);
			}
    }
		catch (IOException e)
		{
			return false;
    }
    return true; 
	} 

}//public class Shell
