package cn.edu.thss.iise.beehivez.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.processmining.framework.models.petrinet.PNEdge;
import org.processmining.framework.models.petrinet.PNNode;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.Place;
import org.processmining.framework.models.petrinet.Transition;

import com.ibm.bpm.model.Activity;
import com.ibm.bpm.model.EndEvent;
import com.ibm.bpm.model.Flow;
import com.ibm.bpm.model.Gateway;
import com.ibm.bpm.model.Process;
import com.ibm.bpm.model.StartEvent;

public class BPMNConvertor {

	public static PetriNet convertOriginalBPMNTOProcessModel(Process process) {
		PetriNet result = new PetriNet();
		List<Activity> activies = process.getActivityList();
		Map<String, PNNode> PNNodes = new HashMap<String, PNNode>();
		// 1.add All activities
		for (Activity activity : activies) {
			String label = activity.getName();
			String id = activity.getId();

			Transition t = new Transition(label, result);
			t.setAttribute("id", id);
			result.addTransition(t);
			PNNodes.put(id, t);
		}

		// 2.add All places
		List<Gateway> gateways = process.getGatewayList();
		for (Gateway gateway : gateways) {
			String label = gateway.getName();
			String id = gateway.getId();
			Place p = new Place(label, result);
			result.addPlace(p);
			PNNodes.put(id, p);
		}

		// 3.add All events
		List<EndEvent> endEvents = process.getEndeventList();
		for (EndEvent endEvent : endEvents) {
			String label = endEvent.getName();
			String id = endEvent.getId();
			Place p = new Place(label, result);
			result.addPlace(p);
			PNNodes.put(id, p);
		}
		ArrayList<Place> startPlaces = new ArrayList<Place>();

		List<StartEvent> startEvents = process.getStarteventList();
		for (StartEvent startEvent : startEvents) {
			String label = startEvent.getName();
			String id = startEvent.getId();
			Place p = new Place(label, result);
			startPlaces.add(p);
			result.addPlace(p);
			PNNodes.put(id, p);
		}

		// 4 ����flow
		int count = 0;
		int countT = 0;
		List<Flow> isomorphicNode = new ArrayList<Flow>();
		List<Flow> flows = process.getFlowList();
		for (Flow flow : flows) {
			String srcId = flow.getSrcNode().getId();
			String tarId = flow.getTargetNode().getId();
			PNNode from, to;
			from = PNNodes.get(srcId);
			to = PNNodes.get(tarId);
			PNEdge edge;

			if (from instanceof Transition && to instanceof Place) {
				edge = new PNEdge((Transition) from, (Place) to);
				result.addEdge(edge);
			} else if (from instanceof Place && to instanceof Transition) {
				edge = new PNEdge((Place) from, (Transition) to);
				result.addEdge(edge);
			} else if (from instanceof Transition && to instanceof Transition) {
				isomorphicNode.add(flow);
			} else if (from instanceof Place && to instanceof Place) {
				// ��ô���ܡ�������������С���
				isomorphicNode.add(flow);
			}
		}

		// ����ͬ�ʽڵ�
		for (Flow flow : isomorphicNode) {
			String srcId = flow.getSrcNode().getId();
			String tarId = flow.getTargetNode().getId();
			PNNode from, to;
			from = PNNodes.get(srcId);
			to = PNNodes.get(tarId);

			if (from instanceof Transition && to instanceof Transition) {
				if (to.getPredecessors().size() == 0) {
					Place p = new Place("countP" + count, result);
					result.addPlace(p);
					count++;
					PNEdge edge1 = new PNEdge((Transition) from, (Place) p);
					PNEdge edge2 = new PNEdge((Place) p, (Transition) to);
					result.addEdge(edge1);
					result.addEdge(edge2);
				}
				// ��ǰtoֻ��һ��ǰ��������ǰ��ָֻ��to
				else if (to.getPredecessors().size() == 1
						&& ((Place) to.getPredecessors().iterator().next())
								.getSuccessors().size() == 1) {
					Place p = (Place) to.getPredecessors().iterator().next();
					PNEdge edge1 = new PNEdge((Transition) from, (Place) p);
					result.addEdge(edge1);
				}
				// ��ǰto�ж��ǰ��������һ��ǰ�������ǲ���ָ��to
				else {
					HashSet places = (HashSet) to.getPredecessors();
					// 1.�Ͼ�precessors�뵱ǰ�ڵ�Ĺ�ϵ
					Iterator it = places.iterator();
					while (it.hasNext()) {
						Place p = (Place) it.next();
						result.delEdge(p, to);
					}
					// ����һ���µĽڵ�
					Place pp = new Place("countP" + count, result);
					result.addPlace(pp);
					count++;
					PNEdge edge = new PNEdge((Place) pp, (Transition) to);
					result.addEdge(edge);
					// 3.ÿ�����еĽڵ����
					it = places.iterator();
					while (it.hasNext()) {
						Place p = (Place) it.next();
						Transition t = new Transition("countT" + countT, result);
						result.addTransition(t);
						countT++;
						PNEdge edge1 = new PNEdge((Place) p, (Transition) t);
						PNEdge edge2 = new PNEdge((Transition) t, (Place) pp);
						result.addEdge(edge1);
						result.addEdge(edge2);
					}
					// 4.�½ڵ����
					PNEdge edge1 = new PNEdge((Transition) from, (Place) pp);
					result.addEdge(edge1);
				}

			} else if (from instanceof Place && to instanceof Place) {
				// ��ô���ܡ�������������С���
				Transition t = new Transition("countT" + countT, result);
				result.addTransition(t);
				countT++;
				PNEdge edge1 = new PNEdge((Place) from, (Transition) t);
				PNEdge edge2 = new PNEdge((Transition) t, (Place) to);
				result.addEdge(edge1);
				result.addEdge(edge2);
			}
		}
		// ����һ��transition�ж����������
		ArrayList<Transition> transitions = (ArrayList<Transition>) result
				.getTransitions().clone();
		for (Transition transition : transitions) {
			if (transition.getPredecessors().size() > 1) // ���transition��������1
			{
				Place p = new Place("countP" + count, result); // transition֮ǰ���Ǹ�
				result.addPlace(p);
				count++;
				Iterator it = transition.getPredecessors().iterator();
				while (it.hasNext()) {
					Place p1 = (Place) it.next();

					Transition t = new Transition("countT" + countT, result);
					result.addTransition(t);
					countT++;
					PNEdge edge1 = new PNEdge((Place) p1, (Transition) t);
					PNEdge edge2 = new PNEdge((Transition) t, (Place) p);
					result.addEdge(edge1);
					result.addEdge(edge2);
					result.delEdge(p1, transition);
				}
				PNEdge edge = new PNEdge(p, transition);
				result.addEdge(edge);
			}
		}
		// ����һ��transition�ж����������
		for (Transition transition : transitions) {
			if (transition.getSuccessors().size() > 1) {
				Place p = new Place("countP" + count, result); // transition֮ǰ���Ǹ�
				result.addPlace(p);
				count++;
				Iterator it = transition.getSuccessors().iterator();
				while (it.hasNext()) {
					Place p1 = (Place) it.next();

					Transition t = new Transition("countT" + countT, result);
					result.addTransition(t);
					countT++;
					PNEdge edge1 = new PNEdge((Place) p, (Transition) t);
					PNEdge edge2 = new PNEdge((Transition) t, (Place) p1);
					result.addEdge(edge1);
					result.addEdge(edge2);
					result.delEdge(transition, p1);
				}
				PNEdge edge = new PNEdge(transition, p);
				result.addEdge(edge);
			}
		}
		return result;
	}

	private static String refine(String name) {
		String sb;
		int pos = name.lastIndexOf("/");
		if (pos == -1)
			return name;
		pos++;
		while (name.charAt(pos) == ' ')
			pos++;
		sb = name.substring(pos);
		return sb;
	}
}
