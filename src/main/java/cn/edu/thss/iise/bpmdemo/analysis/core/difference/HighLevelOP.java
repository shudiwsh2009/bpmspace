package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.kie.api.definition.process.Process;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;

public class HighLevelOP {

	/**
	 * @author �ν��
	 */

	static List<Line> lines1 = new ArrayList<Line>();
	static LinkedList<GraphNode> nodes1 = new LinkedList<GraphNode>();
	static ArrayList<String> ids1 = new ArrayList<String>();
	static ArrayList<String> labels1 = new ArrayList<String>();
	static List<String> types1 = new ArrayList<String>();

	static List<Line> lines2 = new ArrayList<Line>();
	static LinkedList<GraphNode> nodes2 = new LinkedList<GraphNode>();
	static ArrayList<String> ids2 = new ArrayList<String>();
	static ArrayList<String> labels2 = new ArrayList<String>();
	static List<String> types2 = new ArrayList<String>();
	static LinkedList<String> s1 = new LinkedList<String>();
	static LinkedList<LinkedList<String>> before1 = new LinkedList<LinkedList<String>>();
	static LinkedList<LinkedList<String>> before2 = new LinkedList<LinkedList<String>>();
	// static List<Vertex> topoList1=new ArrayList<Vertex>();
	// static List<Vertex> topoList2=new ArrayList<Vertex>();
	static List<Matrix> ms = new ArrayList<Matrix>();
	static LinkedList<GraphNode> commen = new LinkedList<GraphNode>();
	static QMC qmc = new QMC();
	// static List<String> QMoutput=new ArrayList<String>();
	static List<String> deleteOp = new ArrayList<String>();
	static List<String> insertOp = new ArrayList<String>();
	static List<String> moveOp = new ArrayList<String>();
	static List<String> highLevelOp = new ArrayList<String>();
	static List<String> moveItem = new ArrayList<String>();

	static void resetVariables() {
		lines1 = new ArrayList<Line>();
		nodes1 = new LinkedList<GraphNode>();
		ids1 = new ArrayList<String>();
		labels1 = new ArrayList<String>();
		types1 = new ArrayList<String>();

		lines2 = new ArrayList<Line>();
		nodes2 = new LinkedList<GraphNode>();
		ids2 = new ArrayList<String>();
		labels2 = new ArrayList<String>();
		types2 = new ArrayList<String>();
		s1 = new LinkedList<String>();
		before1 = new LinkedList<LinkedList<String>>();
		before2 = new LinkedList<LinkedList<String>>();
		// static List<Vertex> topoList1=new ArrayList<Vertex>();
		// static List<Vertex> topoList2=new ArrayList<Vertex>();
		ms = new ArrayList<Matrix>();
		commen = new LinkedList<GraphNode>();
		qmc = new QMC();
		// static List<String> QMoutput=new ArrayList<String>();
		deleteOp = new ArrayList<String>();
		insertOp = new ArrayList<String>();
		moveOp = new ArrayList<String>();
		highLevelOp = new ArrayList<String>();
		moveItem = new ArrayList<String>();
	}

	public static GraphNode buildGraph(List<String> types, List<String> ids,
			List<String> labels, LinkedList<GraphNode> nodes, List<Line> lines) {

		GraphNode root = new GraphNode();

		// ��ӽڵ���Ϣ
		for (int i = 0; i < ids.size(); i++) {
			GraphNode node = new GraphNode();
			node.id = ids.get(i);
			node.label = labels.get(i);
			node.type = types.get(i);
			nodes.add(node);

		}

		// ���ڵ�
		root = nodes.peek();
		// ��ÿ��ͼ�е��ߵ���Ϣ�����ڵ�
		for (Line line : lines) {

			for (GraphNode nodep : nodes) {
				// ��ʼ��
				if (line.start.equals(nodep.id)) {
					for (GraphNode nodec : nodes) {
						// ��ֹ��
						if (line.end.equals(nodec.id)) {
							// �ڽڵ�����Ӹ��ڵ���ߺ��ӽڵ�
							nodep.addChildren(nodec);
							nodec.addParents(nodep);
						}
					}
				}
			}

		}
		return root;

	}

	/*
	 * public static GraphNode
	 * buildBpmnGraph(List<String>types,List<String>labels,
	 * LinkedList<GraphNode> nodes,List<Line> lines) {
	 * 
	 * GraphNode root = new GraphNode();
	 * 
	 * 
	 * 
	 * // ��ӽڵ���Ϣ for (int i = 0; i < labels.size(); i++) { GraphNode node =
	 * new GraphNode(); node.label = labels.get(i); node.type = types.get(i);
	 * nodes.add(node);
	 * 
	 * }
	 * 
	 * // ���ڵ� root = nodes.peek(); // ��ÿ��ͼ�е��ߵ���Ϣ�����ڵ� for (Line line :
	 * lines) {
	 * 
	 * for (GraphNode nodep : nodes) { // ��ʼ�� if (line.start.equals(nodep.id))
	 * { for (GraphNode nodec : nodes) { // ��ֹ�� if (line.end.equals(nodec.id))
	 * { // �ڽڵ�����Ӹ��ڵ���ߺ��ӽڵ�
	 * nodep.addChildren(nodes.get(nodes.indexOf(Integer.parseInt(line.end))));
	 * nodec.addParents(nodep); } } } }
	 * 
	 * } return root;
	 * 
	 * }
	 */
	public void addNode(GraphNode ancestor, LinkedList<String> record,
			ArrayList<GraphNode> list) {

	}

