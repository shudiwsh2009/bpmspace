package cn.edu.thss.iise.xiaohan.bpcd.lcs;

public class HashStringIndex {
	private int start;
	private int end;

	public HashStringIndex(int start, int end) {
		this.start = start;
		this.end = end;
	}

	public boolean overlaps(HashStringIndex o) {
		return !(o.end < start || end < o.start);
	}

	public String toString() {
		return "HashStringIndex(" + start + ", " + end + ")";
	}
}
