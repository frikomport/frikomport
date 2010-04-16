/**
 * FkpUserBindingStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Nov 19, 2006 (02:31:34 GMT+00:00) WSDL2Java emitter.
 */

package no.unified.soak.dao.ldapuser;

public class FkpUserBindingStub extends org.apache.axis.client.Stub implements no.unified.soak.dao.ldapuser.FkpUserPortType {
    private java.util.Vector cachedSerClasses = new java.util.Vector();
    private java.util.Vector cachedSerQNames = new java.util.Vector();
    private java.util.Vector cachedSerFactories = new java.util.Vector();
    private java.util.Vector cachedDeserFactories = new java.util.Vector();

    static org.apache.axis.description.OperationDesc [] _operations;

    static {
        _operations = new org.apache.axis.description.OperationDesc[1];
        _initOperationDesc1();
    }

    private static void _initOperationDesc1(){
        org.apache.axis.description.OperationDesc oper;
        org.apache.axis.description.ParameterDesc param;
        oper = new org.apache.axis.description.OperationDesc();
        oper.setName("opOppslagSVVAnsatt");
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:no:vegvesen:ldap.wsdl:OppslagSVVAnsatt:1:0", "WrapperImp"), org.apache.axis.description.ParameterDesc.INOUT, new javax.xml.namespace.QName("urn:no:vegvesen:felles:wrapper:cct:1:0", "WrapperListType"), WrapperType[].class, false, true);
//        param.setItemQName(new javax.xml.namespace.QName("urn:no:vegvesen:felles:wrapper:cct:1:0", "WrapperElement"));
//        oper.addParameter(param);
//        param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0", "Request"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("urn:no:vegvesen:ldap:soa2:SVVUIDType:cct:1:0", "SVVUID"), SVVUID.class, true, false);
//        oper.addParameter(param);
//        oper.setReturnType(new javax.xml.namespace.QName("urn:no:vegvesen:ldap:soa2:SVVAnsattListeType:cct:1:0", "ListeType"));
//        oper.setReturnClass(_0._1.cct.SVVAnsattListeType.soa2.ldap.vegvesen.no.ObjSVVAnsattType[].class);
//        oper.setReturnQName(new javax.xml.namespace.QName("urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0", "Response"));
//        param = oper.getReturnParamDesc();
//        param.setItemQName(new javax.xml.namespace.QName("urn:no:vegvesen:ldap:soa2:SVVAnsattListeType:cct:1:0", "ObjSVVAnsatt"));
//        oper.setStyle(org.apache.axis.constants.Style.DOCUMENT);
//        oper.setUse(org.apache.axis.constants.Use.LITERAL);
//        oper.addFault(new org.apache.axis.description.FaultDesc(
//                      new javax.xml.namespace.QName("urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0", "Error"),
//                      "_0._1.cct.SVVFeilType.landskap.vegvesen.no.SVVFeilType",
//                      new javax.xml.namespace.QName("urn:no:vegvesen:landskap:SVVFeilType:cct:1:0", "SVVFeilType"), 
//                      true
//                     ));
        _operations[0] = oper;
        
        

    }
    
    public FkpUserBindingStub() throws org.apache.axis.AxisFault {
         this(null);
    }

