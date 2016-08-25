package cn.edu.thss.iise.xiaohan.abpcd.similarity.graphconvert;

import java.util.HashMap;

import cn.edu.thss.iise.xiaohan.abpcd.similarity.util.OPGraph;
import cn.edu.thss.iise.xiaohan.abpcd.similarity.util.OPLine;
import cn.edu.thss.iise.xiaohan.abpcd.similarity.util.OPNode;
import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;

public class GraphConvert {

	private Graph graph;
	private OPGraph opgraph;

	public GraphConvert(Graph graph) {
		this.graph = graph;
		this.opgraph = new OPGraph();
		this.opgraph = convert();
	}

	public Graph getGraph() {
		return this.graph;
	}

	public OPGraph getOPGraph() {
		return this.opgraph;
	}

	public void printGraph() {
		for (Integer v : graph.getVertices()) {
			System.out.println(v + " " + graph.getLabel(v));
		}
		System.out.println();
	}

	public void printOPGraph() {
		for (int i = 0; i < opgraph.nodes.size(); i++) {
			System.out.println(opgraph.nodes.get(i).id + " "
					+ opgraph.nodes.get(i).label);
		}
		System.out.println();
	}

	private OPGraph convert() {
		HashMap<String, OPNode> map = new HashMap<String, OPNode>();

		for (Integer v : graph.getVertices()) {
			OPNode node = new OPNode();

			node.id = v.toString();
			node.label = graph.getLabel(v.intValue());
			opgraph.nodes.add(node);
			map.put(node.id, node);
		}

		for (Edge e : graph.getEdges()) {
			OPLine line = new OPLine();
			OPNode nodep, nodec;

			line.setStart(e.getFirst().toString());
			line.setEnd(e.getSecond().toString());
			if (map.containsKey(line.getStart())
					&& map.containsKey(line.getEnd())) {
				nodep = map.get(line.getStart());
				nodec = map.get(line.getEnd());

				nodep.addChildren(nodec);
				nodec.addParents(nodep);
			}
			opgraph.lines.add(line);
		}

		// printGraph();printOPGraph();
		return opgraph;
	}

}
