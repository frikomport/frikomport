/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import no.unified.soak.validation.DigitsOnly;
import no.unified.soak.validation.MinLength;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;


/**
 * User class
 *
 * This class is used to generate Spring Validation rules
 * as well as the Hibernate mapping file.
 *
 * <p><a href="User.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 *         Updated by Dan Kibler (dan@getrolling.com)
 *
 * @hibernate.class table="app_user" lazy="false"
 */
public class User extends BaseObject implements Serializable {
    private static final long serialVersionUID = 3832626162173359411L;
    protected String username;
    protected String password;
    protected String confirmPassword;
    protected String firstName;
    protected String lastName;
    protected Address address = new Address();
    protected String phoneNumber;
    protected String email;
    protected String website;
    protected String passwordHint;
    protected Integer version;
    protected Set roles = new HashSet();
    protected Boolean enabled;
    protected Organization organization;
    protected Organization organization2;
    protected Long organizationid;
    protected Long organization2id;
    protected String mobilePhone;
    protected Integer employeeNumber;
    protected String jobTitle;
    protected String workplace;
    protected ServiceArea serviceArea;
    protected Long serviceAreaid;
    protected String hash;
    protected String invoiceName;
    protected Address invoiceAddress = new Address();
    protected String closestLeader;
    protected Boolean hashuser;
    protected Date birthdate;
  
    public User() {
    }

    public User(String username) {
        this.username = username;
    }

    /**
     * Returns the username.
     *
     * @return String
     *
     * @hibernate.id column="username" length="100" generator-class="assigned"
     *               unsaved-value="version"
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password.
     * @return String
     *
     * @hibernate.property column="password" not-null="false"
     */
    public String getPassword() {
        return password;
    }

