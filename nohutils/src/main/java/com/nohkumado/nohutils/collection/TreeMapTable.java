package com.nohkumado.nohutils.collection;
import android.util.*;
import java.util.*;

@SuppressWarnings({"WeakerAccess", "UnusedReturnValue"})
public class TreeMapTable<E,G> implements Iterable
{
  TreeMap<E,ArrayMap<E,G>> rows = new TreeMap<>();
  TreeMap<Integer,E> colNames =   new TreeMap<>();

  public TreeMapTable()
  {
    if(rows == null) rows = new TreeMap<>();
    if(colNames == null)  colNames =   new TreeMap<>();
  }//CTOR zenfone problem search....

  public boolean contains(String token)
  {
	  return rows.containsKey(token);
  }
  public synchronized G set(E row, E col, G val)
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
  @SuppressWarnings("SameParameterValue")
  public G get(E row, E col)
  {
    try
    {
      ArrayMap<E,G> aRow = rows.get(row);
      return aRow.get(col);
    }
    catch (NullPointerException ignored)
    {

    }
    return null;
  }//public G get(E row, E col)
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
    //String form = sb.toString();
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
      sb.append(String.format(Locale.getDefault(),"%3d %-10s ", i, row.getKey()));
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
  public synchronized Iterator iterator()
  {
    return rows.keySet().iterator();
  }

  public TreeMapTable<E,G> insert(TreeMapTable<E,G> toCopy)
  {
    for (Map.Entry<E,ArrayMap<E,G>> row: toCopy.rows.entrySet())
    {
      E rowkey = row.getKey();

      ArrayMap<E,G> aRow = row.getValue();
      for (Map.Entry<E,G> rowData: aRow.entrySet())
      {
        E col = rowData.getKey();

        set(rowkey, col, rowData.getValue());
      }
    }
    return this;
  }
  /**
   * remove a whole line
   */
  @SuppressWarnings("SuspiciousMethodCalls")
  public ArrayMap<E,G> remove(String line)
  {
    return rows.remove(line);
  }
  /**
   * remove a field
   */
  @SuppressWarnings("SuspiciousMethodCalls")
  public G remove(String line, String col)
  {
    ArrayMap<E,G> lineMap = rows.get(line);
    if (lineMap != null)
    {
      return lineMap.remove(col);  
    }
    return null;
  }
  /**
   * return the header
   */
  public TreeMap<Integer,E> header()
  {
    return colNames; 
  }
  /**
   * add a new column
   */
  public void addCol(E col)
  {
    colNames.put(colNames.size(), col);
  }
  /**
   *
   * hasCol
   * check if a column exists
   */
  public boolean hasCol(E column)
  {
    return colNames.containsValue(column);
  }//public boolean hasCol(E column)
  public void clear()
  {
    rows.clear();
    colNames.clear();
  }
	public boolean rename(E oldTok,E newTok)
  {
	  if(rows.keySet().contains(oldTok))
	  {
		  if(!"".equals(newTok))
		  {
			  rows.put(newTok,rows.remove(oldTok));
			  return true;
		  }//if(!"".equals(newTok))
	  }//if(rows.keySet().contains(oldTok))
	  return false;
  }//public boolean rename(String oldTok,String newTok)
	
}//public class TreeMapTable<E,G> implements Iterable
