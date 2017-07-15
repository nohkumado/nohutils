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

public class PathViewFragment<E> extends Fragment implements OnClickListener,OnItemSelectedListener
{
  public final static String TAG = "PVF";
  private ArrayList<Integer> addLater = new ArrayList<>();

  protected  ArrayList<Spinner> spinViews = new ArrayList<Spinner>();

  private LinearLayout viewContainer;

  private ImageButton pathChangeBut;
  protected int lastSpinnerTouched = -1;

  private DataStorageFrag<E> dataFrag;

  private Context context;

  private ImageButton pathUpBut;
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
    Log.d(TAG, "createspinnerfor " + ".... " + pos);

    //dataFrag.setItem(actItem, pos);

    BaseAdapter sAdapter = dataFrag.getAdapter(pos);
    if (sAdapter != null) 
    {
      Log.d(TAG, "valid adapter");
      if (spinViews.size() <= pos) 
      {
        Spinner v = new SelectAgainSpinner(context);
        Log.d(TAG, "created new spinner");
        //v.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //lastSpinnerCreation = new Date().getTime();
        v.setOnItemSelectedListener(this);
        v.setAdapter(sAdapter);
        Log.d(TAG, "check container : " + viewContainer);
        if (viewContainer != null)
        {
          LinearLayout container = (LinearLayout) viewContainer.findViewById(R.id.treeselector);
          Log.d(TAG, "adding spinner to container");
          //spinInit.add(true);
          spinViews.add(v);
          container.addView(v);
          container.invalidate();
          container.refreshDrawableState();
        }//if(viewContainer != null)
        else
        {
          Log.d(TAG, "no container defined");
          addLater.add(pos);
        }

      }//if (spinViews.size() <= pos) 
      else
      {
        Spinner v =  spinViews.get(pos);
        //spinInit.set(pos, true);
        v.setAdapter(sAdapter);
        Log.d(TAG, "swapping spinner adapter adapter");
      }
    }//if (sAdapter != null)
    else Log.d(TAG, "adapter was null....");

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
    
    
    if (addLater.size() > 0)
    {
      for (int n: addLater)
      {
        createSpinnerForPath( n);
      }
      addLater.clear();
    }//if(addLater.size() > 0)

    return viewContainer;
  }//public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  @Override
  public void onClick(View knopf)
  {
    if (knopf == pathChangeBut) changePath();  
    else if (knopf == pathUpBut) changePath();
  }//public void onClick(View knopf)

  private void changePath()
  {
    Log.d(TAG,"##############entering changePath with "+lastSpinnerTouched);
    if (lastSpinnerTouched < 0)
    {
      //no spinner changed, touched etc...
      Log.d(TAG, "no spinner touched, change path invalid, bailing");
      return;
    }//if (lastSpinnerTouched < 0)

    E storedIt = dataFrag.getItem(lastSpinnerTouched);
    Spinner actSpin = spinViews.get(lastSpinnerTouched);
    E selectedIt = dataFrag.extractData(actSpin.getSelectedItem(), actSpin.getSelectedItemPosition());
    if (!selectedIt.equals(storedIt))    
    {
      Log.d(TAG, "no the same! exchanging items in dataFrag");
      dataFrag.setItem(selectedIt, lastSpinnerTouched);
      //TODO check that in dataFrag we set up the recordview recordFrag.setData(selectedItem);
    }//if(!selectedIt.equals(storedIt))
    else 
    {
      Log.d(TAG, "just doodled with the spinner..."); 
      dataFrag.setItem(selectedIt, lastSpinnerTouched);
      }
    

    for (int index = spinViews.size() - 1; index > lastSpinnerTouched; index--)
    {
      Log.d(TAG, "reducing path at index "+index);
      dataFrag.remove(index);
      if (viewContainer != null)  viewContainer.removeView(spinViews.remove(index));
    }//for(int index = spinViews.size() -1; index > lastSpinnerTouched; index-- )
    if (viewContainer != null)  viewContainer.invalidate();
    if (viewContainer != null)  viewContainer.refreshDrawableState();
    //Log.e(TAG, "ehm selected inexistend spinner item?? " + adapterPos);
    //finished here, but if we doodle, send back we selected the last default entry...
    lastSpinnerTouched = spinViews.size() -1;
    Log.d(TAG,"------ end set last touched "+lastSpinnerTouched);
  }//private void changePath()



  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)
  {
    if (view == null) return; //startup! just ignore this buggy implementation

    lastSpinnerTouched = spinViews.indexOf(parent);
  }//public void onItemSelected(AdapterView<?> parent, View view, int pos, long id)



  @Override
  public void onNothingSelected(AdapterView<?> parent)
  {
    lastSpinnerTouched = -1;
  }//public void onNothingSelected(AdapterView<?> parent)

}//public class PathViewFragment extends Fragment
