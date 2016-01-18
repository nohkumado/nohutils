package com.nohkumado.nohutils;

import android.app.*;
import android.content.res.*;
import android.media.*;
import android.util.*;

public class Msg2RString extends Activity implements MsgR2StringI
{

	public static final String TAG = "msg2Str";
	
	@Override
	public String msg(int stringid)
	{
		try
		{
			//int resourceId = context.getResources().getIdentifier(m, "strings", context.getPackageName());
			return(getResources().getString(stringid));
		}
		catch (Resources.NotFoundException e)
		{ Log.e(TAG, "not found message : " + stringid);}
		return("MSGNOTFOUND");
	}

	@Override
	public String msg(String stringid)
	{
		int resourceId = getResources().getIdentifier(stringid, "strings", getPackageName());
		return msg(resourceId);
	}
	/**
	 */
	public void playSound()
	{
		final ToneGenerator tg = new ToneGenerator(AudioManager.STREAM_NOTIFICATION, 100);
		tg.startTone(ToneGenerator.TONE_PROP_BEEP);
		// Double beeps:     tg.startTone(ToneGenerator.TONE_PROP_ACK);
		// Double beeps:     tg.startTone(ToneGenerator.TONE_PROP_BEEP2);
		// Sounds all wrong: tg.startTone(ToneGenerator.TONE_CDMA_KEYPAD_VOLUME_KEY_LITE); 
    // Double beeps:     tg.startTone(ToneGenerator.TONE_DTMF_0);
    // Double beeps:     tg.startTone(ToneGenerator.TONE_DTMF_S);

		/* this one works
		 try
		 {
		 Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		 Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
		 r.play();
		 }
		 catch (Exception e)
		 {
		 e.printStackTrace();
		 }
		 */

		/*
		 try
		 {
		 Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		 MediaPlayer mMediaPlayer = new MediaPlayer();
		 mMediaPlayer.setDataSource(this, soundUri);
		 final AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		 if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0)
		 {
		 mMediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
		 //mMediaPlayer.setLooping(true);
		 mMediaPlayer.prepare();
		 mMediaPlayer.start();
		 }
		 }
		 catch (IllegalArgumentException ia)
		 {Log.e(TAG, "illegal arg: " + ia);} 
		 catch (SecurityException ia)
		 {Log.e(TAG, "security: " + ia);}
		 catch (IllegalStateException ia)
		 {Log.e(TAG, "illegal state: " + ia);}
		 catch (IOException ia)
		 {Log.e(TAG, "io ex: " + ia);}
		 */
	}
	
}
