package psa.phone;

import psa.navi.NaviRouteActivity;
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

public class MsgConfirmActivity extends Activity implements OnClickListener{
	private Typeface tfMsgConfirm;
	private TextView msgContent;
	private TextView msgHint;
	private Button confirmmSendBtn;
	private Button cancelSendBtn;
	private String sendSuccess;
	private MediaPlayer msgSendSuccTestMediaPlayer;

	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) 
		{
			sendMsgSuccess();
			Intent mIntent = new Intent(MsgConfirmActivity.this,MainActivity.class);
			startActivity(mIntent);
			
			//MsgConfirmActivity.this.finish();
		};
	};
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		MyApplication.getInstance().setMsgConfirmHandler(mHandler);
		setContentView(R.layout.view_msg_confirm_send);
		initial();
	}

	private void initial() {
		// TODO Auto-generated method stub
		tfMsgConfirm = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCNLight.ttf");
		msgContent  =(TextView)this.findViewById(R.id.msg_confirm_content_text);
		msgHint = (TextView)this.findViewById(R.id.msg_confirm_hint_text);	
		msgContent.setTypeface(tfMsgConfirm);
		msgHint.setTypeface(tfMsgConfirm);
		confirmmSendBtn = (Button)this.findViewById(R.id.msg_confirm_send_btn);
		cancelSendBtn = (Button)this.findViewById(R.id.msg_cancel_btn);
		confirmmSendBtn.setOnClickListener(this);
		cancelSendBtn.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.msg_confirm_send_btn:
			//Intent msgSendSuccIntent = new Intent(MsgConfirmActivity.this,MsgActivity.class);
			
			sendSuccess = "sendSuccess";
			Intent msgSendSuccIntent = new Intent();

			msgSendSuccIntent.putExtra("sendSuccess", sendSuccess);
			msgSendSuccIntent.setClass(MsgConfirmActivity.this,MsgActivity.class);

			sendMsgSuccess();
			startActivity(msgSendSuccIntent);
			break;
		case R.id.msg_cancel_btn:
			this.finish();
			break;

		default:
			break;
		}
	}


	private void sendMsgSuccess() {
		// TODO Auto-generated method stub
		msgSendSuccTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.send_success); 
	    if(msgSendSuccTestMediaPlayer!=null){
	    	msgSendSuccTestMediaPlayer.start();
	    	
	    }
	}
	
	private boolean isMsgConfirmFinished = true;
	class MsgConfirmTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isMsgConfirmFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgConfirmUsing(true);
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
			isMsgConfirmFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgConfirmUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	
	
}
