package com.chinamobile.bpmspace.core.domain.index;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "lengthIndex")
public class CaseLengthIndex {
	@Id
	private String id;
	private int length = 0;
	private List<String> logList = new ArrayList<String>();

	public CaseLengthIndex() {

	}

	public void addLogList(String _logId) {
		if (this.logList.indexOf(_logId) < 0) {
			this.logList.add(_logId);
		}
	}

	public String getId() {
		return id;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public List<String> getLogList() {
		return logList;
	}

	public void setLogList(List<String> logList) {
		this.logList = logList;
	}

	public void removeLogFromLogList(String _logId) {
		this.logList.remove(this.logList.indexOf(_logId));
	}

}
