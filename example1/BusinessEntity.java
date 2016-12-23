package com.efx.ic.service;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class BusinessEntity {

	private static final int UNKNOWN_ENTITY = -1;

	private int identifier;
	
	private String name;
	
	public BusinessEntity(int identifier, String name) {
		this.identifier = identifier;
		this.name = name;
	}

	@ApiModelProperty(position = 1, required = true, value = "Business Identifier")
	public int getIdentifier() {
		return identifier;
	}	
	
	@ApiModelProperty(position = 2, required = true, value = "User Name")
	public String getName() {
		return name;
	}

	public boolean isUnknown() {
		return identifier == UNKNOWN_ENTITY;
	}
	
}
