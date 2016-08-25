package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

public class AuditTrailEntry {

	private String workflowModelElement;
	private String eventType;
	private String originator;
	private String timestamp;

	public AuditTrailEntry(String newWorkflowModelElement, String newEventType,
			String newTimestamp) {
		this.workflowModelElement = newWorkflowModelElement;
		this.eventType = newEventType;
		this.timestamp = newTimestamp;
	}

	public void setWorkflowModelElement(String newWorkflowModelElement) {
		this.workflowModelElement = newWorkflowModelElement;
	}

	public String getWorkflowModelElement() {
		return workflowModelElement;
	}

	public void setEventType(String newEventType) {
		this.eventType = newEventType;
	}

	public String getEventType() {
		return eventType;
	}

	public void setOriginator(String newOriginator) {
		this.originator = newOriginator;
	}

	public String getOriginator() {
		return originator;
	}

	public void setTimestamp(String newTimestamp) {
		this.timestamp = newTimestamp;
	}

	public String getTimestamp() {
		return timestamp;
	}

}
