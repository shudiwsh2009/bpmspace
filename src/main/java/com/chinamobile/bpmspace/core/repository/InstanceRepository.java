package com.chinamobile.bpmspace.core.repository;

import java.util.ArrayList;
import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;

public class InstanceRepository {
	private MongoAccess mongo = new MongoAccess();
	public ArrayList<Activity> ast = new ArrayList<Activity>();

	public Case instc = new Case();

	public String add(String _logId, String _caseId, String _name,
			String _description, String _ownerId) throws BasicException {

		instc = new Case(_logId, _caseId, _name, _description, _ownerId, ast);

		mongo.addCase(instc);
		return instc.getId();
	}

	public void makeActivitySet(long _startTime, long _endTime,
			String _activityName, String _actor, String _caseId) {
		Activity nas = new Activity();
		nas.setCaseId(_caseId);
		if (_startTime > _endTime) {
			long tmp = _startTime;
			_startTime = _endTime;
			_endTime = tmp;
		}
		nas.setStartTime(_startTime);
		nas.setEndTime(_endTime);
		nas.setName(_activityName);
		nas.setActor(_actor);
		this.makeActivityList(nas);

	}

	public void makeActivityList(Activity nas) {
		this.ast.add(nas);
	}

	public void makeActivityListClean() {
		instc.flushInstanceItem();
	}

	public Case getCaseById(String _caseId) {
		return mongo.getCaseById(_caseId);
	}

	public List<Case> getInstancesOfLog(String _logId) throws NoExistException,
			EmptyFieldException {
		if (_logId == null || _logId.equals("")) {
			throw new EmptyFieldException("日志不存在");
		}
		Log log = mongo.getLogById(_logId);
		if (log == null) {
			throw new NoExistException("日志不存在");
		}
		List<Case> ninstcl = mongo.getCasesBylogId(_logId);
		return ninstcl;
	}
}