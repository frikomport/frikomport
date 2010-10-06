package no.unified.soak.service;

import java.util.List;

import no.unified.soak.dao.ConfigurationDAO;
import no.unified.soak.model.Configuration;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 10.nov.2008
 * Time: 11:24:46
 */
public interface ConfigurationManager extends Manager{

    public void setConfigurationDao(ConfigurationDAO dao);

    /**
     * Returns value if configuration is present and active, else defaultvalue
     * @param key
     * @param defaultvalue
     * @return value
     */
    public String getValue(String key, String defaultvalue);
    
    /**
     * Configuration status if present, else defaultvalue
     * @param key
     * @param defaultvalue
     * @return active
     */
    public boolean isActive(String key, boolean defaultvalue);
    
    /**
     * Get all configurations
     * @return configurations
     */
	public List<Configuration> getConfigurations();

	/**
	 * Save configuration
	 * @param configuration
	 */
	public void saveConfiguration(Configuration configuration);
	
	/**
	 * Checks if configuration exists
	 * @param key Key to check
	 * @return true if configuration exists
	 */
	public boolean exists(String key);
	
	/**
	 * Gets the next free id
	 * @return
	 */
	public Long nextId();
}
