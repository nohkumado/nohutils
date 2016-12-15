package com.nohkumado.nohutils;

import android.content.*;
import android.content.res.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;

public interface MsgR2StringI
{

  public SQLiteOpenHelper getDbHelper();


	public String[] msgArr(int resourceId);


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
	public String callback(String name, HashMap<String,Object> args);
  public void runOnUiThread (Runnable action);
}
