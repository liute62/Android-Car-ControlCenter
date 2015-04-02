package psa.radio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.apache.http.client.ClientProtocolException;

import psa.music.MusicActivity;
import psa.music.PlayerService;




import android.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.NetworkInfo.State;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import app.psa.Http;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class RadioActivity extends Activity implements OnClickListener{
	
	
	private Button radioVoiceMax;
	private Button radioVoiceMin;
	private Button radioNextBtn;
	private Button radioPreBtn;
	
	private Button playAndPause;
	
	private Button toPlay;
	private Typeface tfRadioVenera;
	private Typeface tfRadioNexaLight;
	
	private TextView mRadioNumCenter;
	private TextView radioVoice;
	
	private int radioVoiceTmp;
	private int systemVoiceMax;
	private int systemVoiceCur;

	private AudioManager mAudioManager;
	public static MediaPlayer radioMediaPlayer;
	
	//private MediaPlayer radioIsToPlayMediaPlayer;
	
	private RadioChannelTask radioVoiceTask;
	Boolean radioIsPlaying = false;

	/**highlight ¿Ø¼þ**/
	RelativeLayout mRadioSlideParent;
	ImageView mRadioSlideLight;
    private View selected_item = null;
	private int offset_x = 0;
	private int default_x = 0;
	private int default_y = 100;
	RelativeLayout.LayoutParams lp;
	int count = 0;
	
	/**voice highlight ¿Ø¼þ**/
	RelativeLayout mVoiceSlideParent;
	ImageView mVoiceSlideLight;
	
	boolean isChannelFinished = true;
	RadioChannelTask mChannelTask;
	private List<String> radioList = new ArrayList<String>();
	private int radioListPos = 0;
	MusicVoiceTask musicVoiceTask;
	private void playAndPause(){
		if(radioIsPlaying){
			if(radioMediaPlayer!=null){
				radioMediaPlayer.release();
			}
			playAndPause.setBackgroundResource(R.drawable.music_play_icon);
			radioIsPlaying = false;
		}
		else{
			String tmp = mRadioNumCenter.getText().toString();
			playRadio(tmp);
			radioIsPlaying = true;
			playAndPause.setBackgroundResource(R.drawable.music_pause_icon);
			if (isChannelFinished) {
				mChannelTask = new RadioChannelTask();
				mChannelTask.execute(String.valueOf(findRadioIndex(tmp)));
			}
		}
	}
	
	private boolean isRadioVolumeFinished = true;
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.music_volume) {
				String result = (String)msg.obj;
				String tmpVoice = radioVoice.getText().toString();
				radioVoice.setText(result);
				if (Integer.valueOf(result) > Integer.valueOf(tmpVoice)) {
					//Jinle();
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_RAISE, 0);
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_SYSTEM,
							AudioManager.ADJUST_RAISE, 0);	
				}
				if (Integer.valueOf(result) < Integer.valueOf(tmpVoice)) {
					//Jinle();
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_LOWER, 0);
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_SYSTEM,
							AudioManager.ADJUST_LOWER, 0);
				}
				if ( isRadioVolumeFinished) {
					musicVoiceTask = new MusicVoiceTask();
					musicVoiceTask.execute(result);
				}
			}
			
			if (msg.what == Utils.radio_volume_left) {
				if (! isRadioVolumeFinished) {
					radioVoiceDown();
				}
				isRadioVolumeFinished = true;
			}
			if (msg.what == Utils.radio_volume_right) {
				if (! isRadioVolumeFinished) {
					radioVoiceUp();
				}
				isRadioVolumeFinished = true;
			}
			if (msg.what == Utils.radio_control) {
				Bundle result = (Bundle) msg.obj;
				String action = result.getString("action");
				Log.e("handler", action);
				if (action.equals("play")) {
					//ConfirmPlay();
					playAndPause();
				}
				if (action.equals("pause")) {
					//pause()
					playAndPause();
				}
				if (action.equals("next")) {
					radioIsPlaying = true;
					playRadioNext();
				}
				if (action.equals("previous")) {
					radioIsPlaying = true;
					playRadioPre();
				}
			}
			if (msg.what == Utils.radio_search) {
				//String result = (String) msg.obj;
				//Log.e("radio_search", result);
				//Ìøµ½101.7
				if (isChannelFinished) {
					mChannelTask = new RadioChannelTask();
					mChannelTask.execute("3");
				}
				if (radioMediaPlayer!= null
						&& radioMediaPlayer.isPlaying()) {
					radioMediaPlayer.release();
				}
				//playRadio("101.8");
				playRadio("101.7");
				mRadioNumCenter.setText("101.7");
				
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_radio);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
		MyApplication.serverStop();
		MyApplication.getInstance().setRadioHandler(mHandler);
		MyApplication.serverStart();

		initial();
		if (isFinishing()) {
			radioMediaPlayer.release();
		}
		radioVoiceSlide();
		radioSlideInitial();

	}

	private void radioListInitial() {
		// TODO Auto-generated method stub
		radioList.add("87.9");
		radioList.add("94.0");
		radioList.add("99.0");
		radioList.add("101.7");
		radioList.add("103.7");
	}
	
	private void initial() {
		radioListInitial();
		tfRadioVenera = Typeface.createFromAsset(getAssets(),
				"fonts/venera300.ttf");
		tfRadioNexaLight = Typeface.createFromAsset(getAssets(),
				"fonts/nexalight.ttf");

		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		systemVoiceCur = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		systemVoiceMax = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		mRadioNumCenter = (TextView) this.findViewById(R.id.radio_num_center);
		radioVoice = (TextView) this.findViewById(R.id.text_radio_voice);

		mRadioNumCenter.setTypeface(tfRadioNexaLight);
		mRadioNumCenter.setText(radioList.get(Utils.currentRadioIndex));
		
		radioVoice.setTypeface(tfRadioVenera);
		radioVoice.setText(String.valueOf(systemVoiceCur));
		
		radioVoiceMax = (Button)this.findViewById(R.id.radio_voice_max);
		radioVoiceMax.setOnClickListener(this);
		radioVoiceMin = (Button)this.findViewById(R.id.radio_voice_min);
		radioVoiceMin.setOnClickListener(this);
		radioNextBtn = (Button)this.findViewById(R.id.radio_next);
		radioNextBtn.setOnClickListener(this);
		radioPreBtn = (Button)this.findViewById(R.id.radio__pre);
		radioPreBtn.setOnClickListener(this);
		toPlay = (Button)this.findViewById(R.id.istoplay);
		toPlay.setOnClickListener(this);
		playAndPause = (Button)this.findViewById(R.id.radioSwitch);
		playAndPause.setOnClickListener(this);
		if (radioIsPlaying) {
			playAndPause.setBackgroundResource(R.drawable.music_pause_icon);
			
		}
	}




	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.radio_voice_max:
			radioVoiceTmp = Integer.valueOf(radioVoice.getText().toString());
			radioVoiceTmp++;
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, 0);
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_RAISE, 0);
			if (radioVoiceTmp > systemVoiceMax) {
				radioVoice.setText(String.valueOf(systemVoiceMax));
			    radioVoiceTmp = 15;

			} else {
				radioVoice.setText(String.valueOf(radioVoiceTmp));
				
			}
			break;
			
		case R.id.radio_voice_min:
			radioVoiceTmp = Integer.valueOf(radioVoice.getText().toString());
			radioVoiceTmp--;
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, 0);
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_LOWER, 0);
			if (radioVoiceTmp < 0) {
				radioVoice.setText("0");
				radioVoiceTmp = 0;
			} else {
				radioVoice.setText(String.valueOf(radioVoiceTmp));
			}
			
			break;
			
		case R.id.radio__pre:
			if(PlayerService.mediaPlayer!=null){
				PlayerService.mediaPlayer.release();
				
			}
			radioIsPlaying = true;
			
			playRadioPre();
			
			break;
			
		case R.id.radio_next:
			if(PlayerService.mediaPlayer!=null){
				PlayerService.mediaPlayer.release();
				
			}
			radioIsPlaying = true;
			
			playRadioNext();
			
			break;
		case R.id.istoplay:
			ConfirmPlay();
			break;
		case R.id.radioSwitch:
			playAndPause();
			break;
		}
		
		
		
	}
	
	
	private void playRadioNext() {
		// TODO Auto-generated method stub
		
		if(radioMediaPlayer!=null){
		radioMediaPlayer.release();}
		Utils.currentRadioIndex++;
		radioListPos = Utils.currentRadioIndex;
		//radioListPos += 1;
		if(radioListPos >=radioList.size()){
			radioListPos = 0;
			Utils.currentRadioIndex = 0;
		}
		//Utils.currentRadioIndex = radioListPos;
		playAndPause.setBackground(getResources().getDrawable(R.drawable.music_pause_icon));
		String a = radioList.get(radioListPos).toString();
		mRadioNumCenter.setText(a);
		playRadio(a);
		
		if (isChannelFinished) {
			mChannelTask = new RadioChannelTask();
			mChannelTask.execute(String.valueOf(radioListPos));
		}
				
	
	}

	

	private void IsToPlay() {
		// TODO Auto-generated method stub
		/**radioIsToPlayMediaPlayer = MediaPlayer.create(getBaseContext(),
				R.raw.istoplay);
		if (isFinishing()) {
			radioMediaPlayer.release();	
		}
		if (radioIsToPlayMediaPlayer != null) {
			radioIsToPlayMediaPlayer.start();

		}**/
	}
	

	private void playRadio(String num) {
		if(PlayerService.mediaPlayer!=null){
			PlayerService.mediaPlayer.release();
			Utils.isMusicRelease = true;
			//PlayerService.mediaPlayer.stop();
		}
		
		if(num.equals("87.9"))
		{
			radioMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fm879);  
			radioMediaPlayer.start();
			radioMediaPlayer.setLooping(true);
			
		}
		else if(num.equals("101.7"))
		{
			radioMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fm935);  
			radioMediaPlayer.start();
			radioMediaPlayer.setLooping(true);
			
		}else if (num.equals("101.8")) {
			if (isFinishing()) {
				radioMediaPlayer.release();	
			}
			IsToPlay();
		}
		else if(num.equals("94.0")){
			radioMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fm940);  
			radioMediaPlayer.start();
			radioMediaPlayer.setLooping(true);
		}
		
		else if(num.equals("99.0")){
			radioMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fm990);  
			radioMediaPlayer.start();
			radioMediaPlayer.setLooping(true);
		}
		
		else if(num.equals("103.7")){
			radioMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fm1037);  
			radioMediaPlayer.start();
			radioMediaPlayer.setLooping(true);
		}
		
		if (isChannelFinished) {
			mChannelTask = new RadioChannelTask();
			mChannelTask.execute(String.valueOf(findRadioIndex(num)));
		}
	}

	private void ConfirmPlay() {
		// TODO Auto-generated method stub
		
			radioMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.fm935);  
			radioMediaPlayer.start();
			radioMediaPlayer.setLooping(true);
			
	}

	private void playRadioPre() {
		// TODO Auto-generated method stub
		if(radioMediaPlayer!=null){
			radioMediaPlayer.release();}
		Utils.currentRadioIndex--;
		radioListPos = Utils.currentRadioIndex;
		//radioListPos -= 1;
		if(radioListPos < 0){
			radioListPos = radioList.size()-1;
			Utils.currentRadioIndex = radioListPos;
		}
		//Utils.currentRadioIndex = radioListPos;
		playAndPause.setBackground(getResources().getDrawable(R.drawable.music_pause_icon));
		String a = radioList.get(radioListPos).toString();
		mRadioNumCenter.setText(a);
		if (isChannelFinished) {
			mChannelTask = new RadioChannelTask();
			mChannelTask.execute(String.valueOf(radioListPos));
		}
		playRadio(a);
		
	}

	
	RelativeLayout.LayoutParams radioVoiceSlideLp;
	float x = 0;
	float tmp = 0; 
	private float DownX;
	private float DownY;
	
	private void radioVoiceUp(){
		radioVoiceTmp = Integer.valueOf(radioVoice.getText().toString());
		radioVoiceTmp+=1;
		mAudioManager.adjustStreamVolume(
				AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_RAISE, 0);
		mAudioManager.adjustStreamVolume(
				AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_RAISE, 0);
		if (radioVoiceTmp > systemVoiceMax) {
			radioVoice.setText(String.valueOf(systemVoiceMax));
			radioVoiceTmp = 15;

		} else {
			radioVoice.setText(String.valueOf(radioVoiceTmp));
		}
		/*String a = radioVoice.getText().toString();
		int b = Integer.valueOf(a);
		b+=1;
		//musicVoice.setText(String.valueOf(b));
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_RAISE, 0);
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_RAISE, 0);
		Message message = new Message();
		if(b<15)
		{
		message.obj = String.valueOf(b);
		mHandler.sendMessageDelayed(message, 150);
		}
		else{
			message.obj = String.valueOf(15);
			mHandler.sendMessageDelayed(message, 150);
		}*/
	}
	private void radioVoiceDown(){
		/*String a = radioVoice.getText().toString();
		int b = Integer.valueOf(a);
		b-=1;
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_LOWER, 0);
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_LOWER, 0);
		if(b>=0){
		Message message = new Message();
		//musicVoice.setText(String.valueOf(b));
		message.obj = String.valueOf(b);
		mHandler.sendMessageDelayed(message, 150);
		}else{
			Message message = new Message();
			//musicVoice.setText(String.valueOf(b));
			message.obj = String.valueOf(0);
			mHandler.sendMessageDelayed(message, 150);
		}*/
		radioVoiceTmp = Integer.valueOf(radioVoice.getText().toString());
		radioVoiceTmp-=1;
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_LOWER, 0);
		mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_LOWER, 0);
		if (radioVoiceTmp < 0) {
			radioVoice.setText("0");
			radioVoiceTmp = 0;

		} else {
			radioVoice.setText(String.valueOf(radioVoiceTmp));
		}

	}
	
	private void radioVoiceSlide(){
		mVoiceSlideParent = (RelativeLayout)findViewById(R.id.radio_center_layout);
		mVoiceSlideLight = new ImageView(this);
		mVoiceSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
		mVoiceSlideLight.setVisibility(View.INVISIBLE);
    	radioVoiceSlideLp = new LayoutParams(300,300);
    	radioVoiceSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mVoiceSlideLight.setLayoutParams(radioVoiceSlideLp);
    	mVoiceSlideParent.addView(mVoiceSlideLight);
    	final Stack<Float> mStack = new Stack<Float>();
    	final Stack<Integer> isDown = new Stack<Integer>();
    	mVoiceSlideParent.setOnTouchListener(new OnTouchListener() {
  
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				tmp = x;
				x = event.getX();
				
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							x = event.getX();
							if (Utils.isCenterMusicSlideInField(x)) {
								isDown.push(1);
							}else {
								isDown.push(0);
							}
							Log.e("Music ACTION_DOWN", String.valueOf(x));
							mVoiceSlideLight.setLayoutParams(radioVoiceSlideLp);
							mVoiceSlideLight.setX(x-150);
							mVoiceSlideLight.setVisibility(View.VISIBLE);
							DownX = event.getX();//float DownX
							DownY = event.getY();//float DownY
						break;
					case MotionEvent.ACTION_MOVE:
							if (isDown.pop() == 1)
							{
								if(x > 1300){
									if (isRadioVolumeFinished) {
										isRadioVolumeFinished = false;
										mHandler.sendEmptyMessageDelayed(Utils.radio_volume_right, 200);
										
									}
								}
								if (x <350) {
									if (isRadioVolumeFinished) {
										isRadioVolumeFinished = false;
										mHandler.sendEmptyMessageDelayed(Utils.radio_volume_left,200);
										
									}
								}
								isDown.push(1);
							}else {
								isDown.push(0);
							}
							if(Utils.isCenterMusicSlideInField(x))
							{
								int a = Utils.sildeUtil(mStack, x);
								mVoiceSlideParent.removeView(mVoiceSlideLight);
								mVoiceSlideParent.addView(mVoiceSlideLight);
								mVoiceSlideLight.setLayoutParams(radioVoiceSlideLp);
								mVoiceSlideLight.setX(x-150);
								radioVoice.setTextSize(70);
								//float moveX = event.getX() - DownX;//XÖá¾àÀë
								//float moveY = event.getY() - DownY;
								
							
								if(a == 1)
								{
									radioVoiceUp();				
							
								}if (a== 0) {
									radioVoiceDown();
								}
							}
							Log.e("Music ACTION_MOVE x", String.valueOf(tmp));
							Log.e("Music ACTION_MOVE tmp", String.valueOf(x));
						break;
					case MotionEvent.ACTION_UP:
						  
							isRadioVolumeFinished = true;
							mVoiceSlideLight.setVisibility(View.INVISIBLE);
							mVoiceSlideParent.removeView(mVoiceSlideLight);
							radioVoice.setTextSize(27);
							if (isChannelFinished) {
								mChannelTask = new RadioChannelTask();
								mChannelTask.execute(String.valueOf(
										findRadioIndex(mRadioNumCenter.getText().toString())));
							}
						break;
					default:
						break;
				}
				
				return true;
			}
		});
	}
	
	String[] radioSlideNow;
	RelativeLayout.LayoutParams bottomSlideLp;
	
	private void radioSlideInitial(){
    	final Stack<Float> mStack = new Stack<Float>();
    	final String string = "1";
    	
    	mRadioSlideParent = (RelativeLayout)findViewById(R.id.radio_progress);
    	mRadioSlideLight = new ImageView(this);
    	mRadioSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
    	mRadioSlideLight.setVisibility(View.INVISIBLE);
    	bottomSlideLp = new LayoutParams(300,300);
    	bottomSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mRadioSlideLight.setLayoutParams(bottomSlideLp);
    	mRadioSlideParent.addView(mRadioSlideLight);
    	mRadioSlideParent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				float x;
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							x = event.getX();
							Log.e("down", String.valueOf(x));
							mRadioSlideLight.setLayoutParams(bottomSlideLp); 
							mRadioSlideLight.setX(x-150);
							mRadioSlideLight.setVisibility(View.VISIBLE);
							
							mRadioNumCenter.setVisibility(View.VISIBLE);
							
						break;
					case MotionEvent.ACTION_MOVE:
						 mRadioSlideParent.removeView(mRadioSlideLight);
						 x = event.getX();
						if(Utils.isRadioSlideInField(x)){
							mRadioSlideParent.addView(mRadioSlideLight);
							mRadioSlideLight.setLayoutParams(bottomSlideLp);
							mRadioSlideLight.setX(x-150);
							int a = Utils.sildeUtil(mStack, x);
							if ( a == 1) {
								if(radioMediaPlayer!=null){
									radioMediaPlayer.release();
									radioIsPlaying = true;
								playRadioNext();
								
								}
							}if (a == 0) {
								if(radioMediaPlayer!=null){
									radioMediaPlayer.release();
									}
								radioIsPlaying = true;
								playRadioPre();
							}
						}
						break;
					case MotionEvent.ACTION_UP:
						mRadioSlideLight.setVisibility(View.INVISIBLE);
						mRadioSlideParent.removeView(mRadioSlideLight);
						mRadioSlideLight.setVisibility(View.VISIBLE);	
						
						mRadioNumCenter.setVisibility(View.VISIBLE);
						break;
					default:
						break;
				}
				
				return true;
			}
		});
    }
	
	private int findRadioIndex(String name){
		int a = 0;
		if (name.equals("87.9")) {
			a = 0; 
		}if (name.equals("94.0")) {
			a = 1;
		}if (name.equals("99.0")) {
			a = 2;
		}if (name.equals("101.7")) {
			a = 3;
		}if (name.equals("103.7")) {
			a = 4;
		}
		return a ;
	}
	
	private String findRadioByIndex(int index){
		String tmp = "87.9";
		switch (index) {
		case 0:
			tmp = "87.9";
		case 1:
			tmp = "94.0";
		case 2:
			tmp = "99.0";
		case 3:
			tmp = "101.7";
		case 4:
			tmp = "103.7";
			break;
		default:
			break;	
		}
		return tmp;
	}
	
	
		
	class RadioChannelTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isChannelFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setRadioUsing(true);
			MyApplication.serverStart();
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String channel = arg0[0];
			Http.radioNowPost(channel);
			return 1;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			isChannelFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setRadioUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	class MusicVoiceTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isRadioVolumeFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMusicUsing(true);
			MyApplication.serverStart();
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String voice = arg0[0];
			try {
				Http.AudioVolumePost(voice);
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
			isRadioVolumeFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMusicUsing(false);
			MyApplication.serverStart();
		}
		
	}
}