package edu.nouri.photoReX.recommender;

import edu.nouri.photoReX.*;
import java.util.*; 

/*
 * This class is an abstract representation of a single expert for picture recommendation
 */

public abstract class Recommender {

	
	public Recommender(){
		
	}
	
	
	public void load()
	{
		
	}
	
	
	public void save()
	{
		
	}
	
	
	/*
	 * Given a username and number of required recommendations, return a set of recommendations 
	 */
	public abstract ArrayList<RecommendationInfo> recommend(String username, int howMany); 
	
	
}
