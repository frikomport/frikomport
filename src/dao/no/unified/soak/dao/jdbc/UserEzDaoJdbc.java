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

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ResourceBundle;

import no.unified.soak.Constants;
import no.unified.soak.ez.EzUser;
import no.unified.soak.util.NumConvert;

import org.springframework.dao.DataAccessException;

import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author kst
 * 
 */
public class UserEzDaoJdbc {
	DriverManagerDataSource dataSource = new DriverManagerDataSource();

	JdbcTemplate jt = new JdbcTemplate();

	public UserEzDaoJdbc() {
		String driver = ResourceBundle.getBundle(Constants.BUNDLE_KEY)
				.getString("ezpublish.database.driver_class");
		String url = ResourceBundle.getBundle(Constants.BUNDLE_KEY).getString(
				"ezpublish.database.url");
		String user = ResourceBundle.getBundle(Constants.BUNDLE_KEY).getString(
				"ezpublish.database.username");
		String pwd = ResourceBundle.getBundle(Constants.BUNDLE_KEY).getString(
				"ezpublish.database.password");

		dataSource.setDriverClassName(driver);
		dataSource.setUrl(url);
		dataSource.setUsername(user);
		dataSource.setPassword(pwd);

		jt.setDataSource(dataSource);
	}

	public EzUser findUserBySessionID(String sessionId) {
		String sql = "select user_id, expiration_time from ezsession where session_key = \'"
				+ sessionId + "\'";
		SqlRowSet rowSet = jt.queryForRowSet(sql);
		EzUser eZuser = null;
		if (rowSet.next()) {
			Integer uid = rowSet.getInt("user_id");
			Long expTime = rowSet.getLong("expiration_time");
			Long curTime = new Date().getTime() / 1000;
//			System.out.println("UserEzDaoJdbc.findUserBySessionID(): expTime="
//					+ expTime + ", curTime=" + curTime);
			if (curTime < expTime) {
				eZuser = findUser(uid, null, true);
			} else {
				/*
				 * User is not logged in any more, so don't return the user
				 * object.
				 */
				eZuser = new EzUser();
//				System.out
//						.println("UserEzDaoJdbc.findUserBySessionID(): eZ user has timedout. Java module is therefore not accepting the user.");
			}
		}
		return eZuser;
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
	private EzUser findUser(Integer userid, String roleCriteria,
			boolean findRelated) {
		if (userid == null) {
			return null;
		}
		String roleCriteriaSQL = "";
		if (roleCriteria != null && roleCriteria.trim().length() > 0) {
			roleCriteriaSQL = " and R.name in (" + roleCriteria + ")";
		}
		EzUser user = new EzUser();
		try {
			String sql = "select O.id, O.name, CA.identifier, A.data_int, A.data_text from ezcontentobject O \r\n"
					+ "inner join ezcontentclass C on O.contentclass_id = C.id\r\n"
					+ "inner join ezcontentobject_attribute A on A.contentobject_id = O.id\r\n"
					+ "inner join ezcontentclass_attribute CA on CA.contentclass_id = C.id and CA.id = A.contentclassattribute_id\r\n"
					+ "where C.identifier = \'user\' and O.current_version = A.version and CA.identifier in (\'first_name\',\'last_name\',\'kommune\')\r\n"
					+ "and O.id = "
					+ userid
					+ " and exists \r\n"
					+ " (select null from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2 \r\n"
					+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id and OT2.contentobject_id = O.id and UR.role_id = R.id"
					+ roleCriteriaSQL + " )\r\n" + "order by O.id, CA.id";
			SqlRowSet rowSet = jt.queryForRowSet(sql);
			int curId = 0;

			while (rowSet.next()) {
				if (rowSet.getInt("id") != curId) {
					curId = rowSet.getInt("id");
					user.setId(curId);
					user.setName(rowSet.getString("name"));
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
		}

		return user;
	}

	private List<String> findRoles(Integer userid) {
		if (userid == null) {
			return null;
		}
		String sql = "select R.name from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2"
				+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id"
				+ " and OT2.contentobject_id = "
				+ userid
				+ " and UR.role_id = R.id";
		SqlRowSet rowSet = jt.queryForRowSet(sql);
		List<String> roles = new LinkedList();
		while (rowSet.next()) {
			roles.add(rowSet.getString("name"));
		}
		return roles;
	}

	public EzUser findKursansvarligUser(Integer userid) {
		return findUser(userid, "\'Kursansvarlig\', \'Opplæringsansvarlig\'",
				false);
	}

	public List findKursansvarligeUser() {
		List eZUsers = new ArrayList();

		try {
			String sql = "select O.id, O.name, CA.identifier, A.data_int, A.data_text from ezcontentobject O \r\n"
					+ "inner join ezcontentclass C on O.contentclass_id = C.id\r\n"
					+ "inner join ezcontentobject_attribute A on A.contentobject_id = O.id\r\n"
					+ "inner join ezcontentclass_attribute CA on CA.contentclass_id = C.id and CA.id = A.contentclassattribute_id\r\n"
					+ "where C.identifier = \'user\' and O.current_version = A.version and CA.identifier in (\'first_name\',\'last_name\',\'kommune\')\r\n"
					+ "and exists \r\n"
					+ " (select null from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2 \r\n"
					+ " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id and OT2.contentobject_id = O.id and UR.role_id =  R.id and R.name in (\'Kursansvarlig\', \'Opplæringsansvarlig\') )\r\n"
					+ "order by O.id, CA.id";
			SqlRowSet rowSet = jt.queryForRowSet(sql);
			int curId = 0;

			EzUser user = null;
			while (rowSet.next()) {
				if (rowSet.getInt("id") != curId) {
					user = new EzUser();
					eZUsers.add(user);
					curId = rowSet.getInt("id");
					user.setId(curId);
					user.setName(rowSet.getString("name"));
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

	public List<String> findRoles(){
	        String sql = "select distinct R.name from ezcontentobject_tree OT, ezuser_role UR, ezrole R, ezcontentobject_tree OT2"
	                + " where OT.contentobject_id = UR.contentobject_id and OT.node_id = OT2.parent_node_id"
	                + " and UR.role_id = R.id"
	                + " and R.id = 1 or R.id > 5"; 
	        SqlRowSet rowSet = jt.queryForRowSet(sql);
	        List<String> roles = new LinkedList();
	        while (rowSet.next()) {
	            roles.add(rowSet.getString("name"));
	        }
	        return roles;
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		UserEzDaoJdbc denne = new UserEzDaoJdbc();
		List users = denne.findKursansvarligeUser();
		for (Iterator iter = users.iterator(); iter.hasNext();) {
			EzUser user = (EzUser) iter.next();
//			System.out.println("ez User: " + user.getId() + ", " + user + " "
//					+ user.getKommune());
		}
	}
}
