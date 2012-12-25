package edu.nouri.photoReX.recommender;

import edu.nouri.photoReX.RecommendationTask;

public class RecommenderThread extends Thread {
	public RecommendationTask task; 		//the task waiting to be run 
	public String username; 
	public int	howMany; 
	
	public Recommender recommender; 
	
	
	public RecommenderThread(Recommender r, RecommendationTask t, String user, int h)
	{
		recommender = r; 
		task = t; 
		username = user; 
		howMany = h; 
	}
	
	
	
	//individual recommender don't need to implement this 
	public void run()
	{
		recommender.execute(username, howMany, task); 
		
		synchronized (recommender.outstandingJobs) { 
			recommender.outstandingJobs.remove(task); 
		}
	}
	
	
	
	
	
	
}
