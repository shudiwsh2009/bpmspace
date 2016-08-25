package com.chinamobile.bpmspace.core.repository.model.convertion.bpmn;

public class Line {
	private String id;
	private String name;
	private String toId;
	private String fromId;
	private String shapeType;
	
	public String getShapeType(){
		return shapeType;
	}
	
	public void setShapeType(String shapeType){
		this.shapeType = shapeType;
	}


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getToId() {
		return toId;
	}

	public void setToId(String toId) {
		this.toId = toId;
	}

	public String getFromId() {
		return fromId;
	}

	public void setFromId(String fromId) {
		this.fromId = fromId;
	}
}
