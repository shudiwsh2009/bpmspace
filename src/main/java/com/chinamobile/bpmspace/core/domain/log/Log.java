package com.chinamobile.bpmspace.core.domain.log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "log")
public class Log implements Cloneable {
	@Id
	private String id;
	@Indexed
	private String name = "";
	@Indexed
	private String ownerId = "";
	@Indexed
	private String catalogId = "";
	private Date createTime = new Date();
	private String[] indexFlag = new String[4];
	private List<Integer> lengthList = new ArrayList<Integer>();
	private List<Integer> durationList = new ArrayList<Integer>();
	private List<String> eventList = new ArrayList<String>();
	private List<CaseEventList> caseEventList = new ArrayList<CaseEventList>();

	public List<CaseEventList> getCaseEventList() {
		return caseEventList;
	}

	public void setCaseEventList(List<CaseEventList> caseEventList) {
		this.caseEventList = caseEventList;
	}

	public void setEventList(List<String> eventList) {
		this.eventList = eventList;
	}

	public Log() {

	}

	public void addEventToList(String _eventName) {
		if (this.eventList.indexOf(_eventName) < 0) {
			this.eventList.add(_eventName);
		}
	}

	public List<String> getEventList() {
		return this.eventList;
	}

	public List<Integer> getLengthList() {
		return lengthList;
	}

	public void setIndexFlagTrue(int index) {
		this.indexFlag[index] = "i";
	}

	public void setIndexFlagFalse(int index) {
		this.indexFlag[index] = null;
	}

	public String getIndexFlag(int index) {
		return this.indexFlag[index];
	}

	public void setLengthList(List<Integer> lengthList) {
		this.lengthList = lengthList;
	}

	public Log(String _name, String _ownerId, String _catalogId,
			List<Integer> _lengthList) {
		name = _name;
		ownerId = _ownerId;
		catalogId = _catalogId;
		for (int i = 0; i < 4; i++) {
			this.indexFlag[i] = null;
		}
		this.setLengthList(_lengthList);
	}

	public boolean addLengthItem(int i) {
		if (this.lengthList.indexOf(i) >= 0) {
			return false;
		} else {
			this.lengthList.add(i);
			return true;
		}
	}

	public boolean addDurationItem(int i) {
		if (this.durationList.indexOf(i) >= 0) {
			return false;
		} else {
			this.durationList.add(i);
			return true;
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String _id) {
		this.id = _id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public String getCatalogId() {
		return catalogId;
	}

	public void setCatelogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public void setCatalogId(String catalogId) {
		this.catalogId = catalogId;
	}

	public List<Integer> getDurationList() {
		return durationList;
	}

	public void setDurationList(List<Integer> durationList) {
		this.durationList = durationList;
	}

	@Override
	public Log clone() {
		Log log = null;
		try {
			log = (Log) super.clone();
			// log.setType(type);
			log.setCreateTime((Date) createTime.clone());
			/**
			 * log.setRevision(new HashMap<Long, ProcessRevision>());
			 * 
			 * Iterator<Map.Entry<Long, ProcessRevision>> it =
			 * revision.entrySet().iterator(); while(it.hasNext()) {
			 * Map.Entry<Long, ProcessRevision> entry = it.next();
			 * process.getRevision().put(entry.getKey(), new
			 * ProcessRevision(entry.getValue().getModelId(),
			 * entry.getValue().getCreatorId(), (Date)
			 * entry.getValue().getCreateTime().clone())); }
			 * process.setUserPermission(new HashMap<String, Permission>());
			 * Iterator<Map.Entry<String, Permission>> it2 =
			 * userPermission.entrySet().iterator(); while(it2.hasNext()) {
			 * Map.Entry<String, Permission> entry = it2.next();
			 * process.getUserPermission().put(entry.getKey(),
			 * entry.getValue()); }
			 */
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return log;
	}

	public List<String> getInstances() {
		// TODO Auto-generated method stub
		return null;
	}
}
