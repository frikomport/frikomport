/**
 * 
 */
package no.unified.soak.service;


/**
 * Exception used to show special user friendly error message page when the
 * requested course is not found.
 * 
 * @author touextkla
 * 
 */
public class CourseAccessException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1623122263819640538L;

	public CourseAccessException(String str, Exception e) {
		super(str, e);
	}
}
