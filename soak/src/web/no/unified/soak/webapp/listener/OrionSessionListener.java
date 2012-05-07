package no.unified.soak.webapp.listener;

import java.util.Enumeration;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

/**
 * This listener is invoked by Orion (Oracle Application Server)when a session
 * is invalidated. It removes every variable bound to that session forcing Orion
 * to call every HttpSessionAttributeListener.
 * 
 * @author <a href="mailto:stefano.bertini at plangroup.it">Stefano Bertini</a>
 * 
 * @web.listener
 */
public class OrionSessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent arg0) {
		// Intentionally left blank
	}

	public void sessionDestroyed(HttpSessionEvent event) {
		HttpSession session = event.getSession();
		Enumeration names = session.getAttributeNames();
		while (names.hasMoreElements()) {
			session.removeAttribute(names.nextElement().toString());
		}
	}
}
