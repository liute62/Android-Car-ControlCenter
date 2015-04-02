package psa.phone;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import app.psa.Http;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class PhoneDialSureActivity extends Activity implements OnClickListener {
	private Button phoneReceiveBtn;
	private Button phoneRejectBtn;
	private Typeface tf;
	private TextView sureNum;
	private TextView sureHint;
	private int phone_select_index = -1;
	private String phone_select_name = " ";
	private ImageView mProfile;
	private TextView mName;

	Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.phone_callout) {
				if (Utils.currentScene == 2) {
					Intent mIntent = new Intent(PhoneDialSureActivity.this,PhoneCallingActivity.class);
					
					mIntent.putExtra("zhang", true);
					startActivity(mIntent);
				
				}if (Utils.currentScene == 3) {
					
				}
			}
		}
	};
	
	private MediaPlayer phoneTestMediaPlayer;

	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
		phone_select_name = getIntent().getStringExtra("PHONE_SELECT_NAME");
		initial();
		Log.e("PHONE_DIAL_SURE_INDEX resume", String.valueOf(phone_select_index));
		//dialSure();
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent); 
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
		phone_select_name = getIntent().getStringExtra("PHONE_SELECT_NAME");
		initial();
		Log.e("PHONE_DIAL_SURE_INDEX restart", String.valueOf(phone_select_index));
	}
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_phone_sure);
		dialSure();
		Utils.strangeOrDial = 1;
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
		phone_select_name = getIntent().getStringExtra("PHONE_SELECT_NAME");
		Log.e("PHONE_DIAL_SURE_INDEX", String.valueOf(phone_select_index));
		MyApplication.serverStop();
		MyApplication.getInstance().setPhoneDialSureHandler(mHandler);
		MyApplication.serverStart();
		initial();
	}

	private void initial() {
		// TODO Auto-generated method stub
		
		tf = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		phoneReceiveBtn  = (Button)this.findViewById(R.id.phone_confirm_dial_btn);
		phoneReceiveBtn.setOnClickListener(this);

		phoneRejectBtn = (Button)this.findViewById(R.id.phone_dial_cancel_btn);
		phoneRejectBtn.setOnClickListener(this);
		
		sureNum = (TextView)this.findViewById(R.id.phone_confirm_hint_name);
		sureNum.setTypeface(tf);
		sureHint = (TextView)this.findViewById(R.id.phone_confirm_hint_text);
		sureHint.setTypeface(tf);
		mName = (TextView)findViewById(R.id.phone_confirm_hint_name);
		mName.setText(phone_select_name);
		mProfile = (ImageView)findViewById(R.id.phone_confirm_profile);
		if (phone_select_index == 0) {
			//PhoneDiaSureTask mDiaSureTask = new PhoneDiaSureTask();
			//mDiaSureTask.execute("1");
		}
		switch (phone_select_index) {
		case 0:
			mProfile.setImageResource(Utils.phone_photo[0]);
			break;
		case 1:
			mProfile.setImageResource(Utils.phone_photo[1]);
			break;
		case 2:
			mProfile.setImageResource(Utils.phone_photo[2]);	
			break;
		case 3:
			mProfile.setImageResource(Utils.phone_photo[3]);	
			
			break;
		case 4:
			mProfile.setImageResource(Utils.phone_photo[4]);	
			
			break;
		case 5:
			mProfile.setImageResource(Utils.phone_photo[5]);	
			break;
		case 6:
			mProfile.setImageResource(Utils.phone_photo[6]);
		default:
			mProfile.setImageResource(R.drawable.phone_contact_2);	
			break;
		}
		boolean zhang = getIntent().getBooleanExtra("contact_zhang", false);
		boolean ying = getIntent().getBooleanExtra("contact_ying", false);
		if (zhang) {
			mProfile.setImageResource(Utils.phone_photo[1]);
			mName.setText(Utils.phone_name[1]);
			
		}if (ying) {
			mProfile.setImageResource(Utils.phone_photo[0]);
			mName.setText(Utils.phone_name[0]);	
		}
		
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.phone_confirm_dial_btn:
			Intent mIntent = new Intent(PhoneDialSureActivity.this,PhoneCallingActivity.class);
			mIntent.putExtra("PHONE_SELECT_INDEX", phone_select_index);
			mIntent.putExtra("PHONE_SELECT_NAME", phone_select_name);
			startActivity(mIntent);
			break;
		case R.id.phone_dial_cancel_btn:
			Intent mIntent2 = new Intent(PhoneDialSureActivity.this,MainActivity.class);
			startActivity(mIntent2);
		default:
			break;
		}
	}
	
	private MediaPlayer dialMediaPlayer;
	private void dialSure(){
		dialMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.confirm_dial); 
	    if(dialMediaPlayer!=null){
	    	dialMediaPlayer.start();
	    	
	    }
	}
	
	boolean isPhoneDialSureFinished = true;
	
	class PhoneDiaSureTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPhoneDialSureFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneDialSureUsing(true);
			MyApplication.serverStart();
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String voice = arg0[0];
			try {
				Http.contactDialSure(voice);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return 1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			isPhoneDialSureFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneDialSureUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		//System.exit(0);
	}
}
