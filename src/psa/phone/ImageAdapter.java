package psa.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView.ScaleType;
import app.psa.R;
import app.psa.Utils;

public class ImageAdapter extends BaseAdapter 
{
	//private ImageView[] mImages;		// ���浹ӰͼƬ������

	private Context mContext;
	public List<Map<String, Object>> list;
	

	public int[] imgs = Utils.phone_photo;
	public String[] titles = Utils.phone_name;
	public List<Integer> selected = new ArrayList<Integer>();
	
	Bitmap bitmapWithReflection;
	int width;
	int height;
	float scale;
	int mheight;
	final int reflectionGap = 0;
	
	public  View mConvertView;
	List<ViewHolder> viewHolders = new ArrayList<ImageAdapter.ViewHolder>();
	
	public ImageAdapter(Context c) 
	{
		this.mContext = c;
		list = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < imgs.length; i++) {
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("image", imgs[i]);
			list.add(map);
		}
	   for (int i = 0; i < titles.length; i++) {
		   if (i == 0) {
			   selected.add(1);
		   }else {
			   selected.add(0);
		   }
	   }
	   viewHolders = new ArrayList<ImageAdapter.ViewHolder>();
	   for (int i = 0; i < titles.length; i++) {
		   ViewHolder viewHolder = new ViewHolder();
		   viewHolders.add(viewHolder);
	   }
	}

	public void setSelected(int pos){
		selected.set(pos, 1);
		for (int i = 0; i < selected.size(); i++) {
			if (i != pos) {
				selected.set(i, 0);
			}
		}
		notifyDataSetChanged();
	}
	
	/** ���䵹Ӱ */
	public boolean createReflectedImages(int id) 
	{
		final int Height = 400;
		int index = 0;
			// ��ȡԭʼͼƬ
			Bitmap originalImage = BitmapFactory.decodeResource(mContext.getResources(), id);	
			width = originalImage.getWidth();
			height = originalImage.getHeight();
			scale = Height / (float)height;
			Matrix sMatrix = new Matrix();
			sMatrix.postScale(scale, scale);
			Bitmap miniBitmap = Bitmap.createBitmap(originalImage, 0, 0,
					originalImage.getWidth(), originalImage.getHeight(), sMatrix, true);
			
			//�Ƿ�ԭͼƬ���ݣ���ʡ�ڴ�
			originalImage.recycle();
			int mwidth = miniBitmap.getWidth();
			mheight = miniBitmap.getHeight();
			Matrix matrix = new Matrix();
			matrix.preScale(1, -1);			// ͼƬ����任���ӵͲ��򶥲��ĵ�Ӱ��
			Bitmap reflectionImage = Bitmap.createBitmap(miniBitmap, 0, mheight/2, mwidth, mheight/2, matrix, false);	// ��ȡԭͼ�°벿��
			bitmapWithReflection = Bitmap.createBitmap(mwidth, (mheight + mheight / 2), Config.ARGB_8888);			// ������ӰͼƬ���߶�Ϊԭͼ3/2��

			Canvas canvas = new Canvas(bitmapWithReflection);	// ���Ƶ�Ӱͼ��ԭͼ + ��� + ��Ӱ��
			canvas.drawBitmap(miniBitmap, 0, 0, null);		// ����ԭͼ
			Paint paint = new Paint();
			//canvas.drawRect(0, mheight, mwidth, mheight + reflectionGap, paint);		// ����ԭͼ�뵹Ӱ�ļ��
			//canvas.drawBitmap(reflectionImage, 0, mheight + reflectionGap, null);	// ���Ƶ�Ӱͼ

			paint = new Paint();
			LinearGradient shader = new LinearGradient(0, miniBitmap.getHeight(), 0, bitmapWithReflection.getHeight()
					+ reflectionGap, 0x70ffffff, 0x00ffffff, TileMode.CLAMP);
			paint.setShader(shader);	// ���Խ���Ч��
			paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));		// ��Ӱ����Ч��
			//canvas.drawRect(0, mheight, mwidth, bitmapWithReflection.getHeight() + reflectionGap, paint);		// ���Ƶ�Ӱ����ӰЧ��

			//ImageView imageView = new ImageView(mContext);
			
		
		return true;
	}

	@Override
	public int getCount() {
		return imgs.length;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder;
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(R.layout.item_contact_profile, null);
			viewHolder.img = (ImageView)convertView.findViewById(R.id.contactprofile_item_img);
			viewHolder.name = (TextView)convertView.findViewById(R.id.contactprofile_item_name);
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder)convertView.getTag();
			
		}
		createReflectedImages(imgs[position]);
		viewHolder.img.setImageBitmap(bitmapWithReflection);		// ���õ�ӰͼƬ
		if (selected.get(position) == 1) {
			viewHolder.img.setAlpha(1f);
			viewHolder.name.setAlpha(1f);
		}
		mConvertView = convertView;
		viewHolder.img.setScaleType(ScaleType.MATRIX);
		viewHolder.name.setText(titles[position]);
		return convertView;		// ��ʾ��ӰͼƬ����ǰ��ȡ���㣩
	}

	/**
	public View getView(int position, View convertView, ViewGroup arg2) {
		ViewHolder viewHolder;
		convertView = LayoutInflater.from(mContext).inflate(
				R.layout.item_contact_profile, null);
		viewHolder = new ViewHolder();
		viewHolder.img = (ImageView) convertView
				.findViewById(R.id.contactprofile_item_img);
		viewHolder.name = (TextView) convertView
				.findViewById(R.id.contactprofile_item_name);
		viewHolders.add(viewHolder);
		createReflectedImages(imgs[position]);
		viewHolder.img.setImageBitmap(bitmapWithReflection); // ���õ�ӰͼƬ
		// viewHolder.img.setLayoutParams(new
		// RelativeLayout.LayoutParams((int)(width * scale),
		// (int)(mheight * 3 / 2.0 + reflectionGap)));
		viewHolder.img.setScaleType(ScaleType.MATRIX);
		if (selected.get(position) == 1) {
			viewHolder.img.setAlpha(1f);
			viewHolder.name.setAlpha(1f);
		}
		viewHolder.name.setText(titles[position]);
		mConvertView = convertView;
		Log.e("getView", String.valueOf(viewHolders.size()));
		return convertView; // ��ʾ��ӰͼƬ����ǰ��ȡ���㣩
	}**/

	public ImageView getViewHolderImg(int pos) {
		return viewHolders.get(pos).img;
	}

	public TextView getViewHolderName(int pos) {
		return viewHolders.get(pos).name;
	}

	public void setDefaultViewHolder(int pos) {
		for (int i = 0; i < viewHolders.size(); i++) {
			if (i != pos) {
				if (viewHolders.get(i).img != null) {
					viewHolders.get(i).img.setAlpha(0.3f);
					viewHolders.get(i).name.setAlpha(0.3f);	
					Log.e(String.valueOf(i), "yes");
				}
			}
		}
		Log.e("test", String.valueOf(pos));
	}

	private class ViewHolder {
		ImageView img;
		TextView name;
	}

	public float getScale(boolean focused, int offset) {
		return Math.max(0, 1.0f / (float) Math.pow(2, Math.abs(offset)));
	}

}
