/**
 * FkpUser_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package no.unified.soak.dao.fkpuser;

public class FkpUser_ServiceLocator extends org.apache.axis.client.Service implements no.unified.soak.dao.fkpuser.FkpUser_Service {

    private static final long serialVersionUID = 138295962537922086L;

    public FkpUser_ServiceLocator() {
    }


    public FkpUser_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public FkpUser_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for FkpUserPort
    private java.lang.String FkpUserPort_address = "http://localhost/nusoap.php/fkpuser";

    public java.lang.String getFkpUserPortAddress() {
        return FkpUserPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String FkpUserPortWSDDServiceName = "FkpUserPort";

    public java.lang.String getFkpUserPortWSDDServiceName() {
        return FkpUserPortWSDDServiceName;
    }

    public void setFkpUserPortWSDDServiceName(java.lang.String name) {
        FkpUserPortWSDDServiceName = name;
    }

    public no.unified.soak.dao.fkpuser.FkpUserPortType getFkpUserPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(FkpUserPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getFkpUserPort(endpoint);
    }

    public no.unified.soak.dao.fkpuser.FkpUserPortType getFkpUserPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            no.unified.soak.dao.fkpuser.FkpUserBindingStub _stub = new no.unified.soak.dao.fkpuser.FkpUserBindingStub(portAddress, this);
            _stub.setPortName(getFkpUserPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setFkpUserPortEndpointAddress(java.lang.String address) {
        FkpUserPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (no.unified.soak.dao.fkpuser.FkpUserPortType.class.isAssignableFrom(serviceEndpointInterface)) {
                no.unified.soak.dao.fkpuser.FkpUserBindingStub _stub = new no.unified.soak.dao.fkpuser.FkpUserBindingStub(new java.net.URL(FkpUserPort_address), this);
                _stub.setPortName(getFkpUserPortWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        java.lang.String inputPortName = portName.getLocalPart();
        if ("FkpUserPort".equals(inputPortName)) {
            return getFkpUserPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:fkpuser", "FkpUser");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:fkpuser", "FkpUserPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("FkpUserPort".equals(portName)) {
            setFkpUserPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
