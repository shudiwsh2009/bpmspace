package com.ibm.bpm.analyzer;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.write.WritableSheet;

import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.metric.BTSSimilarity_Wang;
import cn.edu.thss.iise.beehivez.util.BPMNConvertor;

import com.ibm.bpm.model.Activity;
import com.ibm.bpm.model.EndEvent;
import com.ibm.bpm.model.Flow;
import com.ibm.bpm.model.Gateway;
import com.ibm.bpm.model.Process;
import com.ibm.bpm.model.ProcessNode;
import com.ibm.bpm.model.StartEvent;
import com.ibm.bpm.util.BPMNModel;
import com.ibm.bpm.util.FilenameFilterBpmn;

public class Calculation {

	public static ProcessNode getPreActivity(String gId, List<Flow> fList) {
		int size = fList.size();
		for (int i = 0; i < size; i++) {
			Flow f = fList.get(i);
			ProcessNode srcNode = f.getSrcNode();
			ProcessNode tgtNode = f.getTargetNode();
			String srcId = srcNode.getId();
			String tgtId = tgtNode.getId();

			if (gId.equalsIgnoreCase(tgtId)) {
				if (srcNode instanceof Activity
						|| srcNode instanceof StartEvent) {
					return srcNode;
				} else if (srcNode instanceof Gateway) {
					return getPreActivity(srcId, fList);
				}
			}
		}
		return null;
	}

	public static void getSucActivity(String gId, List<Flow> fList,
			List<ProcessNode> list) {
		int size = fList.size();
		for (int i = 0; i < size; i++) {
			Flow f = fList.get(i);
			ProcessNode srcNode = f.getSrcNode();
			ProcessNode tgtNode = f.getTargetNode();
			String srcId = srcNode.getId();
			String tgtId = tgtNode.getId();

			if (gId.equalsIgnoreCase(srcId)) {
				if (tgtNode instanceof Activity || tgtNode instanceof EndEvent) {
					list.add(tgtNode);
				} else if (tgtNode instanceof Gateway) {
					getSucActivity(tgtId, fList, list);
				}
			}
		}
	}

	public static double compareActivity(Activity a1, Activity a2, Process p1,
			Process p2) {

		String a1Id = a1.getId();
		String a2Id = a2.getId();
		List<Flow> fList = p1.getFlowList();
		int size = fList.size();
		List<ProcessNode> preA1List = new ArrayList();
		List<ProcessNode> sucA1List = new ArrayList();
		for (int i = 0; i < size; i++) {
			Flow f = fList.get(i);
			ProcessNode srcNode = f.getSrcNode();
			ProcessNode tgtNode = f.getTargetNode();
			String srcId = srcNode.getId();
			String tgtId = tgtNode.getId();

			if (a1Id.equalsIgnoreCase(tgtId)) {
				if (srcNode instanceof Activity
						|| srcNode instanceof StartEvent) {
					preA1List.add(srcNode);
				} else if (srcNode instanceof Gateway) {
					preA1List.add(getPreActivity(srcId, fList));
				}
			} else if (a1Id.equalsIgnoreCase(srcId)) {
				if (tgtNode instanceof Activity || tgtNode instanceof EndEvent) {
					sucA1List.add(tgtNode);
				} else if (tgtNode instanceof Gateway) {
					getSucActivity(tgtId, fList, sucA1List);
				}
			}
		}

		List<Flow> f2List = p2.getFlowList();
		int size2 = f2List.size();
		List<ProcessNode> preA2List = new ArrayList();
		List<ProcessNode> sucA2List = new ArrayList();
		for (int i = 0; i < size2; i++) {
			Flow f = f2List.get(i);
			ProcessNode srcNode = f.getSrcNode();
			ProcessNode tgtNode = f.getTargetNode();
			String srcId = srcNode.getId();
			String tgtId = tgtNode.getId();

			if (a2Id.equalsIgnoreCase(tgtId)) {

				if (srcNode instanceof Activity
						|| srcNode instanceof StartEvent) {
					preA2List.add(srcNode);
				} else if (srcNode instanceof Gateway) {
					preA2List.add(getPreActivity(srcId, f2List));
				}
			} else if (a2Id.equalsIgnoreCase(srcId)) {
				if (tgtNode instanceof Activity || tgtNode instanceof EndEvent) {
					sucA2List.add(tgtNode);
				} else if (tgtNode instanceof Gateway) {
					getSucActivity(tgtId, f2List, sucA2List);
				}
			}
		}
		double preValue = 0;
		double sucValue = 0;

		int preLen = preA1List.size();
		int preLen2 = preA2List.size();

		for (int i = 0; i < preLen; i++) {
			String n1 = preA1List.get(i).getName();
			for (int j = 0; j < preLen2; j++) {
				String n2 = preA2List.get(j).getName();
				if (n1.equalsIgnoreCase(n2)) {
					preValue += 1;
				}
			}
		}

		int sucLen = sucA1List.size();
		int sucLen2 = sucA2List.size();
		for (int i = 0; i < sucLen; i++) {
			String n1 = sucA1List.get(i).getName();
			for (int j = 0; j < sucLen2; j++) {
				String n2 = sucA2List.get(j).getName();
				if (n1.equalsIgnoreCase(n2)) {
					sucValue += 1;
				}
			}
		}
		preValue = (2 * preValue) / (preLen + preLen2);
		sucValue = (2 * sucValue) / (sucLen + sucLen2);

		return (0.1 * preValue + 0.1 * sucValue);
	}

