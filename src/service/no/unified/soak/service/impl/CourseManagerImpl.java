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
package no.unified.soak.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import no.unified.soak.dao.CourseDAO;
import no.unified.soak.dao.PostalCodeDistanceDAO;
import no.unified.soak.model.Course;
import no.unified.soak.model.Person;
import no.unified.soak.model.PostalCodeCoordinate;
import no.unified.soak.model.PostalCodeDistance;
import no.unified.soak.service.CourseManager;
import no.unified.soak.util.GeoMathUtil;

import org.apache.commons.lang.StringUtils;


/**
 * Implementation of CourseManager interface to talk to the persistence layer.
 *
 * @author hrj
 */
public class CourseManagerImpl extends BaseManager implements CourseManager {
    private CourseDAO courseDAO;
    private PostalCodeDistanceDAO postalCodeDistanceDAO;

    
    /**
	 * @param postalCodeDistanceDAO the postalCodeDistanceDAO to set
	 */
	public void setPostalCodeDistanceDAO(PostalCodeDistanceDAO postalCodeDistanceDAO) {
		this.postalCodeDistanceDAO = postalCodeDistanceDAO;
	}

	/**
     * Set the DAO for communication with the data layer.
     * @param dao
     */
    public void setCourseDAO(CourseDAO dao) {
        this.courseDAO = dao;
    }

    /**
     * @see no.unified.soak.service.CourseManager#getAllCourses()
     */
    public List getAllCourses() {
        return courseDAO.getAllCourses();
    }

    /**
     * @see no.unified.soak.service.CourseManager#getCourse(String id)
     */
    public Course getCourse(final String id) {
    	Course result = null;
    	if (!StringUtils.isEmpty(id))
        	result = courseDAO.getCourse(new Long(id));
    	else
    		log.error("Call to CourseManagerImpl.getCourse with empty or null id (" + id + ")");
    	return result;
    }

    /**
     * @see no.unified.soak.service.CourseManager#saveCourse(Course course)
     */
    public void saveCourse(Course course) {
        courseDAO.saveCourse(course);
    }

    /**
     * @see no.unified.soak.service.CourseManager#removeCourse(String id)
     */
    public void removeCourse(final String id) {
    	if (!StringUtils.isEmpty(id))
    		courseDAO.removeCourse(new Long(id));
    	else
    		log.error("Call to CourseManagerImpl.removeCourse with empty or null id (" + id + ")");
    		
    }

    /**
     * @see no.unified.soak.service.CourseManager#searchCourses(no.unified.soak.model.Course, Date, Date)
     */
    public List<Course> searchCourses(Course course, Date startDate, Date stopDate, Integer[] status) {
        return courseDAO.searchCourses(course, startDate, stopDate, status);
    }

    /**
     * @see no.unified.soak.service.CourseManager#getWaitingListCourses()
     */
    public List<Course> getWaitingListCourses() {
        return courseDAO.getWaitingListCourses();
    }

    public List<Course> findByInstructor(Person person) {
        return courseDAO.findByInstructor(person);
    }

    public List<Course> findByPostalCodeGeoProximity(String postalCode, Integer numberOfHits) {
    	return courseDAO.findByPostalCodeGeoProximity(postalCode, numberOfHits);
    }
    
    /**
     * @see no.unified.soak.service.CourseManager#getUnpublished()
     */
    public List<Course> getUnpublished(Course course) {
        return courseDAO.getUnpublished(course);
    }

    /**
     * @see no.unified.soak.service.CourseManager#getUnpublished()
     */
    public List <String> getChangedList(Course originalCourse, Course changedCourse, String dateFormat) {
    	List <String> changedList = new ArrayList<String>();
    	SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
    	
        if (originalCourse.getStatus().intValue() != changedCourse.getStatus().intValue()){
            changedList.add("status");
        }
		if (!originalCourse.getName().equals(changedCourse.getName())){
			changedList.add("name");
		}
		if (!StringUtils.equals(originalCourse.getType(), changedCourse.getType())){
			changedList.add("type");
		}
		if (!sdf.format(originalCourse.getStartTime()).equals(sdf.format(changedCourse.getStartTime()))){
			changedList.add("startTime");
		}
		if (!sdf.format(originalCourse.getStopTime()).equals(sdf.format(changedCourse.getStopTime()))){
			changedList.add("stopTime");
		}
		if (!StringUtils.equals(originalCourse.getDuration(), changedCourse.getDuration())) {
			changedList.add("duration");
		}
		if (!originalCourse.getOrganizationid().equals(changedCourse.getOrganizationid())){
			changedList.add("organization");
		}
		if ((originalCourse.getServiceAreaid() == null && changedCourse.getServiceAreaid() != null) || (originalCourse.getServiceAreaid() != null && !originalCourse.getServiceAreaid().equals(changedCourse.getServiceAreaid()))){
			changedList.add("serviceArea");
		}
		if (!originalCourse.getLocationid().equals(changedCourse.getLocationid())){
			changedList.add("location");
		}
		if (!originalCourse.getResponsibleUsername().equals(changedCourse.getResponsibleUsername())){
			changedList.add("responsible");
		}
		if (!originalCourse.getInstructorid().equals(changedCourse.getInstructorid())){
			changedList.add("instructor");
		}
		if (originalCourse.getMaxAttendants() != changedCourse.getMaxAttendants()){
			changedList.add("maxAttendants");
		}
		if (!StringUtils.equals(originalCourse.getDescription(), changedCourse.getDescription())){
			changedList.add("description");
		}
		if (originalCourse.getChargeoverdue() != changedCourse.getChargeoverdue()){
			changedList.add("chargeoverdue");
		}
		return changedList;
    }
    
