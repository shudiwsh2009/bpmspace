package com.ibm.bpm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.ibm.bpm.model.Activity;
import com.ibm.bpm.model.EndEvent;
import com.ibm.bpm.model.Flow;
import com.ibm.bpm.model.FlowNodeRef;
import com.ibm.bpm.model.Gateway;
import com.ibm.bpm.model.Gateway.GatewayType;
import com.ibm.bpm.model.OWLClass;
import com.ibm.bpm.model.Process;
import com.ibm.bpm.model.ProcessNode;
import com.ibm.bpm.model.Property;
import com.ibm.bpm.model.Segment;
import com.ibm.bpm.model.StartEvent;
import com.ibm.bpm.model.Structure;
import com.ibm.bpm.util.OWLModel.ActivityEntity;

public class BPMNModel {

	private List<List<String>> _SynonymActivities;
	private List<List<String>> _SynonymGateways;
	private List<List<String>> _SynonymFlows;

	private List<ProcessNode> handledNodes = null;

	private List<Activity> cutOffActivityList;
	private List<FlowNodeRef> cutOffFlowNodeRefList;
	private List<Gateway> cutOffGatewayList;
	private List<Flow> cutOffFlowList;
	private List<Flow> cutOffOutgoingFlowList;

	public int threshold = 0;

	public int totalActivityNumber = 0;
	public int totalGatewayNumber = 0;
	public int totalActivityMergedNumber = 0;
	public int totalGatewayMergedNumber = 0;
	public int perActivityNumber = 0;
	public int perGatewayNumber = 0;

	public void readSemanticTable() throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException {
		readSemanticTable("E:/work/Clients/CMCC/test/wangwj/semanticTable.xml");
	}

	public void readSemanticTable(String filePath)
			throws FileNotFoundException, ParserConfigurationException,
			SAXException, IOException {
		Element element = readFile(filePath);

		NodeList actListNodes = element.getElementsByTagName("activities");
		this._SynonymActivities = new ArrayList<List<String>>();
		for (int i = 0; i < actListNodes.getLength(); i++) {
			Element actListElement = (Element) actListNodes.item(i);
			NodeList actNodes = actListElement.getElementsByTagName("activity");
			List<String> list = new ArrayList<String>();
			for (int j = 0; j < actNodes.getLength(); j++) {
				Element act = (Element) actNodes.item(j);
				list.add(act.getAttribute("name"));
			}
			this._SynonymActivities.add(list);
		}

		NodeList gatewayListNodes = element.getElementsByTagName("gateways");
		this._SynonymGateways = new ArrayList<List<String>>();
		for (int i = 0; i < gatewayListNodes.getLength(); i++) {
			Element gatewayListElement = (Element) gatewayListNodes.item(i);
			NodeList gatewayNodes = gatewayListElement
					.getElementsByTagName("gateway");
			List<String> list = new ArrayList<String>();
			for (int j = 0; j < gatewayNodes.getLength(); j++) {
				Element gateway = (Element) gatewayNodes.item(j);
				list.add(gateway.getAttribute("name"));
			}
			this._SynonymGateways.add(list);
		}

		NodeList flowListNodes = element.getElementsByTagName("flows");
		this._SynonymFlows = new ArrayList<List<String>>();
		for (int i = 0; i < flowListNodes.getLength(); i++) {
			Element flowListElement = (Element) flowListNodes.item(i);
			NodeList flowNodes = flowListElement.getElementsByTagName("flow");
			List<String> list = new ArrayList<String>();
			for (int j = 0; j < flowNodes.getLength(); j++) {
				Element flow = (Element) flowNodes.item(j);
				list.add(flow.getAttribute("name"));
			}
			this._SynonymFlows.add(list);
		}
	}

	public void readSemanticTableOwl(String filePath) throws Exception {
		OWLModel model = new OWLModel();
		Element element = model.getElement(filePath);
		ArrayList<OWLClass> listTable = model.getOWLList(element);

		this._SynonymActivities = new ArrayList<List<String>>();
		for (OWLClass owl : listTable) {
			List<String> list = new ArrayList<String>();
			list.add(owl.getRdf().replace("_", " "));
			for (Property property : owl.getProp()) {
				list.add(property.getValuesFrom_value().replace("_", " "));
			}
			this._SynonymActivities.add(list);
		}
	}

	public Process getProcess(String fileName) throws Exception {
		Element element = readFile(fileName);
		Process process = setAttribute(element);
		process.setActivityList(getActivityList(element));
		process.setGatewayList(getGatewayList(element));
		process.setStarteventList(getStartEventList(element));
		process.setEndeventList(getEndEventList(element));
		process.setFlowNodeRefList(getFlowNodeRefList(element, process));
		process.setFlowList(getFlowList(element, process));
		return process;
	}

	public Segment getFragmentWithOwl(String fragmentFilePath,
			String owlFilePath) throws Exception {
		Element element = readFile(fragmentFilePath);
		Segment fragment = setAttributeForSegment(element);
		fragment.setActivityList(getActivityList(element));
		fragment.setGatewayList(getGatewayList(element));
		fragment.setStarteventList(getStartEventList(element));
		fragment.setEndeventList(getEndEventList(element));
		fragment.setFlowNodeRefList(getFlowNodeRefList(element, fragment));
		fragment.setFlowList(getFlowList(element, fragment));
		// handle the inNodeList and outNodeList
		this.setMultiInMultiOut(fragment, owlFilePath);
		return fragment;
	}

	private void setMultiInMultiOut(Segment fragment, String owlFilePath)
			throws Exception {
		OWLModel model = new OWLModel();
		Element element = model.getElement(owlFilePath);
		ArrayList<OWLClass> list = model.getOWLList(element);
		List<Flow> inNodeList = new ArrayList<Flow>();
		List<Flow> outNodeList = new ArrayList<Flow>();
		for (OWLClass owl : list) {
			String owlName = owl.getRdf().replace("_", " ");
			ProcessNode node = this.findNodeByName(fragment, owlName);
			// System.out.println("find node " + node.getId() + " from owl");
			for (Property property : owl.getProp()) {
				if (property.getOnProperty_value().equals("Incoming")
						|| property.getOnProperty_value().equals("Outcoming")) {
					String[] strTempNames = property.getValuesFrom_value()
							.split("-");
					String nodeName = strTempNames[0].replace("_", " ");
					String processName = strTempNames[1].replace("_", " ");
					Flow flow = new Flow();
					flow.setName("");
					flow.setId("bpmn-" + UUID.randomUUID());
					ProcessNode intraNode = null;
					String intraNodeId = "bpmn-" + UUID.randomUUID();
					if (nodeName.contains(" / ")) { // activity
						intraNode = new Activity();
						((Activity) intraNode)
								.setType(Activity.ACTIVITY_TYPE_TASK);
					} else { // gateway
						intraNode = new Gateway();
						((Gateway) intraNode)
								.setType(GatewayType.exclusiveGateway);
					}
					intraNode.setId(intraNodeId);
					intraNode.setName(nodeName);
					intraNode.setProcessName(processName);
					if (property.getOnProperty_value().equals("Incoming")) {
						flow.setSrcNode(intraNode);
						flow.setTargetNode(node);
						inNodeList.add(flow);
					} else if (property.getOnProperty_value().equals(
							"Outcoming")) {
						flow.setSrcNode(node);
						flow.setTargetNode(intraNode);
						outNodeList.add(flow);
					}
				}
			}
		}
		fragment.setInNodeList(inNodeList);
		fragment.setOutNodeList(outNodeList);
	}

