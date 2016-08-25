package com.chinamobile.bpmspace.core.exception;

public class ActionRejectException extends BasicException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1557920068257629644L;

	public ActionRejectException() {
		super();
	}

	public ActionRejectException(String _info) {
		super(_info);
	}

	@Override
	public String toString() {
		return String.format("ActionRejectException[info='%s']", info);
	}

	@Override
	public void printStackTrace() {
		System.out.println(this.toString());
	}
}
