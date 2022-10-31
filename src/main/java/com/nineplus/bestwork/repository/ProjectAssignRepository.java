package com.nineplus.bestwork.repository;

public interface ProjectAssignRepository {

	Long getCompanyId();
	
	String getCompanyName();

	String getUserName();

	Long getUserId();

	Boolean getCanView();

	Boolean getCanEdit();
}