	private Process setAttribute(Element element) {
		Process process = new Process();
		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			// System.out.println("node: " + name);
			// System.out.println("node: " + node.getNodeValue());
			if ("process".equalsIgnoreCase(name)) {
				process.setId(((Element) node).getAttribute("id"));
				process.setName(((Element) node).getAttribute("name"));
				process.setType("OA");
				// System.out.println("id " +
				// ((Element)node).getAttribute("id"));
			}
		}
		return process;

	}

	private Segment setAttributeForSegment(Element element) {
		Segment process = new Segment();
		NodeList nodes = element.getChildNodes();
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			String name = node.getNodeName();
			// System.out.println("node: " + name);
			// System.out.println("node: " + node.getNodeValue());
			if ("process".equalsIgnoreCase(name)) {
				process.setId(((Element) node).getAttribute("id"));
				process.setName(((Element) node).getAttribute("name"));
				process.setType("OA");
				// System.out.println("id " +
				// ((Element)node).getAttribute("id"));
			}
		}
		return process;

	}

	private Element readFile(String fileName) throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException {
		File f = new File(fileName);
		InputStream input = new FileInputStream(f);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document document = builder.parse(input);
		Element element = document.getDocumentElement();
		return element;
	}

	private List<Activity> getActivityList(Element element) throws Exception {
		List<Activity> list = new ArrayList<Activity>();
		NodeList actNodes = element.getElementsByTagName("task");
		for (int i = 0; i < actNodes.getLength(); i++) {
			Element actElement = (Element) actNodes.item(i);
			Activity act = new Activity();
			act.setId(actElement.getAttribute("id"));
			// Add by Wangwj
			act.setType(Activity.ACTIVITY_TYPE_TASK);
			String description = actElement.getAttribute("name");
			act.setName(description);
			// System.out.println("getActivityList: task " + act.getId() + " " +
			// act.getName() + " " + description);
			list.add(act);
		}// end for i

		NodeList calActNodes = element.getElementsByTagName("callActivity");
		for (int i = 0; i < calActNodes.getLength(); i++) {
			Element actElement = (Element) calActNodes.item(i);
			Activity act = new Activity();
			act.setId(actElement.getAttribute("id"));
			// Add by Wangwj
			act.setType(Activity.ACTIVITY_TYPE_CALLACTIVITY);
			String description = actElement.getAttribute("name");
			act.setCalledElement(actElement.getAttribute("calledElement"));
			act.setName(description);
			// System.out.println("getActivityList: callActivity " + act.getId()
			// + " " + act.getName() + " " + description);
			list.add(act);
		}// end for i
		return list;
	}

	private List<FlowNodeRef> getFlowNodeRefList(Element element,
			Process process) {
		List<FlowNodeRef> list = new ArrayList<FlowNodeRef>();

		NodeList flowNodeRefs = element.getElementsByTagName("flowNodeRef");
		for (int i = 0; i < flowNodeRefs.getLength(); i++) {
			Element flowNodeElement = (Element) flowNodeRefs.item(i);
			ProcessNode node = this.findNodeById(process,
					flowNodeElement.getTextContent());
			FlowNodeRef flowNodeRef = new FlowNodeRef(node);
			// System.out.println("Get flowNodeRef id:" + node.getId());
			list.add(flowNodeRef);
		}// end for i
		return list;
	}

	private List<Flow> getFlowList(Element element, Process process) {
		List<Flow> list = new ArrayList<Flow>();

		NodeList flowNodes = element.getElementsByTagName("sequenceFlow");
		for (int i = 0; i < flowNodes.getLength(); i++) {
			Element flowElement = (Element) flowNodes.item(i);
			Flow flow = new Flow();
			flow.setId(flowElement.getAttribute("id"));
			flow.setName(flowElement.getAttribute("name"));
			int num = 0;
			for (FlowNodeRef nodeRef : process.getFlowNodeRefList()) {
				if (nodeRef.getNode().getId()
						.equals(flowElement.getAttribute("sourceRef"))) {
					flow.setSrcNode(nodeRef.getNode());
					num++;
				}
				if (nodeRef.getNode().getId()
						.equals(flowElement.getAttribute("targetRef"))) {
					flow.setTargetNode(nodeRef.getNode());
					num++;
				}
				if (num == 2) {
					num = 0;
					break;
				}
			}
			// System.out.println(flow.getId() + "  " +
			// flow.getSrcNode().getId() + " ==> " +
			// flow.getTargetNode().getId());
			list.add(flow);
		}// end for i
		return list;
	}

	private List<StartEvent> getStartEventList(Element element)
			throws Exception {
		List<StartEvent> list = new ArrayList<StartEvent>();
		NodeList startEventNodes = element.getElementsByTagName("startEvent");
		for (int i = 0; i < startEventNodes.getLength(); i++) {
			Element flowElement = (Element) startEventNodes.item(i);
			StartEvent se = new StartEvent();
			se.setId(flowElement.getAttribute("id"));
			se.setName(flowElement.getAttribute("name"));
			// System.out.println(se.getId() + " " + se.getName());
			list.add(se);
		}// end for i
		return list;
	}

	private List<EndEvent> getEndEventList(Element element) throws Exception {
		List<EndEvent> list = new ArrayList<EndEvent>();
		NodeList endEventNodes = element.getElementsByTagName("endEvent");
		for (int i = 0; i < endEventNodes.getLength(); i++) {
			Element flowElement = (Element) endEventNodes.item(i);
			EndEvent ee = new EndEvent();
			ee.setId(flowElement.getAttribute("id"));
			ee.setName(flowElement.getAttribute("name"));
			// System.out.println(ee.getId() + " " + ee.getName());
			list.add(ee);
		}// end for i
		return list;
	}

	private List<Gateway> getGatewayList(Element element) {
		List<Gateway> list = new ArrayList<Gateway>();

		NodeList exclusiveGatewayNodes = element
				.getElementsByTagName("exclusiveGateway");
		for (int i = 0; i < exclusiveGatewayNodes.getLength(); i++) {
			Element gatewayElement = (Element) exclusiveGatewayNodes.item(i);
			Gateway gateway = new Gateway();
			gateway.setId(gatewayElement.getAttribute("id"));
			gateway.setName(gatewayElement.getAttribute("name"));
			gateway.setGatewayDirection(gatewayElement
					.getAttribute("gatewayDirection"));
			gateway.setType(GatewayType.exclusiveGateway);
			// System.out.println(gateway.getId() + " " + gateway.getName() +
			// " " + gateway.getGatewayDirection());
			list.add(gateway);
		}// end for i

		NodeList inclusiveGatewayNodes = element
				.getElementsByTagName("inclusiveGateway");
		for (int i = 0; i < inclusiveGatewayNodes.getLength(); i++) {
			Element gatewayElement = (Element) inclusiveGatewayNodes.item(i);
			Gateway gateway = new Gateway();
			gateway.setId(gatewayElement.getAttribute("id"));
			gateway.setName(gatewayElement.getAttribute("name"));
			gateway.setGatewayDirection(gatewayElement
					.getAttribute("gatewayDirection"));
			gateway.setType(GatewayType.exclusiveGateway);
			// System.out.println(gateway.getId() + " " + gateway.getName() +
			// " " + gateway.getGatewayDirection());
			list.add(gateway);
		}// end for i
		return list;
	}

	public Process Transformation(Process pre_p) {
		List<FlowNodeRef> flowNodeRefList = new ArrayList<FlowNodeRef>();
		List<Flow> flowList = pre_p.getFlowList();
		List<Gateway> gatewayList = pre_p.getGatewayList();
		List<Activity> actList = pre_p.getActivityList();
		List<Structure> stru = new ArrayList<Structure>();
		List<Structure> struGateway = new ArrayList<Structure>();
		StartEvent s = pre_p.getStarteventList().get(0);
		List<EndEvent> ee = pre_p.getEndeventList();
		FlowNodeRef fnode = new FlowNodeRef();
		fnode.setNode(s);
		flowNodeRefList.add(fnode);

		for (int l = 0; l < ee.size(); l++) {
			FlowNodeRef fnode0 = new FlowNodeRef();
			fnode0.setNode(ee.get(l));
			flowNodeRefList.add(fnode0);
		}

		// record gateway
		Iterator<Gateway> iteratorgateway = gatewayList.iterator();
		while (iteratorgateway.hasNext()) {
			FlowNodeRef fnode0 = new FlowNodeRef();
			Gateway gw = iteratorgateway.next();
			fnode0.setNode(gw);
			flowNodeRefList.add(fnode0);
			Structure stru0 = new Structure();
			stru0.setGateway(gw);
			List<Flow> flow = new ArrayList<Flow>();
			List<String> label = new ArrayList<String>();
			Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
			while (iter_flow.hasNext()) {
				Flow f = iter_flow.next();
				if (f.getSrcNode().getId().equals(gw.getId())) {
					flow.add(f);
					label.add(f.getName());
				}
			}
			stru0.setFlowList(flow);
			stru0.setLabel(label);
			struGateway.add(stru0);
		}

		for (int a = 0; a < struGateway.size(); a++) {
			if (struGateway.get(a).Check(struGateway.get(a).getLabel())) {
				List<String> str = struGateway.get(a).GetString(
						struGateway.get(a).getLabel());
				for (int b = 0; b < str.size(); b++) {
					String suuid = "bpmn-" + UUID.randomUUID().toString();
					Gateway gateway = new Gateway("Decision", suuid,
							"Diverging", GatewayType.inclusiveGateway);// ����ӵ�gateway
					FlowNodeRef flownode = new FlowNodeRef(gateway);
					gatewayList.add(gateway);
					flowNodeRefList.add(flownode);

					Iterator<Flow> iter_flow = flowList.iterator();
					int num = 0;
					while (iter_flow.hasNext()) {
						Flow f = iter_flow.next();
						if (f.getSrcNode()
								.getId()
								.equals(struGateway.get(a).getGateway().getId())
								&& f.getName().equals(str.get(b))) {
							f.setSrcNode(gateway);
							if (num == 0)
								f.setName("No");
							else
								f.setName("Yes");
							num++;
						}
					}
					String fuuid = "bpmn-" + UUID.randomUUID().toString();
					Flow f0 = new Flow(str.get(b), fuuid, struGateway.get(a)
							.getGateway(), gateway);
					flowList.add(f0);
				}
			}
		}

		Iterator<Activity> iter_act = actList.iterator(); // iterate
															// activityList to
															// get flow
		while (iter_act.hasNext()) {
			Structure stru0 = new Structure();
			FlowNodeRef fn = new FlowNodeRef();
			Activity act = iter_act.next();
			fn.setNode(act);
			flowNodeRefList.add(fn);
			stru0.setActivity(act);
			List<Flow> flow = new ArrayList<Flow>();
			Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
			while (iter_flow.hasNext()) {
				Flow f = iter_flow.next();
				if (f.getSrcNode().getId().equals(act.getId())) {
					flow.add(f);
				}
			}
			stru0.setFlowList(flow);
			stru.add(stru0);

		}

		Structure stru00 = new Structure();
		stru00.setSe(s);

		List<Flow> flow = new ArrayList<Flow>();
		Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
		while (iter_flow.hasNext()) {

			Flow f = iter_flow.next();
			if (f.getSrcNode().getId().equals(s.getId())) {
				flow.add(f);
			}
		}
		stru00.setFlowList(flow);
		stru.add(stru00);
		for (int j = 0; j < stru.size(); j++) {
			if (stru.get(j).getFlowList().size() >= 2) {
				String suuid = "bpmn-" + UUID.randomUUID().toString();
				Gateway gateway = new Gateway("Decision", suuid, "Diverging",
						GatewayType.inclusiveGateway);// ����ӵ�gateway
				FlowNodeRef flownode = new FlowNodeRef(gateway);
				gatewayList.add(gateway);
				flowNodeRefList.add(flownode);
				if (stru.get(j).getActivity() != null) {
					Iterator<Flow> iterflow = pre_p.getFlowList().iterator();
					while (iterflow.hasNext()) {
						Flow f = iterflow.next();
						if (f.getSrcNode().getId()
								.equals(stru.get(j).getActivity().getId())) {
							f.setSrcNode(gateway);
						}
					}
					String fuuid = "bpmn-" + UUID.randomUUID().toString();
					Flow f0 = new Flow("", fuuid, stru.get(j).getActivity(),
							gateway);
					flowList.add(f0);
				}
				if (stru.get(j).getSe() != null) {
					for (int n = 0; n < flowList.size(); n++) {
						if (flowList.get(n).getSrcNode().getId()
								.equals(stru.get(j).getSe().getId())) {
							flowList.get(n).setSrcNode(gateway);
						}
					}
					String fuuid = "bpmn-" + UUID.randomUUID().toString();
					Flow f0 = new Flow("", fuuid, stru.get(j).getSe(), gateway);
					flowList.add(f0);
				}

			}
		}
		Process post_p = new Process();
		post_p.setName(pre_p.getName());
		post_p.setId(pre_p.getId());
		post_p.setType(pre_p.getType());
		post_p.setActivityList(actList);
		post_p.setFlowList(flowList);
		post_p.setEndeventList(pre_p.getEndeventList());
		post_p.setStarteventList(pre_p.getStarteventList());
		post_p.setGatewayList(gatewayList);
		post_p.setFlowNodeRefList(flowNodeRefList);
		return post_p;
	}

	public Segment processProcess(Process pre_p, String fileName)
			throws IOException // �γ�segment
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream("E:/work/Clients/CMCC/test/wangwj/read/"
						+ fileName + ".txt"))); // ��ȡsegment activity name
		String str = null;
		List<Activity> actList = new ArrayList<Activity>();
		// List<Activity> inactList = new ArrayList<Activity>();
		// List<Activity> outactList = new ArrayList<Activity>();
		List<Flow> flowList = new ArrayList<Flow>();
		List<Gateway> gatewayList = new ArrayList<Gateway>();
		// List<Gateway> ingatewayList = new ArrayList<Gateway>();
		// List<Gateway> outgatewayList = new ArrayList<Gateway>();
		List<FlowNodeRef> flowNodeRefList = new ArrayList<FlowNodeRef>();
		List<Flow> inNodeList = new ArrayList<Flow>();
		List<Flow> outNodeList = new ArrayList<Flow>();

		// ��txt�����е�activity����ӵ�actList��flowNodeRefList��
		Activity firstActivity = null;
		while ((str = br.readLine()) != null) {// according to activity get
												// activity
			// System.out.println("Read Activity From File: " + str);
			Iterator<Activity> iter_act = pre_p.getActivityList().iterator();
			while (iter_act.hasNext()) {
				Activity act = iter_act.next();
				if (act.getName().equals(str)) {
					actList.add(act);
					FlowNodeRef flownode = new FlowNodeRef();
					flownode.setNode(act);
					flowNodeRefList.add(flownode);

					if (firstActivity == null) {
						firstActivity = act;
						// System.out.println("Get first Activity! " +
						// firstActivity.getId());
					}
				}
			}
		}

		// ��pre_p�Ҳ����κ�txt�����е�activity������null
		if (actList.size() == 0) {
			return null;
		}

		// ��������activity������flow����ӵ�flowList��
		Iterator<Activity> iter_act = actList.iterator(); // iterate
															// activityList to
															// get flow
		while (iter_act.hasNext()) {
			Activity act = iter_act.next();
			Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
			while (iter_flow.hasNext()) {
				Flow f = iter_flow.next();
				if (f.getSrcNode().getId().equals(act.getId())
						|| f.getTargetNode().getId().equals(act.getId())) {
					flowList.add(f);// û�а�����ʼ��gateway ��������gateway����
				}
			}
		}

		// find flow source is activity target is gateway ,add this gateway
		// ,this gateway as flow source to find target is gatway add it
		Iterator<Activity> iter_a = actList.iterator(); // iterate activityList
														// to get flow
		while (iter_a.hasNext()) {
			Activity act = iter_a.next();
			Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
			while (iter_flow.hasNext()) {
				Flow f = iter_flow.next();
				Iterator<Gateway> iter_gateawy = pre_p.getGatewayList()
						.iterator();
				while (iter_gateawy.hasNext()) {
					Gateway g = iter_gateawy.next();
					if (f.getSrcNode().getId().equals(act.getId())
							&& f.getTargetNode().getId().equals(g.getId())) {
						gatewayList.add(g); // source is activity and target is
											// gateway ��add these gateways
						FlowNodeRef flownode_gw = new FlowNodeRef();
						flownode_gw.setNode(g);
						flowNodeRefList.add(flownode_gw);
						// the gateway as new source find all target is gateway
						// System.out.println(f.getSrcNode().getId()+"gateway ��id "+g.getId()+" "+g.getName());
					}
				}
			}
		}
		for (int i = 0; i < gatewayList.size(); i++) {

			// Gateway g = itergateway.next();
			Iterator<Flow> iter_fl = pre_p.getFlowList().iterator();// start
																	// with the
																	// flow
																	// contains
																	// that
																	// source
																	// target is
																	// this
																	// gateway
			while (iter_fl.hasNext()) {
				Flow fl = iter_fl.next();
				Iterator<Gateway> iter_gwy = pre_p.getGatewayList().iterator();
				while (iter_gwy.hasNext()) {
					Gateway gw = iter_gwy.next();
					if (fl.getSrcNode().getId()
							.equals(gatewayList.get(i).getId())
							&& fl.getTargetNode().getId().equals(gw.getId())) {
						gatewayList.add(gw);
						FlowNodeRef flownode_gw0 = new FlowNodeRef();
						flownode_gw0.setNode(gw);
						flowNodeRefList.add(flownode_gw0);
						// System.out.println(fl.getSrcNode().getId()+"gateway ��id "+gw.getId()+" "+gw.getName());
					}

				}

			}
		}

		// find source is gatewayList and target is gatewayList from
		// pre_p.flowlist
		Iterator<Flow> iter_f = pre_p.getFlowList().iterator();
		while (iter_f.hasNext()) {
			Flow f = iter_f.next();
			Iterator<Gateway> iter_gateawy = gatewayList.iterator();
			while (iter_gateawy.hasNext()) {
				Gateway g = iter_gateawy.next();
				if (f.getSrcNode().getId().equals(g.getId())) {
					Iterator<Gateway> iter_gwy = gatewayList.iterator();
					while (iter_gwy.hasNext()) {
						Gateway gw = iter_gwy.next();
						if (f.getTargetNode().getId().equals(gw.getId()))
							flowList.add(f);
					}
				}
			}
		}

		// Iterator<StartEvent> iter_startenent =
		// pre_p.getStarteventList().iterator();
		// while(iter_startenent.hasNext()){
		// StartEvent se = iter_startenent.next();
		// FlowNodeRef flownode_se = new FlowNodeRef();
		// flownode_se.setNode(se);
		// flowNodeRefList.add(flownode_se);
		// }
		//
		// Iterator<EndEvent> iter_endevent =
		// pre_p.getEndeventList().iterator();
		// while(iter_endevent.hasNext()){
		// EndEvent ee = iter_endevent.next();
		// FlowNodeRef flownode_ee = new FlowNodeRef();
		// flownode_ee.setNode(ee);
		// flowNodeRefList.add(flownode_ee);
		// }

		if (pre_p.getStarteventList().size() > 0) {
			StartEvent start = pre_p.getStarteventList().get(0);
			FlowNodeRef flownode_se = new FlowNodeRef();
			flownode_se.setNode(start);
			flowNodeRefList.add(flownode_se);

			boolean needToAddStartFlow = true;
			for (Flow f : flowList) {
				if (f.getSrcNode().getId().equals(start.getId())) {
					needToAddStartFlow = false;
					break;
				}
			}
			if (needToAddStartFlow) {
				// System.out.println("Added Start Flow: " + start.getId() +
				// " -> " + firstActivity.getId());
				Flow ff = new Flow("", "bpmn-" + UUID.randomUUID(), start,
						firstActivity);
				flowList.add(ff);
			}
		}

		boolean needToAddEndEvent = false;
		if (pre_p.getEndeventList().size() > 0) {
			EndEvent end = pre_p.getEndeventList().get(0);
			for (Flow f : flowList) {
				if (f.getTargetNode().getId().equals(end.getId())) {
					FlowNodeRef flownode_ee = new FlowNodeRef();
					flownode_ee.setNode(end);
					flowNodeRefList.add(flownode_ee);
					needToAddEndEvent = true;
					break;
				}
			}
		}

		// find source is gateway and target is not FlowNodeRefID
		Iterator<Flow> iter_fl = pre_p.getFlowList().iterator();
		while (iter_fl.hasNext()) {
			Flow f = iter_fl.next();
			Boolean tag = true;
			Iterator<Gateway> iter_gateawy = gatewayList.iterator();
			while (iter_gateawy.hasNext()) {
				Gateway g = iter_gateawy.next();
				if (f.getSrcNode().getId().equals(g.getId())) {
					Iterator<FlowNodeRef> ifnode = flowNodeRefList.iterator(); // iterate
																				// activityList
																				// to
																				// get
																				// flow
					while (ifnode.hasNext()) {
						FlowNodeRef fnode = ifnode.next();
						if (f.getTargetNode().getId()
								.equals(fnode.getNode().getId())) {
							tag = false;
						}
					}
					if (tag) {
						flowList.add(f);
					}

				}
			}
		}

		// find source and target are both in flowNoderef
		List<Flow> flowlist = new ArrayList<Flow>();
		Iterator<Flow> iter_flow = flowList.iterator();
		while (iter_flow.hasNext()) {
			Flow f = iter_flow.next();
			Iterator<FlowNodeRef> iter_fnode = flowNodeRefList.iterator();
			while (iter_fnode.hasNext()) {
				FlowNodeRef fnode = iter_fnode.next();
				if (f.getSrcNode().getId().equals(fnode.getNode().getId())) {
					Iterator<FlowNodeRef> iter_fnoderef = flowNodeRefList
							.iterator();
					while (iter_fnoderef.hasNext()) {
						FlowNodeRef fnoderef = iter_fnoderef.next();
						if (f.getTargetNode().getId()
								.equals(fnoderef.getNode().getId())) {
							if (!flowlist.contains(f)) {// ��ֹ�ظ����
								flowlist.add(f);
							}
						}
					}
				}
			}
		}

		Iterator<Flow> iterflow = flowList.iterator();
		while (iterflow.hasNext()) {
			Flow f = iterflow.next();
			Boolean start_tag = true;
			Boolean end_tag = true;
			Iterator<FlowNodeRef> iter_fnode = flowNodeRefList.iterator();
			while (iter_fnode.hasNext()) {
				FlowNodeRef fnode = iter_fnode.next();
				if (f.getSrcNode().getId().equals(fnode.getNode().getId())) {
					start_tag = false;
				}
				if (f.getTargetNode().getId().equals(fnode.getNode().getId())) {
					end_tag = false;
				}
			}
			boolean isAdded = false;
			if (start_tag) {// source is not flowNoderef
				Iterator<Activity> iter_Act = pre_p.getActivityList()
						.iterator(); // iterate activityList to get flow
				while (iter_Act.hasNext()) {
					Activity act = iter_Act.next();
					if (f.getSrcNode().getId().equals(act.getId())) {
						// inactList.add(act);
						f.getSrcNode().setProcessName(pre_p.getName());
						inNodeList.add(f);
						isAdded = true;
						break;
					}
				}
				if (!isAdded) {
					Iterator<Gateway> iter_gateway = pre_p.getGatewayList()
							.iterator();
					while (iter_gateway.hasNext()) {
						Gateway g = iter_gateway.next();
						if (f.getSrcNode().getId().equals(g.getId())) {
							// ingatewayList.add(g);
							f.getSrcNode().setProcessName(pre_p.getName());
							inNodeList.add(f);
							break;
						}
					}
				}
				// Flow ff = new
				// Flow(f.getName(),f.getId(),f.getSrcNode().getId(),f.getTargetNode().getId());
				// ff.setSrcActivityId(pre_p.getStarteventList().get(0).getId());
				// flowlist.add(ff);
			}
			if (end_tag) {// target is not flowNoderef
				Iterator<Gateway> iter_gateway = pre_p.getGatewayList()
						.iterator();
				while (iter_gateway.hasNext()) {
					Gateway g = iter_gateway.next();
					if (f.getTargetNode().getId().equals(g.getId())) {
						// outgatewayList.add(g);
						f.getTargetNode().setProcessName(pre_p.getName());
						outNodeList.add(f);
						isAdded = true;
						break;
					}
				}
				if (!isAdded) {
					Iterator<Activity> iter_Act = pre_p.getActivityList()
							.iterator(); // iterate activityList to get flow
					while (iter_Act.hasNext()) {
						Activity act = iter_Act.next();
						if (f.getTargetNode().getId().equals(act.getId())) {
							// outactList.add(act);
							f.getTargetNode().setProcessName(pre_p.getName());
							outNodeList.add(f);
							break;
						}
					}
				}

				// Flow ff = new
				// Flow(f.getName(),f.getId(),f.getSrcNode().getId(),f.getTargetNode().getId());
				// ff.setTgtActivityId(pre_p.getEndeventList().get(0).getId());
				// flowlist.add(ff);
			}
		}
		Segment s = new Segment(inNodeList, outNodeList);
		// post_p.setSegment(s);
		s.setName(pre_p.getName()); // �γ��µ�po����segement
		s.setId(pre_p.getId());
		s.setType(pre_p.getType());
		s.setActivityList(actList);
		s.setFlowList(flowlist);
		if (needToAddEndEvent) {
			s.setEndeventList(pre_p.getEndeventList());
		} else {
			s.setEndeventList(new ArrayList<EndEvent>());
		}
		s.setStarteventList(pre_p.getStarteventList());
		s.setGatewayList(gatewayList);
		s.setFlowNodeRefList(flowNodeRefList);
		return s;
	}

	public Segment extractSegment(Process pre_p,
			List<String> activitiesOfSegment) throws IOException // �γ�segment
	{
		List<Activity> actList = new ArrayList<Activity>();
		// List<Activity> inactList = new ArrayList<Activity>();
		// List<Activity> outactList = new ArrayList<Activity>();
		List<Flow> flowList = new ArrayList<Flow>();
		List<Gateway> gatewayList = new ArrayList<Gateway>();
		// List<Gateway> ingatewayList = new ArrayList<Gateway>();
		// List<Gateway> outgatewayList = new ArrayList<Gateway>();
		List<FlowNodeRef> flowNodeRefList = new ArrayList<FlowNodeRef>();
		List<Flow> inNodeList = new ArrayList<Flow>();
		List<Flow> outNodeList = new ArrayList<Flow>();

		// ��txt�����е�activity����ӵ�actList��flowNodeRefList��
		Activity firstActivity = null;
		for (String str : activitiesOfSegment) {// according to activity get
												// activity
			// System.out.println("Read Activity From File: " + str);
			Iterator<Activity> iter_act = pre_p.getActivityList().iterator();
			while (iter_act.hasNext()) {
				Activity act = iter_act.next();
				if (act.getName().equals(str)) {
					actList.add(act);
					FlowNodeRef flownode = new FlowNodeRef();
					flownode.setNode(act);
					flowNodeRefList.add(flownode);

					if (firstActivity == null) {
						firstActivity = act;
						// System.out.println("Get first Activity! " +
						// firstActivity.getId());
					}
				}
			}
		}

		// ��pre_p�Ҳ����κ�txt�����е�activity������null
		if (actList.size() == 0) {
			return null;
		}

		// ��������activity������flow����ӵ�flowList��
		Iterator<Activity> iter_act = actList.iterator(); // iterate
															// activityList to
															// get flow
		while (iter_act.hasNext()) {
			Activity act = iter_act.next();
			Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
			while (iter_flow.hasNext()) {
				Flow f = iter_flow.next();
				if (f.getSrcNode().getId().equals(act.getId())
						|| f.getTargetNode().getId().equals(act.getId())) {
					flowList.add(f);// û�а�����ʼ��gateway ��������gateway����
				}
			}
		}

		// find flow source is activity target is gateway ,add this gateway
		// ,this gateway as flow source to find target is gatway add it
		Iterator<Activity> iter_a = actList.iterator(); // iterate activityList
														// to get flow
		while (iter_a.hasNext()) {
			Activity act = iter_a.next();
			Iterator<Flow> iter_flow = pre_p.getFlowList().iterator();
			while (iter_flow.hasNext()) {
				Flow f = iter_flow.next();
				Iterator<Gateway> iter_gateawy = pre_p.getGatewayList()
						.iterator();
				while (iter_gateawy.hasNext()) {
					Gateway g = iter_gateawy.next();
					if (f.getSrcNode().getId().equals(act.getId())
							&& f.getTargetNode().getId().equals(g.getId())) {
						gatewayList.add(g); // source is activity and target is
											// gateway ��add these gateways
						FlowNodeRef flownode_gw = new FlowNodeRef();
						flownode_gw.setNode(g);
						flowNodeRefList.add(flownode_gw);
						// the gateway as new source find all target is gateway
						// System.out.println(f.getSrcNode().getId()+"gateway ��id "+g.getId()+" "+g.getName());
					}
				}
			}
		}
		for (int i = 0; i < gatewayList.size(); i++) {

			// Gateway g = itergateway.next();
			Iterator<Flow> iter_fl = pre_p.getFlowList().iterator();// start
																	// with the
																	// flow
																	// contains
																	// that
																	// source
																	// target is
																	// this
																	// gateway
			while (iter_fl.hasNext()) {
				Flow fl = iter_fl.next();
				Iterator<Gateway> iter_gwy = pre_p.getGatewayList().iterator();
				while (iter_gwy.hasNext()) {
					Gateway gw = iter_gwy.next();
					if (fl.getSrcNode().getId()
							.equals(gatewayList.get(i).getId())
							&& fl.getTargetNode().getId().equals(gw.getId())) {
						gatewayList.add(gw);
						FlowNodeRef flownode_gw0 = new FlowNodeRef();
						flownode_gw0.setNode(gw);
						flowNodeRefList.add(flownode_gw0);
						// System.out.println(fl.getSrcNode().getId()+"gateway ��id "+gw.getId()+" "+gw.getName());
					}

				}

			}
		}

		// find source is gatewayList and target is gatewayList from
		// pre_p.flowlist
		Iterator<Flow> iter_f = pre_p.getFlowList().iterator();
		while (iter_f.hasNext()) {
			Flow f = iter_f.next();
			Iterator<Gateway> iter_gateawy = gatewayList.iterator();
			while (iter_gateawy.hasNext()) {
				Gateway g = iter_gateawy.next();
				if (f.getSrcNode().getId().equals(g.getId())) {
					Iterator<Gateway> iter_gwy = gatewayList.iterator();
					while (iter_gwy.hasNext()) {
						Gateway gw = iter_gwy.next();
						if (f.getTargetNode().getId().equals(gw.getId()))
							flowList.add(f);
					}
				}
			}
		}

		// Iterator<StartEvent> iter_startenent =
		// pre_p.getStarteventList().iterator();
		// while(iter_startenent.hasNext()){
		// StartEvent se = iter_startenent.next();
		// FlowNodeRef flownode_se = new FlowNodeRef();
		// flownode_se.setNode(se);
		// flowNodeRefList.add(flownode_se);
		// }
		//
		// Iterator<EndEvent> iter_endevent =
		// pre_p.getEndeventList().iterator();
		// while(iter_endevent.hasNext()){
		// EndEvent ee = iter_endevent.next();
		// FlowNodeRef flownode_ee = new FlowNodeRef();
		// flownode_ee.setNode(ee);
		// flowNodeRefList.add(flownode_ee);
		// }

		if (pre_p.getStarteventList().size() > 0) {
			StartEvent start = pre_p.getStarteventList().get(0);
			FlowNodeRef flownode_se = new FlowNodeRef();
			flownode_se.setNode(start);
			flowNodeRefList.add(flownode_se);

			boolean needToAddStartFlow = true;
			for (Flow f : flowList) {
				if (f.getSrcNode().getId().equals(start.getId())) {
					needToAddStartFlow = false;
					break;
				}
			}
			if (needToAddStartFlow) {
				// System.out.println("Added Start Flow: " + start.getId() +
				// " -> " + firstActivity.getId());
				Flow ff = new Flow("", "bpmn-" + UUID.randomUUID(), start,
						firstActivity);
				flowList.add(ff);
			}
		}

		boolean needToAddEndEvent = false;
		if (pre_p.getEndeventList().size() > 0) {
			EndEvent end = pre_p.getEndeventList().get(0);
			for (Flow f : flowList) {
				if (f.getTargetNode().getId().equals(end.getId())) {
					FlowNodeRef flownode_ee = new FlowNodeRef();
					flownode_ee.setNode(end);
					flowNodeRefList.add(flownode_ee);
					needToAddEndEvent = true;
					break;
				}
			}
		}

		// find source is gateway and target is not FlowNodeRefID
		Iterator<Flow> iter_fl = pre_p.getFlowList().iterator();
		while (iter_fl.hasNext()) {
			Flow f = iter_fl.next();
			Boolean tag = true;
			Iterator<Gateway> iter_gateawy = gatewayList.iterator();
			while (iter_gateawy.hasNext()) {
				Gateway g = iter_gateawy.next();
				if (f.getSrcNode().getId().equals(g.getId())) {
					Iterator<FlowNodeRef> ifnode = flowNodeRefList.iterator(); // iterate
																				// activityList
																				// to
																				// get
																				// flow
					while (ifnode.hasNext()) {
						FlowNodeRef fnode = ifnode.next();
						if (f.getTargetNode().getId()
								.equals(fnode.getNode().getId())) {
							tag = false;
						}
					}
					if (tag) {
						flowList.add(f);
					}

				}
			}
		}

		// find source and target are both in flowNoderef
		List<Flow> flowlist = new ArrayList<Flow>();
		Iterator<Flow> iter_flow = flowList.iterator();
		while (iter_flow.hasNext()) {
			Flow f = iter_flow.next();
			Iterator<FlowNodeRef> iter_fnode = flowNodeRefList.iterator();
			while (iter_fnode.hasNext()) {
				FlowNodeRef fnode = iter_fnode.next();
				if (f.getSrcNode().getId().equals(fnode.getNode().getId())) {
					Iterator<FlowNodeRef> iter_fnoderef = flowNodeRefList
							.iterator();
					while (iter_fnoderef.hasNext()) {
						FlowNodeRef fnoderef = iter_fnoderef.next();
						if (f.getTargetNode().getId()
								.equals(fnoderef.getNode().getId())) {
							if (!flowlist.contains(f)) {// ��ֹ�ظ����
								flowlist.add(f);
							}
						}
					}
				}
			}
		}

		Iterator<Flow> iterflow = flowList.iterator();
		while (iterflow.hasNext()) {
			Flow f = iterflow.next();
			Boolean start_tag = true;
			Boolean end_tag = true;
			Iterator<FlowNodeRef> iter_fnode = flowNodeRefList.iterator();
			while (iter_fnode.hasNext()) {
				FlowNodeRef fnode = iter_fnode.next();
				if (f.getSrcNode().getId().equals(fnode.getNode().getId())) {
					start_tag = false;
				}
				if (f.getTargetNode().getId().equals(fnode.getNode().getId())) {
					end_tag = false;
				}
			}
			boolean isAdded = false;
			if (start_tag) {// source is not flowNoderef
				Iterator<Activity> iter_Act = pre_p.getActivityList()
						.iterator(); // iterate activityList to get flow
				while (iter_Act.hasNext()) {
					Activity act = iter_Act.next();
					if (f.getSrcNode().getId().equals(act.getId())) {
						// inactList.add(act);
						f.getSrcNode().setProcessName(pre_p.getName());
						inNodeList.add(f);
						isAdded = true;
						break;
					}
				}
				if (!isAdded) {
					Iterator<Gateway> iter_gateway = pre_p.getGatewayList()
							.iterator();
					while (iter_gateway.hasNext()) {
						Gateway g = iter_gateway.next();
						if (f.getSrcNode().getId().equals(g.getId())) {
							// ingatewayList.add(g);
							f.getSrcNode().setProcessName(pre_p.getName());
							inNodeList.add(f);
							break;
						}
					}
				}
				// Flow ff = new
				// Flow(f.getName(),f.getId(),f.getSrcNode().getId(),f.getTargetNode().getId());
				// ff.setSrcActivityId(pre_p.getStarteventList().get(0).getId());
				// flowlist.add(ff);
			}
			if (end_tag) {// target is not flowNoderef
				Iterator<Gateway> iter_gateway = pre_p.getGatewayList()
						.iterator();
				while (iter_gateway.hasNext()) {
					Gateway g = iter_gateway.next();
					if (f.getTargetNode().getId().equals(g.getId())) {
						// outgatewayList.add(g);
						f.getTargetNode().setProcessName(pre_p.getName());
						outNodeList.add(f);
						isAdded = true;
						break;
					}
				}
				if (!isAdded) {
					Iterator<Activity> iter_Act = pre_p.getActivityList()
							.iterator(); // iterate activityList to get flow
					while (iter_Act.hasNext()) {
						Activity act = iter_Act.next();
						if (f.getTargetNode().getId().equals(act.getId())) {
							// outactList.add(act);
							f.getTargetNode().setProcessName(pre_p.getName());
							outNodeList.add(f);
							break;
						}
					}
				}

				// Flow ff = new
				// Flow(f.getName(),f.getId(),f.getSrcNode().getId(),f.getTargetNode().getId());
				// ff.setTgtActivityId(pre_p.getEndeventList().get(0).getId());
				// flowlist.add(ff);
			}
		}
		Segment s = new Segment(inNodeList, outNodeList);
		// post_p.setSegment(s);
		s.setName(pre_p.getName()); // �γ��µ�po����segement
		s.setId(pre_p.getId());
		s.setType(pre_p.getType());
		s.setActivityList(actList);
		s.setFlowList(flowlist);
		if (needToAddEndEvent) {
			s.setEndeventList(pre_p.getEndeventList());
		} else {
			s.setEndeventList(new ArrayList<EndEvent>());
		}
		s.setStarteventList(pre_p.getStarteventList());
		s.setGatewayList(gatewayList);
		s.setFlowNodeRefList(flowNodeRefList);
		return s;
	}

	// �γ� .bpmn
	// inFileName δ�����.bpmn
	// outFileName segment .bpmn
	public void exportProcess(String inFileName, String outFileName, Process p)
			throws FileNotFoundException, ParserConfigurationException,
			SAXException, IOException, TransformerException {

		if (p == null) {
			return;
		}

		Element srcElement = readFile(inFileName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		Element desRootNode = (Element) doc.importNode(srcElement, false);

		// import
		NodeList ImportList = srcElement.getElementsByTagName("import");
		for (int i = 0; i < ImportList.getLength(); i++) {
			desRootNode.appendChild((Element) doc.importNode(
					ImportList.item(i), false));
		}

		// process
		Element ProcessNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("process").item(0), false);
		// ExtensionElementsNode and laneSet
		Element ExtensionElementsNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("extensionElements").item(0), false);
		Element BpmAttributesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:bpmAttributes").item(0), false);
		Element MilestonesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestones").item(0), false);
		Element MilestoneNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestone").item(0), false);
		if (p.getFlowNodeRefList() != null && p.getFlowNodeRefList().size() > 0) {
			for (FlowNodeRef flowNodeRef : p.getFlowNodeRefList()) {
				Element tempFlowNodeRefElement = doc
						.createElement("flowNodeRef");
				tempFlowNodeRefElement.setTextContent(flowNodeRef.getNode()
						.getId());
				MilestoneNode.appendChild(tempFlowNodeRefElement);
			}
		}
		MilestonesNode.appendChild(MilestoneNode);
		BpmAttributesNode.appendChild(MilestonesNode);
		ExtensionElementsNode.appendChild(BpmAttributesNode);
		ProcessNode.appendChild(ExtensionElementsNode);
		ProcessNode.appendChild((Element) doc.importNode(srcElement
				.getElementsByTagName("laneSet").item(0), false));

		// sequenceFlow
		if (p.getFlowList() != null && p.getFlowList().size() > 0) {
			for (Flow f : p.getFlowList()) {
				Element tempFlowElement = doc.createElement("sequenceFlow");
				tempFlowElement.setAttribute("targetRef", f.getTargetNode()
						.getId());
				tempFlowElement.setAttribute("sourceRef", f.getSrcNode()
						.getId());
				if (f.getName() != "")
					tempFlowElement.setAttribute("name", f.getName());
				tempFlowElement.setAttribute("id", f.getId());
				ProcessNode.appendChild(tempFlowElement);
			}
		}
		// startEvent
		if (p.getStarteventList() != null && p.getStarteventList().size() > 0) {
			for (StartEvent se : p.getStarteventList()) {
				Element tempStartEventElement = doc.createElement("startEvent");
				tempStartEventElement.setAttribute("name", se.getName());
				tempStartEventElement.setAttribute("id", se.getId());
				ProcessNode.appendChild(tempStartEventElement);
			}
		}// task(Activity)
		if (p.getActivityList() != null && p.getActivityList().size() > 0) {
			for (Activity activity : p.getActivityList()) {
				Element tempActivityElement = null;
				// Modify by Wangwj
				// if(activity.getName().equals("Related department / Department manager / countersign"))
				if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_CALLACTIVITY)) {
					tempActivityElement = doc.createElement("callActivity");
					// Need change here !!!!
					tempActivityElement
							.setAttribute(
									"xmlns:bwl1",
									"http://www.ibm.com/WebSphere/bpm/BlueworksLive/10000f83e62451d-20000a13e62c91d");
					tempActivityElement.setAttribute("calledElement",
							activity.getCalledElement());
				} else if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_TASK)) {
					tempActivityElement = doc.createElement("task");
				}
				if (tempActivityElement != null) {
					tempActivityElement.setAttribute("name",
							activity.getDisplayName());
					tempActivityElement.setAttribute("id", activity.getId());

					// add documentation to activity
					Element documentation = doc.createElement("documentation");
					documentation.setAttribute("textFormat", "text/html");
					StringBuilder sb = new StringBuilder();

					// handle inNodeList and outNodeList for BPMN
					if (p instanceof Segment) {
						Segment tempSeg = (Segment) p;
						boolean haveInOrOutNode = false;
						int flowNum = 0;
						StringBuilder sbFlow = new StringBuilder();
						for (Flow f : tempSeg.getInNodeList()) {
							if (f.getTargetNode().getId()
									.equals(activity.getId())) {
								flowNum++;
								ProcessNode node = f.getSrcNode();
								sbFlow.append("<br>" + flowNum + ". From ");
								if (node instanceof Activity) {
									sbFlow.append("Activity ");
								} else {
									sbFlow.append("Gateway ");
								}
								sbFlow.append("\"" + node.getName() + "\"");
								if (node.getProcessName() != null
										&& !"".equals(node.getProcessName())) {
									sbFlow.append(" of \""
											+ node.getProcessName() + "\"");
								}
							}
						}
						if (flowNum > 0) {
							haveInOrOutNode = true;
							sb.append("<br><b>Incoming Flow:</b>");
							sb.append(sbFlow);
						}
						flowNum = 0;
						sbFlow = new StringBuilder();
						for (Flow f : tempSeg.getOutNodeList()) {
							if (f.getSrcNode().getId().equals(activity.getId())) {
								flowNum++;
								ProcessNode node = f.getTargetNode();
								sbFlow.append("<br>" + flowNum + ". To ");
								if (node instanceof Activity) {
									sbFlow.append("Activity ");
								} else {
									sbFlow.append("Gateway ");
								}
								sbFlow.append("\"" + node.getName() + "\"");
								if (node.getProcessName() != null
										&& !"".equals(node.getProcessName())) {
									sbFlow.append(" of \""
											+ node.getProcessName() + "\"");
								}
							}
						}
						if (flowNum > 0) {
							haveInOrOutNode = true;
							sb.append("<br><b>Outgoing Flow:</b>");
							sb.append(sbFlow);
						}

						// set activity color
						if (haveInOrOutNode) {
							Element extensionElements = doc
									.createElement("extensionElements");
							Element bpmAttributes = doc
									.createElement("ns2:bpmAttributes");
							Element properties = doc
									.createElement("ns2:properties");
							Element property = doc
									.createElement("ns2:property");
							property.setAttribute("id",
									"bpmn-" + UUID.randomUUID());
							Element propertyName = doc
									.createElement("ns2:propertyName");
							propertyName.setTextContent("color");
							Element stringliteralValue = doc
									.createElement("ns2:stringliteralValue");
							Element value = doc.createElement("ns2:value");
							value.setTextContent("red");
							stringliteralValue.appendChild(value);
							property.appendChild(propertyName);
							property.appendChild(stringliteralValue);
							properties.appendChild(property);
							bpmAttributes.appendChild(properties);
							extensionElements.appendChild(bpmAttributes);
							tempActivityElement.appendChild(extensionElements);
						}
					}

					documentation.setTextContent(sb.toString());
					tempActivityElement.appendChild(documentation);

					// add activity to process
					ProcessNode.appendChild(tempActivityElement);
				}
			}
		}
		// exclusiveGateway
		if (p.getGatewayList() != null && p.getGatewayList().size() > 0) {
			for (Gateway g : p.getGatewayList()) {
				Element tempGatewayElement = doc.createElement(g.getType()
						.name());
				tempGatewayElement.setAttribute("gatewayDirection",
						g.getGatewayDirection());
				tempGatewayElement.setAttribute("name", g.getName());
				tempGatewayElement.setAttribute("id", g.getId());

				// add documentation to gateway
				// handle inNodeList and outNodeList for BPMN
				if (p instanceof Segment) {
					Segment tempSeg = (Segment) p;
					StringBuilder sb = new StringBuilder();
					boolean haveInOrOutNode = false;
					int flowNum = 0;
					StringBuilder sbFlow = new StringBuilder();
					for (Flow f : tempSeg.getInNodeList()) {
						if (f.getTargetNode().getId().equals(g.getId())) {
							flowNum++;
							ProcessNode node = f.getSrcNode();
							sbFlow.append("<br>" + flowNum + ". From ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Incoming Flow:</b>");
						sb.append(sbFlow);
					}
					flowNum = 0;
					sbFlow = new StringBuilder();
					for (Flow f : tempSeg.getOutNodeList()) {
						if (f.getSrcNode().getId().equals(g.getId())) {
							flowNum++;
							ProcessNode node = f.getTargetNode();
							sbFlow.append("<br>" + flowNum + ". To ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Outgoing Flow:</b>");
						sb.append(sbFlow);
					}

					if (haveInOrOutNode) {
						Element documentation = doc
								.createElement("documentation");
						documentation.setAttribute("textFormat", "text/html");
						documentation.setTextContent(sb.toString());
						tempGatewayElement.appendChild(documentation);
					}
				}
				ProcessNode.appendChild(tempGatewayElement);
			}
		}
		// endEvent
		if (p.getEndeventList() != null && p.getEndeventList().size() > 0) {
			for (EndEvent ee : p.getEndeventList()) {
				Element tempEndEventElement = doc.createElement("endEvent");
				tempEndEventElement.setAttribute("name", ee.getName());
				tempEndEventElement.setAttribute("id", ee.getId());
				ProcessNode.appendChild(tempEndEventElement);
			}
		}
		desRootNode.appendChild(ProcessNode);
		doc.appendChild(desRootNode);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new java.io.File(outFileName));
		result.setSystemId(result.getSystemId().replaceAll("%20", " "));
		transformer.transform(source, result);
	}

	// export fragment to bpmn and owl file
	public void exportFragment(String inFileName, String outFileName,
			String owlFile, Segment p) throws FileNotFoundException,
			ParserConfigurationException, SAXException, IOException,
			TransformerException {

		if (p == null) {
			return;
		}

		Element srcElement = readFile(inFileName);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		Element desRootNode = (Element) doc.importNode(srcElement, false);

		// import
		NodeList ImportList = srcElement.getElementsByTagName("import");
		for (int i = 0; i < ImportList.getLength(); i++) {
			desRootNode.appendChild((Element) doc.importNode(
					ImportList.item(i), false));
		}

		// process
		Element ProcessNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("process").item(0), false);
		// ExtensionElementsNode and laneSet
		Element ExtensionElementsNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("extensionElements").item(0), false);
		Element BpmAttributesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:bpmAttributes").item(0), false);
		Element MilestonesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestones").item(0), false);
		Element MilestoneNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestone").item(0), false);
		if (p.getFlowNodeRefList() != null && p.getFlowNodeRefList().size() > 0) {
			for (FlowNodeRef flowNodeRef : p.getFlowNodeRefList()) {
				Element tempFlowNodeRefElement = doc
						.createElement("flowNodeRef");
				tempFlowNodeRefElement.setTextContent(flowNodeRef.getNode()
						.getId());
				MilestoneNode.appendChild(tempFlowNodeRefElement);
			}
		}
		MilestonesNode.appendChild(MilestoneNode);
		BpmAttributesNode.appendChild(MilestonesNode);
		ExtensionElementsNode.appendChild(BpmAttributesNode);
		ProcessNode.appendChild(ExtensionElementsNode);
		ProcessNode.appendChild((Element) doc.importNode(srcElement
				.getElementsByTagName("laneSet").item(0), false));

		// sequenceFlow
		if (p.getFlowList() != null && p.getFlowList().size() > 0) {
			for (Flow f : p.getFlowList()) {
				Element tempFlowElement = doc.createElement("sequenceFlow");
				tempFlowElement.setAttribute("targetRef", f.getTargetNode()
						.getId());
				tempFlowElement.setAttribute("sourceRef", f.getSrcNode()
						.getId());
				if (f.getName() != "")
					tempFlowElement.setAttribute("name", f.getName());
				tempFlowElement.setAttribute("id", f.getId());
				ProcessNode.appendChild(tempFlowElement);
			}
		}
		// startEvent
		if (p.getStarteventList() != null && p.getStarteventList().size() > 0) {
			for (StartEvent se : p.getStarteventList()) {
				Element tempStartEventElement = doc.createElement("startEvent");
				tempStartEventElement.setAttribute("name", se.getName());
				tempStartEventElement.setAttribute("id", se.getId());
				ProcessNode.appendChild(tempStartEventElement);
			}
		}
		// OWLModel, generate the owl file
		OWLModel model = new OWLModel();
		ArrayList<ActivityEntity> entityList = new ArrayList<ActivityEntity>();
		// task(Activity)
		if (p.getActivityList() != null && p.getActivityList().size() > 0) {
			for (Activity activity : p.getActivityList()) {
				Element tempActivityElement = null;
				// Modify by Wangwj
				// if(activity.getName().equals("Related department / Department manager / countersign"))
				if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_CALLACTIVITY)) {
					tempActivityElement = doc.createElement("callActivity");
					// Need change here !!!!
					tempActivityElement
							.setAttribute(
									"xmlns:bwl1",
									"http://www.ibm.com/WebSphere/bpm/BlueworksLive/10000f83e62451d-20000a13e62c91d");
					tempActivityElement.setAttribute("calledElement",
							activity.getCalledElement());
				} else if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_TASK)) {
					tempActivityElement = doc.createElement("task");
				}
				if (tempActivityElement != null) {
					tempActivityElement.setAttribute("name",
							activity.getDisplayName());
					tempActivityElement.setAttribute("id", activity.getId());

					// add documentation to activity
					Element documentation = doc.createElement("documentation");
					documentation.setAttribute("textFormat", "text/html");
					StringBuilder sb = new StringBuilder();

					// handle inNodeList and outNodeList for BPMN
					ActivityEntity entity = model.new ActivityEntity();
					entity.setName(activity.getName().replace(" ", "_"));
					ArrayList<String> incoming = new ArrayList<String>();
					ArrayList<String> outgoing = new ArrayList<String>();
					boolean haveInOrOutNode = false;
					int flowNum = 0;
					StringBuilder sbFlow = new StringBuilder();
					for (Flow f : p.getInNodeList()) {
						if (f.getTargetNode().getId().equals(activity.getId())) {
							flowNum++;
							ProcessNode node = f.getSrcNode();
							sbFlow.append("<br>" + flowNum + ". From ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}

							incoming.add(node.getName().replace(" ", "_") + "-"
									+ node.getProcessName().replace(" ", "_"));
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Incoming Flow:</b>");
						sb.append(sbFlow);

						entity.setIncoming(incoming);
					}
					flowNum = 0;
					sbFlow = new StringBuilder();
					for (Flow f : p.getOutNodeList()) {
						if (f.getSrcNode().getId().equals(activity.getId())) {
							flowNum++;
							ProcessNode node = f.getTargetNode();
							sbFlow.append("<br>" + flowNum + ". To ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}

							outgoing.add(node.getName().replace(" ", "_") + "-"
									+ node.getProcessName().replace(" ", "_"));
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Outgoing Flow:</b>");
						sb.append(sbFlow);

						entity.setOutcoming(outgoing);
					}

					// set activity color
					if (haveInOrOutNode) {
						Element extensionElements = doc
								.createElement("extensionElements");
						Element bpmAttributes = doc
								.createElement("ns2:bpmAttributes");
						Element properties = doc
								.createElement("ns2:properties");
						Element property = doc.createElement("ns2:property");
						property.setAttribute("id", "bpmn-" + UUID.randomUUID());
						Element propertyName = doc
								.createElement("ns2:propertyName");
						propertyName.setTextContent("color");
						Element stringliteralValue = doc
								.createElement("ns2:stringliteralValue");
						Element value = doc.createElement("ns2:value");
						value.setTextContent("red");
						stringliteralValue.appendChild(value);
						property.appendChild(propertyName);
						property.appendChild(stringliteralValue);
						properties.appendChild(property);
						bpmAttributes.appendChild(properties);
						extensionElements.appendChild(bpmAttributes);
						tempActivityElement.appendChild(extensionElements);

						entityList.add(entity);
					}

					documentation.setTextContent(sb.toString());
					tempActivityElement.appendChild(documentation);

					// add activity to process
					ProcessNode.appendChild(tempActivityElement);
				}
			}
		}
		// exclusiveGateway
		if (p.getGatewayList() != null && p.getGatewayList().size() > 0) {
			for (Gateway g : p.getGatewayList()) {
				Element tempGatewayElement = doc.createElement(g.getType()
						.name());
				tempGatewayElement.setAttribute("gatewayDirection",
						g.getGatewayDirection());
				tempGatewayElement.setAttribute("name", g.getName());
				tempGatewayElement.setAttribute("id", g.getId());

				// add documentation to gateway
				// handle inNodeList and outNodeList for BPMN
				ActivityEntity entity = model.new ActivityEntity();
				entity.setName(g.getName().replace(" ", "_"));
				ArrayList<String> incoming = new ArrayList<String>();
				ArrayList<String> outgoing = new ArrayList<String>();

				StringBuilder sb = new StringBuilder();
				boolean haveInOrOutNode = false;
				int flowNum = 0;
				StringBuilder sbFlow = new StringBuilder();
				for (Flow f : p.getInNodeList()) {
					if (f.getTargetNode().getId().equals(g.getId())) {
						flowNum++;
						ProcessNode node = f.getSrcNode();
						sbFlow.append("<br>" + flowNum + ". From ");
						if (node instanceof Activity) {
							sbFlow.append("Activity ");
						} else {
							sbFlow.append("Gateway ");
						}
						sbFlow.append("\"" + node.getName() + "\"");
						if (node.getProcessName() != null
								&& !"".equals(node.getProcessName())) {
							sbFlow.append(" of \"" + node.getProcessName()
									+ "\"");
						}

						incoming.add(node.getName().replace(" ", "_") + "-"
								+ node.getProcessName().replace(" ", "_"));
					}
				}
				if (flowNum > 0) {
					haveInOrOutNode = true;
					sb.append("<br><b>Incoming Flow:</b>");
					sb.append(sbFlow);

					entity.setIncoming(incoming);
				}
				flowNum = 0;
				sbFlow = new StringBuilder();
				for (Flow f : p.getOutNodeList()) {
					if (f.getSrcNode().getId().equals(g.getId())) {
						flowNum++;
						ProcessNode node = f.getTargetNode();
						sbFlow.append("<br>" + flowNum + ". To ");
						if (node instanceof Activity) {
							sbFlow.append("Activity ");
						} else {
							sbFlow.append("Gateway ");
						}
						sbFlow.append("\"" + node.getName() + "\"");
						if (node.getProcessName() != null
								&& !"".equals(node.getProcessName())) {
							sbFlow.append(" of \"" + node.getProcessName()
									+ "\"");
						}

						outgoing.add(node.getName().replace(" ", "_") + "-"
								+ node.getProcessName().replace(" ", "_"));
					}
				}
				if (flowNum > 0) {
					haveInOrOutNode = true;
					sb.append("<br><b>Outgoing Flow:</b>");
					sb.append(sbFlow);

					entity.setOutcoming(outgoing);
				}

				if (haveInOrOutNode) {
					Element documentation = doc.createElement("documentation");
					documentation.setAttribute("textFormat", "text/html");
					documentation.setTextContent(sb.toString());
					tempGatewayElement.appendChild(documentation);

					entityList.add(entity);
				}

				ProcessNode.appendChild(tempGatewayElement);
			}
		}
		// endEvent
		if (p.getEndeventList() != null && p.getEndeventList().size() > 0) {
			for (EndEvent ee : p.getEndeventList()) {
				Element tempEndEventElement = doc.createElement("endEvent");
				tempEndEventElement.setAttribute("name", ee.getName());
				tempEndEventElement.setAttribute("id", ee.getId());
				ProcessNode.appendChild(tempEndEventElement);
			}
		}
		desRootNode.appendChild(ProcessNode);
		doc.appendChild(desRootNode);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new java.io.File(outFileName));
		result.setSystemId(result.getSystemId().replaceAll("%20", " "));
		transformer.transform(source, result);
		// generate owl file
		model.generateOWL(entityList, owlFile);
	}

	public Process mergeTwoProcesses(Process process1, Process process2)
			throws Exception {
		List<Process> list = new ArrayList<Process>();
		list.add(process1);
		list.add(process2);
		return this.mergeProcess(list);
	}

	// merge processes/segments
	public Process mergeProcess(List<Process> listProcesses) throws Exception {

		for (Process p : listProcesses) {
			if (p != null && p.getActivityList() != null) {
				for (Activity act : p.getActivityList()) {
					if (act.getAppearProcess().size() == 0) {
						act.setAppearNum(1);
						act.getAppearProcess().add(p.getName());
					}
				}
			}
		}
		Process firstSeg = null;
		while (firstSeg == null && listProcesses.size() > 0) {
			firstSeg = listProcesses.remove(0);
		}
		if (firstSeg == null) {
			throw new Exception("No segment to be merged!");
		}

		for (int i = 0; i < listProcesses.size(); i++) {
			Process seg = listProcesses.get(i);
			if (seg == null) {
				continue;
			}

			this.handledNodes = new ArrayList<ProcessNode>();
			for (StartEvent start : seg.getStarteventList()) {
				this.mergeNode(firstSeg, seg, start);
			}
		}
		return firstSeg;
	}

	// cut off branch by threshold
	public void cutOffBranch(Process process, int intThreshold) {
		if (intThreshold <= 1) {
			System.out
					.println("The threshold should >= 1, please input again.");
			return;
		}
		this.threshold = intThreshold;
		this.cutOffFlowNodeRefList = new ArrayList<FlowNodeRef>();
		this.cutOffActivityList = new ArrayList<Activity>();
		this.cutOffGatewayList = new ArrayList<Gateway>();
		this.cutOffFlowList = new ArrayList<Flow>();
		for (Activity act : process.getActivityList()) {
			this.cutOffActivity(process, act);
		}
		process.getFlowNodeRefList().removeAll(this.cutOffFlowNodeRefList);
		process.getActivityList().removeAll(this.cutOffActivityList);
		process.getGatewayList().removeAll(this.cutOffGatewayList);
		process.getFlowList().removeAll(this.cutOffFlowList);
	}

	private void cutOffActivity(Process process, Activity activity) {
		if (activity.getDisplayAppearNum() < this.threshold) {
			if (!this.activityCanBeCutOff(process, activity)) {
				return;
			}
			boolean needBreak = true;
			for (Flow f : process.getFlowList()) {
				if (f.getSrcNode().getId().equals(activity.getId())) {
					ProcessNode nextNode = f.getTargetNode();
					if (nextNode instanceof Gateway
							|| nextNode instanceof EndEvent) {
						needBreak = false;
						break;
					}
					if (nextNode instanceof Activity
							&& !this.activityCanBeCutOff(process,
									(Activity) nextNode)) {
						needBreak = false;
						break;
					}
				}
			}
			this.removeActivity(process, activity, needBreak);
		}
	}

	private void removeActivity(Process process, Activity activity,
			boolean needBreak) {
		this.removeNode(process, activity);
		if (needBreak) {
			for (Flow f : process.getFlowList()) {
				if (f.getTargetNode().getId().equals(activity.getId())) {
					ProcessNode previousNode = f.getSrcNode();
					// process.getFlowList().remove(f);
					this.cutOffFlowList.add(f);
					if (previousNode instanceof Gateway) {
						int outFlowNumber = this.getOutFlowNum(process,
								previousNode);
						if (outFlowNumber < 2) {
							this.removeGateway(process, (Gateway) previousNode);
						}
					}
					break;
				}
			}
		} else {
			Flow previousFlow = null;
			ProcessNode followingNode = null;
			for (Flow f : process.getFlowList()) {
				if (f.getTargetNode().getId().equals(activity.getId())) {
					ProcessNode previousNode = f.getSrcNode();
					if (previousNode instanceof Activity
							&& this.activityCanBeCutOff(process,
									(Activity) previousNode)) {
						// process.getFlowList().remove(f);
						this.cutOffFlowList.add(f);
					} else {
						previousFlow = f;
					}
				}
				if (f.getSrcNode().getId().equals(activity.getId())) {
					ProcessNode nextNode = f.getTargetNode();
					if (nextNode instanceof Gateway
							|| nextNode instanceof EndEvent) {
						followingNode = nextNode;
						// process.getFlowList().remove(f);
						this.cutOffFlowList.add(f);
					}
					if (nextNode instanceof Activity) {
						if (!this.activityCanBeCutOff(process,
								(Activity) nextNode)) {
							followingNode = nextNode;
						}
						// process.getFlowList().remove(f);
						this.cutOffFlowList.add(f);
					}
				}
			}
			if (previousFlow != null && followingNode != null) {
				previousFlow.setTargetNode(followingNode);
			}
		}
	}

	// remove node from FlowNodeRefList and ActivityList/GatewayList
	private void removeNode(Process process, ProcessNode node) {
		// remove from FlowNodeRefList
		for (FlowNodeRef ref : process.getFlowNodeRefList()) {
			if (ref.getNode().getId().equals(node.getId())) {
				// process.getFlowNodeRefList().remove(ref);
				this.cutOffFlowNodeRefList.add(ref);
				break;
			}
		}
		if (node instanceof Activity) {
			// remove from ActivityList
			for (Activity a : process.getActivityList()) {
				if (a.getId().equals(node.getId())) {
					// process.getActivityList().remove(a);
					this.cutOffActivityList.add(a);
					break;
				}
			}
		} else if (node instanceof Gateway) {
			// remove from gatewayList
			for (Gateway g : process.getGatewayList()) {
				if (g.getId().equals(node.getId())) {
					// process.getGatewayList().remove(g);
					this.cutOffGatewayList.add(g);
					break;
				}
			}
		}
	}

	private void removeGateway(Process process, Gateway gateway) {
		this.removeNode(process, gateway);
		for (Flow f : process.getFlowList()) {
			if (f.getTargetNode().getId().equals(gateway.getId())) {
				ProcessNode previousNode = f.getSrcNode();
				// process.getFlowList().remove(f);
				this.cutOffFlowList.add(f);
				// outgoing flow
				if (process instanceof Segment) {
					this.cutOffOutgoingFlowList = new ArrayList<Flow>();
					for (Flow outFlow : ((Segment) process).getOutNodeList()) {
						if (outFlow.getSrcNode().getId()
								.equals(gateway.getId())) {
							if (this.isDuplicateOutgoingFlow((Segment) process,
									previousNode, outFlow.getTargetNode())) {
								this.cutOffOutgoingFlowList.add(outFlow);
							} else {
								outFlow.setSrcNode(previousNode);
							}
						}
					}
					((Segment) process).getOutNodeList().removeAll(
							this.cutOffOutgoingFlowList);
				}
				// check the previous gateway
				if (previousNode instanceof Gateway) {
					int outFlowNumber = this.getOutFlowNum(process,
							previousNode);
					if (outFlowNumber < 2) {
						this.removeGateway(process, (Gateway) previousNode);
					}
				}
				break;
			}
		}
	}

	private boolean isDuplicateOutgoingFlow(Segment segment,
			ProcessNode baseNode, ProcessNode targetNode) {
		for (Flow f : segment.getOutNodeList()) {
			if (f.getSrcNode().getId().equals(baseNode.getId())) {
				ProcessNode node = f.getTargetNode();
				if (node.getName().equals(targetNode.getName())
						&& node.getProcessName().equals(
								targetNode.getProcessName())) {
					return true;
				}
			}
		}
		return false;
	}

	private boolean activityCanBeCutOff(Process process, Activity activity) {
		if (process instanceof Segment) {
			for (Flow f : ((Segment) process).getInNodeList()) {
				if (f.getTargetNode().getId().equals(activity.getId())) {
					return false;
				}
			}
			for (Flow f : ((Segment) process).getOutNodeList()) {
				if (f.getSrcNode().getId().equals(activity.getId())) {
					return false;
				}
			}
		}
		int numberOfIncoming = 0;
		for (Flow f : process.getFlowList()) {
			if (f.getTargetNode().getId().equals(activity.getId())) {
				ProcessNode sourceNode = f.getSrcNode();
				if (sourceNode instanceof Activity
						&& this.activityCanBeCutOff(process,
								(Activity) sourceNode)) {
					continue;
				}
				numberOfIncoming++;
			}
		}
		return numberOfIncoming < 2;
	}

	private int getOutFlowNum(Process process, ProcessNode node) {
		int intNum = 0;
		for (Flow f : process.getFlowList()) {
			if (f.getSrcNode().getId().equals(node.getId())) {
				intNum++;
			}
		}
		return intNum;
	}

	private String getProcessNameByFilePath(String filePath) {
		String processName = filePath.substring(filePath.lastIndexOf("/") + 1);
		return processName.replaceAll(".bpmn", "");
	}

	private String getImportLocation(Node item) {
		String location = ((Element) item).getAttribute("location");
		if (location != null && location.length() > 0) {
			location = location.substring(location.lastIndexOf("/") + 1);
		}
		return location;
	}

	private void mergeNode(Process firstSeg, Process seg, ProcessNode node) {

		this.handledNodes.add(node);

		if (node instanceof EndEvent) {
			// System.out.println("Find end event id: " + node.getId());
		} else {
			final String nodeId = node.getId();
			final String nodeName = node.getName();
			String targetId;
			ProcessNode baseNode = null;
			boolean nodeExistInBaseSeg = false;

			if (node instanceof StartEvent || node instanceof Activity) {
				if (node instanceof StartEvent) { // node is start event
					nodeExistInBaseSeg = true;
					baseNode = firstSeg.getStarteventList().get(0);
				} else { // node is activity
					for (Activity a : firstSeg.getActivityList()) {
						if (a.getName().equals(nodeName)) {
							nodeExistInBaseSeg = true;
							this.mergeActivitySynonym(a, (Activity) node);

							baseNode = a;
							break;
						}
					}
					// find if has synonym activity
					if (!nodeExistInBaseSeg) {
						for (Activity a : firstSeg.getActivityList()) {
							if (this.isSynonymActivity(a, node)) {
								nodeExistInBaseSeg = true;
								this.mergeActivitySynonym(a, (Activity) node);

								baseNode = a;
								break;
							}
						}
					}
				}

				if (nodeExistInBaseSeg) {
					// seg�е�ǰ�ڵ���baseSeg��Ҳ����

					// handle flow who's target is current activity
					if (node instanceof Activity) {
						for (Flow f : firstSeg.getFlowList()) {
							if (f.getTargetNode().getId().equals(node.getId())) {
								f.setTargetNode(baseNode);
							}
						}
					}

					ProcessNode nextNode = this.getNextNode(seg, nodeId);

					if (nextNode instanceof Activity) { // seg����һ�ڵ���Activity
						// found next node in baseSeg
						Activity baseNextNode = this.getActivityByName(
								firstSeg, nextNode.getName());
						if (baseNextNode == null) {
							baseNextNode = this.findSynonymActivity(firstSeg,
									nextNode);
						}
						if (baseNextNode == null) {
							// baseSeg�в�����Activity, add gateway and flow
							this.addFlowOrGateway(firstSeg, baseNode, nextNode);
						} else {
							// baseSeg�к���Activity, find preceding activities
							boolean baseNodeIsPreceding = false;
							List<ProcessNode> preceding = this
									.findPrecedingNodes(firstSeg,
											baseNextNode.getId());
							for (ProcessNode n : preceding) {
								if (n.getId().equals(baseNode.getId())) {
									baseNodeIsPreceding = true;
									break;
								}
							}
							if (!baseNodeIsPreceding) {
								// add gateway and flow
								this.addFlowOrGateway(firstSeg, baseNode,
										baseNextNode);
							}
						}
					} else if (nextNode instanceof Gateway) { // seg����һ�ڵ���Gateway
						// find base next node
						ProcessNode baseNextNode = this.getNextNode(firstSeg,
								baseNode.getId());
						if (baseNextNode instanceof Activity
								|| baseNextNode instanceof EndEvent) {
							// add gateway and flow
							this.addFlowOrGateway(firstSeg, baseNode, nextNode);
						} else if (baseNextNode instanceof Gateway) {
							List<String> listNNextNodeNames = this
									.getNextActivityNames(seg, nextNode.getId());
							List<String> listBaseNNextNodeNames = this
									.getNextActivityNames(firstSeg,
											baseNextNode.getId());
							if (!listBaseNNextNodeNames
									.containsAll(listNNextNodeNames)
									&& !baseNextNode.getName().equals(
											nextNode.getName())) {
								// System.out.println("Add flow or gateway and flow by: "
								// + nodeName);
								this.addFlowOrGateway(firstSeg, baseNode,
										nextNode);
							} else {
								// if
								// (baseNextNode.getName().equals(nextNode.getName()))
								// {
								// handle the next gateway which is not need to
								// add to base seg
								this.handleGatewayNotNeedToAdd(firstSeg, seg,
										nextNode, baseNextNode);
								// }

								// handle inNodeList and outNodeList of
								// nextNode(gateway)
								if (firstSeg instanceof Segment
										&& seg instanceof Segment) {
									for (Flow f : ((Segment) seg)
											.getInNodeList()) {
										if (f.getTargetNode().getId()
												.equals(nextNode.getId())) {
											// if
											// (!this.inNodeIsContained(baseSeg,
											// f.getSrcNode(), baseNextNode)) {
											Flow tempFlow = new Flow(
													f.getName(), f.getId(),
													f.getSrcNode(),
													baseNextNode);
											((Segment) firstSeg)
													.getInNodeList().add(
															tempFlow);
											// }
										}
									}
									for (Flow f : ((Segment) seg)
											.getOutNodeList()) {
										if (f.getSrcNode().getId()
												.equals(nextNode.getId())) {
											// if
											// (!this.outNodeIsContained(baseSeg,
											// baseNextNode, f.getTargetNode()))
											// {
											Flow tempFlow = new Flow(
													f.getName(), f.getId(),
													baseNextNode,
													f.getTargetNode());
											((Segment) firstSeg)
													.getOutNodeList().add(
															tempFlow);
											// }
										}
									}
								}
							}
						}
					} else if (nextNode instanceof EndEvent) { // seg����һ�ڵ���EndEvent
						// find preceding activities of end event
						EndEvent end = null;
						if (firstSeg.getEndeventList().size() > 0) { // baseSeg
																		// has
																		// EndEvent
							end = firstSeg.getEndeventList().get(0);
							boolean baseNodeIsPreceding = false;
							List<ProcessNode> preceding = this
									.findPrecedingNodes(firstSeg, end.getId());
							for (ProcessNode n : preceding) {
								if (n.getId().equals(baseNode.getId())) {
									baseNodeIsPreceding = true;
									break;
								}
							}
							if (!baseNodeIsPreceding) {
								// add gateway and flow
								this.addFlowOrGateway(firstSeg, baseNode, end);
							}
						} else { // baseSeg does not have EndEvent
							end = (EndEvent) nextNode;
							firstSeg.getEndeventList().add(end);
							firstSeg.getFlowNodeRefList().add(
									new FlowNodeRef(end));
							if (this.hasFollowingFlow(firstSeg, baseNode)) {
								// add gateway and flow
								this.addFlowOrGateway(firstSeg, baseNode, end);
							} else {
								Flow tempFlow = new Flow("", "bpmn-"
										+ UUID.randomUUID(), baseNode, end);
								firstSeg.getFlowList().add(tempFlow);
							}
						}
					}

					// handle inNodeList and outNodeList
					if (firstSeg instanceof Segment && seg instanceof Segment) {
						for (Flow f : ((Segment) seg).getInNodeList()) {
							if (f.getTargetNode().getId().equals(node.getId())) {
								// boolean needToAddFlow = true;
								// for (Flow bf : baseSeg.getInNodeList()) {
								// if
								// (bf.getTargetNode().getId().equals(baseNode.getId())
								// &&
								// bf.getSrcNode().getName().equals(f.getSrcNode().getName()))
								// {
								// needToAddFlow = false;
								// break;
								// }
								// }
								// if (!this.inNodeIsContained(baseSeg,
								// f.getSrcNode(), baseNode)) {
								Flow tempFlow = new Flow(f.getName(),
										f.getId(), f.getSrcNode(), baseNode);
								((Segment) firstSeg).getInNodeList().add(
										tempFlow);
								// }
							}
						}
						for (Flow f : ((Segment) seg).getOutNodeList()) {
							if (f.getSrcNode().getId().equals(node.getId())) {
								// boolean needToAddFlow = true;
								// for (Flow bf : baseSeg.getOutNodeList()) {
								// if
								// (bf.getSrcNode().getId().equals(baseNode.getId())
								// &&
								// bf.getTargetNode().getName().equals(f.getTargetNode().getName()))
								// {
								// needToAddFlow = false;
								// break;
								// }
								// }
								// if (!this.outNodeIsContained(baseSeg,
								// baseNode, f.getTargetNode())) {
								Flow tempFlow = new Flow(f.getName(),
										f.getId(), baseNode, f.getTargetNode());
								((Segment) firstSeg).getOutNodeList().add(
										tempFlow);
								// }
							}
						}
					}

				} else {
					// seg�е�ǰ�ڵ���baseSeg�в�����
					// if ()

					// add this activity to baseSeg
					Activity act = (Activity) node;
					// act.setAppearNum(1);
					// List<String> appearProcess = new ArrayList<String>();
					// appearProcess.add(seg.getName());
					// act.setAppearProcess(appearProcess);
					firstSeg.getActivityList().add(act);
					firstSeg.getFlowNodeRefList().add(new FlowNodeRef(act));

					// add flow of this activity to baseSeg
					for (Flow f : seg.getFlowList()) {
						if (f.getSrcNode().getId().equals(act.getId())) {
							ProcessNode nextNode = f.getTargetNode();
							if (nextNode instanceof EndEvent) {
								if (firstSeg.getEndeventList().size() > 0) { // baseSeg
																				// has
																				// EndEvent
									f.setTargetNode(firstSeg.getEndeventList()
											.get(0));
								} else { // baseSeg does not have EndEvent
									EndEvent end = (EndEvent) nextNode;
									firstSeg.getEndeventList().add(end);
									firstSeg.getFlowNodeRefList().add(
											new FlowNodeRef(end));
								}
							} else if (nextNode instanceof Activity) {
								// found next activity in baseSeg
								Activity baseNextNode = this.getActivityByName(
										firstSeg, nextNode.getName());
								if (baseNextNode != null) {
									f.setTargetNode(baseNextNode);
								}
							}
							firstSeg.getFlowList().add(f);
							break;
						}
					}

					// handle inNodeList and outNodeList
					if (firstSeg instanceof Segment && seg instanceof Segment) {
						for (Flow f : ((Segment) seg).getInNodeList()) {
							if (f.getTargetNode().getId().equals(node.getId())) {
								((Segment) firstSeg).getInNodeList().add(f);
							}
						}
						for (Flow f : ((Segment) seg).getOutNodeList()) {
							if (f.getSrcNode().getId().equals(node.getId())) {
								((Segment) firstSeg).getOutNodeList().add(f);
							}
						}
					}
				}
			} else if (node instanceof Gateway) {
				boolean needToAdd = false;// if need to add this gateway to
											// baseSeg
				for (Flow f : firstSeg.getFlowList()) {
					if (f.getTargetNode().getId().equals(node.getId())) {
						needToAdd = true;
						break;
					}
				}
				if (needToAdd) {
					// add this gateway to baseSeg
					firstSeg.getGatewayList().add((Gateway) node);
					firstSeg.getFlowNodeRefList().add(new FlowNodeRef(node));

					// add flows
					for (Flow f : seg.getFlowList()) {
						if (f.getSrcNode().getId().equals(node.getId())) {
							firstSeg.getFlowList().add(f);
						}
					}

					// handle inNodeList and outNodeList
					if (firstSeg instanceof Segment && seg instanceof Segment) {
						for (Flow f : ((Segment) seg).getInNodeList()) {
							if (f.getTargetNode().getId().equals(node.getId())) {
								((Segment) firstSeg).getInNodeList().add(f);
							}
						}
						for (Flow f : ((Segment) seg).getOutNodeList()) {
							if (f.getSrcNode().getId().equals(node.getId())) {
								((Segment) firstSeg).getOutNodeList().add(f);
							}
						}
					}
				}
			}

			// merge next node
			for (Flow flow : seg.getFlowList()) {
				if (flow.getSrcNode().getId().equals(nodeId)) {
					targetId = flow.getTargetNode().getId();
					ProcessNode nextNode = this.findNodeById(seg, targetId);
					if (nextNode == null) {
						continue;
					}
					if (!this.nodeHasBeenHandled(nextNode)) {
						this.mergeNode(firstSeg, seg, nextNode);
					} else {
						// handle flow who's target is current activity
						if (nextNode instanceof Activity) {
							for (Flow f : firstSeg.getFlowList()) {
								if (f.getTargetNode().getId()
										.equals(nextNode.getId())) {
									f.setTargetNode(this.getActivityByName(
											firstSeg, nextNode.getName()));
								}
							}
						}
					}
				}
			}
		}
	}

	private void mergeActivitySynonym(Activity baseAct, Activity act) {
		boolean isMerged = false;
		// merge act
		if (baseAct.getName().equals(act.getName())) {
			baseAct.setAppearNum(baseAct.getAppearNum() + act.getAppearNum());
			baseAct.getAppearProcess().addAll(act.getAppearProcess());
			isMerged = true;
		} else {
			for (ProcessNode baseSynNode : baseAct.getSynonymNode()) {
				if (baseSynNode.getName().equals(act.getName())) {
					Activity baseSynAct = (Activity) baseSynNode;
					baseSynAct.setAppearNum(baseSynAct.getAppearNum()
							+ act.getAppearNum());
					baseSynAct.getAppearProcess()
							.addAll(act.getAppearProcess());
					isMerged = true;
					break;
				}
			}
		}
		if (!isMerged) {
			baseAct.getSynonymNode().add(act);
		}
		// merge synonym of act
		for (ProcessNode synNode : act.getSynonymNode()) {
			Activity synAct = (Activity) synNode;
			isMerged = false;
			if (synAct.getName().equals(baseAct.getName())) {
				baseAct.setAppearNum(baseAct.getAppearNum()
						+ synAct.getAppearNum());
				baseAct.getAppearProcess().addAll(synAct.getAppearProcess());
				continue;
			}
			for (ProcessNode baseSynNode : baseAct.getSynonymNode()) {
				if (synAct.getName().equals(baseSynNode.getName())) {
					Activity baseSynAct = (Activity) baseSynNode;
					baseSynAct.setAppearNum(baseSynAct.getAppearNum()
							+ synAct.getAppearNum());
					baseSynAct.getAppearProcess().addAll(
							synAct.getAppearProcess());
					isMerged = true;
					break;
				}
			}
			if (!isMerged) {
				baseAct.getSynonymNode().add(synNode);
			}
		}
	}

	private void handleGatewayNotNeedToAdd(Process firstSeg, Process seg,
			ProcessNode node, ProcessNode baseNode) {
		for (Flow f : seg.getFlowList()) {
			if (f.getSrcNode().getId().equals(node.getId())) {
				ProcessNode nextNode = f.getTargetNode();
				if (nextNode instanceof Gateway) {
					boolean isGatewayExsit = false;
					ProcessNode baseNextNode = null;
					for (Flow fbase : firstSeg.getFlowList()) {
						if (fbase.getSrcNode().getId().equals(baseNode.getId())) {
							baseNextNode = fbase.getTargetNode();
							if (baseNextNode instanceof Gateway
									&& baseNextNode.getName().equals(
											nextNode.getName())) {
								isGatewayExsit = true;
								break;
							}
						}
					}
					if (isGatewayExsit) {
						this.handleGatewayNotNeedToAdd(firstSeg, seg, nextNode,
								baseNextNode);

						// handle inNodeList and outNodeList of
						// nextNode(gateway)
						if (firstSeg instanceof Segment
								&& seg instanceof Segment) {
							for (Flow flow : ((Segment) seg).getInNodeList()) {
								if (flow.getTargetNode().getId()
										.equals(nextNode.getId())) {
									// if (!this.inNodeIsContained(baseSeg,
									// flow.getSrcNode(), baseNextNode)) {
									Flow tempFlow = new Flow(flow.getName(),
											flow.getId(), flow.getSrcNode(),
											baseNextNode);
									((Segment) firstSeg).getInNodeList().add(
											tempFlow);
									// }
								}
							}
							for (Flow flow : ((Segment) seg).getOutNodeList()) {
								if (flow.getSrcNode().getId()
										.equals(nextNode.getId())) {
									// if (!this.outNodeIsContained(baseSeg,
									// baseNextNode, flow.getTargetNode())) {
									Flow tempFlow = new Flow(flow.getName(),
											flow.getId(), baseNextNode,
											flow.getTargetNode());
									((Segment) firstSeg).getOutNodeList().add(
											tempFlow);
									// }
								}
							}
						}
					} else {
						f.setSrcNode(baseNode);
						firstSeg.getFlowList().add(f);
						this.mergeNode(firstSeg, seg, nextNode);
						// baseSeg.getGatewayList().add((Gateway)nextNode);
						// baseSeg.getFlowNodeRefList().add(new
						// FlowNodeRef(nextNode));
					}
				}
			}
		}
	}

	private Activity findSynonymActivity(Process firstSeg, ProcessNode nextNode) {
		for (Activity act : firstSeg.getActivityList()) {
			if (this.isSynonymActivity(act, nextNode)) {
				return act;
			}
		}
		return null;
	}

	private boolean isSynonymActivity(ProcessNode srcNode,
			ProcessNode targetNode) {
		for (List<String> list : this._SynonymActivities) {
			if (list.contains(srcNode.getName())
					&& list.contains(targetNode.getName())
					&& !this.isHandledSynonymActivity(srcNode, targetNode)) {
				return true;
			}
			// for (ProcessNode node : srcNode.getSynonymNode()) {
			// if (list.contains(node.getName()) &&
			// list.contains(targetNode.getName())
			// && !this.isHandledSynonymActivity(node)) {
			// return true;
			// }
			// }
		}
		return false;
	}

	private boolean isHandledSynonymActivity(ProcessNode node,
			ProcessNode targetNode) {
		for (ProcessNode n : this.handledNodes) {
			if (n.getId().equals(targetNode.getId())) {
				continue;
			}
			if (n.getName().equals(node.getName())) {
				return true;
			}
			for (ProcessNode synNode : node.getSynonymNode()) {
				if (n.getName().equals(synNode.getName())) {
					return true;
				}
			}
		}
		return false;
	}

	private void addFlowOrGateway(Process firstSeg, ProcessNode srcNode,
			ProcessNode targetNode) {
		if (this.hasFollowingFlow(firstSeg, srcNode)) {
			this.addGateway(firstSeg, srcNode.getId(), targetNode);
		} else {
			Flow tempFlow = new Flow("", "bpmn-" + UUID.randomUUID(), srcNode,
					targetNode);
			firstSeg.getFlowList().add(tempFlow);
		}

	}

	private boolean hasFollowingFlow(Process firstSeg, ProcessNode node) {
		for (Flow f : firstSeg.getFlowList()) {
			if (f.getSrcNode().getId().equals(node.getId())) {
				return true;
			}
		}
		return false;
	}

	private boolean inNodeIsContained(Segment segment, ProcessNode srcNode,
			ProcessNode targetNode) {
		for (Flow f : segment.getInNodeList()) {
			if (f.getTargetNode().getId().equals(targetNode.getId())
					&& f.getSrcNode().getName().equals(srcNode.getName())) {
				return true;
			}
		}
		return false;
	}

	private boolean outNodeIsContained(Segment segment, ProcessNode srcNode,
			ProcessNode targetNode) {
		for (Flow f : segment.getOutNodeList()) {
			if (f.getSrcNode().getId().equals(srcNode.getId())
					&& f.getTargetNode().getName().equals(targetNode.getName())) {
				return true;
			}
		}
		return false;
	}

	private List<String> getNextActivityNames(Process segment, String nodeId) {
		List<String> list = new ArrayList<String>();
		for (Flow f : segment.getFlowList()) {
			if (f.getSrcNode().getId().equals(nodeId)) {
				ProcessNode nextNode = f.getTargetNode();
				if (nextNode instanceof Gateway) {
					list.addAll(this.getNextActivityNames(segment,
							nextNode.getId()));
				} else {
					list.add(nextNode.getName());
				}
			}
		}
		return list;
	}

	private boolean nodeHasBeenHandled(ProcessNode nextNode) {
		// System.out.println("nodeHasBeenHandled() > nextNode=" + nextNode);
		for (ProcessNode node : this.handledNodes) {
			if (nextNode.getId().equals(node.getId())) {
				return true;
			}
		}
		return false;
	}

	private void addGateway(Process segment, String nodeId, ProcessNode nextNode) {
		Gateway gw = new Gateway();
		gw.setId("bpmn-" + UUID.randomUUID());
		gw.setName("New Gateway");
		gw.setGatewayDirection("Diverging");
		gw.setType(GatewayType.exclusiveGateway);
		segment.getGatewayList().add(gw);
		segment.getFlowNodeRefList().add(new FlowNodeRef(gw));

		Flow baseFlow = this.getFlow(segment, nodeId);
		Flow flowYes = new Flow();
		flowYes.setId("bpmn-" + UUID.randomUUID());
		flowYes.setName("Yes");
		flowYes.setSrcNode(gw);
		flowYes.setTargetNode(nextNode);
		Flow flowNo = new Flow();
		flowNo.setId("bpmn-" + UUID.randomUUID());
		flowNo.setName("No");
		flowNo.setSrcNode(gw);
		flowNo.setTargetNode(baseFlow.getTargetNode());
		baseFlow.setTargetNode(gw);
		segment.getFlowList().add(flowYes);
		segment.getFlowList().add(flowNo);
	}

	// find preceding activities or start event
	private List<ProcessNode> findPrecedingNodes(Process segment, String nodeId) {
		List<ProcessNode> activities = new ArrayList<ProcessNode>();
		for (Flow f : segment.getFlowList()) {
			if (f.getTargetNode() == null || f.getTargetNode().getId() == null) {
				// System.out.println("");
			}
			if (f.getTargetNode().getId().equals(nodeId)) {
				ProcessNode node = this.findNodeById(segment, f.getSrcNode()
						.getId());
				if (node instanceof Activity || node instanceof StartEvent) {
					activities.add(node);
				} else if (node instanceof Gateway) {
					activities.addAll(this.findPrecedingNodes(segment,
							node.getId()));
				}
			}
		}
		return activities;
	}

	private Flow getFlow(Process segment, String nodeId) {
		for (Flow f : segment.getFlowList()) {
			if (f.getSrcNode().getId().equals(nodeId)) {
				return f;
			}
		}
		return null;
	}

	private ProcessNode getNextNode(Process segment, String nodeId) {
		for (Flow f : segment.getFlowList()) {
			if (f.getSrcNode().getId().equals(nodeId)) {
				return f.getTargetNode();
			}
		}
		return null;
	}

	private Activity getActivityByName(Process segment, String name) {
		// System.out.println("=======getActivityByName==name=" + name);
		for (Activity a : segment.getActivityList()) {
			if (a.getName().equals(name)) {
				return a;
			} else {
				for (ProcessNode node : a.getSynonymNode()) {
					if (node.getName().equals(name)) {
						return a;
					}
				}
			}
		}
		return null;
	}

	private ProcessNode findNodeById(Process segment, String nodeId) {
		for (StartEvent start : segment.getStarteventList()) {
			if (start.getId().equals(nodeId)) {
				return start;
			}
		}
		for (EndEvent end : segment.getEndeventList()) {
			if (end.getId().equals(nodeId)) {
				return end;
			}
		}
		for (Activity act : segment.getActivityList()) {
			if (act.getId().equals(nodeId)) {
				return act;
			}
		}
		for (Gateway gateway : segment.getGatewayList()) {
			if (gateway.getId().equals(nodeId)) {
				return gateway;
			}
		}
		return null;
	}

	private ProcessNode findNodeByName(Process segment, String nodeName) {
		for (Activity act : segment.getActivityList()) {
			if (act.getName().equals(nodeName)) {
				return act;
			}
		}
		for (Gateway gateway : segment.getGatewayList()) {
			if (gateway.getName().equals(nodeName)) {
				return gateway;
			}
		}
		for (StartEvent start : segment.getStarteventList()) {
			if (start.getName().equals(nodeName)) {
				return start;
			}
		}
		for (EndEvent end : segment.getEndeventList()) {
			if (end.getName().equals(nodeName)) {
				return end;
			}
		}
		return null;
	}

	public void exportProcessToBPMN(Process process,
			List<String> listFilePaths, String outputFilePath) throws Exception {
		if (listFilePaths == null || listFilePaths.size() == 0) {
			throw new Exception("No process file names!");
		}
		String firstFilePath = listFilePaths.remove(0);
		Element srcElement = readFile(firstFilePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		Element desRootNode = (Element) doc.importNode(srcElement, false);

		// import
		List<String> importLocations = new ArrayList<String>();
		NodeList ImportList = srcElement.getElementsByTagName("import");
		for (int i = 0; i < ImportList.getLength(); i++) {
			desRootNode.appendChild((Element) doc.importNode(
					ImportList.item(i), false));
			importLocations.add(this.getImportLocation(ImportList.item(i)));
		}

		for (String s : listFilePaths) {
			// add imports to desRootNode
			Element ele;
			try {
				ele = readFile(s);
			} catch (FileNotFoundException ex) {
				continue;
			}
			NodeList nodes = ele.getElementsByTagName("import");
			for (int i = 0; i < nodes.getLength(); i++) {
				if (!importLocations.contains(this.getImportLocation(nodes
						.item(i)))) {
					desRootNode.appendChild((Element) doc.importNode(
							nodes.item(i), false));
					importLocations.add(this.getImportLocation(nodes.item(i)));
				}
			}
		}

		// process
		Element ProcessNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("process").item(0), false);
		ProcessNode.setAttribute("id", "bpmn-" + UUID.randomUUID());
		ProcessNode.setAttribute("name",
				this.getProcessNameByFilePath(outputFilePath));
		// ExtensionElementsNode and laneSet
		Element ExtensionElementsNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("extensionElements").item(0), false);
		Element BpmAttributesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:bpmAttributes").item(0), false);
		Element MilestonesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestones").item(0), false);
		Element MilestoneNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestone").item(0), false);
		if (process.getFlowNodeRefList() != null
				&& process.getFlowNodeRefList().size() > 0) {
			for (FlowNodeRef flowNodeRef : process.getFlowNodeRefList()) {
				Element tempFlowNodeRefElement = doc
						.createElement("flowNodeRef");
				tempFlowNodeRefElement.setTextContent(flowNodeRef.getNode()
						.getId());
				MilestoneNode.appendChild(tempFlowNodeRefElement);
			}
		}
		MilestonesNode.appendChild(MilestoneNode);
		BpmAttributesNode.appendChild(MilestonesNode);
		ExtensionElementsNode.appendChild(BpmAttributesNode);
		ProcessNode.appendChild(ExtensionElementsNode);
		ProcessNode.appendChild((Element) doc.importNode(srcElement
				.getElementsByTagName("laneSet").item(0), false));

		// sequenceFlow
		if (process.getFlowList() != null && process.getFlowList().size() > 0) {
			for (Flow f : process.getFlowList()) {
				Element tempFlowElement = doc.createElement("sequenceFlow");
				tempFlowElement.setAttribute("targetRef", f.getTargetNode()
						.getId());
				tempFlowElement.setAttribute("sourceRef", f.getSrcNode()
						.getId());
				if (f.getName() != "")
					tempFlowElement.setAttribute("name", f.getName());
				tempFlowElement.setAttribute("id", f.getId());
				ProcessNode.appendChild(tempFlowElement);
			}
		}
		// startEvent
		if (process.getStarteventList() != null
				&& process.getStarteventList().size() > 0) {
			for (StartEvent se : process.getStarteventList()) {
				Element tempStartEventElement = doc.createElement("startEvent");
				tempStartEventElement.setAttribute("name", se.getName());
				tempStartEventElement.setAttribute("id", se.getId());
				ProcessNode.appendChild(tempStartEventElement);
			}
		}// task(Activity)
		if (process.getActivityList() != null
				&& process.getActivityList().size() > 0) {
			for (Activity activity : process.getActivityList()) {
				Element tempActivityElement = null;
				// Modify by Wangwj
				// if(activity.getName().equals("Related department / Department manager / countersign"))
				if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_CALLACTIVITY)) {
					tempActivityElement = doc.createElement("callActivity");
					// Need change here !!!!
					tempActivityElement
							.setAttribute(
									"xmlns:bwl1",
									"http://www.ibm.com/WebSphere/bpm/BlueworksLive/10000f83e62451d-20000a13e62c91d");
					tempActivityElement.setAttribute("calledElement",
							activity.getCalledElement());
				} else if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_TASK)) {
					tempActivityElement = doc.createElement("task");
				}
				if (tempActivityElement != null) {
					tempActivityElement.setAttribute("name",
							activity.getDisplayName());
					tempActivityElement.setAttribute("id", activity.getId());

					// add documentation to activity
					Element documentation = doc.createElement("documentation");
					documentation.setAttribute("textFormat", "text/html");
					StringBuilder sb = new StringBuilder();
					sb.append("<b>Appear number: "
							+ activity.getDisplayAppearNum() + "</b>");
					sb.append("<br><b>Appear processes:</b>");
					for (int i = 1; i <= activity.getDisplayAppearProcess()
							.size(); i++) {
						sb.append("<br>" + i + ". "
								+ activity.getDisplayAppearProcess().get(i - 1));
					}

					// handle inNodeList and outNodeList for BPMN
					if (process instanceof Segment) {
						boolean haveInOrOutNode = false;
						int flowNum = 0;
						StringBuilder sbFlow = new StringBuilder();
						for (Flow f : ((Segment) process).getInNodeList()) {
							if (f.getTargetNode().getId()
									.equals(activity.getId())) {
								flowNum++;
								ProcessNode node = f.getSrcNode();
								sbFlow.append("<br>" + flowNum + ". From ");
								if (node instanceof Activity) {
									sbFlow.append("Activity ");
								} else {
									sbFlow.append("Gateway ");
								}
								sbFlow.append("\"" + node.getName() + "\"");
								if (node.getProcessName() != null
										&& !"".equals(node.getProcessName())) {
									sbFlow.append(" of \""
											+ node.getProcessName() + "\"");
								}
							}
						}
						if (flowNum > 0) {
							haveInOrOutNode = true;
							sb.append("<br><b>Incoming Flow:</b>");
							sb.append(sbFlow);
						}
						flowNum = 0;
						sbFlow = new StringBuilder();
						for (Flow f : ((Segment) process).getOutNodeList()) {
							if (f.getSrcNode().getId().equals(activity.getId())) {
								flowNum++;
								ProcessNode node = f.getTargetNode();
								sbFlow.append("<br>" + flowNum + ". To ");
								if (node instanceof Activity) {
									sbFlow.append("Activity ");
								} else {
									sbFlow.append("Gateway ");
								}
								sbFlow.append("\"" + node.getName() + "\"");
								if (node.getProcessName() != null
										&& !"".equals(node.getProcessName())) {
									sbFlow.append(" of \""
											+ node.getProcessName() + "\"");
								}
							}
						}
						if (flowNum > 0) {
							haveInOrOutNode = true;
							sb.append("<br><b>Outgoing Flow:</b>");
							sb.append(sbFlow);
						}

						// set activity color
						if (haveInOrOutNode) {
							Element extensionElements = doc
									.createElement("extensionElements");
							Element bpmAttributes = doc
									.createElement("ns2:bpmAttributes");
							Element properties = doc
									.createElement("ns2:properties");
							Element property = doc
									.createElement("ns2:property");
							property.setAttribute("id",
									"bpmn-" + UUID.randomUUID());
							Element propertyName = doc
									.createElement("ns2:propertyName");
							propertyName.setTextContent("color");
							Element stringliteralValue = doc
									.createElement("ns2:stringliteralValue");
							Element value = doc.createElement("ns2:value");
							value.setTextContent("red");
							stringliteralValue.appendChild(value);
							property.appendChild(propertyName);
							property.appendChild(stringliteralValue);
							properties.appendChild(property);
							bpmAttributes.appendChild(properties);
							extensionElements.appendChild(bpmAttributes);
							tempActivityElement.appendChild(extensionElements);
						}
					} else if (activity.getDisplayAppearNum() > 1) {
						Element extensionElements = doc
								.createElement("extensionElements");
						Element bpmAttributes = doc
								.createElement("ns2:bpmAttributes");
						Element properties = doc
								.createElement("ns2:properties");
						Element property = doc.createElement("ns2:property");
						property.setAttribute("id", "bpmn-" + UUID.randomUUID());
						Element propertyName = doc
								.createElement("ns2:propertyName");
						propertyName.setTextContent("color");
						Element stringliteralValue = doc
								.createElement("ns2:stringliteralValue");
						Element value = doc.createElement("ns2:value");
						value.setTextContent("purple");
						stringliteralValue.appendChild(value);
						property.appendChild(propertyName);
						property.appendChild(stringliteralValue);
						properties.appendChild(property);
						bpmAttributes.appendChild(properties);
						extensionElements.appendChild(bpmAttributes);
						tempActivityElement.appendChild(extensionElements);
					}

					documentation.setTextContent(sb.toString());
					tempActivityElement.appendChild(documentation);

					// textAnnotation and association
					/*
					 * Element textAnnotation =
					 * doc.createElement("textAnnotation");
					 * textAnnotation.setAttribute("id", "bpmn-" +
					 * UUID.randomUUID());
					 * textAnnotation.setAttribute("textFormat", "text/plain");
					 * Element text = doc.createElement("text");
					 * text.setTextContent("kkkkkkk");
					 * textAnnotation.appendChild(text);
					 * ProcessNode.appendChild(textAnnotation);
					 * 
					 * Element association = doc.createElement("association");
					 * association.setAttribute("associationDirection", "None");
					 * association.setAttribute("id", "bpmn-" +
					 * UUID.randomUUID()); association.setAttribute("sourceRef",
					 * textAnnotation.getAttribute("id"));
					 * association.setAttribute("targetRef", activity.getId());
					 * ProcessNode.appendChild(association);
					 */

					// add activity to process
					ProcessNode.appendChild(tempActivityElement);
				}
			}
		}
		// exclusiveGateway
		if (process.getGatewayList() != null
				&& process.getGatewayList().size() > 0) {
			for (Gateway g : process.getGatewayList()) {
				Element tempGatewayElement = doc.createElement(g.getType()
						.name());
				tempGatewayElement.setAttribute("gatewayDirection",
						g.getGatewayDirection());
				tempGatewayElement.setAttribute("name", g.getName());
				tempGatewayElement.setAttribute("id", g.getId());

				// add documentation to gateway
				// handle inNodeList and outNodeList for BPMN
				if (process instanceof Segment) {
					StringBuilder sb = new StringBuilder();
					boolean haveInOrOutNode = false;
					int flowNum = 0;
					StringBuilder sbFlow = new StringBuilder();
					for (Flow f : ((Segment) process).getInNodeList()) {
						if (f.getTargetNode().getId().equals(g.getId())) {
							flowNum++;
							ProcessNode node = f.getSrcNode();
							sbFlow.append("<br>" + flowNum + ". From ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Incoming Flow:</b>");
						sb.append(sbFlow);
					}
					flowNum = 0;
					sbFlow = new StringBuilder();
					for (Flow f : ((Segment) process).getOutNodeList()) {
						if (f.getSrcNode().getId().equals(g.getId())) {
							flowNum++;
							ProcessNode node = f.getTargetNode();
							sbFlow.append("<br>" + flowNum + ". To ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Outgoing Flow:</b>");
						sb.append(sbFlow);
					}

					if (haveInOrOutNode) {
						Element documentation = doc
								.createElement("documentation");
						documentation.setAttribute("textFormat", "text/html");
						documentation.setTextContent(sb.toString());
						tempGatewayElement.appendChild(documentation);
					}
				}

				ProcessNode.appendChild(tempGatewayElement);
			}
		}
		// endEvent
		if (process.getEndeventList() != null
				&& process.getEndeventList().size() > 0) {
			for (EndEvent ee : process.getEndeventList()) {
				Element tempEndEventElement = doc.createElement("endEvent");
				tempEndEventElement.setAttribute("name", ee.getName());
				tempEndEventElement.setAttribute("id", ee.getId());
				ProcessNode.appendChild(tempEndEventElement);
			}
		}
		desRootNode.appendChild(ProcessNode);
		doc.appendChild(desRootNode);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new java.io.File(outputFilePath));
		result.setSystemId(result.getSystemId().replaceAll("%20", " "));
		transformer.transform(source, result);
	}

	public void exportMergedFragmentToBPMN(Process process,
			List<String> listFilePaths, String outputFilePath, String owlFile)
			throws Exception {
		if (listFilePaths == null || listFilePaths.size() == 0) {
			throw new Exception("No process file names!");
		}
		String firstFilePath = listFilePaths.remove(0);
		Element srcElement = readFile(firstFilePath);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		Document doc = null;
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.newDocument();
		doc.setXmlStandalone(true);
		Element desRootNode = (Element) doc.importNode(srcElement, false);

		// import
		List<String> importLocations = new ArrayList<String>();
		NodeList ImportList = srcElement.getElementsByTagName("import");
		for (int i = 0; i < ImportList.getLength(); i++) {
			desRootNode.appendChild((Element) doc.importNode(
					ImportList.item(i), false));
			importLocations.add(this.getImportLocation(ImportList.item(i)));
		}

		for (String s : listFilePaths) {
			// add imports to desRootNode
			Element ele;
			try {
				ele = readFile(s);
			} catch (FileNotFoundException ex) {
				continue;
			}
			NodeList nodes = ele.getElementsByTagName("import");
			for (int i = 0; i < nodes.getLength(); i++) {
				if (!importLocations.contains(this.getImportLocation(nodes
						.item(i)))) {
					desRootNode.appendChild((Element) doc.importNode(
							nodes.item(i), false));
					importLocations.add(this.getImportLocation(nodes.item(i)));
				}
			}
		}

		// process
		Element ProcessNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("process").item(0), false);
		ProcessNode.setAttribute("id", "bpmn-" + UUID.randomUUID());
		ProcessNode.setAttribute("name",
				this.getProcessNameByFilePath(outputFilePath));
		// ExtensionElementsNode and laneSet
		Element ExtensionElementsNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("extensionElements").item(0), false);
		Element BpmAttributesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:bpmAttributes").item(0), false);
		Element MilestonesNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestones").item(0), false);
		Element MilestoneNode = (Element) doc.importNode(srcElement
				.getElementsByTagName("ns2:milestone").item(0), false);
		if (process.getFlowNodeRefList() != null
				&& process.getFlowNodeRefList().size() > 0) {
			for (FlowNodeRef flowNodeRef : process.getFlowNodeRefList()) {
				Element tempFlowNodeRefElement = doc
						.createElement("flowNodeRef");
				tempFlowNodeRefElement.setTextContent(flowNodeRef.getNode()
						.getId());
				MilestoneNode.appendChild(tempFlowNodeRefElement);
			}
		}
		MilestonesNode.appendChild(MilestoneNode);
		BpmAttributesNode.appendChild(MilestonesNode);
		ExtensionElementsNode.appendChild(BpmAttributesNode);
		ProcessNode.appendChild(ExtensionElementsNode);
		ProcessNode.appendChild((Element) doc.importNode(srcElement
				.getElementsByTagName("laneSet").item(0), false));

		// sequenceFlow
		if (process.getFlowList() != null && process.getFlowList().size() > 0) {
			for (Flow f : process.getFlowList()) {
				Element tempFlowElement = doc.createElement("sequenceFlow");
				tempFlowElement.setAttribute("targetRef", f.getTargetNode()
						.getId());
				tempFlowElement.setAttribute("sourceRef", f.getSrcNode()
						.getId());
				if (f.getName() != "")
					tempFlowElement.setAttribute("name", f.getName());
				tempFlowElement.setAttribute("id", f.getId());
				ProcessNode.appendChild(tempFlowElement);
			}
		}
		// startEvent
		if (process.getStarteventList() != null
				&& process.getStarteventList().size() > 0) {
			for (StartEvent se : process.getStarteventList()) {
				Element tempStartEventElement = doc.createElement("startEvent");
				tempStartEventElement.setAttribute("name", se.getName());
				tempStartEventElement.setAttribute("id", se.getId());
				ProcessNode.appendChild(tempStartEventElement);
			}
		}
		// OWLModel, generate the owl file
		OWLModel model = new OWLModel();
		ArrayList<ActivityEntity> entityList = new ArrayList<ActivityEntity>();
		// task(Activity)
		if (process.getActivityList() != null
				&& process.getActivityList().size() > 0) {
			for (Activity activity : process.getActivityList()) {
				Element tempActivityElement = null;
				// Modify by Wangwj
				// if(activity.getName().equals("Related department / Department manager / countersign"))
				if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_CALLACTIVITY)) {
					tempActivityElement = doc.createElement("callActivity");
					// Need change here !!!!
					tempActivityElement
							.setAttribute(
									"xmlns:bwl1",
									"http://www.ibm.com/WebSphere/bpm/BlueworksLive/10000f83e62451d-20000a13e62c91d");
					tempActivityElement.setAttribute("calledElement",
							activity.getCalledElement());
				} else if (activity.getType().equals(
						Activity.ACTIVITY_TYPE_TASK)) {
					tempActivityElement = doc.createElement("task");
				}
				if (tempActivityElement != null) {
					tempActivityElement.setAttribute("name",
							activity.getDisplayName());
					tempActivityElement.setAttribute("id", activity.getId());

					// add documentation to activity
					Element documentation = doc.createElement("documentation");
					documentation.setAttribute("textFormat", "text/html");
					StringBuilder sb = new StringBuilder();
					sb.append("<b>Appear number: "
							+ activity.getDisplayAppearNum() + "</b>");
					sb.append("<br><b>Appear processes:</b>");
					for (int i = 1; i <= activity.getDisplayAppearProcess()
							.size(); i++) {
						sb.append("<br>" + i + ". "
								+ activity.getDisplayAppearProcess().get(i - 1));
					}

					// handle inNodeList and outNodeList for BPMN
					if (process instanceof Segment) {
						// handle inNodeList and outNodeList for BPMN
						ActivityEntity entity = model.new ActivityEntity();
						entity.setName(activity.getName().replace(" ", "_"));
						ArrayList<String> incoming = new ArrayList<String>();
						ArrayList<String> outgoing = new ArrayList<String>();
						boolean haveInOrOutNode = false;
						int flowNum = 0;
						StringBuilder sbFlow = new StringBuilder();
						for (Flow f : ((Segment) process).getInNodeList()) {
							if (f.getTargetNode().getId()
									.equals(activity.getId())) {
								flowNum++;
								ProcessNode node = f.getSrcNode();
								sbFlow.append("<br>" + flowNum + ". From ");
								if (node instanceof Activity) {
									sbFlow.append("Activity ");
								} else {
									sbFlow.append("Gateway ");
								}
								sbFlow.append("\"" + node.getName() + "\"");
								if (node.getProcessName() != null
										&& !"".equals(node.getProcessName())) {
									sbFlow.append(" of \""
											+ node.getProcessName() + "\"");
								}

								incoming.add(node.getName().replace(" ", "_")
										+ "-"
										+ node.getProcessName().replace(" ",
												"_"));
							}
						}
						if (flowNum > 0) {
							haveInOrOutNode = true;
							sb.append("<br><b>Incoming Flow:</b>");
							sb.append(sbFlow);

							entity.setIncoming(incoming);
						}
						flowNum = 0;
						sbFlow = new StringBuilder();
						for (Flow f : ((Segment) process).getOutNodeList()) {
							if (f.getSrcNode().getId().equals(activity.getId())) {
								flowNum++;
								ProcessNode node = f.getTargetNode();
								sbFlow.append("<br>" + flowNum + ". To ");
								if (node instanceof Activity) {
									sbFlow.append("Activity ");
								} else {
									sbFlow.append("Gateway ");
								}
								sbFlow.append("\"" + node.getName() + "\"");
								if (node.getProcessName() != null
										&& !"".equals(node.getProcessName())) {
									sbFlow.append(" of \""
											+ node.getProcessName() + "\"");
								}

								outgoing.add(node.getName().replace(" ", "_")
										+ "-"
										+ node.getProcessName().replace(" ",
												"_"));
							}
						}
						if (flowNum > 0) {
							haveInOrOutNode = true;
							sb.append("<br><b>Outgoing Flow:</b>");
							sb.append(sbFlow);

							entity.setOutcoming(outgoing);
						}

						// set activity color
						if (haveInOrOutNode) {
							Element extensionElements = doc
									.createElement("extensionElements");
							Element bpmAttributes = doc
									.createElement("ns2:bpmAttributes");
							Element properties = doc
									.createElement("ns2:properties");
							Element property = doc
									.createElement("ns2:property");
							property.setAttribute("id",
									"bpmn-" + UUID.randomUUID());
							Element propertyName = doc
									.createElement("ns2:propertyName");
							propertyName.setTextContent("color");
							Element stringliteralValue = doc
									.createElement("ns2:stringliteralValue");
							Element value = doc.createElement("ns2:value");
							value.setTextContent("red");
							stringliteralValue.appendChild(value);
							property.appendChild(propertyName);
							property.appendChild(stringliteralValue);
							properties.appendChild(property);
							bpmAttributes.appendChild(properties);
							extensionElements.appendChild(bpmAttributes);
							tempActivityElement.appendChild(extensionElements);

							entityList.add(entity);
						}
					} else if (activity.getDisplayAppearNum() > 1) {
						Element extensionElements = doc
								.createElement("extensionElements");
						Element bpmAttributes = doc
								.createElement("ns2:bpmAttributes");
						Element properties = doc
								.createElement("ns2:properties");
						Element property = doc.createElement("ns2:property");
						property.setAttribute("id", "bpmn-" + UUID.randomUUID());
						Element propertyName = doc
								.createElement("ns2:propertyName");
						propertyName.setTextContent("color");
						Element stringliteralValue = doc
								.createElement("ns2:stringliteralValue");
						Element value = doc.createElement("ns2:value");
						value.setTextContent("purple");
						stringliteralValue.appendChild(value);
						property.appendChild(propertyName);
						property.appendChild(stringliteralValue);
						properties.appendChild(property);
						bpmAttributes.appendChild(properties);
						extensionElements.appendChild(bpmAttributes);
						tempActivityElement.appendChild(extensionElements);
					}

					documentation.setTextContent(sb.toString());
					tempActivityElement.appendChild(documentation);

					// textAnnotation and association
					/*
					 * Element textAnnotation =
					 * doc.createElement("textAnnotation");
					 * textAnnotation.setAttribute("id", "bpmn-" +
					 * UUID.randomUUID());
					 * textAnnotation.setAttribute("textFormat", "text/plain");
					 * Element text = doc.createElement("text");
					 * text.setTextContent("kkkkkkk");
					 * textAnnotation.appendChild(text);
					 * ProcessNode.appendChild(textAnnotation);
					 * 
					 * Element association = doc.createElement("association");
					 * association.setAttribute("associationDirection", "None");
					 * association.setAttribute("id", "bpmn-" +
					 * UUID.randomUUID()); association.setAttribute("sourceRef",
					 * textAnnotation.getAttribute("id"));
					 * association.setAttribute("targetRef", activity.getId());
					 * ProcessNode.appendChild(association);
					 */

					// add activity to process
					ProcessNode.appendChild(tempActivityElement);
				}
			}
		}
		// exclusiveGateway
		if (process.getGatewayList() != null
				&& process.getGatewayList().size() > 0) {
			for (Gateway g : process.getGatewayList()) {
				Element tempGatewayElement = doc.createElement(g.getType()
						.name());
				tempGatewayElement.setAttribute("gatewayDirection",
						g.getGatewayDirection());
				tempGatewayElement.setAttribute("name", g.getName());
				tempGatewayElement.setAttribute("id", g.getId());

				// add documentation to gateway
				// handle inNodeList and outNodeList for BPMN
				if (process instanceof Segment) {
					// handle inNodeList and outNodeList for BPMN
					ActivityEntity entity = model.new ActivityEntity();
					entity.setName(g.getName().replace(" ", "_"));
					ArrayList<String> incoming = new ArrayList<String>();
					ArrayList<String> outgoing = new ArrayList<String>();

					StringBuilder sb = new StringBuilder();
					boolean haveInOrOutNode = false;
					int flowNum = 0;
					StringBuilder sbFlow = new StringBuilder();
					for (Flow f : ((Segment) process).getInNodeList()) {
						if (f.getTargetNode().getId().equals(g.getId())) {
							flowNum++;
							ProcessNode node = f.getSrcNode();
							sbFlow.append("<br>" + flowNum + ". From ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}

							incoming.add(node.getName().replace(" ", "_") + "-"
									+ node.getProcessName().replace(" ", "_"));
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Incoming Flow:</b>");
						sb.append(sbFlow);

						entity.setIncoming(incoming);
					}
					flowNum = 0;
					sbFlow = new StringBuilder();
					for (Flow f : ((Segment) process).getOutNodeList()) {
						if (f.getSrcNode().getId().equals(g.getId())) {
							flowNum++;
							ProcessNode node = f.getTargetNode();
							sbFlow.append("<br>" + flowNum + ". To ");
							if (node instanceof Activity) {
								sbFlow.append("Activity ");
							} else {
								sbFlow.append("Gateway ");
							}
							sbFlow.append("\"" + node.getName() + "\"");
							if (node.getProcessName() != null
									&& !"".equals(node.getProcessName())) {
								sbFlow.append(" of \"" + node.getProcessName()
										+ "\"");
							}

							outgoing.add(node.getName().replace(" ", "_") + "-"
									+ node.getProcessName().replace(" ", "_"));
						}
					}
					if (flowNum > 0) {
						haveInOrOutNode = true;
						sb.append("<br><b>Outgoing Flow:</b>");
						sb.append(sbFlow);

						entity.setOutcoming(outgoing);
					}

					if (haveInOrOutNode) {
						Element documentation = doc
								.createElement("documentation");
						documentation.setAttribute("textFormat", "text/html");
						documentation.setTextContent(sb.toString());
						tempGatewayElement.appendChild(documentation);

						entityList.add(entity);
					}
				}

				ProcessNode.appendChild(tempGatewayElement);
			}
		}
		// endEvent
		if (process.getEndeventList() != null
				&& process.getEndeventList().size() > 0) {
			for (EndEvent ee : process.getEndeventList()) {
				Element tempEndEventElement = doc.createElement("endEvent");
				tempEndEventElement.setAttribute("name", ee.getName());
				tempEndEventElement.setAttribute("id", ee.getId());
				ProcessNode.appendChild(tempEndEventElement);
			}
		}
		desRootNode.appendChild(ProcessNode);
		doc.appendChild(desRootNode);

		TransformerFactory tFactory = TransformerFactory.newInstance();
		Transformer transformer = tFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.setOutputProperty(
				"{http://xml.apache.org/xslt}indent-amount", "4");
		DOMSource source = new DOMSource(doc);
		StreamResult result = new StreamResult(new java.io.File(outputFilePath));
		result.setSystemId(result.getSystemId().replaceAll("%20", " "));
		transformer.transform(source, result);
		// generate owl file
		model.generateOWL(entityList, owlFile);
	}

	public static void main(String[] args) {
		try {
			BPMNModel m = new BPMNModel();

			List<String> listSegmentDirs = new ArrayList<String>();
			for (int i = 1; i <= 5; i++) {
				listSegmentDirs.add("S" + i + "/");
			}
			// listSegmentDirs.add("S1/");
			List<String> listSegmentFiles = new ArrayList<String>();
			listSegmentFiles.add("S1_Department review and approve");
			listSegmentFiles.add("S2_DGA review and approve");
			listSegmentFiles.add("S3_Co. review and approve");
			listSegmentFiles.add("S4_DGA distribute and execution");
			listSegmentFiles.add("S5_Dpt execution");

			m.readSemanticTable();

			// m.threshold = 3;

			int activityNum = 0;
			int gatewayNum = 0;

			for (int i = 0; i < listSegmentDirs.size(); i++) {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						new FileInputStream(
								"E:/work/Clients/CMCC/test/wangwj/test0.txt")));
				String str = null;
				List<String> listFileNames = new ArrayList<String>();
				List<Process> listSegments = new ArrayList<Process>();
				m.totalActivityNumber = 0;
				m.totalGatewayNumber = 0;
				activityNum = 0;
				gatewayNum = 0;
				while ((str = br.readLine()) != null) {
					String inFileName = "E:/work/Clients/CMCC/test/wangwj/source/"
							+ str;
					String outFileName = "E:/work/Clients/CMCC/test/wangwj/result/"
							+ listSegmentDirs.get(i) + str;

					listFileNames.add(outFileName);

					Process p = m.getProcess(inFileName);
					Segment post_p = m.processProcess(p,
							listSegmentFiles.get(i));

					if (post_p != null) {
						activityNum += post_p.getActivityList().size();
						gatewayNum += post_p.getGatewayList().size();
					}

					listSegments.add(post_p);
					if (post_p != null) {
						m.totalActivityNumber += post_p.getActivityList()
								.size();
						m.totalGatewayNumber += post_p.getGatewayList().size();
					}

					m.exportProcess(inFileName, outFileName, post_p);
				}
				System.out.println("The activity number of "
						+ listSegmentFiles.get(i) + " = " + activityNum);
				System.out.println("The gateway number of "
						+ listSegmentFiles.get(i) + " = " + gatewayNum);

				String destSegmentFileName = "E:/work/Clients/CMCC/test/wangwj/segment/"
						+ listSegmentFiles.get(i) + ".bpmn";
				Date start = new Date();
				Process processResult = m.mergeProcess(listSegments);
				m.exportProcessToBPMN(processResult, listFileNames,
						destSegmentFileName);
				Date end = new Date();
				// System.out.println("Used time for merge: " + (end.getTime() -
				// start.getTime()));
				// System.out.println("After merge, " + listSegmentDirs.get(i) +
				// " activity number = " +
				// processResult.getActivityList().size());
				// System.out.println("After merge, " + listSegmentDirs.get(i) +
				// " gateway number = " +
				// processResult.getGatewayList().size());
			}

			// System.out.println("Finished!");
		} catch (Exception e) {
			// //System.out.println(e.toString());
			e.printStackTrace();
		}
	}
}
