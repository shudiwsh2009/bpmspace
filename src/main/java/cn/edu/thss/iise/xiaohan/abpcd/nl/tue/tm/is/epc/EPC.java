package cn.edu.thss.iise.xiaohan.abpcd.nl.tue.tm.is.epc;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

public class EPC {

	private Map<String, Function> functions;
	private Map<String, Event> events;
	private Map<String, Connector> connectors;
	private Set<Arc> arcs;
	private Map<Node, Set<Arc>> arcsBySource;
	private Map<Node, Set<Arc>> arcsByTarget;
	private int nextid = 1;

	public EPC() {
		functions = new HashMap<String, Function>();
		events = new HashMap<String, Event>();
		;
		connectors = new HashMap<String, Connector>();
		arcs = new HashSet<Arc>();
		arcsBySource = new HashMap<Node, Set<Arc>>();
		arcsByTarget = new HashMap<Node, Set<Arc>>();
	}

	private String getNextid() {
		String idToString = "" + nextid;
		while (functions.containsKey(idToString)
				|| events.containsKey(idToString)
				|| connectors.containsKey(idToString) || hasArc(idToString)) {

			nextid++;
			idToString = "" + nextid;
		}
		nextid++;
		return idToString;
	}

	private boolean hasArc(String id) {
		for (Arc a : arcs) {
			if (a.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	private void removeSelfCycles() {
		LinkedList<Arc> toRemove = new LinkedList<Arc>();
		for (Arc a : arcs) {
			if (a.getSource().getId().equals(a.getTarget().getId())) {
				toRemove.add(a);
			}
		}

		for (Arc rm : toRemove) {
			arcs.remove(rm);
			arcsBySource.get(rm.getSource()).remove(rm);
			arcsByTarget.get(rm.getTarget()).remove(rm);
		}
	}

	private void removeSingleNodes(Map<String, ? extends Node> nodemap) {

		LinkedList<String> toRemove = new LinkedList<String>();
		for (Node n : nodemap.values()) {
			if (!arcsBySource.containsKey(n) && !arcsByTarget.containsKey(n)) {
				toRemove.add(n.getId());
			}
		}

		for (String rm : toRemove) {
			nodemap.remove(rm);
		}
	}

	private void removeNodesWithoutLabel(Map<String, ? extends Node> nodemap) {

		LinkedList<Node> toRemove = new LinkedList<Node>();

		for (Node n : nodemap.values()) {
			if (n.getName() == null || n.getName().equals("")
					|| n.getName().trim().equals("\\n")) {
				toRemove.add(n);
			}
		}

		for (Node rm : toRemove) {
			// we should use this method only in case of functions and events
			// the connectors should have a name ..
			// parent
			Set<Arc> fromParent = arcsByTarget.get(rm);

			// childs
			Set<Arc> toChild = arcsBySource.get(rm);

			// we have a source node
			if (fromParent == null || fromParent.size() == 0) {
				// remove node and edge
				nodemap.remove(rm.getId());
				arcsBySource.remove(rm);
				// remove arc toChild
				Node child = toChild.iterator().next().getTarget();
				Set<Arc> childParents = arcsByTarget.get(child);

				if (childParents != null) {
					// this is the only child
					if (childParents.size() == 1) {
						arcsByTarget.remove(child);
						Arc a = childParents.iterator().next();
						arcs.remove(a);
					}
					// there are more parents of its child
					else {
						Arc toRemoveA = null;
						for (Arc a : childParents) {
							if (a.getSource().getId().equals(rm.getId())) {
								toRemoveA = a;
								break;
							}
						}
						if (toRemoveA != null) {
							childParents.remove(toRemoveA);
							arcs.remove(toRemoveA);
						}
					}
				}
			}
			// we have fall node
			else if (toChild == null || toChild.size() == 0) {
				// remove node and edge
				nodemap.remove(rm.getId());
				arcsByTarget.remove(rm);

				// remove arc from parent
				Node parent = fromParent.iterator().next().getSource();
				Set<Arc> parentChildren = arcsBySource.get(parent);

				// this is the only child
				if (parentChildren != null && parentChildren.size() == 1) {
					arcsBySource.remove(parent);
					arcs.remove(parentChildren.iterator().next());
				}
				// there are more children
				else {
					Arc toRemoveA = null;
					for (Arc a : parentChildren) {
						if (a.getTarget().getId().equals(rm.getId())) {
							toRemoveA = a;
							break;
						}
					}
					if (toRemoveA != null) {
						parentChildren.remove(toRemoveA);
						arcs.remove(toRemoveA);
					}
				}
			} else {
				nodemap.remove(rm.getId());
				// use the first arc
				Arc toUse = fromParent.iterator().next();

				Node parent = toUse.getSource();
				Node child = toChild.iterator().next().getTarget();

				arcsByTarget.remove(rm);
				arcsBySource.remove(rm);

				toUse.setTarget(child);

				Set<Arc> parentChildren = arcsBySource.get(parent);
				parentChildren.add(toUse);
				Arc toRemoveA = null;
				for (Arc a : parentChildren) {
					if (a.getTarget().getId().equals(rm.getId())) {
						toRemoveA = a;
						break;
					}
				}
				if (toRemoveA != null) {
					parentChildren.remove(toRemoveA);
					arcs.remove(toRemoveA);
				}

				Set<Arc> childParents = arcsByTarget.get(child);
				childParents.add(toUse);
				toRemoveA = null;
				for (Arc a : childParents) {
					if (a.getSource().getId().equals(rm.getId())) {
						toRemoveA = a;
						break;
					}
				}
				if (toRemoveA != null) {
					childParents.remove(toRemoveA);
					arcs.remove(toRemoveA);
				}
				arcs.add(toUse);
			}
		}
	}

	private void removeSingleEntryExitGateways(Map<String, Connector> nodemap) {

		LinkedList<Node> toRemove = new LinkedList<Node>();

		for (Node n : nodemap.values()) {
			Set<Arc> fromParent = arcsByTarget.get(n);
			Set<Arc> toChild = arcsBySource.get(n);

			if ((fromParent == null || fromParent.size() <= 1)
					&& (toChild == null || toChild.size() <= 1)) {
				toRemove.add(n);
			}
		}

		for (Node rm : toRemove) {
			// we should use this method only in case of functions and events
			// the connectors should have a name ..
			// parent
			Set<Arc> fromParent = arcsByTarget.get(rm);

			// childs
			Set<Arc> toChild = arcsBySource.get(rm);

			// we have a source node
			if (fromParent == null || fromParent.size() == 0) {
				// remove node and edge
				nodemap.remove(rm.getId());
				arcsBySource.remove(rm);

				// remove arc toChild
				Node child = toChild.iterator().next().getSource();
				Set<Arc> childParents = arcsByTarget.get(child);

				if (childParents != null) {
					// this is the only child
					if (childParents.size() == 1) {
						arcsByTarget.remove(child);
					}
					// there are more children
					else {
						Arc toRemoveA = null;
						for (Arc a : childParents) {
							if (a.getSource().getId().equals(rm.getId())) {
								toRemoveA = a;
								break;
							}
						}
						if (toRemoveA != null) {
							childParents.remove(toRemoveA);
						}
					}
				}
			}
			// we have fall node
			else if (toChild == null || toChild.size() == 0) {
				// remove node and edge
				nodemap.remove(rm.getId());
				arcsByTarget.remove(rm);

				// remove arc from parent
				Node parent = fromParent.iterator().next().getSource();
				Set<Arc> parentChildren = arcsBySource.get(parent);

				// this is the only child
				if (parentChildren != null && parentChildren.size() == 1) {
					arcsBySource.remove(parent);
				}
				// there are more children
				else {
					Arc toRemoveA = null;
					for (Arc a : parentChildren) {
						if (a.getTarget().getId().equals(rm.getId())) {
							toRemoveA = a;
							break;
						}
					}
					if (toRemoveA != null) {
						parentChildren.remove(toRemoveA);
					}
				}
			} else {
				nodemap.remove(rm.getId());
				// use the first arc
				Arc toUse = fromParent.iterator().next();

				Node parent = toUse.getSource();
				Node child = toChild.iterator().next().getTarget();

				arcsByTarget.remove(rm);
				arcsBySource.remove(rm);

				arcsBySource.remove(parent);
				arcsByTarget.remove(child);

				toUse.setTarget(child);
				arcsBySource.put(parent, fromParent);
				arcsByTarget.put(child, fromParent);
			}
			nodemap.remove(rm.getId());
		}
	}

	public void addSplitJoinGatewayInfo() {
		addSplitJoinGatewayInfo(connectors);
	}

	private void addSplitJoinGatewayInfo(Map<String, Connector> nodemap) {

		for (Node n : nodemap.values()) {
			Set<Arc> fromParent = arcsByTarget.get(n);
			if (fromParent != null && fromParent.size() > 1) {
				n.setName(n.getName() + "join");
			} else {
				n.setName(n.getName() + "split");
			}
		}
	}

	public void cleanEPC() {
		// remove self cycles - where the start point and end point
		// are the same
		removeSelfCycles();

		// remove nodes that have no parents and children
		removeSingleNodes(functions);
		removeSingleNodes(events);
		removeSingleNodes(connectors);

		removeNodesWithoutLabel(functions);
		removeNodesWithoutLabel(events);
		addGatewaysToFunctionsEvents(functions);
		addGatewaysToFunctionsEvents(events);
		addGatewayToSplitJoinGw();
		removeSingleEntryExitGateways(connectors);
		addSplitJoinGatewayInfo(connectors);
	}

	private void addGatewaysToFunctionsEvents(
			Map<String, ? extends Node> nodemap) {

		LinkedList<Node> toRemove = new LinkedList<Node>();
		for (Node n : nodemap.values()) {
			Set<Arc> fromParent = arcsByTarget.get(n);
			Set<Arc> toChild = arcsBySource.get(n);

			if ((fromParent != null && fromParent.size() > 1)
					|| (toChild != null && toChild.size() > 1)) {
				toRemove.add(n);
			}
		}

		for (Node rm : toRemove) {
			// we should use this method only in case of functions and events
			// the connectors should have a name ..
			// parent
			Set<Arc> fromParent = arcsByTarget.get(rm);

			// childs
			Set<Arc> toChild = arcsBySource.get(rm);

			// function/event has more that one child
			if (toChild != null && toChild.size() > 1) {
				Connector rp = new Connector(getNextid(), "XOR");
				addConnector(rp);
				// remove old and add new node
				arcsBySource.remove(rm);
				arcsBySource.put(rp, toChild);

				for (Arc a : toChild) {
					if (a.getSource().getId().equals(rm.getId())) {
						a.setSource(rp);
					}
				}
				addArc(new Arc(getNextid(), rm, rp));
			}

			// function/event has more that one parent
			if (fromParent != null && fromParent.size() > 1) {

				Connector rp = new Connector(getNextid(), "XOR");
				addConnector(rp);
				// remove old and add new node
				arcsByTarget.remove(rm);
				arcsByTarget.put(rp, fromParent);

				for (Arc a : fromParent) {
					if (a.getTarget().getId().equals(rm.getId())) {
						a.setTarget(rp);
					}
				}
				addArc(new Arc(getNextid(), rp, rm));
			}
		}
	}

	private void addGatewayToSplitJoinGw() {
		LinkedList<Node> toRemove = new LinkedList<Node>();

		for (Connector n : connectors.values()) {
			Set<Arc> fromParent = arcsByTarget.get(n);
			Set<Arc> toChild = arcsBySource.get(n);

			if ((fromParent != null && fromParent.size() > 1)
					&& (toChild != null && toChild.size() > 1)) {
				toRemove.add(n);
			}
		}

		for (Node rm : toRemove) {
			// we should use this method only in case of functions and events
			// the connectors should have a name ..

			// childs
			Set<Arc> toChild = arcsBySource.get(rm);
			Connector rp = new Connector(getNextid(), rm.getName());
			addConnector(rp);
			// remove old and add new node
			arcsBySource.remove(rm);
			arcsBySource.put(rp, toChild);

			for (Arc a : toChild) {
				if (a.getSource().getId().equals(rm.getId())) {
					a.setSource(rp);
				}
			}
			addArc(new Arc(getNextid(), rm, rp));
		}
	}

	public static EPC loadEPML(String fileLocation) {
		EPC result = new EPC();

		EPCParser handler = new EPCParser(result);
		SAXParserFactory factory = SAXParserFactory.newInstance();

		try {
			SAXParser saxParser = factory.newSAXParser();
			saxParser.parse(new File(fileLocation), handler);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}

	public void addFunction(Function f) {
		functions.put(f.getId(), f);
	}

	public void addEvent(Event e) {
		events.put(e.getId(), e);
	}

	public void addConnector(Connector c) {
		connectors.put(c.getId(), c);
	}

	public void addArc(Arc a) {
		arcs.add(a);
		Set<Arc> arcsFromSource = arcsBySource.get(a.getSource());
		if (arcsFromSource == null) {
			arcsFromSource = new HashSet<Arc>();
			arcsFromSource.add(a);
			arcsBySource.put(a.getSource(), arcsFromSource);
		} else {
			arcsFromSource.add(a);
		}
		Set<Arc> arcsToTarget = arcsByTarget.get(a.getTarget());
		if (arcsToTarget == null) {
			arcsToTarget = new HashSet<Arc>();
			arcsToTarget.add(a);
			arcsByTarget.put(a.getTarget(), arcsToTarget);
		} else {
			arcsToTarget.add(a);
		}
	}

	public Function findFunction(String id) {
		return functions.get(id);
	}

	public Event findEvent(String id) {
		return events.get(id);
	}

	public Connector findConnector(String id) {
		return connectors.get(id);
	}

	public Node findNode(String id) {
		Node result = events.get(id);
		if (result != null) {
			return result;
		}
		result = functions.get(id);
		if (result != null) {
			return result;
		}
		result = connectors.get(id);
		return result;
	}

	public Set<Node> getPre(Node n) {
		Set<Node> result = new HashSet<Node>();
		Set<Arc> incoming = arcsByTarget.get(n);
		if (incoming != null) {
			for (Iterator<Arc> i = incoming.iterator(); i.hasNext();) {
				Arc a = i.next();
				result.add(a.getSource());
			}
		}
		return result;
	}

	public Set<Node> getPost(Node n) {
		Set<Node> result = new HashSet<Node>();
		Set<Arc> outgoing = arcsBySource.get(n);
		if (outgoing != null) {
			for (Iterator<Arc> i = outgoing.iterator(); i.hasNext();) {
				Arc a = i.next();
				result.add(a.getTarget());
			}
		}
		return result;
	}

	public Set<Function> getFunctions() {
		return new HashSet<Function>(functions.values());
	}

	public Set<Event> getEvents() {
		return new HashSet<Event>(events.values());
	}

	public Set<Connector> getConnectors() {
		return new HashSet<Connector>(connectors.values());
	}

	public Set<Arc> getArcs() {
		return new HashSet<Arc>(arcs);
	}

	public Set<Node> getNodes() {
		Set<Node> result = new HashSet<Node>();
		result.addAll(functions.values());
		result.addAll(events.values());
		result.addAll(connectors.values());
		return result;
	}

	public String toString() {
		String result = "";

		for (Function f : functions.values()) {
			result += f.getName() + "\n";
		}
		for (Event e : events.values()) {
			result += e.getName() + "\n";
		}
		for (Connector c : connectors.values()) {
			result += c.getName() + "\n";
		}
		for (Arc a : arcs) {
			result += "(" + a.getSource().getName() + ", "
					+ a.getTarget().getName() + ")\n";
		}

		return result;
	}
}
