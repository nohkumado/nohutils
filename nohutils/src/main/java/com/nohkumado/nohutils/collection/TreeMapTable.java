package com.nohkumado.nohutils.collection;
import java.util.*;
import android.util.*;

public class TreeMapTable<E,G>
{
  TreeMap<E,ArrayMap<E,G>> rows = new TreeMap<>();
  
  public G set(E row, E col, G val)
  {
    ArrayMap<E,G> aRow = rows.get(row);
    if(aRow == null)
    {
      aRow = new ArrayMap<>();
      rows.put(row,aRow);
    }
    aRow.put(col,val);
    return val;
  }
  public G get(E row, E col)
  {
    ArrayMap<E,G> aRow = rows.get(row);
    if(aRow != null)
    {
      return aRow.get(col);
    }
    return null;
  }
}
