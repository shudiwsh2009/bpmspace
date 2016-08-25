package com.ibm.bpm.model;

import java.util.List;

public class Process {
	public Process() {

	}

	public Process(String name, String type, String id) {
		super();
		this.name = name;
		this.type = type;
		this.id = id;
	}

	String name;
	String type;
	String id;
	List<Activity> activityList;
	List<Flow> flowList;
	List<FlowNodeRef> flowNodeRefList;
	List<Gateway> gatewayList;
	List<EndEvent> endeventList;
	List<StartEvent> starteventList;

	public List<EndEvent> getEndeventList() {
		return endeventList;
	}

	public void setEndeventList(List<EndEvent> endeventList) {
		this.endeventList = endeventList;
	}

	public List<StartEvent> getStarteventList() {
		return starteventList;
	}

	public void setStarteventList(List<StartEvent> starteventList) {
		this.starteventList = starteventList;
	}

	public List<Gateway> getGatewayList() {
		return gatewayList;
	}

	public void setGatewayList(List<Gateway> gatewayList) {
		this.gatewayList = gatewayList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<Activity> getActivityList() {
		return activityList;
	}

	public void setActivityList(List<Activity> activityList) {
		this.activityList = activityList;
	}

	public List<Flow> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<Flow> flowList) {
		this.flowList = flowList;
	}

	public List<FlowNodeRef> getFlowNodeRefList() {
		return flowNodeRefList;
	}

	public void setFlowNodeRefList(List<FlowNodeRef> flowNodeRefList) {
		this.flowNodeRefList = flowNodeRefList;
	}

	public ProcessNode findNodeById(String id2) {
		for (Activity activity : activityList)
			if (activity.getId().equals(id2))
				return activity;
		for (Gateway gateway : gatewayList)
			if (gateway.getId().equals(id2))
				return gateway;
		for (EndEvent endEvent : endeventList)
			if (endEvent.getId().equals(id2))
				return endEvent;
		for (StartEvent startEvent : starteventList)
			if (startEvent.getId().equals(id2))
				return startEvent;
		return null;
	}
}
