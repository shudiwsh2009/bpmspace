package cn.edu.thss.iise.bpmdemo.analysis.core.statistic;

import java.util.Collections;
import java.util.Comparator;

import cn.edu.thss.iise.bpmdemo.analysis.core.mining.Log;
import cn.edu.thss.iise.bpmdemo.analysis.core.mining.Trace;

/**
 * ��־ͳ�Ʒ���
 * 
 * @author keh
 *
 */
public class LogStatisticUtil {

	/**
	 * ��ȡ������
	 * 
	 * @return
	 */
	public int getProcessesNum() {
		return 1; // Ĭ��һ����־�ļ���Ӧ1�����̣�����"������������"
	}

	/**
	 * ��ȡ������
	 * 
	 * @param log
	 * @return
	 */
	public int getCasesNum(Log log) {
		return log.getTraces().size();
	}

	/**
	 * ��ȡ�¼�����
	 * 
	 * @param log
	 * @return
	 */
	public int getEventsNum(Log log) {
		int num = 0;
		for (Trace trace : log.getTraces()) {
			num += trace.getEvents().size();
		}
		return num;
	}

	/**
	 * ��ȡ������(����"�Ǽ�"��"������ʵ"��)��
	 * 
	 * @param log
	 * @return
	 */
	public int getEventClassesNum(Log log) {
		return log.getTasks().size();
	}

	/**
	 * ��ȡ�¼���������Ĭ������:"start","complete"
	 * 
	 * @return
	 */
	public int getEventTypesNum() {
		return 2;
	}

	/**
	 * ��ȡִ������
	 * 
	 * @param log
	 * @return
	 */
	public int getOriginatorsNum(Log log) {
		return log.getPerformers().size();
	}

	/**
	 * ��ȡÿ�������¼������
	 * 
	 * @param log
	 * @return
	 */
	public int getMinEventsPerCase(Log log) {
		Collections.sort(log.getTraces(), new EventsNumPerCaseComparator());
		return log.getTraces().get(0).getEvents().size();
	}

	/**
	 * ��ȡÿ�������¼�ƽ����
	 * 
	 * @param log
	 * @return
	 */
	public int getMeanEventsPerCase(Log log) {
		return (int) Math.round((double) getEventsNum(log) / getCasesNum(log));
	}

	/**
	 * ��ȡÿ�������¼�������
	 * 
	 * @param log
	 * @return
	 */
	public int getMaxEventsPerCase(Log log) {
		Collections.sort(log.getTraces(), new EventsNumPerCaseComparator());
		return log.getTraces().get(log.getTraces().size() - 1).getEvents()
				.size();
	}

	/**
	 * ��ȡÿ���������������
	 * 
	 * @param log
	 * @return
	 */
	public int getMaxEventsClassesNumPerCase(Log log) {
		Collections.sort(log.getTraces(),
				new EventsClassesNumPerCaseComparator());
		return log.getTraces().get(0).getTasksPerTrace().size();
	}

	/**
	 * ��ȡÿ����������ƽ����
	 * 
	 * @param log
	 * @return
	 */
	public int getMeanEventsClassesNumPerCase(Log log) {
		double num = 0;
		for (Trace trace : log.getTraces()) {
			num += trace.getTasksPerTrace().size();
		}
		return (int) Math.round(num / getCasesNum(log));
	}

	/**
	 * ��ȡÿ����������������
	 * 
	 * @param log
	 * @return
	 */
	public int getMinEventsClassesNumPerCase(Log log) {
		Collections.sort(log.getTraces(),
				new EventsClassesNumPerCaseComparator());
		return log.getTraces().get(log.getTraces().size() - 1)
				.getTasksPerTrace().size();
	}

	class EventsNumPerCaseComparator implements Comparator<Trace> {
		public int compare(Trace trace1, Trace trace2) {
			return trace1.getEvents().size() - trace2.getEvents().size();
		}
	}

	class EventsClassesNumPerCaseComparator implements Comparator<Trace> {
		public int compare(Trace trace1, Trace trace2) {
			return trace1.getTasksPerTrace().size()
					- trace2.getTasksPerTrace().size();
		}
	}

	public void test() {
		String inputFile = "C:\\Users\\chenhz\\Documents\\Thss SVN\\THSS JBPM\\log(new)��BPMN�ļ�\\log\\���\\4A�˺Ź�������.xls";
		Log log = new Log(inputFile);
		System.out.println("Key data");
		System.out.println("Processes" + "\t" + getProcessesNum());
		System.out.println("Cases" + "\t" + getCasesNum(log));
		System.out.println("Events" + "\t" + getEventsNum(log));
		System.out.println("Event classes" + "\t" + getEventClassesNum(log));
		System.out.println("Event types" + "\t" + getEventTypesNum());
		System.out.println("Originators" + "\t" + getOriginatorsNum(log));
		System.out.println("\nEvents per case");
		System.out.println("Min" + "\t" + getMinEventsPerCase(log));
		System.out.println("Mean" + "\t" + getMeanEventsPerCase(log));
		System.out.println("Max" + "\t" + getMaxEventsPerCase(log));
		System.out.println("\nEvent classes per case");
		System.out.println("Min" + "\t" + getMinEventsClassesNumPerCase(log));
		System.out.println("Mean" + "\t" + getMeanEventsClassesNumPerCase(log));
		System.out.println("Max" + "\t" + getMaxEventsClassesNumPerCase(log));
	}

	public static LogStatisticsInfo getLogStatisticsInfo(String inputFile) {
		LogStatisticsInfo logInfo = new LogStatisticsInfo();
		Log log = new Log(inputFile);
		/*
		 * if(inputFile.contains("\\")) { logInfo.logName =
		 * inputFile.substring(inputFile.lastIndexOf('\\') + 1); } else {
		 * logInfo.logName = inputFile.substring(inputFile.lastIndexOf('/') +
		 * 1); }
		 */
		LogStatisticUtil ls = new LogStatisticUtil();
		logInfo.processesNumber = ls.getProcessesNum();
		logInfo.casesNumber = ls.getCasesNum(log);
		logInfo.eventsNumber = ls.getEventsNum(log);
		logInfo.eventClassesNum = ls.getEventClassesNum(log);
		logInfo.eventTypesNum = ls.getEventTypesNum();
		logInfo.originatorsNum = ls.getOriginatorsNum(log);
		logInfo.minEventsPerCase = ls.getMinEventsPerCase(log);
		logInfo.meanEventsPerCase = ls.getMeanEventsPerCase(log);
		logInfo.maxEventsPerCase = ls.getMaxEventsPerCase(log);
		logInfo.minEventsClassesNumPerCase = ls
				.getMinEventsClassesNumPerCase(log);
		logInfo.meanEventsClassesNumPerCase = ls
				.getMeanEventsClassesNumPerCase(log);
		logInfo.maxEventsClassesNumPerCase = ls
				.getMaxEventsClassesNumPerCase(log);

		return logInfo;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		LogStatisticUtil statistic = new LogStatisticUtil();
		statistic.test();
	}

}
