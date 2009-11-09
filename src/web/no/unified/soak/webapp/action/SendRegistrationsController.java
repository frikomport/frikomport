package no.unified.soak.webapp.action;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Vector;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.MailUtil;
import no.unified.soak.util.PdfUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;


/**
 * Controller for sending registrationlists as mailattachements
 * @author sa
 */
public class SendRegistrationsController implements Controller {
    private final Log log = LogFactory.getLog(SendRegistrationsController.class);
    private RegistrationManager registrationManager = null;
    private CourseManager courseManager = null;
    private UserManager userManager = null;
    private MessageSource messageSource = null;
    private MailEngine mailEngine = null;
    private MailSender mailSender = null;    
	private WaitingListManager waitingListManager = null;
    
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setCourseManager(CourseManager courseManager){
        this.courseManager = courseManager;
    }

    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }
    
	public void setWaitingListManager(WaitingListManager waitingListManager) {
		this.waitingListManager = waitingListManager;
	}

    /**
     * @see org.springframework.web.servlet.mvc.Controller#handleRequest(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse response) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }
        Map<String,Object> model = new HashMap<String,Object>();

        String courseid = request.getParameter("cid");
        Course course = courseManager.getCourse(courseid);
        
        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String start = formatter.format(course.getStartTime());
        String stop = formatter.format(course.getStopTime());
        String date = start.equals(stop)? start : start + " - " + stop;
        
        String instructor = course.getInstructor().getName();
        String instructorMail = course.getInstructor().getEmail();
        String responsible = course.getResponsible().getFullName();
        String responsibleMail = course.getResponsible().getEmail();

        String[] to = new String[2];
        to[0] = instructorMail;
        to[1] = responsibleMail;
        
        Locale locale = request.getLocale();

        String[] orderBy = new String[] {"lastName", "firstName"};
        
        List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null, null, null, null, null, orderBy);
        
        
        String attachementFilename = createPdf(course, registrations);
        log.debug("attachementFilename: " + attachementFilename);
        
        DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
        String filenameInMail = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.id")) + "-" + course.getId() + "_" + f.format(course.getStartTime()) + ".pdf";
        
    	StringBuffer msg = MailUtil.create_EMAIL_EVENT_REGISTRATIONLIST_body(course);
		MimeMessage email = MailUtil.getMailMessage(to, null, null, null, (course.getName() + ", " + date), msg, filenameInMail, new File(attachementFilename), mailSender);

		if(email != null && attachementFilename != null) {
			mailEngine.send(email);
			model.put("success", "true");
		} else { model.put("success", "false"); }

		model.put("coursename", course.getName() + ", " + date);
		model.put("location", course.getLocation().getName() + ", " + course.getLocation().getAddress());
		model.put("instructor", instructor + " <" + instructorMail + ">");
		model.put("responsible", responsible + " <" + responsibleMail + ">");
    		
        return new ModelAndView("registrationsSent", "sent", model);
    }

	private String createPdf(Course course, List<Registration> registrations) {

		try {
			String tempDir = System.getProperty("java.io.tmpdir");
			File file = File.createTempFile("frikom",".pdf", new File(tempDir));
			file.deleteOnExit();
	
			String filename = file.getPath();
			
			PdfUtil pdf = null;
			try {
				pdf = new PdfUtil(filename, PdfUtil.A4, PdfUtil.LANDSCAPE, "Registrationlist: " + course.getName(), "FriKomPort");
				pdf.createRegistrationListExport(course, registrations);
				return filename;
				
			} catch (Exception e) {
				log.error("Error creating pdf document", e);
			} finally {
				if(pdf != null)
					pdf.close();
			}
		}
		catch(IOException e) {
			log.error("Error creating tmpFile", e);
		}
		return null;
	}

    /**
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(String registrationid)
        throws Exception {
        Registration registration = new Registration();
        if (registrationid == null) {
            // ERROR - should never happen
        } else {
            registration = registrationManager.getRegistration(registrationid);
        }
        return registration;
    }

}
