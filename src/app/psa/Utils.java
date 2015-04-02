package app.psa;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import psa.music.Mp3Info;
import psa.music.PlayerService;

import fi.iki.elonen.NanoHTTPD.DefaultAsyncRunner;

import android.R.integer;
import android.R.string;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.database.CursorJoiner.Result;
import android.net.NetworkInfo.State;
import android.util.Log;

public class Utils {

	public static String [] phone_name = new String[]{"����","���", "��ͮ", "���", "����", "��ï��", "��껶�"};
	public static int [] phone_photo = new int[]{R.drawable.phone_contact_7,
			R.drawable.phone_contact_1, R.drawable.phone_contact_3, 
			R.drawable.phone_contact_4,R.drawable.phone_contact_2,
			R.drawable.phone_contact_5, R.drawable.phone_contact_6
		 };
	/****/
	public static int trasfer_music = 2312312;
	public static int main_air_num = 0;
	public static int main_air_flow_status = 1;
	public static int main_air_flow_speed = 100000;
	public static int main_air_status = 2;
	public static int music_action = 3;
	public static int music_search = 4;
	public static int music_next = 5;
	public static int music_volume = 6;
	public static int music_volume_left = 60;
	public static int music_volume_right = 61;
	public static int musci_now = 700000;
	public static int music_slide_left = 700100;
	public static int music_slide_right = 700101;
	public static int contact_search = 700001;
	public static int contact_action = 700002;
	public static int phone_coming = 7;
	public static int phone_reject = 8;
	public static int main_phone_calling = 9;
	public static int phone_callout = 900001;
	public static int phone_answer = 900002;
	public static int phone_strange_answer = 910002;
	public static int phone_input = 900003;
	public static int main_phone_msg = 900004;
	public static int radio_control = 900000;
	public static int radio_search = 1000000;
	public static int radio_volume_left = 1000001;
	public static int radio_volume_right = 1000002;
	public static int radio_start = 10;
	public static int radio_channel = 11;
	public static int radio_yes = 12;
	public static int radio_next = 13;
	public static int msg_send = 14;
	public static int msg_sure = 15;
	public static int msg_input = 150000;
	public static int msg_edit = 1500001;
	public static int msg_check = 1500002;
	public static int msg_ignore = 1500003;
	public static int navi_last = 16;
	public static int navi_poi_search = 17;
	public static int navi_poi_action = 18;
	public static int navi_poi_rest = 181;
	public static int navi_poi_gas = 182;
	public static int navi_poi_house = 183;
	public static int navi_list = 19;
	public static int navi_select = 20;
	public static int navi_action = 21;
	public static int main_reset = 22;
	public static int transfer_radio = 23;
	public static int share = 24;
	public static int share_music = 25;
	public static int share_park = 26;
	
	static int default_air_max = 30;
	static int default_air_min = 16;
	
	public static boolean isAirConOpen = false;
	public static boolean isMusicPlaying = false;
	public static boolean isMusicPause = true;
	public static boolean isPhoneConfirm = false;
	public static boolean isPhoneIncoming = false;
	public static boolean isMsgIncoming = false;
	public static boolean isCallouting = false;
	public static boolean isDiaSure = false;
	public static boolean isNaviBegin = false;
	public static boolean isMusicRelease = false;
	
	public static int comingOrCalling = -1;
	public static int strangeOrDial = -1;
	
	public static int airMaxTempNum = 30;
	public static int airMinTempNum = 16;
	public static int airMaxFlowNum = 15;
	public static int airMinFlowNum = 0;
	
	/****/
	public static int naviTurnFinish_Intent = 0;
	public static int currentScene = 1;
	public static int currentView = 1;
	public static int currentMusicIndex = 0;
	public static int currentRadioIndex = 0;
	public static boolean isPoi = false;
	public static Mp3Info currentMp3Info;
	public static boolean isNaviInitFinished = false;
	public static boolean isWangbing = false;
	
	public static void reset(Activity mActivity){
		Intent mIntent = new Intent(mActivity,MainActivity.class);
		mIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		mIntent.putExtra("Main_Reset", true);
		mActivity.startActivity(mIntent);
	}
	
	/**
	 * �жϿյ���ֵ�Ƿ���ȷ
	 * @return
	 */
	public static boolean isAirNumRight(int num){
		
		if (default_air_min< num && num < default_air_max) {
			return true;
		}
		return false;
	}
	