	/*
	 * public static void main(String[] args) { // TODO Auto-generated method
	 * stub //������һ��ͼ topoGraph topo1 = new topoGraph();
	 * 
	 * 
	 * ids1.add("A"); ids1.add("X"); ids1.add("B"); ids1.add("C");
	 * ids1.add("D"); ids1.add("E"); ids1.add("F"); ids1.add("G");
	 * 
	 * Vertex v1=new Vertex("A"); Vertex v2=new Vertex("X"); Vertex v3=new
	 * Vertex("B"); Vertex v4=new Vertex("C"); Vertex v5=new Vertex("D"); Vertex
	 * v6=new Vertex("E"); Vertex v7=new Vertex("F"); Vertex v8=new Vertex("G");
	 * topo1.addVertex(v1); // 0 topo1.addVertex(v2); // 1 topo1.addVertex(v3);
	 * // 2 topo1.addVertex(v4); // 3 topo1.addVertex(v5); // 4
	 * topo1.addVertex(v6); // 5 topo1.addVertex(v7); // 6 topo1.addVertex(v8);
	 * // 7
	 * 
	 * 
	 * Line line1 = new Line(); line1.start = "A"; line1.end = "X";
	 * topo1.addEdge(ids1.indexOf("A"), ids1.indexOf("X")); lines1.add(line1);
	 * Line line2 = new Line(); line2.start = "X"; line2.end = "B";
	 * topo1.addEdge(ids1.indexOf("X"), ids1.indexOf("B")); lines1.add(line2);
	 * Line line3 = new Line(); line3.start = "B"; line3.end = "C";
	 * topo1.addEdge(ids1.indexOf("B"), ids1.indexOf("C")); lines1.add(line3);
	 * Line line4 = new Line(); line4.start = "B"; line4.end = "E";
	 * topo1.addEdge(ids1.indexOf("B"), ids1.indexOf("E")); lines1.add(line4);
	 * Line line5 = new Line(); line5.start = "C"; line5.end = "D";
	 * topo1.addEdge(ids1.indexOf("C"), ids1.indexOf("D")); lines1.add(line5);
	 * Line line6 = new Line(); line6.start = "E"; line6.end = "F";
	 * topo1.addEdge(ids1.indexOf("E"), ids1.indexOf("F")); lines1.add(line6);
	 * Line line7 = new Line(); line7.start = "D"; line7.end = "G";
	 * topo1.addEdge(ids1.indexOf("D"), ids1.indexOf("G")); lines1.add(line7);
	 * Line line8 = new Line(); line8.start = "F"; line8.end = "G";
	 * topo1.addEdge(ids1.indexOf("F"), ids1.indexOf("G")); lines1.add(line8);
	 * 
	 * types1.add("xor"); types1.add("humannode"); // types.add("humannode");
	 * types1.add("and"); types1.add("humannode"); types1.add("humannode");
	 * types1.add("humannode"); types1.add("humannode"); types1.add("and");
	 * 
	 * System.out.println("topo1:"); topoList1=topo1.topo();
	 * System.out.println(topoList1);
	 * 
	 * //�����ڶ���ͼ topoGraph topo2 = new topoGraph(); ids2.add("Y");
	 * ids2.add("A"); ids2.add("B"); ids2.add("D"); ids2.add("Z");
	 * ids2.add("E"); ids2.add("C"); ids2.add("F"); ids2.add("G"); Line line11 =
	 * new Line(); line11.start = "Y"; line11.end = "A";
	 * topo2.addEdge(ids2.indexOf("Y"), ids2.indexOf("A")); lines2.add(line11);
	 * Line line12 = new Line(); line12.start = "Y"; line12.end = "B";
	 * topo2.addEdge(ids2.indexOf("Y"), ids2.indexOf("B")); lines2.add(line12);
	 * Line line13 = new Line(); line13.start = "A"; line13.end = "D";
	 * topo2.addEdge(ids2.indexOf("A"), ids2.indexOf("D")); lines2.add(line13);
	 * Line line14 = new Line(); line14.start = "B"; line14.end = "D";
	 * topo2.addEdge(ids2.indexOf("B"), ids2.indexOf("D"));
	 * 
	 * lines2.add(line14); Line line15 = new Line(); line15.start = "D";
	 * line15.end = "Z"; topo2.addEdge(ids2.indexOf("D"), ids2.indexOf("Z"));
	 * lines2.add(line15); Line line16 = new Line(); line16.start = "D";
	 * line16.end = "C"; topo2.addEdge(ids2.indexOf("D"), ids2.indexOf("C"));
	 * lines2.add(line16); Line line17 = new Line(); line17.start = "Z";
	 * line17.end = "E"; topo2.addEdge(ids2.indexOf("Z"), ids2.indexOf("E"));
	 * lines2.add(line17); Line line18 = new Line(); line18.start = "C";
	 * line18.end = "F"; topo2.addEdge(ids2.indexOf("C"), ids2.indexOf("F"));
	 * lines2.add(line18); Line line19= new Line(); line19.start = "F";
	 * line19.end = "G"; topo2.addEdge(ids2.indexOf("F"), ids2.indexOf("G"));
	 * lines2.add(line19); Line line20 = new Line(); line20.start = "E";
	 * line20.end = "G"; topo2.addEdge(ids2.indexOf("E"), ids2.indexOf("G"));
	 * lines2.add(line20);
	 * 
	 * 
	 * 
	 * 
	 * Vertex v10=new Vertex("Y"); Vertex v11=new Vertex("A"); Vertex v12=new
	 * Vertex("B"); Vertex v13=new Vertex("D"); Vertex v14=new Vertex("Z");
	 * Vertex v15=new Vertex("E"); Vertex v16=new Vertex("C"); Vertex v17=new
	 * Vertex("F"); Vertex v18=new Vertex("G"); topo2.addVertex(v10); // 0
	 * topo2.addVertex(v11); // 0 topo2.addVertex(v12); // 1
	 * topo2.addVertex(v13); // 2 topo2.addVertex(v14); // 3
	 * topo2.addVertex(v15); // 4 topo2.addVertex(v16); // 5
	 * topo2.addVertex(v17); // 6 topo2.addVertex(v18); // 7
	 * 
	 * 
	 * 
	 * 
	 * types2.add("xor"); types2.add("humannode"); types2.add("humannode");
	 * types2.add("and"); types2.add("humannode"); types2.add("humannode");
	 * types2.add("humannode"); types2.add("humannode"); types2.add("and");
	 * 
	 * System.out.println("topo2:"); topoList2=topo2.topo();
	 * System.out.println(topoList2);
	 * 
	 * 
	 * GraphNode root1 = new GraphNode(); root1 =
	 * buildGraph(types1,ids1,nodes1,lines1); GraphNode root2 = new GraphNode();
	 * root2 = buildGraph(types2,ids2,nodes2,lines2); // ���һ���ڵ�ĸ��ڵ�ͺ��ӽڵ�
	 * for (GraphNode node : nodes1) { if (node.id.equals("G")) {
	 * 
	 * for (GraphNode nodep : node.getParent()) { System.out.println("parents" +
	 * nodep.id); } if (node.getChildren() != null) { for (GraphNode nodec :
	 * node.getChildren()) { System.out.println("children" + nodec.id); } } } }
	 * // ���һ���ڵ�����ǰ��Ľڵ� /* for (GraphNode node : nodes) { if
	 * (node.id.equals("G")) { GraphNode temp = node; while (temp.getParent() !=
	 * null) {
	 * 
	 * for (GraphNode parent : temp.getParent()) {
	 * System.out.println(parent.id); temp = parent; }
	 * 
	 * } // System.out.println(temp.id);
	 * 
	 * } }
	 *///

