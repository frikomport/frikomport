/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.webapp.action;

import no.unified.soak.Constants;

import org.springframework.validation.BindException;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Controller class to upload Files.
 *
 * <p>
 * <a href="FileUploadFormController.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class FileUploadController extends BaseFormController {
	private String resourceFolder = "resources";
	
	public void setResourceFolder(String resourceFolder) {
		this.resourceFolder = resourceFolder;
	}
	
    public ModelAndView processFormSubmission(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        if (request.getParameter("cancel") != null) {
            return new ModelAndView(getCancelView());
        }

        return super.processFormSubmission(request, response, command, errors);
    }

    public ModelAndView onSubmit(HttpServletRequest request,
        HttpServletResponse response, Object command, BindException errors)
        throws Exception {
        FileUpload fileUpload = (FileUpload) command;

        // Are we to cancel?
        if (request.getParameter("docancel") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'cancel' from jsp");
            }

            return new ModelAndView(getCancelView());
        } else {
            // validate a file was entered
            if (fileUpload.getFile().length == 0) {
                Object[] args = new Object[] {
                        getText("uploadForm.file", request.getLocale())
                    };
                errors.rejectValue("file", "errors.required", args, "File");

                return showForm(request, response, errors);
            }

            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            CommonsMultipartFile file = (CommonsMultipartFile) multipartRequest.getFile(
                    "file");

            // the directory to upload to
            String uploadDir = resourceFolder + "/" + getServletContext();
            if (!resourceFolder.startsWith("/")) {
            	uploadDir = getServletContext() + "/" + uploadDir;
			}

            // Create the directory if it doesn't exist
            File dirPath = new File(uploadDir);

            if (!dirPath.exists()) {
                dirPath.mkdirs();
            }

            //retrieve the file data
            InputStream stream = file.getInputStream();

            //write the file to the file specified
            OutputStream bos = new FileOutputStream(uploadDir +
                    file.getOriginalFilename());
            int bytesRead = 0;
            byte[] buffer = new byte[8192];

            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.close();

            //close the stream
            stream.close();

            // place the data into the request for retrieval on next page
            request.setAttribute("friendlyName", fileUpload.getName());
            request.setAttribute("fileName", file.getOriginalFilename());
            request.setAttribute("contentType", file.getContentType());
            request.setAttribute("size", file.getSize() + " bytes");
            request.setAttribute("location",
                dirPath.getAbsolutePath() + Constants.FILE_SEP +
                file.getOriginalFilename());

            String link = request.getContextPath() + "/resources" + "/" +
                request.getRemoteUser() + "/";

            request.setAttribute("link", link + file.getOriginalFilename());
        }

        return new ModelAndView(getSuccessView());
    }
}
