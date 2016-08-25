package cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import cn.edu.thss.iise.xiaohan.bpcd.console.ConsoleDag;
import cn.edu.thss.iise.xiaohan.bpcd.graph.Helper;
import cn.edu.thss.iise.xiaohan.bpcd.graph.TreeVisitorDB;
import cn.edu.thss.iise.xiaohan.bpcd.graph.Visitor;
import cn.edu.thss.iise.xiaohan.bpcd.graph.rpsdag.db.DBHelperDb;
import cn.edu.thss.iise.xiaohan.bpcd.mtree.index.Fragment;
import cn.edu.thss.iise.xiaohan.bpcd.mtree.index.Stats;
import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;
import de.bpt.hpi.ogdf.rpst.ExpRPST;
import de.bpt.hpi.ogdf.spqr.SPQRNodeType;
import de.bpt.hpi.ogdf.spqr.TreeNode;

public class RPSDag {

	Map<String, List<String>> index = new HashMap<String, List<String>>();
	Set<String> candidates = new HashSet<String>();
	private DBHelperDb dbHelper;
	// ---------------------------------------------------------------------------
	// Create MTree, insert fragments and other operations about MTree
	public Stats stats = new Stats();

	// ---------------------------------------------------------------------------

	public DBHelperDb getDbHelper() {
		return dbHelper;
	}

	Properties properties = new Properties();

	public RPSDag() {
		dbHelper = new DBHelperDb("refactoring", "refactor", "refactor");
		createMTree();
	}

	public RPSDag(String dbName) {
		dbHelper = new DBHelperDb(dbName, "refactor", "refactor");
		createMTree();
	}

	// ---------------------------------------------------------------------------
	// The parameter could be changed,and MTree.DEFAULT_MIN_NODE_CAPACITY = 50
	public void createMTree() {
		stats.createMTree(10);
	}

	// ---------------------------------------------------------------------------

	// Add a new parameter filename in these two functions
	public void addProcessModel(Helper helper, String filename)
			throws Exception {
		addProcessModel(helper, false, filename);
	}

	public void addProcessModel(Helper helper, boolean print, String filename)
			throws Exception {
		Graph graph = helper.getGraph();
		ExpRPST tree = normalizeGraph(graph);

		Map<TreeNode, String> codeMap = new HashMap<TreeNode, String>();

		// ---------------------------------------------------------------------------
		// Insert the fragments of the process models into MTree
		Fragment fragment = new Fragment(graph);
		fragment.setModelFile(filename);
		stats.insertMTree(fragment);
		// ---------------------------------------------------------------------------

		dbHelper.startTransaction();
		traverse(tree, codeMap, helper);
		dbHelper.closeConnection();
	}

	private void traverse(ExpRPST tree, Map<TreeNode, String> codeMap,
			Helper helper) {
		Set<Integer> vertices = new HashSet<Integer>(tree.getRootNode()
				.getOriginalVertices());
		Set<Edge> edges = new HashSet<Edge>(tree.getRootNode()
				.getOriginalEdges());
		Visitor visitor = new TreeVisitorDB(helper, dbHelper);
		TreeNode root = tree.getRootNode();
		Graph graphp = tree.getExpandedGraph().clone();

		traverse(tree, graphp, root, edges, vertices, visitor, codeMap, helper);
		Collection<Integer> v = root.getOriginalVertices();
		visitor.visitRootSNode(graphp, edges, vertices, tree.getEntry(root),
				tree.getExit(root), v.size());
		codeMap.put(tree.getRootNode(), "code");
	}

