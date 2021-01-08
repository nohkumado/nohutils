package eu.nohkumado.nohutils.collection;



public class BTreeAtomFactory
{
	public BTreeAtom instantiate(BTreeAtom root)
	{
		BTreeAtom copy = instantiate(root.name()); 
		copy.copy(root);
		return copy;
	}
	public BTreeAtom instantiate(String n)
	{ return new BTreeAtom(n, this);}
}//public class PolishAtomFactory