    static int xCount = 0;
	/**
	 * �ж����Ʒ���
	 * @param stack
	 * @param x
	 * @return -1 ��������
	 * 			0 �󻬶���
	 * 			1 �һ�����
	 * 		
	 */
	public static int sildeUtil(Stack<Float> stack,float x){
			if (xCount == 3 && stack.size() < 2) {
				stack.push(x);
				xCount = 0;
			}if (stack.size() == 2) {
				float x2 = stack.pop();
				float x1 = stack.pop();
				if (Math.abs(x2-x1) < 15) {
					//�仯��̫С
					stack.clear();
					return -1;
				}
				if (x2 - x1 > 0) {
					//˵����ʱ���һ���
					stack.clear();
					Log.e("mBottomss", "��");
					return 1;
				}else {
					stack.clear();
					Log.e("mBottomss", "��");
					return 0;
				}
			}
		xCount++;
		return -1;
	}
	
	/**
	 * �жϻ���slide �Ƿ�����Ұ��
	 * ����slide Ϊ�յ�
	 * @param x
	 * @return
	 */
	public static boolean isBottomAirSlideInField(double x){
		
		if (x >100 && x<450) {
			return true;
		}
		return false;
	}
	public static boolean isTopMusicSlideInField(double x){
		if (x >100 && x<400) {
			return true;
		}
		return false;
	}
	
	public static boolean isCenterMusicSlideInField(double x){
		if (x >300 && x<1350) {
			return true;
		}
		return false;
	}
	
	
	
	public static boolean isBottomMusicSlideInField(double x){
		if (x >320 && x<1200) {
			return true;
		}
		return false;
	}
	
	public static boolean isRadioSlideInField(double x){
		if (x>120 && x<1200) {
			return true;
		}
		return false;
	}
	
	public static int leftOrRight(double x,double y){ 
		//����λ�õ��ж�
		if ((50<x && x <600) && (y>200 && y<800)) {
			//˵�������
			return 0;
		}if ((x>1000 && x<1400) && (y>200 && y<800)) {
			//˵�����Ҳ�
			return 1;
		}if ((1000> x && x>500) &&(1200>y && y>900)) {
			return 2;
		}
		return -1;
	}
	
	static String [] string3 = new String[]{"JAZZ","FUNK","ROCK"}; //״̬1
	static String [] string4 = new String[]{"ROCK","JAZZ","FUNK"}; //״̬2
	static String [] string5 = new String[]{"FUNK","ROCK","JAZZ"}; //״̬3
	static List<String[]> list2 = new ArrayList<String[]>();
	public static void centerMusicSlideInitial(){
		list2.add(string3);
		list2.add(string4);
		list2.add(string5);
	}
	public static String[] centerMusicSlide(int direction,String[] now){
		String [] result = null;
		//һ����0 1 2 ����״̬
		if (direction == 0) {
			//��
			for (int i = 0; i < list2.size(); i++) {
				if (list2.get(i)[0].equals(now[0])) {
					//����ҵ��˵�ǰ״̬
					switch (i) {
					case 0:
						//0��Ϊ2״̬
						result = list2.get(2);
						break;
					case 1:
						//1��Ϊ0״̬
						result = list2.get(0);
						break;
					case 2:
						//2��Ϊ1״̬
						result = list2.get(1);
						break;
					default:
						break;
					}
				}
			}
			
		}if (direction == 1) {
			//�һ�
			for (int i = 0; i < list2.size(); i++) {
				if (list2.get(i)[0].equals(now[0])) {
					//����ҵ��˵�ǰ״̬
					switch (i) {
					case 0:
						//0�һ�Ϊ1״̬
						result = list2.get(1);
						break;
					case 1:
						//1�һ�Ϊ2״̬
						result = list2.get(2);
						break;
					case 2:
						//2�һ�Ϊ0״̬
						result = list2.get(0);
						break;
					default:
						break;
					}
				}
			}
		}
		return result;
	}
	
	static String [] string0 = new String[]{"����","����","�Զ�"}; //״̬1
	static String [] string1 = new String[]{"�Զ�","����","����"}; //״̬2
	static String [] string2 = new String[]{"����","�Զ�","����"}; //״̬3
	
	static List<String[]>list1 = new ArrayList<String[]>();
	
	public static void bottomAirSlideInitial(){
		list1.add(string0);
		list1.add(string1);
		list1.add(string2);
	}
	public static String[] bottomAirSlide(int direction,String[] now){
		String [] result = null;
		//һ����0 1 2 ����״̬
		if (direction == 0) {
			//��
			for (int i = 0; i < list1.size(); i++) {
				Log.e("list", String.valueOf(list1.get(i).length));
				if (list1.get(i)[0].equals(now[0])) {
					//����ҵ��˵�ǰ״̬
					switch (i) {
					case 0:
						//0��Ϊ2״̬
						result = list1.get(2);
						break;
					case 1:
						//1��Ϊ0״̬
						result = list1.get(0);
						break;
					case 2:
						//2��Ϊ1״̬
						result = list1.get(1);
						break;
					default:
						break;
					}
				}
			}
			
		}if (direction == 1) {
			//�һ�
			for (int i = 0; i < list1.size(); i++) {
				if (list1.get(i)[0].equals(now[0])) {
					//����ҵ��˵�ǰ״̬
					switch (i) {
					case 0:
						//0�һ�Ϊ1״̬
						result = list1.get(1);
						break;
					case 1:
						//1�һ�Ϊ2״̬
						result = list1.get(2);
						break;
					case 2:
						//2�һ�Ϊ0״̬
						result = list1.get(0);
						break;
					default:
						break;
					}
				}
			}
		}
		return result;
	}
	

