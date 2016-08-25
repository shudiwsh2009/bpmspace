package cn.edu.thss.iise.bpmdemo.analysis.core.fragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import nl.tue.tm.is.epc.EPC;

import org.jbpm.ruleflow.core.RuleFlowProcess;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;
import ee.ut.graph.EPCHelper;
import ee.ut.graph.test.RPSDagTest;

public class FragmentUtilOld {

	public static void fragment() {

		String folderPath = "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\work\\thss-bpmn-project\\THSS JBPM\\org.drools.eclipse\\models\\a-epc";
		File folder = new File(folderPath);
		File[] files = folder.listFiles();
		ArrayList<EPC> inputEPC = new ArrayList<EPC>();
		for (File file : files) {
			EPC epc = EPC.loadEPML(file.getAbsolutePath());
			inputEPC.add(epc);
		}
		// RPSDagTest.doxx(inputEPC);
	}

	public static void fragment(String inputFilePath, String outputFilePath) {
		File inputFolder = new File(inputFilePath);
		File[] files = inputFolder.listFiles();
		ArrayList<RuleFlowProcess> inputProcesses = new ArrayList<RuleFlowProcess>();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getAbsolutePath().endsWith(".bpmn"))
				continue;
			RuleFlowProcess flowProcess;
			try {
				flowProcess = (RuleFlowProcess) DataUtil.importFromXmlFile(
						files[i].getAbsolutePath()).get(0);
				inputProcesses.add(flowProcess);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		ArrayList<EPC> inputEPCs = new ArrayList<EPC>();

		int count1 = 0;
		ArrayList<String> fileNames = new ArrayList<String>();
		for (RuleFlowProcess inputProcess : inputProcesses) {

			EPC epc;
			epc = DataUtil.convertBPMNProcesstoEPC(inputProcess);
			String fileName = "D:\\" + String.valueOf(count1) + ".epml";
			count1++;
			EPCHelper.writeModel(fileName, epc);
			fileNames.add(fileName);
			inputEPCs.add(epc);
		}

		List<EPC> outputEPCs;
		outputEPCs = RPSDagTest.doxx(inputEPCs, fileNames);
		int count = 0;
		for (EPC epc : outputEPCs) {
			RuleFlowProcess flowProcess = DataUtil.convertEPCtoBPMNProcess(epc);
			String processContent = DataUtil
					.convertBPMNProcessToXmlString(flowProcess);
			FileWriter fw;
			try {
				fw = new FileWriter(outputFilePath + "\\" + count + ".bpmn");
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(processContent);
				bw.close();
				count++;
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

	public static void main(String args[]) {
		String filePath1 = "C:\\Users\\chenhz\\Desktop\\bpmn文件reduced";
		String filePath2 = "C:\\Users\\chenhz\\Desktop\\bpmn文件reduced\\1";
		fragment(filePath1, filePath2);
		// fragment();
	}

}
