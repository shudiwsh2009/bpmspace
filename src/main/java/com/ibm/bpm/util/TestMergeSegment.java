package com.ibm.bpm.util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ibm.bpm.analyzer.Calculation;
import com.ibm.bpm.analyzer.GenerateNewick;
import com.ibm.bpm.model.Newickformat;
import com.ibm.bpm.model.Process;
import com.ibm.bpm.model.Segment;

public class TestMergeSegment {

	public static String findMaxResult;
	public static String processLeft;
	public static String processRight;
	public static int intNum = 10;

	public static void main(String[] args) {
		try {
			BPMNModel m = new BPMNModel();
			m.readSemanticTable();
			String sourcePath = "E:/work/Clients/CMCC/test/wangwj/source/";
			String outputPath = "E:/work/Clients/CMCC/test/wangwj/result/seg1/";
			String excelPath = "E:/work/Clients/CMCC/test/wangwj/result/xls/";
			BufferedReader br = new BufferedReader(new InputStreamReader(
					new FileInputStream(
							"E:/work/Clients/CMCC/test/wangwj/test0.txt")));
			String str = null;
			List<String> listFileNames = new ArrayList<String>();
			Map<String, Process> processMap = new HashMap<String, Process>();
			while ((str = br.readLine()) != null) {
				String inFileName = sourcePath + str;
				String outFileName = outputPath + str;
				listFileNames.add(outFileName);

				Process p = m.getProcess(inFileName);
				Segment post_p = m.processProcess(p,
						"S1_Department review and approve");

				String strProcessName = str.replace(".bpmn", "");
				strProcessName = strProcessName.replace("(", "_");
				strProcessName = strProcessName.replace(")", "_");
				processMap.put(strProcessName, post_p);

				m.exportProcess(inFileName, outFileName, post_p);
			}

			int i = 1;
			String[][] results = Calculation.Compare(outputPath);
			String excelFilePath = excelPath + i + ".xls";
			Calculation.writeExl(results, excelFilePath);

			List list = GenerateNewick.readExcel(excelFilePath);

			Process mergeResult = null;
			while (list.size() > 2) {
				list = findMax(list);
				mergeResult = m.mergeTwoProcesses(
						processMap.remove(processLeft),
						processMap.remove(processRight));
				processMap.put(findMaxResult, mergeResult);
			}

			m.cutOffBranch(mergeResult, 3);

			// Process processResult = m.mergeProcess(listSegments);
			if (mergeResult != null) {
				String destSegmentFileName = "E:/work/Clients/CMCC/test/wangwj/segment/Frag1_Department review and approve.bpmn";
				m.exportProcessToBPMN(mergeResult, listFileNames,
						destSegmentFileName);
			}

			System.out.println("Finish!");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static List findMax(List li) {

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

		// System.out.println("Find left and right: " + left + "  --->  " +
		// right);
		processLeft = left;
		processRight = right;

		Newickformat nf = new Newickformat(left, max, right);
		String result = nf.getResult();
		findMaxResult = result;

		// System.out.println("find max result: " + result);
		// result2 = result;

		// merge to the same one;

		List list = GenerateNewick.merge(li, maxi, maxj, result);
		GenerateNewick.wirteExl(list,
				"E:/work/Clients/CMCC/test/wangwj/result/xls/" + intNum
						+ ".xls");
		intNum++;
		return list;
	}
}
