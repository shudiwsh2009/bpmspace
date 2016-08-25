package com.ibm.bpm.analyzer;

import com.ibm.bpm.model.Process;
import com.ibm.bpm.util.BPMNModel;

public class Transformation {
	public Transformation() {
	}

	public static void main(String[] args) {
		try {
			BPMNModel m = new BPMNModel();
			String inFileName = "C:\\Wangli\\Xizang.bpmn";
			String outFileName = "C:\\Wangli\\Results\\Xizang_new.bpmn";
			Process pp = null;
			;
			/*
			 * BufferedReader brr=new BufferedReader(new InputStreamReader(new
			 * FileInputStream("C:\\Wangli\\testt.txt"))); String strr=null;
			 * while((strr=brr.readLine())!=null) { String inFileName0 =
			 * "C:\\Wangli\\Test\\"+strr; pp = m.getProcess(inFileName0); }
			 */
			String inFileName0 = "C:\\Wangli\\Test\\Hainan Co.Countersign Subprocess.bpmn";
			pp = m.getProcess(inFileName0);

			Process p = m.getProcess(inFileName);

			for (int i = 0; i < p.getActivityList().size(); i++) {
				if (p.getActivityList()
						.get(i)
						.getContent()
						.equals("Related department / Department manager / countersign")) {
					p.getActivityList().get(i)
							.setCalledElement("bwl1:" + pp.getId());
				}
			}

			Process post_p = m.Transformation(p);

			m.exportProcess(inFileName, outFileName, post_p);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}
}
