package cn.edu.thss.iise.xiaohan.abpcd.graph;

public class MatrixQueueEntry extends QueueEntry {

	public MatrixQueueEntry(Integer vertex, String matrixLabel, Integer label,
			int nodesize) {
		super(vertex, matrixLabel, label, nodesize);
	}

	public int compareTo(MatrixQueueEntry theother) {
		return this.matrixLabel.compareTo(theother.matrixLabel);
	}
}
