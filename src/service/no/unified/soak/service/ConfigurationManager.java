package no.unified.soak.service;

import no.unified.soak.model.Configuration;
import no.unified.soak.dao.ConfigurationDAO;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 10.nov.2008
 * Time: 11:24:46
 */
public interface ConfigurationManager extends Manager{

    public void setConfigurationDao(ConfigurationDAO dao);

    public String getValue(String key, String defaultvalue);
}
