package edu.nouri.photoReX;

import java.util.*; 
import com.google.gson.Gson; 
/*
 * This represents one page of recommendations to be sent to the user 
 */


public class RecommendationPage {

	public long pageid; 							//page number of this collection 
	public ArrayList<RecommendationInfo> pics; 
	
	
	public String toJson()
	{
		Gson gson = new Gson(); 
		String result = gson.toJson(this); 
		return result; 
	}
}
