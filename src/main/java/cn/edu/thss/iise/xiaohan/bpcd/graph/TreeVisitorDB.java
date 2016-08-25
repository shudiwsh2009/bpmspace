package cn.edu.thss.iise.xiaohan.bpcd.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag.db.DBHelperDb;
import cn.edu.thss.iise.xiaohan.bpcd.graph.util.SortedListPermutationGenerator;
import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;
import de.bpt.hpi.graph.Pair;

public class TreeVisitorDB implements Visitor {
	private boolean insertingMode = true;

	public void setInsertingMode(boolean insertingMode) {
		this.insertingMode = insertingMode;
	}

	Helper helper;
	DBHelperDb DBHelperDb;

	public TreeVisitorDB(Helper helper, DBHelperDb DBHelperDb) {
		this.helper = helper;
		this.DBHelperDb = DBHelperDb;
	}

	public TreeVisitorDB(Helper helper, DBHelperDb DBHelperDb,
			boolean insertingMode) {
		this.helper = helper;
		this.DBHelperDb = DBHelperDb;
		this.insertingMode = insertingMode;
	}

	public String visitPNode(Graph graph, Set<Edge> edges,
			Set<Integer> vertices, Integer entry, Integer exit, int nodesize) {

		LinkedList<Integer> children = new LinkedList<Integer>();
		LinkedList<QueueEntry> sortedLabels = new LinkedList<QueueEntry>();

		for (Integer v : vertices) {
			sortedLabels.add(new QueueEntry(v, getLabelForQueueEntry(v, graph,
					vertices), DBHelperDb.getIndexForHash(
					graph.getLabel(v).replaceAll("\\s+", "")).getFirst(),
					DBHelperDb.getIndexForHash(
							graph.getLabel(v).replaceAll("\\s+", ""))
							.getSecond()));
		}

		Collections.sort(sortedLabels);

		String[] buffer = getBestLabelHash(graph, edges, vertices, entry, exit,
				sortedLabels, children);

		if (insertingMode) {
			Integer id = DBHelperDb.addModelHash(buffer[0], nodesize);
			DBHelperDb.addParentChildRelations(id, children);
		}

		foldComponent(graph, edges, vertices, entry, exit, buffer[0]);
		return buffer[0];
	}

	private String[] getBestLabelHash(Graph graph, Set<Edge> edges,
			Set<Integer> vertices, Integer entry, Integer exit,
			LinkedList<QueueEntry> sortedEntries, LinkedList<Integer> children) {

		String lexSmallest = null;
		int lexSmallestSize = 0;

		SortedListPermutationGenerator gen = new SortedListPermutationGenerator(
				sortedEntries);
		int nrC = gen.getNrCombinations().intValue();
		helper.addTotalNumberOfCombinations(nrC);
		helper.setMaxCombinationsInRegion(nrC);
		helper.addTotalNrOfRegions();

		int number = 1;

		while (gen.hasMoreConbinations()) {
			LinkedList<QueueEntry> perm = gen.getNextCombination();
			int nodesize = 0;

			StringBuffer buffer = new StringBuffer();
			Map<Integer, Integer> idmap = new HashMap<Integer, Integer>();

			boolean addChildren = children.size() == 0;

			for (QueueEntry ent : perm) {
				idmap.put(ent.vertex, idmap.size());
				Integer ndx = ent.getLabel();
				if (!DBHelperDb.isEntryOrExitNode(ndx)) {
					nodesize += ent.getNodeSize();
				}
				buffer.append(ndx + ".");
				if (addChildren) {
					children.add(ndx);
				}
			}

			char matrix[][] = new char[idmap.size()][idmap.size()];

			for (int i = 0; i < idmap.size(); i++) {
				for (int j = 0; j < idmap.size(); j++) {
					if (i != j)
						matrix[i][j] = '0';
					else if (i == 0)
						matrix[i][j] = graph.getLabel(entry).charAt(0);
					else if (i == idmap.size() - 1)
						matrix[i][j] = graph.getLabel(exit).charAt(0);
					else
						matrix[i][j] = 'x';
				}
			}

			for (Edge e : edges) {
				if (vertices.contains(e.getSource())
						&& vertices.contains(e.getTarget())) {
					int i = idmap.get(e.getSource());
					int j = idmap.get(e.getTarget());
					matrix[i][j] = '1';
				}
			}

			for (int i = 0; i < matrix.length; i++) {
				for (int j = 0; j < matrix[0].length; j++) {
					buffer.append(matrix[i][j]);
					buffer.append(".");
				}
			}

			String hash = buffer.substring(0, buffer.length() - 1);
			buffer = null;

			if (lexSmallest == null || hash.compareTo(lexSmallest) < 0) {
				lexSmallest = hash;
				lexSmallestSize = nodesize;
			}
			number++;
		}
		return new String[] { lexSmallest, "" + lexSmallestSize };
	}

