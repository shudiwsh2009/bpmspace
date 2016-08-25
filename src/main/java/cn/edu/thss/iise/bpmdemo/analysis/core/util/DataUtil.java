package cn.edu.thss.iise.bpmdemo.analysis.core.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import nl.tue.tm.is.epc.Arc;
import nl.tue.tm.is.epc.Connector;
import nl.tue.tm.is.epc.EPC;
import nl.tue.tm.is.epc.Event;
import nl.tue.tm.is.epc.Function;

import org.drools.core.xml.SemanticModules;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.bpmn2.xml.XmlBPMNProcessDumper;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.Node;
import org.jbpm.workflow.core.impl.ConnectionImpl;
import org.jbpm.workflow.core.node.ActionNode;
import org.jbpm.workflow.core.node.EndNode;
import org.jbpm.workflow.core.node.ForEachNode;
import org.jbpm.workflow.core.node.HumanTaskNode;
import org.jbpm.workflow.core.node.Join;
import org.jbpm.workflow.core.node.Split;
import org.jbpm.workflow.core.node.StartNode;
import org.kie.api.definition.process.Connection;
import org.kie.api.definition.process.Process;
import org.processmining.framework.models.ModelGraphEdge;
import org.processmining.framework.models.ModelGraphVertex;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;

import cn.edu.thss.iise.bpmdemo.analysis.core.fragment.Dot;
import cn.edu.thss.iise.bpmdemo.analysis.core.fragment.DotArc;
import cn.edu.thss.iise.bpmdemo.analysis.core.mining.Log;
import cn.edu.thss.iise.bpmdemo.analysis.core.mining.Trace;

import com.ibm.bpm.model.Activity;
import com.ibm.bpm.model.EndEvent;
import com.ibm.bpm.model.Flow;
import com.ibm.bpm.model.FlowNodeRef;
import com.ibm.bpm.model.Gateway;
import com.ibm.bpm.model.Gateway.GatewayType;
import com.ibm.bpm.model.ProcessNode;
import com.ibm.bpm.model.StartEvent;

//TODO
//1.
//2.
public class DataUtil {

	public static int i = 0;

	// public static void main(String agrgs[]) throws IOException
	// {
	// String filePath1 =
	// "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\pnml文件\\pnml文件726";
	// String filePath2 =
	// "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件\\bpmn文件728";
	// File folder = new File(filePath1);
	// File[] files = folder.listFiles();
	// for (File file : files)
	// {
	// System.out.println(file.getName());
	// if (!file.getName().endsWith("pnml"))
	// {
	// continue;
	// }
	// PetriNet petriNet = PetriNetUtil.getPetriNetFromPnmlFile(file);
	// RuleFlowProcess process = convertPetriNettoProcess(petriNet);
	// String string = convertBPMNProcessToXmlString(process);
	// FileWriter fw = new FileWriter(filePath2+file.getName()+".bpmn");
	// BufferedWriter bw = new BufferedWriter(fw);
	// bw.write(string);
	// bw.close();
	// }
	// }

