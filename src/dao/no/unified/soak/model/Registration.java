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

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import no.unified.soak.validation.DigitsOnly;
import no.unified.soak.validation.Email;
import no.unified.soak.validation.MinLength;
import no.unified.soak.validation.MinValue;
import no.unified.soak.validation.Required;
import no.unified.soak.validation.ValidateOnlyIfConfigurationIsTrue;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class is used to hold reservations
 * 
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 * 
 * @author hrj
 * @hibernate.class table="registration" lazy="false"
 */
@XmlRootElement()
public class Registration extends BaseObject implements Serializable {
	


	public enum Status {
		INVITED(0), WAITING (1), RESERVED(2), CANCELED(3);
		private Integer statusDBValue;
		
		Status(int status) {
			this.statusDBValue = status; 
		}

		public static Status getStatusFromDBValue(Integer dbValue) {
			for (Registration.Status status : values()) {
				if (status.getDBValue().equals(dbValue)) {
					return status;
				}
			}
			return null;
		}
		
		public Integer getDBValue() {
			return statusDBValue;
		}
	}

	/**
	 * Eclipse generated UID
	 */
	private static final long serialVersionUID = 2175157764429098074L;

	private Long id;

	private Course course;

	private User user;

	private Organization organization;

	private ServiceArea serviceArea;

	private String jobTitle;

	private Integer employeeNumber;

	private String firstName;

	private String lastName;

	private String email;

	private String emailRepeat;

	private String phone;

	private String mobilePhone;

	private String useMailAddress;

	private Date registered;

	private Boolean invoiced;

	private Long organizationid;

	private Long courseid;

	private Long serviceAreaid;

	private String username;

	private String locale;

	private String comment;

	private Boolean attended;

	private String workplace;

	private String invoiceName;

	private String closestLeader;

	private Address invoiceAddress = new Address();

	private Status status;
	
    private Date birthdate;

	private Integer participants;

	/**
	 * Default constructor
	 */
	public Registration() {
		// Set default values to the required attributes
		registered = new Date();
		invoiced = Boolean.FALSE;
		attended = Boolean.FALSE;
	}

	
	/**
	 * @return the number of participants or 1 if value is <code>null</code>
	 * @hibernate.property column="participants" not-null="false"
	 */
	@XmlElement(name="deltakere")
	public Integer getParticipants() {
		return participants;
	}


	/**
	 * Sets number of participants. Sets 1 on this object if input parameter is
	 * <code>null</code>.
	 * 
	 * @param participants
	 *            the number of participants to set
	 */
	@Required
	@MinValue("1")
	public void setParticipants(Integer participants) {
		this.participants = participants;
	}


	/**
	 * @return Returns the course.
	 * @hibernate.many-to-one not-null="true" column="courseid" insert="false"
	 *                        update="false" cascade="none"
	 */
	@XmlTransient
	public Course getCourse() {
		return course;
	}

	/**
	 * @param course
	 *            The course to set.
	 */
	public void setCourse(Course course) {
		this.course = course;
	}

	/**
	 * @return Returns the user.
	 * @hibernate.many-to-one not-null="false" column="username" insert="false" update="false" cascade="none" optimistic-lock="false"
	 */
	@XmlTransient
	public User getUser() {
		return user;
	}

	/**
	 * @param user
	 *            The user to set.
	 */
	public void setUser(User user) {
		this.user = user;
	}

	/**
	 * @return Returns the courseid.
	 * @hibernate.property column="courseid" not-null="true"
	 */
	@XmlElement(name="kursId")
	public Long getCourseid() {
		return courseid;
	}

	/**
	 * @param courseid
	 *            The courseid to set.
	 * @spring.validator type="required"
	 */
	public void setCourseid(Long courseid) {
		this.courseid = courseid;
	}

	/**
	 * @return Returns the email.
	 * @hibernate.property column="email" not-null="true" length="50"
	 */
	@XmlElement(name="epost")
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 * @spring.validator type="required"
	 * @spring.validator type="email"
	 */
	@Required
	@Email
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * @return the email repeated
	 */
	@XmlTransient
	public String getEmailRepeat() {
		return emailRepeat;
	}

	/**
	 * 
	 * @param emailRepeat
	 *            The email repeated
	 * 
	 * @spring.validator type="email"
	 */
	public void setEmailRepeat(String emailRepeat) {
		this.emailRepeat = emailRepeat;
	}

	/**
	 * @return Returns the employeeNumber.
	 * @hibernate.property column="employeenumber"
	 */
	@XmlTransient
	public Integer getEmployeeNumber() {
		return employeeNumber;
	}

