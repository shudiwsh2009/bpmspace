package com.chinamobile.bpmspace.core.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsOperations;

import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.index.CaseDurationIndex;
import com.chinamobile.bpmspace.core.domain.index.CaseLengthIndex;
import com.chinamobile.bpmspace.core.domain.log.Case;
import com.chinamobile.bpmspace.core.domain.log.CaseEventList;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.gridfs.GridFSDBFile;

public class MongoAccess {
	public static final ApplicationContext CTX = new GenericXmlApplicationContext(
			"SpringConfig.xml");
	public static final MongoOperations MONGO = (MongoOperations) MongoAccess.CTX
			.getBean("mongoTemplate");
	public static final GridFsOperations GRIDFS = (GridFsOperations) CTX
			.getBean("gridFsTemplate");

	public MongoAccess() {

	}

	/*
	 * Data access for User
	 */

	public User addUser(String _username, String _password) {
		User newUser = new User(_username, _password);
		MongoAccess.MONGO.save(newUser);
		return newUser;
	}

	public User saveUser(User _user) {
		MongoAccess.MONGO.save(_user);
		return _user;
	}

	public User getUserById(String _userId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_userId)), User.class);
	}

	public User getUserByUsername(String _username) {
		return MongoAccess.MONGO.findOne(new Query(Criteria.where("username")
				.is(_username)), User.class);
	}

	/*
	 * Data access for Process Catalog
	 */

	public ProcessCatalog addProcessCatalog(String _name, String _parentId,
			CatalogType _type, String _rootUserId, String _ownerId,
			String _ownerName) {
		ProcessCatalog newCatalog = new ProcessCatalog(_name, _parentId, _type,
				_rootUserId, _ownerId, _ownerName);
		MongoAccess.MONGO.save(newCatalog);
		return newCatalog;
	}

	public ProcessCatalog saveProcessCatalog(ProcessCatalog _catalog) {
		MongoAccess.MONGO.save(_catalog);
		return _catalog;
	}

	public void removeProcessCatalog(String _catalogId) {
		MongoAccess.MONGO.remove(
				new Query(Criteria.where("id").is(_catalogId)),
				ProcessCatalog.class);
	}

	public void removeProcessCatalogs(List<String> _catalogIds) {
		for (String id : _catalogIds) {
			this.removeProcessCatalog(id);
		}
	}

	public ProcessCatalog getProcessCatalogById(String _catalogId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_catalogId)),
				ProcessCatalog.class);
	}

	public List<ProcessCatalog> getProcessCatalogsByParentId(
			String _parentCatalogId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("parentId").is(_parentCatalogId)),
				ProcessCatalog.class);
	}

	public List<ProcessCatalog> getProcessCatalogsByParentIdAndName(
			String _parentCatalogId, String _name) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("parentId").is(_parentCatalogId)
						.andOperator(Criteria.where("name").is(_name))),
				ProcessCatalog.class);
	}

	public ProcessCatalog getPublicRootProcessCatalog() {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("name").is("Public Catalog")
						.andOperator(Criteria.where("parentId").is(""))),
				ProcessCatalog.class);
	}

	public List<ProcessCatalog> getRootProcessCatalogs(String _userId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("parentId").is("")
						.andOperator(Criteria.where("ownerId").is(_userId))),
				ProcessCatalog.class);
	}

	/*
	 * Data access for Log Catalog
	 */

	public LogCatalog addLogCatalog(String _name, String _parentId,
			CatalogType _type, String _rootUserId, String _ownerId,
			String _ownerName) {
		LogCatalog newCatalog = new LogCatalog(_name, _parentId, _type,
				_rootUserId, _ownerId, _ownerName);
		MongoAccess.MONGO.save(newCatalog);
		return newCatalog;
	}

	public LogCatalog saveLogCatalog(LogCatalog _catalog) {
		MongoAccess.MONGO.save(_catalog);
		return _catalog;
	}

	public void removeLogCatalog(String _catalogId) {
		MongoAccess.MONGO.remove(
				new Query(Criteria.where("id").is(_catalogId)),
				LogCatalog.class);
	}

	public void removeLogCatalogs(List<String> _catalogIds) {
		for (String id : _catalogIds) {
			this.removeProcessCatalog(id);
		}
	}

	public LogCatalog getLogCatalogById(String _catalogId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_catalogId)),
				LogCatalog.class);
	}

	public List<LogCatalog> getLogCatalogsByParentId(String _parentCatalogId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("parentId").is(_parentCatalogId)),
				LogCatalog.class);
	}

	public List<LogCatalog> getLogCatalogsByParentIdAndName(
			String _parentCatalogId, String _name) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("parentId").is(_parentCatalogId)
						.andOperator(Criteria.where("name").is(_name))),
				LogCatalog.class);
	}

	public LogCatalog getPublicRootLogCatalog() {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("name").is("Public Catalog")
						.andOperator(Criteria.where("parentId").is(""))),
				LogCatalog.class);
	}

	public List<LogCatalog> getRootLogCatalogs(String _userId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("parentId").is("")
						.andOperator(Criteria.where("ownerId").is(_userId))),
				LogCatalog.class);
	}

	/*
	 * Data access for Process
	 */

	public Process addProcess(String _name, String _description,
			String _catalogId, ProcessType _type, String _rootUserId,
			String _ownerId, String _ownerName) {
		Process newProcess = new Process(_name, _description, _catalogId,
				_type, _rootUserId, _ownerId, _ownerName);
		MongoAccess.MONGO.save(newProcess);
		return newProcess;
	}

	public Process saveProcess(Process _process) {
		MongoAccess.MONGO.save(_process);
		return _process;
	}

	public void removeProcess(String _processId) {
		MongoAccess.MONGO.remove(
				new Query(Criteria.where("id").is(_processId)), Process.class);
	}

	public Process getProcessById(String _processId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_processId)), Process.class);
	}

	public List<Process> getProcessesByCatalogId(String _catalogId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("catalogId").is(_catalogId)),
				Process.class);
	}

	public long countProcessesByCatalogId(String _catalogId) {
		return MongoAccess.MONGO.count(new Query(Criteria.where("catalogId")
				.is(_catalogId)), Process.class);
	}

	public List<Process> getProcessesByCatalogId(String _catalogId,
			int _indexStart, int _pageSize) {
		Query query = new Query(Criteria.where("catalogId").is(_catalogId));
		long totalCount = MongoAccess.MONGO.count(query, Process.class);
		if (totalCount == 0) {
			return new ArrayList<Process>();
		}
		if (_indexStart > totalCount - 1) {
			return new ArrayList<Process>();
		}
		query.skip(_indexStart);
		query.limit(_pageSize);
		return MongoAccess.MONGO.find(query, Process.class);
	}

	public Process hasProcessInCatalog(String _name, String _catalogId,
			ProcessType _type) {
		List<Process> processes = MongoAccess.MONGO.find(
				new Query(Criteria.where("catalogId").is(_catalogId)
						.andOperator(Criteria.where("name").is(_name))),
				Process.class);
		for (Process p : processes) {
			if (p.getType() == _type) {
				return p;
			}
		}
		return null;
	}

	/*
	 * Data access for Model
	 */

	public Model addModel(String _processId, String _creatorId,
			String _creatorName, long _revision, String _jsonStr,
			String _svgStr, String _xmlStr, double _size) {
		Model newModel = new Model(_processId, _creatorId, _creatorName,
				_revision, _jsonStr, _svgStr, _xmlStr, _size);
		MongoAccess.MONGO.save(newModel);
		return newModel;
	}

	public Model saveModel(Model _model) {
		MongoAccess.MONGO.save(_model);
		return _model;
	}

	public void removeModel(String _modelId) {
		MongoAccess.MONGO.remove(new Query(Criteria.where("id").is(_modelId)),
				Model.class);
	}

	public Model getModelById(String _modelId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_modelId)), Model.class);
	}

	public List<Model> getModelListSoretdDescByDate(String _processId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("processId").is(_processId))
						.with(new Sort(Sort.Direction.DESC, "createTime")),
				Model.class);
	}

	public List<Model> getModelWithType(ProcessType type, int pageNo,
			int pageSize) {
		List<Model> modelList = new ArrayList<Model>();
		if (pageNo <= 0) {
			return modelList;
		}
		List<Process> processList = MongoAccess.MONGO
				.find(new Query(Criteria.where("type").is(type.toString()))
						.skip((pageNo - 1) * pageSize).limit(pageSize),
						Process.class);
		for (Process pro : processList) {
			Model model = MongoAccess.MONGO.findOne(
					new Query(Criteria.where("processId").is(pro.getId()))
							.with(new Sort(Sort.Direction.DESC, "createTime")),
					Model.class);
			modelList.add(model);
		}
		return modelList;
	}

	public long countProcessNumberWithType(ProcessType type) {
		return MongoAccess.MONGO.count(
				new Query(Criteria.where("type").is(type.toString())),
				Process.class);
	}

	/**
	 * Data for case event list
	 */
	public CaseEventList saveCaseEventList(CaseEventList _caseEventList) {
		MongoAccess.MONGO.save(_caseEventList);
		return _caseEventList;
	}

	public void removeCaseEvetnList(String _caseEventName) {
		MongoAccess.MONGO.remove(
				new Query(Criteria.where("eventName").is(_caseEventName)),
				CaseEventList.class);
	}

	public CaseEventList getCasesByName(String _evetnName) {
		return MongoAccess.MONGO.findOne(new Query(Criteria.where("eventName")
				.is(_evetnName)), CaseEventList.class);
	}

	public List<CaseEventList> findAllEvents() {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("type").is("EVENT")),
				CaseEventList.class);
	}

	/*
	 * Data for Case
	 */

	public Case saveCase(Case _case) {
		MongoAccess.MONGO.save(_case);
		return _case;
	}

	public void addCase(Case _case) {
		MongoAccess.MONGO.save(_case);
	}

	public void removeCase(String _caseId) {
		MongoAccess.MONGO.remove(new Query(Criteria.where("id").is(_caseId)),
				Case.class);
	}

	public List<Case> getCasesBylogId(String _logId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("logId").is(_logId)), Case.class);
	}

	public Case getCaseById(String _caseId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_caseId)), Case.class);
	}

	/**
	 * Data for Log
	 */
	public Log saveLog(Log _log) {
		MongoAccess.MONGO.save(_log);
		return _log;
	}

	public Log getLogById(String _logId) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("id").is(_logId)), Log.class);
	}

	public void addLog(Log nlog) {
		MongoAccess.MONGO.save(nlog);
	}

	public Log hasLogInCatalog(String _name, String _catalogId) {
		List<Log> logs = MongoAccess.MONGO.find(
				new Query(Criteria.where("catalogId").is(_catalogId)
						.andOperator(Criteria.where("name").is(_name))),
				Log.class);
		if (logs.size() == 0) {
			return null;
		}
		return logs.get(0);
	}

	public List<Log> getLogList() {
		return MongoAccess.MONGO.findAll(Log.class);
	}

	public List<Log> getLogByCatalogId(String _catalogId) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("catalogId").is(_catalogId)),
				Log.class);
	}

	public void removeLog(String _logId) {
		MongoAccess.MONGO.remove(new Query(Criteria.where("id").is(_logId)),
				Log.class);
	}

	public long countLogNumber() {
		return MongoAccess.MONGO.count(null, Log.class);
	}

	public List<Log> findAllLogs() {
		return MongoAccess.MONGO.find(new Query(Criteria.where("type")
				.is("LOG")), Log.class);
	}

	/**
	 * Data for duration Index
	 */
	public CaseDurationIndex getIDIByDuration(int _duration) {
		return MongoAccess.MONGO.findOne(new Query(Criteria.where("duration")
				.is(_duration)), CaseDurationIndex.class);
	}

	public String addIDIndex(CaseDurationIndex nInstanceDurationIndex) {
		MongoAccess.MONGO.save(nInstanceDurationIndex);
		return nInstanceDurationIndex.getId();
	}

	public List<CaseDurationIndex> getIDIGreatThan(int _down) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("duration").gte(_down)),
				CaseDurationIndex.class);
	}

	public List<CaseDurationIndex> getIDILessThan(int _up) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("duration").lte(_up)),
				CaseDurationIndex.class);
	}

	public List<CaseDurationIndex> getIDIBewteen(int _down, int _up) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("duration").lte(_up)
						.andOperator(Criteria.where("duration").gte(_down))),
				CaseDurationIndex.class);
	}

	public void removeIDIndex(int _duration) {
		MongoAccess.MONGO.remove(
				new Query(Criteria.where("duration").is(_duration)),
				CaseDurationIndex.class);
	}

	/**
	 * Data for length Index
	 */
	public CaseLengthIndex getILIByLength(int _length) {
		return MongoAccess.MONGO.findOne(
				new Query(Criteria.where("length").is(_length)),
				CaseLengthIndex.class);
	}

	public void removeILIndex(int _length) {
		MongoAccess.MONGO.remove(
				new Query(Criteria.where("length").is(_length)),
				CaseLengthIndex.class);
	}

	public String addILIndex(CaseLengthIndex nInstanceLengthIndex) {
		MongoAccess.MONGO.save(nInstanceLengthIndex);
		return nInstanceLengthIndex.getId();
	}

	public List<CaseLengthIndex> getILIGreatThan(int _down) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("length").gte(_down)),
				CaseLengthIndex.class);
	}

	public List<CaseLengthIndex> getILILessThan(int _up) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("length").lte(_up)),
				CaseLengthIndex.class);
	}

	public List<CaseLengthIndex> getILIBewteen(int _down, int _up) {
		return MongoAccess.MONGO.find(
				new Query(Criteria.where("length").lte(_up)
						.andOperator(Criteria.where("length").gte(_down))),
				CaseLengthIndex.class);
	}

	/**
	 * File
	 */

	public boolean saveFile(String _filePath, String _filename,
			String _contentType) throws NoExistException {
		InputStream input = null;
		try {
			if (!FileUtil.exists(_filePath)) {
				return false;
			}
			input = new FileInputStream(_filePath);
			MongoAccess.GRIDFS.store(input, _filename, _contentType);
		} catch (FileNotFoundException e) {
			throw new NoExistException("文件不存在");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new NoExistException("文件流关闭失败");
				}
			}
			File file = new File(_filePath);
			if (file.exists() && file.isFile()) {
				file.delete();
			}
		}
		return true;
	}

	public boolean saveFile(String _filePath, String _filename,
			String _contentType, Map<String, String> _metaData)
			throws NoExistException {
		DBObject metaData = new BasicDBObject();
		for (Map.Entry<String, String> entry : _metaData.entrySet()) {
			metaData.put(entry.getKey(), entry.getValue());
		}

		InputStream input = null;
		try {
			input = new FileInputStream(_filePath);
			MongoAccess.GRIDFS.store(input, _filename, _contentType, metaData);
		} catch (FileNotFoundException e) {
			throw new NoExistException("文件不存在");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					throw new NoExistException("文件流关闭失败");
				}
			}
		}
		return true;
	}

	public GridFSDBFile getFileByFilename(String _filename) {
		return MongoAccess.GRIDFS.findOne(new Query(Criteria.where("filename")
				.is(_filename)));
	}

	public void removeFileByFilename(String _filename) {
		MongoAccess.GRIDFS.delete(new Query(Criteria.where("filename").is(
				_filename)));
	}

	public void getFileByFilename(String _filename, String _targetPath)
			throws NoExistException {
		GridFSDBFile file = getFileByFilename(_filename);

		try {
			if (file != null) {
				file.writeTo(_targetPath);
			} else {
				throw new NoExistException("文件不存在");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public InputStream getFileInputStreamByFilename(String _filename)
			throws NoExistException {
		GridFSDBFile file = getFileByFilename(_filename);
		if (file != null) {
			return file.getInputStream();
		} else {
			throw new NoExistException("文件不存在");
		}
	}
}
