package cn.edu.thss.iise.xiaohan.bpcd.mtree.index;

import cn.edu.thss.iise.xiaohan.bpcd.mtree.MTree;

class GraphMTree extends MTree<Fragment> {

	public GraphMTree() {
		this(MTree.DEFAULT_MIN_NODE_CAPACITY);
	}

	public GraphMTree(int minNodeCapacity) {
		super(minNodeCapacity, new GraphDistanceFunction(), null);
	}
}
