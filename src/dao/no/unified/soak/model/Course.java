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

import no.unified.soak.ez.EzUser;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;

import java.util.Date;


/**
 * This class is used to represent a course
 *
 * This class is used to generate Spring Validation rules as well as the
 * Hibernate mapping file.
 *
 * @author hrj
 * @hibernate.class table="course"
 */
public class Course extends BaseObject implements Serializable {
    /**
     * When adding / removing attributes to this class, remember to update the copyAllButId-function
     */
    private static final long serialVersionUID = -4869033333668709720L;
    private Long id;
    private Municipalities municipality;
    private Long municipalityid;
    private EzUser responsible;
    private Long responsibleid;
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
    private Integer reservedMunicipal;
    private String detailURL;
    private Double feeMunicipal;
    private Double feeExternal;
    private String description;
    private String role;
    private Integer availableAttendants; 
    
    /**
     * @return Returns the role.
     * @hibernate.property column="role" length="50" not-null="true"
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role
     *            The role to set.
     * @spring.validator type="required"
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @hibernate.property column="instructorid" not-null="true"
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
    public void setInstructorid(Long instructorid) {
        this.instructorid = instructorid;
    }

    /**
     * @hibernate.property column="responsibleid" not-null="true"
     * @return Returns the responsibleid.
     */
    public Long getResponsibleid() {
        return responsibleid;
    }

    /**
     * @param responsibleid
     *            The responsibleid to set.
     * @spring.validator type="required"
     */
    public void setResponsibleid(Long responsibleid) {
        this.responsibleid = responsibleid;
    }

    /**
     * @hibernate.property column="serviceareaid" not-null="true"
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
    public void setServiceAreaid(Long serviceAreaid) {
        this.serviceAreaid = serviceAreaid;
    }

    /**
     * @return Returns the detailURL.
     * @hibernate.property column="detailurl" length="200"
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
     * @hibernate.property column="duration" length="100" not-null="true"
     */
    public String getDuration() {
        return duration;
    }

    /**
     * @param duration
     *            The duration to set.
     * @spring.validator type="required"
     */
    public void setDuration(String duration) {
        this.duration = duration;
    }

    /**
     * @return Returns the feeExternal.
     * @hibernate.property column="feexternal" not-null="true"
     */
    public Double getFeeExternal() {
        return feeExternal;
    }

    /**
     * @param feeExternal
     *            The feeExternal to set.
     * @spring.validator type="required"
     */
    public void setFeeExternal(Double feeExternal) {
        this.feeExternal = feeExternal;
    }

    /**
     * @return Returns the feeMunicipal.
     * @hibernate.property column="feemunicipal" not-null="true"
     */
    public Double getFeeMunicipal() {
        return feeMunicipal;
    }

    /**
     * @param feeMunicipal
     *            The feeMunicipal to set.
     * @spring.validator type="required"
     */
    public void setFeeMunicipal(Double feeMunicipal) {
        this.feeMunicipal = feeMunicipal;
    }

    /**
     * @return Returns the freezeAttendance.
     * @hibernate.property column="freezeAttendance" not-null="true"
     */
    public Date getFreezeAttendance() {
        return freezeAttendance;
    }

