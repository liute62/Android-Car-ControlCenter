package psa.music;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class MediaUtil {
	//private TextView musicTitle;
	
	private static final Uri albumArtUri = Uri.parse("content://media/external/audio/albumart");
	//Adapter mAdapter;
	public static List<Mp3Info> getMp3Infos(Context context){
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		
		List<Mp3Info> mp3Infos = new ArrayList<Mp3Info>(); //mp3Infos ÊÇÒ»¸ölist
		for(int i=0;i<cursor.getCount();i++)
		{
			cursor.moveToNext();
			Mp3Info mp3Info = new Mp3Info();
			long id = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media._ID));	
			String title = cursor.getString((cursor	
					.getColumnIndex(MediaStore.Audio.Media.TITLE)));
			String musicArtist = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ARTIST)); 
			String album = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.ALBUM));	
			String displayName = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
			long albumId = cursor.getInt(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
			String url = cursor.getString(cursor
					.getColumnIndex(MediaStore.Audio.Media.DATA));
			/*long duration = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.DURATION)); 
			long size = cursor.getLong(cursor
					.getColumnIndex(MediaStore.Audio.Media.SIZE));
			;*/
			int isMusic = cursor.getInt(cursor
					.getColumnIndex(MediaStore.Audio.Media.IS_MUSIC));
			
			
			if (isMusic != 0) { 
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArtist(musicArtist);
				mp3Info.setAlbum(album);
				//mp3Info.setDisplayName(displayName);
				mp3Info.setAlbumId(albumId);
				//mp3Info.setDuration(duration);
				//mp3Info.setSize(size);
				mp3Info.setUrl(url);
				mp3Infos.add(mp3Info);
				}
		}
		return mp3Infos;
		
	}
	
	
	public static List<HashMap<String, String>> getMusicMaps(List<Mp3Info> mp3Infos){
		List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Info mp3Info = (Mp3Info) iterator.next();
			HashMap<String, String> map = new HashMap<String, String>();
			map.put("title", mp3Info.getTitle());
			map.put("Artist", mp3Info.getArtist());
			map.put("album", mp3Info.getAlbum());
			//map.put("displayName", mp3Info.getDisplayName());
			map.put("albumId", String.valueOf(mp3Info.getAlbumId()));
			//map.put("duration", formatTime(mp3Info.getDuration()));
			//map.put("size", String.valueOf(mp3Info.getSize()));
			map.put("url", mp3Info.getUrl());
			mp3list.add(map);
		}
		return mp3list;
	
	}
	
	/*public void setListAdpter(List<Mp3Info> mp3Infos) {  
        List<HashMap<String, String>> mp3list = new ArrayList<HashMap<String, String>>();  
        for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {  
            Mp3Info mp3Info = (Mp3Info) iterator.next();  
            HashMap<String, String> map = new HashMap<String, String>();  
            map.put("title", mp3Info.getTitle());  
            map.put("Artist", mp3Info.getArtist());  
            //map.put("duration", String.valueOf(mp3Info.getDuration()));  
            //map.put("size", String.valueOf(mp3Info.getSize()));  
            map.put("url", mp3Info.getUrl());  
            mp3list.add(map);  
        }  
           
    }
	*/
	

}