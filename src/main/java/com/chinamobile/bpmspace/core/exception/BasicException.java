package com.chinamobile.bpmspace.core.exception;

public class BasicException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7337565912230179381L;
	protected String info = "";

	public BasicException() {

	}

	public BasicException(String _info) {
		info = _info;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}
}
