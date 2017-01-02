package com.nohkumado.nohutils.collection;
import android.util.*;
import java.util.*;

public class TreeMapTable<E,G> implements Iterable
{
  TreeMap<E,ArrayMap<E,G>> rows = new TreeMap<>();
  TreeMap<Integer,E> colNames =   new TreeMap<>();

  public G set(E row, E col, G val)
  {
    ArrayMap<E,G> aRow = rows.get(row);
    if (aRow == null)
    {
      aRow = new ArrayMap<>();
      rows.put(row, aRow);
    }
    aRow.put(col, val);
    if (!colNames.containsValue(col)) colNames.put(colNames.size(), col);
    return val;
  }
  public G get(E row, E col)
  {
    try
    {
      ArrayMap<E,G> aRow = rows.get(row);
      return aRow.get(col);
    }
    catch (NullPointerException e)
    {

    }
    return null;
  }
  public E get(int row)
  {
    int i= 0;
    E theKey = null;
    for (E aKey:  rows.keySet())
    {
      if (i == row)
      {
        theKey = aKey;
        break;
      }
      i++;
    }
    return theKey;
  }
  public int size()
  {
    return rows.size();
  }

  @Override
  public String toString()
  {
    StringBuilder sb = new StringBuilder();
    sb.append("%10s ");
    for (int i = 0; i <= colNames.size(); i++) sb.append("%10s ");
    String form = sb.toString();
    sb =  new StringBuilder();
    sb.append(String.format("%3s %10s ", "num", "token"));
    for (Map.Entry<Integer,E> col: colNames.entrySet())
    {
      sb.append(String.format("%10s ", col.getValue()));
    }
    sb.append("\n");
    int i = 0; 
    for (Map.Entry<E,ArrayMap<E,G>> row: rows.entrySet())
    {
      sb.append(String.format("%3d %-10s ", i, row.getKey()));
      i++;

      ArrayMap<E,G> aRow = row.getValue();
      for (Map.Entry<Integer,E> col: colNames.entrySet())
      {
        E lkey = col.getValue();
        if (aRow.containsKey(lkey)) sb.append(String.format("%-10s ", aRow.get(lkey)));
        else sb.append(String.format("%10s ", " "));
      }
      sb.append("\n");
    }


    return sb.toString();
  }

  @Override
  public Iterator iterator()
  {
    return rows.keySet().iterator();
  }

  
}
