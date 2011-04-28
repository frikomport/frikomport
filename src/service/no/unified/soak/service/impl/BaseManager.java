/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service.impl;

import java.io.PrintWriter;
import java.io.Serializable;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;

import no.unified.soak.dao.DAO;
import no.unified.soak.model.User;
import no.unified.soak.service.Manager;
import no.unified.soak.service.UserManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Base class for Business Services - use this class for utility methods and
 * generic CRUD methods.
 *
 * <p><a href="BaseManager.java.html"><i>View Source</i></a></p>
 *
 * @author <a href="mailto:matt@raibledesigns.com">Matt Raible</a>
 */
public class BaseManager implements Manager {
    protected final Log log = LogFactory.getLog(getClass());
    protected DAO dao = null;

    /**
     * @see no.unified.soak.service.Manager#setDAO(no.unified.soak.dao.DAO)
     */
    public void setDAO(DAO dao) {
        this.dao = dao;
    }

    /**
     * @see no.unified.soak.service.Manager#getObject(java.lang.Class, java.io.Serializable)
     */
    public Object getObject(Class clazz, Serializable id) {
        return dao.getObject(clazz, id);
    }

    /**
     * @see no.unified.soak.service.Manager#getObjects(java.lang.Class)
     */
    public List getObjects(Class clazz) {
        return dao.getObjects(clazz);
    }

    /**
     * @see no.unified.soak.service.Manager#removeObject(java.lang.Class, java.io.Serializable)
     */
    public void removeObject(Class clazz, Serializable id) {
        dao.removeObject(clazz, id);
    }

    /**
     * @see no.unified.soak.service.Manager#saveObject(java.lang.Object)
     */
    public void saveObject(Object o) {
        dao.saveObject(o);
    }

	public void evict(Object entity) {
		dao.evict(entity);
	}

	public void flush() {
		dao.flush();
	}

	public boolean contains(Object entity) {
		return dao.contains(entity);
	}
	
	public boolean handleStaleObjectStateExceptionForUserObject(Exception e, UserManager userManager){
		// finne username for User-objektet problemet oppstod for, og deretter hente og kaste det ut av hibernate-sesjonen
		String username = null;
		try {
			Writer writer = new StringWriter();
		    PrintWriter printWriter = new PrintWriter(writer);
		    e.printStackTrace(printWriter);
		    String msg = writer.toString();
//		    String msg = e.getMessage();
		    if(msg.indexOf(".User#") != -1){
		    	int start = msg.indexOf(".User#");
		    	msg = msg.substring(start);
		    	username = msg.substring(start + ".User#".length(), msg.indexOf("]"));
		    	if(username != null){
			    	User problem = userManager.getUser(username);
			    	if(problem != null){
				    	userManager.evict(problem);
			    		log.warn("User#" + username + " evicted from session!");
				    	return true; // try previous getter again
			    	}
		    	}
		    }
		}catch(Exception e2){ 
			/* problem not solved */ 
		}
		log.warn("User#" + username + " not evicted from session..!");
		return false;
	}
	
}
