package com.nohkumado.utilsapp;

import android.app.*;
import android.os.*;
import com.nohkumado.nohutils.*;
import android.content.*;
import android.view.*;
import android.util.*;
import android.widget.*;

public class MainActivity extends Activity 
{
	protected Shell shell = null;
	private final static String TAG ="MA";


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		Log.d(TAG, "********************* start ************************");
		if (shell == null) shell = new Shell(this);
		
		setContentView(R.layout.main);
		TextView screen = (TextView) findViewById(R.id.textOut);
		EditText cmdLine = (EditText) findViewById(R.id.TextIn);
		Log.d(TAG, "setting inout ");
		shell.setInOut(cmdLine, screen);
		Log.d(TAG, "calling init");
		shell.init();
		
	}
}
