/**
 * 
 */
package com.nohkumado.nohutils;

import java.math.*;
import java.util.*;
import android.util.*;

/**
 * @author bboett
 * 
 */
public class Tester
{
	public static final String TAG = "Tester";
	protected static String testName = "Tester";
	protected ShellI shell = null;
	/** 
	 * 
	 * 
	 * @param toCheck 
	 */
	public boolean doTrans(boolean result, String msg)
	{
		if (result == true)
		{
			StringBuilder sb = new StringBuilder();
			sb.append(testName);
			if (msg != null && msg.length()>0) sb.append(msg);
			sb.append(" ERROR");
			
			if(shell != null) shell.print(sb.toString());
			else Log.e(TAG, sb.toString());
		}//# if(toCheck.status())
		return(!result);
	}// public void doTrans(Item toCheck)
	/** 
	 * 
	 * 
	 * @param toCheck 
	 */
	public boolean doTrans(ReturnValue status)
	{
		return doTrans(!status.status(), status.report());
	}// public void doTrans(Item toCheck)
	/** 
	 * 
	 * 
	 * @param m 
	 */
	public static void print(String m)
	{
		Log.d(TAG, m);
	}// public void print(String m)
}// public class Tester
