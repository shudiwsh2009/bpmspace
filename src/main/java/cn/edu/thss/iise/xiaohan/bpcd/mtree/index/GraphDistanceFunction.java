package cn.edu.thss.iise.xiaohan.bpcd.mtree.index;

import cn.edu.thss.iise.xiaohan.bpcd.mtree.DistanceFunction;

class GraphDistanceFunction implements DistanceFunction<Fragment> {

	@Override
	public double calculate(Fragment fragment1, Fragment fragment2) {
		return GraphDistance.graphDistance(fragment1, fragment2);
	}
}