    public FkpUserBindingStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
         this(service);
         super.cachedEndpoint = endpointURL;
    }

    public FkpUserBindingStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
        if (service == null) {
            super.service = new org.apache.axis.client.Service();
        } else {
            super.service = service;
        }
        ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
            java.lang.Class cls;
            javax.xml.namespace.QName qName;
            javax.xml.namespace.QName qName2;
            java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
            java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
            java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
            java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
            java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
            java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
            java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
            java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
            java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
            java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
            qName = new javax.xml.namespace.QName("urn:fkpuser", "FkpRole");
            cachedSerQNames.add(qName);
            cls = no.unified.soak.dao.fkpuser.FkpRole.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fkpuser", "FkpRoles");
            cachedSerQNames.add(qName);
            cls = no.unified.soak.dao.fkpuser.FkpRole[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:fkpuser", "FkpRole");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

            qName = new javax.xml.namespace.QName("urn:fkpuser", "FkpUser");
            cachedSerQNames.add(qName);
            cls = no.unified.soak.dao.fkpuser.FkpUser_Type.class;
            cachedSerClasses.add(cls);
            cachedSerFactories.add(beansf);
            cachedDeserFactories.add(beandf);

            qName = new javax.xml.namespace.QName("urn:fkpuser", "FkpUsers");
            cachedSerQNames.add(qName);
            cls = no.unified.soak.dao.fkpuser.FkpUser_Type[].class;
            cachedSerClasses.add(cls);
            qName = new javax.xml.namespace.QName("urn:fkpuser", "FkpUser");
            qName2 = null;
            cachedSerFactories.add(new org.apache.axis.encoding.ser.ArraySerializerFactory(qName, qName2));
            cachedDeserFactories.add(new org.apache.axis.encoding.ser.ArrayDeserializerFactory());

    }

    protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
        try {
            org.apache.axis.client.Call _call = super._createCall();
            if (super.maintainSessionSet) {
                _call.setMaintainSession(super.maintainSession);
            }
            if (super.cachedUsername != null) {
                _call.setUsername(super.cachedUsername);
            }
            if (super.cachedPassword != null) {
                _call.setPassword(super.cachedPassword);
            }
            if (super.cachedEndpoint != null) {
                _call.setTargetEndpointAddress(super.cachedEndpoint);
            }
            if (super.cachedTimeout != null) {
                _call.setTimeout(super.cachedTimeout);
            }
            if (super.cachedPortName != null) {
                _call.setPortName(super.cachedPortName);
            }
            java.util.Enumeration keys = super.cachedProperties.keys();
            while (keys.hasMoreElements()) {
                java.lang.String key = (java.lang.String) keys.nextElement();
                _call.setProperty(key, super.cachedProperties.get(key));
            }
            // All the type mapping information is registered
            // when the first call is made.
            // The type mapping information is actually registered in
            // the TypeMappingRegistry of the service, which
            // is the reason why registration is only needed for the first call.
            synchronized (this) {
                if (firstCall()) {
                    // must set encoding style before registering serializers
                    _call.setEncodingStyle(null);
                    for (int i = 0; i < cachedSerFactories.size(); ++i) {
                        java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
                        javax.xml.namespace.QName qName =
                                (javax.xml.namespace.QName) cachedSerQNames.get(i);
                        java.lang.Object x = cachedSerFactories.get(i);
                        if (x instanceof Class) {
                            java.lang.Class sf = (java.lang.Class)
                                 cachedSerFactories.get(i);
                            java.lang.Class df = (java.lang.Class)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                        else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
                            org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
                                 cachedSerFactories.get(i);
                            org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
                                 cachedDeserFactories.get(i);
                            _call.registerTypeMapping(cls, qName, sf, df, false);
                        }
                    }
                }
            }
            return _call;
        }
        catch (java.lang.Exception _t) {
            throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
        }
    }

    public no.unified.soak.dao.fkpuser.FkpUser_Type getUser(java.lang.String key) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[0]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:fkpuserwsdl#getUser");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:fkpuserwsdl", "getUser"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {key});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (no.unified.soak.dao.fkpuser.FkpUser_Type) _resp;
            } catch (java.lang.Exception _exception) {
                return (no.unified.soak.dao.fkpuser.FkpUser_Type) org.apache.axis.utils.JavaUtils.convert(_resp, no.unified.soak.dao.fkpuser.FkpUser_Type.class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public no.unified.soak.dao.fkpuser.FkpUser_Type[] getUsers(java.lang.String roles) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[1]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:fkpuserwsdl#getUsers");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:fkpuserwsdl", "getUsers"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {roles});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (no.unified.soak.dao.fkpuser.FkpUser_Type[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (no.unified.soak.dao.fkpuser.FkpUser_Type[]) org.apache.axis.utils.JavaUtils.convert(_resp, no.unified.soak.dao.fkpuser.FkpUser_Type[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public no.unified.soak.dao.fkpuser.FkpRole[] getRoles(java.lang.String userid) throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[2]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:fkpuserwsdl#getRoles");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:fkpuserwsdl", "getRoles"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {userid});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (no.unified.soak.dao.fkpuser.FkpRole[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (no.unified.soak.dao.fkpuser.FkpRole[]) org.apache.axis.utils.JavaUtils.convert(_resp, no.unified.soak.dao.fkpuser.FkpRole[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

    public no.unified.soak.dao.fkpuser.FkpRole[] getAllRoles() throws java.rmi.RemoteException {
        if (super.cachedEndpoint == null) {
            throw new org.apache.axis.NoEndPointException();
        }
        org.apache.axis.client.Call _call = createCall();
        _call.setOperation(_operations[3]);
        _call.setUseSOAPAction(true);
        _call.setSOAPActionURI("urn:fkpuserwsdl#getAllRoles");
        _call.setEncodingStyle(null);
        _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
        _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
        _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
        _call.setOperationName(new javax.xml.namespace.QName("urn:fkpuserwsdl", "getAllRoles"));

        setRequestHeaders(_call);
        setAttachments(_call);
 try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {});

        if (_resp instanceof java.rmi.RemoteException) {
            throw (java.rmi.RemoteException)_resp;
        }
        else {
            extractAttachments(_call);
            try {
                return (no.unified.soak.dao.fkpuser.FkpRole[]) _resp;
            } catch (java.lang.Exception _exception) {
                return (no.unified.soak.dao.fkpuser.FkpRole[]) org.apache.axis.utils.JavaUtils.convert(_resp, no.unified.soak.dao.fkpuser.FkpRole[].class);
            }
        }
  } catch (org.apache.axis.AxisFault axisFaultException) {
  throw axisFaultException;
}
    }

}
