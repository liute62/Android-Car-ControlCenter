package app.psa;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;

public class MyBroadcast extends BroadcastReceiver{

	private Activity activity;

	public static final String CLOSE_ACTION = "app.close.action";
	
	Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) 
		{
			activity.finish();// πÿ±’ΩÁ√Ê
		}
	};

	public MyBroadcast(Activity activity)
	{
		this.activity = activity;
	}

	
	
	@Override
	public void onReceive(Context context, Intent intent)
	{
		handler.sendEmptyMessage(0);
	}

}
