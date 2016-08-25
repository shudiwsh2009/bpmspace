package com.chinamobile.bpmspace.core.exception;

public class IndexDecriptorAccessException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6165636231014084917L;

	public IndexDecriptorAccessException() {
		super();
	}

	@Override
	public String toString() {
		return String.format("IndexDecriptorAccessException[info='%s']", info);
	}

	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
	}
}
