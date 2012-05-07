package no.unified.soak.dao.hibernate;

import org.hibernate.StaleObjectStateException;

import junit.framework.TestCase;

public class RetryMechanismTest extends TestCase {

	public void testExecuteOperationWithRetriesOnStaleObjectState() {
		RetryMechanism.OperationInterface<String, String> myOp = new RetryMechanism.OperationInterface<String, String>() {
			int i = 0;
			public String operate(String param) {
				if (1 > i) {
					i++;
					throw new StaleObjectStateException(null, null);
				} else {
					return "Works.";
				}
			}
		};
		
		assertEquals("Works.", RetryMechanism.executeOperationWithRetriesOnStaleObjectState(myOp, null));
		
		myOp = new RetryMechanism.OperationInterface<String, String>() {
			public String operate(String param) {
				throw new StaleObjectStateException(null, null);
			}
		};
		
		try {
			RetryMechanism.executeOperationWithRetriesOnStaleObjectState(myOp, null);
			fail("Expecting exception");
		} catch (Exception e) {
			assertTrue(e instanceof RuntimeException);
			assertTrue(e.getCause() instanceof StaleObjectStateException);
		}
	}

}
