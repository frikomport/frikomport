package no.unified.soak;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import javax.xml.namespace.QName;

public class TestWS {

    public static void main(String[] args) {

        try {
//            String endpoint = "http://localhost:8089/mockportOppslagSVVAnsattBnd";
            String endpoint = "http://corona:8080/jboss-net/services/blacklistCreditCard";

            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new java.net.URL(endpoint));
//            call.setOperationName(new QName("http://localhost:8089/", "UID"));
            call.setOperationName(new QName("no.nsb.mtserver.webservice.blacklist.ceditcard", "uploadCreditCardList"));    

            String ret = (String) call.invoke(new Object[] { "Hello" });

            System.out.println("Sent 'Hello!', got '" + ret + "'");
        } catch (Exception e) {
            System.err.println(e.toString());
            System.out.println(e.getStackTrace());
            e.printStackTrace();
        }
    }
}
