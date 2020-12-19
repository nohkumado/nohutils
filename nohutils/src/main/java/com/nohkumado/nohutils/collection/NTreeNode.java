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

import java.util.*;
import java.util.regex.*;
import android.util.*;

/**
 * Node of a n-dimensional tree
 */
@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class NTreeNode<E>  extends NTreeAtom<E>
{
	protected HashMap<String, NTreeAtom<E>> childs = new HashMap<>();
	protected Pattern slash = Pattern.compile("/");
	public static final String TAG = "NTN";
	/**
	 * remove a subtree
	 */
	@Override
	public NTreeAtom<E> remove(String path)
	{
		Matcher match = slash.matcher(path);
		//String[] splitted = path.split("/");
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);

			NTreeAtom<E> child = childs.get(localKey);
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
	public NTreeAtom<E> get(String path)
	{
		//Log.d(TAG, "asked for " + path);
		if ("".equals(path)) return  this;

		//String[] splitted = path.split("/");
		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);
			String restPath = path.substring(excise + 1);
			//Log.d(TAG, "extracted local key: " + localKey + " / " + restPath);

			NTreeAtom<E> child = childs.get(localKey);
			if (child != null)
			{
				//Log.d(TAG, "returning node... " + child.get(restPath));
				return child.get(restPath);
			}
			//Log.d(TAG, "nothing found under this path.. available "+childs.keySet());
			return null;
		}//if (match.find())
		//Log.d(TAG, "returning leave " + path+" "+childs.get(path));
		return childs.get(path);
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

		NTreeAtom<E> child = childs.get(localKey);
		if (child != null)
		{
			path.remove(0);
			//Log.d(TAG, "returning node... " + child.get(restPath));
			return child.get(path);
		}

		//Log.d(TAG, "nothing found under this path.. available "+childs.keySet());
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

	/**
	 * insert a content somewhere in the tree at the end of the / separated path given,
	 * create intermediary nodes if necessary
	 */

	/*public NTreeAtom<E> set(E someContent, String path)
	 {
	 super.set(someContent,path);
	 NTreeAtom<E> child;
	 Log.d(TAG, "setting with content"+someContent+" in node '" + path+"'");
	 String localKey = path;
	 String restPath = "";

	 Matcher match = slash.matcher(path);
	 if (match.find())
	 {
	 int excise = path.indexOf("/");
	 localKey = path.substring(0, excise);
	 restPath = path.substring(excise + 1);
	 //Log.d(TAG, "set extracted local key: " + localKey + " rest " + restPath);
	 }

	 Log.d(TAG, "splitted path into " + localKey + " / " + restPath);

	 if (restPath.length() > 0)
	 {
	 child = childs.get(localKey);
	 if (child == null)
	 {
	 Log.d(TAG, "creating new node " + localKey);
	 child = new NTreeNode<>();
	 childs.put(localKey, child);
	 }
	 child = child.set(someContent, restPath);
	 }
	 else
	 {
	 Log.d(TAG, "am leave ");

	 child = childs.get(path);
	 if (child == null)
	 {
	 Log.d(TAG, "creating leave " + path);
	 child = new NTreeLeave<>();
	 childs.put(path, child);
	 }
	 child.setContent(someContent);
	 }
	 Log.d(TAG, "done setting  " + someContent + " to " + path);

	 return child;
	 }*/
	public NTreeAtom<E> set(NTreeAtom<E> aNode, String path)
	{
		Matcher match = slash.matcher(path);
		if (match.find())
		{
			int excise = path.indexOf("/");
			String localKey = path.substring(0, excise);
			String restPath = path.substring(excise + 1);
			NTreeNode<E> child;
			if (!childs.containsKey(localKey))
			{
				child = copy();
				childs.put(localKey, child);
			}
			else child = (NTreeNode<E>) childs.get(localKey); //TODO beware cast exception possible
			child.set(aNode, restPath);
		}//if (match.find())
		else
		{
			//no / thus a plain key
			childs.put(path, aNode);
		}
		return this;
	}//	public void set(NTreeNode aNode, String p1)

	public NTreeAtom<E> set(NTreeAtom<E> aProfile, String path, String name)
	{
		if (path != null && path.length() > 0) path += "/" + name;
		else path = name;
		return set(aProfile, path);
	}
	public NTreeAtom<E> set(ArrayList<String> path, E kto)
	{
		String localKey = path.remove(0);
		NTreeAtom<E> child;
		if (!childs.containsKey(localKey))
		{
			//Log.d(TAG, "create new atom " + localKey + "/" + path+" in "+name());
			if (path.size() > 0) child = copy();
			else child = makeLeaf();
			child.name(localKey);
			childs.put(localKey, child);
		}
		else 
		{
			//Log.d(TAG, "found atom " + localKey + "/" + path);
			child = childs.get(localKey);
		}
		if (child.isLeaf())
		{
			//Log.d(TAG, "setting kto to leaf ");
			if (path.size() > 0) 
			{
				Log.e(TAG, "still have " + path + "to walk, but " + child + " is leaf exchanging...");
				NTreeNode<E> replacement = copy();
				replacement.setContent(child.getContent()).name(child.name());
				childs.put(localKey,replacement);
				return replacement.set(path, kto);
			}
			else return child.setContent(kto);
		}
		else
		{
			//Log.d(TAG, "continuing descending " + path);
			return ((NTreeNode<E>)child).set(path, kto);
		}
	}//public NTreeAtom set(ArrayList<String> path, E kto)

	public NTreeAtom<E> makeLeaf()
	{
		return new NTreeLeave<>();
	}//public NTreeAtom set(ArrayList<String> path, E kto)

	public NTreeNode<E> copy()
	{
		// TODO: Implement this method
		return new NTreeNode<>();
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
		if (indented && !dumpPrint) result.append(indent);
		if(!dumpPrint && name() != null && name().length() >0) result.append(name());
		if (!dumpPrint)result.append("[");
		if (indented && !dumpPrint) result.append("\n").append(indent).append(indent);
		//result.append(indent).append("[");
		if (childs.size() <= 0)
		{
			result.append("empty");
		}
		else 
		{
			for (String name : childs.keySet())
			{
				//result.append(name).append("-").append(childs.get(name).toString(indent + indent));
				if(childs.get(name).isNode())result.append(childs.get(name).toString(indent + indent));
				else if(!dumpPrint) result.append(indent).append(name).append(childs.get(name).toString(indent + indent)).append("\n");
				else result.append(childs.get(name).toString(indent + indent)).append("\n");
				if (indented && !dumpPrint) result.append(indent);
				//if (indented) result.append("\n").append(indent).append(indent);
			}//for (String name : childs.keySet())
		}//else 
		if (indented && !dumpPrint) result.append(indent);
		if (!dumpPrint) result.append("]");
		if (!dumpPrint) if (indented) result.append("\n");
		return result.toString();
	}//public String toString(String indent)
}//public class NTreeNode<E>  extends NTreeAtom<E>
