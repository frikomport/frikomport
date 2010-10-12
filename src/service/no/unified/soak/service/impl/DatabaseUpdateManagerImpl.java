package no.unified.soak.service.impl;

import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.sql.DataSource;

import no.unified.soak.Constants;
import no.unified.soak.model.Address;
import no.unified.soak.model.Category;
import no.unified.soak.model.Configuration;
import no.unified.soak.model.Course;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Registration;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.model.User;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.apache.commons.validator.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * User: gv
 * Date: 05.jun.2008
 * Time: 10:26:23
 */
public class DatabaseUpdateManagerImpl extends BaseManager implements DatabaseUpdateManager {

    private JdbcTemplate jt = new JdbcTemplate();
    private DataSource dataSource = null;
    private CourseManager courseManager = null;
    private RegistrationManager registrationManager = null;
    private UserManager userManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private OrganizationManager organizationManager = null;
    private CategoryManager categoryManager = null;
    private ConfigurationManager configurationManager = null;


    // hack for setting messagesource and locale to ApplicationResourcesUtil once
    public void setLocale(Locale locale) {
//        ApplicationResourcesUtil.setLocale(locale);
    }
    public void setMessageSource(MessageSource messageSource) {
        ApplicationResourcesUtil.setMessageSource(messageSource);
    }
    // --- end hack
    
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
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
    
    public void updateDatabase() {
        updateCategories();
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
        //update configuration content
        updateConfigurations();
    }

    private void updateCategories() {
        String sql = "";
        try{
            sql = "select * from category";
            SqlRowSet rowSet = jt.queryForRowSet(sql);
            if(rowSet.next()){
                log.debug(rowSet.toString());
            } else {
                sql = "insert into category values (1,'Kurs',true)";
                jt.execute(sql);
            }
        }
        catch(Exception e){
            log.info("Could not insert category");
        }
    }

