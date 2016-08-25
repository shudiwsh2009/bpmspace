package cn.edu.thss.iise.bpmdemo.analysis.core;

//package org.thss.analysis.core;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//
//import org.jbpm.ruleflow.core.RuleFlowProcess;
//import org.jbpm.workflow.core.Node;
//import org.jbpm.workflow.core.impl.ConnectionImpl;
//import org.jbpm.workflow.core.node.ActionNode;
//import org.jbpm.workflow.core.node.EndNode;
//import org.jbpm.workflow.core.node.Join;
//import org.jbpm.workflow.core.node.Split;
//import org.jbpm.workflow.core.node.StartNode;
//import org.processmining.framework.models.ModelGraphEdge;
//import org.processmining.framework.models.ModelGraphVertex;
//import org.processmining.framework.models.petrinet.PetriNet;
//import org.processmining.framework.models.petrinet.Place;
//import org.processmining.framework.models.petrinet.Transition;
//
//public class DataUtil {
//	
//	public static RuleFlowProcess convertPetriNettoProcess(PetriNet petriNet)
//	{
//		System.out.println(petriNet.getTransitions().size());
//		ArrayList<Node> nodeList = new ArrayList<Node>();
//		ArrayList<ModelGraphVertex> singularNodeList = new ArrayList<ModelGraphVertex>();
//		RuleFlowProcess result = new RuleFlowProcess();
//		//1.name
//		if (petriNet.getIdentifier()!=null)
//			result.setName(petriNet.getIdentifier());
//		//2.id
//		result.setId(petriNet.getIdentifier());
//		//3.startnode	
//		StartNode startNode = new StartNode();
//		ModelGraphVertex sourceNode = petriNet.getSource();
//		if (sourceNode.getIdentifier() != null)
//		{
//			startNode.setName(sourceNode.getIdentifier());			
//		}
//		startNode.setId(sourceNode.getId());
//		result.addNode(startNode);
//		nodeList.add(startNode);
//		//4.endnode
//		EndNode endNode = new EndNode();
//		petriNet.getEndNodes();
//		ArrayList<ModelGraphVertex> sinkNodes = new ArrayList<ModelGraphVertex>();
//		for (ModelGraphVertex sinkNode	:	petriNet.getEndNodes())
//		{
//			//ModelGraphVertex sinkNode = petriNet.getSink();
//			if (sinkNode.getIdentifier() != null)
//				endNode.setName(sinkNode.getIdentifier());
//			
//			endNode.setId(sinkNode.getId());
//			result.addNode(endNode);
//			nodeList.add(endNode);
//		}
//		//transition
//		for (Transition transition	:	petriNet.getTransitions())
//		{
//			//if (!transition.getLogEvent().getEventType().equals("complete"))
//			//	continue;				
//			ActionNode task = new ActionNode();
//			if (transition.getIdentifier()!=null ){
//				task.setName(transition.getIdentifier() /*+ "_"+transition.getLogEvent().getEventType()*/);
//			}
//			task.setId(transition.getId());		
//			result.addNode(task);
//			nodeList.add(task);
//			if (transition.getInEdges().size()>1 || transition.getOutEdges().size() >1)
//				singularNodeList.add(transition);
//		}		
//		//6.place to gateway
//		for (Place place	:	petriNet.getPlaces())
//		{
//			if (place.equals(sourceNode))
//				continue;
//			//if (place.equals(sinkNode))
//			if (sinkNodes.contains(place))
//				continue;
//			if (place.getPredecessors().size() == 0 || place.getSuccessors().size() > 0)	//split
//			{
//				Split split = new Split();
//				if (place.getIdentifier() != null)
//					split.setName(place.getIdentifier());
//				split.setId(place.getId());
//				result.addNode(split);
//				nodeList.add(split);
//			}
//			else if (place.getSuccessors().size() == 0 || place.getPredecessors().size() > 0 )	//join
//			{
//				Join join = new Join();
//				if (place.getIdentifier() != null)
//					join.setName(place.getIdentifier());
//				join.setId(place.getId());
//				result.addNode(join);
//				nodeList.add(join);
//			} 
//			else
//			{
//				singularNodeList.add(place);			//multiple in-degree and out-degree, process later
//			}
//						
//		}
//		//��Ҫ���ظ�����
//		//7.arc to connectionimpl
//		for (ModelGraphEdge edge	: (ArrayList<ModelGraphEdge>) petriNet.getEdges())
//		{
//			ModelGraphVertex source  = edge.getSource();
//			ModelGraphVertex sink = edge.getDest();
//			Node sourceN = null, sinkN = null;
//			for (Node node	:	nodeList)
//			{
//				if (node.getId() == source.getId())
//					sourceN = node;
//				if (node.getId() == sink.getId())
//					sinkN = node;
//			}
//			if (sourceN instanceof ActionNode && sourceN.getOutgoingConnections().size()==1)
//				continue;
//			if (sinkN instanceof ActionNode && sinkN.getIncomingConnections().size()==1)
//				continue;	
//			if (sourceN == null || sinkN == null)
//				continue;
//			if (sinkN instanceof Join && sinkN != endNode && sinkN.getOutgoingConnections().size() == 0)
//				new ConnectionImpl(
//						sourceN, Node.CONNECTION_DEFAULT_TYPE,
//						endNode, Node.CONNECTION_DEFAULT_TYPE
//				 );				
//			else
//				new ConnectionImpl(
//						sourceN, Node.CONNECTION_DEFAULT_TYPE,
//						sinkN, Node.CONNECTION_DEFAULT_TYPE
//				 );			
//		}
//		//8.����singularProcess
//		for (ModelGraphVertex vertex	:	singularNodeList)
//		{
//			if (vertex instanceof Place)	//��������place	
//			{
//				Place place = (Place) vertex;
//				Split split = new Split();
//				if (place.getIdentifier() != null)
//					split.setName(place.getIdentifier()+"_split");
//				split.setId(place.getId()+1000);
//				result.addNode(split);
//				
//				Join join = new Join();
//				if (place.getIdentifier() != null)
//					join.setName(place.getIdentifier()+"_join");
//				join.setId(place.getId()+10000);
//				result.addNode(join);
//				
//				//1.split join����ӱ�
//				new ConnectionImpl(
//						join, Node.CONNECTION_DEFAULT_TYPE,
//						split, Node.CONNECTION_DEFAULT_TYPE
//				 );
//				
//				//2.split����
//				for (ModelGraphVertex outV	:(HashSet<ModelGraphVertex>) place.getSuccessors())
//				{
//					Node sink = null;
//					for (Node node	:	nodeList)
//					{
//						if (node.getId() == outV.getId())
//							sink = node;
//					}
//					if (sink == null)
//						continue;
//					new ConnectionImpl(
//							split, Node.CONNECTION_DEFAULT_TYPE,
//							sink, Node.CONNECTION_DEFAULT_TYPE
//					 );
//				}
//				
//				//3.join���
//				for (ModelGraphVertex inV	:(HashSet<ModelGraphVertex>)	 place.getPredecessors())
//				{
//					Node source = null;
//					for (Node node	:	nodeList)
//					{
//						if (node.getId() == inV.getId())
//							source = node;
//					}
//					if (source == null)
//						continue;
//					new ConnectionImpl(
//							source, Node.CONNECTION_DEFAULT_TYPE,
//							join, Node.CONNECTION_DEFAULT_TYPE
//					 );
//				}
//			}
//			else if (vertex instanceof Transition)	//������߶����transition
//			{
//				
//			}
//				
//				
//		}
//		return result;
//	}
// }
