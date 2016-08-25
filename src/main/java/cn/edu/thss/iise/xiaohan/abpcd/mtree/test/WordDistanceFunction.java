package cn.edu.thss.iise.xiaohan.abpcd.mtree.test;

import cn.edu.thss.iise.xiaohan.abpcd.mtree.DistanceFunction;

class WordDistanceFunction implements DistanceFunction<String> {

	@Override
	public double calculate(String word1, String word2) {
		return WordDistance.wordDistance(word1, word2);
	}
}
