package cn.edu.thss.iise.bpmdemo.editor.fragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.drools.eclipse.flow.common.editor.core.DefaultElementWrapper;
import org.drools.eclipse.flow.common.editor.core.ElementWrapper;
import org.drools.eclipse.flow.ruleflow.core.ActionWrapper;
import org.drools.eclipse.flow.ruleflow.core.ConnectionWrapper;
import org.drools.eclipse.flow.ruleflow.core.DynamicNodeWrapper;
import org.drools.eclipse.flow.ruleflow.core.EndNodeWrapper;
import org.drools.eclipse.flow.ruleflow.core.EventNodeWrapper;
import org.drools.eclipse.flow.ruleflow.core.FaultNodeWrapper;
import org.drools.eclipse.flow.ruleflow.core.ForEachNodeWrapper;
import org.drools.eclipse.flow.ruleflow.core.HumanTaskNodeWrapper;
import org.drools.eclipse.flow.ruleflow.core.JoinWrapper;
import org.drools.eclipse.flow.ruleflow.core.SplitWrapper;
import org.drools.eclipse.flow.ruleflow.core.StartNodeWrapper;
import org.eclipse.draw2d.PositionConstants;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.draw2d.graph.DirectedGraph;
import org.eclipse.draw2d.graph.DirectedGraphLayout;
import org.eclipse.draw2d.graph.Edge;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.node.ActionNode;
import org.jbpm.workflow.core.node.DynamicNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.EventNode;
import org.jbpm.workflow.core.node.FaultNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.Join;
import org.jbpm.workflow.core.node.Split;
import org.jbpm.workflow.core.node.StartNode;
import org.kie.api.definition.process.Connection;
import org.kie.api.definition.process.Node;
import org.kie.api.definition.process.WorkflowProcess;

public class FragmentFactory {

	public static ForEachNodeWrapper createFragmentModel(RuleFlowProcess process) {
		ForEachNodeWrapper fnw = new ForEachNodeWrapper();
		fnw.setName("Fragment");
		// add nodes wrapper
		Node[] nodes = process.getNodes();
		HashMap<Node, ElementWrapper> map = new HashMap<Node, ElementWrapper>();
		Map<String, ElementWrapper> elements = new HashMap<String, ElementWrapper>();

		int i;
		for (i = 0; i < nodes.length; i++) {
			ElementWrapper ew = getElementWrapper(nodes[i]);
			map.put(nodes[i], ew);
			elements.put(ew.getId(), ew);
			ew.setParent(fnw);
			fnw.addElement(ew);
		}
		// add connection wrapper
		for (i = 0; i < nodes.length; i++) {
			Map<String, List<Connection>> conMap = nodes[i]
					.getOutgoingConnections();

			if (conMap != null && conMap.size() != 0) {
				List<Connection> connections = conMap.get("DROOLS_DEFAULT");
				for (int j = 0; j < connections.size(); j++) {

					Node fromNode = connections.get(j).getFrom();
					Node toNode = connections.get(j).getTo();
					((org.jbpm.workflow.core.impl.ConnectionImpl) connections
							.get(j)).terminate();
					ConnectionWrapper ec = new ConnectionWrapper();
					ec.connect(map.get(fromNode), map.get(toNode));
				}
			}
		}
		FragmentFactory.horizontalAutoLayout(fnw, process, elements);
		return fnw;
	}

