package no.unified.soak.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class is used to see whether or not a reminder has been sent
 * 
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 * 
 * @author hrj
 * @hibernate.class table="notification"
 */
public class Notification extends BaseObject implements Serializable {
	private static final long serialVersionUID = 2L;

	private Long id;

	private Registration registration;
	
	private Long registrationid;

	private boolean reminderSent;

	private boolean isFollowup;

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
	 * @return Returns the registration.
	 * @hibernate.many-to-one not-null="true" column="registrationid" insert="false"
	 *                        update="false" cascade="none"
	 */
	public Registration getRegistration() {
		return registration;
	}

	/**
	 * @param id
	 *            The registration to set.
	 */
	public void setRegistration(Registration registration) {
		this.registration = registration;
	}

	/**
	 * @return Returns the registered.
	 * @hibernate.property column="reminderSent" not-null="true"
	 */
	public boolean getReminderSent() {
		return reminderSent;
	}

	/**
	 * @param id
	 *            Set reminderSent.
	 */
	public void setReminderSent(boolean reminderSent) {
		this.reminderSent = reminderSent;
	}

	/**
	 * @return Returns if this is a followup reminder.
	 * @hibernate.property column="isFollowup"
	 */
	public boolean getIsFollowup() {
		return isFollowup;
	}

	/**
	 * @param isFollowup
	 *				Set if reminder is for a followup.
	 */
	public void setIsFollowup(boolean isFollowup) {
		this.isFollowup = isFollowup;
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", id).append(
				"registration", registration).append("reminderSent",
				reminderSent).toString();
	}

	public boolean equals(final Object other) {
		if (!(other instanceof Notification))
			return false;
		Notification castOther = (Notification) other;
		return new EqualsBuilder().append(id, castOther.id).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

	/**
	 * @return Returns the registrationid.
	 * @hibernate.property column="registrationid" not-null="true"
	 */
	public Long getRegistrationid() {
		return registrationid;
	}

	/**
	 * @param registrationid the registrationid to set
	 */
	public void setRegistrationid(Long registrationid) {
		this.registrationid = registrationid;
	}
}
