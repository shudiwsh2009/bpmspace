package cn.edu.thss.iise.xiaohan.bpcd.test.convert;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.edu.thss.iise.xiaohan.bpcd.graph.EPCHelper;
import cn.edu.thss.iise.xiaohan.bpcd.nl.tue.tm.is.epc.EPC;
import cn.edu.thss.iise.xiaohan.bpcd.similarity.highlevelop.HighLevelOP;
import de.bpt.hpi.graph.Graph;

public class ConvertTest {

	public static void TestHighLevelOperation(Graph graph1, Graph graph2) {
		List<String> oplist = HighLevelOP.GetHighLevelOPList(graph1, graph2);

		for (String op : oplist) {
			System.out.println(op);
		}
	}

	public static void test1() {
		String name1 = "\\models\\ibm\\b3\\B3.s00000825__s00006242.epml"; // B3.s00000179__s00002108.epml
		String name2 = "\\models\\ibm\\b3\\B3.s00000473__s00003626.epml"; // B3.s00000281__s00002490.epml
		// B3.s00000179__s00002108.epml B3.s00000557__s00006400.epml
		String filepath1 = System.getProperty("user.dir") + name1;
		String filepath2 = System.getProperty("user.dir") + name2;

		filepath1 = "C:\\Users\\shudi\\Desktop\\epc\\2.epml";
		filepath2 = "C:\\Users\\shudi\\Desktop\\epc\\5.epml";

		System.err.println(name1);
		System.err.println(name2);
		System.out.println();

		try {
			EPC epcmodel1 = EPC.loadEPML(filepath1);
			epcmodel1.cleanEPC();
			EPC epcmodel2 = EPC.loadEPML(filepath2);
			epcmodel2.cleanEPC();

			EPCHelper epc1 = new EPCHelper(epcmodel1, filepath1);
			EPCHelper epc2 = new EPCHelper(epcmodel2, filepath2);

			Graph graph1 = epc1.getGraph();
			Graph graph2 = epc2.getGraph();

			TestHighLevelOperation(graph1, graph2);

		} catch (Exception e) {
			System.out.println("Some Bugs!!!");
		}
	}

	public static void test2() {
		String foldername = System.getProperty("user.dir")
				+ "\\models\\paper_experiment";
		File folder = new File(foldername);
		File[] listOfFiles = null;
		try {
			listOfFiles = folder.listFiles();
		} catch (Exception e) {
			System.out.println("Invalid input: folder " + foldername);
			return;
		}

		List<Graph> graphList = new ArrayList<Graph>();
		List<String> filenameList = new ArrayList<String>();
		List<Integer> distanceList = new ArrayList<Integer>();
		List<Double> similarityList = new ArrayList<Double>();
		for (int k = 0; k < listOfFiles.length; k++) {
			if (listOfFiles[k].isFile()) {
				String filepath = foldername + "\\" + listOfFiles[k].getName();
				String filename = listOfFiles[k].getName();
				filenameList.add(filename);
				try {
					EPC epcmodel = EPC.loadEPML(filepath);
					epcmodel.cleanEPC();
					EPCHelper epc = new EPCHelper(epcmodel, filepath);
					Graph graph = epc.getGraph();
					graphList.add(graph);
				} catch (Throwable e) {
					System.out.println("Problem with model " + filepath + ".");
				}
			}
		}

		for (int i = 0; i < graphList.size(); i++) {
			System.out.printf("%2s", i + ": ");
			System.out.printf("%12s", filenameList.get(i));
			for (int j = 0; j < graphList.size(); j++) {
				List<String> opList = HighLevelOP.GetHighLevelOPList(
						graphList.get(i), graphList.get(j));
				double similarity = HighLevelOP.GetHighLevelOPSimilarity(
						graphList.get(i), graphList.get(j));
				DecimalFormat df = new DecimalFormat("0.00");

				distanceList.add(opList.size());
				similarityList.add(similarity);

				System.out.printf("%9s",
						opList.size() + "/" + df.format(similarity));
				if (j == graphList.size() - 1)
					System.out.println();
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// test2();
		test1();
	}

}
