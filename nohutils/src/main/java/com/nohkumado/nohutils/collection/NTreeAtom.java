package com.nohkumado.nohutils.collection;
/* $Id: NTreeAtom.java 2016 bboett Exp $ -*- java -*-
 * Copyright (C) 2016 Bruno Böttcher
 * nohkumado@gmail.com
 * https://sites.google.com/site/nokumado/
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
import android.util.*;

/**
* common ancestor to node an leave, abstract since it shouldn't have an 
* implementation....
*/
public abstract class NTreeAtom<E>
{

	public final static  String TAG = "NTA";

	public NTreeAtom set(E aProfile, String cathegory, String name)
	{
		return null;
	}

	protected boolean isLeave(){return true;}
	abstract public NTreeAtom set(E aProfile, String path);
	
	public E get(String path, String name){return null;}
	public NTreeAtom get(String path)
	{
		return this;
	}
	public void setContent(E profil)
	{
	}

	public E getContent()
	{
		// TODO: Implement this method
		return null;
	}

	
	public NTreeAtom remove(String path)
	{
		Log.e(TAG,"we shouldn't be calling remove on a leave...");
		return null;
	}

	@Override
	public String toString()
	{
		return toString("");
	}

	public String toString(String indent)
	{
		return indent;
	}
}
