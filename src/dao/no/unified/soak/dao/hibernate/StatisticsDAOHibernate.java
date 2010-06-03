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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import no.unified.soak.dao.StatisticsDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.StatisticsTableRow;
import no.unified.soak.util.DefaultQuotedNamingStrategy;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.SQLQuery;

/**
 * Implementation of the CourseDAO
 * 
 * @author hrj
 */
public class StatisticsDAOHibernate extends BaseDAOHibernate implements StatisticsDAO {

	public List<StatisticsTableRow> findByDates(Date beginPeriod, Date endPeriod) {
		String sql;
		if (DefaultQuotedNamingStrategy.usesOracle()) {
			sql = "select OP.\"name\" as Region, O.\"name\" as område, \r\n" + "count(distinct C.\"id\") as numCourses, \r\n"
					+ "count(distinct R.\"id\") as numRegistrations, \r\n"
					+ "sum(distinct R.\"participants\") as numRegistered, \r\n"
					+ "sum(distinct C.\"attendants\") as numAttendants " + "from ORGANIZATION O \r\n"

					+ "inner join COURSE C on O.\"id\" = C.\"organization2id\" \r\n"
					+ "left outer join REGISTRATION R on (R.\"courseid\" = C.\"id\" and R.\"status\" = 2) \r\n"
					+ "left outer join ORGANIZATION OP on OP.\"id\" = O.\"parentid\" \r\n"

					+ "where C.\"starttime\" >= :beginPeriod and \r\n" + "C.\"starttime\" <= :endPeriod \r\n"
					+ "and C.\"attendants\" > 0 and O.\"type\" = 3 \r\n"
					+ "group by rollup (OP.\"name\", O.\"name\") \r\n" 
					
					+ "union \r\n"

					+ "select OP.\"name\" as Region, O.\"name\" as område, \r\n" + "count(distinct C.\"id\") as numCourses, \r\n"
					+ "count(distinct R.\"id\") as numRegistrations, \r\n"
					+ "sum(distinct R.\"participants\") as numRegistered, \r\n"
					+ "sum(distinct R.\"participants\") as numAttendants from ORGANIZATION O \r\n"

					+ "inner join COURSE C on O.\"id\" = C.\"organization2id\" \r\n"
					+ "inner join REGISTRATION R on R.\"courseid\" = C.\"id\" \r\n"
					+ "left outer join ORGANIZATION OP on OP.\"id\" = O.\"parentid\" \r\n"

					+ "where C.\"starttime\" >= :beginPeriod and \r\n" + "C.\"starttime\" <= :endPeriod \r\n"
					+ "and (C.\"attendants\" is null or C.\"attendants\" = 0) \r\n"
					+ "and R.\"status\" = 2 and O.\"type\" = 3 \r\n" + "group by rollup (OP.\"name\", O.\"name\") \r\n"
					+ "order by 1 asc, 2 desc";
		} else {
			sql = "";
		}

		// String hql =
		// "select new no.unified.soak.model.StatisticsTableRow(OP.name, O.name, " +
		// "count(distinct C) as numCourses, count(distinct R.id) as numRegistrations, " +
		// "sum(R.participants) as numRegistered, sum(C.attendants) as numAttendants) "
		// + "from Course C inner join C.organization2 O "
		// + "inner join C.registrations R "
		// + "left join O.parent OP "
		// + "where C.startTime >= :beginPeriod and C.startTime <= :endPeriod "
		// + "and C.attendants > 0 and R.status = 2 and OP.id != O.id "
		// + "group by rollup (OP.name, O.name) "
		// + "order by 1 asc, 2 desc";
		SQLQuery query = getSession().createSQLQuery(sql);

		// Query query = getSession().createQuery(hql);
		query.setDate("beginPeriod", beginPeriod);
		query.setDate("endPeriod", endPeriod);
		List<Object[]> objRow = query.list();
		List<StatisticsTableRow> statRows = new ArrayList<StatisticsTableRow>(60);
		StatisticsTableRow prevRow = null;
		for (Object[] objArr : objRow) {
			StatisticsTableRow currentRow = new StatisticsTableRow((String) objArr[0], (String) objArr[1], toLong(objArr[2]),
					toLong(objArr[3]), toLong(objArr[4]), toLong(objArr[5]));
			if (currentRow.getUnitParent() == null) {
				currentRow.setUnit("Hele landet");
				currentRow.setCssClass("sumfinal");
			} else if (currentRow.getUnit() == null) {
				currentRow.setUnit(currentRow.getUnitParent());
				currentRow.setCssClass("sum1");
			} else {
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
			} else {
				statRows.add(currentRow);
				prevRow = currentRow;
			}
		}
		return statRows;
	}

	public List<Course> findEmptyCoursesByDates(Date beginPeriod, Date endPeriod){
		String sql;
		List<Course> emptyCourses = new ArrayList<Course>();
		if (DefaultQuotedNamingStrategy.usesOracle()) {
			sql = "select \"id\", \"organization2id\" from course where \"id\" not in \r\n"
				+ "(select distinct \"courseid\" from REGISTRATION o, COURSE c \r\n" 
				+ "where o.\"courseid\" = c.\"id\" and c.\"starttime\" >= :beginPeriod and c.\"starttime\" <= :endPeriod) \r\n" 
				+ "and (\"attendants\" is null or \"attendants\" = 0) \r\n" 
				+ "and \"starttime\" >= :beginPeriod and \"starttime\" <= :endPeriod \r\n"
				+ "order by \"starttime\" asc, \"organization2id\" asc";
			
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
		} else {
			log.error("Got number object " + number + " from database. Exepcted class is BigDecimal. Returning null");
			return null;
		}
	}

}
