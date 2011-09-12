/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created Dec 20. 2005
 */
package no.unified.soak.dao.hibernate;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import no.unified.soak.dao.StatisticsDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.StatisticsTableRow;
import no.unified.soak.util.ApplicationResourcesUtil;
import no.unified.soak.util.DefaultQuotedNamingStrategy;

import org.apache.commons.lang.StringUtils;
import org.hibernate.SQLQuery;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

/**
 * Implementation of the CourseDAO
 * 
 * @author hrj
 */
public class StatisticsDAOHibernate extends BaseDAOHibernate implements StatisticsDAO {

	public List<StatisticsTableRow> findByDates(Date beginPeriod, Date endPeriod) {
		try {
			String sql = "";
			if (DefaultQuotedNamingStrategy.usesOracle()) {
				sql = "select Region, Område, \r\n"
					+ "sum(partNumCourses) as numCourses, \r\n"
					+ "sum(partNumRegistrations) as numRegistrations, \r\n"
					+ "sum(partNumRegistered) as numRegistered, \r\n"
					+ "sum(partNumAttendants) as numAttendants \r\n"
					+ "from ( \r\n"
						+ "select C.\"id\" as cid, OP.\"name\" as Region, O.\"name\" as Område, \r\n"
						+ "count(distinct C.\"id\") as partNumCourses, \r\n"
						+ "count(R.\"id\") as partNumRegistrations, \r\n"
						+ "sum(R.\"participants\") as partNumRegistered, \r\n"
							+ "(select C2.\"attendants\" from COURSE C2 where C2.\"id\" = C.\"id\") as partNumAttendants \r\n"
				
						+ "from ORGANIZATION O \r\n"
						+ "inner join COURSE C on O.\"id\" = C.\"organization2id\" \r\n"
						+ "left outer join REGISTRATION R on (R.\"courseid\" = C.\"id\" and R.\"status\" = 2) \r\n"
						+ "left outer join ORGANIZATION OP on OP.\"id\" = O.\"parentid\" \r\n"
				
						+ "where \r\n"
						// for testing i Aqua Data Studio el. benytt følgende for generering av timestamp: to_timestamp('2011-05-02 00:00:00','yyyy-mm-dd hh24:mi:ss')
						+ "C.\"starttime\" >= :beginPeriod and C.\"starttime\" <= :endPeriod \r\n"
						+ "and C.\"attendants\" > 0 and O.\"type\" = 3 and C.\"status\" != 3 \r\n"
								
						+ "group by (C.\"id\", OP.\"name\", O.\"name\") \r\n"
					+ ") "
					+ "group by rollup (Region, Område) \r\n"
											
					+ "union \r\n"
			
					+ "select OP.\"name\" as Region, O.\"name\" as Område, \r\n" 
					+ "count(distinct C.\"id\") as numCourses, \r\n"
					+ "count(R.\"id\") as numRegistrations, \r\n"
					+ "sum(R.\"participants\") as numRegistered, \r\n"
					+ "sum(R.\"participants\") as numAttendants  \r\n"
						
					+ "from ORGANIZATION O \r\n"
					+ "inner join COURSE C on O.\"id\" = C.\"organization2id\" \r\n"
					+ "inner join REGISTRATION R on R.\"courseid\" = C.\"id\" \r\n"
					+ "left outer join ORGANIZATION OP on OP.\"id\" = O.\"parentid\" \r\n"
						
					+ "where \r\n"
					// for testing i Aqua Data Studio el. benytt følgende for generering av timestamp: to_timestamp('2011-05-02 00:00:00','yyyy-mm-dd hh24:mi:ss')
					+ "C.\"starttime\" >= :beginPeriod and C.\"starttime\" <= :endPeriod \r\n"
					+ "and (C.\"attendants\" is null or C.\"attendants\" = 0) \r\n"
					+ "and R.\"status\" = 2 and O.\"type\" = 3 and C.\"status\" != 3 \r\n"
					+ "group by rollup (OP.\"name\", O.\"name\") \r\n"
						
					+ "order by 1 asc, 2 desc";
			} else {
				sql = "select F.Organisasjon, F.Tjenesteomrade, \r\n" 
					+ "sum(F.partNumCourses) as numCourses, \r\n"
					+ "sum(F.partNumRegistrations) as numRegistrations, \r\n"
					+ "sum(F.partNumRegistered) as numRegistered, \r\n"
					+ "sum(F.partNumAttendants) as numAttendants \r\n"
					+ "from ( \r\n"
					+ "select C.id as cid, O.name as Organisasjon, S.name as Tjenesteomrade, \r\n"
					+ "count(distinct C.id) as partNumCourses, \r\n"
					+ "count(R.id) as partNumRegistrations, \r\n"
					+ "sum(R.participants) as partNumRegistered, \r\n"
					+ "(select C2.attendants from course C2 where C2.id = C.id) as partNumAttendants \r\n" 
					+ "from servicearea S \r\n" 
					+ "inner join course C on C.serviceareaid = S.id \r\n" 
					+ "left outer join registration R on (R.courseid = C.id and R.status = 2) \r\n" 
					+ "left outer join organization O on O.id = S.organizationid \r\n"
					+ "where \r\n"
					+ "C.starttime >= :beginPeriod  and C.starttime <= :endPeriod \r\n"
					+ "and C.attendants > 0 and C.status != 3 \r\n" 
					+ "group by C.id, O.name, S.name \r\n" 
					+ ") as F \r\n"
					+ "group by F.Organisasjon, F.Tjenesteomrade with rollup \r\n" 
															
					+ "union \r\n"
							
					+ "select O.name as Organisasjon, S.name as Tjenesteomrade, \r\n" 
					+ "count(distinct C.id) as numCourses, \r\n"
					+ "count(R.id) as numRegistrations, \r\n"
					+ "sum(R.participants) as numRegistered, \r\n" 
					+ "sum(R.participants) as numAttendants \r\n"  
					
					+ "from servicearea S \r\n"
					+ "inner join course C on C.serviceareaid = S.id \r\n"
					+ "inner join registration R on R.courseid = C.id \r\n" 
					+ "left outer join organization O on O.id = S.organizationid \r\n" 
					
					+ "where \r\n" 
					    // for testing i Aqua Data Studio el. benytt følgende for generering av timestamp: to_timestamp('2011-05-02 00:00:00','yyyy-mm-dd hh24:mi:ss')
					+ "C.starttime >= :beginPeriod  and C.starttime <= :endPeriod \r\n" 
					+ "and (C.attendants is null or C.attendants = 0) \r\n" 
					+ "and R.status = 2 and C.status != 3 \r\n"
					+ "group by Organisasjon, Tjenesteomrade with rollup"; 
			}
	
			SQLQuery query = getSession().createSQLQuery(sql);
	
			query.setDate("beginPeriod", beginPeriod);
			query.setDate("endPeriod", endPeriod);
			List<Object[]> objRow = query.list();
			List<StatisticsTableRow> statRows = new ArrayList<StatisticsTableRow>(60);
			StatisticsTableRow prevRow = null;
			for (Object[] objArr : objRow) {
				StatisticsTableRow currentRow = new StatisticsTableRow((String) objArr[0], (String) objArr[1], toLong(objArr[2]),
						toLong(objArr[3]), toLong(objArr[4]), toLong(objArr[5]));
				if (currentRow.getUnitParent() == null) {
					if(ApplicationResourcesUtil.isSVV()){ currentRow.setUnit("Hele landet"); }
					else { currentRow.setUnit("Alle organisasjoner"); }
					currentRow.setCssClass("sumfinal");
				}
				else if (currentRow.getUnit() == null) {
					currentRow.setUnit(currentRow.getUnitParent());
					currentRow.setCssClass("sum1");
				}
				else {
					currentRow.setUnit(currentRow.getUnit());
				}
				
				if (prevRow == null) {
					prevRow = currentRow;
					statRows.add(currentRow);
					continue;
				}
				
				if (StringUtils.equals(prevRow.getUnit(), currentRow.getUnit())
						&& StringUtils.equals(prevRow.getUnitParent(), currentRow.getUnitParent())) {
					prevRow.setNumAttendants(prevRow.getNumAttendants() + currentRow.getNumAttendants());
					prevRow.setNumCourses(prevRow.getNumCourses() + currentRow.getNumCourses());
					prevRow.setNumRegistered(prevRow.getNumRegistered() + currentRow.getNumRegistered());
					prevRow.setNumRegistrations(prevRow.getNumRegistrations() + currentRow.getNumRegistrations());
				} 
				else {
					statRows.add(currentRow);
					prevRow = currentRow;
				}
			}


			if (!DefaultQuotedNamingStrategy.usesOracle()) {
				// Alternativ sortering av rader pga. manglende støtte for kombinasjon av rollup og order by for mysql-versjon(er) på fkp01/02.
				// Alle rader har allerede korrekte summer og css-param, men trenger og resorteres ettersom rollup legger på ekstra rader.
				
//				System.out.println("Antall rader funnet: " + statRows.size());
				Hashtable rowHolder = new Hashtable();
				StatisticsTableRow lastRowWithSumTotal = null;
				
				// putter alle rader i en hash med unitparent + unit som nøkkel
				Object[] rows = statRows.toArray();
				for(int r=0; r<rows.length; r++){
					StatisticsTableRow current = (StatisticsTableRow)rows[r];
//					System.out.println("Prosesserer rad: " + current.getUnitParent() + " / " + current.getUnit());
					if(current.getUnitParent() != null){
						String elementKey = current.getUnitParent() + (current.getUnit().equals(current.getUnitParent()) ? "" : current.getUnit()); // organisasjoner har "seg selv både som parent og unit)
						rowHolder.put(elementKey, current);
//						System.out.println("... lagt til i rowHolder!");
					}else{
						// siste rad med totalsum
						lastRowWithSumTotal = current;
//						System.out.println("... lagt til i som siste!");
					}
				}
				
				// legger alle nøkler i en vektor
				Enumeration keys = rowHolder.keys();
				Vector tmp = new Vector();
				while(keys.hasMoreElements()){
					String key = (String)keys.nextElement();
					tmp.add(key);
				}
//				System.out.println("Antall nøkler: " + tmp.size());
				
				// sorterer alle nøkler
				Object[] keysSorted = tmp.toArray();
				Arrays.sort(keysSorted);
				
				List<StatisticsTableRow> statRowsSorted = new ArrayList<StatisticsTableRow>(60);
				// henter ut rader basert sortert nøkkelliste
				for(int k=0; k<keysSorted.length; k++){
					String key = (String)keysSorted[k];
//					System.out.println("Henter ut rad basert på : \"" + key + "\"");
					statRowsSorted.add((StatisticsTableRow)rowHolder.get(key));
				}
	
				// legger til siste rad på slutten
				statRowsSorted.add(lastRowWithSumTotal); 
//				System.out.println("Antall rader re-sortert: " + statRowsSorted.size());
				
				statRows = statRowsSorted;
				// slutt alternativ sorting -----------------------------------------------
			}
			
			if(statRows.isEmpty()) statRows = null;
	
			return statRows;
		
		}
		catch(Exception e){
			logger.error("Feil ved uthenting av statistikkdata", e);
			return null;
		}
	}

