package cn.edu.thss.iise.bpmdemo.analysis.core.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.kie.api.definition.process.Connection;
import org.kie.api.definition.process.Node;

public class RuleFlowProcessUtil {

	public static ArrayList<Node> getIncomingNodes(Node node) {
		ArrayList<Node> result = new ArrayList<Node>();
		Map<String, List<Connection>> connections = node
				.getIncomingConnections();
		for (List<Connection> connectionList : connections.values()) {
			for (Connection connection : connectionList) {
				Node src = connection.getFrom();
				result.add(src);
			}
		}
		return result;
	}

	public static ArrayList<Node> getOutgoingNodes(Node node) {
		ArrayList<Node> result = new ArrayList<Node>();
		Map<String, List<Connection>> connections = node
				.getOutgoingConnections();
		for (List<Connection> connectionList : connections.values()) {
			for (Connection connection : connectionList) {
				Node to = connection.getTo();
				result.add(to);
			}
		}
		return result;
	}
}
