package com.chinamobile.bpmspace.core.domain.index;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "indexdescriptor")
public class IndexDescriptor {
	@Id
	private String id;
	private String class_name;
	private String description;
	private IndexState state;
	private IndexCategory category;
	private String type; // determine to index which type of object
	private String supportedQueryType;

	public IndexDescriptor() {
		this.state = IndexState.UNAUTHORIZED;
	}

	public String getId() {
		return id;
	}

	public String getClass_name() {
		return class_name;
	}

	public void setClass_name(String class_name) {
		this.class_name = class_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public IndexState getState() {
		return state;
	}

	public void setState(IndexState state) {
		this.state = state;
	}

	public IndexCategory getCategory() {
		return category;
	}

	public void setCategory(IndexCategory category) {
		this.category = category;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSupportedQueryType() {
		return supportedQueryType;
	}

	public void setSupportedQueryType(String supportedQueryType) {
		this.supportedQueryType = supportedQueryType;
	}

}
