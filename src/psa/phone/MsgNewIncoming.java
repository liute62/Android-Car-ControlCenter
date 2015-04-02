package psa.phone;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class MsgNewIncoming extends Activity implements OnClickListener{
	private Button msgCheckBtn;
	private MediaPlayer Jingle;
	private MediaPlayer reject;
	
	
	
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == Utils.msg_check) {
				
				Intent mIntent = new Intent(MsgNewIncoming.this,MsgIncomingActivity.class);
				startActivity(mIntent);
			}
			if (msg.what == Utils.msg_ignore) {
				voiceReject();
				MsgNewIncoming.this.finish();
			}
		};
	};
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.msg_new_incoming);
		MyApplication.serverStop();
		MyApplication.getInstance().setMsgIncomingHandler(mHandler);
		MyApplication.serverStart();
		initial();
		VoiceJingle();
	}


	private void VoiceJingle() {
		// TODO Auto-generated method stub
		Jingle = MediaPlayer.create(getBaseContext(), R.raw.jingle1_dot); 
	    if(Jingle!=null){
	    	Jingle.start();
	    	
	    }
	}
	
	private void voiceReject(){
		reject = MediaPlayer.create(getBaseContext(), R.raw.jingle2_msg_reject); 
	    if(reject!=null){
	    	reject.start();
	    	
	    }
	}


	private void initial() {
		// TODO Auto-generated method stub
		msgCheckBtn = (Button)this.findViewById(R.id.msg_check_btn);
		msgCheckBtn.setOnClickListener(this);
	}

	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.msg_check_btn:
			Intent mIntent = new Intent(MsgNewIncoming.this,MsgIncomingActivity.class);
			startActivity(mIntent);
			break;
		case R.id.msg_cancel_btn:
			this.finish();
		default:
			break;
		}
	}
	
	private boolean isMsgNewIncomingFinished = true;
	class PhoneCallingTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isMsgNewIncomingFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgNewIncomingUsing(true);
			MyApplication.serverStart();
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String voice = arg0[0];
			return 1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			isMsgNewIncomingFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgNewIncomingUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}

}

