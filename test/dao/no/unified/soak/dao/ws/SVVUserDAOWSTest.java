/**
 * 
 */
package no.unified.soak.dao.ws;

import no.unified.soak.ez.ExtUser;
import junit.framework.TestCase;

/**
 * @author touextkla
 *
 */
public class SVVUserDAOWSTest extends TestCase {

	/**
	 * @param name
	 */
	public SVVUserDAOWSTest(String name) {
		super(name);
	}

	/*
	 * Tester parsing av xml. Fjern "Test" i klassenavn for å kjøre ved behov.
	 */
	public void testParseUserXML () {
		String xmlString =  
			"<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns0=\"urn:no:vegvesen:landskap:SVVFeilType:cct:1:0\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">\r\n" + 
			"   <env:Header>\r\n" + 
			"      <tns:WrapperImp xmlns:tns=\"urn:no:vegvesen:ldap.wsdl:OppslagSVVAnsatt:1:0\"/>\r\n" + 
			"   </env:Header>\r\n" + 
			"   <env:Body>\r\n" + 
			"      <tns:Response xmlns:tns=\"urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0\">\r\n" + 
			"         <urn:ObjSVVAnsatt xmlns:urn=\"urn:no:vegvesen:ldap:soa2:SVVAnsattListeType:cct:1:0\">\r\n" + 
			"            <urn:ObjInfo>\r\n" + 
			"               <urn1:ObjectClass xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">dspswuser</urn1:ObjectClass>\r\n" + 
			"               <urn1:CreateTimestamp xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">20091215131102Z</urn1:CreateTimestamp>\r\n" + 
			"               <urn1:CreatorsName xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">cn=directory manager</urn1:CreatorsName>\r\n" + 
			"               <urn1:ModifiersName xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">cn=directory manager</urn1:ModifiersName>\r\n" + 
			"				<urn2:Pair xmlns:urn2=\"urn:no:vegvesen:ldap:felles:KeyPairListType:cct:1:0\"><urn2:Key>984761805000000000</urn2:Key><urn2:Value/></urn2:Pair>" +
			"            </urn:ObjInfo>\r\n" + 
			"            <urn:SVVAnsatt>\r\n" +
			"               <urn1:sn xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Aalberg[&#248;&#230;&#229;]</urn1:sn>\r\n" + 
			"               <urn1:cn xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Anders Aalberg[&#248;&#230;&#229;]</urn1:cn>\r\n" + 
			"               <urn1:uid xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">ANAALB</urn1:uid>\r\n" + 
			"               <urn1:telephoneNumber xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">74122126</urn1:telephoneNumber>\r\n" + 
			"               <urn1:department xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Prosjekt</urn1:department>\r\n" + 
			"               <urn1:givenName xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Anders[&#248;&#230;&#229;]</urn1:givenName>\r\n" + 
			"               <urn1:mail xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">anders.aalberg@vegvesen.no</urn1:mail>\r\n" + 
			"               <urn1:mobile xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">91184399</urn1:mobile>\r\n" + 
			"               <urn1:svvansattstatus xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Aktiv organisasjonstilknytning</urn1:svvansattstatus>\r\n" + 
			"               <urn1:svvansattype xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">F</urn1:svvansattype>\r\n" + 
			"               <urn1:svvdateofhire xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">20070901</urn1:svvdateofhire>\r\n" + 
			"               <urn1:svvkjonn xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">M</urn1:svvkjonn>\r\n" + 
			"               <urn1:svvlocation xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">2240</urn1:svvlocation>\r\n" + 
			"               <urn1:svvlocationtext xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Trondheim, anl 3</urn1:svvlocationtext>\r\n" + 
			"               <urn1:svvorgunit xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">43370</urn1:svvorgunit>\r\n" + 
			"               <urn1:svvorgunittext xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">E6 Trondheim-Stjørdal, parsell Trondheim</urn1:svvorgunittext>\r\n" + 
			"               <urn1:svvpercentage xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">100</urn1:svvpercentage>\r\n" + 
			"               <urn1:svvpersontype xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Ansatt</urn1:svvpersontype>\r\n" + 
			"               <urn1:svvprivpostnummer xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">7500</urn1:svvprivpostnummer>\r\n" + 
			"               <urn1:svvprivpoststed xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">STJØRDAL</urn1:svvprivpoststed>\r\n" + 
			"               <urn1:svvregion xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">4</urn1:svvregion>\r\n" + 
			"               <urn1:svvregiontext xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Region midt</urn1:svvregiontext>\r\n" + 
			"               <urn1:svvusertype xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">11111</urn1:svvusertype>\r\n" + 
			"            </urn:SVVAnsatt>\r\n" + 
			"         </urn:ObjSVVAnsatt>\r\n" + 
			"      </tns:Response>\r\n" + 
			"   </env:Body>\r\n" + 
			"</env:Envelope>\r\n" + 
			"\r\n" + 
			"\r\n";
		ExtUser extUser = null;
		String username = "ANAALB";
		SVVUserDAOWS svvUserDAOWS = new SVVUserDAOWS();
		extUser = svvUserDAOWS.parseUserXML(xmlString, extUser, username);
		System.out.println(extUser);
	}
	
	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

}
