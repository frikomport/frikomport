package no.unified.soak.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import no.unified.soak.model.Course;
import no.unified.soak.model.Registration;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SMSUtil {

	public static final Log log = LogFactory.getLog(MailUtil.class);

	public SMSUtil(){}
	
	public static void sendRegistrationConfirmedMessage(Registration registration, Course course){
		String mobilnr = registration.getMobilePhone();
		if(mobilnr == null) return; // no mobilnr in registration

    	String courseName = course.getName();
    	String courseLocation = course.getLocation().getName() + ", " + course.getLocation().getAddress();
    	String courseStart = DateUtil.getDateTime(ApplicationResourcesUtil.getText("datetime.format"), course.getStartTime());

		String msg = ApplicationResourcesUtil.getText("misc.hello") + " " + registration.getFirstName() + "!\n";
		msg += "Du er p�meldt \"" + courseName + "\" " + courseStart + " p� " + courseLocation + ".\n";
		msg += "Hilsen Statens vegvesen";
		
		// avventer tekst fra SVV
		
		SMS sms = new SMS(mobilnr, msg);
		log.info(sms);
		// send sms
	}
	
	public static void sendCourseChangedMessage(Course course, List<Registration> registrations, List<String> changedList){
		String msg = "";
		List<SMS> toSend = new ArrayList<SMS>();
		
        if(changedList != null) {
        	String courseName = course.getName();
        	String courseLocation = course.getLocation().getName();
        	String courseStart = DateUtil.getDateTime(ApplicationResourcesUtil.getText("datetime.format"), course.getStartTime());

        	if(changedList.contains("status") && course.getStatus() == CourseStatus.COURSE_CANCELLED){
        		// m�tet er avlyst
        		// avventer tekst fra SVV
        		
        		msg += "\"" + courseName + "\" p� " + courseLocation + " " + courseStart + " som du er p�meldt dessverre er avlyst!\n";
        		msg += "Hilsen Statens vegvesen";
        	}
        	else if(changedList.contains("startTime") || changedList.contains("location")){
        		// m�tet er endret
        		// avventer tekst fra SVV
        		msg += "\"" + courseName + "\" er endret!\n";
        		
        		if (changedList.contains("startTime")) {
		            msg += StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.startTime"))
		                    + ": "
		                    + DateUtil.getDateTime(ApplicationResourcesUtil.getText("datetime.format"), course.getStartTime()) + "\n";
		        }
	
		        if (changedList.contains("location")) {
		            msg += StringEscapeUtils.unescapeHtml(ApplicationResourcesUtil.getText("course.location")) + ": "
		                    + course.getLocation().getName() + ", " + course.getLocation().getAddress() + "\n";
		        }
		        
		        msg += "Hilsen Statens vegvesen";
        	}
        	
        	if(msg.length() > 0){
        		Iterator<Registration> it = registrations.iterator();
        		while(it.hasNext()){
        			Registration r = it.next();
        			String mobil = r.getMobilePhone();
        			if(mobil != null){
        				String name = r.getFirstName();
        				String pmsg = ApplicationResourcesUtil.getText("misc.hello") + " " + name + "!\n" + msg;
        				SMS sms = new SMS(mobil, pmsg);
        				toSend.add(sms);
        			}
        		}
        		
        		Iterator<SMS> s = toSend.iterator();
        		while(s.hasNext()){
        			SMS m = s.next();
        			// send m
        			log.info(m);
        		}
        	}
        }
	}
}

class SMS {
	String mobil;
	String message;
	int length;
	
	public SMS(String mobil, String message){
		this.mobil = mobil;
		this.message = message;
	}
	
	public void setMobil(String mobil){
		this.mobil = mobil;
	}
	
	public void setMessage(String message){
		this.message = message;
	}
	
	public String getMobil(){
		return mobil;
	}
	
	public String getMessage(){
		return message;
	}
	
	public int length(){
		if(message == null) return 0;
		return message.length();
	}
	
	public String toString(){
		return mobil + ": \"" + message + "\" (" + length() + ")";
	}
}

