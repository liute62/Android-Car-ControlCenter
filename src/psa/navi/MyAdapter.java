package psa.navi;

import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;
import app.psa.R;

public class MyAdapter extends BaseAdapter{

	Context mContext;
	List<String> mList;
	private Typeface mTypeface;
	public MyAdapter(Context context) {
		// TODO Auto-generated constructor stub
		mContext = context;
		mTypeface = Typeface.createFromAsset(context.getAssets(),
				"fonts/SourceHanSansCN-ExtraLight.ttf");	
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
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_listview_navi,null);
			viewHolder.item = (TextView)convertView.findViewById(R.id.navi_item);
			convertView.setTag(viewHolder);	
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
		}
		viewHolder.item.setTextSize(25);
		viewHolder.item.setText(mList.get(position));
		viewHolder.item.setTypeface(mTypeface);
		return convertView;
	}
	
	class ViewHolder{
		TextView item;
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
