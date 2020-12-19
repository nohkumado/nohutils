/*
 * 
 */
package com.nohkumado.nohutils;

import android.util.*;

/**
 * @author bboett
 * 
 */
@SuppressWarnings("WeakerAccess")
public class Tester
{
	public static final String TAG = "Tester";
	//protected static String testName = "Tester";
	protected ShellI shell;
	protected boolean doAlsoLogOutput = false;

	public Tester(ShellI s)
	{
		shell = s;
	}

	public void setLogOutput(boolean doAlsoLogOutput)
	{
		this.doAlsoLogOutput = doAlsoLogOutput;
	}
	/** 
	 * 
	 * 
	 * @param result  boolean
	 */
	public boolean doTrans(boolean result, String msg)
	{
		if (result)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(TAG);
			if (msg != null && msg.length() > 0) sb.append(msg);
			sb.append(" ERROR");

			if (shell != null) 
			{
				shell.print(sb.toString());
				if (doAlsoLogOutput)Log.e(TAG, sb.toString());
			}
			else Log.e(TAG, sb.toString());

		}//# if(toCheck.status())
		return(!result);
	}// public void doTrans(Item toCheck)
	/** 
	 * 
	 * 
	 * @param status status
	 */
	public boolean doTrans(ReturnValue status)
	{
		return doTrans(!status.status(), status.report());
	}// public void doTrans(Item toCheck)
	/** 
	 * 
	 * 
	 * @param m msg
	 */
	public static void print(String m)
	{
		Log.d(TAG, m);
	}// public void print(String m)
}// public class Tester
