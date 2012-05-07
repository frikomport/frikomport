package no.unified.soak.webapp.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Configuration;

import org.apache.commons.lang.BooleanUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

public class ConfigurationFormController extends BaseFormController {

	/**
	 * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
	 */
	protected Object formBackingObject(HttpServletRequest request) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'formBackingObject' method in ConfigurationFormController...");
		}
		ConfigurationsBackingObject configurationsBackingObject = new ConfigurationsBackingObject();

        if ((Boolean) request.getAttribute("isAdmin")) {
            List<Configuration> configurations = configurationManager.getConfigurations();
            configurationsBackingObject.setConfigurations(configurations);
        }

		return configurationsBackingObject;
	}

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse, java.lang.Object, org.springframework.validation.BindException)
	 */
	public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command,
 BindException errors)
            throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'onSubmit' method in ConfigurationFormController...");
        }

        ConfigurationsBackingObject configurationsBackingObject = (ConfigurationsBackingObject) command;

        Map model = new HashMap();

        // Are we to cancel?
        if (request.getParameter("docancel") != null) {
            if (log.isDebugEnabled()) {
                log.debug("recieved 'cancel' from jsp");
            }
            model.put("cancelled", Boolean.TRUE);
            return new ModelAndView(getCancelView(), model);
        } else {
            // save configuration
            if (configurationsBackingObject != null && (Boolean) request.getAttribute("isAdmin")) {
                if (persistChanges(request, configurationsBackingObject)) {
                    model.put("updated", Boolean.TRUE);
                }
            }
        }
        
        return new ModelAndView(getSuccessView(), model);
    }

	/**
	 * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
	 */
    protected Map referenceData(HttpServletRequest request) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'referenceData' method in Administration controller...");
        }

        Map model = new HashMap();
        // Needs to detect also after redirect:
        if (BooleanUtils.toBoolean((String) request.getParameter("updated"))) {
            model.put("updated", Boolean.TRUE);
        }
        if (BooleanUtils.toBoolean((String) request.getParameter("cancelled"))) {
            model.put("cancelled", Boolean.TRUE);
        }

        return model;
    }
	
	/**
	 * Scans through the list of objects for changes, and persists the changes that have been made
	 * 
	 * @param request
	 *            The HTTP Request object
	 * @param configurationsBackingObject
	 *            The objects from the form
	 * @return true if there were changes that have been persisted
	 */
	private boolean persistChanges(HttpServletRequest request, ConfigurationsBackingObject configurationsBackingObject) {
		boolean saved = false;

		// Loop over the rows and check for changes and the presist what changes there are
		for (int i = 0; i < configurationsBackingObject.getConfigurations().size(); i++) {
			boolean changed = false;

			// Get current row
			Configuration thisConfiguration = configurationsBackingObject.getConfigurations().get(i);

			String activeCheckbox = request.getParameter("id_" + thisConfiguration.getId());
			String hiddenActiveCheckbox = request.getParameter("_id" + thisConfiguration.getId()); 

			boolean active = checkboxToBoolean(activeCheckbox);
			boolean hiddenActivePresent = checkboxToBoolean(hiddenActiveCheckbox);

			// Check changes
			if (thisConfiguration.getActive().booleanValue() != active && hiddenActivePresent) {
				changed = true;
				thisConfiguration.setActive(new Boolean(active));
			}
			
			// add more tests if needed
			
			if (changed) {
				configurationManager.saveConfiguration(thisConfiguration);
				saved = true;
			}
		}

		return saved;
	}


	/**
	 * Converts a forms textual boolean to a boolean Expects input "null" or (null) or "false" for false - all others
	 * are read as true.
	 * 
	 * @param request
	 *            String returned from form.
	 * @return request converted to boolean value
	 */
	private boolean checkboxToBoolean(String request) {
		boolean result = true;

		if ((request == null) || (request.compareTo("null") == 0) || (request.compareTo("false") == 0)) {
			result = false;
		}

		return result;
	}

}
