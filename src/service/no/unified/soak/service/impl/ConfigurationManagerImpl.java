package no.unified.soak.service.impl;

import java.util.List;

import no.unified.soak.dao.ConfigurationDAO;
import no.unified.soak.model.Configuration;
import no.unified.soak.service.ConfigurationManager;


public class ConfigurationManagerImpl extends BaseManager implements
		ConfigurationManager {

	private ConfigurationDAO dao;

	public void setConfigurationDao(ConfigurationDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see ConfigurationManager#getValue(String, String)
	 */
	public String getValue(String key, String defaultvalue) {
		Configuration configuration = dao.getConfiguration(key);
		if (configuration == null || configuration != null && !configuration.getActive()) {
			return defaultvalue;
		}
		return configuration.getValue();
	}

	/**
	 * @see ConfigurationManager#isActive(String, boolean)
	 */
	public boolean isActive(String key, boolean defaultvalue) {
		Configuration configuration = dao.getConfiguration(key);
		if (configuration == null) {
			return defaultvalue;
		}
		return configuration.getActive();
	}
	
	/**
	 * @see ConfigurationManager#getConfigurations()
	 */
	public List<Configuration> getConfigurations(){
		return dao.getConfigurations();
	}

	/**
	 * @see ConfigurationManager#saveConfiguration(Configuration)
	 */
	public void saveConfiguration(Configuration configuration) {
		dao.saveConfiguration(configuration);
	}
	
    /**
     * @see ConfigurationManager#exists(String)
     */
	public boolean exists(String key) {
	    Configuration configuration = dao.getConfiguration(key);
	    if (configuration == null) {
            return false;
        }
	    return true;
	}
	
	public Long nextId() {
	    return dao.nextId();
	}
	
}
