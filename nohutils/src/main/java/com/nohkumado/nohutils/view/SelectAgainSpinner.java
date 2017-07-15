package com.nohkumado.nohutils.view;

import android.content.*;
import android.util.*;
import android.widget.*;

/*
 public class SelectAgainSpinner extends Spinner
 {
 OnItemSelectedListener listener;

 public SelectAgainSpinner(Context context, AttributeSet attrs)
 {
 super(context, attrs);
 }
 public SelectAgainSpinner(Context context)
 {
 super(context);
 }
 @Override
 public void setSelection(int position)
 {
 super.setSelection(position);
 if (listener != null)  listener.onItemSelected(null, null, position, 0);
 }

 public void setOnItemSelectedEvenIfUnchangedListener(OnItemSelectedListener listener)
 {
 this.listener = listener;
 }
 }
 */
public class SelectAgainSpinner extends Spinner
{
  private int lastSelected = 0;

  public SelectAgainSpinner(Context context)
  { super(context); }

  public SelectAgainSpinner(Context context, AttributeSet attrs)
  { super(context, attrs); }

  public SelectAgainSpinner(Context context, AttributeSet attrs, int defStyle)
  { super(context, attrs, defStyle); }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b)
  {
    if (this.lastSelected == this.getSelectedItemPosition() && getOnItemSelectedListener() != null)
      getOnItemSelectedListener().onItemSelected(this, getSelectedView(), this.getSelectedItemPosition(), getSelectedItemId());
    if (!changed)
      lastSelected = this.getSelectedItemPosition();

    super.onLayout(changed, l, t, r, b);
  }//protected void onLayout(boolean changed, int l, int t, int r, int b)
}//public class SelectAgainSpinner extends Spinner

