package psa.navi;

import java.util.ArrayList;
import java.util.List;

import psa.phone.ImageAdapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import app.psa.R;

public class StarAdapter extends BaseAdapter{

	Context mContext;
	List<String> mList;
	List<String> mData;
	private Typeface mTypeface;
	public StarAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mTypeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");	
		mData = new ArrayList<String>();
		mData.add("300m");
		mData.add("350m");
		mData.add("500m ");
		mData.add("550m ");
		mData.add("950m");
	}
	
	public void setData(List<String> mData){
		mList = mData;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_navi_star,null);
			viewHolder.item = (TextView)convertView.findViewById(R.id.navi_item_title);
			viewHolder.img = (ImageView)convertView.findViewById(R.id.navi_item_star);
			viewHolder.meter = (TextView)convertView.findViewById(R.id.navi_item_meter);
			convertView.setTag(viewHolder);	
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.item.setTextSize(25);
		viewHolder.item.setText(mList.get(position));
		viewHolder.item.setTypeface(mTypeface);
		viewHolder.meter.setTextSize(25);
		viewHolder.meter.setText(mData.get(position));
		viewHolder.meter.setTypeface(mTypeface);
		if (position == 0 || position == 1|| position ==4) {
			viewHolder.img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star5));
				
		}else {
			viewHolder.img.setImageDrawable(mContext.getResources().getDrawable(R.drawable.star4));
				
		}
		return convertView;
	}
	
	class ViewHolder{
		TextView item;
		ImageView img;
		TextView meter;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}
}
