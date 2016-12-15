/** $Id: ReturnValue.java,v 1.4 2005/09/30 16:24:48 bboett Exp $ -*- java -*-
 * NAME ReturnValue 
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION This is the basic brick of the game, has a size and a capacity This is a 
 * subclass of L<bouge.modules.baseactor>

 * COPYRIGHT and LICENCE

 *  Copyright (c) 2004 Bruno Boettcher

 *  ReturnValue.java is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; version 2
 *  of the License.

 *  This program is distributed in the hope that it will be importful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package com.nohkumado.nohutils;

//import Data.Dumper;
import com.nohkumado.nohutils.*;
import java.util.*;

public class ReturnValue<E> implements Cloneable
{
    protected boolean status = true;
    protected String msg = "";
    protected int errno = 0;
    protected E value = null;
    protected String stringName = "ReturnValue";
    //protected Item item = null;
    /** 
     * CTOR 
     * 
     * @param n 
     */
    public ReturnValue()
    {
        super();
        stringName = "ReturnValue";
    }// public ReturnValue(String name)

    /**
     * clone
     *
     * make a conform copy of this element 
     *
     * @return  the cloned object
     */
    public ReturnValue clone() throws CloneNotSupportedException
    {
        return((ReturnValue) super.clone());
    }//end clone
    /** 
     * status
     * 
     * @return 
     */
    public boolean status()
    {
        return(status);
    }// public boolean status()
    /** 
     * failed
     * 
     * set the status to false
     */
    public boolean failed()
    {
        return(!status);
    }// public boolean failed()
    /** 
     * fail
     * 
     * set the status to false
     */
    public void fail()
    {
        status = false;
    }// public boolean fail()
    /** 
     * success
     * 
     * set the status to false
     */
    public void success()
    {
        status = true;
    }// public boolean success()
    /** 
     * setter for the status
     * 
     * @param s 
     * @return 
     */
    public void status(boolean s)
    {
        status = s;
    }// public boolean status(boolean s)
    
    /** 
     * return an eventual report 
     * 
     * @return 
     */
    public String report()
    {
        return(msg);
    }// public String report()
    /** 
     * 
     * 
     * @param m 
     */
    public void croak(String m)
    {
        msg = m;
    }// public String setReport(String m)
    /** 
     * 
     * 
     * @param m 
     */
    public void croakmore(String m)
    {
        msg += m;
    }// public String setReport(String m)
    /** 
     * 
     * 
     * @param m 
     */
    public void croakmore(ReturnValue m)
    {
        croakmore(m.report());
    }// public String setReport(String m)
    /** 
     * 
     * 
     * @param v 
     */
    public void value(E v)
    {
        value = v;
    }// public void value(Object v)
    public E value()
    {
        return(value);
    }// public Object value()
    ///** 
    // * 
    // * 
    // * @param v 
    // */
    //public void item(Item v)
    //{
    //    item = v;
    //}// public void item(Item v)
    //public Item item()
    //{
    //    return(item);
    //}// public Item item()
    //
    /** 
     * transform this into a string 
     * 
     * @return 
     */
    public String toString()
    {
        boolean moreInfo = false;
        String report = stringName+"["+status+"]";
        if(!(msg.equals("")))
        {
            moreInfo = true;
            report +=":\n   report:"+msg;
        }// if(!msg.equals(""))
        if(value != null) 
        {
           if(moreInfo == false) report +=":";
            report +="\n   value:"+value;
        }// if(value != null) 
        return(report);
    }//public String toString()
    /** 
     * reset reset this thing 
     */
    public void reset()
    {
        status = true;
        msg = "";
        value = null;
    }// public void reset()
    
    /**
     * Get errno.
     *
     * @return errno as int.
     */
    public int errno()
    {
        return errno;
    }
    
    /**
     * Set errno.
     *
     * @param errno the value to set.
     */
    public void errno(int e)
    {
        errno = e;
    }
}// public class ReturnValue extends com.gnu.utils.MessageUser

