package edu.nouri.photoReX;

import com.gmail.yuyang226.flickr.photos.Photo;


public class FlickrPictureInfo extends PictureInfo {

private String id; 
private String server; 
private String farm; 
private String secret; 
	
	public FlickrPictureInfo()
	{
		super("flickr");
	}

	public FlickrPictureInfo(String _id, String _server, String _farm, String _secret ) 
	{
		this(); 
		id = _id; 
		server =_server; 
		farm = _farm; 
		secret = _secret; 
		
		hash = toHash(); 
	}
	
	
	public FlickrPictureInfo(Photo p)
	{
		this(); 
		
		id = p.getId();
		server = p.getServer(); 
		farm = p.getFarm(); 
		secret = p.getSecret();
		
		hash = toHash(); 
	}
	
	
	public String toString()
	{
		return "flickr:" + id + ":" + server + ":" + farm + ":" + secret; 
		
	}
	
	
}