	/*
	 * for (GraphNode node : nodes1) { if (node.id.equals("C")) {
	 * 
	 * allBefore(node, before1); } } for (GraphNode node : nodes2) { if
	 * (node.id.equals("G")) {
	 * 
	 * allBefore(node, before2); } } System.out.println(before1);
	 * ArrayList<String> path1 = combine(before1); System.out.println(path1);
	 * System.out.println(before2); ArrayList<String> path2 = combine(before2);
	 * System.out.println(path2);
	 * 
	 * // LinkedList<GraphNode> commen=commenNodes(nodes1,nodes2);
	 * commen=commenNodes(nodes1,nodes2); System.out.println("commen______");
	 * for(GraphNode s:commen) { System.out.println(s.id); }
	 * 
	 * LinkedList<GraphNode> same1=findSameNodes(nodes1,commen);
	 * LinkedList<GraphNode> same2=findSameNodes(nodes2,commen); //
	 * LinkedList<GraphNode> sortSame1=new LinkedList<GraphNode>();
	 * LinkedList<GraphNode> sortSame2=new LinkedList<GraphNode>();
	 * 
	 * //�ѽڵ㰴��һ����˳������
	 * 
	 * for(GraphNode s1:same1) { for(GraphNode s2:same2) {
	 * if(s1.getId().equals(s2.getId())) { sortSame2.add(s2); } }
	 * 
	 * // System.out.println(s.id); }
	 * 
	 * System.out.println(same1); System.out.println("same1______");
	 * for(GraphNode s:same1) { System.out.println(s.id); }
	 * System.out.println("same2______"); for(GraphNode s:same2) {
	 * System.out.println(s.id); }
	 * 
	 * System.out.println("same__________"); for (GraphNode node : same1) { if
	 * (node.id.equals("G")) {
	 * 
	 * for (GraphNode nodep : node.getParent()) { System.out.println("parents" +
	 * nodep.id); } if (node.getChildren() != null) { for (GraphNode nodec :
	 * node.getChildren()) { System.out.println("children" + nodec.id); } } } }
	 * // for (GraphNode node : same2) { for (GraphNode node : sortSame2) { if
	 * (node.id.equals("G")) {
	 * 
	 * for (GraphNode nodep : node.getParent()) { System.out.println("parents" +
	 * nodep.id); } if (node.getChildren() != null) { for (GraphNode nodec :
	 * node.getChildren()) { System.out.println("children" + nodec.id); } } } }
	 * 
	 * Matrix m1=createMatrix(same1,nodes1,1); // Matrix
	 * m2=createMatrix(same2,nodes2,2); Matrix
	 * m2=createMatrix(sortSame2,nodes2,2);
	 * 
	 * Matrix m=findDifference(m1,m2); ms=divide(m);
	 * 
	 * System.out.println("divide:"); for(Matrix t:ms) {
	 * System.out.println(t.getNames()); QuineMcCluskey q = new
	 * QuineMcCluskey(); List<String> QMoutput=new ArrayList<String>(); for(int
	 * i=0;i<t.n;i++) { String line=""; for(int j=0;j<t.n;j++) {
	 * line=line+t.matrix[i][j]; System.out.print(t.matrix[i][j]+" "); } try {
	 * System.out.println("line"+line); q.addTerm(line); } catch (ExceptionQuine
	 * e) { // TODO Auto-generated catch block e.printStackTrace(); }
	 * System.out.println(); } try { q.simplify(); } catch (ExceptionQuine e) {
	 * // TODO Auto-generated catch block e.printStackTrace(); }
	 * qmc.writeOutputFile(q); for(int i=0;i<q.count;i++) { try {
	 * QMoutput.add(q.getTerm(i)); } catch (ExceptionQuine e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } }
	 * 
	 * findMoveItem(QMoutput,t);
	 * 
	 * }
	 * 
	 * 
	 * for(int i=0;i<m1.n;i++) {
	 * 
	 * 
	 * for(int j=0;j<m1.n;j++) {
	 * 
	 * System.out.print(m1.matrix[i][j]+" "); }
	 * 
	 * 
	 * System.out.println(); }
	 * 
	 * 
	 * for(int i=0;i<m2.n;i++) { for(int j=0;j<m2.n;j++) {
	 * 
	 * System.out.print(m2.matrix[i][j]+" "); } System.out.println(); }
	 * deleteOp=delete(); insertOp=insert(); moveOp=move();
	 * System.out.println(deleteOp); System.out.println(insertOp);
	 * System.out.println(moveOp); System.out.println(moveItem); }
	 */

