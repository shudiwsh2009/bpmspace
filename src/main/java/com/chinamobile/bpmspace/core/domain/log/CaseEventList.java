package com.chinamobile.bpmspace.core.domain.log;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "caseEventList")
public class CaseEventList {
	@Id
	private String id;
	private String eventName;
	private String nName;

	public String getnName() {
		return nName;
	}

	public void setnName(String nName) {
		this.nName = nName;
	}

	// private String type = "EVENT";
	private String logId;

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	private List<String> nextEventName;

	// private List<String> logId;
	// private String[] indexFlag = new String[4];

	public List<String> getNextEventName() {
		return nextEventName;
	}

	public void setNextEventName(List<String> nextEventName) {
		this.nextEventName = nextEventName;
	}

	public CaseEventList() {
		this.nextEventName = new ArrayList<String>();
		// this.logId = new ArrayList<String>();
	}

	public void addNextEventName(String _nextEventName) {
		if (this.nextEventName.indexOf(_nextEventName) < 0) {
			this.nextEventName.add(_nextEventName);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

}
