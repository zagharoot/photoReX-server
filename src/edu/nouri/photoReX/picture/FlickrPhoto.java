package edu.nouri.photoReX.picture;

import com.google.gson.Gson;

// This is basically a wrapper around Flickr Photo class. I wanted it to have the same structure as 500Px photos to make it easier to work with multiple websites

public class FlickrPhoto extends FlickrPictureInfo {
	public com.gmail.yuyang226.flickr.photos.Photo thePhoto; 

	
	public FlickrPhoto(com.gmail.yuyang226.flickr.photos.Photo p)
	{
		super(); 
		
		this.thePhoto = p; 
		
		this.id = p.getId(); 
		this.farm = p.getFarm(); 
		this.server = p.getServer(); 
		this.secret = p.getSecret(); 
		
		this.hash = toHash(); 
	}
	
	//creates an object using the json in the redis 
	public static FlickrPhoto photoFromJson(String json)
	{
		FlickrPhoto result; 
		try{
			Gson gson = new Gson();
			result  =  gson.fromJson(json, FlickrPhoto.class);
		}
		catch(Exception e)
		{
			return null; 
		}
		return result; 
	}
	
	
	public FlickrPictureInfo pictureInfo()
	{
		return new FlickrPictureInfo(this); 
	}

}
