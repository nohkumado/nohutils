/*
 * 
 */
package com.nohkumado.nohutils.test;

import com.nohkumado.nohutils.*;

import android.util.*;

/**
 * @author bboett
 * 
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class UtilsTester
{
  private final static String TAG ="UT";
  protected StringBuilder log = new StringBuilder();
  protected boolean immediate = false;

  protected static String testName = "UtilsTester";
  /** 
   * 
   * 
   * @param result result
   */
  public boolean doTrans(boolean result, String msg)
  {
    if (result)
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
   * @param result result
   */
  public boolean doTrans(boolean result, StringBuilder msg)
  {
    if (result)
    {
      
      if (msg == null || msg.length() <= 0)
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
   * @param status status
   */
  public boolean doTrans(ReturnValue status)
  {
    return doTrans(!status.status(), status.report());
  }// public void doTrans(Item toCheck)
  /** 
   * print 
   * 
   * @param m  msg
   */
  public void print(String m)
  {
    if (immediate) Log.d(TAG, m);
    else log.append(m).append("\n");
  }// public void print(String m)
  /** 
   * error 
   * 
   * @param m msg
   */
  public void error(String m)
  {
    if (immediate) Log.e(TAG, m);
    else log.append("ERROR:").append(m).append("\n");
  }// public void print(String m)
  /** 
   * error 
   * 
   * @param m msg
   */
  public StringBuilder error(StringBuilder m)
  {
    if (immediate) Log.e(TAG, m.toString());
    else log.append("ERROR:").append(m).append("\n");
    return m;
  }// public void print(String m)
  
}// public class UtilsTester
