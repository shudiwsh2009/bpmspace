package com.chinamobile.bpmspace.core.repository.processmining.miner;

import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogSummary;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.mining.petrinetmining.PetriNetResult;
import org.processmining.mining.regionmining.RegionMiner;
import org.processmining.mining.regionmining.RegionMinerOptionsPanel;

public class Region_Miner extends ControlFlowMiner {

	@Override
	public String getDesription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "RegionMiner";
	}

	@Override
	public PetriNet mine(LogReader logReader) {
		// TODO Auto-generated method stub
		if (logReader != null) {
			// Mine the log for a Petri net.
			LogSummary summary = logReader.getLogSummary();
			RegionMiner miningPlugin = new RegionMiner();
			miningPlugin.options = new RegionMinerOptionsPanel(summary);
			PetriNetResult PNetResult = (PetriNetResult) miningPlugin
					.mine(logReader);
			PetriNet result = PNetResult.getPetriNet();
			return result;
		} else {
			System.err.println("No log reader could be constructed.");
			return null;
		}
	}

}
