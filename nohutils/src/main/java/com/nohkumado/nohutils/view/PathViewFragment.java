package com.nohkumado.nohutils.view;


import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.view.View.*;
import android.widget.*;
import android.widget.AdapterView.*;
import com.nohkumado.nohutils.*;
import java.util.*;

import android.view.View.OnClickListener;

public class PathViewFragment<E> extends Fragment implements OnClickListener//,OnItemSelectedListener
{
  public final static String TAG = "PVF";
  private ArrayList<Integer> addLater = new ArrayList<>();

  protected  ArrayList<Spinner> spinViews = new ArrayList<Spinner>();

  private LinearLayout viewContainer;

  private ImageButton pathChangeBut;
  //protected int lastSpinnerTouched = 0;

  private DataStorageFrag<E> dataFrag;

  private Context context;

  private ImageButton pathUpBut;
  private ImageButton pathHomeBut;


  /** setter for the context */
  public void setContext(Context p0)
  {
    context = p0;
  }//public void setContext(Context p0)

  /** 
   create a spinner
   */
  public void createSpinnerForPath(int pos)
  {
    //Log.d(TAG, "createspinnerfor " + ".... " + pos);

    //dataFrag.setItem(actItem, pos);

    BaseAdapter sAdapter = dataFrag.getAdapter(pos);
    if (sAdapter != null) 
    {
      //Log.d(TAG, "valid adapter");
      if (spinViews.size() <= pos) 
      {
        Spinner v = new SelectAgainSpinner(context);
        //Log.d(TAG, "created new spinner");
        //v.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //lastSpinnerCreation = new Date().getTime();
        //v.setOnItemSelectedListener(this);
        v.setAdapter(sAdapter);
        //Log.d(TAG, "check container : " + viewContainer);
        if (viewContainer != null)
        {
          LinearLayout container = (LinearLayout) viewContainer.findViewById(R.id.treeselector);
          //Log.d(TAG, "adding spinner to container");
          //spinInit.add(true);
          spinViews.add(v);
          container.addView(v);
          container.invalidate();
          container.refreshDrawableState();
        }//if(viewContainer != null)
        else
        {
          //Log.d(TAG, "no container defined");
          addLater.add(pos);
        }

      }//if (spinViews.size() <= pos) 
      else
      {
        Spinner v =  spinViews.get(pos);
        //spinInit.set(pos, true);
        v.setAdapter(sAdapter);
        //Log.d(TAG, "swapping spinner adapter adapter");
      }
    }//if (sAdapter != null)
    //else Log.d(TAG, "adapter was null....");
    //lastSpinnerTouched = pos;
  }//public void createSpinnerForPath(E actItem,  int pos)

  /**
   setter for the datafrag
   */
  public void setStorage(DataStorageFrag dataFrag)
  {
    this.dataFrag =  dataFrag;
  }///public void setStorage(DataStorageFrag dataFrag)

  /**
   createView
   */
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    //setRetainInstance(true);
    viewContainer = (LinearLayout) super.onCreateView(inflater, container, savedInstanceState);
    if (viewContainer == null)
    {
      viewContainer = (LinearLayout)inflater.inflate(R.layout.pathviewfrag, null);
    }
    //Log.d(TAG, "arg container : " + container + " inflated one  : " + viewContainer);
    //textFrame = (TextView) viewContainer.findViewById(R.id.loggerview);

    pathChangeBut = (ImageButton) viewContainer.findViewById(R.id.path_ok);
    pathChangeBut.setOnClickListener(this);
    pathUpBut = (ImageButton) viewContainer.findViewById(R.id.upButton);
    pathUpBut.setOnClickListener(this);
    pathHomeBut = (ImageButton) viewContainer.findViewById(R.id.homeButton);
    pathHomeBut.setOnClickListener(this);


    if (addLater.size() > 0)
    {
      for (int n: addLater)
      {
        createSpinnerForPath(n);
      }
      addLater.clear();
    }//if(addLater.size() > 0)

