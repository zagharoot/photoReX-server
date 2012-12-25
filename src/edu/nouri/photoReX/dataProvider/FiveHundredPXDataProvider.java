package edu.nouri.photoReX.dataProvider;

import java.io.IOException;

import us.monoid.json.JSONArray;
import us.monoid.json.JSONObject;
import us.monoid.web.JSONResource;
import us.monoid.web.Resty;
import edu.nouri.photoReX.picture.*;

public class FiveHundredPXDataProvider {

public static final String CONSUMER_KEY = "xqNgbk94AvX7E0RbGEPTFKjMFWbnNSEjOdYOeCU3"; 	

// sample id: 1195521
public FiveHundredPXPhoto getPicture(String id)
{
	//TODO: fill 
	
	String url = "https://api.500px.com/v1/photos/" + id + "?comments=0&comments_page=1&consumer_key=" + CONSUMER_KEY; 
	Resty r = new Resty(); 
	JSONResource js = null; 
	try {
		js = r.json(url);
	} catch (IOException e) {
		e.printStackTrace();
	} 

	JSONObject obj = null; 
	try {
		obj = (JSONObject) js.get("photo");
	} catch (Exception e) {
		e.printStackTrace();
	} 
	
	FiveHundredPXPhoto result = FiveHundredPXPhoto.photoFromWebsiteJson(obj); 
	
	return result; 
}

public void fillPicture(FiveHundredPXPhoto picture)
{
	
	picture.fill(getPicture(picture.id)) ;
}

public FiveHundredPXPhotoCollection getPictures(String feature, int resultPerPage,  int pageNumber)
{
	//website doesn't return more than 100 per page
	if (resultPerPage>100)
		resultPerPage = 100; 

	String url = "https://api.500px.com/v1/photos?feature=" + feature + "&rpp=" + resultPerPage +  "&page=" + pageNumber + "&consumer_key=" + CONSUMER_KEY; 
	Resty r = new Resty(); 
	JSONResource js = null; 
	try {
		js = r.json(url);
	} catch (IOException e) {
		e.printStackTrace();
	} 

	JSONArray obj = null; 
	try {
		obj = (JSONArray) js.get("photos");
	} catch (Exception e) {
		e.printStackTrace();
	} 
	
	FiveHundredPXPhotoCollection result = new FiveHundredPXPhotoCollection(); 
	for(int i=0; i< obj.length(); i++)
	{
		try{
			JSONObject po = obj.getJSONObject(i); 
			FiveHundredPXPhoto p = FiveHundredPXPhoto.photoFromWebsiteJson(po); 
			result.pics.add(p); 
		}
		catch(Exception e)
		{
			
		}
	}
	
	return result; 
}

}