	public static void horizontalAutoLayout(ForEachNodeWrapper fnw,
			RuleFlowProcess process, Map<String, ElementWrapper> elements) {
		// ������ֵ������¼�����ڵ�Ĳ���
		int minX = 10000, minY = 10000;
		int maxX = 0, maxY = 0;
		// ����process�Ľڵ㽨������ͼ
		Map<Long, org.eclipse.draw2d.graph.Node> mapping = new HashMap<Long, org.eclipse.draw2d.graph.Node>();
		DirectedGraph graph = FragmentFactory.createDirectedGraph(mapping,
				process);
		DirectedGraphLayout layout = new DirectedGraphLayout();
		layout.visit(graph);
		// ������ͼ����Ϣ���ص�wrapper��
		for (Map.Entry<Long, org.eclipse.draw2d.graph.Node> entry : mapping
				.entrySet()) {
			org.eclipse.draw2d.graph.Node node = entry.getValue();
			DefaultElementWrapper elementWrapper = (DefaultElementWrapper) elements
					.get(entry.getKey() + "");
			if (minX > node.x)
				minX = node.x;
			if (maxX < node.x + node.width)
				maxX = node.x + node.width;
			if (minY > node.y)
				minY = node.y;
			if (maxY < node.y + node.height)
				maxY = node.y + node.height;
			elementWrapper.setConstraint(new Rectangle(node.x, node.y,
					node.width, node.height));
		}
		Rectangle constraint = new Rectangle();
		constraint.height = maxY - minY;
		constraint.width = maxX - minX;
		fnw.setConstraint(constraint);
	}

	public static DirectedGraph createDirectedGraph(
			Map<Long, org.eclipse.draw2d.graph.Node> mapping,
			WorkflowProcess process) {
		DirectedGraph graph = new DirectedGraph();
		for (org.kie.api.definition.process.Node processNode : process
				.getNodes()) {
			org.eclipse.draw2d.graph.Node node = new org.eclipse.draw2d.graph.Node();
			Integer width = (Integer) processNode.getMetaData().get("width");
			Integer height = (Integer) processNode.getMetaData().get("height");
			if (width == null || width <= 0) {
				width = 80;
			}
			if (height == null || height <= 0) {
				height = 40;
			}
			node.setSize(new Dimension(width, height));
			graph.nodes.add(node);
			mapping.put(processNode.getId(), node);
		}
		for (org.kie.api.definition.process.Node processNode : process
				.getNodes()) {
			for (List<Connection> connections : processNode
					.getIncomingConnections().values()) {
				for (Connection connection : connections) {
					org.eclipse.draw2d.graph.Node source = mapping
							.get(connection.getFrom().getId());
					org.eclipse.draw2d.graph.Node target = mapping
							.get(connection.getTo().getId());
					graph.edges.add(new Edge(source, target));
				}
			}
		}
		graph.setDirection(PositionConstants.HORIZONTAL);
		return graph;
	}

	public static ElementWrapper getElementWrapper(Node node) {
		ElementWrapper ew = null;

		if (node instanceof EndNode) {
			EndNodeWrapper snw = new EndNodeWrapper((EndNode) node); // convert
																		// to
																		// node
																		// wrapper
			ew = (ElementWrapper) snw;
		} else if (node instanceof StartNode) {
			StartNodeWrapper snw = new StartNodeWrapper((StartNode) node); // convert
																			// to
																			// node
																			// wrapper
			ew = (ElementWrapper) snw;
		} else if (node instanceof HumanTaskNode) {
			HumanTaskNodeWrapper snw = new HumanTaskNodeWrapper(
					(HumanTaskNode) node); // convert to node wrapper
			ew = (ElementWrapper) snw;
		} else if (node instanceof ActionNode) {
			ActionWrapper aw = new ActionWrapper((ActionNode) node);
			ew = (ElementWrapper) aw;
		} else if (node instanceof DynamicNode) {
			DynamicNodeWrapper dnw = new DynamicNodeWrapper((DynamicNode) node);
			ew = (ElementWrapper) dnw;
		} else if (node instanceof EventNode) {
			EventNodeWrapper enw = new EventNodeWrapper((EventNode) node);
			ew = (ElementWrapper) enw;
		} else if (node instanceof FaultNode) {
			FaultNodeWrapper fnw = new FaultNodeWrapper((FaultNode) node);
			ew = (ElementWrapper) fnw;
		} else if (node instanceof Join) {
			JoinWrapper jnw = new JoinWrapper((Join) node);
			ew = (ElementWrapper) jnw;
		} else if (node instanceof Split) {
			SplitWrapper snw = new SplitWrapper((Split) node);
			ew = (ElementWrapper) snw;
		}
		return ew;
	}
}
