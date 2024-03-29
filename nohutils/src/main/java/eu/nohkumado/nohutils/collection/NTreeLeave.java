package eu.nohkumado.nohutils.collection;
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

import android.util.Log;

import java.util.ArrayList;

/**
* Tree leave, its sole purpose is to hold the reference to some content
*/
public class NTreeLeave<E> extends NTreeAtom<E>
{
	public NTreeLeave<E>  set(E arg)
	{
		content = arg;
		return this;
	}

	@Override
	public String toString(String indent)
	{
		StringBuilder result = new StringBuilder();
		if(!dumpPrint) result.append("(");
		if(content != null) result.append(content);
		if(!dumpPrint) result.append(")");
		return result.toString();
	}//public String toString(String indent)

	@Override
	public NTreeAtom<E> set(NTreeAtom<E> aNode, String path) {
		Log.e(TAG, "called add to path on leave..."+aNode);
		return this;
	}

	@Override
	protected E set(ArrayList<String> path, E kto) {
		content = kto;
		return content;
	}
	public TreeContext accept(TreeVisitor visitor, TreeContext context)
	{
		return visitor.visit(this, context);
	}

	@Override
	public String name()
	{
		if( super.name() == null) return  getContent().toString();
		return (super.name());
	}
}//public class NTreeLeave<E> extends NTreeAtom<E>
