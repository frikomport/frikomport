/**
 * 
 */
package no.unified.soak.webapp.util;

import java.util.ArrayList;
import java.util.List;

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
			objArr[1] = (Integer)objArr[1] + valueToAdd;
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

}
