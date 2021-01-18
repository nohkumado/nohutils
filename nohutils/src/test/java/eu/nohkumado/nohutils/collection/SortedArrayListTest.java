package eu.nohkumado.nohutils.collection;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class SortedArrayListTest
{

  @Test
  public void insertSorted()
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
    assertEquals(expected,sorted.toString());
  }
}