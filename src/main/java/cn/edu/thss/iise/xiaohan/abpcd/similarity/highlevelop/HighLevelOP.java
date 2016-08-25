package cn.edu.thss.iise.xiaohan.abpcd.similarity.highlevelop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import cn.edu.thss.iise.xiaohan.abpcd.similarity.graphconvert.GraphConvert;
import cn.edu.thss.iise.xiaohan.abpcd.similarity.util.OPGraph;
import cn.edu.thss.iise.xiaohan.abpcd.similarity.util.OPLine;
import cn.edu.thss.iise.xiaohan.abpcd.similarity.util.OPNode;
import de.bpt.hpi.graph.Graph;

public class HighLevelOP {

	/**
	 * @author Xiao Han fixs it
	 */

	public static double GetHighLevelOPSimilarity(Graph graph1, Graph graph2) {
		GraphConvert convert1 = new GraphConvert(graph1);
		GraphConvert convert2 = new GraphConvert(graph2);

		OPGraph opgraph1 = convert1.getOPGraph();
		OPGraph opgraph2 = convert2.getOPGraph();

		List<String> common = new LinkedList<String>();
		common = commonLabels(opgraph1.nodes, opgraph2.nodes);

		double distance = GetHighLevelOPDistance(graph1, graph2);
		double similarity = 1
				- distance
				/ (opgraph1.nodes.size() + opgraph2.nodes.size() - common
						.size());

		return similarity;
	}

	public static int GetHighLevelOPDistance(Graph graph1, Graph graph2) {
		List<String> list = GetHighLevelOPList(graph1, graph2);

		if (list == null)
			return 0;
		else
			return list.size();
	}

	public static List<String> GetHighLevelOPList(Graph graph1, Graph graph2) {
		GraphConvert convert1 = new GraphConvert(graph1);
		GraphConvert convert2 = new GraphConvert(graph2);

		OPGraph opgraph1 = convert1.getOPGraph();
		OPGraph opgraph2 = convert2.getOPGraph();

		return calculateHighLevelOP(opgraph1, opgraph2);
	}

	private static void printGraph(OPGraph graph1) {
		for (OPNode node : graph1.nodes)
			System.out.print(node.id + "->" + node.label + " ");
		System.out.println();

		for (OPLine line : graph1.lines)
			System.out.print(line.start + "->" + line.end + " ");
		System.out.println();

		for (OPNode node : graph1.nodes)
			for (OPNode node2 : node.parents)
				System.out.println(node2.label + "->" + node.label);

		for (OPNode node : graph1.nodes)
			for (OPNode node2 : node.children)
				System.out.println(node.label + "->" + node2.label);
	}

	private static void printMatrix(Matrix m1, Matrix m2, List<OPNode> same1,
			List<OPNode> same2) {
		for (int i = 0; i < m1.n; i++) {
			System.out.printf("%12s", same1.get(i).label + "  ");
			for (int j = 0; j < m1.n; j++)
				System.out.print(m1.matrix[i][j] + "  ");
			System.out.println();
		}
		System.out.println();
		for (int i = 0; i < m2.n; i++) {
			System.out.printf("%12s", same2.get(i).label + "  ");
			for (int j = 0; j < m2.n; j++)
				System.out.print(m2.matrix[i][j] + "  ");
			System.out.println();
		}
		System.out.println("\n--------------------------------------");
	}

	/**
	 * return operation change lists
	 * 
	 * @param pro1
	 * @param pro2
	 * @return
	 */
	private static List<String> calculateHighLevelOP(OPGraph graph1,
			OPGraph graph2) {

		// calculate the common set
		List<String> common = new LinkedList<String>();
		common = commonLabels(graph1.nodes, graph2.nodes);

		List<String> highLevelOp = new ArrayList<String>();
		highLevelOp = createOptimizedOP(common, graph1, graph2);

		return highLevelOp;
	}

