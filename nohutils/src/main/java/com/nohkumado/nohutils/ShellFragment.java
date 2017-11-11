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

	private View viewContainer;
	
	public ShellFragment(MsgR2StringI callback)
	{
		handler = callback; //need? need not?
	}

	@Override
	public void onAttach(Context context)
	{
		super.onAttach(context);
		if(context instanceof MsgR2StringI)
		{
			if(handler == null || !handler.equals(context))
			{
				Log.d(TAG,"attaching "+context+" instead of "+handler);
				handler = (MsgR2StringI) context;
				shell.setContext(handler);	
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
		if (shell == null) shell = new Shell(handler);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
	{
		setRetainInstance(true);
		viewContainer = super.onCreateView(inflater, container, savedInstanceState);
		
		if (viewContainer == null)
		{
			//viewContainer = (LinearLayout)inflater.inflate(R.layout.shellviewfrag, container,false);
			viewContainer = (RelativeLayout)inflater.inflate(R.layout.shellviewfrag, null,false);
		}
		/*EditText cmdLine = (EditText) viewContainer.findViewById(R.id.TextIn);
		LoggerFrag logger = (LoggerFrag)getFragmentManager().findFragmentById(R.id.textOut);
		if (logger == null)
		{
			logger = (LoggerFrag)getFragmentManager().findFragmentByTag("screen");
			if (logger == null) logger = new LoggerFrag(); //well everything failed....
			getFragmentManager().beginTransaction().replace(R.id.textOut, logger, "screen").commit();
		}//if (logger == null)
		shell.setInOut(cmdLine, logger);
		shell.init();
		//shell.print("Welcome ");*/
		return viewContainer;
	}//public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
}//public class ShellFragment extends Fragment

