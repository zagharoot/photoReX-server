package edu.nouri.photoReX;

import edu.nouri.photoReX.picture.PictureInfo;

//import edu.nouri.photoReX.recommender.Recommender; 

/*
 * This class represents one recommendation from a specific recommender for a specific user
 */

public class RecommendationInfo implements Comparable<RecommendationInfo> {

	public PictureInfo picture;				//link to the picture info 
	public String username; 		
//	public Recommender recommender; 
	public double score; 				//the confidence parameter

	
public int compareTo(RecommendationInfo rhs) 
{
	
	double rs = ((RecommendationInfo) rhs).score; 
	
	if (score < rs)
		return -1; 
	else if ( score > rs)
		return 1; 
	else
		return 0; 
	
}

}