	/**
	 * Generates a hash code for a Rigid Component.
	 * 
	 * At this moment it only grabs labels of the children (either tasks or
	 * folded components), sort them and concatenate into a single string. --
	 * Requires the generation of permutations a their corresponding --
	 * adjacency matrices.
	 */
	public String visitRNode(Graph graph, Set<Edge> edges,
			Set<Integer> vertices, Integer entry, Integer exit, int nodesize) {
		// Grab children labels. Skip gateway/connector.

		LinkedList<QueueEntry> sortedLabels = new LinkedList<QueueEntry>();
		LinkedList<Integer> children = new LinkedList<Integer>();

		for (Integer v : vertices) {
			sortedLabels.add(new QueueEntry(v, getLabelForQueueEntry(v, graph,
					vertices), DBHelperDb.getIndexForHash(
					graph.getLabel(v).replaceAll("\\s+", "")).getFirst(),
					DBHelperDb.getIndexForHash(
							graph.getLabel(v).replaceAll("\\s+", ""))
							.getSecond()));
		}

		Collections.sort(sortedLabels);

		String[] buffer = getBestLabelHash(graph, edges, vertices, entry, exit,
				sortedLabels, children);

		if (insertingMode) {
			Integer id = DBHelperDb.addModelHash(buffer[0], nodesize);
			DBHelperDb.addParentChildRelations(id, children);
		}

		foldComponent(graph, edges, vertices, entry, exit, buffer[0]);

		return buffer[0];
	}

	public String visitRootSNode(Graph graph, Set<Edge> edges,
			Set<Integer> vertices, Integer entry, Integer exit, int size) {
		StringBuffer buffer = new StringBuffer();
		LinkedList<Integer> children = new LinkedList<Integer>();

		Integer curr, succ = entry;

		// there can be more than one branch connected to the entry node (if
		// model is disconnected)
		LinkedList<Integer> entryChildren = new LinkedList<Integer>();
		for (Edge e : edges) {
			if (e.getSource().equals(entry)) {
				entryChildren.add(e.getTarget());
			}
		}
		Collections.sort(entryChildren);

		int nodesize = 0;
		int nrcycles = 0;

		// insert entry node
		String label = graph.getLabel(entry).replaceAll("\\s+", "");
		Pair data = DBHelperDb.getIndexForHash(label);
		if (data != null) {
			Integer ndx = data.getFirst();
			// do not count entry and exit nodes
			if (!DBHelperDb.isEntryOrExitNode(ndx)) {
				nodesize += data.getSecond();
			}
			buffer.append(ndx + ".");
			children.add(ndx);
		} else {
			buffer.append(label + ".");
			nodesize++;
		}

		for (Integer i : entryChildren) {
			succ = i;
			do {
				curr = succ;
				// Skip Gateways/Connectors ...
				if (!helper.isGateway(curr)) {
					label = graph.getLabel(succ).replaceAll("\\s+", "");
					data = DBHelperDb.getIndexForHash(label);
					if (data != null) {
						Integer ndx = data.getFirst();
						// do not count entry and exit nodes
						if (!DBHelperDb.isEntryOrExitNode(ndx)) {
							nodesize += data.getSecond();
						}
						buffer.append(ndx + ".");
						children.add(ndx);
					} else {
						buffer.append(label + ".");
						nodesize++;
					}
				}
				nrcycles++;
				succ = -1;
				for (Edge e : edges) {
					if (e.getSource().equals(curr)) {
						succ = e.getTarget();
						break;
					}
				}
			} while (!succ.equals(exit));
		}

		// add exit node
		label = graph.getLabel(exit).replaceAll("\\s+", "");
		data = DBHelperDb.getIndexForHash(label);
		if (data != null) {
			Integer ndx = data.getFirst();
			// do not count entry and exit nodes
			if (!DBHelperDb.isEntryOrExitNode(ndx)) {
				nodesize += data.getSecond();
			}
			buffer.append(ndx + ".");
			children.add(ndx);
		} else {
			buffer.append(label + ".");
			nodesize++;
		}

		String hash = buffer.substring(0, buffer.length() - 1);
		helper.addTotalNumberOfCombinations(1);
		helper.setMaxCombinationsInRegion(1);
		helper.addTotalNrOfRegions();

		if (insertingMode) {
			Integer id = DBHelperDb.addModelHash(hash, size, children);
			DBHelperDb.addParentChildRelations(id, children);
			DBHelperDb.addRootNode(helper.getGraphName(), id);
		}

		foldComponent(graph, edges, vertices, entry, exit, hash);

		return hash;
	}

