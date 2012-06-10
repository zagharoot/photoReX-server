package edu.nouri.photoReX.recommender;

import edu.nouri.photoReX.*; 

/*
 * Learner delegates are notified whenever the recommendation task for a username is fully constructed 
 */


public interface LearnerDelegate {
	
	void recommendationDidComplete(Learner sender, RecommendationTask task);
}
