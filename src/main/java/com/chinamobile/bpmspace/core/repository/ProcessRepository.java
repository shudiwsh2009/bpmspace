package com.chinamobile.bpmspace.core.repository;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.processmining.exporting.DotPngExport;
import org.processmining.framework.models.petrinet.PetriNet;
import org.processmining.framework.models.petrinet.algorithms.PnmlWriter;
import org.processmining.framework.plugin.ProvidedObject;
import org.processmining.importing.epml.EpmlImport;
import org.processmining.importing.pnml.PnmlImport;
import org.processmining.mining.epcmining.EPCResult;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.Permission;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.domain.process.ProcessRevision;
import com.chinamobile.bpmspace.core.domain.process.ProcessType;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.BasicException;
import com.chinamobile.bpmspace.core.exception.DuplicateFieldException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.FormatException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.DateUtil;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.PermissionUtil;
import com.chinamobile.bpmspace.core.util.PetriNetUtil;
import com.chinamobile.bpmspace.core.util.TimeUtil;

public class ProcessRepository {
	private MongoAccess mongo = new MongoAccess();

	public boolean checkProcess(String _name, String _description,
			String _catalogId, ProcessType _type, String _ownerId)
			throws EmptyFieldException, DuplicateFieldException,
			NoExistException, ActionRejectException {
		// Empty field validity
		if (_name == null || _name.equals("")) {
			throw new EmptyFieldException("流程名为空");
		} else if (_description == null) {
			throw new EmptyFieldException("描述为空");
		} else if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_type == null) {
			throw new EmptyFieldException("类型为空");
		} else if (_ownerId == null || _ownerId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		ProcessCatalog catalog = mongo.getProcessCatalogById(_catalogId);
		User user = mongo.getUserById(_ownerId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (mongo.hasProcessInCatalog(_name, _catalogId, _type) != null) {
			throw new DuplicateFieldException("该目录下已有同名流程");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
			throw new ActionRejectException("用户无权限在该目录下创建流程");
		}
		return true;
	}

	public String[] addProcess(String _name, String _description,
			String _catalogId, ProcessType _type, String _ownerId,
			String _jsonPath, String _svgPath, String _xmlPath)
			throws BasicException {
		// Empty field validity
		if (_name == null || _name.equals("")) {
			throw new EmptyFieldException("流程名为空");
		} else if (_description == null) {
			throw new EmptyFieldException("描述为空");
		} else if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_type == null) {
			throw new EmptyFieldException("类型为空");
		} else if (_ownerId == null || _ownerId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		ProcessCatalog catalog = mongo.getProcessCatalogById(_catalogId);
		User user = mongo.getUserById(_ownerId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (mongo.hasProcessInCatalog(_name, _catalogId, _type) != null) {
			throw new DuplicateFieldException("该目录下已有同名流程");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
			throw new ActionRejectException("用户无权限在该目录下创建流程");
		}
		Process newProcess = mongo.addProcess(_name, _description, _catalogId,
				_type, catalog.getRootUserId(), _ownerId, user.getUsername());
		String jsonFileName = FileUtil.nameGridFSFile(user.getId(),
				newProcess.getId(), 1L)
				+ FileUtil.JSON_SUFFIX;
		String svgFileName = FileUtil.nameGridFSFile(user.getId(),
				newProcess.getId(), 1L)
				+ FileUtil.SVG_SUFFIX;
		String xmlFileName = FileUtil.nameGridFSFile(user.getId(),
				newProcess.getId(), 1L);
		double jsonFileLength = FileUtil.getFileLength(_jsonPath);
		boolean jsonFileSave = mongo.saveFile(_jsonPath, jsonFileName,
				FileUtil.CONTENT_TYPE_JSON);
		boolean svgFileSave = mongo.saveFile(_svgPath, svgFileName,
				FileUtil.CONTENT_TYPE_SVG);
		String newXmlFilePath = "";
		boolean xmlFileSave = false;
		switch (_type) {
		case BPMN:
			xmlFileName += FileUtil.BPMN_SUFFIX;
			newXmlFilePath = _xmlPath;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_BPMN);
			break;
		case EPC:
			xmlFileName += FileUtil.EPML_SUFFIX;
			newXmlFilePath = _xmlPath;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_EPC);
			break;
		case PETRINET:
			xmlFileName += FileUtil.PNML_SUFFIX;
			newXmlFilePath = FileUtil.WEBAPP_ROOT + FileUtil.XML_PREFIX
					+ xmlFileName;
			try {
				FileInputStream fInput = new FileInputStream(new File(_xmlPath));
				PnmlImport pnmlImport = new PnmlImport();
				PetriNet pn = pnmlImport.read(fInput);
				PetriNetUtil.makeVisible(pn);
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						newXmlFilePath));
				PnmlWriter.write(false, true, pn, bw);
				bw.close();
			} catch (Exception e) {
				throw new BasicException("未知错误");
			}
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_PNML);
			break;
		default:
			xmlFileName += FileUtil.XML_SUFFIX;
			newXmlFilePath = _xmlPath;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_XML);
			break;
		}
		File file = new File(_xmlPath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		file = new File(newXmlFilePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		Model newModel = mongo.addModel(newProcess.getId(), _ownerId,
				user.getUsername(), 1L, jsonFileSave ? jsonFileName : "",
				svgFileSave ? svgFileName : "", xmlFileSave ? xmlFileName : "",
				jsonFileLength);
		newProcess.getRevision().put(
				newModel.getRevision(),
				new ProcessRevision(newModel.getId(), newModel.getCreatorId(),
						newModel.getCreateTime()));
		String svgSrc = FileUtil.SVG_PREFIX
				+ FileUtil.nameGridFSFile(newModel.getCreatorId(),
						newModel.getProcessId(), newModel.getRevision())
				+ FileUtil.SVG_SUFFIX;
		String svgPath = FileUtil.WEBAPP_ROOT + svgSrc;
		if (!newModel.getSvgFilename().equals("")) {
			mongo.getFileByFilename(newModel.getSvgFilename(), svgPath);
		}
		mongo.saveProcess(newProcess);

		/**
		 * chenhz, for insert index
		 */
		IndexRepository indexRepository = new IndexRepository();
		// delete old index
		if (newProcess.getRevision().size() > 1) {
			List<Model> modelList = mongo
					.getModelListSoretdDescByDate(newProcess.getId());
			try {
				indexRepository.removeFromModelIndex(modelList.get(1),
						newProcess.getType());
			} catch (BasicException be) {
				be.printStackTrace();
			}
		}
		// insert new index
		try {
			indexRepository.addToModelIndex(newModel, newProcess.getType());
		} catch (BasicException be) {
			be.printStackTrace();
		}

		String[] result = new String[8];
		result[0] = newProcess.getId(); // id of the process
		result[1] = svgSrc; // path of the svg file
		result[2] = newProcess.getName(); // name
		result[3] = Process.convertType(newProcess.getType());
		result[4] = newProcess.getOwnerName() + " 于 "
				+ DateUtil.convertDate(newProcess.getCreateTime());
		result[5] = newModel.getCreatorName() + " 于 "
				+ DateUtil.convertDate(newModel.getCreateTime());
		BigDecimal sizeBD = new BigDecimal(jsonFileLength / 1024);
		sizeBD = sizeBD.setScale(1, BigDecimal.ROUND_HALF_UP);
		result[6] = sizeBD.toString() + " KB"; // size
		result[7] = newModel.getRevision() + "";
		return result;
	}

	public String[] checkEditProcess(String _processId, String _ownerId)
			throws EmptyFieldException, DuplicateFieldException,
			NoExistException, ActionRejectException {
		// Empty field validity
		if (_processId == null || _processId.equals("")) {
			throw new EmptyFieldException("流程不存在");
		} else if (_ownerId == null || _ownerId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		Process process = mongo.getProcessById(_processId);
		User user = mongo.getUserById(_ownerId);
		if (process == null) {
			throw new NoExistException("流程不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (PermissionUtil.hasRWPermission(process, user) == false) {
			throw new ActionRejectException("用户无权限编辑该流程");
		}
		ModelRepository mr = new ModelRepository();
		String modelId = process.getRevision()
				.get((long) process.getRevision().size()).getModelId();
		Model model = mr.getModel(modelId);
		String jsonSrc = FileUtil.XML_PREFIX
				+ FileUtil.nameGridFSFile(model.getId(), model.getProcessId(),
						model.getRevision()) + FileUtil.XML_SUFFIX;
		String jsonPath = FileUtil.WEBAPP_ROOT + jsonSrc;
		if (!model.getJsonFilename().equals("")) {
			mongo.getFileByFilename(model.getJsonFilename(), jsonPath);
		}

		String[] result = new String[2];
		// jsonSrc
		result[0] = jsonSrc;
		result[1] = Process.convertType(process.getType());
		return result;
	}

	public String[] editProcess(String _processId, String _ownerId,
			String _jsonPath, String _svgPath, String _xmlPath)
			throws BasicException {
		// Empty field validity
		if (_processId == null || _processId.equals("")) {
			throw new EmptyFieldException("流程不存在");
		} else if (_ownerId == null || _ownerId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		Process process = mongo.getProcessById(_processId);
		User user = mongo.getUserById(_ownerId);
		if (process == null) {
			throw new NoExistException("流程不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (PermissionUtil.hasRWPermission(process, user) == false) {
			throw new ActionRejectException("用户无权限编辑该模型");
		}
		String jsonFileName = FileUtil.nameGridFSFile(user.getId(),
				process.getId(), process.getRevision().size() + 1)
				+ FileUtil.JSON_SUFFIX;
		String svgFileName = FileUtil.nameGridFSFile(user.getId(),
				process.getId(), process.getRevision().size() + 1)
				+ FileUtil.SVG_SUFFIX;
		String xmlFileName = FileUtil.nameGridFSFile(user.getId(),
				process.getId(), process.getRevision().size() + 1);
		double jsonFileLength = FileUtil.getFileLength(_jsonPath);
		boolean jsonFileSave = mongo.saveFile(_jsonPath, jsonFileName,
				FileUtil.CONTENT_TYPE_JSON);
		boolean svgFileSave = mongo.saveFile(_svgPath, svgFileName,
				FileUtil.CONTENT_TYPE_SVG);
		String newXmlFilePath = "";
		boolean xmlFileSave = false;
		switch (process.getType()) {
		case BPMN:
			xmlFileName += FileUtil.BPMN_SUFFIX;
			newXmlFilePath = _xmlPath;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_BPMN);
			break;
		case EPC:
			xmlFileName += FileUtil.EPML_SUFFIX;
			newXmlFilePath = _xmlPath;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_EPC);
			break;
		case PETRINET:
			xmlFileName += FileUtil.PNML_SUFFIX;
			newXmlFilePath = FileUtil.WEBAPP_ROOT + FileUtil.XML_PREFIX
					+ xmlFileName;
			try {
				FileInputStream fInput = new FileInputStream(new File(_xmlPath));
				PnmlImport pnmlImport = new PnmlImport();
				PetriNet pn = pnmlImport.read(fInput);
				PetriNetUtil.makeVisible(pn);
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						newXmlFilePath));
				PnmlWriter.write(false, true, pn, bw);
				bw.close();
			} catch (Exception e) {
				throw new BasicException("未知错误");
			}
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_PNML);
			break;
		default:
			xmlFileName += FileUtil.XML_SUFFIX;
			newXmlFilePath = _xmlPath;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_XML);
			break;
		}
		File file = new File(_xmlPath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		file = new File(newXmlFilePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		Model newModel = mongo.addModel(process.getId(), _ownerId, user
				.getUsername(), process.getRevision().size() + 1,
				jsonFileSave ? jsonFileName : "", svgFileSave ? svgFileName
						: "", xmlFileSave ? xmlFileName : "", jsonFileLength);
		process.getRevision().put(
				newModel.getRevision(),
				new ProcessRevision(newModel.getId(), newModel.getCreatorId(),
						newModel.getCreateTime()));
		String svgSrc = FileUtil.SVG_PREFIX
				+ FileUtil.nameGridFSFile(newModel.getCreatorId(),
						newModel.getProcessId(), newModel.getRevision())
				+ FileUtil.SVG_SUFFIX;
		String svgPath = FileUtil.WEBAPP_ROOT + svgSrc;
		if (!newModel.getSvgFilename().equals("")) {
			mongo.getFileByFilename(newModel.getSvgFilename(), svgPath);
		}
		mongo.saveProcess(process);

		/**
		 * chenhz, for insert index
		 * 
		 * IndexRepository indexRepository = new IndexRepository(); //delete old
		 * index if(newProcess.getRevision().size() > 1){ List<Model> modelList=
		 * mongo.getModelListSoretdDescByDate(newProcess.getId()); try{
		 * indexRepository
		 * .removeFromModelIndex(modelList.get(1),newProcess.getType());
		 * }catch(BasicException be){ be.printStackTrace(); } } //insert new
		 * index try{
		 * indexRepository.addToModelIndex(newModel,newProcess.getType());
		 * }catch(BasicException be){ be.printStackTrace(); }
		 */

		String[] result = new String[8];
		result[0] = process.getId(); // id of the process
		result[1] = svgSrc; // path of the svg file
		result[2] = process.getName(); // name
		result[3] = Process.convertType(process.getType());
		result[4] = process.getOwnerName() + " 于 "
				+ DateUtil.convertDate(process.getCreateTime());
		result[5] = newModel.getCreatorName() + " 于 "
				+ DateUtil.convertDate(newModel.getCreateTime());
		BigDecimal sizeBD = new BigDecimal(jsonFileLength / 1024);
		sizeBD = sizeBD.setScale(1, BigDecimal.ROUND_HALF_UP);
		result[6] = sizeBD.toString() + " KB"; // size
		result[7] = newModel.getRevision() + "";
		return result;
	}

	public void renameProcess(String _processId, String _newName, String _userId)
			throws EmptyFieldException, NoExistException,
			DuplicateFieldException, ActionRejectException {
		// Empty field validity
		if (_processId == null || _processId.equals("")) {
			throw new EmptyFieldException("流程不存在");
		} else if (_newName == null || _newName.equals("")) {
			throw new EmptyFieldException("流程名不能为空");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		Process process = mongo.getProcessById(_processId);
		User user = mongo.getUserById(_userId);
		if (process == null) {
			throw new NoExistException("流程不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(process
				.getCatalogId());
		if (catalog == null) {
			throw new NoExistException("找不到目录");
		}
		// Logical validity
		if (mongo.hasProcessInCatalog(_newName, catalog.getId(),
				process.getType()) != null) {
			throw new DuplicateFieldException("该目录下已有同名同类型流程");
		} else if (PermissionUtil.hasRWPermission(process, user) == false) {
			throw new ActionRejectException("用户无权限修改该流程名");
		}
		// rename
		process.setName(_newName);
		mongo.saveProcess(process);
	}

	public String[] removeProcesses(String _processIds, String _userId)
			throws EmptyFieldException, NoExistException,
			ActionRejectException, BasicException {
		// Empty field validity
		if (_processIds == null || _processIds.equals("")) {
			throw new EmptyFieldException("未选中模型");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		List<Process> processList = new ArrayList<Process>();
		String[] processIds = _processIds.split(":");
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null) {
				continue;
			}
			processList.add(p);
		}
		User user = mongo.getUserById(_userId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		StringBuilder b1 = new StringBuilder();
		StringBuilder b2 = new StringBuilder();
		/**
		 * chenhz, for insert index
		 */
		IndexRepository indexRepository = new IndexRepository();
		// remove
		for (Process p : processList) {
			if (PermissionUtil.hasRWPermission(p, user) == false) {
				// 无权限，加入返回信息
				b1.append(p.getId());
				b1.append(":");
				b2.append(p.getName());
				b2.append(FileUtil.SPLITTER);
				continue;
			}
			// index: only clear the newest model
			if (!p.getRevision().isEmpty()) {
				Model newestModel = mongo.getModelListSoretdDescByDate(
						p.getId()).get(0);
				indexRepository.removeFromModelIndex(newestModel, p.getType());
			}

			for (ProcessRevision revision : p.getRevision().values()) {
				Model model = mongo.getModelById(revision.getModelId());
				if (model != null) {
					mongo.removeFileByFilename(model.getJsonFilename());
					mongo.removeFileByFilename(model.getSvgFilename());
					mongo.removeFileByFilename(model.getXmlFilename());
				}
				mongo.removeModel(revision.getModelId());
			}
			mongo.removeProcess(p.getId());
		}

		String[] result = new String[2];
		result[0] = b1.toString();
		result[1] = b2.toString();
		return result;
	}

	public String[] moveProcesses(String _processIds, String _targetCatalogId,
			String _operator, String _userId) throws EmptyFieldException,
			NoExistException, ActionRejectException, DuplicateFieldException {
		// Empty field validity
		if (_processIds == null || _processIds.equals("")) {
			throw new EmptyFieldException("未选中模型");
		} else if (_targetCatalogId == null || _targetCatalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		List<Process> processList = new ArrayList<Process>();
		String[] processIds = _processIds.split(":");
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null) {
				continue;
			}
			processList.add(p);
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(_targetCatalogId);
		User user = mongo.getUserById(_userId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (catalog.getType().equals(CatalogType.SHARE)) {
			throw new ActionRejectException("不允许移动模型到共享库中");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
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
		// move
		for (Process p : processList) {
			Process ori = null;
			if (PermissionUtil.hasRWPermission(p, user) == false) {
				b1.append(p.getId());
				b1.append(":");
				b2.append(p.getName());
				b2.append(FileUtil.SPLITTER);
				continue;
			} else if ((ori = mongo.hasProcessInCatalog(p.getName(),
					catalog.getId(), p.getType())) != null) {
				if (!_operator.equals("FORCE")) {
					b3.append(p.getId());
					b3.append(":");
					b4.append(p.getName());
					b4.append(FileUtil.SPLITTER);
					continue;
				} else {
					// 强制覆盖
					for (ProcessRevision revision : ori.getRevision().values()) {
						Model model = mongo.getModelById(revision.getModelId());
						if (model != null) {
							mongo.removeFileByFilename(model.getJsonFilename());
							mongo.removeFileByFilename(model.getSvgFilename());
							mongo.removeFileByFilename(model.getXmlFilename());
						}
						mongo.removeModel(revision.getModelId());
					}
					mongo.removeProcess(ori.getId());
				}
			}
			p.setCatalogId(catalog.getId());
			p.setRootUserId(catalog.getRootUserId());
			mongo.saveProcess(p);
		}
		result[0] = b1.toString();
		result[1] = b2.toString();
		result[2] = b3.toString();
		result[3] = b4.toString();
		return result;
	}

	public String[] cloneProcesses(String _processIds, String _targetCatalogId,
			String _operator, String _userId) throws EmptyFieldException,
			NoExistException, ActionRejectException, DuplicateFieldException {
		// Empty field validity
		if (_processIds == null || _processIds.equals("")) {
			throw new EmptyFieldException("未选中模型");
		} else if (_targetCatalogId == null || _targetCatalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		List<Process> processList = new ArrayList<Process>();
		String[] processIds = _processIds.split(":");
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null) {
				continue;
			}
			processList.add(p);
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(_targetCatalogId);
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
		for (Process p : processList) {
			Process ori = null;
			if (PermissionUtil.hasRWPermission(p, user) == false) {
				b1.append(p.getId());
				b1.append(":");
				b2.append(p.getName());
				b2.append(FileUtil.SPLITTER);
				continue;
			} else if ((ori = mongo.hasProcessInCatalog(p.getName(),
					catalog.getId(), p.getType())) != null) {
				if (!_operator.equals("FORCE")) {
					b3.append(p.getId());
					b3.append(":");
					b4.append(p.getName());
					b4.append(FileUtil.SPLITTER);
					continue;
				} else {
					// 强制覆盖
					for (ProcessRevision revision : ori.getRevision().values()) {
						Model model = mongo.getModelById(revision.getModelId());
						if (model != null) {
							mongo.removeFileByFilename(model.getJsonFilename());
							mongo.removeFileByFilename(model.getSvgFilename());
							mongo.removeFileByFilename(model.getXmlFilename());
						}
						mongo.removeModel(revision.getModelId());
					}
					mongo.removeProcess(ori.getId());
				}
			}
			if (p.getRevision().size() == 0) {
				continue;
			}
			// clone original process
			Process newP = p.clone();
			if (newP == null) {
				continue;
			}
			newP.setCatalogId(catalog.getId());
			newP.setRootUserId(catalog.getRootUserId());
			newP.setOwnerId(user.getId());
			newP.setOwnerName(user.getUsername());
			newP.setCreateTime(new Date());
			newP.getRevision().clear();
			newP.getUserPermission().clear();
			newP.getUserPermission().put(newP.getRootUserId(), Permission.RW);
			if (!newP.getOwnerId().equals(newP.getRootUserId())) {
				newP.getUserPermission().put(newP.getOwnerId(), Permission.RW);
			}

			// save the latest revision of model
			String modelId = p.getRevision().get((long) p.getRevision().size())
					.getModelId();
			Model m = mongo.getModelById(modelId);
			Model newM = m.clone();
			if (newM == null) {
				continue;
			}
			mongo.saveProcess(newP);
			String jsonFileName = FileUtil.nameGridFSFile(user.getId(),
					newP.getId(), 1L)
					+ FileUtil.JSON_SUFFIX;
			String svgFileName = FileUtil.nameGridFSFile(user.getId(),
					newP.getId(), 1L)
					+ FileUtil.SVG_SUFFIX;
			String xmlFileName = FileUtil.nameGridFSFile(user.getId(),
					newP.getId(), 1L);
			mongo.getFileByFilename(m.getJsonFilename(), FileUtil.WEBAPP_ROOT
					+ FileUtil.XML_PREFIX + jsonFileName);
			mongo.getFileByFilename(m.getSvgFilename(), FileUtil.WEBAPP_ROOT
					+ FileUtil.XML_PREFIX + svgFileName);
			mongo.getFileByFilename(m.getXmlFilename(), FileUtil.WEBAPP_ROOT
					+ FileUtil.XML_PREFIX + xmlFileName + FileUtil.XML_SUFFIX);
			boolean jsonFileSave = mongo.saveFile(FileUtil.WEBAPP_ROOT
					+ FileUtil.XML_PREFIX + jsonFileName, jsonFileName,
					FileUtil.CONTENT_TYPE_JSON);
			boolean svgFileSave = mongo.saveFile(FileUtil.WEBAPP_ROOT
					+ FileUtil.XML_PREFIX + svgFileName, svgFileName,
					FileUtil.CONTENT_TYPE_SVG);
			boolean xmlFileSave = false;
			switch (newP.getType()) {
			case BPMN:
				xmlFileSave = mongo.saveFile(FileUtil.WEBAPP_ROOT
						+ FileUtil.XML_PREFIX + xmlFileName
						+ FileUtil.XML_SUFFIX, xmlFileName
						+ FileUtil.BPMN_SUFFIX, FileUtil.CONTENT_TYPE_BPMN);
				xmlFileName += FileUtil.BPMN_SUFFIX;
				break;
			case EPC:
				xmlFileSave = mongo.saveFile(FileUtil.WEBAPP_ROOT
						+ FileUtil.XML_PREFIX + xmlFileName
						+ FileUtil.XML_SUFFIX, xmlFileName
						+ FileUtil.EPML_SUFFIX, FileUtil.CONTENT_TYPE_EPC);
				xmlFileName += FileUtil.EPML_SUFFIX;
				break;
			case PETRINET:
				xmlFileSave = mongo.saveFile(FileUtil.WEBAPP_ROOT
						+ FileUtil.XML_PREFIX + xmlFileName
						+ FileUtil.XML_SUFFIX, xmlFileName
						+ FileUtil.PNML_SUFFIX, FileUtil.CONTENT_TYPE_PNML);
				xmlFileName += FileUtil.PNML_SUFFIX;
				break;
			default:
				xmlFileSave = mongo.saveFile(FileUtil.WEBAPP_ROOT
						+ FileUtil.XML_PREFIX + xmlFileName
						+ FileUtil.XML_SUFFIX, xmlFileName
						+ FileUtil.XML_SUFFIX, FileUtil.CONTENT_TYPE_XML);
				xmlFileName += FileUtil.XML_SUFFIX;
				break;
			}
			newM.setJsonFilename(jsonFileSave ? jsonFileName : "");
			newM.setSvgFilename(svgFileSave ? svgFileName : "");
			newM.setXmlFilename(xmlFileSave ? xmlFileName : "");
			newM.setProcessId(newP.getId());
			newM.setCreatorId(user.getId());
			newM.setCreatorName(user.getUsername());
			newM.setCreateTime(new Date());
			newM.setRevision(1L);
			mongo.saveModel(newM);

			// add process revision
			newP.getRevision().put(
					newM.getRevision(),
					new ProcessRevision(newM.getId(), newM.getCreatorId(), newM
							.getCreateTime()));
			mongo.saveProcess(newP);
		}
		result[0] = b1.toString();
		result[1] = b2.toString();
		result[2] = b3.toString();
		result[3] = b4.toString();
		return result;
	}

	public List<Process> getProcesses(String _catalogId, String _indexStart,
			String _pageSize) throws EmptyFieldException, NoExistException,
			FormatException {
		if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(_catalogId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		}
		int indexStart = -1, pageSize = 0;
		try {
			indexStart = Integer.parseInt(_indexStart);
			pageSize = Integer.parseInt(_pageSize);
		} catch (NumberFormatException e) {
			throw new FormatException("页码错误");
		}
		if (indexStart < 0 || pageSize <= 0) {
			throw new EmptyFieldException("页码错误");
		} else {
			List<Process> processes = mongo.getProcessesByCatalogId(_catalogId,
					indexStart, pageSize);
			ModelRepository mr = new ModelRepository();
			for (Process p : processes) {
				if (p.getRevision().size() == 0) {
					continue;
				}
				String modelId = p.getRevision()
						.get((long) p.getRevision().size()).getModelId();
				Model model = mr.getModel(modelId);
				String svgSrc = FileUtil.SVG_PREFIX
						+ FileUtil.nameGridFSFile(model.getCreatorId(),
								model.getProcessId(), model.getRevision())
						+ FileUtil.SVG_SUFFIX;
				String svgPath = FileUtil.WEBAPP_ROOT + svgSrc;
				if (!model.getSvgFilename().equals("")) {
					mongo.getFileByFilename(model.getSvgFilename(), svgPath);
				}
			}
			return processes;
		}
	}

	public long countProcesses(String _catalogId) throws EmptyFieldException,
			NoExistException, FormatException {
		if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(_catalogId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		}
		return mongo.countProcessesByCatalogId(_catalogId);
	}

	public String exportProcesses(String _processIds, String _userId)
			throws EmptyFieldException, NoExistException,
			ActionRejectException, IOException {
		// Empty field validity
		if (_processIds == null || _processIds.equals("")) {
			throw new EmptyFieldException("未选中模型");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		List<Process> processList = new ArrayList<Process>();
		String[] processIds = _processIds.split(":");
		for (String pId : processIds) {
			Process p = mongo.getProcessById(pId);
			if (p == null) {
				// throw new NoExistException("流程不存在");
				continue;
			}
			processList.add(p);
		}
		User user = mongo.getUserById(_userId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		for (Process p : processList) {
			String catalogId = p.getCatalogId();
			ProcessCatalog catalog = mongo.getProcessCatalogById(catalogId);
			if (catalog == null) {
				continue;
			}
			if (catalog.getType() == CatalogType.SHARE) {

			} else if (catalog.getType() == CatalogType.PUBLIC) {

			} else if (catalog.getType() == CatalogType.PRIVATE) {
				if (catalog.getPermission(user.getId()) == Permission.NO) {
					throw new ActionRejectException("用户无权限导出该流程");
				}
			}
		}
		// export
		List<String> localFilenames = new ArrayList<String>();
		String xmlServerFilepathPrefix = FileUtil.EXPORT_PREFIX + user.getId()
				+ "/";
		String xmlLocalFilepathPrefix = FileUtil.WEBAPP_ROOT
				+ FileUtil.EXPORT_PREFIX + user.getId() + File.separator;
		File xmlLocalFilepath = new File(xmlLocalFilepathPrefix);
		if (!xmlLocalFilepath.exists()) {
			xmlLocalFilepath.mkdirs();
		}
		for (Process p : processList) {
			if (p.getRevision().size() == 0) {
				continue;
			}
			String modelId = p.getRevision().get((long) p.getRevision().size())
					.getModelId();
			Model model = mongo.getModelById(modelId);
			if (model == null) {
				continue;
			}
			String xmlLocalFilename = p.getName() + "_" + p.getId();
			switch (p.getType()) {
			case BPMN:
				xmlLocalFilename += FileUtil.BPMN_SUFFIX;
				break;
			case EPC:
				xmlLocalFilename += FileUtil.EPML_SUFFIX;
				break;
			case PETRINET:
				xmlLocalFilename += FileUtil.PNML_SUFFIX;
				break;
			default:
				xmlLocalFilename += FileUtil.XML_SUFFIX;
				break;
			}
			if (!model.getXmlFilename().equals("")) {
				mongo.getFileByFilename(model.getXmlFilename(),
						xmlLocalFilepathPrefix + xmlLocalFilename);
			}
			localFilenames.add(xmlLocalFilename);
		}
		if (localFilenames.isEmpty()) {
			return "";
		} else if (localFilenames.size() == 1) {
			return xmlServerFilepathPrefix + localFilenames.get(0);
		} else {
			try {
				String zipFilename = user.getUsername() + "_"
						+ TimeUtil.getCurrentYMD() + FileUtil.ZIP_SUFFIX;
				File zipFile = new File(xmlLocalFilepathPrefix + zipFilename);
				ZipOutputStream zipOut = new ZipOutputStream(
						new FileOutputStream(zipFile));
				int temp = 0;
				for (String fname : localFilenames) {
					File localFile = new File(xmlLocalFilepathPrefix + fname);
					InputStream input = new FileInputStream(localFile);
					zipOut.putNextEntry(new ZipEntry(fname));
					while ((temp = input.read()) != -1) {
						zipOut.write(temp);
					}
					input.close();
				}
				zipOut.close();
				return xmlServerFilepathPrefix + zipFilename;
			} catch (FileNotFoundException e) {
				throw new NoExistException("本地文件找不到");
			} catch (IOException e) {
				throw new NoExistException("本地文件找不到");
			}
		}
	}

	public String[] importProcessPnml(String name, String description,
			String catalogId, ProcessType type, String ownerId,
			String importJsonFile, String importXmlFile) throws BasicException {
		// Empty field validity
		if (name == null || name.equals("")) {
			throw new EmptyFieldException("流程名为空");
		} else if (description == null) {
			throw new EmptyFieldException("描述为空");
		} else if (catalogId == null || catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (type == null) {
			throw new EmptyFieldException("类型为空");
		} else if (ownerId == null || ownerId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		// Database validity
		ProcessCatalog catalog = mongo.getProcessCatalogById(catalogId);
		User user = mongo.getUserById(ownerId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		// Logical validity
		if (type != ProcessType.PETRINET) {
			throw new ActionRejectException("暂时只支持PetriNet类型的模型导入");
		} else if (mongo.hasProcessInCatalog(name, catalogId, type) != null) {
			throw new DuplicateFieldException("该目录下已有同名流程");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
			throw new ActionRejectException("用户无权限在该目录下创建流程");
		}
		Process newProcess = mongo.addProcess(name, description, catalogId,
				type, catalog.getRootUserId(), ownerId, user.getUsername());
		String importSvgFile = this.processPnmlToPng(importXmlFile);
		String jsonFileName = FileUtil.nameGridFSFile(user.getId(),
				newProcess.getId(), 1L)
				+ FileUtil.JSON_SUFFIX;
		String svgFileName = FileUtil.nameGridFSFile(user.getId(),
				newProcess.getId(), 1L)
				+ FileUtil.PNG_SUFFIX;
		String xmlFileName = FileUtil.nameGridFSFile(user.getId(),
				newProcess.getId(), 1L);
		double jsonFileLength = FileUtil.getFileLength(importJsonFile);
		boolean jsonFileSave = mongo.saveFile(importJsonFile, jsonFileName,
				FileUtil.CONTENT_TYPE_JSON);
		boolean svgFileSave = mongo.saveFile(importSvgFile, svgFileName,
				FileUtil.CONTENT_TYPE_SVG);
		String newXmlFilePath = "";
		boolean xmlFileSave = false;
		switch (type) {
		case BPMN:
			xmlFileName += FileUtil.BPMN_SUFFIX;
			newXmlFilePath = importXmlFile;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_BPMN);
			break;
		case EPC:
			xmlFileName += FileUtil.EPML_SUFFIX;
			newXmlFilePath = importXmlFile;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_EPC);
			break;
		case PETRINET:
			xmlFileName += FileUtil.PNML_SUFFIX;
			newXmlFilePath = FileUtil.WEBAPP_ROOT + FileUtil.XML_PREFIX
					+ xmlFileName;
			try {
				FileInputStream fInput = new FileInputStream(new File(
						importXmlFile));
				PnmlImport pnmlImport = new PnmlImport();
				PetriNet pn = pnmlImport.read(fInput);
				PetriNetUtil.makeVisible(pn);
				BufferedWriter bw = new BufferedWriter(new FileWriter(
						newXmlFilePath));
				PnmlWriter.write(false, true, pn, bw);
				bw.close();
			} catch (Exception e) {
				throw new BasicException("未知错误");
			}
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_PNML);
			break;
		default:
			xmlFileName += FileUtil.XML_SUFFIX;
			newXmlFilePath = importXmlFile;
			xmlFileSave = mongo.saveFile(newXmlFilePath, xmlFileName,
					FileUtil.CONTENT_TYPE_XML);
			break;
		}
		File file = new File(importXmlFile);
		if (file.exists() && file.isFile()) {
			file.delete();
		}
		file = new File(newXmlFilePath);
		if (file.exists() && file.isFile()) {
			file.delete();
		}

		Model newModel = mongo.addModel(newProcess.getId(), ownerId,
				user.getUsername(), 1L, jsonFileSave ? jsonFileName : "",
				svgFileSave ? svgFileName : "", xmlFileSave ? xmlFileName : "",
				jsonFileLength);
		newProcess.getRevision().put(
				newModel.getRevision(),
				new ProcessRevision(newModel.getId(), newModel.getCreatorId(),
						newModel.getCreateTime()));
		String svgSrc = FileUtil.SVG_PREFIX
				+ FileUtil.nameGridFSFile(newModel.getCreatorId(),
						newModel.getProcessId(), newModel.getRevision())
				+ FileUtil.SVG_SUFFIX;
		String svgPath = FileUtil.WEBAPP_ROOT + svgSrc;
		if (!newModel.getSvgFilename().equals("")) {
			mongo.getFileByFilename(newModel.getSvgFilename(), svgPath);
		}
		mongo.saveProcess(newProcess);

		/**
		 * chenhz, for insert index
		 */
		IndexRepository indexRepository = new IndexRepository();
		// delete old index
		if (newProcess.getRevision().size() > 1) {
			List<Model> modelList = mongo
					.getModelListSoretdDescByDate(newProcess.getId());
			try {
				indexRepository.removeFromModelIndex(modelList.get(1),
						newProcess.getType());
			} catch (BasicException be) {
				be.printStackTrace();
			}
		}
		// insert new index
		try {
			indexRepository.addToModelIndex(newModel, newProcess.getType());
		} catch (BasicException be) {
			be.printStackTrace();
		}

		String[] result = new String[8];
		result[0] = newProcess.getId(); // id of the process
		result[1] = svgSrc; // path of the svg file
		result[2] = newProcess.getName(); // name
		result[3] = Process.convertType(newProcess.getType());
		result[4] = newProcess.getOwnerName() + " 于 "
				+ DateUtil.convertDate(newProcess.getCreateTime());
		result[5] = newModel.getCreatorName() + " 于 "
				+ DateUtil.convertDate(newModel.getCreateTime());
		BigDecimal sizeBD = new BigDecimal(jsonFileLength / 1024);
		sizeBD = sizeBD.setScale(1, BigDecimal.ROUND_HALF_UP);
		result[6] = sizeBD.toString() + " KB"; // size
		result[7] = newModel.getRevision() + "";
		return result;
	}

	public String processPnmlToPng(String pnmlFile) throws BasicException {
		// Empty field validity
		if (pnmlFile == null || pnmlFile.equals("")) {
			throw new EmptyFieldException("文件不存在");
		}
		File file = new File(pnmlFile);
		if (!file.exists()) {
			throw new NoExistException("文件不存在");
		}
		FileInputStream fInput = null;
		try {
			fInput = new FileInputStream(file);
		} catch (Exception e) {
			throw new BasicException("读取文件错误");
		}
		PnmlImport pnmlImport = new PnmlImport();
		PetriNet pn = null;
		try {
			pn = pnmlImport.read(fInput);
		} catch (Exception e) {
			throw new BasicException("无法处理非PetriNet模型");
		}
		if (pn == null) {
			throw new BasicException("无法处理非PetriNet模型");
		}
		String pngFile = FileUtil.PNG_PREFIX + pnmlFile + "_"
				+ System.nanoTime() + FileUtil.PNG_SUFFIX;
		String localPngFile = FileUtil.WEBAPP_ROOT + pngFile;
		ProvidedObject po = new ProvidedObject("petrinet", pn);
		DotPngExport dpe = new DotPngExport();
		try {
			OutputStream image = new FileOutputStream(localPngFile);
			dpe.export(po, image);
		} catch (Exception e) {
			throw new BasicException("无法转换成png格式图片");
		}
		return pngFile;
	}

	public String processEpmlToPng(String epmlFile) throws BasicException {
		// Empty field validity
		if (epmlFile == null || epmlFile.equals("")) {
			throw new EmptyFieldException("文件不存在");
		}
		File file = new File(epmlFile);
		if (!file.exists()) {
			throw new NoExistException("文件不存在");
		}
		FileInputStream fInput = null;
		try {
			fInput = new FileInputStream(file);
		} catch (Exception e) {
			throw new BasicException("读取文件错误");
		}

		EpmlImport epmlImport = new EpmlImport();
		EPCResult epcResult = null;
		try {
			epcResult = (EPCResult) epmlImport.importFile(fInput);
		} catch (FileNotFoundException e) {
			throw new NoExistException("文件不存在");
		} catch (IOException e) {
			throw new BasicException("无法处理非EPC模型");
		}
		if (epcResult == null) {
			throw new BasicException("无法处理非EPC模型");
		}
		String pngFile = FileUtil.PNG_PREFIX + file.getName() + "_"
				+ System.nanoTime() + FileUtil.PNG_SUFFIX;
		String localPngFile = FileUtil.WEBAPP_ROOT + pngFile;
		ProvidedObject po = new ProvidedObject("configurable epc",
				epcResult.getFirstConfigurableEPC());
		DotPngExport dpe = new DotPngExport();
		try {
			OutputStream image = new FileOutputStream(localPngFile);
			dpe.export(po, image);
		} catch (Exception e) {
			throw new BasicException("无法转换成png格式图片");
		}
		return pngFile;
	}

}
