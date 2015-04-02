package psa.phone;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.psa.Http;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;
import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;

public class PhoneCallingActivity extends Activity implements OnClickListener {
	private TextView phoneTime;
	private TextView phoneVoice;
	private TextView contactName;
	private String phone_name = null;
	private int systemVoiceMax;
	private int systemVoiceCur;
	private int phoneVoiceTmp;
	private Typeface tfPhoneVenera;

	private Typeface tfPhoneSourceHanSansCNLight;

	private Button phoneVoiceMax;
	private Button phoneVoiceMin;
	private Button phoneHangUp;

	private TextView minText; // ·Ö
	private TextView secText; // Ãë
	private TextView mDiaView; 
	
	private boolean isPaused = false;
	private String timeUsed;
	private String callingNum;
	private int timeUsedInsec;
	private AudioManager mPhoneManager;
	private int diaCount = 0;
	RelativeLayout mProfile;
	
	Bundle bl;
	Intent intent;
	int phone_select_index = -1;
	

	
	private Handler uiHandle = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case 0:
				mDiaView.setVisibility(View.VISIBLE);
				minText.setVisibility(View.INVISIBLE);
				secText.setVisibility(View.INVISIBLE);
				uiHandle.sendEmptyMessageDelayed(1, 1000);
			break;
			case 1:
				mDiaView.setVisibility(View.INVISIBLE);
				minText.setVisibility(View.VISIBLE);
				secText.setVisibility(View.VISIBLE);
				if (!isPaused) {
					addTimeUsed();
					updateClockUI();
				}
				uiHandle.sendEmptyMessageDelayed(1, 1000);
				break;
			default:
				break;
			}
		}
	};
	
	private Handler mHandler = new Handler(){
		
		public void handleMessage(Message msg) 
		{
			if (msg.what == Utils.phone_answer) {
				Intent mIntent = new Intent(PhoneCallingActivity.this,MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(mIntent);
			}
		};
	};

	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone_calling);
		Intent intent1 = getIntent();
		Utils.comingOrCalling = 1;
		callingNum = intent1.getStringExtra("dialNoString"); 
		if (callingNum == null) {
			callingNum = " ";
		}
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
		phone_name = getIntent().getStringExtra("PHONE_SELECT_NAME");
		initial();
		if (phone_name != null) {
			contactName.setText(phone_name);	
		}		
		MyApplication.serverStop();
		MyApplication.getInstance().setPhoneCallingHandler(mHandler);
		MyApplication.serverStart();
		uiHandle.removeMessages(1);
		startTime();
		isPaused = false;
	}

	private void initial() {

	
		tfPhoneVenera = Typeface.createFromAsset(getAssets(),
				"fonts/venera300.ttf");
		tfPhoneSourceHanSansCNLight = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCNLight.ttf");
		mPhoneManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		systemVoiceCur = mPhoneManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		systemVoiceMax = mPhoneManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		phoneVoice = (TextView) this.findViewById(R.id.phone_voice_num);
		contactName = (TextView) this.findViewById(R.id.phone_calling_contact);

		minText = (TextView) findViewById(R.id.num1);		
		secText = (TextView) findViewById(R.id.num2);
		secText.setVisibility(View.INVISIBLE);
		mDiaView = (TextView)findViewById(R.id.phone_calling_dial);
		
		minText.setTypeface(tfPhoneVenera);
		minText.setVisibility(View.INVISIBLE);
		secText.setTypeface(tfPhoneVenera);
		mDiaView.setTypeface(tfPhoneSourceHanSansCNLight);
		// phoneTime.setTypeface(tfPhoneVenera);
		phoneVoice.setTypeface(tfPhoneVenera);
		contactName.setTypeface(tfPhoneSourceHanSansCNLight);
		
		contactName.setText(callingNum);

		phoneVoiceMax = (Button) this.findViewById(R.id.phone_voice_max);
		phoneVoiceMax.setOnClickListener(this);

		phoneVoiceMin = (Button) this.findViewById(R.id.phone_voice_min);
		phoneVoiceMin.setOnClickListener(this);

		phoneVoice.setText(String.valueOf(systemVoiceCur));

		phoneHangUp = (Button) this.findViewById(R.id.phone_hang_up_btn);
		phoneHangUp.setOnClickListener(this);
		mProfile = (RelativeLayout)findViewById(R.id.phone_calling_profile);
		switch (phone_select_index) {
		case 0:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[0]));
			break;
		case 1:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[1]));
			break;
		case 2:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[2]));
			break;
		case 3:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[3]));
			
			break;
		case 4:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[4]));
			
			
			break;
		case 5:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[5]));
			break;
		case 6:
			mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[6]));
			
			break;
		default:
			mProfile.setBackground(getResources().getDrawable(R.drawable.phone_contact_2));
			break;
		}
	   boolean isdial = getIntent().getBooleanExtra("is_dial", false);
	   boolean iszhang = getIntent().getBooleanExtra("zhang", false);
	   boolean isN = getIntent().getBooleanExtra("n", false);
	   if (isdial == true) {
		   mProfile.setBackground(getResources().getDrawable(R.drawable.n10086));		
	   }if (iszhang) {
		   mProfile.setBackground(getResources().getDrawable(Utils.phone_photo[1]));		
		   contactName.setText(Utils.phone_name[1]);
	   }if (isN) {
		   mProfile.setBackground(getResources().getDrawable(R.drawable.n10086));		
		   contactName.setText("10086");
	   }
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.phone_hang_up_btn:
			Intent mIntent = new Intent(PhoneCallingActivity.this,MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mIntent);
			PhoneCallingActivity.this.finish();
			break;

		case R.id.phone_voice_max:
			phoneVoiceTmp = Integer.valueOf(phoneVoice.getText().toString());
			phoneVoiceTmp++;
			mPhoneManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, 0);
			mPhoneManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_RAISE, 0);
			if (phoneVoiceTmp > systemVoiceMax) {
				phoneVoice.setText(String.valueOf(systemVoiceMax));

			} else {
				phoneVoice.setText(String.valueOf(phoneVoiceTmp));
			}
			break;

		case R.id.phone_voice_min:
			phoneVoiceTmp = Integer.valueOf(phoneVoice.getText().toString());
			phoneVoiceTmp--;
			mPhoneManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, 0);
			mPhoneManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_LOWER, 0);
			if (phoneVoiceTmp < 0) {
				phoneVoice.setText("0");
			} else {
				phoneVoice.setText(String.valueOf(phoneVoiceTmp));
			}
			break;

		default:
			break;
		}
		// TODO Auto-generated method stub

	}

	/***********************************/

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		isPaused = true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		isPaused = false;
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		phone_name = getIntent().getStringExtra("PHONE_SELECT_NAME");
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
	}

	private void startTime() {
		Message message = new Message();
		message.what = diaCount;
		uiHandle.sendMessageDelayed(message,5000);
	}

	private void updateClockUI() {
		minText.setText(getMin() + ":");
		secText.setText(getSec());
	}

	public void addTimeUsed() {
		timeUsedInsec = timeUsedInsec + 1;
		timeUsed = this.getMin() + ":" + this.getSec();
	}

	public CharSequence getMin() {
		int min = timeUsedInsec/60;
		return min < 10 ? "0" + min : String.valueOf(timeUsedInsec / 60);
		//return String.valueOf( timeUsedInsec / 60);
	}

	public CharSequence getSec() {
		int sec = timeUsedInsec % 60;
		return sec < 10 ? "0" + sec : String.valueOf(sec);
	}
	
	
	private boolean isPhoneCallingFinished = true;
	class PhoneCallingTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPhoneCallingFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneCallingUsing(true);
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
			isPhoneCallingFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneCallingUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
}
