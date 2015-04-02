package psa.music;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.IBinder;
import android.util.Log;
import app.psa.Utils;

@SuppressLint("NewApi")  
public class PlayerService extends Service {  
    public static MediaPlayer mediaPlayer;       //Ã½Ìå²¥·ÅÆ÷¶ÔÏó  
    private String path;  
    private int msg;//ÒôÀÖÎÄ¼þÂ·¾¶  
    private boolean isPause;                    //ÔÝÍ£×´Ì¬  
    private int current = 0;
    private List<Mp3Info> mp3Infos;
    private int duration;
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate(){
		super.onCreate();
		initial();	
	}
	
	private void initial(){
		mediaPlayer = new MediaPlayer();
		mp3Infos = MediaUtil.getMp3Infos(PlayerService.this);
		Log.e("PlayerService", "onCreate");
		
		mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
			
			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				current++;
				if(current>mp3Infos.size()-1){
					current = 0;
				}
				path = mp3Infos.get(current).getUrl();
				play(0);
					
			}
		});
	
	}
	@Override
	public void onStart(Intent intent, int startId) {
		Log.e("PlayerService", "onStart");
		if(intent!=null){
		path = intent.getStringExtra("url");		//歌曲路径
		current = intent.getIntExtra("listPosition", -1);	//当前播放歌曲的在mp3Infos的位置
		msg = intent.getIntExtra("MSG", 0);			//播放信息
		if (msg == AppConstant.PlayerMsg.PLAY_MSG) {	//直接播放音乐
			play(0);
		} else if (msg == AppConstant.PlayerMsg.PAUSE_MSG) {	//暂停
			pause();	
		} else if (msg == AppConstant.PlayerMsg.STOP_MSG) {		//停止
			stop();
		} else if (msg == AppConstant.PlayerMsg.CONTINUE_MSG) {	//继续播放
			resume();	
		} else if (msg == AppConstant.PlayerMsg.PRIVIOUS_MSG) {	//上一首
			previous();
		} else if (msg == AppConstant.PlayerMsg.NEXT_MSG) {		//下一首
			next();
		} 
		}
		super.onStart(intent, startId);
	}

	private void stop() {
		if (mediaPlayer != null) {
			mediaPlayer.stop();
			try {
				mediaPlayer.prepare(); // 在调用stop后如果需要再次通过start进行播放,需要之前调用prepare函数
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		// TODO Auto-generated method stub
		
	}

	private void next() {
		//Intent sendIntent = new Intent(UPDATE_ACTION);
		//sendIntent.putExtra("current", current);
		// 发送广播，将被Activity组件中的BroadcastReceiver接收到
		//sendBroadcast(sendIntent);
		play(0);
	}
		// TODO Auto-generated method stub
		
	

	private void previous() {
		// TODO Auto-generated method stub
		play(0);
	}

	private void resume() {
		// TODO Auto-generated method stub
		if (isPause) {
			mediaPlayer.start();
			isPause = false;
		}
	}

	private void pause() {
		// TODO Auto-generated method stub
		if (Utils.isMusicRelease) {
			initial();
			Utils.isMusicRelease = false;
		}
		if (mediaPlayer != null && mediaPlayer.isPlaying()) {
		  mediaPlayer.pause();
			isPause = true;
		}
		
	}
	
	

	private void play(int currentTime) {
		try {
			
			mediaPlayer.reset();// 把各项参数恢复到初始状态
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare(); // 进行缓冲
			mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));// 注册一个监听器
			//handler.sendEmptyMessage(1);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
		// TODO Auto-generated method stub
		
	private final class PreparedListener implements OnPreparedListener {
		private int currentTime;

		public PreparedListener(int currentTime) {
			this.currentTime = currentTime;
		}

		@Override
		public void onPrepared(MediaPlayer mp) {
			//if(mediaPlayer!=null){
			mediaPlayer.start(); 
			//}// 开始播放
			if (currentTime > 0) { // 如果音乐不是从头播放
				mediaPlayer.seekTo(currentTime);
			}
			Intent intent = new Intent();
			//intent.setAction(MUSIC_DURATION);
			duration = mediaPlayer.getDuration();
			intent.putExtra("duration", duration);	//通过Intent来传递歌曲的总长度
			sendBroadcast(intent);
		}
	}
	}
	
	
	
      
