/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created January 16th 2006
 */
package no.unified.soak.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;


/**
 * This class holds information about the attachment/files that are connected to
 * either a course or a location (but not both)
 *
 * @author Henrik RJ
 * @hibernate.class table = "attachment"
 */
public class Attachment extends BaseObject implements Serializable {
    /**
     * Eclipse-generated UID
     */
    private static final long serialVersionUID = 2027089783395343907L;
    private Long id;
    private String filename;
    private String storedname;
    private String contentType;
    private Long size;
    private Course course;
    private Long courseId;
    private Location location;
    private Long locationId;

    /**
    * @hibernate.property column="CONTENTTYPE" length="100"
     * @return Returns the contentType.
     */
    public String getContentType() {
        return contentType;
    }

    /**
     * @param contentType The contentType to set.
     */
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    /**
     * @return Returns the course.
    * @hibernate.many-to-one column="COURSEID"
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
    * @hibernate.property column="COURSEID"
     * @return Returns the courseId.
     */
    public Long getCourseId() {
        return courseId;
    }

    /**
     * @param courseId The courseId to set.
     */
    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    /**
    * @hibernate.property column="FILENAME" length="100"
     * @return Returns the filename.
     */
    public String getFilename() {
        return filename;
    }

    /**
     * @param filename The filename to set.
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }

    /**
    * @hibernate.id column="ID" generator-class="native" unsaved-value="null"
     * @return Returns the id.
     */
    public Long getId() {
        return id;
    }

    /**
     * @param id The id to set.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
    * @hibernate.many-to-one column="LOCATIONID"
    *                        insert="false" update="false" cascade="none"
     * @return Returns the location.
     */
    public Location getLocation() {
        return location;
    }

    /**
     * @param location The location to set.
     */
    public void setLocation(Location location) {
        this.location = location;
    }

    /**
    * @hibernate.property column="LOCATIONID"
     * @return Returns the locationId.
     */
    public Long getLocationId() {
        return locationId;
    }

    /**
     * @param locationId The locationId to set.
     */
    public void setLocationId(Long locationId) {
        this.locationId = locationId;
    }

    /**
    * @hibernate.property column="SIZE"
     * @return Returns the size.
     */
    public Long getSize() {
        return size;
    }

    /**
     * @param size The size to set.
     */
    public void setSize(Long size) {
        this.size = size;
    }

    /**
    * @hibernate.property column="STOREDNAME" size="100"
     * @return Returns the storedname.
     */
    public String getStoredname() {
        return storedname;
    }

    /**
     * @param storedname The storedname to set.
     */
    public void setStoredname(String storedname) {
        this.storedname = storedname;
    }

    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(final Object other) {
        if (!(other instanceof Attachment)) {
            return false;
        }

        Attachment castOther = (Attachment) other;

        return new EqualsBuilder().append(id, castOther.id)
                                  .append(filename, castOther.filename)
                                  .append(contentType, castOther.contentType)
                                  .append(size, castOther.size)
                                  .append(courseId, castOther.courseId)
                                  .append(locationId, castOther.locationId)
                                  .isEquals();
    }

    /**
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(filename)
                                    .append(contentType).append(size)
                                    .append(courseId).append(locationId)
                                    .toHashCode();
    }

    /**
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id)
                                        .append("filename", filename)
                                        .append("storedname", storedname)
                                        .append("contentType", contentType)
                                        .append("size", size)
                                        .append("courseId", courseId)
                                        .append("locationId", locationId)
                                        .toString();
    }
}