	private static List<String> createOptimizedOP(List<String> common,
			OPGraph graph1, OPGraph graph2) {

		HashMap<String, Boolean> mapID1 = new HashMap<String, Boolean>();
		HashMap<String, Boolean> mapID2 = new HashMap<String, Boolean>();
		HashMap<String, Boolean> mapLabel1 = new HashMap<String, Boolean>();
		HashMap<String, Boolean> mapLabel2 = new HashMap<String, Boolean>();

		for (OPNode v : graph1.nodes)
			mapID1.put(v.id, false);
		for (OPNode v : graph2.nodes)
			mapID2.put(v.id, false);
		for (String label : common) {
			if (graph1.getLabelNumber(label) >= graph2.getLabelNumber(label)) {
				mapLabel1.put(label, true);
				mapLabel2.put(label, false);
			} else {
				mapLabel1.put(label, false);
				mapLabel2.put(label, true);
			}
		}

		List<OPNode> nodeTaken1 = new ArrayList<OPNode>();
		List<OPNode> nodeTaken2 = new ArrayList<OPNode>();
		List<List<OPNode>> allNodeTaken1 = new ArrayList<List<OPNode>>();
		List<List<OPNode>> allNodeTaken2 = new ArrayList<List<OPNode>>();

		tryNodeLabel(0, common, graph1, mapID1, mapLabel1, nodeTaken1,
				allNodeTaken1);
		// System.out.println("\n--------------------------------------");
		tryNodeLabel(0, common, graph2, mapID2, mapLabel2, nodeTaken2,
				allNodeTaken2);
		// System.out.println("\n--------------------------------------");

		if (allNodeTaken1.isEmpty() || allNodeTaken2.isEmpty())
			return createHighLevelOP(null, null, graph1, graph2);

		List<String> result = new ArrayList<String>();
		List<String> resultBest = new ArrayList<String>();
		for (int i = 0; i < allNodeTaken1.size(); i++) {
			for (int j = 0; j < allNodeTaken2.size(); j++) {
				result = createHighLevelOP(allNodeTaken1.get(i),
						allNodeTaken2.get(j), graph1, graph2);
				if (i == 0 && j == 0)
					resultBest = result;
				else if (resultBest.size() > result.size())
					resultBest = result;
			}
		}
		return resultBest;
	}

	private static void tryNodeLabel(int flag, List<String> common,
			OPGraph graph, HashMap<String, Boolean> mapIDs,
			HashMap<String, Boolean> mapLabel, List<OPNode> nodeTaken,
			List<List<OPNode>> allNodeTaken) {

		if (common.isEmpty())
			return;

		List<OPNode> sameNodes = new ArrayList<OPNode>();
		for (OPNode node : graph.nodes)
			if (common.get(flag).equals(node.label))
				sameNodes.add(node);
		// System.out.println(flag + " Flag coming!");

		if (mapLabel.get(common.get(flag)) == true) {
			for (OPNode node : sameNodes) {
				if (mapIDs.get(node.id) == true)
					continue;
				mapIDs.put(node.id, true);
				nodeTaken.add(node);
				// System.out.println("Node coming: " + sameNodes.get(i).label);

				if (flag == common.size() - 1) {
					List<OPNode> nodeTrace = new ArrayList<OPNode>(nodeTaken);
					allNodeTaken.add(nodeTrace);
					/*
					 * System.out.print("\nNEW TRACE: "); for(int j = 0; j <
					 * nodeTaken.size(); j++)
					 * System.out.print(nodeTaken.get(j).label + ":" +
					 * nodeTaken.get(j).id + " ");
					 */
				} else
					tryNodeLabel(flag + 1, common, graph, mapIDs, mapLabel,
							nodeTaken, allNodeTaken);

				mapIDs.put(node.id, false);
				nodeTaken.remove(flag);
			}
		} else {
			for (OPNode node : sameNodes) {
				if (mapIDs.get(node.id) == true)
					continue;
				mapIDs.put(node.id, true);
				nodeTaken.add(node);

				if (flag == common.size() - 1) {
					List<OPNode> nodeTrace = new ArrayList<OPNode>(nodeTaken);
					allNodeTaken.add(nodeTrace);
				} else
					tryNodeLabel(flag + 1, common, graph, mapIDs, mapLabel,
							nodeTaken, allNodeTaken);
			}
		}
	}