	/**
	 * @param employeeNumber
	 *            The employeeNumber to set.
	 */
	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	/**
	 * @return Returns the firstName.
	 * @hibernate.property column="firstname" length="100" not-null="true"
	 */
	@XmlElement(name="fornavn")
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            The firstName to set.
	 * @spring.validator type="required"
	 */
	@Required
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * @return Returns the id.
	 * @hibernate.id column="id" generator-class="native" unsaved-value="null"
	 */
	@XmlTransient
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
	@XmlTransient
	public Boolean getInvoiced() {
		return invoiced;
	}

	/**
	 * @param invoiced
	 *            The invoiced to set.
	 */
	public void setInvoiced(Boolean invoiced) {
		this.invoiced = invoiced;
	}

	/**
	 * @return Returns the jobTitle.
	 * @hibernate.property column="jobtitle" length="100"
	 */
	@XmlTransient
	public String getJobTitle() {
		return jobTitle;
	}

	/**
	 * @param jobTitle
	 *            The jobTitle to set.
	 */
	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	/**
	 * @return Returns the lastName.
	 * @hibernate.property column="lastname" length="100" not-null="true"
	 */
	@XmlElement(name="etternavn")
	public String getLastName() {
		return lastName;
	}

	/**
	 * @param lastName
	 *            The lastName to set.
	 * @spring.validator type="required"
	 */
	@Required
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * @return Returns the mobilePhone.
	 * @hibernate.property column="mobilephone" length="30"
	 * 
	 */
	@XmlElement(name="mobil")
	public String getMobilePhone() {
		return mobilePhone;
	}

	/**
	 * @param mobilePhone
	 *            The mobilePhone to set.
	 */
	@DigitsOnly
	@MinLength("8")
	@ValidateOnlyIfConfigurationIsTrue("access.registration.mobilePhone.digitsOnly.minLength8")
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return Returns the locale with which this registration was performed.
	 * @hibernate.property column="locale" length="10" not-null="true"
	 */
	@XmlTransient
	public String getLocale() {
		return locale;
	}

	/**
	 * @param locale
	 *            The locale to set.
	 */
	public void setLocale(String locale) {
		this.locale = locale;
	}

	/**
	 * @return Returns the organization.
	 * @hibernate.many-to-one not-null="false" column="organizationid"
	 *                        not-found="ignore" insert="false" update="false"
	 *                        cascade="none"
	 */
	@XmlTransient
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            The organization to set.
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return Returns the organizationid.
	 * @hibernate.property column="organizationid" not-null="false"
	 */
	@XmlTransient
	public Long getOrganizationid() {
		return organizationid;
	}

	/**
	 * @param organizationid
	 *            The organizationid to set.
	 * @spring.validator type="required"
	 */
	@Required
	public void setOrganizationid(Long organizationid) {
		this.organizationid = organizationid;
	}

	/**
	 * @return Returns the phone.
	 * @hibernate.property column="phone" length="30"
	 */
	@XmlTransient
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            The phone to set.
	 */
	@DigitsOnly
	@MinLength("8")
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return Returns the registered.
	 * @hibernate.property column="registered" not-null="true"
	 */
	@XmlTransient
	public Date getRegistered() {
		return registered;
	}

	/**
	 * @param registered
	 *            The registered to set.
	 */
	public void setRegistered(Date registered) {
		this.registered = registered;
	}

	/**
	 * Gets info of whether the registration is reserved. Used by jsp-files.
	 * 
	 * @return <code>true</code> if the registration is reserved
	 *         <code>false</code> otherwise.
	 * 
	 * @return
	 */
	@XmlTransient
	public boolean getReserved() {
		return getStatusAsEnum() == Status.RESERVED;
	}

	/**
	 * @return Returns the serviceArea.
	 * @hibernate.many-to-one not-null="false" column="serviceareaid"
	 *                        not-found="ignore" insert="false" update="false"
	 *                        cascade="none"
	 */
	@XmlTransient
	public ServiceArea getServiceArea() {
		return serviceArea;
	}

	/**
	 * @param serviceArea
	 *            The serviceArea to set.
	 */
	public void setServiceArea(ServiceArea serviceArea) {
		this.serviceArea = serviceArea;
	}

	/**
	 * @return Returns the serviceAreaid.
	 * @hibernate.property column="serviceareaid" not-null="false"
	 */
	@XmlTransient
	public Long getServiceAreaid() {
		return serviceAreaid;
	}

	/**
	 * @param serviceAreaid
	 *            The serviceAreaid to set.
	 */
	public void setServiceAreaid(Long serviceAreaid) {
		this.serviceAreaid = serviceAreaid;
	}

