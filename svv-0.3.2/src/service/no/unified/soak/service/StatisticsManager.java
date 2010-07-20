/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 20. dec 2005
 */
package no.unified.soak.service;

import java.util.Date;
import java.util.List;

import no.unified.soak.dao.StatisticsDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.StatisticsTableRow;


/**
 * Business Service Interface to handle communication between web and
 * persistence layer.
 *
 * @author hrj
 */
public interface StatisticsManager extends Manager {
    /**
     * Setter for DAO, convenient for unit testing
     */
    public void setStatisticsDAO(StatisticsDAO statisticsDAO);

	public List<StatisticsTableRow> findByDates(Date beginPeriod, Date endPeriod);
	
	public List<Course> findEmptyCoursesByDates(Date beginPeriod, Date endPeriod);

}