	private void traverse(ExpRPST tree, Graph graph, TreeNode curr,
			Set<Edge> edges, Set<Integer> vertices, Visitor visitor,
			Map<TreeNode, String> codeMap, Helper helper) {

		if (curr.getNodeType() == SPQRNodeType.Q)
			return;
		Set<Edge> ledges = new HashSet<Edge>(curr.getOriginalEdges());
		Set<Integer> lvertices = new HashSet<Integer>(
				curr.getOriginalVertices());
		for (TreeNode child : curr.getChildNodes()) {
			if (child.getNodeType() == SPQRNodeType.Q) {
				if (!helper.isGateway(tree.getEntry(child))) {
					String code = graph.getLabel(tree.getEntry(child))
							.replaceAll("\\s+", "");
					// ---------------------------------------------------------------------------
					dbHelper.addModelHash(code, 1);
				}
				continue;
			}
			Set<Edge> cedges = new HashSet<Edge>(child.getOriginalEdges());
			Set<Integer> cvertices = new HashSet<Integer>(
					child.getOriginalVertices());

			traverse(tree, graph, child, cedges, cvertices, visitor, codeMap,
					helper);
			Integer entry = tree.getEntry(child);
			Integer exit = tree.getExit(child);
			String code = null;
			switch (child.getNodeType()) {
			case S:
				Collection<Integer> v = child.getOriginalVertices();
				code = visitor.visitSNode(graph, cedges, cvertices, entry,
						exit, v.size());
				break;
			case P:
				v = child.getOriginalVertices();
				code = visitor.visitPNode(graph, cedges, cvertices, entry,
						exit, v.size());
				break;
			case Q:
				break;
			case R:
				v = child.getOriginalVertices();
				code = visitor.visitRNode(graph, cedges, cvertices, entry,
						exit, v.size());
				break;
			}
			ledges.removeAll(child.getOriginalEdges());
			lvertices.removeAll(child.getOriginalVertices());
			ledges.addAll(cedges);
			lvertices.addAll(cvertices);

			codeMap.put(child, code);
		}

		edges.clear();
		edges.addAll(ledges);
		vertices.clear();
		vertices.addAll(lvertices);
	}

	private ExpRPST normalizeGraph(Graph graph) {
		Set<Integer> srcs = graph.getSourceNodes();
		Set<Integer> tgts = graph.getSinkNodes();

		srcs.retainAll(tgts);
		// remove nodes that have no input and output edges
		for (Integer v : srcs) {
			graph.removeVertex(v);
		}

		srcs = graph.getSourceNodes();
		tgts = graph.getSinkNodes();

		Integer entry = graph.addVertex("_entry_");
		Integer exit = graph.addVertex("_exit_");

		// connect all source nodes with one entry node
		for (Integer tgt : srcs)
			graph.addEdge(entry, tgt);

		// connect all sink nodes with one exit node
		for (Integer src : tgts)
			graph.addEdge(src, exit);

		return new ExpRPST(graph, entry, exit);
	}

	public boolean printProcessModelClone(Helper helper, int nodeId)
			throws Exception {
		Graph graph = helper.getGraph();
		ExpRPST tree = normalizeGraph(graph);

		Map<TreeNode, String> codeMap = new HashMap<TreeNode, String>();
		Map<Integer, Collection<Integer>> idOriginalVerticesMap = new HashMap<Integer, Collection<Integer>>();
		return traverseClones(tree, codeMap, helper, nodeId,
				idOriginalVerticesMap);
	}

	private boolean traverseClones(ExpRPST tree, Map<TreeNode, String> codeMap,
			Helper helper, int nodeId,
			Map<Integer, Collection<Integer>> idOriginalVerticesMap) {
		Set<Integer> vertices = new HashSet<Integer>(tree.getRootNode()
				.getOriginalVertices());
		Set<Edge> edges = new HashSet<Edge>(tree.getRootNode()
				.getOriginalEdges());
		Visitor visitor = new TreeVisitorDB(helper, dbHelper, false);
		TreeNode root = tree.getRootNode();
		Graph graphp = tree.getExpandedGraph().clone();

		Collection<Integer> v = root.getOriginalVertices();
		boolean fileWritten = traverseClones(tree, graphp, root, edges,
				vertices, visitor, codeMap, helper, nodeId,
				idOriginalVerticesMap);

		if (fileWritten) {
			return true;
		}

		String code = visitor.visitRootSNode(graphp, edges, vertices,
				tree.getEntry(root), tree.getExit(root), v.size());
		if (dbHelper.getIndexForHash(code).getFirst() == nodeId) {
			helper.serializeToFile(v, "" + nodeId);
			return true;
		}

		// this is a substring
		if (("." + code + ".").indexOf("." + ConsoleDag.idToHashMap.get(nodeId)
				+ ".") != -1) {
			helper.serializeToFile(ConsoleDag.idToHashMap.get(nodeId),
					idOriginalVerticesMap, "" + nodeId);
			return true;
		}

		return false;
	}

