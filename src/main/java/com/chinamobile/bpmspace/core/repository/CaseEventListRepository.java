package com.chinamobile.bpmspace.core.repository;

import java.util.ArrayList;
import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.log.CaseEventList;

public class CaseEventListRepository {
	private static MongoAccess mongo = new MongoAccess();

	private List<CaseEventList> celList = new ArrayList<CaseEventList>();

	public void getCaseEvent(String _eventName, String _nextEventName,
			String _logId) {
		CaseEventList cel = new CaseEventList();
		cel.setEventName(_eventName);
		if (_nextEventName != null && !_nextEventName.equals("")) {
			cel.setnName(_nextEventName);
		}
		cel.setLogId(_logId);
		addEventToList(cel);
	}

	public void addEventToList(CaseEventList _cel) {
		boolean hasItemInList = false;
		for (CaseEventList ncel : this.celList) {
			if (ncel.getEventName().equals(_cel.getEventName())) {
				ncel.addNextEventName(_cel.getnName());
				hasItemInList = true;
			}
		}
		if (!hasItemInList) {
			this.celList.add(_cel);
			for (CaseEventList ncel : this.celList) {
				if (ncel.getEventName().equals(_cel.getEventName())) {
					ncel.addNextEventName(_cel.getnName());
					// cel.addNextEventName(_cel.getnName());
					// hasItemInList = true;
				}
			}
		}
	}

	public void flushList() {
		this.celList.clear();
	}

	public List<CaseEventList> getCaseEventList() {
		return this.celList;
	}
	/*
	 * public boolean addCaseEventToList(String _eventName, String
	 * _nextEventName, String _logId) {
	 * 
	 * if (mongo.getCasesByName(_eventName) == null ||
	 * mongo.getCasesByName(_eventName).equals("")) {
	 * this.cel.setEventName(_eventName); if (_nextEventName != null &&
	 * !_nextEventName.equals("")){ this.cel.addNextEventName(_nextEventName); }
	 * this.cel.addLogId(_logId); //mongo.saveCaseEventList(cel); return true; }
	 * else { CaseEventList cel = mongo.getCasesByName(_eventName); if
	 * (_nextEventName != null && !_nextEventName.equals("")){
	 * cel.addNextEventName(_nextEventName); } cel.addLogId(_logId);
	 * mongo.saveCaseEventList(cel); return false; } }
	 * 
	 * 
	 * public void delCaseEventFromList(String _eventName) {
	 * 
	 * }
	 */
}
