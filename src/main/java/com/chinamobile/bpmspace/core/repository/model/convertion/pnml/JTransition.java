package com.chinamobile.bpmspace.core.repository.model.convertion.pnml;

public class JTransition {
	private String id;
	private int x;
	private int y;
	private String name;
	private int height;
	private int width;

	public JTransition() {
		name = "";
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
		this.x = 2*Math.abs(x)+50;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = 2*Math.abs(y)+50;
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

}
