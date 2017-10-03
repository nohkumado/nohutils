package com.nohkumado.nohutils;

import android.view.*;
import android.widget.*;
import com.nohkumado.nohutils.view.*;

@SuppressWarnings({"WeakerAccess", "CanBeFinal"})
public class PrintOnTextView implements Runnable
{
  LoggerFrag out;
  StringBuilder sb;

  public PrintOnTextView(LoggerFrag o, StringBuilder s)
  {
    out = o;
    sb = s;
  }  

  @Override
  public void run()
  {
    out.add(sb.toString());

    ViewParent obj = out.getParent();
    if (obj instanceof ScrollView)
    {
      ScrollView scroll_view = (ScrollView) obj;
      
      DownScroller scroll = new DownScroller(scroll_view);
      scroll_view.post(scroll);
      scroll_view.fullScroll(View.FOCUS_DOWN);
    }
    //out.invalidate();
  }
  
}
