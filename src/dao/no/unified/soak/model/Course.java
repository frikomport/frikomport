/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created on 11.des.2005
 */
package no.unified.soak.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import no.unified.soak.validation.LessThanField;
import no.unified.soak.validation.MinValue;
import no.unified.soak.validation.Required;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * This class is used to represent a course
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="course" lazy="false"
 */
public class Course extends BaseObject implements Serializable {
    /**
     * When adding / removing attributes to this class, remember to update the copyAllButId-function
     */
    private static final long serialVersionUID = -4869033333668709720L;
    private Long id;
    private Long copyid;
    private Category category;
    private Long categoryid;
    private Organization organization;
    private Organization organization2;
    private Long organizationid;
    private Long organization2id;
    private User responsible;
    private Long responsibleid;
    private String responsibleUsername;
    private ServiceArea serviceArea;
    private Long serviceAreaid;
    private Location location;
    private Long locationid;
    private Person instructor;
    private Long instructorid;
    private String name;
    private String type;
    private Date startTime;
    private Date stopTime;
    private String duration;
    private Date registerBy;
    private Date registerStart;
    private Date freezeAttendance;
    private Date reminder;
    private Integer maxAttendants;
    private Integer reservedInternal;
    private String detailURL;
    private Double feeInternal;
    private Double feeExternal;
    private String description;
    private String role;
    private Integer availableAttendants;
    private Integer status = 0;
    private Boolean restricted = false;
    private Boolean chargeoverdue = false;
    private Integer attendants;
    private Set<Registration> registrations;
    private Integer numberOfRegistrations;
    private Integer numberOfParticipants;
    
    public void setNumberOfParticipants(Integer numberOfParticipants) {
		this.numberOfParticipants = numberOfParticipants;
	}

	public Integer getNumberOfParticipants() {
		return numberOfParticipants;
	}

	public Integer getNumberOfRegistrations() {
		return numberOfRegistrations;
	}

	public void setNumberOfRegistrations(Integer numberOfRegistrations) {
		this.numberOfRegistrations = numberOfRegistrations;
	}

	/**
     * @return the registrations
     * @hibernate.set
     *  inverse="true"
     *  cascade="none"
     * @hibernate.collection-key
     *  column="COURSEID"
     * @hibernate.collection-one-to-many class="no.unified.soak.model.Registration"
     */
    public Set<Registration> getRegistrations() {
		return registrations;
	}

	/**
	 * @param registrations the registrations to set
	 */
	public void setRegistrations(Set<Registration> registrations) {
		this.registrations = registrations;
	}

	/**
	 * @return the number of attendants
     * @hibernate.property column="ATTENDANTS" not-null="false"
	 */
	public Integer getAttendants() {
		return attendants;
	}

	/**
	 * @param attendants the number of attendants to set
	 */
    @MinValue("0")
	public void setAttendants(Integer attendants) {
		this.attendants = attendants;
	}

	/**
     * @return
     * @hibernate.many-to-one not-null="false" column="CATEGORYID"
     *                        insert="false" update="false" cascade="none"
     */
    public Category getCategory() {
        return category;
    }

    /**
     * @param category
     */
    public void setCategory(Category category) {
        this.category = category;
    }

    /**
     * @return The categoryid
     * @hibernate.property column="CATEGORYID" not-null="false"
     */
    public Long getCategoryid() {
        return categoryid;
    }

    /**
     * @param categoryid
     * @spring.validator type="required"
     */
    public void setCategoryid(Long categoryid) {
        this.categoryid = categoryid;
    }

    /**
     * @return Returns the role.
     * @hibernate.property column="ROLE" length="50" not-null="true"
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     *            The role to set.
     * @spring.validator type="required"
     */
    @Required
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @hibernate.property column="INSTRUCTORID" not-null="true"
     * @return Returns the instructorid.
     */
    public Long getInstructorid() {
        return instructorid;
    }

    /**
     * @param instructorid
     *            The instructorid to set.
     * @spring.validator type="required"
     */
    @Required
    public void setInstructorid(Long instructorid) {
        this.instructorid = instructorid;
    }

