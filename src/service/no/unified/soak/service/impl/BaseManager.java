/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
package no.unified.soak.service.impl;

import no.unified.soak.dao.DAO;
import no.unified.soak.service.Manager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;

import java.util.List;


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

	/**
	 * Evict entity for hiibernate sessions. this avoids automatic saving
	 * (flush) of the entity.
	 * 
	 * @param entity
	 */
	public void evict(Object entity) {
		dao.evict(entity);
	}
}
