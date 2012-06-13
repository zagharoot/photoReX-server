package edu.nouri.photoReX.picture;

import us.monoid.json.JSONObject;

/*
 * This is a more complete class representing the pictures from 500px. the info here is mostly used by the learners (not sent to client)
 */
public class FiveHundredPXPhoto extends FiveHundredPXPictureInfo {

	
public String name; 

public double rating; 
public int category; 			//the meaning of this can be found at 500px.com 
public int width;  				//donno if this is usefull at all
public int height; 			
public int voteCount; 	
public int favoriteCount; 
public int commentCount; 

// creates an object using the json sent by the 500px website. 
public static FiveHundredPXPhoto photoFromWebsiteJson(JSONObject json)
{
	FiveHundredPXPhoto result = new FiveHundredPXPhoto(); 
	try{
		result.id = json.getString("id"); 
		result.url = json.getString("image_url"); 
		result.url = result.url.substring(0,  result.url.lastIndexOf("/")+1);

		result.hash = result.toHash(); 	// hash only uses id and url 

		result.name = json.getString("name"); 
		result.description = json.getString("description"); 
		
		result.timesViewed = json.getInt("times_viewed"); 
		result.rating = json.getDouble("rating"); 
		result.category = json.getInt("category"); 
		result.width =  json.getInt("width"); 
		result.height = json.getInt("height"); 
		result.voteCount = json.getInt("votes_count"); 
		result.favoriteCount = json.getInt("favorites_count"); 
		result.commentCount = json.getInt("comments_count"); 
		
		//things about the author
		JSONObject user = json.getJSONObject("user"); 
		result.userFullName = user.getString("fullname"); 
		result.userid = user.getString("id"); 
	}
	catch(Exception e)
	{
		return null; 
	}
	
	return result; 
	
}

//creates an object using the json in the redis 
public static FiveHundredPXPhoto photoFromJson(JSONObject json)
{
	FiveHundredPXPhoto result = new FiveHundredPXPhoto(); 
	try{
		
		result.id = json.getString("id"); 
		result.url = json.getString("url"); 
		result.hash = json.getString("hash"); 

		result.name = json.getString("name"); 
		result.description = json.getString("description"); 
		
		result.timesViewed = json.getInt("timesViewed"); 
		result.rating = json.getDouble("rating"); 
		result.category = json.getInt("category"); 
		result.width =  json.getInt("width"); 
		result.height = json.getInt("height"); 
		result.voteCount = json.getInt("voteCount"); 
		result.favoriteCount = json.getInt("favoriteCount"); 
		result.commentCount = json.getInt("commentCount"); 
		
		result.userFullName = json.getString("userFullName"); 
		result.userid = json.getString("userid"); 
	}
	catch(Exception e)
	{
		return null; 
	}
	
	return result; 
	
}




public FiveHundredPXPictureInfo pictureInfo()
{
	return new FiveHundredPXPictureInfo(this); 
}


public void fill(FiveHundredPXPhoto rhs)
{
	
}

	
}
