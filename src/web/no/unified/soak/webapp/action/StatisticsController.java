package no.unified.soak.webapp.action;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Course;
import no.unified.soak.model.StatisticsTableRow;
import no.unified.soak.service.StatisticsManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.DateUtil;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class StatisticsController implements Controller {
	private final Log log = LogFactory.getLog(StatisticsController.class);

	private StatisticsManager statisticsManager;

	public void setStatisticsManager(StatisticsManager statisticsManager) {
		this.statisticsManager = statisticsManager;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object,
	 *      org.springframework.validation.BindException)
	 */
	public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse reponse) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'onSubmit' method in StatisticsFormController...");
		}

		ModelAndView modelAndView = new ModelAndView("statistics");

		if (!((Boolean) request.getAttribute("isAdmin") || (Boolean) request.getAttribute("isEducationResponsible")
				|| (Boolean) request.getAttribute("isEventResponsible") || (Boolean) request.getAttribute("isReader"))) {
			ApplicationResourcesUtil.saveErrorMessage(request, ApplicationResourcesUtil.getText("access.denied"));
			return modelAndView;
		}

		String beginDateStr = request.getParameter("dateBeginInclusive");
		Date beginDate = null;
		if (!StringUtils.isEmpty(beginDateStr)) {
			beginDate = DateUtil.convertStringToDate(beginDateStr);
			modelAndView.addObject("dateBeginInclusive", beginDateStr);
		} else {
			ApplicationResourcesUtil.saveMessage(request, ApplicationResourcesUtil.getText("statistics.message.dateBeginInclusive"));
		}

		String endDateStr = request.getParameter("dateEndInclusive");
		Date endDate = null;
		if (!StringUtils.isEmpty(endDateStr)) {
			endDate = DateUtil.convertStringToDate(endDateStr);
			modelAndView.addObject("dateEndInclusive", endDateStr);
		} else {
			ApplicationResourcesUtil.saveMessage(request, ApplicationResourcesUtil.getText("statistics.message.dateEndInclusive"));
		}

		if (beginDate != null && endDate != null) {
				List<StatisticsTableRow> statisticsRows = statisticsManager.findByDates(beginDate, endDate);
				modelAndView.addObject("statisticsRows", statisticsRows);
				
				List<Course> emptyCourses = statisticsManager.findEmptyCoursesByDates(beginDate, endDate);
				modelAndView.addObject("courseList", emptyCourses);
		}
		return modelAndView;
	}

}
