package cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc;

public class Function extends Node {

	public Function() {
	}

	public Function(String id) {
		super(id);
	}

	public Function(String id, String label) {
		super(id, label);
	}

	@Override
	public String toString() {
		return "Function(" + getId() + ", " + getName() + ")";
	}
}
