package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.RoleEnum;

public interface ExtUserDAO {

    /**
     * Henter innlogga bruker basert på sessionid
     * @param sessionId Henta fra eZSESSID cookie
     * @return
     */
    public abstract ExtUser findUserBySessionID(String sessionId);

    /**
     * Henter innlogga bruker basert på username (i http header).
     * @param username Kan være henta fra konfigurerbar http header.
     * @return
     */
    public abstract ExtUser findUserByUsername(String username);

    /**
     * Henter brukere med gitte roller.
     * Brukes for å hente brukere med rett til å registrer kurs
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
    
	/**
	 * Gives rolename in the external system for a specific RoleEnum.
	 * 
	 * @param role
	 * @return
	 */
	public abstract String getStringForRole(RoleEnum role);

}