package no.unified.soak.webapp.action;

import javax.servlet.http.HttpServletRequest;

import no.unified.soak.dao.jdbc.IUserDaoJdbc;
import no.unified.soak.ext.IUser;

public class ExternalUserController extends BaseFormController
{
    private IUserDaoJdbc userEzDaoJdbc;
    
    public void setUserEzDaoJdbc(IUserDaoJdbc userDaoJdbc) {
        this.userEzDaoJdbc = userDaoJdbc;
    }
    
    /* (non-Javadoc)
     * @see org.springframework.web.servlet.mvc.AbstractFormController#formBackingObject(javax.servlet.http.HttpServletRequest)
     */
    protected Object formBackingObject(HttpServletRequest request)
        throws Exception {
        String id = request.getParameter("id");
        return userEzDaoJdbc.findKursansvarligUser(Integer.parseInt(id));
    }
}
