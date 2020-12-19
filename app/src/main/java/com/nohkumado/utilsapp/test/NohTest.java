package com.nohkumado.utilsapp.test;

import com.nohkumado.nohutils.ShellI;
import com.nohkumado.nohutils.test.BTreeAtomTest;
import com.nohkumado.nohutils.test.NTreeNodeTest;
import com.nohkumado.nohutils.test.SortedArrayTest;
import com.nohkumado.nohutils.test.TreeMapTableTest;
import com.nohkumado.nohutils.test.UtilsTester;
import com.nohkumado.nohutils.test.UtilsTesterTest;

/**
 * Begin, don't know where the proper testsuite fell of... :( somehow it disappeared...
 * will slowly rebuild it here...
 */
@SuppressWarnings("UnusedParameters")
public class NohTest extends UtilsTester {
  private final static String TAG = "NoT";
  StringBuilder result = new StringBuilder();
  UtilsTester tester = new TreeMapTableTest();


  public NohTest(ShellI s) {
  }

  public String runTest() {

    try {

      test();
    }//try
    catch (NullPointerException e) {
      result.append(tester.log()).append("\n").append(e.toString());
    }
    return result.toString();
  }//public String runTest()

  @Override
  public boolean test() {
    result.append("UtilsTester test:");
    tester = new UtilsTesterTest();
    if (tester.test()) result.append("success!\n");
    else result.append("failed ! \n").append(tester.log()).append("\n");

    result.append("TreeMapTable test:");
    tester = new TreeMapTableTest();
    if (tester.test()) result.append("success!\n");
    else result.append("failed ! ").append(tester.log()).append("\n");
    //print("End TreeMapTable test");

    result.append("BTreeAtom test:");
    tester = new BTreeAtomTest();
    if (tester.test()) result.append("success!");
    else {
      result.append("failed ! ");
      result.append(tester.log());
    }

    //print("TreeMapTable test");
    result.append("\nNTreeNode test:");
    tester = new NTreeNodeTest();
    if (tester.test()) result.append("success!");
    else {
      result.append("failed ! ");
      result.append(tester.log());
    }

    result.append("\nSorted Array test:");

    tester = new SortedArrayTest();
    if (tester.test()) result.append("success!");
    else {
      result.append("failed ! ");
      result.append(tester.log());
    }
    return true;
  }
}//public class NohTest extends UtilsTester

