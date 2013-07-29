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
import no.unified.soak.util.ApplicationResourcesUtil;
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
	public static final String ROLE_ADMINISTRATOR = "FKPAdministrator";
	public static final String ROLE_REGIONSADMINISTRATOR = "FKPRegionsadministrator";
	public static final String ROLE_MOTEADMINISTRATOR = "FKPMoteadministrator";
	public static final String ROLE_LESEBRUKER = "FKPLesebruker";
	final String ROLEKEY_FROM_WEBSERVICE = "FKPM";

	UserDAO userDAO;
	RoleDAO roleDAO;
	String endpoint;
	private transient final static Log log = LogFactory.getLog(SVVUserDAOWS.class);

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

	public ExtUser findUserByUsername(String username) throws Exception {
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

				// Parsing av rollene etter endring hos SVV:
				extUser.setRolenames(SVVUserDAOWS.getTagvaluesAfterSiblingTagInOuterTag(xmlString, "urn1:svvrole", "urn2:Key",
						ROLEKEY_FROM_WEBSERVICE, "urn2:Value", adminRoles, editorRoles, eventResponsible, readerRoles));

			}
		} catch (Exception e) {
			log.error("Feilet ifbm. oppslag på [" + username.toUpperCase() + "] fra webservice!", e);
			throw e;
		}

		if (extUser == null) {
			log.error("Fant ikke brukernavn [" + username.toUpperCase() + "] ved oppslag mot ldap.");
		}
		return extUser;
	}

	public String getUserXMLFromWebservice(String username) throws Exception {

		if(endpoint == null){
			endpoint = ApplicationResourcesUtil.getText("javaapp.ldapEndpoint");
			
			if(!StringUtils.isNotBlank(endpoint)){
				log.fatal("javaapp.ldapEndpoint ikke funnet fra ApplicationResources_no_NO_FKPSVV.properties - henting av brukere vil ikke fungere!");
			}
		}
		String responseString = null;
		PrintWriter output = null;
		InputStream inputStream = null;
		String requestXML = null;
		try {
			URL url = new URL(endpoint);
			URLConnection connection = null;
			
			try {
				connection = url.openConnection();
			} 
			catch (IOException e) {
				log.error("Failed connecting to WS-endpoint: " + endpoint, e);
				throw e;
			}
			
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

			try {
				inputStream = connection.getInputStream();
			} 
			catch (IOException e) {
				log.error("Failed getting inputstream from connection (endpoint: " + endpoint + ")", e);
				throw e;
			}
			
			try {
				responseString = convertStreamToString(inputStream);
			} 
			catch (IOException e) {
				log.error("Failed converting inputstream to string", e);
				throw e;
			}
			
		}
		catch (Exception e) {
			log.error("Getting user-XML from WS failed.", e);
			throw e;
		}
		finally {
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
			log.error("Webservice-kall gav tom streng. Prøvde URL: " + endpoint + " \nRequest string=" + requestXML);
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

	public static List<String> getTagvaluesAfterSiblingTagInOuterTag(String xml, String outerTagname, String siblingTagBefore,
			String siblingtagValue, String valueTagname, String... valueTagValues) {
		try {
			int outerTagStartFirstpos = xml.indexOf("<" + outerTagname);
			int outerTagStartLastpos = xml.indexOf(">", outerTagStartFirstpos);
			int outerTagEndFirstpos = xml.indexOf("</" + outerTagname + ">", outerTagStartLastpos);
			int siblingTagBeforeStartFirstPos = xml.indexOf("<" + siblingTagBefore, outerTagStartLastpos);

			List<String> roleList = new ArrayList<String>();
			if (outerTagStartFirstpos == -1 || siblingTagBeforeStartFirstPos == -1 || outerTagEndFirstpos == -1) {
				return roleList;
			}
			
			List<String> keyList = new ArrayList<String>();
			CollectionUtils.addAll(keyList, valueTagValues);

			for (String rolename : keyList) {
				int rolePosition = xml.indexOf("<" + valueTagname + ">" + rolename + "</" + valueTagname + ">",
						siblingTagBeforeStartFirstPos + 2);
				if (rolePosition < outerTagEndFirstpos && rolePosition > -1) {
					roleList.add(rolename);
				}
			}

			return roleList;
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Problem ved uthenting av <" + outerTagname + "> fra xml.");
			return new ArrayList<String>(); // tom liste uten verdier
		}
	}
	
	public static List<String> getInnerTagValuesInTag(String xml, String outerTagname, String innerTagEndfix,
			String... keyCSVToSearch) {
		try {
			int startTagFirstpos = xml.indexOf("<" + outerTagname);
			int startTagLastpos = xml.indexOf(">", startTagFirstpos);
			int endTagFirstpos = xml.indexOf("</" + outerTagname + ">", startTagLastpos);

			List<String> keyList = new ArrayList<String>();
			CollectionUtils.addAll(keyList, keyCSVToSearch);

			List<String> roleList = new ArrayList<String>();
			for (String rolename : keyList) {
				int rolePosition = xml.indexOf(innerTagEndfix + ">" + rolename + "</", startTagFirstpos + 2);
				if (rolePosition < endTagFirstpos && rolePosition > -1) {
					roleList.add(rolename);
				}
			}

			return roleList;
		} catch (Exception e) {
			if (log.isDebugEnabled())
				log.debug("Problem ved uthenting av <" + outerTagname + "> fra xml.");
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
	
	public static String convertRole(String rolename){
		// FKP til SVV
		if(RoleEnum.ADMIN_ROLE.getJavaDBRolename().equalsIgnoreCase(rolename)) return ROLE_ADMINISTRATOR;
		else if(RoleEnum.EDITOR_ROLE.getJavaDBRolename().equalsIgnoreCase(rolename)) return ROLE_REGIONSADMINISTRATOR;
		else if(RoleEnum.EVENTRESPONSIBLE_ROLE.getJavaDBRolename().equalsIgnoreCase(rolename)) return ROLE_MOTEADMINISTRATOR;
		else if(RoleEnum.READER_ROLE.getJavaDBRolename().equalsIgnoreCase(rolename)) return ROLE_LESEBRUKER;
		else if(RoleEnum.EMPLOYEE.getJavaDBRolename().equalsIgnoreCase(rolename)) return "Ansatt";
		else if(RoleEnum.ANONYMOUS.getJavaDBRolename().equalsIgnoreCase(rolename)) return "Anonymous";
		
		// SVV til FKP
		else if(ROLE_ADMINISTRATOR.equalsIgnoreCase(rolename)) return RoleEnum.ADMIN_ROLE.getJavaDBRolename();
		else if(ROLE_REGIONSADMINISTRATOR.equalsIgnoreCase(rolename)) return RoleEnum.EDITOR_ROLE.getJavaDBRolename();
		else if(ROLE_MOTEADMINISTRATOR.equalsIgnoreCase(rolename)) return RoleEnum.EVENTRESPONSIBLE_ROLE.getJavaDBRolename();
		else if(ROLE_LESEBRUKER.equalsIgnoreCase(rolename)) return RoleEnum.READER_ROLE.getJavaDBRolename();
		else if("Ansatt".equalsIgnoreCase(rolename)) return RoleEnum.EMPLOYEE.getJavaDBRolename();
		else if("Anonymous".equalsIgnoreCase(rolename)) return RoleEnum.ANONYMOUS.getJavaDBRolename();
		else return null;
	}
	
}
