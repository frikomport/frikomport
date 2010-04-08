package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.ez.ExtUser;

public interface ExtUserDAO {

    /**
     * Henter innlogga bruker basert p� sessionid
     * @param sessionId Henta fra eZSESSID cookie
     * @return
     */
    public abstract ExtUser findUserBySessionID(String sessionId);

    /**
     * Henter innlogga bruker basert p� username (i http header).
     * @param username Kan v�re henta fra konfigurerbar http header.
     * @return
     */
    public abstract ExtUser findUserByUsername(String username);

    /**
     * Henter brukere med gitte roller.
     * Brukes for � hente brukere med rett til � registrer kurs
     * @param roles
     * @return
     */
    public abstract List<ExtUser> findUsers(List<String> roles);

    /**
     * Henter alle brukere fra ez
     * @return
     */
    public abstract List<ExtUser> findAll();

    /**
     * Henter alle roller definert i ez
     * @return
     */
    public abstract List<String> findRoles();

}