package cn.edu.thss.iise.xiaohan.bpcd.graph;

import java.util.HashMap;
import java.util.Map;

import cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc.EPC;
import cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc.Node;
import de.bpt.hpi.graph.Graph;

public class EPCGraphFactory {
	/**
	 * Initializes a simple graph from an EPC.
	 * 
	 */
	public Graph createGraph(EPC epc) {
		Map<Node, Integer> map = new HashMap<Node, Integer>();
		Graph graph = new Graph();
		for (Node n : epc.getNodes()) {
			Integer id = graph.addVertex(n.getName());
			map.put(n, id);
		}

		for (Node src : epc.getNodes()) {
			for (Node tgt : epc.getPost(src))
				// do not add self cycles
				if (map.get(src) != map.get(tgt)) {
					graph.addEdge(map.get(src), map.get(tgt));
				}
		}

		return graph;
	}

}
