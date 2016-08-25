package cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc;

public class Node {

	private String id = "";
	private String name = "";

	public Node() {
	}

	public Node(String id) {
		this.id = id;
	}

	public Node(String id, String label) {
		this.id = id;
		this.name = label;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object arg0) {
		if (arg0.getClass().getSimpleName()
				.equals(this.getClass().getSimpleName())) {
			return id.equals(((Node) arg0).getId());
		} else {
			return false;
		}
	}

	// public int hashCode() {
	// return Integer.parseInt(id);
	// }
}
