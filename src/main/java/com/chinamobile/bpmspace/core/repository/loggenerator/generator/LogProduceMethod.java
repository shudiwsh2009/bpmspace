package com.chinamobile.bpmspace.core.repository.loggenerator.generator;

import org.processmining.framework.models.petrinet.PetriNet;

public abstract class LogProduceMethod {
	protected LogIO logIO = null;

	public LogProduceMethod() {
		logIO = new LogIO();
	}

	/**
	 * 
	 * @param fileDir
	 *            where the log info store
	 * @param caseCount
	 *            how many cases will be executed
	 * @param pn
	 */
	public abstract void generateLog(String fileDir, int caseCount,
			PetriNet pn, double completeness, int multiple);

	public abstract void generateLog(String fileDir, int caseCount,
			PetriNet pn, double completeness);

	public abstract void generateLog(String fileDir, PetriNet pn);

	public abstract void generateLog(String fileDir, PetriNet pn,
			CompleteParameters comPara, NoiseParameters noiPara);

	public abstract String getLogType();
}
