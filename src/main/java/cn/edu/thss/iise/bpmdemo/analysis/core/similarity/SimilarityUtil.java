package cn.edu.thss.iise.bpmdemo.analysis.core.similarity;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.jbpm.ruleflow.core.RuleFlowProcess;
import org.kie.api.definition.process.Process;

import cn.edu.thss.iise.bpmdemo.analysis.core.util.DataUtil;

import com.ibm.bpm.analyzer.Calculation;

public class SimilarityUtil {
	public static double similarity(String inputFolderString) {
		double result;
		File folder = new File(inputFolderString);
		File[] files = folder.listFiles();
		if (files.length != 2)
			return -1;
		result = similarity(files[0].getAbsolutePath(),
				files[1].getAbsolutePath());
		return result;
	}

	public static double similarity(String filePath1, String filePath2) {
		double result = 0;
		try {
			List<Process> processes1 = DataUtil.importFromXmlFile(filePath1);
			List<Process> processes2 = DataUtil.importFromXmlFile(filePath2);
			RuleFlowProcess process1 = (RuleFlowProcess) processes1.get(0);
			process1 = DataUtil.deMultiInstance(process1);
			RuleFlowProcess process2 = (RuleFlowProcess) processes2.get(0);
			process2 = DataUtil.deMultiInstance(process2);
			com.ibm.bpm.model.Process p1, p2;
			p1 = DataUtil.convertProcesstoIBMProcess(process1);
			p2 = DataUtil.convertProcesstoIBMProcess(process2);
			result = Calculation.compareProcess(p1, p2);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String agrs[]) {
		String filePath1 = "G:\\Graduate\\Projects\\2013-7~8BPM Keynote Demo\\Model\\bpmn文件\\bpmn文件81";
		File folder = new File(filePath1);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++)
			for (int j = 0; j < files.length; j++) {
				System.out.println(files[j].getName());
				if (!files[j].getName().endsWith("bpmn"))
					continue;
				double d = similarity(files[i].getAbsolutePath(),
						files[j].getAbsolutePath());
				System.out.println(d);
			}
	}
}
