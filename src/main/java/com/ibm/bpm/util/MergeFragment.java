package com.ibm.bpm.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Element;

import com.ibm.bpm.analyzer.Calculation;
import com.ibm.bpm.analyzer.GenerateNewick;
import com.ibm.bpm.model.Newickformat;
import com.ibm.bpm.model.OWLClass;
import com.ibm.bpm.model.Process;
import com.ibm.bpm.model.Property;
import com.ibm.bpm.model.Segment;

public class MergeFragment {

	public static String findMaxResult;
	public static String processLeft;
	public static String processRight;
	public static int intNum = 1;

	public static void main(String[] args) {
		try {
			String[] para = new String[] { "d:/test",
					"S1 Department review and approve" };
			// extract(para);
			mergeFragment(para);
			// extractAndMerge(para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void extract(String[] args) throws Exception {
		try {
			String workSpace = args[0];// "D:/test";
			String fragmentName = args[1];// "S1_Department review and approve";
			String fragmentOwlPath = workSpace + "/config/Fragment.owl";
			String fragmentPath = workSpace + "/" + fragmentName;
			File fragmentFolder = new File(fragmentPath);
			if (!fragmentFolder.exists()) {
				fragmentFolder.mkdir();
			}
			String resultPath = workSpace + "/result/";
			File resultFolder = new File(resultPath);
			if (!resultFolder.exists()) {
				resultFolder.mkdir();
			}
			File sourceFolder = new File(workSpace + "/source");
			BPMNModel m = new BPMNModel();
			m.readSemanticTableOwl(workSpace + "/config/semanticTable.owl");
			List<String> listFileNames = new ArrayList<String>();
			Map<String, Process> processMap = new HashMap<String, Process>();

			List<String> activitiesOfSegment = new ArrayList<String>();
			OWLModel model = new OWLModel();
			Element element = model.getElement(fragmentOwlPath);
			List<OWLClass> listSegmentOwl = model.getOWLList(element);
			for (OWLClass owl : listSegmentOwl) {
				if (owl.getRdf().replace("_", " ").equals(fragmentName)) {
					for (Property property : owl.getProp()) {
						if (property != null
								&& property.getValuesFrom_value() != null) {
							activitiesOfSegment.add(property
									.getValuesFrom_value().replace("_", " "));
						}
					}
					break;
				}
			}

			File[] files = sourceFolder.listFiles();
			// if (files.length <= 1) {
			// System.out.println("Please merge at least two processes!");
			// return;
			// }
			for (File file : files) {
				if (file.isFile() && file.getName().contains(".bpmn")) {
					String filePath = file.getPath().replace("\\", "/");
					String inFileName = workSpace + "/source/" + file.getName();
					String outFileName = fragmentPath + "/" + file.getName();
					String owlFileName = fragmentPath + "/"
							+ file.getName().replace(".bpmn", ".owl");

					listFileNames.add(outFileName);
					Process p = m.getProcess(inFileName);
					Segment post_p = m.extractSegment(p, activitiesOfSegment);
					// Segment post_p = m.processProcess(p, fragmentName);

					String strProcessName = file.getName().replace(".bpmn", "");
					strProcessName = strProcessName.replace("(", "_");
					strProcessName = strProcessName.replace(")", "_");
					processMap.put(strProcessName, post_p);

					// m.exportProcess(inFileName, outFileName, post_p);
					FileUtil.copyFile(workSpace + "/a.dtd", fragmentPath
							+ "/a.dtd");
					m.exportFragment(inFileName, outFileName, owlFileName,
							post_p);
				}
			}

			System.out.println("Extract finished!");
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static void extractAndMerge(String[] args) throws Exception {
		try {
			String workSpace = args[0];// "D:/test";
			String fragmentName = args[1];// "S1_Department review and approve";
			String fragmentOwlPath = workSpace + "/config/Fragment.owl";
			String fragmentPath = workSpace + "/" + fragmentName;
			File fragmentFolder = new File(fragmentPath);
			if (!fragmentFolder.exists()) {
				fragmentFolder.mkdir();
			}
			String resultPath = workSpace + "/result/";
			File resultFolder = new File(resultPath);
			if (!resultFolder.exists()) {
				resultFolder.mkdir();
			}
			File sourceFolder = new File(workSpace + "/source");
			BPMNModel m = new BPMNModel();
			m.readSemanticTableOwl(workSpace + "/config/semanticTable.owl");
			List<String> listFileNames = new ArrayList<String>();
			Map<String, Process> processMap = new HashMap<String, Process>();

			List<String> activitiesOfSegment = new ArrayList<String>();
			OWLModel model = new OWLModel();
			Element element = model.getElement(fragmentOwlPath);
			List<OWLClass> listSegmentOwl = model.getOWLList(element);
			for (OWLClass owl : listSegmentOwl) {
				if (owl.getRdf().replace("_", " ").equals(fragmentName)) {
					for (Property property : owl.getProp()) {
						if (property != null
								&& property.getValuesFrom_value() != null) {
							activitiesOfSegment.add(property
									.getValuesFrom_value().replace("_", " "));
						}
					}
					break;
				}
			}

			File[] files = sourceFolder.listFiles();
			if (files.length <= 1) {
				System.out.println("Please merge at least two processes!");
				return;
			}
			List<Process> listProcess = new ArrayList<Process>();
			for (File file : files) {
				if (file.isFile()) {
					// String filePath = file.getPath().replace("\\", "/");
					String inFileName = workSpace + "/source/" + file.getName();
					String outFileName = fragmentPath + "/" + file.getName();

					listFileNames.add(outFileName);
					Process p = m.getProcess(inFileName);
					Segment post_p = m.extractSegment(p, activitiesOfSegment);
					// Segment post_p = m.processProcess(p, fragmentName);

					String strProcessName = file.getName().replace(".bpmn", "");
					strProcessName = strProcessName.replace("(", "_");
					strProcessName = strProcessName.replace(")", "_");
					processMap.put(strProcessName, post_p);
					listProcess.add(post_p);

					m.exportProcess(inFileName, outFileName, post_p);
				}
			}
			Process mergeResult = null;
			if (fragmentName.equals("S1 Department review and approve")) {
				String[][] results = Calculation.Compare(fragmentPath);
				String excelFilePath = resultPath + fragmentName + ".xls";
				Calculation.writeExl(results, excelFilePath);

				GenerateNewick.generic(excelFilePath, resultPath
						+ "newick.tree");

				List list = GenerateNewick.readExcel(excelFilePath);

				while (list.size() > 2) {
					list = findMax(list, resultPath);
					mergeResult = m.mergeTwoProcesses(
							processMap.remove(processLeft),
							processMap.remove(processRight));
					processMap.put(findMaxResult, mergeResult);
				}
			} else {
				mergeResult = m.mergeProcess(listProcess);
			}
			// System.out.println("activity number = " +
			// mergeResult.getActivityList().size());
			// System.out.println("gateway number = " +
			// mergeResult.getGatewayList().size());

			// Process processResult = m.mergeProcess(listSegments);
			if (mergeResult != null) {
				String destSegmentFileName = resultPath + fragmentName
						+ ".bpmn";
				// m.exportProcessToBPMN(mergeResult, listFileNames,
				// destSegmentFileName);
				String owlFile = resultPath + fragmentName + ".owl";
				FileUtil.copyFile(workSpace + "/a.dtd", resultPath + "/a.dtd");
				m.exportMergedFragmentToBPMN(mergeResult, listFileNames,
						destSegmentFileName, owlFile);
			}

			System.out.println("Extact and Merge fragment finished!");
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static void mergeFragment(String[] args) throws Exception {
		try {
			String workSpace = args[0];// "D:/test/S1 Department review and approve";
			String fragmentName = args[1];// "S1 Department review and approve";

			String resultPath = workSpace + "/result/";
			File resultFolder = new File(resultPath);
			if (!resultFolder.exists()) {
				resultFolder.mkdir();
			}
			String fragmentPath = workSpace + "/" + fragmentName;
			File fragmentFolder = new File(fragmentPath);
			BPMNModel m = new BPMNModel();
			m.readSemanticTableOwl(workSpace + "/config/semanticTable.owl");
			List<String> listFileNames = new ArrayList<String>();
			Map<String, Process> processMap = new HashMap<String, Process>();

			File[] files = fragmentFolder.listFiles();
			String[] fragmentNames = fragmentFolder
					.list(new FilenameFilterBpmn());
			if (fragmentNames.length <= 1) {
				System.out.println("Please merge at least two fragments!");
				return;
			}
			List<Process> listProcess = new ArrayList<Process>();
			for (File file : files) {
				if (file.isFile()
						&& file.getName().toLowerCase().endsWith(".bpmn")) {
					// String filePath = file.getPath().replace("\\", "/");
					String fragmentFilePath = fragmentPath + "/"
							+ file.getName();
					String owlFilePath = fragmentPath + "/"
							+ file.getName().replace(".bpmn", ".owl");

					listFileNames.add(fragmentFilePath);
					Segment seg = m.getFragmentWithOwl(fragmentFilePath,
							owlFilePath);

					String strProcessName = file.getName().replace(".bpmn", "");
					strProcessName = strProcessName.replace("(", "_");
					strProcessName = strProcessName.replace(")", "_");
					processMap.put(strProcessName, seg);
					listProcess.add(seg);
				}
			}
			Process mergeResult = null;
			if (fragmentName.equals("S1 Department review and approve")) {
				String[][] results = Calculation.Compare(fragmentPath);
				String excelFilePath = resultPath + fragmentName + ".xls";
				Calculation.writeExl(results, excelFilePath);

				GenerateNewick.generic(excelFilePath, resultPath
						+ "newick.tree");

				List list = GenerateNewick.readExcel(excelFilePath);

				while (list.size() > 2) {
					list = findMax(list, resultPath);
					mergeResult = m.mergeTwoProcesses(
							processMap.remove(processLeft),
							processMap.remove(processRight));
					processMap.put(findMaxResult, mergeResult);
				}
			} else {
				mergeResult = m.mergeProcess(listProcess);
			}

			if (mergeResult != null) {
				String destSegmentFileName = resultPath + fragmentName
						+ ".bpmn";
				// m.exportProcessToBPMN(mergeResult, listFileNames,
				// destSegmentFileName);
				String owlFile = resultPath + fragmentName + ".owl";
				FileUtil.copyFile(workSpace + "/a.dtd", resultPath + "/a.dtd");
				m.exportMergedFragmentToBPMN(mergeResult, listFileNames,
						destSegmentFileName, owlFile);
			}

			System.out.println("Merge fragment finished!");
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static void extractAndMerge1(String[] args) throws Exception {
		try {
			String workSpace = args[0];// "D:/test";
			String fragmentName = args[1];// "S1_Department review and approve";
			// String fragmentOwlPath = workSpace + "/config/Fragment.owl";
			String fragmentPath = workSpace + "/" + fragmentName;
			File fragmentFolder = new File(fragmentPath);
			if (!fragmentFolder.exists()) {
				fragmentFolder.mkdir();
			}
			String resultPath = workSpace + "/result/";
			File resultFolder = new File(resultPath);
			if (!resultFolder.exists()) {
				resultFolder.mkdir();
			}
			File sourceFolder = new File(workSpace + "/source");
			BPMNModel m = new BPMNModel();
			m.readSemanticTableOwl(workSpace + "/config/semanticTable.owl");
			List<String> listFileNames = new ArrayList<String>();
			// Map<String, Process> processMap = new HashMap<String, Process>();

			List<String> activitiesOfSegment = new ArrayList<String>();
			// OWLModel model = new OWLModel();
			// Element element = model.getElement(fragmentOwlPath);
			// List<OWLClass> listSegmentOwl = model.getOWLList(element);
			// for (OWLClass owl : listSegmentOwl) {
			// if (owl.getRdf().replace("_", " ").equals(fragmentName)) {
			// for (Property property : owl.getProp()) {
			// if (property != null && property.getValuesFrom_value() != null) {
			// activitiesOfSegment.add(property.getValuesFrom_value().replace("_",
			// " "));
			// }
			// }
			// break;
			// }
			// }
			String fragmentConfigPath = workSpace + "/config/" + fragmentName
					+ ".txt";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(fragmentConfigPath)));
			String str;
			while ((str = br.readLine()) != null) {
				activitiesOfSegment.add(str);
			}

			File[] files = sourceFolder.listFiles();
			if (files.length <= 1) {
				System.out.println("Please merge at least two processes!");
				return;
			}
			List<Process> listSegments = new ArrayList<Process>();
			for (File file : files) {
				if (file.isFile()) {
					// String filePath = file.getPath().replace("\\", "/");
					String inFileName = workSpace + "/source/" + file.getName();
					String outFileName = fragmentPath + "/" + file.getName();

					listFileNames.add(outFileName);
					Process p = m.getProcess(inFileName);
					Segment post_p = m.extractSegment(p, activitiesOfSegment);
					// Segment post_p = m.processProcess(p, fragmentName);
					listSegments.add(post_p);

					// String strProcessName = file.getName().replace(".bpmn",
					// "");
					// strProcessName = strProcessName.replace("(", "_");
					// strProcessName = strProcessName.replace(")", "_");
					// processMap.put(strProcessName, post_p);

					m.exportProcess(inFileName, outFileName, post_p);
				}
			}

			Process mergeResult = m.mergeProcess(listSegments);
			System.out.println("activity number = "
					+ mergeResult.getActivityList().size());
			System.out.println("gateway number = "
					+ mergeResult.getGatewayList().size());

			// Process processResult = m.mergeProcess(listSegments);
			if (mergeResult != null) {
				String destSegmentFileName = resultPath + fragmentName
						+ ".bpmn";
				m.exportProcessToBPMN(mergeResult, listFileNames,
						destSegmentFileName);
			}

			System.out.println("Extact and Merge fragment finished!");
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static List findMax(List li, String excelPath) {

		int size = li.size();
		double max = 0;
		int maxi = 0;
		int maxj = 0;
		for (int i = 1; i < size; i++) {
			String[] argsx = (String[]) li.get(i);
			int len = argsx.length;

			// System.out.print("lines: " + i + " ");
			for (int j = 1; j < len; j++) {
				double value = (new Double(argsx[j])).doubleValue();
				if (value > max && i != j) // value < 1)
				{
					max = value;
					maxi = i;
					maxj = j;
				}
			}

		}
		/*
		 * Cell cell = sheet.getCell(0, maxj); String left =
		 * (String[])li.get[0]); cell = sheet.getCell(maxi, 0); String right =
		 * cell.getContents();
		 */

		String[] argsx = (String[]) li.get(0);
		String left = argsx[maxj];

		String[] argsx2 = (String[]) li.get(maxi);
		String right = argsx2[0];

		// //System.out.println("Find left and right: " + left + "  --->  " +
		// right);
		processLeft = left;
		processRight = right;

		Newickformat nf = new Newickformat(left, max, right);
		String result = nf.getResult();
		findMaxResult = result;

		// //System.out.println("find max result: " + result);
		// result2 = result;

		// merge to the same one;

		List list = GenerateNewick.merge(li, maxi, maxj, result);
		// GenerateNewick.wirteExl(list, excelPath + "/" + intNum + ".xls");
		intNum++;
		return list;
	}
}