	public String visitSNode(Graph graph, Set<Edge> edges,
			Set<Integer> vertices, Integer entry, Integer exit, int size) {
		StringBuffer buffer = new StringBuffer();
		LinkedList<Integer> children = new LinkedList<Integer>();

		Integer curr = entry;
		int nodesize = 0;
		int nrcycles = 0;
		while (true) {
			Integer succ = -1;
			for (Edge e : edges)
				if (e.getSource().equals(curr)) {
					succ = e.getTarget();
					break;
				}
			if (succ.equals(exit))
				break;
			if (succ == -1) {
				System.out.println("oops ... succ = -1");
				System.exit(-1);
			}

			String label = graph.getLabel(succ).replaceAll("\\s+", "");
			Pair data = DBHelperDb.getIndexForHash(label);
			if (data != null) {
				Integer ndx = data.getFirst();
				if (!DBHelperDb.isGateway(ndx)
						&& !DBHelperDb.isEntryOrExitNode(ndx)) {
					nodesize += data.getSecond();
				} else {
					size--;
				}
				buffer.append(ndx + ".");
				children.add(ndx);
			} else {
				buffer.append(label + ".");
				nodesize++;
			}
			nrcycles++;

			curr = succ;
		}

		String hash = buffer.substring(0, buffer.length() - 1);
		helper.addTotalNumberOfCombinations(1);
		helper.setMaxCombinationsInRegion(1);
		helper.addTotalNrOfRegions();

		if (insertingMode) {
			Integer id = DBHelperDb.addModelHash(hash, Math.min(1, size),
					children);
			DBHelperDb.addParentChildRelations(id, children);
		}
		foldComponent(graph, edges, vertices, entry, exit, hash);

		return hash;
	}

	private String getLabelForQueueEntry(Integer vertex, Graph graph,
			Set<Integer> vertices) {

		Set<Integer> parents = graph.getPredecessorsOfVertex(vertex);
		Set<Integer> children = graph.getSuccessorsOfVertex(vertex);
		// we have a gateway
		if ((parents.size() > 1 || children.size() > 1)
				&& DBHelperDb.isGateway(graph.getLabel(vertex))) {
			StringBuffer buffer = new StringBuffer();
			// process parent nodes
			LinkedList<Integer> parentEntries = new LinkedList<Integer>();
			for (Integer v : parents) {
				if (vertices.contains(v)) {
					String label = graph.getLabel(v).replaceAll("\\s+", "");
					Pair data = DBHelperDb.getIndexForHash(label);
					Integer ndx = data.getFirst();
					parentEntries.add(ndx);
				}
			}
			Collections.sort(parentEntries);
			for (Integer i : parentEntries) {
				buffer.append(i + ".");
			}

			buffer.append(DBHelperDb.getIndexForHash(
					graph.getLabel(vertex).replaceAll("\\s+", "")).getFirst()
					+ ".");

			// process child nodes
			LinkedList<Integer> childEntries = new LinkedList<Integer>();
			for (Integer v : children) {
				if (vertices.contains(v)) {
					String label = graph.getLabel(v).replaceAll("\\s+", "");
					Pair data = DBHelperDb.getIndexForHash(label);
					Integer ndx = data.getFirst();
					childEntries.add(ndx);
				}
			}

			Collections.sort(childEntries);

			for (Integer i : childEntries) {
				buffer.append(i + ".");
			}

			return buffer.substring(0, buffer.length() - 1);

		} else
			return null;

	}

	private void foldComponent(Graph graph, Set<Edge> edges,
			Set<Integer> vertices, Integer entry, Integer exit, String label) {
		Integer v = graph.addVertex(label);
		vertices.clear();
		edges.clear();
		vertices.add(entry);
		vertices.add(exit);
		vertices.add(v);
		edges.add(graph.addEdge(entry, v));
		edges.add(graph.addEdge(v, exit));
	}
}