	private static List<String> createHighLevelOP(List<OPNode> same1,
			List<OPNode> same2, OPGraph graph1, OPGraph graph2) {

		// get high level operation
		List<String> deleteOp = new ArrayList<String>();
		List<String> insertOp = new ArrayList<String>();
		List<String> moveOp = new ArrayList<String>();
		List<String> highLevelOp = new ArrayList<String>();

		if (same1 != null && same2 != null) {
			// create matrix
			Matrix m1 = createMatrix(same1, graph1);
			Matrix m2 = createMatrix(same2, graph2);
			// printMatrix(m1, m2, same1, same2);

			// find difference and differenced sub-matrixes
			Matrix m = findDifference(m1, m2);

			List<Matrix> ms = new ArrayList<Matrix>();
			ms = divide(m);

			// boolean algebra to find move item
			List<String> moveItem = findMoveItem(ms);
			moveOp = move(graph1, graph2, moveItem);
		}
		// Important!!! Reduce the time, only for the minimized operation
		// numbers
		deleteOp = diff(graph1.getLabelList(), graph2.getLabelList());
		insertOp = diff(graph2.getLabelList(), graph1.getLabelList());
		// deleteOp = delete(graph1,graph2);
		// insertOp = insert(graph1, graph2, same2);

		highLevelOp.addAll(deleteOp);
		highLevelOp.addAll(moveOp);
		highLevelOp.addAll(insertOp);

		return highLevelOp;
	}

	/**
	 * Find same nodes' labels set
	 * 
	 * @param nodes1
	 * @param nodes2
	 * @return
	 */
	private static List<String> commonLabels(List<OPNode> nodes1,
			List<OPNode> nodes2) {
		List<String> common = new LinkedList<String>();
		List<OPNode> list1 = new LinkedList<OPNode>(nodes1);
		List<OPNode> list2 = new LinkedList<OPNode>(nodes2);

		OPNode n = new OPNode();
		for (int i = 0; i < list1.size(); i++) {
			n = list1.get(i);

			for (int j = 0; j < list2.size(); j++) {
				if (n.label.equals(list2.get(j).label)) {
					list2.remove(j);
					common.add(n.label);
					break;
				}
			}
		}
		return common;
	}

	/**
	 * Get differenced sub-matrixes according to difference matrix
	 * 
	 * @param m
	 * @return
	 */
	public static ArrayList<Matrix> divide(Matrix m) {
		ArrayList<Matrix> matrixes = new ArrayList<Matrix>();
		ArrayList<String> names = m.getNames();
		String[][] temp = m.getMatrix();
		Matrix matrix1 = new Matrix();
		Matrix matrix2 = new Matrix();

		// every element is a matrix
		for (String name : names) {
			ArrayList<String> temp1 = new ArrayList<String>();
			temp1.add(name);
			Matrix matrix = new Matrix();
			matrix.setNames(temp1);
			matrixes.add(matrix);
		}

		for (int i = 0; i < m.getN(); i++) {
			for (int j = i; j < m.getN(); j++) {
				if (temp[i][j].equals("1")) {
					matrix1 = findMatrix(names.get(i), matrixes);
					matrix2 = findMatrix(names.get(j), matrixes);

					if (!matrix1.equals(matrix2)) {
						Matrix matrixTemp = mergeMatrix(matrix1, matrix2, m);
						matrixes.remove(matrixes.indexOf(matrix1));
						matrixes.remove(matrixes.indexOf(matrix2));
						matrixes.add(matrixTemp);
					}
				}
			}
		}
		return matrixes;
	}

	/**
	 * Merge Matrix
	 * 
	 * @param m1
	 * @param m2
	 * @param m
	 * @return
	 */
	public static Matrix mergeMatrix(Matrix m1, Matrix m2, Matrix m) {
		ArrayList<String> mNames = m.getNames();
		String[][] mMatrix = m.getMatrix();
		Matrix mergeMatrix = new Matrix();
		ArrayList<String> mergeNames = new ArrayList<String>();
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;

		// bound
		for (String temp1 : m1.getNames()) {
			if (mNames.indexOf(temp1) < min) {
				min = mNames.indexOf(temp1);
			}
			if (mNames.indexOf(temp1) > max) {
				max = mNames.indexOf(temp1);
			}
		}
		for (String temp2 : m2.getNames()) {
			if (mNames.indexOf(temp2) < min) {
				min = mNames.indexOf(temp2);
			}
			if (mNames.indexOf(temp2) > max) {
				max = mNames.indexOf(temp2);
			}
		}

		if (min <= max) {
			for (int i = min; i <= max; i++) {
				mergeNames.add(mNames.get(i));
			}
			mergeMatrix.setN(mergeNames.size());
			mergeMatrix.setNames(mergeNames);
			String[][] mergeFlags = new String[mergeNames.size()][mergeNames
					.size()];

			for (int i = min; i <= max; i++) {
				for (int j = min; j <= max; j++) {
					mergeFlags[i - min][j - min] = mMatrix[i][j];
				}
			}
			mergeMatrix.setMatrix(mergeFlags);
		}

		return mergeMatrix;
	}

