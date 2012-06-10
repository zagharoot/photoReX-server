package edu.nouri.photoReX.recommender;

import java.util.ArrayList;

import edu.nouri.photoReX.RecommendationInfo;
import edu.nouri.photoReX.RecommendationTask;

/*
 *  This class provides a delegate-callback pattern for recommenders. Each recommender has a delegate and informs delegate 
 *  when the recommendation is available 
 */


public interface RecommenderDelegate {
	void recommendationDidComplete(Recommender recommender, RecommendationTask task, ArrayList<RecommendationInfo> recomm); 
}