    /**
     * @hibernate.property column="RESPONSIBLEUSERNAME" not-null="false" length="100"
     * @return Returns the responsibleUsername.
     */
    public String getResponsibleUsername() {
        return responsibleUsername;
    }

    /**
     * @param responsibleusername
     *            The responsibleUsername to set.
     * @spring.validator type="required"
     */
    @Required
    public void setResponsibleUsername(String responsibleusername) {
        this.responsibleUsername = responsibleusername;
    }

    /**
     * @hibernate.property column="RESPONSIBLEID" not-null="false"
     * @return Returns the responsibleid.
     */
    public Long getResponsibleid() {
        return responsibleid;
    }

    /**
     * @param responsibleid
     *            The responsibleid to set.
     */
    public void setResponsibleid(Long responsibleid) {
        this.responsibleid = responsibleid;
    }

    /**
     * @hibernate.property column="SERVICEAREAID" not-null="false"
     * @return Returns the serviceAreaid.
     */
    public Long getServiceAreaid() {
        return serviceAreaid;
    }

    /**
     * @param serviceAreaid
     *            The serviceAreaid to set.
     * @spring.validator type="required"
     */
    @Required
    public void setServiceAreaid(Long serviceAreaid) {
        this.serviceAreaid = serviceAreaid;
    }

    /**
     * @return Returns the detailURL.
     * @hibernate.property column="DETAILURL" length="200"
     */
    public String getDetailURL() {
        return detailURL;
    }

    /**
     * @param detailURL
     *            The detailURL to set.
     */
    public void setDetailURL(String detailURL) {
        this.detailURL = detailURL;
    }

    /**
     * @return Returns the duration.
     * @hibernate.property column="DURATION" length="100" not-null="true"
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration
     *            The duration to set.
     * @spring.validator type="required"
     */
    @Required
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return Returns the feeExternal.
     * @hibernate.property column="FEEXTERNAL" not-null="true"
     */
    public Double getFeeExternal() {
        return feeExternal;
    }

    /**
     * @param feeExternal
     *            The feeExternal to set.
     * @spring.validator type="required"
     */
    @Required
    public void setFeeExternal(Double feeExternal) {
        this.feeExternal = feeExternal;
    }

    /**
     * @return Returns the feeInternal.
     * @hibernate.property column="FEEINTERNAL" not-null="true"
     */
    public Double getFeeInternal() {
        return feeInternal;
    }

    /**
     * @param feeInternal
     *            The feeInternal to set.
     * @spring.validator type="required"
     */
    @Required
    public void setFeeInternal(Double feeInternal) {
        this.feeInternal = feeInternal;
    }

    /**
     * @return Returns the freezeAttendance.
     * @hibernate.property column="FREEZEATTENDANCE" not-null="false"
     */
    public Date getFreezeAttendance() {
        return freezeAttendance;
    }

    /**
     * @param freezeAttendance
     *            The freezeAttendance to set.
     */
    public void setFreezeAttendance(Date freezeAttendance) {
        this.freezeAttendance = freezeAttendance;
    }

    /**
     * @return Returns the id.
     * @hibernate.id column="ID" generator-class="native" unsaved-value="null"
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
     * @return Returns the copyid.
     * @hibernate.property column="COPYID" not-null="false"
     */
    public Long getCopyid() {
        return copyid;
    }

    /**
     * @param copyid
     *            The copyid to set.
     */
    public void setCopyid(Long copyid) {
        this.copyid = copyid;
    }

    /**
     *
     * @return Returns the maxAttendants.
     * @hibernate.property column="MAXATTENDANTS" not-null="true"
     */
    public Integer getMaxAttendants() {
        return maxAttendants;
    }

    /**
     * @param maxAttendants
     *            The maxAttendants to set.
     * @spring.validator type="required"
     */
    @Required
    @MinValue("1")
    public void setMaxAttendants(Integer maxAttendants) {
        this.maxAttendants = maxAttendants;
    }

