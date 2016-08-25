package cn.edu.thss.iise.xiaohan.bpcd.mtree.test;

import cn.edu.thss.iise.xiaohan.bpcd.mtree.MTree;

class WordMTree extends MTree<String> {

	public WordMTree() {
		this(MTree.DEFAULT_MIN_NODE_CAPACITY);
	}

	public WordMTree(int minNodeCapacity) {
		super(minNodeCapacity, new WordDistanceFunction(), null);
	}
}
