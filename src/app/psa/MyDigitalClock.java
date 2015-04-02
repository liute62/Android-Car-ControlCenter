package app.psa;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.SystemClock;
import android.renderscript.Sampler.Value;
import android.text.format.DateUtils;
import android.text.format.Time;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class MyDigitalClock extends View implements OnClickListener{
	private Drawable mHourHand;
	private Drawable mMinuteHand;
	private Drawable mSecondHand;
	private Drawable mDial;
	private Time mCalendar;
	
	//private Typeface tfAirCondition;
	 private String mDay;// 日期  
	 private String mWeek;// 星期  
	 
	 private String mdighour;
	 private String mdigMinute;
	 private Typeface tfClockVenra;
	 
	 private int mDialWidth;
	 private int mDialHeight;
	 
	 private float mHour; //时针值
	 private float mMinutes; //分针值
	 private float mSecond;//秒针值
	 private Boolean mChanged;
	 
	 private Paint mPaint;
	 
	 private Runnable mTicker;
	 private final Handler mHandler = new Handler();
	 
	 private boolean mTickerStopped = false;
	 
	 public MyDigitalClock(Context context) {  
	        this(context, null);  
	    }  
	 
	 public MyDigitalClock(Context context, AttributeSet attrs) {  
	        this(context, attrs, 0);  
	    }  
	 

	public MyDigitalClock(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		//Resources r = getContext().getResources();
		//changeBtn = (Button)this.findViewById(R.id.clock_btn);
		//changeBtn.setOnClickListener(this);
		
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.AnalogClock,defStyle,0);
		mDial = a.getDrawable(R.styleable.AnalogClock_dial); //调用attr里面的 
		mHourHand = a.getDrawable(R.styleable.AnalogClock_hand_hour);
		mMinuteHand = a.getDrawable(R.styleable.AnalogClock_hand_minute);
		mSecondHand = a.getDrawable(R.styleable.AnalogClock_hand_second);
		
		if (mDial == null || mHourHand == null || mMinuteHand == null  
                || mSecondHand == null) {  
            //mDial = r.getDrawable(R.drawable.appwidget_clock_dial);  
            //mHourHand = r.getDrawable(R.drawable.appwidget_clock_hour);  
            //mMinuteHand = r.getDrawable(R.drawable.appwidget_clock_minute);  
            //mSecondHand = r.getDrawable(R.drawable.appwidget_clock_second);  
        }  
        a.recycle();
        
        mDialWidth = mDial.getIntrinsicWidth();
        mDialHeight = mDial.getIntrinsicHeight();
        
        mPaint = new Paint();
        mPaint.setColor(Color.parseColor("#3399ff"));  
        mPaint.setTypeface(Typeface.DEFAULT_BOLD);  
        mPaint.setFakeBoldText(true);  
        mPaint.setAntiAlias(true);  
        
        if (mCalendar == null) {  
            mCalendar = new Time();  
        }  
	}
	
	
	private void onTimeChanged(){
		mCalendar.setToNow();
		int hour = mCalendar.hour;
		int minute = mCalendar.minute;
		int second = mCalendar.second;
		
		mHour = hour + mMinutes / 60.0f + mSecond / 3600.0f;// 小时值，加上分和秒，效果会更加逼真  
        mMinutes = minute + second / 60.0f;// 分钟值，加上秒，也是为了使效果逼真  
        mSecond = second;  
  
        mdighour = Integer.toString(hour);
        if(minute<10){
        	mdigMinute= "0"+Integer.toString(minute);
        }
        else{
        	mdigMinute= Integer.toString(minute);
        }
        mChanged = true;
        //updateContentDescription(mCalendar);
	}
	
	@Override  
    protected void onAttachedToWindow() {  
        mTickerStopped = false;// 添加到窗口中就要更新时间了  
        super.onAttachedToWindow();  
  
        /** 
         * requests a tick on the next hard-second boundary 
         */  
        mTicker = new Runnable() {  
            public void run() {  
                if (mTickerStopped)  
                    return;  
                onTimeChanged();  
                invalidate();  
                long now = SystemClock.uptimeMillis();  
                long next = now + (1000 - now % 1000);// 计算下次需要更新的时间间隔  
                mHandler.postAtTime(mTicker, next);// 递归执行，就达到秒针一直在动的效果  
            }  
        };  
        mTicker.run();  
    }  
	
	@Override  
    protected void onDetachedFromWindow() {  
        super.onDetachedFromWindow();  
        mTickerStopped = true;// 当view从当前窗口中移除时，停止更新  
    }  
	
	/*@SuppressLint("NewApi")
	@Override  
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {  
        // 模式： UNSPECIFIED(未指定),父元素不对子元素施加任何束缚，子元素可以得到任意想要的大小；  
        // EXACTLY(完全)，父元素决定自元素的确切大小，子元素将被限定在给定的边界里而忽略它本身大小；  
        // AT_MOST(至多)，子元素至多达到指定大小的值。  
        // 根据提供的测量值(格式)提取模式(上述三个模式之一)  
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);  
        // 根据提供的测量值(格式)提取大小值(这个大小也就是我们通常所说的大小)  
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);  
        // 高度与宽度类似  
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);  
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);  
  
        float hScale = 1.0f;// 缩放值  
        float vScale = 1.0f;  
  
        if (widthMode != MeasureSpec.UNSPECIFIED && widthSize < mDialWidth) {  
            hScale = (float) widthSize / (float) mDialWidth;// 如果父元素提供的宽度比图片宽度小，就需要压缩一下子元素的宽度  
        }  
  
        if (heightMode != MeasureSpec.UNSPECIFIED && heightSize < mDialHeight) {  
            vScale = (float) heightSize / (float) mDialHeight;// 同上  
        }  
  
        float scale = Math.min(hScale, vScale);// 取最小的压缩值，值越小，压缩越厉害  
        // 最后保存一下，这个函数一定要调用  
        setMeasuredDimension(  
                resolveSizeAndState((int) (mDialWidth * scale),  
                        widthMeasureSpec, 0),  
                resolveSizeAndState((int) (mDialHeight * scale),  
                        heightMeasureSpec, 0));  
    }  
  */
    @Override  
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {  
        super.onSizeChanged(w, h, oldw, oldh);  
        mChanged = true;  
    }  
	
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        
        boolean changed = mChanged;  
  
        if (changed) {  
            mChanged = false;  
        }  
  
        int availableWidth = getRight() - getLeft();// view可用宽度，通过右坐标减去左坐标  
        int availableHeight = getBottom() - getTop();// view可用高度，通过下坐标减去上坐标  
  
        int x = availableWidth / 2;// view宽度中心点坐标  
        int y = availableHeight / 2;// view高度中心点坐标  
  
        final Drawable dial = mDial;// 表盘图片  
        int w = dial.getIntrinsicWidth();// 表盘宽度  
        int h = dial.getIntrinsicHeight();  
  
        // int dialWidth = w;  
        int dialHeight = h;  
        boolean scaled = false;  
        // 最先画表盘，最底层的要先画上画板  
        if (availableWidth < w || availableHeight < h) {// 如果view的可用宽高小于表盘图片，就要缩小图片  
            scaled = true;  
            float scale = Math.min((float) availableWidth / (float) w,  
                    (float) availableHeight / (float) h);// 计算缩小值  
            canvas.save();  
            canvas.scale(scale, scale, x, y);// 实际上是缩小的画板  
        }  
  
        if (changed) {// 设置表盘图片位置。组件在容器X轴上的起点； 组件在容器Y轴上的起点； 组件的宽度；组件的高度  
            dial.setBounds(x - (w / 2)-40, y - (h / 2)-40, x + (w / 2)+40, y + (h / 2)+40);  
            //dial.setbo
        }  
        dial.draw(canvas);// 这里才是真正把表盘图片画在画板上  
        canvas.save();// 一定要保存一下  
        // 其次画日期  
        if (changed) {  
            //w = (int) (mPaint.measureText(mWeek));// 计算文字的宽度  
           // canvas.drawText(mWeek, (x - w / 2), y - (dialHeight / 8), mPaint);// 画文字在画板上，位置为中间两个参数  
            //w = (int) (mPaint.measureText(mDay));  
            //canvas.drawText(mDay, (x - w / 2), y + (dialHeight / 8), mPaint);// 同上  
        }  
        // 再画时针  
        //canvas.rotate(mHour / 12.0f * 360.0f, x, y);// 旋转画板，第一个参数为旋转角度，第二、三个参数为旋转坐标点  
        //final Drawable hourHand = mHourHand;  
        if (changed) {  
        	mPaint.setColor(Color.DKGRAY);
        	mPaint.setTextSize(42);
        	mPaint.setTypeface(MainActivity.tfAirCondition);
        	w = (int) (mPaint.measureText(mdighour));
        	canvas.drawText(mdighour, x-w/2-45, y+(dialHeight / 12) , mPaint);
        	
        	mPaint.setColor(Color.WHITE);
        	w = (int) (mPaint.measureText(mdigMinute));  
            canvas.drawText(mdigMinute, (x+w/2-15 ), y + (dialHeight / 12), mPaint);
           /* w = hourHand.getIntrinsicWidth();  
            h = hourHand.getIntrinsicHeight();  
            hourHand.setBounds(x - (w / 2), y - (h / 2)-30, x + (w / 2), y  
                    + (h / 2)-20);  */
        }  
        //hourHand.draw(canvas);// 把时针画在画板上  
        //canvas.restore();// 恢复画板到最初状态  
  
        canvas.save();  
        // 然后画分针  
       /* canvas.rotate(mMinutes / 60.0f * 360.0f, x, y);  
        final Drawable minuteHand = mMinuteHand;  
        if (changed) {  
            w = minuteHand.getIntrinsicWidth();  
            h = minuteHand.getIntrinsicHeight();  
            minuteHand.setBounds(x - (w / 2), y - (h / 2)-40, x + (w / 2), y  
                    + (h / 2)-30);  
        }  
        minuteHand.draw(canvas);  
        canvas.restore();  
  
        canvas.save();  */
        // 最后画秒针  
       canvas.rotate(mSecond / 60.0f * 360.0f, x, y);  
        final Drawable secondHand = mSecondHand;  
        if (changed) {  
            w = secondHand.getIntrinsicWidth();  
            h = secondHand.getIntrinsicHeight();  
            secondHand.setBounds(x - (w / 2)+118 , y - (h / 2)-0, x + (w / 2)+118, y  
                    + (h / 2));
            //(x - (w / 2), y - (h / 2)-100, x + (w / 2)-20, y  
                   // + (h / 2)-80);  
        }  
        secondHand.draw(canvas);  
        canvas.restore();  
        canvas.save();  
  
        if (scaled) {  
            canvas.restore();  
        }  
    }  
  
    /** 
     * 对这个view描述一下， 
     *  
     * @param time 
     */  
    private void updateContentDescription(Time time) {  
        final int flags = DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_24HOUR;  
        String contentDescription = DateUtils.formatDateTime(getContext(),  
                time.toMillis(false), flags);  
        setContentDescription(contentDescription);  
    }

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		
	}  
  
    /** 
     * 获取当前星期 
     *  
     * @param week 
     * @return 
     */  
  /* private String getWeek(int week) {  
        switch (week) {  
        case 1:  
            return this.getContext().getString(R.string.monday);  
        case 2:  
            return this.getContext().getString(R.string.tuesday);  
        case 3:  
            return this.getContext().getString(R.string.wednesday);  
        case 4:  
            return this.getContext().getString(R.string.thursday);  
        case 5:  
            return this.getContext().getString(R.string.friday);  
        case 6:  
            return this.getContext().getString(R.string.saturday);  
        case 0:  
            return this.getContext().getString(R.string.sunday);  
        default:  
            return "";  
        }  
   }*/

}