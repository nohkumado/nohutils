package com.nohkumado.nohutils.test;
import com.nohkumado.nohutils.collection.*;
import java.util.*;

public class SortedArrayTest extends UtilsTester
{
	protected static String testName  = "SortedArrayTest";

	@Override
	public boolean test()
	{
		List<String> list = Arrays.asList(
				"birnen", "aepfel", "kirschen", "trauben", "orangen"
			);
		SortedArrayList<String> sorted = new SortedArrayList<>();
		for(String item : list)
		{
			sorted.insertSorted(item);	
		}//for(String item : list)
		String expected = "[aepfel, birnen, kirschen, orangen, trauben]";		
		return doTrans(!expected.equals(sorted.toString())," failed sortedarray expected '"+expected+"' got '"+sorted+"'");
	}//public boolean test()
}//public class SortedArrayTest extends UtilsTester