	private boolean traverseClones(ExpRPST tree, Graph graph, TreeNode curr,
			Set<Edge> edges, Set<Integer> vertices, Visitor visitor,
			Map<TreeNode, String> codeMap, Helper helper, int nodeId,
			Map<Integer, Collection<Integer>> idOriginalVerticesMap) {

		if (curr.getNodeType() == SPQRNodeType.Q)
			return false;

		Set<Edge> ledges = new HashSet<Edge>(curr.getOriginalEdges());
		Set<Integer> lvertices = new HashSet<Integer>(
				curr.getOriginalVertices());
		for (TreeNode child : curr.getChildNodes()) {
			if (child.getNodeType() == SPQRNodeType.Q) {
				if (!helper.isGateway(tree.getEntry(child))) {
					Collection<Integer> v = new HashSet<Integer>();
					String code = graph.getLabel(tree.getEntry(child))
							.replaceAll("\\s+", "");
					v.add(tree.getEntry(child));
					idOriginalVerticesMap.put(dbHelper.getIndexForHash(code)
							.getFirst(), v);
					// System.out.println(code);
					// ---------------------------------------------------------------------------
					dbHelper.addModelHash(code, 1);
				}
				continue;
			}
			Set<Edge> cedges = new HashSet<Edge>(child.getOriginalEdges());
			Set<Integer> cvertices = new HashSet<Integer>(
					child.getOriginalVertices());

			boolean fileWritten = traverseClones(tree, graph, child, cedges,
					cvertices, visitor, codeMap, helper, nodeId,
					idOriginalVerticesMap);
			if (fileWritten) {
				return true;
			}

			Integer entry = tree.getEntry(child);
			Integer exit = tree.getExit(child);
			String code = null;
			switch (child.getNodeType()) {
			case S:
				Collection<Integer> v = child.getOriginalVertices();
				code = visitor.visitSNode(graph, cedges, cvertices, entry,
						exit, v.size());
				idOriginalVerticesMap.put(dbHelper.getIndexForHash(code)
						.getFirst(), v);
				if (dbHelper.getIndexForHash(code).getFirst() == nodeId) {
					helper.serializeToFile(v, "" + nodeId);
					return true;
				}

				// this is a substring
				if (("." + code + ".").indexOf("."
						+ ConsoleDag.idToHashMap.get(nodeId) + ".") != -1) {
					helper.serializeToFile(ConsoleDag.idToHashMap.get(nodeId),
							idOriginalVerticesMap, "" + nodeId);
					return true;
				}
				break;
			case P:
				v = child.getOriginalVertices();
				code = visitor.visitPNode(graph, cedges, cvertices, entry,
						exit, v.size());
				idOriginalVerticesMap.put(dbHelper.getIndexForHash(code)
						.getFirst(), v);
				if (dbHelper.getIndexForHash(code).getFirst() == nodeId) {
					helper.serializeToFile(v, "" + nodeId);
					return true;
				}
				break;
			case Q:
				break;
			case R:
				v = child.getOriginalVertices();
				code = visitor.visitRNode(graph, cedges, cvertices, entry,
						exit, v.size());
				idOriginalVerticesMap.put(dbHelper.getIndexForHash(code)
						.getFirst(), v);

				if (dbHelper.getIndexForHash(code).getFirst() == nodeId) {
					helper.serializeToFile(v, "" + nodeId);
					return true;
				}
				break;
			}
			ledges.removeAll(child.getOriginalEdges());
			lvertices.removeAll(child.getOriginalVertices());
			ledges.addAll(cedges);
			lvertices.addAll(cvertices);

			codeMap.put(child, code);
		}

		edges.clear();
		edges.addAll(ledges);
		vertices.clear();
		vertices.addAll(lvertices);
		return false;
	}
}
