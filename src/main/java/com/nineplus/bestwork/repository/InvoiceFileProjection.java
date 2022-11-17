package com.nineplus.bestwork.repository;

public interface InvoiceFileProjection {
	String getCode();

	Long getPostInvoiceId();
	
	Long getFileId();
	
	String getType();
	
	String getName();

}
