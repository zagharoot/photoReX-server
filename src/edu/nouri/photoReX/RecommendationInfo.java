package edu.nouri.photoReX;

//import edu.nouri.photoReX.recommender.Recommender; 

/*
 * This class represents one recommendation from a specific recommender for a specific user
 */

public class RecommendationInfo {

	public PictureInfo picture;				//link to the picture info 
	public String username; 		
//	public Recommender recommender; 
	public double score; 				//the confidence parameter
}
