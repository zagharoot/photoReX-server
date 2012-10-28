package edu.nouri.photoReX;


import java.util.*; 

import edu.nouri.photoReX.recommender.Recommender;

/*
 * This class contains information about performing one or more recommendations for one user 
 * 
 */

//TODO: write the code for the correct hashing of this (because it's used a lot in hashsets)

public class RecommendationTask {
	
	public String username; 
	public ArrayList<String> providers; 	//list of websites we should collect data from 
	
	public int pageCount;				//number of pages to populate 
	
	public long startTime; 				//marks when the task was initialized in the system 
	public HashSet<Recommender> outstandingRecommenders; 	//recommenders we are waiting on 
	public ArrayList<RecommendationPage > recomms; //this is pages of list of individual recommendations 
	
	
	public RecommendationTask(List<String> l)
	{
		//TODO: input validation test here 
		
		String str = l.get(1); 
		String [] strs = str.split(":"); 
		username = strs[0]; 
		pageCount = Integer.parseInt(strs[1]); 	
		
		
		//initialize local variables
		startTime = System.currentTimeMillis(); 						//mark the start of task 
		outstandingRecommenders = new HashSet<Recommender>(); 

		// initialize the recommendations
		recomms = new ArrayList< RecommendationPage > (); 
		for(int i=0; i<pageCount; i++)
			recomms.add( new RecommendationPage()); 
		
	}
	
	
}
