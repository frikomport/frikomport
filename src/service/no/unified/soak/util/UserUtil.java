package no.unified.soak.util;

import no.unified.soak.model.User;

public class UserUtil {
    
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
