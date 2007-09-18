/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 20.des.2005
 */
package no.unified.soak.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import java.util.Date;


/**
 * This class is used to hold reservations
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="registration"
 */
public class Registration extends BaseObject implements Serializable {
    /**
     * Eclipse generated UID
     */
    private static final long serialVersionUID = 2175157764439098070L;
    private Long id;
    private Course course;
    private Municipalities municipality;
    private ServiceArea serviceArea;
    private Integer eZUserId;
    private String jobTitle;
    private Integer employeeNumber;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String mobilePhone;
    private String useMailAddress;
    private Boolean reserved;
    private Date registered;
    private Boolean invoiced;
    private Long municipalityid;
    private Long courseid;
    private Long serviceareaid;

    /**
    * Default constructor
    */
    public Registration() {
        // Set default values to the required attributes
        reserved = new Boolean(false);
        registered = new Date();
        invoiced = new Boolean(false);
    }

    /**
     * @return Returns the course.
     * @hibernate.many-to-one not-null="true" column="courseid"
     *                        insert="false" update="false" cascade="none"
     */
    public Course getCourse() {
        return course;
    }

    /**
     * @param course The course to set.
     */
    public void setCourse(Course course) {
        this.course = course;
    }

    /**
     * @return Returns the courseid.
     * @hibernate.property column="courseid" not-null="true"
     */
    public Long getCourseid() {
        return courseid;
    }

    /**
     * @param courseid The courseid to set.
     * @spring.validator type="required"
     */
    public void setCourseid(Long courseid) {
        this.courseid = courseid;
    }

    /**
     * @return Returns the email.
     * @hibernate.property column="email" length="50" not-null="true"
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email The email to set.
     * @spring.validator type="required"
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return Returns the employeeNumber.
     * @hibernate.property column="employeenumber"
     */
    public Integer getEmployeeNumber() {
        return employeeNumber;
    }

    /**
     * @param employeeNumber The employeeNumber to set.
     */
    public void setEmployeeNumber(Integer employeeNumber) {
        this.employeeNumber = employeeNumber;
    }

    /**
     * @return Returns the eZUserId.
     * @hibernate.property column="ezuserid"
     */
    public Integer getEZUserId() {
        return eZUserId;
    }

    /**
     * @param userId The eZUserId to set.
     */
    public void setEZUserId(Integer userId) {
        eZUserId = userId;
    }

    /**
     * @return Returns the firstName.
     * @hibernate.property column="firstname" length="30" not-null="true"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName The firstName to set.
     * @spring.validator type="required"
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * @return Returns the id.
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id
     *            The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return Returns the invoiced.
     * @hibernate.property column="invoiced" not-null="true"
     */
    public Boolean getInvoiced() {
        return invoiced;
    }

    /**
     * @param invoiced The invoiced to set.
     */
    public void setInvoiced(Boolean invoiced) {
        this.invoiced = invoiced;
    }

    /**
     * @return Returns the jobTitle.
     * @hibernate.property column="jobtitle" length="30"
     */
    public String getJobTitle() {
        return jobTitle;
    }

    /**
     * @param jobTitle The jobTitle to set.
     */
    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    /**
     * @return Returns the lastName.
     * @hibernate.property column="lastname" length="30" not-null="true"
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * @param lastName The lastName to set.
     * @spring.validator type="required"
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @return Returns the mobilePhone.
     * @hibernate.property column="mobilephone" length="30"
     *
     */
    public String getMobilePhone() {
        return mobilePhone;
    }

    /**
     * @param mobilePhone The mobilePhone to set.
     */
    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    /**
     * @return Returns the municipality.
     * @hibernate.many-to-one not-null="true" column="municipalityid"
     *                        insert="false" update="false" cascade="none"
     */
    public Municipalities getMunicipality() {
        return municipality;
    }

    /**
     * @param municipality The municipality to set.
     */
    public void setMunicipality(Municipalities municipality) {
        this.municipality = municipality;
    }

    /**
     * @return Returns the municipalityid.
     * @hibernate.property column="municipalityid" not-null="true"
     */
    public Long getMunicipalityid() {
        return municipalityid;
    }

    /**
     * @param municipalityid The municipalityid to set.
     * @spring.validator type="required"
     */
    public void setMunicipalityid(Long municipalityid) {
        this.municipalityid = municipalityid;
    }

    /**
     * @return Returns the phone.
     * @hibernate.property column="phone" length="30"
     */
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone The phone to set.
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * @return Returns the registered.
     * @hibernate.property column="registered" not-null="true"
     */
    public Date getRegistered() {
        return registered;
    }

    /**
     * @param registered The registered to set.
    */
    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    /**
     * @return Returns the reserved.
     * @hibernate.property column="reserved" not-null="true"
     */
    public Boolean getReserved() {
        return reserved;
    }

    /**
     * @param reserved The reserved to set.
     */
    public void setReserved(Boolean reserved) {
        this.reserved = reserved;
    }

    /**
     * @return Returns the serviceArea.
     * @hibernate.many-to-one not-null="true" column="serviceareaid"
     *                        insert="false" update="false" cascade="none"
     */
    public ServiceArea getServiceArea() {
        return serviceArea;
    }

    /**
     * @param serviceArea The serviceArea to set.
     */
    public void setServiceArea(ServiceArea serviceArea) {
        this.serviceArea = serviceArea;
    }

    /**
     * @return Returns the serviceareaid.
     * @hibernate.property column="serviceareaid" not-null="true"
     */
    public Long getServiceareaid() {
        return serviceareaid;
    }

    /**
     * @param serviceareaid The serviceareaid to set.
     * @spring.validator type="required"
     */
    public void setServiceareaid(Long serviceareaid) {
        this.serviceareaid = serviceareaid;
    }

    /**
     * @return Returns the useMailAddress.
     * @hibernate.property column="usemailaddress" length="100"
     */
    public String getUseMailAddress() {
        return useMailAddress;
    }

    /**
     * @param useMailAddress The useMailAddress to set.
     */
    public void setUseMailAddress(String useMailAddress) {
        this.useMailAddress = useMailAddress;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Registration)) {
            return false;
        }

        Registration castOther = (Registration) other;

        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("eZUserId", eZUserId)
                                        .append("jobTitle", jobTitle)
                                        .append("employeeNumber", employeeNumber)
                                        .append("firstName", firstName)
                                        .append("lastName", lastName)
                                        .append("reserved", reserved)
                                        .append("invoiced", invoiced)
                                        .append("municipalityid", municipalityid)
                                        .append("courseid", courseid)
                                        .append("serviceareaid", serviceareaid)
                                        .toString();
    }
}
