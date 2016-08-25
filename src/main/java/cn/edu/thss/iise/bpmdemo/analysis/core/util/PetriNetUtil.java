/**
 * BeehiveZ is a business process model and instance management system.
 * Copyright (C) 2011  
 * Institute of Information System and Engineering, School of Software, Tsinghua University,
 * Beijing, China
 *
 * Contact: jintao05@gmail.com 
 *
 * This program is a free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation with the version of 2.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

/**
 * only contains static functions
 * used for petri net analysis
 */
package cn.edu.thss.iise.bpmdemo.analysis.core.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Vector;

import org.processmining.framework.models.ModelGraphVertex;
import org.processmining.framework.models.petrinet.PNNode;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.StateSpace;
import org.processmining.framework.models.petrinet.Token;
import org.processmining.framework.models.petrinet.Transition;
import org.processmining.framework.models.petrinet.algorithms.CoverabilityGraphBuilder;
import org.processmining.framework.models.petrinet.algorithms.PnmlWriter;
import org.processmining.importing.pnml.PnmlImport;
import org.processmining.mining.petrinetmining.PetriNetResult;

import att.grappa.Edge;
import att.grappa.Node;

/**
 * @author Tao Jin
 * 
 */
public class PetriNetUtil {
	private static final boolean debug = false;

