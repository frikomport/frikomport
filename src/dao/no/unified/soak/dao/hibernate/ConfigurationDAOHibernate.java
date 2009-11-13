package no.unified.soak.dao.hibernate;

import java.util.List;

import no.unified.soak.dao.ConfigurationDAO;
import no.unified.soak.model.Configuration;

public class ConfigurationDAOHibernate extends BaseDAOHibernate implements ConfigurationDAO {

    public Configuration getConfiguration(String key) {
        Configuration configuration = (Configuration) getHibernateTemplate().get(Configuration.class, key);
        return configuration;
    }
    
    public List<Configuration> getConfigurations() {
		return getHibernateTemplate().find("from Configuration");
    }

    public void saveConfiguration(final Configuration configuration) {
        getHibernateTemplate().saveOrUpdate(configuration);
        getHibernateTemplate().flush();
    }
    
}
