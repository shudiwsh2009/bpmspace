package com.chinamobile.bpmspace.core.repository.index;

public class LogQueryResult implements Comparable<LogQueryResult> {
	private String logId = null;
	private int length = 0;
	private String eventName = null;

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public LogQueryResult(String _logId, int _length) {
		this.logId = _logId;
		this.length = _length;
	}

	public LogQueryResult(String _logId, String eventName) {
		this.logId = _logId;
		this.eventName = eventName;
	}

	@Override
	public int compareTo(LogQueryResult arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getLog_id() {
		return logId;
	}

	public void setLog_id(String log_id) {
		this.logId = log_id;
	}
}
