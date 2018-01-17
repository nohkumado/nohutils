/*
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
import android.os.*;
import android.preference.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.view.inputmethod.*;
import android.widget.*;
import android.widget.TextView.*;
import com.nohkumado.nohutils.foreign.*;
import com.nohkumado.nohutils.view.*;
import java.io.*;
import java.util.*;

/*
 TODO
 -  maybe add a pseudo graphical mode, 
 meaning if a cmd can be done graphically it should be done so....
 - delegate the command queue to a worker thread.... 

 */
@SuppressWarnings({"SameParameterValue", "WeakerAccess", "CanBeFinal", "EmptyMethod", "UnusedReturnValue", "UnusedParameters"})
public class Shell implements ShellI,OnEditorActionListener,OnKeyListener
{
	protected HashMap<String,Object> localVars = new HashMap<>();
	protected ArrayList<String> screenContent = new ArrayList<>();
	protected CommandParserI cmdParser = null;
	//protected  TextView out = null;
	protected  LoggerFrag out = null;
	protected  EditText in = null;

	protected boolean batchMode = false;
	protected boolean logging = false;
	protected BufferedWriter logWriter;
	//protected boolean running = true;
	protected String scanType = null;

	public static final String TAG="Shell";
	protected MsgR2StringI context = null;
	protected ShellTextWatcher watcher = null;

	private static final int MAXLINES = 100;
	int maxLines = 1024;

	protected CommandI actQuestion = null;

	protected ArrayList<String> history = new ArrayList<>();

	protected  int maxHistory = 1024;
	protected int histNavigation = 0;

	private boolean overwrite = false;

	protected ShellI parentShell, childShell = null;

