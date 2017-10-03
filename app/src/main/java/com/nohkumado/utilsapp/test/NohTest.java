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
  
  public String runTest()
  {
    StringBuilder result = new StringBuilder();
    result.append("TreeMapTable test:");
    //print("TreeMapTable test");
    TreeMapTable<String,String> table = new TreeMapTable<>();
    table.set("A","B","vier");
    table.set("A","A","eins"); 
    table.set("C","E","zwei");
    table.set("C","B","drei");

    if(doTrans(!"zwei".equals(table.get()),"failed TreeMapTable test: zwei !="+table.get())) result.append("success!");
    else result.append("failed!");
    //print("End TreeMapTable test");
    
    //print("TreeMapTable test");
    result.append("\nNTreeNode test:");
    NTreeNode root = new NTreeNode();
    /*NTreeAtom leave = root.remove("path");
    leave =  root.get("path");
    leave =  root.set("somethiong","path");
    root.toString();*/
    return result.toString();
  }//public String runTest()

}//public class NohTest extends UtilsTester

