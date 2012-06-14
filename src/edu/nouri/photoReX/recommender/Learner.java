package edu.nouri.photoReX.recommender;

import java.util.*; 

import edu.nouri.photoReX.*;

/*
 * This is the class that aggregates the recommendations from individual experts and combines them into one
 * 
 * The operation of a recommendation happens this way: 
 * 1- Server retrieves the task in the main thread and calls Learner.recommend
 * 2- Learner creates a new thread, so that it doesn't block the main thread. the recommend thread 
 * returns and learner continues in the new thread. Similar thing happens between learner and recommenders 
 * 3- Learner calls recommend on each of its recommenders. Also, it creates a counting latch and blocks its thread, waiting 
 * for recommenders to finish. 
 * 4- Each recommender, creates a new thread and constructs the recommendation, then calls the learner (which is the delegate). 
 * Each call (made in recommender thread) decreases the latch count, and the last one wakes up the learner thread. 
 * 5- Learner thread combines the results and calls Server (which is the delegate)
 * 
 */


public class Learner implements RecommenderDelegate, Runnable{

	protected ArrayList<Recommender> recommenders=new ArrayList<Recommender>(); 
	public int EXPERT_TO_RECOMMEND_RATIO=3; 		//how many times the required pictures to ask from experts
	public int PICTURES_PER_PAGE=12; 				//how many pictures in one page 

	public LearnerDelegate delegate; 
	
	public HashMap<RecommendationTask, LearnerThread> outstandingJobs;
	public RecommendationTask prerunTask; 						//the job that hasn't been run on any thread 
	

	public Learner(LearnerDelegate del)
	{
		delegate = del; 
		
		FlickrRandomRecommender frr = new FlickrRandomRecommender(this); 
		recommenders.add(frr); 

		FHPFeaturedRecommender fhr = new FHPFeaturedRecommender("editors",this); 
		recommenders.add(fhr); 
		
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
		this.prerunTask = task; 
		
		LearnerThread t = new LearnerThread(this);
		
		outstandingJobs.put(task, t); 
		t.start(); 
		
/*		ArrayList<RecommendationPage> result = new ArrayList<RecommendationPage>(); 
		
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
*/
		
	}
	
	public void recommendationDidComplete(Recommender recommender, RecommendationTask task, ArrayList<RecommendationInfo> recomm)
	{
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
		t.latch.countDown(); 
//		outstandingJobs.remove(task); 
		
		
	}


	/*
	 * This is actually the remainder of the recommend method. Now, being on another thread we continue to do the 
	 * actual job. The recommendation task is located in the prerunTask obj, so we use that to spawn the jobs to recommenders
	 * @see java.lang.Runnable#run()
	 */
	public void run() 
	{
		RecommendationTask task = prerunTask; 
		int totalPics = EXPERT_TO_RECOMMEND_RATIO* PICTURES_PER_PAGE * task.pageCount; 
		int perLearner =  (int) java.lang.Math.ceil(totalPics/(double)recommenders.size()); 
		
		for(int i=0; i< recommenders.size(); i++)
		{
			Recommender r = recommenders.get(i); 
			task.outstandingRecommenders.add(r); 		//add this recommender to the outstanding list
			r.recommend(task.username, perLearner, task); 
		}
		
		//we should now wait till all the recommenders have finished. 
		LearnerThread t = outstandingJobs.get(task); 
		t.await(); 
		

		//-----------we have all the recommendations from recommenders
		
		//randomly select a subset for each page
		//distribute these picture among all the pages 
		for (int i=0; i< task.recomms.size();i++)
			while(task.recomms.get(i).pics.size() > PICTURES_PER_PAGE)
			{
				int pos = (int)(Math.random()*task.recomms.get(i).pics.size()-1);
				task.recomms.get(i).pics.remove(pos); 
			}
		
		
		//we are all done, notify our own delegate 
		if (task.outstandingRecommenders.isEmpty())
		{
			outstandingJobs.remove(task); 
			delegate.recommendationDidComplete(this,  task); 
		}		
	}
}
