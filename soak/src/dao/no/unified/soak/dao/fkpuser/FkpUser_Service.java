/**
 * FkpUser_Service.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package no.unified.soak.dao.fkpuser;

public interface FkpUser_Service extends javax.xml.rpc.Service {
    public java.lang.String getFkpUserPortAddress();

    public no.unified.soak.dao.fkpuser.FkpUserPortType getFkpUserPort() throws javax.xml.rpc.ServiceException;

    public no.unified.soak.dao.fkpuser.FkpUserPortType getFkpUserPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
