package no.unified.soak.service;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 05.jun.2008
 * Time: 10:21:55
 * To change this template use File | Settings | File Templates.
 */
public interface DatabaseUpdateManager extends Task{

    /**
     * Updates courses according to new scheme
     */
    public void updateDatabase();
}
