package com.ibm.bpm.model;

public class Flow {

	String name;
	String id;
	ProcessNode srcNode;
	ProcessNode targetNode;

	public Flow() {

	}

	public Flow(String name, String id, ProcessNode srcNode,
			ProcessNode targetNode) {
		super();
		this.name = name;
		this.id = id;
		this.srcNode = srcNode;
		this.targetNode = targetNode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ProcessNode getSrcNode() {
		return srcNode;
	}

	public void setSrcNode(ProcessNode srcNode) {
		this.srcNode = srcNode;
	}

	public ProcessNode getTargetNode() {
		return targetNode;
	}

	public void setTargetNode(ProcessNode targetNode) {
		this.targetNode = targetNode;
	}
}
