package com.pithy.free.crud.ex;

/**
 * 
 * @author shadow
 * 
 */
@SuppressWarnings("serial")
public class DbEx extends Exception {

	private String errorCode;

	public DbEx() {
		super();
	}

	public DbEx(Throwable e) {
		super(e);
	}

	public DbEx(String msg) {
		super(msg);
	}

	public DbEx(String msg, Throwable e) {
		super(msg, e);
	}

	public DbEx(String errorCode, String msg) {
		super(msg);
		setErrorCode(errorCode);
	}

	public DbEx(String msg, Throwable e, String errorCode) {
		super(msg, e);
		setErrorCode(errorCode);
	}

	public String getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

}
