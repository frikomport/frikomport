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

import java.util.Date;
import java.util.List;

import no.unified.soak.dao.StatisticsDAO;
import no.unified.soak.model.StatisticsTableRow;
import no.unified.soak.util.DateUtil;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Query;

/**
 * Implementation of the CourseDAO
 * 
 * @author hrj
 */
public class StatisticsDAOHibernate extends BaseDAOHibernate implements StatisticsDAO {

	public List<StatisticsTableRow> findByDates(Date beginPeriod, Date endPeriod) {
		// TODO Auto-generated method stub
		String hql = "select new no.unified.soak.model.StatisticsTableRow(OP.name, O.name, "
				+ "count(distinct C) as numCourses, count(distinct R.id) as numRegistrations, "
				+ "sum(R.participants) as numRegistered, sum(C.attendants) as numAttendants) "
				//String hql =
				// "select OP.name, O.name, count(distinct C), 4, 3, 1 " +
				+ "from Course C inner join C.organization2 O " 
				+ "inner join C.registrations R "
				+ "left join O.parent OP "
				+ "where C.startTime >= :beginPeriod and C.startTime <= :endPeriod "
				+ "and C.attendants > 0 and R.status = 2 and OP.id != O.id " 
				+ "group by rollup (OP.name, O.name) "
				+ "order by 1 asc, 2 desc";
		Query query = getSession().createQuery(hql);
		query.setDate("beginPeriod", beginPeriod);
		query.setDate("endPeriod", endPeriod);
		// List objRow = query.list();
		List<StatisticsTableRow> statRow = query.list();
		for (StatisticsTableRow aRow : statRow) {
			if (aRow.getUnit() == null) {
				aRow.setUnit(aRow.getUnitParent());
			}
			if (aRow.getUnit() == null) {
				aRow.setUnit("Hele landet");
			}
		}
		// for (Iterator iterator = statRow.iterator(); iterator.hasNext();) {
		// Object obj = iterator.next();
		// StatisticsTableRow aRow = (StatisticsTableRow) obj;
		//			
		// if (aRow.getUnit() == null) {
		// aRow.setUnit("");
		// }
		// }
		return statRow;
	}

}
