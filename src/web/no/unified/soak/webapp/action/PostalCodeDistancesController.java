package no.unified.soak.webapp.action;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Course;
import no.unified.soak.model.PostalCodeCoordinate;
import no.unified.soak.model.StatisticsTableRow;
import no.unified.soak.service.LocationManager;
import no.unified.soak.service.StatisticsManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.DateUtil;
import no.unified.soak.util.PostalCodeDistances;
import no.unified.soak.util.PostalCodesSuperduperLoader;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

public class PostalCodeDistancesController implements Controller {
	private final Log log = LogFactory.getLog(PostalCodeDistancesController.class);

	private StatisticsManager statisticsManager;
	
	private LocationManager locationManager;
	
	/**
	 * @param courseManager the courseManager to set
	 */
	public void setLocationManager(LocationManager courseManager) {
		this.locationManager = courseManager;
	}

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
			log.debug("entering 'onSubmit' method in PostalCodeDistancesController...");
		}


		ModelAndView modelAndView = new ModelAndView("welcome");

		if (!((Boolean) request.getAttribute("isAdmin") || (Boolean) request.getAttribute("isEducationResponsible")
				|| (Boolean) request.getAttribute("isEventResponsible") || (Boolean) request.getAttribute("isReader"))) {
			ApplicationResourcesUtil.saveErrorMessage(request, ApplicationResourcesUtil.getText("access.denied"));
			return modelAndView;
		}

		if (StringUtils.equals("NowPlease", request.getParameter("postalCodeDistancesCalculate"))) {
			return modelAndView;
		}
		
//		final String fromPC = request.getParameter("fromPC");
//		final String toPC = request.getParameter("toPC");
//		if (fromPC != null && toPC != null) {
//			List<PostalCodeCoordinate> coordinates = PostalCodesSuperduperLoader.loadPostalCodes(fromPC, toPC);
//			locationManager.createPostalCodeDistancesInDatabase(coordinates);
//			locationManager.createPostalCodeLocationDistancesInDatabase(coordinates);
//			ApplicationResourcesUtil.saveMessage(request, "");
//		}

		return modelAndView;
	}

}
