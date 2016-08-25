package com.chinamobile.bpmspace.core.repository.model.convertion.pnml;

public class JPlace {
	private String id;
	private int x;
	private int y;
	private int r;
	private String name;

	public JPlace() {
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

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = r;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
