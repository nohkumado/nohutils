/**
 * 
 */
package com.nohkumado.nohutils.test;

import com.nohkumado.nohutils.*;
import java.math.*;
import java.util.*;
import android.util.*;

/**
 * @author bboett
 * 
 */
public class UtilsTester
{
  private final static String TAG ="UT";
  protected StringBuilder log = new StringBuilder();
  protected boolean immediate = false;

  protected static String testName = "UtilsTester";
  /** 
   * 
   * 
   * @param toCheck 
   */
  public boolean doTrans(boolean result, String msg)
  {
    if (result == true)
    {
      if (msg == null || msg.equals(""))
      {
        error(testName);
      }// if(msg != null || !msg.equals(""))
      else
      {
        error(testName + " " + msg);
      }// else
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
   * print 
   * 
   * @param m 
   */
  public void print(String m)
  {
    if (immediate) Log.d(TAG, m);
    else log.append(m).append("\n");
  }// public void print(String m)
  /** 
   * error 
   * 
   * @param m 
   */
  public void error(String m)
  {
    if (immediate) Log.e(TAG, m);
    else log.append("ERROR:").append(m).append("\n");
  }// public void print(String m)

}// public class UtilsTester
