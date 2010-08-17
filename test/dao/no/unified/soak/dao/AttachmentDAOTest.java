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
package no.unified.soak.dao;

import no.unified.soak.model.Attachment;

import java.util.List;


/**
 * Tests the functions in the AttachmentDAO interface
 *
 * @author hrj
 */
public class AttachmentDAOTest extends BaseDAOTestCase {
    private Attachment attachment = null;
    private Long attachmentId = new Long("1");
    private Long courseId = new Long("1");
    private Long locationId = new Long("1");
    private AttachmentDAO dao = null;

    public void setAttachmentDAO(AttachmentDAO dao) {
        this.dao = dao;
    }

    /**
     * Retireves a list of all attachments connected to a Course and verifies
     * that the result has a certain size. Then retrieves a list of all
     * attachments connected to a Location and verifies that the result has a
     * certain size.
     *
     * Prerequisites: The database must contain a course with id=1 that has one
     * or more attachment connected The database must contain a location with
     * id=1 that has one or more attachment connected
     *
     * @throws Exception
     */
    public void testGetAttachments() throws Exception {
        List attachments = dao.getAttachments(courseId, null);
        assertNotNull(attachments);
        assertTrue(attachments.size() > 0);
        attachments = dao.getAttachments(null, locationId);
        assertNotNull(attachments);
        assertTrue(attachments.size() > 0);
    }

    /**
     * Tries retrieving the attachment with id = 1 Prerequisites: An attachment
     * in the database with id = 1
     *
     * @throws Exception
     */
    public void testGetAttachment() throws Exception {
        attachment = dao.getAttachment(attachmentId);
        assertNotNull(attachment);
    }

    /**
     * Test the removal-functionality for a single attachment
     *
     * 1) Retrives and verifies an attachment with Id = 3 2) Deletes the
     * attachment with Id = 3 3) Tries to retrieve the attachment with Id = 3 4)
     * See that 3) fails.
     *
     * Prerequisites: An attachment with id=3 in the database
     *
     * @throws Exception
     */
    public void testRemoveAttachment() throws Exception {
        Long attachmentToBeRemovedId = new Long(3);

        attachment = dao.getAttachment(attachmentToBeRemovedId);
        assertNotNull(attachment);
        assertTrue(attachment.getId().longValue() == attachmentToBeRemovedId.longValue());
        dao.removeAttachment(new Long("3"));

        try {
            attachment = dao.getAttachment(attachmentToBeRemovedId);
            fail("Attachment is still there!");
        } catch (Exception e) {
            assertNotNull(e.getMessage());
        }
    }

    /**
     * Creates a new attachment with id = 99, saves it, retrivies it, verifies
     * its fields, and finally deletes it.
     *
     * @throws Exception
     */
    public void testSaveAttachment() throws Exception {
        Long savedAttachmentId = null;
        attachment = new Attachment();
        attachment.setFilename("filename");
        attachment.setContentType("contenttype");
        attachment.setSize(new Long(100));
        attachment.setStoredname("storedname");

        // Save it
        dao.saveAttachment(attachment);
        savedAttachmentId = attachment.getId();
        assertNotNull(attachment.getId());

        // Retrieve it
        attachment = dao.getAttachment(savedAttachmentId);

        // See that everything is saved
        assertNotNull(attachment);
        assertEquals("filename", attachment.getFilename());
        assertEquals("storedname", attachment.getStoredname());
        assertEquals("contenttype", attachment.getContentType());
        assertEquals(100, attachment.getSize().longValue());
        assertEquals(savedAttachmentId.longValue(),
            attachment.getId().longValue());

        // Remove it now that we're done testing
        dao.removeAttachment(savedAttachmentId);
    }

    /**
     * Save one extra attachment for course with id=1 and one extra attachment
     * for location with id = 1. See that they are persisted. Delete all
     * belonging to a specific course, and delete one belonging to a specific
     * location. Verify that these registrations now have no attachments
     * connected. Then restore the database.
     *
     * @throws Exception
     */
    public void removeAllAttachment() throws Exception {
        // Save the current state of the datbase
        List backupCourseAttachments = dao.getAttachments(courseId, null);
        List backupLocationAttachments = dao.getAttachments(null, locationId);

        // Add an extra attachment to Location with id=1
        attachment = new Attachment();
        attachment.setFilename("filename");
        attachment.setContentType("contenttype");
        attachment.setSize(new Long(100));
        attachment.setStoredname("storedname");
        attachment.setLocationId(locationId);
        dao.saveAttachment(attachment);

        // Add an extra attachment to Location with id=1
        attachment = new Attachment();
        attachment.setFilename("filename2");
        attachment.setContentType("contenttype2");
        attachment.setSize(new Long(102));
        attachment.setStoredname("storedname2");
        attachment.setCourseId(courseId);
        dao.saveAttachment(attachment);

        // Check that everything is as we want it in the database
        assertTrue(dao.getAttachments(courseId, null).size() > 1);
        assertTrue(dao.getAttachments(null, locationId).size() > 1);

        // Now delete all with courseId = 1
        dao.removeAllAttachments(courseId, null);
        assertNull(dao.getAttachments(courseId, null));
        dao.removeAllAttachments(null, locationId);
        assertNull(dao.getAttachments(null, locationId));

        // Restore backup
        for (int i = 0; i < backupCourseAttachments.size(); i++)
            dao.saveAttachment((Attachment) backupCourseAttachments.get(i));

        for (int i = 0; i < backupLocationAttachments.size(); i++)
            dao.saveAttachment((Attachment) backupLocationAttachments.get(i));
    }

    /**
     * Tests the getHighId-function
     */
    public void testGetHighId() {
        Integer result = dao.getHighId(courseId, null);
        assertNotNull(result);
        result = dao.getHighId(null, locationId);
        assertNotNull(result);
    }
}
