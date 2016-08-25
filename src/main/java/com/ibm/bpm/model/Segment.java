package com.ibm.bpm.model;

import java.util.ArrayList;
import java.util.List;

public class Segment extends Process {

	List<Flow> inNodeList = new ArrayList<Flow>();
	List<Flow> outNodeList = new ArrayList<Flow>();

	public Segment() {
	}

	public Segment(List<Flow> inNodeList, List<Flow> outNodeList) {
		this.inNodeList = inNodeList;
		this.outNodeList = outNodeList;
	}

	public List<Flow> getInNodeList() {
		return inNodeList;
	}

	public void setInNodeList(List<Flow> inNodeList) {
		this.inNodeList = inNodeList;
	}

	public List<Flow> getOutNodeList() {
		return outNodeList;
	}

	public void setOutNodeList(List<Flow> outNodeList) {
		this.outNodeList = outNodeList;
	}

	// List<Activity> inAcitivityList = new ArrayList<Activity>();
	// List<Activity> outAcitivityList = new ArrayList<Activity>();
	// List<Gateway> outGatewayList = new ArrayList<Gateway>();
	// List<Gateway> inGatewayList = new ArrayList<Gateway>();

}
