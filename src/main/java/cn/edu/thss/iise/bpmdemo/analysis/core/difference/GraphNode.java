package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.util.ArrayList;
import java.util.List;

public class GraphNode {

	/**
	 * @author �ν��
	 */
	public String type;// startevent,endevent,activity,xor,and
	public String id;
	public String label;
	public List<GraphNode> children;
	public List<GraphNode> parents;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<GraphNode> getChildren() {
		return children;
	}

	public void setChildren(List<GraphNode> children) {
		this.children = children;
	}

	public List<GraphNode> getParent() {
		return parents;
	}

	public void setParent(List<GraphNode> parents) {
		this.parents = parents;
	}

	public void addChildren(GraphNode child) {
		if (this.children == null) {
			List<GraphNode> list = new ArrayList<GraphNode>();
			list.add(child);
			this.setChildren(list);
		} else {
			this.children.add(child);
		}
	}

	public void addParents(GraphNode parent) {
		if (this.parents == null) {
			List<GraphNode> list = new ArrayList<GraphNode>();
			list.add(parent);
			this.setParent(list);
		} else {
			this.parents.add(parent);
		}
	}

	public GraphNode findChild(String name) {
		List<GraphNode> children = this.getChildren();
		if (children != null) {
			for (GraphNode child : children) {
				if (child.getId().equals(name)) {
					return child;
				}
			}
		}
		return null;
	}

	public void printChildName() {
		List<GraphNode> children = this.getChildren();
		if (children != null) {
			for (GraphNode child : children) {
				System.out.print(child.id);
			}
		} else {
			System.out.println("no child");
		}
	}

}
