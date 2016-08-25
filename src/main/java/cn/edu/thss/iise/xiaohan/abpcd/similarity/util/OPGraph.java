package cn.edu.thss.iise.xiaohan.abpcd.similarity.util;

import java.util.ArrayList;
import java.util.List;

public class OPGraph {

	public List<OPLine> lines;
	public List<OPNode> nodes;
	public OPNode root;

	public OPGraph() {
		lines = new ArrayList<OPLine>();
		nodes = new ArrayList<OPNode>();
		root = null;
	}

	public int getLabelNumber(String label) {
		int number = 0;
		for (OPNode node : nodes) {
			if (node.label.equals(label))
				number++;
		}
		return number;
	}

	public ArrayList<String> getLabelList() {
		ArrayList<String> labels = new ArrayList<String>();
		for (OPNode node : nodes) {
			labels.add(node.label);
		}
		return labels;
	}
}
