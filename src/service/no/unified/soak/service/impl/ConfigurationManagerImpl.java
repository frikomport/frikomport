package no.unified.soak.service.impl;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import no.unified.soak.dao.ConfigurationDAO;
import no.unified.soak.model.Configuration;
import no.unified.soak.service.ConfigurationManager;


public class ConfigurationManagerImpl extends BaseManager implements
		ConfigurationManager {

	private ConfigurationDAO dao;
	private static Map<String, Configuration> configurationsCache; 

	public void setConfigurationDao(ConfigurationDAO dao) {
		this.dao = dao;
	}

	/**
	 * @see ConfigurationManager#getValue(String, String)
	 */
	public String getValue(String key, String defaultvalue) {
		Configuration configuration;
		if (configurationsCache != null) {
			configuration = configurationsCache.get(key);
		} else {
			configuration = dao.getConfiguration(key);
		}

		if (configuration == null || !configuration.getActive()) {
			return defaultvalue;
		}
		return configuration.getValue();
	}

	/**
	 * @see ConfigurationManager#isActive(String, boolean)
	 */
	public boolean isActive(String key, boolean defaultvalue) {
		Configuration configuration;
		if (configurationsCache != null) {
			configuration = configurationsCache.get(key);
		} else {
			configuration = dao.getConfiguration(key);
		}
		if (configuration == null) {
			return defaultvalue;
		}
		return configuration.getActive();
	}
	
	/**
	 * Gets configurations and flushes internal cache in this class, ConfigurationManagerImpl.
	 * @see ConfigurationManager#getConfigurations()
	 */
	public List<Configuration> getConfigurations(){
		List<Configuration> configurations = dao.getConfigurations();

		//Building a separate new map to reduce the time in inconsistency. Thereby reducing risk of concurrency problems.
		if (configurationsCache == null) {
			configurationsCache = new ConcurrentHashMap<String, Configuration>(40, 0.75f, 35);
		}
		for (Configuration configuration : configurations) {
			configurationsCache.put(configuration.getName(), configuration);
		}
		
		return configurations;
	}

	/**
	 * @see ConfigurationManager#saveConfiguration(Configuration)
	 */
	public void saveConfiguration(Configuration configuration) {
		dao.saveConfiguration(configuration);
		
		//Also update the cache.
		configurationsCache.put(configuration.getName(), configuration);
	}
	
}
