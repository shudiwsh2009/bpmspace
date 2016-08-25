package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.deckfour.spex.SXDocument;
import org.deckfour.spex.SXTag;

public class ConvertToMXML {

	/**
	 * ���ϲ������־����д�뵽MXML
	 * 
	 * @param process
	 * @throws Exception
	 */
	public void convertToMxml(Process process, String outFilePath)
			throws Exception {
		if (process == null) {
			System.out.print("no process");
		}
		File file = new File(outFilePath);
		OutputStream oStream = new BufferedOutputStream(new FileOutputStream(
				file));
		SXDocument doc = new SXDocument(oStream);
		doc.addComment("MXML version 1.0");
		doc.addComment("Created by Hongfei Ge");
		SXTag root = doc.addNode("WorkflowLog");
		SXTag source = root.addChildNode("Source");
		source.addAttribute("program", "MXML");
		SXTag sprocess = root.addChildNode("Process");
		String id = process.getID();
		sprocess.addAttribute("id", (id == null ? "none" : id));
		sprocess.addAttribute("description", process.getDescription());

		for (ProcessInstance processInstance : process.getProcessInstances()) {
			SXTag instance = sprocess.addChildNode("ProcessInstance");
			instance.addAttribute("id", processInstance.getID());
			for (AuditTrailEntry auditTrailEntry : processInstance
					.getAuditTrailEntries()) {
				SXTag ate = instance.addChildNode("AuditTrailEntry");
				SXTag wfme = ate.addChildNode("WorkflowModelElement");
				wfme.addTextNode(auditTrailEntry.getWorkflowModelElement());
				SXTag type = ate.addChildNode("EventType");
				type.addTextNode(auditTrailEntry.getEventType());
				SXTag timestamp = ate.addChildNode("timestamp");
				timestamp.addTextNode(auditTrailEntry.getTimestamp());
			}
		}
		doc.close();
	}
}