    /**
     * Returns the confirmedPassword.
     * @return String
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /**
     * Returns the firstName.
     * @return String
     *
     * @hibernate.property column="first_name" not-null="true" length="100"
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Returns the lastName.
     * @return String
     *
     * @hibernate.property column="last_name" not-null="true" length="100"
     */
    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + ' ' + lastName;
    }
    
   /**
     * Returns the address.
     *
     * @return Address
     *
     * @hibernate.component not-null="false"
     */
    public Address getAddress() {
        return address;
    }

    /**
     * Returns the email.  This is an optional field for specifying a
     * different e-mail than the username.
     *
     * @return String
     *
     * @hibernate.property name="email" not-null="true" unique="true" length="100"
     */
    public String getEmail() {
        return email;
    }

    /**
     * Returns the phoneNumber.
     * @return String
     *
     * @hibernate.property column="phone_number" not-null="false"
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Returns the website.
     * @return String
     *
     * @hibernate.property column="website" not-null="false"
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Returns the passwordHint.
     * @return String
     *
     * @hibernate.property column="password_hint" not-null="false"
     */
    public String getPasswordHint() {
        return passwordHint;
    }

    /**
     * Returns the user's roles.
     * @return Set
     *
     * @hibernate.set table="user_role" cascade="all" lazy="false"
     * @hibernate.collection-key column="username"
     * @hibernate.collection-many-to-many class="no.unified.soak.model.Role"
     *                                    column="role_name"
     */
    public Set getRoles() {
        return roles;
    }
    
    
    /**
     * Adds a role for the user
     *
     * @param role
     */
    public void addRole(Role role) {
        getRoles().add(role);
    }

    /**
     * Adds a role for the user
     *
     * @param roles
     */
    public void addRoles(Collection<String> roles) {
        for (String rolleNavn : roles) {
            getRoles().add(new Role(rolleNavn));
        }
    }
    
    /**
     * Removes a role for the user
     *
     * @param role
     */
    public void removeRole(Role role) {
        getRoles().remove(role);
    }

    /**
     * Removes all roles for the user
     *
     */
    public void removeAllRoles() {
    	Set allRoles = getRoles();
    	getRoles().removeAll(allRoles);
    }

    
    /**
     * Sets the username.
     * @param username The username to set
     * @spring.validator type="required"
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Sets the password.
     * @param password The password to set
     *
     */
     /* @spring.validator type="twofields" msgkey="errors.twofields"
     * @spring.validator-args arg1resource="user.password"
     * @spring.validator-args arg1resource="user.confirmPassword"
     * @spring.validator-var name="secondProperty" value="confirmPassword"
    */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Sets the confirmedPassword.
     * @param confirmPassword The confirmed password to set
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    /**
     * Sets the firstName.
     * @spring.validator type="required"
     * @param firstName The firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Sets the lastName.
     * @param lastName The lastName to set
     *
     * @spring.validator type="required"
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Sets the address.
     * @param address The address to set
     *
     */
    public void setAddress(Address address) {
    	if(address == null) {
    		address = new Address();
    	}
        this.address = address;
    }

    /**
     * Sets the email.
     * @param email The email to set
     *
     * @spring.validator type="required"
     * @spring.validator type="email"
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Sets the phoneNumber.
     * @param phoneNumber The phoneNumber to set
     *
     */
    // @spring.validator type="mask" msgkey="errors.phone"
    // @spring.validator-var name="mask" value="${phone}"
	@DigitsOnly
	@MinLength("8")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * Sets the website.
     * @param website The website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    /**
     * @param passwordHint The password hint to set
     *
     */
    public void setPasswordHint(String passwordHint) {
        this.passwordHint = passwordHint;
    }

    /**
     * Sets the roles.
     * @param roles The roles to set
     */
    public void setRoles(Set roles) {
        this.roles = roles;
    }

    /**
     * @return Returns the updated timestamp.
     * @hibernate.version
     */
    public Integer getVersion() {
        return version;
    }

    /**
     * @param version The updated version to set.
     */
    public void setVersion(Integer version) {
        this.version = version;
    }


    /**
     * @return Returns the enabled.
     * @hibernate.property column="enabled"
     */
    public Boolean getEnabled() {
        return enabled;
    }

    /**
     * @param enabled The enabled to set.
     */
    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    /**
     * Returns a list with the rolenames
     */
    public List <String> getRoleNameList() {
    	List<String> rolenames = new ArrayList<String>();

        if (this.roles != null) {
            for (Iterator it = roles.iterator(); it.hasNext();) {
                Role role = (Role) it.next();
                rolenames.add(role.getName());
            }
        }

        return rolenames;
    }

    public void setRoleNameList(List<String> roles) {
        for (String roleName : roles) {
            addRole(new Role(roleName));
        }
    }

    /**
	 * Returns the organization.
	 * @return Organization
	 * @hibernate.many-to-one column="organizationid" insert="false" update="false" not-found="ignore" not-null="false" 
	 */
	public Organization getOrganization() {
		return organization;
	}

    /**
	 * Sets the organization.
	 * 
	 * @param organization
	 *            The organization to set
	 * 
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

    /**
     * @return Returns the organization2.
     * @hibernate.many-to-one not-null="false" column="organization2id" update="false" cascade="none" insert="false"
     */
    public Organization getOrganization2() {
        return organization2;
    }

    public void setOrganization2(Organization organization2) {
        this.organization2 = organization2;
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
     * @return Returns the organization2id.
     * @hibernate.property column="organization2id" not-null="false"
     */
    public Long getOrganization2id() {
        return organization2id;
    }

    /**
     * @param organizationid
     *            The organizationid to set.
     */
    public void setOrganization2id(Long organization2id) {
        this.organization2id = organization2id;
    }

    /**
	 * @return Returns the serviceArea.
	 * @hibernate.many-to-one column="serviceareaid" insert="false" update="false" not-found="ignore" not-null="false"
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
	 * @param serviceAreaid The serviceAreaid to set.
	 */
	public void setServiceAreaid(Long serviceAreaid) {
		this.serviceAreaid = serviceAreaid;
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
	 * @return Returns the jobTitle.
	 * @hibernate.property column="jobtitle" length="100"
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
	@DigitsOnly
	@MinLength("8")
	public void setMobilePhone(String mobilePhone) {
		this.mobilePhone = mobilePhone;
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
    * 
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

    /**
     * @return the hash
	 * @hibernate.property column="hash" 
	 */
	public String getHash() {
		return hash;
	}

	/**
	 * Sets the hash.
	 * 
	 * @param hash
	 *            The hash to set
	 * @spring.validator type="required"
	 */
	public void setHash(String hash) {
		this.hash = hash;
	}

	/**
     * @return true if user has an emailaddress as username
	 * @hibernate.property column="hashuser" not-null="true"
	 */
	public Boolean getHashuser() {
		if(hashuser == null) {
			hashuser = false;
		}
		return hashuser;
	}

	
	/**
     * @param hashuser
     */
	public void setHashuser(Boolean hashuser) {
		if(hashuser == null) {
			hashuser = false;
		}
		this.hashuser = hashuser;
	}
	
	
	 /**
	    * @return date of birth
	    * @hibernate.property column="birthdate" not-null="false"
	    */
		public Date getBirthdate() {
			return birthdate;
		}

		/**
	    * @param birthdate
	    *            The birthdate to set.
	    */
		public void setBirthdate(Date birthdate) {
			this.birthdate = birthdate;
		}

	
	public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof User)) {
            return false;
        }

        final User user = (User) o;

        if ((username != null) ? (!username.equals(user.getUsername())) : (user
				.getUsername() != null)) {
			return false;
		}

		if ((firstName != null) ? (!firstName.equals(user.getFirstName())) : (user
				.getFirstName() != null)) {
			return false;
		}

		if ((lastName != null) ? (!lastName.equals(user.getLastName())) : (user
				.getLastName() != null)) {
			return false;
		}

		if ((email != null) ? (!email.equals(user.getEmail())) : (user
				.getEmail() != null)) {
			return false;
		}
		
		if ((roles != null) ? (!roles.equals(user.getRoles())) : (user
				.getRoles() != null)) {
			return false;
		}
		
		if ((organizationid != null) ? (!organizationid.equals(user.getOrganizationid())) : (user
				.getOrganizationid() != null)) {
			return false;
		}

		if ((organization2id != null) ? (!organization2id.equals(user.getOrganization2id())) : (user
				.getOrganization2id() != null)) {
			return false;
		}

		return true;
    }
    
	public int hashCode() {
        return ((username != null) ? username.hashCode() : 0);
    }

    /**
     * Generated using Commonclipse (http://commonclipse.sf.net)
     */
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE).append("roles",
            this.roles).append("firstName", this.firstName).append("lastName",
            this.lastName).append("passwordHint", this.passwordHint).append("username",
            this.username).append("fullName", this.getFullName()).append("email",
            this.email).append("phoneNumber", this.phoneNumber).append("password",
            this.password).append("address", this.address).append("confirmPassword",
            this.confirmPassword).append("website", this.website).append("version",
            this.getVersion()).append("enabled", this.getEnabled()).toString();
    }
    
    public Address getInvoiceAddressCopy() {
    	if (invoiceAddress == null) {
    		return null;
    	}
    	Address copy = new Address();
    	copy.setAddress(invoiceAddress.getAddress());
    	copy.setCity(invoiceAddress.getCity());
    	copy.setCountry(invoiceAddress.getCountry());
    	copy.setPostalCode(invoiceAddress.getPostalCode());
    	copy.setProvince(invoiceAddress.getProvince());
    	
		return copy;
	}

}
