package com.nohkumado.nohutils.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Html;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nohkumado.nohutils.BuildConfig;
import com.nohkumado.nohutils.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;

/**
 * Created by bboett on 08.03.18.
 * the fragment to create an about dialog
 */

public class AboutDialogFragment extends DialogFragment
{
  private static final String TAG = "ADia";
  private static Context context;
  private String info;
  private String legal;

  @SuppressLint("ValidFragment")
  public AboutDialogFragment(Context mainActivity)
  {
    super();
    context = mainActivity;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    // Get the layout inflater
    LayoutInflater inflater = getActivity().getLayoutInflater();

    View diaView = inflater.inflate(R.layout.about, null);
    int resourceId = context.getResources().getIdentifier("app_name", "string", context.getPackageName());
    if(resourceId > 0)
    {
      ((TextView) diaView.findViewById(R.id.about_title)).setText(context.getResources().getString(resourceId));
    }
    ((TextView) diaView.findViewById(R.id.about_version)).setText(version());

    ((TextView) diaView.findViewById(R.id.v_label)).setText(firstCap(context.getResources().getString(R.string.version)));
    ((TextView) diaView.findViewById(R.id.cpy_label)).setText(firstCap(context.getResources().getString(R.string.copyright)));
    ((TextView) diaView.findViewById(R.id.about_copyright)).setText(context.getResources().getString(R.string.copyright_date));

    TextView tv = ((TextView) diaView.findViewById(R.id.about_contact));
    tv.setText(context.getResources().getString(R.string.about_contact));
    //tv.setLinkTextColor(Color.WHITE);
    Linkify.addLinks(tv, Linkify.ALL);


    if(legal != null && !"".equals(legal))
    {
      tv = (TextView) diaView.findViewById(R.id.legal_text);
      tv.setText(legal);
      Log.d(TAG,"set legal to "+legal);
    }

    //TextView tv = (TextView) diaView.findViewById(R.id.info_text);
    //tv.setText(Html.fromHtml(info));
    //tv.setLinkTextColor(Color.WHITE);
    //Linkify.addLinks(tv, Linkify.ALL);
    //Log.d(TAG,"set body to "+info);
    // Inflate and set the layout for the dialog
    // Pass null as the parent view because its going in the dialog layout
    builder.setView(diaView);

    builder.setTitle(R.string.about_msg)
    //builder.setView(R.layout.about)
        //builder.setMessage(Html.fromHtml(body))
        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int id)
          {
          }
        })
    /*    .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener()
        {
          public void onClick(DialogInterface dialog, int id)
          {
            // User cancelled the dialog
          }
        })*/
    ;
    // Create the AlertDialog object and return it

    return builder.create();
  }

  public static String readRawTextFile(int id)
  {
    InputStream inputStream = context.getResources().openRawResource(id);

    InputStreamReader in = new InputStreamReader(inputStream);
    BufferedReader buf = new BufferedReader(in);

    String line;

    StringBuilder text = new StringBuilder();
    try
    {

      while((line = buf.readLine()) != null) text.append(line);
    } catch(IOException e)
    {
      return null;

    }
    return text.toString();
  }

  public void setInfo(String info)
  {
    this.info = info;
  }

  public void setLegal(String s)
  {
    legal = s;
  }

  public String version()
  {
    Field[] fields = BuildConfig.class.getFields();
    boolean aide_bug = true;
    int versionCode = 10;//AIDE bug BuildConfig.VERSION_CODE;
    String versionName = "1.0";//BuildConfig.VERSION_NAME;

    for (Field af : fields)
    {
      try
      {
        if ("VERSION_NAME".equals(af.getName()))//ok, studio version
        {
          versionName = "" + af.get(BuildConfig.class);
          aide_bug = false;
        }
        if ("VERSION_CODE".equals(af.getName()))//ok, studio version
        {
          versionCode = af.getInt(BuildConfig.class);
          aide_bug = false;
        }
      }
      catch (IllegalAccessException e)
      {
      }
    }

    if (aide_bug)
    {
      try
      {
        versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
      }
      catch (PackageManager.NameNotFoundException ignored)
      { }
    }//if(aide_bug)
Log.d(TAG," returning version "+versionCode + "(" + versionName + ")");
    return versionCode + "(" + versionName + ")";
  }
  public String firstCap(String msg)
  {
    return msg.substring(0, 1).toUpperCase() + msg.substring(1);
  }
}
