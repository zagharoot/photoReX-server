package edu.nouri.photoReX.recommender;

import java.util.*;


import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;

import com.gmail.yuyang226.flickr.*;

import com.gmail.yuyang226.flickr.interestingness.*;

import edu.nouri.photoReX.*; 
import edu.nouri.photoReX.picture.FlickrPhoto;
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
	
	
	@Override 
	public String name()
	{
		return "FlickrRandomRecommender"; 
	}
	
/* this is the old model where we directly contact flickr to get results back 	
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
					rinfo.score = 0.3; 			//default :) 
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
*/
	
	//this is the new model that uses the results from crawler to construct recomm
	public void execute(String username, int howMany, RecommendationTask task) 
	{
		Jedis redis = new Jedis("localhost"); 
		ArrayList<RecommendationInfo> result = new ArrayList<RecommendationInfo>(); 

		try{
			//the set that contains pics for the user
			String userSet = "user:" + username + ":flickrFeatured:tmp"; 
			
			//the set visited by user
			String userVisitedSet = "user:" + username + ":visited"; 
			
			//the set from the crawler 
			String crawlerSet = "crawler:flickrFeatured"; 
			
			//first see if we already have a set big enough: 
			long currentSetSize = redis.scard(userSet); 
			
			
			if (currentSetSize < howMany)  //need to add to it 
			{
				Pipeline p = redis.pipelined(); 
				p.sdiffstore(userSet,  crawlerSet, userVisitedSet); 
				Response<Long> newSize =  p.scard(userSet); 
				p.sync(); 
				
				Long size = newSize.get(); 
				if (size==null || size==0)		//we have to decide whether to return null if size < howMany
				{
					delegate.recommendationDidComplete(this, task, null); 
					return; 
				}
				
				currentSetSize = size.longValue(); 
			}
			
			//will contain the set of picture hashes as the result
			ArrayList<Response<String> > hashResults = new ArrayList<Response<String> >(); 
			
			Pipeline p = redis.pipelined(); 
			for(int i=0; i< howMany; i++)
			{
				Response<String> res = p.spop(userSet); 
				hashResults.add(res); 
			}
			p.sync(); 
			
			
			//ok, now we need to actually retrieve the json for the images 
			
			ArrayList<Response<String> > imageResults = new ArrayList<Response<String> >(); 
			p = redis.pipelined(); 
			for(Response<String> hash : hashResults)
			{
				
				String picKey = "pic:" + hash.get(); 
				Response<String> res = p.get(picKey); 
				imageResults.add(res); 
			}
			p.sync(); 
			
			
			//now imageResults contains all the jsons for the result set 
			for(Response<String> picJson : imageResults)
			{
				String jjson = picJson.get(); 
				if (jjson==null) continue; 
				
				RecommendationInfo rec = new RecommendationInfo(); 
				FlickrPhoto photo = FlickrPhoto.photoFromJson(jjson); 
				if (photo == null)
					continue; 
				rec.picture = new FlickrPictureInfo(photo); 
				rec.score = 0.6; 
				result.add(rec); 
			}
				
			//voila, result has the results :) 
			delegate.recommendationDidComplete(this, task, result); 

		
		}catch(JedisConnectionException jce)
		{
			delegate.recommendationDidComplete(this, task, null); 
			return; 
		}
	}

	
	@Override
	public boolean handlesProvider(ArrayList<String> providerList)
	{
		//we only handle flickr here 
		for(String p : providerList)
		{
			if (p.compareTo("flickrAccount") == 0 )
				return true; 
		}
		
		return false; 
	}
	
	
}
