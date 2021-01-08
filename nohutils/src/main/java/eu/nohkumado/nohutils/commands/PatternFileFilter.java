/* $Id: PatternFileFilter.java,v 2.1.1.1 2002/08/27 08:18:00 bboett Exp $ -*- java -*-
 * Copyright (C) 1997 Bruno Böttcher
 * bboett@adlp.org
 * http://bboett.free.fr/~bboett
 *
 * All rights reserved
 *
 * Permission to use, copy, modify and distribute this material for
 * any purpose and without fee is hereby granted, provided that the
 * above copyright notice and this permission notice appear in all
 * copies, and that my name is not used in advertising or publicity 
 * pertaining to this material without my specific, prior written 
 * permission 
 *
 * I MAKE NO REPRESENTATIONS AND EXTENDS NO WARRANTIES,
 * EXPRESS OR IMPLIED, WITH RESPECT TO THE SOFTWARE, INCLUDING, BUT
 * NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 * FITNESS FOR ANY PARTICULAR PURPOSE, AND THE WARRANTY AGAINST
 * INFRINGEMENT OF PATENTS OR OTHER INTELLECTUAL PROPERTY RIGHTS.  THE
 * SOFTWARE IS PROVIDED "AS IS", AND IN NO EVENT SHALL I  BE LIABLE FOR 
 * ANY DAMAGES, INCLUDING ANY LOST PROFITS OR OTHER INCIDENTAL OR 
 * CONSEQUENTIAL DAMAGES RELATING  TO THE SOFTWARE.
 */

package eu.nohkumado.nohutils.commands;
 import java.io.*;
import java.util.regex.*;

 @SuppressWarnings({"WeakerAccess", "CanBeFinal"})
 public class PatternFileFilter implements FilenameFilter
{
  protected Pattern pattern;
  /** 
   * CTOR 
   * 
   * @param pat  pattern
   */
  public PatternFileFilter(String pat)
  {
    pattern = Pattern.compile(pat);
  }// public PatternFileFilter(String pat)
  
  /** 
   * check against the stored pattern 
   * 
   * @param dir dir
   * @param name name
   * @return yes or no
   */
  public boolean accept(File dir, String name)
  {
    if(pattern != null)
    {
      //System.out.println("comparing "+pattern+" vs "+name);
      Matcher matcher = pattern.matcher(name);
      return matcher.find();
    }// if(pattern != null)
    return(true);
  }//public boolean accept(File dir, String name)
}//public class PatternFileFilter implements FilenameFilter