	/**
	 * Find matrix in matrixes that contains s
	 * 
	 * @param s
	 * @param matrixes
	 * @return
	 */
	public static Matrix findMatrix(String s, ArrayList<Matrix> matrixes) {
		for (Matrix temp : matrixes) {
			if (temp.getNames().contains(s)) {
				return temp;
			}
		}
		return null;
	}

	/**
	 * Get Flag Matrix
	 * 
	 * @param m1
	 * @param m2
	 * @return
	 */
	public static Matrix findDifference(Matrix m1, Matrix m2) {
		Matrix m = new Matrix();
		String[][] s1 = m1.getMatrix();
		String[][] s2 = m2.getMatrix();
		ArrayList<String> n1 = m1.getNames();
		String[][] flags = new String[n1.size()][n1.size()];
		// System.out.println(n1);
		for (int i = 0; i < n1.size(); i++) {
			for (int j = 0; j < n1.size(); j++) {
				if (s1[i][j].equals(s2[i][j])) {
					flags[i][j] = "0";
				} else {
					flags[i][j] = "1";
				}
			}
		}

		m.setMatrix(flags);
		m.setNames(n1);
		m.setN(n1.size());

		return m;
	}

	// The parameter String type can be "and" or "xor"
	// In order to record the predecessors which are "and" or "xor" nodes
	private static List<OPNode> BFSType(OPNode node, String type, OPGraph graph) {
		List<OPNode> nodeList = new ArrayList<OPNode>();
		List<OPNode> visitList = new ArrayList<OPNode>();
		HashMap<String, Boolean> mapIDs = new HashMap<String, Boolean>();
		OPNode v = new OPNode();

		for (int i = 0; i < graph.nodes.size(); i++) {
			v = graph.nodes.get(i);
			mapIDs.put(v.id, false);
		}
		v = node;
		for (int i = 0; i < graph.nodes.size(); i++)
			if (!mapIDs.get(v.id)) {
				mapIDs.put(v.id, true);
				if (v.label.toLowerCase().contains(type))
					nodeList.add(v);

				visitList.add(v);
				while (!visitList.isEmpty()) {
					OPNode u = new OPNode(visitList.remove(0)); // use the List
																// as a Queue
					for (int j = 0; j < u.parents.size(); j++) {
						OPNode w = new OPNode(u.parents.get(j));
						if (!mapIDs.get(w.id)) {
							mapIDs.put(w.id, true);
							if (w.label.toLowerCase().contains(type))
								nodeList.add(v);
							visitList.add(w);
						}
					}
				}
			}
		return nodeList;
	}

	// If there is a path from node1 to node2, return true
	private static boolean BFSTraverse(OPNode node1, OPNode node2, OPGraph graph) {
		List<OPNode> visitList = new ArrayList<OPNode>();
		HashMap<String, Boolean> mapIDs = new HashMap<String, Boolean>();
		OPNode v = new OPNode();

		for (int i = 0; i < graph.nodes.size(); i++) {
			v = graph.nodes.get(i);
			mapIDs.put(v.id, false);
			// System.out.println(i+ " "+v.id);
		}
		v = node1;
		for (int i = 0; i < graph.nodes.size(); i++)
			if (!mapIDs.get(v.id)) {
				mapIDs.put(v.id, true);
				if (v.id == node2.id)
					return true;

				visitList.add(v);
				while (!visitList.isEmpty()) {
					OPNode u = new OPNode(visitList.remove(0)); // use the List
																// as a Queue
					for (int j = 0; j < u.children.size(); j++) {
						OPNode w = new OPNode(u.children.get(j));
						// System.out.print('\n'+w.id);
						if (!mapIDs.get(w.id)) { // mapIDs.get(w.id) == null,
													// maybe
							mapIDs.put(w.id, true);
							if (w.id == node2.id)
								return true;
							visitList.add(w);
						}
					}
				}
			}

		return false;
	}

