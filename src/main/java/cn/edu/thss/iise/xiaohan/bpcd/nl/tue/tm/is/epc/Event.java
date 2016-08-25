package cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc;

public class Event extends Node {

	public Event() {
	}

	public Event(String id) {
		super(id);
	}

	public Event(String id, String label) {
		super(id, label);
	}

	@Override
	public String toString() {
		return "Event(" + getId() + ", " + getName() + ")";
	}
}
