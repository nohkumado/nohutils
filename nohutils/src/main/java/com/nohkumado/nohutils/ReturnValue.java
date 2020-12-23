/*NAME ReturnValue
 *
 * AUTHOR Bruno Boettcher <bboett at adlp.org> 
 *
 * SEE ALSO no docu at the moment 
 *
 * DESCRIPTION This is the basic brick of the game, has a size and a capacity This is a 

 * COPYRIGHT and LICENCE

 *  Copyright (c) 2004 Bruno Boettcher

 *  ReturnValue.java is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; version 2
 *  of the License.

 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.

 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
*/

package com.nohkumado.nohutils;

public class ReturnValue<E> implements Cloneable
{
    protected boolean status = true;
    protected String msg = "";
    protected int errno = 0;
    protected E value = null;
    protected String stringName;
    /**
     * CTOR 
     *
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
    public ReturnValue<E> clone() throws CloneNotSupportedException, ClassCastException {
        return (ReturnValue<E>) super.clone();
    }//end clone
    /** 
     * status
     * 
     * @return  status
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
     * @param s status to set
     */
    public void status(boolean s)
    {
        status = s;
    }// public boolean status(boolean s)
    
    /** 
     * return an eventual report 
     * 
     * @return report
     */
    public String report()
    {
        return(msg);
    }// public String report()
    /** 
     * 
     * 
     * @param m msg
     */
    public void croak(String m)
    {
        msg = m;
    }// public String setReport(String m)
    /** 
     * 
     * 
     * @param m msg
     */
    public void croakmore(String m)
    {
        msg += m;
    }// public String setReport(String m)
    /** 
     * 
     * 
     * @param m msg
     */
    public void croakmore(ReturnValue<E> m)
    {
        croakmore(m.report());
    }// public String setReport(String m)
    /** 
     * 
     * 
     * @param v value
     */
    public void value(E v)
    {
        value = v;
    }// public void value(Object v)
    public E value()
    {
        return(value);
    }// public Object value()
    /**
     * transform this into a string 
     * 
     * @return toString
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
           if(!moreInfo) report +=":";
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
     * @param e errno the value to set.
     */
    public void errno(int e)
    {
        errno = e;
    }
}// public class ReturnValue extends com.nohkumado.utils.MessageUser
