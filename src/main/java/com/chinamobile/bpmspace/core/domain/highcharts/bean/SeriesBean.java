package com.chinamobile.bpmspace.core.domain.highcharts.bean;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class SeriesBean {
	@JsonProperty("name")
	private String name;
	@JsonProperty("color")
	private String color;
	@JsonProperty("data")
	private List<Integer> data;

	public SeriesBean(String name, String color, List<Integer> data) {
		this.name = name;
		this.color = color;
		this.data = data;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public List<Integer> getData() {
		return data;
	}

	public void setData(List<Integer> data) {
		this.data = data;
	}
}
