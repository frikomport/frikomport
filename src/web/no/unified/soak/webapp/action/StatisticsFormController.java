package no.unified.soak.webapp.action;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Configuration;
import no.unified.soak.model.Course;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.service.CourseManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.DateUtil;

import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class StatisticsFormController extends BaseFormController {

	private CourseManager courseManager;

	public void setCourseManager(CourseManager courseManager) {
		this.courseManager = courseManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
 BindException errors)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method in StatisticsFormController...");
        }
        Map model = new HashMap();

        if ( !( (Boolean) request.getAttribute("isAdmin") || 
        		(Boolean) request.getAttribute("isEducationResponsible") ||
        		(Boolean) request.getAttribute("isEventResponsible") ||
        		(Boolean) request.getAttribute("isReader") ) ) {
        	saveErrorMessage(request, ApplicationResourcesUtil.getText("access.denied"));
        	return new ModelAndView(getCancelView(), model);
        }

        StatisticsTableRow statisticsBackingObject = (StatisticsTableRow) command;


        // Are we to cancel?
        if (request.getParameter("docancel") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'cancel' from jsp");
            }
            model.put("cancelled", Boolean.TRUE);
            return new ModelAndView(getCancelView(), model);
        }
        
        String beginDateStr = request.getParameter("dateBeginInclusive");
        Date beginDate = DateUtil.convertStringToDate(beginDateStr);
		if (!StringUtils.isEmpty(beginDateStr)) {
			model.put("dateBeginInclusive", beginDate);
        }
        String endDateStr = request.getParameter("dateEndInclusive");
        Date endDate = DateUtil.convertStringToDate(endDateStr);
		if (!StringUtils.isEmpty(endDateStr)) {
			model.put("dateEndInclusive", endDate);
        }
		
		List<Course> courses = courseManager.findByDates(beginDate, endDate);
        
        return new ModelAndView(getSuccessView(), model);
    }

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
    protected Map referenceData(HttpServletRequest request) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method in Administration controller...");
        }

        Map model = new HashMap();
        // Needs to detect also after redirect:
        if (BooleanUtils.toBoolean((String) request.getParameter("updated"))) {
            model.put("updated", Boolean.TRUE);
        }
        if (BooleanUtils.toBoolean((String) request.getParameter("cancelled"))) {
            model.put("cancelled", Boolean.TRUE);
        }

        return model;
    }

    /**
     * formBackingObject used to retrieve lists of configurations from Forms
     * 
     * @author sa
     */
    public class StatisticsTableRow {

    	private String enhet;
    	private Integer antMoter;
    	private Integer antPameldinger;
    	private Integer antOppmotte;
    	private Integer antPameldte;

    	/**
    	 * @return the enhet
    	 */
    	public String getEnhet() {
    		return enhet;
    	}

    	/**
    	 * @param enhet
    	 *            the enhet to set
    	 */
    	public void setEnhet(String enhet) {
    		this.enhet = enhet;
    	}

    	/**
    	 * @return the antMoter
    	 */
    	public Integer getAntMoter() {
    		return antMoter;
    	}

    	/**
    	 * @param antMoter
    	 *            the antMoter to set
    	 */
    	public void setAntMoter(Integer antMoter) {
    		this.antMoter = antMoter;
    	}

    	/**
    	 * Antall førerkortkandidater antas å være likt antall påmeldinger.
    	 * 
    	 * @return the antPameldinger
    	 */
    	public Integer getAntPameldinger() {
    		return antPameldinger;
    	}

    	/**
    	 * @param antPameldinger
    	 *            the antPameldinger to set
    	 */
    	public void setAntPameldinger(Integer antPameldinger) {
    		this.antPameldinger = antPameldinger;
    	}

    	/**
    	 * @return the antOppmotte
    	 */
    	public Integer getAntOppmotte() {
    		return antOppmotte;
    	}

    	/**
    	 * @param antOppmotte
    	 *            the antOppmotte to set
    	 */
    	public void setAntOppmotte(Integer antOppmotte) {
    		this.antOppmotte = antOppmotte;
    	}

    	/**
    	 * @return the antPameldte
    	 */
    	public Integer getAntPameldte() {
    		return antPameldte;
    	}

    	/**
    	 * @param antPameldte
    	 *            the antPameldte to set
    	 */
    	public void setAntPameldte(Integer antPameldte) {
    		this.antPameldte = antPameldte;
    	}
    }
    
}
