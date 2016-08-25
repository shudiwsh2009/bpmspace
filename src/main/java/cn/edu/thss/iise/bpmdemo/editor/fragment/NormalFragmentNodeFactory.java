package cn.edu.thss.iise.bpmdemo.editor.fragment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.drools.core.xml.SemanticModules;
import org.drools.eclipse.flow.ruleflow.core.FragmentNodeWrapper;
import org.jbpm.bpmn2.xml.BPMNDISemanticModule;
import org.jbpm.bpmn2.xml.BPMNExtensionsSemanticModule;
import org.jbpm.bpmn2.xml.BPMNSemanticModule;
import org.jbpm.compiler.xml.XmlProcessReader;
import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.kie.api.definition.process.Process;

public class NormalFragmentNodeFactory {

	public static String fragmentPath = "C:\\Users\\chenhz\\Desktop\\fra";
	public static String fragmentStatisticsFile = "C:\\Users\\chenhz\\Desktop\\fra\\fragment.sta";

	public static List<RuleFlowProcess> frgaments = new ArrayList<RuleFlowProcess>();
	public static List<String> fragmentsNames = new ArrayList<String>();

	public NormalFragmentNodeFactory() {
	}

	public static int count = 0;

	public void loadFragments() {
		File inputFolder = new File(fragmentPath);
		File[] inputFiles = inputFolder.listFiles();
		for (File file : inputFiles) {
			try {
				String bpmnFile = file.getAbsolutePath();
				if (file.isFile() && bpmnFile.endsWith(".bpmn")) {
					List<Process> processes = importFromXmlFile(bpmnFile);
					if (processes != null && processes.size() >= 1) {
						RuleFlowProcess fragment = (RuleFlowProcess) processes
								.get(0);
						frgaments.add(fragment);
						fragmentsNames.add(file.getName());
					}
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public static FragmentNodeWrapper createFragmentNode() {
		FragmentNodeWrapper fw = new FragmentNodeWrapper();
		fw.setFragment(frgaments.get(count));
		fw.setFragmentName(fragmentsNames.get(count));
		count++;
		return fw;
	}

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

	public static void doFragmentReuseStatistics(FragmentNodeWrapper fw)
			throws IOException {
		File file = new File(NormalFragmentNodeFactory.fragmentStatisticsFile);
		FileWriter writer = null;
		String ls = System.getProperty("line.separator");
		if (!file.exists()) {
			file.createNewFile();
			writer = new FileWriter(file);
			for (int i = 0; i < NormalFragmentNodeFactory.fragmentsNames.size(); i++) {
				writer.write(NormalFragmentNodeFactory.fragmentsNames.get(i)
						+ ",0" + ls);
			}
			writer.close();
			writer = null;
		}
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String fc = "";
		String tempString;
		while ((tempString = reader.readLine()) != null) {
			if (tempString.startsWith(fw.getFragmentName())) {
				String[] sg = tempString.split(",");
				if (sg.length == 2) {
					int number = Integer.parseInt(sg[1]);
					number++;
					sg[1] = String.valueOf(number);
					tempString = sg[0] + "," + sg[1];
				}
			}
			fc += tempString + ls;
		}
		writer = new FileWriter(file);
		writer.write(fc);
		writer.close();
		reader.close();
	}
}
