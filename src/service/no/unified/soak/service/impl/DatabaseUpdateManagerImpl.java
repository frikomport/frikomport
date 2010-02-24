package no.unified.soak.service.impl;

import java.util.ArrayList;
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

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.validator.EmailValidator;
import org.springframework.context.MessageSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.jdbc.support.rowset.SqlRowSetMetaData;

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

    public void updateDatabase() {
        insertBySQLStatements();
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

    private void insertBySQLStatements() {

        String[][] sqlSelectAndInsertRoleArray = {
                // Role insert
                { "select count(*) from role where name='anonymous';",
                        "INSERT INTO role (name, description, version) VALUES('anonymous', 'Anonymous', 1);" },
                { "select count(*) from role where name='admin';",
                        "INSERT INTO role (name, description, version) VALUES('admin', 'Administrator', 1);" },
                { "select count(*) from role where name='employee';",
                        "INSERT INTO role (name, description, version) VALUES('employee', 'Ansatt', 1);" },
                { "select count(*) from role where name='instructor';",
                        "INSERT INTO role (name, description, version) VALUES('instructor', 'Kursansvarlig', 1);" },
                { "select count(*) from role where name='editor';",
                        "INSERT INTO role (name, description, version) VALUES('editor', 'Opplaringsansvarlig', 1);" } };
        insertIntoTableBySQLStatements("role", sqlSelectAndInsertRoleArray);

        String[][] sqlSelectAndInsertCategoryArray = { { "select count(*) from category;",
                "INSERT INTO category (name, selectable) VALUES ('Hendelse', true);" } };
        insertIntoTableBySQLStatements("category", sqlSelectAndInsertCategoryArray);

        //Insert configuration that are common for all environments. 
        String[][] sqlSelectAndInsertConfigurationArray = {
                // Configuration insert
                { "select count(*) from configuration where name = 'access.registration.delete';",
                        "insert INTO configuration (name, active) VALUES ('access.registration.delete', false);" },
                { "select count(*) from configuration where name = 'access.registration.userdefaults';",
                        "insert INTO configuration (name, active) VALUES ('access.registration.userdefaults', false);" },
                { "select count(*) from configuration where name = 'access.registration.emailrepeat';",
                        "insert INTO configuration (name, active) VALUES ('access.registration.emailrepeat', false);" },
                { "select count(*) from configuration where name = 'access.registration.showComment';",
                        "insert INTO configuration (name, active) VALUES ('access.registration.showComment', true);" },
                { "select count(*) from configuration where name = 'mail.course.sendSummary';",
                        "insert INTO configuration (name, active) VALUES ('mail.course.sendSummary', true);" },
                { "select count(*) from configuration where name = 'mail.registration.notifyResponsible';",
                        "insert INTO configuration (name, active) VALUES ('mail.registration.notifyResponsible', false);" },
                { "select count(*) from configuration where name = 'show.menu';",
                        "insert INTO configuration (name, active) VALUES ('show.menu', false);" } };
        insertIntoTableBySQLStatements("configuration", sqlSelectAndInsertConfigurationArray);

        String localeVariant = ApplicationResourcesUtil.getLocaleVariant();
        if (localeVariant != null && localeVariant.equalsIgnoreCase("FKPSVV")) {
            String[][] sqlSelectAndInsertOrganizationArray = {
                    { "select count(*) from organization where name='Østfold'",
                            "insert into organization (name, number, type, selectable) values ('Østfold', 01, 2, true)" },
                    { "select count(*) from organization where name='Akershus'",
                            "insert into organization (name, number, type, selectable) values ('Akershus', 02, 2, true)" },
                    { "select count(*) from organization where name='Oslo'",
                            "insert into organization (name, number, type, selectable) values ('Oslo', 03, 2, true)" },
                    { "select count(*) from organization where name='Hedmark'",
                            "insert into organization (name, number, type, selectable) values ('Hedmark', 04, 2, true)" },
                    { "select count(*) from organization where name='Oppland'",
                            "insert into organization (name, number, type, selectable) values ('Oppland', 05, 2, true)" },
                    { "select count(*) from organization where name='Buskerud'",
                            "insert into organization (name, number, type, selectable) values ('Buskerud', 06, 2, true)" },
                    { "select count(*) from organization where name='Vestfold'",
                            "insert into organization (name, number, type, selectable) values ('Vestfold', 07, 2, true)" },
                    { "select count(*) from organization where name='Telemark'",
                            "insert into organization (name, number, type, selectable) values ('Telemark', 08, 2, true)" },
                    { "select count(*) from organization where name='Aust-Agder'",
                            "insert into organization (name, number, type, selectable) values ('Aust-Agder', 09, 2, true)" },
                    { "select count(*) from organization where name='Vest-Agder'",
                            "insert into organization (name, number, type, selectable) values ('Vest-Agder', 10, 2, true)" },
                    { "select count(*) from organization where name='Rogaland'",
                            "insert into organization (name, number, type, selectable) values ('Rogaland', 11, 2, true)" },
                    { "select count(*) from organization where name='Hordaland'",
                            "insert into organization (name, number, type, selectable) values ('Hordaland', 12, 2, true)" },
                    { "select count(*) from organization where name='Sogn og Fjordane'",
                            "insert into organization (name, number, type, selectable) values ('Sogn og Fjordane', 14, 2, true)" },
                    { "select count(*) from organization where name='Møre og Romsdal'",
                            "insert into organization (name, number, type, selectable) values ('Møre og Romsdal', 15, 2, true)" },
                    { "select count(*) from organization where name='Sør-Trøndelag'",
                            "insert into organization (name, number, type, selectable) values ('Sør-Trøndelag', 16, 2, true)" },
                    { "select count(*) from organization where name='Nord-Trøndelag'",
                            "insert into organization (name, number, type, selectable) values ('Nord-Trøndelag', 17, 2, true)" },
                    { "select count(*) from organization where name='Nordland'",
                            "insert into organization (name, number, type, selectable) values ('Nordland', 18, 2, true)" },
                    { "select count(*) from organization where name='Troms'",
                            "insert into organization (name, number, type, selectable) values ('Troms', 19, 2, true)" },
                    { "select count(*) from organization where name='Finnmark'",
                            "insert into organization (name, number, type, selectable) values ('Finnmark', 20, 2, true)" } };
            insertIntoTableBySQLStatements("Organization", sqlSelectAndInsertOrganizationArray);

            String[][] sqlInsertServiceareaArray = { {
                    null,
                    "insert into servicearea (name, selectable, organizationid) "
                            + "(select 'Mengdetrening', true, id from organization O where O.type = 2 and not exists (select null from servicearea S0 where S0.organizationid = O.id))" } };
            insertIntoTableBySQLStatements("servicearea", sqlInsertServiceareaArray);
            
            // Some insert settings are different for FKPSVV enviroment.
            String[][] sqlSelectAndInsertConfigurationSVVArray = {
                    // Configuration insert
                    { "select count(*) from configuration where name = 'access.registration.showEmployeeFields';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showEmployeeFields', false);" },
                    { "select count(*) from configuration where name = 'access.registration.showServiceArea';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showServiceArea', false);" },
                    { "select count(*) from configuration where name = 'access.registration.showJobTitle';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showJobTitle', false);" },
                    { "select count(*) from configuration where name = 'access.registration.showWorkplace';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showWorkplace', false);" } };
            insertIntoTableBySQLStatements("configuration", sqlSelectAndInsertConfigurationSVVArray);

        } else {
            // Some insert settings are different for non-FKPSVV enviroment.
            String[][] sqlSelectAndInsertConfigurationDefaultArray = {
                    // Configuration insert
                    { "select count(*) from configuration where name = 'access.registration.showEmployeeFields';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showEmployeeFields', true);" },
                    { "select count(*) from configuration where name = 'access.registration.showServiceArea';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showServiceArea', true);" },
                    { "select count(*) from configuration where name = 'access.registration.showJobTitle';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showJobTitle', true);" },
                    { "select count(*) from configuration where name = 'access.registration.showWorkplace';",
                            "insert INTO configuration (name, active) VALUES ('access.registration.showWorkplace', true);" }};
            insertIntoTableBySQLStatements("configuration", sqlSelectAndInsertConfigurationDefaultArray);
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
                existCount = jt.queryForInt(aSelectAndInsert[0]);
            }
            if (existCount == 0 && aSelectAndInsert[1] != null) {
                count += jt.update(aSelectAndInsert[1]);
            }
        }
        if (count > 0 && log.isInfoEnabled()) {
            log.info("Number of " + table + " rows inserted (or updated) in database: " + count);
        }
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
            log
                    .info(nRowsAffected
                            + " rows affected by convertion from reserved to status field in database. The field \"reserved\" in table \"registration\" can be dropped.");
        }
    }

    private boolean reservedFieldExist() {
        SqlRowSet rowSet = jt.queryForRowSet("select * from registration limit 1");
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
                        User user = userManager.findUser(registration.getEmail());
                        if (user == null) {
                            user = userManager.addUser(registration.getEmail(), registration.getFirstName(), registration
                                    .getLastName(), registration.getEmail(), new Integer(0), null, new Integer(0));
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