	public static void main(String[] args) {
		List<Process> processes1;

		List<Process> processes2;
		try {
			processes1 = ConvertBPMNXMLToProcess
					.importFromXmlFile("models/3.bpmn");
			processes2 = ConvertBPMNXMLToProcess
					.importFromXmlFile("models/2.bpmn");
			highLevelOp = createHighLevelOP(processes1.get(0),
					processes2.get(0));
			if (highLevelOp.size() == 0) {
				System.out.println("the two models are the same");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public static List<String> dif(String file1, String file2)
			throws IOException {
		List<Process> processes1;
		List<Process> processes2;
		processes1 = ConvertBPMNXMLToProcess.importFromXmlFile(file1);
		processes2 = ConvertBPMNXMLToProcess.importFromXmlFile(file2);
		if (processes1 != null && processes2 != null) {
			return createHighLevelOP(processes1.get(0), processes2.get(0));
		}
		return null;
	}

	public static List<String> createHighLevelOP(Process pro1, Process pro2) {
		// ������һ��ͼ
		// topoGraph topo1 = new topoGraph();

		// ����һ��bpmn�ļ����ļ��пɰ������ģ�ͣ�����ģ���б�
		resetVariables();
		RuleFlowProcess process1 = (RuleFlowProcess) pro1;
		process1 = DataUtil.deMultiInstance(process1);
		// ģ�ͺ��ӽڵ����Ϣ
		org.kie.api.definition.process.Node[] nodes = process1.getNodes();
		System.out.println("node number:" + nodes.length);

		int q;
		// Integer count1[] = new Integer [nodes.length];
		for (q = 0; q < nodes.length; q++) {
			int count = 0;
			// count1[q] = 1;
			// System.out.println(nodes[i].getClass()+"nodesclass");
			System.out.println(nodes[q].getId() + "nodesid");
			ids1.add(String.valueOf(nodes[q].getId()));
			for (int i = 0; i < q; i++) {
				// System.out.println(nodes[i].getName()+"=="+nodes[q].getName());
				if (nodes[i].getName().trim().equals(nodes[q].getName().trim()))
					count++;
				// System.out.println(count);
			}
			String s = nodes[q].getName();

			if (count != 0)
				s += count;

			labels1.add(s);
			Vertex v = new Vertex(s);
			// topo1.addVertex(v);
			/*
			 * if(labels1.contains(nodes[q].getName())) {
			 * 
			 * for(String tempS:labels1) { if(tempS.equals(nodes[q].getName()))
			 * { //count1[q] ++; } } String
			 * s=nodes[q].getName()+nodes[q].getId();
			 * 
			 * labels1.add(s); Vertex v=new Vertex(s); topo1.addVertex(v); }
			 * else { labels1.add(nodes[q].getName()); Vertex v=new
			 * Vertex(nodes[q].getName()); topo1.addVertex(v); }
			 */

			types1.add(nodes[q].getClass().getName());
			// System.out.println(nodes[q].getClass().getName());

			Map<String, List<org.kie.api.definition.process.Connection>> connections = nodes[q]
					.getIncomingConnections();
			//

			Set<Map.Entry<String, List<org.kie.api.definition.process.Connection>>> set = connections
					.entrySet();
			for (Iterator<Map.Entry<String, List<org.kie.api.definition.process.Connection>>> it = set
					.iterator(); it.hasNext();) {
				Map.Entry<String, List<org.kie.api.definition.process.Connection>> entry = (Map.Entry<String, List<org.kie.api.definition.process.Connection>>) it
						.next();

				for (org.kie.api.definition.process.Connection c : entry
						.getValue()) {
					Line line = new Line();
					line.start = String.valueOf(c.getFrom().getId());
					// line.end = c.getTo().getName();
					line.end = String.valueOf(c.getTo().getId());
					// topo1.addEdge((int)(c.getFrom().getId()),
					// (int)(c.getTo().getId()));
					lines1.add(line);
					// System.out.println("value");

					// System.out.println(c.getFromType());
					// System.out.println(c.getToType());
					// System.out.println(c.getFrom().getId());
					// System.out.println(c.getFrom().getName());
					// System.out.println(c.getToType());
					// System.out.println(c.getTo().getId());
					// System.out.println(c.getTo().getName());
				}

				// System.out.println("key"+entry.getKey() + "--->" +"value"+
				// entry.getValue());
			}

			//
			System.out.println("connection number: " + connections.size());

		}
		System.out.println("topo1:");
		System.out.println(ids1);
		System.out.println(labels1);
		System.out.println(lines1);
		// topoList1=topo1.topo();
		// System.out.println(topoList1);
		// ������2��ͼ
		// topoGraph topo2 = new topoGraph();
		RuleFlowProcess process2 = (RuleFlowProcess) pro2;
		process2 = DataUtil.deMultiInstance(process2);
		// ģ�ͺ��ӽڵ����Ϣ
		// org.kie.api.definition.process.Node[] nodesp2 = process2.getNodes();
		nodes = process2.getNodes();
		System.out.println("node number:" + nodes.length);
		int p;
		// Integer count2[] = new Integer [nodes.length];
		for (p = 0; p < nodes.length; p++) {

			int count = 0;
			// count1[q] = 1;
			// System.out.println(nodes[i].getClass()+"nodesclass");
			System.out.println(nodes[p].getId() + "nodesid");
			ids2.add(String.valueOf(nodes[p].getId()));
			for (int i = 0; i < p; i++) {
				// System.out.println(nodes[i].getName()+"=="+nodes[p].getName());
				if (nodes[i].getName().trim().equals(nodes[p].getName().trim()))
					count++;
				// System.out.println(count);
			}
			String s = nodes[p].getName();

			if (count != 0)
				s += count;

			labels2.add(s);
			Vertex v = new Vertex(s);
			// topo2.addVertex(v);

			/*
			 * //count2[p]=1; //
			 * System.out.println(nodes[i].getClass()+"nodesclass");
			 * System.out.println(nodes[p].getId()+"nodesid");
			 * ids2.add(String.valueOf(nodes[p].getId()));
			 * 
			 * // labels2.add(nodesp2[p].getName());
			 * if(labels2.contains(nodes[p].getName())) {
			 * 
			 * for(String tempS:labels2) { if(tempS.equals(nodes[p].getName()))
			 * { //count2[p]++; } } String
			 * s=nodes[p].getName()+nodes[p].getId(); //String
			 * s=nodes[p].getName(); //s=s+String.valueOf(count2[p]);
			 * labels2.add(s); Vertex v=new Vertex(s); topo2.addVertex(v); // 0
			 * } else { labels2.add(nodes[p].getName()); Vertex v=new
			 * Vertex(nodes[p].getName()); topo2.addVertex(v); // 0 }
			 */

			// Vertex v=new Vertex(nodesp2[p].getName());
			// topo2.addVertex(v); // 0

			types2.add(nodes[p].getClass().getName());

			Map<String, List<org.kie.api.definition.process.Connection>> connections = nodes[p]
					.getIncomingConnections();
			//

			Set<Map.Entry<String, List<org.kie.api.definition.process.Connection>>> set = connections
					.entrySet();
			for (Iterator<Map.Entry<String, List<org.kie.api.definition.process.Connection>>> it = set
					.iterator(); it.hasNext();) {
				Map.Entry<String, List<org.kie.api.definition.process.Connection>> entry = (Map.Entry<String, List<org.kie.api.definition.process.Connection>>) it
						.next();

				for (org.kie.api.definition.process.Connection c : entry
						.getValue()) {
					Line line = new Line();
					line.start = String.valueOf(c.getFrom().getId());
					line.end = String.valueOf(c.getTo().getId());
					// topo2.addEdge((int)c.getFrom().getId(),
					// (int)c.getTo().getId());
					lines2.add(line);
					/*
					 * System.out.println("value");
					 * 
					 * System.out.println(c.getFromType());
					 * System.out.println(c.getToType());
					 * System.out.println(c.getFrom().getId());
					 * System.out.println(c.getFrom().getName());
					 * System.out.println(c.getToType());
					 * System.out.println(c.getTo().getId());
					 * System.out.println(c.getTo().getName());
					 */
				}

				// System.out.println("key"+entry.getKey() + "--->" +"value"+
				// entry.getValue());
			}

			//
			System.out.println("connection number: " + connections.size());

		}
		System.out.println("topo2:");
		System.out.println(ids2);
		System.out.println(labels2);
		System.out.println(lines2);
		// topoList2=topo2.topo();
		// System.out.println(topoList2);

		// ///////////////////
		GraphNode root1 = new GraphNode();
		root1 = buildGraph(types1, ids1, labels1, nodes1, lines1);
		GraphNode root2 = new GraphNode();
		root2 = buildGraph(types2, ids2, labels2, nodes2, lines2);
		for (GraphNode node : nodes1) {

			System.out.println("nodes1" + node.label);

		}
		System.out.println("nodes2s" + nodes2.size());
		for (GraphNode node : nodes2) {

			System.out.println("nodes2" + node.label);

		}

		// ���һ���ڵ�ĸ��ڵ�ͺ��ӽڵ�
		for (GraphNode node : nodes1) {
			if (node.label.equals("Self Evaluation")) {

				for (GraphNode nodep : node.getParent()) {
					System.out.println("parents" + nodep.label);
				}
				if (node.getChildren() != null) {
					for (GraphNode nodec : node.getChildren()) {
						System.out.println("children" + nodec.label);
					}
				}
			}
		}
		// ���һ���ڵ�����ǰ��Ľڵ�
		/*
		 * for (GraphNode node : nodes) { if (node.id.equals("G")) { GraphNode
		 * temp = node; while (temp.getParent() != null) {
		 * 
		 * for (GraphNode parent : temp.getParent()) {
		 * System.out.println(parent.id); temp = parent; }
		 * 
		 * } // System.out.println(temp.id);
		 * 
		 * } }
		 */

		for (GraphNode node : nodes1) {
			if (node.label.equals("C")) {

				allBefore(node, before1);
			}
		}
		for (GraphNode node : nodes2) {
			if (node.label.equals("Self Evaluation")) {

				allBefore(node, before2);
			}
		}
		System.out.println(before1);
		ArrayList<String> path1 = combine(before1);
		System.out.println(path1);
		System.out.println(before2);
		ArrayList<String> path2 = combine(before2);
		System.out.println(path2);

		// LinkedList<GraphNode> commen=commenNodes(nodes1,nodes2);
		commen = commenNodes(nodes1, nodes2);
		System.out.println("commen______");
		for (GraphNode s : commen) {
			System.out.println(s.label);
		}

		LinkedList<GraphNode> same1 = findSameNodes(nodes1, commen);
		LinkedList<GraphNode> same2 = findSameNodes(nodes2, commen);
		// LinkedList<GraphNode> sortSame1=new LinkedList<GraphNode>();
		LinkedList<GraphNode> sortSame2 = new LinkedList<GraphNode>();

		// �ѽڵ㰴��һ����˳������

		for (GraphNode s1 : same1) {
			for (GraphNode s2 : same2) {
				if (s1.getId().equals(s2.getId())) {
					sortSame2.add(s2);
				}
			}

			// System.out.println(s.id);
		}
		System.out.println("ids1" + labels1);
		System.out.println("ids2" + labels2);
		// System.out.println(topoList1);
		// System.out.println(topoList2);
		System.out.println(same1);
		System.out.println("same1______");
		for (GraphNode s : same1) {
			System.out.println(s.label);
		}
		System.out.println("same2______");
		for (GraphNode s : same2) {
			System.out.println(s.label);
		}

		System.out.println("same__________");

		// for (GraphNode node : same2) {
		for (GraphNode node : sortSame2) {
			if (node.label.equals("Self Evaluation")) {

				for (GraphNode nodep : node.getParent()) {
					System.out.println("parents" + nodep.label);
				}
				if (node.getChildren() != null) {
					for (GraphNode nodec : node.getChildren()) {
						System.out.println("children" + nodec.label);
					}
				}
			}
		}

		Matrix m1 = createMatrix(same1, nodes1, 1);
		Matrix m2 = createMatrix(same2, nodes2, 2);
		// Matrix m2=createMatrix(sortSame2,nodes2,2);

		Matrix m = findDifference(m1, m2);
		ms = divide(m);

		/*
		 * for(int i=0;i<m1.n;i++) {
		 * 
		 * 
		 * for(int j=0;j<m1.n;j++) {
		 * 
		 * System.out.print(m1.matrix[i][j]+" "); }
		 * 
		 * 
		 * System.out.println(); }
		 * 
		 * 
		 * for(int i=0;i<m2.n;i++) { for(int j=0;j<m2.n;j++) {
		 * 
		 * System.out.print(m2.matrix[i][j]+" "); } System.out.println(); }
		 * 
		 * 
		 * for(int i=0;i<m.n;i++) {
		 * 
		 * 
		 * for(int j=0;j<m.n;j++) {
		 * 
		 * System.out.print(m.matrix[i][j]+" "); }
		 * 
		 * 
		 * System.out.println(); }
		 * 
		 * for(Matrix t:ms) { if(t.n==0) continue;
		 * System.out.println("divide"+t.n);
		 * 
		 * for(int i=0;i<t.n;i++) {
		 * 
		 * 
		 * for(int j=0;j<t.n;j++) {
		 * 
		 * System.out.print(t.matrix[i][j]+" "); }
		 * 
		 * 
		 * System.out.println(); }
		 * 
		 * }
		 */

		// System.out.println("divide:");
		for (Matrix t : ms) {
			if (t.n == 0)
				continue;
			System.out.println(t.getNames());
			QuineMcCluskey qm = new QuineMcCluskey();
			List<String> QMoutput = new ArrayList<String>();
			for (int i = 0; i < t.n; i++) {
				String line = "";
				for (int j = 0; j < t.n; j++) {
					line = line + t.matrix[i][j];
					System.out.print(t.matrix[i][j] + " ");
				}
				try {
					System.out.println("line" + line);
					boolean flag = false;
					for (char a : line.toCharArray()) {
						if (a != '0') {
							flag = true;
							break;
						}
					}
					if (flag)
						qm.addTerm(line);
				} catch (ExceptionQuine e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println();
			}
			try {
				qm.simplify();
			} catch (ExceptionQuine e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			qmc.writeOutputFile(qm);
			for (int i = 0; i < qm.count; i++) {
				try {
					QMoutput.add(qm.getTerm(i));
				} catch (ExceptionQuine e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			findMoveItem(QMoutput, t);

		}

		deleteOp = delete();
		insertOp = insert();
		moveOp = move();
		System.out.println(deleteOp);
		System.out.println(insertOp);
		System.out.println(moveOp);
		// System.out.println(moveItem);
		highLevelOp.addAll(deleteOp);
		highLevelOp.addAll(insertOp);
		highLevelOp.addAll(moveOp);

		return highLevelOp;

	}

	// һ���ڵ�ǰ�����еĽڵ�
	public static void allBefore(GraphNode gn,
			LinkedList<LinkedList<String>> before) {

		// LinkedList<String> s1=new LinkedList<String>();
		if (gn.getParent() == null) {

			LinkedList<String> s2 = new LinkedList<String>(s1);
			before.add(s2);
			// System.out.println(before);
		} else {

			for (GraphNode parent : gn.getParent()) {

				if (s1.contains(parent.label)) {
					LinkedList<String> s2 = new LinkedList<String>(s1);
					before.add(s2);
				} else {
					// if(!before.contains(parent.id))
					// {
					s1.addFirst(parent.label);
					// }
					// before.add(parent.id);

					allBefore(parent, before);

					s1.remove(s1.indexOf(parent.label));

				}

			}

		}

	}

	/*
	 * public static ArrayList<FlagMatrix> divide(Matrix m1 ,Matrix m2) { int
	 * num=m1.n; String[][] flag=new String[num][num];
	 * String[][]temp1=m1.matrix; String[][]temp2=m2.matrix; ArrayList<String>
	 * tempNames1=m1.sameNodesName;//����Ȱ���ͼ2�������� ArrayList<String>
	 * tempNames2=m2.sameNodesName; for(int i=0;i<num;i++) { for(int
	 * j=0;j<num;j++) { if(!temp1[i][j].equals(temp2[i][j])) { flag[i][j]="1";
	 * 
	 * } else { flag[i][j]="0"; } } }
	 * 
	 * 
	 * 
	 * return null;
	 * 
	 * }
	 */
	// �ϲ�����·��������·���ڵ�
	public static ArrayList<String> combine(
			LinkedList<LinkedList<String>> before) {
		ArrayList<String> path = new ArrayList<String>();
		int bn = before.size();
		int max = 0;
		for (LinkedList<String> temp : before) {
			if (temp.size() > max) {
				max = temp.size();
			}

			for (int i = 0; i < temp.size(); i++) {
				if (!path.contains(temp.get(i))) {
					path.add(i, temp.get(i));// change

				}
			}
		}

		return path;

	}

	public static LinkedList<GraphNode> commenNodes(
			LinkedList<GraphNode> nodes1, LinkedList<GraphNode> nodes2) {
		LinkedList<GraphNode> commen = new LinkedList<GraphNode>();
		for (GraphNode n1 : nodes1) {
			for (GraphNode n2 : nodes2) {
				if (n1.label.equals(n2.label))
					commen.add(n1);
			}

		}
		return commen;

	}

	public static LinkedList<GraphNode> findSameNodes(
			LinkedList<GraphNode> nodes, LinkedList<GraphNode> commen) {
		LinkedList<GraphNode> same = new LinkedList<GraphNode>();

		for (GraphNode node : nodes) {
			for (GraphNode c : commen) {
				if (node.label.equals(c.label))
					same.add(node);
				// break;
			}

		}

		return same;

	}

	public static ArrayList<Matrix> divide(Matrix m) {
		ArrayList<Matrix> matrixes1 = new ArrayList<Matrix>();
		// ArrayList<Matrix> matrixes2=new ArrayList<Matrix>();
		ArrayList<String> names = m.getNames();

		for (String name : names) {
			ArrayList<String> temp = new ArrayList<String>();
			temp.add(name);
			Matrix matrix = new Matrix();
			matrix.setNames(temp);
			matrixes1.add(matrix);

		}
		String[][] temp = m.getMatrix();
		Matrix matrix1 = new Matrix();
		Matrix matrix2 = new Matrix();
		for (int i = 0; i < m.getN(); i++) {
			for (int j = i; j < m.getN(); j++) {
				if (temp[i][j].equals("1")) {

					matrix1 = findMatrix(names.get(i), matrixes1);
					matrix2 = findMatrix(names.get(j), matrixes1);

					if (!matrix1.equals(matrix2)) {
						Matrix matrixTemp = mergeMatrix(matrix1, matrix2, m);
						matrixes1.remove(matrixes1.indexOf(matrix1));
						matrixes1.remove(matrixes1.indexOf(matrix2));
						matrixes1.add(matrixTemp);
					}

				}
			}
		}

		return matrixes1;

	}

	public static Matrix mergeMatrix(Matrix m1, Matrix m2, Matrix m) {

		ArrayList<String> mNames = m.getNames();
		String[][] mMatrix = m.getMatrix();

		Matrix mergeMatrix = new Matrix();

		ArrayList<String> mergeNames = new ArrayList<String>();
		// mergeFlags;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for (String temp1 : m1.getNames()) {
			if (mNames.indexOf(temp1) < min) {
				min = mNames.indexOf(temp1);
			}
			if (mNames.indexOf(temp1) > max) {
				max = mNames.indexOf(temp1);
			}

		}
		for (String temp2 : m2.getNames()) {
			if (mNames.indexOf(temp2) < min) {
				min = mNames.indexOf(temp2);
			}
			if (mNames.indexOf(temp2) > max) {
				max = mNames.indexOf(temp2);
			}
		}
		if (min <= max) {
			for (int i = min; i <= max; i++) {
				mergeNames.add(mNames.get(i));
			}
			mergeMatrix.setN(mergeNames.size());
			mergeMatrix.setNames(mergeNames);
			String[][] mergeFlags = new String[mergeNames.size()][mergeNames
					.size()];
			for (int i = min; i <= max; i++) {
				for (int j = min; j <= max; j++) {
					mergeFlags[i - min][j - min] = mMatrix[i][j];
				}
			}
			mergeMatrix.setMatrix(mergeFlags);
		}

		return mergeMatrix;

	}

	public static Matrix findMatrix(String s, ArrayList<Matrix> matrixes) {

		for (Matrix temp : matrixes) {

			if (temp.getNames().contains(s)) {
				return temp;
			}

		}

		return null;

	}

	public static Matrix findDifference(Matrix m1, Matrix m2) {
		Matrix m = new Matrix();
		String[][] s1 = m1.getMatrix();
		String[][] s2 = m2.getMatrix();
		System.out.println(s1.length);
		System.out.println(s2.length);
		ArrayList<String> n1 = m1.getNames();
		String[][] flags = new String[n1.size()][n1.size()];
		// for(int i=0;i<s1.length;i++)
		for (int i = 0; i < n1.size(); i++) {
			// for(int j=0;j<s1[i].length;j++)
			for (int j = 0; j < n1.size(); j++) {

				if (!s1[i][j].equals(s2[i][j])) {
					flags[i][j] = "1";
				} else {
					flags[i][j] = "0";
				}
			}
		}

		m.setMatrix(flags);
		m.setNames(n1);
		m.setN(n1.size());
		return m;

	}

	public static Matrix createMatrix(LinkedList<GraphNode> sames,
			LinkedList<GraphNode> nodes, int flag) {

		Matrix m = new Matrix();
		m.n = sames.size();
		System.out.print("n" + m.n);
		for (GraphNode tempn : sames) {
			m.names.add(tempn.label);
		}

		m.matrix = new String[m.n][m.n];
		for (int i = 0; i < m.n; i++) {
			for (int j = 0; j < m.n; j++) {
				if (i == j) {
					m.matrix[i][j] = "k";
				} else

				{
					before1.clear();
					before2.clear();

					allBefore(sames.get(i), before1);
					allBefore(sames.get(j), before2);
					ArrayList<String> path1 = combine(before1);
					// ArrayList<String> pathTopo1=new ArrayList<String>();
					// ������

					/*
					 * if(flag==1) { for(Vertex v:topoList1) {
					 * if(path1.contains(v.label)) { pathTopo1.add(v.label); } }
					 * }else if(flag==2){
					 * 
					 * for(Vertex v:topoList2) { if(path1.contains(v.label)) {
					 * pathTopo1.add(v.label); } }
					 * 
					 * 
					 * }
					 */

					path1.add(sames.get(i).label);// ���ϱ���

					// pathTopo1.add(sames.get(i).label);

					ArrayList<String> path2 = combine(before2);
					System.out.println("path2:" + path2);
					// ArrayList<String> pathTopo2=new ArrayList<String>();

					/*
					 * if(flag==1) { // System.out.println("1");
					 * System.out.println(topoList1.size()+"size");
					 * 
					 * for(Vertex v:topoList1)
					 * 
					 * { // System.out.println("label"+v.label);
					 * if(path2.contains(v.label)) {
					 * 
					 * // System.out.println("c"); pathTopo2.add(v.label); } }
					 * }else if(flag==2){
					 * 
					 * for(Vertex v:topoList2) {
					 * 
					 * if(path2.contains(v.label)) { pathTopo2.add(v.label); }
					 * 
					 * }
					 * 
					 * 
					 * }
					 */
					// pathTopo2.add(sames.get(j).label);
					path2.add(sames.get(j).label);
					// System.out.println("path1"+path1+"path2"+path2+sames.get(i).id+" "+sames.get(j).id);
					// System.out.println("patht1"+pathTopo1+"patht2"+pathTopo2+sames.get(i).label+" "+sames.get(j).label);
					System.out.println("path1" + path1 + "path2" + path2
							+ sames.get(i).label + " " + sames.get(j).label);
					String same = null;
					for (int p = path1.size() - 1; p >= 0; p--) {
						// for (int p = pathTopo1.size() - 1; p >= 0; p--) {
						boolean flag2 = false;
						for (int q = path2.size() - 1; q >= 0; q--) {
							// for (int q = pathTopo2.size() - 1; q >= 0; q--) {

							if (path1.get(p).equals(path2.get(q))) {
								// if
								// (pathTopo1.get(p).equals(pathTopo2.get(q))) {

								same = path1.get(p);
								// same = pathTopo1.get(p);
								// System.out.println("pathtopo1"+pathTopo1.get(p));
								flag2 = true;
								break;
							}

						}
						if (flag2) {
							break;
						}
					}
					System.out.println("same:" + same);
					// System.out.println("samegeti:"+sames.get(i).label);

					if (same.equals(sames.get(i).label)) {

						m.matrix[i][j] = "1";
						m.matrix[j][i] = "0";
						System.out.println(same + "same" + sames.get(i).label
								+ " " + sames.get(j).label + "1");

					} else if (same.equals(sames.get(j).label)) {
						m.matrix[j][i] = "1";
						m.matrix[i][j] = "0";
						System.out.println(same + "same" + sames.get(i).label
								+ " " + sames.get(j).label + "0");

					} else {

						for (GraphNode node : nodes) {
							if (node.label.equals(same)) {
								// System.out.println(same+"same"+sames.get(i).id+" "+sames.get(j).id+"else");
								if (node.type
										.equals("org.jbpm.workflow.core.node.Split")) {
									m.matrix[i][j] = "-";
									m.matrix[j][i] = "-";
									System.out.println(same + "same"
											+ sames.get(i).label + " "
											+ sames.get(j).label + "-");
								} else if (node.type
										.equals("org.jbpm.workflow.core.node.Join")) {
									System.out.println(same + "same"
											+ sames.get(i).label + " "
											+ sames.get(j).label + "and");
									m.matrix[i][j] = "*";
									m.matrix[j][i] = "*";
								} else {
									System.out.println(same + "same"
											+ sames.get(i).label + " "
											+ sames.get(j).label + "?");
									m.matrix[i][j] = "?";
									m.matrix[j][i] = "?";
								}
							}
						}

					}

				}
			}
		}

		return m;

	}

	public static ArrayList<String> delete() {
		ArrayList<String> del = new ArrayList<String>();

		ArrayList<String> dif = diff(labels1, labels2);
		for (String st : dif) {
			String t = "";
			t = t + "delete(S," + st + ")";
			del.add(t);
		}
		System.out.println(dif);
		return del;

	}

	public static ArrayList<String> insert() {
		ArrayList<String> ins = new ArrayList<String>();

		ArrayList<String> dif = diff(labels2, labels1);
		for (String s : dif) {
			for (GraphNode n : nodes2) {
				if (s.equals(n.label)) {
					String t = "";
					if (n.getParent() != null) {
						if (n.getParent().size() == 1) {
							t = "insert(S," + n.label + ","
									+ n.getParent().get(0).label + ",";

						} else if (n.getParent().size() >= 1) {

							String temp = "";
							// System.out.println("lenth"+temp.length());
							for (GraphNode nodep : n.getParent()) {
								if (temp.length() == 0) {
									System.out.println("hi");
									temp = temp + nodep.label;
								} else {
									temp = temp + "," + nodep.label;
								}

							}

							t = "insert(S," + n.label + "," + "{" + temp + "}"
									+ ",";
						}

					} else {
						t = "insert(S," + n.label + "," + "startNode" + ",";
					}
					if (n.getChildren() != null) {
						if (n.getChildren().size() == 1) {
							t = t + n.getChildren().get(0).label + ")";

						} else if (n.getChildren().size() >= 1) {

							String temp = "";
							for (GraphNode nodec : n.getChildren()) {
								// temp=temp+","+nodec.id;
								if (temp.length() == 0) {
									// System.out.println("hi");
									temp = temp + nodec.label;
								} else {
									temp = temp + "," + nodec.label;
								}
							}

							t = t + "{" + temp + "}" + ")";
						}
					} else {
						t = t + "endNode" + ")";
					}

					ins.add(t);

				}
			}
		}

		System.out.println(dif);
		return ins;

	}

	public static List<String> findMoveItem(List<String> output, Matrix t) {
		// List<String> item=new ArrayList<String>();
		String mins = "";
		int min = Integer.MAX_VALUE;
		for (String temp : output) {
			int count = 0;
			if (temp.length() > 0) {
				for (int i = 0; i < temp.length(); i++) {

					if (temp.charAt(i) == 1) {
						count++;
					}
				}
				if (count < min) {
					min = count;
					mins = temp;
				}
			}
		}
		if (mins.length() > 0) {
			for (int i = 0; i < mins.length(); i++) {
				if (mins.charAt(i) == '1') {

					moveItem.add(t.getNames().get(i));
				}
			}
		} else {

		}
		System.out.println("mins" + mins);

		return moveItem;
	}

	public static ArrayList<String> move()

	{
		ArrayList<String> moves = new ArrayList<String>();

		for (String s : moveItem) {
			for (GraphNode n : nodes2) {
				if (s.equals(n.label)) {
					String t = "";
					if (n.getParent() != null) {
						if (n.getParent().size() == 1) {
							t = "move(S," + n.label + ","
									+ n.getParent().get(0).label + ",";

						} else if (n.getParent().size() >= 1) {

							String temp = "";
							// System.out.println("lenth"+temp.length());
							for (GraphNode nodep : n.getParent()) {
								if (temp.length() == 0) {
									System.out.println("hi");
									temp = temp + nodep.label;
								} else {
									temp = temp + "," + nodep.label;
								}

							}

							t = "move(S," + n.label + "," + "{" + temp + "}"
									+ ",";
						}

					} else {
						t = "move(S," + n.label + "," + "startNode" + ",";
					}
					if (n.getChildren() != null) {
						if (n.getChildren().size() == 1) {
							t = t + n.getChildren().get(0).label + ")";

						} else if (n.getChildren().size() >= 1) {

							String temp = "";
							for (GraphNode nodec : n.getChildren()) {
								// temp=temp+","+nodec.id;
								if (temp.length() == 0) {
									// System.out.println("hi");
									temp = temp + nodec.label;
								} else {
									temp = temp + "," + nodec.label;
								}
							}

							t = t + "{" + temp + "}" + ")";
						}
					} else {
						t = t + "endNode" + ")";
					}

					moves.add(t);

				}
			}
		}

		// System.out.println(dif);
		return moves;

		// return null;

	}

	public static ArrayList<String> diff(ArrayList<String> ls,
			ArrayList<String> ls2) {
		ArrayList<String> list = new ArrayList<String>();
		list.addAll(ls);
		list.removeAll(ls2);
		return list;
	}

}
