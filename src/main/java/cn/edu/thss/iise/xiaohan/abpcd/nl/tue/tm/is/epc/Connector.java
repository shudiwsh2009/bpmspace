package cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc;

public class Connector extends Node {

	public static final String ANDLabel = "AND";
	public static final String ORLabel = "OR";
	public static final String XORLabel = "XOR";

	public Connector() {
	}

	public Connector(String id) {
		super(id);
	}

	public Connector(String id, String label) {
		super(id, label);
	}

	@Override
	public String toString() {
		return "Connector(" + getId() + ", " + getName() + ")";
	}
}
