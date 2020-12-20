package com.nohkumado.nohutils;
import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import com.nohkumado.nohutils.view.*;

public class ShellFragment extends Fragment
{
	private final static String TAG ="SFrag";

	private ShellI shell;

	private MsgR2StringI handler;

  public ShellFragment()
	{
	}

	public void callback(MsgR2StringI callback)
	{
		handler = callback; //need? need not?
    handler.giveShellCommands(shell());
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if(context instanceof MsgR2StringI)
		{
			if(handler == null || !handler.equals(context))
			{
				//Log.d(TAG,"attaching "+context+" instead of "+handler);
				handler = (MsgR2StringI) context;
				shell().setContext(handler);
				handler.giveShellCommands(shell());
			}//if(handler == null || !handler.equals(context))
		}//if(context instanceof MsgR2StringI)
		else Log.e(TAG,"couldn't attach context, no MsgR2StringI... "+context);
	}//public void onAttach(Context context)

	public void shell(ShellI shell)
	{
		this.shell = shell;
	}

	public ShellI shell()
	{
		if (shell == null) shell = new Shell(handler);
		return shell;
	}

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		shell();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setRetainInstance(true);
    View viewContainer = super.onCreateView(inflater, container, savedInstanceState);
		
		if (viewContainer == null)
		{
			//Log.d(TAG,"inflating shellview");
			viewContainer = inflater.inflate(R.layout.shellviewfrag, container,false);
			//viewContainer = (RelativeLayout)inflater.inflate(R.layout.shellviewfrag, null,false);
		}
		EditText cmdLine = viewContainer.findViewById(R.id.shell_text_in);
		//FragmentManager fm = getFragmentManager();
		FragmentManager fm = getChildFragmentManager();
		LoggerFrag logger = (LoggerFrag)fm.findFragmentById(R.id.shell_text_out);
		if (logger == null)
		{
			//Log.d(TAG,"logger was null... error...");
			//for(int index=0; index<((ViewGroup)viewContainer).getChildCount(); ++index) {
			//	View nextChild = ((ViewGroup)viewContainer).getChildAt(index);
			//	Log.d(TAG,"child["+index+"] "+nextChild);
			//}
			logger = (LoggerFrag)fm.findFragmentByTag("screen");
			if (logger == null) 
			{
				logger = new LoggerFrag(); //well everything failed....
				//Log.e(TAG,"ultra bug... had to create the fragment");
			}
			
			ViewParent parent = logger.getParent();
			//Log.e(TAG,"fragment parent "+parent);
			
			if(parent == null)
			{
				//Log.e(TAG,"replacing logger");
				fm.beginTransaction().replace(R.id.shell_text_out, logger, "screen").commit();
				
			} else Log.e(TAG,"can't replace logger ");
				//Log.e(TAG,"done with logger");
		}//if (logger == null)
		//logger.add("Shellfrag started logger");
		shell().setInOut(cmdLine, logger);
		shell.init();
		//shell.print("Welcome ");*/
		return viewContainer;
	}//public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
}//public class ShellFragment extends Fragment