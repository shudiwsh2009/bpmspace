package com.chinamobile.bpmspace.core.domain.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;

public class Activity {
	private String name;
	private String caseId;
	private String actor;
	private long startTime;
	private long endTime;
	private HashMap<String, String> activitylist = new HashMap<String, String>();
	private HashMap<String, String> attributelist = new HashMap<String, String>();

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String InstanceID) {
		caseId = InstanceID;
	}

	public HashMap<String, String> getActivitylist() {
		return activitylist;
	}

	public void setActivitylist(HashMap<String, String> activitylist) {
		this.activitylist = activitylist;
	}

	public HashMap<String, String> getAttributelist() {
		return attributelist;
	}

	public void setAttributelist(HashMap<String, String> attributelist) {
		this.attributelist = attributelist;
	}

	public void putActivity(String key, String value) {
		this.activitylist.put(key, value);
	}

	public void putAttribute(String key, String value) {
		this.attributelist.put(key, value);
	}

	public String getAttribute(String key) {
		return this.attributelist.get(key);
	}

	public String getActivity(String key) {
		return this.activitylist.get(key);
	}

	public String getActivitystr() {
		ArrayList<String> lls = new ArrayList<String>();
		for (String as : activitylist.keySet()) {
			lls.add(as);
		}
		Collections.sort(lls);
		String llx = "[";
		for (int index = 0; index < lls.size(); index++) {
			if (index == 0) {
				llx += activitylist.get(lls.get(index));
			} else {
				llx += "," + activitylist.get(lls.get(index));
			}

		}
		llx += "]";
		return llx;
	}

	public Date gettimedata() {
		return null;
	}

	public void setStartTime(long _startTime) {
		this.startTime = _startTime;
	}

	public void setEndTime(long _endTime) {
		this.endTime = _endTime;
	}

	public void setName(String _name) {
		this.name = _name;
	}

	public void setActor(String _actor) {
		this.actor = _actor;
	}

	public String getName() {
		return this.name;
	}

	public String getActor() {
		return this.actor;
	}

	public long getStartTime() {
		return startTime;
	}

	public long getEndTime() {
		return endTime;
	}
}