	/**
	 * Construct the Order Matrix
	 * 
	 * @param sames
	 * @param nodes
	 * @param flag
	 * @return
	 */
	private static Matrix createMatrix(List<OPNode> sames, OPGraph graph) {
		Matrix m = new Matrix();
		m.n = sames.size();
		m.matrix = new String[m.n][m.n];
		for (OPNode node : sames) {
			m.names.add(node.label);
		}

		for (int i = 0; i < m.n; i++)
			for (int j = 0; j < m.n; j++)
				m.matrix[i][j] = "?";

		for (int i = 0; i < sames.size(); i++)
			for (int j = i; j < sames.size(); j++) {
				if (i == j) {
					m.matrix[i][j] = "k";
					continue;
				}

				boolean pathij = BFSTraverse(sames.get(i), sames.get(j), graph);
				boolean pathji = BFSTraverse(sames.get(j), sames.get(i), graph);

				if (pathij == true && pathji == false) {
					m.matrix[i][j] = "1";
					m.matrix[j][i] = "0";
				} else if (pathij == false && pathji == true) {
					m.matrix[i][j] = "0";
					m.matrix[j][i] = "1";
				} else if (pathij == true && pathji == true) {
					m.matrix[i][j] = "*";
					m.matrix[j][i] = "*";
				} else {
					m.matrix[i][j] = "*";
					m.matrix[j][i] = "*";
					/*
					 * List<OPNode> xorList1= new ArrayList<OPNode>();
					 * List<OPNode> xorList2= new ArrayList<OPNode>(); xorList1
					 * = BFSType (sames.get(i), "xor", graph); xorList2 =
					 * BFSType (sames.get(j), "xor", graph);
					 * //System.out.println(xorList1.size()); for(OPNode n :
					 * xorList1) if(xorList2.contains(n)){ m.matrix[i][j] = "-";
					 * m.matrix[j][i] = "-"; //System.out.println("----------");
					 * break; }
					 * 
					 * List<OPNode> andList1= new ArrayList<OPNode>();
					 * List<OPNode> andList2= new ArrayList<OPNode>(); andList1
					 * = BFSType (sames.get(i), "and", graph); andList2 =
					 * BFSType (sames.get(j), "and", graph); for(OPNode n :
					 * andList1) if(andList2.contains(n)){ m.matrix[i][j] = "*";
					 * m.matrix[j][i] = "*"; //System.out.println("----------");
					 * break; }
					 */
				}
			}

		return m;
	}

