package edu.nouri.photoReX.Picture;

import us.monoid.json.JSONObject;

public class FiveHundredPXPhoto {

	
public String id;
public String url; 
public String name; 
public String description; 

public int timesViewed; 
public double rating; 
public int category; 			//the meaning of this can be found at 500px.com 
public int width;  				//donno if this is usefull at all
public int height; 			
public int voteCount; 	
public int favoriteCount; 
public int commentCount; 

public FiveHundredPXPhoto(JSONObject json)
{
	try{
		id = json.getString("id"); 
		url = json.getString("image_url"); 
		url = url.substring(0,  url.lastIndexOf("/")+1);
		name = json.getString("name"); 
		description = json.getString("description"); 
		
		timesViewed = json.getInt("times_viewed"); 
		rating = json.getDouble("rating"); 
		category = json.getInt("category"); 
		width =  json.getInt("width"); 
		height = json.getInt("height"); 
		voteCount = json.getInt("votes_count"); 
		favoriteCount = json.getInt("favorites_count"); 
		commentCount = json.getInt("comments_count"); 
	}
	catch(Exception e)
	{
		
	}
	
}

public FiveHundredPXPictureInfo pictureInfo()
{
	FiveHundredPXPictureInfo result = new FiveHundredPXPictureInfo(this); 
	
	return result; 
}

public void fill(FiveHundredPXPhoto rhs)
{
	
}

	
}
