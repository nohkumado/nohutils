package com.nohkumado.nohutils;

import android.content.*;
import android.content.res.*;
import android.database.sqlite.*;
import java.io.*;
import java.util.*;

@SuppressWarnings({"SameReturnValue", "UnusedParameters"})
public interface MsgR2StringI
{

  SQLiteOpenHelper getDbHelper();


	String[] msgArr(int resourceId);


	void display(String path, String type);

	String msg(int stringid);
	String msg(String stringid);
	//from Context...
    SharedPreferences getSharedPreferences(String pckage, int mode);
	Resources getResources();
	String getPackageName();
	void startActivity(Intent intent);
	File getExternalFilesDir(String type);
	void playSound();
	String callback(String name, HashMap<String, Object> args);
  void runOnUiThread(Runnable action);
}
