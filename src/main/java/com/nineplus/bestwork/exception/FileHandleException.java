package com.nineplus.bestwork.exception;

public class FileHandleException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3546492425917531927L;

	/**
	 * Instantiates a new file handle exception.
	 */
	public FileHandleException() {
		super();
	}

	/**
	 * Instantiates a new File handle exception.
	 *
	 * @param msg the msg
	 */
	public FileHandleException(String msg) {
		super(msg);
	}

	/**
	 * Instantiates a new File handle exception.
	 *
	 * @param msg the msg
	 */
	public FileHandleException(String msg, Exception e) {
		super(msg, e);
	}

}
