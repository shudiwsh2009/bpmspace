package com.chinamobile.bpmspace.core.repository;

import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.CaseDurationIndex;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;

public class DurationIndexRepository {
	private static MongoAccess mongo = new MongoAccess();

	public List<CaseDurationIndex> getDurationBewteen(int _down, int _up)
			throws EmptyFieldException {
		if (_down == 0 || _up == 0) {
			throw new EmptyFieldException("耗时索引有错");
		}
		return mongo.getIDIBewteen(_down, _up);
	}

	public List<CaseDurationIndex> getGreatThan(int _down)
			throws EmptyFieldException {
		if (_down == 0) {
			throw new EmptyFieldException("耗时索引有错");
		}
		return mongo.getIDIGreatThan(_down);
	}

	public List<CaseDurationIndex> getLessThan(int _up)
			throws EmptyFieldException {
		if (_up == 0) {
			throw new EmptyFieldException("耗时索引有错");
		}
		return mongo.getIDILessThan(_up);
	}

	public String addDurationIndex(int _duration, String _logId)
			throws EmptyFieldException {
		if (_duration == 0 || _logId == null || _logId.equals("")) {
			throw new EmptyFieldException("耗时索引有错");
		}
		CaseDurationIndex nidiIndex = mongo.getIDIByDuration(_duration);
		CaseDurationIndex nInstanceDurationIndex;
		if (nidiIndex != null) {
			nInstanceDurationIndex = nidiIndex;
			nInstanceDurationIndex.addLogList(_logId);
			mongo.removeIDIndex(nidiIndex.getDuration());
		} else {
			nInstanceDurationIndex = new CaseDurationIndex();
			nInstanceDurationIndex.setDuration(_duration);
			nInstanceDurationIndex.addLogList(_logId);
		}
		return mongo.addIDIndex(nInstanceDurationIndex);
	}
	/*
	 * public void removeLogInLengthIndex(String _logId) { Log log =
	 * mongo.getLogById(_logId); InstanceLengthIndex niliIndex; for (int i :
	 * log.getLengthList()) { niliIndex = mongo.getILIByLength(i);
	 * mongo.removeILIndex(i); niliIndex.removeLog(_logId);
	 * mongo.addILIndex(niliIndex); } }
	 */
}
