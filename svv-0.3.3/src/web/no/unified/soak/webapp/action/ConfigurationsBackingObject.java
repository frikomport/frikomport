package no.unified.soak.webapp.action;

import java.util.ArrayList;
import java.util.List;

import no.unified.soak.model.Configuration;


/**
 * formBackingObject used to retrieve lists of configurations from Forms
 *
 * @author sa
 */
public class ConfigurationsBackingObject {
    List<Configuration> configurations = new ArrayList<Configuration>();

    public List<Configuration> getConfigurations() {
        return configurations;
    }

    public void setConfigurations(List<Configuration> configurations) {
        this.configurations = configurations;
    }
}
