/**
 * 
 */
package no.unified.soak.dao.ws;

import java.util.List;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;

/**
 * Class for fetching user info at SVV with no possibility of fetching 1) users based on role or 2) fetching roles or 3)
 * fatching all users.
 * 
 * @author kst
 * 
 */
public class SVVUserDAOWS implements ExtUserDAO {

    UserDAO userDAO;
    
    public void setUserDAO(UserDAO dao) {
        this.userDAO = dao;
    }

    
    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findAll()
     */
    @Override
    public List<ExtUser> findAll() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findRoles()
     */
    @Override
    public List<String> findRoles() {
        // TODO Auto-generated method stub
        return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findUserBySessionID(java.lang.String)
     */
    @Override
    public ExtUser findUserBySessionID(String sessionId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException("findUserBySessionID(sessionId) is unsupported in SVV environment. Use findUserByUsername(username) instead.");
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findUsers(java.util.List)
     */
    @Override
    public List<ExtUser> findUsers(List<String> roles) {
        // TODO Auto-generated method stub
        return null;
    }

}