    /**
     * @return Returns the name.
     * @hibernate.property column="NAME" length="100" not-null="true"
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     * @spring.validator type="required"
     */
    @Required
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the registerBy.
     * @hibernate.property column="REGISTERBY" not-null="true"
     */
    public Date getRegisterBy() {
        return registerBy;
    }

    /**
     * @param registerBy
     *            The registerBy to set.
     * @spring.validator type="required"
     */
    @Required
    public void setRegisterBy(Date registerBy) {
        this.registerBy = registerBy;
    }

    /**
     * @return Returns the registerStart.
     * @hibernate.property column="REGISTERSTART" not-null="false"
     */
    public Date getRegisterStart() {
        return registerStart;
    }

    /**
     * @param registerStart
     *            The registerStart to set.
     */
    public void setRegisterStart(Date registerStart) {
        this.registerStart = registerStart;
    }

    /**
     * @return Returns the reminder.
     * @hibernate.property column="REMINDER"
     */
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
     * @return Returns the reservedInternal.
     * @hibernate.property column="RESERVEDINTERNAL" not-null="true"
     */
    public Integer getReservedInternal() {
        return reservedInternal;
    }

    /**
     * @param reservedInternal
     *            The reservedInternal to set.
     * @spring.validator type="required"
     */
    @Required
    public void setReservedInternal(Integer reservedInternal) {
        this.reservedInternal = reservedInternal;
    }

    /**
     * @return Returns the startTime.
     * @hibernate.property column="STARTTIME" not-null="true"
     */
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
     * @hibernate.property column="STOPTIME" not-null="true"
     */
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
	 * Gets "maalgruppe".
	 * 
	 * @return Returns the type.
	 * @hibernate.property column="TYPE" length="100"
	 */
    public String getType() {
        return type;
    }

    /**
     * @param type
     *            The type to set.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return Returns the description.
     * @hibernate.property column="DESCRIPTION" length="1000" not-null="false"
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description
     *            The description to set.
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Returns the instructor.
     * @hibernate.many-to-one not-null="true" column="INSTRUCTORID"
     *                        insert="false" update="false" cascade="none"
     */
    public Person getInstructor() {
        return instructor;
    }

    /**
     * @param instructor
     *            The instructor to set.
     */
    public void setInstructor(Person instructor) {
        this.instructor = instructor;
    }

    /**
     * @return Returns the location.
     * @hibernate.many-to-one not-null="true" column="LOCATIONID" update="false"
     *                        cascade="none" insert="false"
     */
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
     * @hibernate.property column="LOCATIONID" not-null="true"
     */
    public Long getLocationid() {
        return locationid;
    }

    /**
     * @param locationid
     *            The locationid to set.
     * @spring.validator type="required"
     */
    @Required
    public void setLocationid(Long locationid) {
        this.locationid = locationid;
    }

    /**
     * @return Returns the organization.
     * @hibernate.many-to-one not-null="true" column="ORGANIZATIONID" update="false" cascade="none" insert="false"
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
     * @hibernate.property column="ORGANIZATIONID" not-null="true"
     */
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
     * @return Returns the organization2id.
     * @hibernate.property column="ORGANIZATION2ID" not-null="false"
     */
    public Long getOrganization2id() {
        return organization2id;
    }

    /**
     * @param organizationid
     *            The organization2id to set.
     * @spring.validator type="required"
     */
    @Required
    public void setOrganization2id(Long organization2id) {
        this.organization2id = organization2id;
    }

    /**
     * @return Returns the organization2.
     * @hibernate.many-to-one not-null="false" column="ORGANIZATION2ID" update="false" cascade="none" insert="false"
     */
    public Organization getOrganization2() {
    	if(organization2 == null){
    		this.organization2 = new Organization();
    	}
        return organization2;
    }

    public void setOrganization2(Organization organization2) {
    	if(organization2 == null){
    		organization2 = new Organization();
    	}
        this.organization2 = organization2;
    }

     /**
     * @return Returns the responsible.
     * @hibernate.many-to-one not-null="true" column="RESPONSIBLEUSERNAME" insert="false" update="false" cascade="none" optimistic-lock="false"
     */
     public User getResponsible() {
     return responsible;
     }

