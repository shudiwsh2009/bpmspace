package com.ibm.bpm.util;

import com.ibm.bpm.analyzer.Calculation;

public class CompareProcess {

	public static void main(String[] args) {
		compare(args);
	}

	public static void compare(String[] args) {
		String processPath = args[0];
		String[][] results = Calculation.Compare(processPath);
		String excelFilePath = processPath + "/compare.xls";
		Calculation.writeExl(results, excelFilePath);
		System.out.println("Compare finished!");
	}
}
