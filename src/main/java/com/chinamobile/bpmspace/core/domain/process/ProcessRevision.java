package com.chinamobile.bpmspace.core.domain.process;

import java.util.Date;

public class ProcessRevision {
	private String modelId = "";
	private String creatorId = "";
	private Date createTime = new Date();

	public ProcessRevision() {

	}

	public ProcessRevision(String _modelId, String _creatorId, Date _createTime) {
		modelId = _modelId;
		creatorId = _creatorId;
		createTime = _createTime;
	}

	public String getModelId() {
		return modelId;
	}

	public void setModelId(String modelId) {
		this.modelId = modelId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

}
