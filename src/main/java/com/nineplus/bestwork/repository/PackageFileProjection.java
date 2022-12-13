package com.nineplus.bestwork.repository;

public interface PackageFileProjection {
	Long getAwbId();

	String getCode();

	Long getPostPackageId();
	
	Long getFileId();
	
	String getType();
	
	String getName();
	
	String getPathFileServer();

}