	public static void main(String agrs[]) throws IOException {
		String filePath1 = "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件\\bpmn文件730\\with";
		String filePath2 = "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件\\bpmn文件730\\normal";
		File folder = new File(filePath1);
		File[] files = folder.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
			if (!file.getName().endsWith("bpmn")) {
				continue;
			}
			if (file.getName().startsWith("231")) {
				int a;
				a = 1;
			}
			List<Process> processes = importFromXmlFile(file.getAbsolutePath());
			RuleFlowProcess process = (RuleFlowProcess) processes.get(0);

			RuleFlowProcess convertedProcess = deMultiInstance(process);

			// RuleFlowProcess process = convertPetriNettoProcess(petriNet);
			String string = convertBPMNProcessToXmlString(convertedProcess);
			FileWriter fw = new FileWriter(filePath2 + file.getName());
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(string);
			bw.close();
		}
	}

	public static void addSplit(Node node, int splitId, RuleFlowProcess result,
			HashMap<String, Node> splitSet) {
		Split split = new Split();
		split.setName("AddtoSplit" + splitId);
		split.setId(splitId);
		splitId++;
		result.addNode(split);
		new ConnectionImpl(node, Node.CONNECTION_DEFAULT_TYPE, split,
				Node.CONNECTION_DEFAULT_TYPE);
		splitSet.put(String.valueOf(node.getId()), split);
	}

	public static void addJoin(Node node, int joinId, RuleFlowProcess result,
			HashMap<String, Node> joinSet) {
		Join join = new Join();
		join.setName("AddtoJoin" + joinId);
		join.setId(joinId);
		joinId++;
		result.addNode(join);
		new ConnectionImpl(join, Node.CONNECTION_DEFAULT_TYPE, node,
				Node.CONNECTION_DEFAULT_TYPE);
		joinSet.put(String.valueOf(node.getId()), join);
	}

	public static RuleFlowProcess convertPetriNettoProcessForMining(
			PetriNet petriNet, Log log) {
		RuleFlowProcess result = new RuleFlowProcess();
		HashMap<String, Node> splitSet = new HashMap<String, Node>();
		HashMap<String, Node> joinSet = new HashMap<String, Node>();
		int splitId = 10000, joinId = 20000;
		// 1.name
		if (petriNet.getIdentifier() != null)
			result.setName(petriNet.getIdentifier());
		// 2.id
		result.setId(petriNet.getIdentifier());
		// 3.startnode
		StartNode startNode = new StartNode();
		ModelGraphVertex sourceNode = petriNet.getSource();
		if (sourceNode.getIdentifier() != null) {
			startNode.setName(sourceNode.getIdentifier());
		}
		startNode.setId(sourceNode.getId());
		result.addNode(startNode);
		// if (RuleFlowProcessUtil.getOutgoingNodes(startNode).size() >1)
		if (sourceNode.getSuccessors().size() > 1)
			addSplit(startNode, splitId++, result, splitSet);

		// 4.endnode

		ArrayList<ModelGraphVertex> sinkNodes = new ArrayList<ModelGraphVertex>();
		for (ModelGraphVertex sinkNode : petriNet.getEndNodes()) {
			EndNode endNode = new EndNode();
			// ModelGraphVertex sinkNode = petriNet.getSink();
			if (sinkNode.getIdentifier() != null)
				endNode.setName(sinkNode.getIdentifier());

			endNode.setId(sinkNode.getId());
			result.addNode(endNode);
			sinkNodes.add(sinkNode);
			if (sinkNode.getPredecessors().size() > 1) {
				addJoin(endNode, joinId++, result, joinSet);
			}
		}

		// transition
		for (Transition transition : petriNet.getTransitions()) {
			// if (!transition.getLogEvent().getEventType().equals("complete"))
			// continue;
			if (transition.getLogEvent() != null) // the true event
			{
				HumanTaskNode task = new HumanTaskNode();
				ForEachNode forEachNode = new ForEachNode();

				if (transition.getIdentifier() != null) {
					task.setName(transition.getIdentifier() /*
															 * + "_"+transition.
															 * getLogEvent
															 * ().getEventType()
															 */);
					for (Trace trace : log.getTraces()) {
						if (trace.getQxbTasks().contains(
								transition.getIdentifier())) {
							i = 1; // 抢先办模式任务
							break;
						} else if (trace.getConTasks().contains(
								transition.getIdentifier())
								&& trace.getRecTasks().contains(
										transition.getIdentifier())) {
							i = 2; // 会签模式任务
							break;
						} else if (trace.getMcTasks().contains(
								transition.getIdentifier())) {
							System.out
									.println("1" + transition.getIdentifier());
							i = 3; // 主控任务
							break;
						} else {
							i = 4; // 主控任务
							break;
						}
					}
					if (i == 1 || i == 2) {
						// node = new ForEachNode();
						forEachNode.setName("Multiple Instances");
						forEachNode.setId(transition.getId());
						task.setName(transition.getIdentifier());
						// task.setId(transition.getId());
						forEachNode.addNode(task);
						result.addNode(forEachNode);
						if (transition.getInEdges().size() > 1)
							addJoin(forEachNode, joinId, result, joinSet);
						if (transition.getOutEdges().size() > 1)
							addSplit(forEachNode, splitId, result, splitSet);
					} else if (i == 3 || i == 4) {
						// node = new HumanTaskNode();
						task.setName(transition.getIdentifier());
						task.setId(transition.getId());
						result.addNode(task);
						if (transition.getInEdges().size() > 1)
							addJoin(task, joinId, result, joinSet);
						if (transition.getOutEdges().size() > 1)
							addSplit(task, splitId, result, splitSet);
					}
					i = 0;
				}
			} else // invisible task,deal the invisible task same with the
					// place, delete it.
			{
				if (transition.getPredecessors().size() == 1
						&& transition.getSuccessors().size() == 1) {
					Join join = new Join();
					if (transition.getIdentifier() != null)
						join.setName(transition.getIdentifier());
					join.setId(transition.getId());
					result.addNode(join);
				} else if (transition.getPredecessors().size() > 1
						&& transition.getSuccessors().size() == 1) {
					Join join = new Join();
					if (transition.getIdentifier() != null)
						join.setName(transition.getIdentifier());
					join.setId(transition.getId());
					result.addNode(join);
				} else if (transition.getPredecessors().size() == 1
						&& transition.getSuccessors().size() > 1) {
					Split split = new Split();
					if (transition.getIdentifier() != null)
						split.setName(transition.getIdentifier());
					split.setId(transition.getId());
					result.addNode(split);
				} else if (transition.getPredecessors().size() > 1
						&& transition.getSuccessors().size() > 1) {
					Split split = new Split();
					if (transition.getIdentifier() != null)
						split.setName(transition.getIdentifier());
					split.setId(transition.getId());
					result.addNode(split);
					addJoin(split, joinId, result, joinSet);
				}

			}

		}
		// 6.place to gateway
		for (Place place : petriNet.getPlaces()) {
			if (place.equals(sourceNode))
				continue;
			// if (place.equals(sinkNode))
			if (sinkNodes.contains(place))
				continue;
			if (place.getPredecessors().size() > 1
					&& place.getSuccessors().size() > 1) {
				Split split = new Split();
				if (place.getIdentifier() != null)
					split.setName(place.getIdentifier());
				split.setId(place.getId());
				result.addNode(split);
				addJoin(split, joinId, result, joinSet);
			} else if (place.getPredecessors().size() == 1
					&& place.getSuccessors().size() > 1) // split
			{
				Split split = new Split();
				if (place.getIdentifier() != null)
					split.setName(place.getIdentifier());
				split.setId(place.getId());
				result.addNode(split);
			} else if (place.getPredecessors().size() > 1
					&& place.getSuccessors().size() == 1) // join
			{
				Join join = new Join();
				if (place.getIdentifier() != null)
					join.setName(place.getIdentifier());
				join.setId(place.getId());
				result.addNode(join);

			} else if (place.getSuccessors().size() == 1
					|| place.getPredecessors().size() == 1) // 用一个join代替
			{
				Join join = new Join();
				if (place.getIdentifier() != null)
					join.setName(place.getIdentifier());
				join.setId(place.getId());
				result.addNode(join);
			}

		}
		for (ModelGraphEdge edge : (ArrayList<ModelGraphEdge>) petriNet
				.getEdges()) {
			ModelGraphVertex source = edge.getSource();
			ModelGraphVertex sink = edge.getDest();
			org.kie.api.definition.process.Node sourceN = null;
			org.kie.api.definition.process.Node sinkN = null;
			for (org.kie.api.definition.process.Node node : result.getNodes()) {
				if (node.getId() == source.getId())
					sourceN = node;
				if (node.getId() == sink.getId())
					sinkN = node;
			}
			if (sourceN == null || sinkN == null)
				continue;
			addConnection(sourceN, sinkN, splitSet, joinSet);
		}

		// TODO
		deleteSuperfluousEdges(result);
		// 2. delete1edge
		return result;

	}

	public static RuleFlowProcess convertPetriNettoProcess(PetriNet petriNet) {
		RuleFlowProcess result = new RuleFlowProcess();
		HashMap<String, Node> splitSet = new HashMap<String, Node>();
		HashMap<String, Node> joinSet = new HashMap<String, Node>();
		int splitId = 10000, joinId = 20000;
		// 1.name
		if (petriNet.getIdentifier() != null)
			result.setName(petriNet.getIdentifier());
		// 2.id
		result.setId(petriNet.getIdentifier());
		// 3.startnode
		StartNode startNode = new StartNode();
		ModelGraphVertex sourceNode = petriNet.getSource();
		if (sourceNode.getIdentifier() != null) {
			startNode.setName(sourceNode.getIdentifier());
		}
		startNode.setId(sourceNode.getId());
		result.addNode(startNode);
		// if (RuleFlowProcessUtil.getOutgoingNodes(startNode).size() >1)
		if (sourceNode.getSuccessors().size() > 1)
			addSplit(startNode, splitId++, result, splitSet);

		// 4.endnode

		ArrayList<ModelGraphVertex> sinkNodes = new ArrayList<ModelGraphVertex>();
		for (ModelGraphVertex sinkNode : petriNet.getEndNodes()) {
			EndNode endNode = new EndNode();
			// ModelGraphVertex sinkNode = petriNet.getSink();
			if (sinkNode.getIdentifier() != null)
				endNode.setName(sinkNode.getIdentifier());

			endNode.setId(sinkNode.getId());
			result.addNode(endNode);
			sinkNodes.add(sinkNode);
			if (sinkNode.getPredecessors().size() > 1) {
				addJoin(endNode, joinId++, result, joinSet);
			}
		}

		// transition
		for (Transition transition : petriNet.getTransitions()) {
			// if (!transition.getLogEvent().getEventType().equals("complete"))
			// continue;
			if (transition.getLogEvent() != null) // the true event
			{
				HumanTaskNode task = new HumanTaskNode();

				if (transition.getIdentifier() != null) {
					task.setName(transition.getIdentifier() /*
															 * + "_"+transition.
															 * getLogEvent
															 * ().getEventType()
															 */);
				}
				task.setId(transition.getId());
				result.addNode(task);
				if (transition.getInEdges().size() > 1)
					addJoin(task, joinId, result, joinSet);
				if (transition.getOutEdges().size() > 1)
					addSplit(task, splitId, result, splitSet);
			} else // invisible task,deal the invisible task same with the
					// place, delete it.
			{
				if (transition.getPredecessors().size() == 1
						&& transition.getSuccessors().size() == 1) {
					Join join = new Join();
					if (transition.getIdentifier() != null)
						join.setName(transition.getIdentifier());
					join.setId(transition.getId());
					result.addNode(join);
				} else if (transition.getPredecessors().size() > 1
						&& transition.getSuccessors().size() == 1) {
					Join join = new Join();
					if (transition.getIdentifier() != null)
						join.setName(transition.getIdentifier());
					join.setId(transition.getId());
					result.addNode(join);
				} else if (transition.getPredecessors().size() == 1
						&& transition.getSuccessors().size() > 1) {
					Split split = new Split();
					if (transition.getIdentifier() != null)
						split.setName(transition.getIdentifier());
					split.setId(transition.getId());
					result.addNode(split);
				} else if (transition.getPredecessors().size() > 1
						&& transition.getSuccessors().size() > 1) {
					Split split = new Split();
					if (transition.getIdentifier() != null)
						split.setName(transition.getIdentifier());
					split.setId(transition.getId());
					result.addNode(split);
					addJoin(split, joinId, result, joinSet);
				}

			}

		}
		// 6.place to gateway
		for (Place place : petriNet.getPlaces()) {
			if (place.equals(sourceNode))
				continue;
			// if (place.equals(sinkNode))
			if (sinkNodes.contains(place))
				continue;
			if (place.getPredecessors().size() > 1
					&& place.getSuccessors().size() > 1) {
				Split split = new Split();
				if (place.getIdentifier() != null)
					split.setName(place.getIdentifier());
				split.setId(place.getId());
				result.addNode(split);
				addJoin(split, joinId, result, joinSet);
			} else if (place.getPredecessors().size() == 1
					&& place.getSuccessors().size() > 1) // split
			{
				Split split = new Split();
				if (place.getIdentifier() != null)
					split.setName(place.getIdentifier());
				split.setId(place.getId());
				result.addNode(split);
			} else if (place.getPredecessors().size() > 1
					&& place.getSuccessors().size() == 1) // join
			{
				Join join = new Join();
				if (place.getIdentifier() != null)
					join.setName(place.getIdentifier());
				join.setId(place.getId());
				result.addNode(join);

			} else if (place.getSuccessors().size() == 1
					|| place.getPredecessors().size() == 1) // 用一个join代替
			{
				Join join = new Join();
				if (place.getIdentifier() != null)
					join.setName(place.getIdentifier());
				join.setId(place.getId());
				result.addNode(join);
			}

		}
		for (ModelGraphEdge edge : (ArrayList<ModelGraphEdge>) petriNet
				.getEdges()) {
			ModelGraphVertex source = edge.getSource();
			ModelGraphVertex sink = edge.getDest();
			org.kie.api.definition.process.Node sourceN = null;
			org.kie.api.definition.process.Node sinkN = null;
			for (org.kie.api.definition.process.Node node : result.getNodes()) {
				if (node.getId() == source.getId())
					sourceN = node;
				if (node.getId() == sink.getId())
					sinkN = node;
			}
			if (sourceN == null || sinkN == null)
				continue;
			addConnection(sourceN, sinkN, splitSet, joinSet);
		}

		// TODO
		deleteSuperfluousEdges(result);
		// 2. delete1edge
		return result;

	}

	public static void deleteSuperfluousEdges(RuleFlowProcess process) {
		for (org.kie.api.definition.process.Node node : process.getNodes()) {
			if (node instanceof Join || node instanceof Split) {
				if (RuleFlowProcessUtil.getIncomingNodes(node).size() == 1
						&& RuleFlowProcessUtil.getOutgoingNodes(node).size() == 1) {
					org.kie.api.definition.process.Node src = RuleFlowProcessUtil
							.getIncomingNodes(node).get(0);
					org.kie.api.definition.process.Node tgt = RuleFlowProcessUtil
							.getOutgoingNodes(node).get(0);
					int pos1 = -1, pos2 = -1;
					for (int i = 0; i < src.getOutgoingConnections()
							.get(Node.CONNECTION_DEFAULT_TYPE).size(); i++) {
						Connection connection = src.getOutgoingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).get(i);
						if (connection.getTo().equals(node))
							pos1 = i;
					}
					for (int i = 0; i < tgt.getIncomingConnections()
							.get(Node.CONNECTION_DEFAULT_TYPE).size(); i++) {
						Connection connection = tgt.getIncomingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).get(i);
						if (connection.getFrom().equals(node))
							pos2 = i;
					}
					if (pos1 != -1)
						src.getOutgoingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).remove(pos1);
					if (pos2 != -1)
						tgt.getIncomingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).remove(pos2);
					process.removeNode(node);
					if (src.getName().equals("AddtoJoin20000")) {
						int a;
						a = 1;
					}
					new ConnectionImpl(src, Node.CONNECTION_DEFAULT_TYPE, tgt,
							Node.CONNECTION_DEFAULT_TYPE);
				}
			}
		}
	}

	// public static RuleFlowProcess convertPetriNettoProcess(PetriNet petriNet)
	// {
	// System.out.println(petriNet.getTransitions().size());
	// ArrayList<Node> nodeList = new ArrayList<Node>();
	// ArrayList<ModelGraphVertex> singularNodeList = new
	// ArrayList<ModelGraphVertex>();
	// RuleFlowProcess result = new RuleFlowProcess();
	// //1.name
	// if (petriNet.getIdentifier()!=null)
	// result.setName(petriNet.getIdentifier());
	// //2.id
	// result.setId(petriNet.getIdentifier());
	// //3.startnode
	// StartNode startNode = new StartNode();
	// ModelGraphVertex sourceNode = petriNet.getSource();
	// if (sourceNode.getIdentifier() != null)
	// {
	// startNode.setName(sourceNode.getIdentifier());
	// }
	// startNode.setId(sourceNode.getId());
	// result.addNode(startNode);
	// nodeList.add(startNode);
	// //4.endnode
	//
	// ArrayList<ModelGraphVertex> sinkNodes = new
	// ArrayList<ModelGraphVertex>();
	// for (ModelGraphVertex sinkNode : petriNet.getEndNodes())
	// {
	// EndNode endNode = new EndNode();
	// //ModelGraphVertex sinkNode = petriNet.getSink();
	// if (sinkNode.getIdentifier() != null)
	// endNode.setName(sinkNode.getIdentifier());
	//
	// endNode.setId(sinkNode.getId());
	// result.addNode(endNode);
	// nodeList.add(endNode);
	// sinkNodes.add(sinkNode);
	// }
	// //transition
	// for (Transition transition : petriNet.getTransitions())
	// {
	// //if (!transition.getLogEvent().getEventType().equals("complete"))
	// // continue;
	// HumanTaskNode task = new HumanTaskNode();
	//
	// if (transition.getIdentifier()!=null ){
	// task.setName(transition.getIdentifier() /*+
	// "_"+transition.getLogEvent().getEventType()*/);
	// }
	// task.setId(transition.getId());
	// result.addNode(task);
	// nodeList.add(task);
	// if (transition.getInEdges().size()>1 || transition.getOutEdges().size()
	// >1)
	// singularNodeList.add(transition);
	// }
	// //6.place to gateway
	// for (Place place : petriNet.getPlaces())
	// {
	// if (place.equals(sourceNode))
	// continue;
	// //if (place.equals(sinkNode))
	// if (sinkNodes.contains(place))
	// continue;
	// if (place.getPredecessors().size()>1 && place.getSuccessors().size()>1)
	// {
	// singularNodeList.add(place);
	// continue;
	// }
	// if ( place.getSuccessors().size() > 1) //split
	// {
	// Split split = new Split();
	// if (place.getIdentifier() != null)
	// split.setName(place.getIdentifier());
	// split.setId(place.getId());
	// result.addNode(split);
	// nodeList.add(split);
	// }
	// else if ( place.getPredecessors().size() > 1 ) //join
	// {
	// Join join = new Join();
	// if (place.getIdentifier() != null)
	// join.setName(place.getIdentifier());
	// join.setId(place.getId());
	// result.addNode(join);
	// nodeList.add(join);
	// }
	// else if (place.getSuccessors().size() == 1 ||
	// place.getPredecessors().size() == 1)
	// {
	// // ModelGraphVertex src =(ModelGraphVertex)
	// place.getPredecessors().iterator().next();
	// // ModelGraphVertex tgt =(ModelGraphVertex)
	// place.getSuccessors().iterator().next();
	//
	// //singularNodeList.add(place); //multiple in-degree and out-degree,
	// process later
	// Join join = new Join();
	// if (place.getIdentifier() != null)
	// join.setName(place.getIdentifier());
	// join.setId(place.getId());
	// result.addNode(join);
	// nodeList.add(join);
	// }
	//
	// }
	// //濞戞挸绉烽—鍛村箮婵犲洤娅㈠璺虹Х椤撳摜绮婚敓锟� //7.arc to connectionimpl
	// for (ModelGraphEdge edge : (ArrayList<ModelGraphEdge>)
	// petriNet.getEdges())
	// {
	// ModelGraphVertex source = edge.getSource();
	// ModelGraphVertex sink = edge.getDest();
	// Node sourceN = null, sinkN = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == source.getId())
	// sourceN = node;
	// if (node.getId() == sink.getId())
	// sinkN = node;
	// }
	// // if (sourceN instanceof ActionNode &&
	// sourceN.getOutgoingConnections().size()==1)
	// // continue;
	// // if (sinkN instanceof ActionNode &&
	// sinkN.getIncomingConnections().size()==1)
	// // continue;
	// if (sourceN == null || sinkN == null)
	// continue;
	// if (singularNodeList.contains(source) || singularNodeList.contains(sink))
	// continue;
	// // if (sinkN instanceof Join && sinkN != endNode &&
	// sinkN.getOutgoingConnections().size() == 0)
	// // new ConnectionImpl(
	// // sourceN, Node.CONNECTION_DEFAULT_TYPE,
	// // endNode, Node.CONNECTION_DEFAULT_TYPE
	// // );
	// // else
	// // if (sinkN.getIncomingConnections().size() ==1)
	// // {
	// // //TODO
	// // continue;
	// // }
	// new ConnectionImpl(
	// sourceN, Node.CONNECTION_DEFAULT_TYPE,
	// sinkN, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	// //8.濠㈣泛瀚幃濡攊ngularProcess
	// for (ModelGraphVertex vertex : singularNodeList)
	// {
	// if (vertex instanceof Place) //
	// {
	// Place place = (Place) vertex;
	// Split split = new Split();
	// if (place.getIdentifier() != null)
	// split.setName(place.getIdentifier()+"_split");
	// split.setId(place.getId()+1000);
	// result.addNode(split);
	//
	// Join join = new Join();
	// if (place.getIdentifier() != null)
	// join.setName(place.getIdentifier()+"_join");
	// join.setId(place.getId()+10000);
	// result.addNode(join);
	//
	// //1.split join濞戞挶鍊楅崑锝夊礉閻樿櫣鐝�
	// new ConnectionImpl(
	// join, Node.CONNECTION_DEFAULT_TYPE,
	// split, Node.CONNECTION_DEFAULT_TYPE
	// );
	//
	// //2.split闁告垿缂氱粩锟�
	// for (ModelGraphVertex outV :(HashSet<ModelGraphVertex>)
	// place.getSuccessors())
	// {
	// Node sink = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == outV.getId())
	// sink = node;
	// }
	// if (sink == null)
	// continue;
	// new ConnectionImpl(
	// split, Node.CONNECTION_DEFAULT_TYPE,
	// sink, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	//
	// //3.join闁稿繈鍎寸粩锟�
	// for (ModelGraphVertex inV :(HashSet<ModelGraphVertex>)
	// place.getPredecessors())
	// {
	// Node source = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == inV.getId())
	// source = node;
	// }
	// if (source == null)
	// continue;
	// new ConnectionImpl(
	// source, Node.CONNECTION_DEFAULT_TYPE,
	// join, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	// }
	// else if (vertex instanceof Transition) //
	// {
	// if (!(vertex.getInEdges().size()>1 || vertex.getOutEdges().size()>1))
	// continue;
	// int count1 = 0, count2 = 0;
	// Transition transition = (Transition) vertex;
	// HumanTaskNode action = new HumanTaskNode();
	// action.setName(transition.getIdentifier());
	// action.setId(transition.getId());
	// result.addNode(action);
	// if (transition.getPredecessors().size()>1)
	// {
	// Join join = new Join();
	// join.setName("AddtoJoin" + count1);
	// join.setId(count1+100000);
	// result.addNode(join);
	// count1++;
	//
	// for (ModelGraphVertex inV :
	// (HashSet<ModelGraphVertex>)vertex.getPredecessors())
	// {
	// Node source = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == inV.getId())
	// source = node;
	// }
	// if (source == null)
	// continue;
	// new ConnectionImpl(
	// source, Node.CONNECTION_DEFAULT_TYPE,
	// join, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	// new ConnectionImpl(
	// join, Node.CONNECTION_DEFAULT_TYPE,
	// action, Node.CONNECTION_DEFAULT_TYPE
	// );
	// if (transition.getSuccessors().size() == 1)
	// {
	// ModelGraphVertex outV = (ModelGraphVertex)
	// transition.getSuccessors().iterator().next();
	// Node target = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == outV.getId())
	// target = node;
	// }
	// if (target == null)
	// continue;
	// new ConnectionImpl(
	// action, Node.CONNECTION_DEFAULT_TYPE,
	// target, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	// }
	// if (transition.getSuccessors().size() >1)
	// {
	// Split split = new Split();
	// split.setName("AddtoSplit" + count2);
	// split.setId(count2+1000000);
	// result.addNode(split);
	// count2++;
	//
	// for (ModelGraphVertex outV :
	// (HashSet<ModelGraphVertex>)vertex.getSuccessors())
	// {
	// Node target = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == outV.getId())
	// target = node;
	// }
	// if (target == null)
	// continue;
	// new ConnectionImpl(
	// split, Node.CONNECTION_DEFAULT_TYPE,
	// target, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	// new ConnectionImpl(
	// action, Node.CONNECTION_DEFAULT_TYPE,
	// split, Node.CONNECTION_DEFAULT_TYPE
	// );
	// if (transition.getPredecessors().size() == 1)
	// {
	// ModelGraphVertex inV = (ModelGraphVertex)
	// transition.getPredecessors().iterator().next();
	// Node source = null;
	// for (Node node : nodeList)
	// {
	// if (node.getId() == inV.getId())
	// source = node;
	// }
	// if (source == null)
	// continue;
	// if (RuleFlowProcessUtil.getOutgoingNodes(source).size() < 1)
	// {
	// //TODO
	// new ConnectionImpl(
	// source, Node.CONNECTION_DEFAULT_TYPE,
	// action, Node.CONNECTION_DEFAULT_TYPE
	// );
	//
	// }
	// }
	// }
	// }
	//
	// }
	// //9.闁告帞濞�▍搴ㄥ础閺囩偛寮抽柛妤佹礀閸ゎ參鎯冮崚顫eway
	// org.kie.api.definition.process.Node[] temp = result.getNodes().clone();
	// for (org.kie.api.definition.process.Node node : temp)
	// {
	// boolean tag = false;
	// for (int i=0; i<result.getNodes().length; i++)
	// {
	// if (result.getNodes()[i].equals(node))
	// tag = true;
	// }
	// if (!tag)
	// continue;
	// ArrayList<org.kie.api.definition.process.Node> srcs =
	// RuleFlowProcessUtil.getIncomingNodes(node);
	// ArrayList<org.kie.api.definition.process.Node> tgts =
	// RuleFlowProcessUtil.getOutgoingNodes(node);
	//
	// if (node instanceof Join || node instanceof Split)
	// {
	// org.kie.api.definition.process.Node srcNode =
	// RuleFlowProcessUtil.getIncomingNodes(node).get(0);
	// org.kie.api.definition.process.Node tgtNode =
	// RuleFlowProcessUtil.getOutgoingNodes(node).get(0);
	// if (RuleFlowProcessUtil.getOutgoingNodes(srcNode).size() >1 ||
	// RuleFlowProcessUtil.getIncomingNodes(tgtNode).size() >1)
	// continue;
	// if (srcs.size() == 1 && tgts.size() == 1)
	// {
	// long srcId = srcs.get(0).getId();
	// long tgtId = tgts.get(0).getId();
	// org.kie.api.definition.process.Node src = null;
	// org.kie.api.definition.process.Node tgt = null;
	// for (int i=0; i<result.getNodes().length; i++)
	// {
	// if (result.getNodes()[i].getId() == srcId)
	// src = result.getNodes()[i];
	// if (result.getNodes()[i].getId() == tgtId)
	// tgt = result.getNodes()[i];
	// }
	// if (src == null || tgt == null)
	// continue;
	//
	// src.getOutgoingConnections().get(Node.CONNECTION_DEFAULT_TYPE).clear();
	// tgt.getIncomingConnections().get(Node.CONNECTION_DEFAULT_TYPE).clear();
	// result.removeNode(node);
	// new ConnectionImpl(
	// src, Node.CONNECTION_DEFAULT_TYPE,
	// tgt, Node.CONNECTION_DEFAULT_TYPE
	// );
	// }
	// }
	// }
	// //delete misc gateways
	// for (org.kie.api.definition.process.Node node : result.getNodes())
	// {
	// if (node instanceof Split)
	// {
	// org.kie.api.definition.process.Node pre =
	// RuleFlowProcessUtil.getIncomingNodes(node).get(0);
	// if (pre instanceof Split)
	// {
	// //TODO
	// }
	// }
	// if (node instanceof Join)
	// {
	// //TODO
	// }
	// }
	// return result;
	// }

	public static List<Process> importFromXmlFile(String bpmnFile)
			throws IOException {
		List<Process> processes = null;

		InputStreamReader isr = new InputStreamReader(new FileInputStream(
				new File(bpmnFile)), "UTF-8");
		SemanticModules semanticModules = new SemanticModules();
		semanticModules.addSemanticModule(new BPMNSemanticModule());
		semanticModules.addSemanticModule(new BPMNExtensionsSemanticModule());
		semanticModules.addSemanticModule(new BPMNDISemanticModule());
		XmlProcessReader xmlReader = new XmlProcessReader(semanticModules,
				Thread.currentThread().getContextClassLoader());
		try {
			processes = xmlReader.read(isr);
		} catch (Throwable t) {
			System.out.println(t);
			System.out
					.println("Could not read RuleFlow file"
							+ "An exception occurred while reading in the RuleFlow XML: "
							+ t.getMessage()
							+ " See the error log for more details.");
		}
		if (isr != null) {
			isr.close();
		}
		return processes;
	}

	private static void addConnection(Node src, Node tgt,
			HashMap<String, Node> splitSet, HashMap<String, Node> joinSet) {
		Node ssrc;
		Node ttgt;
		if (splitSet.containsKey(String.valueOf(src.getId())))
			ssrc = splitSet.get(String.valueOf(src.getId()));
		else
			ssrc = src;
		if (joinSet.containsKey(String.valueOf(tgt.getId())))
			ttgt = joinSet.get(String.valueOf(tgt.getId()));
		else
			ttgt = tgt;

		new ConnectionImpl(ssrc, Node.CONNECTION_DEFAULT_TYPE, ttgt,
				Node.CONNECTION_DEFAULT_TYPE);
	}

	public static RuleFlowProcess deMultiInstance(RuleFlowProcess process) {
		RuleFlowProcess result = new RuleFlowProcess();
		result.setName(process.getName());
		result.setId(process.getId());
		HashSet<org.kie.api.definition.process.Node> insideNodes = new HashSet<org.kie.api.definition.process.Node>();
		for (org.kie.api.definition.process.Node node : process.getNodes()) {
			if (node instanceof ForEachNode) {
				for (org.kie.api.definition.process.Node insideNode : ((ForEachNode) node)
						.getNodes()) {
					insideNodes.add(insideNode);
				}
			}
		}
		for (org.kie.api.definition.process.Node node : process.getNodes()) {
			if (node instanceof Split) {
				Split split = new Split();
				split.setName(node.getName());
				split.setId(node.getId());
				result.addNode(split);
			} else if (node instanceof Join) {
				Join join = new Join();
				join.setName(node.getName());
				join.setId(node.getId());
				result.addNode(join);
			} else if (node instanceof HumanTaskNode
					|| node instanceof ActionNode) {
				if (insideNodes.contains(node))
					continue;
				HumanTaskNode humanTaskNode = new HumanTaskNode();
				humanTaskNode.setName(node.getName());
				humanTaskNode.setId(node.getId());
				result.addNode(humanTaskNode);
			} else if (node instanceof StartNode) {
				StartNode startNode = new StartNode();
				startNode.setName(node.getName());
				startNode.setId(node.getId());
				result.addNode(startNode);
			} else if (node instanceof EndNode) {
				EndNode endNode = new EndNode();
				endNode.setName(node.getName());
				endNode.setId(node.getId());
				result.addNode(endNode);
			} else if (node instanceof ForEachNode) {
				HumanTaskNode humanTaskNode = new HumanTaskNode();
				String name;
				name = ((ForEachNode) node).getNodes()[0].getName();
				humanTaskNode.setName(name);

				humanTaskNode.setId(node.getId());
				result.addNode(humanTaskNode);
			}

		}
		for (org.kie.api.definition.process.Node node : process.getNodes()) {
			List<Connection> conns = node.getIncomingConnections().get(
					Node.CONNECTION_DEFAULT_TYPE);
			if (conns == null)
				continue;
			for (Connection connection : conns) {
				long srcId = connection.getFrom().getId();
				long tgtId = connection.getTo().getId();
				org.kie.api.definition.process.Node src = result.getNode(srcId);
				org.kie.api.definition.process.Node tgt = result.getNode(tgtId);
				new ConnectionImpl(src, Node.CONNECTION_DEFAULT_TYPE, tgt,
						Node.CONNECTION_DEFAULT_TYPE);
			}
		}

		return result;
	}

	public static RuleFlowProcess convertIBMProcesstoProcess(
			com.ibm.bpm.model.Process process) {
		HashMap<String, Node> splitSet, joinSet;
		splitSet = new HashMap<String, Node>();
		joinSet = new HashMap<String, Node>();

		RuleFlowProcess result = new RuleFlowProcess();
		if (process.getName() != null)
			result.setName(process.getName());
		result.setId(process.getId());
		ArrayList<Node> nodeList = new ArrayList<Node>();
		// TODO
		// 1.activityList
		ArrayList<ProcessNode> sigularNode = new ArrayList<ProcessNode>();
		for (Activity activity : process.getActivityList()) {
			HumanTaskNode action = new HumanTaskNode();
			String name = activity.getName();
			String id = activity.getId();
			action.setName(name);
			action.setId(id.hashCode());
			result.addNode(action);
			nodeList.add(action);
		}
		// 2.gatewayList
		for (Gateway gateway : process.getGatewayList()) {
			if (gateway.getGatewayDirection().equals("Diverging")) // split
			{
				Split split = new Split();
				String name = gateway.getName();
				String id = gateway.getId();
				split.setName(name);
				split.setId(id.hashCode());
				result.addNode(split);
				nodeList.add(split);
			} else if (gateway.getGatewayDirection().equals("Converging")) // join
			{
				Join join = new Join();
				String name = gateway.getName();
				String id = gateway.getId();
				join.setName(name);
				join.setId(id.hashCode());
				result.addNode(join);
				nodeList.add(join);
			}
		}
		// 3.startevent
		for (StartEvent startEvent : process.getStarteventList()) {
			StartNode startNode = new StartNode();
			String name = startEvent.getName();
			String id = startEvent.getId();
			startNode.setName(name);
			startNode.setId(id.hashCode());
			result.addNode(startNode);
			nodeList.add(startNode);
		}
		// 4.endevent
		for (EndEvent endEvent : process.getEndeventList()) {
			EndNode endNode = new EndNode();
			String name = endEvent.getName();
			String id = endEvent.getId();
			endNode.setName(name);
			endNode.setId(id.hashCode());
			result.addNode(endNode);
			nodeList.add(endNode);
		}
		// 5.flow
		int count1 = 10000, count2 = 100000;
		for (Flow flow : process.getFlowList()) {
			ProcessNode srcNode = flow.getSrcNode();
			ProcessNode tgtNode = flow.getTargetNode();
			int srcId = srcNode.getId().hashCode();
			int tgtId = tgtNode.getId().hashCode();
			Node srcProcessNode = null, tgtProcessNode = null;
			for (Node node : nodeList) {
				if (node.getId() == (long) srcId) {
					srcProcessNode = node;
				}
				if (node.getId() == (long) tgtId) {
					tgtProcessNode = node;
				}
			}
			if (srcProcessNode == null || tgtProcessNode == null)
				continue;
			boolean tag = true;
			if (RuleFlowProcessUtil.getOutgoingNodes(srcProcessNode).size() == 1
					|| RuleFlowProcessUtil.getIncomingNodes(tgtProcessNode)
							.size() == 1) {
				// TODO

				if (RuleFlowProcessUtil.getOutgoingNodes(srcProcessNode).size() == 1
						&& !(srcProcessNode instanceof Split)
						&& !(splitSet.containsKey(String.valueOf(srcProcessNode
								.getId())))) {
					// 鍔犱竴涓猻plit
					Split split = new Split();
					split.setName("AddtoSplit" + count1);
					split.setId(count1);
					count1++;
					result.addNode(split);
					org.kie.api.definition.process.Node tgt = RuleFlowProcessUtil
							.getOutgoingNodes(srcProcessNode).get(0);
					srcProcessNode.getOutgoingConnections()
							.get(Node.CONNECTION_DEFAULT_TYPE).clear();
					int pos = -1;
					for (int i = 0; i < tgt.getIncomingConnections()
							.get(Node.CONNECTION_DEFAULT_TYPE).size(); i++) {
						Connection connect = tgt.getIncomingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).get(i);
						if (connect.getFrom().getId() == srcProcessNode.getId())
							pos = i;
					}
					if (pos != -1)
						tgt.getIncomingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).remove(pos);
					new ConnectionImpl(srcProcessNode,
							Node.CONNECTION_DEFAULT_TYPE, split,
							Node.CONNECTION_DEFAULT_TYPE);
					new ConnectionImpl(split, Node.CONNECTION_DEFAULT_TYPE,
							tgt, Node.CONNECTION_DEFAULT_TYPE);
					splitSet.put(String.valueOf(srcProcessNode.getId()), split);
					tag = false;
				}
				if (RuleFlowProcessUtil.getIncomingNodes(tgtProcessNode).size() == 1
						&& !(tgtProcessNode instanceof Join)
						&& !(joinSet.containsKey(String.valueOf(tgtProcessNode
								.getId())))) {

					Join join = new Join();
					join.setName("AddtoJoin" + count2);
					join.setId(count2 + 1000);
					count2++;
					result.addNode(join);
					org.kie.api.definition.process.Node src = RuleFlowProcessUtil
							.getIncomingNodes(tgtProcessNode).get(0);
					tgtProcessNode.getIncomingConnections()
							.get(Node.CONNECTION_DEFAULT_TYPE).clear();
					int pos = -1;
					for (int i = 0; i < src.getOutgoingConnections()
							.get(Node.CONNECTION_DEFAULT_TYPE).size(); i++) {
						Connection connect = src.getOutgoingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).get(i);
						if (connect.getTo().getId() == tgtProcessNode.getId())
							pos = i;
					}
					if (pos != -1)
						src.getOutgoingConnections()
								.get(Node.CONNECTION_DEFAULT_TYPE).remove(pos);
					new ConnectionImpl(src, Node.CONNECTION_DEFAULT_TYPE, join,
							Node.CONNECTION_DEFAULT_TYPE);
					new ConnectionImpl(join, Node.CONNECTION_DEFAULT_TYPE,
							tgtProcessNode, Node.CONNECTION_DEFAULT_TYPE);
					tag = false;
					joinSet.put(String.valueOf(tgtProcessNode.getId()), join);
				}
				if (tag == false)
					addConnection(srcProcessNode, tgtProcessNode, splitSet,
							joinSet);
				// continue;
			}
			if (tag)
				addConnection(srcProcessNode, tgtProcessNode, splitSet, joinSet);
		}
		return result;
	}

	public static com.ibm.bpm.model.Process convertProcesstoIBMProcess(
			RuleFlowProcess process1) {
		com.ibm.bpm.model.Process result = new com.ibm.bpm.model.Process();
		// 1. name
		String name = process1.getName();
		result.setName(name);
		// 2.id
		String id = process1.getId();
		result.setId(id);
		// 3.nodes
		// 3.1 activity 3.2 gateway
		org.kie.api.definition.process.Node[] nodes = process1.getNodes();
		List<Activity> activityList = new ArrayList<Activity>();
		List<Flow> flowList = new ArrayList<Flow>();
		List<Gateway> gatewayList = new ArrayList<Gateway>();
		List<EndEvent> endeventList = new ArrayList<EndEvent>();
		List<StartEvent> starteventList = new ArrayList<StartEvent>();
		for (org.kie.api.definition.process.Node node : nodes) {
			if (node instanceof ActionNode || node instanceof HumanTaskNode) {
				Activity activity = new Activity();
				if (node.getName() != null) {
					name = node.getName();
					activity.setName(name);
				}
				activity.setId(String.valueOf(node.getId()));
				activity.setType(Activity.ACTIVITY_TYPE_TASK);
				activityList.add(activity);
			} else if (node instanceof Split) {
				Split split = (Split) node;
				Gateway gateway = new Gateway();
				if (split.getName() != null) {
					name = split.getName();
					gateway.setName(name);
				}
				gateway.setId(String.valueOf(split.getId()));
				if (split.getType() == Split.TYPE_AND)
					gateway.setType(GatewayType.inclusiveGateway);
				else
					gateway.setType(GatewayType.exclusiveGateway); // 濮掓稒顭堥濠氬及椤栨氨纾介柟杈炬嫹
				gateway.setGatewayDirection("Diverging"); // split
				gatewayList.add(gateway);
			} else if (node instanceof Join) {
				Join join = (Join) node;
				Gateway gateway = new Gateway();
				if (join.getName() != null) {
					name = join.getName();
					gateway.setName(name);
				}
				gateway.setId(String.valueOf(join.getId()));
				if (join.getType() == Split.TYPE_AND)
					gateway.setType(GatewayType.inclusiveGateway);
				else
					gateway.setType(GatewayType.exclusiveGateway); // 濮掓稒顭堥濠氬及椤栨氨纾介柟杈炬嫹
				gateway.setGatewayDirection("Converging"); // join
				gatewayList.add(gateway);
			} else if (node instanceof StartNode) {
				StartNode startNode = (StartNode) node;
				StartEvent startEvent = new StartEvent();
				if (startNode.getName() != null) {
					name = startNode.getName();
					startEvent.setName(name);
				}
				startEvent.setId(String.valueOf(startNode.getId()));
				starteventList.add(startEvent);
			} else if (node instanceof EndNode) {
				EndNode endNode = (EndNode) node;
				EndEvent endEvent = new EndEvent();
				if (endNode.getName() != null) {
					name = endNode.getName();
					endEvent.setName(name);
				}
				endEvent.setId(String.valueOf(endNode.getId()));
				endeventList.add(endEvent);
			}
		}
		// 3.5 set
		result.setActivityList(activityList);
		result.setEndeventList(endeventList);
		result.setFlowNodeRefList(new ArrayList<FlowNodeRef>());
		result.setGatewayList(gatewayList);
		result.setStarteventList(starteventList);
		// 4.flow
		for (org.kie.api.definition.process.Node node : nodes) {
			// ProcessNode processNode = result.findNodeById(node.getId());
			// 闁稿繈鍎寸粩锟�
			Map<String, List<Connection>> connections = node
					.getIncomingConnections();
			for (List<Connection> connectionList : connections.values()) {
				for (Connection connection : connectionList) {
					long id1 = connection.getFrom().getId();
					long id2 = connection.getTo().getId();
					String iid1 = String.valueOf(id1);
					String iid2 = String.valueOf(id2);
					ProcessNode from = result.findNodeById(iid1);
					ProcessNode to = result.findNodeById(iid2);
					Flow flow = new Flow();
					flow.setName(from.getId() + "_" + to.getId());
					flow.setId(from.getId() + "_" + to.getId());
					flow.setSrcNode(from);
					flow.setTargetNode(to);
					flowList.add(flow);
				}
			}
			// 闁告垿缂氱粩锟�
			// //婵絽绻嬮柌婊堟倷閻熸澘娑ч柛蹇撳暱缁洪箖宕楅妷銊х彾闁告鍟胯ぐ鏌ユ晬鐏炶棄姣夐弶鍫ワ拷缁楀鎮介妸銉ュ綘闊洤鍠涚槐婵嬫焼閸喖甯抽梺鎻掔Т椤﹀墽鎷嬮敍鍕毈
		}
		// 5.setAll
		result.setFlowList(flowList);
		return result;
	}

	public static String convertBPMNProcessToXmlString(RuleFlowProcess process) {
		String output = XmlBPMNProcessDumper.INSTANCE.dump(process, true);
		return output;
	}

	public static EPC convertBPMNProcesstoEPC(RuleFlowProcess process) {
		EPC result = new EPC();
		// 1.name
		// ...
		// 2.id
		// ...
		int count = 0;
		for (org.kie.api.definition.process.Node node : process.getNodes()) {
			// 3.startevent //4.endevent
			if (node instanceof StartNode || node instanceof EndNode) {
				// Event event = new Event();
				// if (node.getName() != null)
				// {
				// String name = node.getName();
				// event.setName(name);
				// }
				// String id = String.valueOf(node.getId());
				// event.setId(id);
				// result.addEvent(event);
				Function function = new Function();
				if (node.getName() != null) {
					String name = node.getName();
					function.setName(name);
				}
				String id = String.valueOf(node.getId());
				function.setId(id);
				result.addFunction(function);
				count++;
			}

			// 5.gateway
			if (node instanceof Split || node instanceof Join) {

				Connector connector = new Connector();
				if (node instanceof Split) {
					Split cc = (Split) node;
					if (cc.getType() == Split.TYPE_AND)
						connector.setName(Connector.ANDLabel + "join");
					else if (cc.getType() == Split.TYPE_OR)
						connector.setName(Connector.ORLabel + "join");
					else
						connector.setName(Connector.XORLabel + "join");
				} else {
					Join cc = (Join) node;
					if (cc.getType() == Split.TYPE_AND)
						connector.setName(Connector.ANDLabel + "split");
					else if (cc.getType() == Split.TYPE_OR)
						connector.setName(Connector.ORLabel + "split");
					else
						connector.setName(Connector.XORLabel + "split");
				}
				String id = String.valueOf(node.getId());
				connector.setId(id);
				result.addConnector(connector);
				count++;
			}
			// 6.activity
			if (node instanceof ActionNode || node instanceof HumanTaskNode) {
				Function function = new Function();
				if (node.getName() != null) {
					String name = node.getName();
					function.setName(name);
				}
				String id = String.valueOf(node.getId());
				function.setId(id);
				result.addFunction(function);
				count++;
			}
		}

		// 7.arcs
		for (org.kie.api.definition.process.Node node : process.getNodes()) {
			List<Connection> conns = node.getIncomingConnections().get(
					Node.CONNECTION_DEFAULT_TYPE);
			if (conns == null)
				continue;
			for (Connection connection : conns) {
				count++;
				String id1 = String.valueOf(connection.getFrom().getId());
				String id2 = String.valueOf(connection.getTo().getId());
				Arc a = new Arc();
				nl.tue.tm.is.epc.Node source = null, target = null;
				for (nl.tue.tm.is.epc.Node n : result.getNodes()) {
					if (n.getId().equals(id1)) {
						source = n;
					}
					if (n.getId().equals(id2)) {
						target = n;
					}
				}
				if (source == null || target == null) {
					continue;
				}
				a.setSource(source);
				a.setTarget(target);
				a.setId(String.valueOf(count));
				result.addArc(a);
			}
		}
		// result.cleanEPC();
		return result;
	}

	public static RuleFlowProcess convertEPCtoBPMNProcess(EPC epc) {
		RuleFlowProcess result = new RuleFlowProcess();
		// 1.name
		// ...
		// 2.id
		// ...
		// 3.startevent //4.endevent
		ArrayList<nl.tue.tm.is.epc.Node> singularNodes = new ArrayList<nl.tue.tm.is.epc.Node>();
		HashMap<String, Node> splitSet = new HashMap<String, Node>();
		HashMap<String, Node> joinSet = new HashMap<String, Node>();
		for (Event event : epc.getEvents()) {
			if (epc.getPost(event).size() == 0) // end
			{
				EndNode endNode = new EndNode();
				if (event.getName() != null) {
					String name = event.getName();
					endNode.setName(name);
				}
				String id = event.getId();
				endNode.setId(id.hashCode());
				result.addNode(endNode);
			} else {
				StartNode startNode = new StartNode();
				if (event.getName() != null) {
					String name = event.getName();
					startNode.setName(name);
				}
				String id = event.getId();
				startNode.setId(id.hashCode());
				result.addNode(startNode);
			}
		}
		// 5.gateway
		for (Connector connector : epc.getConnectors()) {
			if (epc.getPost(connector).size() > 1
					&& epc.getPre(connector).size() > 1) // singular..
			{
				singularNodes.add(connector);
			} else if (epc.getPost(connector).size() == 1
					&& epc.getPre(connector).size() > 1) // join..
			{
				Join join = new Join();
				if (connector.getName() != null) {
					String name = connector.getName();
					join.setName(name);
				}
				String id = connector.getId();
				join.setId(id.hashCode());
				result.addNode(join);
			} else /*
					 * if (epc.getPost(connector).size()>1 &&
					 * epc.getPre(connector).size() == 1)
					 */// split || single input/output..
			{
				Split split = new Split();
				if (connector.getName() != null) {
					String name = connector.getName();
					split.setName(name);
				}
				String id = connector.getId();
				split.setId(id.hashCode());
				result.addNode(split);
			}
		}
		// 6.activity
		for (Function function : epc.getFunctions()) {
			if (epc.getPost(function).size() <= 1
					&& epc.getPre(function).size() <= 1) {
				HumanTaskNode action = new HumanTaskNode();
				if (function.getName() != null) {
					String name = function.getName();
					action.setName(name);
				}
				String id = function.getId();
				action.setId(id.hashCode());
				result.addNode(action);
			} else // singular
			{
				singularNodes.add(function);
			}
		}

		// 6.5处理 singular节点
		int count1 = 0, count2 = 0;

		for (nl.tue.tm.is.epc.Node node : singularNodes) {
			if (node instanceof Connector) {
				Join join = new Join();
				join.setName("AndToJoin" + count1);
				join.setId(count1 + 1000);
				count1++;
				result.addNode(join);

				Split split = new Split();
				split.setName("AndToSplit" + count2);
				split.setId(count1 + 1000);
				count2++;
				result.addNode(split);
				new ConnectionImpl(join, Node.CONNECTION_DEFAULT_TYPE, split,
						Node.CONNECTION_DEFAULT_TYPE);
				String id = node.getId();
				splitSet.put(id, split);
				joinSet.put(id, join);

			} else if (node instanceof Function) {
				if (epc.getPre(node).size() > 1) {
					Join join = new Join();
					join.setName("AndToJoin" + count1);
					join.setId(count1 + 1000);
					count1++;
					result.addNode(join);

					String id = node.getId();
					joinSet.put(id, join);
				}
				if (epc.getPost(node).size() > 1) {

					Split split = new Split();
					split.setName("AndToSplit" + count2);
					split.setId(count1 + 1000);
					count2++;
					result.addNode(split);

					String id = node.getId();
					splitSet.put(id, split);
				}
			}

		}
		// 7.arcs
		for (Arc arc : epc.getArcs()) {
			nl.tue.tm.is.epc.Node src = arc.getSource();
			nl.tue.tm.is.epc.Node tgt = arc.getTarget();
			if (src == null || tgt == null) {
				if (epc.getNodes().size() == 2) {
					if (src == null && tgt != null) {
						for (nl.tue.tm.is.epc.Node node : epc.getNodes())
							if (node != tgt)
								src = node;
					} else if (src != null && tgt == null) {
						for (nl.tue.tm.is.epc.Node node : epc.getNodes())
							if (node != src)
								tgt = node;
					}
				} else
					continue;
			}
			int id1 = src.getId().hashCode();
			int id2 = tgt.getId().hashCode();
			org.kie.api.definition.process.Node srcNode = null, tgtNode = null;
			for (org.kie.api.definition.process.Node node : result.getNodes()) {
				if (node.getId() == id1)
					srcNode = node;
				if (node.getId() == id2)
					tgtNode = node;
			}
			if (srcNode == null || tgtNode == null)
				continue;
			addConnection(srcNode, tgtNode, splitSet, joinSet);

		}
		// //8.删除单一的边
		// org.kie.api.definition.process.Node[] temp =
		// result.getNodes().clone();
		// for (org.kie.api.definition.process.Node node : temp)
		// {
		// boolean tag = false;
		// for (int i=0; i<result.getNodes().length; i++)
		// {
		// if (result.getNodes()[i].equals(node))
		// tag = true;
		// }
		// if (!tag)
		// continue;
		// ArrayList<org.kie.api.definition.process.Node> srcs =
		// RuleFlowProcessUtil.getIncomingNodes(node);
		// ArrayList<org.kie.api.definition.process.Node> tgts =
		// RuleFlowProcessUtil.getOutgoingNodes(node);
		//
		// if (node instanceof Join || node instanceof Split)
		// {
		// org.kie.api.definition.process.Node srcNode =
		// RuleFlowProcessUtil.getIncomingNodes(node).get(0);
		// org.kie.api.definition.process.Node tgtNode =
		// RuleFlowProcessUtil.getOutgoingNodes(node).get(0);
		// if (RuleFlowProcessUtil.getOutgoingNodes(srcNode).size() >1 ||
		// RuleFlowProcessUtil.getIncomingNodes(tgtNode).size() >1)
		// continue;
		// if (srcs.size() == 1 && tgts.size() == 1)
		// {
		// long srcId = srcs.get(0).getId();
		// long tgtId = tgts.get(0).getId();
		// org.kie.api.definition.process.Node src = null;
		// org.kie.api.definition.process.Node tgt = null;
		// for (int i=0; i<result.getNodes().length; i++)
		// {
		// if (result.getNodes()[i].getId() == srcId)
		// src = result.getNodes()[i];
		// if (result.getNodes()[i].getId() == tgtId)
		// tgt = result.getNodes()[i];
		// }
		// if (src == null || tgt == null)
		// continue;
		//
		// src.getOutgoingConnections().get(Node.CONNECTION_DEFAULT_TYPE).clear();
		// tgt.getIncomingConnections().get(Node.CONNECTION_DEFAULT_TYPE).clear();
		// result.removeNode(node);
		// new ConnectionImpl(
		// src, Node.CONNECTION_DEFAULT_TYPE,
		// tgt, Node.CONNECTION_DEFAULT_TYPE
		// );
		// }
		// }
		// }
		return result;
	}

	private static void addConnection(org.kie.api.definition.process.Node src,
			org.kie.api.definition.process.Node tgt,
			HashMap<String, Node> splitSet, HashMap<String, Node> joinSet) {
		org.kie.api.definition.process.Node ssrc;
		org.kie.api.definition.process.Node ttgt;
		if (splitSet.containsKey(String.valueOf(src.getId())))
			ssrc = splitSet.get(String.valueOf(src.getId()));
		else
			ssrc = src;
		if (joinSet.containsKey(String.valueOf(tgt.getId())))
			ttgt = joinSet.get(String.valueOf(tgt.getId()));
		else
			ttgt = tgt;
		new ConnectionImpl(ssrc, Node.CONNECTION_DEFAULT_TYPE, ttgt,
				Node.CONNECTION_DEFAULT_TYPE);
	}

	public static RuleFlowProcess parseRuleFlowProcessFromDot(File file)
			throws IOException {
		RuleFlowProcess result;
		int countId = 0;
		String processName;
		String processId;

		// 2.each node and arc

		FileInputStream fis = new FileInputStream(file);
		InputStreamReader isr = new InputStreamReader(fis, "GBK");
		BufferedReader br = new BufferedReader(isr);
		StringBuffer sb = new StringBuffer();
		String line = br.readLine();
		while (line != null) {
			sb.append(line);
			line = br.readLine();
		}
		Dot dot = new Dot();
		dot.parseDotFromString(sb.toString());
		result = DataUtil.convertDotToProcess(dot);

		// 1.getName, getId;
		int dotPos = file.getName().lastIndexOf(".");
		int slashPos = file.getName().indexOf("_");
		processName = file.getName().substring(0, dotPos);
		processId = file.getName().substring(slashPos, dotPos);
		result.setName(processName);
		result.setId(processId);
		return result;
	}

	private static RuleFlowProcess convertDotToProcess(Dot dot) {
		RuleFlowProcess result = new RuleFlowProcess();
		HashMap<String, Node> splitSet = new HashMap<String, Node>();
		HashMap<String, Node> joinSet = new HashMap<String, Node>();
		int splitId = 10000, joinId = 20000;

		for (cn.edu.thss.iise.bpmdemo.analysis.core.fragment.DotNode node : dot
				.getNodeList()) {
			if (node.getType() == cn.edu.thss.iise.bpmdemo.analysis.core.fragment.DotNode.Function) {
				HumanTaskNode task = new HumanTaskNode();

				if (node.getLabel() != null) {
					task.setName(node.getLabel() /*
												 * +
												 * "_"+transition.getLogEvent()
												 * .getEventType()
												 */);
				}
				task.setId(node.getId());
				result.addNode(task);
				if (dot.getInEdges(node).size() > 1)
					addJoin(task, joinId++, result, joinSet);
				if (dot.getOutEdges(node).size() > 1)
					addSplit(task, splitId++, result, splitSet);
			} else if (node.getType() == cn.edu.thss.iise.bpmdemo.analysis.core.fragment.DotNode.Connector) {
				if (dot.getInEdges(node).size() > 1
						&& dot.getOutEdges(node).size() > 1) {
					Split split = new Split();
					if (node.getLabel() != null)
						split.setName(node.getLabel());
					split.setId(node.getId());
					result.addNode(split);
					addJoin(split, joinId, result, joinSet);
				} else if (dot.getInEdges(node).size() <= 1
						&& dot.getOutEdges(node).size() > 1) // split
				{
					Split split = new Split();
					if (node.getLabel() != null)
						split.setName(node.getLabel());
					split.setId(node.getId());
					result.addNode(split);
				} else if (dot.getInEdges(node).size() > 1
						&& dot.getOutEdges(node).size() <= 1) // join
				{
					Join join = new Join();
					if (node.getLabel() != null)
						join.setName(node.getLabel());
					join.setId(node.getId());
					result.addNode(join);

				} else if (dot.getInEdges(node).size() <= 1
						|| dot.getOutEdges(node).size() <= 1) // 用一个join代替
				{
					Join join = new Join();
					if (node.getLabel() != null)
						join.setName(node.getLabel());
					join.setId(node.getId());
					result.addNode(join);
				}
			}

		}

		for (DotArc arc : dot.getArcList()) {
			String srcName = arc.getFromName();
			String tgtName = arc.getToName();
			cn.edu.thss.iise.bpmdemo.analysis.core.fragment.DotNode src = dot
					.getNode(srcName);
			cn.edu.thss.iise.bpmdemo.analysis.core.fragment.DotNode tgt = dot
					.getNode(tgtName);
			org.kie.api.definition.process.Node sourceN = null;
			org.kie.api.definition.process.Node sinkN = null;
			for (org.kie.api.definition.process.Node node : result.getNodes()) {
				if (node.getId() == src.getId())
					sourceN = node;
				if (node.getId() == tgt.getId())
					sinkN = node;
			}
			if (sourceN == null || sinkN == null)
				continue;
			addConnection(sourceN, sinkN, splitSet, joinSet);

		}

		// TODO
		deleteSuperfluousEdges(result);
		// 2. delete1edge
		return result;
	}
}
