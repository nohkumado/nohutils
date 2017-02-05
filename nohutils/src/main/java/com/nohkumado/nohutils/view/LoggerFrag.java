package com.nohkumado.nohutils.view;
import android.app.*;
import android.graphics.*;
import android.os.*;
import android.text.*;
import android.text.method.*;
import android.util.*;
import android.view.*;
import android.view.ViewTreeObserver.*;
import android.widget.*;
import com.nohkumado.nohutils.*;
import java.util.*;
import android.view.ViewGroup.*;

public class LoggerFrag extends Fragment
{
  protected ArrayList<String> content = new ArrayList<>();
  protected int max_lines;

  protected View viewContainer;
  protected TextView textFrame;

  public final static String TAG = "Logger";

  private int lastLines;

  public void setTextView(TextView alternate)
  {
    textFrame = alternate;
    refresh();
  }
  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    setRetainInstance(true);
    viewContainer = super.onCreateView(inflater, container, savedInstanceState);

    if (viewContainer == null)
    {
      viewContainer = inflater.inflate(com.nohkumado.nohutils.R.layout.loggerfrag, container);
    }
    //Log.d(TAG, "arg container : " + container + " inflated one  : " + viewContainer);
    textFrame = (TextView) viewContainer.findViewById(R.id.loggerview);

    textFrame.setText("starting up");
    if (max_lines > 0) textFrame.setMaxLines(max_lines);
    if (max_lines <= 0)
    {
      StringBuilder sb = new StringBuilder();
      for (int i= 0; i < 256; i++)
      {
        sb.append("\n");
      }
      textFrame.setText(sb.toString());
      //Log.d(TAG, "extracted frame " + textFrame + " " + textFrame.getWidth() + ":" + textFrame.getHeight());

/*
      textFrame.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
          @Override
          public void onGlobalLayout()
          {
            final int fragmentWidth = viewContainer.findViewById(R.id.logc).getWidth();
            if (fragmentWidth != 0)
            {
              viewContainer.findViewById(R.id.loggerview).getLayoutParams().width = fragmentWidth;
            }
          }
        });
*/

    }
    /*
     ViewTreeObserver vto = this.textFrame.getViewTreeObserver();
     vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

     public void onGlobalLayout() {
     ViewTreeObserver obs = textFrame.getViewTreeObserver();
     obs.removeGlobalOnLayoutListener(this);
     Log.d(TAG,"#######Line Count is : " + textFrame.getLineCount());

     }
     });
     */
    return viewContainer;
  }

  public void clear()
  {
    content.clear();
    refresh();
  }

  public void setMaxLines(int max_lines)
  {
    this.max_lines = max_lines;
  }

  public int getMaxLines()
  {
    return max_lines;
  }

  public LoggerFrag add(String aLine)
  {
    //Log.d(TAG, "adding " + aLine);
    content.add(aLine);
    if (max_lines > 0)
    {
      while (content.size() > max_lines) content.remove(0);
    }
    if (textFrame != null)
    {
      refresh();
    }
    return this;
  }

  /**

   */
  private void refresh()
  {
    int height    = textFrame.getHeight();
    int scrollY   = textFrame.getScrollY();
    Layout layout = textFrame.getLayout();

    if (layout != null)
    {
      int firstVisibleLineNumber = layout.getLineForVertical(scrollY);
      int lastVisibleLineNumber  = layout.getLineForVertical(scrollY + height);
      int totalLines = lastVisibleLineNumber - firstVisibleLineNumber;

      //Log.d(TAG,"data:"+content.size()+"vis lin = "+totalLines+" vs lc:"+textFrame.getLayout().getLineCount());


      if ((totalLines == lastLines) && totalLines < content.size())
      {
        textFrame.setMaxLines(lastLines);
        textFrame.setScrollContainer(true);
        textFrame.setMovementMethod(new ScrollingMovementMethod());
        textFrame.setGravity(Gravity.BOTTOM);
        //while(totalLines < content.size()) content.remove(0);
      }
      lastLines = totalLines;
    }



    final StringBuilder sb = new StringBuilder();
    for (String line : content) sb.append(line).append("\n");
    //StringBuilder debug = new StringBuilder();
    //debug.append("refresh about to print ");
    //.append(sb).append(" ")
    //debug.append(content.size()).append("/");
    //if(textFrame.getLayout() == null) debug.append("unknown");
    //else debug.append(textFrame.getLayout().getLineCount());
    //Log.d(TAG, debug.toString());
    getActivity().runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          textFrame.setText(sb.toString());
          textFrame.invalidate();
          if(textFrame.getHeight() == 0) textFrame.setHeight(LayoutParams.FILL_PARENT);
          if(textFrame.getWidth() == 0) textFrame.setWidth(LayoutParams.FILL_PARENT);
          //Log.d(TAG, "set screen to  " + sb);

          ViewParent obj = viewContainer.getParent();
          if (obj instanceof ScrollView)
          {
            ScrollView scroll_view = (ScrollView) obj;
            scroll_view.scrollTo(0, scroll_view.getBottom());
            //scroll_view.post(scroll);
            scroll_view.fullScroll(View.FOCUS_DOWN);
          }
          else
          {
            //fixed size window....

            //Log.d(TAG,"chekcing size\n");
          }

        }
      });
  }

  /*public Layout getLayout()
   {
   if (textFrame != null) return textFrame.getLayout();
   return null;
   }

   public int getScrollY()
   {    if (textFrame != null) return textFrame.getScrollY();

   return 0;
   }

   public int getHeight()
   {
   if (textFrame != null) return textFrame.getHeight();
   return 0;
   }*/
  public int getDisplayWidth()
  {
    int result = 20;
    if (textFrame != null)
    {
      result = (int) (textFrame.getWidth() / textFrame.getTextSize());
    }
    return result;
	}
  public ViewParent getParent()
  {
    if (viewContainer != null) return viewContainer.getParent();
    return null;
  }

  public void setTypeface(Typeface chosenFont)
  {
    if (textFrame != null) textFrame.setTypeface(chosenFont);
  }

 /* public void setChildLayoutParams(ViewGroup.LayoutParams layout)
  {
    if(textFrame != null)
    {
      textFrame.setLayoutParams(layout);
      Log.d(TAG,"set the params of textviiew to "+layout);
    }
    // TODO: Implement this method
  }*/

  public int getTextHeight()
  {
    if (textFrame != null) return textFrame.getHeight();
    return 0;
  }

}//class
