package edu.nouri.photoReX.recommender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import edu.nouri.photoReX.RecommendationTask;



public class LearnerThread extends Thread {
	public Learner learner; 				// pointer to the Learner object (which is only one for all threads)
	CountDownLatch latch; 
	RecommendationTask task; 
	
	public LearnerThread(Learner learner, RecommendationTask task) {
		this.learner = learner; 
		this.task = task; 
		
		this.setName("Learner Thread"); 
	}

	
	public  void await() {
		try {
//			latch.await();
			latch.await(10, TimeUnit.SECONDS); 		//we wait 10 seconds for recommenders to return sth 
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new Error(e);
		}
	}
	
	

	/*
	 * This is actually the remainder of the recommend method from Learner class. Now, being on another thread we continue to do the 
	 * actual job. The recommendation task is located in the prerunTask obj, so we use that to spawn the jobs to recommenders
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() 
	{
		if (learner == null)
			throw new NullPointerException("learner object is null in learner thread"); 
		
		
		//first decide how many of our recommenders can work on this task based on the desired providers
		ArrayList<Recommender> goodProviders = new ArrayList<Recommender>(); 
		for(Recommender r: learner.recommenders)
		{
			if (r.handlesProvider(task.providers))
				goodProviders.add(r); 
		}
		
		//create the latch for number of good providers 
		latch = new CountDownLatch(goodProviders.size()); 
		
		
		
		int totalPics = learner.EXPERT_TO_RECOMMEND_RATIO* learner.PICTURES_PER_PAGE * task.pageCount; 
		int perLearner =  (int) java.lang.Math.ceil(totalPics/(double)goodProviders.size()); 
		
		for (Recommender r: goodProviders)
		{
//			System.out.println("recommender " + r.name() + " is starting"); 
			task.outstandingRecommenders.add(r); 		//add this recommender to the outstanding list
			r.recommend(task.username, perLearner, task); 
		}
	
		//we should now wait till all the recommenders have finished. 
		await(); 
		

		//-----------we have all the recommendations from recommenders

		for (int i=0; i< task.recomms.size();i++)		//for each recomm page 
		{

/*			//randomly select a subset for each page
			//distribute these picture among all the pages 
			while(task.recomms.get(i).pics.size() > PICTURES_PER_PAGE)
			{
				int pos = (int)(Math.random()*task.recomms.get(i).pics.size()-1);
				task.recomms.get(i).pics.remove(pos); 
				
			}
*/
			
			Collections.sort(task.recomms.get(i).pics); 
//			ArrayList<RecommendationInfo> gholi = task.recomms.get(i).pics; 
			while(task.recomms.get(i).pics.size() > learner.PICTURES_PER_PAGE)
			{
				task.recomms.get(i).pics.remove(0); 
			}
		}
		
		
		//we are all done, notify our own delegate 
		if (task.outstandingRecommenders.isEmpty())
		{
			learner.outstandingJobs.remove(task); 
			learner.delegate.recommendationDidComplete(learner,  task); 
		}		
	}
	
	
	
}
