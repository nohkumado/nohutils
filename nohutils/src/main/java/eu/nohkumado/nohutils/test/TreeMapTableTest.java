package eu.nohkumado.nohutils.test;

import eu.nohkumado.nohutils.collection.TreeMapTable;

public class TreeMapTableTest extends UtilsTester
{

	@Override
	public boolean test()
	{
		error = new StringBuilder();
		TreeMapTable<String,String> table = new TreeMapTable<>();
		table.set("A","B","vier");
		table.set("A","A","eins"); 
		table.set("C","E","zwei");
		table.set("C","B","drei");

		return doTrans(!"zwei".equals(table.get("C", "E")), "failed TreeMapTable test: zwei !=" + table.get("C", "E"));

	}//public boolean test()
}//public class TreeMapTableTest extends UtilsTester
