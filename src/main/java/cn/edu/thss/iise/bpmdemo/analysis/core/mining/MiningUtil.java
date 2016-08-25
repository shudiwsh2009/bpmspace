package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.jbpm.bpmn2.xml.XmlBPMNProcessDumper;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;

public class MiningUtil {

	/**
	 * @param args
	 * @throws Exception
	 */
	/*
	 * public static void main(String[] args) throws Exception { // TODO
	 * Auto-generated method stub String inputFile = "D://231个部门收文的流程跟踪.xlsx",
	 * mxml = "D://流程跟踪.mxml", pnml = "D://流程跟踪.mxml"; Log log = new
	 * Log(inputFile); PatternRecognition patternRecognition = new
	 * PatternRecognition(); patternRecognition.recognize(log); EventsCombine
	 * eventscombine = new EventsCombine(); Process process =
	 * eventscombine.combine(log); ConvertToMXML ctm = new ConvertToMXML();
	 * ctm.convertToMxml(process, mxml); //AlphaMiner alphaMiner = new
	 * AlphaMiner(mxml, pnml);
	 * 
	 * }
	 */

	public static void fun(String inputFile, String outputFile)
			throws Exception {
		Log log = new Log(inputFile);
		PatternRecognition patternRecognition = new PatternRecognition();
		patternRecognition.recognize(log);
		// for (Trace trace : log.getTraces()) {
		// for (int i = 0; i < trace.getMcTasks().size(); i++) {
		// System.out.println("主控：" + trace.getId()
		// + trace.getMcTasks().get(i));
		// }
		//
		// for (int i = 0; i < trace.getConTasks().size(); i++) {
		// // System.out.println("并发："+trace.getConTasks().get(i));
		// if (trace.getConTasks().get(i).equals("部门领导批示")) {
		// System.out.println("部门领导批示是并发的ID：" + trace.getId());
		// }
		// }
		// for (int i = 0; i < trace.getRecTasks().size(); i++) {
		// // System.out.println("递归："+trace.getRecTasks().get(i));
		// }
		// }

		EventsCombine eventscombine = new EventsCombine();
		Process process = eventscombine.combine(log);
		String mxml = "test.mxml";
		ConvertToMXML ctm = new ConvertToMXML();
		ctm.convertToMxml(process, mxml);
		PetriNet petriNet = null;
		// AlphaMiner alphaMiner = new AlphaMiner(mxml);
		// petriNet = alphaMiner.mine();

		AlphaSharpMiner alphasharp = new AlphaSharpMiner(mxml);
		petriNet = alphasharp.mine();

		RuleFlowProcess result = DataUtil.convertPetriNettoProcessForMining(
				petriNet, log);
		// RuleFlowProcess result =
		// DataUtil.convertPetriNettoProcessForM(petriNet);
		FileWriter fw = new FileWriter(outputFile);
		BufferedWriter bw = new BufferedWriter(fw);
		String buffer = convertBPMNProcessToXmlString(result);
		bw.write(buffer);
		bw.close();
	}

	public static String convertBPMNProcessToXmlString(RuleFlowProcess process) {
		String output = XmlBPMNProcessDumper.INSTANCE.dump(process, true);
		return output;
	}

	public static void mining(String inputFolderString,
			String outputFolderString) {
		File inputFolder, outputFolder;
		inputFolder = new File(inputFolderString);
		outputFolder = new File(outputFolderString);
		File[] inputFiles = inputFolder.listFiles();
		for (File file : inputFiles) {
			try {
				int pos = file.getName().lastIndexOf(".");
				String fileName = file.getName().substring(0, pos);
				fun(file.getAbsolutePath(), outputFolderString + "\\"
						+ fileName + ".bpmn");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	// public static void main(String args[])
	// {
	// String inputFile = "D:\\用印管理.xls";
	// String outputFile = "D:\\用印管理.bpmn";
	// try {
	// fun(inputFile, outputFile);
	// } catch (Exception e) {
	// // TODO Auto-generated catch block
	// e.printStackTrace();
	// }
	// }

	public static void main(String agrs[]) throws Exception {
		String inputFolder = "D:\\tmp2";
		String outputFolder = "D:\\tmp1\\";
		File folder = new File(inputFolder);
		File[] files = folder.listFiles();
		for (File file : files) {
			System.out.println(file.getName());
			String outputFile = outputFolder
					+ file.getName().replace("xls", "bpmn") + ".bpmn";
			fun(file.getAbsolutePath(), outputFile);
		}
	}

}
