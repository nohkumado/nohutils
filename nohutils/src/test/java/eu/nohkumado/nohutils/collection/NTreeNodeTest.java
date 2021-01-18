package eu.nohkumado.nohutils.collection;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

public class NTreeNodeTest
{
@Test
public void ctor()
{
  NTreeNode<String> root = new NTreeNode<String>().name("root");
  assertNotNull(root);
  root.dump(false);

}
  @Test
  public void remove()
  {
    NTreeNode<String> root = new NTreeNode<String>().name("root");
    buildSampleTree(root);
    root.remove("/three/l5");
    assertEquals("root[one[(l1),(l2)],two[(l3),(l4)],three[(l6)]]",root.toString("").trim());
  }

  @Test
  public void isNode()
  {
    NTreeNode<String> root = new NTreeNode<String>().name("root");
    assertTrue(root.isNode());
    assertFalse(root.isLeaf());
    NTreeLeave<String> leaf = new NTreeLeave<>();
    assertFalse(leaf.isNode());
    assertTrue(leaf.isLeaf());
  }

  @Test
  public void copy()
  {
  }

  @Test
  public void name()
  {
    NTreeNode<String> root = new NTreeNode<String>().name("root");
    assertEquals("root[empty]",root.toString().trim());
  }

  @Test
  public void set()
  {
    NTreeNode<String> root = new NTreeNode<String>().name("root");
    NTreeLeave<String> leaf = new NTreeLeave<>();
    leaf.set("toto");
    assertEquals("set/get failed expected 'toto' got '"+leaf.get("")+"'","toto",leaf.get("").getContent());
    assertEquals("(toto)",leaf.toString("  ").trim());

    root.dump(false);

    buildSampleTree(root);
    assertEquals("root[one[(l1),(l2)],two[(l3),(l4)],three[(l5),(l6)]]",root.toString("").trim());
  }

  private void buildSampleTree(NTreeNode<String> root)
  {
    String[] names = new String[] { "one", "two","three" };
    String[] subname =  new String[]{ "l1","l2","l3","l4","l5","l6" };
    int n = 0;
    for (String name : names) {
      NTreeNode<String> aNode = new NTreeNode<String>().name(name);
      for (int x = 0; n < subname.length && x < 2; n++, x++)
      {
        NTreeLeave<String> aLeave = new NTreeLeave<String>().set(subname[n]); //create and set content
        //aLeave.name(subname[n]); //give ti the same name
        aNode.set(aLeave, subname[n]);
      }
      root.set(aNode, name);
    }//for(int i = 0; i < names.length; i++)
  }

  @Test
  public void accept()
  {
    TreeVisitorImpl visitor = new TreeVisitorImpl();
    NTreeNode<String> root = new NTreeNode<String>().name("root");
    buildSampleTree(root);
    TreeContextImpl<String> context = new TreeContextImpl<>();
    root.accept(visitor,context);
    ArrayList<String> expected = new ArrayList<>(Arrays.asList("root", "one", "l1", "l2", "two", "l3", "l4", "three", "l5", "l6"));
    assertEquals(expected, context.get());

  }
}