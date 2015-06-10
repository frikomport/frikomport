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
import no.unified.soak.model.Category;
import no.unified.soak.model.Course;
import no.unified.soak.model.Followup;
import no.unified.soak.model.Location;
import no.unified.soak.model.Organization;
import no.unified.soak.model.Person;
import no.unified.soak.model.Registration;
import no.unified.soak.model.User;
import no.unified.soak.model.Organization.Type;
import no.unified.soak.model.Registration.Status;
import no.unified.soak.service.AttachmentManager;
import no.unified.soak.service.CourseAccessException;
import no.unified.soak.service.CourseManager;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.MailEngine;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.OrganizationManager;
import no.unified.soak.service.PersonManager;
import no.unified.soak.service.RegistrationManager;
import no.unified.soak.service.ServiceAreaManager;
import no.unified.soak.service.UserManager;
import no.unified.soak.service.CategoryManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.CourseStatus;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.MailUtil;
import no.unified.soak.util.SMSUtil;
import no.unified.soak.webapp.util.FileUtil;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.dao.DataAccessException;
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

    private CategoryManager categoryManager = null;

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

    public void setMailSender(MailSender mailSender) {
        this.mailSender = mailSender;
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

    public void setOrganizationManager(OrganizationManager organizationManager) {
        this.organizationManager = organizationManager;
    }

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public void setRegistrationManager(RegistrationManager registrationManager) {
        this.registrationManager = registrationManager;
    }

    
    /**
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected Map referenceData(HttpServletRequest request) throws Exception {
        Map<String, Object> model = new HashMap<String, Object>();
        Locale locale = request.getLocale();
        if (log.isDebugEnabled()) {
            log.debug("entering CourseFormController.referenceData() method...");
        }

        List categories = categoryManager.getCategories(new Category(),new Boolean(false));
        if(categories != null){
            model.put("categories",categories);
        }

        // Retrieve all serviceareas into an array
        List serviceAreas = serviceAreaManager.getAllIncludingDummy(getText("misc.none", locale));
        if (serviceAreas != null) {
            model.put("serviceareas", serviceAreas);
        }

        // Retrieve all people into an array
        List people = personManager.getPersons(null, new Boolean(false));
        if (people != null) {
            model.put("instructors", people);
        }

        List responsibles = userManager.getResponsibles(false);
        if (responsibles != null) {
            model.put("responsible", responsibles);
        }

        // Retrieves visibility roles into an array
        List roles = userManager.getRoles();
        if (roles != null) {
            model.put("roles", roles);
        }

        // Retrieve all locations into an array
        List <Location> locations = locationManager.getAllIncludingDummy(null, new Boolean(false), getText("misc.none", locale));
        if (locations != null) {
            model.put("locations", locations);
        }
        
        // Retrieve all organization into an array
        String typeDBvalue = ApplicationResourcesUtil.getText("show.organization.pulldown.typeDBvalue");
        if (typeDBvalue != null) {
        	Integer value = Integer.valueOf(typeDBvalue);
        	Type type = Organization.Type.getTypeFromDBValue(value);
        	model.put("organizations", organizationManager.getByTypeIncludingDummy(type, getText("misc.all", locale)));
        } else {
        	model.put("organizations", organizationManager.getAllIncludingDummy(getText("misc.all", locale)));
        }
        model.put("organizations2", organizationManager.getByTypeIncludingDummy(Organization.Type.AREA, getText("misc.all", locale)));
        
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
        if ((courseid != null) && StringUtils.isNotBlank(courseid) && StringUtils.isNumeric(courseid)) {
            Course course = courseManager.getCourse(courseid);

            HttpSession session = request.getSession(true);

            if (course != null) {
            	session.setAttribute(Constants.ORG_COURSE_KEY, course);

            	Date today = new Date();
                Boolean allowRegistration = new Boolean(false);

                if (today.before(course.getRegisterBy()) && (course.getRegisterStart() == null 
                		|| today.after(course.getRegisterStart()))) {
                    allowRegistration = new Boolean(true);
                }

                if(course.getStatus().equals(CourseStatus.COURSE_CANCELLED)){
                    allowRegistration = new Boolean(false);
                    saveMessage(request, getText("course.status.cancelled", locale));                    
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

                	// evnt. deaktivering av ventelistefunksjonalitet
                    Integer availability = registrationManager.getAvailability(true, course);
                    if (availability.intValue() > 0)
                    	model.put("isCourseFull", new Boolean(false));
                    else { 
                    	if(configurationManager.isActive("access.registration.useWaitlists", true)){
                    		// ventelistefunksjonalitet er aktivert, p�melding tillatt uten ledige plasser
            	        	model.put("isCourseFull", new Boolean(false));
                    		saveMessage(request, getText("errors.courseFull.waitlistwarning", locale));
                    	}
                    	else {
                    		model.put("isCourseFull", new Boolean(true));
                    		saveMessage(request, getText("errors.courseFull.warning", locale));
                    	}
                    }
                    
                    Integer registrations = registrationManager.getNumberOfAttendants(false, course, true);
                    Integer attachments = attachmentManager.getCourseAttachments(course.getId()).size();
                    // Course with registrations cannot be deleted.
                    model.put("canDelete", Boolean.valueOf(registrations.intValue() == 0 && attachments.intValue() == 0));
                    model.put("canUnpublish", Boolean.valueOf(registrations.intValue() == 0));
                    model.put("cancelPrefix", getText("course.numberOfParticipants", new Object[]{registrations}, locale));
                }

                //Check if course is published
                model.put("isPublished", CourseStatus.COURSE_PUBLISHED.equals(course.getStatus()));
                model.put("isCancelled", CourseStatus.COURSE_CANCELLED.equals(course.getStatus()));
            }
            
            String altUsername = (String) request.getAttribute("altusername");
            if (!StringUtils.isEmpty(altUsername)) {
				model.put("hash", (String) request.getParameter("hash"));
            	List<Registration> registrations = registrationManager.getUserRegistrationsForCourse(altUsername, new Long(courseid));
				if (registrations.size() > 0 && registrations.get(0).getId() != null) {
					model.put("isRegistered", Boolean.TRUE);
					model.put("registrationid", registrations.get(0).getId().toString());
				} else {
					model.put("isRegistered", Boolean.FALSE);
				}
            }
            
//			if (BooleanUtils.toBoolean((String) request.getAttribute("unregister"))) {
//				String registrationId = (String) request.getAttribute("registrationId");
//				registrationManager.cancelRegistration(registrationId);
//				List<String> messages = new LinkedList<String>();
//				ApplicationResourcesUtil.getText("registrationCancel.completed");
//				messages.add("P�meldingen er n� slettet");
//				model.put("messages", messages);
//			}
			
			String registrationCanceled = (String) request.getAttribute("registrationCanceled");
			if (BooleanUtils.toBoolean(registrationCanceled)) {
				model.put("isRegistrationCanceled", Boolean.TRUE);
			}
			
        }

        return model;
    }

    private void setDefaultValues(Map<String, Object> model, Locale locale) {
        String startTimeTime = messageSource.getMessage("course.startTimeTime", null, locale);
        model.put("startTimeTime",startTimeTime);
        model.put("followupStartTimeTime",startTimeTime);
        String stopTimeTime = messageSource.getMessage("course.stopTimeTime", null, locale);
        model.put("stopTimeTime",stopTimeTime);
        model.put("followupStopTimeTime",stopTimeTime);

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

		if (!StringUtils.isEmpty(id)) {
			try {
				return courseManager.getCourse(id);
			} catch (DataAccessException e) {
				String[] stringArr = {id};
				request.setAttribute("courseErrorPage.heading.localized", messageSource.getMessage("courseErrorPage.heading", null, request.getLocale()));
				throw new CourseAccessException(messageSource.getMessage("courseErrorPage.message", stringArr, request.getLocale()), e);
			}
		} else if (!StringUtils.isEmpty(copyid)) {
            Course course = courseManager.getCourse(copyid);
            Course newCourse = new Course();
            newCourse.copyAllButId(course);
            newCourse.setCopyid(new Long(copyid));
            return newCourse;
		} else {
			Course course = new Course();
			course.setRole(Constants.ANONYMOUS_ROLE);

            if (!ApplicationResourcesUtil.isSVV()) {
    			Category hendelseCategory = categoryManager.getCategory(Category.Name.HENDELSE.getDBValue());
    			if (hendelseCategory != null) {
    				course.setCategoryid(hendelseCategory.getId());
    			} else {
    				course.setCategoryid(1L);
    			}
    			course.setCategory(hendelseCategory);
            }


			// Check if a default organization should be applied
			User user = (User) request.getSession().getAttribute(Constants.USER_KEY);

			// default organizations
			Object orgId = user.getOrganizationid();
			if ((orgId != null) && StringUtils.isNumeric(orgId.toString())) {
				course.setOrganizationid(new Long(orgId.toString()));
			}

			Object org2id = user.getOrganization2id();
			if ((org2id != null) && StringUtils.isNumeric(org2id.toString())) {
				course.setOrganization2id(new Long(org2id.toString()));
			}

			// Default responsible
			course.setResponsibleUsername(user.getUsername());

            // Add a Followup
            course.setFollowup(new Followup());

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
            log.debug("entering CourseFormController.onSubmit() method...");
        }

        Map<String,Object> model = new HashMap<String,Object>();
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
			log.debug("recieved 'save/publish/cancel' from jsp");
			Object[] args = null;

			args = setDatesToCourseAndMakeErrorMessages(request, errors, course, args);

			setCourseStatus(request, course, isNew);
			
			if(course.getStatus() == CourseStatus.COURSE_CANCELLED){
				/*
				 * Dersom et kurs/m�te skal avlyses velger vi � se bort i fra de faktiske innholdet i 
				 * course-objektet mottatt fra form, og velger derfor � hente orginalen p� nytt og sette 
				 * status p� nytt. 
				 * - Dette gj�res for � unng� evnt. valideringsproblemer n� eneste �nske er � avlyse.
				 */
				courseManager.evict(course); // for � evnt. valideringsfeil pga. deaktivert lokale e.l.
				course = courseManager.getCourse(course.getId().toString());
				course.setStatus(CourseStatus.COURSE_CANCELLED);
			}

			if (!configurationManager.isActive("access.course.usePayment", true)) {
				enrichWithDefaultvaluesToAvoidErrors(course);
			}

			if (validateAnnotations(course, errors, null) > 0) {
				args = new Object[] {};
			}

			if (course.getAttendants() != null && new Date().before(course.getStartTime())) {
				args = new Object[] { DateUtil.convertDateToString(new Date()) };
				errors.rejectValue("attendants", "errors.toearlytosaveAttendants", args,
						"Antall oppm�tte kan ikke registreres f�r arrangementet har startet.");
			}

            args = handleFollowupData(request, errors, course, args);

			if (args != null) {
				courseManager.evict(course);
				return showForm(request, response, errors);
			}

			// h�ndtering av eventer som blir opprettet med dato tilbake i tid
			if(course.getStartTime().before(new Date()) && course.getStopTime().before(new Date())){
				course.setStatus(CourseStatus.COURSE_FINISHED);
			}
			
			courseManager.saveCourse(course);

			String key = getUserMessageKey(request, course, isNew);
			saveMessage(request, getText(key, locale));

			boolean enablemail = false;
			boolean waitinglist = false;

			// If not new, we need to send out a message to everyone registered
			// to the course that things have changed
			// and check if the notificationlist for the course needs to be
			// reset
			if (isNew) {
				// check if this course is a copy and is being published
				if ((course.getCopyid() != null) && (request.getParameter("publish") != null)) {
					List<Long> ids = new ArrayList<Long>();
					ids.add(course.getCopyid());
					List<Registration> registrationsOnOrginalCourse = registrationManager.getWaitingListRegistrations(ids);
					// check if the original course has a waiting list
					if (!registrationsOnOrginalCourse.isEmpty()) {
						enablemail = true;
						waitinglist = true;
					}
				}
				model.put("newCourse", "true");
				courseId = course.getId();
				sendEmailToResponsible(course);

			} else {
				List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null,
						(Status) null, null, null, null, null, null, null);
				if (registrations.isEmpty()) {
					// check if this course is a copy and is being published
					if ((course.getCopyid() != null) && (request.getParameter("publish") != null)) {
						List<Long> ids = new ArrayList<Long>();
						ids.add(course.getCopyid());
						List<Registration> registrationsOnOrginalCourse = registrationManager.getWaitingListRegistrations(ids);
						// check if the original course has a waiting list
						if (!registrationsOnOrginalCourse.isEmpty()) {
							enablemail = true;
							waitinglist = true;
						}
					}
				} else {

					// check if there has been a change relevant for users
					// registered on the course
					Course originalCourse = (Course) request.getSession().getAttribute(Constants.ORG_COURSE_KEY);
					List<String> changedList = new ArrayList<String>();
					if (originalCourse != null) {
						String format = getText("date.format", request.getLocale()) + " " + getText("time.format", request.getLocale());
						changedList = courseManager.getChangedList(originalCourse, course, format);
						if (changedList.size() != 0) {
							enablemail = true;
                            SMSUtil.sendCourseChangedMessage(course, registrations, changedList);
							if(configurationManager.isActive("sms.confirmedRegistrationChangedCourse", false)){
								SMSUtil.sendCourseChangedMessage(course, registrations, changedList);
							}
						}
					}
				}

				// If the reminder is set to be after this date, we need to make
				// sure the notifications are set as not-sent
				if (course.getReminder() != null) {
					if (course.getReminder().after(new Date())) {
						log.debug("Resetting notifications");
						notificationManager.resetCourse(course);
					}
				}
			}
			
			model.put("enablemail", enablemail);
			model.put("waitinglist", waitinglist);
		}

        // Let the next page know what course we were editing here
        model.put("id", courseId.toString());

        return new ModelAndView(getSuccessView(), model);
    }

	private void sendEmailToResponsible(Course course) {
		User responsible = userManager.getUser(course.getResponsibleUsername());
		course.setResponsible(responsible);
		
		Person instructor = personManager.getPerson(course.getInstructorid().toString());
		course.setInstructor(instructor);
		
		Location location = locationManager.getLocation(course.getLocationid().toString());
		course.setLocation(location);
		
		Organization organization = organizationManager.getOrganization(course.getOrganizationid());
		course.setOrganization(organization);
		
		MailUtil.sendCourseCreatedMailToResponsible(course, mailEngine, mailSender, configurationManager.getConfigurationsMap());
	}

	private Object[] setDatesToCourseAndMakeErrorMessages(HttpServletRequest request, BindException errors, Course course,
			Object[] args) {
		String format = getText("date.format", request.getLocale()) + " " + getText("time.format", request.getLocale());

		try {
			Date time = parseDateAndTime(request, "startTime", format);

			if (time != null) {
				course.setStartTime(time);
			} else {
				course.setStartTime(null);
				throw new BindException(course, "startTime");
			}
		} catch (Exception e) {
			args = new Object[] { getText("course.startTime", request.getLocale()),
					getText("date.format.localized", request.getLocale()), getText("time.format.localized", request.getLocale()) };
			errors.rejectValue("startTime", "errors.dateformat", args, "Invalid date or time");
		}

		try {
			Date time = parseDateAndTime(request, "stopTime", format);

			if (time != null) {
				course.setStopTime(time);
			} else {
				course.setStopTime(null);
				throw new BindException(course, "stopTime");
			}
		} catch (Exception e) {
			args = new Object[] { getText("course.stopTime", request.getLocale()),
					getText("date.format.localized", request.getLocale()), getText("time.format.localized", request.getLocale()) };
			errors.rejectValue("stopTime", "errors.dateformat", args, "Invalid date or time");
		}

		try {
			course.setRegisterStart(parseDateAndTime(request, "registerStart", format));
		} catch (Exception e) {
			args = new Object[] { getText("course.registerStart", request.getLocale()),
					getText("date.format.localized", request.getLocale()), getText("time.format.localized", request.getLocale()) };
			errors.rejectValue("registerStart", "errors.dateformat", args, "Invalid date or time");
		}

		try {
			course.setReminder(parseDateAndTime(request, "reminder", format));
		} catch (Exception e) {
			args = new Object[] { getText("course.reminder", request.getLocale()),
					getText("date.format.localized", request.getLocale()), getText("time.format.localized", request.getLocale()) };
			errors.rejectValue("reminder", "errors.dateformat", args, "Invalid date or time");
		}

		try {
			Date registerBy = parseDateAndTime(request, "registerBy", format);
			Date startTime = course.getStartTime();
			boolean useRegisterBy = configurationManager.isActive("access.course.useRegisterBy", true);
			
			if (registerBy != null && useRegisterBy) {
				// alt ok, registerBy settes eksplisitt fra form
				course.setRegisterBy(registerBy);
			}
			else if(registerBy == null && useRegisterBy){
				// feil, registerBy skal settes fra form, men er ikke tilstede
				throw new BindException(course, "registerBy");
			}
			else if(registerBy == null && startTime != null && !useRegisterBy){
				// registerBy settes ikke fra form, men settes fra startTime for kurs/m�te
				course.setRegisterBy(startTime);
			}
			else {
				// registerBy settes ikke fra form, validering av startTime har feilet
				// -- feilmelding skal ikke kastes, da denne vil bli kastet for startTime-feltet
			}
		} catch (Exception e) {
			args = new Object[] { getText("course.registerBy", request.getLocale()),
					getText("date.format.localized", request.getLocale()), getText("time.format.localized", request.getLocale()) };
			errors.rejectValue("registerBy", "errors.dateformat", args, "Invalid date or time");
		}
		return args;
	}

    private Object[] handleFollowupData(HttpServletRequest request, BindException errors, Course course, Object[] args) {
        String format = getText("date.format", request.getLocale()) + " " + getText("time.format", request.getLocale());

        if (course.hasFollowup()) {
            Category category = categoryManager.getCategory(course.getCategoryid());
            if ((category != null) && category.getUseFollowup()) {
                Followup followup = course.getFollowup();

                try {
                    Date time = parseDateAndTime(request, "followupStartTime", format);

                    if (time != null) {
                        followup.setStartTime(time);
                    }
                    else {
                        followup.setStartTime(null);
                        throw new BindException(followup, "startTime");
                    }
                }
                catch (Exception e) {
                    args = new Object[] {
                        getText("followup.startTime", request.getLocale()),
                        getText("date.format.localized", request.getLocale()),
                        getText("time.format.localized", request.getLocale())
                    };
                    errors.rejectValue("followup.startTime", "errors.dateformat", args, "Invalid date or time");
                }

                try {
                    Date time = parseDateAndTime(request, "followupStopTime", format);

                    if (time != null) {
                        followup.setStopTime(time);
                    } else {
                        followup.setStopTime(null);
                        throw new BindException(followup, "stopTime");
                    }
                }
                catch (Exception e) {
                    args = new Object[] {
                        getText("followup.stopTime", request.getLocale()),
                        getText("date.format.localized", request.getLocale()),
                        getText("time.format.localized", request.getLocale())
                    };
                    errors.rejectValue("followup.stopTime", "errors.dateformat", args, "Invalid date or time");
                }

                Date start = followup.getStartTime();
                if (start != null) {
                    Date stop = followup.getStopTime();
                    if ((stop != null) && (start.after(stop)) || start.equals(stop)) {
                        args = new Object[] {
                            getText("followup.startTime", request.getLocale()),
                            getText("followup.stopTime", request.getLocale())
                        };
                        errors.rejectValue("followup.startTime", "errors.XMustBeLessThanY", args, "Start time must be less than stop time");
                    }

                    stop = course.getStopTime();
                    if ((stop != null) && (stop.after(start))) {
                        args = new Object[] {
                            getText("followup.startTime", request.getLocale()),
                            getText("course.stopTime", request.getLocale())
                        };
                        errors.rejectValue("followup.startTime", "followup.error.mustbeafter", args, "Start time of followup must be after stop time of course");
                    }
                }

                try {
                    followup.setReminder(parseDateAndTime(request, "followupReminder", format));
                }
                catch (Exception e) {
                    args = new Object[] {
                        getText("followup.reminder", request.getLocale()),
                        getText("date.format.localized", request.getLocale()),
                        getText("time.format.localized", request.getLocale())
                    };
                    errors.rejectValue("followup.reminder", "errors.dateformat", args, "Invalid date or time");
                }

                if (followup.getLocationid() == null) {
                    args = new Object[] {
                        getText("followup.locationid", request.getLocale())
                    };
                    errors.rejectValue("followup.location", "errors.required", args, "Is required");
                }
            }
            else {
                //Course shouldn't have the followup - remove it!
                course.setFollowup(null);
            }
        }

        return args;
    }

	private void setCourseStatus(HttpServletRequest request, Course course, boolean isNew) {
		if (request.getParameter("save") != null && isNew) {
			course.setStatus(CourseStatus.COURSE_CREATED);
		}
		if (request.getParameter("save") != null && CourseStatus.COURSE_FINISHED.equals(course.getStatus())
				&& course.getStartTime() != null && new Date().before(course.getStartTime())) {
			course.setStatus(CourseStatus.COURSE_CREATED);
		}
		if (request.getParameter("unpublish") != null) {
			course.setStatus(CourseStatus.COURSE_CREATED);
		}
		if (request.getParameter("publish") != null) {
			course.setStatus(CourseStatus.COURSE_PUBLISHED);
		}
		if (request.getParameter("cancelled") != null) {
			course.setStatus(CourseStatus.COURSE_CANCELLED);
		}
	}

	private String getUserMessageKey(HttpServletRequest request, Course course, boolean isNew) {
		String key = null;
		if (course.getStatus().equals(CourseStatus.COURSE_PUBLISHED)) {
			key = "course.published";
		} else if (StringUtils.isNotBlank(request.getParameter("unpublish"))) {
			key = "course.unpublished";
		} else if (course.getStatus().equals(CourseStatus.COURSE_CREATED) || isNew) {
			key = "course.created";
		} else if (course.getStatus().equals(CourseStatus.COURSE_CANCELLED)) {
			key = "course.cancelled";
		} else {
			key = "course.updated";
		}
		return key;
	}

	private void enrichWithDefaultvaluesToAvoidErrors(Course course) {
		if(!configurationManager.isActive("access.course.usePayment", true)){ // dersom betaling for kurs er deaktivert
			if (course.getReservedInternal() == null) {
				course.setReservedInternal(0);
			}
			if (course.getFeeInternal() == null) {
				course.setFeeInternal(0d);
			}
			if (course.getFeeExternal() == null) {
				course.setFeeExternal(0d);
			}
		}

		if(course.getRegisterBy() == null && !configurationManager.isActive("access.course.useRegisterBy", true)){
			// setter p�meldingsfrist til starttid dersom p�meldingsfrist ikke benyttes
			course.setRegisterBy(course.getStartTime());
		}
		
		if (StringUtils.isEmpty(course.getDuration()) && !configurationManager.isActive("access.course.showDuration", true)){
			course.setDuration("N/A");
		}
		
		if (StringUtils.isEmpty(course.getName()) && !configurationManager.isActive("access.course.showCourseName", true)){
            Category category = categoryManager.getCategory(course.getCategoryid());
            String name = (category != null)? category.getDefaultName() : null;
			course.setName((name != null)? name : ApplicationResourcesUtil.getText("course.defaultname"));
		}
		
		if(StringUtils.isEmpty(course.getRole())){
			course.setRole(Constants.ANONYMOUS_ROLE);
		}
		
		if(ApplicationResourcesUtil.isSVV()){
			// p�meldinger er ikke offentlig tilgjengelig via courseDetails.jsp
			course.setRestricted(true);
			course.setRegisterStart(new Date());
		}
	}

	protected Date parseDateAndTime(HttpServletRequest request, String fieldName, String format) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        formatter.setLenient(false);
        formatter.set2DigitYearStart(formatter.parse("01.01.2000 00:00"));
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
    private void sendMail(Locale locale, Course course, int event, String mailComment) {
        log.debug("Sending mail from CourseFormController");
        // Get all registrations
        List<Registration> registrations = registrationManager.getSpecificRegistrations(course.getId(), null, null, (Status) null, null, null, null, null, null, null);

        // Build standard e-mail body
		StringBuffer msg = null;
		switch(event) {
			case Constants.EMAIL_EVENT_COURSEDELETED:
				msg = MailUtil.create_EMAIL_EVENT_COURSEDELETED_body(course, mailComment, configurationManager.getConfigurationsMap());
				break;
			default:
				if(log.isDebugEnabled()) log.debug("sendMail: Handling of event:" + event + " not implemented..!");
		}
        
        // Add sender etc.
        ArrayList<MimeMessage> emails = MailUtil.getMailMessages(registrations, event, course, msg, null, mailSender, false);
        MailUtil.sendMimeMails(emails, mailEngine);		
        
		if(configurationManager.isActive("mail.course.sendSummary", true)) {
			MailUtil.sendSummaryToResponsibleAndInstructor(course, null, registrations, msg, mailEngine, mailSender);
		}
    }

}
