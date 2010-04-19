/**
 * 
 */
package no.unified.soak.dao.ws;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.RoleDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.Role;
import no.unified.soak.model.User;
import no.unified.soak.util.ApplicationResourcesUtil;
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
    String endpoint = "http://klaus-PC:8089/mockportOppslagSVVAnsatt";
    private transient final Log log = LogFactory.getLog(SVVUserDAOWS.class);

    // Only used for testing when real web service is unavailable.
    private static Map<String, ExtUser> hardcodedExtUsers = new HashMap<String, ExtUser>(6);

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
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
        ExtUser extUser = null;
        try {
            String xmlString = getUserXMLFromWebservice(username);
            extUser = new ExtUser();
            extUser.setEmail(getTagValue("urn1:mail", xmlString));
            extUser.setFirst_name(SVVUserDAOWS.getTagValue("urn1:givenName", xmlString));
            extUser.setLast_name(SVVUserDAOWS.getTagValue("urn1:sn", xmlString));
            extUser.setName(SVVUserDAOWS.getTagValue("urn1:cn", xmlString));

            String adminRoles = ApplicationResourcesUtil.getText("role.admin");
            String eventAdminRoles = ApplicationResourcesUtil.getText("MoteAdminRoles ");
            String eventResponsible = ApplicationResourcesUtil.getText("role.eventresponsible");
            String ansattRoles = ApplicationResourcesUtil.getText("role.employee");
            String readeRoles = ApplicationResourcesUtil.getText("role.reader");

            extUser.setRolenames(SVVUserDAOWS.getInnerTagValuesInTag(xmlString, "urn1:svvrole", "Key", new String[] { adminRoles, eventAdminRoles,
                    eventResponsible, ansattRoles, readeRoles }));

        } catch (Exception e) {
            log.error("Feilet ved henting av user fra webservice", e);
        }

        if (extUser == null) {
            extUser = getHardcodedExtUser(username);
        }
        return extUser;
    }

    public String getUserXMLFromWebservice(String username) {
        String responseString = null;
        PrintWriter output = null;
        InputStream inputStream = null;
        try {
            URL url = new URL("http://svvjcapsu04.vegvesen.no:18201/ldap_searchemployees/portOppslagSVVAnsattBndPort");
            URLConnection connection = url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true); // Only if you expect to read a
            // response...
            connection.setUseCaches(false); // Highly recommended...
            connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
            connection.setRequestProperty("SOAPAction",
                    "\"urn:no:vegvesen:ldap.wsdl:OppslagSVVAnsatt:1:0/portOppslagSVVAnsatt/opOppslagSVVAnsatt\"");
            connection.setRequestProperty("Host", "svvjcapsu04.vegvesen.no:18201");

            String requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0\" xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVUIDType:cct:1:0\">"
                    + "<soapenv:Body><urn:Request><urn1:UID>"
                    + username
                    + "</urn1:UID></urn:Request></soapenv:Body></soapenv:Envelope>";

            connection.setRequestProperty("Content-Length", String.valueOf(requestXML.length()));
            output = new PrintWriter(new OutputStreamWriter(connection.getOutputStream()));
            output.print(requestXML);
            output.flush();

            inputStream = connection.getInputStream();
            responseString = convertStreamToString(inputStream);

        } catch (IOException e) {
            log.info("Webservice call failed. " + e);
        } catch (Exception e2) {
            log.info("Parsing webservice answer failed. " + e2);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.warn("Could not close InputStream in finally block for webservice call. " + e);
                }
            }
            if (output != null) {
                output.close();
            }
        }

        return responseString;
    }

    public static String getTagValue(String tagname, String xml) {
        int startTagFirstpos = xml.indexOf("<" + tagname);
        int startTagLastpos = xml.indexOf(">", startTagFirstpos);
        int endTagFirstpos = xml.indexOf("</" + tagname + ">", startTagLastpos);
        return xml.substring(startTagLastpos + 1, endTagFirstpos);
    }

    public static List<String> getInnerTagValuesInTag(String xml, String outerTagname, String innerTagEndfix, String[] keyCSVToSearch) {
        int startTagFirstpos = xml.indexOf("<" + outerTagname);
        int startTagLastpos = xml.indexOf(">", startTagFirstpos);
        int endTagFirstpos = xml.indexOf("</" + outerTagname + ">", startTagLastpos);

        List<String> keyList = new ArrayList<String>();
        for (int i = 0; i < keyCSVToSearch.length; i++) {
            String string = keyCSVToSearch[i];
            String[] keyValues = StringUtils.split(string, ',');
            CollectionUtils.addAll(keyList, keyValues);
        }

        List<String> roleList = new ArrayList<String>();
        for (String rolename : keyList) {
            int rolePosition = xml.indexOf(innerTagEndfix+">" + rolename + "</", startTagFirstpos + 2);
            if (rolePosition < endTagFirstpos && rolePosition > -1) {
                roleList.add(rolename);
            }
        }
        return roleList;
    }

    public static String convertStreamToString(InputStream is) throws IOException {
        /*
         * To convert the InputStream to String we use the BufferedReader.readLine() method. We iterate until the
         * BufferedReader return null which means there's no more data to read. Each line will appended to a
         * StringBuilder and returned as String.
         */
        if (is != null) {
            StringBuilder sb = new StringBuilder();
            String line;

            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                while ((line = reader.readLine()) != null) {
                    sb.append(line).append("\n");
                }
            } finally {
                is.close();
            }
            return sb.toString();
        } else {
            return "";
        }
    }

    private ExtUser getHardcodedExtUser(String username) {
        if (hardcodedExtUsers.isEmpty()) {
            hardcodedExtUsers.put("admin", new ExtUser(14, "admin", "admin_@stafto.no", "Truls", "Testesen", "FKPAdministrator"));
            hardcodedExtUsers.put("sindre", new ExtUser(15, "sindre", "sa@knowit.no", "Sindre", "Amundsen", "FKPAdministrator"));
            hardcodedExtUsers.put("klaus", new ExtUser(16, "klaus", "kst@knowit.no", "Klaus", "Stafto", "FKPAdministrator"));
            hardcodedExtUsers.put("moteadmin", new ExtUser(17, "moteadmin", "moteadmin_@stafto.no", "Møte", "Adminssønn", "FKPMoteansvarlig"));
            hardcodedExtUsers.put("regionadmin", new ExtUser(18, "regionadmin", "regionadmin_@stafto.no", "Regiona", "Adminsdatter", "FKPMoteadministrator"));
            hardcodedExtUsers.put("lese", new ExtUser(19, "lese", "lese_@stafto.no", "Lese", "Brukersønn", "FKPLesebruker"));
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
