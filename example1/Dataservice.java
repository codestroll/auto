package com.efx.ic.service;


public class Dataservice {

	public static BusinessEntity getById(String businessIdentifier) {
		if (businessIdentifier.equals("ENRON")) {
			return new BusinessEntity(125, "ENRON");			
		}
		return new BusinessEntity(-1, "UNKNOWN");
	}

}
