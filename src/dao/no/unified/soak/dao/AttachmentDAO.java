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
package no.unified.soak.dao;

import no.unified.soak.model.Attachment;

import java.util.List;


/**
 *
 * User Data Access Object (DAO) interface.
 *
 * @author Henrik RJ
 */
public interface AttachmentDAO extends DAO {
    /**
    * Get all attachments that fits the parameters. If both parameters are null, all attachments in the database will be retrieved
    *
     * @param courseId Restrict the search to this course only
     * @param locationId Restrict the search to this location only. This parameter is ignored if courseId is set
     * @return A list of attachments
     */
    public List getAttachments(final Long courseId, final Long locationId);

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
    * Delete all attachments linked to a specific Course or Location
     * @param courseId The id of the Course to delete from. Ignored if null.
     * @param locationId The id of the Location to delete from. Ignored if null or if courseId is not null
     */
    public void removeAllAttachments(final Long courseId, final Long locationId);

    /**
     * Finds number of attachments connected to a Course or Location.
     * If the courseId parameter is given, the locationId parameter is ignored
     *
     * @param courseId the courseId to find number of attachments for
     * @param locationId the locationId to find number of attachments for
     */
    public Integer getHighId(final Long courseId, final Long locationId);
}
