package cn.edu.thss.iise.bpmdemo.analysis.core.fragment;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import nl.tue.tm.is.epc.EPC;

import org.jbpm.ruleflow.core.RuleFlowProcess;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;
import ee.ut.graph.EPCHelper;

public class FragmentUtilNew {

	public static void main(String args[]) {
		String filePath1 = "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件\\bpmn文件81";
		String filePath2 = "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件\\bpmn文件81\\fragments\\";
		fragment(filePath1, filePath2);
	}

	public static void excuteCmd() throws IOException, InterruptedException {
		// System.out.println(System.getProperty("user.dir"));
		String driver = System.getProperty("user.dir").substring(0,
				System.getProperty("user.dir").indexOf(":"));
		System.out.println(driver);
		String cmd1 = "fragment.bat";
		// String cmd = "cmd /c start \""+
		// System.getProperty("user.dir")+"\\bpcd\\run.bat\"";
		// String cmd =
		// "cd \""+System.getProperty("user.dir")+"\\bpcd\"\njava -jar bpcd.jar -folder \\models\\tmpModel\nrun.bat";
		System.out.println(cmd1);
		Process child = Runtime.getRuntime().exec(cmd1);
		InputStream child_in = child.getInputStream();
		InputStreamReader isr = new InputStreamReader(child_in, "GBK");
		int c;
		while ((c = isr.read()) != -1) {
			System.out.print((char) c);
		}
		child_in.close();
		child.waitFor();
	}

	private static ArrayList<RuleFlowProcess> parseResult() throws IOException {
		ArrayList<RuleFlowProcess> result = new ArrayList<RuleFlowProcess>();
		String fragmentPath = System.getProperty("user.dir")
				+ "\\bpcd\\fragments";
		File folder = new File(fragmentPath);
		for (File file : folder.listFiles()) {
			RuleFlowProcess process = DataUtil
					.parseRuleFlowProcessFromDot(file);
			result.add(process);
		}
		return result;
	}

	public static void fragment(String inputFilePath, String outputFilePath) {
		// 1.readfile
		File inputFolder = new File(inputFilePath);
		File[] files = inputFolder.listFiles();
		ArrayList<RuleFlowProcess> inputProcesses = new ArrayList<RuleFlowProcess>();
		for (int i = 0; i < files.length; i++) {
			if (!files[i].getAbsolutePath().endsWith(".bpmn"))
				continue;
			System.out.println(files[i].getName());
			RuleFlowProcess flowProcess;
			try {
				flowProcess = (RuleFlowProcess) DataUtil.importFromXmlFile(
						files[i].getAbsolutePath()).get(0);
				flowProcess = DataUtil.deMultiInstance(flowProcess);
				flowProcess.setName(files[i].getName());
				inputProcesses.add(flowProcess);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		// 1.5 clear fragment
		String fragmentPath = System.getProperty("user.dir")
				+ "\\bpcd\\fragments";
		File folder = new File(fragmentPath);
		for (File file : folder.listFiles()) {
			file.delete();
		}
		// 2.convert
		ArrayList<EPC> inputEPCs = new ArrayList<EPC>();
		int count1 = 0;
		ArrayList<String> fileNames = new ArrayList<String>();
		String folderPath = System.getProperty("user.dir")
				+ "\\bpcd\\models\\tmpModel";
		System.out.println(folderPath);
		folder = new File(folderPath);
		for (File file : folder.listFiles()) {
			file.delete();
		}
		for (RuleFlowProcess inputProcess : inputProcesses) {
			EPC epc;
			epc = DataUtil.convertBPMNProcesstoEPC(inputProcess);
			String fileName = folderPath + "\\"
					+ inputProcess.getName().replace("bpmn", "epml");
			EPCHelper.writeModel(fileName, epc);
		}
		// 3.excute cmd
		try {
			excuteCmd();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		// 4.parseResult
		ArrayList<RuleFlowProcess> result = new ArrayList<RuleFlowProcess>();
		try {
			result = parseResult();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// 5.saveResult
		for (RuleFlowProcess process : result) {
			String path = outputFilePath + "\\" + process.getName() + ".bpmn";
			String processContent = DataUtil
					.convertBPMNProcessToXmlString(process);
			FileWriter fw;
			try {
				fw = new FileWriter(path);
				BufferedWriter bw = new BufferedWriter(fw);
				bw.write(processContent);
				bw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}
