package psa.phone;

import java.text.BreakIterator;

import psa.navi.NaviActivity;
import psa.navi.NaviRouteActivity;
import psa.navi.NaviSearchActivity;
import psa.navi.NaviTurnToTurnActivity;
import psa.radio.RadioActivity;

import android.R.integer;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class PhoneActivity extends Activity implements OnClickListener{

	private TextView tvTitle; 	
	private GalleryView gallery; 	
	private ImageAdapter adapter;
	private RelativeLayout mParent;
	private RelativeLayout.LayoutParams rl;
	private ImageView mHighlight;
	private TextView phoneSearchBarLeft;
	private TextView phoneSearchBarRight;
	private Typeface tfphoneVen;
	private Button testIncome;
	private Button testVoice;
	private Button testIsDial;
	private Button testMsg;
	private MediaPlayer phoneTestMediaPlayer;
	
	RadioActivity radioMediaPlayer;
	private ImageView mDial;
	private int currentPos = 0;
	
	private Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.contact_search) {
				String result = (String)msg.obj;
				
			}if (msg.what == Utils.contact_action) {
				String result = (String)msg.obj;
				 if(result.equals("0")){
					//上
					 currentPos--;
					 if (currentPos<0) {
						currentPos = 0;
					}
					 adapter.setSelected(currentPos);
				}if (result.equals("1")) {
					//下
					currentPos++;
					if (currentPos > adapter.getCount() - 1) {
						currentPos = adapter.getCount() - 1;
					}
					adapter.setSelected(currentPos);
				}if (result.equals("2")) {
					//选定
					itemClicked(3);
				}
			}if (msg.what == Utils.phone_callout) {
				//呼出
				Bundle bundle = (Bundle)msg.obj;
				String name = bundle.getString("name");
				String number = bundle.getString("number");
			}if (msg.what == Utils.phone_answer) {
				String result = (String)msg.obj;
				
			}if (msg.what ==  Utils.phone_input) {
				String number = (String)msg.obj;
				
			}
		}
		
	};
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_phone);
		MyApplication.serverStop();
		MyApplication.getInstance().setPhoneHandler(mHandler);
		MyApplication.serverStart();
		initRes();
	}
	

	private void initRes(){
		tfphoneVen = Typeface.createFromAsset(getAssets(),
				"fonts/venera300.ttf");
		mDial = (ImageView)findViewById(R.id.phone_dial);
		mDial.setOnClickListener(this);
		tvTitle = (TextView) findViewById(R.id.tvTitle);
		
		phoneSearchBarLeft = (TextView)this.findViewById(R.id.phoone_bar_a);
		phoneSearchBarLeft.setTypeface(tfphoneVen);
		phoneSearchBarRight = (TextView)this.findViewById(R.id.phone_bar_z);
		phoneSearchBarRight.setTypeface(tfphoneVen);
		testIncome = (Button)this.findViewById(R.id.phone_test);
		testIncome.setOnClickListener(this);
		testVoice = (Button)this.findViewById(R.id.phone_test_is_callout);
		testVoice.setOnClickListener(this);
		testMsg = (Button)this.findViewById(R.id.msg_test_incoming);
		testMsg.setOnClickListener(this);
		
		testIsDial = (Button)this.findViewById(R.id.phone_test_confirm_callout);
		testIsDial.setOnClickListener(this);
		gallery = (GalleryView) findViewById(R.id.mygallery);
		mHighlight = new ImageView(this);
		mHighlight.setImageResource(R.drawable.music_equalizer_bar_hl);
		mHighlight.setVisibility(View.INVISIBLE);
		rl = new LayoutParams(300,300);
		rl.addRule(RelativeLayout.CENTER_IN_PARENT);
		mHighlight.setLayoutParams(rl);
		mParent = (RelativeLayout)findViewById(R.id.phone_slide);
		mParent.addView(mHighlight);
		adapter = new ImageAdapter(this); 	
		gallery.setAdapter(adapter);
		mParent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				// TODO Auto-generated method stub
				float x;
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							x = event.getX();
							mHighlight.setLayoutParams(rl);
							mHighlight.setX(x-150);
							mHighlight.setVisibility(View.VISIBLE);
						break;
					case MotionEvent.ACTION_MOVE:
							mParent.removeView(mHighlight);
							x = event.getX();
							mParent.addView(mHighlight);
							mHighlight.setLayoutParams(rl);
							mHighlight.setX(x-150);
						break;
					case MotionEvent.ACTION_UP:
						mHighlight.setVisibility(View.INVISIBLE);
						mParent.removeView(mHighlight);
						break;
					default:
						break;
				}
				
				return true;
			}
		});
		
		gallery.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override 
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				currentPos = position;
				tvTitle.setText(adapter.titles[position]);
				//adapter.getViewHolderImg(position).setAlpha(1f);
				//adapter.getViewHolderName(position).setAlpha(1f);
				//adapter.setDefaultViewHolder(position);
				adapter.setSelected(position);
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
				
			}
		});

		gallery.setOnItemClickListener(new OnItemClickListener() {			// 设置点击事件监听

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if (arg2 == currentPos) {
					itemClicked(arg2);	
				}
			}
		});
		
	}
	
	private void itemClicked(int index){
		Intent mIntent = new Intent(PhoneActivity.this,MainActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); 新实例
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY); 新实例
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); 新实例
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		//mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mIntent.putExtra("PHONE_SELECT", true);
		mIntent.putExtra("PHONE_SELECT_INDEX",index);
		startActivity(mIntent);
		//PhoneActivity.this.finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.phone_dial:
			Intent mIntent = new Intent(PhoneActivity.this,PhoneDialPanelActivity.class);
			startActivity(mIntent);
			break;
		case R.id.phone_test:
			Intent incomingIntent = new Intent(PhoneActivity.this,PhoneIncomingActivity.class);
			startActivity(incomingIntent);
			break;
			
		case R.id.phone_test_is_callout:
			phoneTestvoice();
			break;
		case R.id.phone_test_confirm_callout:
			voiceStartDial();
			break;
			
		case R.id.msg_test_incoming:
			msgVoiceTest();
			break;
		default:
			break;
		}
	}
	
	private void msgVoiceTest() {
		// TODO Auto-generated method stub
		/*msgTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.new_message); 
	    if(msgTestMediaPlayer!=null){
	    	msgTestMediaPlayer.start();
	    	
	    }*/
	    
	    Intent newMsgIntent = new Intent(PhoneActivity.this,MsgNewIncoming.class);
	    startActivity(newMsgIntent);
		
	}


	private void voiceStartDial() {
		// TODO Auto-generated method stub
		Intent incomingIntent = new Intent(PhoneActivity.this,PhoneCallingActivity.class);
		startActivity(incomingIntent);
	}


	public void phoneTestvoice(){
		phoneTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.is_call_out); 
	    if(phoneTestMediaPlayer!=null){
	    	phoneTestMediaPlayer.start();
	    	
	    }
	}

	private boolean isPhoneFinished = true;
	class PhoneTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPhoneFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneUsing(true);
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
			isPhoneFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPhoneUsing(false);
			MyApplication.serverStart();
		}
		
	}
}

