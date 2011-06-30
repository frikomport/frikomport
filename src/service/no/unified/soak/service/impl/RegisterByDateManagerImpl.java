	/*
	 * This file is distributed under the GNU Public Licence (GPL).
	 * See http://www.gnu.org/copyleft/gpl.html for further details
	 * of the GPL.
	 *
	 * @author Know IT Objectnet AS
	 * Created November 26 2009
	 */
package no.unified.soak.service.impl;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.mail.internet.MimeMessage;

import no.unified.soak.Constants;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.RegisterByDateManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.MailUtil;
import no.unified.soak.util.PdfUtil;

import org.apache.commons.lang.StringEscapeUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;

/**
 * @author sa
 */
public class RegisterByDateManagerImpl extends BaseManager implements RegisterByDateManager {
    private RegistrationManager registrationManager;
    private ConfigurationManager configurationManager;
    private CourseManager courseManager;
    protected MailEngine mailEngine = null;
    protected MailSender mailSender = null;

    public void executeTask() {
        log.info("running RegisterByDateManager");
        checkRegisterByDates();
    }

    public void setLocale(Locale locale) {}

    public void setMessageSource(MessageSource messageSource) {}

    public void setCourseManager(CourseManager courseManager) {
        this.courseManager = courseManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    public void setConfigurationManager(ConfigurationManager configurationManager) {
    	this.configurationManager = configurationManager;
    }
    
    public void setMailEngine(MailEngine mailEngine) {
        this.mailEngine = mailEngine;
    }

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
    }

    
    /**
     * @see no.unified.soak.service.RegisterByDateManager#checkRegisterByDates()
     */
	public void checkRegisterByDates() {
		// find all courses where registerBy recently expired
		List<Course> courses = courseManager.getCoursesWhereRegisterByExpired(Constants.TASK_RUN_INTERVAL_EVERY_HOUR);
		
		// send registration lists for courses where registerBy date expired less than "Constants.TASK_RUN_INTERVAL" ago
		Iterator<Course> it = courses.iterator();
		while(it.hasNext()) {
			Course course = it.next();
			
	        DateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
	        String start = formatter.format(course.getStartTime());
	        String stop = formatter.format(course.getStopTime());
	        String date = start.equals(stop)? start : start + " - " + stop;
	        
	        String instructorMail = course.getInstructor().getEmail();
	        String responsibleMail = course.getResponsible().getEmail();

	        String[] to = new String[2];
	        to[0] = instructorMail;
	        to[1] = responsibleMail;
	        
	        String[] orderBy = new String[] {"lastName", "firstName"};
	        
	        List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null, (Status)null, null, null, null, null, null, orderBy);
	        
	        String attachementFilename = createPdf(course, registrations);
	        log.debug("attachementFilename: " + attachementFilename);
	        
	        DateFormat f = new SimpleDateFormat("dd-MM-yyyy");
	        String filenameInMail = StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.id")) + "-" + course.getId() + "_" + f.format(course.getStartTime()) + ".pdf";
	        
	    	StringBuffer msg = MailUtil.create_EMAIL_EVENT_REGISTRATIONLIST_body(course, configurationManager.getConfigurationsMap());
			MimeMessage email = MailUtil.getMailMessage(to, null, null, null, (course.getName() + ", " + date), msg, filenameInMail, new File(attachementFilename), mailSender);

			if(email != null && attachementFilename != null) {
				mailEngine.send(email);
			}
		}
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
}
