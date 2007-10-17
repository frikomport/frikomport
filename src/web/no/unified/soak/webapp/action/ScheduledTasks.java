package no.unified.soak.webapp.action;

import java.util.Locale;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.unified.soak.service.NotificationManager;
import no.unified.soak.service.WaitingListManager;
import no.unified.soak.webapp.listener.StartupListener;

public class ScheduledTasks extends TimerTask {
	WaitingListManager wlmanager = null;
	NotificationManager nmanager = null;
	private static final Log log = LogFactory.getLog(ScheduledTasks.class);
	
	public ScheduledTasks(WaitingListManager mgr, NotificationManager nmgr) {
		if (wlmanager == null) {
			wlmanager = mgr;
		}
		if (nmanager == null) {
			nmanager = nmgr;
		}
	}

	@Override
	public void run() {
		Locale locale = new Locale("NO");
		wlmanager.processEntireWaitingList(locale);
		nmanager.sendReminders();
		log.debug("Ran notification manager and waiting list manager");
	}
}
