/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created January 19th 2006
 */
package no.unified.soak.webapp.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.Constants;
import no.unified.soak.model.Attachment;
import no.unified.soak.model.Course;
import no.unified.soak.service.AttachmentManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.webapp.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;


/**
 * Handles uploading/deletion of files attached to a course
 *
 * @author hrj
 */
public class CourseFileFormController extends BaseFormController {
    private CourseManager courseManager = null;
    private AttachmentManager attachmentManager = null;

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setAttachmentManager(AttachmentManager attachmentManager) {
        this.attachmentManager = attachmentManager;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest request)
        throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Course course = null;
        String courseid = request.getParameter("courseid");
        course = courseManager.getCourse(courseid);
        model.put("course", course);

        AttachmentsBackingObject abo = getAttachmentsBackingObjects(new Long(
                    courseid));
        model.put("attachmentsBackingObject", abo);

        return model;
    }

    /**
     * Gets the info of the attachments attached to given objects
     *
     * @param courseid
     *            the course to get the attachements for
     * @return The attachments
     */
    private AttachmentsBackingObject getAttachmentsBackingObjects(Long courseid) {
        AttachmentsBackingObject abo = new AttachmentsBackingObject();
        List attachments = attachmentManager.getCourseAttachments(courseid);
        abo.setAttachments(attachments);

        return abo;
    }

    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     *      javax.servlet.http.HttpServletResponse, java.lang.Object,
     *      org.springframework.validation.BindException)
     */
    public ModelAndView onSubmit(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method...");
        }

        Map<String, Object> model = new HashMap<String, Object>();
        FileUpload fileUpload = (FileUpload) command;
        String courseid = request.getParameter("courseid");
        Locale locale = request.getLocale();

        // Are we to cancel?
        if (request.getParameter("docancel") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'cancel' from jsp");
            }

            return new ModelAndView(getCancelView(), "id", courseid);
        } // or to download?
        else if (request.getParameter("download") != null) {
            // Stream file to the client
            try {
                String attachmentId = request.getParameter("attachmentid");

                if ((attachmentId != null) &&
                        StringUtils.isNumeric(attachmentId) &&
                        (new Integer(attachmentId).intValue() != 0)) {
                    Attachment attachment = attachmentManager.getAttachment(new Long(
                                attachmentId));
                    String filename = getServletContext()
                                          .getRealPath("/resources") + "/" +
                        attachment.getStoredname();

                    FileUtil.downloadFile(request, response,
                        attachment.getContentType(), filename,
                        attachment.getFilename());
                    saveMessage(request, getText("attachment.sent", locale));
                }
            } catch (FileNotFoundException fnfe) {
                log.error(fnfe);

                String key = "attachment.sendError";
                errors.reject(key);

                return showForm(request, response, errors);
            } catch (IOException ioe) {
                log.error(ioe);

                String key = "errors.ioerror";
                errors.reject(key);

                return showForm(request, response, errors);
            }
        } else if (request.getParameter("delete") != null) {
            // Delete the file and the attachment-object
            String attachmentId = request.getParameter("attachmentid");

            if ((attachmentId != null) && StringUtils.isNumeric(attachmentId) &&
                    (new Integer(attachmentId).intValue() != 0)) {
                Attachment attachment = attachmentManager.getAttachment(new Long(
                            attachmentId));
                String filename = getServletContext().getRealPath("/resources") +
                    "/" + attachment.getStoredname();

                try {
                    File fileToBeDeleted = new File(filename);
                    boolean deleted = fileToBeDeleted.delete();

                    if (!deleted) {
                        String key = "errors.deleteFile";
                        errors.reject(key);

                        return showForm(request, response, errors);
                    } else {
                        attachmentManager.removeAttachment(new Long(
                                attachmentId));
                    }

                    saveMessage(request, getText("attachment.deleted", locale));
                } catch (Exception e) {
                    log.error(e);

                    String key = "errors.deleteFile";
                    errors.reject(key);

                    return showForm(request, response, errors);
                }
            }
        } else {
            // Recieve file from client
            if (fileUpload.getFile().length == 0) {
                Object[] args = new Object[] {
                        getText("uploadForm.file", request.getLocale())
                    };
                errors.rejectValue("file", "errors.required", args, "File");

                return showForm(request, response, errors);
            }

            String key = "attachment.maxsize";
            String maxSize = getText(key, locale);

            // Is the file within the size limit?
            if (fileUpload.getFile().length > new Long(maxSize)) {
                key = "errors.filetoobig";

                String[] args = {
                        fileUpload.getName(),
                        String.valueOf(fileUpload.getFile().length), maxSize
                    };
                errors.rejectValue("file", key, args,
                    "File is bigger than allowed size (" + maxSize + " bytes)");

                return showForm(request, response, errors);
            }

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(
                    "file");

            // the directory to upload to
            String uploadDir = getServletContext().getRealPath("/resources") +
                "/";

            // Create the directory if it doesn't exist
            File dirPath = new File(uploadDir);

            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }

            Attachment attachment = saveAttachment(courseid, file);

            String storedname = Constants.ATTACHMENT_FILE_PREFIX +
                attachment.getId();

            try {
                FileUtil.recieveFile(file, uploadDir, storedname);
                saveMessage(request, getText("attachment.recieved", locale));
            } catch (FileNotFoundException fnfe) {
                log.error(fnfe);
                key = "attachment.recieveError";
                errors.reject(key);

                return showForm(request, response, errors);
            } catch (IOException ioe) {
                log.error(ioe);
                key = "errors.ioerror";
                errors.reject(key);

                return showForm(request, response, errors);
            }

            attachment.setStoredname(storedname);
            attachmentManager.saveAttachment(attachment);
        }

        // Fetch info about the course
        Course course = courseManager.getCourse(courseid);
        model.put("course", course);

        // Fetch the new file list
        AttachmentsBackingObject abo = getAttachmentsBackingObjects(new Long(
                    courseid));
        model.put("attachmentsBackingObject", abo);

        // Reset the fileUpload-object
        fileUpload = new FileUpload();
        model.put("fileUpload", fileUpload);

        return new ModelAndView(getSuccessView(), model);
    }

    /**
     * Persists the attachment
     *
     * @param courseid
     *            The course the attachment is going to be connected to
     * @param file
     *            The file that contains the needed info to create the
     *            attachment
     * @return The persisted attachment
     */
    private Attachment saveAttachment(String courseid, CommonsMultipartFile file) {
        Attachment attachment = new Attachment();
        attachment.setContentType(file.getContentType());
        attachment.setFilename(file.getOriginalFilename());
        attachment.setCourseId(new Long(courseid));
        attachment.setSize(file.getSize());
        attachmentManager.saveAttachment(attachment);

        return attachment;
    }
}
