package eu.nohkumado.nohutils.collection;
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


import java.util.*;

/**
* common ancestor to node an leave, abstract since it shouldn't have an 
* implementation....
*/
@SuppressWarnings({"WeakerAccess", "EmptyMethod", "UnusedParameters"})
public abstract class NTreeAtom<E>
{

	public final static  String TAG = "NTA";

	private String name;
	protected boolean dumpPrint;
	protected E content;

	public String prettyPrint(String prefix)
	{
		return toString();
	}

	
	public NTreeAtom<E> name(String localKey)
	{
		name = localKey;
		return this;
	}
	public String name()
	{
		return name;
	}
	public boolean isLeaf()
	{
		return true;
	}
	public boolean isNode()
	{
		return false;
	}
	
	public NTreeAtom<E> set(E aProfile, String category, String name)
	{
		return null;
	}
	public E get(String path, String name){return content;}
	public NTreeAtom<E> get(String path)
	{
		return this;
	}
	public NTreeAtom<E> get(ArrayList<String> path)
	{
		return this;
	}
	public NTreeAtom<E> setContent(E profile)
	{
	    content = profile;
		return this;
	}

	public E getContent()
	{
		return content;
	}

	
	public NTreeAtom<E> remove(String path)
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
	public void dump(boolean p0)
	{
		dumpPrint = p0;
	}

	public abstract NTreeAtom<E> set(NTreeAtom<E> aNode, String path)//	public void set(NTreeNode aNode, String p1)
	;

	protected abstract E set(ArrayList<String> path, E kto);

	protected abstract TreeContext accept(TreeVisitor visitor, TreeContext nothing);
}//class
