package psa.phone;

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
import android.widget.TextView;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class MsgIncomingActivity extends Activity implements OnClickListener{
	private Button msgComSendBtn;
	private Button msgComRejectBtn;
	private TextView msgIncomingName;
	private TextView msgIncomingContent;
	private Typeface tf;
	private MediaPlayer msgTestMediaPlayer;
	private MediaPlayer msgSayContentMediaPlayer;
	Handler mHandler = new Handler(){
		public void handleMessage(android.os.Message msg)
		{
			if(msg.what == Utils.msg_edit){
				//sayMsgContent();
				Intent mIntent = new Intent(MsgIncomingActivity.this,MsgActivity.class);
				startActivity(mIntent);
				Utils.isMsgIncoming = false;
			}
		};
	};
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_msg_incoming);
		initial();
		IncomingVoice();
		MyApplication.serverStop();
		MyApplication.getInstance().setMsgEditHandler(mHandler);
		MyApplication.serverStart();
	}
	 private void sayMsgContent(){
		 msgSayContentMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.msg_edit); 
		    if(msgSayContentMediaPlayer!=null){
		    	msgSayContentMediaPlayer.start();
		    	
		    }
	 }

	private void IncomingVoice() {
		// TODO Auto-generated method stub
		msgTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.new_message); 
	    if(msgTestMediaPlayer!=null){
	    	msgTestMediaPlayer.start();
	    	
	    }
	}

	private void initial() {
		// TODO Auto-generated method stub
		tf = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		msgComSendBtn = (Button)this.findViewById(R.id.msg_incoming_confirm_btn);
		msgComRejectBtn = (Button)this.findViewById(R.id.msg_incoming_reject_btn);
		msgComSendBtn.setOnClickListener(this);
		msgComRejectBtn.setOnClickListener(this);
		msgIncomingName = (TextView)this.findViewById(R.id.msg_incoming_contact_name);
		msgIncomingContent = (TextView)this.findViewById(R.id.msg_incoming_contact_num);
		msgIncomingName.setTypeface(tf);
		msgIncomingContent.setTypeface(tf);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.msg_incoming_confirm_btn:
			Intent mIntent = new Intent(MsgIncomingActivity.this,MsgActivity.class);
			startActivity(mIntent);
			break;
		case R.id.msg_incoming_reject_btn:
			Utils.isMsgIncoming = false;
			Intent mIntent2 = new Intent(MsgIncomingActivity.this,MainActivity.class);
			mIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mIntent2);
			this.finish();
			break;

		default:
			break;
		}
	}
	
	private boolean isMsgIncomingFinished = true;
	class MsgIncomingTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isMsgIncomingFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgIncomingUsing(true);
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
			isMsgIncomingFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgIncomingUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	

}
