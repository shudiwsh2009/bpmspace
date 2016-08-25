package cn.edu.thss.iise.bpmdemo.analysis.core.test;

import java.io.File;

import org.processmining.framework.models.petrinet.PetriNet;

import cn.edu.thss.iise.beehivez.server.metric.BTSSimilarity_Wang;
import cn.edu.thss.iise.bpmdemo.analysis.core.util.PetriNetUtil;

public class TestModelSimilarity {

	public static double similarity(File file1, File file2) {
		PetriNet pn1 = PetriNetUtil.getPetriNetFromPnmlFile(file1);
		PetriNet pn2 = PetriNetUtil.getPetriNetFromPnmlFile(file2);
		BTSSimilarity_Wang wang = new BTSSimilarity_Wang();
		return wang.similarity(pn1, pn2);

	}

	public static void main(String args[]) {
		String filePath = "C:\\Users\\Guo-68\\Desktop\\pnml�ļ�";
		File folder = new File(filePath);
		File[] files = folder.listFiles();
		int size = files.length;
		for (int i = 0; i < size; i++)
			for (int j = i + 1; j < size; j++) {
				double result = similarity(files[i], files[j]);
				System.out.println(result);
			}
	}
}