	public List<Course> findEmptyCoursesByDates(Date beginPeriod, Date endPeriod){
		String sql = "";
		List<Course> emptyCourses = new ArrayList<Course>();
		
		// henter kun kurs/møter som har status CREATED (LAGRET)
		if (DefaultQuotedNamingStrategy.usesOracle()) {
			sql = "select \"id\", \"organization2id\" from course where \"status\" = 1 and \"id\" not in \r\n"
				+ "(select distinct \"courseid\" from REGISTRATION o, COURSE c \r\n" 
				+ "where o.\"courseid\" = c.\"id\" and c.\"starttime\" >= :beginPeriod and c.\"starttime\" <= :endPeriod) \r\n" 
				+ "and (\"attendants\" is null or \"attendants\" = 0) \r\n" 
				+ "and \"starttime\" >= :beginPeriod and \"starttime\" <= :endPeriod \r\n"
				+ "order by \"starttime\" asc, \"organization2id\" asc";
		}
		else {
			sql = "select id, organizationid from course where status = 1 and id not in \r\n"
				+ "(select distinct courseid from registration o, course c \r\n" 
				+ "where o.courseid = c.id and c.starttime >= :beginPeriod and c.starttime <= :endPeriod) \r\n" 
				+ "and (attendants is null or attendants = 0) \r\n" 
				+ "and starttime >= :beginPeriod and starttime <= :endPeriod \r\n"
				+ "order by starttime asc, organizationid asc";
		}
		
		SQLQuery query = getSession().createSQLQuery(sql);
		query.setDate("beginPeriod", beginPeriod);
		query.setDate("endPeriod", endPeriod);
		List<Object[]> objRow = query.list();

		for (Object[] objArr : objRow) {
			Long id = toLong(objArr[0]);
	        Course course = (Course) getHibernateTemplate().get(Course.class, id);
	        if (course != null) {
	        	emptyCourses.add(course);
	        }
		}
		return emptyCourses;
	}
	
	private Long toLong(Object number) {
		if (number == null) {
			return 0L;
		}

		if (number instanceof BigDecimal) {
			BigDecimal numBD = (BigDecimal) number;
			return numBD.longValue();
		} else if(number instanceof BigInteger){
			BigInteger numBI = (BigInteger) number;
			return numBI.longValue();
		}
		else {
			log.error("Got number object " + number + " from database. Exepcted class is BigDecimal. Returning null");
			return null;
		}
	}

}
