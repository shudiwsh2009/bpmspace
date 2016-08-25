package com.ibm.bpm.model;

public class Newickformat {
	static String left;
	static double distance;
	static String right;

	public Newickformat(String left, double max, String right) {
		this.left = left;
		this.distance = 1 - max;
		this.right = right;

	}

	public static String getLeft() {
		return left;
	}

	public static String getRight() {
		return right;
	}

	public static String getResult() {
		// System.out.println("left : " + left);
		// System.out.println("right : " + right);
		// System.out.println("max : " + distance);

		if (left.startsWith("(") && right.startsWith("(")) {

			int begin = left.indexOf(':');
			int end = left.indexOf(',');
			double v1 = new Double(left.substring(begin + 1, end))
					.doubleValue();

			begin = right.indexOf(':');
			end = right.indexOf(',');
			double v2 = new Double(right.substring(begin + 1, end))
					.doubleValue();

			double flat1 = compute(left);
			double flat2 = compute(right);

			if (v1 > v2) {
				String result = "(" + left + ":" + (distance - flat1) + ","
						+ right + ":" + (distance - flat2) + ")";
				return result;
			} else {

				String result = "(" + right + ":" + (distance - flat2) + ","
						+ left + ":" + (distance - flat1) + ")";
				return result;
			}

		} else if (left.startsWith("(")) {
			double value = compute(left);
			String result = "(" + right + ":" + distance + "," + left + ":"
					+ (distance - value) + ")";
			return result;
		} else if (right.startsWith("(")) {
			double value = compute(right);
			String result = "(" + left + ":" + distance + "," + right + ":"
					+ (distance - value) + ")";
			return result;
		} else {
			String result = "(" + left + ":" + distance + "," + right + ":"
					+ distance + ")";
			return result;
		}
	}

	public static double compute(String left) {
		int seg = left.indexOf(':');
		int index = 0;
		int start = 0;
		for (int i = 0; i < seg; i++) {
			int flag = left.indexOf('(', start);
			if (flag >= 0 && flag < seg) {
				index++;
				start = flag + 1;
			}
		}

		int begin = left.indexOf(':');
		int end = left.indexOf(',');
		String str = left.substring(begin + 1, end);
		double value = new Double(str).doubleValue();

		for (int j = 0; j < index - 1; j++) {
			int flag = left.indexOf(")", end);
			int ends = left.indexOf(",", flag);
			int starts = left.lastIndexOf(':', ends);

			String temp = left.substring(starts + 1, ends);
			value += new Double(temp).doubleValue();

			end = ends;
		}

		// System.out.println("value: " + str);
		return value;
	}

	public static void main(String[] args) {
		try {
			/*
			 * calValue(
			 * "((Xizang Department Review and Approve:0.527,Xizang Branch Notice:0.527):0.09599999999999997,(Hainan Co Supervise2:0.45799999999999996,(Hainan Co Ask for Instructions:0.18600000000000005,Hainan Co Report:0.18600000000000005):0.2719999999999999):0.16500000000000004)"
			 * ); calValue(
			 * "((Hainan Co. Manager Instruction.bpmn:0.7515000000000001,(Xizang Department Review and Approve.bpmn:0.65,Xizang Branch Notice.bpmn:0.65):0.10150000000000003):0.019625000000000004,(Hainan Co. Supervise2.bpmn:0.699,(Hainan Co. Report.bpmn:0.249,Hainan Co. Ask for Instructions.bpmn:0.249):0.44999999999999996):0.0721250000000001)"
			 * ); calValue(
			 * "(((Ningxia Co. Receive Document _ Register.bpmn:0.6579999999999999,Ningxia Branch Receive Document.bpmn:0.6579999999999999):0.10525000000000007,(Xizang Branch Entity Receive Document.bpmn:0.476,Hainan Co. Receive Document 2.bpmn:0.476):0.28725):0.1628750000000001,(Xizang Branch Receive Document.bpmn:0.64,Xizang Branch Department Receive Document.bpmn:0.64):0.180875)"
			 * ); /*
			 */
			// calValue("((Xizang New Department Send Documen:0.32899999999999996,Xizang Branch Send Documen:0.32899999999999996):0.11699999999999999,(Xizang DGA.Send Documen:0.274,Xizang Department Send Documen:0.274):0.17199999999999993)");
			// calValue("((Xizang Party Group Send Documen:0.345,Xizang Co.Send Documen:0.345):0.135,((Xizang New Department Send Documen:0.32899999999999996,Xizang Branch Send Documen:0.32899999999999996):0.11699999999999999,(Xizang DGA.Send Documen:0.274,Xizang Department Send Documen:0.274):0.17199999999999993):0.03400000000000003)");
			compute("(Hainan Co. and Labour Union Joint Send Documen:0.32799999999999996,(Hainan Co.Send Document _private company:0.19699999999999995,Hainan Co.Send Document _listed company_Ne:0.19699999999999995):0.131)");
			// //System.out.println((Math.floor(5.3/2)));
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
