package edu.nouri.photoReX.recommender;

import java.util.*; 

import edu.nouri.photoReX.*;

/*
 * This is the class that aggregates the recommendations from individual experts and combines them into one
 * 
 * The operation of a recommendation happens this way: 
 * 1- Server retrieves the task in the main thread and calls Learner.recommend
 * 2- Learner creates a new thread, so that it doesn't block the main thread. the recommend thread 
 * returns and learnerThread object continues in the new thread. Similar thing happens between learner and recommenders 
 * 3- Learner calls recommend on each of its recommenders. Also, it creates a counting latch and blocks its thread, waiting 
 * for recommenders to finish. 
 * 4- Each recommender, creates a new thread and constructs the recommendation, then calls the learner (which is the delegate). 
 * Each call (made in recommender thread) decreases the latch count, and the last one wakes up the learner thread. 
 * 5- Learner thread combines the results and calls Server (which is the delegate)
 * 
 */


public class Learner implements RecommenderDelegate{

	protected ArrayList<Recommender> recommenders=new ArrayList<Recommender>(); 
	public int EXPERT_TO_RECOMMEND_RATIO=3; 		//how many times the required pictures to ask from experts
	public int PICTURES_PER_PAGE=12; 				//how many pictures in one page 

	public LearnerDelegate delegate; 
	
	public HashMap<RecommendationTask, LearnerThread> outstandingJobs;
	

	public Learner(LearnerDelegate del)
	{
		delegate = del; 
		
		FlickrRandomRecommender frr = new FlickrRandomRecommender(this); 
		recommenders.add(frr); 

		FHPFeaturedRecommender fhr = new FHPFeaturedRecommender("editors",this); 
		recommenders.add(fhr); 
		
		FHPFeaturedRecommender fhrr = new FHPFeaturedRecommender("popular", this); 
		recommenders.add(fhrr); 

		outstandingJobs = new HashMap<RecommendationTask,LearnerThread> (); 
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
	
	// This method is called from the main thread by the server 
	public void recommend(RecommendationTask task)
	{
		LearnerThread t = new LearnerThread(this, task);
		
		outstandingJobs.put(task, t); 
		t.start(); 
		
	}
	
	public void recommendationDidComplete(Recommender recommender, RecommendationTask task, ArrayList<RecommendationInfo> recomm)
	{
		if (recomm == null)
			return; 

		
//		System.out.println("recommender " + recommender.name() + " finished"); 

		int perPage = recomm.size() /  task.pageCount ; 
		int k=0; 
	
		//distribute these picture among all the pages 
		for (int i=0; i< task.recomms.size();i++)
			for(int j=0; j< perPage; j++,k++)
				task.recomms.get(i).pics.add(recomm.get(k)); 

		//remove recommender from outstanding list 
		task.outstandingRecommenders.remove(recommender); 
	
		//update the latch for this task 
		LearnerThread t = outstandingJobs.get(task); 
		if (t == null) 
			System.out.println("learnerthread is null"); 
				
		t.latch.countDown(); 
//		outstandingJobs.remove(task); 
	}

}
