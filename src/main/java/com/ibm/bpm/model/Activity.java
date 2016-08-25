package com.ibm.bpm.model;

import java.util.ArrayList;
import java.util.List;

public class Activity extends ProcessNode {

	public Activity() {
	}

	public Activity(String name, String description, String type, String id,
			String role, String bu, String content) {
		// super();
		this.name = name;
		this.description = description;
		this.type = type;
		this.id = id;
		this.role = role; // the second department role
		this.bu = bu; // mark the first department
		this.content = content;
	}

	public static final String ACTIVITY_TYPE_TASK = "task";
	public static final String ACTIVITY_TYPE_CALLACTIVITY = "callActivity";

	// String name;
	String description;
	String type;
	// String id;
	String role;
	String bu;
	String content;
	String calledElement;
	int appearNum;
	List<String> appearProcess = new ArrayList<String>();

	// public String getName() {
	// return name;
	// }
	// public void setName(String name) {
	// this.name = name;
	// }
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	// public String getId() {
	// return id;
	// }
	// public void setId(String id) {
	// this.id = id;
	// }
	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getBu() {
		return bu;
	}

	public void setBu(String bu) {
		this.bu = bu;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getCalledElement() {
		return calledElement;
	}

	public void setCalledElement(String calledElement) {
		this.calledElement = calledElement;
	}

	public int getAppearNum() {
		return appearNum;
	}

	public void setAppearNum(int appearNum) {
		this.appearNum = appearNum;
	}

	public List<String> getAppearProcess() {
		return appearProcess;
	}

	public void setAppearProcess(List<String> appearProcess) {
		this.appearProcess = appearProcess;
	}

	public String getDisplayName() {
		if (this.getSynonymNode().size() == 0) {
			return this.name;
		} else {
			String[] strName = this.name.split(" / ");
			StringBuilder sbBU = new StringBuilder();
			sbBU.append(strName[0] + " (");
			StringBuilder sbPosition = new StringBuilder();
			sbPosition.append(strName[1] + " (");
			int i = 0;
			for (ProcessNode node : this.getSynonymNode()) {
				String[] strNodeName = node.getName().split(" / ");
				if (i > 0) {
					sbBU.append(", ");
					sbPosition.append(", ");
				}
				i++;
				sbBU.append(strNodeName[0]);
				sbPosition.append(strNodeName[1]);
			}
			sbBU.append(")");
			sbPosition.append(")");

			StringBuilder sb = new StringBuilder();
			sb.append(sbBU);
			sb.append(" / ");
			sb.append(sbPosition);
			sb.append(" / ");
			sb.append(strName[2]);
			return sb.toString();
		}
	}

	public int getDisplayAppearNum() {
		if (this.getSynonymNode().size() == 0) {
			return this.appearNum;
		} else {
			int sumAppearNum = this.appearNum;
			for (ProcessNode node : this.getSynonymNode()) {
				sumAppearNum += ((Activity) node).getAppearNum();
			}
			return sumAppearNum;
		}
	}

	public List<String> getDisplayAppearProcess() {
		if (this.getSynonymNode().size() == 0) {
			return this.appearProcess;
		} else {
			List<String> list = new ArrayList<String>();
			list.addAll(this.appearProcess);
			for (ProcessNode node : this.getSynonymNode()) {
				list.addAll(((Activity) node).getAppearProcess());
			}
			return list;
		}
	}

}
