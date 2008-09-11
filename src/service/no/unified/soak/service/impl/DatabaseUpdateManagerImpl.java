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
import java.util.ArrayList;

import org.springframework.validation.Validator;
import org.springframework.orm.ObjectRetrievalFailureException;
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
        updateUserInvoiceAddress();
        updateUserServiceAreas();
        // Organization updates
        updateOrganizationInvoiceAddress();
        // Course updates
        updateResponsibleUsername();
        updateRolename();
        updateRestrictions();
        updateCourseServiceAreas();
        // Registration updates
        updateRegistrations();
        updateRegistrationsServiceAreas();
    }

    /**
     * Updates servicearea for registrations
     * @since 1.5
     */
    private void updateRegistrationsServiceAreas() {
        List<Registration> registrations = registrationManager.getRegistrations(new Registration());
        if(registrations != null && !registrations.isEmpty()){
            Iterator<Registration> it = registrations.iterator();
            while(it.hasNext()){
                Registration registration = it.next();
                if(registration.getOrganization() != null && registration.getServiceArea() != null){
                    if(!registration.getOrganization().equals(registration.getServiceArea().getOrganization())){
                        ServiceArea search = new ServiceArea();
                        search.setOrganizationid(registration.getOrganizationid());
                        List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                        Iterator<ServiceArea> sit = serviceAreas.iterator();
                        while (sit.hasNext()){
                            ServiceArea serviceArea = sit.next();
                            if(registration.getServiceArea().getName().equals(serviceArea.getName())){
                                registration.setServiceArea(serviceArea);
                                registration.setServiceAreaid(serviceArea.getId());
                                registrationManager.saveRegistration(registration);
                                break;
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Updates serviceareas for users
     * @since 1.5
     */
    private void updateUserServiceAreas() {
        List<User> users = userManager.getUsers(new User());
        if(users != null && !users.isEmpty()){
            Iterator<User> it = users.iterator();
            while(it.hasNext()){
                User user = it.next();
                if(user.getOrganization() != null && user.getServiceArea() != null){
                    if(!user.getOrganization().equals(user.getServiceArea().getOrganization())){
                        ServiceArea search = new ServiceArea();
                        search.setOrganizationid(user.getOrganizationid());
                        List<ServiceArea> serviceAreas = serviceAreaManager.searchServiceAreas(search);
                        Iterator<ServiceArea> sit = serviceAreas.iterator();
                        while (sit.hasNext()){
                            ServiceArea serviceArea = sit.next();
                            if(user.getServiceArea().getName().equals(serviceArea.getName())){
                                user.setServiceArea(serviceArea);
                                user.setServiceAreaid(serviceArea.getId());
                                userManager.updateUser(user);
                                break;
                            }
                        }

                    }
                }
            }
        }
    }

    /**
     * Updates invoiceaddress for organization
     * @since 1.5
     */
    private void updateOrganizationInvoiceAddress(){
        List<Organization> organizations = organizationManager.getAllIncludingDisabled();
        if(organizations != null && !organizations.isEmpty()){
            Iterator<Organization> it = organizations.iterator();
            while(it.hasNext()){
                Organization organization = it.next();
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
    private void updateUserInvoiceAddress(){
        List<User> users = userManager.getUsers(new User());
        if(users != null && !users.isEmpty()){
            Iterator<User> it = users.iterator();
            while(it.hasNext()){
                User user = it.next();
                if(user.getInvoiceAddress() == null){
                    Address invoice = new Address();
                    invoice.setPostalCode("0");
                    user.setInvoiceAddress(invoice);
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
                Registration registration = it.next();
                User admin = userManager.getUser("admin");
                // Only use valid emails
                if(registration.getUsername() == null || registration.getUsername().trim().length() == 0){
                    if(EmailValidator.getInstance().isValid(registration.getEmail())){
                        User user = userManager.findUser(registration.getEmail());
                        if(user == null){
                            user = userManager.addUser(registration.getEmail(), registration.getFirstName(), registration.getLastName(), registration.getEmail(), new Integer(0), null, new Integer(0));
                        }
                        // Connect user with registration
                        registration.setUser(user);
                        registration.setUsername(user.getUsername());
                        registrationManager.saveRegistration(registration);
                    }
                    else {
                        // Connect with admin user
                        registration.setUser(admin);
                        registration.setUsername(admin.getUsername());
                        registrationManager.saveRegistration(registration);
                    }
                }
            }
        }
    }

    /**
     * @since 1.4
     */
    private void updateRolename() {
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(course.getRole().equals("Anonymous")){
                    course.setRole(Constants.ANONYMOUS_ROLE);
                } else if(course.getRole().equals("Ansatt")){
                    course.setRole(Constants.EMPLOYEE_ROLE);
                } else if(course.getRole().equals("Kommuneansatt")){
                    course.setRole(Constants.EMPLOYEE_ROLE);
                } else if(course.getRole().equals("Kursansvarlig")){
                    course.setRole(Constants.INSTRUCTOR_ROLE);
                } else if(course.getRole().equals("Opplaringsansvarlig")){
                    course.setRole(Constants.EDITOR_ROLE);
                } else if(course.getRole().equals("Oppl�ringsansvarlig")){
                    course.setRole(Constants.EDITOR_ROLE);
                } else if(course.getRole().equals("Admin")){
                    course.setRole(Constants.ADMIN_ROLE);
                }
                courseManager.saveCourse(course);
            }
        }
    }

    /**
     * @since 1.4
     */
    private void updateResponsibleUsername() {
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(course.getResponsible() != null && course.getResponsibleUsername() == null){
                    course.setResponsibleUsername(course.getResponsible().getUsername());
                    courseManager.saveCourse(course);
                }
            }
        }
    }

    /**
     * @since 1.5
     */
    private void updateRestrictions(){
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(course.getRestricted() == null){
                    course.setRestricted(false);
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

    /**
     * @since 1.5
     */
    private void updateCourseServiceAreas(){
        List<Course> courses = courseManager.getCourses(new Course());
        if(courses != null && !courses.isEmpty()){
            Iterator<Course> it = courses.iterator();
            while (it.hasNext()){
                Course course = it.next();
                if(!course.getServiceArea().getOrganization().equals(course.getOrganization())){
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
                            courseManager.saveCourse(course);
                            break;
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
