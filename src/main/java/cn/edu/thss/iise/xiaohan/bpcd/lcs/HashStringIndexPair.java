package cn.edu.thss.iise.xiaohan.bpcd.lcs;

public class HashStringIndexPair {
	private HashStringIndex s1Index;
	private HashStringIndex s2Index;

	public HashStringIndexPair(HashStringIndex s1Index, HashStringIndex s2Index) {
		this.s1Index = s1Index;
		this.s2Index = s2Index;
	}

	public boolean overlaps(HashStringIndexPair pair) {
		return s1Index.overlaps(pair.s1Index) || s2Index.overlaps(pair.s2Index);
	}

	public String toString() {
		return "HashStringIndexPair(" + s1Index + ", " + s2Index + ")";
	}
}
