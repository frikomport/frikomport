package no.unified.soak.model;

/**
 * Data transfar class used to retrieve and display statistics. The class is not
 * used for data storage.
 * 
 * @author Klaus
 */
public class StatisticsTableRow {
	private String unitParent;
	private String unit;
	private Integer numCourses;
	private Integer numRegistrations;
	private Integer numRegistered;
	private Integer numAttendants;

	public StatisticsTableRow(String unitParent, String unit, Long numCourses, Long numRegistrations,
			Long numRegistered, Long numAttendants) {
		this.unitParent = unitParent;
		this.unit = unit;
		this.numCourses = numCourses.intValue();
		this.numRegistrations = numRegistrations.intValue();
		this.numRegistered = numRegistered.intValue();
		this.numAttendants = numAttendants.intValue();
	}

	/**
	 * @return the unitParent
	 */
	public String getUnitParent() {
		return unitParent;
	}

	/**
	 * @param unitParent the unitParent to set
	 */
	public void setUnitParent(String unitParent) {
		this.unitParent = unitParent;
	}

	/**
	 * @return The unit for which the numbers apply.
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * @param unit
	 *            the unit for which the numbers apply.
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/**
	 * @return the number of courses
	 */
	public Integer getNumCourses() {
		return numCourses;
	}

	/**
	 * @param numCourses
	 *            number of courses
	 */
	public void setNumCourses(Integer numCourses) {
		this.numCourses = numCourses;
	}

	/**
	 * Antall førerkortkandidater antas å være likt antall påmeldinger.
	 * 
	 * @return Number of registrations
	 */
	public Integer getNumRegistrations() {
		return numRegistrations;
	}

	/**
	 * @param numRegistrations
	 *            number og registrations to set
	 */
	public void setNumRegistrations(Integer numRegistrations) {
		this.numRegistrations = numRegistrations;
	}

	/**
	 * @return number of attendan
	 */
	public Integer getNumAttendants() {
		return numAttendants;
	}

	/**
	 * @param numAttendants
	 *            number of attendants
	 */
	public void setNumAttendants(Integer numAttendants) {
		this.numAttendants = numAttendants;
	}

	/**
	 * @return number of registered
	 */
	public Integer getNumRegistered() {
		return numRegistered;
	}

	/**
	 * @param numRegistered
	 *            number of registered
	 */
	public void setNumRegistered(Integer numRegistered) {
		this.numRegistered = numRegistered;
	}
}