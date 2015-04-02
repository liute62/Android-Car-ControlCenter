package psa.navi;

import java.util.ArrayList;
import java.util.List;

import psa.phone.PhoneActivity;
import psa.phone.PhoneDialPanelActivity;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import app.psa.MyApplication;
import app.psa.R;

public class NaviActivity extends Activity implements OnClickListener{
	
	private Typeface tfNaviExtraLight;
	private TextView naviNowLocation;
	private TextView naviNowLocationTitle;
	
	private Button naviSearchBtn;
	//private ViewPager naviPager;
	//private List<View> listViews;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_navi);
		overridePendingTransition(R.anim.left_in,R.anim.left_out);
		
		initial();
		//InitViewPager();
	}

	private void initial() {
		// TODO Auto-generated method stub
		tfNaviExtraLight = Typeface.createFromAsset(getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");
		naviNowLocation = (TextView)this.findViewById(R.id.local_title);
		naviNowLocationTitle = (TextView)this.findViewById(R.id.local_now);
		naviNowLocation.setTypeface(tfNaviExtraLight);
		naviNowLocationTitle.setTypeface(tfNaviExtraLight);
		naviSearchBtn = (Button)this.findViewById(R.id.navi_search);
		naviSearchBtn.setOnClickListener(this);
	}
	
	/*private void InitViewPager(){
		naviPager = (ViewPager)this.findViewById(R.id.navi_view);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.activity_navi, null));
		listViews.add(mInflater.inflate(R.layout.view_navi_normal, null));
		naviPager.setAdapter(new MyNaviPagerAdapter(listViews));

	}*/

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.navi_search:
			Intent mIntent = new Intent(NaviActivity.this,NaviSearchActivity.class);
			startActivity(mIntent);
			break;
			
		default:
			break;
		}
	}
	
	boolean isNaviFinished = true;
	class NaviTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			isNaviFinished = false;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviUsing(true);
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
			isNaviFinished = true;
			MyApplication.serverStop();
			MyApplication.getInstance().setNaviUsing(false);
			MyApplication.serverStart();
		}
		
	}
	
}
