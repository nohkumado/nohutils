package com.nohkumado.nohutils.collection;
/* $Id: NTreeLeave.java 2016 bboett Exp $ -*- java -*-
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
/**
* Tree leave, its sole purpose is to hold the reference to some content
*/
public class NTreeLeave<E> extends NTreeAtom<E> 
{

	protected E content;


	public void setE(E e)
	{
		this.content = e;
	}

	public E getE()
	{
		return content;
	}

	@Override
	public NTreeAtom set(E aEe, String path)
	{
		content = aEe;
		return this;
	}


	@Override
	public String toString(String indent)
	{
		StringBuilder result = new StringBuilder();
		result.append(indent).append("leave");
		if(content != null) result.append("*");
		result.append(indent).append("\n");
		return result.toString();
	}
}
