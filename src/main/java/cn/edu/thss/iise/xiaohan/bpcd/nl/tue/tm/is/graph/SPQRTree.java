package cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.graph;

/**
 * SPQR-tree proxy to OGDF
 * 
 * @author Remco Dijkman
 */
public class SPQRTree {
	/**
	 * Creates a native graph, returns a pointer to that graph.
	 * 
	 * @return pointer to created graph.
	 */
	public static native int createGraph();

	/**
	 * Adds a node to a graph, returns a pointer to that node.
	 * 
	 * @param toGraph
	 *            pointer to graph to add node to.
	 * @return pointer to the created node.
	 */
	public static native int addNode(int toGraph);

	/**
	 * Adds an edge to a graph.
	 * 
	 * @param toGraph
	 *            pointer to graph to add edge to.
	 * @param srcNode
	 *            pointer to node that is the source for the edge.
	 * @param tgtNode
	 *            pointer to node that is the target for the edge.
	 */
	public static native int addEdge(int toGraph, int srcNode, int tgtNode);

	/**
	 * Computes the SPQR tree for a graph.
	 * 
	 * @param forGraph
	 *            pointer to graph to compute SPQR tree for.
	 * @return pointer to the SPQR tree.
	 */
	public static native int createSPQRTree(int forGraph);

	public static native int createSPQRTree(int forGraph, int atEdge);

	/**
	 * Returns the root node of the SPQR Tree.
	 * 
	 * @param ofTree
	 *            the pointer to the SPQR tree.
	 * @return pointer to the root node of the SPQR tree.
	 */
	public static native int spqrRootNode(int ofTree);

	/**
	 * Returns the array of child nodes of this node in the SPQR tree.
	 * 
	 * @param ofTree
	 *            pointer to the SPQR tree.
	 * @param ofNode
	 *            pointer to the SPQR tree node for which to return the child
	 *            nodes.
	 * @return array of pointers to child nodes.
	 */
	public static native int[] spqrChildNodes(int ofTree, int ofNode,
			int parentNode);

	/**
	 * Returns a pointer to the pertinent graph of a node in an SPQR tree.
	 * 
	 * @param ofTree
	 *            pointer to the SPQR tree.
	 * @param ofNode
	 *            pointer to the SPQR tree node for which to return the
	 *            pertinent graph nodes.
	 * @return pointer to a pertinent graph.
	 */
	public static native int spqrPertinentGraph(int ofTree, int ofNode);

	/**
	 * Returns the array of nodes of a pertinent graph.
	 * 
	 * @param pg
	 *            pointer to a pertinent graph.
	 * @return array of pointers to nodes from the pertinent graph.
	 */
	public static native int[] spqrPertinentNodes(int pg);

	/**
	 * Returns the array of edges of a pertinent graph.
	 * 
	 * @param pg
	 *            pointer to a pertinent graph.
	 * @return an array of pointers to edges in the pertinent graph.
	 */
	public static native int[] spqrPertinentEdges(int pg);

	/**
	 * Returns the node from the original graph that corresponds to the given
	 * node in the given pertinent graph.
	 * 
	 * @param pg
	 *            pointer to a pertinent graph.
	 * @param n
	 *            pointer to a node in the pertinent graph.
	 * @return pointer to a node in the original graph.
	 */
	public static native int spqrPertinentOriginalNode(int pg, int n);

	/**
	 * Returns the array of nodes from the original graph that correspond to the
	 * nodes of the given edge.
	 * 
	 * @param pg
	 *            pointer to a pertinent graph.
	 * @param e
	 *            pointer to an edge in the pertinent graph.
	 * @return array[2] of pointers to nodes in the original graph: array[0] =
	 *         source node, array[2] = target node.
	 */
	public static native int[] spqrPertinentEdgeOriginalNodes(int pg, int e);

	public static native int spqrPertinentReferenceEdge(int pg);

	public static native int spqrPertinentSkeletonReferenceEdge(int pg);

	/**
	 * Returns the skeleton graph for the given node in the given SPQR tree.
	 * 
	 * @param ofTree
	 *            a pointer to an SPQR tree.
	 * @param ofNode
	 *            a pointer to a node in the SPQR tree.
	 * @return pointer to a skeleton graph.
	 */
	public static native int spqrSkeleton(int ofTree, int ofNode);

	/**
	 * Returns the nodes in the skeleton graph.
	 * 
	 * @param sg
	 *            pointer to a skeleton graph.
	 * @return array of pointers to nodes.
	 */
	public static native int[] spqrSkeletonNodes(int sg);

	/**
	 * Returns the array of edges of a skeleton graph.
	 * 
	 * @param sg
	 *            pointer to a skeleton graph.
	 * @return an array of pointers to edges in the skeleton graph.
	 */
	public static native int[] spqrSkeletonEdges(int sg);

	/**
	 * Returns the node from the original graph that corresponds to the given
	 * node in the given skeleton graph.
	 * 
	 * @param sg
	 *            pointer to a skeleton graph.
	 * @param n
	 *            pointer to a node in the skeleton graph.
	 * @return pointer to a node in the original graph.
	 */
	public static native int spqrSkeletonOriginalNode(int sg, int n);

	/**
	 * Returns the array of nodes from the original graph that correspond to the
	 * nodes of the given edge.
	 * 
	 * @param sg
	 *            pointer to a skeleton graph.
	 * @param e
	 *            pointer to an edge in the skeleton graph.
	 * @return array[2] of pointers to nodes in the original graph: array[0] =
	 *         source node, array[2] = target node.
	 */
	public static native int[] spqrSkeletonEdgeOriginalNodes(int sg, int e);

	/**
	 * Returns true if and only if the given edge from the given skeleton graph
	 * is a virtual edge.
	 * 
	 * @param sg
	 *            pointer to a skeleton graph.
	 * @param e
	 *            pointer to an edge in the skeleton graph.
	 * @return true if the edge is virtual, false if the edge is real.
	 */
	public static native boolean spqrSkeletonEdgeIsVirtual(int sg, int e);

	/**
	 * Returns the type of the given node in the given SPQR tree.
	 * 
	 * @param ofTree
	 *            pointer to an SPQR tree.
	 * @param ofNode
	 *            pointer to a node in the SPQR tree.
	 * @return 0 iff node is an S-Node, 1 iff node is a P-Node, 2 iff node is an
	 *         R-Node.
	 */
	public static native int spqrNodeType(int ofTree, int ofNode);

	// Load the native library for computing the SPQR-tree<
	static {
		System.loadLibrary("spqr/ogdf");
	}
}
