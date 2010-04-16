/**
 * 
 */
package no.unified.soak.dao.ws;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.xml.namespace.QName;
import javax.xml.rpc.ServiceException;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.RoleDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.dao.ldapuser.FkpUserPortType;
import no.unified.soak.dao.ldapuser.FkpUser_ServiceLocator;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.util.ConvertDAO;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
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
    String endpoint = "http://klaus-PC:8089/mockportOppslagSVVAnsatt";
    private transient final Log log = LogFactory.getLog(SVVUserDAOWS.class);
    
    //TODO: Only used for testing when real web service is unavailable.
    private static Map<String, ExtUser> hardcodedExtUsers = new HashMap<String, ExtUser>(6); 

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private FkpUserPortType getPort() throws ServiceException {
        FkpUser_ServiceLocator locator = new FkpUser_ServiceLocator();
        locator.setEndpointAddress("LDAPUserPort", endpoint);
        FkpUserPortType port = locator.getFkpUserPort();
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
            // FkpUser_Type user = getPort().getUser(username);

            String requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\">" 
//                xmlns:urn=\"urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0\" xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVUIDType:cct:1:0\">"
                    + "<soapenv:Body><urn:Request><urn1:UID>"
                    + username
                    + "</urn1:UID></urn:Request></soapenv:Body></soapenv:Envelope>";
            Service service = new Service();
            Call call = (Call) service.createCall();
            call.setTargetEndpointAddress(new java.net.URL(endpoint));
            call.setOperationName(new QName("opOppslagSVVAnsatt"));
//            String soapResponse = (String) call.invoke(new Object[] { requestXML });
//            log.debug("Got: [" + soapResponse + "]");
        } catch (Exception e) {
            log.error("Feilet ved henting av user fra webservice", e);
        }

        ExtUser extUser = null;
        
        // Dummy, før webservice-kall fungerer:
        extUser = getHardcodedExtUser(username);
       
        return extUser;
    }

    private ExtUser getHardcodedExtUser(String username) {
        if (hardcodedExtUsers.isEmpty()) {
            hardcodedExtUsers.put("admin", new ExtUser(14, "admin", "admin_@stafto.no", "Truls", "Testesen", "FKPAdministrator"));
            hardcodedExtUsers.put("sindre", new ExtUser(15, "sindre", "sa@knowit.no", "Sindre", "Amundsen", "FKPAdministrator"));
            hardcodedExtUsers.put("klaus", new ExtUser(15, "klaus", "kst@knowit.no", "Klaus", "Stafto", "FKPAdministrator"));
        }

        return hardcodedExtUsers.get(username);
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
