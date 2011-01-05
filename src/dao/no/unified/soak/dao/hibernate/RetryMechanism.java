package no.unified.soak.dao.hibernate;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.StaleObjectStateException;
import org.springframework.orm.hibernate3.HibernateOptimisticLockingFailureException;

public class RetryMechanism {
	protected static final Log log = LogFactory.getLog(RetryMechanism.class);
	
	//Default access for junit.
	public static interface OperationInterface<T, U> {
		T operate(U param);
	}

	/**
	 * Executes a given operation. If it fails due to stale object state, retry
	 * in various fashions. Only throw an exception if nothing works. Protected
	 * for JUnit.
	 */
	public static final <T,U> T executeOperationWithRetriesOnStaleObjectState(OperationInterface<T,U> operation, U parameter) {
		T result = null;
    	try {
    		result = operation.operate(parameter);
    	} catch (RuntimeException re) {
    		log.warn("Exception performing operation. Will look for stale object state and possibly retry.", re);
    		
	    	Throwable loopy = re;
	    	outer: while (loopy != null) {
	    		if (loopy instanceof StaleObjectStateException || loopy instanceof HibernateOptimisticLockingFailureException) {
	    			//Retry MAY work.
	    			for (int i =1; i < 4; i++) {
						try {
							Thread.sleep(100); //Short sleep in order to give time between retries.
							result = operation.operate(parameter);
							break outer;
						} catch (Exception e) {
							log.warn("Got exception during attempt "
									+i
									+" at retry due to stale object state."
									+" Original exception has been logged above at the warn level.", e);
						}
	    			}
	    		}
	    		loopy = loopy.getCause();
			}
	    	if (loopy == null) {
	    		log.error("Retry mechanism failed or no retryable situation was found for the following exception:", re);
	    		//Will only happen if we didn't break outer, thus there either was no 
	    		//stale object state or all retries failed.
	    		throw new RuntimeException("Retry mechanism failed or no retryable situation was found for the following exception:", re);
	    	}
    	}
    	return result;
    }
}
