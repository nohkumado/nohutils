/*
 * 
 */
package eu.nohkumado.nohutils.test;

import android.util.Log;

import eu.nohkumado.nohutils.ReturnValue;

/**
 * @author bboett
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public abstract class UtilsTester {
  private final static String TAG = "UT";
  protected StringBuilder log = new StringBuilder();
  protected boolean immediate = false;
  protected StringBuilder error;
  protected static String testName = "UtilsTester";

  public String log() {
    if (log != null) return log.toString();
    return "";
  }

  abstract public boolean test() ;

  /**
   * @param result result
   */
  public boolean doTrans(boolean result, String msg) {
    if (result) {
      if (msg == null || msg.equals("")) {
        error(testName);
      }// if(msg != null || !msg.equals(""))
      else {
        error(testName + " " + msg);
      }// else
    }//# if(toCheck.status())
    return (!result);
  }// public void doTrans(Item toCheck)

  /**
   * @param result result
   */
  public boolean doTrans(boolean result, StringBuilder msg) {
    if (result) {
      if (msg == null || msg.length() <= 0) {
        error(testName);
      }// if(msg != null || !msg.equals(""))
      else {
        error(testName + " " + msg);
      }// else
    }//# if(toCheck.status())
    return (!result);
  }// public void doTrans(Item toCheck)

  /**
   * @param status status
   */
  public boolean doTrans(ReturnValue status) {
    return doTrans(status.status(), status.report());
  }// public void doTrans(Item toCheck)

  /**
   * print
   *
   * @param m msg
   */
  public void print(String m) {
    Log.d(TAG, "print appendung to " + log + " m");
    if (immediate) Log.d(TAG, m);
    else log.append(m).append("\n");
  }// public void print(String m)

  /**
   * error
   *
   * @param m msg
   */
  public void error(String m) {
    Log.d(TAG, "err appending to " + log + " " + m);
    if (immediate) Log.e(TAG, m);
    else log.append("ERROR:").append(m).append("\n");
  }// public void print(String m)

  /**
   * error
   *
   * @param m msg
   */
  public StringBuilder error(StringBuilder m) {
    if (immediate) Log.e(TAG, m.toString());
    else log.append("ERROR:").append(m).append("\n");
    return m;
  }// public void print(String m)

  public boolean assertTrue(boolean cond, String msg) {
    if (cond) return cond;
    error(msg);
    return cond;
  }

  public boolean assertFalse(boolean cond, String msg) {
    return assertTrue(!cond, msg);
  }
  public boolean assertTrue(boolean cond) {
    return cond;
  }

  public boolean assertFalse(boolean cond) {
    return assertTrue(!cond);
  }
  public boolean assertNull(Object cond, String msg) {
    return assertTrue((cond == null), msg);
  }

  public boolean assertNotNull(Object cond, String msg) {
    return assertTrue((cond != null), msg);
  }
  public boolean assertNull(Object cond) {
    return assertTrue(cond == null);
  }

  public boolean assertNotNull(Object cond) {
    return assertTrue(cond != null);
  }

  public boolean assertEquals(Object one, Object two)
  {
    return assertNull(one) && assertNull(two) || !(assertNull(one) || assertNull(two)) && assertTrue((one.equals(two)));
  }

  public boolean assertNotEquals(Object one, Object two)
  {
    return !assertEquals(one,two);
  }

  public boolean assertArrayEquals(Object[] one, Object[] two) {
    if (assertNull(one) && assertNull(two)) return true;
    if (assertNull(one) || assertNull(two)) return false;
    if (assertFalse(one.length == two.length)) return false;
    for (int i = 0; i < one.length; i++)
      if (assertNotEquals(one[i], two[i])) return false;
    return true;

  }
}// public class UtilsTester
