package com.chinamobile.bpmspace.core.exception;

public class EmptyFieldException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6414845463523967166L;

	public EmptyFieldException() {
		super();
	}

	public EmptyFieldException(String _info) {
		super(_info);
	}

	@Override
	public String toString() {
		return String.format("EmptyFieldException[info='%s']", info);
	}

	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
	}
}
