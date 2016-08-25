package cn.edu.thss.iise.xiaohan.abpcd.console;

import java.util.HashSet;

public class DagNode {

	private HashSet<Integer> parents = new HashSet<Integer>();

	public void addParent(Integer parent) {
		parents.add(parent);
	}

	public HashSet<Integer> getParents() {
		return parents;
	}
}