    /**
     * Updates invoiceaddress for organization
     * @since 1.5
     */
    private void updateOrganizations(){
        List<Organization> organizations = organizationManager.getAllIncludingDisabled();
        if(organizations != null && !organizations.isEmpty()){
            Iterator<Organization> it = organizations.iterator();
            while(it.hasNext()){
                Organization organization = it.next();
                // update invoiceaddress
                if(organization.getInvoiceAddress() == null){
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
     * @since 1.5
     */
    private void updateUsers(){
        List<User> users = userManager.getUsers(new User());
        if(users != null && !users.isEmpty()){
            Iterator<User> it = users.iterator();
            while(it.hasNext()){
                boolean save = false;
                User user = it.next();
                // Updates invoice address
                if(user.getInvoiceAddress() == null){
                    Address invoice = new Address();
                    invoice.setPostalCode("0");
                    user.setInvoiceAddress(invoice);
                    save = true;
                }
                // updates serviceareas
                if(user.getOrganization() != null && user.getServiceArea() != null){
                    if(!user.getOrganizationid().equals(user.getServiceArea().getOrganizationid())){
                        ServiceArea search = new ServiceArea();
                        search.setOrganizationid(user.getOrganizationid());
                        List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                        Iterator<ServiceArea> sit = serviceAreas.iterator();
                        while (sit.hasNext()){
                            ServiceArea serviceArea = sit.next();
                            if(user.getServiceArea().getName().equals(serviceArea.getName())){
                                user.setServiceArea(serviceArea);
                                user.setServiceAreaid(serviceArea.getId());
                                save = true;
                                break;
                            }
                        }
                    }
                }
                //sanitycheck for email
                if("".equals(user.getEmail()) && user.getUsername().contains("@")){
                    user.setEmail(user.getUsername());
                    save = true;
                }
                if(save){
                    userManager.updateUser(user);
                }
            }
        }
    }

    /**
     * Checks the registrations table and creates users based on email addresses
     * @since 1.4
     */
    private void updateRegistrations() {
        List<Registration> registrations = registrationManager.getRegistrations(new Registration());
        if(registrations != null && !registrations.isEmpty()){
            Iterator<Registration> it = registrations.iterator();
            while (it.hasNext()){
                boolean save = false;
                Registration registration = it.next();
                User admin = userManager.getUser("admin");
                // Checks the registrations table and creates users based on email addresses
                if(registration.getUsername() == null || registration.getUsername().trim().length() == 0){
                    if(EmailValidator.getInstance().isValid(registration.getEmail())){
                        User user = userManager.findUser(registration.getEmail());
                        if(user == null){
                            user = userManager.addUser(registration.getEmail(), registration.getFirstName(), registration.getLastName(), registration.getEmail(), new Integer(0), null, new Integer(0));
                        }
                        // Connect user with registration
                        registration.setUser(user);
                        registration.setUsername(user.getUsername());
                        save = true;
                    }
                    else {
                        // Connect with admin user
                        registration.setUser(admin);
                        registration.setUsername(admin.getUsername());
                        save = true;
                    }
                }
                // updates the invoice address
                if(registration.getInvoiceAddress() == null){
                    Address invoice = new Address();
                    invoice.setPostalCode("0");
                    registration.setInvoiceAddress(invoice);
                    save = true;
                }
                // updates serviceareas
                if(registration.getOrganization() != null && registration.getServiceArea() != null){
                    if(!registration.getOrganizationid().equals(registration.getServiceArea().getOrganizationid())){
                        ServiceArea search = new ServiceArea();
                        search.setOrganizationid(registration.getOrganizationid());
                        List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                        Iterator<ServiceArea> sit = serviceAreas.iterator();
                        while (sit.hasNext()){
                            ServiceArea serviceArea = sit.next();
                            if(registration.getServiceArea().getName().equals(serviceArea.getName())){
                                registration.setServiceArea(serviceArea);
                                registration.setServiceAreaid(serviceArea.getId());
                                save = true;
                                break;
                            }
                        }
                    }
                }
                if(save){
                    try {
                        registrationManager.saveRegistration(registration);
                    } catch (Exception e) {
                        log.error("Could not update registration" + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * @since 1.4
     */
    private void updateCourses() {
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                boolean save = false;
                Course course = it.next();
                if(course.getRole().equals("Anonymous")){
                    course.setRole(Constants.ANONYMOUS_ROLE);
                    save = true;
                } else if(course.getRole().equals("Ansatt")){
                    course.setRole(Constants.EMPLOYEE_ROLE);
                    save = true;
                } else if(course.getRole().equals("Kommuneansatt")){
                    course.setRole(Constants.EMPLOYEE_ROLE);
                    save = true;
                } else if(course.getRole().equals("Kursansvarlig")){
                    course.setRole(Constants.INSTRUCTOR_ROLE);
                    save = true;
                } else if(course.getRole().equals("Opplaringsansvarlig")){
                    course.setRole(Constants.EDITOR_ROLE);
                    save = true;
                } else if(course.getRole().equals("Oppl�ringsansvarlig")){
                    course.setRole(Constants.EDITOR_ROLE);
                    save = true;
                } else if(course.getRole().equals("Admin")){
                    course.setRole(Constants.ADMIN_ROLE);
                    save = true;
                }
                // responsible username
                if(course.getResponsible() != null && course.getResponsibleUsername() == null){
                    course.setResponsibleUsername(course.getResponsible().getUsername());
                    save = true;
                }
                // restrictions
                if(course.getRestricted() == null){
                    course.setRestricted(false);
                    save = true;
                }
                // service areas
                if(!course.getServiceArea().getOrganizationid().equals(course.getOrganizationid())){
                    // m� hente servicearea som passer
                    ServiceArea search = new ServiceArea();
                    search.setOrganizationid(course.getOrganizationid());
                    List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                    Iterator<ServiceArea> sit = serviceAreas.iterator();
                    while (sit.hasNext()){
                        ServiceArea serviceArea = sit.next();
                        if(course.getServiceArea().getName().equals(serviceArea.getName())){
                            course.setServiceArea(serviceArea);
                            course.setServiceAreaid(serviceArea.getId());
                            save = true;
                            break;
                        }
                    }
                }
                if(course.getCategory() == null || course.getCategoryid() == 0){
                    Category category = categoryManager.getCategory(new Long(1));
                    course.setCategory(category);
                    course.setCategoryid(category.getId());
                    save = true;
                }

                // chargeoverdue
                if(course.getChargeoverdue() == null){
                    course.setChargeoverdue(false);
                    save = true;
                }
                if(save){
                    try {
                        courseManager.saveCourse(course);
                    } catch (Exception e) {
                        log.error("Could not save course." + e.getMessage());
                    }
                }
            }
        }
    }

    /**
     * @since 1.5
     */
    private void updateServiceAreas(){
        List<ServiceArea> serviceAreas = serviceAreaManager.getAllIncludingDisabled();
        List<Organization> organizations = organizationManager.getAllIncludingDisabled();
        if(serviceAreas != null && !serviceAreas.isEmpty()){
            Iterator<ServiceArea> it = serviceAreas.iterator();
            while(it.hasNext()){
                ServiceArea serviceArea = it.next();
                if(serviceArea.getOrganizationid() == -1){
                    for(int i = 0; i < organizations.size() ; i++){
                        Organization organization = organizations.get(i);
                        if(i == 0){ // oppdater første og lag kopier etterpå.
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
		 * DB has changed from key, value to key, active, value. Most values in
		 * DB are boolean defined as strings i value field, this method moves
		 * values to correct new field
		 */
    	List<Configuration> configurations = configurationManager.getConfigurations();
    	if(configurations != null) {
    		Iterator<Configuration> iterator = configurations.iterator();
    		while(iterator.hasNext()) {
    			Configuration configuration = iterator.next();
    			String name = configuration.getName();
    			String value = configuration.getValue();
    			boolean updated = false;

    			if(value != null && value.equals("true")) {
    				configuration.setActive(new Boolean(true));
    				configuration.setValue(null);
    				updated = true;
    			}
    			if(value != null && value.equals("false")) {
    				configuration.setActive(new Boolean(false));
    				configuration.setValue(null);
    				updated = true;
    			}

    			// invers properties from hide* to show*
    			if(name.equals("access.registration.hideEmployeeFields")) {
    				configuration.setName("access.registration.showEmployeeFields");
    				if(configuration.getActive()) configuration.setActive(new Boolean(false));
    				else configuration.setActive(new Boolean(true));
    				updated = true;
    			}

    			if(name.equals("access.registration.hideServiceArea")) {
    				configuration.setName("access.registration.showServiceArea");
    				if(configuration.getActive()) configuration.setActive(new Boolean(false));
    				else configuration.setActive(new Boolean(true));
    				updated = true;
    			}

    			if(name.equals("access.registration.hideComment")) {
    				configuration.setName("access.registration.showComment");
    				if(configuration.getActive()) configuration.setActive(new Boolean(false));
    				else configuration.setActive(new Boolean(true));
    				updated = true;
    			}
    			// -- end invers
    			
    			if(updated) configurationManager.saveConfiguration(configuration);
    		}
    	}
    	// Create new configurations
    	if(!configurationManager.exists("access.course.showAdditionalInfo")) {
    	    configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.course.showAdditionalInfo", false));
    	}
    	if(!configurationManager.exists("access.course.singleprice")) {
    	    configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.course.singleprice", false));
    	}
    	if(!configurationManager.exists("access.course.filterlocation")) {
    	    configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.course.filterlocation", false));
    	}
        if(!configurationManager.exists("access.registration.delete")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.delete", false));
        }
        if(!configurationManager.exists("access.registration.anonymous")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.anonymous", false));
        }
        if(!configurationManager.exists("access.registration.userdefaults")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.userdefaults", false));
        }
        if(!configurationManager.exists("access.registration.emailrepeat")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.emailrepeat", false));
        }
        if(!configurationManager.exists("access.registration.showEmployeeFields")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.showEmployeeFields", true));
        }
        if(!configurationManager.exists("access.registration.showServiceArea")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.showServiceArea", true));
        }
        if(!configurationManager.exists("access.registration.showComment")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"access.registration.showComment", true));
        }
        if(!configurationManager.exists("mail.registration.notifyResponsible")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"mail.registration.notifyResponsible", false));
        }
        if(!configurationManager.exists("mail.course.sendSummary")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"mail.course.sendSummary", true));
        }
        if(!configurationManager.exists("show.menu")) {
            configurationManager.saveConfiguration(new Configuration(configurationManager.nextId(),"show.menu", false));
        }
    }
    
    public void executeTask() {
        log.info("running databaseUpdateManager");
        updateDatabase();
    }
}
