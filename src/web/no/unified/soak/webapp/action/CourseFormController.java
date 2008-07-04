/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created 20. dec 2005
 */
package no.unified.soak.webapp.action;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import no.unified.soak.Constants;
import no.unified.soak.model.Attachment;
import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.service.AttachmentManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.MailUtil;
import no.unified.soak.webapp.util.FileUtil;

import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * Implementation of SimpleFormController that interacts with the CourseManager
 * to retrieve/persist values to the database.
 * 
 * @author hrj
 */
public class CourseFormController extends BaseFormController {

	private CourseManager courseManager = null;

	private ServiceAreaManager serviceAreaManager = null;

	private PersonManager personManager = null;
	
	private UserManager userManager = null;

	private LocationManager locationManager = null;

	private OrganizationManager organizationManager = null;

	private AttachmentManager attachmentManager = null;

	private RegistrationManager registrationManager = null;

	private NotificationManager notificationManager = null;

	private MessageSource messageSource = null;

	protected MailEngine mailEngine = null;

    protected MailSender mailSender = null;

    protected SimpleMailMessage message = null;
	
	/**
	 * @param notificationManager the notificationManager to set
	 */
	public void setNotificationManager(NotificationManager notificationManager) {
		this.notificationManager = notificationManager;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}

	public void setMailEngine(MailEngine mailEngine) {
		this.mailEngine = mailEngine;
	}

	public void setMessage(SimpleMailMessage message) {
		this.message = message;
	}

	public void setCourseManager(CourseManager courseManager) {
		this.courseManager = courseManager;
	}

	public void setAttachmentManager(AttachmentManager attachmentManager) {
		this.attachmentManager = attachmentManager;
	}

	public void setServiceAreaManager(ServiceAreaManager serviceAreaManager) {
		this.serviceAreaManager = serviceAreaManager;
	}

	public void setPersonManager(PersonManager personManager) {
		this.personManager = personManager;
	}

	public void setUserManager(UserManager userManager) {
		this.userManager = userManager;
	}
	
	public void setLocationManager(LocationManager locationManager) {
		this.locationManager = locationManager;
	}

