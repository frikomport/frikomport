package no.unified.soak.model;

import java.io.Serializable;
import java.util.Date;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;

import no.unified.soak.validation.LessThanField;
import no.unified.soak.validation.Required;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * This class is used to represent a 65+ course follow-up
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author henhed
 * @hibernate.class table="followup" lazy="false"
 */
public class Followup extends BaseObject implements Serializable {

	private static final long serialVersionUID = -4419585793047249127L;
	
	private Long id;
	private Course course;
	private Date startTime;
    private Date stopTime;
    private Date reminder;
    private Location location;
    //private Long locationid;
    
    /**
     * @return Returns the id.
     * @hibernate.id column="id" generator-class="native" unsaved-value="null"
     */
    @XmlElement(name="oppfolgingId")
    public Long getId() {
        return id;
    }

    /**
     * @param id  The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }
    
    /**
     * @return
     * @hibernate.many-to-one not-null="true"
     *                        column="courseid"
     *                        class="no.unified.soak.model.Course"
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
     * @return Returns the startTime.
     * @hibernate.property column="starttime" not-null="true"
     */
    @XmlElement(name="start")
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            The startTime to set.
     * @spring.validator type="required"
     */
    @Required
    @LessThanField("stopTime")
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Returns the stopTime.
     * @hibernate.property column="stoptime" not-null="true"
     */
    @XmlElement(name="slutt")
    public Date getStopTime() {
        return stopTime;
    }

    /**
     * @param stopTime
     *            The stopTime to set.
     * @spring.validator type="required"
     */
    @Required
    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }
    
    /**
     * @return Returns the reminder.
     * @hibernate.property column="reminder"
     */
	@XmlTransient
    public Date getReminder() {
        return reminder;
    }

    /**
     * @param reminder
     *            The reminder to set.
     */
    public void setReminder(Date reminder) {
        this.reminder = reminder;
    }
	
	/**
     * @return Returns the location.
     * @hibernate.one-to-one not-null="true"
     *                        column="locationid"
     *                        class="no.unified.soak.model.Location"
     */
    @XmlElement(name="lokale")
    public Location getLocation() {
        return location;
    }

    /**
     * @param location
     *            The location to set.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
     * @return Returns the locationid.
     * @hibernate.property column="locationid" not-null="true"
     */
    /*@XmlTransient
    public Long getLocationid() {
        return locationid;
    }*/

    /**
     * @param locationid
     *            The locationid to set.
     * @spring.validator type="required"
     */
    /*@Required
    public void setLocationid(Long locationid) {
        this.locationid = locationid;
    }*/
	
	/**
     * @see java.lang.Object#toString()
     */
	@Override
	public String toString() {
		return new ToStringBuilder(this).append("id", id)
                .append("course", course)
                .append("location", location)
                .append("startTime", startTime)
                .toString();
	}

	/**
     * @see java.lang.Object#equals(Object) ()
     */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Followup)) {
            return false;
        }

        return new EqualsBuilder().append(id, ((Followup)other).id).isEquals();
	}

	/**
     * @see java.lang.Object#hashCode()
     */
	@Override
	public int hashCode() {
		return new HashCodeBuilder().append(id).toHashCode();
	}

}
