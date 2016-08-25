package com.chinamobile.bpmspace.core.repository.index.test.model;

/**
 * time: seconds
 * 
 * @author chenhz
 * 
 */
public class ModelIndexConstructionStatisticsItem {
	public String modelId;

	public double modelInsertTime;
	public double accModelInsertTime;
	// public double modelCollectionSize;

	public double indexInsertTime;
	public double accIndexInsertTime;
	// public double indexCollectionSize;

	public double featureExtractionTime;

	@Override
	public String toString() {
		return super.toString();
	}

}
