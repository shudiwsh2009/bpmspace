package com.chinamobile.bpmspace.core.domain.process;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "model")
public class Model implements Cloneable {
	@Id
	private String id;
	@Indexed
	private String processId = "";
	private String creatorId = "";
	private String creatorName = "";
	private Date createTime = new Date();
	private long revision = 0;
	private String jsonFilename = "";
	private String svgFilename = "";
	private String xmlFilename = "";
	private double size = 0;

	public Model() {

	}

	public Model(String _processId, String _creatorId, String _creatorName,
			long _revision, String _jsonFilename, String _svgFilename,
			String _xmlFilename, double _size) {
		processId = _processId;
		creatorId = _creatorId;
		creatorName = _creatorName;
		revision = _revision;
		jsonFilename = _jsonFilename;
		svgFilename = _svgFilename;
		xmlFilename = _xmlFilename;
		size = _size;
	}

	@Override
	public Model clone() {
		Model model = null;
		try {
			model = (Model) super.clone();
			model.setId(null);
			model.setCreateTime((Date) createTime.clone());
		} catch (CloneNotSupportedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return model;
	}

	@Override
	public String toString() {
		return String.format("Model[id=%s, processId='%s', creatorId=%s, "
				+ "creatorName='%s', revision=%s, size=%s]", id, processId,
				creatorId, creatorName, revision, size);
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

	public String getProcessId() {
		return processId;
	}

	public void setProcessId(String processId) {
		this.processId = processId;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public String getCreatorName() {
		return creatorName;
	}

	public void setCreatorName(String creatorName) {
		this.creatorName = creatorName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public long getRevision() {
		return revision;
	}

	public void setRevision(long revision) {
		this.revision = revision;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public String getJsonFilename() {
		return jsonFilename;
	}

	public void setJsonFilename(String jsonFilename) {
		this.jsonFilename = jsonFilename;
	}

	public String getSvgFilename() {
		return svgFilename;
	}

	public void setSvgFilename(String svgFilename) {
		this.svgFilename = svgFilename;
	}

	public String getXmlFilename() {
		return xmlFilename;
	}

	public void setXmlFilename(String xmlFilename) {
		this.xmlFilename = xmlFilename;
	}
}
