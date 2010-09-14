package no.unified.soak.dao.hibernate;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import no.unified.soak.dao.PostalCodeDistanceDAO;
import no.unified.soak.model.PostalCodeDistance;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

/**
 * @author extkla
 * 
 */
public class PostalCodeDistanceDAOHibernate extends BaseDAOHibernate implements PostalCodeDistanceDAO {
	public final static String POSTALCODE_DISTANCE_TABLE = "PostalCodeDistance";
	public final static String POSTALCODE_LOCATION_DISTANCE_TABLE = "PostalCodeLocationDistance";

	private JdbcTemplate jt = new JdbcTemplate();

	public void setDataSource(DataSource dataSource) {
		jt.setDataSource(dataSource);
	}

	public void savePostalCodeDistance(PostalCodeDistance distance) {
		String sql;
		if (existPostalCodeDistance(distance)) {
			sql = "update PostalCodeDistance set distance=" + distance.getDistance() + " where postalCode1='"
					+ distance.getPostalCode1() + "' and postalCode2 = '" + distance.getPostalCode2() + "'";
			jt.update(sql);
		} else {
			insertPostalCodeDistance(distance);
		}
	}

	public void insertPostalCodeDistance(PostalCodeDistance distance) {
		String sql = "insert into PostalCodeDistance values ('" + distance.getPostalCode1() + "','" + distance.getPostalCode2()
				+ "', " + distance.getDistance() + ")";
		jt.update(sql);
	}

	private boolean existPostalCodeDistance(PostalCodeDistance distance) {
		String sql = "select count(*) from PostalCodeDistance where PostalCode1 = '" + distance.getPostalCode1()
				+ "' and PostalCode2='" + distance.getPostalCode2() + "'";
		int nRows = jt.queryForInt(sql);
		return (nRows > 0);
	}

	public List<PostalCodeDistance> findDistancesByPostalCode(String postalCode) {
		List<PostalCodeDistance> postalCodeDistances = new ArrayList<PostalCodeDistance>(4600);
		String sql = "select * from PostalCodeDistance where PostalCode1 = '" + postalCode + "' or PostalCode2 = '" + postalCode + "'";
		SqlRowSet postalCodeDistancesRS = jt.queryForRowSet(sql);

		while (postalCodeDistancesRS.next()) {
			PostalCodeDistance postalCodeDistance = new PostalCodeDistance();
			postalCodeDistance.setDistance(postalCodeDistancesRS.getInt("Distance"));
			postalCodeDistance.setPostalcode1(postalCodeDistancesRS.getString("PostalCode1"));
			postalCodeDistance.setPostalcode2(postalCodeDistancesRS.getString("PostalCode2"));

			postalCodeDistances.add(postalCodeDistance);
		}
		return postalCodeDistances;
	}

	public void savePostalCodeLocationDistance(String postalCode, Long locationid, int distance) {
		String sql;
		if (existPostalCodeLocationDistance(postalCode, locationid)) {
			sql = "update PostalCodeLocationDistance set distance=" + distance + " where postalCode='" + postalCode
					+ "' and locationId = " + locationid;
			jt.update(sql);
		} else {
			insertPostalCodeLocationDistance(postalCode, locationid, distance);
		}
	}

	public void insertPostalCodeLocationDistance(String postalCode, Long locationid, int distance) {
		String sql = "insert into PostalCodeLocationDistance values ('" + postalCode + "'," + locationid + ", " + distance + ")";
		jt.update(sql);
	}

	private boolean existPostalCodeLocationDistance(String postalCode, Long locationId) {
		String sql = "select count(*) from PostalCodeLocationDistance where PostalCode = '" + postalCode + "' and locationId='"
				+ locationId + "'";
		int nRows = jt.queryForInt(sql);
		return (nRows > 0);
	}
	
	public List getLocationIds(String postalcode){
		List<Long> locationIds = new ArrayList<Long>();

		String sql = "select locationId from PostalCodeLocationDistance where postalCode = '" + postalcode + "' order by distance asc";
		SqlRowSet idsRS = jt.queryForRowSet(sql);
		while (idsRS.next()) {
			Long id = idsRS.getLong("locationid");
			locationIds.add(id);
		}
		return locationIds;
	}
	

	public void removePostalCodeLocationDistance(Long locationid) {
		String sql = "delete from PostalCodeLocationDistance where locationId = " + locationid;
		int nRows = jt.update(sql);
		log.info("Deleted " + nRows + " rows from PostalCodeLocationDistance.");
	}

	public void createIndexes() {
		String sql = "CREATE INDEX POSTALCODE2_IDX ON " + POSTALCODE_DISTANCE_TABLE + "(POSTALCODE2)";
		jt.execute(sql);

		sql = "create index locationid_distance_idx on " + POSTALCODE_LOCATION_DISTANCE_TABLE + " (locationid, distance)";
		jt.execute(sql);
	}
	
	
	// test
	private String PCDFile = "C:\\PostalCodeDistanceExport.csv";
	FileWriter fw = null; 
	
	public void openPCDFile(){
		log.info("Opening file: " + PCDFile);
		try {
			fw = new FileWriter(PCDFile, true);
	        String th = "\"POSTALCODE1\",\"POSTALCODE2\",\"DISTANCE\"\n";
	        fw.write(th);
		}catch(Exception e){
			log.error("Error opening file: " + PCDFile, e);
		}
	}
	
	public void	appendToExportFilePCD(PostalCodeDistance pcd){
		String row = "\"" + pcd.getPostalCode1() + "\"," + "\"" + pcd.getPostalCode2() + "\"," + pcd.getDistance() + "\n";
		try {
			fw.write(row);
		}catch(Exception e){
			log.error("Error writing row to file: " + PCDFile, e);
		}
	}

	public void closePDCFile(){
		try {
			fw.close();
		}catch(Exception e){
			log.error("Error closing file: " + PCDFile, e);
		}
		log.info("Closing file: " + PCDFile);
	}
}
