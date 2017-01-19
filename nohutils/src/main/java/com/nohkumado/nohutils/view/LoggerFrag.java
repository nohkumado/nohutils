package com.nohkumado.nohutils.view;
import android.content.*;
import android.util.*;
import android.widget.*;
import java.util.*;
import android.app.*;
import android.view.*;
import android.os.*;
import com.nohkumado.nohutils.R;
import android.text.*;
import android.graphics.*;

public class LoggerFrag extends Fragment
{
  protected ArrayList<String> content = new ArrayList<>();
  protected int max_lines;

  protected View viewContainer;
  protected TextView textFrame;

  public final static String TAG = "Logger";

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    setRetainInstance(true);
    viewContainer = super.onCreateView(inflater, container, savedInstanceState);
    if (viewContainer == null)
    {
      viewContainer = inflater.inflate(com.nohkumado.nohutils.R.layout.loggerfrag, null);
    }
    textFrame = (TextView) viewContainer.findViewById(R.id.loggerview);
    textFrame.setText("starting up");
    if (max_lines > 0) textFrame.setMaxLines(max_lines);
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
    Log.d(TAG, "adding " + aLine);
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

  private void refresh()
  {
    final StringBuilder sb = new StringBuilder();
    for (String line : content) sb.append(line).append("\n");
    Log.d(TAG, "refresh about to print " + sb);
    getActivity().runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          textFrame.setText(sb.toString());
          textFrame.invalidate();
          Log.d(TAG, "set screen to  " + sb);

          ViewParent obj = viewContainer.getParent();
          if (obj instanceof ScrollView)
          {
            ScrollView scroll_view = (ScrollView) obj;
            scroll_view.scrollTo(0, scroll_view.getBottom());
            //scroll_view.post(scroll);
            scroll_view.fullScroll(View.FOCUS_DOWN);
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


}//class
