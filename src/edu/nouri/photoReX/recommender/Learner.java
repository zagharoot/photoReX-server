package edu.nouri.photoReX.recommender;

import java.util.*; 

import edu.nouri.photoReX.*;


/*
 * This is the class that aggregates the recommendations from individual experts and combines them into one
 * 
 */


public class Learner {

	protected ArrayList<Recommender> recommenders=new ArrayList<Recommender>(); 
	public int EXPERT_TO_RECOMMEND_RATIO=3; 		//how many times the required pictures to ask from experts
	public int PICTURES_PER_PAGE=12; 				//how many pictures in one page 

	public Learner()
	{
		FlickrRandomRecommender frr = new FlickrRandomRecommender(); 
		recommenders.add(frr); 
		
	}
	
	
	/*
	 * Loads learners from [somewhere]
	 */
	public void load()
	{
		
	}
	
	
	// Saves learners to [somewhere]
	public void save()
	{
		
	}
	
	
	public ArrayList<RecommendationPage> recommend(RecommendationTask task)
	{
		ArrayList<RecommendationPage> result = new ArrayList<RecommendationPage>(); 
		
		int totalPics = EXPERT_TO_RECOMMEND_RATIO* PICTURES_PER_PAGE * task.pageCount; 
		int perLearner =  (int) java.lang.Math.ceil(totalPics/(double)recommenders.size()); 
		
		ArrayList<RecommendationInfo> allPics = new ArrayList<RecommendationInfo>(); 
		
		for(int i=0; i< recommenders.size(); i++)
		{
			ArrayList<RecommendationInfo> pics = recommenders.get(i).recommend(task.username, perLearner); 
			allPics.addAll(pics); 
		}

		
		//now put everything in pages 
		int stIndex = 0; 
		for(int i=0; i< task.pageCount; i++)
		{
			RecommendationPage rp = new RecommendationPage();		//the pageid field is generated later by server obj
			rp.pics = new ArrayList<RecommendationInfo>(); 
			for(int j=0; j< PICTURES_PER_PAGE; j++)
				rp.pics.add(allPics.get(stIndex+j)); 

			stIndex += PICTURES_PER_PAGE; 
			
			result.add(rp); 
		}
		
		return result; 
	}
	
	
}
