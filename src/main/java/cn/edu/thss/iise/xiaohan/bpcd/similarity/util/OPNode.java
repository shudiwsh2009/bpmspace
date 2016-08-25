package cn.edu.thss.iise.xiaohan.bpcd.similarity.util;

import java.util.ArrayList;
import java.util.List;

public class OPNode {

	/**
	 * @author ËÎ½ð·ï
	 */
	// public String type;//startevent,endevent,activity,xor,and
	public String id;
	public String label;
	public List<OPNode> children = new ArrayList<OPNode>();;
	public List<OPNode> parents = new ArrayList<OPNode>();;

	public OPNode() {

	}

	public OPNode(OPNode node) {
		this.id = node.id;
		this.label = node.label;
		this.children = node.children;
		this.parents = node.parents;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<OPNode> getChildren() {
		return children;
	}

	public void setChildren(List<OPNode> children) {
		this.children = children;
	}

	public List<OPNode> getParent() {
		return parents;
	}

	public void setParent(List<OPNode> parents) {
		this.parents = parents;
	}

	public void addChildren(OPNode child) {
		if (this.children == null) {
			List<OPNode> list = new ArrayList<OPNode>();
			list.add(child);
			this.setChildren(list);
		} else {
			this.children.add(child);
		}
	}

	public void addParents(OPNode parent) {
		if (this.parents == null) {
			List<OPNode> list = new ArrayList<OPNode>();
			list.add(parent);
			this.setParent(list);
		} else {
			this.parents.add(parent);
		}
	}

	public OPNode findChild(String name) {
		List<OPNode> children = this.getChildren();
		if (children != null) {
			for (OPNode child : children) {
				if (child.getId().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	public void printChildName() {
		List<OPNode> children = this.getChildren();
		if (children != null) {
			for (OPNode child : children) {
				System.out.print(child.id);
			}
		} else {
			System.out.println("no child");
		}
	}
}
