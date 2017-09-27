package com.nohkumado.utilsapp.test;
import com.nohkumado.nohutils.test.*;
import com.nohkumado.nohutils.collection.*;
/**
Begin, don't know where the proper testsuite fell of... :( somehow it disappeared...
will slowly rebuild it here...
*/
public class NohTest extends UtilsTester
{
  private final static String TAG ="NoT";
  
  public NohTest()
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

    doTrans(!"zwei".equals(table.get("C","E")),"failed TreeMapTable test: zwei !="+table.get("C","E"));
    //print("End TreeMapTable test");
    
    //print("TreeMapTable test");
    NTreeNode root = new NTreeNode();
    return log.toString();
  }
  
}
