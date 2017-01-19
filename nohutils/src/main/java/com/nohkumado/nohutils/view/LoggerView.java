package com.nohkumado.nohutils.view;
import android.content.*;
import android.util.*;
import android.widget.*;
import java.util.*;
import android.app.*;
import android.view.*;
import android.os.*;
import com.nohkumado.nohutils.R;

public class LoggerView extends Fragment
{
  protected ArrayList<String> content = new ArrayList<>();
  protected int max_lines;
  protected TextView textFrame;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
  {
    View v = super.onCreateView(inflater, container, savedInstanceState);
    if(v == null)
    {
      v = inflater.inflate(R.layout.loggerfrag,null);
    }
    textFrame = (TextView) v.findViewById(R.id.loggerview);
    if(max_lines>0) textFrame.setMaxLines(max_lines);
    return v;
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

  public LoggerView add(String aLine)
  {
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
    getActivity().runOnUiThread(new Runnable()
      {
        @Override
        public void run()
        {
          textFrame.setText(sb.toString());
          textFrame.invalidate();
        }
      });
  }


}//class
