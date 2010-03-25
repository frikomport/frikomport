package no.unified.soak.dao.ws;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.rpc.ServiceException;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.fkpuser.FkpRole;
import no.unified.soak.dao.fkpuser.FkpUserPortType;
import no.unified.soak.dao.fkpuser.FkpUser_ServiceLocator;
import no.unified.soak.dao.fkpuser.FkpUser_Type;
import no.unified.soak.ez.ExtUser;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class EzUserDAOWS implements ExtUserDAO {
    private transient final Log log = LogFactory.getLog(EzUserDAOWS.class);
    String endpoint = "http://localhost/nusoap.php/fkpuser";
    
    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    private FkpUserPortType getPort() throws ServiceException {
        FkpUser_ServiceLocator locator = new FkpUser_ServiceLocator();
        locator.setEndpointAddress("FkpUserPort", endpoint);
        FkpUserPortType port =  locator.getFkpUserPort();
        return port;
    }

    public List<ExtUser> findAll() {
        return findUsers(findRoles());
    }

    public List<String> findRoles() {
        List<String> roles = new ArrayList<String>();
        try {
            roles = getRolenames(getPort().getAllRoles());
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return roles;
    }

    public ExtUser findUserBySessionID(String sessionId) {
        ExtUser user = null;
        try {
            user = getEzUser(getPort().getUser(sessionId));
            user.setRolenames(getRolenames(getPort().getRoles(user.getId().toString())));
        } catch (Exception e) {
            log.error("Feilet ved henting av user", e);
        }
        return user;
    }

    private ExtUser getEzUser(FkpUser_Type fkpUser) {
        ExtUser user = null;
        if(fkpUser != null){
            user = new ExtUser();
            user.setUsername(fkpUser.getLogin());
            user.setName(fkpUser.getName());
            user.setFirst_name(fkpUser.getFirst_name());
            user.setLast_name(fkpUser.getLast_name());
            user.setEmail(fkpUser.getEmail());
            user.setId(Integer.parseInt(fkpUser.getId()));
            if(!"".equals(fkpUser.getOrganization())){
                user.setKommune(Integer.parseInt(fkpUser.getOrganization()));
            }
        }
        return user;
    }

    private List<String> getRolenames(FkpRole[] roles) {
        List<String> roleNames = new ArrayList<String>();
        for (int i = 0; i < roles.length; i++) {
            roleNames.add(roles[i].getName());
        }
        return roleNames;
    }

    public List<ExtUser> findUsers(List<String> roles) {
        List<ExtUser> users = new ArrayList<ExtUser>();
        String roleNames = "";
        for (int i = 0; i < roles.size(); i++) {
            roleNames += roles.get(i);
            if(i != roles.size()){
                roleNames += ",";
            }
        }
        try {
            FkpUser_Type userArray[] = getPort().getUsers(roleNames);
            for (int i = 0; i < userArray.length; i++) {
                ExtUser user = getEzUser(userArray[i]);
                user.setRolenames(getRolenames(getPort().getRoles(user.getId().toString())));
                users.add(user);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        } catch (ServiceException e) {
            e.printStackTrace();
        }
        return users;
    }

}
