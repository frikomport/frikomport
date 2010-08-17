package no.unified.soak.dao.hibernate;

import java.util.List;

import no.unified.soak.dao.ConfigurationDAO;
import no.unified.soak.model.Configuration;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

public class ConfigurationDAOHibernate extends BaseDAOHibernate implements ConfigurationDAO {

    public Configuration getConfiguration(Long id) {
        Configuration configuration = (Configuration) getHibernateTemplate().get(Configuration.class, id);
        return configuration;
    }

    public Configuration getConfiguration(String name) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Configuration.class);
		criteria.add(Restrictions.eq("name", name));
		List<Configuration> result = getHibernateTemplate().findByCriteria(criteria);
        if(result == null || result.size() != 1){
            return null;
        }
        return result.get(0);
    
    }

    public List<Configuration> getConfigurations() {
		DetachedCriteria criteria = DetachedCriteria.forClass(Configuration.class);
		criteria.addOrder(Order.asc("name"));
		return getHibernateTemplate().findByCriteria(criteria);
    }

    public void saveConfiguration(final Configuration configuration) {
        getHibernateTemplate().saveOrUpdate(configuration);
        getHibernateTemplate().flush();
    }
    
}
