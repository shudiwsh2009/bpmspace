package com.chinamobile.bpmspace.core.repository.processmining.miner;

import org.processmining.converting.HNetToPetriNetConverter;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.models.heuristics.HeuristicsNet;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.mining.heuristicsmining.HeuristicsMiner;
import org.processmining.mining.heuristicsmining.HeuristicsNetResult;

public class HeuristicMiner extends ControlFlowMiner {

	@Override
	public String getDesription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "HeuristicsMiner";
	}

	@Override
	public PetriNet mine(LogReader logReader) {
		// TODO Auto-generated method stub
		if (logReader != null) {
			// Mine the log for a Petri net.
			HeuristicsMiner miningPlugin = new HeuristicsMiner();
			HeuristicsNetResult HNetResult = (HeuristicsNetResult) miningPlugin
					.mine(logReader);
			HeuristicsNet HNet = HNetResult.getHeuriticsNet();
			HNetToPetriNetConverter converter = new HNetToPetriNetConverter();
			PetriNet result = converter.convert(HNet);
			return result;
		} else {
			System.err.println("No log reader could be constructed.");
			return null;
		}
	}

}
