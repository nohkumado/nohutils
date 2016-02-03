package com.nohkumado.nohutils.collection;
/* $Id: NTreeNode.java 2016 bboett Exp $ -*- java -*-
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

import java.util.*;
import android.util.*;
import java.util.regex.*;

/**
 * Node of a n-dimensional tree
 */
public class NTreeNode<E>  extends NTreeAtom<E> 
{
	protected HashMap<String, NTreeAtom> childs = new HashMap<String, NTreeAtom>();
	protected Pattern slash = Pattern.compile("/");
	public static final String TAG = "NTN";

	/**
	 * remove a subtree
	 */
	@Override
	public NTreeAtom remove(String path)
	{
		Matcher match = slash.matcher(path);
		//String[] splitted = path.split("/");
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);

			NTreeAtom child = childs.get(localKey);
			if (child != null)
			{
				return child.remove(path.substring(excise + 1));
			}
			return null;
		}
		return childs.remove(path);
	}

	/**
	 * get an atom, use a / separated path to navigate in the tree
	 */
	@Override
	public NTreeAtom get(String path)
	{
		Log.d(TAG, "asked for " + path);
		if (path.equals("")) return  this;

		//String[] splitted = path.split("/");
		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);
			String restPath = path.substring(excise + 1);
			Log.d(TAG, "extracted local key: " + localKey + " / " + restPath);

			NTreeAtom child = childs.get(localKey);
			if (child != null)
			{
				return child.get(restPath);
			}
			Log.d(TAG, "nothing found under this path..");
			Log.d(TAG, "this node... " + this);
			return null;
		}
		Log.d(TAG, "returning leave " + path);
		return childs.get(path);
	}

	/**
	 * return true if this is a leave
	 */
	@Override
	protected boolean isLeave()
	{
		return false;
	}


	@Override
	public E get(String path, String name)
	{
		StringBuilder sb = new StringBuilder();
		if (path != null && path.length() > 0) sb.append(path).append("/");
		sb.append(name);

		NTreeAtom<E> child = get(sb.toString());
		if (child != null) return child.getContent();
		else return null;
	}

	/**
	 * insert a content somewhere in the tree at the end of the / separated path given,
	 * create intermediary nodes if necessary
	 */
	@Override
	public NTreeAtom set(E aProfile, String path)
	{
		NTreeAtom child = null;
		Log.d(TAG, "setting profile in node " + path);
		String localKey = path;
		String restPath = "";

		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			localKey = path.substring(0, excise);
			restPath = path.substring(excise + 1);
			Log.d(TAG, "set extracted local key: " + localKey + " rest " + restPath);
		}

		Log.d(TAG, "splitted path into " + localKey + " / " + restPath);


		if (restPath != null && restPath.length() > 0)
		{
			child = childs.get(localKey);
			if (child == null)
			{
				Log.d(TAG, "creating new node " + localKey);
				child = new NTreeNode();
				childs.put(localKey, child);
			}
			child = child.set(aProfile, restPath);
		}
		else
		{
			Log.d(TAG, "am leave ");

			child = childs.get(path);
			if (child == null)
			{
				Log.d(TAG, "creating leave " + path);

				child = new NTreeLeave();
				childs.put(path, child);
			}
			child.setContent(aProfile);
		}
		return child;
	}

	public NTreeAtom set(E aProfile, String path, String name)
	{
		if (path != null && path.length() > 0) path += "/" + name;
		else path = name;
		return set(aProfile, path);
	}
	/**
	* create a string representation of this tree
	 */
	@Override
	public String toString(String indent)
	{
		StringBuilder result = new StringBuilder();
		if (childs.size() <= 0)
		{
			result.append(indent).append("| empty");
		}
		else 
			for (String name : childs.keySet())
			{
				result.append(indent).append("|").append(name).append(" - ").append(childs.get(name).toString(indent + "  "));
			}
		result.append("\n");
		return result.toString();
	}
}
