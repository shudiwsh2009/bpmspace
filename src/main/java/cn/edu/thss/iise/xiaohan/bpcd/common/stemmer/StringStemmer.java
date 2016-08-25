package cn.edu.thss.iise.xiaohan.bpcd.common.stemmer;

import java.util.LinkedList;
import java.util.Set;
import java.util.StringTokenizer;

public class StringStemmer {

	private static SnowballStemmer englishStemmer;
	public static String STRING_DELIMETER = " ,.:;&/?!#()";

	private static SnowballStemmer getEnglishStemmer() {
		if (englishStemmer == null) {
			englishStemmer = getStemmer("english");
		}

		return englishStemmer;
	}

	private static SnowballStemmer getStemmer(String language) {
		@SuppressWarnings("rawtypes")
		Class stemClass;
		SnowballStemmer stemmer;

		try {
			stemClass = Class.forName("common.stemmer.englishStemmer");
			stemmer = (SnowballStemmer) stemClass.newInstance();

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		return stemmer;
	}

	public static String getStemmedString(String a) {

		LinkedList<String> aTokens = new LinkedList<String>();

		StringTokenizer tokensA = new StringTokenizer(a, STRING_DELIMETER);
		while (tokensA.hasMoreTokens()) {
			String aToken = tokensA.nextToken();
			aTokens.add(aToken);
		}
		aTokens = removeStopWordsAndStem(aTokens, getEnglishStemmer());

		String result = "";
		for (String aT : aTokens) {
			result += aT + " ";
		}
		return result;
	}

	private static LinkedList<String> removeStopWordsAndStem(
			LinkedList<String> toRemove, SnowballStemmer stemmer) {

		LinkedList<String> result = new LinkedList<String>();
		Set<String> stopWords = stemmer.getStopWords();
		int repeat = 1;

		for (String s : toRemove) {
			s = s.toLowerCase();
			if (s.length() <= 2) {
				result.add(s);
			} else if (s.length() > 2
					&& (!stemmer.hasStopWords() || stemmer.hasStopWords()
							&& !stopWords.contains(s))) {
				stemmer.setCurrent(s);
				for (int i = repeat; i != 0; i--) {
					stemmer.stem();
				}
				String stemmedString = stemmer.getCurrent();
				result.add(stemmedString);
			}
		}
		return result;
	}
}
