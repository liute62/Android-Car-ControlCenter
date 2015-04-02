package psa.navi;

import java.util.ArrayList;
import java.util.List;

import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;
import psa.navi.KeyboardUtil;
import app.psa.MainActivity;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class NaviSearchActivity extends Activity implements OnClickListener{
	private ListView naviSearchlist;
	private Typeface tfNaviList;
	private TextView naviSearch;
	private EditText edit;
	private Context ctx;
	private Activity act;
	private Button naviMiddleBtn;
	private Button homeBtn;
	private Button oilStationBtn;
	private Button resBtn;
	boolean result;
	private MediaPlayer restSelectPlayer;
	private MediaPlayer gasSelectPlayer;
	int index = -1;
	MyAdapter myAdapter;
	StarAdapter starAdapter;
	int beginNavi = -1;
	
	MediaPlayer scenePlayer4;
	public void sceneRest_3(){
		scenePlayer4 = MediaPlayer.create(getBaseContext(), R.raw.navi_s3_rest); 
	    if(scenePlayer4 != null){
	    	scenePlayer4.start();
	    }
	}
	
	MediaPlayer scenePlayer1;	
	public void sceneGas_2(){
		scenePlayer1 = MediaPlayer.create(getBaseContext(), R.raw.navi_s2_gas); 
	    if(scenePlayer1!=null){
	    	scenePlayer1.start();
	    }
	}
	
	
	
	Handler mHandler = new Handler(){
		
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.navi_action) {
				if (Utils.currentScene == 3) {
					Intent mIntent = new Intent(NaviSearchActivity.this,NaviRouteActivity.class);
					startActivity(mIntent);	
				}
			}
			if (msg.what == Utils.navi_select) {
				//VoiceSelect();
				
				Intent mIntent = new Intent(NaviSearchActivity.this,NaviRouteActivity.class);
				startActivity(mIntent);
			}if (msg.what == Utils.navi_poi_action) {
				if (Utils.currentScene == 2) {
					sceneGas_2();
					Intent mIntent = new Intent(NaviSearchActivity.this,MainActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
					mIntent.putExtra("TURN_TO_TURN", true);
					//mIntent.putExtra("is_poi", true);
					Utils.isPoi = true;
					startActivity(mIntent);
					NaviSearchActivity.this.finish();
				
				}if (Utils.currentScene == 3) {
					sceneRest_3();
					Intent mIntent = new Intent(NaviSearchActivity.this,MainActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
					mIntent.putExtra("TURN_TO_TURN", true);
					//mIntent.putExtra("is_poi", true);
					Utils.isPoi = true;
					startActivity(mIntent);
					NaviSearchActivity.this.finish();
				
				}
			}
		}
	};
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_navi_search);
		MyApplication.serverStop();
		MyApplication.getInstance().setNaviSearchHandler(mHandler);
		MyApplication.serverStart();
		ctx = this;
		act = this;
		initial();
		myAdapter = new MyAdapter(this);
		starAdapter = new StarAdapter(this);
		/**boolean isNaviList = getIntent().getBooleanExtra("isNaviList", false);
		naviSearchlist.setVisibility(View.INVISIBLE);
		if (isNaviList) {
			naviSearchlist.setVisibility(View.VISIBLE);
		}**/
		String poi_name = getIntent().getStringExtra("poi_name");
		if (poi_name != null && poi_name.equals("jiayouzhan")) {
			poiGasClicked();
		}if (poi_name != null && poi_name.equals("suzhoubowuguan")) {
			index = 3;
			myAdapter.setData(getData(index));
			naviSearchlist.setAdapter(myAdapter);
			naviSearchlist.setVisibility(View.VISIBLE);
			//YUYING
		}if (poi_name != null && poi_name.equals("canguan")) {
			resClicked();
			//YUYING
		}
		naviSearchlist.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(beginNavi == 1){
					Intent mIntent = new Intent(NaviSearchActivity.this,MainActivity.class);
					mIntent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT); //无传值
					mIntent.putExtra("TURN_TO_TURN", true);
					startActivity(mIntent);
					NaviSearchActivity.this.finish();
				}
				if(beginNavi == 0){
					Intent mIntent = new Intent(NaviSearchActivity.this,NaviRouteActivity.class);
					startActivity(mIntent);		
				}
			}
		});
		edit.setInputType(InputType.TYPE_NULL);
		new KeyboardUtil(act, ctx, edit).showKeyboard();
		
		TextWatcher watcher = new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
					int arg3) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				if (edit.getText().toString().equals("T")) {
					index = 0;
					
					myAdapter.setData(getData(index));
					naviSearchlist.setAdapter(myAdapter);
					naviSearchlist.setVisibility(View.VISIBLE);	
				}
				if (edit.getText().toString().equals("TJ")) {
					index = 1;
					myAdapter.setData(getData(index));
					naviSearchlist.setAdapter(myAdapter);
					naviSearchlist.setVisibility(View.VISIBLE);	
				}if (edit.getText().toString().equals("JIA")) {
					index = 2;
					myAdapter.setData(getData(index));
					naviSearchlist.setAdapter(myAdapter);
					naviSearchlist.setVisibility(View.VISIBLE);	
				}if (edit.getText().toString().equals("SZBWG")) {
					index = 3;
					myAdapter.setData(getData(index));
					naviSearchlist.setAdapter(myAdapter);
					naviSearchlist.setVisibility(View.VISIBLE);	
				}
			}
		};
		edit.addTextChangedListener(watcher);
	}

	/**protected void VoiceSelect() {
		// TODO Auto-generated method stub
		selectPlayer = MediaPlayer.create(getBaseContext(), R.raw.pleaseselect);  
		selectPlayer.start();
	}**/

	protected void GasVoiceSelect() {
		// TODO Auto-generated method stub
		gasSelectPlayer = MediaPlayer.create(getBaseContext(), R.raw.select_gas);  
		gasSelectPlayer.start();
	}
	
	protected void RestVoiceSelect() {
		// TODO Auto-generated method stub
		restSelectPlayer = MediaPlayer.create(getBaseContext(), R.raw.select_res);  
		restSelectPlayer.start();
	}
	
	private void initial() {
		// TODO Auto-generated method stub
		tfNaviList = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		//naviSearch = (TextView)(View)findViewById(R.id.navi_search_list_text);
		//naviSearch.setTypeface(tfNaviList);
		naviSearchlist = new ListView(this);
		naviSearchlist = (ListView)this.findViewById(R.id.navi_search_result_list);
		//naviSearchlist.setVisibility(View.INVISIBLE);
		edit = (EditText)this.findViewById(R.id.navi_search_word);
		naviMiddleBtn = (Button)this.findViewById(R.id.navi_middle_icon);
		naviMiddleBtn.setOnClickListener(this);
		oilStationBtn = (Button)this.findViewById(R.id.poi_oil);
		oilStationBtn.setOnClickListener(this);
		homeBtn = (Button)this.findViewById(R.id.poi_home);
		homeBtn.setOnClickListener(this);
		resBtn = (Button)this.findViewById(R.id.poi_restuarant);
		resBtn.setOnClickListener(this);
		//naviSearchlist.setVisibility(View.INVISIBLE);
	}

	 private List<String> getData(int index){
         
		 List<String> data = new ArrayList<String>();
		 if (index == 0) {
			data.add("1 同心路幼儿园");
			data.add("2 同进贸易有限公司");
			data.add("3 同泰楼面馆 ");
			data.add("4 同济大学 ");
		 }if (index == 1) {
			 data.add("1 同济大学 ");
		     data.add("2 同济大学 沪西校区");
		     data.add("3 同济大学 嘉定校区");
		     beginNavi = 0;
		 }if (index == 2) {
			data.add("1 家");
		 }if (index == 3) {
			data.add("1 苏州博物馆             34.3km");
			data.add("2 苏州博物馆停车场 34.5km");
			data.add("3 苏州博物馆保卫处 35.6km");
			data.add("4 苏州博物馆西门     35.7km");
			beginNavi = 0;
			
		 }if (index == 4) {
				data.add("1 望湘园      ");
				data.add("2 麻辣诱惑  ");
				data.add("3 小南国      ");
				data.add("4 呷哺呷哺  ");
				data.add("5 汉泰          ");
				beginNavi = 1;
			
		 }if (index == 5) {
			 data.add("1 海宁加油站                                331m");
	         data.add("2 大田加油站                                1.5km");
	         data.add("3 中田加油站                                2.4km");
	         data.add("4 新炼加油站                                2.6km");
	         beginNavi = 1;
	    
		}if (index == 6) {
			 data.add("1 家                                                18km");
			 beginNavi = 1;
		}
	        return data;
	    }
	 
	 @Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		result = getIntent().getBooleanExtra("TURN_TO_TURN", false);
		Log.e("onStart", String.valueOf(result));
		
	}
	 
	 @Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		result = getIntent().getBooleanExtra("TURN_TO_TURN", false);
		Log.e("onRestart", String.valueOf(result));
	}
	 
	 @Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		result = getIntent().getBooleanExtra("TURN_TO_TURN", false);
		Log.e("onResume", String.valueOf(result));
		
	}

	 private void poiGasClicked(){
		 	oilStationBtn.setBackground(getResources().getDrawable(R.drawable.gas_highlight));
			resBtn.setBackground(getResources().getDrawable(R.drawable.rest_normal));
			homeBtn.setBackground(getResources().getDrawable(R.drawable.home_normal));
			index = 5;
			//naviSearchlist.setAdapter(new ArrayAdapter<String>(NaviSearchActivity.this, R.layout.item_list_navisearch, R.id.navi_search_list_text, getData(index)));
			myAdapter.setData(getData(index));
			naviSearchlist.setAdapter(myAdapter);
			naviSearchlist.setVisibility(View.VISIBLE);	
			
	 }
	 
	private void resClicked(){
		oilStationBtn.setBackground(getResources().getDrawable(R.drawable.gas_normal));
		resBtn.setBackground(getResources().getDrawable(R.drawable.rest_hightlight));
		homeBtn.setBackground(getResources().getDrawable(R.drawable.home_normal));
		index = 4;
		starAdapter.setData(getData(index));
		naviSearchlist.setAdapter(starAdapter);
		naviSearchlist.setVisibility(View.VISIBLE);	
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.navi_middle_icon:
			this.finish();
			break;
		case R.id.poi_oil:
			poiGasClicked();
			/**Intent mIntent2 = new Intent(NaviSearchActivity.this,POIActivity.class);
			mIntent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mIntent2.putExtra("POI_INDEX",2);
			startActivity(mIntent2);**/
			break;
		case R.id.poi_restuarant:
			resClicked();
			/**Intent mIntent1 = new Intent(NaviSearchActivity.this,POIActivity.class);
			mIntent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			mIntent1.putExtra("POI_INDEX", 1);
			startActivity(mIntent1);**/
			break;
		case R.id.poi_home:
			oilStationBtn.setBackground(getResources().getDrawable(R.drawable.gas_normal));
			resBtn.setBackground(getResources().getDrawable(R.drawable.rest_normal));
			homeBtn.setBackground(getResources().getDrawable(R.drawable.home_highlight));
			index = 6;
			myAdapter.setData(getData(index));
			naviSearchlist.setAdapter(myAdapter);
			naviSearchlist.setVisibility(View.VISIBLE);	
			break;
		default:
			break;
		}
	}
	
	boolean isNaviSearchFinished = true;
	class PhoneSelectTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isNaviSearchFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviSearchUsing(true);
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
			isNaviSearchFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviSearchUsing(false);
			MyApplication.serverStart();
		}
		
	}
}
