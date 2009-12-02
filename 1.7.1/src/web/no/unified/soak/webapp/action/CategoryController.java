package no.unified.soak.webapp.action;

import org.springframework.validation.Errors;
import org.springframework.web.servlet.mvc.Controller;
import org.springframework.web.servlet.ModelAndView;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import no.unified.soak.service.impl.CategoryManager;
import no.unified.soak.model.Category;
import no.unified.soak.Constants;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 17.des.2008
 */
public class CategoryController implements Controller {
    private final Log log = LogFactory.getLog(OrganizationController.class);
    private CategoryManager categoryManager;

    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

     public ModelAndView handleRequest(HttpServletRequest request,
        HttpServletResponse reponse) throws Exception {
        if (log.isDebugEnabled()) {
            log.debug("entering 'handleRequest' method...");
        }

        return new ModelAndView("categoryList",
            Constants.CATEGORY_LIST, categoryManager.getCategories(new Category(), new Boolean(true)));
    }
}
