package com.nineplus.bestwork.repository;

public interface ProjectAssignRepository {

	Long getCompanyId();
	
	String getCompanyName();

	String getName();

	Long getUserId();

	Boolean getCanView();

	Boolean getCanEdit();
}
