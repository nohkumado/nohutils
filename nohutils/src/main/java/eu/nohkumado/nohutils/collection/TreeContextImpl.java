package eu.nohkumado.nohutils.collection;

import java.util.ArrayList;
import java.util.List;

public class TreeContextImpl<E> implements TreeContext
{
  List<E> flatList = new ArrayList<E>();

  public void add(E key)
  {
    flatList.add(key);
  }

  public List<E> get()
  {
    return flatList;
  }
}