    /**
     * @param freezeAttendance
     *            The freezeAttendance to set.
     * @spring.validator type="required"
     */
    public void setFreezeAttendance(Date freezeAttendance) {
        this.freezeAttendance = freezeAttendance;
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
     *
     * /**
     *
     * @return Returns the maxAttendants.
     * @hibernate.property column="maxattendants" not-null="true"
     */
    public Integer getMaxAttendants() {
        return maxAttendants;
    }

    /**
     * @param maxAttendants
     *            The maxAttendants to set.
     * @spring.validator type="required"
     */
    public void setMaxAttendants(Integer maxAttendants) {
        this.maxAttendants = maxAttendants;
    }

    /**
     * @return Returns the name.
     * @hibernate.property column="name" length="100" not-null="true"
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     *            The name to set.
     * @spring.validator type="required"
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Returns the registerBy.
     * @hibernate.property column="registerby" not-null="true"
     */
    public Date getRegisterBy() {
        return registerBy;
    }

    /**
     * @param registerBy
     *            The registerBy to set.
     * @spring.validator type="required"
     */
    public void setRegisterBy(Date registerBy) {
        this.registerBy = registerBy;
    }

    /**
     * @return Returns the registerStart.
     * @hibernate.property column="registerstart" not-null="false"
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
     * @hibernate.property column="reminder"
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
     * @return Returns the reservedMunicipal.
     * @hibernate.property column="reservedmunicipal" not-null="true"
     */
    public Integer getReservedMunicipal() {
        return reservedMunicipal;
    }

    /**
     * @param reservedMunicipal
     *            The reservedMunicipal to set.
     * @spring.validator type="required"
     */
    public void setReservedMunicipal(Integer reservedMunicipal) {
        this.reservedMunicipal = reservedMunicipal;
    }

    /**
     * @return Returns the startTime.
     * @hibernate.property column="starttime" not-null="true"
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     *            The startTime to set.
     * @spring.validator type="required"
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return Returns the stopTime.
     * @hibernate.property column="stoptime" not-null="true"
     */
    public Date getStopTime() {
        return stopTime;
    }

    /**
     * @param stopTime
     *            The stopTime to set.
     * @spring.validator type="required"
     */
    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;
    }

    /**
     * @return Returns the type.
     * @hibernate.property column="type" length="50"
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
     * @hibernate.property column="description" length="1000" not-null="false"
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
     * @hibernate.many-to-one not-null="true" column="instructorid"
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
     * @hibernate.many-to-one not-null="true" column="locationid" update="false"
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
     * @hibernate.property column="locationid" not-null="true"
     */
    public Long getLocationid() {
        return locationid;
    }

    /**
     * @param locationid
     *            The locationid to set.
     * @spring.validator type="required"
     */
    public void setLocationid(Long locationid) {
        this.locationid = locationid;
    }

    /**
     * @return Returns the municipality.
     * @hibernate.many-to-one not-null="true" column="municipalityid"
     *                        update="false" cascade="none" insert="false"
     */
    public Municipalities getMunicipality() {
        return municipality;
    }

    /**
     * @param municipality
     *            The municipality to set.
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
     * @param municipalityid
     *            The municipalityid to set.
     * @spring.validator type="required"
     */
    public void setMunicipalityid(Long municipalityid) {
        this.municipalityid = municipalityid;
    }

    // /**
    // * @return Returns the responsible.
    // * @hibernate.many-to-one not-null="true" cascade="none"
    // column="responsibleid" insert="false" update="false" cascade="none"
    // */
    // public Person getResponsible() {
    // return responsible;
    // }

    // /**
    // * @param responsible
    // * The responsible to set.
    // */
    // public void setResponsible(Person responsible) {
    // this.responsible = responsible;
    // }

    /**
     * @return Returns the serviceArea.
     * @hibernate.many-to-one not-null="true" column="serviceareaid"
     *                        cascade="none" insert="false" update="false"
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
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("location", location)
                                        .append("instructor", instructor)
                                        .append("name", name)
                                        .append("type", type)
                                        .append("startTime", startTime)
                                        .toString();
    }

    /**
     * @see java.lang.Object#equals()
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
     * @return Returns the responsible.
     */
    public EzUser getResponsible() {
        return responsible;
    }

    /**
     * @param responsible
     *            The responsible to set.
     */
    public void setResponsible(EzUser responsible) {
        this.responsible = responsible;
    }
    
    /**
     * Copies all the attribues of an existing course into this instance
     * 
     * @param original The course from which we copy the attributes
     */
    public void copyAllButId(Course original) {
    	this.setDescription(original.getDescription());
    	this.setDetailURL(original.getDetailURL());
    	this.setDuration(original.getDuration());
    	this.setFeeExternal(original.getFeeExternal());
    	this.setFeeMunicipal(original.getFeeMunicipal());
    	this.setFreezeAttendance(original.getFreezeAttendance());
    	this.setInstructor(original.getInstructor());
    	this.setInstructorid(original.getInstructorid());
    	this.setMunicipality(original.getMunicipality());
    	this.setMunicipalityid(original.getMunicipalityid());
    	this.setResponsible(original.getResponsible());
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
    	this.setReservedMunicipal(original.getReservedMunicipal());
    	this.setRole(original.getRole());
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
}