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
package no.unified.soak.service.impl;

import no.unified.soak.dao.AttachmentDAO;
import no.unified.soak.model.Attachment;
import no.unified.soak.service.AttachmentManager;

import java.util.List;


/**
 * Implementation of AttachmentManager
 *
 * @author hrj
 */
public class AttachmentManagerImpl extends BaseManager
    implements AttachmentManager {
    private AttachmentDAO dao;

    /**
     * Set the DAO for communication with the data layer.
     *
     * @param dao
     */
    public void setAttachmentDAO(AttachmentDAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#getCourseAttachments(java.lang.Long)
     */
    public List getCourseAttachments(Long courseId) {
        return dao.getAttachments(courseId, null);
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#getLocationAttachments(java.lang.Long)
     */
    public List getLocationAttachments(Long locationId) {
        return dao.getAttachments(null, locationId);
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#getAttachment(java.lang.Long)
     */
    public Attachment getAttachment(Long attachmentId) {
        return dao.getAttachment(attachmentId);
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#removeAttachment(java.lang.Long)
     */
    public void removeAttachment(Long attachmentId) {
        dao.removeAttachment(attachmentId);
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#saveAttachment(no.unified.soak.model.Attachment)
     */
    public void saveAttachment(Attachment attachment) {
        dao.saveAttachment(attachment);
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#removeAllCourseAttachments(java.lang.Long, java.lang.Long)
     */
    public void removeAllCourseAttachments(Long courseId) {
        dao.removeAllAttachments(courseId, null);
    }

    /**
     * @see no.unified.soak.service.AttachmentManager#removeAllLocationAttachments(java.lang.Long, java.lang.Long)
     */
    public void removeAllLocationAttachments(Long locationId) {
        dao.removeAllAttachments(null, locationId);
    }
}