     /**
     * @param responsible
     * The responsible to set.
     */
     public void setResponsible(User responsible) {
     this.responsible = responsible;
     }

    /**
     * @return Returns the serviceArea.
     * @hibernate.many-to-one not-null="false" column="SERVICEAREAID" cascade="none" insert="false" update="false"
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
     *
     * @return
     * @hibernate.property column="STATUS" not-null="true"
     */
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("location", location)
                                        .append("instructor", instructor)
                                        .append("name", name)
                                        .append("type", type)
                                        .append("status", status)
                                        .append("startTime", startTime)
                                        .toString();
    }

    /**
     * @see java.lang.Object#equals(Object) ()
     */
    public boolean equals(final Object other) {
        if (!(other instanceof Course)) {
            return false;
        }

        Course castOther = (Course) other;

        return new EqualsBuilder().append(id, castOther.id).isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

    /**
     * Copies all the attribues of an existing course into this instance
     * 
     * @param original The course from which we copy the attributes
     */
    public void copyAllButId(Course original) {
        this.setCategory(original.getCategory());
        this.setCategoryid(original.getCategoryid());
    	this.setDescription(original.getDescription());
    	this.setDetailURL(original.getDetailURL());
    	this.setDuration(original.getDuration());
    	this.setFeeExternal(original.getFeeExternal());
    	this.setFeeInternal(original.getFeeInternal());
    	this.setFreezeAttendance(original.getFreezeAttendance());
    	this.setInstructor(original.getInstructor());
    	this.setInstructorid(original.getInstructorid());
    	this.setOrganization(original.getOrganization());
    	this.setOrganizationid(original.getOrganizationid());
    	this.setOrganization2(original.getOrganization2());
    	this.setOrganization2id(original.getOrganization2id());
    	this.setResponsible(original.getResponsible());
        this.setResponsibleUsername(original.getResponsibleUsername());
    	this.setResponsibleid(original.getResponsibleid());
    	this.setServiceArea(original.getServiceArea());
    	this.setServiceAreaid(original.getServiceAreaid());
    	this.setLocation(original.getLocation());
    	this.setLocationid(original.getLocationid());
    	this.setName(original.getName());
    	this.setType(original.getType());
    	this.setStartTime(original.getStartTime());
    	this.setStopTime(original.getStopTime());
    	this.setRegisterBy(original.getRegisterBy());
    	this.setRegisterStart(original.getRegisterStart());
    	this.setReminder(original.getReminder());
    	this.setMaxAttendants(original.getMaxAttendants());
    	this.setReservedInternal(original.getReservedInternal());
    	this.setRole(original.getRole());
    	this.setRestricted(original.getRestricted());
        this.setChargeoverdue(original.getChargeoverdue());
    	this.setStatus(original.getStatus());
    	this.setAttendants(original.getAttendants());
    }

    /**
     * @return the availableAttendants
     */
    public Integer getAvailableAttendants() {
        return availableAttendants;
    }

    /**
     * @param availableAttendants the availableAttendants to set
     */
    public void setAvailableAttendants(Integer availableAttendants) {
        this.availableAttendants = availableAttendants;
    }

    /**
     * Sets if attendees are visible to other attendees
     * @return
//     * @hibernate.property column="RESTRICTED" not-null="false"
     */
    public Boolean getRestricted() {
        return restricted;
    }

    public void setRestricted(Boolean restricted) {
        this.restricted = restricted;
    }
    
    /**
     * Charge course fee if user cancel registration after duedate
     * @return
     * @hibernate.property column="CHARGEOVERDUE" not-null="false"
     */
    public Boolean getChargeoverdue() {
        return chargeoverdue;
    }

    public void setChargeoverdue(Boolean chargeoverdue) {
        this.chargeoverdue = chargeoverdue;
    }
    
    /**
     * The course is expired if registerBy is past
     * @return expired
     */
    public boolean isExpired() {
    	if(new Date().after(registerBy)) return true;
    	else return false;
    }
    
}