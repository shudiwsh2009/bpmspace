package cn.edu.thss.iise.xiaohan.bpcd.graph;

import java.util.Set;

import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;

public interface Visitor {
	String visitRootSNode(Graph graph, Set<Edge> edges, Set<Integer> vertices,
			Integer entry, Integer exit, int nodesize);

	String visitSNode(Graph graph, Set<Edge> edges, Set<Integer> vertices,
			Integer entry, Integer exit, int nodesize);

	String visitPNode(Graph graph, Set<Edge> edges, Set<Integer> vertices,
			Integer entry, Integer exit, int nodesize);

	String visitRNode(Graph graph, Set<Edge> edges, Set<Integer> vertices,
			Integer entry, Integer exit, int nodesize);
}
