package no.unified.soak.service.impl;

import java.util.ArrayList;
import java.util.List;

import no.unified.soak.service.CategoryManager;
import no.unified.soak.dao.CategoryDAO;
import no.unified.soak.model.Category;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 16.des.2008
 * @author gv
 */
public class CategoryManagerImpl extends BaseManager implements CategoryManager {
    private CategoryDAO categoryDAO;
    
    public void setCategoryDAO(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    public Category getCategory(Long id) {
        return categoryDAO.getCategory(id);
    }

    public Category getCategory(String name) {
        return categoryDAO.getCategory(name);
    }
    
    public List<Category> getAll() {
        return categoryDAO.getCategories(new Category(),new Boolean(false));
    }

    public List<Category> getCategories(Category category, Boolean includeDisabled) {
        return categoryDAO.getCategories(category, includeDisabled);
    }

    public List<Category> getAllIncludingDummy(String dummy) {
        List categories = new ArrayList();
        Category categoryDummy = new Category();
        categoryDummy.setId(null);
        categoryDummy.setName(dummy);
        categories.add(categoryDummy);
        categories.addAll(getAll());
        return categories;
    }

    public void saveCategory(Category category) {
        categoryDAO.saveCategory(category);
    }
    
    public void removeCategory(Category category) {
        categoryDAO.removeCategory(category.getId());
    }

    
}
