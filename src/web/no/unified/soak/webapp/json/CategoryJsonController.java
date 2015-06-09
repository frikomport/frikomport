package no.unified.soak.webapp.json;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import no.unified.soak.model.Category;
import no.unified.soak.service.CategoryManager;


@Path("/kategorier")
public class CategoryJsonController {

	private CategoryManager categoryManager;

	public CategoryJsonController() {
	}

	public void setCategoryManager(CategoryManager categoryManager) {
        this.categoryManager = categoryManager;
    }

    @GET
	@Produces( MediaType.APPLICATION_JSON + ";charset=utf-8")
	public List<Category> getCategories() {
		return categoryManager.getCategories(new Category(), false);
	}
}