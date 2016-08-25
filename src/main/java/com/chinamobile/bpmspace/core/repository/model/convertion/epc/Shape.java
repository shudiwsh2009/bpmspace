package com.chinamobile.bpmspace.core.repository.model.convertion.epc;

public class Shape {
	private int x;
	private int y;
	private int height;
	private int width;
	private String id;
	private String name;
	private int layer=-1;

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
		this.x = 2 * Math.abs(x);
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = 2 * Math.abs(y);
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

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
	
	public String getType(){
		return "";
	}

}
