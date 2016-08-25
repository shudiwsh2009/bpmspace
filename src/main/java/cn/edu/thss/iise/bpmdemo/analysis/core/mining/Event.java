package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

public class Event {

	private String id; // �¼�id
	private String identifier; // ������
	private String beginTime; // ����ʱ��
	private String endTime; // ����ʱ��
	private String activityStatus; // �״̬
	private String performer; // ������
	private String route; // �ύ·��
	private String nextTask; // ����������

	public void setId(String newId) {
		this.id = newId;
	}

	public String getId() {
		return this.id;
	}

	public void setIdentifier(String newIdentifier) {
		this.identifier = newIdentifier;
	}

	public String getIdentifier() {
		return this.identifier;
	}

	public void setBeginTime(String newBeginTime) {
		this.beginTime = newBeginTime;
	}

	public String getBeginTime() {
		return this.beginTime;
	}

	public void setEndTime(String newEndTime) {
		this.endTime = newEndTime;
	}

	public String getEndTime() {
		return this.endTime;
	}

	public void setActivityStatus(String newActivityStatus) {
		this.activityStatus = newActivityStatus;
	}

	public String getActivityStatus() {
		return this.activityStatus;
	}

	public void setPerformer(String newPerformer) {
		this.performer = newPerformer;
	}

	public String getPerformer() {
		return this.performer;
	}

	public void setRoute(String newRoute) {
		this.route = newRoute;
	}

	public String getRoute() {
		return this.route;
	}

	public void setNextTask(String newNextTask) {
		this.nextTask = newNextTask;
	}

	public String getNextTask() {
		return this.nextTask;
	}

}
