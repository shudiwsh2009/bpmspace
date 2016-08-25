package cn.edu.thss.iise.xiaohan.bpcd.lcs;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

public class HashLCS {

	public static LinkedList<String> findAllNotOverlappingSubstrings(
			HashString str1, HashString str2) {
		return findAllNotOverlappingSubstrings(longestCommonSubString1(str1,
				str2));
	}

	private static LinkedList<String> findAllNotOverlappingSubstrings(
			TreeMap<Integer, HashMap<String, HashSubstring>> longestCommonSubString1) {

		LinkedList<String> toReturn = new LinkedList<String>();

		while (true) {

			if (longestCommonSubString1.size() == 0) {
				break;
			}

			int occ = 0;
			HashSubstring substring = null;
			// longest substring
			for (HashSubstring substrings : longestCommonSubString1.lastEntry()
					.getValue().values()) {
				if (substrings.getCount() > occ) {
					substring = substrings;
					occ = substring.getCount();
				}
			}
			// most commonly occurred substring found
			toReturn.add(substring.getSubstring());
			longestCommonSubString1.lastEntry().getValue()
					.remove(substring.getSubstring());
			if (longestCommonSubString1.lastEntry().getValue().size() == 0) {
				longestCommonSubString1.remove(longestCommonSubString1
						.lastEntry().getKey());
			}

			Set<Entry<Integer, HashMap<String, HashSubstring>>> substrings = longestCommonSubString1
					.entrySet();
			Iterator<Entry<Integer, HashMap<String, HashSubstring>>> i = substrings
					.iterator();
			while (i.hasNext()) {
				Map.Entry<Integer, HashMap<String, HashSubstring>> currentEntry = (Map.Entry<Integer, HashMap<String, HashSubstring>>) i
						.next();
				Set<Entry<String, HashSubstring>> entries = currentEntry
						.getValue().entrySet();
				Iterator<Entry<String, HashSubstring>> it = entries.iterator();
				while (it.hasNext()) {
					Map.Entry<String, HashSubstring> entry = (Map.Entry<String, HashSubstring>) it
							.next();

					// all overlaps are removed
					for (HashStringIndexPair occX : substring.getOcc()) {
						Iterator<HashStringIndexPair> i2 = entry.getValue()
								.getOcc().iterator();
						while (i2.hasNext()) {
							if (i2.next().overlaps(occX)) {
								i2.remove();
							}
						}
					}
					if (entry.getValue().getOcc().size() == 0) {
						it.remove();
					}
				}
				if (currentEntry.getValue().entrySet().size() == 0) {
					i.remove();
				}
			}
		}
		return toReturn;
	}

	private static TreeMap<Integer, HashMap<String, HashSubstring>> longestCommonSubString1(
			HashString str1, HashString str2) {
		TreeMap<Integer, HashMap<String, HashSubstring>> substrings = new TreeMap<Integer, HashMap<String, HashSubstring>>();

		if (str1 == null || str1.length() == 0 || str2 == null
				|| str2.length() == 0)
			return substrings;

		int[][] num = new int[str1.length()][str2.length()];

		for (int i = 0; i < str1.length(); i++) {
			for (int j = 0; j < str2.length(); j++) {
				if (str1.charAt(i) != str2.charAt(j)) {
					num[i][j] = 0;
				} else {
					if ((i == 0) || (j == 0)) {
						num[i][j] = 1;
					} else {
						num[i][j] = 1 + num[i - 1][j - 1];
					}
				}
				// we have found the substring
				if ((i == str1.length() - 1 && num[i][j] > 1)
						|| (j == str2.length() - 1 && num[i][j] > 1)) {
					addSubstringToMap(str1, substrings, num, i + 1, j + 1);

				} else if (i > 0 && j > 0 && num[i][j] == 0
						&& num[i - 1][j - 1] > 1) {
					addSubstringToMap(str1, substrings, num, i, j);
				}
			}
		}
		return substrings;
	}

	private static void addSubstringToMap(HashString str1,
			TreeMap<Integer, HashMap<String, HashSubstring>> substrings,
			int[][] num, int i, int j) {
		int substrLen = num[i - 1][j - 1];
		HashMap<String, HashSubstring> set = substrings.get(substrLen);
		String toAdd = str1.substring(i - substrLen, i);
		// we have no substring with this length
		if (set == null) {
			set = new HashMap<String, HashSubstring>();
			HashSubstring substring = new HashSubstring(toAdd, 1,
					new HashStringIndex(i - substrLen, i - 1),
					new HashStringIndex(j - substrLen, j - 1));
			set.put(toAdd, substring);
			substrings.put(substrLen, set);
		} else if (set.containsKey(toAdd)) {
			HashSubstring substring = set.get(toAdd);
			substring.addCount();
			substring.addOccurrence(new HashStringIndex(i - substrLen, i - 1),
					new HashStringIndex(j - substrLen, j - 1));
		} else {
			HashSubstring substring = new HashSubstring(toAdd, 1,
					new HashStringIndex(i - substrLen, i - 1),
					new HashStringIndex(j - substrLen, j - 1));
			set.put(toAdd, substring);
		}
	}
}
