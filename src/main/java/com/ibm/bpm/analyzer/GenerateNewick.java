package com.ibm.bpm.analyzer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WritableSheet;

import com.ibm.bpm.model.Newickformat;

public class GenerateNewick {
	public static String result2 = "";
	public static Sheet sheet = null;

	public static List readExcel(String excelFileName) throws BiffException,
			IOException {

		// ����һ��list �����洢��ȡ������
		List list = new ArrayList();
		Workbook rwb = null;
		Cell cell = null;

		// ��ȡExcel�ļ�����
		rwb = Workbook.getWorkbook(new File(excelFileName));

		// ��ȡ�ļ���ָ�������� Ĭ�ϵĵ�һ��
		sheet = rwb.getSheet(0);

		// ����(��ͷ��Ŀ¼����Ҫ����1��ʼ)
		for (int i = 0; i < sheet.getRows(); i++) {

			// ����һ������ �����洢ÿһ�е�ֵ
			String[] str = new String[sheet.getColumns()];

			// ����
			for (int j = 0; j < sheet.getColumns(); j++) {

				// ��ȡ��i�У���j�е�ֵ
				cell = sheet.getCell(j, i);
				str[j] = cell.getContents().toString();

			}
			// �Ѹջ�ȡ���д���list
			list.add(str);
		}

		// ����ֵ����
		return list;
	}

