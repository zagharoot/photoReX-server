package edu.nouri.photoReX.recommender;

import java.util.ArrayList;
import edu.nouri.photoReX.dataProvider.*;
import edu.nouri.photoReX.picture.*;

import edu.nouri.photoReX.RecommendationInfo;
import edu.nouri.photoReX.RecommendationTask;

public class FHPFeaturedRecommender extends Recommender {

public String feature; 	//look at 500px website for possible feature values 	
	
	@Override
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

	public FHPFeaturedRecommender(String f, RecommenderDelegate del)
	{
		super(del); 
		
		feature = f; 
	}
	
	
	
	
	
}