	/*
	 * public static double compareProcess(Process p1, Process p2) { double
	 * value = 0; // compare how many is the same. List actList =
	 * p1.getActivityList(); List act2 = (List)p2.getActivityList(); int size =
	 * actList.size(); int len = act2.size(); for(int i=0; i<size; i++) {
	 * for(int j=0; j<len; j++) { Activity a1 = (Activity)actList.get(i);
	 * Activity a2 = (Activity)act2.get(j); String n1 = a1.getName(); String n2
	 * = a2.getName(); String n11 = n1.replace(" ", ""); String n22 =
	 * n2.replace(" ", ""); if (n11.equalsIgnoreCase(n22)) { // decide its
	 * pre/success double result = 0.8 + compareActivity(a1, a2, p1, p2); //
	 * pre/succ value += result; } } } value = (value *2) /(size + len); return
	 * value; }
	 */

	public static double compareProcess(Process p1, Process p2) {
		PetriNet pn1 = BPMNConvertor.convertOriginalBPMNTOProcessModel(p1);
		PetriNet pn2 = BPMNConvertor.convertOriginalBPMNTOProcessModel(p2);
		// JFrame frame = new JFrame();
		// Container c = frame.getContentPane();
		// c.add(pn2.getGrappaVisualization());
		// frame.setSize(1000, 1000);
		// frame.setVisible(true);
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// byte[] result1 = PetriNetUtil.getPnmlBytes(pn2);
		// System.out.println(result1);
		double result;
		// JaccardTARSimilarity tarSimilarity = new JaccardTARSimilarity();
		// result = tarSimilarity.similarity(pn1, pn2);
		BTSSimilarity_Wang wang = new BTSSimilarity_Wang();
		result = wang.similarity(pn1, pn2);
		return result;
	}

