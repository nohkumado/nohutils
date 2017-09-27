package com.nohkumado.nohutils.collection;
/* Copyright (C) 2016 Bruno Böttcher
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

import com.nohkumado.nohutils.*;
import java.util.*;

/**
 * Node of a n-dimensional tree
 */
public class BTreeAtom 
{
	protected BTreeAtomFactory factory;
	//public static BTreeAtom factory(String n)
	//{ return new BTreeAtom(n);}

	protected String name;
	protected BTreeAtom parent, left,right;
	public static final String TAG = "BTA";

	protected BTreeAtom(String name, BTreeAtomFactory f)
	{
		this.name = name;
		factory = f;
	}
	/**
	 * remove a subtree
	 */
	public BTreeAtom remove_left()
	{
		BTreeAtom tmp = left;
		tmp.parent = null;
		left = null;
		return tmp;
	}//public BTreeAtom remove_left()
	public BTreeAtom remove_right()
	{
		BTreeAtom tmp = right;
		tmp.parent = null;
		right = null;
		return tmp;
	}//public BTreeAtom remove_left()

	/**
	 * get an atom, use a / separated path to navigate in the tree
	 */
	public BTreeAtom left()
	{
		return left;
	}
	public BTreeAtom right()
	{
		return right;
	}
	public BTreeAtom left(BTreeAtom tmp)
	{
		tmp.parent = this;
		left = tmp;
		return left;
	}
	public BTreeAtom right(BTreeAtom tmp)
	{
		tmp.parent = this;
		right = tmp;
		return right;
	}
	/**
	 * return true if this is a leave
	 */
	protected boolean isLeave()
	{
		if (parent != null || (left == null && right == null)) return true;
		return false;
	}

	/**
	 * create a string representation of this tree
	 */
	public String toString(String indent)
	{
		StringBuilder result = new StringBuilder();
		result.append(indent).append(name).append(" ");;
		if (!isLeave()) result.append(" (");
		if (left != null) result.append(left.toString(indent + "  "));
		if (right != null) result.append(right.toString(indent + "  "));
		if (!isLeave()) result.append(")");
		result.append("\n");
		return result.toString();
	}

	@Override
	public String toString()
	{
		StringBuilder result = new StringBuilder();
		result.append(name).append(" ");;
		if (left != null) result.append(left.toString());
		if (right != null) result.append(right.toString());
		return result.toString();
	}//public String toString(String indent)

	/** -------------------------------------------------------------------
	 * make a recursive one on one copy of this structure 
	 * ------------------------------------------------------------------- */
	/*public BTreeAtom clone(BTreeAtomFactory factory)
	 {

	 BTreeAtom cloned = factory.instantiate(name);

	 if (left != null) cloned.left(left.clone(factory)); 
	 if (right != null) cloned.right(right.clone(factory)); 
	 return cloned;
	 }// public java.lang.Object clone()

	 */
	public void copy(BTreeAtom root)
	{
		
		name = root.name;
		if (root.left != null) 
		{
			left = factory.instantiate(root.left);
		}
		
		if (root.right != null) 
		{
			right = factory.instantiate(root.right);
		}
	}//public void copy(GeneticAtom root)

	/** -----------------------------------------------------------------
	 * override the equals method of those dman thing, seems the default is
	 * broken...
	 * ------------------------------------------------------------------*/
	public boolean equals(Object obj)
	{
		if (! (obj instanceof BTreeAtom)) return false;
		BTreeAtom cmpTo = (BTreeAtom) obj;
		if (!name.equals(cmpTo.name)) return false;
		boolean toRet = true;
		if (left == null && cmpTo.left == null && 
			right == null && cmpTo.right == null) return toRet; //is leaf
		if (left != null && cmpTo.left != null) toRet &= left.equals(cmpTo.left);
		else toRet &= false;
		if (right != null && cmpTo.right != null) toRet &= right.equals(cmpTo.right);
		else toRet &= false;

		return toRet;
	}// public boolean equals(Object obj)

	public String name()
	{
		return name;
	}//public String name()

	public String name(String n)
	{
		name = n;
		return name;
	}//public String name()
	public void walk(ArrayList<BTreeAtom> flatList)
	{
		flatList.add(this);
		if(left != null) left.walk(flatList);
		if(right != null)  right.walk(flatList);
	}
	
}//class
