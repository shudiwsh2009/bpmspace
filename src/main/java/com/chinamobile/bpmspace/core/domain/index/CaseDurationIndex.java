package com.chinamobile.bpmspace.core.domain.index;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "durationIndex")
public class CaseDurationIndex {
	@Id
	private String id;
	private int duration = 0;
	private List<String> logList = new ArrayList<String>();

	public CaseDurationIndex() {

	}

	public void addLogList(String _logId) {
		if (this.logList.indexOf(_logId) < 0) {
			this.logList.add(_logId);
		}
	}

	public boolean removeLogFromDurationList(String _logId) {
		if (_logId.equals("") || _logId == null
				|| this.logList.indexOf(_logId) < 0) {
			return false;
		}
		this.logList.remove(this.logList.indexOf(_logId));
		return true;
	}

	public String getId() {
		return id;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

	public List<String> getLogList() {
		return logList;
	}

	public void setLogList(List<String> logList) {
		this.logList = logList;
	}

}
