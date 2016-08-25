package com.chinamobile.bpmspace.core.repository;

import java.util.List;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.CatalogType;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.log.Log;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.exception.ActionRejectException;
import com.chinamobile.bpmspace.core.exception.EmptyFieldException;
import com.chinamobile.bpmspace.core.exception.NoExistException;
import com.chinamobile.bpmspace.core.util.FileUtil;
import com.chinamobile.bpmspace.core.util.PermissionUtil;
import com.chinamobile.bpmspace.core.util.ServerInit;

public class InstanceCatalogRepository {
	private MongoAccess mongo = new MongoAccess();

	public String addInstanceCatalog(String _name, String _parentId,
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
		LogCatalog catalog = mongo.getLogCatalogById(_parentId);
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
		LogCatalog newCatalog = mongo.addLogCatalog(_name, _parentId,
				catalog.getType(), catalog.getRootUserId(), _ownerId,
				user.getUsername());
		return newCatalog.getId();
	}

	public void removeInstanceCatalog(String _catalogId, String _userId)
			throws EmptyFieldException, NoExistException, ActionRejectException {
		if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		} else if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		LogCatalog catalog = mongo.getLogCatalogById(_catalogId);
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
		removeInstanceCatalogRecursively(catalog.getId());
	}

	private void removeInstanceCatalogRecursively(String _catalogId) {
		List<LogCatalog> catalogs = mongo.getLogCatalogsByParentId(_catalogId);
		List<Log> logs = mongo.getLogByCatalogId(_catalogId);
		// remove instance
		for (Log p : logs) {
			for (String instance : p.getInstances()) {
				mongo.removeCase(instance);
			}
			mongo.removeLog(p.getId());
		}
		// remove catalogs recursively
		for (LogCatalog c : catalogs) {
			removeInstanceCatalogRecursively(c.getId());
		}
		mongo.removeLogCatalog(_catalogId);
	}

	public List<LogCatalog> getRootInstanceCatalogs(String _userId)
			throws EmptyFieldException, NoExistException {
		if (_userId == null || _userId.equals("")) {
			throw new EmptyFieldException("用户不存在");
		}
		User user = mongo.getUserById(_userId);
		if (user == null) {
			throw new NoExistException("用户不存在");
		}
		List<LogCatalog> rootInstanceCatalogs = mongo
				.getRootLogCatalogs(_userId);
		LogCatalog publicRootIntanceCatalog = mongo
				.getLogCatalogById(FileUtil.PUBLIC_ROOT_PROCESS_CATALOG);
		if (publicRootIntanceCatalog == null) {
			publicRootIntanceCatalog = ServerInit.createPublicRootLogCatalog();
		}
		rootInstanceCatalogs.add(publicRootIntanceCatalog);
		return rootInstanceCatalogs;
	}

	public List<LogCatalog> getInstanceCatalogs(String _parentCatalogId)
			throws EmptyFieldException, NoExistException {
		if (_parentCatalogId == null || _parentCatalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		}
		LogCatalog catalog = mongo.getLogCatalogById(_parentCatalogId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		}
		List<LogCatalog> catalogs = mongo
				.getLogCatalogsByParentId(_parentCatalogId);
		return catalogs;
	}

	public LogCatalog getInstanceCatalog(String _catalogId)
			throws EmptyFieldException, NoExistException {
		if (_catalogId == null || _catalogId.equals("")) {
			throw new EmptyFieldException("目录不存在");
		}
		LogCatalog catalog = mongo.getLogCatalogById(_catalogId);
		if (catalog == null) {
			throw new NoExistException("目录不存在");
		}
		return catalog;
	}
}
