package com.chinamobile.bpmspace.core.repository.index;

import java.util.List;

public interface CaseIndex extends Index {
	/*
	 * //add and delete public void addCase(CaseBuffer cb,String taskid); public
	 * void addCases(ArrayList<CaseBuffer> cb, String taskid); public void
	 * deleteCase(CaseBuffer cb, String taskid); public void
	 * deleteCases(ArrayList<CaseBuffer> cbl, String taskid);
	 * 
	 * //query public HashSet<CaseID> getTraces(String Queryinfo,String taskid);
	 * public HashSet<CaseID> getTraces(String left,String right,String taskid);
	 * public HashSet<CaseID> getSimilarTraces(String likeString, String
	 * taskid); public HashSet<CaseID> getContainedTrace(ArrayList<String> keys,
	 * String taskid);
	 */
	// public String getsavekey();
	public void addInstance(Object o);

	public void del(Object o);

	public List<LogQueryResult> getLogs(Object o, int length);

	public List<LogQueryResult> getLogs(Object o, String eventName);

	List<LogQueryResult> getLogs(String eventName, String _adjacentEventName);
}
