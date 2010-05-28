/*
 * This file is distributed under the GNU Public Licence (GPL).
 * See http://www.gnu.org/copyleft/gpl.html for further details
 * of the GPL.
 *
 * @author Unified Consulting AS
*/
/*
 * Created 2010
 */
package no.unified.soak.service.impl;

import java.util.Date;
import java.util.List;

import no.unified.soak.dao.StatisticsDAO;
import no.unified.soak.model.StatisticsTableRow;
import no.unified.soak.service.StatisticsManager;


/**
 * Implementation of StatisticsManager interface to talk to the persistence layer.
 *
 */
public class StatisticsManagerImpl extends BaseManager implements StatisticsManager {
    private StatisticsDAO dao;

    /**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setStatisticsDAO(StatisticsDAO dao) {
        this.dao = dao;
    }

	public List<StatisticsTableRow> findByDates(Date beginPeriod, Date endPeriod) {
		return dao.findByDates(beginPeriod, endPeriod);
	}
}
