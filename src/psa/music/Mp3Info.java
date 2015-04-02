package psa.music;

public class Mp3Info {
	private long id;
	private long albumId;
	private String title;
	private String album;
	private String musicArtist;
	private String url;
	
	public Mp3Info(){
		super();
	}
	
	public Mp3Info(long id,String title,String album,long albumId,String musicArtist,String url){
		this.id = id;
		this.title = title;
		this.album = album;
		this.albumId = albumId;
		this.musicArtist = musicArtist;
		this.url = url;
	}
	
	@Override
	public String toString(){
		return "Mp3Info [id=" + id + ", title=" + title + ", album=" + album
				+ ", albumId=" + albumId + ", artist=" + musicArtist + ", url=" + url + "]";

		
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String album) {
		this.album = album;
	}

	public long getAlbumId() {
		return albumId;
	}

	public void setAlbumId(long albumId) {
		this.albumId = albumId;
	}

	public String getArtist() {
		return musicArtist;
	}

	public void setArtist(String artist) {
		this.musicArtist = artist;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}