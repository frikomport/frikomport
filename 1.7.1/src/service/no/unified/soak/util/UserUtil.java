package no.unified.soak.util;

import no.unified.soak.model.User;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class UserUtil {
    private final static Log log = LogFactory.getLog(UserUtil.class);
    
    public static User transformEmail(User user, String newDomain) {
        String useremail = user.getEmail();
        if(useremail != null || "".equals(useremail)) {
            useremail=user.getUsername();
        }
        int index = useremail.indexOf('@');
        user.setEmail(useremail.substring(0,index<0?useremail.length():index) + newDomain);
        return user;
    }
}
