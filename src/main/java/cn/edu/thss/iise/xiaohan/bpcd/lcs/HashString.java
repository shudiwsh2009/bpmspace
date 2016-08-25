package cn.edu.thss.iise.xiaohan.bpcd.lcs;

import java.util.LinkedList;
import java.util.StringTokenizer;

public class HashString {

	int[] numericHash;

	public HashString(String hash) {
		StringTokenizer st = new StringTokenizer(hash, ".");
		numericHash = new int[st.countTokens()];
		for (int i = 0; i < numericHash.length; i++) {
			String t = st.nextToken();
			numericHash[i] = Integer.parseInt(t);
		}
	}

	HashString(LinkedList<Integer> newHashString) {
		numericHash = new int[newHashString.size()];
		for (int i = 0; i < newHashString.size(); i++) {
			numericHash[i] = newHashString.get(i);
		}
	}

	public int length() {
		return numericHash.length;
	}

	public int charAt(int i) {
		return numericHash[i];
	}

	public String substring(int startindex, int endindex) {
		// System.out.println("substring " + this);
		String toReturn = "" + numericHash[startindex];
		startindex++;
		while (startindex < endindex) {
			toReturn += "." + numericHash[startindex];
			startindex++;
		}

		return toReturn;
	}

	public HashString remove(int startpos, int nrpos) {
		LinkedList<Integer> newHashString = new LinkedList<Integer>();
		if (startpos > 0) {
			for (int i = 0; i < startpos; i++) {
				newHashString.add(numericHash[i]);
			}
		}
		int continuepos = startpos + nrpos;
		if (continuepos < numericHash.length) {
			for (int i = continuepos; i < numericHash.length; i++) {
				newHashString.add(numericHash[i]);
			}
		}

		return new HashString(newHashString);
	}

	public String toString() {
		String toReturn = numericHash[0] + "";

		for (int i = 1; i < numericHash.length; i++) {
			toReturn += "." + numericHash[i];
		}
		return toReturn;
	}
}
