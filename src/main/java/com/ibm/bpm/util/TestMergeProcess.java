package com.ibm.bpm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.ibm.bpm.model.Process;

public class TestMergeProcess {

	public static void main(String[] args) {
		// TestMergeProcess.mergeProcessOrderByName();
		TestMergeProcess.mergeProcessOrderByNewickTree();
	}

	private static void mergeProcessOrderByNewickTree() {
		try {
			String pathResource = "E:/work/Clients/CMCC/test/wangwj/source/";
			String pathResult = pathResource
					+ "/result/merged process order by newick tree.bpmn";
			File dirResult = new File(pathResource + "/result");
			if (!dirResult.exists()) {
				dirResult.mkdir();
			}
			File dir = new File(pathResource);
			File[] files = dir.listFiles();
			if (files.length <= 1) {
				System.out.println("Please merge at least two files!");
				return;
			}
			List<String> fileNames = new ArrayList<String>();
			// int totalActivityNumber = 0;
			// int totalGatewayNumber = 0;
			BPMNModel m = new BPMNModel();
			m.readSemanticTable();
			for (File file : files) {
				if (file.isFile()) {
					String filePath = file.getPath().replace("\\", "/");
					System.out.println(filePath);
					fileNames.add(filePath);
				}
			}
			Process p1 = m.getProcess(pathResource
					+ "Hainan Co.Send Document (private company).bpmn");
			Process p2 = m.getProcess(pathResource
					+ "Hainan Co.Send Document (listed company)New.bpmn");
			Process prc1 = m.mergeTwoProcesses(p1, p2);

			Process p3 = m.getProcess(pathResource
					+ "Ningxia Department Send Document.bpmn");
			Process p4 = m.getProcess(pathResource
					+ "Ningxia Co.Send Document.bpmn");
			Process prc2 = m.mergeTwoProcesses(p3, p4);

			Process p5 = m.getProcess(pathResource
					+ "Xizang DGA.Send Document.bpmn");
			Process p6 = m.getProcess(pathResource
					+ "Xizang Department Send Document.bpmn");
			Process prc3 = m.mergeTwoProcesses(p5, p6);

			Process p7 = m.getProcess(pathResource
					+ "Xizang New Department Send Document.bpmn");
			Process p8 = m.getProcess(pathResource
					+ "Xizang Branch Send Document.bpmn");
			Process prc4 = m.mergeTwoProcesses(p7, p8);

			Process p9 = m.getProcess(pathResource
					+ "Hainan Co. and Labour Union Joint Send Document.bpmn");
			Process prc5 = m.mergeTwoProcesses(prc1, p9);

			Process p10 = m.getProcess(pathResource
					+ "Xizang Party Group Send Document.bpmn");
			Process p11 = m.getProcess(pathResource
					+ "Xizang Co.Send Document.bpmn");
			Process prc6 = m.mergeTwoProcesses(p10, p11);

			Process prc7 = m.mergeTwoProcesses(prc4, prc3);

			Process prc8 = m.mergeTwoProcesses(prc7, prc6);

			Process prc9 = m.mergeTwoProcesses(prc8, prc5);

			Process prc10 = m.mergeTwoProcesses(prc9, prc2);

			Process p12 = m.getProcess(pathResource
					+ "Hainan Co. Labour Union Send Document.bpmn");
			Process prc11 = m.mergeTwoProcesses(prc10, p12);

			m.exportProcessToBPMN(prc11, fileNames, pathResult);
			// System.out.println("Before merge, all activity number = " +
			// totalActivityNumber);
			// System.out.println("Before merge, all gateway number = " +
			// totalGatewayNumber);
			System.out.println("After merge, activity number = "
					+ prc11.getActivityList().size());
			System.out.println("After merge, gateway number = "
					+ prc11.getGatewayList().size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void mergeProcessOrderByName() {
		try {
			String pathResource = "E:/work/Clients/CMCC/test/wangwj/mergeProcess/";
			String pathResult = pathResource
					+ "/result/merged process order by name.bpmn";
			File dirResult = new File(pathResource + "/result");
			if (!dirResult.exists()) {
				dirResult.mkdir();
			}
			File dir = new File(pathResource);
			File[] files = dir.listFiles();
			if (files.length <= 1) {
				System.out.println("Please merge at least two files!");
				return;
			}
			List<Process> processes = new ArrayList<Process>();
			List<String> fileNames = new ArrayList<String>();
			int totalActivityNumber = 0;
			int totalGatewayNumber = 0;
			BPMNModel m = new BPMNModel();
			m.readSemanticTable();
			for (File file : files) {
				if (file.isFile()) {
					String filePath = file.getPath().replace("\\", "/");
					System.out.println(filePath);
					Process p = m.getProcess(filePath);
					processes.add(p);
					fileNames.add(filePath);
					totalActivityNumber += p.getActivityList().size();
					totalGatewayNumber += p.getGatewayList().size();
				}
			}
			Process proResult = m.mergeProcess(processes);
			m.exportProcessToBPMN(proResult, fileNames, pathResult);
			System.out.println("Before merge, all activity number = "
					+ totalActivityNumber);
			System.out.println("Before merge, all gateway number = "
					+ totalGatewayNumber);
			System.out.println("After merge, activity number = "
					+ proResult.getActivityList().size());
			System.out.println("After merge, gateway number = "
					+ proResult.getGatewayList().size());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
