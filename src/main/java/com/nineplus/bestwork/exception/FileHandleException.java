package com.nineplus.bestwork.exception;

public class FileHandleException extends RuntimeException {

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
