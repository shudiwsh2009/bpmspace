package cn.edu.thss.iise.xiaohan.bpcd.similarity.util;

public class OPLine {
	/**
	 * @author ËÎ½ð·ï
	 */
	public String start;
	public String end;

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
