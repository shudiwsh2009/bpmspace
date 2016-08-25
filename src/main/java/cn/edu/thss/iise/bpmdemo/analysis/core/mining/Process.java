package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.util.ArrayList;

public class Process {

	private String id;
	private String description;
	private ArrayList<ProcessInstance> processInstances;

	public Process(String newID, String newDescription,
			ArrayList<ProcessInstance> newProcessInstances) {
		this.id = newID;
		this.description = newDescription;
		this.processInstances = newProcessInstances;
	}

	public void setID(String newID) {
		this.id = newID;
	}

	public String getID() {
		return this.id;
	}

	public void setDescription(String newDescription) {
		this.description = newDescription;
	}

	public String getDescription() {
		return this.description;
	}

	public void setProcessInstances(
			ArrayList<ProcessInstance> newProcessInstances) {
		this.processInstances = newProcessInstances;
	}

	public ArrayList<ProcessInstance> getProcessInstances() {
		return this.processInstances;
	}

}
