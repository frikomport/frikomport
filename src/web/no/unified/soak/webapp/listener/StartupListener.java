/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.listener;

import no.unified.soak.Constants;
import no.unified.soak.service.LookupManager;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.service.CourseStatusManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.webapp.action.ScheduledTasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;

import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * StartupListener class used to initialize and database settings and populate
 * any application-wide drop-downs.
 * 
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 * 
 * @web.listener
 */
public class StartupListener extends ContextLoaderListener implements
		ServletContextListener {
	private static final Log log = LogFactory.getLog(StartupListener.class);
	private static Timer timer = new Timer();

	public void contextInitialized(ServletContextEvent event) {
		if (log.isDebugEnabled()) {
			log.debug("initializing context...");
		}

		// call Spring's context ContextLoaderListener to initialize
		// all the context files specified in web.xml
		super.contextInitialized(event);

		ServletContext context = event.getServletContext();
		String daoType = context.getInitParameter(Constants.DAO_TYPE);

		// if daoType is not specified, use DAO as default
		if (daoType == null) {
			log.warn("No 'daoType' context carameter, using hibernate");
			daoType = Constants.DAO_TYPE_HIBERNATE;
		}

		// Orion starts Servlets before Listeners, so check if the config
		// object already exists
		Map config = (HashMap) context.getAttribute(Constants.CONFIG);

		if (config == null) {
			config = new HashMap();
		}

		// Create a config object to hold all the app config values
		config.put(Constants.DAO_TYPE, daoType);
		context.setAttribute(Constants.CONFIG, config);

		// output the retrieved values for the Init and Context Parameters
		if (log.isDebugEnabled()) {
			log.debug("daoType: " + daoType);
			log.debug("populating drop-downs...");
		}

		setupContext(context);
	}

	public static void setupContext(ServletContext context) {
		ApplicationContext ctx = WebApplicationContextUtils
				.getRequiredWebApplicationContext(context);

		LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");

		// get list of possible roles
		context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());

        // Run tasks
		NotificationManager notificationManager = (NotificationManager) ctx.getBean("notificationManager");
		WaitingListManager waitingListManager = (WaitingListManager) ctx.getBean("waitingListManager");
        CourseStatusManager courseStatusManager = (CourseStatusManager) ctx.getBean("courseStatusManager");
        UserSynchronizeManager userSynchronizeManager = (UserSynchronizeManager) ctx.getBean("userSynchronizeManager");


        ScheduledTasks st = new ScheduledTasks();
        st.addTask(userSynchronizeManager);
        st.addTask(notificationManager);
        st.addTask(waitingListManager);
        st.addTask(courseStatusManager);
        // Here we set the intervals for how often
		timer.schedule(st, Constants.TASK_INITIAL_DELAY, Constants.TASK_RUN_INTERVAL);

		if (log.isDebugEnabled()) {
			log.debug("drop-down initialization complete [OK]");
		}
	}
}
