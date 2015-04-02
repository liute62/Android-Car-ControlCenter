package psa.navi;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.MyBroadcast;
import app.psa.R;
import app.psa.Utils;

public class POIActivity extends Activity implements OnClickListener{

	private TextView POIText;
	private ListView poiSearchlist;
	private Button poiFinishBtn;
	private MyBroadcast closeBroad;
	int index = -1;
	
	/**
	 * 为界面注册关闭界面广播
	 */
	public void registerCloseAppBroad()
	{
		closeBroad = new MyBroadcast(this);
		IntentFilter filter = new IntentFilter(MyBroadcast.CLOSE_ACTION);
		registerReceiver(closeBroad, filter);//注册关闭界面广播
	}

	
	
	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) 
		{
			if (msg.what == Utils.navi_poi_action) {
				NaviTestvoice();
				Intent mIntent = new Intent(POIActivity.this,MainActivity.class);
				mIntent.putExtra("TURN_TO_TURN", true);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				startActivity(mIntent);
			}
			if (msg.what == Utils.main_reset) {
				Utils.reset(POIActivity.this);
				
			}
		};
	};
	
	
	private MediaPlayer naviTestMediaPlayer;
	public void NaviTestvoice(){
		naviTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.navistart); 
	    if(naviTestMediaPlayer!=null){
	    	naviTestMediaPlayer.start();
	    	
	    }
	}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_poi_content);
		initial();
		MyApplication.serverStop();
		MyApplication.getInstance().setPoiHandler(mHandler);
		MyApplication.serverStart();
		index = getIntent().getIntExtra("POI_INDEX", -1);
		poiSearchlist.setAdapter(new ArrayAdapter<String>(this, R.layout.item_poi_select, R.id.poi_search_list_text, getData(index)));
        
		poiSearchlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				//开始poi导航
				Intent mIntent = new Intent(POIActivity.this,MainActivity.class);
				mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
				mIntent.putExtra("TURN_TO_TURN", true);
				startActivity(mIntent);
				
			}
		});
		
		
}

	private void initial() {
		// TODO Auto-generated method stub
		poiFinishBtn = (Button)this.findViewById(R.id.poi_middle_icon);
		poiFinishBtn.setOnClickListener(this);
		POIText = (TextView)this.findViewById(R.id.navi_poi_word);
		poiSearchlist = new ListView(this);
		poiSearchlist = (ListView)this.findViewById(R.id.poi_search_result_list);
		//poiSearchlist.setAdapter(new ArrayAdapter<String>(this, R.layout.item_poi_select, R.id.poi_search_list_text, getData(index)));
	}
	
	
	private List<String> getData(int index){
        
        List<String> data = new ArrayList<String>();
        //餐厅
        if (index == 1) {
			data.add("1 唐朝            123m");
			data.add("2 金钱豹酒店 2.1km");
			data.add("3 和平饭店     2.4km");
			data.add("4 红辣椒         3.6km");
		}
        if (index == 2) {
			
        	data.add("1 海宁加油站 331m");
        	data.add("2 大田加油站 1.5km");
        	data.add("3 中田加油站 2.4km");
        	data.add("4 新炼加油站 2.6km");
        }
    	return data;
    }
	

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.poi_middle_icon:
			this.finish();
			break;

		default:
			break;
		}
	}
	
	boolean isPOIFinished = true;
	class POITask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isPOIFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setPOIUsing(true);
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
			isPOIFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setPOIUsing(false);
			MyApplication.serverStart();
		}
		
	}

}
