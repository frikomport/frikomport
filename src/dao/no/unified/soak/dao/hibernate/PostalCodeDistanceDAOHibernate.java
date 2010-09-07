package no.unified.soak.dao.hibernate;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;

import no.unified.soak.dao.PostalCodeDistanceDAO;
import no.unified.soak.model.PostalCodeDistance;

/**
 * @author extkla
 * 
 */
public class PostalCodeDistanceDAOHibernate extends BaseDAOHibernate implements PostalCodeDistanceDAO {
    private JdbcTemplate jt = new JdbcTemplate();

	public void setDataSource(DataSource dataSource) {
		jt.setDataSource(dataSource);
    }
	
	public void savePostalCodeDistance(PostalCodeDistance distance) {
		String sql;
		if (exist(distance)) {
			sql = "update PostalCodeDistance set distance=" + distance.getDistance() + " where postalCode1='"
					+ distance.getPostalCode1() + "' and postalCode2 = '" + distance.getPostalCode2() + "'";
			jt.update(sql);
		} else {
			sql = "insert into PostalCodeDistance values ('" + distance.getPostalCode1() + "','" + distance.getPostalCode2() + "', "
					+ distance.getDistance() + ")";
			jt.update(sql);
		}
	}

	private boolean exist(PostalCodeDistance distance) {
		String sql = "select count(*) from PostalCodeDistance where PostalCode1 = '"+distance.getPostalCode1()+"' and PostalCode2='"+distance.getPostalCode2()+"'";
		int nRows = jt.queryForInt(sql);
		return (nRows > 0);
	}
}
