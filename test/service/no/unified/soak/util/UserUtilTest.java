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
}
