package com.nohkumado.nohutils.collection;
/* Copyright (C) 2016 Bruno Böttcher
 * nohkumado@gmail.com
 * https://nokumado.eu
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
import java.util.regex.*;
import android.util.*;

/**
 * Node of a n-dimensional tree
 */
public class NTreeNode<E>  extends NTreeAtom<E>
{
	protected HashMap<String, NTreeAtom<E>> children = new HashMap<>();
	protected Pattern slash = Pattern.compile("/");
	public static final String TAG = "NTN";
	/**
	 * remove a subtree
	 */
	@Override
	public NTreeAtom<E> remove(String path)
	{
		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);

			NTreeAtom<E> child = children.get(localKey);
			if (child != null)
			{
				return child.remove(path.substring(excise + 1));
			}
			return null;
		}
		return children.remove(path);
	}

	/**
	 * get an atom, use a / separated path to navigate in the tree
	 */
	@Override
	public NTreeAtom<E> get(String path)
	{
		//Log.d(TAG, "asked for " + path);
		if ("".equals(path)) return  this;

		//String[] split = path.split("/");
		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);
			String restPath = path.substring(excise + 1);
			//Log.d(TAG, "extracted local key: " + localKey + " / " + restPath);

			NTreeAtom<E> child = children.get(localKey);
			if (child != null)
			{
				//Log.d(TAG, "returning node... " + child.get(restPath));
				return child.get(restPath);
			}
			//Log.d(TAG, "nothing found under this path.. available "+children.keySet());
			return null;
		}//if (match.find())
		//Log.d(TAG, "returning leave " + path+" "+children.get(path));
		return children.get(path);
	}//public NTreeAtom<E> get(String path)
	/**
	 * get an atom, use a / separated path to navigate in the tree
	 */
	@Override
	public NTreeAtom<E> get(ArrayList<String> path)
	{
		//Log.d(TAG, "asked for " + path);
		if (path.size() <= 0) return  this;
        String localKey = path.get(0);

		NTreeAtom<E> child = children.get(localKey);
		if (child != null)
		{
			path.remove(0);
			//Log.d(TAG, "returning node... " + child.get(restPath));
			return child.get(path);
		}

		//Log.d(TAG, "nothing found under this path.. available "+children.keySet());
		return null;
	}//public NTreeAtom<E> get(String path)

	/**
	 * return true if this is a leave
	 */
	@Override
	public boolean isLeaf()
	{
		return false;
	}
	@Override
	public boolean isNode()
	{
		return true;
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


	public NTreeAtom<E> makeLeaf()
	{
		return new NTreeLeave<>();
	}//public NTreeAtom set(ArrayList<String> path, E kto)

    @SuppressWarnings("unchecked")
	public NTreeNode<E> copy()
	{
		NTreeNode<E> copy = new NTreeNode<>();
		copy.children = (HashMap<String, NTreeAtom<E>>) children.clone();
		copy.slash = slash;
		return copy;
	}
	/**
	 * create a string representation of this tree
	 */
	@Override
	public String toString(String indent)
	{
		boolean indented = false;
		if (indent != null && indent.length() > 0) indented = true;

		StringBuilder result = new StringBuilder();
		if (indented && dumpPrint) result.append(indent);
		if(name() != null && name().length() >0) result.append(name());
		result.append("[");
		if (indented && dumpPrint) result.append("\n").append(indent).append(indent);
		//result.append(indent).append("[");
		if (children.size() <= 0)
		{
			result.append("empty");
		}
		else 
		{
			boolean first = true;
			for (Map.Entry<String, NTreeAtom<E>> entry : children.entrySet())
			{
				String name = entry.getKey();
				NTreeAtom<E> child = entry.getValue();
				if(first) first = false;
				else result.append(",");
				if(child.isNode())result.append(child.toString(indent + indent));
				else if(dumpPrint) result.append(indent).append(name).append(child.toString(indent + indent)).append("\n");
				else result.append(child.toString(indent));
				//if (indented && !dumpPrint) result.append(indent);
			}
		}//else
		if (indented && !dumpPrint) result.append(indent);
		result.append("]");
		if (dumpPrint) if (indented) result.append("\n");
		return result.toString();
	}//public String toString(String indent)
	public NTreeNode<E> name(String localKey)
	{
		super.name(localKey);
		return this;
	}
	public void dump(boolean p0)
	{
		super.dump(p0);
		for (Map.Entry<String, NTreeAtom<E>> entry : children.entrySet()) {
			entry.getValue().dump(p0);
		}
	}//public void dump(boolean p0)

	@Override
	public NTreeAtom<E> set(NTreeAtom<E> aNode, String path) {
		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);
			String restPath = path.substring(excise + 1);
			NTreeNode<E> child;
			if (!children.containsKey(localKey))
			{
				child = copy();
				children.put(localKey, child);
			}
			else child = (NTreeNode<E>) children.get(localKey);
			if(child != null) child.set(aNode, restPath);
		}//if (match.find())
		else children.put(path, aNode);
		return this;
	}

	@Override
	protected E set(ArrayList<String> path, E kto) {
		String localKey = path.remove(0);
		NTreeAtom<E> child;
		if (!children.containsKey(localKey))
		{
			//Log.d(TAG, "create new atom " + localKey + "/" + path+" in "+name());
			if (path.size() > 0) child = copy();
			else child = makeLeaf();
			child.name(localKey);
			children.put(localKey, child);
		}
		else
		{
			//Log.d(TAG, "found atom " + localKey + "/" + path);
			child = children.get(localKey);
		}
		if (child != null && child.isLeaf())
		{
			//Log.d(TAG, "setting kto to leaf ");
			if (path.size() > 0)
			{
				Log.e(TAG, "still have " + path + "to walk, but " + child + " is leaf exchanging...");
				NTreeNode<E> replacement = copy();
				replacement.setContent(child.getContent()).name(child.name());
				children.put(localKey,replacement);
				return replacement.set(path, kto);
			}
			else {
				child.setContent(kto);
				return kto;
			}
		}
		else
		{
			//Log.d(TAG, "continuing descending " + path);
			 child.set(path, kto);
			return kto;
		}
	}
	/*
	@Override
	public NTreeAtom<E> set(NTreeAtom<E> aProfile, String path, String name)
	{
		if (path != null && path.length() > 0) path += "/" + name;
		else path = name;
		return set(aProfile, path);
	}
	*/
}//public class NTreeNode<E>  extends NTreeAtom<E>
