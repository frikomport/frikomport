package no.unified.soak.service;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 04.jun.2008
 * Time: 11:30:38
 * To change this template use File | Settings | File Templates.
 */
public interface UserSynchronizeManager extends Task{

    /**
     * Checks users and updates local database
     */
    public void synchronizeUsers();
}