	public void setOrganizationManager(
			OrganizationManager organizationManager) {
		this.organizationManager = organizationManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
	protected Map referenceData(HttpServletRequest request) throws Exception {
		Map<String, Object> model = new HashMap<String, Object>();
        Locale locale = request.getLocale();
        
        // Retrieve all serviceareas into an array
		List serviceAreas = serviceAreaManager.getAll();
		if (serviceAreas != null) {
			model.put("serviceareas", serviceAreas);
		}

		// Retrieve all people into an array
		List people = personManager.getPersons(null, new Boolean(false));
		if (people != null) {
			model.put("instructors", people);
		}

		List responsibles = userManager.getResponsibles();
		if (responsibles != null) {
			model.put("responsible", responsibles);
		}
		
		// Retrieves visibility roles into an array
		List roles = userManager.getRoles();
		if (roles != null) {
            model.put("roles", roles);
        }

		// Retrieve all locations into an array
		List locations = locationManager.getLocations(null, new Boolean(false));
		if (locations != null) {
			model.put("locations", locations);
		}

		// Retrieve all organization into an array
		List organizations = organizationManager.getAll();
		if (organizations != null) {
			model.put("organizations", organizations);
		}

		// Current time
		List<Date> time = new ArrayList<Date>();
		time.add(new Date());
		model.put("time", time);
		
		setDefaultValues(model, locale);
		
		// Are we to enable mail comment field and buttons?
		Boolean enableMail = new Boolean(false);
		String mailParam = request.getParameter("enableMail");
		if (mailParam != null && mailParam.compareToIgnoreCase("true") == 0) {
			enableMail = new Boolean(true);
		}
		model.put("enableMail", enableMail);

		String courseid = request.getParameter("id");

		// Check whether or not we allow registrations
		if ((courseid != null) && StringUtils.isNumeric(courseid)) {
			Course course = courseManager.getCourse(courseid);
			
			HttpSession session = request.getSession(true);
			session.setAttribute(Constants.ORG_COURSE_KEY, course);

			if (course != null) {
				Date today = new Date();
				Boolean allowRegistration = new Boolean(false);

				if (today.before(course.getRegisterBy())
						&& (course.getRegisterStart() == null || today
								.after(course.getRegisterStart()))) {
					allowRegistration = new Boolean(true);
				}

                if(course.getStatus().intValue() == CourseStatus.COURSE_CANCELLED.intValue()){
                    allowRegistration = new Boolean(false);
                    saveMessage(request,getText("course.status.cancelled", locale));                    
                }

                model.put("allowRegistration", allowRegistration);

				// Retrieve all attachments belonging to the course into an
				// array
				if ((courseid != null) && StringUtils.isNumeric(courseid)
						&& StringUtils.isNotBlank(courseid)
						&& (Long.parseLong(courseid) != 0)) {
					List attachments = attachmentManager
							.getCourseAttachments(new Long(courseid));

					if (attachments != null) {
						model.put("attachments", attachments);
					}
				}
				
                if ((courseid != null) && StringUtils.isNumeric(courseid)
                        && StringUtils.isNotBlank(courseid)
                        && (Long.parseLong(courseid) != 0)) {
                    Integer available = registrationManager.getAvailability(true, course);
                    model.put("isCourseFull", new Boolean(available.intValue() == 0 ));
                    Integer registrations = registrationManager.getNumberOfAttendants(false, course);
                    // Course with registrations cannot be deleted.
                    model.put("canDelete", new Boolean(registrations.intValue() == 0));
                }

                //Check if course is published
                model.put("isPublished", new Boolean(course.getStatus() > 0));
            }
		}
		
				
		return model;
	}

	private void setDefaultValues(Map<String, Object> model, Locale locale) {
		String startTimeTime = messageSource.getMessage("course.startTimeTime", null, locale);
		model.put("startTimeTime",startTimeTime);
		String stopTimeTime = messageSource.getMessage("course.stopTimeTime", null, locale);
		model.put("stopTimeTime",stopTimeTime);

		Date currentTime = new Date();
		SimpleDateFormat dfDate = new SimpleDateFormat("dd.MM.yyyy");
		model.put("registerStartDate", dfDate.format(currentTime));
		SimpleDateFormat dfTime = new SimpleDateFormat("HH:mm");
		model.put("registerStartTime", dfTime.format(currentTime));
	}

	/**
	 * 
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request)
			throws Exception {
		String id = request.getParameter("id");
		String copyid = request.getParameter("copyid");
		Course course = null;
		Course newCourse = null;

		if (!StringUtils.isEmpty(id)) {
			course = courseManager.getCourse(id);
		} else if (!StringUtils.isEmpty(copyid)) {
			course = courseManager.getCourse(copyid);
			newCourse = new Course();
			newCourse.copyAllButId(course);
		} else {
			course = new Course();
			// Check if a default organization should be applied
			User user = (User) request.getSession().getAttribute(Constants.USER_KEY);
			Object omid = user.getOrganizationid();
			if ((omid != null) && StringUtils.isNumeric(omid.toString())) {
				course.setOrganizationid(new Long(omid.toString()));
			}
		}
		if (newCourse != null) {
			return newCourse;
		} else {
			return course;
		}
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

		Map model = new HashMap();
		Course course = (Course) command;
		Long courseId = course.getId();
		
		boolean isNew = (course.getId() == null);
		Locale locale = request.getLocale();

		String mailComment = request.getParameter("mailcomment");

		// Are we to return to the course list?
		if (request.getParameter("return") != null) {
			if (log.isDebugEnabled()) {
				log.debug("recieved 'return' from jsp");
			}
			return new ModelAndView(getCancelView());
		} // or to delete the course?
		else if (request.getParameter("delete") != null) {
			log.debug("recieved 'delete' from jsp");
			// First (try to) delete the course
			courseManager.removeCourse(course.getId().toString());
			// Then send mail (in case the deletion was rejected)
			sendMail(locale, course, Constants.EMAIL_EVENT_COURSEDELETED, mailComment);
			saveMessage(request, getText("course.deleted", locale));
			// Finally go to the course list
			return new ModelAndView(getCancelView());
		} // or to download files?
		else if (request.getParameter("download") != null) {
			log.debug("recieved 'download' from jsp");
			try {
				String attachmentId = request.getParameter("attachmentid");

				if ((attachmentId != null)
						&& StringUtils.isNumeric(attachmentId)
						&& (new Integer(attachmentId).intValue() != 0)) {
					Attachment attachment = attachmentManager
							.getAttachment(new Long(attachmentId));
					String filename = getServletContext().getRealPath(
							"/resources")
							+ "/" + attachment.getStoredname();

					FileUtil.downloadFile(request, response, attachment
							.getContentType(), filename, attachment
							.getFilename());
				}
			} catch (FileNotFoundException fnfe) {
				log.error(fnfe);

				String key = "attachment.sendError";
				errors.reject(getText(key, request.getLocale()));

				return showForm(request, response, errors);
			} catch (IOException ioe) {
				log.error(ioe);

				String key = "errors.ioerror";
				errors.reject(getText(key, request.getLocale()));

				return showForm(request, response, errors);
			}
		} // or to save/update?
        else {
            // Save or publish
            if(request.getParameter("save") != null && isNew){
                course.setStatus(CourseStatus.COURSE_CREATED);
            }
            if(request.getParameter("publish") != null){
                course.setStatus(CourseStatus.COURSE_PUBLISHED);
            }
            if(request.getParameter("cancelled") != null){
                course.setStatus(CourseStatus.COURSE_CANCELLED);
            }
            log.debug("recieved 'save/update' from jsp");
			// Parse date and time fields together
			String format = getText("date.format", request.getLocale()) + " " + getText("time.format", request.getLocale());
			Object[] args = null;

			try {
				Date time = parseDateAndTime(request, "startTime", format);

				if (time != null) {
					course.setStartTime(time);
				} else {
					throw new BindException(course, "startTime");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.startTime", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("startTime", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				Date time = parseDateAndTime(request, "stopTime", format);

				if (time != null) {
					course.setStopTime(time);
				} else {
					throw new BindException(course, "stopTime");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.stopTime", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("stopTime", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				course.setRegisterStart(parseDateAndTime(request,
						"registerStart", format));
			} catch (Exception e) {
				args = new Object[] {
						getText("course.registerStart", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("registerStart", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				course
						.setReminder(parseDateAndTime(request, "reminder",
								format));
			} catch (Exception e) {
				args = new Object[] {
						getText("course.reminder", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("reminder", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				Date time = parseDateAndTime(request, "registerBy", format);

				if (time != null) {
					course.setRegisterBy(time);
				} else {
					throw new BindException(course, "registerBy");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.registerBy", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("registerBy", "errors.dateformat", args,
						"Invalid date or time");
			}

			try {
				Date time = parseDateAndTime(request, "freezeAttendance",
						format);

				if (time != null) {
					course.setFreezeAttendance(time);
				} else {
					throw new BindException(course, "freezeAttendance");
				}
			} catch (Exception e) {
				args = new Object[] {
						getText("course.freezeAttendance", request.getLocale()),
						getText("date.format.localized", request.getLocale()),
						getText("time.format.localized", request.getLocale()) };
				errors.rejectValue("freezeAttendance", "errors.dateformat",
						args, "Invalid date or time");
			}

			if (args != null) {
				return showForm(request, response, errors);
			}

			courseManager.saveCourse(course);

			String key = null;
            if(course.getStatus() == CourseStatus.COURSE_CREATED)
                key = "course.created";
            else if(course.getStatus() == CourseStatus.COURSE_PUBLISHED)
                key = "course.published";
            else if(course.getStatus() == CourseStatus.COURSE_CANCELLED)
                key = "course.cancelled";
            else
                key = "course.updated";

            saveMessage(request, getText(key, locale));

			// If not new, we need to send out a message to everyone registered
			// to the course that things have changed
			// and check if the notificationlist for the course needs to be reset
			if (isNew) {
				model.put("enablemail", "false");
				model.put("newCourse", "true");
				courseId = course.getId();
			} else {
				List<Registration> registrations = registrationManager
						.getSpecificRegistrations(course.getId(), null, null,
								null, null, null, null);
				if (registrations.isEmpty()) {
					model.put("enablemail", "false");
				} else {
					
					//check if there has been a change relevant for users registered on the course
					Course originalCourse = (Course) request.getSession().getAttribute(Constants.ORG_COURSE_KEY);
					List <String> changedList = new ArrayList<String>();
					if (originalCourse != null){
						changedList = courseManager.getChangedList(originalCourse, course, format);
						if (changedList.size() != 0){
							model.put("enablemail", "true");
						}
					}
				}
				
				// If the reminder is set to be after this date, we need to make sure the notifications are set as not-sent
				if (course.getReminder() != null) {
					if (course.getReminder().after(new Date())) {
						log.debug("Resetting notifications");
						notificationManager.resetCourse(course);
					}
				}
			}
		}

		// Let the next page know what course we were editing here
		model.put("id", courseId.toString());

		return new ModelAndView(getSuccessView(), model);
	}

    private Date parseDateAndTime(HttpServletRequest request, String fieldName, String format) throws ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		formatter.setLenient(false);
//		formatter.set2DigitYearStart(formatter.parse("01.01.2000 00:00"));
		String date = request.getParameter(fieldName + "Date");
		String time = request.getParameter(fieldName + "Time");

		if (StringUtils.isEmpty(date)) {
			return null;
		} else if (StringUtils.isEmpty(time)) {
			return (Date) formatter.parse(date + " 00:00");
		} else {
			return (Date) formatter.parse(date + " " + time + ":00");
		}
	}

	/**
	 * Sends mail to the user
	 * 
	 * @param locale
	 *            The locale to use
	 * @param course
	 *            The course the applicant has registered for
	 */
	private void sendMail(Locale locale, Course course, int event,
			String mailComment) {
		log.debug("Sending mail from CourseFormController");
		// Get all registrations
		List<Registration> registrations = registrationManager
		.getSpecificRegistrations(course.getId(), null, null, null,
				null, null, null);

		// Build standard e-mail body
		StringBuffer msg = MailUtil.createStandardBody(course, event, locale, messageSource, mailComment);
		
		// Add sender etc.
		ArrayList<MimeMessage> emails = MailUtil.getMailMessages(registrations, event, course, msg, messageSource, locale, null, mailSender);
		MailUtil.sendMimeMails(emails, mailEngine);		
	}

	/**
	 * @param registrationManager
	 *            The registrationManager to set.
	 */
	public void setRegistrationManager(RegistrationManager registrationManager) {
		this.registrationManager = registrationManager;
	}
}
