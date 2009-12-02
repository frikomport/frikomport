package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.ez.EzUser;

public interface EzUserDAO {

    /**
     * Henter innlogga bruker basert p� sessionid
     * @param sessionId Henta fra eZSESSID cookie
     * @return
     */
    public abstract EzUser findUserBySessionID(String sessionId);

    /**
     * Henter brukere med gitte roller.
     * Brukes for � hente brukere med rett til � registrer kurs
     * @param roles TODO
     * @return
     */
    public abstract List<EzUser> findUsers(List<String> roles);

    /**
     * Henter alle brukere fra ez
     * @return
     */
    public abstract List<EzUser> findAll();

    /**
     * Henter alle roller definert i ez
     * @return
     */
    public abstract List<String> findRoles();

}