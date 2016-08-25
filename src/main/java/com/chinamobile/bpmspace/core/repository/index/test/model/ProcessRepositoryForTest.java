package com.chinamobile.bpmspace.core.repository.index.test.model;

import java.math.BigDecimal;
import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
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
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.repository.IndexRepository;
import com.chinamobile.bpmspace.core.util.DateUtil;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.PermissionUtil;

public class ProcessRepositoryForTest {
	private MongoAccess mongo = new MongoAccess();

	public String[] addProcess(String _name, String _description,
			String _catalogId, ProcessType _type, String _ownerId,
			String _jsonPath, String _svgPath, String _xmlPath)
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
		Process newProcess = mongo.addProcess(_name, _description, _catalogId,
				_type, catalog.getRootUserId(), _ownerId, user.getUsername());
		String[] jsonContent = FileUtil.readModelFile(_jsonPath);
		String svgContent = FileUtil.readModelFile(_svgPath)[0];
		String xmlContent = FileUtil.readModelFile(_xmlPath)[0];
		// String xmlContent = _xmlPath;
		// String svgContent = _svgPath;
		double size = Double.parseDouble(jsonContent[1]);
		Model newModel = mongo.addModel(newProcess.getId(), _ownerId,
				user.getUsername(), 1L, jsonContent[0], svgContent, xmlContent,
				size);
		newProcess.getRevision().put(
				newModel.getRevision(),
				new ProcessRevision(newModel.getId(), newModel.getCreatorId(),
						newModel.getCreateTime()));
		String svgSrc = FileUtil.SVG_PREFIX + newModel.getCreatorId() + "_"
				+ newModel.getId() + "_" + newModel.getRevision()
				+ FileUtil.SVG_SUFFIX;
		// String svgPath = FileUtil.WEBAPP_ROOT + svgSrc;
		// FileUtil.writeModelFile(svgPath, newModel.getSvgStr());
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
		BigDecimal sizeBD = new BigDecimal(size / 1024);
		sizeBD = sizeBD.setScale(1, BigDecimal.ROUND_HALF_UP);
		result[6] = sizeBD.toString() + " KB"; // size
		result[7] = newModel.getRevision() + "";
		return result;
	}

}
