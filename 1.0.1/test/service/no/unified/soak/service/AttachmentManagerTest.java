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
import no.unified.soak.service.impl.AttachmentManagerImpl;

import org.jmock.Mock;

import org.springframework.orm.ObjectRetrievalFailureException;

import java.util.ArrayList;
import java.util.List;


/**
 * Test class for AttachmentManager
 * @author hrj
 */
public class AttachmentManagerTest extends BaseManagerTestCase {
    private final Long attachmentId = new Long(1);
    private final Long courseId = new Long(1);
    private final Long locationId = new Long(1);
    private AttachmentManager attachmentManager = new AttachmentManagerImpl();
    private Mock attachmentDAO = null;
    private Attachment attachment = null;

    protected void setUp() throws Exception {
        super.setUp();
        attachmentDAO = new Mock(AttachmentDAO.class);
        attachmentManager.setAttachmentDAO((AttachmentDAO) attachmentDAO.proxy());
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        attachmentManager = null;
    }

    /**
     * Simple test of getCourseAttachments
     */
    public void testGetCourseAttachments() {
        List results = new ArrayList();

        attachment = new Attachment();
        results.add(attachment);

        // set expected behavior on dao
        attachmentDAO.expects(once()).method("getAttachments")
                     .will(returnValue(results));

        List attachments = attachmentManager.getCourseAttachments(courseId);
        assertTrue(attachments.size() == 1);
        attachmentDAO.verify();
    }

    /**
     * Simple test of getLocationAttachments
     */
    public void testGetLocationAttachments() {
        List results = new ArrayList();

        attachment = new Attachment();
        results.add(attachment);

        // set expected behavior on dao
        attachmentDAO.expects(once()).method("getAttachments")
                     .will(returnValue(results));

        List attachments = attachmentManager.getLocationAttachments(locationId);
        assertTrue(attachments.size() == 1);
        attachmentDAO.verify();
    }

    /**
     * Simple test of getAttachment
     *
     */
    public void testGetAttachment() {
        // set expected behavior on dao
        attachmentDAO.expects(once()).method("getAttachment")
                     .will(returnValue(new Attachment()));
        attachment = attachmentManager.getAttachment(attachmentId);
        assertTrue(attachment != null);
        attachmentDAO.verify();
    }

    /**
     * Test of the removeAttachment function
     *
     * First we insert an attachment, then we delete it
     *
     * @param attachmentId Id of the attachment to delete
     */
    public void testRemoveAttachment() {
        Long attachmentToBeRemovedId = new Long(9919);
        attachment = new Attachment();

        // set required fields
        attachment.setId(new Long(9919));
        attachment.setContentType("hei");
        attachment.setFilename("hei");
        attachment.setSize(new Long(1024));

        // set expected behavior on dao
        attachmentDAO.expects(once()).method("saveAttachment")
                     .with(same(attachment)).isVoid();
        attachmentManager.saveAttachment(attachment);
        attachmentDAO.verify();

        // reset expectations
        attachmentDAO.reset();

        attachmentDAO.expects(once()).method("removeAttachment")
                     .with(eq(attachmentToBeRemovedId));
        attachmentManager.removeAttachment(attachmentToBeRemovedId);
        attachmentDAO.verify();

        // reset expectations
        attachmentDAO.reset();

        // remove
        Exception ex = new ObjectRetrievalFailureException(Attachment.class,
                attachment.getId());
        attachmentDAO.expects(once()).method("removeAttachment").isVoid();
        attachmentDAO.expects(once()).method("getAttachment")
                     .will(throwException(ex));
        attachmentManager.removeAttachment(attachmentToBeRemovedId);

        try {
            attachmentManager.getAttachment(attachmentToBeRemovedId);
            fail("Attachment with identifier '" + attachmentToBeRemovedId +
                "' found in database");
        } catch (ObjectRetrievalFailureException e) {
            assertNotNull(e.getMessage());
        }

        attachmentDAO.verify();
    }

    /**
     * Simple test of the saveAttachment function.
     */
    public void testSaveAttachment() {
        // set expected behavior on dao
        attachmentDAO.expects(once()).method("saveAttachment")
                     .with(same(attachment)).isVoid();

        attachmentManager.saveAttachment(attachment);
        attachmentDAO.verify();
    }

    /**
     * Simple test for the removeAllCourseAttachments-function.
     */
    public void testRemoveAllCourseAttachments() {
        Long courseAttachmentsForRemovalId = new Long(3);
        // set expected behavior on dao
        attachmentDAO.expects(once()).method("removeAllAttachments")
                     .with(eq(courseAttachmentsForRemovalId), eq(null));
        attachmentManager.removeAllCourseAttachments(new Long(3));
        attachmentDAO.verify();
    }
}
