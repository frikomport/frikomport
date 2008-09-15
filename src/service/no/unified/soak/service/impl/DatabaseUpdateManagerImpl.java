package no.unified.soak.service.impl;

import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.model.ServiceArea;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Address;
import no.unified.soak.Constants;

import java.util.Locale;
import java.util.List;
import java.util.Iterator;

import org.apache.commons.validator.EmailValidator;

/**
 * User: gv
 * Date: 05.jun.2008
 * Time: 10:26:23
 */
public class DatabaseUpdateManagerImpl extends BaseManager implements DatabaseUpdateManager {

    private CourseManager courseManager = null;
    private RegistrationManager registrationManager = null;
    private UserManager userManager = null;
    private ServiceAreaManager serviceAreaManager = null;
    private OrganizationManager organizationManager = null;

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

    public void updateDatabase() {
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
                    // må hente servicearea som passer
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
                if(save){
                    courseManager.saveCourse(course);
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

    public void executeTask() {
        log.info("running databaseUpdateManager");
        updateDatabase();
    }

    public void setLocale(Locale locale) {
        // Do nothing
    }
}