    return viewContainer;
  }//public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  @Override
  public void onClick(View knopf)
  {
    if (knopf == pathChangeBut) changePath();  
    else if (knopf == pathUpBut) 
    {
      int lastIndex = spinViews.size() - 1;
      E actIt = dataFrag.extractData(spinViews.get(lastIndex).getSelectedItem(), spinViews.get(lastIndex).getSelectedItemPosition());
      
      remove(lastIndex);
      dataFrag.remove(lastIndex);
    }//else if (knopf == pathUpBut) 
    else if (knopf == pathHomeBut)
    {
      Log.d(TAG,"######about to go home! ");
      
      for (int lastIndex = spinViews.size() - 1; lastIndex > 0; lastIndex--)
      {
        remove(lastIndex);
        dataFrag.remove(lastIndex);  
      }//for(int lastIndex = spinViews.size() -1; lastIndex > 0; lastIndex--)
    }//else if (knopf == pathHomeBut)

  }//public void onClick(View knopf)

  private void changePath()
  {
    //Log.d(TAG, "##############entering changePath with " + lastSpinnerTouched);
    //if (lastSpinnerTouched < 0)
    //{
      //no spinner changed, touched etc...
      //Log.d(TAG, "no spinner touched, change path invalid, bailing");
      //return;
    //}//if (lastSpinnerTouched < 0)

    E storedIt = dataFrag.getItem(spinViews.size()-1);
    Spinner actSpin = spinViews.get(spinViews.size()-1);
    E selectedIt = dataFrag.extractData(actSpin.getSelectedItem(), actSpin.getSelectedItemPosition());
//    if (!selectedIt.equals(storedIt))    
//    {
//      //Log.d(TAG, "no the same! exchanging items in dataFrag");
//      dataFrag.setItem(selectedIt, lastSpinnerTouched);
//      //TODO check that in dataFrag we set up the recordview recordFrag.setData(selectedItem);
//    }//if(!selectedIt.equals(storedIt))
//    else 
//    {
//      //Log.d(TAG, "just doodled with the spinner..."); 
//      dataFrag.setItem(selectedIt, lastSpinnerTouched);
//    }
    dataFrag.setItem(selectedIt, spinViews.size()-1);
    

  /*  for (int index = spinViews.size() - 1; index > lastSpinnerTouched; index--)
    {
      //Log.d(TAG, "reducing path at index " + index);
      
      remove(index);
      
      dataFrag.remove(index);
    }//for(int index = spinViews.size() -1; index > lastSpinnerTouched; index-- )
    */
    if (viewContainer != null)  viewContainer.invalidate();
    if (viewContainer != null)  viewContainer.refreshDrawableState();
    //Log.e(TAG, "ehm selected inexistend spinner item?? " + adapterPos);
    //finished here, but if we doodle, send back we selected the last default entry...
    //lastSpinnerTouched = spinViews.size() - 1;
    //Log.d(TAG, "------ end set last touched " + lastSpinnerTouched);
  }//private void changePath()
  /**
  remove
  */
  public void remove(int index)
  {
    if (index <= 0)
    {
      //Log.e(TAG, "we shouldn't try remove the first spinner baka.... " + index);
      return;
    }
    //if (index > 0)lastSpinnerTouched = index - 1;
    //else lastSpinnerTouched = 0;
    
    Spinner spin = spinViews.remove(index);
    //E actIt = dataFrag.extractData(spin.getSelectedItem(), spin.getSelectedItemPosition());
    spin.setAdapter(null);
    LinearLayout parent = (LinearLayout) spin.getParent();
    if (parent != null) 
    {
      parent.removeView(spin); 
      parent.invalidate();
      parent.refreshDrawableState();
    }//if (parent != null) 
    
  }//public void remove(int index)

  /*
   @Override
   public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
   {

   if (view == null) return; //startup! just ignore this buggy implementation

   lastSpinnerTouched = spinViews.indexOf(parent);
   Log.d(TAG, "set touched to " + lastSpinnerTouched + " from pos " + pos + " of " + view);
   }//public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)



   @Override
   public void onNothingSelected(AdapterView<?> parent)
   {
   lastSpinnerTouched = -1;
   Log.d(TAG, "reset touched to " + lastSpinnerTouched);
   }//public void onNothingSelected(AdapterView<?> parent)
   */
  public void redrawPathView()
  {
    if (viewContainer != null)  
    {
      viewContainer.removeAllViews();
      int i = 0,count = spinViews.size();
      for (Spinner sp : spinViews)
      {
        viewContainer.addView(sp);
        if (i < count)
        {
          TextView slash = new TextView(context);
          slash.setText("/");
          viewContainer.addView(slash);
        } 
        i++;
      }
      viewContainer.invalidate();
      viewContainer.refreshDrawableState();
    }//if (viewContainer != null)  

  }//public void redrawPathView()

}//public class PathViewFragment extends Fragment