	/**
	 * Find the move item
	 * 
	 * @param ms
	 * @return
	 */
	public static List<String> findMoveItem(List<Matrix> ms) {
		QMC qmc = new QMC();
		List<String> moveItem = new ArrayList<String>();

		for (Matrix t : ms) {
			if (t.n == 0)
				continue;
			// System.out.println("findMoveItem Function");

			QuineMcCluskey qm = new QuineMcCluskey();
			List<String> QMoutput = new ArrayList<String>();
			for (int i = 0; i < t.n; i++) {
				String line = "";
				for (int j = 0; j < t.n; j++) {
					line = line + t.matrix[i][j];
				}
				try {
					boolean flag = false;
					for (char a : line.toCharArray()) {
						if (a != '0') {
							flag = true;
							break;
						}
					}
					if (flag)
						qm.addTerm(line);
				} catch (ExceptionQuine e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				qm.simplify();
			} catch (ExceptionQuine e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			qmc.writeOutputFile(qm);
			for (int i = 0; i < qm.count; i++) {
				try {
					QMoutput.add(qm.getTerm(i));
				} catch (ExceptionQuine e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			findMinimalItem(QMoutput, t, moveItem);
		}
		// System.out.println("1: "+moveItem);
		return moveItem;
	}

	/**
	 * Find the minimal pair
	 * 
	 * @param output
	 * @param t
	 * @return
	 */
	public static void findMinimalItem(List<String> output, Matrix t,
			List<String> moveItem) {
		String mins = "";
		int min = Integer.MAX_VALUE;
		// System.out.println("findMinimalItem Function");
		for (String temp : output) {
			int count = 0;
			if (temp.length() > 0) {
				for (int i = 0; i < temp.length(); i++) {
					if (temp.charAt(i) == 1) {
						count++;
					}
				}
				if (count < min) {
					min = count;
					mins = temp;
				}
			}
		}
		if (mins.length() > 0) {
			for (int i = 0; i < mins.length(); i++) {
				if (mins.charAt(i) == '1') {
					moveItem.add(t.getNames().get(i));
				}
			}
		}
		// System.out.println("2: "+moveItem);
	}

	/**
	 * logic operation
	 * 
	 * @param ls
	 * @param ls2
	 * @return
	 */
	private static ArrayList<String> diff(ArrayList<String> ls1,
			ArrayList<String> ls2) {
		ArrayList<String> list = new ArrayList<String>();
		// list.addAll(ls1);
		// list.removeAll(ls2);
		List<String> list1 = new ArrayList<String>(ls1);
		List<String> list2 = new ArrayList<String>(ls2);
		List<String> temp = new ArrayList<String>();

		for (int i = 0; i < list1.size(); i++)
			for (int j = 0; j < list2.size(); j++)
				if (list1.get(i).equals(list2.get(j))) {
					list2.remove(j);
					temp.add(list1.get(i));
					break;
				}
		for (int i = 0; i < temp.size(); i++)
			for (int j = 0; j < list1.size(); j++) {
				if (temp.get(i).equals(list1.get(j))) {
					list1.remove(j);
					break;
				}
			}
		list.addAll(list1);
		return list;
	}

	/**
	 * Delete nodes in graph1 but not in graph2
	 * 
	 * @param graph1
	 * @param graph2
	 * @return
	 */
	public static ArrayList<String> delete(OPGraph graph1, OPGraph graph2) {
		ArrayList<String> del = new ArrayList<String>();
		ArrayList<String> dif = diff(graph1.getLabelList(),
				graph2.getLabelList());
		for (String st : dif) {
			String t = "";
			t = t + "delete(S," + st + ")";
			del.add(t);
		}
		// System.out.println(dif);
		return del;
	}

	/**
	 * Move the common nodes to the same positions
	 * 
	 * @param graph1
	 * @param graph2
	 * @return
	 */
	public static ArrayList<String> move(OPGraph graph1, OPGraph graph2,
			List<String> moveItem) {
		ArrayList<String> moves = new ArrayList<String>();

		for (String s : moveItem) {
			for (OPNode n : graph2.nodes) {
				if (s.equals(n.label)) {
					String t = "";
					// parent nodes
					if (n.getParent() != null) {
						if (n.getParent().size() == 1) {
							t = "move(S," + n.label + ","
									+ n.getParent().get(0).label + ",";
						} else if (n.getParent().size() >= 1) {
							String temp = "";
							for (OPNode nodep : n.getParent()) {
								if (temp.length() == 0) {
									temp = temp + nodep.label;
								} else {
									temp = temp + "," + nodep.label;
								}
							}

							t = "move(S," + n.label + "," + "{" + temp + "}"
									+ ",";
						}
					} else {
						t = "move(S," + n.label + "," + "startNode" + ",";
					}

					// child nodes
					if (n.getChildren() != null) {
						if (n.getChildren().size() == 1) {
							t = t + n.getChildren().get(0).label + ")";
						} else if (n.getChildren().size() >= 1) {
							String temp = "";
							for (OPNode nodec : n.getChildren()) {
								if (temp.length() == 0) {
									temp = temp + nodec.label;
								} else {
									temp = temp + "," + nodec.label;
								}
							}
							t = t + "{" + temp + "}" + ")";
						}
					} else {
						t = t + "endNode" + ")";
					}
					moves.add(t);
				}
			}
		}
		return moves;
	}

	/**
	 * Insert nodes in graph2 but not in graph1
	 * 
	 * @param graph1
	 * @param graph2
	 * @return
	 */
	private static List<String> insert(OPGraph graph1, OPGraph graph2,
			List<OPNode> nodeResult) {

		List<String> insertList = new ArrayList<String>();
		ArrayList<String> diffList = diff(graph2.getLabelList(),
				graph1.getLabelList());
		HashMap<String, Boolean> mapID2 = new HashMap<String, Boolean>();

		for (OPNode v : graph2.nodes)
			mapID2.put(v.id, false);
		for (OPNode v : nodeResult)
			mapID2.put(v.id, true);

		OPNode v = new OPNode();
		for (int i = 0; i < diffList.size(); i++) {
			String operation = "insert(S," + diffList.get(i) + ",";
			for (int j = 0; j < graph2.nodes.size(); j++) {
				v = graph2.nodes.get(j);
				if (diffList.get(i).equals(v.label)
						&& mapID2.get(v.id) == false) {
					mapID2.put(v.id, true);
					String parentNode = "", childNode = "";

					for (int k = 0; k < v.parents.size(); k++)
						parentNode += v.parents.get(k).label + ",";
					if (!parentNode.isEmpty())
						parentNode = parentNode.substring(0,
								parentNode.length() - 1);

					for (int k = 0; k < v.children.size(); k++)
						childNode += v.children.get(k).label + ",";
					if (!childNode.isEmpty())
						childNode = childNode.substring(0,
								childNode.length() - 1);

					operation += "{" + parentNode + "}," + "{" + childNode
							+ "})";
					insertList.add(operation);
					break;
				}
			}
		}
		return insertList;
	}
}