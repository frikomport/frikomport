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
 * @hibernate.class table="registration"
 */
public class Registration extends BaseObject implements Serializable {
	/**
	 * Eclipse generated UID
	 */
	private static final long serialVersionUID = 2175157764439098070L;

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

	private String phone;

	private String mobilePhone;

	private String useMailAddress;

	private Boolean reserved;

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
	/**
	 * Default constructor
	 */
	public Registration() {
		// Set default values to the required attributes
		reserved = new Boolean(false);
		registered = new Date();
		invoiced = new Boolean(false);
		attended = new Boolean(false);
	}

	/**
	 * @return Returns the course.
	 * @hibernate.many-to-one not-null="true" column="courseid" insert="false"
	 *                        update="false" cascade="none"
	 */
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
	 * @hibernate.many-to-one not-null="false" column="username" insert="false" update="false" cascade="none"
	 */
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
	 * @hibernate.property column="email" not-null="true"
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            The email to set.
	 * @spring.validator type="required"
     * @spring.validator type="email"
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
	 * @param employeeNumber
	 *            The employeeNumber to set.
	 */
	public void setEmployeeNumber(Integer employeeNumber) {
		this.employeeNumber = employeeNumber;
	}

	/**
	 * @return Returns the firstName.
	 * @hibernate.property column="firstname" length="30" not-null="true"
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @param firstName
	 *            The firstName to set.
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
	 * @param invoiced
	 *            The invoiced to set.
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
	 * @param jobTitle
	 *            The jobTitle to set.
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
	 * @param lastName
	 *            The lastName to set.
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
	 * @param mobilePhone
	 *            The mobilePhone to set.
	 */
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
	}

	/**
	 * @return Returns the locale with which this registration was performed.
	 * @hibernate.property column="locale" length="10" not-null="true"
	 */
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
	 * @hibernate.many-to-one not-null="false" column="organizationid" not-found="ignore"
	 *                        insert="false" update="false" cascade="none"
	 */
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
	public Long getOrganizationid() {
		return organizationid;
	}

	/**
	 * @param organizationid
	 *            The organizationid to set.
	 */
	public void setOrganizationid(Long organizationid) {
		this.organizationid = organizationid;
	}

	/**
	 * @return Returns the phone.
	 * @hibernate.property column="phone" length="30"
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            The phone to set.
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
	 * @param registered
	 *            The registered to set.
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
	 * @param reserved
	 *            The reserved to set.
	 */
	public void setReserved(Boolean reserved) {
		this.reserved = reserved;
	}

	/**
	 * @return Returns the serviceArea.
	 * @hibernate.many-to-one not-null="false" column="serviceareaid" not-found="ignore"
	 *                        insert="false" update="false" cascade="none"
	 */
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
		return new ToStringBuilder(this).append("id", id).append("jobTitle", jobTitle).append("employeeNumber",
				employeeNumber).append("firstName", firstName).append("lastName", lastName)
				.append("reserved", reserved).append("invoiced", invoiced).append("organizationid", organizationid)
				.append("courseid", courseid).append("serviceareaid", serviceAreaid).toString();
	}

    /**
	 * @return the comment
	 * @hibernate.property column="comment" length="100"
	 */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment the comment to set

     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * @return the attended
     * @hibernate.property column="attended" not-null="true"
     */
    public Boolean getAttended() {
        return attended;
    }

    /**
     * @param attended the attended to set
     */
    public void setAttended(Boolean attended) {
        this.attended = attended;
    }

    /**
     * @return the workplace
     * @hibernate.property column="workplace" length="100"
     */
    public String getWorkplace() {
        return workplace;
    }

    /**
     * @param workplace the workplace to set
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
	public Address getInvoiceAddress() {
		return invoiceAddress;
	}

	  /**
     * Sets the invoice address.
     * @param invoiceAddress The invoice address to set
     *
     */
	public void setInvoiceAddress(Address invoiceAddress) {
		this.invoiceAddress = invoiceAddress;
	}

	 /**
     * @return the invoice name.
     * @hibernate.property column="invoice_name" not-null="false"
     */
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
}
