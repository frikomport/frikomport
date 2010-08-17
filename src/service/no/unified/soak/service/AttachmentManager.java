/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created January 17th 2006
 */
package no.unified.soak.service;

import no.unified.soak.dao.AttachmentDAO;
import no.unified.soak.model.Attachment;

import java.util.List;


/**
 * The service layer for attachments
 *
 * @author hrj
 */
public interface AttachmentManager extends Manager {
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setAttachmentDAO(AttachmentDAO attachmentDAO);

    /**
     * Get all attachments connected to a course. If the parameters is null, all attachments in the database will be retrieved
     *
     * @param courseId Restrict the search to this course only
     * @return A list of attachments
     */
    public List getCourseAttachments(final Long courseId);

    /**
     * Get all attachments connected to a location. If the parameters is null, all attachments in the database will be retrieved
     *
     * @param locationId Restrict the search to this course only
     * @return A list of attachments
     */
    public List getLocationAttachments(final Long locationId);

    /**
     * Get a single attachment
     *
     * @param attachmentId Id of the Attachment to retrieve
     * @return the attachment with attachmentId (if any)
     */
    public Attachment getAttachment(final Long attachmentId);

    /**
     * Delete a single attachment
     *
     * @param attachmentId Id of the attachment to delete
     */
    public void removeAttachment(final Long attachmentId);

    /**
     * Persist an attachment
     *
     * @param attachment The attachment to persist
     */
    public void saveAttachment(Attachment attachment);

    /**
     * Delete all attachments linked to a specific Course
     * @param courseId The id of the Course to delete from. Ignored if null.
     * @param locationId The id of the Location to delete from. Ignored if null or if courseId is not null
     */
    public void removeAllCourseAttachments(final Long courseId);

    /**
     * Delete all attachments linked to a specific Location
     * @param courseId The id of the Course to delete from. Ignored if null.
     * @param locationId The id of the Location to delete from. Ignored if null or if courseId is not null
     */
    public void removeAllLocationAttachments(final Long locationId);
}
