package cn.edu.thss.iise.xiaohan.abpcd.graph;

public class QueueEntry implements Comparable<QueueEntry> {
	Integer vertex;
	String matrixLabel;
	Integer label;
	int nodeSize;

	public int getNodeSize() {
		return nodeSize;
	}

	public QueueEntry(Integer vertex, String matrixLabel, Integer label,
			int nodeSize) {
		this.vertex = vertex;
		this.matrixLabel = matrixLabel;
		this.label = label;
		this.nodeSize = nodeSize;
	}

	public QueueEntry(Integer label) {
		this.label = label;
	}

	public int compareTo(QueueEntry theother) {
		return this.label.compareTo(theother.label);
	}

	public Integer getVertex() {
		return vertex;
	}

	public Integer getLabel() {
		return label;
	}

	public String getMatrixLabel() {
		return matrixLabel;
	}

	public String toString() {
		return "(" + vertex + ", " + matrixLabel + ", " + label + ", "
				+ nodeSize + ")";
	}
}
