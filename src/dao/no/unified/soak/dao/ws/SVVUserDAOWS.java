/**
 * 
 */
package no.unified.soak.dao.ws;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.RoleDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.dao.fkpuser.FkpUserPortType;
import no.unified.soak.dao.fkpuser.FkpUser_ServiceLocator;
import no.unified.soak.dao.fkpuser.FkpUser_Type;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.util.ConvertDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class for fetching user info at SVV with no possibility of fetching 1) users based on role or 2) fetching roles or 3)
 * fatching all users.
 * 
 * @author kst
 * 
 */
public class SVVUserDAOWS implements ExtUserDAO {

    UserDAO userDAO;
    RoleDAO roleDAO;
    String endpoint = "http://localhost/nusoap.php/fkpuser";
    private transient final Log log = LogFactory.getLog(SVVUserDAOWS.class);

    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private FkpUserPortType getPort() throws ServiceException {
        FkpUser_ServiceLocator locator = new FkpUser_ServiceLocator();
        locator.setEndpointAddress("FkpUserPort", endpoint);
        FkpUserPortType port =  locator.getFkpUserPort();
        return port;
    }
    
    public void setRoleDAO(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public void setUserDAO(UserDAO dao) {
        this.userDAO = dao;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findAll()
     */
    public List<ExtUser> findAll() {
        return findUsers(findRoles());
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findRoles()
     */
    public List<String> findRoles() {
        List roleList = roleDAO.getRoles(null);
        List<String> RoleStringList = new LinkedList<String>();
        for (Iterator iterator = roleList.iterator(); iterator.hasNext();) {
            Role role = (Role) iterator.next();
            RoleStringList.add(role.getName());
        }
        return RoleStringList;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findUsers(java.util.List)
     */
    public List<ExtUser> findUsers(List<String> roles) {
        User searchUser = new User();
        searchUser.addRoles(roles);
        List users = userDAO.getUsers(searchUser);
        List<ExtUser> extUsers = new ArrayList<ExtUser>(users.size());
        
        for (Iterator iterator = users.iterator(); iterator.hasNext();) {
            User user = (User) iterator.next();
            extUsers.add(ConvertDAO.User2ExtUser(user));
        }
        
        return extUsers;
    }

    public ExtUser findUserByUsername(String username) {
        // TODO Klaus: SVV-tjeneste for henting av brukerinfo mangler, dummy foreløpig.
        try {
//            FkpUser_Type user = getPort().getUser(username);
        } catch (Exception e) {
            log.error("Feilet ved henting av user fra webservice", e);
        }
        
        
        //Dummy, før webservice-kall fungerer:
        ExtUser extUser = new ExtUser();
        extUser.setId(14);
        extUser.setEmail("test_@stafto.no");
        extUser.setFirst_name("Tes");
        extUser.setLast_name("Testson");
        extUser.setUsername(username);
        String[] RoleStringList = StringUtils.split("FKPAdministrator,FKPMoteadministrator,FKPMoteansvarlig,FKPLesebruker", ',');
        List<String> rolenamesList = extUser.getRolenames();
        CollectionUtils.addAll(rolenamesList, RoleStringList);
        extUser.setRolenames(rolenamesList);
        
        return extUser;
    }

    /*
     * (non-Javadoc)
     * 
     * @see no.unified.soak.dao.ExtUserDAO#findUserBySessionID(java.lang.String)
     */
    public ExtUser findUserBySessionID(String sessionId) throws UnsupportedOperationException {
        throw new UnsupportedOperationException(
                "findUserBySessionID(sessionId) is unsupported in SVV environment. Use findUserByUsername(username) instead.");
    }

}
