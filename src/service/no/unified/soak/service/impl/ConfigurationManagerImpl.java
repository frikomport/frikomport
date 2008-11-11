package no.unified.soak.service.impl;

import no.unified.soak.model.Configuration;
import no.unified.soak.service.ConfigurationManager;
import no.unified.soak.dao.ConfigurationDAO;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 10.nov.2008
 * Time: 12:01:05
 */
public class ConfigurationManagerImpl extends BaseManager implements ConfigurationManager {

    private ConfigurationDAO dao;

    public void setConfigurationDao(ConfigurationDAO dao) {
        this.dao = dao;
    }

    public String getValue(String key, String defaultvalue) {
        Configuration configuration = dao.getConfiguration(key);
        if(configuration == null){
            return defaultvalue;
        }
        return configuration.getValue();
    }
}
