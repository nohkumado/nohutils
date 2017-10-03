package com.nohkumado.nohutils.test;
import com.nohkumado.nohutils.collection.*;

@SuppressWarnings("WeakerAccess")
public class BTreeAtomTest extends UtilsTester implements Cloneable
{
	protected static String testName  = "BTreeAtom";
	
	public BTreeAtomTest()
	{
		BTreeAtomFactory factory = new BTreeAtomFactory();
		String[] names = new String[]{"root","+","*","1","2","3","4"};
		BTreeAtom root = factory.instantiate(names[0]);
		BTreeAtom[] tmp = new BTreeAtom[names.length];
		for (int i = 1; i < names.length; i++)
			tmp[i - 1] =  factory.instantiate(names[i]);
		root.left(tmp[0]);
		root.right(tmp[1]);
		tmp[0].left(tmp[2]);
		tmp[0].right(tmp[3]);
		tmp[1].left(tmp[4]);
		tmp[1].right(tmp[5]);
	}//public BTreeAtomTest()
}//public class BTreeAtomTest extends UtilsTester
