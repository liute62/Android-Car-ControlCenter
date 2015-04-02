package psa.music;

import java.io.IOException;
import java.text.BreakIterator;
import java.util.List;
import java.util.Stack;

import org.apache.http.client.ClientProtocolException;
import org.w3c.dom.Text;

import psa.navi.HelloServer;
import psa.radio.RadioActivity;

import android.R.bool;
import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.VoicemailContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.RelativeLayout.LayoutParams;
import app.psa.Http;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class MusicActivity extends Activity implements OnClickListener {
	
	private TextView musicTitle;
	private TextView musicArtist;
	private TextView musicAlbum;
	private TextView musicVoice;
	
	private TextView musicEqualizerRock; 
	private TextView musicEqualizerFunk;
	private TextView musicEqualizerJazz;
	
	private Typeface tfMusicNexaLight;
	private Typeface tfMusicVenera;

	private Button musicVoiceMax;
	private Button musicVoiceMin;
	private Button musicNextBtn;
	private Button musicPreBtn;
	private Button musicPausePlayBtn;

	private int musicVoiceTmp;
	private int musicListPos = 0;
	private int systemVoiceMax;
	private int systemVoiceMin;
	private int systemVoiceCur;
	private int a;
	
	private float DownX;
	private float DownY;
	
	private RelativeLayout musicPlayBg;

	private AudioManager mAudioManager;
	private MediaPlayer musicSearchVoice;
	List<Mp3Info> mp3Infos;

	/**topSlide**/
	private RelativeLayout mTopSlideParent;
	private RelativeLayout.LayoutParams topSlideLp;
	private ImageView mTopSlideLight;
	
	/**centerSlide**/
	private RelativeLayout mCenterSlideParent;
	private RelativeLayout.LayoutParams centerSlideLp;
	private ImageView mCenterSlideLight;
	
	/**bottomSlide**/
	private RelativeLayout mBottomSlideParent;
	private RelativeLayout.LayoutParams bottomSlideLp;
	private ImageView mBottomSlideLight;
	
	private MusicVoiceTask musicVoiceTask;
	private MusicVoiceTask musicSlideVoiceTask;

	private HelloServer mMusicServer;
	private Button testSearch;
	
	//private MediaPlayer JinglePlayer;
	
	MusicTitleTask mTitleTask;
	MusicVoiceTask mVoiceTask;
	private boolean isTitleFinished = true;
	private boolean isVoiceFinished = true;
	private boolean isMusicVolumeFinished = true;
	private int leftOrRight = -1;
	private Handler mHandler = new Handler(){
		@Override
		public void dispatchMessage(Message msg) {
			// TODO Auto-generated method stub
			super.dispatchMessage(msg);
			if (msg.what == Utils.music_volume_left) {
				if (! isMusicVolumeFinished) {
					voiceDown();	
				}
				//isMusicVolumeFinished = true;
				
			}if (msg.what == Utils.music_volume_right) {
				if (! isMusicVolumeFinished) {
					voiceUp();	
				}
				//isMusicVolumeFinished = true;
			}
			
			if (msg.what == Utils.music_slide_left) {
				//×ó±ßÇÐ¸è
				playPre();
				
			}if (msg.what == Utils.music_slide_right) {
				//ÓÒ±ßÇÐ¸è
				playNext();
			}
			if (msg.what == Utils.music_volume) {
				String result = (String)msg.obj;
				String tmpVoice = musicVoice.getText().toString();
				musicVoice.setText(result);
				if (Integer.valueOf(result) > Integer.valueOf(tmpVoice)) {
					Jinle();
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_RAISE, 0);
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_SYSTEM,
							AudioManager.ADJUST_RAISE, 0);	
				}
				if (Integer.valueOf(result) < Integer.valueOf(tmpVoice)) {
					Jinle();
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_MUSIC,
							AudioManager.ADJUST_LOWER, 0);
					mAudioManager.adjustStreamVolume(
							AudioManager.STREAM_SYSTEM,
							AudioManager.ADJUST_LOWER, 0);
				}
				if ( isVoiceFinished) {
					mVoiceTask = new MusicVoiceTask();
					mVoiceTask.execute(result);
				}
			}
			if (msg.what == Utils.music_action) {
				String result = (String)msg.obj;
				 if(result.equals("play")){
					 
					play();
				}if (result.equals("pause")) {
					play();
				}if (result.equals("next")) {
					Jinle();
					playNext();
				}if (result.equals("previous")) {
					Jinle();
					playPre();
				}
			}if (msg.what == Utils.music_search) {
				String result = (String)msg.obj;
				//Ö±½ÓÌøµ½´«Ææ
				//VoiceIstoPlay();
				Utils.isMusicPlaying = true;
				if (Utils.currentScene == 1) {
					Utils.currentMusicIndex = 2;
					//musicListPos = 2;
					if (isTitleFinished) {
						mTitleTask = new MusicTitleTask();
						mTitleTask.execute("2");
					}
					play();
					play();	
				}if(Utils.currentScene == 2){
					Utils.currentMusicIndex = 3;
					//musicListPos = 3;
					if (isTitleFinished) {
						mTitleTask = new MusicTitleTask();
						mTitleTask.execute("3");
					}
					play();
					play();	
				}
			}
		}
	};
	
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_music);
		overridePendingTransition(R.anim.left_in, R.anim.left_out);
		MyApplication.serverStop();
		MyApplication.getInstance().setMusicHandler(mHandler);
		MyApplication.serverStart();
		initial();
		//server.setHandler(OZAudioHandler);
		if (isTitleFinished) {
			mTitleTask = new MusicTitleTask();
			mTitleTask.execute("0");
		}
	}

	protected void VoiceIstoPlay() {
		// TODO Auto-generated method stub
		//musicSearchVoice = MediaPlayer.create(getBaseContext(), R.raw.istoplay);  
		//musicSearchVoice.start();
	}

	protected void Jinle() {
		// TODO Auto-generated method stub
		//JinglePlayer = MediaPlayer.create(getBaseContext(), R.raw.jinle); 
	    //if(JinglePlayer!=null){
	    	//JinglePlayer.start();
	    //}
	}

	private void initial() {
		tfMusicVenera = Typeface.createFromAsset(getAssets(),
				"fonts/venera300.ttf");
		tfMusicNexaLight = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		
		mAudioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
		systemVoiceCur = mAudioManager
				.getStreamVolume(AudioManager.STREAM_MUSIC);
		systemVoiceMax = mAudioManager
				.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

		mp3Infos = MediaUtil.getMp3Infos(getApplicationContext());

		musicTitle = (TextView) this.findViewById(R.id.text_music_title);
		musicArtist = (TextView) this.findViewById(R.id.text_music_artist);
		musicAlbum = (TextView) this.findViewById(R.id.text_music_album);
		musicVoice = (TextView) this.findViewById(R.id.text_music_voice);
		musicVoice.setText(String.valueOf(systemVoiceCur));
		
		musicEqualizerFunk = (TextView)findViewById(R.id.text_music_equalizer_funk);
		musicEqualizerRock = (TextView)findViewById(R.id.text_music_equalizer_rock);
		musicEqualizerJazz = (TextView)findViewById(R.id.text_music_equalizer_jazz);
		
		musicTitle.setTypeface(tfMusicNexaLight);
		musicArtist.setTypeface(tfMusicNexaLight);
		musicAlbum.setTypeface(tfMusicNexaLight);
		musicVoice.setTypeface(tfMusicVenera);
		
		musicEqualizerFunk.setTypeface(tfMusicVenera);
		musicEqualizerRock.setTypeface(tfMusicVenera);
		musicEqualizerJazz.setTypeface(tfMusicVenera);
		
		musicTitle.setText(mp3Infos.get(Utils.currentMusicIndex).getTitle());
		musicArtist.setText(mp3Infos.get(Utils.currentMusicIndex).getArtist());
		musicAlbum.setText(mp3Infos.get(Utils.currentMusicIndex).getAlbum());
		musicPlayBg = (RelativeLayout)this.findViewById(R.id.music_album_bg);
		
		
		musicVoiceMax = (Button) this.findViewById(R.id.music_voice_max);
		musicVoiceMin = (Button) this.findViewById(R.id.music_voice_min);
		musicNextBtn = (Button) this.findViewById(R.id.music_next);
		musicPreBtn = (Button) this.findViewById(R.id.musci_pre);
		musicPausePlayBtn = (Button) this.findViewById(R.id.music_pause);
		if (Utils.isMusicPlaying == true) {
			musicPausePlayBtn.setBackgroundDrawable(getResources().getDrawable(R.drawable.music_pause_icon));
		}
		testSearch = (Button)this.findViewById(R.id.music_test);
		testSearch.setOnClickListener(this);
		
		musicVoiceMax.setOnClickListener(this);
		musicVoiceMin.setOnClickListener(this);
		musicNextBtn.setOnClickListener(this);
		musicPreBtn.setOnClickListener(this);
		musicPausePlayBtn.setOnClickListener(this);
		topInitial();
		centerInitial();
		bottomInitial();
	}

	float tmp = 0;
	float xTop = 0;
	float xTopDown = 0;
	static float xTopDis = 0;
	private void topInitial(){
	   mTopSlideParent = (RelativeLayout)findViewById(R.id.music_top_layout);
	   mTopSlideLight = new ImageView(this);
	   mTopSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
	   mTopSlideLight.setVisibility(View.INVISIBLE);
	   topSlideLp = new LayoutParams(300,300);
	   topSlideLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
	   //topSlideLp.setMargins(0, 55, 0, 0);
	   mTopSlideLight.setLayoutParams(topSlideLp);
	   mTopSlideParent.addView(mTopSlideLight);
	   mTopSlideParent.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				xTop = event.getX();
				
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							xTop = event.getX();
							mTopSlideLight.setLayoutParams(topSlideLp);
							mTopSlideLight.setX(xTop-150);
							mTopSlideLight.setVisibility(View.VISIBLE);
							xTopDown = event.getX();
						break;
					case MotionEvent.ACTION_MOVE:
						    mTopSlideParent.removeView(mTopSlideLight);
							xTop = event.getX();
							mTopSlideParent.addView(mTopSlideLight);
							mTopSlideLight.setLayoutParams(topSlideLp);
							mTopSlideLight.setX(xTop-150);
							
						break;
					case MotionEvent.ACTION_UP:
						xTop = event.getX();
						if (xTop - xTopDown > 0) {
							//ÍùÓÒ±ß»¬¶¯
							xTopDis = 0;
							xTopDis = xTop - xTopDown;
							if (xTopDis > 100) {
								mHandler.sendEmptyMessageDelayed(Utils.music_slide_right, 300);
							}
						}else {
							//Íù×ó±ß»¬¶¯
							xTopDis = 0;
							xTopDis = xTopDown - xTop;
							if (xTopDis > 100) {
								mHandler.sendEmptyMessageDelayed(Utils.music_slide_left,300);
							}
						}
						mTopSlideLight.setVisibility(View.INVISIBLE);
						mTopSlideParent.removeView(mTopSlideLight);
						break;
					default:
						break;
				}
				
				return true;
			}
		});
	}
	float xCenter = 0;
	private void voiceUp(){
		musicVoiceTmp = Integer.valueOf(musicVoice.getText().toString());
		musicVoiceTmp+=1;
		mAudioManager.adjustStreamVolume(
				AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_RAISE, 0);
		mAudioManager.adjustStreamVolume(
				AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_RAISE, 0);
		if (musicVoiceTmp > systemVoiceMax) {
			musicVoice.setText(String.valueOf(systemVoiceMax));
			musicVoiceTmp = 15;

		} else {
			musicVoice.setText(String.valueOf(musicVoiceTmp));
		}
		//Message message = new Message();
		//message.obj = String.valueOf(musicVoiceTmp);
		//mHandler.sendMessageDelayed(message, 150);
		/*String a = musicVoice.getText().toString();
		int b = Integer.valueOf(a);
		b += 1;
		if (b <= systemVoiceMax) {
			for (int i = 0; i < b; i++) {
				mAudioManager.adjustStreamVolume(
						AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, 0);
				mAudioManager.adjustStreamVolume(
						AudioManager.STREAM_SYSTEM,
						AudioManager.ADJUST_RAISE, 0);
			}

			// musicVoice.setText(String.valueOf(b));
			Message message = new Message();
			message.obj = String.valueOf(b);
			mHandler.sendMessageDelayed(message, 150);
		} else {

		}*/
		MusicVoiceTask musicVoiceTask3 = new MusicVoiceTask();
		musicVoiceTask3.execute(String.valueOf(musicVoiceTmp));

	}
	private void voiceDown(){
		/*String a = musicVoice.getText().toString();
		int b = Integer.valueOf(a);
		b -= 1;
		if (b >= 0) {
			for (int i = 0; i < b; i++) {
				mAudioManager.adjustStreamVolume(
						AudioManager.STREAM_MUSIC,
						AudioManager.ADJUST_RAISE, 0);
				mAudioManager.adjustStreamVolume(
						AudioManager.STREAM_SYSTEM,
						AudioManager.ADJUST_RAISE, 0);
			}

			// musicVoice.setText(String.valueOf(b));
			Message message = new Message();
			message.obj = String.valueOf(b);
			mHandler.sendMessageDelayed(message, 150);
		} else {

		}*/
		musicVoiceTmp = Integer.valueOf(musicVoice.getText()
				.toString());
		musicVoiceTmp -= 1;
		mAudioManager.adjustStreamVolume(
				AudioManager.STREAM_MUSIC,
				AudioManager.ADJUST_LOWER, 0);
		mAudioManager.adjustStreamVolume(
				AudioManager.STREAM_SYSTEM,
				AudioManager.ADJUST_LOWER, 0);
		if (musicVoiceTmp <= 0) {
			musicVoice.setText("0");
			musicVoiceTmp = 0;

		} else {
			musicVoice.setText(String.valueOf(musicVoiceTmp));
		}
		MusicVoiceTask musicVoiceTask4 = new MusicVoiceTask();
		musicVoiceTask4.execute(String.valueOf(musicVoiceTmp));

	}
	
	private void centerInitial(){
		mCenterSlideParent = (RelativeLayout)findViewById(R.id.music_center_layout);
    	mCenterSlideLight = new ImageView(this);
    	mCenterSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
    	mCenterSlideLight.setVisibility(View.INVISIBLE);
    	centerSlideLp = new LayoutParams(300,300);
    	centerSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
    	mCenterSlideLight.setLayoutParams(centerSlideLp);
    	mCenterSlideParent.addView(mCenterSlideLight);
    	final Stack<Float> mStack = new Stack<Float>();
    	final Stack<Integer> isDown = new Stack<Integer>();
    	final int a = 0;
    	mCenterSlideParent.setOnTouchListener(new OnTouchListener() {
  
			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				
				tmp = xCenter;
				xCenter = event.getX();
				
				switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
							xCenter = event.getX();
							if (Utils.isCenterMusicSlideInField(xCenter)) {
								isDown.push(1);
								
							}else {
								isDown.push(0);
							}
							Log.e("RadioVoice ACTION_DOWN", String.valueOf(xCenter));
							mCenterSlideLight.setLayoutParams(centerSlideLp);
							mCenterSlideLight.setX(xCenter-150);
							mCenterSlideLight.setVisibility(View.VISIBLE);
							//DownX = event.getX();//float DownX
							//DownY = event.getY();//float DownY
						break;
				case MotionEvent.ACTION_MOVE:
					xCenter = event.getX();
					//float moveX = event.getX() - DownX;// XÖá¾àÀë
					// float moveY = event.getY() - DownY;
					if (isDown.pop() == 1)
					{
						if(xCenter > 1300){
							if (isMusicVolumeFinished) {
								isMusicVolumeFinished = false;
								mHandler.sendEmptyMessageDelayed(Utils.music_volume_right, 200);
								
							}
						}
						if (xCenter <300) {
							if (isMusicVolumeFinished) {
								isMusicVolumeFinished = false;
								mHandler.sendEmptyMessageDelayed(Utils.music_volume_left,200);
								
							}
						}
						isDown.push(1);
					}else {
						isDown.push(0);
					}
					if(Utils.isCenterMusicSlideInField(xCenter))
					{
						int a = Utils.sildeUtil(mStack, xCenter);
						mCenterSlideParent.removeView(mCenterSlideLight);
						mCenterSlideParent.addView(mCenterSlideLight);
						mCenterSlideLight.setLayoutParams(centerSlideLp);
						mCenterSlideLight.setX(xCenter - 150);
						musicVoice.setTextSize(70);
						
					if (a == 1) {
						voiceUp();
					}
					if (a == 0) {
						voiceDown();
											// MusicVoiceTask musicVoiceTask3= new MusicVoiceTask();
					// musicVoiceTask3.execute(String.valueOf(musicVoiceTmp));
					  }
					}
					Log.e("Music ACTION_MOVE x", String.valueOf(tmp));
					Log.e("Music ACTION_MOVE tmp", String.valueOf(xCenter));
					break;
					case MotionEvent.ACTION_UP:
							
							isMusicVolumeFinished = true;
						    if (mStack.size() != 0) {

								mStack.clear();	
							}if (isDown.size() != 0) {
								isDown.pop();
							}
							mCenterSlideLight.setVisibility(View.INVISIBLE);
							mCenterSlideParent.removeView(mCenterSlideLight);
							musicVoice.setTextSize(27);
							if (isVoiceFinished) {
								mVoiceTask = new MusicVoiceTask();
								mVoiceTask.execute(musicVoice.getText().toString());
							}
						break;
					default:
						break;
				}
				
				return true;
			}
		});
	}
	
	private String[] musicTypeNow;

	private void bottomInitial() {
		mBottomSlideParent = (RelativeLayout) findViewById(R.id.music_bottom_layout);
		mBottomSlideLight = new ImageView(this);
		mBottomSlideLight.setImageResource(R.drawable.music_equalizer_bar_hl);
		mBottomSlideLight.setVisibility(View.INVISIBLE);
		bottomSlideLp = new LayoutParams(300, 300);
		bottomSlideLp.addRule(RelativeLayout.CENTER_IN_PARENT);
		mBottomSlideLight.setLayoutParams(bottomSlideLp);
		mBottomSlideParent.addView(mBottomSlideLight);
		musicTypeNow = new String[] { "FUNK", "ROCK", "JAZZ" };
		Utils.centerMusicSlideInitial();
		final Stack<Float> mStack = new Stack<Float>();
		mBottomSlideParent.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View arg0, MotionEvent event) {
				float x;
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					x = event.getX();
					mBottomSlideLight.setLayoutParams(bottomSlideLp);
					mBottomSlideLight.setX(x - 150);
					mBottomSlideLight.setVisibility(View.VISIBLE);
					musicEqualizerFunk.setVisibility(View.VISIBLE);
					musicEqualizerJazz.setVisibility(View.VISIBLE);
					break;
				case MotionEvent.ACTION_MOVE:
					mBottomSlideParent.removeView(mBottomSlideLight);
					x = event.getX();
					Log.e("test", String.valueOf(x));
					if (Utils.isBottomMusicSlideInField(x)) {
						mBottomSlideParent.addView(mBottomSlideLight);
						mBottomSlideLight.setLayoutParams(bottomSlideLp);
						mBottomSlideLight.setX(x - 150);
						musicEqualizerRock.setTextSize(40);
						int a = Utils.sildeUtil(mStack, x);
						if (a == 1) {
							musicTypeNow = Utils.centerMusicSlide(1,
									musicTypeNow);
							musicEqualizerFunk.setText(musicTypeNow[0]);
							musicEqualizerJazz.setText(musicTypeNow[1]);
							musicEqualizerRock.setText(musicTypeNow[2]);
						}
						if (a == 0) {
							musicTypeNow = Utils.centerMusicSlide(0,
									musicTypeNow);
							musicEqualizerFunk.setText(musicTypeNow[0]);
							musicEqualizerJazz.setText(musicTypeNow[1]);
							musicEqualizerRock.setText(musicTypeNow[2]);
						}
					}
					break;
				case MotionEvent.ACTION_UP:
					mBottomSlideLight.setVisibility(View.INVISIBLE);
					mBottomSlideParent.removeView(mBottomSlideLight);
					musicEqualizerFunk.setVisibility(View.INVISIBLE);
					musicEqualizerJazz.setVisibility(View.INVISIBLE);
					musicEqualizerRock.setTextSize(27);
					break;
				default:
					break;
				}

				return true;
			}
		});
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.music_pause:
			if(RadioActivity.radioMediaPlayer!=null){
				RadioActivity.radioMediaPlayer.release();
			}
			//RadioActivity.radioMediaPlayer.release();
			play();
			break;
		case R.id.musci_pre:
			if(RadioActivity.radioMediaPlayer!=null){
				RadioActivity.radioMediaPlayer.release();
			}
			//RadioActivity.radioMediaPlayer.release();
			playPre();
			musicPausePlayBtn
					.setBackgroundResource(R.drawable.music_pause_icon);
			break;
		case R.id.music_next:
			if(RadioActivity.radioMediaPlayer!=null){
				RadioActivity.radioMediaPlayer.release();
			}
			//RadioActivity.radioMediaPlayer.release();
			playNext();
			musicPausePlayBtn.setBackgroundResource(R.drawable.music_pause_icon);
			break;
			
		case R.id.music_voice_max:
			musicVoiceTmp = Integer.valueOf(musicVoice.getText().toString());
			musicVoiceTmp++;
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_RAISE, 0);
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_RAISE, 0);
			if (musicVoiceTmp > systemVoiceMax) {
				musicVoice.setText(String.valueOf(systemVoiceMax));
				musicVoiceTmp = 15;

			} else {
				musicVoice.setText(String.valueOf(musicVoiceTmp));
			}
			MusicVoiceTask musicVoiceTask= new MusicVoiceTask();
			musicVoiceTask.execute(String.valueOf(musicVoiceTmp));
			break;
			
		case R.id.music_voice_min:
			musicVoiceTmp = Integer.valueOf(musicVoice.getText().toString());
			musicVoiceTmp--;
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC,
					AudioManager.ADJUST_LOWER, 0);
			mAudioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM,
					AudioManager.ADJUST_LOWER, 0);
			if (musicVoiceTmp < 0) {
				musicVoice.setText("0");
				musicVoiceTmp = 0;
			} else {
				musicVoice.setText(String.valueOf(musicVoiceTmp));
			}
			MusicVoiceTask musicVoiceTask2= new MusicVoiceTask();
			musicVoiceTask2.execute(String.valueOf(musicVoiceTmp));
			break;
			
		case R.id.music_test:
			IstoPlay();
			break;

		}
		
		
			

		// TODO Auto-generated method stub

	}

	private void IstoPlay() {
		// TODO Auto-generated method stub
		
	}

	private void setMusicBg(){
		if(musicTitle.getText().toString().equals("°²¾²"));
		{
			musicPlayBg.setBackgroundResource(R.drawable.music_bg_anjin);
		}
		if(musicTitle.getText().toString().equals("²Ð¿áÔÂ¹â"));
		{
			musicPlayBg.setBackgroundResource(R.drawable.music_bg_canku);
		}
		if(musicTitle.getText().toString().equals("´«Ææ"));
		{
			musicPlayBg.setBackgroundResource(R.drawable.music_bg_chuanqi);
		}
		if(musicTitle.getText().toString().equals("ºÃ¾Ã²»¼û"));
		{
			musicPlayBg.setBackgroundResource(R.drawable.music_bg_haojiu);
		}
		if(musicTitle.getText().toString().equals("Áú¾í·ç"));
		{
			musicPlayBg.setBackgroundResource(R.drawable.music_bg_long);
		}
	}
	
	private void play() {
		// TODO Auto-generated method stub
		//Utils.currentMusicIndex = musicListPos;
		musicListPos = Utils.currentMusicIndex;
		Mp3Info mp3Info = mp3Infos.get(Utils.currentMusicIndex);
		Utils.currentMp3Info = mp3Info;
		musicTitle.setText(mp3Info.getTitle());
		//setMusicBg();
		musicArtist.setText(mp3Info.getArtist());
		musicAlbum.setText(mp3Info.getAlbum());
		Intent intent = new Intent();
		if (isTitleFinished) {
			mTitleTask = new MusicTitleTask();
			mTitleTask.execute(Utils.getMusicID(mp3Info.getTitle()));
		}
		if (Utils.isMusicPlaying == false) {
			musicPausePlayBtn
					.setBackgroundResource(R.drawable.music_pause_icon);
			intent.putExtra("url", mp3Info.getUrl());
			intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
			intent.setClass(getApplicationContext(), PlayerService.class);
			startService(intent);
			Utils.isMusicPlaying = true;
			// isPause = false;
			// isFirstTime = false;
		} else {
			if (Utils.isMusicPlaying) {
				musicPausePlayBtn
						.setBackgroundResource(R.drawable.music_play_icon);
				intent.putExtra("url", mp3Info.getUrl());
				intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
				intent.setClass(getApplicationContext(), PlayerService.class);
				startService(intent);
				Utils.isMusicPlaying = false;
				Utils.isMusicPause = true;
			} else if (Utils.isMusicPause = true) {
				musicPausePlayBtn
						.setBackgroundResource(R.drawable.music_play_icon);
				intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
				intent.setClass(getApplicationContext(), PlayerService.class);
				startService(intent);
				Utils.isMusicPause = false;
				Utils.isMusicPlaying = true;
			}
		}
	}

	private void playNext() {
		Utils.isMusicPlaying = true;
		Utils.currentMusicIndex = Utils.currentMusicIndex+1;
		musicListPos = Utils.currentMusicIndex;
		if (musicListPos > mp3Infos.size() - 1) {
			musicListPos = 0;
			Utils.currentMusicIndex = 0;
		}
		//Utils.currentMusicIndex = musicListPos;
		musicPausePlayBtn
		.setBackgroundResource(R.drawable.music_pause_icon);
		Mp3Info mp3Info = mp3Infos.get(musicListPos);
		Utils.currentMp3Info = mp3Info;
		musicTitle.setText(mp3Info.getTitle());
		musicArtist.setText(mp3Info.getArtist());
		musicAlbum.setText(mp3Info.getAlbum());
		if (isTitleFinished) {
			mTitleTask = new MusicTitleTask();
			mTitleTask.execute(Utils.getMusicID(mp3Info.getTitle()));
		}
		setMusicBg();

		Intent intent = new Intent();
		intent.putExtra("url", mp3Info.getUrl());
		intent.putExtra("MSG", AppConstant.PlayerMsg.NEXT_MSG);
		intent.setClass(getApplicationContext(), PlayerService.class);
		startService(intent);
	}

	private void playPre() {
		// TODO Auto-generated method stub
		Utils.isMusicPlaying = true;
		Utils.currentMusicIndex--;
		musicListPos = Utils.currentMusicIndex;
		//musicListPos = musicListPos - 1;
		if (musicListPos < 0) {
			musicListPos = mp3Infos.size() - 1;
			Utils.currentMusicIndex = musicListPos;
			
		}
		//Utils.currentMusicIndex = musicListPos;
		musicPausePlayBtn
		.setBackgroundResource(R.drawable.music_pause_icon);
		Mp3Info mp3Info = mp3Infos.get(musicListPos);
		Utils.currentMp3Info = mp3Info;
		musicTitle.setText(mp3Info.getTitle());
		musicArtist.setText(mp3Info.getArtist());
		musicAlbum.setText(mp3Info.getAlbum());
		if (isTitleFinished) {
			mTitleTask = new MusicTitleTask();
			mTitleTask.execute(Utils.getMusicID(mp3Info.getTitle()));
		}
		Intent intent = new Intent();
		intent.putExtra("url", mp3Info.getUrl());
		intent.putExtra("MSG", AppConstant.PlayerMsg.PRIVIOUS_MSG);
		intent.setClass(getApplicationContext(), PlayerService.class);
		startService(intent);
	}
		
	class MusicTitleTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isTitleFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setMusicUsing(true);
			MyApplication.serverStart();
		}
		
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String voice = arg0[0];
			try {
				Http.musicNowPost(voice);
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
			isTitleFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMusicUsing(false);
			MyApplication.serverStart();
		}
	}
	
	class MusicVoiceTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isVoiceFinished = false;
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
			isVoiceFinished = true;
			isMusicVolumeFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setMusicUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
	}
}
