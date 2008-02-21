package no.unified.soak.dao.jdbc;

import java.util.List;

import no.unified.soak.ext.IUser;

public interface IUserDaoJdbc
{

    public abstract IUser findUserBySessionID(String sessionId);

    public abstract IUser findKursansvarligUser(Integer userid);

    public abstract List findKursansvarligeUser();

    public abstract List<String> findRoles();

}