	static String [] string_0 = new String[]{"ǿ��","����","�º�"}; //״̬1
	static String [] string_1 = new String[]{"�º�","ǿ��","����"}; //״̬2
	static String [] string_2 = new String[]{"����","�º�","ǿ��"}; //״̬3
	
	static List<String[]>list3 = new ArrayList<String[]>();
	public static void bottomWindSlideInitial(){
		list3.add(string_0);
		list3.add(string_1);
		list3.add(string_2);
	}
	public static String[] bottomWindSlide(int direction,String[] now){
		String [] result = null;
		//һ����0 1 2 ����״̬
		if (direction == 0) {
			//��
			for (int i = 0; i < list3.size(); i++) {
				Log.e("list3", String.valueOf(list3.get(i).length));
				if (list3.get(i)[0].equals(now[0])) {
					//����ҵ��˵�ǰ״̬
					switch (i) {
					case 0:
						//0��Ϊ2״̬
						result = list3.get(2);
						break;
					case 1:
						//1��Ϊ0״̬
						result = list3.get(0);
						break;
					case 2:
						//2��Ϊ1״̬
						result = list3.get(1);
						break;
					default:
						break;
					}
				}
			}
			
		}if (direction == 1) {
			//�һ�
			for (int i = 0; i < list3.size(); i++) {
				if (list3.get(i)[0].equals(now[0])) {
					//����ҵ��˵�ǰ״̬
					switch (i) {
					case 0:
						//0�һ�Ϊ1״̬
						result = list3.get(1);
						break;
					case 1:
						//1�һ�Ϊ2״̬
						result = list3.get(2);
						break;
					case 2:
						//2�һ�Ϊ0״̬
						result = list3.get(0);
						break;
					default:
						break;
					}
				}
			}
		}
		return result;
	}
	
	public static String[] radioSlide(int direction,String [] now){
		String [] result = null;
		if (direction == 0) {
			//��
			 Log.e("now[0]", now[0]);
			 float a1 = (float) (Float.valueOf(now[0])-0.1);
			 float a2 = (float) (Float.valueOf(now[0])-0.1);
			 float a3 = (float) (Float.valueOf(now[0])-0.1);
			 float a4 = (float) (Float.valueOf(now[0])-0.1);
			 float a5 = (float) (Float.valueOf(now[0])-0.1);
			 Log.e("a1", String.valueOf(a1));
			 result = new String[5];
			 result[0] = getSubStr(String.valueOf(a1));
			 result[1] = getSubStr(String.valueOf(a2));
			 result[2] = getSubStr(String.valueOf(a3));
			 result[3] = getSubStr(String.valueOf(a4));
			 result[4] = getSubStr(String.valueOf(a5));
		}if (direction == 1) {
			//��
			float a1 = (float) (Float.valueOf(now[0]) + 0.1);
			float a2 = (float) (Float.valueOf(now[0]) + 0.1);
			float a3 = (float) (Float.valueOf(now[0]) + 0.1);
			float a4 = (float) (Float.valueOf(now[0]) + 0.1);
			float a5 = (float) (Float.valueOf(now[0]) + 0.1);
			result = new String[5];
			result[0] = getSubStr(String.valueOf(a1));
			result[1] = getSubStr(String.valueOf(a2));
			result[2] = getSubStr(String.valueOf(a3));
			result[3] = getSubStr(String.valueOf(a4));
			result[4] = getSubStr(String.valueOf(a5));
		}
		return result;
	}
	
	private static  String getSubStr(String str){
		char tmp;
		String result = str;
		if (str.length() > 3) {
			result = str.substring(0,4);
		}
		return result;
	}
	
	public static String getMusicID(String name){
		
		String id = "0";
		if (name.equals("����")) {
			id = "0";
		}
		if (name.equals("�п��¹�")) {
			id = "1";
		}
		if (name.equals("����")) {
			id = "2";
		}
		if (name.equals("�þò���")) {
			id = "3";
		}
		if (name.equals("�����")) {
			id = "4";
		}
		return id;
	}
}
