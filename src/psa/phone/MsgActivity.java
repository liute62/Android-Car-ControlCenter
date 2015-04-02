package psa.phone;

import java.util.ArrayList;
import java.util.List;

import psa.navi.KeyboardUtil;
import psa.navi.NaviActivity;
import psa.navi.NaviRouteActivity;
import psa.navi.NaviSearchActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import app.psa.MyApplication;
import app.psa.R;
import app.psa.Utils;

public class MsgActivity extends Activity implements OnClickListener{
	private EditText msgEditText;
	private Button msgBackBtn;
	private TextView msgTitleText;
	private Button msgSendBtn;
	private Context ctx;
	private Activity act;
	private Typeface tfMsgFont;
	private ListView msgList;
	private TextView msgText;
	private String msgSendSucces;
	private RelativeLayout msgSendLayout;
	private MediaPlayer msgConfirmTestMediaPlayer;
	private MediaPlayer msgSendSuccTestMediaPlayer;
	
	
	
	Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			if (msg.what == Utils.msg_input) {
				//编辑
				IsSendMsg();
				msgEditText.setText(msgList.getItemAtPosition(0).toString());
				Intent mIntent = new Intent(MsgActivity.this,MsgConfirmActivity.class);
				startActivity(mIntent);	
			}
			
			if (msg.what == Utils.msg_send) {
				//进行发送操作
				sendMsgSuccess();
				Intent mIntent = new Intent(MsgActivity.this,MsgConfirmActivity.class);
				startActivity(mIntent);	
				//MsgActivity.this.finish();
			}
		}
	};
	
	public void onCreate(Bundle savedInstanceState) {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_msg);
			Intent intent1 = getIntent();
			MyApplication.serverStop();
			MyApplication.getInstance().setMsgHandler(mHandler);
			MyApplication.serverStart();
			msgSendSucces = intent1.getStringExtra("sendSuccess");
			ctx = this;
			act = this;
			initial();
			
		if (msgSendSucces != null) {

			if (msgSendSucces.equals("sendSuccess")) {
				msgSendLayout.setVisibility(View.VISIBLE);
			}
		}
			msgList.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
						long arg3) {
					// TODO Auto-generated method stub
					msgEditText.setText(msgList.getItemAtPosition(pos).toString());
				}
			});
			msgEditText.setInputType(InputType.TYPE_NULL);
			new KeyboardUtil(act, ctx, msgEditText).showKeyboard();
		}

		private void initial() {
			// TODO Auto-generated method stub
			tfMsgFont = Typeface.createFromAsset(getAssets(),
					"fonts/SourceHanSansCN-ExtraLight.ttf");
			msgSendBtn = (Button)this.findViewById(R.id.msg_send_btn);
			msgSendBtn.setOnClickListener(this);
			msgBackBtn = (Button)this.findViewById(R.id.msg_middle_icon);
			msgBackBtn.setOnClickListener(this);
			msgEditText = (EditText)this.findViewById(R.id.msg_edit_text);
			msgEditText.setTypeface(tfMsgFont);
			msgTitleText = (TextView)this.findViewById(R.id.msg_title);
			msgTitleText.setTypeface(tfMsgFont);
			msgSendLayout = (RelativeLayout)this.findViewById(R.id.msg_send_ok_bg);
			msgList = new ListView(this);
			msgList = (ListView)this.findViewById(R.id.msg_common_list);
			msgList.setAdapter(new ArrayAdapter<String>(this, R.layout.item_list_msg_list, R.id.msg_list_text, getData()));
			
		}

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			switch (v.getId()) {
			case R.id.msg_send_btn:
				IsSendMsg();
				
				Intent mIntent = new Intent(MsgActivity.this,MsgConfirmActivity.class);
				startActivity(mIntent);
				break;
				
			case R.id.msg_middle_icon:
				this.finish();
				break;

			default:
				break;
			}
		}
		 private void IsSendMsg() {
			// TODO Auto-generated method stub
			 msgConfirmTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.confirm_send); 
			    if(msgConfirmTestMediaPlayer!=null){
			    	msgConfirmTestMediaPlayer.start();
			    	
			    }
		}
		 
		

		private List<String> getData(){
	         
		        List<String> data = new ArrayList<String>();
		        data.add("我正在开车，稍后回电");
		        data.add("我马上到");
		        data.add("您好我现在有事");
		       
		        return data;
		    }
		
		private void sendMsgSuccess() {
			// TODO Auto-generated method stub
			msgSendSuccTestMediaPlayer = MediaPlayer.create(getBaseContext(), R.raw.send_success); 
		    if(msgSendSuccTestMediaPlayer!=null){
		    	msgSendSuccTestMediaPlayer.start();
		    	
		    }
		}
		
		private boolean isMsgFinished = true;
		class MsgTask extends AsyncTask<String,Integer,Integer>{

			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
				isMsgFinished = false;
				MyApplication.serverStop();
				MyApplication.getInstance().setMsgUsing(true);
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
				isMsgFinished = true;
				MyApplication.serverStop();
				MyApplication.getInstance().setMsgUsing(false);
				MyApplication.serverStart();
			}
			
		}
		
		@Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			System.exit(0);
		}
	}