	public static byte[] getPnmlBytes(PetriNet pn) {
		byte[] ret = null;
		try {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			OutputStreamWriter ow = new OutputStreamWriter(bos);
			BufferedWriter bw = new BufferedWriter(ow);

			PnmlWriter.write(false, true, pn, bw);

			bw.close();
			ow.close();
			ret = bos.toByteArray();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ret;
	}

	public static PetriNet getPetriNetFromPnmlBytes(byte[] pnml) {
		PetriNet pn = null;
		try {
			ByteArrayInputStream bis = new ByteArrayInputStream(pnml);
			pn = getPetriNetFromPnml(bis);
			bis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pn;
	}

	public static void export2pnml(PetriNet pn, String filename) {
		if (filename != null && !filename.equals("")) {
			try {
				FileWriter fw = new FileWriter(filename, false);
				BufferedWriter bw = new BufferedWriter(fw);
				PnmlWriter.write(false, true, pn, bw);
				bw.close();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static PetriNet getPetriNetFromPnmlFile(File file) {
		PetriNet pn = null;
		try {
			FileInputStream in = new FileInputStream(file);
			pn = getPetriNetFromPnml(in);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pn;
	}

	public static PetriNet getPetriNetFromPnmlFile(String fileName) {
		PetriNet pn = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			pn = getPetriNetFromPnml(in);
			in.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pn;
	}

	public static PetriNet getPetriNetFromPnml(InputStream pnml) {
		PetriNet pn = null;
		try {
			PnmlImport pnmlImport = new PnmlImport();
			PetriNetResult result;
			result = (PetriNetResult) pnmlImport.importFile(pnml);
			pn = result.getPetriNet();
			result.destroy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pn;
	}

	public static boolean isInLengthOneLoop(Transition t) {
		HashSet inPlaces = t.getPredecessors();
		HashSet outPlaces = t.getSuccessors();
		if (inPlaces.size() == 0 || outPlaces.size() == 0) {
			return false;
		}

		inPlaces.retainAll(outPlaces);
		if (inPlaces.size() == 0) {
			return false;
		} else {
			return true;
		}

	}

	public static boolean isInLengthTwoLoop(Transition t1, Transition t2) {
		HashSet inPlaces1 = t1.getPredecessors();
		HashSet outPlaces1 = t1.getSuccessors();
		HashSet inPlaces2 = t2.getPredecessors();
		HashSet outPlaces2 = t2.getSuccessors();

		if (inPlaces1.size() == 0 || outPlaces1.size() == 0
				|| inPlaces2.size() == 0 || outPlaces2.size() == 0) {
			return false;
		}

		outPlaces1.retainAll(inPlaces2);
		outPlaces2.retainAll(inPlaces1);

		if (outPlaces1.size() == 0 || outPlaces2.size() == 0) {
			return false;
		} else {
			return true;
		}
	}

	public static boolean existCommunication(Transition startTransition,
			Transition endTransition) {
		if (debug) {
			System.out.println("existCommunication begin");
		}
		Iterator it1 = startTransition.getSuccessors().iterator();
		HashSet set2 = endTransition.getPredecessors();
		while (it1.hasNext()) {
			Place pSuc1 = (Place) it1.next();
			if (set2.contains(pSuc1)) {
				return true;
			}
		}
		return false;
	}

	public static boolean existSynchronization(Place p1, Place p2) {
		if (debug) {
			System.out.println("existSynchronization begin");
		}
		Iterator it1 = p1.getSuccessors().iterator();
		HashSet set2 = p2.getSuccessors();
		while (it1.hasNext()) {
			Transition tSuc1 = (Transition) it1.next();
			if (set2.contains(tSuc1)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * how many edges exist in the path from startNode to endNode, breadth first
	 * traverse
	 * 
	 * @param startNode
	 * @param endNode
	 * @return
	 */
	public static int maxLength(PNNode startNode, PNNode endNode) {
		int len = 0;
		int level = 0;
		boolean found = false;
		HashSet<PNNode> visited = new HashSet<PNNode>();
		ArrayList<PNNode> queue = new ArrayList<PNNode>();
		queue.add(startNode);
		while (!queue.isEmpty()) {
			boolean record = false;
			ArrayList<PNNode> newQueue = new ArrayList<PNNode>();
			for (PNNode node : queue) {
				visited.add(node);
				if (node == endNode) {
					record = true;
					found = true;
				}
				Iterator<PNNode> itnsuc = node.getSuccessors().iterator();
				while (itnsuc.hasNext()) {
					PNNode newNode = itnsuc.next();
					if (!visited.contains(newNode)) {
						newQueue.add(newNode);
					}
				}
			}
			if (record) {
				len = level;
			}
			level++;
			queue = newQueue;
		}

		if (found) {
			return len;
		} else {
			return Integer.MIN_VALUE;
		}
	}

	public static boolean isParallel(Transition t1, Transition t2, PetriNet pn) {
		PNNode n = nearestCommonAncestor(t1, t2, pn);
		if (n != t1 && n != t2 && n instanceof Transition) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * the input Petri net must be a workflow net, node1 and node2 must belong
	 * to the same workflow net
	 */
	public static PNNode nearestCommonAncestor(PNNode node1, PNNode node2,
			PetriNet pn) {
		HashMap<PNNode, Integer> levels = numberLevelOfNodes(pn);

		// the nodes in the queue are descending ordered by level
		// the queue with bigger level
		ArrayList<PNNode> queue1 = new ArrayList<PNNode>();

		// the queue with smaller level
		ArrayList<PNNode> queue2 = new ArrayList<PNNode>();

		queue1.add(node1);
		queue2.add(node2);

		while (true) {

			int level1 = levels.get(queue1.get(0));
			int level2 = levels.get(queue2.get(0));

			while (level1 != level2) {
				// the smaller level
				int level = level1 > level2 ? level2 : level1;

				if (level == level1) {
					ArrayList<PNNode> q = queue1;
					queue1 = queue2;
					queue2 = q;
				}

				// deal with the queue with bigger level
				while (queue1.size() > 0) {
					PNNode n = queue1.get(0);
					int l = levels.get(n);
					if (l > level) {
						queue1.remove(0);
						Iterator<PNNode> itPre = n.getPredecessors().iterator();
						while (itPre.hasNext()) {
							PNNode pre = itPre.next();
							int preLevel = levels.get(pre);
							if (preLevel >= l) {
								continue;
							}
							int k = 0;
							for (k = 0; k < queue1.size(); k++) {
								PNNode temp = queue1.get(k);
								int tempLevel = levels.get(temp);
								if (preLevel > tempLevel) {
									break;
								}
							}
							queue1.add(k, pre);
						}
					} else {
						break;
					}
				}

				level1 = levels.get(queue1.get(0));
				level2 = levels.get(queue2.get(0));
			}

			// level1 == level2
			for (int i = 0; i < queue1.size(); i++) {
				PNNode n1 = queue1.get(i);
				if (levels.get(n1) < level1) {
					break;
				}
				for (int j = 0; j < queue2.size(); j++) {
					PNNode n2 = queue2.get(j);
					if (levels.get(n2) < level2) {
						break;
					}
					if (n1 == n2) {
						return n1;
					}
				}
			}

			// delete the nodes with equal level and add the predecessor nodes
			int l1 = levels.get(queue1.get(0));
			while (l1 == level1) {
				PNNode n1 = queue1.get(0);
				queue1.remove(0);
				Iterator<PNNode> itPre = n1.getPredecessors().iterator();
				while (itPre.hasNext()) {
					PNNode pre = itPre.next();
					int preLevel = levels.get(pre);
					if (preLevel >= l1) {
						continue;
					}
					int k = 0;
					for (k = 0; k < queue1.size(); k++) {
						PNNode temp = queue1.get(k);
						int tempLevel = levels.get(temp);
						if (preLevel > tempLevel) {
							break;
						}
					}
					queue1.add(k, pre);
				}
				l1 = levels.get(queue1.get(0));
			}

			int l2 = levels.get(queue2.get(0));
			while (l2 == level2) {
				PNNode n2 = queue2.get(0);
				queue2.remove(0);
				Iterator<PNNode> itPre = n2.getPredecessors().iterator();
				while (itPre.hasNext()) {
					PNNode pre = itPre.next();
					int preLevel = levels.get(pre);
					if (preLevel >= l2) {
						continue;
					}
					int k = 0;
					for (k = 0; k < queue2.size(); k++) {
						PNNode temp = queue2.get(k);
						int tempLevel = levels.get(temp);
						if (preLevel > tempLevel) {
							break;
						}
					}
					queue2.add(k, pre);
				}
				l2 = levels.get(queue2.get(0));
			}

		}
	}

	private static HashMap<PNNode, Integer> numberLevelOfNodes(PetriNet pn) {
		HashMap<PNNode, Integer> result = new HashMap<PNNode, Integer>();

		// find the start place
		Place startPlace = null;
		for (Place p : pn.getPlaces()) {
			if (p.inDegree() == 0) {
				startPlace = p;
				break;
			}
		}

		if (startPlace != null) {
			LinkedList<PNNode> queue = new LinkedList<PNNode>();
			queue.add(startPlace);
			result.put(startPlace, 0);
			while (queue.size() > 0) {
				PNNode node = queue.poll();
				int level = result.get(node);
				Iterator<PNNode> itSuc = node.getSuccessors().iterator();
				while (itSuc.hasNext()) {
					PNNode suc = itSuc.next();
					if (!result.containsKey(suc)) {
						result.put(suc, level + 1);
						queue.add(suc);
					}
				}
			}
		}

		return result;
	}

	/**
	 * used to check whether the start node can reach the end node in the Petri
	 * net
	 */
	public static boolean canReach(PNNode startNode, PNNode endNode) {
		if (debug) {
			System.out.println("canReach begin");
		}
		// search by breadth-first
		// use a queue
		// if the queue is empty, the check is end

		// initialize the queue
		if (startNode == endNode) {
			if (debug) {
				System.out.println("canReach end");
			}
			return true;
		}

		LinkedList<PNNode> queue = new LinkedList<PNNode>();
		Iterator it = startNode.getSuccessors().iterator();
		while (it.hasNext()) {
			PNNode node = (PNNode) it.next();
			queue.offer(node);
		}

		// check
		PNNode node = queue.poll();
		Vector<PNNode> visited = new Vector<PNNode>();
		while (node != null) {
			visited.add(node);
			if (node == endNode) {
				if (debug) {
					System.out.println("canReach end");
				}
				return true;
			} else {
				Iterator it1 = node.getSuccessors().iterator();
				while (it1.hasNext()) {
					PNNode suc = (PNNode) it1.next();
					if (!visited.contains(suc) && !queue.contains(suc)) {
						queue.offer(suc);
					}
				}
				node = queue.poll();
			}
		}

		if (debug) {
			System.out.println("canReach end");
		}
		return false;
	}

	/**
	 * @param pn
	 *            workflow net ignoring all the tokens it has
	 * @return
	 */
	public static StateSpace buildCoverabilityGraph(PetriNet pn, int depth) {
		for (Place place : pn.getPlaces()) {
			place.removeAllTokens();
			if (place.inDegree() == 0) {
				place.addToken(new Token());
			}
		}
		return CoverabilityGraphBuilder.build(pn, depth);
	}

	public static StateSpace buildCoverabilityGraph(PetriNet pn) {
		for (Place place : pn.getPlaces()) {
			place.removeAllTokens();
			if (place.inDegree() == 0) {
				place.addToken(new Token());
			}
		}
		return CoverabilityGraphBuilder.build(pn);
	}

	/**
	 * Get the Number of Non-Free Choice of Petri Net.
	 * 
	 * @return the number
	 */
	public static int getNumberofNonFreeChoice(PetriNet pn) {
		int nonfreechoice = 0;
		for (Place p : pn.getPlaces())
			if (p.outDegree() > 1) {
				HashSet<ModelGraphVertex> sucNodes = p.getSuccessors();
				for (ModelGraphVertex tnode : sucNodes) {
					HashSet<ModelGraphVertex> preNodes = tnode
							.getPredecessors();
					if (preNodes.size() > 1)
						nonfreechoice++;

				}
			}
		return nonfreechoice;
	}

	/**
	 * Get the Number of Arbituary Cycle of the Petri Net
	 * 
	 * @return the number
	 */
	public static int getNumberofArbitaryCycle(PetriNet pn) {
		int arbitaryCycle = 0;
		List<List<Node>> cycleList = getCycleList(pn);
		// cycleList = trimCycle(cycleList);
		for (List<Node> cycle : cycleList) {
			int outdegree = 0;
			int indegree = 0;
			for (Node n : cycle) {
				outdegree += n.outDegree() - 1;
				indegree += n.inDegree() - 1;
			}
			if (outdegree + indegree > 2) // 鍥犱负鍑哄彛鍏ュ彛姝ゅ埢蹇呴』鑷冲皯涓�
				arbitaryCycle++;
		}
		return arbitaryCycle;
	}

	/**
	 * Get the Number of Or-Join of the Petri Net
	 * 
	 * @return the number
	 */
	public static int getNumberofOrJoin(PetriNet pn) {
		int result = 0;
		for (Place p : pn.getPlaces()) {
			boolean isOrJoin = false;
			if (p.getPredecessors().size() < 3)
				continue;
			for (Transition t : (HashSet<Transition>) p.getPredecessors())
				if (getdupFuntionTransition(pn, t, p) >= 2) {
					isOrJoin = true;
					break;
				}
			if (isOrJoin == true)
				result++;
		}
		return result;
	}

	/**
	 * Get the Number of Simple Loop of the Petri Net
	 * 
	 * @return the number
	 */
	public static int getNumberofSimpleLoop(PetriNet pn) {
		List<List<Node>> cycleList = getCycleList(pn);
		// cycleList = trimCycle(cycleList);
		return cycleList.size();
	}

	/**
	 * Get the Number of Nested Loop of the Petri Net
	 * 
	 * @return the number
	 */
	public static int getNumberofNestedLoop(PetriNet pn) {
		int nestedSloopCount = 0;
		if (pn.getNumOfCycle() > 1) {

			List<List<Node>> cycleList = getCycleList(pn);
			// cycleList = trimCycle(cycleList);
			for (int i = 0; i < cycleList.size() - 1; i++) {
				int joinNum = 0; // 涓や釜cycleList鐩镐氦閮ㄥ垎鐨勪釜鏁�
				for (int j = i + 1; j < cycleList.size(); j++) {
					List<Node> singleCycleList1 = cycleList.get(i);
					List<Node> singleCycleList2 = cycleList.get(j);
					if (singleCycleList1.size() == singleCycleList2.size()) // 濡傛灉涓や釜鐨勫惊鐜殑鏁伴噺鏄竴鏍峰鐨勶紝閭ｄ箞灏卞綋鍋氬寘鍚簡
						break;
					int contilTag = 0; // 鏄惁杩炵画鐨勬爣蹇�
					int joinTag = 0; // 鏄惁鐩镐氦
					int firstITag = 0; // 绗竴涓槸鍚︽槸椤归摼鐨勬爣蹇�
					for (Node iNode : singleCycleList1) {
						joinTag = 0;
						for (Node jNode : singleCycleList2) {
							if (jNode.equals(iNode)) {
								joinTag = 1;
								break;
							}
						}
						if (singleCycleList1.indexOf(iNode) == 0
								&& joinTag == 1)
							firstITag = 1;
						if (joinTag == 0 && contilTag == 1) {
							contilTag = 0;
							joinNum++;
						}
						if (joinTag == 1 && contilTag == 0) {
							contilTag = 1;
						}
						if (joinTag == 1
								&& contilTag == 1
								&& singleCycleList1.indexOf(iNode) == (singleCycleList1
										.size() - 1) && firstITag == 0) // i鏄渶鍚庝竴涓暟瀛楃殑鎯呭喌锛岃繖鏍峰鏋滅涓�釜鍏冪礌涓嶆槸鐩镐氦鐨勶紝閭ｄ箞闇�灏嗘渶鍚庝竴涓厓绱犵浉浜�
						{
							joinNum++;
						}

					}

				}
				if (joinNum == 1) {
					nestedSloopCount++;
				}
			}
		}
		return nestedSloopCount;
	}

	/**
	 * Get the list of cycles of the PetriNet, of which a cycle is represented
	 * by a list of nodes.
	 * 
	 * @return the list of the cycles
	 */
	public static List<List<Node>> getCycleList(PetriNet pn) {
		List<List<Node>> result = new ArrayList<List<Node>>();

		ArrayList<Node> queue = new ArrayList<Node>();
		List<Node> visitedNodes = new ArrayList<Node>();
		List<Integer> preList = new ArrayList<Integer>();
		for (Node node : pn.getNodes()) {
			Node ini = node;
			visitedNodes = new ArrayList<Node>();
			preList = new ArrayList<Integer>();
			queue = new ArrayList<Node>();
			queue.add(node);
			preList.add(-1);
			int head = 0;
			while (queue.size() > head) {
				Node x = queue.get(head);
				visitedNodes.add(x);
				List<Edge> outgoingArcs = x.getOutEdges();
				if (outgoingArcs == null)
					outgoingArcs = new LinkedList<Edge>();
				for (Edge edge : outgoingArcs) {

					Node opposite = edge.getHead();
					if (opposite.equals(ini)) {
						// to add a new cycle list
						ArrayList<Node> singleCycleList = new ArrayList<Node>();
						int prePointer = head;
						while (prePointer != -1) {
							singleCycleList.add(queue.get(prePointer));
							prePointer = preList.get(prePointer);
						}
						// cycleNodes.add(opposite);
						result.add(singleCycleList);
					}
					if (!visitedNodes.contains(opposite)) {
						queue.add(opposite);
						preList.add(head);
					}
				}
				head++;

			}
		}
		result = trimCycle(result);
		return result;
	}

	/**
	 * Delete the duplicate cycles in a list of cycles
	 * 
	 * @param originalCycleList
	 *            to duplicate cycles
	 * @return the list of the cycles without duplication
	 */
	private static List<List<Node>> trimCycle(List<List<Node>> originalCycleList) {
		List<List<Node>> result = new ArrayList<List<Node>>();
		for (List<Node> toAddCycle : originalCycleList) {
			boolean isOKTag = true;
			for (List<Node> addedCycle : result) {
				if (addedCycle.size() != toAddCycle.size())
					continue;
				boolean notContainTag = false;
				for (Node n : toAddCycle)
					if (!addedCycle.contains(n)) {
						notContainTag = true;
						break;
					}
				if (!notContainTag) {
					isOKTag = false;
					break;
				}
			}
			if (isOKTag)
				result.add(toAddCycle);
		}
		return result;
	}

	/**
	 * Get the number of the places, which has at least two transitions as
	 * successors, and the successors transitions comply with the following
	 * rules: 1. There should be the input transition in the successors. 2.
	 * There should be at least another transition which has the input place as
	 * the successors.
	 * 
	 * @param t
	 *            input transition
	 * @param p
	 *            input place
	 * @return number
	 */
	private static int getdupFuntionTransition(PetriNet pn, Transition t,
			Place p) {
		int result = 0;
		for (Transition tpeer : pn.getTransitions()) {
			if (tpeer.equals(t))
				continue;
			if (!((HashSet<Place>) tpeer.getSuccessors()).contains(p))
				continue;
			for (Place phead : (HashSet<Place>) tpeer.getPredecessors())
				if (((HashSet<Transition>) phead.getSuccessors()).contains(t)) {
					result++;
					break;
				}
		}
		return result;
	}

}
