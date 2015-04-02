package app.psa;

import java.io.IOException;
import java.net.UnknownHostException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.entity.mime.MinimalField;


import psa.music.MusicActivity;
import psa.navi.HelloServer;
import psa.navi.NaviActivity;
import psa.navi.NaviSearchActivity;
import psa.navi.NaviTurnToTurnActivity;
import psa.navi.POIActivity;
import psa.phone.MsgActivity;
import psa.phone.MsgIncomingActivity;
import psa.phone.PhoneActivity;
import psa.phone.PhoneDialSureActivity;
import psa.phone.PhoneIncomingActivity;
import psa.phone.PhoneStrangeDialSure;
import psa.phone.PhoneSelectActivity;
import psa.phone.MsgNewIncoming;
import psa.radio.RadioActivity;
import android.media.MediaPlayer;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.R.color;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.ActivityGroup;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import app.psa.MySurfaceView.AirFrontTempLeftTask;

public class MainActivity extends ActivityGroup implements OnClickListener{

	RelativeLayout container;
	ImageView mMusic;
	ImageView mNavi;
	ImageView mMiddle; //模拟下reset
	ImageView mRadio;
	ImageView mPhone;
	Intent mIntent;
	Animation mInAnim;
	Animation mOutAnim;	
	private RelativeLayout clockRelative;
	private RelativeLayout clockRelative2;
	//private Button leftAirNumUp;
	//private Button leftAirNumDown;
	//private Button rightAirNumUp;
	//private Button rightAirNumDown;
	private Button acLeftSwitchBtn;
	private Button acRightSwitchBtn;
	private Button changeBtn;
	private int temp;
	//int n ;
	public static Typeface tfAirCondition;
	private Typeface tfAirmode;
	private TextView acLeftNum;
	private TextView acRightNum;
	/**底部空调类型**/
	private TextView acModeAuto;
	private TextView acModeComfort;
	private TextView acModeEnviroment;
	
	private TextView acModeLow;
	private TextView acModeMid;
	private TextView acModeHigh;
	
	private ImageView mMusicUnderline;
	private ImageView mNaviUnderline;
	private ImageView mRadioUnderline;
	private ImageView mPhoneUnderline;
	
	/**底部的slide**/
	private RelativeLayout mBottomSlideParent;
	private RelativeLayout.LayoutParams bottomSlideLp;
	private ImageView mBottomSlideLight;
	
	private TextView mBottomUpLeftNum;
	private TextView mBottomUpRightNum;
	
	private TextView mBottomCircleLeftNum;
	private TextView mBottomCircleRightNum;
	
	private MySurfaceView mLeftView;
	private MySurfaceView mRightView;
	
	boolean Navi_State;
	boolean Phone_Select_State;
	boolean Phone_Main_State;
	boolean Navi_Turn_To_Turn_State;
	
	boolean acIsOpen = false;
	/**windSlide**/
	private RelativeLayout mWindSlideParent;
	private RelativeLayout.LayoutParams windSlideLp;
	private ImageView mWindSlideLight;
	private TextView windNum;
	
	private AirStatusTask mStatusTask;
	private boolean isStatusFinish = true;
	private AirTempTask mTempTask;
	private boolean isTempFinish = true;
	private AirFlowTask mFlowTask;
	private boolean isFlowFinish = true;
	RelativeLayout mLeftNumUpField;
	RelativeLayout mLeftNumDownField;
	RelativeLayout mRightNumUpField;
	RelativeLayout mRightNumDownField;

