/**
 * 
 */
package no.unified.soak.webapp.action;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.service.CategoryManager;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.Controller;

/**
 * Class for returning http status 200 to the load balancer when application is running ok.
 * 
 */
public class HealthCheckController implements Controller {
    private final Log log = LogFactory.getLog(HealthCheckController.class);
    private CategoryManager categoryManager;

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String errorMsg = "";
        boolean isHealthy = true;

        if (categoryManager == null) {
            errorMsg = errorMsg
                    .concat("Bean with id \"categoryManager\" is not working. Either initialization failed or the bean has been removed.\n");
            isHealthy = false;
        }

        if (isHealthy) {
            return new ModelAndView("healthCheckOK");
        } else {
            response.setStatus(503);
            response.addHeader("Reason-Phrase", "SVV_LB_HEALTH_CHECK_FAILED");

            errorMsg = StringUtils.remove(errorMsg, "RUNNING");
            request.setAttribute("errorMsg", errorMsg);
            log.error(errorMsg);
            return new ModelAndView("healthCheckERROR");
        }
    }

}
