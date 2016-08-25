package cn.edu.thss.iise.xiaohan.bpcd.lcs;

import java.util.HashSet;

public class HashSubstring {
	private String substring;
	private int count;
	private HashSet<HashStringIndexPair> occ = new HashSet<HashStringIndexPair>();

	public HashSubstring(String substring, int count, HashStringIndex occS1ndx,
			HashStringIndex occS2ndx) {
		this.substring = substring;
		this.count = count;
		occ.add(new HashStringIndexPair(occS1ndx, occS2ndx));
	}

	public String getSubstring() {
		return substring;
	}

	public int getCount() {
		return count;
	}

	public HashSet<HashStringIndexPair> getOcc() {
		return occ;
	}

	public void addCount() {
		count++;
	}

	public void addOccurrence(HashStringIndex occS1ndx, HashStringIndex occS2ndx) {
		occ.add(new HashStringIndexPair(occS1ndx, occS2ndx));
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof HashSubstring) {
			return ((HashSubstring) o).getSubstring().equals(getSubstring());
		}
		return false;
	}

	@Override
	public int hashCode() {
		return getSubstring().hashCode();
	}

	public String toString() {
		String toreturn = "HashSubstring(" + substring + ", " + count
				+ ", \n\t occS1(";

		for (HashStringIndexPair i : occ) {

			toreturn += i + ", ";
		}
		toreturn += ")";
		return toreturn;
	}
}
