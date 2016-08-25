package cn.edu.thss.iise.xiaohan.abpcd.graph.rpsdag;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.Fragment;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.FragmentProcess;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.Stats;
import cn.edu.thss.iise.xiaohan.abpcd.mtree.index.Timer;
import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;
import de.bpt.hpi.ogdf.rpst.ExpRPST;
import de.bpt.hpi.ogdf.spqr.TreeNode;

public class DBSCAN {

	public class Record {
		public List<String> modelfiles;
		public Boolean origin;

		public Record() {
			this.modelfiles = new ArrayList<String>();
			this.origin = false;
		}

		public Record(Fragment fg) {
			this.modelfiles = new ArrayList<String>();
			this.modelfiles = fg.getModelFiles();
			this.origin = fg.origin;
		}

		public void addRecord(Record r) {
			this.modelfiles.addAll(r.modelfiles);
			this.origin = r.origin;
		}
	};

	private int modelnumbers;
	private int fragmentnumbers;
	public Set<Fragment> setfragment;
	public List<Graph> listgraph;
	public Map<Fragment, Record> mapfragment;

	// Create MTree, insert fragments and other operations about MTree
	public Stats stats;
	private static int minsize = 8;
	private static int threshold = 2;
	private static int mingraphsize = 12;

	public DBSCAN() {
		this.modelnumbers = 0;
		this.fragmentnumbers = 0;
		this.setfragment = new HashSet<Fragment>();
		this.listgraph = new LinkedList<Graph>();
		this.mapfragment = new HashMap<Fragment, Record>();
	}

	public void createMTree() {
		statistics();

		// The parameter could be changed,and MTree.DEFAULT_MIN_NODE_CAPACITY =
		// 50
		stats = new Stats();
		stats.createMTree(40);

		serialNumber();

		int i = 0;
		for (Fragment f : setfragment) {
			stats.insertMTree(f);
			if (++i % 100 == 0)
				System.out.println("Add fragment: " + i);
		}
		System.err.println("All fragments number: " + i);
	}

	public void statistics() {
		int clone = 0;
		int min = 5, max = 5;
		int size = 0;
		double avg = 0, number = 0;
		for (Fragment g : setfragment) {
			number++;
			if (g.modelfiles.size() > 1)
				clone++;

			size = g.graph.getVertices().size();
			if (size < min)
				min = size;
			if (size > max)
				max = size;
			avg += size;
		}
		DecimalFormat df = new DecimalFormat("0.00");
		avg /= number;
		System.err.println("Min node Number: " + min);
		System.err.println("Max node Number: " + max);
		System.err.println("Avg node Number: " + df.format(avg));
		System.err.println("Clone Number   : " + clone + "\n");
	}

	public void recordFragments(ExpRPST tree, String filename) {
		this.modelnumbers++;
		Graph origingraph = tree.getOriginalGraph();

		for (TreeNode node : tree.getNodes()) {
			Graph graph = new Graph();
			Graph temp = node.getOriginalGraph();

			if (temp.getEdges().size() == 0) {
				System.err.println("This graph Don't have any edge!");
				continue;
			}
			for (Edge e : temp.getEdges()) { // Many graphs are incomplete!
				graph.addVertex(e.getFirst(),
						origingraph.getLabel(e.getFirst()));
				graph.addVertex(e.getSecond(),
						origingraph.getLabel(e.getSecond()));
				graph.addEdge(e.getFirst(), e.getSecond());
			}

			listgraph.add(graph);
			Fragment fragment = new Fragment(graph, filename);
			if (graph.getVertices().size() == origingraph.getVertices().size()) {
				fragment.setOriginGraph(true);
			}

			Record r = new Record(fragment);
			if (setfragment.contains(fragment)) {
				setfragment.remove(fragment);
				r.addRecord(mapfragment.get(fragment));

				fragment.setModelFiles(r.modelfiles);
				fragment.setOriginGraph(r.origin);
			}
			setfragment.add(fragment);
			mapfragment.put(fragment, r);
		}
	}

