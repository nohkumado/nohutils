package com.nohkumado.nohutils;

import android.widget.*;

public interface DataStorageFrag<E>
{
  //public int actPos();
  //public String getIdPathString(E targetItem);
  //public String getPath2String();
  //public E getLastValidItem();
 // public void removeFromHere(int i);

  void remove(int lastIndex);
  //public E getLastItem();

  //public E fillItem(int pos, Cursor dataCrs);

  E getItem(int id);
  E getItem();

  void setItem(E actItem, int pos);
  void setItem(E get);

  int pathSize();

  BaseAdapter getAdapter(int pos);
  E extractData(Object selectedItem, int selectedItemPosition);
  
  //public void setAdapter(int pos, SimpleCursorAdapter sAdapter);
  //public void swapCursor(int id, Cursor data);


  //public void notifyDataChange();

  //public void setOnItemListener(OnItemSelectedListener p0);
}//public interface DataStorageFrag<E>

