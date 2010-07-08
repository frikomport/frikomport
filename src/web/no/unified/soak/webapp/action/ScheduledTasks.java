package no.unified.soak.webapp.action;

import java.util.Iterator;
import java.util.TimerTask;
import java.util.Vector;

import no.unified.soak.service.Task;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.jdbc.ConnectionWrapper;

public class ScheduledTasks extends TimerTask {
    private Vector<Task> tasks = null;
    private static final Log log = LogFactory.getLog(ScheduledTasks.class);

    public ScheduledTasks()
    {
        this.tasks = new Vector<Task>();
    }

    public boolean addTask(Task task){
        return tasks.add(task);
    }

	@Override
	public void run() {
		ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
		Thread.currentThread().setContextClassLoader(ConnectionWrapper.class.getClassLoader());
		try {

        Iterator<Task> it = tasks.iterator();
        while (it.hasNext()){
            Task task = it.next();
            try{
                task.executeTask();
            }
            catch (Exception e){
                log.error("Task failed",e);
            }
        }
        log.debug("Ran tasks");
		} finally {
			Thread.currentThread().setContextClassLoader(contextClassLoader);
		}
	}
}
