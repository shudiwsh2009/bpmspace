package com.chinamobile.bpmspace.core.repository.model.convertion.bpmn;

public class Shape {
	private int x;
	private int y;
	private int height;
	private int width;
	private String id;
	private String name;
	private String shapeType;
	private OriginalType originalType;
	private int layer = -1;
	
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

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = Math.abs(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = Math.abs(y);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public OriginalType getOriginalType() {
		return originalType;
	}

	public void setOriginalType(OriginalType rec) {
		this.originalType = rec;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
}
