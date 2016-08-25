package com.ibm.bpm.model;

public class OWLClass {

	public OWLClass(String rdf, Property[] prop) {
		super();
		this.rdf = rdf;
		this.prop = prop;
	}

	public OWLClass() {

	}

	private String rdf;
	private Property[] prop;

	public String getRdf() {
		return rdf;
	}

	public void setRef(String rdf) {
		this.rdf = rdf;
	}

	public Property[] getProp() {
		return prop;
	}

	public void setProp(Property[] prop) {
		this.prop = prop;
	}

	public void setRdf(String rdf) {
		this.rdf = rdf;
	}

}
