package edu.nouri.photoReX.recommender;

import java.io.IOException;
import java.util.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;

import org.json.JSONException;

import com.gmail.yuyang226.flickr.*;
import com.gmail.yuyang226.flickr.photos.*;

import com.gmail.yuyang226.flickr.interestingness.*;

import edu.nouri.photoReX.*; 
import edu.nouri.photoReX.picture.FlickrPictureInfo;


public class FlickrRandomRecommender extends Recommender {

	private Flickr flickr; 
	InterestingnessInterface interestingInterface; 
	
	
	public FlickrRandomRecommender(RecommenderDelegate del)
	{
		super(del); 
		flickr = new Flickr("945d26e355114ac74c1366d828aadb5e"); 
		
		this.interestingInterface = flickr.getInterestingnessInterface(); 
	}
	
	
	public void execute(String username, int howMany, RecommendationTask task)
	{

 		ArrayList<RecommendationInfo> result = new ArrayList<RecommendationInfo>(); 
 
		   try {
//				PhotoList pl = 	this.interestingInterface.getList();
				PhotoList pl = 	this.interestingInterface.getList((String) null, Extras.ALL_EXTRAS, howMany, 1);
				
				Iterator <Photo> it = pl.iterator(); 

				while (it.hasNext() && result.size()<howMany)
				{
					Photo p = it.next(); 
					FlickrPictureInfo pinfo = new FlickrPictureInfo(p); 
					RecommendationInfo rinfo = new RecommendationInfo(); 
					rinfo.picture = pinfo ; 
					rinfo.username = username; 
					rinfo.score = 0.5; 			//default :) 
				//	rinfo.recommender = this; 
					
					result.add(rinfo); 
				}
						
				delegate.recommendationDidComplete(this, task, result); 
				
			} catch (InvalidKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				delegate.recommendationDidComplete(this, task, null); 
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				delegate.recommendationDidComplete(this, task, null); 
			} catch (FlickrException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				delegate.recommendationDidComplete(this, task, null); 
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				delegate.recommendationDidComplete(this, task, null); 
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				delegate.recommendationDidComplete(this, task, null);
			} 
	}
}
