package com.ibm.bpm.model;

import java.util.ArrayList;
import java.util.List;

public class Structure {
	StartEvent se;
	Activity activity; // record activity
	List<Flow> flowList; // record flow
	Gateway gateway; // record gateway
	List<String> label;

	public List<Flow> getFlowList() {
		return flowList;
	}

	public void setFlowList(List<Flow> flowList) {
		this.flowList = flowList;
	}

	public Activity getActivity() {
		return activity;
	}

	public void setActivity(Activity activity) {
		this.activity = activity;
	}

	public StartEvent getSe() {
		return se;
	}

	public void setSe(StartEvent se) {
		this.se = se;
	}

	public Gateway getGateway() {
		return gateway;
	}

	public void setGateway(Gateway gateway) {
		this.gateway = gateway;
	}

	public List<String> getLabel() {
		return label;
	}

	public void setLabel(List<String> label) {
		this.label = label;
	}

	public boolean Check(List<String> label) {
		for (int i = 0; i < label.size(); i++) {
			for (int j = i + 1; j < label.size(); j++) {
				if (label.get(i).equals(label.get(j))) {
					return true;
				}
			}
		}
		return false;
	}

	public List<String> GetString(List<String> label) {
		List<String> string = new ArrayList<String>();
		for (int i = 0; i < label.size(); i++) {
			for (int j = i + 1; j < label.size(); j++) {
				if (label.get(i).equals(label.get(j))) {
					string.add(label.get(i));

				}
			}
		}
		return string;
	}

}
