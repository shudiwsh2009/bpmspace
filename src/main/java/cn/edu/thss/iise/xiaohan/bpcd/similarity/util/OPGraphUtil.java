package cn.edu.thss.iise.xiaohan.bpcd.similarity.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.jbpt.graph.DirectedEdge;
import org.jbpt.graph.Fragment;
import org.jbpt.hypergraph.abs.Vertex;

public class OPGraphUtil {

	public static OPGraph Fragment2Graph(Fragment fragment) {
		OPGraph graph = new OPGraph();
		List<OPLine> lines = new ArrayList<OPLine>();
		LinkedList<OPNode> gnodes = new LinkedList<OPNode>();
		@SuppressWarnings("rawtypes")
		Iterator iterator = fragment.iterator();
		HashSet<Vertex> set = new HashSet<Vertex>();
		HashMap<String, OPNode> map = new HashMap<String, OPNode>();

		// add nodes
		while (iterator.hasNext()) {
			DirectedEdge directedEdge = (DirectedEdge) iterator.next();
			Vertex src = directedEdge.getSource();
			Vertex tgt = directedEdge.getTarget();
			if (!set.contains(src)) {
				OPNode node = new OPNode();
				node.id = src.getName();
				node.label = src.getName();
				gnodes.add(node);
				set.add(src);
				map.put(node.id, node);
			}
			if (!set.contains(tgt)) {
				OPNode node = new OPNode();
				node.id = tgt.getName();
				node.label = tgt.getName();
				gnodes.add(node);
				set.add(tgt);
				map.put(node.id, node);
			}
		}

		// add lines
		iterator = fragment.iterator();
		while (iterator.hasNext()) {
			DirectedEdge directedEdge = (DirectedEdge) iterator.next();
			Vertex src = directedEdge.getSource();
			Vertex tgt = directedEdge.getTarget();
			OPNode nodep, nodec;
			OPLine line = new OPLine();

			line.start = src.getName();
			line.end = tgt.getName();
			if (map.containsKey(line.start) && map.containsKey(line.end)) {
				nodep = map.get(line.start);
				nodec = map.get(line.end);

				nodep.addChildren(nodec);
				nodec.addParents(nodep);
			}
		}

		graph.lines = lines;
		graph.nodes = gnodes;
		graph.root = gnodes.peek();
		return graph;
	}
}
