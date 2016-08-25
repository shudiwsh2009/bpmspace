package cn.edu.thss.iise.bpmdemo.analysis.core.difference;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.core.xml.SemanticModules;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.jbpm.workflow.core.Node;
import org.kie.api.definition.process.Process;

public class ConvertBPMNXMLToProcess {

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

	// ���demoչʾ��
	// 1.��κʹ�xml�ļ��ж�ȡbpmn2.oģ�ͣ�
	// 2.�������ģ�͵�ֵ��������ֻչʾ�˺��ٵ�һ���֣����������ʹ����������ڵ���״̬�Ȳ鿴process������Щֵ�ͷ����ٷ���
	public static void main(String[] args) throws IOException {
		// ����һ��bpmn�ļ����ļ��пɰ������ģ�ͣ�����ģ���б�
		// List<Process> processes =
		// ConvertBPMNXMLToProcess.importFromXmlFile("C://Users//chenhz//Desktop//����ССѧ��//jbpm//jbpm-6.0.0.Beta2-examples//jbpm-examples//src//main//resources//evaluation//Evaluation.bpmn");
		List<Process> processes = ConvertBPMNXMLToProcess
				.importFromXmlFile("D:/y/Evaluation.bpmn");
		System.out.println("ģ�͵ĸ�����" + processes.size());
		// ��һ��ģ��
		RuleFlowProcess process = (RuleFlowProcess) processes.get(0);
		// ģ�ͱ������Ϣ
		System.out.println("process Id:" + process.getId());
		System.out.println("process Name:" + process.getName());
		System.out.println("process NameSpace:" + process.getNamespace());
		System.out.println("process Package:" + process.getPackageName());
		System.out.println("process Type:" + process.getType());
		System.out.println("process Version:" + process.getVersion());

		// ģ�ͺ��ӽڵ����Ϣ
		org.kie.api.definition.process.Node[] nodes = process.getNodes();
		System.out.println("node number:" + nodes.length);
		int i;
		for (i = 0; i < nodes.length; i++) {
			System.out.println(nodes[i].getClass().getName() + "nodesclass");
			System.out.println(nodes[i].getId() + "nodesid");
			Map<String, List<org.kie.api.definition.process.Connection>> connections = nodes[i]
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
					System.out.println("value");

					System.out.println(c.getFromType());
					System.out.println(c.getToType());
					System.out.println(c.getFrom().getId());
					System.out.println(c.getFrom().getName());
					System.out.println(c.getToType());
					System.out.println(c.getTo().getId());
					System.out.println(c.getTo().getName());
				}

				System.out.println("key" + entry.getKey() + "--->" + "value"
						+ entry.getValue());
			}

			//
			System.out.println("connection number: " + connections.size());

		}
		Node node = (Node) nodes[0];
		System.out.println("node name" + node.getName());
		// ��ȡ���ӵ���Ϣ
		Map<String, List<org.kie.api.definition.process.Connection>> connections = node
				.getIncomingConnections();

		System.out.println("connection number: " + connections.size());
	}

}