	protected Stack<String> promptStack = new Stack<>();

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
	}

	@Override
	public ShellI cpyCtor()
	{
		Shell cpy = new Shell(context);
		for (String key: localVars.keySet())
		{
			Object value = localVars.get(key);
			//if(value instanceof Cloneable)
			//cpy.set(key,((Cloneable)value).clone());
			//else 
			cpy.set(key, value);

		}
		cpy.set("prompt", "$ ");
		cpy.parentShell = this;
		set("shell", this);

		return cpy;
	}// public Shell()

	//public void setInOut(EditText in, TextView out)
	public void setInOut(EditText in, LoggerFrag out)
	{

		this.out = out;
		this.in = in;
		//Log.d(TAG,"setting in pout "+in+","+out);
		//out.add("setting in pout "+in+","+out);
		if (out == null) screenContent = new ArrayList<>();
		else
		{
			if (screenContent == null) screenContent = new ArrayList<>();
			//screenContent.add(cmdParser.help());
			for (String line: screenContent) out.add(line);
			screenContent = null;
		}//else

		if (in != null)
		{
			in.setOnEditorActionListener(this);
			in.setOnKeyListener(this);
			/* seems i can't remove the listener....
			 if(parentShell != null)
			 {
			 in.remove
			 in.rmOnEditorActionListener(parentShell);
			 in.rmOnKeyListener(parentShell);
			 }*/
		}
		else error("couldn't set action listener");
		if (out == null) error("no output screen....");
		else prompt();
		//debug("done setting in/out");
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

		//debug( "init printing start message");
		if (screenContent != null && screenContent.size() <= 0) print(msg(com.nohkumado.nohutils.R.string.start));
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
		//Log.d(TAG,"in process '"+line+"'");
		String retVal = "";
		ArrayList<CommandI> toWorkOf = cmdParser.parse(line);
		if (toWorkOf.size() > 0)
			for (CommandI aCmd : toWorkOf)
			{
				if (aCmd != null)
				{
					//System.out.println("abpout to exe: "+aCmd);
					retVal = aCmd.execute();
					//TODO pipe ahould interced e here 
					//debug("res\n" + retVal);

					if (!Objects.equals(retVal, "")) print(retVal);
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
			//debug( "res2\n" + retVal);

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
		String prompt;
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
		//set("prompt", prompt);
		return(prompt);
	}

	public void printOnCmdline(final String toPrint)
	{
		if (in != null)
		{
			if (context != null)
				context.runOnUiThread(
					new Runnable()
					{
						@Override
						public void run()
						{
							in.setText(toPrint);
							in.setSelection(toPrint.length());
							setEditTextFocus(in, true);
							in.requestFocus();			
						}//public void run()
					});
		}
	}//end prompt
	public String getCmdline()
	{
		if (in != null) return in.getText().toString();
		return("");
	}
	/** 
	 * setter for promtp 
	 * 
	 * @param p prompt
	 */
	public void prompt(String p)
	{ set("prompt", p); }
	/**

	 exit

	 quit ans close the shell


	 */
	public void exit()
	{
		if (parentShell != null)
		{
			parentShell.rmChild();
			parentShell = null;
		}
		else
		{
			rmRessource("pwd");
			rmRessource("prompt");
		}
		//running = false;
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
	 * @param envname envname
	 */
	public String preference(String envname)
	{
		if (context == null) return envname;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context)context);

		//SharedPreferences prefs = context.getSharedPreferences(
		//	context.getPackageName(), Context.MODE_PRIVATE);
		return(prefs.getString(envname, envname));
	}// public Object ressource(String envname)
	@Override
	public String preference(String locname, Object res)
	{
		if (context == null) return locname;
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences((Context)context);

		//SharedPreferences prefs = context.getSharedPreferences(
		//	context.getPackageName(), Context.MODE_PRIVATE);
		if (res instanceof String) prefs.edit().putString(locname, (String)res).apply();
		else if (res instanceof Integer) prefs.edit().putInt(locname, (Integer)res).apply();
		return null;
	}
	public int intPref(String key)
	{
		int result = 0;
		try
		{
			String value = preference(key);
			if (value != null) result = Integer.valueOf(value);
		}
		catch (NumberFormatException e)
		{
			Log.e(TAG, "no number for " + key + " : " + preference(key)); 
			error("no number for " + key + " : " + preference(key));
		}
		return result;
	}//private void intPref(String key)
	/** 
	 * rmRessources 
	 *
	 * equivalent to the rm environment variables of a shell....
	 * 
	 * @param envname envname
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
	 * @param varname varname
	 * @return the objet of the setting to get
	 */
	public Object get(String varname)
	{
		return(localVars.get(varname));
	}

	public int getInt(String key)
	{
		int result = 0;
		try
		{
			String value = preference(key);
			if (value != null) result = Integer.valueOf(value);
		}
		catch (NumberFormatException e)
		{
			Log.e(TAG, "no number for " + key + " : " + preference(key)); 
			error("no number for " + key + " : " + preference(key));
		}
		return result;
	}//public int getInt(String varname)

	public Map<String,Object> getAll()
	{
		return(localVars);
	}

	/** 
	 * ressources 
	 *
	 * equivalent to the environment variables of a shell....
	 * 
	 * @param varname varname
	 */
	public Object set(String varname, Object value)
	{
		if (varname == null) return(value);
		return(localVars.put(varname, value));
	}
	/** 
	 * prototype for a help function 
	 * 
	 * @return help
	 */
	public String help()
	{

		return(cmdParser.help());
	}//end help
	/** 
	 * returns the message associated with a token
	 * resourceId
	 * @param  resourceId resourceId
	 * @return msg
	 */
	public String msg(int resourceId)
	{
		if (context == null) return "no context";

		return(context.getResources().getString(resourceId));
	}// public String msg(String m)
	/** 
	 * returns the message associated with a token
	 * 
	 * @param m msg
	 * @return msg
	 */
	public String msg(String m)
	{
		if (context == null)
		{
			print("no context.... returning " + m + " litterally");
			return m; 
		}

		/*
		 if(m.equals(result))
		 {

		 try
		 {
		 int resourceId = context.getResources().getIdentifier(m, "strings", "com.nohkumado.nohutils");
		 return(context.getResources().getString(resourceId));
		 }
		 catch (Resources.NotFoundException e)
		 { error("not found message : " + m);}
		 }
		 */
		return(context.msg(m));
	}// public String msg(String m)
	/**
	 executeCommands

	 @param toWorkOf

	 cycle over list and execute the stored commands, i think not thread safe...
	 */
	private void executeCommands(ArrayList<CommandI> toWorkOf)
	{
		if (toWorkOf.size() > 0)
			for (CommandI aCmd : toWorkOf)
			{
				if (aCmd != null)
				{
					//debug("abpout to exe: "+aCmd);
					String retVal = aCmd.execute();
					//debug( "res3\n" + retVal);

					if (!Objects.equals(retVal, "")) print(retVal);
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
		/* now initialise the loop that will read from the inputStream until
		 *exhaustion */
		if (in == null)
		{
			error("oyoyoy??? no input field given....");
			return "";
		}
		String inputLine = "";
		//TODO we can't wait here.... we should put the question in the prompt after saving a backup, 
		//and notify the eventhandler callback that we are waiting for an answer....
		//means asynchronous working... we have to store the next workflow until we get an answer...

		if (scanType != null && scanType.equals("numeric"))
		{
			print(msg(com.nohkumado.nohutils.R.string.numeric_asked));
			boolean isNumeric;
			do
			{
				try
				{
					inputLine = Double.parseDouble(in.getText().toString()) + "";
					isNumeric = true;
				}// try
				catch (InputMismatchException e)
				{
					print(msg(com.nohkumado.nohutils.R.string.not_a_numeric) + "\n>");
					isNumeric = false;
				}// catch(InputMismatchException e)
			}
			while(!isNumeric);
			scanType = null;
		}// if(scanType != null && scanType.equals("numeric"))
		else if (scanType != null && scanType.equals("int"))
		{
			print(msg(R.string.question_is_int));
			boolean isNumeric;
			do
			{
				try
				{
					inputLine = Integer.parseInt(in.getText().toString()) + "";
					isNumeric = true;
				}// try
				catch (InputMismatchException e)
				{
					print(msg(com.nohkumado.nohutils.R.string.not_a_int) + "\n>");
					isNumeric = false;
				}// catch(InputMismatchException e)
			}
			while(!isNumeric);
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
	 * @param actionId, actionId
	 * @param event event
	 */
	@Override
	public boolean onEditorAction(TextView tw, int actionId, KeyEvent event)
	{
		//debug("editoraction[" + tw.getText() + "] id : " + actionId + " ev " + event);

		//if (tw != out) debug( "wrong source");
		//else debug( "good source");

		if ((event != null && event.getAction() == KeyEvent.ACTION_DOWN))
		{}
		else if ((event == null && actionId == EditorInfo.IME_NULL) || (event != null && event.getAction() == KeyEvent.ACTION_UP))
		{
			//Log.d(TAG,"event : "+actionId+" , "+event);
			reactToEnter(tw);
		}
		else
		{
			error("ooooyy??? onEditorAction unknown event...n :" + actionId + " vs ime: " + EditorInfo.IME_NULL + " ev: " + event);
			Log.e(TAG, "onEditorAction unknown event... " + actionId + " vs ime: " + EditorInfo.IME_NULL + " ev: " + event);
		}

		return true;
	}//public boolean onEditorAction(TextView tw, int actionId, KeyEvent event)

	private void reactToEnter(TextView tw)
	{
		histNavigation = 0;
		//debug( "hit the enter key... calling read");
		String incoming = tw.getText().toString().trim();
		//debug("hit the enter key... got " + incoming);
		//removing prompt from incoming line

		String lastprompt = prompt();
		if (lastprompt != null) incoming = incoming.replace(lastprompt.trim(), "").trim();

		if (incoming.length() > 0)  history.add(incoming);
		if (history.size() > maxHistory)
			while (history.size() > maxHistory) history.remove(0);

		//debug("read extracted " + incoming);
		if (actQuestion != null)
		{
			/*StringBuilder sb = new StringBuilder();
			 sb.append("diverting to actquestion ").append(actQuestion);
			 sb.append(" actp:"+get("prompt"));
			 */
			if (!promptStack.empty()) prompt(promptStack.pop());
            //sb.append(" newp:"+get("prompt"));
            //debug( sb.toString());
			//debug("diverting  question to " + actQuestion);
			CommandI toExe = actQuestion;
			//TODO eventually her we should mitigate if its a keylistener we should rescind
			//from killing it, but we needa mechanism to tell that we don't need forwarding
			//anymore... maybe we should reask a dummy question.... in the lsitener....
			//to reset the actquestion

			actQuestion = null;
			//setting to 0 beforehand, since the parsing could arise new questions...
			toExe.parse(incoming);
			toExe.execute();
			printOnCmdline(prompt());
		}
		else if ("".equals(incoming)) debug("incoming was empty...");
		else 
		{
			//String logEntry = prompt() + " " + incoming;
			//debug("should print out '" + incoming + "'");

			print(incoming);
			//debug( "proceeding to parse");

			ArrayList<CommandI> toWorkOf = cmdParser.parse(incoming);
			//debug("cmd list " + toWorkOf);
			executeCommands(toWorkOf);
		}//else
	}//private void reactToEnter(TextView tw)


	/**------------------------------------------------------------------
	 * onKey
	 * @param v, the view the event happened
	 * @param keyCode, keyCode
	 * @param event event
	 */

	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) 
	{
		//sous traite l'evenement si pertinent
		boolean result = false;
		//if (actQuestion != null && actQuestion instanceof KeyPressListener) debug( "forwarding keyevent to "+actQuestion);
		if (actQuestion != null && actQuestion instanceof KeyPressListener) 
			result = ((KeyPressListener)actQuestion).onKey(v, keyCode, event);
		//if (result) debug("actQuestion handled the keypress");

		if (result) return false;

		//debug( "hit key :" + keyCode + " stamp " + event.getEventTime());
		if (keyCode == KeyEvent.KEYCODE_DPAD_UP && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			histNavigation++;
			int actPost = history.size() - histNavigation;
			if (actPost < 0)
			{
				context.playSound();
				actPost = 0;
				histNavigation = history.size();
			}
			//TODO beware, if there are expansion commands in the prompt what then??
			if (history.size() > actPost) printOnCmdline(prompt() + " " + history.get(actPost));
			else printOnCmdline(prompt());
		}
		else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			histNavigation--;
			int actPost = history.size() - histNavigation;
			String content;
			if (actPost >= history.size())
			{
				if (actPost != history.size())
				{
					context.playSound();
					histNavigation = 0;	
				}//if (actPost != history.size())
				content = "";
			}
			else content = history.get(actPost);
			//TODO beware, if there are expansion commands in the prompt what then??
			printOnCmdline(prompt() + " " + content);
		}
		else if (keyCode == KeyEvent.KEYCODE_TAB && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			//tabcount++;
			//debug("hit tab key at " + event.getEventTime());

			//if (tabcount == 1)
			//{
			if (v instanceof EditText)
			{
				EditText tw = (EditText)v;
				String incoming = tw.getText().toString().trim();
				String lastprompt = prompt();
				if (lastprompt != null) incoming = incoming.replace(lastprompt.trim(), "").trim();

				//debug("tabkey, parsing: '" + incoming + "'");
				ArrayList<CommandI> toWorkOf = cmdParser.parse(incoming);
				//debug("retrieved cmds :" + toWorkOf);
				if (toWorkOf.size() > 0)
				{
					CommandI lastCmd = toWorkOf.get(toWorkOf.size() - 1);
					StringBuilder corrected = new StringBuilder();
					corrected.append(prompt());
					if (!"".equals(lastCmd.name())) corrected.append(lastCmd.name());
					else corrected.append(incoming);
					String args = lastCmd.expand();
					if (!"".equals(args)) corrected.append(" ").append(args);
					//debug("found cmd "+lastCmd+" changed line to "+corrected.toString());
					printOnCmdline(corrected.toString());
				}
				else
				{
					print("couldn't parse " + incoming);
				}
			}
			else
				print("uhm tab key but not from the edittext??");
			//int tabcount = 0;
		}
		else if (keyCode == KeyEvent.KEYCODE_INSERT && event.getAction() == KeyEvent.ACTION_DOWN)
		{
			overwrite = !overwrite;
			if (overwrite) print(msg(R.string.shell_changetooverwrite));
			else  print(msg(R.string.shell_changetoinsert));
 			setInputMode(overwrite);
		}
		//}
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
		//debug("print:"+something);

		something = something.trim();
		//debug("print after trim '"+something+"'");

		/*if (screenContent == null)
		 {error( "oyoyoy??? no history to print " + something + "!!"); return; }
		 if (something.length() > 0) 
		 {
		 String[] splitted = something.split("\n");
		 for (String line: splitted) 
		 {
		 screenContent.add(line);
		 //debug("l:'"+line+"'");
		 }
		 }
		 //debug( "maxlines = " + maxLines);
		 if (screenContent.size() > maxLines) 
		 while (screenContent.size() > maxLines) screenContent.remove(0);
		 */
		if (something.length() > 0) 
		{
			log(something);
			String[] splitted = something.split("\n");
			for (String line: splitted) 
			{
				if (out == null) 
				{
					//Log.e(TAG,"oyoyoy??? no screen to print " + line + "!!");
					screenContent.add(line);
				}
				else
					out.add(line);
				//debug("l:'"+line+"'");
			}
		}
		//determine visible part of screen
		/*int height    = out.getHeight();
		 int scrollY   = out.getScrollY();
		 Layout layout = out.getLayout();

		 int firstVisibleLineNumber = 0;
		 int lastVisibleLineNumber  = 0;
		 if (layout != null)
		 {
		 firstVisibleLineNumber = layout.getLineForVertical(scrollY);
		 lastVisibleLineNumber  = layout.getLineForVertical(scrollY + height);
		 //debug("first visinble : "+firstVisibleLineNumber+" last visible "+lastVisibleLineNumber);
		 }
		 //else debug("no layout to determine size.... ");

		 //debug("height : "+height);


		 //encheck for lines

		 StringBuilder sb = new StringBuilder();

		 if (height > maxLines || height == 0)
		 for (String s : screenContent)
		 {
		 //debug("adding line '"+s+"'");
		 sb.append(s);
		 sb.append("\n");
		 }
		 else for (int i = Math.max(screenContent.size() - height, 0); i < screenContent.size(); i++)
		 {
		 String s = screenContent.get(i);
		 //debug("adding line '"+s+"'");
		 sb.append(s);
		 sb.append("\n");
		 }
		 //debug( "complete text '" + sb.toString() + "'");
		 if (out == null)
		 {
		 error( sb.toString());
		 }
		 else 
		 {
		 Log.d(TAG,"called add on out with "+sb);
		 out.add(sb.toString());
		 //out.setText(Html.fromHtml(sb.toString()));
		 //getContext().runOnUiThread(new PrintOnTextView(out,sb));

		 }
		 */
	}//protected void print(String something)
	public void log(String something)
	{
		if (something == null) something = "";
		if (something.length() > 0) 
		{
			try
			{
				if (logging)
				{
					logWriter.write(something);
					if (!something.endsWith("\n")) logWriter.write("\n");
				}
			}
			catch (IOException e)
			{
				error("no logging " + e);
			}
		}//if (something.length() > 0) 
	}//protected void log(String something)

	//------------------------------------------------------------------
	//public void setEditTextFocus(EditText searchEditText, boolean isFocused)
	public void setEditTextFocus(EditText searchEditText, boolean isFocused)
	{
		//boolean isFocused = true;
		searchEditText.setCursorVisible(isFocused);
		searchEditText.setFocusable(isFocused);
		searchEditText.setFocusableInTouchMode(isFocused);
		//if (isFocused) {
		searchEditText.requestFocus();
		//} else {
		//	InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE); 
		//	inputManager.hideSoftInputFromWindow(searchEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS );
		//}
	} 
	//------------------------------------------------------------------
	public void error(String aMessage) 
	{
		Log.e(TAG, "Error:" + aMessage);
		print("<font color=\"red\">" + aMessage + "</font>");
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
		//Log.d(TAG,"asking "+question+" from "+caller);
		actQuestion = caller;
		//print("should ask : "+question+", p:"+prompt());
		//debug("should ask : "+question+", p:"+prompt());

		if (question.length() > 0) pushPrompt(question + " ");
		prompt();
		//print("prompt now "+prompt());
		//print(question + " ");
		//return(read());
	}

	private void pushPrompt(String question)
	{

		if (!question.equals(get("prompt")))
		{
			//debug("pushing " + get("prompt") + " in favour of " + question);
			promptStack.push(prompt());
			prompt(question);
		}
		else
		{
			debug("rejecting new prompt, its the same " + get("prompt") + " in favour of " + question);
		}
	}//public String ask()
	/**
	 * in fact read a line...
	 * 
	 * @param question a string to issue to prompts for an answer
	 * @param options more data, e.g. default value, a range selection, captions for range selection,  
	 * @return user inpoout
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
			@SuppressWarnings("unchecked") List<String> captions = (List<String>) options.get("captions");

			List<String> selectCopy = new ArrayList<>();
			List<String> captionsCopy = new ArrayList<>();
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
					selectCopy.add(select.get(i).toString());
					if (captions != null && captions.size() > i)captionsCopy.add(captions.get(i));
					else captionsCopy.add("");
				}//if(!select.get(i).equals(defVal))
			}// for(int i = 0 ; i < select.length; i++)

			StringBuilder questionBuilder = new StringBuilder(question);
			for (int i = 0; i < selectCopy.size(); i++)
			{
				questionBuilder.append("[").append(i).append("] : ").append(selectCopy.get(i));
				String cap = "" + captionsCopy.get(i);
				if (cap.length() > 0) questionBuilder.append(" (").append(cap).append(")");
				questionBuilder.append("\n>");
			}// for(int i = 0 ; i < select.length; i++)
			question = questionBuilder.toString();

			int index;
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
						index = Integer.valueOf(answer);
					}// try
					catch (NumberFormatException e)
					{
						System.out.println("no valid number: " + answer); 
						index = -1;
					}// catch(NumberFormatException e)
				}//else
			}// do
			while(index < 0  || index >= select.size()); 
			//answer = selectCopy.get(index) + "";
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
	 * @param baseName  baseName
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
	//public boolean isRunning()
	//{ return(running);}
	/**
	 *
	 *  feedCmds
	 *
	 * @param cmds cmds
	 * @return list
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
			//result = (int) (out.getWidth() / out.getTextSize());
			result = out.getDisplayWidth();
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
		//debug("listing asset files");
		//listAssetFiles(""); 
		//debug("trying to open asset "+name);
		return context.getResources().getAssets().open(name);
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
					//debug("asset: " + path + "/" + file);
					if (!listAssetFiles(path + "/" + file))
						return false;
				}
			}
			/*else
			 {
			 // This is a file
			 // TODO: add file name to an array list
			 //debug( "asset single file : " + path);
			 }*/
		}
		catch (IOException e)
		{
			return false;
		}
		return true; 
	} 

	public void beep()
	{
		if (context != null)		context.playSound();
	}

	@Override
	public void endQuestion()
	{
		actQuestion = null;
		//debug("stopping forwarding");
	}
	@Override
	public void setInputMode(boolean overwrite)
	{
		if (watcher == null) watcher = new ShellTextWatcher();
		watcher.setOverwrite(overwrite);
		if (in != null) in.addTextChangedListener(watcher);
	}
	@Override
	public void restoreState(Bundle savedInstanceState)
	{
		//debug("restoring state");		
		if (savedInstanceState == null)
		{
			error("no bundle to restore from");
			return;
		}
		scanType = savedInstanceState.getString("scanType");
		batchMode = savedInstanceState.getBoolean("batchMode");
		//running = savedInstanceState.getBoolean("running");
		overwrite = savedInstanceState.getBoolean("overwrite");
		maxLines = savedInstanceState.getInt("maxLines");
		maxHistory = savedInstanceState.getInt("maxHistory");
		histNavigation = savedInstanceState.getInt("histNavigation");
		//screenContent = savedInstanceState.getStringArrayList("screenContent");
		history = savedInstanceState.getStringArrayList("history");
		ArrayList<String> varnames = savedInstanceState.getStringArrayList("localVarNames");
		ArrayList<String> varval = savedInstanceState.getStringArrayList("localVarValues");

		if (localVars == null)
		{
			error("no localVars to restore to....");
			return;
		}
		int index = 0;
		assert varnames != null;
		for (String name : varnames)
		{
			assert varval != null;
			localVars.put(name, varval.get(index));
			index++;
		}
		print("restored");
	}

	@Override
	public void saveState(Bundle savedInstanceState)
	{
		//debug("saving state");
		if (savedInstanceState == null)
		{
			error("no bundle to save to");
			return;
		}
		savedInstanceState.putString("scanType", scanType);
		savedInstanceState.putBoolean("batchMode", batchMode);
		//savedInstanceState.putBoolean("running", running);
		savedInstanceState.putBoolean("overwrite", overwrite);
		savedInstanceState.putBoolean("overwrite", overwrite);
		savedInstanceState.putInt("maxLines", maxLines);
		savedInstanceState.putInt("maxHistory", maxHistory);
		savedInstanceState.putInt("histNavigation", histNavigation);
		//savedInstanceState.putStringArrayList("screenContent", screenContent);
		savedInstanceState.putStringArrayList("history", history);
		ArrayList<String> varnames = new ArrayList<>();
		ArrayList<String> varval = new ArrayList<>();
		for (String name : localVars.keySet())
		{
			if (localVars.get(name) instanceof String)
			{
				varnames.add(name);
				varval.add((String)localVars.get(name));
			}
		}
		savedInstanceState.putStringArrayList("localVarNames", varnames);
		savedInstanceState.putStringArrayList("localVarValues", varval);
	}
	@Override
	public void setContext(MsgR2StringI c)
	{
		context = c;
		//for(CommandI cmd : commands)
		//{

		//	}//for(CommandI cmd : commands)

	}//public void setContext(MsgR2StringI c)
	public MsgR2StringI getContext()
	{
		return context;
	}
	@Override
	public void clearCmds()
	{
		cmdParser.clearCmds();
	}
	@Override
	public void parseMode(String p0)
	{
		cmdParser.parseMode(p0);
	}
	@Override
	public void setChild(ShellI actShell)
	{
		// seems no need for this if (childShell != null) childShell.setInOut(null, null);

		childShell = actShell;
		if (childShell != null) 
		{
			//debug("diverting in out ot childshell");
			childShell.setInOut(in, out); 
		}//if (childShell != null)
		debug("done");
	}//public void setChild(ShellI actShell)
	/**
	 destroy the reference to the child shell, and re-establish the listeners, 
	 responsible for the event loop of the shell to this one

	 */
	@Override
	public ShellI rmChild()
	{
		ShellI result = childShell;
		//seems there's no need to remove the listener if (childShell != null) childShell.setInOut(null, null);
		childShell = null;
		if (in != null)
		{
			in.setOnEditorActionListener(this);
			in.setOnKeyListener(this);
		}
		prompt();
		return result;
	}

	@Override
	public void setParent(ShellI s)
	{
		parentShell = s;
		if (parentShell != null) 
		{
			//debug("setting child to subshell");
			parentShell.setChild(this);
		}
	}//public void setParent(ShellI s)
	@Override
	public void startlog(String logFileName)
	{
	    File logFile = new File(logFileName);
		//if(logFile.canWrite()) 
		//{
		logging = true;
		try
		{
			logWriter = new BufferedWriter(new FileWriter(logFile));
			//print("openend for logging "+logFileName);
		}
		catch (IOException e)
		{
			error("couldn't open logfile " + e);
			logging = false;
		}//catch (IOException e)
		//}//if(logFile.canWrite()) 
		//else error("can't write to "+logFileName);
	}
	@Override
	public void closelog()
	{
		try
		{
			if (logWriter != null) logWriter.close();
			//print("closed logging ");
		}
		catch (IOException e)
		{
			error("couldn't close logfile " + e);
		}
		logging = false;
	}//public void closelog()

}//public class Shell
