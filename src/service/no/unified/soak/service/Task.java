package no.unified.soak.service;

import java.util.Locale;

/**
 * User: gv
 * Date: 04.jun.2008
 * Time: 10:27:23
 */
public interface Task extends Manager {

    /**
     * Execute the task
     */
    public void executeTask();

    /**
     * In case the task need Locale
     */
    public void setLocale(Locale locale);
}
