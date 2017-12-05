package com.nohkumado.nohutils.test;

import com.nohkumado.nohutils.collection.*;

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

		if(doTrans(!"zwei".equals(table.get("C","E")),"failed TreeMapTable test: zwei !="+table.get("C","E"))) 
			return true;
		
		return false;
	}//public boolean test()
}//public class TreeMapTableTest extends UtilsTester
