/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
 */
/*
 * Created 20. Dec 2005
 */
package no.unified.soak.dao;

import java.util.Date;
import java.util.List;

import no.unified.soak.model.StatisticsTableRow;

/**
 * Statistics Data Access Object (DAO) interface.
 * 
 * @author kst
 */
public interface StatisticsDAO extends DAO {

	/**
	 * Finds courses based on date.
	 * 
	 * @param course
	 * @return
	 */
	public List<StatisticsTableRow> findByDates(Date beginPeriod, Date endPeriod);
}
