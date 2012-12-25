package edu.nouri.photoReX.recommender;

import edu.nouri.photoReX.*;

import java.util.*; 

/*
 * This class is an abstract representation of a single expert for picture recommendation
 */

public abstract class Recommender   {

	
	public RecommenderDelegate delegate; 
	public HashMap<RecommendationTask, Thread> outstandingJobs; 


	
	public Recommender(RecommenderDelegate del){
		delegate = del; 
		
		outstandingJobs = new HashMap<RecommendationTask, Thread>(); 
	}
	
	//given a list of providers, is there any provider that this recommender can handle
	public boolean handlesProvider(ArrayList<String> providerList)
	{
		return false; 
	}
	
	
	public void load()
	{
		
	}
	
	
	public void save()
	{
		
	}
	
	//name of the current recommender
	public String name()
	{
		return ""; 
	}
	
	/*
	 * Given a username and number of required recommendations, return a set of recommendations. This method 
	 * returns instantly and the result is actually returned by calling 
	 * the delegate method after done. This function is threadsafe (not now)
	 */

	public Thread recommend(String username, int howMany, RecommendationTask task)
	{
		
		RecommenderThread t = new RecommenderThread(this, task, username, howMany); 
		t.setName(this.name()); 
		
		synchronized(outstandingJobs){
			outstandingJobs.put(task,  t); 
		}

		t.start(); 
		return t; 
	}
	
	//internal method that does recommendation (each recommender has one of these)
	protected abstract void execute(String username, int howMany, RecommendationTask task); 
	
	
	
}
