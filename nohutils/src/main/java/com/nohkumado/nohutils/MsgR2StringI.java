package com.nohkumado.nohutils;

import android.content.*;
import android.content.res.*;
import java.io.*;

public interface MsgR2StringI
{

	public void display(String path, String type);

	public String msg(int stringid);
	public String msg(String stringid);
	//from Context...
	public SharedPreferences getSharedPreferences(String pckage,int mode);
	public Resources getResources();
	public String getPackageName();
	public void startActivity (Intent intent);
	public File getExternalFilesDir(String type);
	public void playSound();
}
