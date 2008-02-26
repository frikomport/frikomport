/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak;

/**
 * Constant values used throughout the application.
 * 
 * <p>
 * <a href="Constants.java.html"><i>View Source</i></a>
 * </p>
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class Constants {
	// ~ Static fields/initializers
	// =============================================

	/** The name of the ResourceBundle used in this application */
	public static final String BUNDLE_KEY = "ApplicationResources";

	/** The application scoped attribute for persistence engine used */
	public static final String DAO_TYPE = "daoType";

	public static final String DAO_TYPE_HIBERNATE = "hibernate";

	/** Application scoped attribute for authentication url */
	public static final String AUTH_URL = "authURL";

	/** Application scoped attributes for SSL Switching */
	public static final String HTTP_PORT = "httpPort";

	public static final String HTTPS_PORT = "httpsPort";

	/** The application scoped attribute for indicating a secure login */
	public static final String SECURE_LOGIN = "secureLogin";

	/** The encryption algorithm key to be used for passwords */
	public static final String ENC_ALGORITHM = "algorithm";

	/** A flag to indicate if passwords should be encrypted */
	public static final String ENCRYPT_PASSWORD = "encryptPassword";

	/** File separator from System properties */
	public static final String FILE_SEP = System.getProperty("file.separator");

	/** User home from System properties */
	public static final String USER_HOME = System.getProperty("user.home")
			+ FILE_SEP;

	/**
	 * The session scope attribute under which the breadcrumb ArrayStack is
	 * stored
	 */
	public static final String BREADCRUMB = "breadcrumbs";

	/**
	 * The session scope attribute under which the User object for the currently
	 * logged in user is stored.
	 */
	public static final String USER_KEY = "currentUserForm";

	/**
	 * The request scope attribute under which an editable user form is stored
	 */
	public static final String USER_EDIT_KEY = "userForm";

	/**
	 * The request scope attribute that holds the user list
	 */
	public static final String USER_LIST = "userList";

	/**
	 * The request scope attribute for indicating a newly-registered user
	 */
	public static final String REGISTERED = "registered";

	/**
	 * The name of the Administrator role, as specified in web.xml
	 */
	public static final String ADMIN_ROLE = "admin";

	/**
	 * The name of the User role, as specified in web.xml
	 */
	public static final String USER_ROLE = "tomcat";

	/**
	 * The name of the user's role list, a request-scoped attribute when
	 * adding/editing a user.
	 */
	public static final String USER_ROLES = "userRoles";

	/**
	 * The name of the available roles list, a request-scoped attribute when
	 * adding/editing a user.
	 */
	public static final String AVAILABLE_ROLES = "availableRoles";

	/**
	 * User role in eZ publish for administrator.
	 */
	public static final String EZROLE_ADMIN = "Administrator";

	/**
	 * User role in eZ publish for education manager.
	 */
	public static final String EZROLE_EDUCATIONMANAGER = "Opplaringsansvarlig";

	/**
	 * User role in eZ publish for course responsible.
	 */
	public static final String EZROLE_COURSERESPONSIBLE = "Kursansvarlig";

	/**
	 * User role in eZ publish for course participants.
	 */
	public static final String EZROLE_COURSEPARTICIPANTS = "Ansatt";

    /**
     * User role in eZ publish for anonumous.
     */
    public static final String EZROLE_ANONYMOUS = "Anonymous";

    /**
	 * Name of cookie for "Remember Me" functionality.
	 */
	public static final String LOGIN_COOKIE = "sessionId";

	/**
	 * The name of the configuration hashmap stored in application scope.
	 */
	public static final String CONFIG = "appConfig";

	// Municipalities-START
	/**
	 * The request scope attribute that holds the municipalities list
	 */
	public static final String MUNICIPALITIES_LIST = "municipalitiesList";

	// Municipalities-END

	// Location-START
	/**
	 * The request scope attribute that holds the location form.
	 */
	public static final String LOCATION_KEY = "locationForm";

	/**
	 * The request scope attribute that holds the location list
	 */
	public static final String LOCATION_LIST = "locationList";

	// Location-END

	// Person-START
	/**
	 * The request scope attribute that holds the person form.
	 */
	public static final String PERSON_KEY = "personForm";

	/**
	 * The request scope attribute that holds the person list
	 */
	public static final String PERSON_LIST = "personList";

	// Person-END

	// ServiceArea-START
	/**
	 * The request scope attribute that holds the serviceArea form.
	 */
	public static final String SERVICEAREA_KEY = "serviceAreaForm";

	/**
	 * The request scope attribute that holds the serviceArea list
	 */
	public static final String SERVICEAREA_LIST = "serviceAreaList";

	// ServiceArea-END

	// Course-START
	/**
	 * The request scope attribute that holds the course form.
	 */
	public static final String COURSE_KEY = "courseForm";

	/**
	 * The request scope attribute that holds the course list
	 */
	public static final String COURSE_LIST = "courseList";

	// Course-END

	// Registration-START
	/**
	 * The request scope attribute that holds the registration form.
	 */
	public static final String REGISTRATION_KEY = "registrationForm";

	/**
	 * The request scope attribute that holds the registration list
	 */
	public static final String REGISTRATION_LIST = "registrationList";

	// Registration-END

	// Attachment-START
	public static final String ATTACHMENT_FILE_PREFIX = "attach_";

	// Attachment-END

	/**
	 * Key string for transporting eZ publish sessionid to jsp file.
	 */
	public static final String EZ_SESSIONID = "eZSessionid";

	/**
	 * Key string for transporting eZ publish userid to jsp file.
	 */
	public static final String EZ_USERID = "eZUserid";

	/**
	 * Key string for transporting eZ publish user municipality id to jsp file.
	 */
	public static final String EZ_MUNICIPALITY = "eZMunicipality";

	/**
     * Key string for transporting eZ publish user roles to jsp file.
     */
    public static final String EZ_ROLES = "eZroles";
    
	/**
	 * Session scope attribute that holds the locale set by the user. By setting
	 * this key to the same one that Struts uses, we get synchronization in
	 * Struts w/o having to do extra work or have two session-level variables.
	 */
	public static final String PREFERRED_LOCALE_KEY = "org.apache.struts.action.LOCALE";

	/**
	 * Application scope attribute that allows override of the browser's locale.
	 */
	public static final String OVERRIDE_LOCALE_KEY = "javax.servlet.jsp.jstl.fmt.locale";

	/**
	 * Application scope attribute that allows true or false override of
	 * grouping used when data binding variables of class Int, Long or Double.
	 */
	public static final String OVERRIDE_GROUPING_KEY = "overrideFieldBindingLocaleGroupingUsed";

	/**
	 * Application scope attribute that allows override of the number of decimal
	 * digits used when data binding variables of class Int or Long.
	 */
	public static final String OVERRIDE_INTEGER_KEY = "overrideFieldBindingLocaleIntegerDigits";

	/**
	 * Application scope attribute that allows override of the number of decimal
	 * digits used when data binding variables of class Double.
	 */
	public static final String OVERRIDE_DECIMAL_KEY = "overrideFieldBindingLocaleDecimalDigits";

	/**
	 * Application scope attribute that allows override of the number of decimal
	 * digits used when data binding variables of class Double.
	 */
	public static final String MESSAGES_INFO_KEY = "messages";

	// Notification-START
	/**
	 * The request scope attribute that holds the notification form.
	 */
	public static final String NOTIFICATION_KEY = "notificationForm";

	/**
	 * The request scope attribute that holds the notification list
	 */
	public static final String NOTIFICATION_LIST = "notificationList";

	// Notification-END

	// E-mail event types
	public static final int EMAIL_EVENT_COURSEDELETED = 1;

	public static final int EMAIL_EVENT_COURSECHANGED = 2;

	public static final int EMAIL_EVENT_NOTIFICATION = 3;
	
	public static final int EMAIL_EVENT_WAITINGLIST_NOTIFICATION = 4;
	
	public static final int EMAIL_EVENT_REGISTRATION_DELETED = 5;
	
	public static final int EMAIL_EVENT_REGISTRATION_CONFIRMED = 6;
	
	public static final int EMAIL_EVENT_REGISTRATION_MOVED_TO_WAITINGLIST = 7;
	
	// Timers
	
	public static final long NOTIFICATION_INTIAL_DELAY_TIMER = 900000;
	public static final long NOTIFICATION_RUN_INTERVAL = 1800000;
}
