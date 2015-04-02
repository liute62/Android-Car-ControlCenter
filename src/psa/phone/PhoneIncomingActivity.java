package psa.phone;

import psa.music.AppConstant;
import psa.music.MusicActivity;
import psa.music.PlayerService;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class PhoneIncomingActivity extends Activity implements OnClickListener{
	
	private Button phoneRejectBtn;
	private Button phoneConfirmBtn;
	private Typeface tfPhoneIncoming;
	private Typeface tfPhoneIncoming2;
	private TextView incomingName;
	private TextView incomingNum;
	private MediaPlayer mPlayer;
	private RelativeLayout mProfile;

	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) 
		{
			if(msg.what == Utils.phone_answer){
				String result = (String)msg.obj;
				if (result.equals("accept")) {
					Utils.isPhoneIncoming = false;
					
				}if (result.equals("reject")) {
					PhoneIncomingActivity.this.finish();
					Utils.isPhoneIncoming = false;
					reject();
				}
			}
		};
	};
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_phone_incoming);
		Utils.comingOrCalling = 0;
		MyApplication.serverStop();
		MyApplication.getInstance().setPhoneIncomingHandler(mHandler);
		MyApplication.serverStart();
		if (Utils.isMusicPlaying) {
			Intent intent = new Intent();
			intent.putExtra("url", Utils.currentMp3Info.getUrl());
			intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
			intent.setClass(getApplicationContext(), PlayerService.class);
			startService(intent);
			Utils.isMusicPlaying = false;
			Utils.isMusicPause = true;
		}
		mPlayer = MediaPlayer.create(getBaseContext(), R.raw.phoneincoming);  
		mPlayer.start();
		mPlayer.setLooping(true);
		initial();
		mProfile = (RelativeLayout)findViewById(R.id.phone_incoming_profile);
		if (Utils.currentScene == 1) {
			mProfile.setBackgroundDrawable(getResources().getDrawable(R.drawable.n10086));
			incomingNum.setText("18001982735");
			incomingName.setText("ÄäÃû");
		}
		
	}


	private void initial() {
		// TODO Auto-generated method stub
		tfPhoneIncoming = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCNLight.ttf");
		tfPhoneIncoming2 = Typeface.createFromAsset(getAssets(),
				"fonts/nexalight.ttf");
		
		
		phoneConfirmBtn = (Button)this.findViewById(R.id.phone_incoming_confirm_btn);
		phoneConfirmBtn.setOnClickListener(this);
		phoneRejectBtn = (Button)this.findViewById(R.id.phone_incoming_reject_btn);
		phoneRejectBtn.setOnClickListener(this);
		incomingName = (TextView)this.findViewById(R.id.phone_incoming_contact_name);
		incomingNum  =(TextView)this.findViewById(R.id.phone_incoming_contact_num);
		incomingName.setTypeface(tfPhoneIncoming);
		incomingNum.setTypeface(tfPhoneIncoming2);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.phone_incoming_confirm_btn:
			break;
			
		case R.id.phone_incoming_reject_btn:
			reject();
			Utils.isPhoneIncoming = false;
			this.finish();
			break;

		default:
			break;
		}
		
			
	}
	private MediaPlayer JinglePlayer;
	
	private void jingle(){
		JinglePlayer = MediaPlayer.create(getBaseContext(), R.raw.jingle2_msg_reject); 
	    if(JinglePlayer!=null){
	    	JinglePlayer.start();
	    }
	}
	
	private void reject(){
		
		if (mPlayer != null) {
			mPlayer.release();
		}
		jingle();
	}
	
	boolean isPhoneIncomingFinished = true;
	class PhoneIncomingTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPhoneIncomingFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneIncomingUsing(true);
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
			isPhoneIncomingFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneIncomingUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
}
