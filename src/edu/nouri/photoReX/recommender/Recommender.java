package edu.nouri.photoReX.recommender;

import edu.nouri.photoReX.*;

import java.util.*; 

/*
 * This class is an abstract representation of a single expert for picture recommendation
 */

public abstract class Recommender implements Runnable  {

	
	public RecommenderDelegate delegate; 
	public HashMap<RecommendationTask, Thread> outstandingJobs; 

	protected RecommendationTask prerunTask; 		//the task waiting to be run 
	protected String prerunUsername; 
	protected int	prerunHowMany; 
	
	public Recommender(RecommenderDelegate del){
		delegate = del; 
		
		outstandingJobs = new HashMap<RecommendationTask, Thread>(); 
	}
	
	
	public void load()
	{
		
	}
	
	
	public void save()
	{
		
	}
	
	
	/*
	 * Given a username and number of required recommendations, return a set of recommendations. This method 
	 * returns instantly and the result is actually returned by calling 
	 * the delegate method after done. Remember that this function is not thread safe, it should always be called 
	 * from one thread. The reason is the assignment to prerunTask  
	 */
	public Thread recommend(String username, int howMany, RecommendationTask task)
	{
		prerunUsername = username; 
		prerunHowMany = howMany; 
		prerunTask = task; 
		
		Thread t = new Thread(this); 
		t.setName(this.getClass().getSimpleName()); 
		
		outstandingJobs.put(task,  t); 
		t.start(); 
		return t; 
	}
	
	//internal method that does recommendation (each recommender has one of these)
	protected abstract void execute(String username, int howMany, RecommendationTask task); 
	
	//individual recommender don't need to implement this 
	public void run()
	{
		String username = prerunUsername; 
		int howMany = prerunHowMany; 
		RecommendationTask task = prerunTask; 
		this.execute(username, howMany, task); 
		
		outstandingJobs.remove(task); 
	}
	
	
}
