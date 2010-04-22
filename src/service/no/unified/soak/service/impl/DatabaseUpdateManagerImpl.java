package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.sql.DataSource;

import no.unified.soak.Constants;
import no.unified.soak.model.Address;
import no.unified.soak.model.Category;
import no.unified.soak.model.Configuration;
import no.unified.soak.model.Course;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Role;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.RoleManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.DefaultQuotedNamingStrategy;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;
import org.springframework.orm.ObjectRetrievalFailureException;

/**
 * User: gv Date: 05.jun.2008 Time: 10:26:23
 */
public class DatabaseUpdateManagerImpl extends BaseManager implements DatabaseUpdateManager {

    private JdbcTemplate jt = new JdbcTemplate();
    private CourseManager courseManager = null;
    private RegistrationManager registrationManager = null;
    private UserManager userManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private OrganizationManager organizationManager = null;
    private CategoryManager categoryManager = null;
    private ConfigurationManager configurationManager = null;
    private RoleManager roleManager = null;
    
    // hack for setting messagesource and locale to ApplicationResourcesUtil
    // once
    public void setLocale(Locale locale) {
        // ApplicationResourcesUtil.setLocale(locale);
    }

    public void setMessageSource(MessageSource messageSource) {
        ApplicationResourcesUtil.setMessageSource(messageSource);
    }

    // --- end hack

