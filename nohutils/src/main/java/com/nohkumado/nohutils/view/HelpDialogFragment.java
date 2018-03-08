package com.nohkumado.nohutils.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.nohkumado.nohutils.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by bboett on 08.03.18.
 * Manual screen
 */

public class HelpDialogFragment extends DialogFragment
{
  private static final String TAG = "HelpFrag";
  private final Context context;

  public HelpDialogFragment(Context ctxt)
  {
    super();
    context = ctxt;
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState)
  {
    // Use the Builder class for convenient dialog construction
    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


    SpannableString content = new SpannableString(Html.fromHtml( readRawTextFile(R.raw.manual)));
 /*   SpannableString content = new SpannableString(Html.fromHtml( readRawTextFile(R.raw.manual)));
    Log.e(TAG,"gfailed to inkify "+content);


    //tv.setLinkTextColor(Color.WHITE);
    if(!Linkify.addLinks(content, Linkify.ALL))
    {
      Log.e(TAG,"gfailed to inkify "+content);

    }
    else
      Log.e(TAG,"success to inkify "+content);
*/


    builder.setTitle(R.string.manual_title)
        //.setMessage(content)
        .setMessage(content)
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
    AlertDialog d = builder.create();
    // Create the AlertDialog object and return it
    //((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());

    return d;
  }
  public String readRawTextFile(int id)
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
  @Override
  public void onStart() {
    super.onStart();

    // Make the dialog's TextView clickable
    ((TextView)this.getDialog().findViewById(android.R.id.message))
        .setMovementMethod(LinkMovementMethod.getInstance());
  }

}
