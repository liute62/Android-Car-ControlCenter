package psa.phone;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class PhoneStrangeDialSure extends Activity implements OnClickListener{
	private Button phoneMoshenReceiveBtn;
	private Button phoneMoshenRejectBtn;

	private Typeface tf;
	private TextView moshenNum;
	private TextView moshenHint;
	
	
	Handler mHandler = new Handler(){	
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.phone_callout) {
				if (Utils.currentScene == 3) {
					
				}
				Intent mIntent = new Intent(PhoneStrangeDialSure.this,PhoneCallingActivity.class);
				mIntent.putExtra("n", true);
				startActivity(mIntent);
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_phone_sure_moshen);
		Utils.strangeOrDial = 0;
		MyApplication.serverStop();
		MyApplication.getInstance().setPhoneStrangeHandler(mHandler);
		MyApplication.serverStart();
		initial();
	}

	private void initial() {
		tf = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		// TODO Auto-generated method stub
		phoneMoshenReceiveBtn  = (Button)this.findViewById(R.id.moshen_phone_confirm_dial_btn);
		phoneMoshenReceiveBtn.setOnClickListener(this);

		phoneMoshenRejectBtn = (Button)this.findViewById(R.id.moshen_phone_dial_cancel_btn);
		phoneMoshenRejectBtn.setOnClickListener(this);
		moshenHint = (TextView)this.findViewById(R.id.moshen_phone_confirm_hint_text);
		moshenHint.setTypeface(tf);
		moshenNum = (TextView)this.findViewById(R.id.moshen_phone_confirm_name);
		moshenNum.setTypeface(tf);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.moshen_phone_confirm_dial_btn:
			Intent mIntent = new Intent(PhoneStrangeDialSure.this,PhoneCallingActivity.class);
			startActivity(mIntent);
			PhoneStrangeDialSure.this.finish();
			break;
		default:
			break;
		}
	}
	
	boolean isPhoneStrangeDialSure = true;
	class PhoneStrangeDialSureTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPhoneStrangeDialSure = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneStrangeUsing(true);
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
			isPhoneStrangeDialSure = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneStrangeUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
}