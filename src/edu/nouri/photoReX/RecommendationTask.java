package edu.nouri.photoReX;


import java.util.*; 

/*
 * This class contains information about performing one or more recommendations for one user 
 * 
 */

public class RecommendationTask {
	
	public String username; 
	public int pageCount;				//number of pages to populate 
	
	public RecommendationTask(List<String> l)
	{
		//TODO: input validation test here 
		
		String str = l.get(1); 
		String [] strs = str.split(":"); 
		username = strs[0]; 
		pageCount = Integer.parseInt(strs[1]); 	
		
	}
}
