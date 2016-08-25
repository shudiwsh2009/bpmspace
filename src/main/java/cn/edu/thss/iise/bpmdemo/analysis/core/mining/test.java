package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import org.processmining.framework.models.petrinet.PetriNet;

public class test {

	/**
	 * @param args
	 * @throws Exception
	 */
	/*
	 * public static void main(String[] args) throws Exception { // TODO
	 * Auto-generated method stub String inputFile = "D://231个部门收文的流程跟踪.xlsx",
	 * mxml = "D://流程跟踪.mxml", pnml = "D://流程跟踪.mxml"; Log log = new
	 * Log(inputFile); PatternRecognition patternRecognition = new
	 * PatternRecognition(); patternRecognition.recognize(log); EventsCombine
	 * eventscombine = new EventsCombine(); Process process =
	 * eventscombine.combine(log); ConvertToMXML ctm = new ConvertToMXML();
	 * ctm.convertToMxml(process, mxml); //AlphaMiner alphaMiner = new
	 * AlphaMiner(mxml, pnml);
	 * 
	 * }
	 */

	public void fun(String inputFile, String outputFile) throws Exception {
		Log log = new Log(inputFile);
		PatternRecognition patternRecognition = new PatternRecognition();
		patternRecognition.recognize(log);
		EventsCombine eventscombine = new EventsCombine();
		Process process = eventscombine.combine(log);
		String mxml = "流程跟踪.mxml";
		ConvertToMXML ctm = new ConvertToMXML();
		ctm.convertToMxml(process, mxml);
		PetriNet petriNet = null;
		AlphaMiner alphaMiner = new AlphaMiner(mxml);
		petriNet = alphaMiner.mine();

	}

}
