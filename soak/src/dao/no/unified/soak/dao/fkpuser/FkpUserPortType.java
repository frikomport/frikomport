/**
 * FkpUserPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package no.unified.soak.dao.fkpuser;

public interface FkpUserPortType extends java.rmi.Remote {

    /**
     * Fetches user based on ezsessid
     */
    public no.unified.soak.dao.fkpuser.FkpUser_Type getUser(java.lang.String key) throws java.rmi.RemoteException;

    /**
     * Fetches users based on roles
     */
    public no.unified.soak.dao.fkpuser.FkpUser_Type[] getUsers(java.lang.String roles) throws java.rmi.RemoteException;

    /**
     * Fetches roles for a user
     */
    public no.unified.soak.dao.fkpuser.FkpRole[] getRoles(java.lang.String userid) throws java.rmi.RemoteException;

    /**
     * Fetches all roles
     */
    public no.unified.soak.dao.fkpuser.FkpRole[] getAllRoles() throws java.rmi.RemoteException;
}
