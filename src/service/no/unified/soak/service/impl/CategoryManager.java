package no.unified.soak.service.impl;

import no.unified.soak.service.Manager;
import no.unified.soak.model.Category;
import no.unified.soak.dao.CategoryDAO;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 16.des.2008
 * @author gv
 */
public interface CategoryManager extends Manager {

    public void setCategoryDAO(CategoryDAO categoryDAO);

    public Category getCategory(Long id);

    public List<Category> getAll();

    public List<Category> getCategories(Category category, Boolean includeDisabled);

    public List<Category> getAllIncludingDummy(String dummy);

    public void saveCategory(Category category);
    
    public void removeCategory(Category category);
}
