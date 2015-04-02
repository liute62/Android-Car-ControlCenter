package psa.phone;


import psa.navi.NaviRouteActivity;
import android.R.array;
import android.animation.ArgbEvaluator;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Event;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class PhoneSelectActivity extends Activity{

	
	private ImageView mProfile;
	private ImageView mDial;
	private ImageView mHover;
	private ImageView mCancle;
 	private RelativeLayout mMainLayout;
	private RelativeLayout mProfileLayout;
	private RelativeLayout mHoverLayout;
	private RelativeLayout mCancleLayout;
	private RelativeLayout.LayoutParams RL;
	private float default_x = 770;
	private float default_y = 520;
	private int offset_x = 200;
	private int offset_y = 200;
	private int default_count;
	private int phone_state = -1;
	private int phone_select_index = -1;
	private String phone_select_name = " ";
	TextView mPhoneName;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_phone_select);
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX", -1);
		mDial = (ImageView)findViewById(R.id.phone_select_dial);
		mHover = (ImageView)findViewById(R.id.phone_select_hover);
		mMainLayout = (RelativeLayout)findViewById(R.id.phone_select_mainlayout);
		mProfileLayout = (RelativeLayout)findViewById(R.id.phone_select_layout1);
		mProfileLayout.setBackgroundDrawable(null); 
		mHoverLayout = (RelativeLayout)findViewById(R.id.phone_select_layout2);
		mHoverLayout.setBackgroundDrawable(null);
		mCancleLayout = (RelativeLayout)findViewById(R.id.phone_select_layout3);
		mCancleLayout.setBackgroundDrawable(null);
		mProfile = new ImageView(this);
		switch (phone_select_index) {
		case 0:
			mProfile.setImageResource(Utils.phone_photo[0]);
			phone_select_name = Utils.phone_name[0];
			break;
		case 1:
			mProfile.setImageResource(Utils.phone_photo[1]);
			phone_select_name = Utils.phone_name[1];
			break;
		case 2:
			mProfile.setImageResource(Utils.phone_photo[2]);
			phone_select_name = Utils.phone_name[2];
			break;
		case 3:
			mProfile.setImageResource(Utils.phone_photo[3]);
			phone_select_name = Utils.phone_name[3];
			
			break;
		case 4:
			mProfile.setImageResource(Utils.phone_photo[4]);
			phone_select_name = Utils.phone_name[4];
			break;
		case 5:
			mProfile.setImageResource(Utils.phone_photo[5]);
			phone_select_name = Utils.phone_name[5];
			break;
		case 6:
			mProfile.setImageResource(Utils.phone_photo[6]);
			phone_select_name = Utils.phone_name[6];
			break;
		default:
			mProfile.setImageResource(R.drawable.phone_contact_2);	
			phone_select_name = "王斌";
			break;
		}
		RL = new LayoutParams(400,400);
		RL.addRule(RelativeLayout.CENTER_HORIZONTAL);
		RL.setMargins(0, 250, 0, 0);
		mProfile.setLayoutParams(RL);
		mMainLayout.addView(mProfile);
		mMainLayout.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				// TODO Auto-generated method stub
				float x;
				float y;
				switch (arg1.getAction()) {
			    case MotionEvent.ACTION_DOWN:
			    	x = arg1.getX();
			    	y = arg1.getY();
			    	Log.e("ACTION_DOWNx", String.valueOf(x));
			    	Log.e("ACTION_DOWNy", String.valueOf(y));
			    	mProfile.setLayoutParams(RL);
					mProfile.setX(x-offset_x);
					mProfile.setY(y-offset_y);
					break;
				case MotionEvent.ACTION_MOVE:
					mMainLayout.removeView(mProfile);
					x = arg1.getX();
				    y = arg1.getY();
					Log.e("ACTION_MOVEx", String.valueOf(x));
					Log.e("ACTION_MOVEy", String.valueOf(y));
					if (Utils.leftOrRight(x, y) == 0) {
						if (phone_state == -1) {
							mProfileLayout.setBackgroundResource(R.drawable.phone_select_onhover);	
							Log.e("result", "左");
							phone_state = 0;
						}
					}if(Utils.leftOrRight(x, y) == 1){
						if (phone_state == -1) {
							mHoverLayout.setBackgroundResource(R.drawable.phone_select_onhover);
							Log.e("result", "右");
							phone_state = 1;	
						}
					}if (Utils.leftOrRight(x, y) == 2) {
						//返回
						if (phone_state == -1) {
							Log.e("result", "下");
							phone_state = 2;	
						}
					}
					if(Utils.leftOrRight(x, y) == -1){
					    mHoverLayout.setBackgroundDrawable(null);
						mProfileLayout.setBackgroundDrawable(null);
						phone_state = -1;	
					}
					mMainLayout.addView(mProfile);
					mProfile.setLayoutParams(RL);
					mProfile.setX(x-offset_x);
					mProfile.setY(y-offset_y);
					break;
				case MotionEvent.ACTION_UP:
					//mMainLayout.removeView(mProfile);
					mProfile.setX(default_x-offset_x);
					mProfile.setY(default_y-offset_y);
					mProfileLayout.setBackgroundDrawable(null);
					mHoverLayout.setBackgroundDrawable(null);
					if (phone_state == 0) {
						//打开通话面板
						phone_state = -1;
						Intent mIntent = new Intent(PhoneSelectActivity.this,PhoneDialSureActivity.class);
						mIntent.putExtra("PHONE_SELECT_INDEX",phone_select_index);
						mIntent.putExtra("PHONE_SELECT_NAME", phone_select_name);
						startActivity(mIntent);
					}if (phone_state == 1) {
						//短信面板
						phone_state = -1;
						Intent mIntent = new Intent(PhoneSelectActivity.this,MsgActivity.class);
						mIntent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
				     	//mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
						//mIntent.putExtra("PHONE_MAIN", true);
						startActivity(mIntent);
						
					}if (phone_state == 2) {
						 phone_state = -1;
						 Intent mIntent = new Intent(PhoneSelectActivity.this,MainActivity.class);
						 mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
						 mIntent.putExtra("PHONE_MAIN", true);
						 startActivity(mIntent);
					}
					break;
				default:
					break;
				}
				return true;
			}
		});
	}
	
	boolean isPhoneSelectFinished = true;
	class PhoneSelectTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPhoneSelectFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneSelectUsing(true);
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
			isPhoneSelectFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneSelectUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
}
