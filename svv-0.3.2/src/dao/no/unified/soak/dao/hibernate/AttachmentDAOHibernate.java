/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/**
 * Created January 17th 2006
 */
package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.AttachmentDAO;
import no.unified.soak.model.Attachment;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.List;


/**
 * Implementation of AttachmentDAO
 *
 * @author hrj
 */
public class AttachmentDAOHibernate extends BaseDAOHibernate
    implements AttachmentDAO {
    /**
     * @see no.unified.soak.dao.AttachmentDAO#getAttachments(java.lang.Long,
     *      java.lang.Long)
     */
    public List getAttachments(final Long courseId, final Long locationId) {
        // The default setup - returns everything
        DetachedCriteria criteria = DetachedCriteria.forClass(Attachment.class);

        if ((courseId != null) && (courseId.longValue() != 0)) {
            criteria.add(Restrictions.eq("courseId", courseId));
        } else if ((locationId != null) && (locationId.longValue() != 0)) {
            criteria.add(Restrictions.eq("locationId", locationId));
        }

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * @see no.unified.soak.dao.AttachmentDAO#getAttachment(java.lang.Long)
     */
    public Attachment getAttachment(final Long attachmentId) {
        Attachment attachment = (Attachment) getHibernateTemplate()
                                                 .get(Attachment.class,
                attachmentId);

        if (attachment == null) {
            log.warn("uh oh, attachment with id '" + attachmentId +
                "' not found...");
            throw new ObjectRetrievalFailureException(Attachment.class,
                attachmentId);
        }

        return attachment;
    }

    /**
     * @see no.unified.soak.dao.AttachmentDAO#removeAttachment(java.lang.Long)
     */
    public void removeAttachment(final Long attachmentId) {
        getHibernateTemplate().delete(getAttachment(attachmentId));
    }

    /**
     * @see no.unified.soak.dao.AttachmentDAO#saveAttachment(no.unified.soak.model.Attachment)
     */
    public void saveAttachment(final Attachment attachment) {
        getHibernateTemplate().save(attachment);
    }

    /**
     * @see no.unified.soak.dao.AttachmentDAO#removeAllAttachment(java.lang.Long,
     *      java.lang.Long)
     */
    public void removeAllAttachments(final Long courseId, final Long locationId) {
        if ((courseId != null) && (courseId.longValue() != 0)) {
            // Delete all attachments belonging to a specific course
            getHibernateTemplate().deleteAll(getAttachments(courseId, null));
        } else if ((locationId != null) && (locationId.longValue() != 0)) {
            // Delete all attachments belonging to a specific location
            getHibernateTemplate().deleteAll(getAttachments(null, locationId));
        }
    }

    /**
     * @see no.unified.soak.dao.AttachmentDAO#getHighId(java.lang.Long, java.lang.Long)
     */
    public Integer getHighId(Long courseId, Long locationId) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Attachment.class);

        // Find number of registrations totally on the course
        if ((courseId != null) && (courseId.longValue() != 0)) {
            criteria.add(Restrictions.eq("courseId", courseId));
        } else if ((locationId != null) && (locationId.longValue() != 0)) {
            criteria.add(Restrictions.eq("locationId", locationId));
        }

        criteria.setProjection(Projections.rowCount());

        List queryResult = getHibernateTemplate().findByCriteria(criteria);
        Integer result = (Integer) queryResult.get(0);

        return result;
    }
}
