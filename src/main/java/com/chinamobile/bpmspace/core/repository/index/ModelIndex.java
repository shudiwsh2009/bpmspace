package com.chinamobile.bpmspace.core.repository.index;

import java.util.TreeSet;

public interface ModelIndex extends Index {

	public void addProcessModel(Object o);

	public void delProcessModel(Object o);

	/**
	 * @param o
	 *            maybe String or petri net object
	 * 
	 * @param similarity
	 *            float value between 0 and 1
	 * 
	 * @return set of process id and similarity
	 */
	public TreeSet<ProcessQueryResult> getProcessModels(Object o,
			float similarity);

	public float getStorageSizeInMB();
}
