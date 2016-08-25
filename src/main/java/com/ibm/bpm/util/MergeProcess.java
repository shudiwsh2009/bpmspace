package com.ibm.bpm.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.chinamobile.bpmspace.core.util.FileUtil;
import com.ibm.bpm.model.Process;

public class MergeProcess {

	private static String dir = "C:\\Users\\chenhz\\Documents\\Thss SVN\\THSS JBPM\\org.drools.eclipse\\";

	public static void main(String[] args) {
		try {
			String[] para = new String[] { "D:/test/process_merge" };
			merge(para);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void merge(String[] args) throws Exception {
		try {
			String pathResource = args[0];
			String workSpace = pathResource + "/..";
			String pathResult = pathResource + "/result/merged process.bpmn";
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
			m.readSemanticTableOwl(workSpace + "/config/semanticTable.owl");
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
			// System.out.println("activity: " +
			// proResult.getActivityList().size());
			// System.out.println("gateway: " +
			// proResult.getGatewayList().size());
			m.exportProcessToBPMN(proResult, fileNames, pathResult);
			System.out.println("Process merged!");
		} catch (Exception ex) {
			throw ex;
		}
	}

	public static Process merge(ArrayList<Process> processes) {

		try {
			BPMNModel m = new BPMNModel();
			m.readSemanticTableOwl(FileUtil.WEBAPP_ROOT
					+ FileUtil.CONFIG_FOLDER + "semanticTable.xml");
			Process proResult = m.mergeProcess(processes);
			return proResult;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