	public static String findMax(List li) {

		int size = li.size();
		if (size > 2) {
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
			 * (String[])li.get[0]); cell = sheet.getCell(maxi, 0); String right
			 * = cell.getContents();
			 */

			String[] argsx = (String[]) li.get(0);
			String left = argsx[maxj];

			String[] argsx2 = (String[]) li.get(maxi);
			String right = argsx2[0];

			Newickformat nf = new Newickformat(left, max, right);
			String result = nf.getResult();

			// System.out.println(result);
			result2 = result;

			// merge to the same one;

			return findMax(merge(li, maxi, maxj, result));
			// List list = merge(li, maxi, maxj, result);
			// wirteExl(list,
			// "E://Customer//CMCC//Tool//Test//send//senddoc13.xls");
			// return result;
		} else
			return result2;
	}

	public static List merge(List list, int row, int col, String result) {
		List newlist = new ArrayList();
		List rowValues = new ArrayList();
		List colValues = new ArrayList();

		int size = list.size();

		int size2 = 0;
		for (int i = 0; i < size; i++) {
			String[] str = (String[]) list.get(i);
			size2 = str.length - 2;
			if (i != row && i != col) {
				for (int j = 0; j < str.length; j++) {
					if (j == col) {
						colValues.add(str[j]);
					}
				}

			} else if (i == row) {
				for (int j = 0; j < str.length; j++) {
					if (i != j && j != col) {
						rowValues.add(str[j]);
					}
				}
			}
		}

		// System.out.println("rowvalues" + rowValues);
		// System.out.println(colValues);

		int index = 0;
		String[] finalRow = new String[size2 + 1];

		for (int i = 0; i < size; i++) {
			String[] str = (String[]) list.get(i);
			String[] newStr = new String[size2 + 1];
			if (i != row && i != col) {
				if (row < col) {
					for (int j = 0; j < row; j++) {
						newStr[j] = str[j];
					}
					for (int j = row; j < col - 1; j++) {
						newStr[j] = str[j + 1];
					}
					for (int j = col - 1; j < size2; j++) {
						newStr[j] = str[j + 2];
					}
				} else if (row > col) {
					for (int j = 0; j < col; j++) {
						newStr[j] = str[j];
					}

					for (int j = col; j < row - 1; j++) {
						newStr[j] = str[j + 1];
					}

					for (int j = row - 1; j < size2; j++) {
						newStr[j] = str[j + 2];
					}
				}
				if (i == 0) {
					newStr[size2] = result;
					finalRow[index] = result;
					index++;
				} else {

					// System.out.println(index);
					double d1 = new Double((String) rowValues.get(index))
							.doubleValue();
					double d2 = new Double((String) colValues.get(index))
							.doubleValue();

					double d12 = (d1 + d2) / 2;
					String val = new Double(d12).toString();
					newStr[size2] = val;
					finalRow[index] = val;
					index++;
				}
				newlist.add(newStr);
			}
		}

		finalRow[size2] = "1";
		newlist.add(finalRow);
		return newlist;
	}

	public static void calc(String fileName1, String flag) {
		try {
			List li = readExcel(fileName1);
			String result = findMax(li);
			// System.out.println("NW format: " + result);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"D:\\FigTree v1.4.0\\" + flag + ".tree")));
			writer.write(result);
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("ErroR" );
		}
	}

	public static void generic(String excelPath, String treeFilePath) {
		try {
			List li = readExcel(excelPath);
			String result = findMax(li);
			// System.out.println("NW format: " + result);
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					treeFilePath)));
			writer.write(result);
			writer.close();
			System.out.println("Newick tree generated!");
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("ErroR" );
		}
	}

	public static void comparePercent(String f1, String f2, double threshold) {
		try {
			//
			List results = readExcel(f1);
			List results2 = readExcel(f2);
			int len = results.size();
			int len2 = results2.size();
			double index = 0;
			double all = 0;
			for (int i = 1; i < len; i++) {
				String[] str = (String[]) results.get(i);
				int size = str.length;
				for (int j = 1; j < size; j++) {
					String temp = str[j];
					double value = (new Double(temp)).doubleValue();
					if (value > threshold) {
						index++;
					}
					all++;
				}
			}

			// System.out.println("  " +index + "  " + all + "   " + index/all);

			index = 0;
			all = 0;
			for (int i = 1; i < len2; i++) {
				String[] str = (String[]) results2.get(i);
				int size = str.length;
				for (int j = 1; j < size; j++) {
					String temp = str[j];
					double value = (new Double(temp)).doubleValue();
					if (value > threshold) {
						index++;
					}
					all++;
				}
			}
			// System.out.println(index/all);

		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("ErroR" );
		}
	}

	public static void main(String[] args) {
		try {

			// String fileName =
			// "E:\\Customer\\CMCC\\Mar\\deliverables\\data.xls";
			/*
			 * String fileName1 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment1.xls";
			 * calc(fileName1, "fragment1");
			 * 
			 * String fileName2 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment2.xls";
			 * calc(fileName2, "fragment2");
			 * 
			 * 
			 * String fileName3 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment3.xls";
			 * calc(fileName3, "fragment3");
			 * 
			 * String fileName4 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment4.xls";
			 * calc(fileName4, "fragment4");
			 * 
			 * String fileName5 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment5.xls";
			 * calc(fileName5, "fragment5");
			 */
			/*
			 * String fileName =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\all.xls";
			 * calc(fileName, "all");
			 * 
			 * String fileName1 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\instruction.xls";
			 * calc(fileName1, "instruction"); String fileName2 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\meetingsummary.xls";
			 * calc(fileName2, "meetingsummary");
			 * 
			 * String fileName3 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\senddoc.xls";
			 * calc(fileName3, "senddoc");
			 * 
			 * String fileName4 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\receivedoc.xls";
			 * calc(fileName4, "receivedoc"); String fileName5 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment1.xls";
			 * calc(fileName5, "fragment1");
			 * 
			 * 
			 * String fileName3 =
			 * "E:\\Customer\\CMCC\\Tool\\Test\\send\\senddoc12.xls";
			 * calc(fileName3, "senddoc");
			 */
			comparePercent(
					"E:\\Customer\\CMCC\\Tool\\Test\\compare\\fragment1.xls",
					"E:\\Customer\\CMCC\\Tool\\Test\\compare\\senddoc.xls",
					0.75);
			// //System.out.println((Math.floor(5.3/2)));
		} catch (Exception e) {
			e.printStackTrace();
			// System.out.println("ErroR" );
		}

	}

	public static void wirteExl(List li, String file) {
		try { // ����Workbook����, ֻ��Workbook���� //Method
				// 1��������д���Excel������
			jxl.write.WritableWorkbook wwb = Workbook.createWorkbook(new File(
					file));
			WritableSheet ws = wwb.createSheet("sheet1", 0);
			int size = li.size();
			for (int i = 0; i < size; i++) {
				String[] str = (String[]) li.get(i);
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
}
