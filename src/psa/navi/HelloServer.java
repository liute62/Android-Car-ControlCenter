package psa.navi;

import java.util.Map;

import android.R.integer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.MultiAutoCompleteTextView;
import app.psa.Utils;

import fi.iki.elonen.NanoHTTPD;
import fi.iki.elonen.ServerRunner;

/**
 * An example of subclassing NanoHTTPD to make a custom HTTP server.
 */
public class HelloServer extends NanoHTTPD {

	Handler mMainHandler;
	Handler mMusicHandler;
	Handler mNaviHandler;
	Handler mNaviRouteHandler;
	Handler mNaviSearchHandler;
	Handler mRadioHandler;
	Handler mContactHandler;
	Handler mMsgHandler;
	Handler mPhoneIncomingHandler;
	Handler mMsgConfirmHandler;
	Handler mPhoneDialSureHandler;
	Handler mPhoneCallingHandler;
	Handler mPhoneStrangeHandler;
	Handler mMsgIncomingHandler;
	Handler mMsgEditHandler;
	Handler mPoiHandler;
	/** Main **/
	private boolean isMainUsing = false;
	/** Music **/
	private boolean isMusicUsing = false;
	/** Navi **/
	private boolean isNaviUsing = false;
	private boolean isNaviRouteUsing = false;
	private boolean isNaviSearchUsing = false;
	private boolean isNaviTurnToTurnUsing = false;
	private boolean isPOIUsing = false;
	/** Radio **/
	private boolean isRadioUsing = false;
	private boolean isContactUsing = false;
	/** Phone **/
	private boolean isPhoneUsing = false;
	private boolean isPhoneCallingUsing = false;
	private boolean isPhoneDialPanelUsing = false;
	private boolean isPhoneDialSureUsing = false;
	private boolean isPhoneSelectUsing = false;
	private boolean isPhoneIncomingUsing = false;
	private boolean isPhoneStrangeUsing = false;
	/** Msg **/
	private boolean isMsgUsing = false;
	private boolean isMsgConfirmUsing = false;
	private boolean isMsgIncomingUsing = false;
	private boolean isMsgNewIncomingUsing = false;

	String serverName;

	public HelloServer(String name) {
		super(8080);
		serverName = name;
		// Log.e("HelloServer", serverName);
	}

