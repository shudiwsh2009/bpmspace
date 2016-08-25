package com.chinamobile.bpmspace.core.repository.index;

public class ProcessQueryResult implements Comparable<ProcessQueryResult> {

	private String process_id;
	private float similarity = 1;

	public ProcessQueryResult(String process_id, float similarity) {
		this.process_id = process_id;
		this.similarity = similarity;
	}

	@Override
	public int compareTo(ProcessQueryResult o) {
		if (this.similarity > o.similarity) {
			return 1;
		} else if (this.similarity < o.similarity) {
			return -1;
		} else {
			return 0;
		}
	}

	public String getProcess_id() {
		return process_id;
	}

	public void setProcess_id(String process_id) {
		this.process_id = process_id;
	}

}
