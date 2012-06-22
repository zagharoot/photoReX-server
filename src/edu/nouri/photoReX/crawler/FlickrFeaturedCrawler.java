package edu.nouri.photoReX.crawler;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

import org.json.JSONException;

import com.gmail.yuyang226.flickr.Flickr;
import com.gmail.yuyang226.flickr.FlickrException;
import com.gmail.yuyang226.flickr.interestingness.InterestingnessInterface;
import com.gmail.yuyang226.flickr.photos.Extras;
import com.gmail.yuyang226.flickr.photos.Photo;
import com.gmail.yuyang226.flickr.photos.PhotoList;

import edu.nouri.photoReX.picture.FlickrPhoto;
import edu.nouri.photoReX.picture.PictureInfo;

public class FlickrFeaturedCrawler extends Crawler {
	
	private final int howMany=500; 
	private Flickr flickr; 
	InterestingnessInterface interestingInterface; 

	
	public FlickrFeaturedCrawler()
	{
		redisSetName = "crawler:flickrFeatured"; 
		flickr = new Flickr("945d26e355114ac74c1366d828aadb5e"); 		
		this.interestingInterface = flickr.getInterestingnessInterface(); 
	}
	
	
	@Override
	public ArrayList<PictureInfo> getFromWebsite() 
	{
 		ArrayList<PictureInfo> result = new ArrayList<PictureInfo>(); 
 		 
		   try {
				PhotoList pl = 	this.interestingInterface.getList((String) null, Extras.ALL_EXTRAS, howMany, 1);
				
				Iterator <Photo> it = pl.iterator(); 

				while (it.hasNext() && result.size()<howMany)
				{
					Photo p = it.next(); 
					FlickrPhoto photo = new FlickrPhoto(p); 
					
//					System.out.println("The picture I got is: " + photo.toJson()); 
					
					result.add(photo); 
				}
				
				return result; 
			} catch (JSONException e) {
				e.printStackTrace();
			} catch (InvalidKeyException e) {
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				e.printStackTrace();
			} catch (FlickrException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} 
		   
		   return null; 
	}
}
