package com.nohkumado.nohutils;

import android.widget.*;

public interface DataStorageFrag<E>
{
  //public int actPos();
  //public String getIdPathString(E targetItem);
  //public String getPath2String();
  //public E getLastValidItem();
 // public void removeFromHere(int i);

  public void remove(int lastIndex);
  //public E getLastItem();

  //public E fillItem(int pos, Cursor dataCrs);

  public E getItem(int id);
  public E getItem();

  public void setItem(E actItem, int pos);
  public void setItem(E get);

  public int pathSize();

  public BaseAdapter getAdapter(int pos);
  public E extractData(Object selectedItem, int selectedItemPosition);
  
  //public void setAdapter(int pos, SimpleCursorAdapter sAdapter);
  //public void swapCursor(int id, Cursor data);


  //public void notifyDataChange();

  //public void setOnItemListener(OnItemSelectedListener p0);
}//public interface DataStorageFrag<E>