    public void setDataSource(DataSource dataSource) {
        jt.setDataSource(dataSource);
    }

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
        this.serviceAreaManager = serviceAreaManager;
    }

    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public void setRoleManager(RoleManager roleManager) {
        this.roleManager = roleManager;
    }

    public void updateDatabase() {
        insertDefaultValues();

        updateBySQLStatements();

        // ServiceArea updates
        updateServiceAreas();
        // User updates
        updateUsers();
        // Organization updates
        updateOrganizations();
        // Course updates
        updateCourses();
        // Registration updates
        updateRegistrations();
        // Schema updates postactions
        updateDatabaseSchemaAfter();

        // update configuration content
        updateConfigurations();
    }

    private void insertDefaultValues() {

        try {
        	roleManager.getRole("anonymous");
        }catch(ObjectRetrievalFailureException e){
        	Role role = new Role();
        	role.setName("anonymous");
        	role.setDescription("Anonymous");
        	role.setVersion(1);
        	log.info("\"Role\" lagt til i DB: " + role);
        }

        try {
        	roleManager.getRole("admin");
        }catch(ObjectRetrievalFailureException e){
        	Role role = new Role();
        	role.setName("admin");
        	role.setDescription("Administrator");
        	role.setVersion(1);
        	log.info("\"Role\" lagt til i DB: " + role);
        }

        try {
        	roleManager.getRole("employee");
        }catch(ObjectRetrievalFailureException e){
        	Role role = new Role();
        	role.setName("employee");
        	role.setDescription("Ansatt");
        	role.setVersion(1);
        	log.info("\"Role\" lagt til i DB: " + role);
        }

        try {
        	roleManager.getRole("instructor");
        }catch(ObjectRetrievalFailureException e){
        	Role role = new Role();
        	role.setName("instructor");
        	role.setDescription("Kursansvarlig");
        	role.setVersion(1);
        	log.info("\"Role\" lagt til i DB: " + role);
        }

        try {
        	roleManager.getRole("editor");
        }catch(ObjectRetrievalFailureException e){
        	Role role = new Role();
        	role.setName("editor");
        	role.setDescription("Opplaringsansvarlig");
        	role.setVersion(1);
        	log.info("\"Role\" lagt til i DB: " + role);
        }

        if (ApplicationResourcesUtil.isSVV()) {
        	// inserts new role for SVV
        	try {
        		roleManager.getRole("reader");
        	}catch(ObjectRetrievalFailureException e){
        		Role role = new Role();
        		role.setName("reader");
        		role.setDescription("Reader");
        		role.setVersion(1);
        		log.info("\"Role\" lagt til i DB: " + role);
        	}
        }    	

        // inserts category if doesn't
        try {
        	categoryManager.getCategory(1L);
        }catch(ObjectRetrievalFailureException e){
        	Category cat = new Category();
        	cat.setName("Hendelse");
        	cat.setSelectable(true);
        	categoryManager.saveCategory(cat);
        	log.info("\"Category\" lagt til i DB: " + cat);
        }

        
        Vector<Configuration> configurationsToInsert = new Vector<Configuration>();
        // common configurations 
    	configurationsToInsert.add(new Configuration("access.registration.delete", false, null));
    	configurationsToInsert.add(new Configuration("access.registration.userdefaults", false, null));
    	configurationsToInsert.add(new Configuration("access.registration.emailrepeat", false, null));
    	configurationsToInsert.add(new Configuration("access.registration.showComment", true, null));
    	configurationsToInsert.add(new Configuration("mail.course.sendSummary", true, null));
    	configurationsToInsert.add(new Configuration("mail.registration.notifyResponsible", false, null));
    	configurationsToInsert.add(new Configuration("show.menu", false, null));
        
        if (ApplicationResourcesUtil.isSVV()) {
            // configurations specific for FKPSVV enviroment.
        	configurationsToInsert.add(new Configuration("access.registration.showEmployeeFields", false, null));
        	configurationsToInsert.add(new Configuration("access.registration.showServiceArea", false, null));
        	configurationsToInsert.add(new Configuration("access.registration.showJobTitle", false, null));
        	configurationsToInsert.add(new Configuration("access.registration.showWorkplace", false, null));

        } else {
            // configurations specific for non-FKPSVV enviroment.
        	configurationsToInsert.add(new Configuration("access.registration.showEmployeeFields", true, null));
        	configurationsToInsert.add(new Configuration("access.registration.showServiceArea", true, null));
        	configurationsToInsert.add(new Configuration("access.registration.showJobTitle", true, null));
        	configurationsToInsert.add(new Configuration("access.registration.showWorkplace", true, null));
        }

        List<Configuration> configurationsInDB = configurationManager.getConfigurations();
        if(configurationsInDB.isEmpty()){
        	// insert all configurations for env
        	for(int i=0; i<configurationsToInsert.size(); i++){
        		Configuration c = configurationsToInsert.get(i);
        		configurationManager.saveConfiguration(c);
	        	log.info("\"Configuration\" lagt til i DB: " + c);
        	}
        }
        else {
        	// insert missing configurations
    		for(int i=0; i<configurationsToInsert.size(); i++){
    			Configuration configuration = configurationsToInsert.get(i);
    			boolean insert = true;
    			Iterator confList = configurationsInDB.iterator();
            	while(confList.hasNext()){
            		Configuration alreadyInDB = (Configuration)confList.next();
            		if(alreadyInDB.getName().equalsIgnoreCase(configuration.getName())){
            			insert = false;
            		}
            	}
            	if(insert) {
            		configurationManager.saveConfiguration(configuration);
    	        	log.info("\"Configuration\" lagt til i DB: " + configuration);
            	}
    		}
        }
        
        if (ApplicationResourcesUtil.isSVV()) {
        	
        	List<Organization> organizationsInDB = organizationManager.getAll();
        	
        	Vector<Organization> organizationsToInsert = new Vector<Organization>();
        	organizationsToInsert.add(new Organization("Østfold", 1, 2, true));
        	organizationsToInsert.add(new Organization("Akershus", 2, 2, true));
        	organizationsToInsert.add(new Organization("Oslo", 3, 2, true));
        	organizationsToInsert.add(new Organization("Hedmark", 4, 2, true));
        	organizationsToInsert.add(new Organization("Oppland", 5, 2, true));
        	organizationsToInsert.add(new Organization("Buskerud", 6, 2, true));
        	organizationsToInsert.add(new Organization("Vestfold", 7, 2, true));
        	organizationsToInsert.add(new Organization("Telemark", 8, 2, true));
        	organizationsToInsert.add(new Organization("Aust-Agder", 9, 2, true));
        	organizationsToInsert.add(new Organization("Vest-Agder", 10, 2, true));
        	organizationsToInsert.add(new Organization("Rogaland", 11, 2, true));
        	organizationsToInsert.add(new Organization("Hordaland", 12, 2, true));
        	organizationsToInsert.add(new Organization("Sogn og Fjordane", 14, 2, true));
        	organizationsToInsert.add(new Organization("Møre og Romsdal", 15, 2, true));
        	organizationsToInsert.add(new Organization("Sør-Trøndelag", 16, 2, true));
        	organizationsToInsert.add(new Organization("Nord-Trøndelag", 17, 2, true));
        	organizationsToInsert.add(new Organization("Nordland", 18, 2, true));
        	organizationsToInsert.add(new Organization("Troms", 19, 2, true));
        	organizationsToInsert.add(new Organization("Finnmark", 20, 2, true));

            if(organizationsInDB.isEmpty()){
            	// insert organizations for env
            	for(int i=0; i<organizationsToInsert.size(); i++){
            		Organization o = organizationsToInsert.get(i);
            		organizationManager.saveOrganization(o);
    	        	log.info("\"Organization\" lagt til i DB: " + o);
            	}
            }
            else {
            	// insert missing organizations
        		for(int i=0; i<organizationsToInsert.size(); i++){
            		Organization organization = organizationsToInsert.get(i);
        			boolean insert = true;
        			Iterator orgList = organizationsInDB.iterator();
                	while(orgList.hasNext()){
                		Organization alreadyInDB = (Organization)orgList.next();
                		if(alreadyInDB.getName().equalsIgnoreCase(organization.getName())){
                			insert = false;
                		}
                	}
                	if(insert) {
                		organizationManager.saveOrganization(organization);
        	        	log.info("\"Organization\" lagt til i DB: " + organization);
                	}
        		}
            }

            // assign servicearea Mengdetrening to SVV-organizations
            
            List<Organization> counties = organizationManager.getOrganizationsByType(Organization.Type.COUNTY);
            List<ServiceArea> serviceAreas = serviceAreaManager.getAll();
            
            if(serviceAreas.isEmpty()){
            	// insert servicearea pr. county
            	Iterator c = counties.iterator();
            	while(c.hasNext()){
            		Organization organization = (Organization)c.next();
            		serviceAreaManager.saveServiceArea(new ServiceArea("Mengdetrening", true, organization.getId()));
            	}
            }
        }
    }

    /**
     * For every row, run the first (select) statement (column index 0) and see if it returns a 0 integer. If it does,
     * then run the insert statement in the next sql statement (column index 1). If the first element (column index 0)
     * is null, then run only second statement without any checking.
     * 
     * @param table
     * @param sqlStatements
     */
    private void insertIntoTableBySQLStatements(String table, String[][] sqlStatements) {
        int count = 0;
        for (int i = 0; i < sqlStatements.length; i++) {
            String[] aSelectAndInsert = sqlStatements[i];

            int existCount = 0;
            if (aSelectAndInsert[0] != null) {
                existCount = jt.queryForInt(adjustToOracle(aSelectAndInsert[0]));
            }
            if (existCount == 0 && aSelectAndInsert[1] != null) {
                count += jt.update(adjustToOracle(aSelectAndInsert[1]));
            }
        }
        if (count > 0 && log.isInfoEnabled()) {
            log.info("Number of " + table + " rows inserted (or updated) in database: " + count);
        }
    }

    private String adjustToOracle(String sql) {
        StringBuffer sqlSB = new StringBuffer(sql.toLowerCase());
        String tablePrefix = DefaultQuotedNamingStrategy.getTablePrefix();
        if (StringUtils.isNotBlank(tablePrefix)) {
            // Table name adjustment for Oracle
            // int fromPos = sqlSB.indexOf(" from ");
            // int tableStartPos = (fromPos > -1 ? fromPos : sqlSB.indexOf("insert into ")) + 6;
            // if (tableStartPos == 5) {
            // return sql;
            // }
            // sqlSB.insert(tableStartPos, tablePrefix);

            // Column name adjustment for Oracle
            int startPos = sqlSB.indexOf(" where ");
            if (startPos > -1) {
                sqlSB.insert(startPos + 7, '"');
                int endPos = sqlSB.indexOf(" ", startPos + 8);
                if (endPos == -1) {
                    endPos = sqlSB.indexOf("=", startPos + 8);
                }
                sqlSB.insert(endPos, '"');
            }

            startPos = sqlSB.indexOf(" (") + 2;
            if (startPos > -1 + 2) {
                int endPos = sqlSB.indexOf(")");
                String[] fieldArray = sqlSB.substring(startPos, endPos).replaceAll(" ", "").split(",", 0);
                String fieldQuoted = "\"" + StringUtils.join(fieldArray, "\",\"") + "\"";
                sqlSB.delete(startPos, endPos);
                sqlSB.insert(startPos, fieldQuoted);
            }
            
            //Boolean value adjustment for Oracle. true->1, false->0.
            startPos = indexOfIgnorecase(sqlSB.toString(), new String[] {" true)", " true,", " true ,", ",true)", ",true )", ",true,", "(true,"});
            if (startPos > -1) {
                sqlSB.replace(startPos+1, startPos+5, "1");
            }
            startPos = indexOfIgnorecase(sqlSB.toString(), new String[] {" false)", " false,", ",false)", ",false ", ",false,", "(false,", "(false "});
            if (startPos > -1) {
                sqlSB.replace(startPos+1, startPos+5, "0");
            }

            return sqlSB.toString();
        } else {
            return sql;
        }
    }
    
    private static int indexOfIgnorecase(String sbOrig, String[] strings){
        String sb = sbOrig.toLowerCase();
        for (int i = 0; i < strings.length; i++) {
            String string = strings[i];
            int index = sb.indexOf(string.toLowerCase());
            if (index > -1) {
                return index;
            }
            
        }
        
        return -1;
    }

    private void updateBySQLStatements() {
        updateRegistrationbySQLStatement();
    }

    private void updateRegistrationbySQLStatement() {
        if (!reservedFieldExist()) {
            // No column 'reserved' to convert data from. No initial writing to 'status' column is done in the database.
            return;
        }

        String sql = "update registration set status = reserved + 1 where status is null";
        int nRowsAffected = jt.update(sql);
        if (nRowsAffected > 0) {
            log.info(nRowsAffected + " rows affected by convertion from reserved to status field in database.");
        } else {
            log.info(nRowsAffected + " rows affected by convertion from reserved to status field in database. The field \"reserved\" in table \"registration\" can be dropped.");
        }
    }

    private boolean reservedFieldExist() {
    	
    	String sql = null;
    	if(ApplicationResourcesUtil.isSVV()){
    		sql = "SELECT * FROM(SELECT * FROM registration) WHERE rownum = 1";
    	}
    	else{
    		sql = "select * from registration limit 1";
    	}
    	
        SqlRowSet rowSet = jt.queryForRowSet(sql);
        SqlRowSetMetaData metaData = rowSet.getMetaData();
        boolean reservedFieldExists = false;
        for (String columnName : metaData.getColumnNames()) {
            if (columnName.equals("reserved")) {
                reservedFieldExists = true;
                continue;
            }
        }
        return reservedFieldExists;
    }

    private void updateDatabaseSchemaAfter() {
    }

    /**
     * Updates invoiceaddress for organization
     * 
     * @since 1.5
     */
    private void updateOrganizations() {
        List<Organization> organizations = organizationManager.getAllIncludingDisabled();
        if (organizations != null && !organizations.isEmpty()) {
            Iterator<Organization> it = organizations.iterator();
            while (it.hasNext()) {
                Organization organization = it.next();
                // update invoiceaddress
                if (organization.getInvoiceAddress() == null) {
                    Address invoice = new Address();
                    invoice.setPostalCode("0");
                    organization.setInvoiceAddress(invoice);
                    organizationManager.saveOrganization(organization);
                }
            }
        }
    }

    /**
     * Updates invoiceaddress for user
     * 
     * @since 1.5
     */
    private void updateUsers() {
        List<User> users = userManager.getUsers(new User());
        if (users != null && !users.isEmpty()) {
            Iterator<User> it = users.iterator();
            while (it.hasNext()) {
                boolean save = false;
                User user = it.next();
                // Updates invoice address
                if (user.getInvoiceAddress() == null) {
                    Address invoice = new Address();
                    invoice.setPostalCode("0");
                    user.setInvoiceAddress(invoice);
                    save = true;
                }
                // updates serviceareas
                if (user.getOrganization() != null && user.getServiceArea() != null) {
                    if (!user.getOrganizationid().equals(user.getServiceArea().getOrganizationid())) {
                        ServiceArea search = new ServiceArea();
                        search.setOrganizationid(user.getOrganizationid());
                        List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                        Iterator<ServiceArea> sit = serviceAreas.iterator();
                        while (sit.hasNext()) {
                            ServiceArea serviceArea = sit.next();
                            if (user.getServiceArea().getName().equals(serviceArea.getName())) {
                                user.setServiceArea(serviceArea);
                                user.setServiceAreaid(serviceArea.getId());
                                save = true;
                                break;
                            }
                        }
                    }
                }
                if (save) {
                    userManager.updateUser(user);
                }
            }
        }
    }

    /**
     * Checks the registrations table and creates users based on email addresses
     * 
     * @since 1.4
     */
    private void updateRegistrations() {
        List<Registration> registrations = registrationManager.getRegistrations(new Registration());
        if (registrations != null && !registrations.isEmpty()) {
            Iterator<Registration> it = registrations.iterator();
            while (it.hasNext()) {
                boolean save = false;
                Registration registration = it.next();
                User admin = userManager.getUser("admin");
                // Checks the registrations table and creates users based on
                // email addresses
                if (registration.getUsername() == null || registration.getUsername().trim().length() == 0) {
                    if (EmailValidator.getInstance().isValid(registration.getEmail())) {
                        User user = userManager.findUserByEmail(registration.getEmail());
                        if (user == null) {
                            user = userManager.addUser(registration.getEmail(), registration.getFirstName(), registration
                                    .getLastName(), registration.getEmail(), new Integer(0), null, new Integer(0), null, null);
                        }
                        // Connect user with registration
                        registration.setUser(user);
                        registration.setUsername(user.getUsername());
                        save = true;
                    } else {
                        // Connect with admin user
                        registration.setUser(admin);
                        registration.setUsername(admin.getUsername());
                        save = true;
                    }
                }
                // updates the invoice address
                if (registration.getInvoiceAddress() == null) {
                    Address invoice = new Address();
                    invoice.setPostalCode("0");
                    registration.setInvoiceAddress(invoice);
                    save = true;
                }
                // updates serviceareas
                if (registration.getOrganization() != null && registration.getServiceArea() != null) {
                    if (!registration.getOrganizationid().equals(registration.getServiceArea().getOrganizationid())) {
                        ServiceArea search = new ServiceArea();
                        search.setOrganizationid(registration.getOrganizationid());
                        List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                        Iterator<ServiceArea> sit = serviceAreas.iterator();
                        while (sit.hasNext()) {
                            ServiceArea serviceArea = sit.next();
                            if (registration.getServiceArea().getName().equals(serviceArea.getName())) {
                                registration.setServiceArea(serviceArea);
                                registration.setServiceAreaid(serviceArea.getId());
                                save = true;
                                break;
                            }
                        }
                    }
                }
                if (save) {
                    registrationManager.saveRegistration(registration);
                }
            }
        }
    }

    /**
     * @since 1.4
     */
    private void updateCourses() {
        List<Course> courses = courseManager.getCourses(new Course());
        if (courses != null && !courses.isEmpty()) {
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()) {
                boolean save = false;
                Course course = it.next();
                if (course.getRole().equals("Anonymous")) {
                    course.setRole(Constants.ANONYMOUS_ROLE);
                    save = true;
                } else if (course.getRole().equals("Ansatt")) {
                    course.setRole(Constants.EMPLOYEE_ROLE);
                    save = true;
                } else if (course.getRole().equals("Kommuneansatt")) {
                    course.setRole(Constants.EMPLOYEE_ROLE);
                    save = true;
                } else if (course.getRole().equals("Kursansvarlig")) {
                    course.setRole(Constants.INSTRUCTOR_ROLE);
                    save = true;
                } else if (course.getRole().equals("Opplaringsansvarlig")) {
                    course.setRole(Constants.EDITOR_ROLE);
                    save = true;
                } else if (course.getRole().equals("Opplï¿½ringsansvarlig")) {
                    course.setRole(Constants.EDITOR_ROLE);
                    save = true;
                } else if (course.getRole().equals("Admin")) {
                    course.setRole(Constants.ADMIN_ROLE);
                    save = true;
                }
                // responsible username
                if (course.getResponsible() != null && course.getResponsibleUsername() == null) {
                    course.setResponsibleUsername(course.getResponsible().getUsername());
                    save = true;
                }
                // restrictions
                if (course.getRestricted() == null) {
                    course.setRestricted(false);
                    save = true;
                }
                // service areas
                if (!course.getServiceArea().getOrganizationid().equals(course.getOrganizationid())) {
                    // må hente servicearea som passer
                    ServiceArea search = new ServiceArea();
                    search.setOrganizationid(course.getOrganizationid());
                    List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                    Iterator<ServiceArea> sit = serviceAreas.iterator();
                    while (sit.hasNext()) {
                        ServiceArea serviceArea = sit.next();
                        if (course.getServiceArea().getName().equals(serviceArea.getName())) {
                            course.setServiceArea(serviceArea);
                            course.setServiceAreaid(serviceArea.getId());
                            save = true;
                            break;
                        }
                    }
                }
                if (course.getCategory() == null || course.getCategoryid() == 0) {
                    Category category = categoryManager.getCategory(Category.Name.HENDELSE.getDBValue());
                    course.setCategory(category);
                    course.setCategoryid(category.getId());
                    save = true;
                }

                // chargeoverdue
                if (course.getChargeoverdue() == null) {
                    course.setChargeoverdue(false);
                    save = true;
                }
                if (save) {
                    courseManager.saveCourse(course);
                }
            }
        }
    }

    /**
     * @since 1.5
     */
    private void updateServiceAreas() {
        List<ServiceArea> serviceAreas = serviceAreaManager.getAllIncludingDisabled();
        List<Organization> organizations = organizationManager.getAllIncludingDisabled();
        if (serviceAreas != null && !serviceAreas.isEmpty()) {
            Iterator<ServiceArea> it = serviceAreas.iterator();
            while (it.hasNext()) {
                ServiceArea serviceArea = it.next();
                if (serviceArea.getOrganizationid() == -1) {
                    for (int i = 0; i < organizations.size(); i++) {
                        Organization organization = organizations.get(i);
                        if (i == 0) { // oppdater fÃ¸rste og lag kopier
                            // etterpÃ¥.
                            serviceArea.setOrganization(organization);
                            serviceArea.setOrganizationid(organization.getId());
                            serviceAreaManager.saveServiceArea(serviceArea);
                        } else {
                            ServiceArea newServiceArea = new ServiceArea();
                            newServiceArea.setName(serviceArea.getName());
                            newServiceArea.setSelectable(serviceArea.getSelectable());
                            newServiceArea.setOrganization(organization);
                            newServiceArea.setOrganizationid(organization.getId());
                            serviceAreaManager.saveServiceArea(newServiceArea);
                        }
                    }
                }
            }
        }
    }

    /**
     * @since 1.7
     */
    private void updateConfigurations() {
        /**
         * DB has changed from key, value to key, active, value. Most values in DB are boolean defined as strings i
         * value field, this method moves values to correct new field
         */
        List<Configuration> configurations = configurationManager.getConfigurations();
        if (configurations != null) {
            Iterator<Configuration> iterator = configurations.iterator();
            while (iterator.hasNext()) {
                Configuration configuration = iterator.next();
                String name = configuration.getName();
                String value = configuration.getValue();
                boolean updated = false;

                if (value != null && value.equals("true")) {
                    configuration.setActive(new Boolean(true));
                    configuration.setValue(null);
                    updated = true;
                }
                if (value != null && value.equals("false")) {
                    configuration.setActive(new Boolean(false));
                    configuration.setValue(null);
                    updated = true;
                }

                // invers properties from hide* to show*
                if (name.equals("access.registration.hideEmployeeFields")) {
                    configuration.setName("access.registration.showEmployeeFields");
                    if (configuration.getActive())
                        configuration.setActive(new Boolean(false));
                    else
                        configuration.setActive(new Boolean(true));
                    updated = true;
                }

                if (name.equals("access.registration.hideServiceArea")) {
                    configuration.setName("access.registration.showServiceArea");
                    if (configuration.getActive())
                        configuration.setActive(new Boolean(false));
                    else
                        configuration.setActive(new Boolean(true));
                    updated = true;
                }

                if (name.equals("access.registration.hideComment")) {
                    configuration.setName("access.registration.showComment");
                    if (configuration.getActive())
                        configuration.setActive(new Boolean(false));
                    else
                        configuration.setActive(new Boolean(true));
                    updated = true;
                }
                // -- end invers

                if (updated)
                    configurationManager.saveConfiguration(configuration);
            }
        }
    }

    public void executeTask() {
        log.info("running databaseUpdateManager");
        updateDatabase();
    }
}
