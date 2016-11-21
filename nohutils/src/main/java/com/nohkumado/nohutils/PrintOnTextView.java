package com.nohkumado.nohutils;

import android.view.*;
import android.widget.*;

public class PrintOnTextView implements Runnable
{
  TextView out;
  StringBuilder sb;

  public PrintOnTextView(TextView o, StringBuilder s)
  {
    out = o;
    sb = s;
  }  

  @Override
  public void run()
  {
    out.setText(sb.toString());

    Object obj = out.getParent();
    if (obj instanceof ScrollView)
    {
      ScrollView scroll_view = (ScrollView) obj;
      DownScroller scroll = new DownScroller(scroll_view);
      scroll_view.post(scroll);
      scroll_view.fullScroll(View.FOCUS_DOWN);
    }
    out.invalidate();
  }
  
}
