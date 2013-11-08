/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/**
 *
 */
package no.unified.soak.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import no.unified.soak.Constants;
import no.unified.soak.dao.ExtUserDAO;
import no.unified.soak.dao.RoleDAO;
import no.unified.soak.dao.UserDAO;
import no.unified.soak.ez.ExtUser;
import no.unified.soak.model.RoleEnum;
import no.unified.soak.model.User;
import no.unified.soak.util.ConvertDAO;
import no.unified.soak.util.NumConvert;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author kst
 * 
 */
public class EzUserDAOJdbc implements ExtUserDAO {
	DataSource dataSource = null;
	UserDAO userDAO;
	RoleDAO roleDAO;
	
	public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
        jt.setDataSource(dataSource);
    }

	public void setRoleDAO(RoleDAO roleDAO) {
		this.roleDAO = roleDAO;
	}

	public void setUserDAO(UserDAO dao) {
		this.userDAO = dao;
	}

	
	JdbcTemplate jt = new JdbcTemplate();

	public EzUserDAOJdbc() {
        try {
            Context env = (Context) new InitialContext().lookup("java:comp/env/");
            dataSource = (DataSource) env.lookup("jdbc/ezdb");
            jt.setDataSource(dataSource);
        }
        catch (NamingException e) {
            // Do nothing
        }
	}

	/* (non-Javadoc)
     * @see no.unified.soak.dao.ExtUserDAO#findUserBySessionID(java.lang.String)
     */
	public ExtUser findUserBySessionID(String sessionId) {
		boolean fakeLogin = false;
		if(Constants.FAKE_LOGIN.equals(sessionId)){
			// session_key for bruker som �nskes benyttet
			sessionId = "qfb4q61lgduigesf41q7kq4kt5";
			fakeLogin = true; // ikke sjekk utl�p for session
		}
		
		ExtUser eZuser = null;
		try {
			String sql = "select user_id, expiration_time from ezsession where session_key = ?";

            PreparedStatement preparedStatement = jt.getDataSource().getConnection().prepareStatement(sql);
            preparedStatement.setString(1,sessionId);
            ResultSet rowSet = preparedStatement.executeQuery();

			if (rowSet.next()) {
				Integer uid = rowSet.getInt("user_id");
				Long expTime = rowSet.getLong("expiration_time");
				Long curTime = new Date().getTime() / 1000;
				// System.out.println("UserEzDaoJdbc.findUserBySessionID(): expTime=" + expTime + ", curTime=" + curTime);
	
				if ((curTime < expTime) || fakeLogin) {
					eZuser = findUser(uid, null, true);
				} else {
					/*
					 * User is not logged in any more, so don't return the user
					 * object.
					 */
					eZuser = new ExtUser();
					// System.out.println("UserEzDaoJdbc.findUserBySessionID(): eZ user has timedout. Java module is therefore not accepting the user.");
				}
			}
		}
		catch (Exception e) {
			/* feil ved oppslag av bruker ved f�rste kobling mot instans - mest sannsynlig pga. sammenblanding av cookies fra andre instanser */ 
			eZuser = new ExtUser();
		}
		return eZuser;
	}

    /* (non-Javadoc)
     * @see no.unified.soak.dao.ExtUserDAO#findUserByUsername(java.lang.String)
     */
    public ExtUser findUserByUsername(String username) {
		if (username == null) {
			return null;
		}
		ExtUser user = new ExtUser();
		try {
            String sql = "select O.id, O.name, CA.identifier, A.data_int, A.data_text, U.email, U.login from ezcontentobject O \r\n"
					+ "inner join ezcontentclass C on O.contentclass_id = C.id\r\n"
					+ "inner join ezcontentobject_attribute A on A.contentobject_id = O.id\r\n"
					+ "inner join ezcontentclass_attribute CA on CA.contentclass_id = C.id and CA.id = A.contentclassattribute_id\r\n"
					+ "inner join ezuser U on U.contentobject_id = O.id\r\n"
					+ "where C.identifier = \'user\' and O.current_version = A.version and CA.identifier in (\'first_name\',\'last_name\',\'kommune\')\r\n"
					+ "and U.login = '?'\r\n"
					+ " and exists \r\n"
					+ " (select null from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2 \r\n"
					+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id and OT2.contentobject_id = O.id and UR.role_id = R.id)\r\n" 
					+ "order by O.id, CA.id";


            PreparedStatement preparedStatement = jt.getDataSource().getConnection().prepareStatement(sql);
            preparedStatement.setString(1,username);
            ResultSet rowSet = preparedStatement.executeQuery();

			int curId = 0;

			while (rowSet.next()) {
				if (rowSet.getInt("id") != curId) {
					curId = rowSet.getInt("id");
					user.setId(curId);
					user.setName(rowSet.getString("name"));
					user.setEmail(rowSet.getString("email"));
                    user.setUsername(rowSet.getString("login"));
                }
				String identifier = rowSet.getString("identifier");
				if ("first_name".equals(identifier)) {
					user.setFirst_name(rowSet.getString("data_text"));
				} else if ("last_name".equals(identifier)) {
					user.setLast_name(rowSet.getString("data_text"));
				} else if ("kommune".equals(identifier)) {
					user.setKommune(NumConvert.convertToIntegerTolerant(rowSet.getString("data_text")));
                }
			}

			if(user.getId() == null && user.getEmail() == null && user.getUsername() == null){
				//System.out.println("Tomt resultat for ExtUser: " + username);
				// Ideelt sett burde new'ing av ExtUser-objektet ligget ett annet sted enn det gj�r n�, men dette var raskeste vei rundt problemet :-)
				return null;
			}
			
			user.setRolenames(findRoles(user.getId()));
		} catch (InvalidResultSetAccessException e) {
			e.printStackTrace();
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
    }

	/**
	 * @param userid
	 * @param roleCriteria
	 *            string of acceptable roles. Must be commaseparated and all
	 *            roles must be within single quotes.
	 *            <P>
	 *            Example string:<BR>
	 *            <code>String role = "'courseresponsible','educationalresponsible','admin'";</code>
	 * @return user fetched from eZ publish database.
	 */
	private ExtUser findUser(Integer userid, String roleCriteria, boolean findRelated) {
		if (userid == null) {
			return null;
		}
		String roleCriteriaSQL = "";
		if (roleCriteria != null && roleCriteria.trim().length() > 0) {
			roleCriteriaSQL = " and R.name in (?)";
		}
		ExtUser user = new ExtUser();
		try {
			String sql = "select O.id, O.name, CA.identifier, A.data_int, A.data_text, U.email, U.login from ezcontentobject O \r\n"
					+ "inner join ezcontentclass C on O.contentclass_id = C.id\r\n"
					+ "inner join ezcontentobject_attribute A on A.contentobject_id = O.id\r\n"
					+ "inner join ezcontentclass_attribute CA on CA.contentclass_id = C.id and CA.id = A.contentclassattribute_id\r\n"
					+ "inner join ezuser U on U.contentobject_id = O.id\r\n"
					+ "where C.identifier = \'user\' and O.current_version = A.version and CA.identifier in (\'first_name\',\'last_name\',\'kommune\')\r\n"
					+ "and O.id = ? and exists \r\n"
					+ " (select null from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2 \r\n"
					+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id and OT2.contentobject_id = O.id and UR.role_id = R.id"
					+ roleCriteriaSQL + " )\r\n" + "order by O.id, CA.id";

            PreparedStatement preparedStatement = jt.getDataSource().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            if (!roleCriteriaSQL.equals("")) {
                preparedStatement.setString(2, roleCriteria);
            }
            ResultSet rowSet = preparedStatement.executeQuery();

			int curId = 0;

			while (rowSet.next()) {
				if (rowSet.getInt("id") != curId) {
					curId = rowSet.getInt("id");
					user.setId(curId);
					user.setName(rowSet.getString("name"));
					user.setEmail(rowSet.getString("email"));
                    user.setUsername(rowSet.getString("login"));
                }
				String identifier = rowSet.getString("identifier");
				if ("first_name".equals(identifier)) {
					user.setFirst_name(rowSet.getString("data_text"));
				} else if ("last_name".equals(identifier)) {
					user.setLast_name(rowSet.getString("data_text"));
				} else if ("kommune".equals(identifier)) {
					user.setKommune(NumConvert.convertToIntegerTolerant(rowSet
							.getString("data_text")));
                }
			}

			user.setRolenames(findRoles(user.getId()));
		} catch (InvalidResultSetAccessException e) {
			e.printStackTrace();
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
            e.printStackTrace();
        }

        return user;
	}

    private List<String> findRoles(Integer userid) {
        if (userid == null) {
            return null;
        }
        List<String> roles = new LinkedList();
        String sql = "select distinct R.name from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2"
                + " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id"
                + " and OT2.contentobject_id = ? and UR.role_id = R.id";
        try {
            PreparedStatement preparedStatement = jt.getDataSource().getConnection().prepareStatement(sql);
            preparedStatement.setInt(1, userid);
            ResultSet rowSet = preparedStatement.executeQuery();
            while (rowSet.next()) {
                roles.add(rowSet.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return roles;
    }




	/*
	 * (non-Javadoc)
	 * 
	 * @see no.unified.soak.dao.ExtUserDAO#findUsers(java.util.List)
	 */
	public List<ExtUser> findUsers(List<String> roles) {
		List users = userDAO.getUsersByRoles(roles); // from local database

		List<ExtUser> extUsers = new ArrayList<ExtUser>(users.size());
		for (Iterator iterator = users.iterator(); iterator.hasNext();) {
			User user = (User) iterator.next();
			extUsers.add(ConvertDAO.User2ExtUser(user));
		}
		return extUsers;
	}
	
	
	/* (non-Javadoc)
     * @see no.unified.soak.dao.ExtUserDAO#findKursansvarligeUser()
     */
	public List findUsers_REPLACED(List<String> roles) {
		
		String roleNames = "''";
		for (int i = 0; i < roles.size(); i++) {
		    String name = roles.get(i);
            roleNames += ",\'" + name +  "\'";
        }

		List eZUsers = new ArrayList();
		try {
			String sql = "select O.id, O.name, CA.identifier, A.data_int, A.data_text, U.email, U.login from ezcontentobject O \r\n"
					+ "inner join ezcontentclass C on O.contentclass_id = C.id\r\n"
					+ "inner join ezcontentobject_attribute A on A.contentobject_id = O.id\r\n"
					+ "inner join ezcontentclass_attribute CA on CA.contentclass_id = C.id and CA.id = A.contentclassattribute_id\r\n"
					+ "inner join ezuser U on U.contentobject_id = O.id\r\n"
					+ "where C.identifier = \'user\' and O.current_version = A.version and CA.identifier in (\'first_name\',\'last_name\',\'kommune\')\r\n"
					+ "and exists \r\n"
					+ " (select null from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2 \r\n"
					+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id and OT2.contentobject_id = O.id"
					+ " and UR.role_id =  R.id and R.name in (?) )\r\n"
					+ " order by O.id, CA.id";
            PreparedStatement preparedStatement = jt.getDataSource().getConnection().prepareStatement(sql);
            preparedStatement.setString(1, roleNames);
            ResultSet rowSet = preparedStatement.executeQuery();
			int curId = 0;

			ExtUser user = null;
			while (rowSet.next()) {
				if (rowSet.getInt("id") != curId) {
					user = new ExtUser();
					eZUsers.add(user);
					curId = rowSet.getInt("id");
					user.setId(curId);
					user.setName(rowSet.getString("name"));
					user.setEmail(rowSet.getString("email"));
                    user.setUsername(rowSet.getString("login"));
                }
				String identifier = rowSet.getString("identifier");
				if ("first_name".equals(identifier)) {
					user.setFirst_name(rowSet.getString("data_text"));
				} else if ("last_name".equals(identifier)) {
					user.setLast_name(rowSet.getString("data_text"));
				} else if ("kommune".equals(identifier)) {
					user.setKommune(NumConvert.convertToIntegerTolerant(rowSet
							.getString("data_text")));
				}
			}
		} catch (InvalidResultSetAccessException e) {
			e.printStackTrace();
		} catch (DataAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
            e.printStackTrace();
        }
        return eZUsers;
	}

    	/* (non-Javadoc)
         * @see no.unified.soak.dao.ExtUserDAO#findAll()
         */
    	public List findAll() {
		List eZUsers = new ArrayList();

		try {
			String sql = "select O.id, O.name, CA.identifier, A.data_int, A.data_text, U.email, U.login from ezcontentobject O \r\n"
					+ "inner join ezcontentclass C on O.contentclass_id = C.id\r\n"
					+ "inner join ezcontentobject_attribute A on A.contentobject_id = O.id\r\n"
					+ "inner join ezcontentclass_attribute CA on CA.contentclass_id = C.id and CA.id = A.contentclassattribute_id\r\n"
					+ "inner join ezuser U on U.contentobject_id = O.id\r\n"
					+ "where C.identifier = \'user\' and O.current_version = A.version and CA.identifier in (\'first_name\',\'last_name\',\'kommune\')\r\n"
					+ "and exists \r\n"
					+ " (select null from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2 \r\n"
					+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id and OT2.contentobject_id = O.id and UR.role_id =  R.id )\r\n"
					+ "order by O.id, CA.id";
			SqlRowSet rowSet = jt.queryForRowSet(sql);
			int curId = 0;

			ExtUser user = null;
			while (rowSet.next()) {
				if (rowSet.getInt("id") != curId) {
					user = new ExtUser();
					eZUsers.add(user);
					curId = rowSet.getInt("id");
					user.setId(curId);
					user.setName(rowSet.getString("name"));
					user.setEmail(rowSet.getString("email"));
                    user.setUsername(rowSet.getString("login"));
                }
				String identifier = rowSet.getString("identifier");
				if ("first_name".equals(identifier)) {
					user.setFirst_name(rowSet.getString("data_text"));
				} else if ("last_name".equals(identifier)) {
					user.setLast_name(rowSet.getString("data_text"));
				} else if ("kommune".equals(identifier)) {
					user.setKommune(NumConvert.convertToIntegerTolerant(rowSet
							.getString("data_text")));
				}
			}
		} catch (InvalidResultSetAccessException e) {
			e.printStackTrace();
		} catch (DataAccessException e) {
			e.printStackTrace();
		}
		return eZUsers;
	}

    /* (non-Javadoc)
     * @see no.unified.soak.dao.ExtUserDAO#findRoles()
     */
    public List<String> findRoles(){
	        String sql = "select distinct R.name from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2"
	                + " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id"
	                + " and UR.role_id = R.id"
	                + " and R.id = 1 or R.id > 5"
	                + " order by R.id";
	        SqlRowSet rowSet = jt.queryForRowSet(sql);
	        List<String> roles = new LinkedList();
	        while (rowSet.next()) {
	            roles.add(rowSet.getString("name"));
	        }
	        return roles;
	}
    
	/* The roles expected from the eZ publish database.
	 * @see no.unified.soak.dao.ExtUserDAO#getStringForRole(no.unified.soak.model.RoleEnum)
	 */
	public String getStringForRole(RoleEnum role) {
		switch (role) {
		case ADMIN_ROLE:
			return "Administrator";
		case EDITOR_ROLE:
			return "Opplaringsansvarlig";
		case EVENTRESPONSIBLE_ROLE:
			return "Kursansvarlig";
		case EMPLOYEE:
			return "Ansatt";
		case ANONYMOUS:
			return "Anonymous";
		case READER_ROLE:
			return "FKPLesebruker";
		}
		return null;
	}

	public boolean isExternalStringRole(String roleStringFromExternal) {
		throw new RuntimeException("Method isExternalStringRole(String) is not implemented in EzUserDAOJdbc implementation of ExtUserDAO.");
	}
}
