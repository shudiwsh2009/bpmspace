package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.util.ArrayList;

public class ProcessInstance {

	/**
	 * @param args
	 */

	private String id;
	private ArrayList<AuditTrailEntry> auditTrailEntries;

	public ProcessInstance(String newID,
			ArrayList<AuditTrailEntry> newAuditTrailEntries) {
		this.id = newID;
		this.auditTrailEntries = newAuditTrailEntries;
	}

	public void setID(String newID) {
		this.id = newID;
	}

	public String getID() {
		return this.id;
	}

	public void setAuditTrailEntries(
			ArrayList<AuditTrailEntry> newAuditTrailEntries) {
		this.auditTrailEntries = newAuditTrailEntries;
	}

	public ArrayList<AuditTrailEntry> getAuditTrailEntries() {
		return this.auditTrailEntries;
	}

}
