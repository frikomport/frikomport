/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/**
 *
 */
package no.unified.soak.util;

import java.io.FilterInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ResourceBundle;

import no.unified.soak.Constants;


/**
 * @author kst
 *
 */
public class WaitingListMain {
    // private static RegistrationManager registrationManager = new
    // RegistrationManagerImpl();
    //
    // private static CourseManager courseManager = new CourseManagerImpl();

    /**
     * @param args
     */
    public static void main(String[] args) {
        // WaitingListManager waitingListManager = setup();
        // waitingListManager.processEntireWaitingList();
        setup();

//        System.out.println("WaitingListMain.main(): End.");
    }

    private static void setup() {
        try {
            String urlStr = ResourceBundle.getBundle(Constants.BUNDLE_KEY)
                                          .getString("waitingList.service.url");
            URL url = new URL(urlStr);
            URLConnection urlConn = url.openConnection();
            urlConn.setConnectTimeout(5000); /* 5 sec timeout */
            urlConn.connect();

//            Map fields = urlConn.getHeaderFields();
//            System.out.println("WaitingListMain.setup(): Status=" +
//                fields.get(null));

            if (urlConn.getContent() instanceof FilterInputStream) {
                FilterInputStream inputStream = (FilterInputStream) urlConn.getContent();
//                System.out.print("Content=[");

                int i;

                do {
                    i = inputStream.read();
//                    System.out.print((char) i);
                } while (i > -1);

//                System.out.println("]");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return;
    } // @SuppressWarnings("unused")

    // private static WaitingListManager setupBlindvei() {
    // SessionFactory sessionFactory = null;
    // try {
    // File file = new File(
    // "src/dao/no/unified/soak/dao/hibernate/applicationContext-hibernate.xml");
    // System.out.println("WaitingListMain.setup(): " + file.canRead());
    // System.out.println(file.getAbsolutePath());
    // try {
    // System.out.println(file.getCanonicalPath());
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
    // sessionFactory = new Configuration().configure(file)
    // .buildSessionFactory();
    // } catch (HibernateException e) {
    // e.printStackTrace();
    // }
    //
    // CourseDAOHibernate courseDAOHibernate = new CourseDAOHibernate();
    // courseDAOHibernate.setSessionFactory(sessionFactory);
    // courseManager.setCourseDAO(courseDAOHibernate);
    //
    // RegistrationDAOHibernate registrationDAOHibernate = new
    // RegistrationDAOHibernate();
    // registrationDAOHibernate.setSessionFactory(sessionFactory);
    // registrationManager.setRegistrationDAO(registrationDAOHibernate);
    //
    // WaitingListManager waitingListManager = new WaitingListManagerImpl();
    // waitingListManager.setCourseManager(courseManager);
    // waitingListManager.setRegistrationManager(registrationManager);
    //
    // // new SessionFactoryImpl();
    //
    // return waitingListManager;
    // }
}
