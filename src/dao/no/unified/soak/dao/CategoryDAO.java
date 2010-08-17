package no.unified.soak.dao;

import no.unified.soak.model.Category;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 16.des.2008
 * @author gv
 */
public interface CategoryDAO extends DAO {

    public Category getCategory(Long id);

    public Category getCategory(String name);

    public List<Category> getCategories(Category category, Boolean includeDisabled);

    public void saveCategory(Category category);

    public void removeCategory(Long id);

}
