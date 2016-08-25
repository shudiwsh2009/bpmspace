package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import org.processmining.framework.models.petrinet.PetriNet;

public class Mining {

	// private String algorithm;

	public PetriNet mine(String algorithm, String inputFile) {
		PetriNet petriNet = null;
		if (algorithm.equals("Alpha Mining")) {
			AlphaMiner alpha = new AlphaMiner(inputFile);
			petriNet = alpha.mine();
		}

		return petriNet;

	}
}
