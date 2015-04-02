package psa.navi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.ScaleAnimation;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.VideoView;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class NaviTurnToTurnActivity extends Activity implements OnClickListener{
	private TextView distanceToTurn;
	private TextView turnGuideText;
	private ImageView turnIcon;
	private Typeface tfTurnVenera;
	private Typeface tfSourceHanLight;
	private HelloServer server;
	private Button naviTurnFinishBtn;
	private Button naviTurnSearchpoiBtn;
	private ViewPager naviPager;
	private VideoView fakeMapView;
	
	private List<View> listViews;
	MediaPlayer player101;
	MediaPlayer player102;
	MediaPlayer player103;
	MediaPlayer player104;
	MediaPlayer player105;
	MediaPlayer player106;
	MediaPlayer player107;
	MediaPlayer player108;
	MediaPlayer player109;
	MediaPlayer player110;
	MediaPlayer player111;
	MediaPlayer player112;
	MediaPlayer player113;
	MediaPlayer player114;
	MediaPlayer player201;
	MediaPlayer player202;
	MediaPlayer player203;
	MediaPlayer player204;
	MediaPlayer player205;
	MediaPlayer player206;
	MediaPlayer player207;
	MediaPlayer player208;
	MediaPlayer player209;
	MediaPlayer player210;
	MediaPlayer player211;
	MediaPlayer player212;
	MediaPlayer player213;
	MediaPlayer player301;
	MediaPlayer player302;
	MediaPlayer player303;
	MediaPlayer player304;
	MediaPlayer player305;
	MediaPlayer player306;
	MediaPlayer player307;
	
	MediaPlayer playera;
	MediaPlayer playerb;
	MediaPlayer playerc;
	MediaPlayer playerd;
	MediaPlayer playere;
	MediaPlayer playerf;
	private int lastMsg = -1;
	private int lastPlay = -1;
	private boolean isPlaying = false;
	private int currentPostion = 0;
	private int endPosition = 2000000;
	
	private void playMusic101(){
		player101 = MediaPlayer.create(getBaseContext(), R.raw.v101); 
	    if(player101!=null){
	    	player101.start();
	    }
	}
	private void playMusic102(){
		player102 = MediaPlayer.create(getBaseContext(), R.raw.v102); 
	    if(player102!=null){
	    	player102.start();
	    }
	}
	private void playMusic103(){
		player103 = MediaPlayer.create(getBaseContext(), R.raw.v103); 
	    if(player103!=null){
	    	player103.start();
	    }
	}
	private void playMusic104(){
		player104 = MediaPlayer.create(getBaseContext(), R.raw.v104); 
	    if(player104!=null){
	    	player104.start();
	    }
	}
	private void playMusic105(){
		player105 = MediaPlayer.create(getBaseContext(), R.raw.v105); 
	    if(player105!=null){
	    	player105.start();
	    }
	}
	private void playMusic106(){
		player106 = MediaPlayer.create(getBaseContext(), R.raw.v106); 
	    if(player106!=null){
	    	player106.start();
	    }
	}
	private void playMusic107(){
		player107 = MediaPlayer.create(getBaseContext(), R.raw.v107); 
	    if(player107!=null){
	    	player107.start();
	    }
	}
	private void playMusic108(){
		player108 = MediaPlayer.create(getBaseContext(), R.raw.v108); 
	    if(player108!=null){
	    	player108.start();
	    }
	}
	private void playMusic109(){
		player109 = MediaPlayer.create(getBaseContext(), R.raw.v109); 
	    if(player109!=null){
	    	player109.start();
	    }
	}
	private void playMusic110(){
		player110 = MediaPlayer.create(getBaseContext(), R.raw.v110); 
	    if(player110!=null){
	    	player110.start();
	    }
	}
	private void playMusic111(){
		player111 = MediaPlayer.create(getBaseContext(), R.raw.v111); 
	    if(player111!=null){
	    	player111.start();
	    }
	}
	private void playMusic112(){
		player112 = MediaPlayer.create(getBaseContext(), R.raw.v112); 
	    if(player112!=null){
	    	player112.start();
	    }
	}
	private void playMusic113(){
		player113 = MediaPlayer.create(getBaseContext(), R.raw.v113); 
	    if(player113!=null){
	    	player113.start();
	    }
	}
	private void playMusic114(){
		player114 = MediaPlayer.create(getBaseContext(), R.raw.v114); 
	    if(player114!=null){
	    	player114.start();
	    }
	}
	private void playMusic201(){
		player201 = MediaPlayer.create(getBaseContext(), R.raw.v201); 
	    if(player201!=null){
	    	player201.start();
	    }
	}
	private void playMusic202(){
		player202 = MediaPlayer.create(getBaseContext(), R.raw.v202); 
	    if(player202!=null){
	    	player202.start();
	    }
	}
	private void playMusic203(){
		player203 = MediaPlayer.create(getBaseContext(), R.raw.v203); 
	    if(player203!=null){
	    	player203.start();
	    }
	}
	private void playMusic204(){
		player204 = MediaPlayer.create(getBaseContext(), R.raw.v204); 
	    if(player204!=null){
	    	player204.start();
	    }
	}
	private void playMusic205(){
		player205 = MediaPlayer.create(getBaseContext(), R.raw.v205); 
	    if(player205!=null){
	    	player205.start();
	    }
	}
	private void playMusic206(){
		player206 = MediaPlayer.create(getBaseContext(), R.raw.v206); 
	    if(player206!=null){
	    	player206.start();
	    }
	}
	private void playMusic207(){
		player207 = MediaPlayer.create(getBaseContext(), R.raw.v207); 
	    if(player207!=null){
	    	player207.start();
	    }
	}private void playMusic208(){
		player208 = MediaPlayer.create(getBaseContext(), R.raw.v208); 
	    if(player208!=null){
	    	player208.start();
	    }
	}
	private void playMusic209(){
		player209 = MediaPlayer.create(getBaseContext(), R.raw.v209); 
	    if(player209!=null){
	    	player209.start();
	    }
	}
	private void playMusic210(){
		player210 = MediaPlayer.create(getBaseContext(), R.raw.v210); 
	    if(player210!=null){
	    	player210.start();
	    }
	}
	private void playMusic211(){
		player211 = MediaPlayer.create(getBaseContext(), R.raw.v211); 
	    if(player211!=null){
	    	player211.start();
	    }
	}
	private void playMusic212(){
		player212 = MediaPlayer.create(getBaseContext(), R.raw.v212); 
	    if(player212!=null){
	    	player212.start();
	    }
	}
	private void playMusic301(){
		player301 = MediaPlayer.create(getBaseContext(), R.raw.v301); 
	    if(player301!=null){
	    	player301.start();
	    }
	}
	private void playMusic302(){
		player302 = MediaPlayer.create(getBaseContext(), R.raw.v302); 
	    if(player302!=null){
	    	player302.start();
	    }
	}
	private void playMusic303(){
		player303 = MediaPlayer.create(getBaseContext(), R.raw.v303); 
	    if(player303!=null){
	    	player303.start();
	    }
	}
	private void playMusic304(){
		player304 = MediaPlayer.create(getBaseContext(), R.raw.v304); 
	    if(player304!=null){
	    	player304.start();
	    }
	}
	private void playMusic305(){
		player305 = MediaPlayer.create(getBaseContext(), R.raw.v305); 
	    if(player305!=null){
	    	player305.start();
	    }
	}
	private void playMusic306(){
		player306 = MediaPlayer.create(getBaseContext(), R.raw.v306); 
	    if(player306!=null){
	    	player306.start();
	    }
	}
	private void playMusic307(){
		player307 = MediaPlayer.create(getBaseContext(), R.raw.v307); 
	    if(player307!=null){
	    	player307.start();
	    }
	}
	
	
	Handler mNaviHandler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			if (msg.what == Utils.navi_poi_search) {
				Intent mIntent = new Intent(NaviTurnToTurnActivity.this,
						NaviSearchActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(mIntent);
			}
			
			if (msg.what == Utils.navi_last) {
				//更新ui
				//Log.e("NaviTurnToTurnActivity", "更新UI");
				Bundle data = (Bundle)msg.obj;
				int message = Integer.valueOf(data.getString("message"));
				String speed = data.getString("speed");
				Log.e("speed", speed);
				double tmpSpeed = Double.valueOf(speed);
				tmpSpeed = Math.abs(tmpSpeed);
				speed = String.valueOf(tmpSpeed);
				String distance = data.getString("distanceToNext");
				Log.e("distance:", distance);
				String playStr = data.getString("play");
				/**距离**/
				char [] tmp = distance.toCharArray();
				int index = 0;
				for (int i = 0; i < tmp.length; i++) {
					index++;
					if (tmp[i] == '.') {
						break;
					}
				}
				distance = distance.substring(0,index-1);
				if (Integer.valueOf(distance) > 1000) {
					//转换为km
					int tmpDis = Integer.valueOf(distance);
					int tmpDis1 = tmpDis / 1000; //获得km
					int tmpDis2 = (tmpDis - 1000);
					tmpDis2 = tmpDis2 / 100;  //获得百米
					distanceToTurn.setText(String.valueOf(tmpDis1)+"."
									+String.valueOf(tmpDis2)+"千米");
				}else {
					distanceToTurn.setText(distance+"米");
				}
				
				/**play**/
				Log.e("play:", playStr);
				int play = Integer.valueOf(playStr);
				int second = 0;
				if (lastPlay != play) {
					lastPlay = play;
					isPlaying = true;
				switch (play) {
				case 0:
					fakeMapView.pause();
					isPlaying = false;
					break;
				case 1:
					fakeMapView.pause();
					isPlaying = false;
					break;
				case 2:
					fakeMapView.pause();
					isPlaying = false;
					break;
				case 3:
					fakeMapView.pause();
					isPlaying = false;
					break;
				case 4:
					second = 5660;
					endPosition = 12735;
					break;
				case 5:
					second = 12735;
					endPosition = 22641;
					break;
				case 6:
					second = 22641;
					endPosition = 39150;
					break;
				case 7:
					second = 39150;
					endPosition = 56603;
					break;
				case 8:
					second = 56603;
					endPosition = 80188;
					break;
				case 9:
					second = 80188;
					endPosition = 97169;
					break;
				case 10:
					second  = 97169;
					endPosition = 99528;
					break;
				case 11:
					second = 99528;
					endPosition = 103301;
					break;
				case 12:
					second = 103301;
					endPosition = 105660;
					break;
				case 13:
					second = 105660;
					endPosition = 108490;
					break;
				case 14:
					second = 108490;
					endPosition = 117924;
					break;
				case 15:
					second = 117924;
					endPosition = 130188;
					break;
				case 16:
					second  =130188;
					endPosition = 133490;
					break;
				case 17:
					second = 133490;
					endPosition = 140094;
					break;
				case 18:
					second = 140094;
					endPosition = 165094;
					break;
				case 19:
					second  = 165094;
					endPosition = 12735;
					break;
				case 20:
					fakeMapView.pause();
					isPlaying = false;
					break;
				default:
					break;
				}
				if (isPlaying) {
					//playMovie(second);					
				}
			}
				/**speed**/
				char [] speedTmp = speed.toCharArray();
				int speedIndex = 0;
				for (int j = 0; j < speedTmp.length; j++) {
					speedIndex++;
					if (speedTmp[j] == '.') {
						break;
					}
				}
				speed = speed.substring(0,speedIndex-1);
				
				Log.e("original_message:", String.valueOf(message));
				if (lastMsg != message) {
					lastMsg = message;
				Log.e("message:", String.valueOf(message));
				int tmpMsg = lastMsg % 1000;
				switch (tmpMsg) {
				case 101:
					turnGuideText.setText("直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic101();
					break;
				case 102:
					turnGuideText.setText("右转");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic102();
					break;
				case 103:
					turnGuideText.setText("右转上高架");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic103();
					break;
				case 104:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic104();
					break;
				case 105:
					turnGuideText.setText("靠右行驶");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic105();
					
					break;
				case 106:
					turnGuideText.setText("前方左侧汇入车辆");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_left);
					playMusic106();
					break;
				case 107:
					turnGuideText.setText("靠右行驶");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic107();
					
					break;
				case 108:
					turnGuideText.setText("靠右下高架");
					playMusic108();
					
					break;
				case 109:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic109();
					
					break;
				case 110:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic110();
					
					break;
				case 111:
					turnGuideText.setText("靠右行驶");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic111();
					
					break;
				case 112:
					turnGuideText.setText("前方右转");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic112();
					
					break;
				case 113:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic113();
					
					break;
				case 114:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic114();
					break;
				case 201:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic201();
					
					break;
				case 202:
					turnGuideText.setText("前方左转");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_left);
					playMusic202();
					
					break;
				case 203:
					turnGuideText.setText("直走");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic203();
					
					break;
				case 204:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic204();
					break;
				case 205:
					turnGuideText.setText("直走");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic205();
					break;
				case 206:
					turnGuideText.setText("前方右上高架");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic206();
					
					break;
				case 207:
					turnGuideText.setText("靠右行驶");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic207();
					
					break;
				case 208:
					turnGuideText.setText("直走");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic208();
					
					break;
				case 209:
					turnGuideText.setText("靠右下高架");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic209();
					
					break;
				case 210:
					turnGuideText.setText("靠右下高架");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					playMusic210();
					
					break;
				case 211:
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					playMusic211();
					
					break;
				case 212:
					playMusic212();
					
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					break;
				case 301:
					playMusic301();
					
					turnGuideText.setText("前方左转");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_left);
					break;
				case 302:
					playMusic302();
				
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					break;
				case 303:
					playMusic303();
					
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					break;
				case 304:
					playMusic304();
					
					turnGuideText.setText("保持直行");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					break;
				case 305:
					playMusic305();
					
					turnGuideText.setText("右转");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_right);
					
					break;
				case 306:
					playMusic306();
					
					turnGuideText.setText("直走");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					break;
				case 307:
					playMusic307();
					
					turnGuideText.setText("直走");
					turnIcon.setBackgroundResource(R.drawable.navi_turn_straight);
					break;
					
				}
			  }
			else {
				
			}	
				
			}
		};
	};
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_navi_pageview);
		Utils.currentView = 10;
		Utils.isNaviBegin = true;
		boolean isPOI = Utils.isPoi;
		if (! Utils.isNaviInitFinished) {
			if (!isPOI && Utils.currentScene == 1) {
				sceneHouse_1();
			}
			if (!isPOI && Utils.currentScene == 2) {
				sceneTongji_2();
			}if (!isPOI && Utils.currentScene == 3) {
				sceneBowuguan_3();
			}if (isPOI) {
				isPOI = false;
			}
			Utils.isNaviInitFinished = true;
		}
		MyApplication.serverStop();
		MyApplication.getInstance().setNaviHandler(mNaviHandler);
		MyApplication.serverStart();
		InitViewPager();
	}
	
	MediaPlayer scenePlayer0;
	public void sceneHouse_1(){
		scenePlayer0 = MediaPlayer.create(getBaseContext(), R.raw.navi_house_2); 
	    if(scenePlayer0!=null){
	    	scenePlayer0.start();
	    }
	}
	
	MediaPlayer scenePlayer1;	
	public void sceneGas_1(){
		scenePlayer1 = MediaPlayer.create(getBaseContext(), R.raw.navi_s2_gas); 
	    if(scenePlayer1!=null){
	    	scenePlayer1.start();
	    }
	}
	
	MediaPlayer scenePlayer2;
	public void sceneTongji_2(){
		scenePlayer2 = MediaPlayer.create(getBaseContext(), R.raw.navi_s2_tongji); 
	    if(scenePlayer2 != null){
	    	scenePlayer2.start();
	    }
	}
	
	MediaPlayer scenePlayer3;
	public void sceneBowuguan_3(){
		scenePlayer3 = MediaPlayer.create(getBaseContext(), R.raw.navi_s3_bowuguan3); 
	    if(scenePlayer3 != null){
	    	scenePlayer3.start();
	    }
	}
	
	MediaPlayer scenePlayer4;
	public void sceneRest_3(){
		scenePlayer4 = MediaPlayer.create(getBaseContext(), R.raw.navi_s3_rest); 
	    if(scenePlayer4 != null){
	    	scenePlayer4.start();
	    }
	}
	
	private void InitViewPager(){
		tfTurnVenera = Typeface.createFromAsset(getAssets(),
				"fonts/venera300.ttf");
		tfSourceHanLight = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		naviPager = (ViewPager)this.findViewById(R.id.navi_turn_to_turn_view);
		
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		
		View layout1 = mInflater.inflate(R.layout.view_navi_turn_to_turn, null);
		View layout2 = mInflater.inflate(R.layout.view_navi_normal, null);
		listViews.add(layout1);
		listViews.add(layout2);
		
		turnIcon = (ImageView)layout1.findViewById(R.id.navi_turn_icon);
		distanceToTurn = (TextView)layout1.findViewById(R.id.navi_distance);
		distanceToTurn.setTypeface(tfTurnVenera);
		turnGuideText = (TextView)layout1.findViewById(R.id.navi_hint);
		turnGuideText.setTypeface(tfSourceHanLight);
		naviTurnFinishBtn = (Button)layout1.findViewById(R.id.navi_turn_to_turn_finish_btn);
        naviTurnFinishBtn.setOnClickListener(this);
        naviTurnSearchpoiBtn = (Button)layout1.findViewById(R.id.navi_turn_to_turn_poi_search);
        naviTurnSearchpoiBtn.setOnClickListener(this);
       // naviTurnSearchpoiBtn = (Button)layout1.findViewById(R.id.navi_tu);
        fakeMapInitial(layout2);
		naviPager.setAdapter(new MyNaviPagerAdapter(listViews));
	}

	private AnimationDrawable animationDrawable;
	private AnimationDrawable animationDrawable2;
	private void fakeMapInitial(View view){
		MediaController fakeMapController = new MediaController(this); 
		fakeMapView = (VideoView)view.findViewById(R.id.fakeMapVideo);

		fakeMapController.setAnchorView(fakeMapView); 
		//fakeMapView.setMediaController(fakeMapController); 
		fakeMapView.setVideoURI(Uri.parse("/sdcard/Video/1210.mp4")); 
		//fakeMapView.setVideoURI(Uri.parse("/sdcard/Video/video.mp4")); 
		fakeMapView.start();
		isPlaying = true;
		new Thread(){
			public void run()
			{
				isPlaying = true;
				while (isPlaying) {
					currentPostion = fakeMapView.getCurrentPosition();
					Log.e("endPosition", String.valueOf(endPosition));
					if(currentPostion > endPosition){
						fakeMapView.pause();
						isPlaying = false;
					}
					try {
						sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			};
		}.start();
		//fakeMapView.
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.navi_turn_to_turn_finish_btn:
			Log.e("navi_turn_to_turn_finish_btn", "finish");
			Utils.isNaviBegin = false;
			Intent mIntent = new Intent(NaviTurnToTurnActivity.this,MainActivity.class);
			mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			mIntent.putExtra("NAVI_TURN_TO_TURN", true);
			startActivity(mIntent);
			break;
		case R.id.navi_turn_to_turn_poi_search:
			Intent mIntent2 = new Intent(NaviTurnToTurnActivity.this,NaviSearchActivity.class);
			mIntent2.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
			startActivity(mIntent2);
		default:
			break;
		}
	}
	
	boolean isNaviTurnToTurnFinished = true;
	class PhoneSelectTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isNaviTurnToTurnFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviTurnToTurnUsing(true);
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
			isNaviTurnToTurnFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviTurnToTurnUsing(false);
			MyApplication.serverStart();
		}
		
	}
	/**private void playMovie(int secondStart){
		fakeMapView.seekTo(secondStart);
		fakeMapView.start();
	}**/
	
	
}