	private MediaPlayer JinglePlayer;
	HelloServer mMainServer;
	MediaPlayer mPlayer;
	
	Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg); 
			String result = (String)msg.obj;
			//windNum.setText(result);
			//mFlowTask = new AirFlowTask();
			//mFlowTask.execute(result);
		}
	};
	
	Handler mMainHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			
			if (msg.what == Utils.main_air_num) {
				//更新ui
				Bundle data = (Bundle)msg.obj;
				int tmp = Integer.valueOf(data.getString("temperature"));
				if (tmp > Utils.airMaxTempNum-1) {
					tmp = Utils.airMaxTempNum;
				}if (tmp < Utils.airMinTempNum+1) {
					tmp = Utils.airMinTempNum;
				}
				JinleVoice();
				acLeftNum.setText(String.valueOf(tmp));
				Log.e("main_air_num", String.valueOf(tmp));
				if (isTempFinish) {

					mTempTask = new AirTempTask();
					mTempTask.execute(String.valueOf(tmp));	
				}
				
			}if (msg.what == Utils.main_air_status) {
				Bundle status = (Bundle)msg.obj;
				String tmp = status.getString("status");
				if (tmp != null && tmp.equals("on")) {
					Log.e("main_air_status", "on");
					JinleVoice();
					acOpen();
				}if (tmp != null && tmp.equals("off")) {
					Log.e("main_air_status", "off");
					JinleVoice();
					acOff();
				}
				
			}if (msg.what == Utils.main_air_flow_status) {
				String status = (String)msg.obj;
				if (status != null && status.equals("on")) {
					Log.e("main_air_flow_status", "on");
				}if (status != null && status.equals("off")) {
					Log.e("main_air_flow_status", "off");
				}
			}if (msg.what == Utils.main_air_flow_speed) {
				String speed = (String)msg.obj;
				//windNum.setText(speed);
				int tmp = Integer.valueOf(speed);
				if (tmp == 1) {
					//弱风
					acModeLow.setText("强风");
					acModeMid.setText("弱风");
					acModeHigh.setText("温和");
				}if (tmp == 2) {
					//温和
					acModeLow.setText("弱风");
					acModeMid.setText("温和");
					acModeHigh.setText("强风");
				}if (tmp == 3) {
					//强风
					acModeLow.setText("温和");
					acModeMid.setText("强风");
					acModeHigh.setText("弱风");
				}
				if (isFlowFinish) {
					JinleVoice();
					mFlowTask = new AirFlowTask();
					mFlowTask.execute(speed);	
				}
				
			}if (msg.what == Utils.main_phone_calling) {
				//来电
				if (Utils.isPhoneIncoming == false) {
					if (Utils.currentScene == 1) {
						Intent incomingIntent = new Intent(MainActivity.this,PhoneIncomingActivity.class);
						
						startActivity(incomingIntent);
						Utils.isPhoneIncoming = true;
						
					}if (Utils.currentScene == 3) {
						Intent incomingIntent = new Intent(MainActivity.this,PhoneIncomingActivity.class);
						
						startActivity(incomingIntent);
						Utils.isPhoneIncoming = true;
						
					}
					if(Utils.currentScene == 2) {
						
						mPlayer = MediaPlayer.create(getBaseContext(), R.raw.phoneincoming);  
						mPlayer.start();
						mPlayer.setLooping(true);
						Utils.isWangbing = true;
						Utils.isPhoneIncoming = true;
					}
				}
			}if (msg.what == Utils.phone_answer) {
				if (mPlayer != null) {
					mPlayer.release();
				}
				Utils.isPhoneIncoming = false;
			}
			
			if (msg.what == Utils.main_phone_msg) {
				//来短信
				if (Utils.isMsgIncoming == false) {
					Intent msgIntent = new Intent(MainActivity.this,MsgNewIncoming.class);
					startActivity(msgIntent);
					Utils.isMsgIncoming = true;
				}
			}
			
			if (msg.what == Utils.contact_search) {
				if (Utils.isCallouting == false) {
					//phoneTestvoice();
					if (Utils.currentScene == 1) {
						Intent calloutIntent = new Intent(MainActivity.this,PhoneDialSureActivity.class);
						calloutIntent.putExtra("contact_ying", true);
						startActivity(calloutIntent);
						
					}if (Utils.currentScene == 2) {
						Intent calloutIntent = new Intent(MainActivity.this,PhoneDialSureActivity.class);
						calloutIntent.putExtra("contact_zhang", true);
						startActivity(calloutIntent);
					
					}
				}
			}
			if (msg.what == Utils.phone_input) {
				phoneTestvoice();
				Intent inputIntent = new Intent(MainActivity.this,PhoneStrangeDialSure.class);
				startActivity(inputIntent);
			}
			
			if (msg.what == Utils.navi_list) {
				//NaviTestvoice();
				if(Utils.currentScene == 2){
					Intent naviIntent = new Intent(MainActivity.this,NaviSearchActivity.class);
					naviIntent.putExtra("isNaviList", true);
					naviIntent.putExtra("poi_name", "jiayouzhan");
					startActivity(naviIntent);	
				}if (Utils.currentScene == 3) {

			    	bowuguanPlayer();
			    
					Intent naviIntent = new Intent(MainActivity.this,NaviSearchActivity.class);
					naviIntent.putExtra("isNaviList", true);
					naviIntent.putExtra("poi_name", "suzhoubowuguan");
					startActivity(naviIntent);	
				
				}
			}
			if (msg.what == Utils.navi_poi_search) {
				//NaviTestvoice();
				if (Utils.currentScene == 2) {
					gasPlayer();
				}if (Utils.currentScene == 3) {
					canguanPlayer();
				}
				if (Utils.currentScene == 1) {
					Intent mIntent = new Intent(MainActivity.this,NaviSearchActivity.class);
					mIntent.putExtra("POI_INDEX",1);
					startActivity(mIntent);
				}if (Utils.currentScene == 2) {
					Intent mIntent = new Intent(MainActivity.this,NaviSearchActivity.class);
					mIntent.putExtra("poi_name","jiayouzhan");
					startActivity(mIntent);
					
				}if (Utils.currentScene ==3) {
					Intent mIntent = new Intent(MainActivity.this,NaviSearchActivity.class);
					mIntent.putExtra("poi_name","canguan");
					startActivity(mIntent);
				}
			}
			
			if (msg.what == Utils.main_reset) {
				Utils.currentMusicIndex = 0;
				Utils.currentRadioIndex = 0;
				Utils.currentScene = 1;
				Utils.currentView = 1;
				System.exit(0);
			}
			
			if (msg.what == Utils.transfer_radio) {
				switchRadio();
			}if (msg.what == Utils.share) {
				String result = (String)msg.obj;
				/**if (result.equals("1")) {
					shareMusic();
				}if (result.equals("3")) {
					sharePark();
				}**/
				sharePark();
			}if (msg.what == Utils.trasfer_music) {
				switchMusic();
			}
		}

		
	};
	MediaPlayer canguanPlayer;
	public void canguanPlayer(){
		canguanPlayer = MediaPlayer.create(getBaseContext(), R.raw.select_res); 
	    if(canguanPlayer!=null){
	    	canguanPlayer.start();
	    }
	}
	
	MediaPlayer bowuguanPlayer;
	public void bowuguanPlayer(){
		bowuguanPlayer = MediaPlayer.create(getBaseContext(), R.raw.navi_s3_bowuguan); 
	    if(bowuguanPlayer!=null){
	    	bowuguanPlayer.start();
	    }
	}
	
	MediaPlayer gasPlayer;
	public void gasPlayer(){
		gasPlayer = MediaPlayer.create(getBaseContext(), R.raw.select_gas); 
	    if(gasPlayer!=null){
	    	gasPlayer.start();
	    }
	}
	
	private MediaPlayer parkMediaPlayer;
	private void sharePark(){
		parkMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.share_success); 
	    if(parkMediaPlayer!=null){
	    	parkMediaPlayer.start();
	    	
	    }
	}
	private MediaPlayer musicMediaPlayer;
	private void shareMusic(){
		musicMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.share_success); 
	    if(musicMediaPlayer!=null){
	    	musicMediaPlayer.start();
	    	
	    }
	}
	
	private MediaPlayer naviTestMediaPlayer;
	public void NaviTestvoice(){
		naviTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.setdestination); 
	    if(naviTestMediaPlayer!=null){
	    	naviTestMediaPlayer.start();
	    	
	    }
	}
	
	private MediaPlayer phoneTestMediaPlayer;
	public void phoneTestvoice(){
		phoneTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.is_call_out); 
	    if(phoneTestMediaPlayer!=null){
	    	phoneTestMediaPlayer.start();
	    	
	    }
	}
	
	private void serverInitial(){
		mMainServer = new HelloServer("Main");
		mMainServer.setMainHandler(mMainHandler);
		try {
			mMainServer.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
    protected void JinleVoice() {
		// TODO Auto-generated method stub
    	JinglePlayer = MediaPlayer.create(getBaseContext(), R.raw.jingle1_dot); 
	    if(JinglePlayer!=null){
	    	JinglePlayer.start();
	    }
	}
    
    protected void Jingle(){
    	JinglePlayer = MediaPlayer.create(getBaseContext(), R.raw.jingle1_dot); 
	    if(JinglePlayer!=null){
	    	JinglePlayer.start();
	    }
	
    }

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        MyApplication.getInstance().setMainHandler(mMainHandler);
        MyApplication.serverStart();
        boolean reset = getIntent().getBooleanExtra("Main_Reset", false);
        if (reset) {
			Intent mIntent = new Intent(MainActivity.this,ResetActivity.class);
			startActivity(mIntent);
			System.exit(0);
		}
        if(bundle != null){
        	Navi_State = bundle.getBoolean("TURN_TO_TURN");
        }
        try {
			initial();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        mMiddle = (ImageView)findViewById(R.id.middle_icon);
        mMiddle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Utils.reset(MainActivity.this); 
			}
		});
        
    }
    

    private void initial() throws IOException{
    	mInAnim = AnimationUtils.loadAnimation(this, R.anim.left_in);
    	mOutAnim = AnimationUtils.loadAnimation(this, R.anim.left_out);
    	container = (RelativeLayout)findViewById(R.id.main_activity_view);
    	clockRelative = (RelativeLayout)findViewById(R.id.clock_layout);
    	clockRelative2 = (RelativeLayout)findViewById(R.id.clock_layout2);
    	container.addView(getLocalActivityManager().startActivity(
                "Music",
                new Intent(MainActivity.this,MusicActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
    	tfAirCondition = Typeface.createFromAsset(getAssets(),
				"fonts/venera300.ttf");
    	tfAirmode = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
    	mMusic = (ImageView)findViewById(R.id.main_music_icon);
    	mMusic.setOnClickListener(this);
    	mNavi = (ImageView)findViewById(R.id.main_navi_icon);
    	mNavi.setOnClickListener(this);
    	mRadio = (ImageView)findViewById(R.id.main_radio_icon);
    	mRadio.setOnClickListener(this);
    	mPhone = (ImageView)findViewById(R.id.main_phone_icon);
    	mPhone.setOnClickListener(this);
    	
    	acLeftNum = (TextView)findViewById(R.id.left_circle_num);
    	acRightNum = (TextView)findViewById(R.id.right_circle_num);
    	acModeAuto = (TextView)findViewById(R.id.air_mode_auto);
    	acModeComfort = (TextView)findViewById(R.id.air_mode_comfort);
    	acModeEnviroment = (TextView)findViewById(R.id.air_mode_enviroment);
    	acModeLow = (TextView)findViewById(R.id.air_wind_low);
    	acModeMid = (TextView)findViewById(R.id.air_wind_mid);
    	acModeHigh = (TextView)findViewById(R.id.air_wind_high);
    	acModeLow.setTypeface(tfAirmode);
    	acModeMid.setTypeface(tfAirmode);
    	acModeHigh.setTypeface(tfAirmode);
    	
    	acLeftNum.setTypeface(tfAirCondition);
    	
    	acRightNum.setTypeface(tfAirCondition);
    	acModeAuto.setTypeface(tfAirmode);
    	acModeComfort.setTypeface(tfAirmode);
    	acModeEnviroment.setTypeface(tfAirmode);
    	
    	changeBtn = (Button)findViewById(R.id.clock_btn);
    	changeBtn.setOnClickListener(this);
    	//leftAirNumUp = (Button)findViewById(R.id.left_bottom_circle_up);
    	//leftAirNumUp.setOnClickListener(this);
    	//leftAirNumDown = (Button)findViewById(R.id.left_bottom_circle_down);
    	//leftAirNumDown.setOnClickListener(this);
    	//rightAirNumUp = (Button)findViewById(R.id.right_bottom_circle_up);
    	//rightAirNumUp.setOnClickListener(this);
    	//rightAirNumDown = (Button)findViewById(R.id.right_bottom_circle_down);
    	//rightAirNumDown.setOnClickListener(this);
    	
    	acLeftSwitchBtn = (Button)this.findViewById(R.id.air_switch_btn);
    	acLeftSwitchBtn.setOnClickListener(this);
    	acRightSwitchBtn = (Button)this.findViewById(R.id.air_switch_btn2);
    	acRightSwitchBtn.setOnClickListener(this);
    	
    	mMusicUnderline = (ImageView)findViewById(R.id.main_music_underline);
    	mNaviUnderline = (ImageView)findViewById(R.id.main_navi_underline);
    	mRadioUnderline = (ImageView)findViewById(R.id.main_radio_underline);
    	mPhoneUnderline = (ImageView)findViewById(R.id.main_phone_underline);
    	mMusicUnderline.setVisibility(View.VISIBLE);
		mNaviUnderline.setVisibility(View.INVISIBLE);
		mRadioUnderline.setVisibility(View.INVISIBLE);
		mPhoneUnderline.setVisibility(View.INVISIBLE);
		
		mBottomUpLeftNum = (TextView)findViewById(R.id.left_bottomup_air_num);
		mBottomUpLeftNum.setTypeface(tfAirCondition);
		mBottomUpRightNum = (TextView)findViewById(R.id.right_bottomup_air_num);
		mBottomUpRightNum.setTypeface(tfAirCondition);
		mLeftNumUpField = (RelativeLayout)findViewById(R.id.main_left_up_field);
		mLeftNumDownField = (RelativeLayout)findViewById(R.id.main_left_down_field);
		mRightNumDownField = (RelativeLayout)findViewById(R.id.main_right_down_field);
		mRightNumUpField = (RelativeLayout)findViewById(R.id.main_right_up_field);
		
		mBottomCircleLeftNum = (TextView)findViewById(R.id.left_circle_num);
		mBottomCircleRightNum = (TextView)findViewById(R.id.right_circle_num);
		mLeftView = (MySurfaceView)findViewById(R.id.left_bottom_circle);
		mLeftView.setLeftOrRight(1);
		mLeftView.setNumUpField(mLeftNumUpField);
		mLeftView.setNumDownField(mLeftNumDownField);
		
    	mRightView = (MySurfaceView)findViewById(R.id.right_bottom_circle);
    	mRightView.setNumUpField(mRightNumUpField);
    	mRightView.setNumDownField(mRightNumDownField);
    	
    	mLeftView.setview(mBottomUpLeftNum);
    	mRightView.setview(mBottomUpRightNum);
    	mLeftView.setCircleView(mBottomCircleLeftNum);
    	mRightView.setCircleView(mBottomCircleRightNum);
    	mLeftView.setPos(0);
    	mRightView.setPos(1);
    	acModeLow = (TextView)findViewById(R.id.air_wind_low);
    	acModeMid = (TextView)findViewById(R.id.air_wind_mid);
    	acModeHigh = (TextView)findViewById(R.id.air_wind_high);
    	acModeLow.setTypeface(tfAirmode);
    	acModeMid.setTypeface(tfAirmode);
    	acModeHigh.setTypeface(tfAirmode);
    	
    	//windNum.setTypeface(tfAirCondition);
		bottomSlideInitial();
		windInitial(); 
    }

    private String [] bottomSlideNow;
    
    private void bottomSlideInitial(){
    	final Stack<Float> mStack = new Stack<Float>();
    	bottomSlideNow = new String[]{"舒适","自动","环保"};
    	Utils.bottomAirSlideInitial();
    	//String a = airModeList.get(1);
    	final String string = "1";
    	
    	mBottomSlideParent = (RelativeLayout)findViewById(R.id.bottom_slide_parent);
    	mBottomSlideLight = new ImageView(this);
    	mBottomSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
    	mBottomSlideLight.setVisibility(View.INVISIBLE);
    	bottomSlideLp = new LayoutParams(300,300);
    	bottomSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mBottomSlideLight.setLayoutParams(bottomSlideLp);
    	mBottomSlideParent.addView(mBottomSlideLight);
    	mBottomSlideParent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				float x;
			if (Utils.isAirConOpen == true) {
				
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							x = event.getX();
							Log.e("down", String.valueOf(x));
							mBottomSlideLight.setLayoutParams(bottomSlideLp); 
							mBottomSlideLight.setX(x-150);
							mBottomSlideLight.setVisibility(View.VISIBLE);	
							acModeComfort.setVisibility(View.VISIBLE);
							acModeAuto.setTextSize(32);
							acModeEnviroment.setVisibility(View.VISIBLE);
						break;
					case MotionEvent.ACTION_MOVE:
						    mBottomSlideParent.removeView(mBottomSlideLight);
							x = event.getX();
							Log.e("move", String.valueOf(x));
							if (Utils.isBottomAirSlideInField(x)) {
								acModeComfort.setVisibility(View.VISIBLE);
								acModeAuto.setTextSize(32);
								acModeEnviroment.setVisibility(View.VISIBLE);
								mBottomSlideParent.addView(mBottomSlideLight);
								mBottomSlideLight.setLayoutParams(bottomSlideLp);
								mBottomSlideLight.setX(x-150);
								int a = Utils.sildeUtil(mStack, x);		
								if ( a == 1) {
									bottomSlideNow = Utils.bottomAirSlide(1, bottomSlideNow);
									acModeComfort.setText(bottomSlideNow[0]);
									acModeAuto.setText(bottomSlideNow[1]);
									acModeEnviroment.setText(bottomSlideNow[2]);
								}if (a == 0) {
									bottomSlideNow = Utils.bottomAirSlide(0, bottomSlideNow);
									acModeComfort.setText(bottomSlideNow[0]);
									acModeAuto.setText(bottomSlideNow[1]);
									acModeEnviroment.setText(bottomSlideNow[2]); 
								}
							}
						break;
					case MotionEvent.ACTION_UP:
						mBottomSlideLight.setVisibility(View.INVISIBLE);
						mBottomSlideParent.removeView(mBottomSlideLight);
						acModeComfort.setVisibility(View.INVISIBLE);
						acModeEnviroment.setVisibility(View.INVISIBLE);
						acModeAuto.setTextSize(24); 
						break;
					default:
						break;
				}
			}
				return true;
			}
		});
    }
    float x = 0;
    float tmp = 0;
    private float DownX;
	private float DownY;
	/**
    private void windInitial() {
		mWindSlideParent = (RelativeLayout)findViewById(R.id.bottom_wind_parent);
    	mWindSlideLight = new ImageView(this);
    	mWindSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
    	mWindSlideLight.setVisibility(View.INVISIBLE);
    	windSlideLp = new LayoutParams(300,300);
    	windSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mWindSlideLight.setLayoutParams(windSlideLp);
    	mWindSlideParent.addView(mWindSlideLight);
    	mWindSlideParent.setOnTouchListener(new OnTouchListener() {
  
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				tmp = x;
				x = event.getX();
				
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							x = event.getX();
							Log.e("RadioVoice ACTION_DOWN", String.valueOf(x));
							mWindSlideLight.setLayoutParams(windSlideLp);
							mWindSlideLight.setX(x-150);
							mWindSlideLight.setVisibility(View.VISIBLE);
							DownX = event.getX();//float DownX
							DownY = event.getY();//float DownY
						break;
					case MotionEvent.ACTION_MOVE:
							mWindSlideParent.removeView(mWindSlideLight);
							if (Utils.isBottomAirSlideInField(x)) {
							mWindSlideParent.addView(mWindSlideLight);
							mWindSlideLight.setLayoutParams(windSlideLp);
							mWindSlideLight.setX(x-150);
							//windNum.setTextSize(70);
							float moveX = event.getX() - DownX;//X轴距离
							//float moveY = event.getY() - DownY;
							if(moveX>0)
							{
								String a = windNum.getText().toString();
								int b = Integer.valueOf(a);
								if (b < Utils.airMaxFlowNum) {
									b+=1;	
								}
								//musicVoice.setText(String.valueOf(b));
								Message message = new Message();
								message.obj = String.valueOf(b);
								mHandler.sendMessageDelayed(message, 150);
							}
							if(moveX<0)
							{
								String a = windNum.getText().toString();
								int b = Integer.valueOf(a);
								if (b > Utils.airMinFlowNum) {
									b-=1;	
								}
								Message message = new Message();
								//musicVoice.setText(String.valueOf(b));
								message.obj = String.valueOf(b);
								mHandler.sendMessageDelayed(message, 150);
							}
							}
							Log.e("Music ACTION_MOVE x", String.valueOf(tmp));
							Log.e("Music ACTION_MOVE tmp", String.valueOf(x));
						break;
					case MotionEvent.ACTION_UP:
						  
							mWindSlideLight.setVisibility(View.INVISIBLE);
							mWindSlideParent.removeView(mWindSlideLight);
							//windNum.setTextSize(27);
						break;
					default:
						break;
				}
				
				return true;
			}
		});
	}**/
	
	private String [] windSlideNow;
	private void windInitial(){
    	final Stack<Float> mStack = new Stack<Float>();
    	windSlideNow = new String[]{"弱风","温和","强风"};
    	Utils.bottomWindSlideInitial();
    	//String a = airModeList.get(1);
    	final String string = "1";
    	
    	mWindSlideParent = (RelativeLayout)findViewById(R.id.bottom_wind_parent);
    	mWindSlideLight = new ImageView(this);
    	mWindSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
    	mWindSlideLight.setVisibility(View.INVISIBLE);
    	windSlideLp = new LayoutParams(300,300);
    	windSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mWindSlideLight.setLayoutParams(windSlideLp);
    	mWindSlideParent.addView(mWindSlideLight);
    	mWindSlideParent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				float x;
			if (Utils.isAirConOpen == true) {
			
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							x = event.getX();
							Log.e("down", String.valueOf(x));
							mWindSlideLight.setLayoutParams(windSlideLp); 
							mWindSlideLight.setX(x-150);
							mWindSlideLight.setVisibility(View.VISIBLE);	
							acModeLow.setVisibility(View.VISIBLE);
							acModeMid.setTextSize(32);
							acModeHigh.setVisibility(View.VISIBLE);
						break;
					case MotionEvent.ACTION_MOVE:
						    mWindSlideParent.removeView(mWindSlideLight);
							x = event.getX();
							Log.e("move", String.valueOf(x));
							if (Utils.isBottomAirSlideInField(x)) {
								acModeLow.setVisibility(View.VISIBLE);
								acModeMid.setTextSize(32);
								acModeHigh.setVisibility(View.VISIBLE);
								mWindSlideParent.addView(mWindSlideLight);
								mWindSlideLight.setLayoutParams(windSlideLp);
								mWindSlideLight.setX(x-150);
								int a = Utils.sildeUtil(mStack, x);		
								if ( a == 1) {
									windSlideNow = Utils.bottomWindSlide(1, windSlideNow);
									acModeLow.setText(windSlideNow[0]);
									acModeMid.setText(windSlideNow[1]);
									acModeHigh.setText(windSlideNow[2]);
								}if (a == 0) {
									windSlideNow = Utils.bottomWindSlide(0, windSlideNow);
									acModeLow.setText(windSlideNow[0]);
									acModeMid.setText(windSlideNow[1]);
									acModeHigh.setText(windSlideNow[2]); 
								}
							}
						break;
					case MotionEvent.ACTION_UP:
						mWindSlideLight.setVisibility(View.INVISIBLE);
						mWindSlideParent.removeView(mWindSlideLight);
						acModeLow.setVisibility(View.INVISIBLE);
						acModeHigh.setVisibility(View.INVISIBLE);
						acModeMid.setTextSize(24);
						String tmp = acModeMid.getText().toString();
						if (tmp.equals("弱风")) {
							tmp = "1";
						}if (tmp.equals("温和")) {
							tmp = "2";
						}if(tmp.equals("强风")){
							tmp = "3";
						}
						if(isFlowFinish == true){
							AirFlowTask airFlowTask = new AirFlowTask();
							airFlowTask.execute(tmp);
						}
						break;
					default:
						break;
				}
				
				return true;
			}
			return true;
		 }
		});
    }
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch (view.getId()) {
		case R.id.main_music_icon:
			switchActivity("Music", 1);
			break;
		case R.id.main_navi_icon:
			switchActivity("Navi", 2);
			break;
		case R.id.main_radio_icon:
			switchActivity("Radio", 3);
			break;
		case R.id.main_phone_icon:
			switchActivity("Phone", 4);
			break;
		case R.id.left_bottom_circle_up:
			temp = Integer.valueOf(acLeftNum.getText().toString());
			temp++;
			acLeftNum.setText(String.valueOf(temp));  
			break;
		case R.id.left_bottom_circle_down:
			temp = Integer.valueOf(acLeftNum.getText().toString());
			temp--;
			acLeftNum.setText(String.valueOf(temp));
			
			break;
		case R.id.right_bottom_circle_up:
			temp = Integer.valueOf(acRightNum.getText().toString());
			temp++;
			acRightNum.setText(String.valueOf(temp));
			break;
		case R.id.right_bottom_circle_down:
			temp = Integer.valueOf(acRightNum.getText().toString());
			temp--;
			acRightNum.setText(String.valueOf(temp));
			break;
			
		case R.id.air_switch_btn:
			if(acIsOpen == false){
				acOpen();
			}else {
				acOff();
			}
			
			break;
		case R.id.air_switch_btn2:
			if(acIsOpen == false){
				acOpen();
			}else {
				acOff();
			}
			break;
		case R.id.clock_btn:
			
			if(clockRelative.getVisibility()==View.VISIBLE){
				clockRelative.setVisibility(View.INVISIBLE);
				clockRelative2.setVisibility(View.VISIBLE);
			}
			else{
				clockRelative.setVisibility(View.VISIBLE);
				clockRelative2.setVisibility(View.INVISIBLE);
				
			}
			
			break;


		default:
			break;
		}
	}
	
	private void acOpen() {
		// TODO Auto-generated method stub
		Utils.isAirConOpen = true;
		acRightNum.setVisibility(View.VISIBLE);
		acLeftNum.setVisibility(View.VISIBLE);
		//windNum.setTextColor(Color.rgb(175,222,255));
		acModeMid.setTextColor(Color.rgb(175,222,255));
		acModeAuto.setTextColor(Color.rgb(175,222,255));
		acLeftSwitchBtn.setBackgroundResource(R.drawable.ac_button_on);
		acRightSwitchBtn.setBackgroundResource(R.drawable.ac_button_on);
		acIsOpen = true;
		mStatusTask = new AirStatusTask();
		mStatusTask.execute("on");
	}

	private void acOff() {
		// TODO Auto-generated method stub
		Utils.isAirConOpen = false;
		acRightNum.setVisibility(View.INVISIBLE);
		acLeftNum.setVisibility(View.INVISIBLE);
		
		//windNum.setTextColor(Color.rgb(53,63,77));
		acModeAuto.setTextColor(Color.rgb(53,63,77));
		acModeMid.setTextColor(Color.rgb(53,63,77));
		acLeftSwitchBtn.setBackgroundResource(R.drawable.ac_button_off);
		acRightSwitchBtn.setBackgroundResource(R.drawable.ac_button_off);
		acIsOpen = false;
		mStatusTask = new AirStatusTask();
		mStatusTask.execute("off");
	}

	private void switchActivity(String TAG,int id){
		
		
		switch(id){
			case 1:
				//Music
				Utils.currentView = 1;
				MusicActivity musicActivity = new MusicActivity();
				mIntent = new Intent(MainActivity.this,MusicActivity.class);
				mMusicUnderline.setVisibility(View.VISIBLE);
				mNaviUnderline.setVisibility(View.INVISIBLE);
				mRadioUnderline.setVisibility(View.INVISIBLE);
				mPhoneUnderline.setVisibility(View.INVISIBLE);
				break;
			case 2:
				//Navi
				if (Utils.isNaviBegin) {
					if (Utils.currentView != 10) {
						
					mIntent = new Intent(MainActivity.this,NaviTurnToTurnActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
					mIntent.putExtra("is_poi", navi_is_poi);
					//startActivity(mIntent);
					}
				}
				else {
					Utils.currentView = 2;
					mIntent = new Intent(MainActivity.this,NaviActivity.class);	
				}
				mMusicUnderline.setVisibility(View.INVISIBLE);
				mNaviUnderline.setVisibility(View.VISIBLE);
				mRadioUnderline.setVisibility(View.INVISIBLE);
				mPhoneUnderline.setVisibility(View.INVISIBLE);
				break;
			case 3:
				//Radio
				Utils.currentView = 3;
				mIntent = new Intent(MainActivity.this,RadioActivity.class);
				mMusicUnderline.setVisibility(View.INVISIBLE);
				mNaviUnderline.setVisibility(View.INVISIBLE);
				mRadioUnderline.setVisibility(View.VISIBLE);
				mPhoneUnderline.setVisibility(View.INVISIBLE);
				break;
			case 4:
				//Phone
				Utils.currentView = 4;
				mIntent = new Intent(MainActivity.this,PhoneActivity.class);
				mMusicUnderline.setVisibility(View.INVISIBLE);
				mNaviUnderline.setVisibility(View.INVISIBLE);
				mRadioUnderline.setVisibility(View.INVISIBLE);
				mPhoneUnderline.setVisibility(View.VISIBLE);
				break;
			default:
				mIntent = new Intent(MainActivity.this,MusicActivity.class);
				mMusicUnderline.setVisibility(View.VISIBLE);
				mNaviUnderline.setVisibility(View.INVISIBLE);
				mRadioUnderline.setVisibility(View.INVISIBLE);
				mPhoneUnderline.setVisibility(View.INVISIBLE);
				break;
		}
		//container.startAnimation(mOutAnim);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                TAG,mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
	}
	
	private void switchRadio(){
		Utils.currentView = 3;
		mIntent = new Intent(MainActivity.this,RadioActivity.class);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                "radio new",mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
		mMusicUnderline.setVisibility(View.INVISIBLE);
		mNaviUnderline.setVisibility(View.INVISIBLE);
		mRadioUnderline.setVisibility(View.VISIBLE);
		mPhoneUnderline.setVisibility(View.INVISIBLE);
	}
	
	private void swithView(){
		Utils.currentView = 2;
		mIntent = new Intent(MainActivity.this,NaviTurnToTurnActivity.class);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                "turn to turn",mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
	}
	
	private void switchPhoneSelect(){
		mIntent = new Intent(MainActivity.this,PhoneSelectActivity.class);
		mIntent.putExtra("PHONE_SELECT_INDEX",phone_select_index);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                "PHONE_SELECT",mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
	}
	
	private void switchPhone(){
		mIntent = new Intent(MainActivity.this,PhoneActivity.class);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                "PHONE_Main",mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
	}
	
	private void switchMusic(){
		mIntent = new Intent(MainActivity.this,MusicActivity.class);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                "Music Main",mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
	}
	
	private void switchNavi(){
		mIntent = new Intent(MainActivity.this,NaviActivity.class);
		container.removeAllViews();
		container.addView(getLocalActivityManager().startActivity(
                "PHONE_Main",mIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                .getDecorView());
	}
	
	private boolean Music_goto = false;
	@Override
	protected void onRestart() {
		//TODO Auto-generated method stub
		super.onRestart();
		Navi_State = getIntent().getBooleanExtra("TURN_TO_TURN", false);
		Phone_Select_State = getIntent().getBooleanExtra("PHONE_SELECT", false);
		Phone_Main_State = getIntent().getBooleanExtra("PHONE_MAIN", false);
		Navi_Turn_To_Turn_State = getIntent().getBooleanExtra("NAVI_TURN_TO_TURN", false);
		navi_is_poi = getIntent().getBooleanExtra("is_poi", false);
		Music_goto = getIntent().getBooleanExtra("music_goto", false);
		Log.e("onRestart Code", String.valueOf(Navi_State));
		if (Music_goto) {
			switchMusic();
		}
		
		if(Navi_State){
			//转换面板
			swithView();
		}if (Phone_Select_State) {
			switchPhoneSelect();
		}if (Phone_Main_State) {
			switchPhone();
		}if(Navi_Turn_To_Turn_State){
			switchNavi();
		}
	}
	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		setIntent(intent); 
	}
	
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Navi_State = getIntent().getBooleanExtra("TURN_TO_TURN", false);
		Log.e("onStart Code", String.valueOf(Navi_State));
		if(Navi_State){
			Log.e("MainActivity", "success");
		}
	
	}
	
	int phone_select_index = -1;
	boolean navi_is_poi = false;
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Navi_State = getIntent().getBooleanExtra("TURN_TO_TURN", false);
		Phone_Select_State = getIntent().getBooleanExtra("PHONE_SELECT", false);
		phone_select_index = getIntent().getIntExtra("PHONE_SELECT_INDEX",-1);
		Phone_Main_State = getIntent().getBooleanExtra("PHONE_MAIN", false);
		Navi_Turn_To_Turn_State = getIntent().getBooleanExtra("NAVI_TURN_TO_TURN", false);
		navi_is_poi = getIntent().getBooleanExtra("is_poi", false);
		Log.e("onResume Code", String.valueOf(Navi_State));
		
		if(Navi_State){
			Log.e("MainActivity", "success");
		}if (Phone_Select_State) {
			switchPhoneSelect();
		}if (Phone_Main_State) {
			switchPhone();
		}if(Navi_Turn_To_Turn_State){
			switchNavi();
		}
	
	}
	
	class AirTempTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isTempFinish = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMainUsing(true);
			MyApplication.serverStart();
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String tmp = arg0[0];
			try {
				Http.AirConTemperaturePost(tmp);
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
			isTempFinish = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMainUsing(false);
			MyApplication.serverStart();
		}
	}
	
	class AirFlowTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isFlowFinish = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMainUsing(true);
			MyApplication.serverStart();
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String flow = arg0[0];
			try {
				Http.AirConFlowSpeedPost(flow);
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
			isFlowFinish = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMainUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	class AirStatusTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isStatusFinish = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMainUsing(true);
			MyApplication.serverStart();
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String status = arg0[0];
			try {
				Http.AirConStatusPost(status);
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
			isStatusFinish = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMainUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		
	}
}
