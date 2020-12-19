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
/**
* Tree leave, its sole purpose is to hold the reference to some content
*/
@SuppressWarnings("WeakerAccess")
public class NTreeLeave<E> extends NTreeAtom<E>
{
	protected E content;

	public NTreeLeave<E>  set(E lname)
	{
		content = lname;
		return this;
	}
	
	public E getE()
	{
		return content;
	}

	@Override
	public E getContent()
	{
		return content;
	}

	@Override
  public NTreeAtom<E> setContent(E profil)
	{
		content = profil;
		return super.setContent(profil);
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
}//public class NTreeLeave<E> extends NTreeAtom<E>
