/**
 * 
 */
package com.nohkumado.nohutils;

import java.math.*;
import java.util.*;

/**
 * @author bboett
 * 
 */
public class Tester
{
    protected static String testName = "Tester";
    /** 
     * 
     * 
     * @param toCheck 
     */
    public static boolean doTrans(boolean result, String msg)
    {
        if(result == true)
        {
            if(msg == null || msg.equals(""))
            {
            System.out.println(testName+" ERROR");
            }// if(msg != null || !msg.equals(""))
            else
            {
                System.out.println(testName+" ERROR:"+msg);
            }// else
        }//# if(toCheck.status())
        return(!result);
    }// public void doTrans(Item toCheck)
    /** 
     * 
     * 
     * @param toCheck 
     */
    public static boolean doTrans(ReturnValue status)
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
        System.out.println(m);
    }// public void print(String m)

}// public class Tester
