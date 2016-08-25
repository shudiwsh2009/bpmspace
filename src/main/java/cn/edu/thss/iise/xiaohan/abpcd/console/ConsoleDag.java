package cn.edu.thss.iise.xiaohan.abpcd.console;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag.db.DBHelperDb;
import de.bpt.hpi.graph.Pair;

public class ConsoleDag {

	private HashMap<Integer, String> idToNameMap = new HashMap<Integer, String>();
	private HashMap<Integer, DagNode> idToDagMap = new HashMap<Integer, DagNode>();
	public static HashMap<Integer, String> idToHashMap = new HashMap<Integer, String>();
	private DBHelperDb dbHelper;

	public void createConsoleDat(DBHelperDb dbHelper) {
		this.dbHelper = dbHelper;
		createDag();
	}

	public ConsoleDag(DBHelperDb dbHelper) {
		this.dbHelper = dbHelper;

		createDag();
	}

	private void createDag() {
		// add the model names to map
		for (Entry<String, Integer> e : dbHelper.getRoots().entrySet()) {
			idToNameMap.put(e.getValue(), e.getKey());
		}

		for (Entry<Integer, LinkedList<Integer>> e : dbHelper
				.getParentChildMap().entrySet()) {
			DagNode parent = idToDagMap.get(e.getKey());

			if (parent == null) {
				parent = new DagNode();
				idToDagMap.put(e.getKey(), parent);
			}
			// process children
			for (Integer c : e.getValue()) {
				DagNode child = idToDagMap.get(c);

				if (child == null) {
					child = new DagNode();
					idToDagMap.put(c, child);
				}
				child.addParent(e.getKey());
			}
		}

		for (Entry<String, Pair> e : dbHelper.getLabelHash().entrySet()) {
			idToHashMap.put(e.getValue().getFirst(), e.getKey());
		}
	}

	public HashSet<String> getModels(Integer nodeId) {

		HashSet<String> toReturn = new HashSet<String>();
		if (!idToDagMap.containsKey(nodeId)) {
			System.out.println("Node not found " + nodeId);
			return toReturn;
		}

		LinkedList<Integer> toProcess = new LinkedList<Integer>();
		HashSet<Integer> processed = new HashSet<Integer>();
		toProcess.add(nodeId);

		while (toProcess.size() > 0) {
			Integer i = toProcess.removeFirst();
			processed.add(i);
			if (idToNameMap.containsKey(i)) {
				toReturn.add(idToNameMap.get(i));
			}

			DagNode d = idToDagMap.get(i);
			if (d == null) {
				continue;
			}

			for (Integer p : d.getParents()) {
				if (!toProcess.contains(p) && !processed.contains(p)) {
					toProcess.add(p);
				}
			}
		}
		return toReturn;
	}
}
