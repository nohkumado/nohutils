package eu.nohkumado.nohutils.view;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by bboett on 08.03.18.
 * a dialog to show some help
 */

public class HelpDialog extends AlertDialog
{
  protected HelpDialog(Context context)
  {
    super(context);
  }
  protected HelpDialog(Context context, String msg)
  {
    this(context);
  }

  protected HelpDialog(Context context, boolean cancelable, OnCancelListener cancelListener)
  {
    super(context, cancelable, cancelListener);
  }

  protected HelpDialog(Context context, int themeResId)
  {
    super(context, themeResId);
  }
}
