package no.unified.soak.util;

import junit.framework.TestCase;
import no.unified.soak.model.User;

public class UserUtilTest extends TestCase{
    User user;
    String email = "test";
    
    public void testTransformEmail() {
        User user = new User();
        user.setEmail(email);
        user.setUsername(email);
        
        assertEquals(email, user.getEmail());
        user = UserUtil.transformEmail(user, "@nonexist.no");
        assertEquals(email + "@nonexist.no", user.getEmail());
    }


    public void testUsernameFromStacktrace(){
    	
    	String usernameExpected = "gaborb";
    	String username = null;
	    String stacktrace = "SEVERE: Could not synchronize database state with session" +
	        "org.hibernate.StaleObjectStateException: Row was updated or deleted by another transaction (or unsaved-value mapping was incorrect): [no.unified.soak.model.User#gaborb]" +
            "at org.hibernate.persister.entity.AbstractEntityPersister.check(AbstractEntityPersister.java:1759)" +
	        "at org.hibernate.persister.entity.AbstractEntityPersister.update(AbstractEntityPersister.java:2402)" +
	        "at org.hibernate.persister.entity.AbstractEntityPersister.updateOrInsert(AbstractEntityPersister.java:2302)" +
	        "at org.hibernate.persister.entity.AbstractEntityPersister.update(AbstractEntityPersister.java:2602)" +
	        "at org.hibernate.action.EntityUpdateAction.execute(EntityUpdateAction.java:96)...";

		if(stacktrace.indexOf(".User#") != -1){
	    	int start = stacktrace.indexOf(".User#");
	    	stacktrace = stacktrace.substring(start);
	    	username = stacktrace.substring(".User#".length(), stacktrace.indexOf("]"));
		}

		assertEquals(usernameExpected, username);
    }


}
