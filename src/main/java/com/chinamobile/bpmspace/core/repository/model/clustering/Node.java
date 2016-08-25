package com.chinamobile.bpmspace.core.repository.model.clustering;

import java.util.ArrayList;

public class Node {
	int id;
	String name = "";
	ArrayList<Node> children = new ArrayList<Node>();

	public Node() {
		this.id = GenerateID.id++;
	}
}
