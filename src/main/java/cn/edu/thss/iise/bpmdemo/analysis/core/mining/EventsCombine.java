package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class EventsCombine {

	/**
	 * 合并日志中所有trace
	 * 
	 * @param log
	 * @return
	 * @throws ParseException
	 */
	public Process combine(Log log) throws ParseException {
		Process process;
		ArrayList<ProcessInstance> processInstances = new ArrayList<ProcessInstance>();
		ProcessInstance processInstance;
		for (int i = 0; i < log.getTraces().size(); i++) {
			processInstance = combineOneTrace(log.getTraces().get(i));
			processInstances.add(processInstance);
		}
		process = new Process("test.mxml.gz", "Converted to MXML",
				processInstances);
		return process;
	}

	/**
	 * 合并一个trace
	 * 
	 * @param trace
	 * @throws ParseException
	 */
	public ProcessInstance combineOneTrace(Trace trace) throws ParseException {
		ProcessInstance processInstance;
		ArrayList<AuditTrailEntry> auditTrailEntries = new ArrayList<AuditTrailEntry>();
		AuditTrailEntry auditTrailEntryStart;
		AuditTrailEntry auditTrailEntryComplete;
		ArrayList<Event> events = trace.getEvents();
		Event ei, ej;
		String identifier;
		int i;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:mm");
		java.util.Date earliestBeginTime, latestEndTime, beginTimeJ, endTimeJ;
		while (events.size() > 0) {
			i = 0;
			ei = events.get(i);
			identifier = ei.getIdentifier();
			if (trace.getQxbTasks().contains(identifier)) { // 合并抢先办模式
				auditTrailEntryStart = new AuditTrailEntry(identifier, "start",
						ei.getBeginTime());
				auditTrailEntryComplete = new AuditTrailEntry(identifier,
						"complete", ei.getEndTime());

			} else if (trace.getConTasks().contains(identifier)) { // 合并会签(并发)模式
				earliestBeginTime = dateFormat.parse(ei.getBeginTime());
				latestEndTime = dateFormat.parse(ei.getEndTime());
				for (int j = i + 1; j < events.size(); j++) {
					ej = events.get(j);
					beginTimeJ = dateFormat.parse(ej.getBeginTime());
					endTimeJ = dateFormat.parse(ej.getEndTime());
					if (beginTimeJ.before(earliestBeginTime)) {
						earliestBeginTime = beginTimeJ;
					}
					if (endTimeJ.after(latestEndTime)) {
						latestEndTime = endTimeJ;
					}
				}
				auditTrailEntryStart = new AuditTrailEntry(identifier, "start",
						earliestBeginTime.toString());
				auditTrailEntryComplete = new AuditTrailEntry(identifier,
						"complete", latestEndTime.toString());

			} else if (trace.getRecTasks().contains(identifier)) { // 合并会签(递归)模式
				earliestBeginTime = dateFormat.parse(ei.getBeginTime());
				latestEndTime = dateFormat.parse(ei.getEndTime());
				for (int j = i + 1; j < events.size(); j++) {
					ej = events.get(j);
					beginTimeJ = dateFormat.parse(ej.getBeginTime());
					endTimeJ = dateFormat.parse(ej.getEndTime());
					if (beginTimeJ.before(earliestBeginTime)) {
						earliestBeginTime = beginTimeJ;
					}
					if (endTimeJ.after(latestEndTime)) {
						latestEndTime = endTimeJ;
					}
				}
				auditTrailEntryStart = new AuditTrailEntry(identifier, "start",
						earliestBeginTime.toString());
				auditTrailEntryComplete = new AuditTrailEntry(identifier,
						"complete", latestEndTime.toString());

			} else if (trace.getMcTasks().contains(identifier)) { // 合并主控模式
				earliestBeginTime = dateFormat.parse(ei.getBeginTime());
				latestEndTime = dateFormat.parse(ei.getEndTime());
				for (int j = i + 1; j < events.size(); j++) {
					ej = events.get(j);
					beginTimeJ = dateFormat.parse(ej.getBeginTime());
					endTimeJ = dateFormat.parse(ej.getEndTime());
					if (beginTimeJ.before(earliestBeginTime)) {
						earliestBeginTime = beginTimeJ;
					}
					if (endTimeJ.after(latestEndTime)) {
						latestEndTime = endTimeJ;
					}
				}
				auditTrailEntryStart = new AuditTrailEntry(identifier, "start",
						earliestBeginTime.toString());
				auditTrailEntryComplete = new AuditTrailEntry(identifier,
						"complete", latestEndTime.toString());

			} else {
				auditTrailEntryStart = new AuditTrailEntry(identifier, "start",
						ei.getBeginTime());
				auditTrailEntryComplete = new AuditTrailEntry(identifier,
						"complete", ei.getEndTime());
			}
			// auditTrailEntries.add(auditTrailEntryStart);
			auditTrailEntries.add(auditTrailEntryComplete);
			remove(events, identifier);
		}
		processInstance = new ProcessInstance(trace.getId(), auditTrailEntries);
		return processInstance;
	}

	/**
	 * 移除trace中每行的处理环节是identifier的event
	 * 
	 * @param table
	 * @param identifier
	 */
	public void remove(ArrayList<Event> events, String identifier) {
		for (int i = 0; i < events.size(); i++) {
			if (events.get(i).getIdentifier().equals(identifier)) {
				events.remove(events.get(i));
				i--;
			}
		}
	}

}
