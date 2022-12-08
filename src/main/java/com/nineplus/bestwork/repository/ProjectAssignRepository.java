package com.nineplus.bestwork.repository;

public interface ProjectAssignRepository {

	Long getCompanyId();
	
	String getCompanyName();

	String getUserName();

	String getRoleName();

	Long getUserId();

	Boolean getCanView();

	Boolean getCanEdit();
}
