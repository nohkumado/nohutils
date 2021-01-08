package eu.nohkumado.nohutils.commands;

import android.app.*;
import android.content.pm.*;

import com.nohkumado.nohutils.BuildConfig;
import com.nohkumado.nohutils.R;

import java.lang.reflect.*;

import eu.nohkumado.nohutils.Command;
import eu.nohkumado.nohutils.ShellI;

/**
 * Created by bboett on 12.01.18.
 * gives back the version
 */

public class VersionCommand extends Command
{
	public VersionCommand(ShellI s)
	{
		super(s);
		if (s != null) name = s.msg(com.nohkumado.nohutils.R.string.version);
	}

	public VersionCommand(ShellI s, String n)
	{
		super(s, n);
	}

	@Override
	public String execute()
	{

		Field[] fields = BuildConfig.class.getFields();
		boolean aide_bug = true;
		int versionCode = 108;//AIDE bug BuildConfig.VERSION_CODE;
		String versionName = "1.08";//BuildConfig.VERSION_NAME;

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
				shell.error("" + e);
			}
			//catch (IllegalArgumentException e)
			//{
			//	shell.error("" + e);
			//}
			//shell.print("found field " + af.getName());
		}

		if (aide_bug)
		{
			try
			{
				Activity context = (Activity)shell.getContext();
				versionCode = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
				versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
			}
			catch (PackageManager.NameNotFoundException ignored)
			{ }
		}//if(aide_bug)
		
		return shell.msg(R.string.version) + " " + versionCode + "(" + versionName + ")";
	}
}//public class VersionCommand extends Command
