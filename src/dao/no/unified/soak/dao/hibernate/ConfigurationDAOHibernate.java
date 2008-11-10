package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.ConfigurationDAO;
import no.unified.soak.model.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 10.nov.2008
 * Time: 11:29:18
 * To change this template use File | Settings | File Templates.
 */
public class ConfigurationDAOHibernate extends BaseDAOHibernate implements ConfigurationDAO{

    public Configuration getConfiguration(String key) {
        Configuration configuration = (Configuration) getHibernateTemplate().get(Configuration.class, key);
        return configuration;
    }
}
