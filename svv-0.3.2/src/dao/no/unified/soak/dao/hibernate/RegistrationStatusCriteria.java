package no.unified.soak.dao.hibernate;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import no.unified.soak.model.Registration;
import no.unified.soak.model.Registration.Status;

public class RegistrationStatusCriteria {
	private static RegistrationStatusCriteria NOT_CANCELED;
	
	LinkedList<Status> statuser = new LinkedList();

    public RegistrationStatusCriteria(Status... statuser) {
        for (Status status : statuser) {
            if (status != null) {
                this.statuser.add(status);
            }
        }
    }
	
	public List<Integer> getStatusValueList() {
		LinkedList<Integer> statusValueList = new LinkedList<Integer>();

		for (Status status : statuser) {
			if (status != null) {
				statusValueList.add(status.getDBValue());
			}
		}
		return statusValueList;
	}
	
	public static RegistrationStatusCriteria getNotCanceledCriteria() {
		if (NOT_CANCELED == null) {
			Collection<Status> statuser = new LinkedList<Status>(); 
			for (Registration.Status status : Registration.Status.values()) {
				if (status != Status.CANCELED) {
					statuser.add(status);
				}
			}
			NOT_CANCELED = new RegistrationStatusCriteria(statuser.toArray(new Status[Status.values().length-1]));
		}
		
		return NOT_CANCELED;
	}
}
