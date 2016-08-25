package com.chinamobile.bpmspace.core.repository;

import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.index.CaseDurationIndex;
import com.chinamobile.bpmspace.core.domain.index.CaseLengthIndex;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;

public class LengthIndexRepository {
	private static MongoAccess mongo = new MongoAccess();

	public CaseLengthIndex getLengthQuery(int _length)
			throws EmptyFieldException {
		if (_length == 0) {
			throw new EmptyFieldException("长度索引有错");
		}
		return mongo.getILIByLength(_length);
	}

	public List<CaseLengthIndex> getLengthBewteen(int _down, int _up)
			throws EmptyFieldException {
		if (_down == 0 || _up == 0) {
			throw new EmptyFieldException("长度索引有错");
		}
		return mongo.getILIBewteen(_down, _up);
	}

	public List<CaseLengthIndex> getGreatThan(int _down)
			throws EmptyFieldException {
		if (_down == 0) {
			throw new EmptyFieldException("长度索引有错");
		}
		return mongo.getILIGreatThan(_down);
	}

	public List<CaseLengthIndex> getLessThan(int _up)
			throws EmptyFieldException {
		if (_up == 0) {
			throw new EmptyFieldException("长度索引有错");
		}
		return mongo.getILILessThan(_up);
	}

	public String addLengthIndex(int _length, String _logId)
			throws EmptyFieldException {
		if (_length == 0 || _logId == null || _logId.equals("")) {
			throw new EmptyFieldException("长度索引有错");
		}
		CaseLengthIndex niliIndex = mongo.getILIByLength(_length);
		CaseLengthIndex nInstanceLengthIndex;
		if (niliIndex != null) {
			nInstanceLengthIndex = niliIndex;
			nInstanceLengthIndex.addLogList(_logId);
			mongo.removeILIndex(niliIndex.getLength());
		} else {
			nInstanceLengthIndex = new CaseLengthIndex();
			nInstanceLengthIndex.setLength(_length);
			nInstanceLengthIndex.addLogList(_logId);
		}
		return mongo.addILIndex(nInstanceLengthIndex);
	}

	public void removeLogInLengthIndex(String _logId)
			throws EmptyFieldException {
		Log log = new Log();
		CaseLengthIndex niliIndex;
		try {
			log = mongo.getLogById(_logId);
			for (int i : log.getLengthList()) {
				niliIndex = mongo.getILIByLength(i);
				niliIndex.removeLogFromLogList(_logId);
				if (niliIndex.getLogList().isEmpty()) {
					mongo.removeILIndex(i);
				} else {
					mongo.addILIndex(niliIndex);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}

	public void removeLogInDurationIndex(String _logId)
			throws EmptyFieldException {
		Log log = new Log();
		CaseDurationIndex nidiIndex;
		try {
			log = mongo.getLogById(_logId);
			for (int i : log.getDurationList()) {
				nidiIndex = mongo.getIDIByDuration(i);
				nidiIndex.removeLogFromDurationList(_logId);
				if (nidiIndex.getLogList().isEmpty()) {
					mongo.removeIDIndex(i);
				} else {
					mongo.addIDIndex(nidiIndex);
				}
				mongo.addIDIndex(nidiIndex);
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
}
