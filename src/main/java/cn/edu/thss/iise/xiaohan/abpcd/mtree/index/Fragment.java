package cn.edu.thss.iise.xiaohan.abpcd.mtree.index;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import de.bpt.hpi.graph.Edge;
import de.bpt.hpi.graph.Graph;

public class Fragment {

	public Graph graph = new Graph();
	public List<String> modelfiles = new ArrayList<String>();
	public Boolean origin = false;
	public int ID;

	public Fragment(Graph graph, String filename) {
		this.graph = graph;
		this.modelfiles.add(filename);
	}

	public Fragment(Graph graph) {
		this.graph = graph;
	}

	public boolean equals(Object o) {
		if (!(o instanceof Fragment))
			return false;

		Fragment that = (Fragment) o;
		Graph graph1 = new Graph();
		Graph graph2 = new Graph();
		graph1 = convert(this);
		graph2 = convert(that);

		return graph1.equals(graph2);
	}

	private Graph convert(Fragment f) {
		Graph graph = new Graph();
		for (int i : f.graph.getVertices()) {
			String s = f.graph.getLabel(i);
			graph.addVertex(s.hashCode(), s);
		}
		for (Edge e : f.graph.getEdges()) {
			int first = e.getFirst();
			int second = e.getSecond();
			String s1 = f.graph.getLabel(first);
			String s2 = f.graph.getLabel(second);
			graph.addEdge(s1.hashCode(), s2.hashCode());
		}
		return graph;
	}

	public int hashCode() {
		Set<Integer> nodes = new HashSet<Integer>();
		int code = 0;
		nodes = graph.getVertices();
		for (String s : graph.getLabels(nodes))
			code += s.hashCode();

		return code;
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Graph getGraph() {
		return this.graph;
	}

	public void addModelFile(String filename) {
		this.modelfiles.add(filename);
	}

	public void addAllModelFile(List<String> filenames) {
		this.modelfiles.addAll(filenames);
	}

	public void setModelFiles(List<String> filenames) {
		this.modelfiles = filenames;
	}

	public List<String> getModelFiles() {
		return this.modelfiles;
	}

	public boolean containModelFile(String filename) {
		if (modelfiles.contains(filename))
			return true;
		else
			return false;
	}

	public int getFilesNumber() {
		return this.modelfiles.size();
	}

	public void setOriginGraph() {
		if (this.modelfiles.size() == 1)
			this.origin = true;
		else
			this.origin = false;
	}

	public void setOriginGraph(Boolean b) {
		this.origin = b;
	}

	public Boolean isOriginGraph() {
		return this.origin;
	}
}
