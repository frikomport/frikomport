package no.unified.soak.dao;

import java.util.List;

import no.unified.soak.model.Configuration;

public interface ConfigurationDAO extends DAO {

    /**
     * @param key
     * @return no.unified.soak.model.Configuration
     */
    public Configuration getConfiguration(String key);
    
    public List<Configuration> getConfigurations();
    
    public void saveConfiguration(Configuration configuration);

    public void deleteConfiguration(Configuration configuration);

}
