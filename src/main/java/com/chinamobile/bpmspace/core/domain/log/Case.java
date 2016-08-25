package com.chinamobile.bpmspace.core.domain.log;

import java.util.ArrayList;
import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "case")
public class Case implements Cloneable {
	@Id
	private String id;
	private String identifier = "";
	// id of log which contains this process
	@Indexed
	private String logId = "";
	@Indexed
	private String ownerId = "";
	private Date createTime = new Date();
	private ArrayList<Activity> activities = new ArrayList<Activity>();

	public Case() {
	}

	public Case(String _logId, String _identifier, String _name,
			String _description, String _ownerId,
			ArrayList<Activity> _activitySet) {
		ownerId = _ownerId;
		activities = _activitySet;
		identifier = _identifier;
		logId = _logId;
	}

	public void flushInstanceItem() {
		ownerId = null;
		// this.as = null;
		identifier = null;
		logId = null;
		activities.clear();
	}

	public String getId() {
		return id;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String id) {
		this.logId = id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public ArrayList<Activity> getActivities() {
		return activities;
	}

	public void setActivities(ArrayList<Activity> as) {
		this.activities = as;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	@Override
	public Case clone() {
		Case _case = null;
		try {
			_case = (Case) super.clone();
			_case.setCreateTime((Date) createTime.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return _case;
	}
}
