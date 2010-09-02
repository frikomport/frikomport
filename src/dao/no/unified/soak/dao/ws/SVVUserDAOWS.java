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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.RoleDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.Role;
import no.unified.soak.model.RoleEnum;
import no.unified.soak.model.User;
import no.unified.soak.util.ConvertDAO;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Class for fetching user info at SVV with no possibility of:<br/>
 * 1) fetching users based on role or <br/>
 * 2) fetching roles or <br/>
 * 3) fetching all users.
 * 
 * @author kst
 * 
 */
public class SVVUserDAOWS implements ExtUserDAO {
	final String ROLE_ADMINISTRATOR = "FKPAdministrator";
	final String ROLE_REGIONSADMINISTRATOR = "FKPRegionsadministrator";
	final String ROLE_MOTEADMINISTRATOR = "FKPMoteadministrator";
	final String ROLE_LESEBRUKER = "FKPLesebruker";

	UserDAO userDAO;
	RoleDAO roleDAO;
	String endpoint;
	private transient final static Log log = LogFactory.getLog(SVVUserDAOWS.class);

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
		List users = userDAO.getUsersByRoles(roles); // from local database

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
			if (StringUtils.isNotBlank(xmlString)) {
				extUser = new ExtUser();

				String uid = getTagValue("urn1:uid", xmlString);
				if (StringUtils.isEmpty(uid)) {
					// Bruker ikke funnet
					log.info("Ingen informasjom om brukernavn [" + username.toUpperCase() + "] funnet fra webserviceoppslag!");
					return null;
				}

				extUser.setUsername(uid.toLowerCase());
				extUser.setEmail(getTagValue("urn1:mail", xmlString));
				extUser.setFirst_name(SVVUserDAOWS.getTagValue("urn1:givenName", xmlString));
				extUser.setLast_name(SVVUserDAOWS.getTagValue("urn1:sn", xmlString));
				extUser.setName(SVVUserDAOWS.getTagValue("urn1:cn", xmlString));
				extUser.setMobilePhone(SVVUserDAOWS.getTagValue("urn1:mobile", xmlString));
				extUser.setPhoneNumber(SVVUserDAOWS.getTagValue("urn1:telephoneNumber", xmlString));

				String adminRoles = getStringForRole(RoleEnum.ADMIN_ROLE);
				String editorRoles = getStringForRole(RoleEnum.EDITOR_ROLE);
				String eventResponsible = getStringForRole(RoleEnum.EVENTRESPONSIBLE_ROLE);
				String readerRoles = getStringForRole(RoleEnum.READER_ROLE);

				extUser.setRolenames(SVVUserDAOWS.getInnerTagValuesInTag(xmlString, "urn1:svvrole", "Key", 
						adminRoles, editorRoles, eventResponsible, readerRoles ));
			}
		} catch (Exception e) {
			log.error("Feilet ved tolkning av data funnet ved oppslag p� [" + username.toUpperCase() + "] fra webservice!", e);
			return null;
		}

		if (extUser == null) {
			log.error("Fant ikke brukernavn [" + username.toUpperCase() + "] ved oppslag mot ldap.");
		}
		return extUser;
	}

	public String getUserXMLFromWebservice(String username) {
		String responseString = null;
		PrintWriter output = null;
		InputStream inputStream = null;
		String requestXML = null;
		try {
			URL url = new URL(endpoint);
			URLConnection connection = url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
			connection.setRequestProperty("SOAPAction",
					"\"urn:no:vegvesen:ldap.wsdl:OppslagSVVAnsatt:1:0/portOppslagSVVAnsatt/opOppslagSVVAnsatt\"");
			connection.setRequestProperty("Host", "svvjcapsu04.vegvesen.no:18201");

			requestXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:urn=\"urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0\" xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVUIDType:cct:1:0\">"
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
		if (StringUtils.isBlank(responseString)) {
			log.error("Webservice-kall gav tom streng. Pr�vde URL: " + endpoint + " \nRequest string=" + requestXML);
		}

		return responseString;
	}

	public static String getTagValue(String tagname, String xml) {
		try {
			int startTagFirstpos = xml.indexOf("<" + tagname);
			int startTagLastpos = xml.indexOf(">", startTagFirstpos);
			int endTagFirstpos = xml.indexOf("</" + tagname + ">", startTagLastpos);
			return xml.substring(startTagLastpos + 1, endTagFirstpos);
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("<" + tagname + "> finnes ikke i xml");
			return null;
		}
	}

	public static List<String> getInnerTagValuesInTag(String xml, String outerTagname, String innerTagEndfix,
			String... keyCSVToSearch) {
		try {
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
				int rolePosition = xml.indexOf(innerTagEndfix + ">" + rolename + "</", startTagFirstpos + 2);
				if (rolePosition < endTagFirstpos && rolePosition > -1) {
					roleList.add(rolename);
				}
			}

			// TEST TEST TEST -- pga. manglende definasjon av n�dvendige roller
			// i SVV's LDAP
			// roleList.add("FKPMoteadministrator");
			// roleList.add("FKPMoteansvarlig");
			// --------------------------

			return roleList;
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Problem ved uthenting av <" + outerTagname + ">");
			return new ArrayList<String>(); // tom liste uten verdier
		}

	}

	public static String convertStreamToString(InputStream is) throws IOException {
		/*
		 * To convert the InputStream to String we use the
		 * BufferedReader.readLine() method. We iterate until the BufferedReader
		 * return null which means there's no more data to read. Each line will
		 * appended to a StringBuilder and returned as String.
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see no.unified.soak.dao.ExtUserDAO#findUserBySessionID(java.lang.String)
	 */
	public ExtUser findUserBySessionID(String sessionId) throws UnsupportedOperationException {
		throw new UnsupportedOperationException(
				"findUserBySessionID(sessionId) is unsupported in SVV environment. Use findUserByUsername(username) instead.");
	}

	/**
	 * The roles expected from the SVV webservice for user info.
	 * 
	 * @see no.unified.soak.dao.ExtUserDAO#getStringForRole(no.unified.soak.model
	 *      .RoleEnum)
	 */
	public String getStringForRole(RoleEnum role) {
		switch (role) {
		case ADMIN_ROLE:
			return ROLE_ADMINISTRATOR;
		case EDITOR_ROLE:
			return ROLE_REGIONSADMINISTRATOR;
		case EVENTRESPONSIBLE_ROLE:
			return ROLE_MOTEADMINISTRATOR;
		case READER_ROLE:
			return ROLE_LESEBRUKER;
		case EMPLOYEE:
			return "Ansatt";
		case ANONYMOUS:
			return "Anonymous";
		}
		return null;
	}
	
	public boolean isExternalStringRole(String roleStringFromExternal) {
		if (StringUtils.isNotBlank(roleStringFromExternal)) {
			if (ROLE_ADMINISTRATOR.equals(roleStringFromExternal) || ROLE_REGIONSADMINISTRATOR.equals(roleStringFromExternal)
					|| ROLE_MOTEADMINISTRATOR.equals(roleStringFromExternal) || ROLE_LESEBRUKER.equals(roleStringFromExternal)) {
				return true;
			}
		}
		return false;
	}
}
