package edu.nouri.photoReX.recommender;

import java.util.concurrent.CountDownLatch;



public class LearnerThread extends Thread {

	CountDownLatch latch; 
	
	public LearnerThread(Learner learner) {
		super(learner); 
		
		latch = new CountDownLatch(learner.recommenders.size()); 
		this.setName("Learner Thread"); 
	}

	
	public  void await() {
		try {
			latch.await();
		} catch (Exception e) {
			e.printStackTrace(System.err);
			throw new Error(e);
		}
	}
	
}
