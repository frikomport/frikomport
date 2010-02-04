package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.ez.EzUser;

public interface EzUserDAO {

    /**
     * Henter innlogga bruker basert på sessionid
     * @param sessionId Henta fra eZSESSID cookie
     * @return
     */
    public abstract EzUser findUserBySessionID(String sessionId);

    /**
     * Henter brukere med gitte roller.
     * Brukes for å hente brukere med rett til å registrer kurs
     * @param roles
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