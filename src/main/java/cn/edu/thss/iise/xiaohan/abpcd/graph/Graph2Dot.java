package cn.edu.thss.iise.xiaohan.abpcd.graph;

import java.io.PrintStream;
import java.util.Set;

import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;

public class Graph2Dot {
	public static void print(Graph graph, PrintStream out) {
		out.println("digraph G {");
		for (Edge e : graph.getEdges())
			try {
				out.printf("\t%s -> %s;\n", graph.getLabel(e.getSource()),
						graph.getLabel(e.getTarget()));
			} catch (Exception e2) {
				System.out.println(graph.getLabel(e.getSource()) + "  "
						+ e.getTarget());
			}
		out.println("}");
	}

	public static void print(Graph graph, Set<Edge> edges, PrintStream out) {
		out.println("digraph G {");
		for (Edge e : edges)
			try {
				out.printf("\t%s -> %s;\n", graph.getLabel(e.getSource()),
						graph.getLabel(e.getTarget()));
			} catch (Exception e2) {
				System.out.println(graph.getLabel(e.getSource()) + "  "
						+ e.getTarget());
			}
		out.println("}");
	}

	public static void print(Graph graph, Set<Integer> vertices,
			Set<Edge> edges, PrintStream out) {
		out.println("digraph G {");
		for (Edge e : edges)
			out.printf("\tn%s -> n%s;\n", e.getSource(), e.getTarget());
		out.println("}");

	}
}
