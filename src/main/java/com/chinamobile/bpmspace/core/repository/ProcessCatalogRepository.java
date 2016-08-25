package com.chinamobile.bpmspace.core.repository;

import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.process.Model;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;
import com.chinamobile.bpmspace.core.domain.process.ProcessRevision;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.PermissionUtil;
import com.chinamobile.bpmspace.core.util.ServerInit;

public class ProcessCatalogRepository {
	private MongoAccess mongo = new MongoAccess();

	public String addProcessCatalog(String _name, String _parentId,
			String _ownerId) throws EmptyFieldException, NoExistException,
			ActionRejectException {
		if (_name == null || _name.equals("")) {
			throw new EmptyFieldException("目录名为空");
		} else if (_parentId == null || _parentId.equals("")) {
			throw new ActionRejectException("无法创建根目录");
		} else if (_ownerId == null || _ownerId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		User user = mongo.getUserById(_ownerId);
		ProcessCatalog catalog = mongo.getProcessCatalogById(_parentId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		} else if (catalog == null) {
			throw new NoExistException("父目录不存在");
		}
		if (catalog.getType() == CatalogType.SHARE) {
			throw new ActionRejectException("无法在共享库中创建目录");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
			throw new ActionRejectException("用户无权限在该目录中创建子目录");
		} else if (mongo.getProcessCatalogsByParentIdAndName(_parentId, _name)
				.size() != 0) {
			throw new ActionRejectException("该目录下已有相同名称目录");
		}
		ProcessCatalog newCatalog = mongo.addProcessCatalog(_name, _parentId,
				catalog.getType(), catalog.getRootUserId(), _ownerId,
				user.getUsername());
		return newCatalog.getId();
	}

	public void removeProcessCatalog(String _catalogId, String _userId)
			throws EmptyFieldException, NoExistException, ActionRejectException {
		if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(_catalogId);
		User user = mongo.getUserById(_userId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		} else if (user == null) {
			throw new NoExistException("用户不存在");
		}
		if (catalog.getParentId().equals("")) {
			throw new ActionRejectException("无法删除根目录");
		} else if (catalog.getType() == CatalogType.SHARE) {
			throw new ActionRejectException("无法删除共享目录");
		} else if (PermissionUtil.hasRWPermission(catalog, user) == false) {
			throw new ActionRejectException("用户无权限删除该目录");
		}
		removeProcessCatalogRecursively(catalog.getId());
	}

	private void removeProcessCatalogRecursively(String _catalogId) {
		List<ProcessCatalog> catalogs = mongo
				.getProcessCatalogsByParentId(_catalogId);
		List<Process> processes = mongo.getProcessesByCatalogId(_catalogId);
		// remove process
		for (Process p : processes) {
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
		// remove catalogs recursively
		for (ProcessCatalog c : catalogs) {
			removeProcessCatalogRecursively(c.getId());
		}
		mongo.removeProcessCatalog(_catalogId);
	}

	public List<ProcessCatalog> getRootProcessCatalogs(String _userId)
			throws EmptyFieldException, NoExistException {
		if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		User user = mongo.getUserById(_userId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		}
		List<ProcessCatalog> rootProcessCatalogs = mongo
				.getRootProcessCatalogs(_userId);
		ProcessCatalog publicRootProcessCatalog = mongo
				.getProcessCatalogById(FileUtil.PUBLIC_ROOT_PROCESS_CATALOG);
		if (publicRootProcessCatalog == null) {
			publicRootProcessCatalog = ServerInit
					.createPublicRootProcessCatalog();
		}
		rootProcessCatalogs.add(publicRootProcessCatalog);
		return rootProcessCatalogs;
	}

	public List<ProcessCatalog> getProcessCatalogs(String _parentCatalogId)
			throws EmptyFieldException, NoExistException {
		if (_parentCatalogId == null || _parentCatalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		}
		ProcessCatalog catalog = mongo.getProcessCatalogById(_parentCatalogId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		}
		List<ProcessCatalog> catalogs = mongo
				.getProcessCatalogsByParentId(_parentCatalogId);
		return catalogs;
	}
}
