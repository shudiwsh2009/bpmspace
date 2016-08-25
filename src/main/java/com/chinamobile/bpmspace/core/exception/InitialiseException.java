package com.chinamobile.bpmspace.core.exception;

public class InitialiseException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -788193508326198135L;

	public InitialiseException() {
		super();
	}

	public InitialiseException(String _info) {
		super(_info);
	}

	@Override
	public String toString() {
		return String.format("InitialiseException[info='%s']", info);
	}

	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
	}

}
