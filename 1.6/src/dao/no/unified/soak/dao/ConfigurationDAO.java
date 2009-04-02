package no.unified.soak.dao;

import no.unified.soak.model.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 10.nov.2008
 * Time: 11:26:28
 */
public interface ConfigurationDAO extends DAO {

    /**
     * @param key
     * @return no.unified.soak.model.Configuration
     */
    public Configuration getConfiguration(String key);
}