	public void cluster() {
		System.err.println("Clustering...");
		Stats stats = new Stats();
		Set<Fragment> setNoise = new HashSet<Fragment>();
		Set<Fragment> setCluster = new HashSet<Fragment>();
		Map<Fragment, Boolean> mapProcess = new HashMap<Fragment, Boolean>();

		for (Fragment fg : setfragment) {
			mapProcess.put(fg, false);
			if (fg.origin || fg.graph.getVertices().size() > mingraphsize) {
				mapProcess.put(fg, true);
				setNoise.add(fg);
			}
		}

		for (Fragment fg : setfragment) {
			if (mapProcess.get(fg) == true) {
				if (fg.graph.getVertices().size() > mingraphsize)
					System.out.println(fg.ID + "  Large graph");
				else if (fg.origin)
					System.out.println(fg.ID + "  Origin graph");
				continue;
			}

			mapProcess.put(fg, true);
			List<Fragment> fgNeighbor = new ArrayList<Fragment>();
			fgNeighbor = stats.getNearestByRange(fg, threshold);

			if (fgNeighbor.size() < minsize) {
				setNoise.add(fg);
				System.out.println(fg.ID + "  Noise " + fgNeighbor.size());
			} else {
				setCluster.add(fg);
				System.out.println(fg.ID + "  Cluster " + fgNeighbor.size());
				for (Fragment m : fgNeighbor) {
					if (m.origin
							|| m.getGraph().getVertices().size() > mingraphsize)
						continue;

					mapProcess.put(m, true);
					List<Fragment> mNeighbor = new ArrayList<Fragment>();
					mNeighbor = stats.getNearestByRange(m, threshold);
					if (mNeighbor.size() < minsize) {
						setNoise.add(m);
						System.out
								.println(m.ID + "  Noise " + mNeighbor.size());
					}
				}// for
			}
		}// for
		System.err.println("Cluster Number  : " + setCluster.size());
		System.err.println("Noise Number    : " + setNoise.size());
		System.err.println("Optimized Number: "
				+ (setCluster.size() + setNoise.size()));
		System.err.println("Remove fragments...");

		Timer t = new Timer();
		int i = 0;
		for (Fragment fg : setfragment) {
			if (!setCluster.contains(fg) && !setNoise.contains(fg)) {
				stats.removeMTree(fg);
				i++;
			}
		}
		Timer.Times times = t.getTimes();
		System.err.println("Remove fragments: " + i);
		System.err.println("RemoveTime = " + times.real / 1000.0);
	}

	public void recall(List<String> listFile, List<Set<Fragment>> listAfter,
			List<Set<Fragment>> listBefore) {
		double avgRecall = 0.00;
		DecimalFormat df = new DecimalFormat("0.00");
		List<Double> target = new ArrayList<Double>();
		System.out.println("The Recall test...");

		for (int i = 0; i < listAfter.size(); i++) {
			target.add(0.0);
			for (Fragment fg : listAfter.get(i)) {
				if (listBefore.get(i).contains(fg)) {
					double t = target.get(i) + 1;
					target.set(i, t);
				}
			}
			double rec;
			if (listBefore.get(i).size() == 0) {
				System.out.println("Attention, the distance is short!");
				rec = 1;
			} else
				rec = target.get(i) / listBefore.get(i).size();

			String filepath = listFile.get(i);
			String filename = filepath
					.substring(filepath.lastIndexOf("\\") + 1);

			avgRecall += rec;
			System.out.println("Recall number " + i + "  " + filename + ":  "
					+ df.format(rec));
		}
		avgRecall = avgRecall / listAfter.size();
		System.out.println("\nThe number of models is " + listAfter.size()
				+ ", the average recall is " + df.format(avgRecall) + "\n\n");
	}

	public void precision(List<String> listFile, List<Set<Fragment>> listAfter,
			List<Set<Fragment>> listBefore) {
		double avgPre = 0.00;
		DecimalFormat df = new DecimalFormat("0.00");
		List<Double> target = new ArrayList<Double>();
		System.out.println("The Precision test...");

		for (int i = 0; i < listAfter.size(); i++) {
			target.add(0.0);
			for (Fragment fg : listAfter.get(i)) {
				if (listBefore.get(i).contains(fg)) {
					double t = target.get(i) + 1;
					target.set(i, t);
				}
			}
			double rec;
			if (listBefore.get(i).size() == 0) {
				System.out.println("Attention, the distance is short!");
				rec = 1;
			} else
				rec = target.get(i) / listBefore.get(i).size();

			String filepath = listFile.get(i);
			String filename = filepath
					.substring(filepath.lastIndexOf("\\") + 1);

			avgPre += rec;
			System.out.println("Precision number " + i + "  " + filename
					+ ":  " + df.format(rec));
		}
		avgPre = avgPre / listAfter.size();
		System.out.println("\nThe number of models is " + listAfter.size()
				+ ", the average Precision is " + df.format(avgPre) + "\n\n");
	}

