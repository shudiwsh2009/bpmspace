package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

public class Line {
	/**
	 * @author �ν��
	 */
	String start;
	String end;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String toString() {
		// System.out.println();
		return "[" + start + "-->" + end + "], ";
	}

}
