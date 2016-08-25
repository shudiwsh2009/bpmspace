package com.chinamobile.bpmspace.core.repository.socialnetworkmining;

import javax.swing.JPanel;

import org.processmining.analysis.originator.OTMatrix2DTableModel;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogSummary;
import org.processmining.mining.MiningPlugin;
import org.processmining.mining.MiningResult;
import org.processmining.mining.snamining.SocialNetworkOptions;
import org.processmining.mining.snamining.miningoperation.BasicOperation;
import org.processmining.mining.snamining.miningoperation.OperationFactory;
import org.processmining.mining.snamining.model.OriginatorsModel;
import org.processmining.mining.snamining.model.SocialNetworkMatrix;

public class newSocialNetworkMiner implements MiningPlugin {
	private int indexType = 0;
	private int choice = 0;
	private SocialNetworkOptions ui = null;
	private BasicOperation baseOprtation = null;
	private OriginatorsModel originatorsModel = null;

	public newSocialNetworkMiner(int indexType) {
		this.indexType = indexType;
		this.choice = indexType / 1000 * 1000;
	}

	public MiningResult mine(LogReader log) {
		LogSummary summary = log.getLogSummary();
		originatorsModel = new OriginatorsModel(summary);

		if (summary.getOriginators().length == 0) {
			System.out.println("Error: summary.getOriginators().length == 0");

			return null;
		}

		baseOprtation = OperationFactory.getOperation(indexType, summary, log);

		if (baseOprtation != null) {
			originatorsModel.setMatrix(baseOprtation.calculation(getBeta(),
					getDepthOfCalculation()));
		}

		if (originatorsModel.getMatrix() == null) {
			System.out
					.println("This type of social network mining is not implemented yet.");

			return null;
		}

		// to make OTMatrix
		OTMatrix2DTableModel otMatrix;
		otMatrix = new OTMatrix2DTableModel(log);

		SocialNetworkMatrix snMatrix = new SocialNetworkMatrix(
				originatorsModel.getSelectedOriginators(),
				originatorsModel.filterResultMatrix());
		snMatrix.setOTMatrix(otMatrix.getFilteredOTMatrix(originatorsModel
				.getSelectedOriginators()));

		return new newSocialNetworkResults(log, snMatrix);
	}

	/**
	 * Returns the beta value specified by the user. This value only applies to
	 * SUBCONTRACTING, HANDOVER_OF_WORK and REASSIGNMENT.
	 * 
	 * @return the beta value
	 */
	public double getBeta() {
		double beta = 0.5;
		switch (choice) {
		case SocialNetworkOptions.SUBCONTRACTING:
			return beta;
		case SocialNetworkOptions.HANDOVER_OF_WORK:
			return beta;
			// case REASSIGNMENT:
			// return Double.parseDouble(raBeta.getText());
		default:
			return 0.0;
		}
	}

	/**
	 * Returns the specified depth of calculation. This value only applies to
	 * SUBCONTRACTING and HANDOVER_OF_WORK.
	 * 
	 * @return the depth of calculation
	 */
	public int getDepthOfCalculation() {
		int depth = 5;
		switch (choice) {
		case SocialNetworkOptions.SUBCONTRACTING:
			return depth;
		case SocialNetworkOptions.HANDOVER_OF_WORK:
			return depth;
		default:
			return 0;
		}
	}

	// inherited abstract methods
	public String getName() {
		return "Social network miner";
	}

	public String getHtmlDescription() {
		return "";
	}

	public JPanel getOptionsPanel(LogSummary summary) {
		if (ui == null) {
			originatorsModel = new OriginatorsModel(summary);
			ui = new SocialNetworkOptions(originatorsModel);
		}
		return ui;
	}
}