	public static String[][] Compare(String fileName) {
		try {

			File file = new File(fileName);
			// String[] filelist = file.list();
			String[] filelist = file.list(new FilenameFilterBpmn());
			int len = filelist.length;
			String[][] results = new String[len + 1][len + 1];
			results[0][0] = "x";

			List pList = new ArrayList<Process>();
			for (int i = 0; i < len; i++) {
				String shortName = filelist[i];
				String inputFile = fileName + "\\" + shortName;

				int index = shortName.indexOf(".bpmn");
				String noFlex = shortName.substring(0, index);
				int flag = noFlex.indexOf('(');
				if (flag > 0) {
					String temp = noFlex.replace('(', '_');
					noFlex = temp.replace(')', '_');
				}
				results[0][i + 1] = noFlex;
				results[i + 1][0] = noFlex;
				BPMNModel m = new BPMNModel();
				Process p = m.getProcess(inputFile);
				pList.add(p);
			}
			/*
			 * Process p1 = (Process)pList.get(0); Process p2 =
			 * (Process)pList.get(3); double value = compareProcess(p1, p2);
			 * //System.out.println("value: " + value);
			 */

			for (int i = 0; i < len; i++) {
				Process p1 = (Process) pList.get(i);

				for (int j = 0; j < len; j++) {
					if (i == j) {
						results[i + 1][j + 1] = "1";
					} else {
						Process p2 = (Process) pList.get(j);
						double value = compareProcess(p1, p2);
						if (value > 0.75) {

							// System.out.println(p1.getName() + "  " +
							// p2.getName() + "   " + value);
						}
						results[i + 1][j + 1] = Double.toString(value);
					}
				}

			}
			return results;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void writeExl(String[][] li, String file) {
		try { // ����Workbook����, ֻ��Workbook���� //Method
				// 1��������д���Excel������
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(
					file));
			WritableSheet ws = wwb.createSheet("sheet1", 0);
			int size = li.length;
			for (int i = 0; i < size; i++) {
				String[] str = (String[]) li[i];
				int len = str.length;
				for (int j = 0; j < len; j++) {

					if ((i == 0) || (j == 0)) {

						jxl.write.Label labelC = new jxl.write.Label(i, j,
								str[j]);
						ws.addCell(labelC);
					} else {
						jxl.write.Number n2 = new jxl.write.Number(i, j,
								new Double(str[j]).doubleValue());
						ws.addCell(n2);
					}
				}
			}
			wwb.write(); // �ر�Excel����������
			wwb.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		try {

			//
			/*
			 * String fileName = "E:\\Customer\\CMCC\\Tool\\Test\\segments\\S1";
			 * String[][] results = Compare(fileName); writeExl(results,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment1.xls");
			 * 
			 * String s2 = "E:\\Customer\\CMCC\\Tool\\Test\\segments\\S2";
			 * String[][] s2results = Compare(s2); writeExl(s2results,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment2.xls");
			 * 
			 * String s3 = "E:\\Customer\\CMCC\\Tool\\Test\\segments\\S3";
			 * String[][] s3results = Compare(s3); writeExl(s3results,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment3.xls");
			 * 
			 * String s4 = "E:\\Customer\\CMCC\\Tool\\Test\\segments\\S4";
			 * String[][] s4results = Compare(s4); writeExl(s4results,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment4.xls");
			 * 
			 * String s5 = "E:\\Customer\\CMCC\\Tool\\Test\\segments\\S5";
			 * String[][] s5results = Compare(s5); writeExl(s5results,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment5.xls");
			 */
			/*
			 * E:\Customer\CMCC\Tool\Test\segments\S2
			 * 
			 * String fileName1 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\process\\instruction\\bpmn";
			 * String[][] results1 = Compare(fileName1); writeExl(results1,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\instruction.xls");
			 * 
			 * String fileName2 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\process\\meetingsummary\\bpmn";
			 * String[][] results2 = Compare(fileName2); writeExl(results2,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\meetingsummary.xls");
			 * 
			 * String fileName3 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\process\\senddoc\\bpmn";
			 * String[][] results3 = Compare(fileName3); writeExl(results3,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\senddoc.xls");
			 * 
			 * String fileName4 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\process\\receivedoc\\bpmn";
			 * String[][] results4 = Compare(fileName4); writeExl(results4,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\receivedoc.xls");
			 * 
			 * String fileName5 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\process\\all"; String[][]
			 * results5 = Compare(fileName5); writeExl(results5,
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\all.xls");
			 */
			/*
			 * String inputFile =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\process\\instruction\\bpmn\\Xizang Department Review and Approve.bpmn"
			 * ; BPMNModel m = new BPMNModel(); Process p =
			 * m.getProcess(inputFile);
			 */
			// //System.out.println((Math.floor(5.3/2)));
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("ErroR" );
		}

	}

}
