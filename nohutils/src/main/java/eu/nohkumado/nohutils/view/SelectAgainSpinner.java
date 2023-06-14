package eu.nohkumado.nohutils.view;

import android.annotation.SuppressLint;
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
@SuppressLint("AppCompatCustomView")
public class SelectAgainSpinner extends Spinner
{
  private final int lastSelected = 0;

  public SelectAgainSpinner(Context context)
  { super(context); }

  public SelectAgainSpinner(Context context, AttributeSet attrs)
  { super(context, attrs); }

  public SelectAgainSpinner(Context context, AttributeSet attrs, int defStyle)
  { super(context, attrs, defStyle); }

  /*@Override
   protected void onLayout(boolean changed, int l, int t, int r, int b)
   {
   if (this.lastSelected == this.getSelectedItemPosition() && getOnItemSelectedListener() != null)
   getOnItemSelectedListener().onItemSelected(this, getSelectedView(), this.getSelectedItemPosition(), getSelectedItemId());
   if (!changed)
   lastSelected = this.getSelectedItemPosition();

   super.onLayout(changed, l, t, r, b);
   }//protected void onLayout(boolean changed, int l, int t, int r, int b)
   */
  
  @Override
  public void setSelection(int position, boolean animate)
  {
    boolean sameSelected = position == getSelectedItemPosition();
    super.setSelection(position, animate);
    if (sameSelected)
    {
      // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
      getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
    }//if (sameSelected)
  }// public void setSelection(int position, boolean animate)
  

  @Override
  public void setSelection(int position)
  {
    boolean sameSelected = position == getSelectedItemPosition();
    super.setSelection(position);
    if (sameSelected)
    {
      // Spinner does not call the OnItemSelectedListener if the same item is selected, so do it manually now
      getOnItemSelectedListener().onItemSelected(this, getSelectedView(), position, getSelectedItemId());
    }//if (sameSelected)
    
  }//public void setSelection(int position)
}//public class SelectAgainSpinner extends Spinner

