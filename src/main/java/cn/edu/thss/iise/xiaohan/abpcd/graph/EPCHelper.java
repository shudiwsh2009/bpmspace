package cn.edu.thss.iise.xiaohan.abpcd.graph;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.Arc;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.Connector;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.EPC;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.Event;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.Function;
import cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc.Node;
import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;

public class EPCHelper implements Helper {
	private EPC epc;
	private Graph graph;
	private Map<Node, Integer> map;
	private Map<Integer, Node> rmap;
	private String graphName;
	private int maxCombinationsInRegion = 0;
	private int combinationsInTotal = 0;
	private int nrRegions = 0;

	public int getMaxCombinationsInRegion() {
		return maxCombinationsInRegion;
	}

	public void setMaxCombinationsInRegion(int maxCombinationsInRegion) {
		if (maxCombinationsInRegion > this.maxCombinationsInRegion) {
			this.maxCombinationsInRegion = maxCombinationsInRegion;
		}
	}

	public void addTotalNumberOfCombinations(int nr) {
		combinationsInTotal += nr;
	}

	@Override
	public int getTotalNumberOfCombinations() {
		return combinationsInTotal;
	}

	@Override
	public void addTotalNrOfRegions() {
		nrRegions++;
	}

	@Override
	public int getTotalNrOfRegions() {
		return nrRegions;
	}

	public EPCHelper(EPC epc) {
		this(epc, "");
	}

	public EPCHelper(EPC epc, String graphName) {
		this.graphName = graphName;
		this.epc = epc;
		map = new HashMap<Node, Integer>();
		rmap = new HashMap<Integer, Node>();
		graph = new Graph();
		for (Node n : epc.getNodes()) {
			Integer id = graph.addVertex(n.getName());
			map.put(n, id);
			rmap.put(id, n);
		}

		for (Node src : epc.getNodes()) {
			for (Node tgt : epc.getPost(src))
				// do not add self cycles
				if (map.get(src) != map.get(tgt)) {
					graph.addEdge(map.get(src), map.get(tgt));
				}
		}
	}

	public Graph getGraph() {
		return graph;
	}

	public String serialize(Collection<Integer> vertices, String code, int count) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		String fragname = String.format("Fragment_%d_%d",
				Math.abs(code.hashCode()), count);
		Graph subgraph = graph.subgraph(new HashSet<Integer>(vertices));

		PrintStream out = new PrintStream(buffer);
		out.println("digraph " + fragname + " {");

		serializeHighlighted(out, vertices);

		for (Edge edge : subgraph.getEdges())
			if (rmap.containsKey(edge.getSource())
					&& rmap.containsKey(edge.getTarget()))
				out.println("\tnode" + edge.getSource() + " -> node"
						+ edge.getTarget());

		out.println("}");

