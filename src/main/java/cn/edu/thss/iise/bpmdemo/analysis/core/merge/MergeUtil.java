package cn.edu.thss.iise.bpmdemo.analysis.core.merge;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jbpm.ruleflow.core.RuleFlowProcess;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;

import com.chinamobile.bpmspace.core.util.FileUtil;
import com.ibm.bpm.util.MergeProcess;

public class MergeUtil {

	private static ArrayList<com.ibm.bpm.model.Process> readHaifa(
			String filePath) throws IOException {
		// 1.转换JBPMtoHaifa
		File folder = new File(filePath);
		File[] files = folder.listFiles();
		ArrayList<org.kie.api.definition.process.Process> jbpmModelList = new ArrayList<org.kie.api.definition.process.Process>();
		ArrayList<com.ibm.bpm.model.Process> result = new ArrayList<com.ibm.bpm.model.Process>();
		for (File file : files) {
			if (!file.getAbsolutePath().endsWith(".bpmn"))
				continue;
			List<org.kie.api.definition.process.Process> processes = DataUtil
					.importFromXmlFile(file.getAbsolutePath());
			RuleFlowProcess process = (RuleFlowProcess) processes.get(0);
			process = DataUtil.deMultiInstance(process);
			jbpmModelList.add(process);
		}
		for (org.kie.api.definition.process.Process process : jbpmModelList) {
			com.ibm.bpm.model.Process IBMProcess = DataUtil
					.convertProcesstoIBMProcess((RuleFlowProcess) process);
			result.add(IBMProcess);
		}
		return result;
	}

	public static void outputModel(com.ibm.bpm.model.Process model,
			String outputFilePath) throws IOException {
		RuleFlowProcess resultModel = DataUtil
				.convertIBMProcesstoProcess(model);
		String modelString = DataUtil
				.convertBPMNProcessToXmlString(resultModel);
		FileWriter fw = new FileWriter(outputFilePath);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(modelString);
		bw.close();
	}

	public static void merge(String filePath, String outputFilePath) {
		// 1.转换JBPMtoHaifa
		ArrayList<com.ibm.bpm.model.Process> haifaModel = null;
		try {
			haifaModel = readHaifa(filePath);
			for (int i = 0; i < haifaModel.size(); i++) {
				com.ibm.bpm.model.Process p = haifaModel.get(i);
				// BPMNModel m = new BPMNModel();
			}
			// 2.merge
			com.ibm.bpm.model.Process mergedModel = MergeProcess
					.merge(haifaModel);
			// 3.转换海法toJBPM
			outputModel(mergedModel, outputFilePath);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String agrs[]) {
		FileUtil.WEBAPP_ROOT = "D:\\Workspace\\apache-tomcat-8.0.9\\webapps\\bpmspace\\";
		String filePath = "D:\\Process Data Group\\02.Process Space\\GQL\\Model\\bpmn文件\\bpmn文件81\\fragments";
		String filePath2 = "D:\\Process Data Group\\02.Process Space\\GQL\\Model\\bpmn文件\\bpmn文件81\\1.bpmn";
		merge(filePath, filePath2);
	}

}
