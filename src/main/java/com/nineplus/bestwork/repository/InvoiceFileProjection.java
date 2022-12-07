package com.nineplus.bestwork.repository;

public interface InvoiceFileProjection {

	Long getAwbId();

	String getCode();

	Long getPostInvoiceId();
	
	Long getFileId();
	
	String getType();
	
	String getName();

	String getPathFileServer();

}
