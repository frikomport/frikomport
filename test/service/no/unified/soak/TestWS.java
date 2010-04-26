package no.unified.soak;

import no.unified.soak.dao.ws.SVVUserDAOWS;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.util.ApplicationResourcesUtil;

import org.springframework.context.MessageSource;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;

public class TestWS {

    static MessageSource messageSource = new ReloadableResourceBundleMessageSource();

    public static void main(String[] args) {
        StringBuffer sb0 = new StringBuffer("01234567890");
        StringBuffer sb1 = sb0;
        StringBuffer sb2 = sb1.delete(1, 2);
        sb2 = sb2.delete(1, 3);
        
		try {
			// String endpoint =
			// "http://localhost:8089/mockportOppslagSVVAnsattBnd";
			String endpoint = "http://corona:8080/jboss-net/services/blacklistCreditCard";

			String ret = "<env:Envelope xmlns:env=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:enc=\"http://schemas.xmlsoap.org/soap/encoding/\" xmlns:ns0=\"urn:no:vegvesen:landskap:SVVFeilType:cct:1:0\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\"><env:Header><tns:WrapperImp xmlns:tns=\"urn:no:vegvesen:ldap.wsdl:OppslagSVVAnsatt:1:0\"/></env:Header><env:Body><tns:Response xmlns:tns=\"urn:no:vegvesen:ldap.wsdl.OppslagSVVAnsattContract:cc:1:0\"><urn:ObjSVVAnsatt xmlns:urn=\"urn:no:vegvesen:ldap:soa2:SVVAnsattListeType:cct:1:0\"><urn:ObjInfo><urn1:ObjectClass xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">top</urn1:ObjectClass><urn1:CreateTimestamp xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">20100415092420Z</urn1:CreateTimestamp><urn1:CreatorsName xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">cn=directory manager</urn1:CreatorsName><urn1:ModifiersName xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVTopType:cct:1:0\">cn=directory manager</urn1:ModifiersName></urn:ObjInfo><urn:SVVAnsatt><urn1:sn xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Stafto</urn1:sn><urn1:cn xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Klaus Stafto</urn1:cn><urn1:uid xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">EXTKLA</urn1:uid><urn1:givenName xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Klaus</urn1:givenName><urn1:mail xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">klaus.stafto@vegvesen.no</urn1:mail><urn1:mobile xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">98257893</urn1:mobile><urn1:svvcompany xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\"><urn2:Pair xmlns:urn2=\"urn:no:vegvesen:ldap:felles:KeyPairListType:cct:1:0\"><urn2:Key>984761805000000000</urn2:Key><urn2:Value/></urn2:Pair></urn1:svvcompany><urn1:svvdateofhire xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">20100311</urn1:svvdateofhire><urn1:svvflag xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">00</urn1:svvflag><urn1:svvlocation xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">264</urn1:svvlocation><urn1:svvlocationtext xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Oslo, Vdt</urn1:svvlocationtext><urn1:svvorgunit xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">67142</urn1:svvorgunit><urn1:svvorgunittext xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Systemene for trafikant og kjøretøy</urn1:svvorgunittext><urn1:svvregion xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">6</urn1:svvregion><urn1:svvregiontext xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\">Vegdirektoratet</urn1:svvregiontext><urn1:svvrole xmlns:urn1=\"urn:no:vegvesen:ldap:soa2:SVVAnsattType:cct:1:0\"><urn2:Pair xmlns:urn2=\"urn:no:vegvesen:ldap:felles:KeyPairListType:cct:1:0\"><urn2:Key>FKPAdministrator</urn2:Key><urn2:Value>user</urn2:Value></urn2:Pair></urn1:svvrole></urn:SVVAnsatt></urn:ObjSVVAnsatt></tns:Response></env:Body></env:Envelope>";

			ExtUser extUser = new ExtUser();
			extUser.setEmail(SVVUserDAOWS.getTagValue("urn1:mail", ret));
			extUser.setFirst_name(SVVUserDAOWS.getTagValue("urn1:givenName", ret));
			extUser.setLast_name(SVVUserDAOWS.getTagValue("urn1:sn", ret));
			extUser.setName(SVVUserDAOWS.getTagValue("urn1:cn", ret));

			String adminRoles = "Administrator,FKPAdministrator";
			String eventAdminRoles = "Opplaringsansvarlig,FKPMoteadministrator";
			String eventResponsible = "Kursansvarlig,FKPMoteansvarlig";
			String ansattRoles = "Ansatt";
			String readeRoles = "FKPLesebruker";

			extUser.setRolenames(SVVUserDAOWS.getInnerTagValuesInTag(ret, "urn1:svvrole", "Key", new String[] { adminRoles,
					eventAdminRoles, eventResponsible, ansattRoles, readeRoles }));

		} catch (Exception e) {
            System.err.println(e.toString());
            System.out.println(e.getStackTrace());
            e.printStackTrace();
        }
    }
}
