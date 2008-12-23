package no.unified.soak.dao.hibernate;

import no.unified.soak.dao.CategoryDAO;
import no.unified.soak.model.Category;
import no.unified.soak.model.Location;

import java.util.List;

import org.springframework.orm.ObjectRetrievalFailureException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Order;

/**
 * Created by IntelliJ IDEA.
 * User: gv
 * Date: 16.des.2008
 * @author gv
 */
public class CategoryDAOHibernate extends BaseDAOHibernate implements CategoryDAO {

    /**
     * @see no.unified.soak.dao.CategoryDAO#getCategory(String)
     */
    public Category getCategory(Long id) {
        Category category = (Category) getHibernateTemplate().get(Category.class, id);

        if (category == null) {
            log.warn("uh oh, category with id '" + id + "' not found...");
            throw new ObjectRetrievalFailureException(Category.class, id);
        }

        return category;
    }

    /**
     * @see no.unified.soak.dao.CategoryDAO#getCategories(no.unified.soak.model.Category,Boolean)
     */
    public List<Category> getCategories(Category category, final Boolean includeDisabled) {
        DetachedCriteria criteria = DetachedCriteria.forClass(Category.class);

        // If the includeDisabled is not true, we only return enabled locations
        if (!includeDisabled.booleanValue()) {
            criteria.add(Restrictions.eq("selectable", new Boolean("true")));
        }

        criteria.addOrder(Order.asc("name"));

        return getHibernateTemplate().findByCriteria(criteria);
    }

    /**
     * @see no.unified.soak.dao.CategoryDAO#saveCategory(no.unified.soak.model.Category)
     */
    public void saveCategory(Category category) {
        getHibernateTemplate().saveOrUpdate(category);
    }
}
