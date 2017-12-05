package com.nohkumado.utilsapp.test;
import com.nohkumado.nohutils.ShellI;
import com.nohkumado.nohutils.test.*;
import com.nohkumado.nohutils.collection.*;
/**
 Begin, don't know where the proper testsuite fell of... :( somehow it disappeared...
 will slowly rebuild it here...
 */
@SuppressWarnings("UnusedParameters")
public class NohTest extends UtilsTester
{
	private final static String TAG ="NoT";


	public NohTest(ShellI s)
	{
	}

	@SuppressWarnings("UnusedAssignment")
	public String runTest()
	{
		StringBuilder result = new StringBuilder();
		UtilsTester tester = new TreeMapTableTest();

		try
		{


			result.append("TreeMapTable test:");
			if (tester.test()) result.append("success!");
			else result.append("failed ! ");
			//print("End TreeMapTable test");

			result.append("\nBTreeAtom test:");
			tester = new BTreeAtomTest();
			if (tester.test()) result.append("success!");
			else 
			{
				result.append("failed ! ");
				result.append(tester.log());
			}

			//print("TreeMapTable test");
			result.append("\nNTreeNode test:");
			tester = new NTreeNodeTest();
			if (tester.test()) result.append("success!");
			else 
			{
				result.append("failed ! ");
				result.append(tester.log());
			}

			result.append("\nSorted Array test:");

			tester = new SortedArrayTest();
			if (tester.test()) result.append("success!");
			else 
			{
				result.append("failed ! ");
				result.append(tester.log());
			}
		}//try
		catch (NullPointerException e)
		{
			result.append(tester.log()).append("\n").append(e.toString());
		}
		return result.toString();
	}//public String runTest()

}//public class NohTest extends UtilsTester