	public List<Set<Fragment>> queryModelByRange(List<String> listFile) {
		FragmentProcess process = new FragmentProcess();
		List<Set<Fragment>> listResults = new ArrayList<Set<Fragment>>();

		for (String filepath : listFile) {
			process.process(filepath);

			int vertices = process.fragment.getGraph().getVertices().size();
			double distance = (double) vertices / 2 + 1;
			List<Fragment> fragments = stats.getNearestByRange(
					process.fragment, distance);

			Set<Fragment> setResult = new HashSet<Fragment>();
			for (Fragment fg : fragments) {
				setResult.add(fg);
			}
			listResults.add(setResult);
		}

		return listResults;
	}

	public List<Set<Fragment>> queryModelByNumber(List<String> listFile) {
		FragmentProcess process = new FragmentProcess();
		List<Set<Fragment>> listResults = new ArrayList<Set<Fragment>>();

		for (String filepath : listFile) {
			process.process(filepath);

			int vertices = process.fragment.getGraph().getVertices().size();
			double distance = (double) vertices / 2 + 1;
			List<Fragment> fragments = stats.getNearest(process.fragment,
					distance, 10);

			Set<Fragment> setResult = new HashSet<Fragment>();
			for (Fragment fg : fragments) {
				setResult.add(fg);
			}
			listResults.add(setResult);
		}

		return listResults;
	}

	public void printFragments() {
		for (Fragment fg : setfragment) {
			serializeToFile(fg.getGraph(), fg.getGraph().getVertices(),
					String.valueOf(fg.ID));
		}
	}

	public void printFragments(String outputFolder) {
		for (Fragment fg : setfragment) {
			serializeToFile(fg.getGraph(), fg.getGraph().getVertices(),
					String.valueOf(fg.ID), outputFolder);
		}
	}

	private void serialNumber() {
		this.fragmentnumbers = 0;
		for (Fragment fg : setfragment) {
			this.fragmentnumbers++;
			fg.ID = this.fragmentnumbers;
		}
	}

	private void serializeToFile(Graph graph, Collection<Integer> vertices,
			String code) {
		String fragname = "Fragment_" + code;
		try {
			// Create file
			FileWriter fstream = new FileWriter(System.getProperty("user.dir")
					+ "\\models\\fragments\\b3s\\" + fragname + ".dot");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("digraph " + fragname + " {\n");

			serializeHighlighted(graph, out, vertices);

			for (Edge edge : graph.getEdges()) {
				out.write("\tnode" + edge.getSource() + " -> node"
						+ edge.getTarget());
			}
			out.write("}");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void serializeToFile(Graph graph, Collection<Integer> vertices,
			String code, String outputFolder) {
		String fragname = "Fragment_" + code;
		try {
			// Create file
			FileWriter fstream = new FileWriter(outputFolder + fragname
					+ ".dot");
			BufferedWriter out = new BufferedWriter(fstream);
			out.write("digraph " + fragname + " {\n");

			serializeHighlighted(graph, out, vertices);

			for (Edge edge : graph.getEdges()) {
				out.write("\tnode" + edge.getSource() + " -> node"
						+ edge.getTarget());
			}
			out.write("}");
			// Close the output stream
			out.close();
		} catch (Exception e) {// Catch exception if any
			System.err.println("Error: " + e.getMessage());
		}
	}

	private void serializeHighlighted(Graph graph, BufferedWriter out,
			Collection<Integer> vertices) throws IOException {
		for (Integer id : vertices) {
			String label = graph.getLabel(id);

			if (label == null) {
				continue;
			}
			if (!label.toLowerCase().contains("join")
					&& !label.toLowerCase().contains("split")) { // findFunction
				out.write("\tnode"
						+ id
						+ " [shape=box, fillcolor=palegreen2, style=\"bold,rounded,filled\", label=\""
						+ label + "\"]\n");
			} else {
				if (label.toLowerCase().contains("andjoin")
						|| label.toLowerCase().contains("andsplit"))
					out.write("\tnode" + id
							+ " [shape=circle, style=bold, label=\"A\"]");
				else if (label.toLowerCase().contains("xorjoin")
						|| label.toLowerCase().contains("xorsplit"))
					out.write("\tnode" + id
							+ " [shape=circle, style=bold, label=\"X\"]");
				else
					out.write("\tnode" + id
							+ " [shape=circle, style=bold, label=\"V\"]");
			}
		}
	}

	public int getFragmentsNumber() {
		return this.setfragment.size();
	}

	public int getGraphsNumber() {
		return this.listgraph.size();
	}

	public int getModelsNumber() {
		return this.modelnumbers;
	}

	public int getMapFileNumber() {
		return this.mapfragment.size();
	}

}
