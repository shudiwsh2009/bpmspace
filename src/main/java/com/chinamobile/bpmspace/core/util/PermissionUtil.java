package com.chinamobile.bpmspace.core.util;

import com.chinamobile.bpmspace.core.data.MongoAccess;
import com.chinamobile.bpmspace.core.domain.Permission;
import com.chinamobile.bpmspace.core.domain.User;
import com.chinamobile.bpmspace.core.domain.log.LogCatalog;
import com.chinamobile.bpmspace.core.domain.process.Process;
import com.chinamobile.bpmspace.core.domain.process.ProcessCatalog;

public class PermissionUtil {
	public static MongoAccess MONGO = new MongoAccess();

	public static boolean hasRWPermission(ProcessCatalog _catalog, User _user) {
		if (_catalog.getPermission(_user.getId()) == Permission.RW) {
			return true;
		}
		ProcessCatalog parentCatalog = _catalog;
		while (!parentCatalog.getParentId().equals("")) {
			parentCatalog = MONGO.getProcessCatalogById(_catalog.getParentId());
			if (parentCatalog.getPermission(_user.getId()) == Permission.RW) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasRWPermission(LogCatalog _catalog, User _user) {
		if (_catalog.getPermission(_user.getId()) == Permission.RW) {
			return true;
		}
		LogCatalog parentCatalog = _catalog;
		while (!parentCatalog.getParentId().equals("")) {
			parentCatalog = MONGO.getLogCatalogById(_catalog.getParentId());
			if (parentCatalog.getPermission(_user.getId()) == Permission.RW) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasRWPermission(Process _process, User _user) {
		if (_process.getPermission(_user.getId()) == Permission.RW) {
			return true;
		}
		ProcessCatalog catalog = MONGO.getProcessCatalogById(_process
				.getCatalogId());
		return hasRWPermission(catalog, _user);
	}
}
