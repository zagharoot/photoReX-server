package edu.nouri.photoReX.recommender;

import java.util.ArrayList;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.Response;
import redis.clients.jedis.exceptions.JedisConnectionException;
import us.monoid.json.JSONException;
import us.monoid.json.JSONObject;
import edu.nouri.photoReX.picture.*;

import edu.nouri.photoReX.RecommendationInfo;
import edu.nouri.photoReX.RecommendationTask;

//remember that there's only one object from this class, but execute will be called in parallel so it needs to be thread-safe
public class FHPFeaturedRecommender extends Recommender {

public String feature; 	//look at 500px website for possible feature values 	
	
/*	@Override    //this is the old model. retrieve the data directly from 500px website. 
	public void execute(String username, int howMany, RecommendationTask task) 
	{
		FiveHundredPXDataProvider dp = new FiveHundredPXDataProvider();
		ArrayList<RecommendationInfo> result = new ArrayList<RecommendationInfo>(); 

		int page = 1; 
		while(howMany > 0)
		{
			int perPage = howMany>100?100:howMany; 
			FiveHundredPXPhotoCollection p =  dp.getPictures(feature,perPage,  page++); 
			howMany -= perPage; 
	
			for(int i=0; i< p.pics.size(); i++)
			{
				RecommendationInfo rec = new RecommendationInfo(); 
				rec.picture = p.pics.get(i).pictureInfo(); 
				rec.score = 0.6; 
				
				result.add(rec); 
			}
		}
		
		delegate.recommendationDidComplete(this,  task, result ); 
	}
*/
	
	@Override
	public String name()
	{
		return "FHPFeaturedRecommender:" + feature; 
	}

	//remember that this function might be called in parallel multiple times from multiple threads 
	public void execute(String username, int howMany, RecommendationTask task) 
	{
		Jedis redis = new Jedis("localhost"); 
		ArrayList<RecommendationInfo> result = new ArrayList<RecommendationInfo>(); 

		try{
			//the set that contains pics for the user
			String userSet = "user:" + username + ":FHPFeatured:" + feature + ":tmp"; 
			
			//the set visited by user
			String userVisitedSet = "user:" + username + ":visited"; 
			
			//the set from the crawler 
			String crawlerSet = "crawler:FHPFeatured:" + feature; 
			
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
				JSONObject jsonObj = new JSONObject(jjson); 
				FiveHundredPXPhoto photo = FiveHundredPXPhoto.photoFromJson(jsonObj); 
				if (photo == null)
					continue; 
				rec.picture = new FiveHundredPXPictureInfo(photo); 
				rec.score = 0.6; 
				result.add(rec); 
			}
				
			//voila, result has the results :) 
			delegate.recommendationDidComplete(this, task, result); 

		
		}catch(JedisConnectionException jce)
		{
			delegate.recommendationDidComplete(this, task, null); 
			return; 
		} catch (JSONException e) {
//			e.printStackTrace();
			delegate.recommendationDidComplete(this, task, null); 
			return; 
		}
	}
	
	
	
	public FHPFeaturedRecommender(String f, RecommenderDelegate del)
	{
		super(del); 
		
		feature = f; 
	}
	
	

	@Override
	public boolean handlesProvider(ArrayList<String> providerList)
	{
		//we only handle 500px here 
		for(String p : providerList)
		{
			if (p.compareTo("fiveHundredPXAccount") == 0 )
				return true; 
		}
		
		return false; 
	}
	
	
	
}
