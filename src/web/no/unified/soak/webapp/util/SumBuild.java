/**
 * 
 */
package no.unified.soak.webapp.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import no.unified.soak.model.Course;

/**
 * @author extkla
 * 
 */
public class SumBuild {

	private List<Object[]> sumList = new ArrayList<Object[]>();
	private int idxPtr = 0;

	public void toFirstSum() {
		idxPtr = 0;
	}

	public int size() {
		return sumList.size();
	}

	public void addToNextSum(String sumName, Object courseObj, String getter) throws SecurityException, IllegalArgumentException,
			NoSuchMethodException, IllegalAccessException, InvocationTargetException {
		addToNextSum(sumName, invokeGetterNullsafe(courseObj, getter));
	}

	public void addToNextSum(String sumName, Integer valueToAdd) {
		if (sumList.size() <= idxPtr) {
			sumList.add(new Object[] { sumName, null });
		}

		if (valueToAdd == null) {
			idxPtr++;
			return;
		}

		Object[] objArr = sumList.get(idxPtr);
		if (objArr[1] == null) {
			objArr[1] = valueToAdd;
		} else {
			objArr[1] = (Integer) objArr[1] + valueToAdd;
		}

		idxPtr++;
	}

	public String get(int idx) {
		final Integer elem = (Integer) sumList.get(idx)[1];
		if (elem != null && elem instanceof Integer) {
			return elem.toString();
		}
		return "";
	}

	public String get(String sumName) {
		for (Object[] sumElem : sumList) {
			if (sumName.equals(sumElem[0]) && sumElem[1] != null) {
				return sumElem[1].toString();
			}
		}
		return "";
	}

	public static Integer invokeGetterNullsafe(Object courseObj, String getter) throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		if (courseObj == null || !(courseObj instanceof Course)) {
			return null;
		}
		Course course = (Course) courseObj;
		Method getterMethod = course.getClass().getMethod(getter);
		Object got = getterMethod.invoke(course);

		if (got == null || !(got instanceof Integer)) {
			return null;
		} else {
			return (Integer)got;
		}
	}
}
