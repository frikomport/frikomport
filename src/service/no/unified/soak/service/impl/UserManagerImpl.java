/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.unified.soak.Constants;
import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.Address;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Registration;
import no.unified.soak.model.RoleEnum;
import no.unified.soak.model.User;
import no.unified.soak.model.UserCookie;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.UserExistsException;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.RandomGUID;
import no.unified.soak.util.StringUtil;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * Implementation of UserManager interface.
 * </p>
 * 
 * <p>
 * <a href="UserManagerImpl.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class UserManagerImpl extends BaseManager implements UserManager {
	private UserDAO dao;

	private ExtUserDAO extUserDAO;

	private RoleManager roleManager;

	private MessageSource messageSource;

	private OrganizationManager organizationManager;

	/**
	 * Set the DAO for communication with the data layer.
	 * 
	 * @param dao
	 */
	public void setUserDAO(UserDAO dao) {
		this.dao = dao;
	}

	public void setExtUserDAO(ExtUserDAO extUserDAO) {
		this.extUserDAO = extUserDAO;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setRoleManager(RoleManager roleManager) {
		this.roleManager = roleManager;
	}

	public void setOrganizationManager(OrganizationManager organizationManager) {
		this.organizationManager = organizationManager;
	}

	/**
	 * @see no.unified.soak.service.UserManager#getUser(java.lang.String)
	 */
	public User getUser(String username) {
		return dao.getUser(username);
	}

	/**
	 * @see no.unified.soak.service.UserManager#getUserByHash(java.lang.String)
	 */
	public User getUserByHash(String hash) {
		return dao.getUserByHash(hash);
	}

	/**
	 * @see no.unified.soak.service.UserManager#getUsers(no.unified.soak.model.User)
	 */
	public List getUsers(User user) {
		return dao.getUsers(user);
	}

	public User findUserByEmail(String email) {
		return dao.findUserByEmail(email);
	}

	/**
	 * @see no.unified.soak.service.UserManager#saveUser(no.unified.soak.model.User)
	 */
	public void saveUser(User user) throws UserExistsException {
		try {
			dao.saveUser(user);
		} catch (DataIntegrityViolationException e) {
			throw new UserExistsException("User '" + user.getUsername() + "' already exists!");
		}
	}

    public void updateUser(User user) {
        dao.updateUser(user);
    }

    /**
	 * @see no.unified.soak.service.UserManager#removeUser(java.lang.String)
	 */
	public void removeUser(String username) {
		if (log.isDebugEnabled()) {
			log.debug("removing user: " + username);
		}

		dao.removeUser(username);
	}

	/**
	 * @see no.unified.soak.service.UserManager#checkLoginCookie(java.lang.String)
	 */
	public String checkLoginCookie(String value) {
		value = StringUtil.decodeString(value);

		String[] values = StringUtils.split(value, "|");

		// in case of empty username in cookie, return null
		if (values.length == 1) {
			return null;
		}

		if (log.isDebugEnabled()) {
			log.debug("looking up cookieId: " + values[1]);
		}

		UserCookie cookie = new UserCookie();
		cookie.setUsername(values[0]);
		cookie.setCookieId(values[1]);
		cookie = dao.getUserCookie(cookie);

		if (cookie != null) {
			if (log.isDebugEnabled()) {
				log.debug("cookieId lookup succeeded, generating new cookieId");
			}

			return saveLoginCookie(cookie);
		} else {
			if (log.isDebugEnabled()) {
				log.debug("cookieId lookup failed, returning null");
			}

			return null;
		}
	}

	/**
	 * @see no.unified.soak.service.UserManager#createLoginCookie(java.lang.String)
	 */
	public String createLoginCookie(String username) {
		UserCookie cookie = new UserCookie();
		cookie.setUsername(username);

		return saveLoginCookie(cookie);
	}

	/**
	 * Convenience method to set a unique cookie id and save to database
	 * 
	 * @param cookie
	 * @return
	 */
	private String saveLoginCookie(UserCookie cookie) {
		cookie.setCookieId(new RandomGUID().toString());
		dao.saveUserCookie(cookie);

		return StringUtil.encodeString(cookie.getUsername() + "|" + cookie.getCookieId());
	}

	/**
	 * @see no.unified.soak.service.UserManager#removeLoginCookies(java.lang.String)
	 */
	public void removeLoginCookies(String username) {
		dao.removeUserCookies(username);
	}

	public List getRoles() {
		List roles = roleManager.getRoles(null);
		return roles;
	}

	public List getResponsibles() {
		List ezUsers = getEZResponsibles();
		List users = new ArrayList();
		for (Iterator iter = ezUsers.iterator(); iter.hasNext();) {
			ExtUser ezUser = (ExtUser) iter.next();
			try {
				users.add(dao.getUser(ezUser.getUsername()));
			} catch (ObjectRetrievalFailureException objectRetrievalFailureException) {
				User user = addUser(ezUser.getUsername(), ezUser.getFirst_name(), ezUser.getLast_name(), ezUser
						.getEmail(), ezUser.getId(), ezUser.getRolenames(), ezUser.getKommune(), null, null);
				users.add(user);
			}
		}
		return users;
	}
	
	public User addUser(String username, String firstName, String lastName, String email, Integer id,
			List<String> rolenames, Integer kommune, String mobilePhone, String phoneNumber) {
		User user = new User(username);
		user.setFirstName(firstName);
		user.setLastName(lastName);
        user.setEmail(email);

		if (kommune != null && kommune != 0) {
			updateKommune(kommune, user);
		}
		if (rolenames == null) {
			rolenames = getDefaultRoles();
		}
		setRoles(rolenames, user);
		user.setEnabled(true);
		user.setMobilePhone(mobilePhone);
		user.setPhoneNumber(phoneNumber);
		user.setAddress(new Address());
		user.setHash(StringUtil.encodeString(username));
		user.setInvoiceAddress(new Address());
		updateInvoiceAddressFromOrganization(user);

		try {
			saveUser(user);
			return user;
		} catch (UserExistsException e) {
			log.error("UserExistsException: " + e);
			return null;
		}
	}
	
	private boolean updateInvoiceAddressFromOrganization(User user) {
		boolean save = false;
		if (user.getOrganization() != null) {
			Address orgAddress = user.getOrganization().getInvoiceAddress();
            Address invoiceAddress = user.getInvoiceAddress();
            if(invoiceAddress == null){
                invoiceAddress = orgAddress;
                save = true;
            }
            else{
                if ("".equals(invoiceAddress.getAddress())) {
                    invoiceAddress.setAddress(orgAddress.getAddress());
                    save = true;
                }
                if ("".equals(invoiceAddress.getCity())) {
                    invoiceAddress.setCity(orgAddress.getCity());
                    save = true;
                }
                if ("".equals(invoiceAddress.getCountry())) {
                    invoiceAddress.setCountry(orgAddress.getCountry());
                    save = true;
                }
                if ("".equals(invoiceAddress.getPostalCode())) {
                    invoiceAddress.setPostalCode(orgAddress.getPostalCode());
                    save = true;
                }
                if ("".equals(invoiceAddress.getProvince())) {
                    invoiceAddress.setProvince(orgAddress.getProvince());
                    save = true;
                }
                if ("".equals(user.getInvoiceName())) {
                    user.setInvoiceName(user.getOrganization().getInvoiceName());
                    save = true;
                }
            }
		}
		return save;
	}

	private boolean updateKommune(Integer kommune, User user) {
		boolean save = false;
		List organizations = organizationManager.getAll();
		// first search in ids
		for (Iterator iter = organizations.iterator(); iter.hasNext();) {
			Organization organization = (Organization) iter.next();

			if (organization.getId().equals(kommune.longValue())) {
				if (user.getOrganizationid() == null || !user.getOrganizationid().equals(organization.getId())) {
					user.setOrganizationid(organization.getId());
					save = true;
				}
				return save;
			}
		}
		// if no match search in numbers.
		for (Iterator iter = organizations.iterator(); iter.hasNext();) {
			Organization organization = (Organization) iter.next();

			if (organization.getNumber().equals(kommune.longValue())) {
				if (user.getOrganizationid() == null || !user.getOrganizationid().equals(organization.getId())) {
					user.setOrganizationid(organization.getId());
					save = true;
				}
				return save;
			}
		}
		return save;
	}

	public void updateUser(User user, String firstName, String lastName, String email, Integer id,
 List<String> rolenames, Integer kommune,
			String mobilePhone, String phoneNumber) {
		Boolean save = false;
		if (!firstName.equals(user.getFirstName())) {
			user.setFirstName(firstName);
			save = true;
		}
		if (!lastName.equals(user.getLastName())) {
			user.setLastName(lastName);
			save = true;
		}
		if (!email.equals(user.getEmail())) {
			user.setEmail(email);
			save = true;
		}

		if (kommune != null && kommune != 0) {
			if (updateKommune(kommune, user)) {
				save = true;
			}
		}

		// Since eZ publish does not give these phone numbers but SVVs ldap
		// does, only update phone numbers when one is presented.
		if (mobilePhone != null && mobilePhone.equals(user.getMobilePhone())) {
			user.setMobilePhone(mobilePhone);
			save = true;
		}
		if (phoneNumber != null && phoneNumber.equals(user.getPhoneNumber())) {
			user.setPhoneNumber(phoneNumber);
			save = true;
		}

		setRoles(rolenames, user, save);
		if (user.getHash() == null || user.getHash().length() == 0) {
			user.setHash(StringUtil.encodeString(user.getUsername()));
			save = true;
		}

		if (updateInvoiceAddressFromOrganization(user)) {
			save = true;
		}

		if (save) {
			user.setEnabled(true);
			try {
				saveUser(user);
			} catch (UserExistsException e) {
				log.error("UserExistsException: " + e);
			}
		}

	}

	private void setRoles(List<String> rolenames, User user) {
		setRoles(rolenames, user, true);
	}

	private void setRoles(List<String> rolenames, User user, Boolean save) {
		// check if roles are the same as the ones set on user.
		if (equalRoles(user, rolenames)) {
			return;
		}
		// Roles are different and needs to be saved.
		save = true;

		// remove existing roles before new ones are added.
		user.removeAllRoles();

		String[] adminRolenames = extUserDAO.getStringForRole(RoleEnum.ADMIN_ROLE).split("\\,");
		String[] editorRolenames = extUserDAO.getStringForRole(RoleEnum.EDITOR_ROLE).split("\\,");
		String[] eventresponsibleRolenames = extUserDAO.getStringForRole(RoleEnum.EVENTRESPONSIBLE_ROLE).split("\\,");
		String[] readerRolenames = extUserDAO.getStringForRole(RoleEnum.READER_ROLE).split("\\,");
		String[] emplyeeRolenames = extUserDAO.getStringForRole(RoleEnum.EMPLOYEE).split("\\,");
		String[] anonymousRolenames = extUserDAO.getStringForRole(RoleEnum.ANONYMOUS).split("\\,");

		for (Iterator iter = rolenames.iterator(); iter.hasNext();) {
			String rolename = (String) iter.next();
			
			if (ArrayUtils.contains(adminRolenames, rolename)) {
                user.addRole(roleManager.getRole(Constants.ADMIN_ROLE));
                // legger til roller lenger ned i hierarkiet
                user.addRole(roleManager.getRole(Constants.EDITOR_ROLE));
                user.addRole(roleManager.getRole(Constants.EVENTRESPONSIBLE_ROLE));
                user.addRole(roleManager.getRole(Constants.READER_ROLE));
                user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
                user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
            } else if (ArrayUtils.contains(editorRolenames, rolename)) {
				user.addRole(roleManager.getRole(Constants.EDITOR_ROLE));
                // legger til roller lenger ned i hierarkiet
                user.addRole(roleManager.getRole(Constants.EVENTRESPONSIBLE_ROLE));
                user.addRole(roleManager.getRole(Constants.READER_ROLE));
                user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
                user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
			} else if (ArrayUtils.contains(eventresponsibleRolenames, rolename)) {
				user.addRole(roleManager.getRole(Constants.EVENTRESPONSIBLE_ROLE));
                // legger til roller lenger ned i hierarkiet
                user.addRole(roleManager.getRole(Constants.READER_ROLE));
                user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
                user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
			} else if (ArrayUtils.contains(readerRolenames, rolename)) {
			    user.addRole(roleManager.getRole(Constants.READER_ROLE));
                // legger til roller lenger ned i hierarkiet
                user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
                user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
			} else if (ArrayUtils.contains(emplyeeRolenames, rolename)) {
                user.addRole(roleManager.getRole(Constants.EMPLOYEE_ROLE));
                // legger til roller lenger ned i hierarkiet
                user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
            } else if (ArrayUtils.contains(anonymousRolenames, rolename)) {
                user.addRole(roleManager.getRole(Constants.ANONYMOUS_ROLE));
            } else if (roleManager.findRole(rolename) != null) {
				user.addRole(roleManager.findRole(rolename));
			} else {
				no.unified.soak.model.Role role = new no.unified.soak.model.Role("role_" + rolename);
				role.setDescription(rolename);
				roleManager.saveRole(role);
				user.addRole(role);
			}
		}
	}

	private boolean equalRoles(User user, List<String> rolenames) {
		if (user.getRoleNameList().size() != rolenames.size()) {
			return false;
		}

		for (String rolename : rolenames) {
			if (!user.getRoleNameList().contains(rolename)) {
				return false;
			}
		}

		return true;
	}

	public User addUser(Registration registration) {
		User user = new User(registration.getEmail());
		user.setEmployeeNumber(registration.getEmployeeNumber());
		user.setFirstName(registration.getFirstName());
		user.setLastName(registration.getLastName());
		user.setEmail(registration.getEmail());
		user.setBirthdate(registration.getBirthdate());
		user.setPhoneNumber(registration.getPhone());
		user.setMobilePhone(registration.getMobilePhone());
		user.setJobTitle(registration.getJobTitle());
		user.setWorkplace(registration.getWorkplace());
		user.setOrganizationid(registration.getOrganizationid());
		user.setServiceAreaid(registration.getServiceAreaid());
		user.setInvoiceName(registration.getInvoiceName());
		user.setInvoiceAddress(registration.getInvoiceAddress());
		user.setClosestLeader(registration.getClosestLeader());

		user.setEnabled(true);
		user.setAddress(new Address());
		setRoles(getDefaultRoles(), user);
		user.setHash(StringUtil.encodeString(user.getUsername()));
		user.setHashuser(true);
		
		try {
			saveUser(user);
			return user;
		} catch (UserExistsException e) {
			log.error("UserExistsException: " + e);
			return null;
		}
	}

	private List<String> getDefaultRoles() {
        List<String> roles = new ArrayList();
        roles.add(extUserDAO.getStringForRole(RoleEnum.ANONYMOUS));
        roles.add(extUserDAO.getStringForRole(RoleEnum.EMPLOYEE));
		return roles;
	}

	private List getEZResponsibles() {
        List<String> roles = new ArrayList();
//        roles.add(messageSource.getMessage("role.eventresponsible", null, locale));
//        roles.add(messageSource.getMessage("role.editor", null, locale));
        roles.add("eventresponsible");
        roles.add("editor");
        List users = extUserDAO.findUsers(roles);
		return users;
	}

    public void disableUser(User user) {
        setEnabledStatus(user,false);
    }

    public void enableUser(User user) {
        setEnabledStatus(user,true);
    }

    private void setEnabledStatus(User user, boolean status){
        user.setEnabled(status);
        try{
            saveUser(user);
        } catch (UserExistsException e) {
            log.error("UserExistsException: " + e);
        }
    }
}
