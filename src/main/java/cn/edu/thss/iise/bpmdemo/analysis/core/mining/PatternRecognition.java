package cn.edu.thss.iise.bpmdemo.analysis.core.mining;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class PatternRecognition {

	public void recognize(Log log) throws ParseException {
		for (int i = 0; i < log.getTraces().size(); i++) {
			recognizeFTD(log.getTraces().get(i));
			recognizeCon(log.getTraces().get(i));
			recognizeRec(log.getTraces().get(i));
			recognizeMC(log.getTraces().get(i));
		}
	}

	/**
	 * ʶ�����Ȱ�ģʽ(һ��trace)
	 */
	public void recognizeFTD(Trace trace) {
		ArrayList<Event> events = trace.getEvents();
		Event ei, ej;
		for (int i = 0; i < events.size(); i++) {
			ei = events.get(i);
			if (trace.getQxbTasks().contains(ei.getIdentifier())) {
				continue;
			}
			for (int j = i + 1; j < events.size(); j++) {
				ej = events.get(j);
				if ((ej.getIdentifier().equals(ei.getIdentifier()))
						&& (ej.getBeginTime().equals(ei.getBeginTime()))
						&& (ej.getEndTime().equals(ei.getEndTime()))) {

					trace.getQxbTasks().add(ei.getIdentifier());
					break;
				}
			}
		}
	}

	/**
	 * ʶ���ǩ��������ģʽ(һ��trace)
	 * 
	 * @throws ParseException
	 */
	public void recognizeCon(Trace trace) throws ParseException {
		ArrayList<Event> events = trace.getEvents();
		Event ei, ej;
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-M-d H:mm");
		java.util.Date beginTimeI, endTimeI, beginTimeJ, endTimeJ;
		for (int i = 0; i < events.size(); i++) {
			ei = events.get(i);
			beginTimeI = dateFormat.parse((String) ei.getBeginTime());
			endTimeI = dateFormat.parse((String) ei.getEndTime());
			if (trace.getConTasks().contains(ei.getIdentifier())) {
				continue;
			}
			for (int j = i + 1; j < events.size(); j++) {
				ej = events.get(j);
				beginTimeJ = dateFormat.parse(ej.getBeginTime());
				endTimeJ = dateFormat.parse(ej.getEndTime());
				if ((ej.getIdentifier().equals(ei.getIdentifier()))
						&& ((((beginTimeI.after(beginTimeJ)) || (beginTimeI
								.equals(beginTimeJ))) && ((beginTimeI
								.before(endTimeJ)) || (beginTimeI
								.equals(endTimeJ)))) || (((endTimeI
								.after(beginTimeJ)) || (endTimeI
								.equals(beginTimeJ))) && ((endTimeI
								.before(endTimeJ)) || (endTimeI
								.equals(endTimeJ)))))) {
					if (!((ei.getBeginTime().equals(ej.getBeginTime())) && (ei
							.getEndTime().equals(ej.getEndTime())))) {
						// if(!ei.getPerformer().equals("adminJT") &&
						// !ej.getPerformer().equals("adminJT")){
						// if(!ei.getNextTask().equals("��adminJT����") &&
						// !ej.getNextTask().equals("��adminJT����")){
						//
						// }
						// }

						if (!ei.getIdentifier().equals("�����쵼��ʾ")) {
							trace.getConTasks().add(ei.getIdentifier());
							break;
						}
					}
				}
			}
		}
	}

	/**
	 * ʶ���ǩ���ݹ飩ģʽ(һ��trace)
	 */
	public void recognizeRec(Trace trace) {
		ArrayList<Event> events = trace.getEvents();
		Event ei, ej;
		for (int i = 0; i < events.size(); i++) {
			ei = events.get(i);
			if (trace.getRecTasks().contains(ei.getIdentifier())) {
				continue;
			}
			for (int j = i + 1; j < events.size(); j++) {
				ej = events.get(j);
				if ((ej.getIdentifier().equals(ei.getIdentifier()))
						&& (ej.getBeginTime().equals(ei.getEndTime()))) {
					trace.getRecTasks().add(ei.getIdentifier());
					break;
				}
			}
		}
	}

	/**
	 * ʶ������ģʽ(һ��trace)
	 */
	public void recognizeMC(Trace trace) {
		ArrayList<Event> events = trace.getEvents();
		Event ei, ej, ek;
		for (int i = 0; i < events.size(); i++) {
			ei = events.get(i);
			if (trace.getMcTasks().contains(ei.getIdentifier())) {
				continue;
			}
			for (int j = i + 1; j < events.size(); j++) {
				ej = events.get(j);
				for (int k = j + 1; k < events.size(); k++) {
					ek = events.get(k);
					if ((ej.getIdentifier().equals(ei.getIdentifier()))
							&& (ek.getIdentifier().equals(ej.getIdentifier()))
							&& (ei.getEndTime().equals(ej.getBeginTime()))
							&& (ej.getBeginTime().equals(ej.getEndTime()))
							&& (ej.getEndTime().equals(ek.getBeginTime()))) {
						trace.getMcTasks().add(ei.getIdentifier());
						break;
					}
				}
			}
		}
	}

}
