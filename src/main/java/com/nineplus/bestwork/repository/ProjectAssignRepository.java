package com.nineplus.bestwork.repository;

public interface ProjectAssignRepository {
	String getName();

	Long getUserId();

	Boolean getCanView();

	Boolean getCanEdit();
}
