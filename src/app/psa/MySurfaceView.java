package app.psa;

import java.io.IOException;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;

import junit.framework.Test;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.renderscript.Sampler.Value;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;


public class MySurfaceView extends SurfaceView implements Callback, Runnable {

	private Thread th;
	private SurfaceHolder sfh;
	private Canvas canvas;
	private Paint paint;
	private boolean flag;
	//固定摇杆背景圆形的X,Y坐标以及半径
	private static int default_RockerCircleX = 20;
	private static int default_RockerCircleY = 20;
	private static int default_SmallRockerCircleX = 75;
	private static int default_SmallRockerCircleY = 70;
	
	private static int default_offset = 100;
	private int RockerCircleX;
	private int RockerCircleY;
	private int RockerCircleR = 0;
	//摇杆的X,Y坐标以及摇杆的半径
	private float SmallRockerCircleY;
	private float SmallRockerCircleR = 0;
	Bitmap mBg; 
	Bitmap mInner; 
	TextView mUpNum;
	TextView mCircleNum;
	int i = 44;
	private AirFrontTempLeftTask airFrontTempLeftTask;
	private boolean airLeftFinished = true;
	private AirBehindTempRightTask airBehindTempRightTask;
	int pos;
	int leftOrRight = -1;
	RelativeLayout mNumUpField;
	RelativeLayout mNumDownField;
	public boolean isTouched = false;
	
	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
			super.handleMessage(msg);
			String text = (String)msg.obj;
			int a = Integer.parseInt(text);
			Log.e("xx",String.valueOf(a));
			//int a = Integer.parseInt(text)-1;
			String text2 = String.valueOf(a);
			mUpNum.setText(text);
		}
	};
	
	public MySurfaceView(Context context,int leftOrRight) {
		super(context);
		this.leftOrRight = leftOrRight;
		initial();
	}

	public MySurfaceView(Context context,AttributeSet attributeSet) {
		// TODO Auto-generated constructor stub
		super(context, attributeSet);
		initial();
	}
	
	/**left 1**/
	public void setLeftOrRight(int index){
		this.leftOrRight = 1;
		initial();
		//draw();
	}
	
	public void setview(TextView textView){
		mUpNum = textView;
	}
	
	public void setCircleView(TextView textView){
		mCircleNum = textView;
	}
	
	public void setPos(int pos){
		this.pos = pos;
	}
	public void setNumUpField(RelativeLayout relativeLayout){
		this.mNumUpField = relativeLayout;
	}
	public void setNumDownField(RelativeLayout relativeLayout){
		this.mNumDownField = relativeLayout;
	}
	
	private void initial(){
		this.setKeepScreenOn(true);
		sfh = this.getHolder();
		sfh.addCallback(this);
		paint = new Paint();
		paint.setAntiAlias(true);
		setFocusable(true);
		setFocusableInTouchMode(true);
		if(leftOrRight == 1){
			mBg = BitmapFactory.decodeResource(getResources(), R.drawable.ac_main_bg_left);	
		}else {
			mBg = BitmapFactory.decodeResource(getResources(), R.drawable.ac_main_bg);
		}
		mInner = BitmapFactory.decodeResource(getResources(), R.drawable.ac_main_inner);
		mBg = picInitial(mBg,500,500);
		mInner = picInitial(mInner, 380, 380);
		RockerCircleX = default_RockerCircleX;
		RockerCircleY = default_RockerCircleY;
		RockerCircleR = mBg.getWidth()/2;
        SmallRockerCircleY = default_SmallRockerCircleY;//mInner.getWidth()/2;
	}
	
	private Bitmap picInitial(Bitmap bitmapOrg,int newWidth,int newHeight){
       
		//获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();
        
        //定义预转换成的图片的宽度和高度      
        //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        
        //旋转图片 动作
        //matrix.postRotate(45);
        
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
        width, height, matrix, true);
        return resizedBitmap;
        //return bitmapOrg;
	}
	
	public void surfaceCreated(SurfaceHolder holder) {
		th = new Thread(this);
		flag = true;
		th.start();
	}

	/***
	 * 得到两点之间的弧度
	 */
	public double getRad(float px1, float py1, float px2, float py2) {
		//得到两点X的距离
		float x = px2 - px1;
		//得到两点Y的距离
		float y = py1 - py2;
		//算出斜边长
		float xie = (float) Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2));
		//得到这个角度的余弦值（通过三角函数中的定理 ：邻边/斜边=角度余弦值）
		float cosAngle = x / xie;
		//通过反余弦定理获取到其角度的弧度
		float rad = (float) Math.acos(cosAngle);
		//注意：当触屏的位置Y坐标<摇杆的Y坐标我们要取反值-0~-180
		if (py2 < py1) {
			rad = -rad;
		}
		return rad;
	}

	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		float y = event.getY();
		Log.e("Y",String.valueOf(y));
	if (Utils.isAirConOpen == true) {
	
		if (event.getAction() == MotionEvent.ACTION_UP) {
			SmallRockerCircleY = default_SmallRockerCircleY;
			mUpNum.setVisibility(View.INVISIBLE);
			mCircleNum.setVisibility(View.VISIBLE);
			mCircleNum.setText(mUpNum.getText().toString());
			if (pos == 0 && airLeftFinished) {
				
				AirFrontTempLeftTask mTask1 = new AirFrontTempLeftTask();
				mTask1.execute(mUpNum.getText().toString());	
			}if (pos == 1) {
				
				AirBehindTempRightTask mTask2 = new AirBehindTempRightTask();
				mTask2.execute(mUpNum.getText().toString());
			}
		}else {

			//SmallRockerCircleY = default_SmallRockerCircleY;
			//mUpNum.setVisibility(View.VISIBLE);
			//mUpNum.setText(mCircleNum.getText().toString());
			//mCircleNum.setVisibility(View.INVISIBLE);
			if (y < 250 && y>20) {
				//mBg = BitmapFactory.decodeResource(getResources(), R.drawable.ac_hover);
				//mBg = picInitial(mBg,500,600);
				
				mNumUpField.setBackground(getResources().getDrawable(R.drawable.ac_cicle));
				mUpNum.setVisibility(View.VISIBLE);
				mCircleNum.setVisibility(View.INVISIBLE);
				numUp();
				//SmallRockerCircleY = y - 150;
				SmallRockerCircleY = 10;
				//if (SmallRockerCircleY < 10) {
					//SmallRockerCircleY = 10;
				//}
				
				
			
			}if (y > 300 && y< 550) {

				//mBg = BitmapFactory.decodeResource(getResources(), R.drawable.ac_hover);
				//mBg = picInitial(mBg,500,600);
				
				
				mNumUpField.setBackground(getResources().getDrawable(R.drawable.ac_cicle));
				mUpNum.setVisibility(View.VISIBLE);
				mCircleNum.setVisibility(View.INVISIBLE);
				numDown();
				//SmallRockerCircleY = y;
				//event.if (SmallRockerCircleY > 350) {
					//SmallRockerCircleY = SmallRockerCircleY - 250;
				//}
				//initial();
				SmallRockerCircleY = 150;
				
			}if (y >= 250 && y<= 300 ) {
				//mBg = BitmapFactory.decodeResource(getResources(), R.drawable.ac_hover);
				//mBg = picInitial(mBg,500,600);
				
				mNumUpField.setBackground(getResources().getDrawable(R.drawable.ac_cicle));
				mUpNum.setVisibility(View.VISIBLE);
				mCircleNum.setVisibility(View.INVISIBLE);
				SmallRockerCircleY = default_SmallRockerCircleY;
				//initial();
			}
		}
	}
		return true;
	}
	
	/**
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		float y = event.getY();
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			Log.e("down", String.valueOf(y));
			SmallRockerCircleY = default_SmallRockerCircleY;
			mUpNum.setVisibility(View.VISIBLE);
			mUpNum.setText(mCircleNum.getText().toString());
			mCircleNum.setVisibility(View.INVISIBLE);
			return true;
		}if (event.getAction() == MotionEvent.ACTION_MOVE) {
//			Log.e("move:", String.valueOf(y));
			if (y<0) {
				numUp();
			}
			if (0<y && y<240) {
				SmallRockerCircleY = y - 120;
				if (SmallRockerCircleY < 10) {
					SmallRockerCircleY = 10;
				}
			}
			if (240<y && y<340) {
				SmallRockerCircleY = default_SmallRockerCircleY;
			}if (y > 340 && y <380) {
				SmallRockerCircleY = y;
				if (SmallRockerCircleY > 350) {
					SmallRockerCircleY = SmallRockerCircleY - 230;
				}
				numDown();
			}if (420 > y && y > 380) {
				numDown();
			}if (500 > y && y>420) {
				
			}if (y>500) {
				numDown();
			}
			
		}if (event.getAction() == MotionEvent.ACTION_UP) {
			SmallRockerCircleY = default_SmallRockerCircleY;
			mUpNum.setVisibility(View.INVISIBLE);
			mCircleNum.setVisibility(View.VISIBLE);
			mCircleNum.setText(mUpNum.getText().toString());
			if (pos == 0 && airLeftFinished) {
				
				AirFrontTempLeftTask mTask1 = new AirFrontTempLeftTask();
				mTask1.execute(mUpNum.getText().toString());	
			}if (pos == 1) {
				
				AirBehindTempRightTask mTask2 = new AirBehindTempRightTask();
				mTask2.execute(mUpNum.getText().toString());
			}
		}
		return true;
	}**/

	/**
	 * 
	 * @param R
	 *            圆周运动的旋转点
	 * @param centerX
	 *            旋转点X
	 * @param centerY
	 *            旋转点Y
	 * @param rad
	 *            旋转的弧度
	 */
	public void getXY(float centerX, float centerY, float R, double rad) {
		
		//Log.d("getXY", "centerX: "+String.valueOf(centerX));
         //获取圆周运动的X坐标 
			//获取圆周运动的Y坐标
			SmallRockerCircleY = (float) (R * Math.sin(rad)) + centerY;
		
	}

	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		// TODO Auto-generated method stub
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		Log.d("onMeasure", "width: "+String.valueOf(widthMeasureSpec));
	}
	
	public void draw() {
		try {
			canvas = sfh.lockCanvas();
			canvas.drawColor(Color.BLACK);
			//canvas.drawCircle(cx, cy, radius, paint);
			//设置透明度
			//paint.setColor(0x70000000);
			//绘制摇杆背景
			//Log.i("draw RockerCircleX", String.valueOf(RockerCircleX));
			//Log.i("draw RockerCircleY", String.valueOf(RockerCircleY));
			//Log.i("draw smallRockerCircleX", String.valueOf(SmallRockerCircleX));
			//Log.i("draw smallRockerCircleY", String.valueOf(SmallRockerCircleY));
			canvas.drawBitmap(mBg,default_RockerCircleX,default_RockerCircleY, null);
			//canvas.drawCircle(RockerCircleX, RockerCircleY, RockerCircleR, paint);
			//paint.setColor(0x70ff0000);
			//绘制摇杆
			Matrix matrix = new Matrix();
			matrix.setScale(0.7f,0.7f);
			canvas.drawBitmap(mInner,default_SmallRockerCircleX,SmallRockerCircleY, null);

			//canvas.drawCircle(SmallRockerCircleX, SmallRockerCircleY, SmallRockerCircleR, paint);
		} catch (Exception e) {
			// TODO: handle exception
		} finally {
			try {
				if (canvas != null)
					sfh.unlockCanvasAndPost(canvas);
			} catch (Exception e2) {

			}
		}
	}

	
	public void run() {
		// TODO Auto-generated method stub
		while (flag) {
			//if(leftOrRight == 1){
				//initial();
			//}
			if (mNumDownField != null) {
				
			}
			if (mNumUpField != null) {
							}
			draw();
			try {
				Thread.sleep(50);
			} catch (Exception ex) {
			}
		}
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		Log.d("surface", "surfaceChanged");
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		flag = false;
		Log.d("surface", "surfaceDestroyed");
	}
	
	private void numUp(){
		String str1 = mUpNum.getText().toString();
		int tmp1 = Integer.valueOf(str1);
		if (tmp1 < Utils.airMaxTempNum) {
			tmp1++;
			//tmp1 = tmp1+1;
			//tmp1 = tmp1+2;
			str1 = String.valueOf(tmp1);
			//String str2 = tmp1
			Log.e(str1, "aa");
			Log.e(String.valueOf(tmp1), "aaa");
			Message msg1 = new Message();
			msg1.what = 1252951;
			msg1.obj = str1;
		    mHandler.sendMessageDelayed(msg1,150);	
		}
	}
	
	private void numDown(){
		String str2 = mUpNum.getText().toString();
		int tmp2 = Integer.valueOf(str2);
		if (tmp2 > Utils.airMinTempNum) {
			tmp2 = tmp2-1;
			//tmp2 = tmp2;
			str2 = String.valueOf(tmp2);
			Message msg2 = new Message();
			msg2.what = 1252952;
			msg2.obj = str2;
			mHandler.sendMessageDelayed(msg2,150);	
		}
	}
	class AirModeTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String mode = arg0[0];
			try {
				Http.AirConFlowPost(mode);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}
	class AirFrontTempLeftTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			airLeftFinished = false;
		}
		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String num = arg0[0];
			try {
				Http.AirConTemperaturePost(num);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			airLeftFinished = true;
		}
	}
	

	
	class AirBehindTempRightTask extends AsyncTask<String,Integer,Integer>{

		@Override
		protected Integer doInBackground(String... arg0) {
			// TODO Auto-generated method stub
			String num = arg0[0];
			try {
				Http.AirConTemperaturePost2(num);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return null;
		}
		
	}



	
}