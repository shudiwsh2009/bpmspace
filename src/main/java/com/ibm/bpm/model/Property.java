package com.ibm.bpm.model;

public class Property {
	public Property() {

	}

	public Property(String onProperty_name, String onProperty_value,
			String valuesFrom_name, String valuesFrom_value) {
		super();
		this.onProperty_name = onProperty_name;
		this.onProperty_value = onProperty_value;
		ValuesFrom_name = valuesFrom_name;
		ValuesFrom_value = valuesFrom_value;
	}

	private String onProperty_name;
	private String onProperty_value;
	private String ValuesFrom_name;
	private String ValuesFrom_value;

	public String getOnProperty_name() {
		return onProperty_name;
	}

	public void setOnProperty_name(String onProperty_name) {
		this.onProperty_name = onProperty_name;
	}

	public String getOnProperty_value() {
		return onProperty_value;
	}

	public void setOnProperty_value(String onProperty_value) {
		this.onProperty_value = onProperty_value;
	}

	public String getValuesFrom_name() {
		return ValuesFrom_name;
	}

	public void setValuesFrom_name(String valuesFrom_name) {
		ValuesFrom_name = valuesFrom_name;
	}

	public String getValuesFrom_value() {
		return ValuesFrom_value;
	}

	public void setValuesFrom_value(String valuesFrom_value) {
		ValuesFrom_value = valuesFrom_value;
	}

}