	/**
	 * @return Returns the username.
	 * @hibernate.property column="username" not-null="false" length="50"
	 */
	@XmlTransient
	public String getUsername() {
		return username;
	}

	/**
	 * @param username
	 *            The usename to set.
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/**
	 * @return Returns the useMailAddress.
	 * @hibernate.property column="usemailaddress" length="100"
	 */
	@XmlTransient
	public String getUseMailAddress() {
		return useMailAddress;
	}

	/**
	 * @param useMailAddress
	 *            The useMailAddress to set.
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
		return new ToStringBuilder(this).append("id", id).append("jobTitle",
				jobTitle).append("employeeNumber", employeeNumber).append(
				"firstName", firstName).append("lastName", lastName).append("email", email).append(
				"status", status).append("invoiced", invoiced).append(
				"organizationid", organizationid).append("courseid", courseid)
				.append("serviceareaid", serviceAreaid).toString();
	}

	/**
	 * @return the comment
	 * @hibernate.property column="`comment`" length="255"
	 */
	@XmlTransient
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment
	 *            the comment to set
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the attended
	 * @hibernate.property column="attended" not-null="true"
	 */
	@XmlTransient
	public Boolean getAttended() {
		return attended;
	}

	/**
	 * @param attended
	 *            the attended to set
	 */
	public void setAttended(Boolean attended) {
		this.attended = attended;
	}

	/**
	 * @return the workplace
	 * @hibernate.property column="workplace" length="100"
	 */
	@XmlTransient
	public String getWorkplace() {
		return workplace;
	}

	/**
	 * @param workplace
	 *            the workplace to set
	 */
	public void setWorkplace(String workplace) {
		this.workplace = workplace;
	}

	/**
	 * 
	 * @return the invoice address
	 * 
	 * @hibernate.component not-null="false" prefix="invoice_"
	 */
	@XmlTransient
	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	/*
	 * Convenience-methods used by JSON - BEGIN
	 */
	public String getPostnr() {
		return invoiceAddress.getPostalCode();
	}
	public void setPostnr(String postnr) {
		invoiceAddress.setPostalCode(postnr);
	}
	public String getAdresse() {
		return invoiceAddress.getAddress();
	}
	public void setAdresse(String adresse) {
		invoiceAddress.setAddress(adresse);
	}
	public String getHash() {
		return user.getHash();
	}
	
	/*
	 * Convenience-methods used by JSON - END
	 */

	/**
	 * Sets the invoice address.
	 * 
	 * @param invoiceAddress
	 *            The invoice address to set
	 * 
	 */
	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	/**
	 * @return the invoice name.
	 * @hibernate.property column="invoice_name" not-null="false"
	 */
	@XmlTransient
	public String getInvoiceName() {
		return invoiceName;
	}

	/**
	 * @param invoiceName
	 *            The invoice name to set.
	 */
	public void setInvoiceName(String invoiceName) {
		this.invoiceName = invoiceName;
	}

	/**
	 * @return the closest leader name.
	 * @hibernate.property column="closest_leader" not-null="false"
	 */
	@XmlTransient
	public String getClosestLeader() {
		return closestLeader;
	}

	/**
	 * @param closestLeader
	 *            The closestLeader to set.
	 */
	public void setClosestLeader(String closestLeader) {
		this.closestLeader = closestLeader;
	}

	/**
	 * Gets info of whether the registration is canceled. Used by jsp-files.
	 * 
	 * @return <code>true</code> if the registration is canceled.
	 *         <code>false</code> otherwise.
	 */
	@XmlTransient
	public Boolean getCanceled() {
		return getStatusAsEnum() == Status.CANCELED;
	}
	
	/**
	 * Gets the status of the registration.
	 * 
	 * @return The registration status
	 * @hibernate.property column="status" not-null="true" type="integer"
	 */
	@XmlTransient
	public Integer getStatus() {
		return (status == null ? null : status.getDBValue());
	}

	@XmlTransient
	public Status getStatusAsEnum() {
		return status;
	}

	public void setStatus(Status status) {
		if (this.status != null && this.status == Status.CANCELED && this.status != status) {
			throw new RuntimeException("Illegal status assignment away from current status CANCELED, for registration " + this);
		}
		this.status = status;
	}

	public void setStatus(Integer status) {
		setStatus(Status.getStatusFromDBValue(status));
	}

	

	/**
	 * @return Returns the date of birth.
	 * @hibernate.property column="birthdate" not-null="false"
	 */
	@XmlElement(name="fodselsdato")
	public Date getBirthdate() {
		return birthdate;
	}

	/**
	 * @param birthdate
	 *            The birthdate to set.
	 * @spring.validator type="required"
	 */
    @Required
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}

	
}