	@Override
	public Response serve(IHTTPSession session) {
		String localMsg = "not find";
		Log.e("serve", serverName + "started");
		Method method = session.getMethod();
		String uri = session.getUri();
		// Log.e("server",method + " '" + uri + "' ");
		
		if (uri.equals("/config/scenario")) {
		
			Utils.currentScene = Integer.valueOf(session.getParms().get("scenario"));
		}
		if (uri.equals("/config/reset")) {
			String msg = "/config/reset no";
			Message message = new Message();
			Bundle bundle = new Bundle();
			message.what = Utils.main_reset;// 匹配handler
				if(mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/config/reset yes";
				}
		
			return new NanoHTTPD.Response(msg);
		}

/*****************************2.1 Main 部分*****************************/
		if (!isMainUsing) {
			if (uri.equals("/control/aircon/1/temperature")) {
				String msg = "/control/aircon/1/temperature no";
				Log.e("temperature", session.getParms().get("temperature"));
				Message message = new Message();
				Bundle bundle = new Bundle();
				message.what = Utils.main_air_num;// 匹配handler
				bundle.putString("temperature",
						session.getParms().get("temperature"));
				message.obj = bundle;// 传递的参数 我可以用bundle绑定
				if (mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/control/aircon/1/temperature yes";
				}
				return new NanoHTTPD.Response(msg);
			}
			// 空调开关
			if (uri.equals("/control/aircon/1/status")) {
				String msg = "/control/aircon/1/status no";
				Message message = new Message();
				message.what = Utils.main_air_status;
				Bundle bundle = new Bundle();
				bundle.putString("status", session.getParms().get("status"));
				message.obj = bundle;
				if (mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/control/aircon/1/status yes";
				}
				return new NanoHTTPD.Response(msg);
			}
			// 空调获取送风状态
			if (uri.equals("/control/aircon/1/flow")) {
				String msg = "/control/aircon/1/flow no";
				Message message = new Message();
				message.what = Utils.main_air_flow_status;
				message.obj = session.getParms().get("status");
				if (mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/control/aircon/1/flow yes";
				}
				return new NanoHTTPD.Response(msg);
			}
			// 空调：风速
			Log.e("uri", uri);
			if (uri.equals("/control/aircon/1/speed")) {
				String msg = "/control/aircon/1/speed no";
				Message message = new Message();
				message.what = Utils.main_air_flow_speed;
				message.obj = session.getParms().get("speed");
				Log.e("flow speed", session.getParms().get("speed"));
				if (mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/control/aircon/1/speed yes";
				}
				return new NanoHTTPD.Response(msg);
			}
		}

/*****************************2.2 Music 部分*****************************/
		if (!isMusicUsing) {

			//2.2.1 播放器：设置音量
			if (uri.equals("/control/audio/volume")) {
				String msg = "/control/audio/volume no";
				Message message = new Message();
				message.what = Utils.music_volume;
				message.obj = session.getParms().get("volume");
				Log.e("volume", session.getParms().get("volume"));
				if (Utils.currentView == 3 && mRadioHandler != null) {
					mRadioHandler.sendMessage(message);	
				}
				else {
					
				if (mMusicHandler != null) {
						mMusicHandler.sendMessage(message);
						Log.e(">", "yes");
						msg = "/control/audio/volume yes";
					}	
				}
				return new NanoHTTPD.Response(msg);
			}

			//2.2.3 音乐: 控制
			if (uri.equals("/control/music/control")) {
				String msg = "/control/music/control no";
				Message message = new Message();
				message.what = Utils.music_action;
				/**
				 * Bundle bundle = new Bundle(); bundle.putString("play",
				 * session.getParms().get("play")); bundle.putString("pause",
				 * session.getParms().get("pause")); bundle.putString("next",
				 * session.getParms().get("next")); bundle.putString("previous",
				 * session.getParms().get("previous"));
				 **/
				message.obj = session.getParms().get("action");
				if (Utils.currentView != 1) {
					if (Utils.isMusicPlaying == false && mMainHandler != null) {
						Message message2 = new Message();
						message2.what = Utils.trasfer_music;
						mMainHandler.sendMessage(message2);
						Utils.currentView = 1;
					}
				}else {
					if (mMusicHandler != null) {
						mMusicHandler.sendMessage(message);
						msg = "/control/music/control yes";
					}	
				}
				return new NanoHTTPD.Response(msg);
			}
			// 2.2.4 音乐: 搜索歌曲
			if (uri.equals("/control/music/search")) {
				String msg = "/control/music/search no";
				Message message = new Message();
				message.what = Utils.music_search;
				message.obj = session.getParms().get("key");
				if (mMusicHandler != null) {
					mMusicHandler.sendMessage(message);
					msg = "/control/music/search yes";
				}
				return new NanoHTTPD.Response(msg);
			}
		}
		
/*****************************2.2 Radio 部分*****************************/
		if (!isRadioUsing) {

			// 2.2.6 收音机：控制
			if (uri.equals("/control/radio/control")) {
				String msg = "/control/radio/control no";
				Message message = new Message();
				message.what = Utils.radio_control;
				Bundle bundle = new Bundle();
				Log.e("bundle", "yes");
				bundle.putString("action", session.getParms().get("action"));
				bundle.putString("radioType",
						session.getParms().get("radioType"));
				message.obj = bundle;
				if (mMainHandler!= null && mRadioHandler == null) {
					Message message2 = new Message();
					message2.what = Utils.transfer_radio;
					mMainHandler.sendMessage(message2);
				}
				if (mRadioHandler != null) {
					mRadioHandler.sendMessage(message);
					msg = "/control/radio/control yes";
				}
				return new NanoHTTPD.Response(msg);
			}
			
			//2.2.8 收音机：搜索预存频道
			if (uri.equals("/control/radio/search")) {
				String msg = "/control/radio/search no";
				if (Utils.currentView == 3) {
					Message message = new Message();
					message.what = Utils.radio_search;
					if (mRadioHandler != null) {
						mRadioHandler.sendMessage(message);
						msg = "/control/radio/search yes";
					}	
				}else {
					Utils.currentView = 3;
					Message message2 = new Message();
					message2.what = Utils.transfer_radio;
					mMainHandler.sendMessage(message2);
					msg = "/control/radio/search yes";
				}
				return new NanoHTTPD.Response(msg);
			}
		}
		
/*****************************2.3 Contact 部分*****************************/
		if (! isContactUsing) {
			//2.3.2 通讯录：搜索通讯录列表：cc收到后会发出声音：确认拨打给xxx
			if (uri.equals("/control/contacts/search")) {
				String msg = "/control/contacts/search no";
				Message message = new Message();
				message.what = Utils.contact_search;
				String result = session.getParms().get("action");
				message.obj = result;
				if (mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/control/contacts/search yes";
				}
				return new NanoHTTPD.Response(msg);
				
			}

			//2.3.2 通讯录：操作：上下选定
			if (uri.equals("/control/contacts/action")) {
				// int 0 上 1 下 2 选定
				String msg = "/control/contacts/action no";
				Message message = new Message();
				message.what = Utils.contact_search;
				String result = session.getParms().get("action");
				message.obj = result;
				if (mMainHandler != null) {
					mMainHandler.sendMessage(message);
					msg = "/control/contacts/action yes";
				}
				return new NanoHTTPD.Response(msg);
			}	
		}
		
/*****************************2.4 Phone 部分*****************************/
	
		//2.4.1 电话：模拟呼入响铃
		if (uri.equals("/control/telephone/callin")) {
			String msg = "/control/telephone/callin no";
			Message message = new Message();
			message.what = Utils.main_phone_calling;
			if (mMainHandler != null) {
				mMainHandler.sendMessage(message);
				msg = "/control/telephone/callin yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		// 2.4.2 电话：呼出
		
		if (uri.equals("/control/telephone/callout")) {
			String msg = "/control/telephone/callout no";
			Message message = new Message();
			message.what = Utils.phone_callout;
			Bundle bundle = new Bundle();
			String name  = session.getParms().get("name");
			String number  = session.getParms().get("number");
			bundle.putString("name",name);
			bundle.putString("number",number);
			bundle.putBoolean("zhang", true);
			message.obj = bundle;
			if (Utils.strangeOrDial == 0 && mPhoneStrangeHandler != null) {
				mPhoneStrangeHandler.sendMessage(message);
				msg = "/control/telephone/answer yes1";
			}
			if (Utils.strangeOrDial == 1 && mPhoneDialSureHandler != null) {
				mPhoneDialSureHandler.sendMessage(message);
				msg = "/control/telephone/callout yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		//2.4.3 电话：接听/挂机
		if (uri.equals("/control/telephone/answer")) {
			String msg = "/control/telephone/answer no";
			Message message = new Message();
			message.what = Utils.phone_answer;
			String answer = session.getParms().get("answer");
			Log.e("answer", answer);
			message.obj = answer;
			if (Utils.isWangbing == true) {
				Utils.isWangbing = false;
				mMainHandler.sendMessage(message);
			}else {
				
			if (Utils.comingOrCalling == 0 && mPhoneIncomingHandler != null) {
				mPhoneIncomingHandler.sendMessage(message);
				msg = "/control/telephone/answer yes2";
			}if (Utils.comingOrCalling == 1 && mPhoneCallingHandler != null) {
				mPhoneCallingHandler.sendMessage(message);
				msg = "/control/telephone/answer yes2";
			}
			}
			return new NanoHTTPD.Response(msg);
		}
		//2.4.5 电话：拨号
		if (uri.equals("/control/telephone/input")) {
			String msg = "/control/telephone/input no";
			Message message = new Message();
			message.what = Utils.phone_input;
			String number = session.getParms().get("number");
			message.obj = number;
			if (mMainHandler != null) {
				mMainHandler.sendMessage(message);
				msg = "/control/telephone/input yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
/*****************************2.5 Message 部分*****************************/	
		//2.5.1 消息：模拟收到消息
		if (uri.equals("/control/message/receive")) {
			String msg = "/control/message/receive no";
			Message message = new Message();
			String name = session.getParms().get("name");
			String number = session.getParms().get("number");
			String msgString = session.getParms().get("message");
			message.what = Utils.main_phone_msg;
			if (mMainHandler != null) {
				mMainHandler.sendMessage(message);
				msg = "/control/message/receive yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		//2.5.2 消息：查看消息
		if (uri.equals("/control/message/check")) {
			String msg = "/control/message/check no";
			Message message = new Message();
			message.what = Utils.msg_check;
			if (mMsgIncomingHandler != null) {
				mMsgIncomingHandler.sendMessage(message);
				msg = "/control/message/check yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		//2.5.3 消息：发送消息
		if (uri.equals("/control/message/send")) {
			String msg = "/control/message/send no";
			Message message = new Message();
			Bundle bundle = new Bundle();
			bundle.putString("name", session.getParms().get("name"));
			bundle.putString("number",session.getParms().get("number"));
			bundle.putString("message",session.getParms().get("message"));
			message.what = Utils.msg_send;
			message.obj = bundle;
			if(mMsgConfirmHandler != null){
				mMsgConfirmHandler.sendMessage(message);
				msg = "/control/message/send yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		//2.5.4 消息：开始编辑，通知中控语音提示
		if (uri.equals("/control/message/edit")) {
			String msg = "/control/message/edit no";
			Message message = new Message();
			message.what = Utils.msg_edit;
			if (mMsgEditHandler != null) {
				mMsgEditHandler.sendMessage(message);
				msg = "/control/message/edit yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		//2.5.5 消息：输入消息内容
		if (uri.equals("/control/message/input")) {
			String msg = "/control/message/input no";
			Message message = new Message();
			message.what = Utils.msg_input;
			String number = session.getParms().get("number");
			message.obj = number;
			if (mMsgHandler != null) {
				mMsgHandler.sendMessage(message);
				msg = "/control/message/input yes";
			}
			return new NanoHTTPD.Response(msg);
		
		//2.5.6 消息：忽略收到的消息
		}if (uri.equals("/control/message/ignore")) {
			Message message = new Message();
			message.what = Utils.msg_ignore;
			if (mMsgIncomingHandler != null) {
				mMsgIncomingHandler.sendMessage(message);
				
			}
		}
		

		
/*****************************2.6 Navi 部分*****************************/		
		//2.6.1 POI地址：显示POI地点搜索列表
		if (uri.equals("/control/poi/search")) {
			String msg = "/control/poi/search no";
			Message message = new Message();
			message.what = Utils.navi_poi_search;
			String number = session.getParms().get("key");
			message.obj = number;
			if (mMainHandler != null) {
				mMainHandler.sendMessage(message);
				msg = "/control/poi/search yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		//2.6.2 POI地址：POI列表操作
		if (uri.equals("/control/poi/action")) {
			String msg = "/control/poi/action no";
			Message message = new Message();
			message.what = Utils.navi_poi_action;
			String number = session.getParms().get("action");
			message.obj = number;
			if (mNaviSearchHandler != null) {
				mNaviSearchHandler.sendMessage(message);
				msg = "/control/poi/action yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		//2.6.3 导航：激活显示目的地列表
		if (uri.equals("/control/navigation/list/address")) {
			String msg = "/control/navigation/list/address no";
			Message message = new Message();
			message.what = Utils.navi_list;
			if (mMainHandler != null) {
				mMainHandler.sendMessage(message);
				msg = "/control/navigation/list/address yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		//2.6.4 导航：选择目的地并显示路径列表
		if (uri.equals("/control/navigation/select/address")) {
			String msg = "/control/navigation/select/address no";
			Message message = new Message();
			message.what = Utils.navi_select;
			if (mNaviSearchHandler != null) {
				mNaviSearchHandler.sendMessage(message);
				msg = "/control/navigation/select/address yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		//2.6.5 导航：选择并确认路径action
		if (uri.equals("/control/navigation/route/action")) {
			String msg = "/control/navigation/route/action no";
			Message message = new Message();
			message.what = Utils.navi_action;
			if (mNaviRouteHandler != null) {
				mNaviRouteHandler.sendMessage(message);
				msg = "/control/navigation/select/address yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		//2.6.6 导航：同步导航信息
		if (uri.equals("/control/navigation/sync")) {
			String msg = "/control/navigation/sync no";
			Message message = new Message();
			Bundle bundle = new Bundle();
			message.what = Utils.navi_last;
			bundle.putString("speed", session.getParms().get("speed"));
			bundle.putString("message", session.getParms().get("message"));
			//bundle.putString("isLast", session.getParms().get("boolean"));
			bundle.putString("distanceToNext",
					session.getParms().get("distanceToNext"));
			bundle.putString("play", session.getParms().get("play"));
			message.obj = bundle;// 传递的参数 我可以用bundle绑定
			if (mNaviHandler != null) {
				mNaviHandler.sendMessage(message);
				msg = "/control/navigation/sync yes";
			}
			return new NanoHTTPD.Response(msg);
		}
		
		/*************2.7 分享**************/
		if (uri.equals("/control/share")) {
			Message message = new Message();
			String integer = session.getParms().get("param");
			//1:音乐 2:导航 3;车位
			message.obj = integer;
			message.what = Utils.share;
			if (mMainHandler != null) {
				mMainHandler.sendMessage(message);
			}
		}
		return new NanoHTTPD.Response(localMsg);
	}

	public void setMainHandler(Handler handler) {
		this.mMainHandler = handler;
	}

	public void setMusicHandler(Handler handler) {
		this.mMusicHandler = handler;
	}

	public void setNaviHandler(Handler handler) {
		this.mNaviHandler = handler;
	}

	public void setRadioHandler(Handler handler) {
		this.mRadioHandler = handler;
	}

	public void setPhoneHandler(Handler handler) {
		this.mContactHandler = handler;
	}

	public void setMsgHandler(Handler handler) {
		this.mMsgHandler = handler;
	}
	
	public void setMsgConfirmHandler(Handler handler){
		this.mMsgConfirmHandler = handler;
	}

	public void setPhoneIncomingHandler(Handler handler) {
		this.mPhoneIncomingHandler = handler;
	}
	
	public void setPhoneDialSureHandler(Handler handler){
		this.mPhoneDialSureHandler = handler;
	}
	
	public void setPhoneCallingHandler(Handler handler){
		this.mPhoneCallingHandler = handler;
	}
	
	public void setPhoneStrangeHandler(Handler handler){
		this.mPhoneStrangeHandler = handler;
	}
	
	public void setMsgIncomingHandler(Handler handler){
		this.mMsgIncomingHandler = handler;
	}
	
	public void setMsgEditHandler(Handler handler){
		this.mMsgEditHandler = handler;
	}
	
	public void setPoiHandler(Handler handler){
		this.mPoiHandler = handler;
	}
	
	public void setNaviSearchHandler(Handler handler){
		this.mNaviSearchHandler = handler;
	}
	
	public void setNaviRouteHandler(Handler handler){
		this.mNaviRouteHandler = handler;
	}
	
	public boolean isMainUsing() {
		return isMainUsing;
	}

	public void setMainUsing(boolean isMainUsing) {
		this.isMainUsing = isMainUsing;
	}

	public boolean isMusicUsing() {
		return isMusicUsing;
	}

	public void setMusicUsing(boolean isMusicUsing) {
		this.isMusicUsing = isMusicUsing;
	}

	public boolean isNaviUsing() {
		return isNaviUsing;
	}

	public void setNaviUsing(boolean isNaviUsing) {
		this.isNaviUsing = isNaviUsing;
	}

	public boolean isRadioUsing() {
		return isRadioUsing;
	}

	public void setRadioUsing(boolean isRadioUsing) {
		this.isRadioUsing = isRadioUsing;
	}

	public boolean isContactUsing() {
		return isContactUsing;
	}

	public void setContactUsing(boolean isContactUsing) {
		this.isContactUsing = isContactUsing;
	}

	public boolean isMsgUsing() {
		return isMsgUsing;
	}

	public void setMsgUsing(boolean isMsgUsing) {
		this.isMsgUsing = isMsgUsing;
	}

	public boolean isPhoneIncomingUsing() {
		return isPhoneIncomingUsing;
	}

	public void setPhoneIncomingUsing(boolean isPhoneIncomingUsing) {
		this.isPhoneIncomingUsing = isPhoneIncomingUsing;
	}

	public boolean isNaviRouteUsing() {
		return isNaviRouteUsing;
	}

	public void setNaviRouteUsing(boolean isNaviRouteUsing) {
		this.isNaviRouteUsing = isNaviRouteUsing;
	}

	public boolean isNaviSearchUsing() {
		return isNaviSearchUsing;
	}

	public void setNaviSearchUsing(boolean isNaviSearchUsing) {
		this.isNaviSearchUsing = isNaviSearchUsing;
	}

	public boolean isNaviTurnToTurnUsing() {
		return isNaviTurnToTurnUsing;
	}

	public void setNaviTurnToTurnUsing(boolean isNaviTurnToTurnUsing) {
		this.isNaviTurnToTurnUsing = isNaviTurnToTurnUsing;
	}

	public boolean isPOIUsing() {
		return isPOIUsing;
	}

	public void setPOIUsing(boolean isPOIUsing) {
		this.isPOIUsing = isPOIUsing;
	}

	public boolean isPhoneUsing() {
		return isPhoneUsing;
	}

	public void setPhoneUsing(boolean isPhoneUsing) {
		this.isPhoneUsing = isPhoneUsing;
	}

	public boolean isPhoneCallingUsing() {
		return isPhoneCallingUsing;
	}

	public void setPhoneCallingUsing(boolean isPhoneCallingUsing) {
		this.isPhoneCallingUsing = isPhoneCallingUsing;
	}

	public boolean isPhoneDialSureUsing() {
		return isPhoneDialSureUsing;
	}

	public void setPhoneDialSureUsing(boolean isPhoneDialSureUsing) {
		this.isPhoneDialSureUsing = isPhoneDialSureUsing;
	}

	public boolean isPhoneDialPanelUsing() {
		return isPhoneDialPanelUsing;
	}

	public void setPhoneDialPanelUsing(boolean isPhoneDialPanelUsing) {
		this.isPhoneDialPanelUsing = isPhoneDialPanelUsing;
	}

	public boolean isPhoneSelectUsing() {
		return isPhoneSelectUsing;
	}

	public void setPhoneSelectUsing(boolean isPhoneSelectUsing) {
		this.isPhoneSelectUsing = isPhoneSelectUsing;
	}

	public boolean isPhoneStrangeUsing() {
		return isPhoneStrangeUsing;
	}

	public void setPhoneStrangeUsing(boolean isPhoneStrangeUsing) {
		this.isPhoneStrangeUsing = isPhoneStrangeUsing;
	}

	public boolean isMsgConfirmUsing() {
		return isMsgConfirmUsing;
	}

	public void setMsgConfirmUsing(boolean isMsgConfirmUsing) {
		this.isMsgConfirmUsing = isMsgConfirmUsing;
	}

	public boolean isMsgNewIncomingUsing() {
		return isMsgNewIncomingUsing;
	}

	public void setMsgNewIncomingUsing(boolean isMsgNewIncomingUsing) {
		this.isMsgNewIncomingUsing = isMsgNewIncomingUsing;
	}

	public boolean isMsgIncomingUsing() {
		return isMsgIncomingUsing;
	}

	public void setMsgIncomingUsing(boolean isMsgIncomingUsing) {
		this.isMsgIncomingUsing = isMsgIncomingUsing;
	}
}
