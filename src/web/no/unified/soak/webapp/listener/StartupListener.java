/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
package no.unified.soak.webapp.listener;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import no.unified.soak.Constants;
import no.unified.soak.service.CourseStatusManager;
import no.unified.soak.service.DatabaseUpdateManager;
import no.unified.soak.service.DecorCacheManager;
import no.unified.soak.service.LookupManager;
import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.RegisterByDateManager;
import no.unified.soak.service.UserSynchronizeManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.webapp.action.ScheduledTasks;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.WebApplicationContextUtils;

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
		ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(context);

		LookupManager mgr = (LookupManager) ctx.getBean("lookupManager");

		// get list of possible roles
		context.setAttribute(Constants.AVAILABLE_ROLES, mgr.getAllRoles());

        // Onetimetasks
        DatabaseUpdateManager databaseUpdateManager = (DatabaseUpdateManager) ctx.getBean("databaseUpdateManager");

        // Recurring tasks
    	RegisterByDateManager registerByDateManager = (RegisterByDateManager) ctx.getBean("registerByDateManager");
		NotificationManager notificationManager = (NotificationManager) ctx.getBean("notificationManager");
		WaitingListManager waitingListManager = (WaitingListManager) ctx.getBean("waitingListManager");
        CourseStatusManager courseStatusManager = (CourseStatusManager) ctx.getBean("courseStatusManager");
        UserSynchronizeManager userSynchronizeManager = (UserSynchronizeManager) ctx.getBean("userSynchronizeManager");
        DecorCacheManager decorCacheManager = (DecorCacheManager) ctx.getBean("decorCacheManager");

        // Tasks to be completed once
        ScheduledTasks once = new ScheduledTasks();
        once.addTask(databaseUpdateManager);
        timer.schedule(once, Constants.TASK_IMMEDIATE);

        // Tasks to happen regularly
        ScheduledTasks recurring1 = new ScheduledTasks();
        recurring1.addTask(decorCacheManager);
        recurring1.addTask(courseStatusManager);
        if(!ApplicationResourcesUtil.isSVV()) recurring1.addTask(registerByDateManager);
        recurring1.addTask(notificationManager);
    	recurring1.addTask(waitingListManager);

    	// Here we set the intervals for how often
    	timer.schedule(recurring1, Constants.TASK_INITIAL_DELAY, Constants.TASK_RUN_INTERVAL_EVERY_HOUR);

    	
        // synchronization every night at 01:00
        Calendar scheduled = new GregorianCalendar();
        scheduled.roll(Calendar.DAY_OF_YEAR, true);
        scheduled.set(Calendar.HOUR_OF_DAY, 1);
        scheduled.set(Calendar.MINUTE, 0);
        scheduled.set(Calendar.SECOND, 0);
        scheduled.set(Calendar.MILLISECOND, 0);
        
        ScheduledTasks recurring2 = new ScheduledTasks();
        recurring2.addTask(userSynchronizeManager);
    	timer.schedule(recurring2, scheduled.getTime(), Constants.TASK_RUN_INTERVAL_EVERY_DAY);
		
		if (log.isDebugEnabled()) {
			log.debug("drop-down initialization complete [OK]");
		}
	}
}
