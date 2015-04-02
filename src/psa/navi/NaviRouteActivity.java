package psa.navi;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class NaviRouteActivity extends Activity implements OnClickListener{
	
	private Button shortRouteBtn;
	private Button fastRouteBtn;
	private Button avoidtRouteBtn;
	private Typeface tfNaviSearch;
	private TextView textShort;
	private TextView textFast;
	private TextView textAvoid;
	private Button naviRouteBtn;
	RelativeLayout mNaviPic;
	TextView mNaviTitle;
	int fastSelectedIndex = 0;
	int avoideSelectedIndex = 0;
	int shortSelectedIndex = 0;
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.navi_action) {
				if (Utils.currentScene == 3) {
					
				}
				Intent mIntent = new Intent(NaviRouteActivity.this,MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
				//mIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); 新实例
				//mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY); 新实例
				//mIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); 新实例
				//mIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
				//mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
				//mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				mIntent.putExtra("TURN_TO_TURN", true);
				startActivity(mIntent);
				NaviRouteActivity.this.finish();
			}
		}
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_navi_route);
		sceneHouse_1();
		MyApplication.serverStop();
		MyApplication.getInstance().setNaviRouteHandler(mHandler);
		MyApplication.serverStart();
		initial();
	}
	
	MediaPlayer scenePlayer0;
	public void sceneHouse_1(){
		scenePlayer0 = MediaPlayer.create(getBaseContext(), R.raw.select_route); 
	    if(scenePlayer0!=null){
	    	scenePlayer0.start();
	    }
	}

	private void initial() {
		// TODO Auto-generated method stub
		tfNaviSearch = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		mNaviTitle = (TextView)this.findViewById(R.id.navi_route_title);
		mNaviPic = (RelativeLayout)this.findViewById(R.id.naviroute_content);
		mNaviPic.setVisibility(View.INVISIBLE);
		shortRouteBtn = (Button)this.findViewById(R.id.short_route_btn);
		shortRouteBtn.setOnClickListener(this);
		fastRouteBtn = (Button)this.findViewById(R.id.fast_route_btn);
		fastRouteBtn.setOnClickListener(this);
		
		avoidtRouteBtn = (Button)this.findViewById(R.id.avoid_route_btn);
		avoidtRouteBtn.setOnClickListener(this);
		
		naviRouteBtn = (Button)this.findViewById(R.id.navi_route_finish_btn);
		naviRouteBtn.setOnClickListener(this);
		
		//textShort = (TextView)this.findViewById(R.id.navi_short_text);
		//textFast = (TextView)this.findViewById(R.id.navi_fast_text);
		//textAvoid = (TextView)this.findViewById(R.id.navi_avoid_text);
		//textShort.setTypeface(tfNaviSearch);
		//textFast.setTypeface(tfNaviSearch);
		//textAvoid.setTypeface(tfNaviSearch);
		shortSelectedIndex++;
		fastSelectedIndex = 0;
		avoideSelectedIndex = 0;
		mNaviPic.setVisibility(View.VISIBLE);
		shortRouteBtn.setBackground(getResources().getDrawable(R.drawable.short_highlight));
		fastRouteBtn.setBackground(getResources().getDrawable(R.drawable.fast_normal));
		avoidtRouteBtn.setBackground(getResources().getDrawable(R.drawable.avoid_normal));
		if (Utils.currentScene == 2) {
			mNaviPic.setBackground(getResources().getDrawable(R.drawable.s2_1_1));
			mNaviTitle.setText("同济大学嘉定校区");
		}if (Utils.currentScene == 3) {
			mNaviPic.setBackground(getResources().getDrawable(R.drawable.s3_1_1));
			mNaviTitle.setText("苏州博物馆");
		}
	}

	//mIntent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME); 新实例
	//mIntent.setFlags(Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY); 新实例
	//mIntent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT); 新实例
	//mIntent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
	//mIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
	//mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.short_route_btn:
			shortSelectedIndex++;
			if (shortSelectedIndex == 1) {
				fastSelectedIndex = 0;
				avoideSelectedIndex = 0;
				mNaviPic.setVisibility(View.VISIBLE);
				shortRouteBtn.setBackground(getResources().getDrawable(R.drawable.short_highlight));
				fastRouteBtn.setBackground(getResources().getDrawable(R.drawable.fast_normal));
				avoidtRouteBtn.setBackground(getResources().getDrawable(R.drawable.avoid_normal));
				if (Utils.currentScene == 2) {
					mNaviPic.setBackground(getResources().getDrawable(R.drawable.s2_1_1));
					mNaviTitle.setText("同济大学嘉定校区");
				}if (Utils.currentScene == 3) {
					mNaviPic.setBackground(getResources().getDrawable(R.drawable.s3_1_1));
					mNaviTitle.setText("苏州博物馆");
				}
				
			}
			if (shortSelectedIndex == 2) {
				avoideSelectedIndex = 0;
				fastSelectedIndex = 0;
				shortSelectedIndex = 0;
				Intent mIntent = new Intent(NaviRouteActivity.this,MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
				mIntent.putExtra("TURN_TO_TURN", true);
				startActivity(mIntent);
				this.finish();	
			}
			break;
		case R.id.fast_route_btn:
			fastSelectedIndex++;
			if (fastSelectedIndex == 1) {
				mNaviPic.setVisibility(View.VISIBLE);
				shortSelectedIndex = 0;
				avoideSelectedIndex = 0;
				shortRouteBtn.setBackground(getResources().getDrawable(R.drawable.short_normal));
				fastRouteBtn.setBackground(getResources().getDrawable(R.drawable.fast_highlight));
				avoidtRouteBtn.setBackground(getResources().getDrawable(R.drawable.avoid_normal));
				if (Utils.currentScene == 2) {
					mNaviPic.setBackground(getResources().getDrawable(R.drawable.s2_2_2));
					mNaviTitle.setText("同济大学嘉定校区");
				}if (Utils.currentScene == 3) {
					mNaviPic.setBackground(getResources().getDrawable(R.drawable.s3_2_2));
					mNaviTitle.setText("苏州博物馆");
				}
			}if (fastSelectedIndex == 2) {
				avoideSelectedIndex = 0;
				fastSelectedIndex = 0;
				shortSelectedIndex = 0;
				Intent mIntent = new Intent(NaviRouteActivity.this,MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
				startActivity(mIntent);
				this.finish();	
				
			}
			break;
		case R.id.avoid_route_btn:
			avoideSelectedIndex++;
			if (avoideSelectedIndex == 1) {
				mNaviPic.setVisibility(View.VISIBLE);
				fastSelectedIndex = 0;
				shortSelectedIndex = 0;
				shortRouteBtn.setBackground(getResources().getDrawable(R.drawable.short_normal));
				fastRouteBtn.setBackground(getResources().getDrawable(R.drawable.fast_normal));
				avoidtRouteBtn.setBackground(getResources().getDrawable(R.drawable.avoid_highlight));
				if (Utils.currentScene == 2) {
					mNaviPic.setBackground(getResources().getDrawable(R.drawable.s2_3_3));
					mNaviTitle.setText("同济大学嘉定校区");
				}if (Utils.currentScene == 3) {
					mNaviPic.setBackground(getResources().getDrawable(R.drawable.s3_3_3));
					mNaviTitle.setText("苏州博物馆");
				}
			}if(avoideSelectedIndex == 2){
				avoideSelectedIndex = 0;
				fastSelectedIndex = 0;
				shortSelectedIndex = 0;
				Intent mIntent = new Intent(NaviRouteActivity.this,MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
				startActivity(mIntent);
				this.finish();	
			
			}
			break;
		case R.id.navi_route_finish_btn:
			this.finish();
		default:
			break;
		}
		// TODO Auto-generated method stub
		
	}

	boolean isNaviRouteFinished = true;
	class NaviRoute extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isNaviRouteFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviRouteUsing(true);
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
			isNaviRouteFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviRouteUsing(false);
			MyApplication.serverStart();
		}
		
	}
}
