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
		//mandatory fields 
		result.id = json.getString("id"); 
		result.url = json.getString("image_url"); 
		result.url = result.url.substring(0,  result.url.lastIndexOf("/")+1);

		result.hash = result.toHash(); 	// hash only uses id and url 

		//optional fields 
		result.name = jsonGetString(json, "name"); 
		result.description = jsonGetString(json, "description"); 
		
		result.timesViewed = jsonGetInt(json, "times_viewed"); 
		result.rating = jsonGetDouble(json, "rating"); 
		result.category = jsonGetInt(json, "category"); 
		result.width =  jsonGetInt(json, "width"); 
		result.height = jsonGetInt(json, "height"); 
		result.voteCount = jsonGetInt(json, "votes_count"); 
		result.favoriteCount = jsonGetInt(json, "favorites_count"); 
		result.commentCount = jsonGetInt(json, "comments_count"); 
		
		//things about the author
		JSONObject user = json.getJSONObject("user"); 
		result.userFullName = jsonGetString(user, "fullname"); 
		result.userid = jsonGetString(user, "id"); 
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
		//mandatory fields 
		result.id = json.getString("id"); 
		result.url = json.getString("url"); 
		result.hash = json.getString("hash"); 

		//optional fields 
		result.name = jsonGetString(json, "name"); 
		result.description = jsonGetString(json, "description"); 
		
		result.timesViewed = jsonGetInt(json, "timesViewed"); 
		result.rating = jsonGetDouble(json, "rating"); 
		result.category = jsonGetInt(json, "category"); 
		result.width =  jsonGetInt(json, "width"); 
		result.height = jsonGetInt(json, "height"); 
		result.voteCount = jsonGetInt(json, "voteCount"); 
		result.favoriteCount = jsonGetInt(json, "favoriteCount"); 
		result.commentCount = jsonGetInt(json, "commentCount"); 
		
		result.userFullName = jsonGetString(json, "userFullName"); 
		result.userid = jsonGetString(json, "userid"); 
	}
	catch(Exception e)
	{
		return null; 
	}
	
	return result; 
	
}

private static String jsonGetString(JSONObject json, String arg)
{
	String result = ""; 
	try{
		result = json.getString(arg); 
	}catch(Exception e)
	{
	}
	
	return result; 
}

private static int jsonGetInt(JSONObject json, String arg)
{
	int result = 0; 
	try{
		result = json.getInt(arg); 
	}
	catch(Exception e)
	{
		
	}
	return result; 
}

private static double jsonGetDouble(JSONObject json, String arg)
{
	double result = 0; 
	try{
		result = json.getDouble(arg); 
	}catch(Exception e)
	{
		
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
