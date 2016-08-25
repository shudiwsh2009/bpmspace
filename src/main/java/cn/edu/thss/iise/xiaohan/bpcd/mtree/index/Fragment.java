package cn.edu.thss.iise.xiaohan.bpcd.mtree.index;

import java.util.ArrayList;
import java.util.List;

import de.bpt.hpi.graph.Graph;

public class Fragment {

	public Graph graph;
	public List<String> modelfiles;

	public Fragment() {
		this.graph = new Graph();
		this.modelfiles = new ArrayList<String>();
	}

	public Fragment(Graph graph) {
		this.graph = graph;
		this.modelfiles = new ArrayList<String>();
	}

	public void setGraph(Graph graph) {
		this.graph = graph;
	}

	public Graph getGraph() {
		return this.graph;
	}

	public void setModelFile(String filename) {
		this.modelfiles.add(filename);
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
}
