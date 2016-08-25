package com.chinamobile.bpmspace.core.repository.socialnetworkmining;

import org.processmining.framework.log.LogFile;
import org.processmining.framework.log.LogReader;
import org.processmining.framework.log.LogReaderFactory;
import org.processmining.mining.MiningResult;

public class SocialNetworkMining {
	// input params
	private String inputFilePath = null;
	private LogReader logReader = null;
	private int indexType = 0;

	// output params

	public SocialNetworkMining(String inputFilePath, int indexType) {
		this.inputFilePath = inputFilePath;
		this.indexType = indexType;
	}

	public MiningResult mining() {
		LogFile logFile = LogFile.getInstance(inputFilePath);
		try {
			logReader = LogReaderFactory.createInstance(null, logFile);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		newSocialNetworkMiner nsnwm = new newSocialNetworkMiner(indexType);
		return nsnwm.mine(logReader);
	}
}