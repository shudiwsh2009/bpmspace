package com.chinamobile.bpmspace.core.exception;

public class DuplicateFieldException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -831062459071817378L;

	public DuplicateFieldException() {
		super();
	}

	public DuplicateFieldException(String _info) {
		super(_info);
	}

	@Override
	public String toString() {
		return String.format("DuplicateFieldException[info='%s']", info);
	}

	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
	}
}
