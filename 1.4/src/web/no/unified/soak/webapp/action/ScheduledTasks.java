package no.unified.soak.webapp.action;

import java.util.Locale;
import java.util.TimerTask;
import java.util.Vector;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import no.unified.soak.service.Task;

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
	}
}
