package com.chinamobile.bpmspace.core.repository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.Permission;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.log.Activity;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.domain.log.CaseEventList;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.DuplicateFieldException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.PermissionUtil;

public class LogRepository {
	private static MongoAccess mongo = new MongoAccess();
	public ArrayList<Activity> ast = new ArrayList<Activity>();

	public Log nlog = new Log();

	public String addLog(String _name, String _ownerId, String _catalogId,
			List<Integer> _lengthList) throws BasicException {
		nlog = new Log(_name, _ownerId, _catalogId, _lengthList);
		mongo.addLog(nlog);
		return nlog.getId();
	}

	public List<Log> findLogByCatalogId(String _catalogId)
			throws EmptyFieldException, NoExistException {
		if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		}
		LogCatalog catalog = mongo.getLogCatalogById(_catalogId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		}
		return mongo.getLogByCatalogId(_catalogId);
	}

	public void deleteLog(String _logId) throws EmptyFieldException,
			NoExistException {
		if (_logId == null || _logId.equals("")) {
			throw new EmptyFieldException("日志不存在");
		}
		Log log = mongo.getLogById(_logId);
		if (log == null) {
			throw new NoExistException("日志不存在");
		}
		mongo.removeLog(_logId);
	}

	public Log getLogByLogId(String _logId) throws EmptyFieldException {
		if (_logId == null || _logId.equals("")) {
			throw new EmptyFieldException("logID 有问题");
		}
		return mongo.getLogById(_logId);
	}

	public void updateLog(String _logId, List<Integer> _lengthList,
			List<Integer> _durationList, List<String> _eventList,
			List<CaseEventList> cels) {
		Log log = new Log();
		log = mongo.getLogById(_logId);
		log.setLengthList(_lengthList);
		log.setDurationList(_durationList);
		log.setEventList(_eventList);
		log.setCaseEventList(cels);
		mongo.addLog(log);
	}

	public String[] moveLogs(String _logIds, String _targetCatalogId,
			String _operator, String _userId) throws EmptyFieldException,
			NoExistException, ActionRejectException, DuplicateFieldException {
		// Empty field validity
		if (_logIds == null || _logIds.equals("")) {
			throw new EmptyFieldException("未选中日志");
		} else if (_targetCatalogId == null || _targetCatalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		List<Log> logList = new ArrayList<Log>();
		String[] logIds = _logIds.split(":");
		for (String lId : logIds) {
			Log l = mongo.getLogById(lId);
			if (l == null) {
				continue;
			}
			logList.add(l);
		}
		LogCatalog catalog = mongo.getLogCatalogById(_targetCatalogId);
		User user = mongo.getUserById(_userId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (catalog.getType().equals(CatalogType.SHARE)) {
			throw new ActionRejectException("不允许移动日志到共享库中");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
			throw new ActionRejectException("用户无该目录读写权限");
		}
		/**
		 * result数组含义 [0]: 无权限日志Id [1]: 无权限日志Name [2]: 询问覆盖日志Id [3]: 询问覆盖日志Name
		 */
		String[] result = new String[4];
		StringBuilder b1 = new StringBuilder();
		StringBuilder b2 = new StringBuilder();
		StringBuilder b3 = new StringBuilder();
		StringBuilder b4 = new StringBuilder();
		// move
		for (Log l : logList) {
			Log ori = null;
			if ((ori = mongo.hasLogInCatalog(l.getName(), catalog.getId())) != null) {
				if (!_operator.equals("FORCE")) {
					b3.append(l.getId());
					b3.append(":");
					b4.append(l.getName());
					b4.append(FileUtil.SPLITTER);
					continue;
				} else {
					// 强制覆盖
					mongo.removeLog(ori.getId());
				}
			}
			l.setCatalogId(catalog.getId());
			mongo.saveLog(l);
		}
		result[0] = b1.toString();
		result[1] = b2.toString();
		result[2] = b3.toString();
		result[3] = b4.toString();
		return result;
	}

	public String[] cloneLogs(String _logIds, String _targetCatalogId,
			String _operator, String _userId) throws EmptyFieldException,
			NoExistException, ActionRejectException, DuplicateFieldException {
		// Empty field validity
		if (_logIds == null || _logIds.equals("")) {
			throw new EmptyFieldException("未选中日志");
		} else if (_targetCatalogId == null || _targetCatalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		List<Log> logList = new ArrayList<Log>();
		String[] logIds = _logIds.split(":");
		for (String lId : logIds) {
			Log l = mongo.getLogById(lId);
			if (l == null) {
				continue;
			}
			logList.add(l);
		}
		LogCatalog catalog = mongo.getLogCatalogById(_targetCatalogId);
		User user = mongo.getUserById(_userId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (catalog.getType().equals(CatalogType.SHARE)) {
			throw new ActionRejectException("不允许拷贝模型到共享库中");
		} else if (catalog.getPermission(_userId) != Permission.RW) {
			throw new ActionRejectException("用户无该目录读写权限");
		}
		/**
		 * result数组含义 [0]: 无权限流程Id [1]: 无权限流程Name [2]: 询问覆盖流程Id [3]: 询问覆盖流程Name
		 */
		String[] result = new String[4];
		StringBuilder b1 = new StringBuilder();
		StringBuilder b2 = new StringBuilder();
		StringBuilder b3 = new StringBuilder();
		StringBuilder b4 = new StringBuilder();
		// copy
		for (Log l : logList) {
			Log ori = null;
			if ((ori = mongo.hasLogInCatalog(l.getName(), catalog.getId())) != null) {
				if (!_operator.equals("FORCE")) {
					b3.append(l.getId());
					b3.append(":");
					b4.append(l.getName());
					b4.append(FileUtil.SPLITTER);
					continue;
				} else {
					// 强制覆盖
					mongo.removeLog(ori.getId());
				}
			}
			Log newL = l.clone();
			if (newL == null) {
				continue;
			}
			newL.setCatalogId(catalog.getId());
			newL.setOwnerId(user.getId());
			newL.setCreateTime(new Date());
			newL.setId(null);
			Log nl = mongo.saveLog(newL);
			cloneInstance(l.getId(), nl.getId());
		}
		result[0] = b1.toString();
		result[1] = b2.toString();
		result[2] = b3.toString();
		result[3] = b4.toString();
		return result;
	}

	public void cloneInstance(String _logId, String _nlogId) {
		List<Case> instances = mongo.getCasesBylogId(_logId);
		for (Case i : instances) {
			Case ni = i.clone();
			ni.setId(null);
			ni.setLogId(_nlogId);
			ni.setActivities(i.getActivities());
			ni.setOwnerId(i.getOwnerId());
			ni.setIdentifier(i.getIdentifier());
			mongo.saveCase(ni);
		}
	}

}