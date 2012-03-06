package edu.nouri.photoReX;

import com.gmail.yuyang226.flickr.photos.Photo;


public class FlickrPictureInfo extends PictureInfo {

public String id; 
public String server; 
public String farm; 
public String secret; 
	
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
	}
	
	
	public FlickrPictureInfo(Photo p)
	{
		this(); 
		
		id = p.getId();
		server = p.getServer(); 
		farm = p.getFarm(); 
		secret = p.getSecret(); 
	}
	
	
}
