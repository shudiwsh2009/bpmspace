package com.ibm.bpm.model;

public class FlowNodeRef {
	private ProcessNode node;

	public FlowNodeRef() {
	}

	public FlowNodeRef(ProcessNode node) {
		super();
		this.node = node;
	}

	public ProcessNode getNode() {
		return node;
	}

	public void setNode(ProcessNode node) {
		this.node = node;
	}

}
