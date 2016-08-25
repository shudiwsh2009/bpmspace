package com.chinamobile.bpmspace.core.exception;

public class NoExistException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 594081629852644636L;

	public NoExistException() {
		super();
	}

	public NoExistException(String _info) {
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
