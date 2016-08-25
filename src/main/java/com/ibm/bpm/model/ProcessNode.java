package com.ibm.bpm.model;

import java.util.ArrayList;
import java.util.List;

public abstract class ProcessNode {

	String id;
	String name;
	String processName;
	List<ProcessNode> synonymNode;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<ProcessNode> getSynonymNode() {
		if (synonymNode == null) {
			synonymNode = new ArrayList<ProcessNode>();
		}
		return synonymNode;
	}

	public void setSynonymNode(List<ProcessNode> synonymNode) {
		this.synonymNode = synonymNode;
	}

	public String getProcessName() {
		return processName;
	}

	public void setProcessName(String processName) {
		this.processName = processName;
	}

}
