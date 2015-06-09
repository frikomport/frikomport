/**
 * 
 */
package no.unified.soak.webapp.action;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import no.unified.soak.model.Category;
import no.unified.soak.service.CategoryManager;

import org.apache.commons.lang.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.servlet.ModelAndView;

/**
 * @author gv
 *
 */
public class CategoryFormController extends BaseFormController {
    private CategoryManager categoryManager = null;
    
    public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }
    
    @Override
    public Object formBackingObject(HttpServletRequest request) throws Exception {
        String id = request.getParameter("id");
        Category category = null;

        if (!StringUtils.isEmpty(id)) {
            category = categoryManager.getCategory(Long.decode(id));
        } else {
            category = new Category();
        }

        return category;
    }
    
    @Override
    public ModelAndView onSubmit(HttpServletRequest request, HttpServletResponse response, Object command, BindException errors)
            throws Exception {
        
        Category category = (Category)command;
        boolean isNew = (category.getId() == null);
        Locale locale = request.getLocale();
        
        if (request.getParameter("delete") != null) {
            log.info("Sletter kategori");
            categoryManager.removeCategory(category);
            saveMessage(request, getText("category.deleted", locale));
        } else if (request.getParameter("cancel") != null ) {
            return new ModelAndView(getCancelView());
        } else {
            categoryManager.saveCategory(category);
            String key = (isNew) ? "category.added" : "category.updated";
            saveMessage(request, getText(key, locale));
            
            if (!isNew) {
                return new ModelAndView("redirect:detailsCategory.html", "id", category.getId());
            }
        }
        
        return super.onSubmit(request, response, command, errors);
    }

}