    public List<Course> getCoursesWhereRegisterByExpired(long millis){
        return courseDAO.getCoursesWhereRegisterByExpired(millis);
    }
    
    @Override
    public void evict(Object entity) {
    	courseDAO.evict(entity);
    }
    
    @Override
    public void flush() {
    	courseDAO.flush();
    }
    
    @Override 
    public boolean contains(Object entity) {
    	return courseDAO.contains(entity);
    }


	/**
	 * Assumes list of PostalCodeCoordinate are ordered by
	 * {@link no.unified.soak.model.PostalCodeCoordinate#getPostalCode()} in
	 * ascending order.
	 * 
	 * @param pcCoordinates
	 */
	public void makeDistancesInDatabase(List<PostalCodeCoordinate> pcCoordinates) {
		for (Iterator iterator = pcCoordinates.iterator(); iterator.hasNext();) {
			PostalCodeCoordinate pc1 = (PostalCodeCoordinate) iterator.next();

			inner: for (Iterator iterator2 = pcCoordinates.iterator(); iterator2.hasNext();) {
				PostalCodeCoordinate pc2 = (PostalCodeCoordinate) iterator2.next();

				if (pc1.compareTo(pc2) >= 0 || avoidableByHeuristics(pc1, pc2)) {
					//Skip calculating distances both directions or between far away places.
					continue inner;
				}

				PostalCodeDistance pcDistance = new PostalCodeDistance(pc1.getPostalCode(), pc2.getPostalCode());
				pcDistance.setDistance(new Double(1000 * GeoMathUtil.distanceDEG(pc1.getLatitude(), pc1.getLongitude(), pc2.getLatitude(), pc2
						.getLongitude())).intValue());
				
				postalCodeDistanceDAO.savePostalCodeDistance(pcDistance);
			}

		}
	}

	/**
	 * Decides if two postal codes are geographically distant enough to exclude
	 * them from distance calculation. <br/>
	 * Assumes pCoordinateA.getPostalCode() < pCoordinateB.getPostalCode()
	 * because those cases are not checked.
	 * <p/>
	 * The heuristics is based on this map of postal codes: <a
	 * href="http://epab.posten.no/Norsk/Nedlasting/_files/Postnummerkart.pdf"
	 * >http://epab.posten.no/Norsk/Nedlasting/_files/Postnummerkart.pdf</a>
	 * 
	 * @param pCoordinateA
	 * @param pCoordinateB
	 * @return
	 */
	private static boolean avoidableByHeuristics(PostalCodeCoordinate pCoordinateA, PostalCodeCoordinate pCoordinateB) {
		char[] omradeA = { pCoordinateA.getPostalCode().charAt(0), pCoordinateA.getPostalCode().charAt(1) };
		char[] omradeB = { pCoordinateB.getPostalCode().charAt(0), pCoordinateB.getPostalCode().charAt(1) };

		char b0 = omradeB[0];
		char a0 = omradeA[0];
		switch (a0) {

		case 6:
			if (b0 == '9') {
				return true;
			}
			break;

		case 5:
			if (b0 == '9') {
				return true;
			}
			break;

		case 4:
			if (b0 > '6') {
				return true;
			}
			break;

		case 3:
			if (b0 == '9') {
				return true;
			}
			break;

		case 2:
			if (b0 == '9') {
				return true;
			}
			break;

		case 1:
			if (b0 > '7') {
				return true;
			}
			break;

		case 0:
			if (b0 > '7') {
				return true;
			}
			break;
		}

		return false;
	}

}