		return buffer.toString();
	}

	public String serializeInContext(Collection<Integer> vertices,
			Collection<Integer> context, String code, int count) {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();

		String fragname = String.format("Fragment_%d_%d",
				Math.abs(code.hashCode()), count);
		Graph subgraph = graph.subgraph(new HashSet<Integer>(context));

		PrintStream out = new PrintStream(buffer);
		out.println("digraph " + fragname + " {");

		serializeHighlighted(out, vertices);
		serializeNormal(out, vertices);

		for (Edge edge : subgraph.getEdges())
			if (rmap.containsKey(edge.getSource())
					&& rmap.containsKey(edge.getTarget()))
				out.println("\tnode" + edge.getSource() + " -> node"
						+ edge.getTarget());

		out.println("}");

		return buffer.toString();
	}

	private String getLabel(Integer id) {
		if (graph.getLabel(id) != null)
			return graph.getLabel(id).replaceAll("\n", " ");
		else
			return null;
	}

	private void serializeHighlighted(PrintStream out,
			Collection<Integer> vertices) {

		for (Integer id : vertices) {
			String label = getLabel(id);
			Node epcnode = rmap.get(id);
			if (epcnode == null) {
				System.out.println("oops");
				continue;
			}
			if (epc.findFunction(epcnode.getId()) != null) {
				out.println("\tnode"
						+ id
						+ " [shape=box, fillcolor=palegreen2, style=\"bold,rounded,filled\", label=\""
						+ label + "\"]");
			} else if (epc.findEvent(epcnode.getId()) != null) {
				out.print("\tnode"
						+ id
						+ " [shape=hexagon, fillcolor=lightpink1, style=\"bold,filled\", label=\""
						+ label + "\"]");
			} else {
				Connector conn = epc.findConnector(epcnode.getId());
				if (conn == null)
					continue;
				if (conn.getName().equals("AND"))
					out.print("\tnode" + id
							+ " [shape=circle, style=bold, label=\"A\"]");
				else if (conn.getName().equals("XOR"))
					out.print("\tnode" + id
							+ " [shape=circle, style=bold, label=\"X\"]");
				else
					out.print("\tnode" + id
							+ " [shape=circle, style=bold, label=\"V\"]");

			}
		}
	}

	public String getGraphName() {
		return graphName;
	}

	public boolean isGateway(Integer id) {
		boolean result = false;
		if (rmap.get(id) instanceof Connector)
			result = true;
		return result;
	}

	private void serializeNormal(PrintStream out, Collection<Integer> vertices) {

		for (Integer id : vertices) {
			String label = getLabel(id);
			Node epcnode = rmap.get(id);
			if (epcnode == null) {
				System.out.println("oops");
				continue;
			}
			if (epc.findFunction(epcnode.getId()) != null) {
				out.println("\tnode"
						+ id
						+ " [shape=box, fillcolor=palegreen2, style=\"rounded,filled\", label=\""
						+ label + "\"]");
			} else if (epc.findEvent(epcnode.getId()) != null) {
				out.print("\tnode"
						+ id
						+ " [shape=hexagon, fillcolor=lightpink1, style=\"filled\", label=\""
						+ label + "\"]");
			} else {
				Connector conn = epc.findConnector(epcnode.getId());
				if (conn == null)
					continue;
				if (conn.getName().equals("AND"))
					out.print("\tnode" + id + " [shape=circle, label=\"A\"]");
				else if (conn.getName().equals("XOR"))
					out.print("\tnode" + id + " [shape=circle, label=\"X\"]");
				else
					out.print("\tnode" + id + " [shape=circle, label=\"V\"]");

			}
		}
	}

	public static void writeModel(String outputfile, EPC g) {
		try {
			PrintWriter output = null;

			output = new PrintWriter(new FileWriter(outputfile));

			// print header
			output.println("<?xml version=\'1.0\' encoding=\'UTF-8\'?>");
			output.println("<epml:epml xmlns:epml=\'http://www.epml.de\' "
					+ "xmlns:xsi=\'http://www.w3.org/2001/XMLSchema-instance\' xsi:schemaLocation=\'http://www.epml.de EPML_2.0.xsd\'>");

			output.println("\t<epc epcId=\'1\' name=\'test\'>");

			addNodes(g.getFunctions(), output);
			addNodes(g.getEvents(), output);
			addNodes(g.getConnectors(), output);

			for (Arc e : g.getArcs()) {
				System.out.println(e.getSource().getId() + " -> "
						+ e.getTarget().getId());
				output.println("\t\t<arc id=\'" + e.getId() + "\'>");
				output.println("\t\t\t<flow source=\'" + e.getSource().getId()
						+ "\' target=\'" + e.getTarget().getId() + "\'/>");
				output.println("\t\t</arc>");
			}
			output.println("\t</epc>");
			output.println("</epml:epml>");

			output.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static void addNodes(Set<? extends Node> nodemap, PrintWriter output) {
		// vertices
		for (Node v : nodemap) {
			System.out.println(v.getId() + " - " + v.getName());
			// functions
			if (v instanceof Function) {
				output.println("\t\t<function defRef=\'" + v.getId()
						+ "\' id=\'" + v.getId() + "\'>");
				output.println("\t\t\t<name>" + wrapLabels(v.getName())
						+ "</name>");
				output.println("\t\t</function>");
			} else if (v instanceof Event) {
				output.println("\t\t<event defRef=\'" + v.getId() + "\' id=\'"
						+ v.getId() + "\'>");
				output.println("\t\t\t<name>" + wrapLabels(v.getName())
						+ "</name>");
				output.println("\t\t</event>");
			} else if (v instanceof Connector) {
				if (v.getName().startsWith("AND")) {
					output.println("\t\t<and id=\'" + v.getId() + "\'>");
					output.println("\t\t\t<name/>");
					output.println("\t\t</and>");
				} else if (v.getName().startsWith("OR")) {
					output.println("\t\t<or id=\'" + v.getId() + "\'>");
					output.println("\t\t\t<name/>");
					output.println("\t\t</or>");
				} else if (v.getName().startsWith("XOR")) {
					output.println("\t\t<xor id=\'" + v.getId() + "\'>");
					output.println("\t\t\t<name/>");
					output.println("\t\t</xor>");
				}
			}
		}
	}

	private static String wrapLabels(String s) {
		StringTokenizer st = new StringTokenizer(s, " ");
		String resultString = "";
		String currentToken = "";
		if (s.length() > 14) {
			while (st.hasMoreTokens()) {
				String next = st.nextToken();
				if (next.length() + 1 + currentToken.length() <= 14) {
					currentToken += " " + next;
				} else {
					resultString += currentToken + "\n";
					currentToken = next;
				}
			}
			if (currentToken.length() > 0) {
				resultString += currentToken + "\n";
			}

			// remove last newline
			resultString = resultString.substring(0, resultString.length() - 1);
		} else {
			return s;
		}
		return resultString;
	}

	@Override
	public void serializeToFile(String string,
			Map<Integer, Collection<Integer>> idOriginalVerticesMap, String code) {

		Collection<Integer> v = new HashSet<Integer>();
		StringTokenizer st = new StringTokenizer(string, ".");
		while (st.hasMoreElements()) {
			String next = st.nextToken();
			try {
				v.addAll(idOriginalVerticesMap.get(Integer.parseInt(next)));
			} catch (Exception e) {
			}
		}
		serializeToFile(v, code);
	}

	@Override
	public void serializeToFile(Collection<Integer> vertices, String code) {
		String fragname = "Fragment_" + code;
		Graph subgraph = graph.subgraph(new HashSet<Integer>(vertices));

		try {
			// Create file
			FileWriter fstream = new FileWriter(System.getProperty("user.dir")
					+ "\\models\\fragments\\b3\\" + fragname + ".dot");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("digraph " + fragname + " {\n");

			serializeHighlighted(out, vertices);

			for (Edge edge : subgraph.getEdges())
				if (rmap.containsKey(edge.getSource())
						&& rmap.containsKey(edge.getTarget()))
					out.write("\tnode" + edge.getSource() + " -> node"
							+ edge.getTarget());

			out.write("}");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}

	}

	private void serializeHighlighted(BufferedWriter out,
			Collection<Integer> vertices) throws IOException {
		for (Integer id : vertices) {
			String label = getLabel(id);
			Node epcnode = rmap.get(id);
			if (epcnode == null) {
				// System.out.println("oops " + id);
				continue;
			}
			if (epc.findFunction(epcnode.getId()) != null) {
				out.write("\tnode"
						+ id
						+ " [shape=box, fillcolor=palegreen2, style=\"bold,rounded,filled\", label=\""
						+ label + "\"]\n");
			} else if (epc.findEvent(epcnode.getId()) != null) {
				out.write("\tnode"
						+ id
						+ " [shape=hexagon, fillcolor=lightpink1, style=\"bold,filled\", label=\""
						+ label + "\"]");
			} else {
				Connector conn = epc.findConnector(epcnode.getId());
				if (conn == null)
					continue;

				if (conn.getName().equals("ANDjoin")
						|| conn.getName().equals("ANDsplit"))
					out.write("\tnode" + id
							+ " [shape=circle, style=bold, label=\"A\"]");
				else if (conn.getName().equals("XORjoin")
						|| conn.getName().equals("XORsplit"))
					out.write("\tnode" + id
							+ " [shape=circle, style=bold, label=\"X\"]");
				else
					out.write("\tnode" + id
							+ " [shape=circle, style=bold, label=\"V\"]");
			}
		}
	}
}
