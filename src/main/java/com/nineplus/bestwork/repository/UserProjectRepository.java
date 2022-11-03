package com.nineplus.bestwork.repository;

public interface UserProjectRepository {
	String getProjectId();

	String getProjectName();

	Boolean getCanView();

	Boolean getCanEdit();

}
